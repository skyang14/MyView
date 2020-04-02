package com.ysk.myview.windmillView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**风车
 * Created by yang.shikun on 2020/4/2 10:50
 */

public class WindMillView extends View {
    private static final int DEFAULT_COLOR = Color.WHITE;
    private static final int DEFAULT_WIDTH = 1;//画笔宽度1dp
    private static final float LENGTH_1 = 5;//下面三角形高度5dp
    private static final float ALPHA = (float) (Math.PI / 6);//旋转角度
    private static final int DELAY = 30;

    private Paint mPaint;
    private Path mPath;
    private float mAngle = 0;//旋转角度 通过改变角度实现旋转动画
    private float length;
    private float length1;

    public WindMillView(Context context) {
        this(context, null);
    }

    public WindMillView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WindMillView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //初始化
    private void init() {
        mPaint = new Paint();
        mPath = new Path();
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mAngle = (float) (mAngle + 3 * Math.PI / 360);

        float centerX = getWidth() / 2;
        float centerY = getHeight() * 4 / 9.0f;
        mPaint.setStrokeWidth(dp2px(getContext(), DEFAULT_WIDTH));
        //绘制风车的身体
        canvas.drawLine(centerX, centerY, centerX, getHeight(), mPaint);
//        canvas.drawLine(centerX, centerY, centerX - getWidth() / 10, getHeight(), mPaint);
//        canvas.drawLine(centerX, centerY, centerX + getWidth() / 10, getHeight(), mPaint);

        //绘制叶片 叶片由两个三角形组成 length1是下面三角形的高 length为整个叶片长度
        length = (float) (dp2px(getContext(), LENGTH_1) * Math.sin(ALPHA)
                + getHeight() * 2 / 9.0f);
        length1 = dp2px(getContext(), LENGTH_1);

        float alpha = (float) (Math.PI / 2 - ALPHA + mAngle);
        drawWindMill(canvas, centerX, centerY, alpha);//画第一个叶片

        //在进行变换之前先调用save方法保存当前状态，之后通过restore方法恢复。
        canvas.save();
        canvas.rotate(120,centerX,centerY);//旋转画布
        drawWindMill(canvas, centerX, centerY, alpha);//画第二个叶片
        canvas.restore();

        canvas.save();
        canvas.rotate(240,centerX,centerY);//旋转画布
        drawWindMill(canvas, centerX, centerY, alpha);//画第三个叶片
        canvas.restore();

        mPath.reset();
        postInvalidateDelayed(DELAY);
    }

    private void drawWindMill(Canvas canvas, float centerX, float centerY, float alpha) {
        mPath.moveTo(centerX, centerY);
        mPath.lineTo((float) (centerX + length1 * Math.cos(alpha)),
                (float) (centerY - length1 * Math.sin(alpha)));

        mPath.lineTo((float) (centerX + length * Math.cos(Math.PI / 2 + mAngle)),
                (float) (centerY - length * Math.sin(Math.PI / 2 + mAngle)));

        mPath.lineTo((float) (centerX + length1 * Math.cos(alpha + 2 * ALPHA)),
                (float) (centerY - length1 * Math.sin(alpha + 2 * ALPHA)));
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }

    public int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}
