package com.muabe.uniboot.net;

/**
 * Created by markj on 2015-12-04.
 */
public abstract class WebExceptionMessage {
    private WebExceptionMessage instance;

    public abstract String getExceptionMessage(Exception e);

    public abstract String getCodeMessage(WebResult webResult, int code);

}
