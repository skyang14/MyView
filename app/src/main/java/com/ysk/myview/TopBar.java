package com.ysk.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by yang.shikun on 2020/3/27 9:28
 */

public class TopBar extends ViewGroup {
    private static final String TAG = "TopBar";
    private Button leftButton, rightButton;
    private Rect mBound;
    private Paint mPaint;
    private String leftBtnText, rightBtnText, title;
    private int leftBtnTextColor, rightBtnTextColor, titleTextColor;
    private Drawable leftBtnBg, rightBtnBg;
    private float titleTextSize;
    private TopBarClickListener topBarClickListener;

    public void setTopBarClickListener(TopBarClickListener topBarClickListener) {
        this.topBarClickListener = topBarClickListener;
    }

    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(0xFFF59563);
        mPaint = new Paint();
        mPaint.setTextSize(50f);
        mPaint.setAntiAlias(true);
        mBound = new Rect();
        init(context, attrs);
        layout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.getTextBounds(title, 0, title.length(), mBound);
        mPaint.setTextSize(titleTextSize);
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
        mPaint.setColor(titleTextColor);
        canvas.drawText(title, getWidth() / 2 - mBound.width() / 2, getHeight() / 2 + mBound.height() / 2, mPaint);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopBar);
        leftBtnText = ta.getString(R.styleable.TopBar_leftButtonText);
        leftBtnTextColor = ta.getColor(R.styleable.TopBar_leftButtonTextColor, 0);
        leftBtnBg = ta.getDrawable(R.styleable.TopBar_leftButtonBackground);

        rightBtnText = ta.getString(R.styleable.TopBar_rightButtonText);
        rightBtnTextColor = ta.getColor(R.styleable.TopBar_rightButtonTextColor, 0);
        rightBtnBg = ta.getDrawable(R.styleable.TopBar_rightButtonBackground);

        title = ta.getString(R.styleable.TopBar_title);
        titleTextColor = ta.getColor(R.styleable.TopBar_titleTextColor, 0);
        titleTextSize = ta.getDimension(R.styleable.TopBar_titleTextSize, 20);
        ta.recycle();

        leftButton = new Button(context);
        leftButton.setText(leftBtnText);
        leftButton.setTextColor(leftBtnTextColor);
        leftButton.setBackground(leftBtnBg);

        rightButton = new Button(context);
        rightButton.setText(rightBtnText);
        rightButton.setTextColor(rightBtnTextColor);
        rightButton.setBackground(rightBtnBg);

    }

    private void layout() {
        addView(leftButton);
        addView(rightButton);
        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topBarClickListener != null)
                    topBarClickListener.leftClick();
            }
        });
        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topBarClickListener != null)
                    topBarClickListener.rightClick();
            }
        });
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //后进行布局 l:左left,t:上top,r:右right,b:底部bottom
        int cCount = getChildCount();
        Log.e(TAG, "onLayout:cCount " + cCount);
        int cWidth = 0;
        int CHeight = 0;
        int cl = 0, ct = 0, cr = 0, cb = 0;
        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            CHeight = childView.getMeasuredHeight();
            LayoutParams params = childView.getLayoutParams();
            MarginLayoutParams cParams = new ViewGroup.MarginLayoutParams(params);
            switch (i) {
                case 0:
                    cl = cParams.leftMargin;
                    ct = cParams.topMargin;
                    break;
                case 1:
                    cl = getWidth() - cWidth - cParams.rightMargin;
                    ct = cParams.topMargin;
                    break;
            }
            cr = cl + cWidth;
            cb = CHeight + ct;
            childView.layout(cl, ct, cr, cb);
        }

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //先测量
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        Log.e(TAG, "onMeasure:sizeHeight== " + sizeHeight);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = 0;
        int height = 0;
        int cCount = getChildCount();
        int cWidth = 0;
        int cHeight = 0;
        //计算wrap_content时的子控件宽高
        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            LayoutParams params = childView.getLayoutParams();
            MarginLayoutParams cParams = new ViewGroup.MarginLayoutParams(params);
            width += cWidth + cParams.leftMargin + cParams.rightMargin;
            height = Math.max(height, cHeight + cParams.topMargin + cParams.bottomMargin);

        }
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? sizeWidth : width,
                (heightMode == MeasureSpec.EXACTLY) ? sizeHeight : height);

    }

    public interface TopBarClickListener {
        void leftClick();

        void rightClick();
    }

}
