package com.muabe.uniboot.extension.recycler;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.markjmind.uni.common.Store;
import com.markjmind.uni.mapper.Mapper;
import com.markjmind.uni.mapper.annotiation.adapter.GetViewAdapter;
import com.markjmind.uni.mapper.annotiation.adapter.OnClickAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class UniRecyclerMultiAdapter extends RecyclerView.Adapter<UniViewHolder<?, ?>>{
    private final Store holderStore = new Store();
    private final Store itemStore = new Store();
    private final static String defaultType = "uni_recycler_default_type";
    private RecyclerView recyclerView;

    public UniRecyclerMultiAdapter(@NotNull RecyclerView recyclerView){
        initRecyclerView(recyclerView);
    }

    public UniRecyclerMultiAdapter initRecyclerView(@NotNull RecyclerView recyclerView){
        this.recyclerView = recyclerView;
        recyclerView.setAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        return this;
    }

    @NotNull
    public RecyclerView getRecyclerView(){
        return this.recyclerView;
    }

    @NonNull
    @Override
    public UniViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return (UniViewHolder)getInstance(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull UniViewHolder<?, ?> holder, int position) {
        holder.onPre();
    }

    public void addHolder(Class<? extends UniViewHolder<?, ?>> holder){
        RecycleHolder recycleHolder = holder.getAnnotation(RecycleHolder.class);
        if(recycleHolder == null){
            throw new RuntimeException("Not define @HolderType annotation");
        }
        String typeName = recycleHolder.value();
        if(typeName.isEmpty()){
            typeName = defaultType;
        }
        holderStore.add(typeName, holder);
    }

    private Object getInstance(ViewGroup parent, int viewType){
        Class<? extends UniViewHolder<?, ?>> holderClass = (Class<? extends UniViewHolder<?, ?>>)holderStore.get(getType(viewType));

        Mapper mapper = new Mapper(holderClass);
        //먼저 Binder를 가져오고
        HolderTypeAdapter holderTypeAdapter = new HolderTypeAdapter(parent);
        mapper.inject(holderTypeAdapter);
        UniViewHolder holder = holderTypeAdapter.getBindInstance(viewType);

        //나머지는 binder.getView()를 중심으로(view) 매핑한다.
        mapper.reset(holderTypeAdapter.getRoot(), holder);
        mapper.inject(new GetViewAdapter(), new OnClickAdapter());
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        int count = 0;
        List<List> map = new ArrayList<List>(itemStore.values());
        for(int i=0; i<map.size(); i++){
            List list = map.get(i);
            if(count <= position && position < count+list.size()){
                return i;
            }
            count += list.size();
        }
        return count;
    }

    private int getTypeIndex(String typeName){
        for(int i = 0; i< itemStore.size(); i++){
            if(itemStore.containsKey(typeName)){
                return i;
            }
        }
        return -1;
    }

    private String getType(int typeIndex){
        String[] keys =  itemStore.getKeys();
        for(int i=0; i<keys.length; i++){
            if(typeIndex == i){
                return keys[i];
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        Object[] map = itemStore.getValues();
        int count = 0;
        for(Object list: map){
            count += ((List)list).size();
        }
        return count;
    }


    @NotNull
    public UniRecyclerMultiAdapter setList(@NotNull String typeName, @NotNull ArrayList list){
        itemStore.add(typeName, list);
        return this;
    }

    @NotNull
    public UniRecyclerMultiAdapter setList(@NotNull ArrayList list){
        this.setList(defaultType, list);
        return this;
    }

    @NotNull
    public UniRecyclerMultiAdapter add(@NotNull int index, @NotNull String typeName, Object value){
        getList(typeName).add(index, value);
        return this;
    }

    @NotNull
    public UniRecyclerMultiAdapter add(@NotNull String typeName, ArrayList list){
        getList(typeName).add(list);
        return this;
    }

    @NotNull
    public UniRecyclerMultiAdapter add(ArrayList list){
        add(defaultType, list);
        return this;
    }

    @NotNull
    public LinkedHashMap<String, ArrayList> getItemStore(){
        return itemStore;
    }

    @NotNull
    public ArrayList getList(@NotNull String typeName){
        ArrayList list;
        if(itemStore.containsKey(typeName)){
            list = (ArrayList) itemStore.get(typeName);

        }else{
            list = new ArrayList();
            setList(typeName, list);
        }
        return list;
    }

    @NotNull
    public ArrayList getList(){
        return getList(defaultType);
    }

}
