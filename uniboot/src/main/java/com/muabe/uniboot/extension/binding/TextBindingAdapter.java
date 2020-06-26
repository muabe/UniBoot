package com.muabe.uniboot.extension.binding;

import android.text.TextUtils;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TextBindingAdapter {
    @BindingAdapter(value = {"android:text", "text:dateformat"}, requireAll = false)
    public static void android_text(TextView textView, String text, String dateformat){
        if (!TextUtils.isEmpty(text)) {
            if(dateformat != null){
                textView.setText(new SimpleDateFormat(dateformat).format(new Date(Long.valueOf(text))));
            }else {
                textView.setText(text);
            }
        }else{
            textView.setText("");
        }
    }
}
