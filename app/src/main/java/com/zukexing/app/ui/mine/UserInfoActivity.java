package com.zukexing.app.ui.mine;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zukexing.app.R;
import com.zukexing.app.pojo.House;
import com.zukexing.app.ui.home.HomeFragment;
import com.zukexing.app.ui.home.HomeListviewAdapter;
import com.zukexing.app.ui.login.LoginActivity;
import com.zukexing.app.ui.login.VerifyActivity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class UserInfoActivity extends AppCompatActivity {

    private String phone, user;
    private int userid;
    private House house;

    private SharedPreferences settings;
    private String avater_path;

    private ImageView userinfo_avater;
    private ImageView userinfo_back;
    private LinearLayout userinfo_avater_btn;
    private LinearLayout userinfo_nick_btn;
    private LinearLayout userinfo_sex_btn;
    private LinearLayout userinfo_birth_btn;
    private LinearLayout userinfo_phone_btn;
    private LinearLayout userinfo_real_btn;
    private LinearLayout userinfo_password_btn;
    private LinearLayout userinfo_time_btn;
    private TextView userinfo_nick;
    private TextView userinfo_sex;
    private TextView userinfo_birth;
    private TextView userinfo_phone;
    private TextView userinfo_real;
    private TextView userinfo_time;

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String obj = (String) msg.obj;
                    if (obj.equals("failed")) {
                        Toast.makeText(getApplicationContext(), "??????????????????", Toast.LENGTH_SHORT).show();
                    } else {
                        house.setUser_avater(obj);
                        // ???????????????
                        SharedPreferences.Editor editor = settings.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(house);
                        editor.putString("user", json);
                        editor.commit();
                        // ???????????????
                        Glide.with(getBaseContext())
//                            .load(getBaseContext().getString(R.string.host) + getBaseContext().getString(R.string.image_url_avater) + house.getUser_avater())
                                .load(avater_path)
                            .placeholder(R.drawable.ic_avater_default).error(R.drawable.ic_avater_default)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .into(userinfo_avater);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        // ?????????????????????
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Gson gson = new Gson();
        settings = getSharedPreferences("User", 0);

        phone = settings.getString("phone", "");
        userid = settings.getInt("userid", 0);
        user = settings.getString("user", "");
        house = gson.fromJson(user, House.class);

        userinfo_back = findViewById(R.id.userinfo_back);
        userinfo_avater_btn = findViewById(R.id.userinfo_avater_btn);
        userinfo_avater = findViewById(R.id.userinfo_avater);
        userinfo_nick_btn = findViewById(R.id.userinfo_nick_btn);
        userinfo_sex_btn = findViewById(R.id.userinfo_sex_btn);
        userinfo_birth_btn = findViewById(R.id.userinfo_birth_btn);
        userinfo_phone_btn = findViewById(R.id.userinfo_phone_btn);
        userinfo_real_btn = findViewById(R.id.userinfo_real_btn);
        userinfo_time_btn = findViewById(R.id.userinfo_time_btn);
        userinfo_nick = findViewById(R.id.userinfo_nick);
        userinfo_sex = findViewById(R.id.userinfo_sex);
        userinfo_birth = findViewById(R.id.userinfo_birth);
        userinfo_phone = findViewById(R.id.userinfo_phone);
        userinfo_real = findViewById(R.id.userinfo_real);
        userinfo_time = findViewById(R.id.userinfo_time);

        // ??????activity
        userinfo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // ?????????
        Glide.with(getBaseContext())
            .load(getBaseContext().getString(R.string.host) + getBaseContext().getString(R.string.image_url_avater) + house.getUser_avater())
            .placeholder(R.drawable.ic_avater_default).error(R.drawable.ic_avater_default)
            .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
            .into(userinfo_avater);
        if (house.getUser_nick() != null)
            userinfo_nick.setText(house.getUser_nick());
        else
            userinfo_nick.setText("???????????????");
        if (house.getUser_sex() != null && !house.getUser_sex().equals(""))
            userinfo_sex.setText(house.getUser_sex());
        else
            userinfo_sex.setText("???????????????");
        if (house.getUser_birth() != null)
            userinfo_birth.setText(house.getUser_birth());
        else
            userinfo_birth.setText("???????????????");
        if (house.getUser_phone() != null)
            userinfo_phone.setText(house.getUser_phone());
        else
            userinfo_phone.setText("???????????????");
        if (house.getIs_real() == 1)
            userinfo_real.setText("???????????????");
        else
            userinfo_real.setText("???????????????");
        if (house.getCreate_time() != null)
            userinfo_time.setText(house.getCreate_time());

        userinfo_avater_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ??????
                getPicFromP();
            }
        });

        userinfo_nick_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(UserInfoActivity.this, UserNickActivity.class);
                intent.putExtra("userId", house.getUser_id());
                intent.putExtra("userNick", house.getUser_nick());
                UserInfoActivity.this.startActivity(intent);
            }
        });

        userinfo_sex_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "????????????", Toast.LENGTH_SHORT).show();
            }
        });
        userinfo_birth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "????????????", Toast.LENGTH_SHORT).show();
            }
        });
        userinfo_phone_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "???????????????", Toast.LENGTH_SHORT).show();
            }
        });
        userinfo_real_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (house.getIs_real() == 1) {
                    Toast.makeText(getApplicationContext(), "???????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(UserInfoActivity.this, VerifiedActivity.class);
                intent.putExtra("userId", house.getUser_id());
                UserInfoActivity.this.startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences settings = getBaseContext().getSharedPreferences("User", 0);
        String user = settings.getString("user", "");
        Gson gson = new Gson();
        house = gson.fromJson(user, House.class);
        userinfo_nick.setText(house.getUser_nick());
        if (house.getIs_real() == 1)
            userinfo_real.setText("???????????????");
        else
            userinfo_real.setText("???????????????");
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences settings = getBaseContext().getSharedPreferences("User", 0);
        String user = settings.getString("user", "");
        Gson gson = new Gson();
        house = gson.fromJson(user, House.class);
        userinfo_nick.setText(house.getUser_nick());
        if (house.getIs_real() == 1)
            userinfo_real.setText("???????????????");
        else
            userinfo_real.setText("???????????????");
    }

    private void getPicFromP() {
        Intent intent = new Intent(Intent.ACTION_PICK);//????????????????????????
        intent.setType("image/*");//??????????????????
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:  // ?????????
                avater_path = parseUri(data);
                if (avater_path == null) {
                    break;
                }
                System.out.println("avater_path:" + avater_path);
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
                            URL url = new URL(getString(R.string.host) + "api/user/updateavater");
                            connection = (HttpURLConnection)url.openConnection();
                            connection.setRequestMethod("POST");
                            connection.setConnectTimeout(8000);
                            connection.setDoOutput(true);
                            connection.setDoInput(true);
                            connection.setUseCaches(false);//Post ????????????????????????
                            //?????????????????????
                            connection.setRequestProperty("Connection", "Keep-Alive");
                            connection.setRequestProperty("Charset", CHARSET);
                            connection.setRequestProperty("Accept-Charset", CHARSET);
                            connection.setRequestProperty("Content-Type", CONTENT_TYPE + ";charset=" + CHARSET +";boundary=" + BOUNDARY);
                            /**
                             * ?????????
                             */
                            //????????????
                            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                            //getStrParams()?????????
                            StringBuilder strSb = new StringBuilder();
                            strSb.append(PREFIX)
                                    .append(BOUNDARY)
                                    .append(LINE_END)
                                    .append("Content-Disposition: form-data; name=\"userid\"" + LINE_END)
//                                    .append("Content-Type: text/plain; charset=" + CHARSET + LINE_END)
                                    .append("Content-Type: " + CONTENT_TYPE + "; charset=" + CHARSET + LINE_END)
                                    .append("Content-Transfer-Encoding: 8bit" + LINE_END)
                                    .append(LINE_END)// ?????????????????????????????????????????????????????????????????????
                                    .append(userid)
                                    .append(LINE_END);
                            dos.write(strSb.toString().getBytes(CHARSET));
                            dos.flush();
                            //????????????
                            File file_avater = new File(avater_path);
                            StringBuilder fileSb = new StringBuilder();
                            fileSb.append(PREFIX).append(BOUNDARY).append(LINE_END)
                                    /**
                                     * ????????????????????? name?????????????????????????????????key ????????????key ??????????????????????????????
                                     * filename??????????????????????????????????????? ??????:abc.png
                                     */
                                    .append("Content-Disposition: form-data; name=avater; filename=\"" + file_avater.getName() + "\"" + LINE_END)
                                    .append("Content-Type: " + CONTENT_TYPE + "; charset=" + CHARSET + LINE_END) //?????????ContentType????????? ????????? ???Content-Type
                                    .append("Content-Transfer-Encoding: 8bit" + LINE_END)
                                    .append(LINE_END);// ?????????????????????????????????????????????????????????????????????
                            dos.write(fileSb.toString().getBytes(CHARSET));
                            dos.flush();
                            InputStream is = new FileInputStream(file_avater);
                            byte[] buffer = new byte[1024];
                            int len = 0;
                            while ((len = is.read(buffer)) != -1){
                                dos.write(buffer,0,len);
                            }
                            is.close();
                            dos.writeBytes(LINE_END);
                            //??????????????????
                            String end = PREFIX + BOUNDARY + PREFIX + LINE_END;
                            dos.write(end.getBytes(CHARSET));
                            dos.flush();
                            dos.close();
                            //???????????????????????????
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
//                            throw new Exception(e.getMessage());
                        }finally {
                            if (connection!=null){
                                connection.disconnect();
                            }
                        }

                        Message msg = new Message();
                        if (response.toString() != null ) {
                            msg.what = 1;
                            msg.obj = response.toString();
                            handler.sendMessage(msg);
                        }
                    }
                }).start();
                break;
            default:
        }
    }

    public String parseUri(Intent data) {
        if (data == null) {
            return null;
        }
        Uri uri = data.getData();
        String imagePath;
        // ???????????????????????????????????????
        Cursor cursor = getContentResolver()
                .query(uri, new String[]{MediaStore.Images.ImageColumns.DATA},
                        null, null, null);
        if (cursor == null) {
            imagePath = uri.getPath();
        } else {
            cursor.moveToFirst();
            // ??????????????????????????????
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            imagePath = cursor.getString(index);  // ????????????????????????
            cursor.close();
        }
        return imagePath;  // ??????????????????
    }

}