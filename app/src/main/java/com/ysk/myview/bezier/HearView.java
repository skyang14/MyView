package com.ysk.myview.bezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**手势点击屏幕，动态心形
 * Created by yang.shikun on 2020/4/3 9:35
 */

public class HearView extends AdvancePathView {

    public HearView(Context context) {
        super(context);
    }

    public HearView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HearView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected void onDraw(Canvas canvas) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        pointFStart = new PointF();
        pointFFirst = new PointF();
        pointFSecond = new PointF();
        pointFEnd = new PointF();

        pointFStart.x = getMeasuredWidth() / 2-bitmap.getWidth()/2;
        pointFStart.y = getMeasuredHeight() - bitmap.getHeight();

        pointFEnd.y = 0;
        pointFEnd.x = random.nextFloat()*getMeasuredWidth();

        pointFFirst.x = random.nextFloat()*getMeasuredWidth();
        pointFSecond.x = getMeasuredWidth() - pointFFirst.x;
        pointFSecond.y = random.nextFloat()*getMeasuredHeight() / 2+getMeasuredHeight()/2;
        pointFFirst.y = random.nextFloat()*getMeasuredHeight()  / 2;
        Log.e("TAG","出发了");
        addHeart();
        return true;
    }

    @Override
    protected void initPoint() {

    }


}
