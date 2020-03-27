package com.ysk.myview;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TopBar topBar = ((TopBar) findViewById(R.id.top_bar));
        topBar.setTopBarClickListener(new TopBar.TopBarClickListener() {
            @Override
            public void leftClick() {
                Toast.makeText(getApplicationContext(),"返回",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void rightClick() {
                Toast.makeText(getApplicationContext(),"更多",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
