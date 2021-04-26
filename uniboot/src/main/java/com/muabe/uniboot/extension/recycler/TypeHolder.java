package com.muabe.uniboot.extension.recycler;

import android.view.View;

import androidx.databinding.ViewDataBinding;

import org.jetbrains.annotations.NotNull;

public abstract class TypeHolder<Item, VDB extends ViewDataBinding> {
    View layout;
    VDB binder;
    Item item;

    void init(View layout, VDB binder){
        this.layout = layout;
        this.binder = binder;
    }

    protected void onCreate(){}
    protected abstract void onBind(@NotNull VDB viewDataBinding, Item item);

    public View getView(){
        return layout;
    }

    public VDB getViewDataBinding(){
        return binder;
    }

    void setItem(Item item){
        this.item = item;
    }

    public Item getItem(){
        return this.item;
    }

}
