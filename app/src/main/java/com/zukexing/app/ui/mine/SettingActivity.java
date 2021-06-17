package com.zukexing.app.ui.mine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.zukexing.app.R;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // 隐藏默认标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


    }
}