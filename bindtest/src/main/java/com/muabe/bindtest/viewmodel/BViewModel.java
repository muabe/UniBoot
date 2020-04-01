package com.muabe.bindtest.viewmodel;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BViewModel extends ViewModel {
    private MutableLiveData<String> name = new MutableLiveData<>();
    private MutableLiveData<Integer> age = new MutableLiveData<>();

    public MutableLiveData<String> getName() {
        return name;
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public MutableLiveData<Integer> getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age.setValue(age);
    }
}
