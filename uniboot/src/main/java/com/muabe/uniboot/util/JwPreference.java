package com.muabe.uniboot.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-11-22
 */


public class JwPreference {
    private Context context;
    private String preferenceName;
    private int preferenceMode = MODE_PRIVATE;


    public JwPreference(Context context, String preferenceName){
        this.context = context;
        this.preferenceName = preferenceName;
    }

    public JwPreference(Context context){
        this.context = context;
        this.preferenceName = getClass().getName();
    }

    public void setString(String key, String value){
        SharedPreferences prefs = context.getSharedPreferences(preferenceName, preferenceMode);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key, String opt){
        SharedPreferences prefs = context.getSharedPreferences(preferenceName, preferenceMode);
        return prefs.getString(key, opt);
    }

    public void setBoolean(String key, boolean value){
        SharedPreferences prefs = context.getSharedPreferences(preferenceName, preferenceMode);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key, boolean opt){
        SharedPreferences prefs = context.getSharedPreferences(preferenceName, preferenceMode);
        return prefs.getBoolean(key, opt);
    }

    public void setInt(String key, int value){
        SharedPreferences prefs = context.getSharedPreferences(preferenceName, preferenceMode);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key, int opt){
        SharedPreferences prefs = context.getSharedPreferences(preferenceName, preferenceMode);
        return prefs.getInt(key, opt);
    }

    public void setLong(String key, long value){
        SharedPreferences prefs = context.getSharedPreferences(preferenceName, preferenceMode);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public long getLong(String key, long opt){
        SharedPreferences prefs = context.getSharedPreferences(preferenceName, preferenceMode);
        return prefs.getLong(key, opt);
    }

    public Map<String, ?> getAll(){
        SharedPreferences prefs = context.getSharedPreferences(preferenceName, preferenceMode);
        return prefs.getAll();
    }

    public Context getContext(){
        return this.context;
    }

}
