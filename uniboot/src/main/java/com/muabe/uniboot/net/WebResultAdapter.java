package com.muabe.uniboot.net;

import com.squareup.okhttp.Response;

/**
 * Created by markj on 2015-12-08.
 */
public class WebResultAdapter {
    Response response;
    String bodyString;

    public WebResultAdapter(){

    }

    public Response getResponse(){
        return this.response;
    }

    public String getBody(){
        return this.bodyString;
    }

    public int getCode(){
        if(response != null) {
            return response.code();
        }else{
            return -1;
        }
    }
}
