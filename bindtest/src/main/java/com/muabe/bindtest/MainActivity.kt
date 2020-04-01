package com.muabe.bindtest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.muabe.bindtest.databinding.ActivityMainBinding
import com.muabe.bindtest.obserable.AFragment
import com.muabe.bindtest.viewmodel.BFragment
import com.muabe.bindtest.viewmodel.BViewModel
import com.muabe.uniboot.boot.wing.MenuBoot

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MenuBoot.putContentView(this)
                .initHomeFragment(AFragment())
    }


}
