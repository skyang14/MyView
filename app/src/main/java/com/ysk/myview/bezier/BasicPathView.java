package com.ysk.myview.bezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/** 基础贝塞尔曲线示例
 * Created by yang.shikun on 2020/4/3 9:32
 */

public class BasicPathView extends LinearLayout {
    private static final String TAG = "BasicPathView";
    private Paint mPaint;
    private Path mPath;
    private float startX, startY, endX, endY, touchX, touchY;

    private boolean isFill;
    private PointF pointFStart,pointFFirst,pointFSecond,pointFEnd;

    public BasicPathView(Context context) {
        super(context);
        initView();
    }

    public BasicPathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BasicPathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        setBackgroundColor(Color.WHITE);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPath = new Path();
        pointFStart = new PointF();
        pointFFirst = new PointF();
        pointFSecond = new PointF();
        pointFEnd = new PointF();
    }

    protected void initPoint() {
        //三阶曲线的起始点，终点
        pointFStart.x = getMeasuredWidth() / 2;
        pointFStart.y = getMeasuredHeight() - 10;

        pointFEnd.y = 10;
        pointFEnd.x = getMeasuredWidth() / 2;
        //三阶曲线两个控制点
        pointFFirst.x = 600;
        pointFSecond.x = getMeasuredWidth() - 600;
        pointFSecond.y = getMeasuredHeight() / 4;
        pointFFirst.y = getMeasuredHeight() * 3 / 4;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        startX = getMeasuredWidth() / 5;
        startY = endY = getMeasuredHeight() / 2;
        endX = getMeasuredWidth() * 4 / 5;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPoint();
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(startX, startY, 10, mPaint);
        canvas.drawCircle(endX, endY, 10, mPaint);
        canvas.drawCircle(touchX, touchY, 10, mPaint);
        canvas.drawCircle(pointFFirst.x,pointFFirst.y,10,mPaint);
        canvas.drawCircle(pointFSecond.x,pointFSecond.y,10,mPaint);
        mPaint.setTextSize(20);
        canvas.drawText("这是一阶贝塞尔曲线,就一条直线,没什么好说的", startX, startY / 4, mPaint);

        mPaint.setColor(Color.BLUE);
        if (isFill) {
            mPaint.setStyle(Paint.Style.FILL);
        } else {
            mPaint.setStyle(Paint.Style.STROKE);
        }
        //绘制一阶曲线
        mPath.moveTo(startX, startY / 3);//path移动到起点
        mPath.lineTo(endX, endY / 3);//确定直线的终点
        canvas.drawPath(mPath, mPaint);//调用canvas绘制贝塞尔曲线
        mPath.reset();

        //绘制二阶曲线
        mPath.moveTo(startX, startY);
        mPath.quadTo(touchX, touchY, endX, endY);
        canvas.drawPath(mPath, mPaint);
        mPath.reset();

        //绘制三阶曲线,就是一个s型
        mPath.moveTo(pointFStart.x, pointFStart.y);
        mPath.cubicTo(pointFFirst.x, pointFFirst.y, pointFSecond.x, pointFSecond.y, pointFEnd.x, pointFEnd.y);
        canvas.drawPath(mPath, mPaint);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchX = event.getX();
        touchY = event.getY();
        //每次触摸触发onDraw
        postInvalidate();
        return true;
    }

    private int index;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final Button button = new Button(getContext());
        button.setText("填充");

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index % 2 == 0) {
                    isFill = true;
                    button.setText("不填充");
                } else {
                    isFill = false;
                    button.setText("填充");

                }
                index++;
            }
        });

        addView(button);
    }
}
