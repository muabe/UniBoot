package com.muabe.bindtest.obserable;

public class UserVo {
    String name = "";
    String age = "";

    public UserVo(){
        name = "이름";
        age = "나이";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
