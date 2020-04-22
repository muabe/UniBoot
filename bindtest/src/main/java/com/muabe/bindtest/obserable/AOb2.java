package com.muabe.bindtest.obserable;

import java.util.Hashtable;

import androidx.databinding.ObservableField;

public class AOb2<T> extends ObservableField<T> {
    ObservableField<String> f;
//    UserVo userVo = new UserVo();

    Hashtable<String, T> hashtable = new Hashtable<>();

    public void set(String key, T value) {

    }

}
