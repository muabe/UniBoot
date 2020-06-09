package com.muabe.uniboot.net;

import com.markjmind.uni.exception.CatchException;
import com.markjmind.uni.exception.OnExceptionListener;

import org.json.JSONException;

import java.net.BindException;
import java.net.ConnectException;
import java.net.HttpRetryException;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

public abstract class WebCatchHelper extends CatchException{

    public WebCatchHelper(){
        addException(WebException.class, webException);
        addException(JSONException.class, jsonException);
        addException(HttpRetryException.class, httpRetryException);
        addException(SocketTimeoutException.class, socketTimeoutException);
        addException(MalformedURLException.class, malformedURLException);
        addException(ProtocolException.class, protocolException);
        addException(SocketException.class, socketException);
        addException(BindException.class, bindException);
        addException(ConnectException.class, connectException);
        addException(NoRouteToHostException.class, noRouteToHostException);
        addException(PortUnreachableException.class, portUnreachableException);
        addException(UnknownHostException.class, unknownHostException);
        addException(UnknownServiceException.class, unknownServiceException);
        addException(URISyntaxException.class, uRISyntaxException);
        setDefaultException(defaultException);
    }

    protected abstract void defaultException(Exception e);

    protected abstract void disconnectInternetException(WebException e);
    protected abstract void not200Exception(WebException e);
    //    protected abstract void unexpectedCodeException(WebException e);
    protected abstract void customResponseCodeException(WebException e);

    protected abstract void jsonException(JSONException e);
    protected abstract void socketTimeoutException(SocketTimeoutException e);
    protected abstract void malformedURLException(MalformedURLException e);
    protected abstract void protocolException(ProtocolException e);
    protected abstract void socketException(SocketException e);
    protected abstract void bindException(BindException e);
    protected abstract void connectException(ConnectException e);
    protected abstract void noRouteToHostException(NoRouteToHostException e);
    protected abstract void portUnreachableException(PortUnreachableException e);
    protected abstract void unknownHostException(UnknownHostException e);
    protected abstract void unknownServiceException(UnknownServiceException e);
    protected abstract void uRISyntaxException(URISyntaxException e);
    protected abstract void httpRetryException(HttpRetryException e);

    OnExceptionListener defaultException = new OnExceptionListener() {
        @Override
        public boolean onException(Exception e) {
            defaultException(e);
            return true;
        }
    };

    OnExceptionListener webException = new OnExceptionListener() {
        @Override
        public boolean onException(Exception exception) {
            WebException e = (WebException) exception;
            if(e.getErrorCode() == WebException.DISCONNECT_INTERNET_CODE){
                disconnectInternetException(e);
            }else if(e.getErrorCode() == WebException.NOT_200_CODE){
                not200Exception(e);
            }else if(e.getErrorCode() == WebException.CUSTOM_RESPONSE_ERROR_CODE){
                customResponseCodeException(e);
            }else if(e.getErrorCode() == WebException.UNEXPECTED_CODE){ //일단 사용하지 않음 Not200과 중복
//                unexpectedCodeException(e);
            }
            return true;
        }
    };

    OnExceptionListener socketTimeoutException = new OnExceptionListener() {
        @Override
        public boolean onException(Exception e) {
            socketTimeoutException((SocketTimeoutException)e);
            return true;
        }
    };

    OnExceptionListener jsonException = new OnExceptionListener() {
        @Override
        public boolean onException(Exception e) {
            jsonException((JSONException)e);
            return true;
        }
    };

    OnExceptionListener malformedURLException = new OnExceptionListener() {
        @Override
        public boolean onException(Exception e) {
            malformedURLException((MalformedURLException)e);
            return true;
        }
    };
    OnExceptionListener protocolException = new OnExceptionListener() {
        @Override
        public boolean onException(Exception e) {
            protocolException((ProtocolException)e);
            return true;
        }
    };
    OnExceptionListener socketException = new OnExceptionListener() {
        @Override
        public boolean onException(Exception e) {
            socketException((SocketException)e);
            return true;
        }
    };
    OnExceptionListener bindException = new OnExceptionListener() {
        @Override
        public boolean onException(Exception e) {
            bindException((BindException)e);
            return true;
        }
    };
    OnExceptionListener connectException = new OnExceptionListener() {
        @Override
        public boolean onException(Exception e) {
            connectException((ConnectException)e);
            return true;
        }
    };
    OnExceptionListener noRouteToHostException = new OnExceptionListener() {
        @Override
        public boolean onException(Exception e) {
            noRouteToHostException((NoRouteToHostException)e);
            return true;
        }
    };
    OnExceptionListener portUnreachableException = new OnExceptionListener() {
        @Override
        public boolean onException(Exception e) {
            portUnreachableException((PortUnreachableException)e);
            return false;
        }
    };
    OnExceptionListener unknownHostException = new OnExceptionListener() {
        @Override
        public boolean onException(Exception e) {
            unknownHostException((UnknownHostException)e);
            return true;
        }
    };
    OnExceptionListener unknownServiceException = new OnExceptionListener() {
        @Override
        public boolean onException(Exception e) {
            unknownServiceException((UnknownServiceException)e);
            return false;
        }
    };
    OnExceptionListener uRISyntaxException = new OnExceptionListener() {
        @Override
        public boolean onException(Exception e) {
            uRISyntaxException((URISyntaxException)e);
            return false;
        }
    };
    OnExceptionListener httpRetryException = new OnExceptionListener() {
        @Override
        public boolean onException(Exception e) {
            httpRetryException((HttpRetryException)e);
            return true;
        }
    };




}
