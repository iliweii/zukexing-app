package com.zukexing.app.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zukexing.app.R;

public class PayDoneActivity extends AppCompatActivity {

    private TextView paydone_price;
    private TextView paydone_info;
    private Button paydone_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_done);

        // 隐藏默认标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        paydone_price = findViewById(R.id.paydone_price);
        paydone_info = findViewById(R.id.paydone_info);
        paydone_btn = findViewById(R.id.paydone_btn);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        double pay = intent.getDoubleExtra("pay", 0);

        paydone_price.setText(String.valueOf(pay));
        paydone_info.setText(type);

        paydone_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}