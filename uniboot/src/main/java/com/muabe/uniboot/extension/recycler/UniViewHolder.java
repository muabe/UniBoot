package com.muabe.uniboot.extension.recycler;

import android.view.View;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class UniViewHolder<Item,T extends ViewDataBinding> extends RecyclerView.ViewHolder{
    public T binder;
    public Item item;
    public int viewType;
    public String typeName;

    public UniViewHolder(View view){
        super(view);
    }

    void setBinder(@NotNull ViewDataBinding binder){
        this.binder = (T)binder;
    }

    public void onPre(){

    }

    public T getBinder() {
        return binder;
    }

    public Item getItem() {
        return item;
    }

    void setItem(Item item) {
        this.item = item;
    }

    public int getViewType() {
        return viewType;
    }

    void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getTypeName() {
        if(typeName == null){
            RecycleHolder recycleHolder = getClass().getAnnotation(RecycleHolder.class);
            if(recycleHolder == null){
                throw new RuntimeException("Not define @HolderType annotation");
            }
            typeName = getClass().getAnnotation(RecycleHolder.class).value();
        }
        return typeName;
    }

    void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
