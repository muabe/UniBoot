package com.muabe.uniboot.net;

import com.squareup.okhttp.Response;

/**
 * Created by markj on 2015-12-08.
 */
public class ResultAdapter {
    Response response;
    String bodyString;

    public ResultAdapter(){

    }

    public Response getResponse(){
        return this.response;
    }

    public String getBody(){
        return this.bodyString;
    }

}
