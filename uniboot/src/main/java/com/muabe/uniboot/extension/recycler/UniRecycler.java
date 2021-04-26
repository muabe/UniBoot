package com.muabe.uniboot.extension.recycler;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * - MultiList 지원 : O
 * - ViewType 나누기 : O
 * - Empty : O
 * - show : O
 * - Failed : O
 * - Heder : O
 * - Footer : O
 * - notify : O
 * - paging : O
 * - Param, OnClick 지원 : O
 * - View방식, ViewModel 방식 지원 : X
 * - group,type remove/put/modify : X
 * - notify 그룹에서도 가능 : X
 * - 네이밍 조정 : X
 * - 문서 작업 : X
 *

 * 기타 편의 함수 제공
 *  - 페이징시 자동 프로그래스바
 *  - Header/Footer 고정
 *  - item insert, remove 할때 에니메이션 자동
 */

public class UniRecycler {
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter = new RecyclerAdapter();

    public UniRecycler(@NotNull RecyclerView recyclerView) {
        this(recyclerView, new LinearLayoutManagerWrapper(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
    }

    static class LinearLayoutManagerWrapper extends LinearLayoutManager {
        LinearLayoutManagerWrapper(Context context) {
            super(context) ;
        }
        LinearLayoutManagerWrapper(Context context, int orientation, boolean reverseLayout){
            super(context, orientation, reverseLayout);
        }
        LinearLayoutManagerWrapper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)   {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public boolean supportsPredictiveItemAnimations(){ return false; }
    }


    public UniRecycler(@NotNull RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager){
        initRecyclerView(recyclerView, layoutManager);
    }


    ScrollDetector scrollDetector = new ScrollDetector();
    private void initRecyclerView(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.LayoutManager layoutManager) {
        this.recyclerView = recyclerView;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(scrollDetector);
    }

    public void subscribeLastScroll(boolean isLastScroll){
        scrollDetector.subscriveLastScorll = isLastScroll;
    }

    public UniRecycler setOnLastListener(OnLastListener listener){
        scrollDetector.subscriveLastScorll = true;
        scrollDetector.listener = listener;
        return this;
    }
    private class ScrollDetector extends RecyclerView.OnScrollListener{
        private boolean subscriveLastScorll = false;
        private OnLastListener listener;

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            int lastPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            Log.i("dd", "last:"+lastPosition+" count:"+adapter.getItemCount());
            if(subscriveLastScorll && listener!= null && lastPosition >= adapter.getItemCount()-1){
                subscriveLastScorll = false;
                listener.onLast(UniRecycler.this);
            }
        }
    }

    public interface OnLastListener{
        void onLast(UniRecycler uniRecycler);
    }

    public RecyclerView getRecyclerView(){
        return recyclerView;
    }

    public RecyclerAdapter getAdapter(){
        return adapter;
    }

    public ListGroup addGroup(String groupName){
        return adapter.addGroup(groupName);
    }

    public ListGroup getGroup(String groupName){
        return adapter.getGroup(groupName);
    }

    public ListGroup addHeader(String groupName){
        return adapter.addHeader(groupName);
    }

    public ListGroup addFooter(String groupName){
        return adapter.addFooter(groupName);
    }

    public UniRecycler setEmpty(Class<? extends TypeHolder> clazz, Object item){
        adapter.setEmpty(clazz, item);
        return this;
    }

    public ListGroup getEmpty(){
        return adapter.getEmpty();
    }

    public UniRecycler setFailed(Class<? extends TypeHolder> clazz, Object item){
        adapter.setFailed(clazz, item);
        return this;
    }

    public ListGroup getFailed(){
        return adapter.getFailed();
    }


    public void notifyDataSetChanged(){
        adapter.notifyDataSetChanged();
    }

    public void notifyItemInserted(int position){
        adapter.notifyItemInserted(notifyCount(position));
    }

    public void notifyItemRemoved(int position){
        adapter.notifyItemRemoved(notifyCount(position));
    }

    public void notifyItemMoved(int fromPosition, int toPosition){
        adapter.notifyItemMoved(notifyCount(fromPosition), notifyCount(toPosition));
    }

    public void notifyItemChanged(int position){
        adapter.notifyItemChanged(notifyCount(position));
    }

    public void notifyItemChanged(int position, Object payload){
        adapter.notifyItemChanged(notifyCount(position), payload);
    }

    public void notifyItemRangeInserted(int positionStart, int itemCount){
        adapter.notifyItemRangeInserted(notifyCount(positionStart), itemCount);
    }

    public void notifyItemRangeRemoved(int positionStart, int itemCount){
        adapter.notifyItemRangeRemoved(notifyCount(positionStart), itemCount);
    }

    public void notifyItemRangeChanged(int positionStart, int itemCount){
        adapter.notifyItemRangeChanged(notifyCount(positionStart), itemCount);
    }

    public void notifyItemRangeChanged(int positionStart, int itemCount, Object payload){
        adapter.notifyItemRangeChanged(notifyCount(positionStart), itemCount, payload);
    }

    private int notifyCount(int position){
        return adapter.getHeaderItemSize()+position;
    }

    public UniRecycler registerAdapterDataObserver(@NotNull RecyclerView.AdapterDataObserver observer){
        adapter.registerAdapterDataObserver(observer);
        return this;
    }

    public UniRecycler unregisterAdapterDataObserver(@NotNull RecyclerView.AdapterDataObserver observer){
        adapter.unregisterAdapterDataObserver(observer);
        return this;
    }

}
