package com.hfut.service.client;

import com.hfut.bean.ChatRequest;
import com.hfut.bean.ChatResponse;
import com.hfut.bean.TempStorageTask;
import com.hfut.service.Listenable;
import com.hfut.utils.BytesUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

import org.omg.Messaging.SyncScopeHelper;

public class ChatClient implements Listenable {
    private Selector selector;
    private RequestBuilder requestBuilder = RequestBuilder.getInstance();
    public  Map<String, TempStorageTask> instances = new HashMap<>();
  

    public ChatClient() {
        System.out.println("客户端启动");
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SocketChannel getChannel(String name) throws Exception {
        if (instances.containsKey(name)) {
            throw new Exception("不存在该客户端");
        }
        return instances.get(name).getChannel();
    }


    public synchronized void addClient(String name) throws Exception {
        System.out.println("客户端添加，名字为：" + name);
        SocketChannel channel = SocketChannel.open();
        channel.socket().connect(new InetSocketAddress("localhost", 9999));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_WRITE|SelectionKey.OP_READ, name);
        checkUsername(name);
        instances.put(name, new TempStorageTask(channel));
        ChatRequest request = requestBuilder.buildConnectionRequest();
        request.setName(name);
        instances.get(name).offerRequest(request);
    }

    public void disconnect(String name) {
        System.out.println("客户端断开:" + name);
        ChatRequest request = requestBuilder.buildCloseRequest();
        request.setName(name);
        instances.get(name).offerRequest(request);
        instances.get(name).disconnected();
        System.out.println();
    }

    public ChatResponse get(String name) {
    	if(instances.get(name)!=null) {
    		return instances.get(name).pollResponse();
    	}
    	else {
    		return null;
    	}
    }

    public void sendMessage(String src, String content) {
        ChatRequest request = requestBuilder.buildChatRequest(src,content);
        request.setName(src);
        instances.get(src).offerRequest(requestBuilder.buildChatRequest(src,content));
    }

    public void sendMessage(String src, String toName, String content) {
        ChatRequest request = requestBuilder.buildChatRequest(toName, content);
        request.setName(src);
        instances.get(src).offerRequest(requestBuilder.buildChatRequest(src,toName, content));
    }

    @Override
    public void handle(SelectionKey key) {
        String name = (String) key.attachment();
        try {
            if (key.isReadable()) {
                handleRead(key);
            }
            if (key.isWritable()) {
                handleWrite(key);
            }
            /*清空已断开连接的客户端*/
            if (!instances.get(name).isConnected() && instances.get(name).isClear()) {
                instances.get(name).close();
            }
        } catch (Exception e) {
            try {
                instances.get(name).close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            instances.remove(name);
        }
    }

    private void checkUsername(String name) throws Exception {
        if (instances.containsKey(name)) {
            System.out.println("用户名重复");
            throw new Exception("用户名已存在，请重新输入用户名");
        }
    }

    private synchronized void handleWrite(SelectionKey key) throws Exception {
        String name = (String) key.attachment();
        SocketChannel channel = instances.get(name).getChannel();
        ChatRequest request = instances.get(name).pollRequest();
        if (request != null) {
            System.out.println("client handleWrite:" + request);
            ByteBuffer buffer = ByteBuffer.allocate(4048);
            buffer.put(BytesUtils.objectToBytes(request));
            buffer.flip();
            channel.write(buffer);
//            key.interestOps(SelectionKey.OP_READ);
        }
    }

    private synchronized void handleRead(SelectionKey key) throws Exception {
        String name = (String) key.attachment();
        SocketChannel channel = instances.get(name).getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(4048);
        channel.read(buffer);
        ChatResponse response = BytesUtils.bytesToObject(buffer.array(), ChatResponse.class);
              
        System.out.println("client handleRead:" + response);
        if (!response.getSucceed()) {
            throw new Exception(response.getErrorMsg());
        }
        instances.get(name).offerResponse(response);
    }

    @Override
    public Selector getSelector() {
        return selector;
    }

}