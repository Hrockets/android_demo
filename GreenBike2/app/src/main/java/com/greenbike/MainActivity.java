package com.greenbike;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //设置按钮监听函数
        findViewById(R.id.button_bluetooth).setOnClickListener(new ButtonListener());
        findViewById(R.id.baidumap).setOnClickListener(new ButtonListener());
        findViewById(R.id.others).setOnClickListener(new ButtonListener());
    }

    class ButtonListener implements View.OnClickListener {
        //实现单击事件处理方法
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (v.getId() == R.id.button_bluetooth) {
                //蓝牙
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
            else if(v.getId() == R.id.baidumap){
                //定位
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Main3Activity.class);
                startActivity(intent);
            }
            else if(v.getId() == R.id.others){

            }
        }
    }


}
