package com.ysk.myview;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Created by yang.shikun on 2020/3/26 16:46
 */

public class CustomView extends View {
    private static final String TAG = "CustomView";
    /**
     * the width of current view.
     */
    protected int mViewWidth;

    /**
     * the height of current view.
     */
    protected int mViewHeight;

    /**
     * default Paint.
     */
    protected Paint mDeafultPaint = new Paint();

    /**
     * default TextPaint
     */
    protected TextPaint mDefaultTextPaint = new TextPaint();

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        Log.e(TAG, "onMeasure:widthSize= "+widthSize );
        Log.e(TAG, "onMeasure:heightSize= "+heightSize );
        if (widthMode==MeasureSpec.AT_MOST){
            Log.e(TAG, "onMeasure: AT_MOST");
        }
        if (widthMode==MeasureSpec.EXACTLY){
            Log.e(TAG, "onMeasure: EXACTLY");
        }
        if (widthMode==MeasureSpec.UNSPECIFIED){
            Log.e(TAG, "onMeasure: UNSPECIFIED");
        }
        if (heightMode==MeasureSpec.AT_MOST){
            Log.e(TAG, "onMeasure: AT_MOST");
        }
        if (heightMode==MeasureSpec.EXACTLY){
            Log.e(TAG, "onMeasure: EXACTLY");
        }
        if (heightMode==MeasureSpec.UNSPECIFIED){
            Log.e(TAG, "onMeasure: UNSPECIFIED");
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
    }
}
