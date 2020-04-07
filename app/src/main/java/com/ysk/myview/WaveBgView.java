package com.ysk.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;

import java.util.Random;

/**动态波浪View
 * Created by yang.shikun on 2020/4/1 9:48
 */

public class WaveBgView extends BaseSurfaceView {
    private Path path1, path2, path3;
    private float x1, x2, x3;
    private Random random;
    private LinearGradient gradient1, gradient2;

    public WaveBgView(Context context) {
        super(context);
    }

    public WaveBgView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WaveBgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onInit() {
        path1 = new Path();
        path2 = new Path();
        path3 = new Path();
        random = new Random();
        x1 = (float) (random.nextFloat() * 2 * Math.PI);
        x2 = (float) (random.nextFloat() * 2 * Math.PI);
        x3 = (float) (random.nextFloat() * 2 * Math.PI);
    }

    @Override
    protected void onReady() {
        gradient1 = new LinearGradient(0, 0, 0, (float) (getMeasuredHeight() * 1.4), new int[]{Color.CYAN, 0xAA0000FF, Color.TRANSPARENT}, new float[]{0.1f, 0.2f, 0.9f}, Shader.TileMode.CLAMP);
        gradient2 = new LinearGradient(0, 0, 0, (float) (getMeasuredHeight()), new int[]{Color.TRANSPARENT, 0xAA0000FF, Color.CYAN}, null, Shader.TileMode.CLAMP);
        startAnim();
    }

    @Override
    protected void onDataUpdate() {
        path1.reset();
        path2.reset();
        path3.reset();
        x1 += 0.01;
        x2 += 0.015;
        x3 -= 0.007;
        path1.moveTo(0, getMeasuredHeight());
        path2.moveTo(0, getMeasuredHeight());
        for (int i = 0; i < getMeasuredWidth(); i++) {
            path1.lineTo(i, (float) (Math.sin((float) i / getMeasuredWidth() / 2 * Math.PI + x1) * getMeasuredHeight() / 4 + getMeasuredHeight() / 1.5));
            path2.lineTo(i, (float) (Math.sin((float) i / getMeasuredWidth() / 2 * Math.PI + x2) * getMeasuredHeight() / 2.5 + getMeasuredHeight() / 1.5));
            path3.lineTo(i, (float) (Math.sin((float) i / getMeasuredWidth() / 2 * Math.PI + x3) * getMeasuredHeight() / 3 + getMeasuredHeight() / 2.5));
        }
        path1.lineTo(getMeasuredWidth(), getMeasuredHeight());
        path1.close();
        path2.lineTo(getMeasuredWidth(), getMeasuredHeight());
        path2.close();
        path3.lineTo(getMeasuredWidth(), 0);
        path3.close();
    }

    @Override
    protected void onRefresh(Canvas canvas) {
        mPaint.setShader(gradient1);
        canvas.drawPath(path1, mPaint);
        canvas.drawPath(path2, mPaint);
        mPaint.setShader(gradient2);
        canvas.drawPath(path3, mPaint);
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
}

