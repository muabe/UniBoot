package com.muabe.uniboot.extension.recycler;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UniSingleRecyclerAdapter<ItemType> extends UniRecyclerAdapter<ItemType>{

    public UniSingleRecyclerAdapter(@NotNull RecyclerView recyclerView) {
        super(recyclerView);
    }

    @NotNull
    @Override
    protected Class<? extends UniViewHolder> getType(ItemType item, int position, List<ItemType> list) {
        return (Class<? extends UniViewHolder>)holderStore.getValues()[0].getClass();
    }
}
