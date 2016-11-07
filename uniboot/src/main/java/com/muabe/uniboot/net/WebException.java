package com.muabe.uniboot.net;

import com.squareup.okhttp.Response;

/**
 * Created by codemasta on 2015-10-26.
 */
public class WebException extends Exception  {
    public Response response;
    public String body;
    public String message;
    private Throwable exception;

    public WebException(Response response, String body, String message) {
        super(message);
        this.response = response;
        this.message = message;
        this.body = body;
    }


    public WebException(Response response, String body,String message, Throwable exception) {
        super(message, exception);
        this.response = response;
        this.message = message;
        this.exception = exception;
        this.body = body;
    }

    public Response getResponse(){
        return this.response;
    }

    public String getBodyString(){
        return body;
    }
}
