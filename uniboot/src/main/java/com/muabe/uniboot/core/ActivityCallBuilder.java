package com.muabe.uniboot.core;

import android.util.Log;

import java.util.HashMap;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-07-15
 */
public class ActivityCallBuilder {
    HashMap<String,Object> param = new HashMap<>();
    ActivityCallBack activityCallBack;

    public ActivityCallBuilder(ActivityCallBack activityCallBack){
        this.activityCallBack = activityCallBack;
    }

    public ActivityCallBuilder addParam(String key, Object value){
        param.put(key, value);
        return this;
    }

    public Object getParam(String key){
        return param.get(key);
    }

    public void call(int code){
        final ActivityCallBack.ServiceCallBack callBack = activityCallBack.callbacks.get(code);
        final int runCode = code;
        Log.e("call","call");

        if(activityCallBack.isForeground()) {
            activityCallBack.getCurrentActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (callBack != null) {
                        activityCallBack.callbacks.get(runCode).callback(param);
                        Log.e("call", "excute");
                    }
                }
            });
        }
    }
}
