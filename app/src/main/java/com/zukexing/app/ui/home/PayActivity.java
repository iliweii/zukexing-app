package com.zukexing.app.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zukexing.app.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class PayActivity extends AppCompatActivity {

    private int userId;
    private int houseId;
    private String startTime;
    private String endTime;
    private double pay;
    private double payH;
    private double payD;

    private ImageView pay_back;
    private TextView pay_time;
    private TextView pay_price;
    private TextView pay_info;
    private LinearLayout pay_balance_btn;
    private LinearLayout pay_wechat_btn;
    private LinearLayout pay_alipay_btn;
    private RadioButton pay_balance;
    private RadioButton pay_wechat;
    private RadioButton pay_alipay;
    private Button pay_btn;

    // 1 钱包支付 2 微信支付 3 支付宝支付
    private int pay_type = 0;

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case 1:
                String obj = (String) msg.obj;
                if (obj.equals("success")) {
                    String type = "";
                    if (pay_type == 1) type = "余额支付";
                    else if (pay_type == 2) type = "微信支付";
                    else if (pay_type == 3) type = "支付宝支付";
                    Intent intent = new Intent();
                    intent.setClass(PayActivity.this, PayDoneActivity.class);
                    intent.putExtra("type", type);
                    intent.putExtra("pay", pay);
                    PayActivity.this.startActivity(intent);

                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "订单生成失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        // 隐藏默认标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", 0);
        houseId = intent.getIntExtra("houseId", 0);
        startTime = intent.getStringExtra("startTime");
        endTime = intent.getStringExtra("endTime");
        pay = intent.getDoubleExtra("pay", 0);
        payH = intent.getDoubleExtra("payH", 0);
        payD = intent.getDoubleExtra("payD", 0);
        String info = intent.getStringExtra("info");

        pay_back = findViewById(R.id.pay_back);
        pay_time = findViewById(R.id.pay_time);
        pay_price = findViewById(R.id.pay_price);
        pay_info = findViewById(R.id.pay_info);
        pay_balance_btn = findViewById(R.id.pay_balance_btn);
        pay_wechat_btn = findViewById(R.id.pay_wechat_btn);
        pay_alipay_btn = findViewById(R.id.pay_alipay_btn);
        pay_balance = findViewById(R.id.radio_balance);
        pay_wechat = findViewById(R.id.radio_wechat);
        pay_alipay = findViewById(R.id.radio_alipay);
        pay_btn = findViewById(R.id.pay_btn);

        pay_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pay_time.setText("30分钟内支付有效");
        pay_price.setText(String.valueOf(pay));
        pay_info.setText(info);

        pay_type = 1;

        pay_balance_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay_type = 1;
                pay_balance.setChecked(true);
                pay_wechat.setChecked(false);
                pay_alipay.setChecked(false);
            }
        });

        pay_wechat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay_type = 2;
                pay_balance.setChecked(false);
                pay_wechat.setChecked(true);
                pay_alipay.setChecked(false);
            }
        });

        pay_alipay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay_type = 3;
                pay_balance.setChecked(false);
                pay_wechat.setChecked(false);
                pay_alipay.setChecked(true);
            }
        });


        pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(PayActivity.this, "提交订单", Toast.LENGTH_SHORT).show();
                // 向服务器提交数据
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        String CHARSET = "UTF-8";
                        String BOUNDARY = UUID.randomUUID().toString();
                        String CONTENT_TYPE = "multipart/form-data";
                        String PREFIX = "--";
                        String LINE_END = "\r\n";
                        StringBuilder response = new StringBuilder();
                        HttpURLConnection connection = null;
                        try {
                            URL url = new URL(getString(R.string.host) + "api/rented/insert");
                            connection = (HttpURLConnection)url.openConnection();
                            connection.setRequestMethod("POST");
                            connection.setConnectTimeout(8000);
                            connection.setDoOutput(true);
                            connection.setDoInput(true);
                            connection.setUseCaches(false);//Post 请求不能使用缓存
                            //设置请求头参数
                            connection.setRequestProperty("Connection", "Keep-Alive");
                            connection.setRequestProperty("Charset", CHARSET);
                            connection.setRequestProperty("Accept-Charset", CHARSET);
                            connection.setRequestProperty("Content-Type", CONTENT_TYPE + ";charset=" + CHARSET +";boundary=" + BOUNDARY);
                            /**
                             * 请求体
                             */
                            //上传参数
                            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                            //getStrParams()为一个
                            StringBuilder strSb = new StringBuilder();
                            strSb.append(PREFIX)
                                    .append(BOUNDARY)
                                    .append(LINE_END)
                                    .append("Content-Disposition: form-data; name=\"rented\"" + LINE_END)
                                    .append("Content-Type: " + CONTENT_TYPE + "; charset=" + CHARSET + LINE_END)
                                    .append("Content-Transfer-Encoding: 8bit" + LINE_END)
                                    .append(LINE_END)// 参数头设置完以后需要两个换行，然后才是参数内容
                                    .append("{userId:" + userId + ",houseId:" + houseId + ",startTime:\"" + startTime + "\",endTime:\"" + endTime + "\",pay:" + pay + ",payH:" + payH + ",payD:" + payD + "}")
                                    .append(LINE_END);
                            dos.write(strSb.toString().getBytes(CHARSET));
                            dos.flush();
                            dos.writeBytes(LINE_END);
                            //请求结束标志
                            String end = PREFIX + BOUNDARY + PREFIX + LINE_END;
                            dos.write(end.getBytes(CHARSET));
                            dos.flush();
                            dos.close();
                            //读取服务器返回信息
                            if (connection.getResponseCode() == 200) {
                                InputStream in = connection.getInputStream();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(in, CHARSET));
                                String line = null;
                                while ((line = reader.readLine()) != null) {
                                    response.append(line);
                                }
                            }
                            connection.disconnect();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Message msg = new Message();
                        if (response.toString() != null ) {
                            msg.what = 1;
                            msg.obj = response.toString();
                            msg.obj = response.toString();
                            handler.sendMessage(msg);
                        }
                    }
                }).start();
            }
        });

    }
}