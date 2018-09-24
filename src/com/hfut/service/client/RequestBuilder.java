package com.hfut.service.client;

import com.hfut.bean.ChatRequest;
import com.hfut.bean.Header;

import java.util.Date;

public class RequestBuilder {
    private static final RequestBuilder INSTANCE = new RequestBuilder();
    public RequestBuilder() {
    }

    public static RequestBuilder getInstance() {
        return INSTANCE;
    }

    public ChatRequest buildConnectionRequest() {
        ChatRequest request = buildAndSetHeader();
        request.getHead().setType(2);
        return request;
    }

    public ChatRequest buildCloseRequest() {
        ChatRequest request = buildAndSetHeader();
        request.getHead().setType(4);
        return request;
    }

    /*有toName为私聊，没有为群聊*/
    public ChatRequest buildChatRequest(String username,String content) {
        ChatRequest request = buildAndSetHeader();
        request.getHead().setType(8);
        request.setContent(content);
        request.setName(username);
        return request;
    }

    public ChatRequest buildChatRequest(String username ,String toName, String content) {
        ChatRequest request = buildAndSetHeader();
        request.getHead().setType(9);
        request.setToName(toName);
        request.setContent(content);
        request.setName(username);
        return request;
    }

    private ChatRequest buildAndSetHeader() {
        ChatRequest request = new ChatRequest();
        Header header = new Header();
        header.setDate(new Date());
        request.setHead(header);
        return request;
    }
}
