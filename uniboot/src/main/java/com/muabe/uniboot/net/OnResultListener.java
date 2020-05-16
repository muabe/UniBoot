package com.muabe.uniboot.net;

import org.json.JSONException;

public interface OnResultListener {
    boolean onResult(WebResult webResult) throws JSONException;
}
