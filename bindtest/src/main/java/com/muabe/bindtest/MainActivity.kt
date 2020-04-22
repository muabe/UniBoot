package com.muabe.bindtest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.muabe.bindtest.obserable.AFragment
import com.muabe.uniboot.boot.wing.MenuBoot

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MenuBoot.putContentView(this)
                .initHomeFragment(AFragment())
    }


}
