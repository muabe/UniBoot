package com.muabe.bindtest.obserable;

import androidx.databinding.ObservableArrayMap;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

public class ModelNamed {
    public ObservableArrayMap<String, Object> data = new ObservableArrayMap<>();
    ObservableArrayMap<Integer, Object> intData = new ObservableArrayMap<>();
    ObservableInt a = new ObservableInt();
    ObservableField<String> b = new ObservableField<>();

    public ObservableArrayMap<String, UserVo> uservo = new ObservableArrayMap<>();

    public ModelNamed(){
        uservo.put("user", new UserVo());
        a.set(1);
        b.set("dd");
    }
}
