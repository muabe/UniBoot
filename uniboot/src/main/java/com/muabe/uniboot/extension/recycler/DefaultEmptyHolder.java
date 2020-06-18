package com.muabe.uniboot.extension.recycler;

import android.view.View;

import com.muabe.uniboot.databinding.DefaultEmptyHolderBinding;

public class DefaultEmptyHolder extends UniViewHolder<String, DefaultEmptyHolderBinding>{

    public DefaultEmptyHolder(View view) {
        super(view);
    }

    void initLayout(View view){
        binder.uniDefaultEmptyHolderLayout.addView(view);
    }
}