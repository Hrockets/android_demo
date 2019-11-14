package com.mingrisoft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //按钮
        findViewById(R.id.btn_back).setOnClickListener(new ButtonListener());
    }

    class ButtonListener implements View.OnClickListener {
        //实现单击事件处理方法
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (v.getId() == R.id.btn_back) {
                //返回地图
                Intent intent = new Intent();
                intent.setClass(Main2Activity.this, MainActivity.class);//
                startActivity(intent);
            }
        }
    }
}
