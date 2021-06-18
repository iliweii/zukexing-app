package com.zukexing.app.ui.mine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zukexing.app.R;
import com.zukexing.app.pojo.House;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class VerifiedActivity extends AppCompatActivity {

    private ImageView verified_back;
    private TextView verified_btn;
    private EditText verified_name;
    private EditText verified_id;

    private int userId;
    private String real_name;
    private String real_id;
    private House house;

    private SharedPreferences settings;

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String obj = (String) msg.obj;
                    if (obj.equals("success")) {
                        house.setIs_real(1);
                        house.setReal_name(real_name);
                        house.setReal_id(real_id);
                        // 先更新数据
                        SharedPreferences.Editor editor = settings.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(house);
                        editor.putString("user", json);
                        editor.commit();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "实名认证失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified);

        // 隐藏默认标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Intent intent =getIntent();
        userId = intent.getIntExtra("userId", 0);

        settings = getSharedPreferences("User", 0);
        String user = settings.getString("user", "");
        Gson gson = new Gson();
        house = gson.fromJson(user, House.class);

        verified_back = findViewById(R.id.verified_back);
        verified_btn = findViewById(R.id.verified_btn);
        verified_name = findViewById(R.id.verified_name);
        verified_id = findViewById(R.id.verified_id);

        verified_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        verified_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                real_id = verified_id.getText().toString();
                real_name = verified_name.getText().toString();
                if (real_id.length() < 18 || real_name.length() == 0) {
                    Toast.makeText(getApplicationContext(), "输入有误", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 执行保存
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
                            URL url = new URL(getString(R.string.host) + "api/user/update");
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
                                    .append("Content-Disposition: form-data; name=\"user\"" + LINE_END)
                                    .append("Content-Type: " + CONTENT_TYPE + "; charset=" + CHARSET + LINE_END)
                                    .append("Content-Transfer-Encoding: 8bit" + LINE_END)
                                    .append(LINE_END)// 参数头设置完以后需要两个换行，然后才是参数内容
                                    .append("{userId:" + userId + ",isReal:1,realName:" + real_name + ",realId:" + real_id + "}")
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

        verified_id.setFilters(new InputFilter[]{new LengthFilterUtil(18)});
        verified_name.setFilters(new InputFilter[]{new LengthFilterUtil(20)});
    }

    public class LengthFilterUtil implements InputFilter {
        private final int maxLength;

        public LengthFilterUtil(int max) {
            maxLength = max;
        }

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                   int dstart, int dend) {
            //限定字符数量
            int dindex = 0;
            int count = 0;
            //计算文本框中已经存在的字符长度
            while (count <= maxLength && dindex < dest.length()) {
                char c = dest.charAt(dindex++);
                //这里是根据ACSII值进行判定的中英文，其中中文及中文符号的ACSII值都是大于128的
                if ((int) c <= 128) {
                    count += 1;
                } else {
                    count += 2;
                }
            }

            if (count > maxLength) {
                return dest.subSequence(0, dindex - 1);
            }
            //计算输入的字符长度
            int sindex = 0;
            while (count <= maxLength && sindex < source.length()) {
                char c = source.charAt(sindex++);
                if ((int) c <= 128) {
                    count += 1;
                } else {
                    count += 2;
                }
            }
            if (count > maxLength) {
                sindex--;
            }
            return source.subSequence(0, sindex);
        }
    }
}