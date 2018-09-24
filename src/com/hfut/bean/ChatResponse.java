package com.hfut.bean;

import java.io.Serializable;

public class ChatResponse implements Serializable {
    private Header head;
    private String src;
    private String content;
    private Boolean succeed;
    private String errorMsg;

    public String getSrc() {

        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Header getHead() {
        return head;
    }

    public void setHead(Header head) {
        this.head = head;
    }

    public Boolean getSucceed() {
        return succeed;
    }

    public void setSucceed(Boolean succeed) {
        this.succeed = succeed;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ChatResponse{" +
                "head=" + head +
                ", src='" + src + '\'' +
                ", content='" + content + '\'' +
                ", succeed=" + succeed +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
