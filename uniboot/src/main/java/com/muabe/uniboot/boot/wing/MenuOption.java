package com.muabe.uniboot.boot.wing;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-11-04
 */

public interface MenuOption {
    int getWidth();
    void setEnable(boolean enable);
    void enableSideDrag(boolean enable);
    void setWidth(int width);
    void open();
    boolean isOpened();
    void close();
    boolean isStartedAnimation();
}
