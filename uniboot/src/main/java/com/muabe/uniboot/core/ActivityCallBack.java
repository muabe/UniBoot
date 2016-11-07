package com.muabe.uniboot.core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-07-15
 */
public class ActivityCallBack implements Application.ActivityLifecycleCallbacks {
    Activity foregroundActivity;

    HashMap<Integer, ServiceCallBack> callbacks = new HashMap<>();

    public interface ServiceCallBack{
        void callback(HashMap<String, Object> param);
    }

    public void registerCallbacks(Application application){
        application.registerActivityLifecycleCallbacks(this);
    }

    public void unregisterCallbacks(Application application){
        foregroundActivity = null;
        application.unregisterActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.d("reg", "onActivityCreated");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.d("reg", "onActivityStarted");
//            foregroundActivity = activity;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.d("reg", "onActivityStopped");
//            foregroundActivity = null;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.d("reg", "onActivitySaveInstanceState");


    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.d("reg", "onActivityResumed");
        foregroundActivity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.d("reg", "onActivityPaused");
        foregroundActivity = null;
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.d("reg", "onActivityDestroyed");
//            foregroundActivity = null;
    }

    public boolean isForeground(){
        return foregroundActivity!=null;
    }

    public Activity getCurrentActivity(){
        return foregroundActivity;
    }

    public void addServiceCallBack(int code, ServiceCallBack callBack){
        callbacks.put(code, callBack);
    }

    public void removeServiceCallBack(int code){
        callbacks.remove(code);
    }
}
