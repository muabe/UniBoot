package com.muabe.uniboot.extension.recycler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class ListGroup {
    private RecyclerAdapter adapter;
    private String groupName;
    private List<?> list;
    private LinkedHashMap<String, ViewType> groupMap = new LinkedHashMap<>();
    private RecyclerAdapter.GroupTypeListener typeListener;
    private boolean show = true;

    ListGroup(RecyclerAdapter adapter, String groupName){
        this.adapter = adapter;
        this.groupName = groupName;
    }

    public String getGroupName(){
        return groupName;
    }

    public ListGroup setList(List<?> list, RecyclerAdapter.GroupTypeListener listener){
        setTypeListener(listener);
        this.list = list;
        return this;
    }

    public ListGroup setList(List<?> list){
        this.list = list;
        return this;
    }

    public List<?> getList(){
        return list;
    }

    public ListGroup setSingleItem(Object item){
        List list = new ArrayList();
        list.add(item);
        return setList(list);
    }

    public ListGroup addType(ViewType viewType){
        viewType.setTypeIndex(++adapter.typeIndex);
        groupMap.put(viewType.getTypeName(), viewType);
        return this;
    }

    public ListGroup addType(String typeName, Class<? extends TypeHolder> clazz){
        addType(new ViewType(typeName, clazz));
        return this;
    }

    public ListGroup addType(Class<? extends TypeHolder> clazz){
        addType(clazz.getName(), clazz);
        return this;
    }

    public ListGroup setShow(boolean isShow){
        this.show = isShow;
        return this;
    }

    public boolean isShow(){
        return show;
    }

    int getItemCount(){
        if(list == null || !show){
            return 0;
        }
        return list.size();
    }

    void clear(){
        if(list != null){
            list.clear();
        }
    }

    public void removeType(String typeName){
        groupMap.remove(typeName);
    }
        ViewType getTypeRecycle(int viewType){
        for(ViewType recycle : groupMap.values()){
            if(recycle.getTypeIndex() == viewType){
                return recycle;
            }
        }
        return null;
    }

    public  ListGroup setTypeListener(RecyclerAdapter.GroupTypeListener listener){
        this.typeListener = listener;
        return this;
    }

    int getItemViewType(int position){
        if(typeListener == null) {
            return Objects.requireNonNull(groupMap.get(groupMap.keySet().iterator().next())).getTypeIndex();
        }
        String typeName = typeListener.getType(list.get(position));
        return Objects.requireNonNull(groupMap.get(typeName)).getTypeIndex();
    }

    public Object getItem(int position){
        return list.get(position);
    }
}