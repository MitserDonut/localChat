package com.hfut.bean;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TempStorageTask {
    /*1表示启动，2表示断开，此时不能接收任务*/
    private boolean connected;
    private SocketChannel channel;
    private BlockingQueue<ChatRequest> requestQueue = new LinkedBlockingQueue<>();
    private BlockingQueue<ChatResponse> responseQueue = new LinkedBlockingQueue<>();

    public boolean isConnected() {
        return connected;
    }

    public boolean isClear() {
        return requestQueue.isEmpty() && responseQueue.isEmpty();
    }

    public void disconnected() {
        connected = false;
    }

    public void close() throws IOException {
        channel.socket().close();
        channel.close();
    }

    public TempStorageTask(SocketChannel channel) {
        connected = true;
        this.channel = channel;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public void offerRequest(ChatRequest request) {
        if (connected) {
            requestQueue.offer(request);
        }
    }

    public void offerResponse(ChatResponse response) {
        responseQueue.offer(response);
    }

    public ChatResponse pollResponse() {
        try {
        	if(responseQueue.isEmpty()) {
        		return null;
        	}
        	else {

                return responseQueue.take();
        	}
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ChatRequest pollRequest() {
        try {
        	if(requestQueue.isEmpty()) {
        		return null;
        	}
        	else {

                return requestQueue.take();
        	}
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
