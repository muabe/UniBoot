package com.muabe.uniboot.extension.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.markjmind.uni.common.Store;
import com.markjmind.uni.mapper.Mapper;
import com.markjmind.uni.mapper.annotiation.adapter.GetViewAdapter;
import com.markjmind.uni.mapper.annotiation.adapter.OnCheckedChangeAdapter;
import com.markjmind.uni.mapper.annotiation.adapter.OnClickAdapter;
import com.markjmind.uni.mapper.annotiation.adapter.ParamAdapter;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class UniRecyclerAdapter extends RecyclerView.Adapter<UniViewHolder<?, ?>>{
    protected Store buliderStore = new Store();
    protected ArrayList<Class> holderList = new ArrayList<>();
    final static String emptyHolderGroup = "uni_recycler_adapter_empty_hodler";
    private AdapterBuilder emptyAdapterBuilder = null;
    protected RecyclerView recyclerView;
    private ScrollDetector scrollDetector;

    public UniRecyclerAdapter(@NotNull RecyclerView recyclerView) {
        scrollDetector = new ScrollDetector(buliderStore);
        initRecyclerView(recyclerView);
    }

    public UniRecyclerAdapter(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.LayoutManager layoutManager) {
        scrollDetector = new ScrollDetector(buliderStore);
        initRecyclerView(recyclerView, layoutManager);
    }

    public UniRecyclerAdapter initRecyclerView(@NotNull RecyclerView recyclerView) {
        return initRecyclerView(recyclerView, new LinearLayoutManager(recyclerView.getContext()));
    }

    public UniRecyclerAdapter initRecyclerView(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.LayoutManager layoutManager) {
        this.recyclerView = recyclerView;
        recyclerView.setAdapter(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(scrollDetector);
        return this;
    }
    public void setOnLastScrollListener(String groupName, OnLastScrollListener listener){
        scrollDetector.setOnLastScrollListener(groupName, listener);
    }

    public void setOnLastScrollListener(int index, OnLastScrollListener listener){
        scrollDetector.setOnLastScrollListener(""+index, listener);
    }

    public void setOnLastScrollListener(OnLastScrollListener listener){
        scrollDetector.setOnLastScrollListener(null, listener);
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
        holder.setItem(getItem(position));
        holder.setAdapter(this);
        holder.setItemList(getListItem(holder.getGroupName()));
        Mapper mapper = new Mapper(holder.binder.getRoot(), holder);
        holder.param = getAdapter(holder.getGroupName()).getParam();
        mapper.inject(new ParamAdapter(holder.param), new GetViewAdapter(), new OnClickAdapter(), new OnCheckedChangeAdapter());
        holder.onPre();
    }

    private AdapterBuilder getAdapter(String groupName){
        if(isEmptyShow()){
            return emptyAdapterBuilder;
        }
        return ((AdapterBuilder)buliderStore.get(groupName));
    }

    private Object getInstance(ViewGroup parents, int viewType){
        try {
            LayoutInflater inflater = LayoutInflater.from(parents.getContext());
            Class<?> targetClass;
            if(viewType < 0){
                if(emptyAdapterBuilder == null){
                    throw new RuntimeException("등록된 Holder class가 없습니다.");
                }
                targetClass = emptyAdapterBuilder.getHolderClass();
            }else{
                targetClass = holderList.get(viewType);
            }
            Class<?> genericClass = (Class<?>)((ParameterizedType)targetClass.getGenericSuperclass()).getActualTypeArguments()[1];
            Method method = genericClass.getDeclaredMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
            ViewDataBinding vb = (ViewDataBinding)method.invoke(null, inflater, parents, false);

            UniViewHolder holder = (UniViewHolder)targetClass.getConstructor(View.class).newInstance(vb.getRoot());
            holder.setBinder(vb);
            holder.setViewType(viewType);
            if(viewType < 0) {
                holder.setGroupName(emptyHolderGroup);
                if(holder instanceof DefaultEmptyHolder){
                    ((DefaultEmptyHolder)holder).initLayout(LayoutInflater.from(recyclerView.getContext()).inflate(emptyAdapterBuilder.layout, ((DefaultEmptyHolder)holder).binder.uniDefaultEmptyHolderLayout, false));
                }
            }else{
                holder.setGroupName(getBuilder(targetClass).getGroupName());
            }
            return holder;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException("HolderTypeAdapter Annotation Exception:1",e);
        }
    }

    @NotNull
    @Override
    public int getItemCount() {
        int totalSize = getItemSize();
        if(isEmptyShow()){
            return 1;
        }else{
            return totalSize;
        }
    }

    public int getItemSize(){
        Object[] values = buliderStore.getValues();
        int totalSize = 0;
        for(Object value : values){
            totalSize +=  ((AdapterBuilder)value).getList().size();
        }
        return totalSize;
    }

    private boolean isEmptyShow(){
        return getItemSize() == 0 && emptyAdapterBuilder != null;
    }

    @NotNull
    @Override
    public int getItemViewType(int position) {
        String[] keys = buliderStore.getKeys();
        int addSize = 0;
        for(String key : keys){
            AdapterBuilder builder = (AdapterBuilder) buliderStore.get(key);
            if(position < addSize+builder.getList().size()){
                Object item = builder.getList().get(position-addSize);
                Class<? extends UniViewHolder<?, ?>> type = builder.getTypeListener().getType(item, position-addSize, builder.getList());
                builder.setHolderClass(type);
                if(!holderList.contains(type)){
                    holderList.add(type);
                }
                return holderList.indexOf(type);
            }
            addSize += builder.getList().size();
        }
        return -1;
    }

    private AdapterBuilder getBuilder(Class holderType){
        Object[] builders = buliderStore.getValues();
        for(Object builder : builders){
            if(((AdapterBuilder)builder).getHolderClass().toString().equals(holderType.toString())){
                return (AdapterBuilder)builder;
            }
        }
        return null;
    }

    public boolean hasGroup(String groupName){
        return buliderStore.containsKey(groupName);
    }

    public void removeGroup(String groupName){
        if(hasGroup(groupName)){
            int size = getListItem(groupName).size();
            int start = getStartGroupPosition(groupName);
            buliderStore.remove(groupName);
            notifyItemRangeRemoved(start, size);
        }
    }

    public void removeGroup(int index){
        this.removeGroup(""+index);
    }

    public int getGroupIndex(String groupName){
        int index = -1;
        for(int i=0; i<buliderStore.size(); i++){
            if (buliderStore.containsKey(groupName)) {
                return i;
            }
        }
        return -1;
    }

    private Object getItem(int position){
        if(isEmptyShow()){
            return emptyAdapterBuilder.getList().get(0);
        }else {
            String[] keys = buliderStore.getKeys();
            int addSize = 0;
            for (String key : keys) {
                AdapterBuilder builder = (AdapterBuilder) buliderStore.get(key);
                if (position < addSize + builder.getList().size()) {
                    return builder.getList().get(position - addSize);
                }
                addSize += builder.getList().size();
            }
        }
        return null;
    }

    int getItemPosition(int adapterPosition) {
        if (isEmptyShow()) {
            return 0;
        } else {
            String[] keys = buliderStore.getKeys();
            int addSize = 0;
            for (String key : keys) {
                AdapterBuilder builder = (AdapterBuilder) buliderStore.get(key);
                if (adapterPosition < addSize + builder.getList().size()) {
                    return adapterPosition - addSize;
                }
                addSize += builder.getList().size();
            }
            return 0;
        }
    }

    public List getListItem(String groupName){
        return getAdapter(groupName).getList();
    }

    public List getListItem(int index){
        return getListItem(""+index);
    }

    public List getListItem(){
        return getListItem(0);
    }

    public Object getSingleItem(int index){
        return getListItem(index).get(0);
    }

    public Object getSingleItem(){
        return getSingleItem(0);
    }



    public AdapterBuilder addListItem(@NotNull String groupName, List list, @NotNull TypeListener<?> typeListener){
        if(list == null){
            list = new ArrayList();
        }
        AdapterBuilder builder = new AdapterBuilder(groupName, list, typeListener);
        buliderStore.add(groupName, builder);
        return builder;
    }

    public AdapterBuilder addListItem(List list, @NotNull TypeListener<?> typeListener){
        String groupName = ""+buliderStore.size();
        return this.addListItem(groupName, list, typeListener);
    }


    public AdapterBuilder addListItem(@NotNull String groupName, List list, @NotNull Class<? extends UniViewHolder<?, ?>> holderClass){
        return this.addListItem(groupName, list, new SingleTypeListener(holderClass));
    }

    public AdapterBuilder addListItem(List list, @NotNull Class<? extends UniViewHolder<?, ?>> holderClass){
        String groupName = ""+buliderStore.size();
        return this.addListItem(groupName, list, holderClass);
    }

    public AdapterBuilder addSingleItem(@NotNull String groupName, Object item, @NotNull Class<? extends UniViewHolder<?, ?>> holderClass){
        ArrayList list = new ArrayList();
        list.add(item);
        return this.addListItem(groupName, list, holderClass);
    }

    public AdapterBuilder addSingleItem(Object item, @NotNull Class<? extends UniViewHolder<?, ?>> holderClass){
        String groupName = ""+buliderStore.size();
        return this.addSingleItem(groupName, item, holderClass);
    }

    public AdapterBuilder setEmptyItem(Object item, @NotNull Class<? extends UniViewHolder<?, ?>> holderClass){
        ArrayList list = new ArrayList();
        list.add(item);
        this.emptyAdapterBuilder = new AdapterBuilder(emptyHolderGroup, list, new SingleTypeListener(holderClass));
        this.emptyAdapterBuilder.setHolderClass(holderClass);
        return this.emptyAdapterBuilder;
    }

    public AdapterBuilder setEmptyItem(int layoutId){
        return setEmptyItem(null, DefaultEmptyHolder.class).setEmptyLayout(layoutId);
    }

    private class SingleTypeListener implements TypeListener{
        Class<? extends UniViewHolder<?, ?>> holderClass;
        SingleTypeListener(Class<? extends UniViewHolder<?, ?>> holderClass) {
            this.holderClass = holderClass;
        }

        @Override
        public Class<? extends UniViewHolder<?, ?>> getType(@NotNull Object item, @NotNull int position, @NotNull List list) {
            return holderClass;
        }
    }

    public int getStartGroupPosition(String groupName){
        int totalCount = 0;

        for(String key : buliderStore.getKeys()){
            if(key.equals(groupName)){
                break;
            }
            totalCount += ((AdapterBuilder)buliderStore.get(key)).getList().size();
        }
        return totalCount;
    }


    private class ScrollDetector extends RecyclerView.OnScrollListener{
        private Store buliderStore;
        private OnLastScrollListener listener;
        private String groupName = null;

        ScrollDetector(Store buliderStore){
            this.buliderStore = buliderStore;
        }

        private int count(){
            int totalCount = 0;
            for(Object b : buliderStore.getValues()){
                totalCount += ((AdapterBuilder)b).getList().size();
            }
            return totalCount;
        }
        void setOnLastScrollListener(String groupName, OnLastScrollListener listener){
            this.listener = listener;
            this.groupName = groupName;
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            int lastPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
//            Log.e("dd", "last:"+((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition()+" count:"+count());
            if(listener != null && lastPosition >= count()-1 && buliderStore.size() > 0){
                Object[] values = buliderStore.getValues();
                if(values.length > 0){
                    List list;
                    if(groupName == null){
                        list = ((AdapterBuilder)values[values.length-1]).getList();
                    }else{
                        list = getListItem(groupName);
                    }
                    if(list!=null && list.size() > 0){
                        onLast(list.get(list.size()-1), list);
                        return;

                    }
                }
                onLast(null, new ArrayList());
            }
        }

        public void onLast(Object item, List<?> itemList)
        {
            listener.onLast(item, itemList);

        }
    }

    public interface OnLastScrollListener<ItemType>{
        boolean onLast(ItemType item, @NotNull List<ItemType> itemList);
    }

    public class AdapterBuilder{
        private Store param = new Store();
        private String groupName;
        private List list;
        private Class<? extends UniViewHolder<?, ?>> holderClass;
        private TypeListener<?> typeListener;
        private View emptyView;

        public AdapterBuilder(String groupName, List list, TypeListener<?> typeListener){
            this.groupName = groupName;
            this.list = list;
            this.typeListener = typeListener;
        }

        AdapterBuilder setEmptyView(View view){
            this.emptyView = view;
            return this;
        }

        public int layout;
        AdapterBuilder setEmptyLayout(int layout){
            this.layout = layout;
            return this;
        }

        Store getParam() {
            return param;
        }

        String getGroupName() {
            return groupName;
        }

        List getList() {
            return list;
        }

        void setList(List list) {
            this.list = list;
        }

        Class<? extends UniViewHolder<?, ?>> getHolderClass() {
            return holderClass;
        }

        void setHolderClass(Class<? extends UniViewHolder<?, ?>> holderClass) {
            this.holderClass = holderClass;
        }

        TypeListener getTypeListener() {
            return typeListener;
        }

        public AdapterBuilder addParam(String key, Object value){
            param.add(key, value);
            return this;
        }

    }

    public interface TypeListener<ItemType>{
        Class<? extends UniViewHolder<?,?>> getType(@NotNull ItemType item, @NotNull int position, @NotNull List<?> list);
    }

}
