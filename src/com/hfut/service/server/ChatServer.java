package com.hfut.service.server;

import com.hfut.bean.ChatRequest;
import com.hfut.bean.ChatResponse;
import com.hfut.service.Listenable;
import com.hfut.utils.BytesUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

public class ChatServer implements Listenable {

    private static MessageHelper handler = new MessageHelper();
    private static Selector selector;
    private Map<String, SocketChannel> clientMap = new HashMap<>();

    public ChatServer(int port) {
        try {
            init(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init(int port) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(port));
        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器启动，端口为：" + port);
    }

    public void handle(SelectionKey key) {
        try {
            if (key.isAcceptable()) {
                handleConnection(key);
            }
            if (key.isValid() && key.isReadable()) {
                handleRead(key);
            }
            if (key.isValid() && key.isWritable()) {
                handlerWrite(key);
            }

        } catch (IOException ew) {
            ew.printStackTrace();
            System.out.println("handleDisconnection");
            key.cancel();
            try {
                ((SocketChannel) key.channel()).socket().close();
                key.channel().close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } catch (Exception e) {
            writeFailedResponse(key, e);
            e.printStackTrace();
        }
    }

    private void writeFailedResponse(SelectionKey key, Exception e) {
        System.out.println("writeFailedResponse");
        ChatRequest request = (ChatRequest) key.attachment();
        SocketChannel channel = (SocketChannel) key.channel();
        if (channel.isOpen()) {
            try {
                clientMap.remove(request.getName());
                ChatResponse response = handler.buildResponse(request, clientMap.keySet());
                byte[] bytes = BytesUtils.objectToBytes(response);
                ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
                for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
                    System.out.println("发送断开响应：" + entry.getKey());
                    System.out.println(response);
                    SocketChannel c = entry.getValue();
                    byteBuffer.clear();
                    byteBuffer.put(bytes);
                    byteBuffer.flip();
                    c.write(byteBuffer);
                }
                key.cancel();
                channel.socket().close();
                channel.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void handleConnection(SelectionKey key) throws Exception {
        System.out.println("handleConnection");
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel client = server.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }

    private void handlerWrite(SelectionKey key) throws Exception {
        ChatRequest request = (ChatRequest) key.attachment();
        int type = request.getHead().getType();
        if ((type & 1) != 0) {
            SocketChannel channel = clientMap.get(request.getToName());
            channel.write(
                    ByteBuffer.wrap(BytesUtils.objectToBytes(handler.buildResponse(request, clientMap.keySet()))));
        } else {
            ChatResponse response = handler.buildResponse(request, clientMap.keySet());
            System.out.println("handlerWrite:" + response);
            byte[] bytes = BytesUtils.objectToBytes(response);
            ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
            for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
                SocketChannel channel = entry.getValue();
                byteBuffer.clear();
                byteBuffer.put(bytes);
                byteBuffer.flip();
                channel.write(byteBuffer);
            }
        }
        key.interestOps(SelectionKey.OP_READ);
    }

    private void handleRead(SelectionKey key) throws Exception {
        SocketChannel channel = (SocketChannel) key.channel();
        ChatRequest request = handler.readObject(channel, ChatRequest.class);
        System.out.println("handleRead:" + request);
        key.attach(request);
        int type = request.getHead().getType();
        if ((type & 2) != 0) {
            clientMap.put(request.getName(), channel);
        } else if ((type & 4) != 0) {
            throw new Exception("客戶端断开连接");
        }
        if (key.isValid()) {
            key.interestOps(SelectionKey.OP_WRITE);
        }
    }

    @Override
    public Selector getSelector() {
        return selector;
    }

    public static void main(String[] args) {
        new ChatServer(9999).listen();

    }
}
