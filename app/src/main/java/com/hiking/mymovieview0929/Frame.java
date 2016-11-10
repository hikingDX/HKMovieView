package com.hiking.mymovieview0929;

import android.graphics.RectF;

/**
 * Created by Administrator on 2016/9/30.
 */
public class Frame {
    private float x;
    private float y;
    private float width;
    private float heigth;

    public Frame(float x, float y, float width, float heigth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.heigth = heigth;
    }

    public RectF toRect() {
        float left = x;
        float top = y;
        float right = left + width;
        float bottom = top + heigth;
        return new RectF(left,top,right,bottom);
    }
}
