package com.hfut.bean;

import java.io.Serializable;

public class ChatRequest implements Serializable {

    private Header head;
    private String name;
    private String toName;
    private String content;
    public String getToName() {
        return toName;
    }
    public void setToName(String toName) {
        this.toName = toName;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Header getHead() {
        return head;
    }

    public void setHead(Header head) {
        this.head = head;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ChatRequest{" +
                "head=" + head +
                ", name='" + name + '\'' +
                ", toName='" + toName + '\'' +
                ", content='" + content + '\'';
    }
}
