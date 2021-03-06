package com.ysk.myview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.ysk.myview.fallingView.FallObject;
import com.ysk.myview.fallingView.FallingView;
import com.ysk.myview.lrc.LrcHelper;
import com.ysk.myview.lrc.LyricView;
import com.ysk.myview.lyric.FakePlayer;
import com.ysk.myview.lyric.Lyric;
import com.ysk.myview.progressSeekBar.ProgressSeekBarWithText;
import com.ysk.myview.spinner.AbstractSpinnerAdapter;
import com.ysk.myview.spinner.SpinerPopWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private AreaChartsView mAreaChartsView;
    private Timer timer;
    private TagsLayout tagsLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTopBar();
        initAreaChartsView();
        initTags();
        initFlowLayout();
        initProgressSeekbar();
        initLrcView();
       // initFallingView();
        initSpinner();
        initBallView();
    }

    private void initBallView(){
        BallView ballView = ((BallView) findViewById(R.id.ball_view));
        ArrayList<String> list = new ArrayList<>();
        for (int i =0;i<10;i++){
            list.add(String.valueOf(i));
        }
        ballView.setData(list);
//        ballView.requestLayout();
    }

    private void initSpinner(){
        final List<String> nameList = new ArrayList<>();
        nameList.add("java");
        nameList.add("C");
        nameList.add("C++");
        nameList.add("C#");
        nameList.add("python");
        final SpinerPopWindow spinerPopWindow = new SpinerPopWindow(this);
        spinerPopWindow.refreshData(nameList, 0);
        final TextView spinnerText = ((TextView) findViewById(R.id.spinner_text_view));
        final Drawable dropDown = ContextCompat.getDrawable(this,R.drawable.ic_drop_down);
        final Drawable dropUp =ContextCompat.getDrawable(this,R.drawable.ic_drop_up);
        spinnerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinerPopWindow.setWidth(spinnerText.getWidth());
                spinerPopWindow.showAsDropDown(spinnerText);
                spinnerText.setCompoundDrawablesWithIntrinsicBounds(null,null, dropUp,null);
            }
        });
        spinerPopWindow.setItemListener(new AbstractSpinnerAdapter.OnItemSelectListener() {
            @Override
            public void onItemClick(int pos) {
                spinnerText.setCompoundDrawablesWithIntrinsicBounds(null,null, dropDown,null);
                spinnerText.setText(nameList.get(pos));
            }

            @Override
            public void onDismiss() {
                spinnerText.setCompoundDrawablesWithIntrinsicBounds(null,null, dropDown,null);
            }
        });
    }

    private void initFallingView(){
        //绘制雪球bitmap
        Paint snowPaint = new Paint();
        snowPaint.setColor(Color.WHITE);
        snowPaint.setStyle(Paint.Style.FILL);
        Bitmap bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
        Canvas bitmapCanvas = new Canvas(bitmap);
        bitmapCanvas.drawCircle(25,25,25,snowPaint);
        //初始化一个雪球样式的fallObject
        /*FallObject.Builder builder = new FallObject.Builder(bitmap);
        FallObject fallObject = builder
                .setSpeed(10)
                .build();*/
        FallObject.Builder builder = new FallObject.Builder(getResources().getDrawable(R.drawable.ic_snow));
        /*FallObject fallObject = builder
                .setSpeed(10,true)
                .setSize(50,50,true)
                .build();*/
        FallObject fallObject = builder
                .setSpeed(7,true)
                .setSize(50,50,true)
                .setWind(5,true,true)
                .build();
        FallingView fallingView = ((FallingView) findViewById(R.id.fallingView));
        fallingView.addFallObject(fallObject,50);//添加50个雪球对象
    }



    private void initLrcView(){
        LyricView lyricView = ((LyricView) findViewById(R.id.lyric));
        lyricView.setLrcData(LrcHelper.parseStr2List(new Lyric().getLyric()));
        FakePlayer player = new FakePlayer();
        player.setLyricView(lyricView);

    }

    private void initProgressSeekbar(){
        ProgressSeekBarWithText progressBar = ((ProgressSeekBarWithText) findViewById(R.id.progress_bar));
        progressBar.setEvaluates(new String[]{"垃圾","一般","还行","可以","很棒"});
    }

    private void initTopBar(){
        TopBar topBar = ((TopBar) findViewById(R.id.top_bar));
        mAreaChartsView = (AreaChartsView)findViewById(R.id.area_charts_view);
        tagsLayout = ((TagsLayout) findViewById(R.id.tags_layout));
        topBar.setTopBarClickListener(new TopBar.TopBarClickListener() {
            @Override
            public void leftClick() {
                Toast.makeText(getApplicationContext(), "返回", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void rightClick() {
                Toast.makeText(getApplicationContext(), "更多", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initFlowLayout(){
        FlowLayout mFlowLayout = (FlowLayout) findViewById(R.id.flow_layout);
        List<String> list = new ArrayList<>();
        list.add("java是世界上最强大的语言，没有之一");
        list.add("javaEE是java最强大的版本，没有之一");
        list.add("javaME是java最...的版本，没有之一");
        list.add("c是世界上最入门的语言，有之一");
        list.add("php是世界上最...的语言");
        list.add("ios是世界上最流畅的系统，没有之一 ");
        list.add("c++是世界上最应该掌握的面向对象语言，之一");
        list.add("c#也是");
        list.add("Android是世界上最爽的开发系统，没有之一");
       // mFlowLayout.setAlignByCenter(FlowLayout.AlienState.CENTER);//居中
        mFlowLayout.setAlignByCenter(FlowLayout.AlienState.LEFT);//左对齐
        mFlowLayout.setAdapter(list, R.layout.flow_item, new FlowLayout.ItemView<String>() {
            @Override
            void getCover(String item, FlowLayout.ViewHolder holder, View inflate, int position) {
                holder.setText(R.id.tv_label_name,item);
            }
        });
    }

    private void initTags(){
        String[] string = {"从我写代码那天起，我就不打算一直写下去", "因为头发很重要",
                "没有头发连老婆都找不到", "连老婆都找不到", "找不到","不写代码干什么","不写代码就能找到老婆吗",
                "不写代码也找不到老婆","那还是写吧"};
        if (tagsLayout != null) {
            tagsLayout.addItem(string);
            tagsLayout.setOnItemClickListener(new TagsLayout.OnItemClickListener() {
                @Override
                public void onItemClick(View view) {
                    if (view instanceof TextView)
                        Toast.makeText(getApplicationContext(),
                                ((TextView) view).getText().toString()
                                ,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 初始化表格
     */
    private void initAreaChartsView(){
        //初始化自定义图表的规格和属性
        ArrayList<Integer> mXLevel = new ArrayList<>();
        ArrayList<Integer> mYLevel = new ArrayList<>();
        ArrayList<String> mGridLevelText = new ArrayList<>();
        ArrayList<Integer> mGridColorLevel = new ArrayList<>();
        ArrayList<Integer> mGridTxtColorLevel = new ArrayList<>();
        //初始化x轴坐标区间
        mXLevel.add(0);
        mXLevel.add(60);
        mXLevel.add(90);
        mXLevel.add(100);
        mXLevel.add(110);
        mXLevel.add(120);
        //初始化y轴坐标区间
        mYLevel.add(0);
        mYLevel.add(90);
        mYLevel.add(140);
        mYLevel.add(160);
        mYLevel.add(180);
        mYLevel.add(200);
        //初始化区间颜色
        mGridColorLevel.add(Color.parseColor("#1FB0E7"));
        mGridColorLevel.add(Color.parseColor("#4FC7F4"));
        mGridColorLevel.add(Color.parseColor("#4FDDF2"));
        mGridColorLevel.add(Color.parseColor("#90E9F4"));
        mGridColorLevel.add(Color.parseColor("#B2F6F1"));
        //初始化区间文字提示颜色
        mGridTxtColorLevel.add(Color.parseColor("#EA8868"));
        mGridTxtColorLevel.add(Color.parseColor("#EA8868"));
        mGridTxtColorLevel.add(Color.parseColor("#EA8868"));
        mGridTxtColorLevel.add(Color.WHITE);
        mGridTxtColorLevel.add(Color.BLACK);
        //初始化区间文字
        mGridLevelText.add("异常");
        mGridLevelText.add("过高");
        mGridLevelText.add("偏高");
        mGridLevelText.add("正常");
        mGridLevelText.add("偏低");

        mAreaChartsView.initGridColorLevel(mGridColorLevel);
        mAreaChartsView.initGridLevelText(mGridLevelText);
        mAreaChartsView.initGridTxtColorLevel(mGridTxtColorLevel);
        mAreaChartsView.initXLevelOffset(mXLevel);
        mAreaChartsView.initYLevelOffset(mYLevel);
        mAreaChartsView.initTitleXY("投入量(H)", "产出量(H)");

    }
    @Override
    protected void onStart() {
        super.onStart();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Random random = new Random();
                int x = random.nextInt(120) % (120 + 1) + 0;
                Random randomy = new Random();
                int y = randomy.nextInt(200) % (200 + 1) + 0;
                //随机模拟赋值
                mAreaChartsView.updateValues(x, y);
            }
        }, 0, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null)
            timer.cancel();
    }
}
