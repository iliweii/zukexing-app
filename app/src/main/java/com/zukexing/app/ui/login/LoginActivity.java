package com.zukexing.app.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zukexing.app.R;
import com.zukexing.app.ui.home.HomeFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 隐藏默认标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        final EditText phoneEditText = findViewById(R.id.login_phone);
        final TextView phoneHint = findViewById(R.id.login_phone_hint);
        final CheckBox acceptCheckBox = findViewById(R.id.login_checkBox);
        final Button loginButton = findViewById(R.id.login_btn);

        final TextView protocol_privacy = findViewById(R.id.protocol_privacy);
        final TextView protocol_service = findViewById(R.id.protocol_service);

        final ImageView login_close = findViewById(R.id.login_close);

        login_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        protocol_service.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, WebviewActivity.class);
                intent.putExtra("url", "policy/service");
                intent.putExtra("title", "用户服务协议");
                LoginActivity.this.startActivity(intent);
            }
        });

        protocol_privacy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, WebviewActivity.class);
                intent.putExtra("url", "policy/privacy");
                intent.putExtra("title", "隐私政策");
                LoginActivity.this.startActivity(intent);
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
                String phone = phoneEditText.getText().toString();
                if (phone.length() == 0) {
                    phoneHint.setVisibility(View.INVISIBLE);
                } else {
                    phoneHint.setVisibility(View.VISIBLE);
                    if (phone.length() == 11) {
                        loginButton.setEnabled(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//                loginViewModel.loginDataChanged(phoneEditText.getText().toString(),
//                        passwordEditText.getText().toString());
            }
        };
        phoneEditText.addTextChangedListener(afterTextChangedListener);
        phoneEditText.setFilters(new InputFilter[]{new LengthFilterUtil(11)});


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (acceptCheckBox.isChecked()) {
                    // 跳转验证码界面

                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, VerifyActivity.class);
                    intent.putExtra("phone", phoneEditText.getText().toString());
                    LoginActivity.this.startActivity(intent);
                    // 结束
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "请勾选协议", Toast.LENGTH_SHORT).show();
                }
            }
        });
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