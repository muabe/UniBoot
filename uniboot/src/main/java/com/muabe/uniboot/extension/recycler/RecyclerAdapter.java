package com.muabe.uniboot.extension.recycler;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedHashMap;

public class RecyclerAdapter extends RecyclerView.Adapter<UniRecyclerHolder2>{

    public interface GroupTypeListener<T>{
        String getType(T item);
    }

    int typeIndex = 0;
    private final LinkedHashMap<String, ListGroup> headerGroup = new LinkedHashMap<>();
    private final LinkedHashMap<String, ListGroup> allGroup = new LinkedHashMap<>();
    private final LinkedHashMap<String, ListGroup> footerGroup = new LinkedHashMap<>();

    private final String emptyGroupName = "uni_recycle_empty_group_name";
    private final String failedGroupName = "uni_recycle_empty_group_name";
    private final ListGroup empty = new ListGroup(this, emptyGroupName);
    private final ListGroup failed = new ListGroup(this, failedGroupName);

    private final GroupSelector groupSelector = new GroupSelector(headerGroup, allGroup, footerGroup);

    RecyclerAdapter(){
        failed.setShow(false);
    }


    public ListGroup addGroup(String groupName){
        ListGroup group = new ListGroup(this, groupName);
        allGroup.put(groupName, group);
        return group;
    }


    public ListGroup addHeader(String groupName){
        ListGroup group = new ListGroup(this, groupName);
        headerGroup.put(groupName, group);
        return group;
    }

    public ListGroup addFooter(String groupName){
        ListGroup group = new ListGroup(this, groupName);
        footerGroup.put(groupName, group);
        return group;
    }

    public void setEmpty(Class<? extends TypeHolder> clazz, Object item){
        if(clazz == null){
            empty.clear();
        }else {
            empty.addType(emptyGroupName, clazz);
            empty.setSingleItem(item);
        }
    }

    public ListGroup getEmpty(){
        return empty;
    }

    public void setFailed(Class<? extends TypeHolder> clazz, Object item){
        if(clazz == null){
            failed.clear();
        }else {
            failed.addType(failedGroupName, clazz);
            failed.setSingleItem(item);
        }
    }

    public ListGroup getFailed(){
        return failed;
    }

    public ListGroup getGroup(String groupName){
        return allGroup.get(groupName);
    }

    public ListGroup getGroup(int position){
        return groupSelector.getGroup(position);
    }





    @NonNull
    @Override
    public UniRecyclerHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UniRecyclerHolder2 holder = groupSelector.getHolder(parent, viewType);
        holder.onCreate();
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull UniRecyclerHolder2 holder, int position) {
          holder.onBind(groupSelector.getItem(position));
    }


    @Override
    public int getItemViewType(int position) {
        return groupSelector.getViewType(position);
    }




    private int getGroupItemSize(LinkedHashMap<String, ListGroup> group){
        int count = 0;
        for(String groupName : group.keySet()){
            count += group.get(groupName).getItemCount();
        }
        return count;
    }

    int getContentsItemSize(){
        return getGroupItemSize(allGroup);
    }

    int getHeaderItemSize(){
        return getGroupItemSize(headerGroup);
    }

    int getFooterItemSize(){
        return getGroupItemSize(footerGroup);
    }

    @Override
    public int getItemCount() {
        return groupSelector.getItemCount();
    }


    class GroupSelector{
        int count;
        LinkedHashMap<String, ListGroup>[] groupMaps;

        GroupSelector(LinkedHashMap<String, ListGroup>... groupMaps){
            this.groupMaps = groupMaps;
        }

        int getItemCount(){
            int count = getHeaderItemSize();
            if(failed.isShow() && failed.getItemCount() > 0){
                return count + failed.getItemCount()+getFooterItemSize();
            }
            count += getContentsItemSize();
            if(getContentsItemSize() == 0 && empty.getItemCount() > 0){
                count++;
            }
            count += getFooterItemSize();
            return count;
        }

        ListGroup getGroup(int position){
            count = 0;
            for(LinkedHashMap<String, ListGroup> groupMap : groupMaps){
                if(groupMap == allGroup && failed.isShow() &&  failed.getItemCount() > 0){
                    if(position > count){
                        count += failed.getItemCount();
                        continue;
                    }else {
                        return failed;
                    }
                }
                if(groupMap == allGroup && getContentsItemSize() == 0 && empty.getItemCount() > 0){
                    if(position > count){
                        count += empty.getItemCount();
                        continue;
                    }else {
                        return empty;
                    }
                }

                for(ListGroup group : groupMap.values()){
                    count += group.getItemCount();
                    if(position < count){
                        return group;
                    }
                }
            }
            throw new IndexOutOfBoundsException();
        }

        Object getItem(int position){
            ListGroup group = getGroup(position);
            if(group == failed){
                return group.getItem(0);
            }else if(group == empty){
                return group.getItem(0);
            }
            return group.getItem(position-(count-group.getItemCount()));
        }

        int getViewType(int position){
            ListGroup group = getGroup(position);
            if(group == failed){
                return group.getItemViewType(0);
            }else if(group == empty){
                return group.getItemViewType(0);
            }
            return group.getItemViewType(position-(count-group.getItemCount()));
        }

        UniRecyclerHolder2 getHolder(ViewGroup parent, int viewType){
            for(LinkedHashMap<String, ListGroup> groupMap : groupMaps){
                if(groupMap == allGroup && failed.isShow() && failed.getItemCount() > 0){
                    ViewType recycle = failed.getTypeRecycle(viewType);
                    return recycle.getHolder(parent);
                }

                if(groupMap == allGroup && getContentsItemSize() == 0 && empty.getItemCount() > 0){
                    ViewType recycle = empty.getTypeRecycle(viewType);
                    if(recycle != null){
                        return recycle.getHolder(parent);
                    }
                }
                for(ListGroup group : groupMap.values()){
                    ViewType recycle = group.getTypeRecycle(viewType);
                    if(recycle != null){
                        return recycle.getHolder(parent);
                    }
                }
            }


            throw new NullPointerException();
        }
    }

}
