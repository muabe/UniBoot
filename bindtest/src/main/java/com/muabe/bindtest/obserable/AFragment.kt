package com.muabe.bindtest.obserable

import android.view.View
import com.markjmind.uni.mapper.annotiation.Binder
import com.markjmind.uni.mapper.annotiation.OnClick
import com.markjmind.uni.mapper.annotiation.Progress
import com.markjmind.uni.progress.UniProgress
import com.markjmind.uni.thread.CancelAdapter
import com.markjmind.uni.thread.LoadEvent
import com.muabe.bindtest.R
import com.muabe.bindtest.databinding.AfragmentBinding


@Progress(mode=UniProgress.VIEW, res = R.layout.progress)
class AFragment : BaseFragment(){
    @Binder
    lateinit var binding : AfragmentBinding
    val observable : AObservable = AObservable()

    override fun onLoad(event: LoadEvent?, cancelAdapter: CancelAdapter?) {
        Thread.sleep(2000)
    }

    override fun onPost() {
        binding.model = observable
        observable.set("name", "hi")
    }

    @OnClick
    fun textView2(view: View){
        observable.name = "ddd"
    }
}