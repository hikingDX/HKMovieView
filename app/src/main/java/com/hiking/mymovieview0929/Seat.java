package com.hiking.mymovieview0929;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Created by Administrator on 2016/9/30.
 */
public class Seat {
    //座位的3种状态
    public static final int SEAT_NORMAL = -1;
    public static final int SEAT_CHECKED = 1;
    public static final int SEAT_LOCK = 2;
    private int seatStatus = SEAT_NORMAL;//座位状态

    private int row;//行
    private int colum;//列

    //类变量
    private static Bitmap normalBitmap;//未选中的图片
    private static Bitmap checkedBitmap;//选中的图片
    private static Bitmap lockBitmap;//锁定的图片
    private static float seat_width;//边长
    private static float seat_left; //座位左边距
    private static float seat_top;  //座位上边距
    private static float movie_left; //电影左边距
    private static float movie_top;  //电影上边距

    private static float margin_left;
    private static float margin_top;


    //小图使用
    private static float small_seat_width;//边长
    private static float small_seat_left; //座位左边距
    private static float small_seat_top;  //座位上边距
    private static float small_movie_left; //电影左边距
    private static float small_movie_top;  //电影上边距

    public static Bitmap getNormalBitmap() {
        return normalBitmap;
    }

    public static void setNormalBitmap(Bitmap normalBitmap) {
        Seat.normalBitmap = normalBitmap;
    }

    public static Bitmap getCheckedBitmap() {
        return checkedBitmap;
    }

    public static void setCheckedBitmap(Bitmap checkedBitmap) {
        Seat.checkedBitmap = checkedBitmap;
    }

    public static Bitmap getLockBitmap() {
        return lockBitmap;
    }

    public static void setLockBitmap(Bitmap lockBitmap) {
        Seat.lockBitmap = lockBitmap;
    }

    public static float getSeat_width() {
        return seat_width;
    }

    public static void setSeat_width(float seat_width) {
        Seat.seat_width = seat_width;
    }

    public static float getSeat_left() {
        return seat_left;
    }

    public static void setSeat_left(float seat_left) {
        Seat.seat_left = seat_left;
    }

    public static float getSeat_top() {
        return seat_top;
    }

    public static void setSeat_top(float seat_top) {
        Seat.seat_top = seat_top;
    }

    public static float getMovie_left() {
        return movie_left;
    }

    public static void setMovie_left(float movie_left) {
        Seat.movie_left = movie_left;
    }

    public static float getMovie_top() {
        return movie_top;
    }

    public static void setMovie_top(float movie_top) {
        Seat.movie_top = movie_top;
    }

    /**
     * 类变量初始化方法
     *
     * @param normalBitmap
     * @param checkedBitmap
     * @param lockBitmap
     * @param seat_width
     * @param seat_left
     * @param seat_top
     * @param movie_left
     * @param movie_top
     */
    public static void init(Bitmap normalBitmap, Bitmap checkedBitmap, Bitmap lockBitmap, float seat_width, float seat_left, float seat_top, float
            movie_left,
                            float movie_top) {
        Seat.normalBitmap = normalBitmap;
        Seat.checkedBitmap = checkedBitmap;
        Seat.lockBitmap = lockBitmap;
        Seat.seat_width = seat_width;
        Seat.seat_left = seat_left;
        Seat.seat_top = seat_top;
        Seat.movie_left = movie_left;
        Seat.movie_top = movie_top;
    }


    public Seat(int row, int colum) {
        this.row = row;
        this.colum = colum;
        this.seatStatus = SEAT_NORMAL;
    }

    /**
     * 通过座位数组绘制座位
     *
     * @param row
     * @param colums
     * @param canvas
     * @param mSeats
     */
    public static void drawSeats(int row, int colums, Canvas canvas, Seat[][] mSeats, boolean isBig) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < colums; j++) {
                if (isBig) {
                    mSeats[i][j].drawSelf(canvas);
                } else {
                    mSeats[i][j].drawSamllSelf(canvas);
                }
            }
        }
    }


    /**
     * 绘制缩略图座位
     *
     * @param canvas
     */
    public void drawSamllSelf(Canvas canvas) {
        //计算座位的x,y
        int x = (int) (margin_left + small_movie_left + (small_seat_width + small_seat_left) * (colum - 1));
        int y = (int) (margin_top + small_movie_top + (small_seat_width + small_seat_top) * (row - 1));
        //确定图片位置
        Frame frame = new Frame(x, y, small_seat_width, small_seat_width);
        //根据图片状态设置座位的图片
        switch (seatStatus) {
            case SEAT_NORMAL: {
                canvas.drawBitmap(normalBitmap, null, frame.toRect(), null);
                break;
            }
            case SEAT_CHECKED: {
                canvas.drawBitmap(checkedBitmap, null, frame.toRect(), null);
                break;
            }
            case SEAT_LOCK: {
                canvas.drawBitmap(lockBitmap, null, frame.toRect(), null);
                break;
            }
            default:
                break;
        }
    }

    /**
     * 绘制座位
     *
     * @param canvas
     */
    public void drawSelf(Canvas canvas) {
        //计算座位的x,y
        int x = (int) (margin_left + movie_left + (seat_width + seat_left) * (colum - 1));
        int y = (int) (margin_top + movie_top + (seat_width + seat_top) * (row - 1));
        //确定图片位置
        Frame frame = new Frame(x, y, seat_width, seat_width);
        //根据图片状态设置座位的图片
        switch (seatStatus) {
            case SEAT_NORMAL: {
                canvas.drawBitmap(normalBitmap, null, frame.toRect(), null);
                break;
            }
            case SEAT_CHECKED: {
                canvas.drawBitmap(checkedBitmap, null, frame.toRect(), null);
                break;
            }
            case SEAT_LOCK: {
                canvas.drawBitmap(lockBitmap, null, frame.toRect(), null);
                break;
            }
            default:
                break;
        }
    }

    /**
     * 缩放方法 类方法
     *
     * @param scaleFactor 缩放比例
     */
    public static void scale(float scaleFactor) {
        seat_width *= scaleFactor;
        movie_top *= scaleFactor;
        movie_left *= scaleFactor;
        seat_left *= scaleFactor;
        seat_top *= scaleFactor;
        Seat.scaleFactor = scaleFactor;
    }

    /**
     * 移动方法
     *
     * @param deltaX
     * @param deltaY
     * @param movieWidth
     * @param movieHeight
     * @param row
     * @param colums
     */
    static float scaleFactor = (float) 0.10;

    public static void move(float deltaX, float deltaY, float movieWidth, float movieHeight, int row, int colums) {
        //Top不能大于20，left不能大于20
        margin_left += deltaX;
        margin_top += deltaY;
        //        margin_left += deltaY / 4;
        //        margin_top += deltaX / 4;
        //        Log.i("2", "前----left:" + movie_left + "top:" + movie_top);


        //        if (movie_top > 21*scaleFactor || movieHeight*scaleFactor > row * (seat_width + seat_top)) {
        //            movie_top = 21*scaleFactor;
        //        } else if (movie_top < movieHeight*scaleFactor - row * (seat_width + seat_top)) {
        //            movie_top = movieHeight*scaleFactor - row * (seat_width + seat_top);
        //        }
        //
        //        if (movie_left > 21*scaleFactor || movieWidth*scaleFactor > colums * (seat_width + seat_left) + 21) {
        //            movie_left = 21*scaleFactor;
        //        } else if (movie_left < movieWidth*scaleFactor - colums * (seat_width + seat_left) + 21) {
        //            movie_left = movieWidth*scaleFactor - colums * (seat_width + seat_left);
        //        }
    }


    public int getSeatStatus() {
        return seatStatus;
    }

    public void setSeatStatus(int seatStatus) {
        this.seatStatus = seatStatus;
    }


    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColum() {
        return colum;
    }

    public void setColum(int colum) {
        this.colum = colum;

    }

    /**
     * @return
     */
    public static RectF getSeatsRect(int row, int colum) {
        return new RectF(margin_left, margin_top, margin_left + (seat_width + seat_left) * row + movie_left, margin_top + (seat_width + seat_top) *
                colum + movie_top);
    }
}
