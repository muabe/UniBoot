package com.muabe.uniboot.extension.recycler;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.ViewDataBinding;

import com.markjmind.uni.common.Store;
import com.markjmind.uni.mapper.Mapper;
import com.markjmind.uni.mapper.annotiation.adapter.GetViewAdapter;
import com.markjmind.uni.mapper.annotiation.adapter.OnCheckedChangeAdapter;
import com.markjmind.uni.mapper.annotiation.adapter.OnClickAdapter;
import com.markjmind.uni.mapper.annotiation.adapter.ParamAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

public class ViewType {
    private Class<? extends TypeHolder> holderClass;
    private String typeName;
    private int typeIndex;
    Store param = new Store();


    public ViewType(String typeName, Class<? extends TypeHolder> holderClass){
        this.typeName = typeName;
        this.holderClass = holderClass;
    }

    public ViewType(Class<? extends TypeHolder> holderClass){
        this.typeName = holderClass.getName();
        this.holderClass = holderClass;
    }

    void setTypeIndex(int typeIndex){
        this.typeIndex = typeIndex;
    }

    int getTypeIndex(){
        return typeIndex;
    }

    void setTypeName(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName(){
        return typeName;
    }

    UniRecyclerHolder2 getHolder(ViewGroup parents){
        try {
            LayoutInflater inflater = LayoutInflater.from(parents.getContext());
            Class<?> genericClass = (Class<?>)((ParameterizedType)holderClass.getGenericSuperclass()).getActualTypeArguments()[1];
            Method method = genericClass.getDeclaredMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
            ViewDataBinding vb = (ViewDataBinding)method.invoke(null, inflater, parents, false);

            TypeHolder viewType = holderClass.newInstance();
            viewType.init(vb.getRoot(), vb);

            Mapper mapper = new Mapper(viewType.layout, viewType);
            mapper.inject(new GetViewAdapter(), new OnClickAdapter(), new ParamAdapter(param), new OnCheckedChangeAdapter());

            return new UniRecyclerHolder2(vb.getRoot(), viewType);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(holderClass.getName()+" can't create instance",e);
        }
    }

    public ViewType addParam(String key, Object value){
        param.add(key, value);
        return this;
    }

}