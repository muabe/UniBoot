package com.muabe.uniboot.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-07-11
 */
public interface HolderInterface {
    View getLayout();
    View getView(int id);
    TextView getTextView(int id);
    ImageView getImageView(int id);
    EditText getEditText(int id);
    Button getButton(int id);
    ViewGroup getViewGroup(int id);
    LinearLayout getLinearLayout(int id);
    FrameLayout getFrameLayout(int id);
    interface Finder{
        View findViewById(int id);
    }
}