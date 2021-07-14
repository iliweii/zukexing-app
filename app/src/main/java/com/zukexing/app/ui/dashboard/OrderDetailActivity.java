package com.zukexing.app.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zukexing.app.R;
import com.zukexing.app.pojo.Rented;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    SimpleDateFormat format = new SimpleDateFormat("yyyy年M月d日 HH时");

    private ImageView order_detail_back;
    private ImageView order_detail_house_image;
    private TextView order_detail_house_title;
    private TextView order_detail_house_info;
    private LinearLayout order_detail_indate_btn;
    private TextView order_detail_indate;
    private LinearLayout order_detail_outdate_btn;
    private TextView order_detail_outdate;
    private TextView order_detail_user_name;
    private TextView order_detail_user_phone;
    private TextView order_detail_price_total;
    private TextView order_detail_price_info;
    private TextView order_detail_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // 隐藏默认标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Intent intent = getIntent();
        String rentedjson = intent.getStringExtra("rented");

        Gson gson = new Gson();
        Rented rented = gson.fromJson(rentedjson, Rented.class);

        order_detail_house_image = findViewById(R.id.order_detail_house_image);
        order_detail_house_title = findViewById(R.id.order_detail_house_title);
        order_detail_house_info = findViewById(R.id.order_detail_house_info);
        order_detail_back = findViewById(R.id.order_detail_back);
        order_detail_indate_btn = findViewById(R.id.order_detail_indate_btn);
        order_detail_indate = findViewById(R.id.order_detail_indate);
        order_detail_outdate_btn = findViewById(R.id.order_detail_outdate_btn);
        order_detail_outdate = findViewById(R.id.order_detail_outdate);
        order_detail_user_name = findViewById(R.id.order_detail_user_name);
        order_detail_user_phone = findViewById(R.id.order_detail_user_phone);
        order_detail_price_total = findViewById(R.id.order_detail_price_total);
        order_detail_price_info = findViewById(R.id.order_detail_price_info);
        order_detail_type = findViewById(R.id.order_detail_type);

        order_detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        List<String> image_names = gson.fromJson(rented.getHouse_img(), new TypeToken<List<String>>() {}.getType());
        String image_name = image_names.get(0);
        Glide.with(getBaseContext())
                .load(getBaseContext().getString(R.string.host) + getBaseContext().getString(R.string.image_url_house) + image_name)
                .placeholder(R.drawable.house_default).error(R.drawable.house_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
                .into(order_detail_house_image);
        order_detail_house_title.setText(rented.getHouse_title());
        order_detail_house_info.setText(rented.getHouse_area() + "㎡ · " + rented.getHouse_type() + " · " + rented.getHouse_dir() + "朝向");
        order_detail_price_total.setText("￥" + rented.getPay());
        order_detail_price_info.setText("押金￥" + rented.getPay_d());
        Date start = new Date(rented.getStart_time());
        Date end = new Date(rented.getEnd_time());
        Date now = new Date();

        if (start.getTime() > now.getTime()) {
            // 开始时间大于（晚于）目前时间
            order_detail_type.setText("待入住");
        } else if (end.getTime() > now.getTime()) {
            // 否则，结束时间大于（晚于）目前时间
            order_detail_type.setText("入住中");
        } else {
            order_detail_type.setText("消费完成");
        }

        order_detail_indate.setText(format.format(start));
        order_detail_outdate.setText(format.format(end));
        order_detail_user_name.setText(rented.getReal_name());
        order_detail_user_phone.setText(rented.getUser_phone());

    }
}