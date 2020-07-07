package com.muabe.uniboot.extension.recycler;

import android.view.View;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.markjmind.uni.common.Store;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UniViewHolder<Item,T extends ViewDataBinding> extends RecyclerView.ViewHolder{

    public Store param = new Store();
    @NotNull
    public T binder;
    private UniRecyclerAdapter adapter;
    private List<Item> list;
    private Item item;
    private int viewType;
    private String groupName;


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

    public List<Item> getItemList(){
        return list;
    }

    void setItemList(List<Item> list){
        this.list = list;
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

    void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }


    public int getItemPosition(){
        return adapter.getItemPosition(getAdapterPosition());
    }

    void setAdapter(UniRecyclerAdapter adapter){
        this.adapter = adapter;
    }

    public UniRecyclerAdapter getAdapter() {
        return adapter;
    }

}
