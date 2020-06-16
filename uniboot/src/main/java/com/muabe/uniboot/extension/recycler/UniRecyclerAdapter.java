package com.muabe.uniboot.extension.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public abstract class UniRecyclerAdapter<ItemType> extends RecyclerView.Adapter<UniViewHolder<?, ?>> {
//    protected final Store holderStore = new Store();
    @NotNull
    protected List<ItemType> itemList = new ArrayList<>();
    final static String defaultType = "uni_recycler_default_type";
    protected RecyclerView recyclerView;

    public UniRecyclerAdapter(@NotNull RecyclerView recyclerView) {
        initRecyclerView(recyclerView);
    }

    public UniRecyclerAdapter initRecyclerView(@NotNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerView.setAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        return this;
    }

    @NotNull
    public RecyclerView getRecyclerView() {
        return this.recyclerView;
    }

    @NonNull
    @Override
    public UniViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return (UniViewHolder) getInstance(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull UniViewHolder holder, int position) {
        holder.setItem(itemList.get(position));
        holder.onPre();
    }

    @NotNull
    protected abstract Class<? extends UniViewHolder> getType(ItemType item, int position, List<ItemType> list);


//    public UniRecyclerAdapter<ItemType> addHolder(@NotNull Class<? extends UniViewHolder<?, ?>>... holders) {
//
//        for(Class<? extends UniViewHolder<?, ?>> holder : holders) {
//            try {
//                holderStore.add(holder.toString(), holder.getConstructor(View.class).newInstance(new View(recyclerView.getContext())));
//            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
//                throw new RuntimeException("Not define @HolderType default constructor", e);
//            }
//        }
//        return this;
//    }


    private Object getInstance(ViewGroup parents, int viewType){
        try {
            LayoutInflater inflater = LayoutInflater.from(parents.getContext());
            Class<?> targetClass = holderList.get(viewType);
            Class<?> genericClass = (Class<?>)((ParameterizedType)targetClass.getGenericSuperclass()).getActualTypeArguments()[1];
            Method method = genericClass.getDeclaredMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
            ViewDataBinding vb = (ViewDataBinding)method.invoke(null, inflater, parents, false);

            UniViewHolder holder = (UniViewHolder)targetClass.getConstructor(View.class).newInstance(vb.getRoot());
            holder.setBinder(vb);
            holder.setViewType(viewType);
            return holder;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException("HolderTypeAdapter Annotation Exception:1",e);
        }
    }


    ArrayList<Class> holderList = new ArrayList<>();
    @NotNull
    @Override
    public int getItemViewType(int position) {
//        ItemType item = itemList.get(position);
//        String[] keys = holderStore.getKeys();
//        int i =0;
//        for(String key : keys){
//            if(key.equals(getType(item, position, getList()).toString())){
//                return i;
//            }
//            i++;
//        }
        ItemType item = itemList.get(position);
        Class type = getType(item, position, getList());
        if(!holderList.contains(type)){
            holderList.add(type);
        }
        return holderList.indexOf(type);
    }




//    public String getTypeName(int viewType){
//        String[] infos = holderStore.getKeys();
//        if(infos == null){
//            return "";
//        }else{
//            return (String)infos[viewType];
//        }
//    }

    @NotNull
    @Override
    public int getItemCount() {
        return itemList.size();
    }


    @NotNull
    public UniRecyclerAdapter setList(@NotNull List list) {
        itemList = list;
        return this;
    }

    @NotNull
    public List<ItemType> getList() {
        return itemList;
    }
}
