package com.muabe.bindtest.viewmodel

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.markjmind.uni.UniFragment
import com.markjmind.uni.mapper.annotiation.Layout
import com.markjmind.uni.thread.CancelAdapter
import com.markjmind.uni.thread.LoadEvent
import com.muabe.bindtest.R
import com.muabe.bindtest.databinding.AfragmentBinding

@Layout(R.layout.bfragment)
class BFragment : UniFragment(){
    lateinit var binding : AfragmentBinding
    lateinit var viewModel : BViewModel

    override fun onBind() {
        binding = AfragmentBinding.inflate(bindLayoutInfo.layoutInflater, bindLayoutInfo.container, false)
    }

//    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        super.onCreateView(inflater, container, savedInstanceState)
//        viewModel = ViewModelProvider().get(BViewModel::class.java)
//        viewModel.name.value = "ddddd"
//        viewModel.age.value = 123123213
//        return binding.root
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
//        viewModel = ViewModelProvider(this).get(BViewModel::class.java)

        viewModel.name.value = "ddddd"
        viewModel.age.value = 123123213
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onPre() {

    }

    override fun onLoad(event: LoadEvent?, cancelAdapter: CancelAdapter?) {

    }

    override fun onPost() {

    }

}

class MyViewModelFactory (private var application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Application::class.java).newInstance(application)
    }
}