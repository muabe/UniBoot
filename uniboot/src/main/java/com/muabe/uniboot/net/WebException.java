package com.muabe.uniboot.net;

import com.squareup.okhttp.Response;

/**
 * Created by codemasta on 2015-10-26.
 */
public class WebException extends Exception  {
    private Response response;
    private String body;
    private String message;
    private Throwable exception;
    private WebResult webResult;
    private int errorCode;
    private ResultInfo resultInfo;

    public static final int DISCONNECT_INTERNET_CODE = 0;
    /**
     * 200~300 까지 코드가 아닌 경우, 성공코드가 아닌경우
     */
    public static final int UNEXPECTED_CODE = 1;

    /**
     * 200 코드가 아닌 경우
     */
    public static final int NOT_200_CODE = 2;

    /**
     * 유저가 정의한 코드에서 에러일 경우
     */
    public static final int CUSTOM_RESPONSE_ERROR_CODE = 3;

//    public WebException(int errorCode, Response response, String body, String message) {
//        super(message);
//        this.errorCode = errorCode;
//        this.response = response;
//        this.message = message;
//        this.body = body;
//    }
//
//
    public WebException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public WebException(int errorCode, WebResult webResult, String message, Throwable exception) {
        super(message, exception);
        this.errorCode = errorCode;
        this.webResult = webResult;
        this.response = webResult.getResponse();
        this.message = message;
        this.exception = exception;
        this.body = webResult.getBody();
    }

    public WebException(int errorCode, WebResult webResult, String message) {
        super(message);
        this.errorCode = errorCode;
        this.webResult = webResult;
        this.response = webResult.getResponse();
        this.message = message;
        this.body = webResult.getBody();
    }

    public WebException setCustomResult(ResultInfo info){
        this.resultInfo = info;
        return this;
    }

    public ResultInfo getCustomResult(){
        return resultInfo;
    }

    public WebResult getWebResult() {
        return webResult;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Throwable getException() {
        return exception;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public Response getResponse(){
        return this.response;
    }

    public int getCode(){
        return this.response.code();
    }

    public String getBodyString(){
        return body;
    }
}
