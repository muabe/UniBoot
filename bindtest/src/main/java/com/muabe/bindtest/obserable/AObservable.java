package com.muabe.bindtest.obserable;


import com.muabe.bindtest.BR;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class AObservable extends BaseObservable {
    @Bindable
    private String name;


    private String age="";

    int count = 0;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
        notifyPropertyChanged(BR.age);
    }

    @Bindable
    public String getAge(){
        count++;
        return age+count;
    }

    public void setAge(String age) {
        this.age = age;
        notifyPropertyChanged(BR.age);
    }

    public AObservable set(String name, Object value){
        try {
            String methodName = "set"+name.substring(0,1).toUpperCase()+name.substring(1);
            Method method = getClass().getDeclaredMethod(methodName, value.getClass());
            method.invoke(this, value);

            notifyPropertyChanged(BR.name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return this;
    }
}
