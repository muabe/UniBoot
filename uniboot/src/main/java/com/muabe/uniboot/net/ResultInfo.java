package com.muabe.uniboot.net;

import com.markjmind.uni.common.Store;

public class ResultInfo {
    private boolean result = true;
    private String message = "";
    public Store param = new Store();

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
