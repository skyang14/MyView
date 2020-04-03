package com.ysk.myview.bezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**贝塞尔曲线
 * Created by yang.shikun on 2020/4/3 8:52
 */

public class BezierView extends View {
    // 开始点X,Y坐标
    private float startXPoint;
    private float startYPoint;
    // 结束点X,Y坐标
    private float endXPoint;
    private float endYPoint;
    // 控制点X,Y坐标
    private float conXPoint;
    private float conYPoint;
    private Path mPath;  // 绘制路径
    private Paint mPaint; // 画笔

    private Paint linePaint; // 用来画辅助线的画笔
    private Paint textPaint; // 用来写字的画笔
    /**在构造方法中初始化变量*/
    public BezierView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);

        /**初始化画辅助线的画笔*/
        linePaint= new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(3);
        linePaint.setColor(Color.GRAY);

        /**初始化写字的画笔*/
        textPaint= new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setColor(Color.YELLOW);
        textPaint.setTextSize(20);
    }

    /**重写onSizeChanged*/
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 开始点的坐标
        startXPoint = w / 4;
        startYPoint = h / 2;
        // 结束点的位置
        endXPoint = w * 3 / 4;
        endYPoint = h / 2;
        // 控制点坐标
        conXPoint = w / 2;
        conYPoint = h / 2 - 300;

        mPath = new Path();
    }

    /**重写onDraw*/
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset(); // 初始化Path
        mPath.moveTo(startXPoint,startYPoint);//移动到开始坐标点开始绘制
        mPath.quadTo(conXPoint,conYPoint,endXPoint,endYPoint); //这段代码就是绘制二阶贝塞尔曲线
        /**画辅助线*/
        canvas.drawLine(startXPoint,startYPoint,conXPoint,conYPoint,linePaint);
        canvas.drawLine(conXPoint,conYPoint,endXPoint,endYPoint,linePaint);
        canvas.drawPoint(startXPoint,startYPoint,linePaint);
        canvas.drawText("p0",startXPoint - 30,startYPoint,textPaint);
        canvas.drawPoint(endXPoint,endYPoint,linePaint);
        canvas.drawText("p1",endXPoint + 10,endYPoint,textPaint);
        canvas.drawPoint(conXPoint,conYPoint,linePaint);
        canvas.drawText("p2",conXPoint,conYPoint -20,textPaint);
        //画出贝塞尔曲线
        canvas.drawPath(mPath,mPaint);
    }
}