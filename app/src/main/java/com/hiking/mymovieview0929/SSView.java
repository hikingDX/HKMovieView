package com.hiking.mymovieview0929;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;

/**
 * Created by Administrator on 2016/9/29.
 */
public class SSView extends View implements ScaleGestureDetector.OnScaleGestureListener,ViewTreeObserver.OnGlobalLayoutListener {
    private Context mContext;
    private float seat_width = 100;
    private float Top = 20;
    private float Left = 20;
    private float margin_top = 10;
    private float margin_left = 10;

    private Seat[][] mSeats;
    private int row;
    private int colums;

    private SmallView smallView;//缩略图
    private ScaleGestureDetector mScaleDetector;


    //接口
    private OnSeatClickListener mSeatClickListener = null;
    /**
     * 缩放
     */
    private float mCurrentScale;//当前缩放比例
    private float mMaxScale;//最大缩放比例
    private float mMidScale;//
    //------------------自由移动-----------------
    /**
     * 记录上一次多点触控的数量
     */
    private int mLastPointerCount;

    private float mLastX;
    private float mLastY;

    private int mTouchSlop;
    private boolean mIsCanDrag;

    private boolean isCheckLeftAndRight;
    private boolean isCheckBottomAndTop;
    private int mContent_width;
    private int mContent_height;

    @Override
    public void onGlobalLayout() {
        mContent_width = getMeasuredWidth();
        mContent_height = getMeasuredHeight();
        init();
    }

    public abstract interface OnSeatClickListener {
        public abstract boolean a(int row, int colums, boolean isChecked);
    }

    public void setOnSeatClickListener(OnSeatClickListener seatClickListener) {
        this.mSeatClickListener = seatClickListener;
    }

    public SSView(Context context) {
        this(context, null, 0);
    }

    public SSView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SSView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
//        init();
        mScaleDetector = new ScaleGestureDetector(context, this);
    }


    private float lastWidth = seat_width;

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scaleFactor = detector.getScaleFactor();

        //缩放范围控制
        //        if (mCurrentScale < mMaxScale && mCurrentScale > 1.0f) {
        //            if (mCurrentScale * scaleFactor < 1.0f) {
        //
        //            }
        //        }
        lastWidth *= scaleFactor;
        if (50 < lastWidth && lastWidth < 200) {
            Seat.scale(scaleFactor);
            checkBorderAndCenterWhenScale();
            invalidate();
        }
        return true;
    }

    public void checkBorderAndCenterWhenScale() {
        RectF rect = Seat.getSeatsRect(row, colums);
        int width = getWidth();
        int height = getHeight();
        float deltaX = 0;
        float deltaY = 0;
        //缩放时进行边界检测，防止白边
        if (rect.width() >= width){
            if (rect.left>0){
                deltaX = -rect.left;
            }
            if (rect.right<width){
                deltaX = width - rect.right;
            }
        }
        if (rect.height() >= height){
            if (rect.top>0){
                deltaY = -rect.top;
            }
            if (rect.bottom<height){
                deltaY = height - rect.bottom;
            }
        }
        Seat.move(deltaX,deltaY,0,0,0,0);
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    /**
     * 是否是move
     *
     * @param dx
     * @param dy
     * @return
     */
    private boolean isMoveAction(float dx, float dy) {

        return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        float x = 0;
        float y = 0;
        //拿到多点触控的数量
        int pointerCount = event.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }
        x /= pointerCount;
        y /= pointerCount;
        if (mLastPointerCount != pointerCount) {
            mIsCanDrag = false;
            mLastX = x;
            mLastY = y;
        }
        mLastPointerCount = pointerCount;

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - mLastX;
                float dy = y - mLastY;

                if (!mIsCanDrag) {
                    mIsCanDrag = isMoveAction(dx, dy);
                }

                if (mIsCanDrag) {
                    Log.e("1", "移动ing");
                    RectF rectF = Seat.getSeatsRect(row, colums);
                    isCheckLeftAndRight = isCheckBottomAndTop = true;
                    //如果宽度小于控件宽度,不允许横向移动
                    if (rectF.width() < getWidth()) {
                        Log.e("1",rectF.width()+" "+getWidth());
                        isCheckLeftAndRight = false;
                        dx = 0;
                    }
                    //如果高度小于控件高度,不允许纵向移动
                    if (rectF.height() < getHeight()) {
                        isCheckBottomAndTop = false;
                        dy = 0;
                    }
                    Seat.move(dx, dy, 0, 0, 0, 0);
                    checkBorderAndCenterWhenScale();
                    invalidate();
                }
                mLastX = x;
                mLastY = y;


                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLastPointerCount = 0;
                break;
        }
//        if (event.getAction() == MotionEvent.ACTION_UP) {
//            smallView.setShow(false);
//            postInvalidateDelayed(2000);
//        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            smallView.setShow(true);
//            invalidate();
//        }

        return true;
    }

    /**
     * 通过坐标值找到对应的座位
     *
     * @param x
     * @param y
     */
    private void findSeat(float x, float y) {

        int checkColums = (int) ((x - Seat.getMovie_left()) / (Seat.getSeat_width() + Seat.getSeat_left())) + 1;
        int checkRow = (int) ((y - Seat.getMovie_top()) / (Seat.getSeat_width() + Seat.getSeat_top())) + 1;
        Seat seat = mSeats[checkRow - 1][checkColums - 1];
        int seatStatus = seat.getSeatStatus();
        if ((checkRow > row || checkColums > colums) && seatStatus != Seat.SEAT_LOCK) {
            return;
        }
        seat.setSeatStatus(-seatStatus);
        mSeatClickListener.a(checkRow, checkColums, true);

        //        smallView.setShow(true);
        postInvalidateDelayed(2000);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }
    /**
     * 初始化方法
     */
    public void init() {
        mTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        initSeats();
        /**
         * 缩放范围初始化
         */
        mCurrentScale = 1.0f;
        mMaxScale = 4.0f;
        mMidScale = 2.0f;


    }
    public void initSeats(){
        row = 10;
        colums = 10;
        //1.获取控件宽高
        int content_width = mContent_width;
        int content_height = mContent_height;
        //2.设置内边距
        int padding_left = content_width/10;
        int padding_top = content_height/5;
        //3.根据row column 计算座位的间隙 及宽
        seat_width = (content_width - 2*padding_left)/row /4*3;
        margin_left = seat_width / 3;

        mSeats = new Seat[row][colums];

        smallView = new SmallView();

        Bitmap normalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.seat_normal);
        Bitmap checkedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.seat_checked);
        Bitmap lockBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.seat_lock);
        Log.e("l1",content_height+" "+content_width+" "+padding_left+" "+padding_top);
        Seat.init(normalBitmap, checkedBitmap, lockBitmap, seat_width, margin_left, margin_left, padding_left, padding_top);

        for (int i = 1; i <= row; i++) {
            for (int j = 1; j <= colums; j++) {
                Seat seat = new Seat(i, j);
                mSeats[i - 1][j - 1] = seat;
            }
        }

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rect = Seat.getSeatsRect(row, colums);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawRect(rect, paint);
        Seat.drawSeats(row, colums, canvas, mSeats, true);
        if (smallView.isShow()) {
            smallView.drawSelf(canvas);
        }
    }
}




