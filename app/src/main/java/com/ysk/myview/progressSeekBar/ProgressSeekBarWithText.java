package com.ysk.myview.progressSeekBar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yang.shikun on 2020/3/30 11:56
 */

public class ProgressSeekBarWithText extends View {
    private static final String TAG = "ProgressSeekBarWithText";
    //灰色背景线段的画笔
    private Paint bgPaint;

    //实际进度绿色线段的画笔
    private Paint progressPaint;

    //圆点指示器的画笔
    private Paint circlePaint;

    //圆点指示器的半径
    private int mCircleRadius = dip2px(getContext(),12);

    //进度条的最大宽度
    private float maxProgress;

    //进度条当前的宽度
    private float currentProgress;

    //当前View的宽度
    private int width;

    //当前View的高度
    private int height;

    //距离左边的内边距
    private int paddingLeft;

    //距离右边的内边距
    private int paddingRight;
    //文本画笔
    private Paint mTextPaint;

    //文本的大小
    private float mTextSize = dip2px(getContext(),14);

    /**
     * 评价文字集合,如体验很好，体验一般，体验不错，体验很好4个等级
     * 之所以使用集合，不使用String数组是想让它保持可扩展性，我们后面课题提供一个接口，让用户自己决定要设置多少个评价等级
     */
    private List<String> evaluates = new ArrayList<>();

    //文本的宽度
    private int textWidth;

    //文本的高度
    private int textHeight;

    //文本的透明度
    private float textAlpha;

    //文本缩放后的最小的大小
    private float textSizeRadio = 0.8f;

    //一段评价区间的大小
    private float distance;

    //一半评价区间的大小
    private float half = distance / 2;


    public ProgressSeekBarWithText(Context context) {
        super(context);
    }

    public ProgressSeekBarWithText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressSeekBarWithText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();//初始化画笔
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        //进度条背景画笔
        bgPaint = new Paint();
        bgPaint.setColor(Color.parseColor("#F0F0F0"));//灰色
        bgPaint.setStyle(Paint.Style.FILL_AND_STROKE);//填充且描边
        bgPaint.setAntiAlias(true);//抗锯齿
        bgPaint.setStrokeCap(Paint.Cap.ROUND);//线冒的头是圆的
        bgPaint.setStrokeWidth(dip2px(getContext(),3));//大小为3dp转px

        //设置进度画笔
        progressPaint = new Paint();
        progressPaint.setColor(Color.parseColor("#0DE6C2"));//绿色
        progressPaint.setStyle(Paint.Style.FILL_AND_STROKE);//填充且描边
        progressPaint.setAntiAlias(true);//抗锯齿
        progressPaint.setStrokeCap(Paint.Cap.ROUND);//线冒的头圆原的
        progressPaint.setStrokeWidth(dip2px(getContext(),3));//大小为3dp转px

        //圆点指示器
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);//设置抗锯齿
        circlePaint.setColor(Color.parseColor("#fafafa"));//颜色
        circlePaint.setShadowLayer(dip2px(getContext(),2), 0, 0, Color.parseColor("#38000000"));//外阴影颜色
        circlePaint.setStyle(Paint.Style.FILL);//填充


        //初始化文本画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setStrokeWidth(3);
        mTextPaint.setColor(Color.parseColor("#9a9a9a"));
        mTextPaint.setTextSize(mTextSize);
    }

    //重新计算控件的宽，高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        //获取文本高度
        textHeight = (int) (mTextPaint.descent() - mTextPaint.ascent());
        //最小部分为你那个相等的部分，下部分为圆点指示器半径*1.8+文本高度，因为进度条永远在中心，所以上面高度也要一样，*2
        int minHeight = (int) ((mCircleRadius * 1.8 + textHeight) * 2);
        int height = resolveSize(minHeight, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    //返回高度值,作用和resolveSize方法一样
    private int measureHeight(int heightMeasureSpec) {
        int result;
        int mode = MeasureSpec.getMode(heightMeasureSpec);//获取高度类型
        int size = MeasureSpec.getSize(heightMeasureSpec);//获取高度数值
        //制定的最小高度标准
        int minHeight = mCircleRadius * 2 + (dip2px(getContext(),2) * 2);
        //如果用户设定了指定大小
        if (mode == MeasureSpec.EXACTLY) {
            /**
             * 虽然用户已经指定了大小，但是万一指定的大小小于圆点指示器的高度，
             * 还是会出现显示不全的情况，所以还要进行判断
             */
            if (size < minHeight) {
                result = minHeight;
            } else {
                result = size;
            }
        }
        //如果用户没有设定明确的值
        else {
            //设定高度为圆点指示器的直径
            result = minHeight;
        }
        return result;
    }

    //初始化几个距离参数
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);


        width = getWidth();//view的宽度
        height = getHeight();//view的高度

        //让左边距至少为半个圆点指示器的距离
        paddingLeft = getPaddingLeft();//距离左边的距离
        if (getPaddingLeft() < mCircleRadius) {
            paddingLeft = mCircleRadius;
        }
        //让右边距至少为半个圆点指示器的距离
        paddingRight = getPaddingRight();//距离右边的距离
        if (getPaddingRight() < mCircleRadius) {
            paddingRight = mCircleRadius;
        }

        //暂时在这里给列表设置三个数据
//        evaluates.add("效果很差");
//        evaluates.add("效果还行");
//        evaluates.add("效果很棒");
        //定义一个标准的文本宽度
        textWidth = (int) mTextPaint.measureText("效果很差");


        //让左边距至少为文字宽度的一半
        if (paddingLeft < textWidth / 2) {
            paddingLeft = textWidth / 2;
        }

        //让右边距至少为文字宽度的一半
        if (paddingRight < textWidth / 2) {
            paddingRight = textWidth / 2;
        }

        //如果当前进度小于左边距
        setCurrentProgress();
        //最大进度长度等于View的宽度-(左边的内边距+右边的内边距)
        maxProgress = width - paddingLeft - paddingRight;

        //规定评价进度条至少要有两个元素值
        if (evaluates.size() < 2) {
            Toast.makeText(getContext(), "数据设置出错", Toast.LENGTH_SHORT).show();
        } else {
            //一个文字区间的宽度
            distance = maxProgress / (evaluates.size() - 1);
        }
        //半个文字区间的宽度
        half = distance / 2;
    }

    //绘制控件
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 绘制背景线段
         */
        canvas.drawLine(paddingLeft, height / 2, width - paddingRight, height / 2, bgPaint);
        /**
         * 绘制实际进度线段
         */
        canvas.drawLine(paddingLeft, height / 2, currentProgress, height / 2, progressPaint);
        //要支持阴影下过必须关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);//发光效果不支持硬件加速
        /**
         * 绘制圆点指示器
         */
        canvas.drawCircle(currentProgress, getHeight() / 2, mCircleRadius, circlePaint);
        /**
         * 绘制文字
         */
//        canvas.drawText(evaluates.get(0), currentProgress, (float) (height / 2 + mCircleRadius * 2.8), mTextPaint);

        int progress = (int) (currentProgress - paddingLeft);
        for (int i = 0; i < evaluates.size(); i++) {
            if ((progress > ((i * distance) - half)) && (progress <= (half + (i * distance)))) {
                /**
                 * 前半段
                 * 从效果去考虑，文字区间的前半段我们应该做的，是让文字从不透明到透明
                 * 让文字从小变大，所以我们需要一个从
                 * 而前半段触摸到的进度，最大都不会超过 (i* distance)的
                 * 不信将i等于1带入公式试一下，不能是0,因为0其实是文字区间的后半部分
                 * 所以前半部分progress - (i * distance)是一个负数，且这个负数值的范围
                 * 是在-half ~ 0 之间，随着进度的增加负数逐渐向0靠拢，
                 * 那么我们就可以拿着这个数值的求负结果/half，就可以得出一个逐渐变大，
                 * 但最大不会超过1的比例，但是我们
                 */
                if ((progress - (i * distance)) < 0) {
                    //重新设置文本大小
                    mTextSize = dip2px(getContext(),14);
                    float radio = -(progress - (i * distance)) / half;
                    textAlpha = 1 - radio;
                    float size = (mTextSize - mTextSize * textSizeRadio) * (radio);
                    mTextSize = mTextSize - size;
                    mTextPaint.setColor(changeAlpha(Color.parseColor("#9a9a9a"),
                            (int) (textAlpha * 0xff)));
                    canvas.drawText(evaluates.get(i), progress + paddingLeft,
                            (float) (height / 2 + mCircleRadius * 2.8), mTextPaint);
                }
                //后半段
                else {
                    float radio = (progress - (i * distance)) / half;
                    //textAlpha之间变小，radio逐渐变大
                    textAlpha = 1 - radio;
                    //要减去的范围，当textAlpha等于1时，文本最小
                    float size = (mTextSize - mTextSize * textSizeRadio) * (radio);
                    mTextSize = mTextSize - size;
                    mTextPaint.setColor(changeAlpha(Color.parseColor("#9a9a9a"), (int) (textAlpha * 0xff)));
                    canvas.drawText(evaluates.get(i), progress + paddingLeft, (float) (height / 2 + mCircleRadius * 2.8), mTextPaint);
                }
            }
        }

//        int progress = (int) (currentProgress - paddingLeft);
//        if (progress < half) {
//            canvas.drawText(evaluates.get(0), currentProgress, (float) (height / 2 + mCircleRadius * 2.8), mTextPaint);
//        }
//        //第二段范围
//        if ((progress > half) && (progress <= half * 3)) {
//            canvas.drawText(evaluates.get(1), currentProgress, (float) (height / 2 + mCircleRadius * 2.8), mTextPaint);
//        }
//        //第三段范围,这里写4也可以，但其实5才是完整的一块文字区间，另外半个在屏幕外
//        if ((progress > half * 3) && (progress <= half * 5)) {
//            canvas.drawText(evaluates.get(2), currentProgress, (float) (height / 2 + mCircleRadius * 2.8), mTextPaint);
//        }
    }

    //开始x
    float startX;
    //结束x
    float endX;
    //当前的进度百分比
    float result;

    //触摸
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            //按住
            case MotionEvent.ACTION_DOWN:
//                //设置进度值
//                setMotionProgress(event);
                startX = event.getX();
                return true;
            //移动
            case MotionEvent.ACTION_MOVE:
                endX = event.getX();
                float dx = endX - startX;
                startX = endX;
                currentProgress = currentProgress + dx;
                //如果当前进度小于左边距
                setCurrentProgress();
                //看数学公式就可以了,实际百分比进度数值
                result = ((currentProgress - paddingLeft) * 100) / maxProgress;
                //进行空值判断
                if (onProgressListener != null) {
                    onProgressListener.onSelect((int) result);
                }
                invalidate();
                return true;
            //抬起回弹效果
            case MotionEvent.ACTION_UP:
                int progress = (int) (currentProgress - paddingLeft);
                for (int i = 0; i < evaluates.size(); i++) {
                    if ((progress > ((i * distance) - half)) && (progress <= (half + (i * distance)))) {
                        currentProgress = distance * i + paddingLeft;
                    }
                }
                //如果当前进度小于左边距
                setCurrentProgress();
                //看数学公式就可以了,实际百分比进度数值
                result = ((currentProgress - paddingLeft) * 100) / maxProgress;
                if (result == 100) {
                    Log.e(TAG, "onTouchEvent: "+"curr:" + currentProgress );
                }
                //进行空值判断
                if (onProgressListener != null) {
                    onProgressListener.onSelect((int) result);
                }
                break;
        }
        return true;
    }

    private void setCurrentProgress() {
        if (currentProgress < paddingLeft) {
            currentProgress = paddingLeft;
        }
        //如果当前进度大于宽度-右边距
        else if (currentProgress > width - paddingRight) {
            currentProgress = width - paddingRight;
        }
    }


    //设置进度值
    private void setMotionProgress(MotionEvent event) {
        //获取当前触摸点，赋值给当前进度
        currentProgress = (int) event.getX();
        //如果当前进度小于左边距
        setCurrentProgress();
        //看数学公式就可以了,实际百分比进度数值
        float result = ((currentProgress - paddingLeft) * 100) / maxProgress;
        //进行空值判断
        if (onProgressListener != null) {
            onProgressListener.onSelect((int) result);
        }
        invalidate();
    }


    //设置当前进度条进度,从1到100
    public void setProgress(int progress) {
        if (progress > 100 || progress < 0) {
            Toast.makeText(getContext(), "输入的进度值不符合规范", Toast.LENGTH_SHORT).show();
        }
        setCurrentProgress();
        //设置当前进度的宽度
        currentProgress = ((progress * maxProgress) / 100) + paddingLeft;
        onProgressListener.onSelect(progress);
        invalidate();
    }


    //当前选中进度的回调
    private OnProgressListener onProgressListener;

    public interface OnProgressListener {
        void onSelect(int progress);
    }

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    //设置字符串集合
    public void setEvaluates(String[] strings) {
        evaluates.clear();
        evaluates.addAll(Arrays.asList(strings));
        //每一段文字区间的大小
        //当集合的长度为0，没有数据时
        if (evaluates.size() < 2) {
            Toast.makeText(getContext(), "少需要两个评价等级", Toast.LENGTH_SHORT).show();
        }
        //当有多个数据时，区间距离就是整个进度条除以集合个数-1
        else {
            distance = maxProgress / (evaluates.size() - 1);
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context,float dpValue) {
        float scale = 1;
        scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context,float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    // 将px值转换为sp值
    public static int px2sp(Context context,float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 修改颜色透明度
     * @param color
     * @param alpha
     * @return
     */
    public static int changeAlpha(int color, int alpha) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }
}
