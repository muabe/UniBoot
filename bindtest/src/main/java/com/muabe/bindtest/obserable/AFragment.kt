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
import com.muabe.bindtest.vo.cmm.vo.CodeVO

@Progress(mode = UniProgress.VIEW, res = R.layout.progress)
class AFragment : BaseFragment(){
    @Binder
    lateinit var binding : AfragmentBinding
    val map : ModelNamed = ModelNamed()
    val observable : CodeVO = CodeVO()

    override fun onPre() {

//        observable.set("name", "hi")
        observable.codeName = "dd"
        binding.model = observable

        map.data.put("age", "sldkjfsldkjf")
        binding.map = map

        map.uservo.get("user")?.name = "33333"
    }

    override fun onLoad(event: LoadEvent?, cancelAdapter: CancelAdapter?) {
        Thread.sleep(2000)
    }

    override fun onPost() {
        observable.codeName = "2222"
        map.data.put("age", "ddddddddddddddddddd")
//        map.intData.put(R.id.textView2, "ssss");
        map.uservo.get("user")?.name = "5555555"
//        Toast.makeText(getContext(), observable.name, Toast.LENGTH_SHORT).show();
    }

    @OnClick
    fun textView2(view: View){
//        observable.name = "ddd"
    }

}