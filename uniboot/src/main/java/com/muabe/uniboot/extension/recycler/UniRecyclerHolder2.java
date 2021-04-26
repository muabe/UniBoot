package com.muabe.uniboot.extension.recycler;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;


public class UniRecyclerHolder2<Item> extends RecyclerView.ViewHolder{
    View layout;
    TypeHolder typeHolder;

    public UniRecyclerHolder2(View itemView, TypeHolder viewTypeHolder) {
        super(itemView);
        this.typeHolder = viewTypeHolder;
    }

    protected void onCreate(){
        typeHolder.onCreate();
    }

    protected void onBind(Item item){
        typeHolder.setItem(item);
        typeHolder.onBind(typeHolder.binder, item);
    }

    public View getView(){
        return layout;
    }



}
