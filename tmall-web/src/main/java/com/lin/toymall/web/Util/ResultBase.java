package com.lin.toymall.web.Util;

import java.io.Serializable;


public class ResultBase implements Serializable {

    /**
     * 状态
     */
    private Status status;

    /**
     * 消息
     */
    private String message;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
