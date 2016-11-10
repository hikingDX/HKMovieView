package com.hiking.mymovieview0929;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by hiking on 16/10/24.
 */
public class SmallView{
    private Paint mPaint;
    private boolean isShow;
//    private Seat[][] mSeats;


    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public SmallView() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        isShow = false;
    }

    public  void drawSelf(Canvas canvas){
        Frame frame = new Frame(0,0,250,200);
        canvas.drawRect(frame.toRect(),mPaint);
    }

}
