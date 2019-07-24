package com.muabe.uniboot.ui.grapher;

import android.graphics.Canvas;
/**
 * 
 * @author 오재웅
 * @email markjmind@gmail.com
 */
public interface IPieSkin {
	void init(PieLayout pieLayout);
	void draw(PieLayout pieLayout, Canvas canvas, float startAngle, float maxAngle, float startRadians, float radians, int arcIndex);
	
}
