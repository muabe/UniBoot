package com.muabe.uniboot.net;

import org.json.JSONException;

public interface OnResultListener {
    void onResult(ResultInfo info, WebResult webResult) throws JSONException;
}
