package com.muabe.bindtest.obserable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class AObservable extends BaseObservable {
    @Bindable
    private String name;
    @Bindable
    private String name2;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }
}
