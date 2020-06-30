package com.muabe.uniboot.extension.binding;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TextBindingAdapter {
    @BindingAdapter(value = {"text:date", "text:dateformat"}, requireAll = false)
    public static void android_text(TextView textView, Long date, String dateformat){
        if(dateformat != null && date !=null){
            textView.setText(new SimpleDateFormat(dateformat).format(new Date(date)));
        }else{
            textView.setText(""+date);
        }
    }
}
