package com.zukexing.app.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.zukexing.app.R;

public class WebviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        // 隐藏默认标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Intent intent =getIntent();
        String url = intent.getStringExtra("url");
        String hosts = this.getString(R.string.host);
        String title = intent.getStringExtra("title");

        final WebView webView = findViewById(R.id.web_view);//绑定ID
        final TextView titleView = findViewById(R.id.webview_title);
        final ImageView imageView = findViewById(R.id.webview_back);

        webView.setWebViewClient(new WebViewClient());//添加WebViewClient实例
        webView.loadUrl(hosts + url);//添加浏览器地址
        titleView.setText(title);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}