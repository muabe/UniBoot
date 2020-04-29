package com.muabe.bindtest.obserable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.markjmind.uni.UniFragment;

public class BaseFragment extends UniFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        //        binding = DataBindingUtil.bind(uniLayout.view)
////        try {
////            Method method = AfragmentBinding.class.getMethod("inflate", LayoutInflater.class);
////            ViewDataBinding vb = (ViewDataBinding)method.invoke(null, getBindLayoutInfo().getLayoutInflater());
////            uniLayout.setView(vb.getRoot());
////            uniLayout.setViewDataBinding(vb);
////            binding = uniLayout.getViewDataBinding();
////            super.onViewCreated(view, savedInstanceState);
////        } catch (NoSuchMethodException e) {
////            e.printStackTrace();
////        } catch (IllegalAccessException e) {
////            e.printStackTrace();
////        } catch (InvocationTargetException e) {
////            e.printStackTrace();
////        }
////        ViewDataBinding vb = DataBindingUtil.inflate(getBindLayoutInfo().getLayoutInflater(), R.layout.afragment, getBindLayoutInfo().getContainer(), false);
//
//    }
}
