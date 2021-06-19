package com.zukexing.app.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zukexing.app.R;
import com.zukexing.app.pojo.Rented;

public class OrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Intent intent = getIntent();
        String rentedjson = intent.getStringExtra("rented");
        System.out.println(rentedjson);

        Gson gson = new Gson();
        Rented rented = gson.fromJson(rentedjson, Rented.class);

        Toast.makeText(this, rented.getRented_id(), Toast.LENGTH_SHORT).show();
    }
}