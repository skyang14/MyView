package com.ysk.myview.meteor;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.ysk.myview.BaseSurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by yang.shikun on 2020/4/7 15:20
 */

public class MeteorAnimSurface extends BaseSurfaceView {
    private static final String TAG = "MeteorAnimSurface";
    private static final int DEFAULT_METEOR_SPEED = 3;//每次刷新移动的距离
    private static final int DEFAULT_METEOR_STAR_SIZE = 8;
    private static final int DEFAULT_METEOR_STAR_NUM = 5;
    private int mHeight, mWidth;

    private ValueAnimator mMeteorAnimator;

    private int meteorColor;

    //流星
    private int meteorNum;
    private Paint mMeteorPaint;
    private int meteorSize = 80;
    private int meteorRadius;

    private Random mMeteorRandom;
    private List<Meteor> meteors;
    private boolean isPauseAnim = false;

    private Handler mHandler;

    private Path triangle = new Path();

    public MeteorAnimSurface(@NonNull Context context) {
        this(context, null);
    }

    public MeteorAnimSurface(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeteorAnimSurface(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void init() {
        setBackgroundColor(Color.TRANSPARENT);
        mMeteorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMeteorPaint.setStyle(Paint.Style.FILL);
        mMeteorRandom = new Random();
        mHandler = new Handler();
        meteorRadius = DEFAULT_METEOR_STAR_SIZE;
        meteorNum = DEFAULT_METEOR_STAR_NUM;
        meteorColor = 0x990772A1;
        meteors = new ArrayList<>();
    }

    public void setMeteorRadius(int meteorRadius) {
        this.meteorRadius = meteorRadius;
    }

    public void setMeteorNum(int meteorNum) {
        isPauseAnim = true;
        this.meteorNum = meteorNum;
        meteors.clear();
        for (int i = 0; i < meteorNum; i++) {
            Meteor meteor = new Meteor();
            float ran = 1 + Math.abs(mMeteorRandom.nextInt(20) / 10f);
            meteor.setmRandomPosition(mMeteorRandom.nextInt(meteorSize + mHeight) - mHeight - meteorSize);
            meteor.setSpeedRate(ran);
            meteors.add(meteor);
        }
        isPauseAnim = false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //拿到布局的高宽
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();

        for (int i = 0; i < meteorNum; i++) {
            Meteor meteor = new Meteor();
            float ran = 1 + Math.abs(mMeteorRandom.nextInt(20) / 10f);
            meteor.setmRandomPosition(mMeteorRandom.nextInt(meteorSize + mHeight) - mHeight - meteorSize);
            meteor.setSpeedRate(ran);
            meteors.add(meteor);
        }
    }

    public void setMeteorColor(@ColorInt int meteorColor) {
        this.meteorColor = meteorColor;
    }

    public void setMeteorColor(String meteorColor) {
        this.meteorColor = Color.parseColor(meteorColor);
    }

    private void drawMeteor(Canvas canvas) {
        if (isPauseAnim)
            return;
        mMeteorPaint.setColor(meteorColor);
        for (int i = 0; i < meteorNum; i++) {
            //meteorSize流星的宽度，最开始的时候流星不显示，需要移出去流星的长度距离
            float needTransX;
            float needTransY;
            meteors.get(i).setMeteorTran((int) (meteors.get(i).getMeteorTran() + DEFAULT_METEOR_SPEED * meteors.get(i).getSpeedRate()));

            //mHeight+metrorSize是为了让流星整个完全的滑出去
            if (meteors.get(i).getMeteorTran() >= mHeight + meteorSize) {

                //为了保证流星能够全屏分布，需要考虑左下角的情况
                meteors.get(i).setmRandomPosition(mMeteorRandom.nextInt(mWidth + mHeight) - mHeight);
                meteors.get(i).setMeteorTran(0);
            }
            needTransX = meteors.get(i).getMeteorTran() + meteors.get(i).getmRandomPosition();
            needTransY = meteors.get(i).getMeteorTran() - meteorSize;

            canvas.save();
            canvas.translate(needTransX, needTransY);

            canvas.drawCircle(meteorSize - meteorRadius, meteorSize - meteorRadius, meteorRadius,
                    mMeteorPaint);

            triangle.reset();
            triangle.lineTo(meteorSize - meteorRadius, (meteorSize - (meteorRadius * 2)));
            triangle.lineTo((meteorSize - (meteorRadius * 2)), meteorSize - meteorRadius);
            triangle.close();

            canvas.drawPath(triangle, mMeteorPaint);

            canvas.restore();
        }
    }

    /**
     * 实现星星的从右往左动画
     */
    private void starAnim() {
        if (null == mMeteorAnimator) {
            mMeteorAnimator = ValueAnimator.ofFloat(0, 100);
            mMeteorAnimator.setRepeatCount(ValueAnimator.INFINITE);//设置无限重复
            mMeteorAnimator.setRepeatMode(ValueAnimator.RESTART);//设置重复模式
            mMeteorAnimator.setInterpolator(new LinearInterpolator());
            mMeteorAnimator.setDuration(5000);
            mMeteorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    invalidate();
                }
            });
        }
        mMeteorAnimator.cancel();
        mMeteorAnimator.start();
    }

    public void beginAnim() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                starAnim();
            }
        }, 100);
    }


    /**
     * 暂停动画
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void pauseAnim() {
        if (mMeteorAnimator == null) return;
        mMeteorAnimator.pause();
    }

    /**
     * 继续动画
     */
    public void resumeAnim() {
        if (mMeteorAnimator == null) return;
        mHandler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                mMeteorAnimator.resume();
            }
        }, 500);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }

        if (mMeteorAnimator != null) {
            mMeteorAnimator.removeAllUpdateListeners();
        }
    }

    @Override
    protected void onInit() {
        init();
    }

    @Override
    protected void onReady() {
        startAnim();
        beginAnim();
    }

    @Override
    protected void onDataUpdate() {

    }

    @Override
    protected void onRefresh(Canvas canvas) {
        //绘制流星
        drawMeteor(canvas);
    }

    @Override
    protected void draw(Canvas canvas, Object data) {

    }

    @Override
    protected void onDrawRect(Canvas canvas, Object data, Rect rect) {

    }

    @Override
    protected boolean preventClear() {
        return false;
    }

    public class Meteor {
        private int meteorTran = 0;//流星水平移动的距离
        private float mRandomPosition = 0;//流星开始的随机位置
        private float speedRate = 1;//流星速率

        public int getMeteorTran() {
            return meteorTran;
        }

        public void setMeteorTran(int meteorTran) {
            this.meteorTran = meteorTran;
        }

        public float getmRandomPosition() {
            return mRandomPosition;
        }

        public void setmRandomPosition(float mRandomPosition) {
            this.mRandomPosition = mRandomPosition;
        }

        public float getSpeedRate() {
            return speedRate;
        }

        public void setSpeedRate(float speedRate) {
            this.speedRate = speedRate;
        }
    }
}
