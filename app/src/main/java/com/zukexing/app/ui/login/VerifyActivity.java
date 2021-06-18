package com.zukexing.app.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zukexing.app.R;
import com.zukexing.app.pojo.House;
import com.zukexing.app.ui.home.HomeFragment;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VerifyActivity extends AppCompatActivity {

    private TextView verify_phone;
    private TextView resend_btn;
    private String VerifyCode;

    private Handler mHander=new Handler();
    private int mtime = 60;
    private String phone;

    private Context context;
    private TextView tv_code1;
    private TextView tv_code2;
    private TextView tv_code3;
    private TextView tv_code4;
    private View v1;
    private View v2;
    private View v3;
    private View v4;
    private EditText et_code;
    private List<String> codes = new ArrayList<>();
    private InputMethodManager imm;

    private Runnable mCounter = new Runnable() {
        @Override
        public void run() {
            if (mtime > 0) {
                resend_btn.setText(mtime + " 秒后重新发送");
                mtime -= 1;
            } else {
                resend_btn.setText("重新发送");
                mtime = 0;
                mHander.removeCallbacks(mCounter);
            }
            mHander.postDelayed(this,1000);
        }
    };

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String json = (String) msg.obj;
                    Gson gson = new Gson();
                    House house = gson.fromJson(json, House.class);
                    SharedPreferences settings = getSharedPreferences("User", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("phone", house.getUser_phone());
                    editor.putInt("userid", house.getUser_id());
                    editor.putString("user", json);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        // 隐藏默认标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        verify_phone = findViewById(R.id.verify_phone);
        resend_btn = findViewById(R.id.verify_resend_btn);
        final ImageView verify_close = findViewById(R.id.verify_close);

        verify_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        tv_code1 = (TextView) findViewById(R.id.tv_code1);
        tv_code2 = (TextView) findViewById(R.id.tv_code2);
        tv_code3 = (TextView) findViewById(R.id.tv_code3);
        tv_code4 = (TextView) findViewById(R.id.tv_code4);
        et_code = (EditText) findViewById(R.id.et_code);
        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        v3 = findViewById(R.id.v3);
        v4 = findViewById(R.id.v4);

        // 生成验证码
        VerifyCode = String.valueOf(RandomVerifyCode());
        Toast.makeText(getApplicationContext(), "验证码：" + VerifyCode, Toast.LENGTH_LONG).show();
        mHander.post(mCounter);

        Intent intent =getIntent();
        phone = intent.getStringExtra("phone");

        verify_phone.setText(phone);
        resend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mtime == 0) {
                    VerifyCode = String.valueOf(RandomVerifyCode());
                    Toast.makeText(getApplicationContext(), "验证码：" + VerifyCode, Toast.LENGTH_LONG).show();
                    mHander.post(mCounter);
                }
            }
        });

        //验证码输入
        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(editable != null && editable.length()>0) {
                    et_code.setText("");
                    if (codes.size() < 4) {
                        codes.add(editable.toString());
                        showCode();
                    }
                    if (codes.size() == 4) {
                        String incode = getPhoneCode();
                        if (incode.equals(VerifyCode)) {
                            // 验证码通过，查询数据库
                            // 若未注册，则注册用户
                            // 若已注册，返回用户id
                            // Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_LONG).show();
                            // 请求登录方法
                            new Thread(new Runnable(){
                                @Override
                                public void run() {
                                    String userJson = null;
                                    try {
                                        URL url = new URL(getString(R.string.host) + "api/user/login?phone=" + phone);
                                        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                                        connection.setRequestMethod("GET");
                                        connection.setConnectTimeout(8000);
                                        connection.connect();
                                        InputStream in = connection.getInputStream();

                                        byte[] buffer = new byte[1024];
                                        int len = 0;
                                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                        while((len = in.read(buffer)) != -1) {
                                            bos.write(buffer, 0, len);
                                        }
                                        userJson = new String(bos.toByteArray(), "utf-8");
                                        bos.close();
                                        connection.disconnect();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Message msg = new Message();
                                    if (userJson != null ) {
                                        msg.what = 1;
                                        msg.obj = userJson;
                                        handler.sendMessage(msg);
                                    }
                                }
                            }).start();

                        } else {
                            Toast.makeText(getApplicationContext(), "验证码错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        // 监听验证码删除按键
        et_code.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN && codes.size()>0) {
                    codes.remove(codes.size()-1);
                    showCode();
                    return true;
                }
                return false;
            }
        });
    }

    private int RandomVerifyCode() {
        int min = 1000;
        int max = 9999;
        Random rand= new Random();
        int tmp = Math.abs(rand.nextInt());
        return tmp % (max - min + 1) + min;
    }
    /**
     * 显示输入的验证码
     */
    private void showCode(){
        String code1 = "";
        String code2 = "";
        String code3 = "";
        String code4 = "";
        if(codes.size()>=1){
            code1 = codes.get(0);
        }
        if(codes.size()>=2){
            code2 = codes.get(1);
        }
        if(codes.size()>=3){
            code3 = codes.get(2);
        }
        if(codes.size()>=4){
            code4 = codes.get(3);
        }
        tv_code1.setText(code1);
        tv_code2.setText(code2);
        tv_code3.setText(code3);
        tv_code4.setText(code4);

        setColor();//设置高亮颜色
        callBack();//回调
    }

    /**
     * 设置高亮颜色
     */
    private void setColor(){
        int color_default = Color.parseColor("#999999");
        int color_focus = Color.parseColor("#3F8EED");
        v1.setBackgroundColor(color_default);
        v2.setBackgroundColor(color_default);
        v3.setBackgroundColor(color_default);
        v4.setBackgroundColor(color_default);
        if(codes.size()==0){
            v1.setBackgroundColor(color_focus);
        }
        if(codes.size()==1){
            v2.setBackgroundColor(color_focus);
        }
        if(codes.size()==2){
            v3.setBackgroundColor(color_focus);
        }
        if(codes.size()>=3){
            v4.setBackgroundColor(color_focus);
        }
    }

    /**
     * 回调
     */
    private void callBack(){
        if(onInputListener == null){
            return;
        }
        if(codes.size()==4){
            onInputListener.onSucess(getPhoneCode());
        }else{
            onInputListener.onInput();
        }
    }

    //定义回调
    public interface OnInputListener{
        void onSucess(String code);
        void onInput();
    }
    private OnInputListener onInputListener;
    public void setOnInputListener(OnInputListener onInputListener){
        this.onInputListener = onInputListener;
    }

    /**
     * 显示键盘
     */
    public void showSoftInput(){
        //显示软键盘
        if(imm!=null && et_code!=null) {
            et_code.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imm.showSoftInput(et_code, 0);
                }
            },200);
        }
    }

    /**
     * 获得手机号验证码
     * @return 验证码
     */
    public String getPhoneCode(){
        StringBuilder sb = new StringBuilder();
        for (String code : codes) {
            sb.append(code);
        }
        return sb.toString();
    }

}