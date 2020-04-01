package com.muabe.bindtest.obserable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class ModelNamed extends BaseObservable {
    @Bindable
    private String name;
    private int age;
}
