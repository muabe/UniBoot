package com.muabe.uniboot.net;

public class ResultInfo {
    private boolean result = true;
    private String message = "";

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
