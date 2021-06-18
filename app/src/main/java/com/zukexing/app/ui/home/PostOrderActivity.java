package com.zukexing.app.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zukexing.app.R;
import com.zukexing.app.pojo.House;
import com.zukexing.app.ui.mine.MineFragment;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PostOrderActivity extends AppCompatActivity {

    private House house;
    private House user;
    private SharedPreferences settings;
    SimpleDateFormat format = new SimpleDateFormat("yyyy年M月d日");
    SimpleDateFormat format1 = new SimpleDateFormat("HH时");
    private double dept = 0;
    private double price = 0;
    private double price_total;

    private ImageView post_house_image;
    private TextView post_house_title;
    private TextView post_house_info;
    private ImageView post_back;
    private LinearLayout post_indate_btn;
    private TextView post_indate;
    private LinearLayout post_outdate_btn;
    private TextView post_outdate;
    private TextView post_user_name;
    private TextView post_user_phone;
    private TextView post_type;
    private TextView post_price_total;
    private TextView post_date_info;
    private TextView post_price_info;
    private TextView post_price_bottom;
    private Button post_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_order);

        // 隐藏默认标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // 获得intent传递的数据
        Gson gson = new Gson();
        Intent intent = getIntent();
        String housedata = intent.getStringExtra("house");
        int type = intent.getIntExtra("type", 0); // 1短期 2长期 3 时钟
        System.out.println(housedata);
        house = gson.fromJson(housedata, House.class);

        // 获得本地存储数据
        settings = getSharedPreferences("User", 0);
        String phone = settings.getString("phone", "");
        int userid = settings.getInt("userid", 0);
        String userdata = settings.getString("user", "");
        user = gson.fromJson(userdata, House.class);

        post_house_image = findViewById(R.id.post_house_image);
        post_house_title = findViewById(R.id.post_house_title);
        post_house_info = findViewById(R.id.post_house_info);
        post_back = findViewById(R.id.post_back);
        post_indate_btn = findViewById(R.id.post_indate_btn);
        post_indate = findViewById(R.id.post_indate);
        post_outdate_btn = findViewById(R.id.post_outdate_btn);
        post_outdate = findViewById(R.id.post_outdate);
        post_user_name = findViewById(R.id.post_user_name);
        post_user_phone = findViewById(R.id.post_user_phone);
        post_type = findViewById(R.id.post_type);
        post_price_total = findViewById(R.id.post_price_total);
        post_date_info = findViewById(R.id.post_date_info);
        post_price_info = findViewById(R.id.post_price_info);
        post_price_bottom = findViewById(R.id.post_price_bottom);
        post_btn = findViewById(R.id.post_btn);


        List<String> image_names = gson.fromJson(house.getHouse_img(), new TypeToken<List<String>>() {
        }.getType());
        String image_name = image_names.get(0);
        Glide.with(getBaseContext())
                .load(getBaseContext().getString(R.string.host) + getBaseContext().getString(R.string.image_url_house) + image_name)
                .placeholder(R.drawable.house_default).error(R.drawable.house_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
                .into(post_house_image);

        post_house_title.setText(house.getHouse_title());
        post_house_info.setText(house.getHouse_area() + "㎡ · " + house.getHouse_type() + " · " + house.getHouse_dir() + "朝向");
        post_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Calendar calendar = Calendar.getInstance();
        String nowtime = null, nexttime = null;
        if (type == 1) {
            // 短期
            nowtime = format.format(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
            nexttime = format.format(calendar.getTime());
            post_type.setText("已选择 " + getResources().getString(R.string.home_nav_short));

            dept = house.getShort_dept();
            price = house.getShort_price();
            price_total = new BigDecimal(dept + price*1).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            post_price_total.setText("￥" + price_total);
            post_price_bottom.setText("￥" + price_total);

            post_price_info.setText("￥" + price + "/晚 | 共" + 1 + "晚 | 押金￥" + dept);

            post_indate_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long max = new Date().getTime() + 15 * 24 * 60 * 60 * 1000; // 上限是从今天向后15天
                    long min = new Date().getTime(); // 下限是今天
                    showDatePickDlg(max, min, "in", type);
                }
            });
            post_outdate_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String indate_str = post_indate.getText().toString();
                    Date input_indate = null;
                    try {
                        input_indate = format.parse(indate_str);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long min = input_indate.getTime() + 1 * 24 * 60 * 60 * 1000; // 下限是入住日期向后一天
                    long max = input_indate.getTime() + 7 * 24 * 60 * 60 * 1000; // 上限是从入住日期向后7天
                    showDatePickDlg(max, min, "out", type);
                }
            });
        } else if (type == 2) {
            // 长期
            nowtime = format.format(calendar.getTime());
            calendar.add(Calendar.DATE, 7);
            nexttime = format.format(calendar.getTime());
            post_type.setText("已选择 " + getResources().getString(R.string.home_nav_long));

            dept = house.getLong_dept();
            price = house.getLong_price();
            price_total = new BigDecimal(dept + price*1).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            post_price_total.setText("￥" + price_total);
            post_price_bottom.setText("￥" + price_total);

            post_price_info.setText("￥" + price + "/周 | 共" + 1 + "周 | 押金￥" + dept);

            post_indate_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long max = new Date().getTime() + 15 * 24 * 60 * 60 * 1000; // 上限是从今天向后15天
                    long min = new Date().getTime(); // 下限是今天
                    showDatePickDlg(max, min, "in", type);
                }
            });
            post_outdate_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String indate_str = post_indate.getText().toString();
                    Date input_indate = null;
                    try {
                        input_indate = format.parse(indate_str);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long min = input_indate.getTime() + 7 * 24 * 60 * 60 * 1000; // 下限是入住日期向后一周（7天）
                    long max = input_indate.getTime() + 21 * 24 * 60 * 60 * 1000; // 上限是从入住日期向后三周（21天）
                    showDatePickDlg(max, min, "out", type);
                }
            });
        } else if (type == 3) {
            // 时钟
            nowtime = format1.format(calendar.getTime());
            calendar.add(Calendar.HOUR, 4);
            nexttime = format1.format(calendar.getTime());
            post_type.setText("已选择 " + getResources().getString(R.string.home_nav_mini));

            dept = house.getMini_dept();
            price = house.getMini_price();
            price_total = new BigDecimal(dept + price*4).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            post_price_total.setText("￥" + price_total);
            post_price_bottom.setText("￥" + price_total);

            post_price_info.setText("￥" + price + "/时 | 共" + 1 + "时 | 押金￥" + dept);

            post_indate_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTimePickDlg("in");
                }
            });
            post_outdate_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTimePickDlg("out");
                }
            });
        }

        post_date_info.setText(post_indate.getText().toString() + " - " + post_outdate.getText().toString() + " 房费");

        post_indate.setText(nowtime);
        post_outdate.setText(nexttime);

        post_user_name.setText(user.getReal_name());
        post_user_phone.setText(user.getUser_phone());

        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PostOrderActivity.this, "提交订单", Toast.LENGTH_SHORT).show();
            }
        });

    }

    protected void showDatePickDlg(long max, long min, String kind, int type) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(PostOrderActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                HouseDetailActivity.this.mEditText.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                String in_str = year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日";
                if (kind.equals("in")) {
                    post_indate.setText(in_str);
                    // 判断 退房时间是否小于入住时间
                    Date input_indate = null, input_outdate = null;
                    try {
                        input_indate = format.parse(in_str);
                        input_outdate = format.parse(post_outdate.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (input_indate.getTime() + 1 * 24 * 60 * 60 * 1000 > input_outdate.getTime() && type == 1) {
                        // 短期 后延一天
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(input_indate);
                        cal.add(Calendar.DATE, 1);
                        post_outdate.setText(format.format(cal.getTime()));
                    } else if (input_indate.getTime() + 7 * 24 * 60 * 60 * 1000 > input_outdate.getTime() && type == 2) {
                        // 长期 后延7天
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(input_indate);
                        cal.add(Calendar.DATE, 7);
                        post_outdate.setText(format.format(cal.getTime()));
                    }
                } else {
                    post_outdate.setText(in_str);
                }

                if (type == 1) {
                    // 短期 计算相隔天数 得价格
                    long dayin = 0, dayout = 0, daydiff = 0;
                    try {
                        dayin = format.parse(post_indate.getText().toString()).getTime() / (24*60*60*1000);
                        dayout = format.parse(post_outdate.getText().toString()).getTime() / (24*60*60*1000);
                        daydiff = dayout - dayin;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    price_total = new BigDecimal(dept + price*daydiff).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                    post_price_total.setText("￥" + price_total);
                    post_price_bottom.setText("￥" + price_total);
                    post_price_info.setText("￥" + price + "/晚 | 共" + daydiff + "晚 | 押金￥" + dept);
                } else {
                    // 长期 计算相隔天数 ，得出相隔周数 得价格
                    long dayin = 0, dayout = 0, daydiff = 0;
                    double weekdiff = 0;
                    try {
                        dayin = format.parse(post_indate.getText().toString()).getTime() / (24*60*60*1000);
                        dayout = format.parse(post_outdate.getText().toString()).getTime() / (24*60*60*1000);
                        daydiff = dayout - dayin;
                        weekdiff = daydiff * 1.0 / 7;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    price_total = new BigDecimal(dept + price*weekdiff).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                    post_price_total.setText("￥" + price_total);
                    post_price_bottom.setText("￥" + price_total);
                    post_price_info.setText("￥" + price + "/周 | 共" + new BigDecimal(weekdiff).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue() + "周 | 押金￥" + dept);
                }
                post_date_info.setText(post_indate.getText().toString() + " - " + post_outdate.getText().toString() + " 房费");



            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMaxDate(max);  ///< 设置日期的上限日期
        datePicker.setMinDate(min);  ///< 设置日期的下限日期，其中是参数类型是long型，为日期的时间戳
        datePickerDialog.show();
    }

    protected void showTimePickDlg(String kind) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog time_dialog = new TimePickerDialog(PostOrderActivity.this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String in_str = hourOfDay + "时";
                if (kind.equals("in")) {
                    post_indate.setText(in_str);
                    // 判断 退房时间是否小于入住时间
                    Date input_indate = null, input_outdate = null;
                    try {
                        input_indate = format1.parse(in_str);
                        input_outdate = format1.parse(post_outdate.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (input_indate.getTime() >= input_outdate.getTime()) {
                        // 短期 后延一小时
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(input_indate);
                        cal.add(Calendar.HOUR, 1);
                        post_outdate.setText(format1.format(cal.getTime()));
                    }
                } else {
                    post_outdate.setText(in_str);
                    // 判断 退房时间是否小于入住时间
                    Date input_indate = null, input_outdate = null;
                    try {
                        input_indate = format1.parse(post_indate.getText().toString());
                        input_outdate = format1.parse(in_str);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (input_indate.getTime() >= input_outdate.getTime()) {
                        // 短期 后延一小时
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(input_indate);
                        cal.add(Calendar.HOUR, 1);
                        post_outdate.setText(format1.format(cal.getTime()));
                    }
                }
                // 时钟 计算相隔小时数 ，得价格
                long hourin = 0, hourout = 0, hourdiff = 0;
                try {
                    hourin = format1.parse(post_indate.getText().toString()).getTime() / (60*60*1000);
                    hourout = format1.parse(post_outdate.getText().toString()).getTime() / (60*60*1000);
                    hourdiff = hourout -hourin;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                price_total = new BigDecimal(dept + price*hourdiff).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                post_price_total.setText("￥" + price_total);
                post_price_bottom.setText("￥" + price_total);
                post_date_info.setText(post_indate.getText().toString() + " - " + post_outdate.getText().toString() + " 房费");
                post_price_info.setText("￥" + price + "/时 | 共" + hourdiff + "时 | 押金￥" + dept);
            }
        },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        time_dialog.show();
    }

}