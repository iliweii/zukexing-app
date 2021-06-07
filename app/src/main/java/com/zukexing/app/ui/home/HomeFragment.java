package com.zukexing.app.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.zukexing.app.R;
import com.google.gson.Gson;
import com.zukexing.app.pojo.Local;
import com.zukexing.app.pojo.Weather;
import com.zukexing.app.pojo.WeatherLive;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView homeTopCity;
    private TextView homeTopWeather;
    private EditText homeTopSearchEdit;
    private HomeFragment that;

    private TextView homeNavReco, homeNavMini, homeNavShort, homeNavLong;

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    homeTopWeather.setText("");
                    Toast.makeText(HomeFragment.this.getActivity(), "网络连接失败，请检查网络状态", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    String obj[] = (String[]) msg.obj;
                    homeTopCity.setText(obj[0]);
                    homeTopWeather.setText(obj[1]);
                    break;
            }
        }
    };

    private void HomeNavClick(int id) {
        if (id == 1) {
            homeNavReco.setTextSize(20);
            homeNavReco.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            homeNavReco.setTextColor(this.getResources().getColor(R.color.stdcolor));
        } else {
            homeNavReco.setTextSize(14);
            homeNavReco.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            homeNavReco.setTextColor(this.getResources().getColor(R.color.black));
        }
        if (id == 2) {
            homeNavMini.setTextSize(20);
            homeNavMini.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            homeNavMini.setTextColor(this.getResources().getColor(R.color.stdcolor));
        } else {
            homeNavMini.setTextSize(14);
            homeNavMini.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            homeNavMini.setTextColor(this.getResources().getColor(R.color.black));
        }
        if (id == 3) {
            homeNavShort.setTextSize(20);
            homeNavShort.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            homeNavShort.setTextColor(this.getResources().getColor(R.color.stdcolor));
        } else {
            homeNavShort.setTextSize(14);
            homeNavShort.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            homeNavShort.setTextColor(this.getResources().getColor(R.color.black));
        }
        if (id == 4) {
            homeNavLong.setTextSize(20);
            homeNavLong.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            homeNavLong.setTextColor(this.getResources().getColor(R.color.stdcolor));
        } else {
            homeNavLong.setTextSize(14);
            homeNavLong.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            homeNavLong.setTextColor(this.getResources().getColor(R.color.black));
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        // final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                // textView.setText(s);
            }
        });

        homeTopCity = root.findViewById(R.id.homeTopCity);
        homeTopWeather = root.findViewById(R.id.homeTopWeather);
        homeTopSearchEdit = root.findViewById(R.id.homeTopSearchEdit);
        homeTopSearchEdit.setFocusable(false);
        homeTopSearchEdit.setFocusableInTouchMode(false);
        that = this;

        // 四个home界面次级导航栏
        homeNavReco = root.findViewById(R.id.homeNavReco);
        homeNavMini = root.findViewById(R.id.homeNavMini);
        homeNavShort = root.findViewById(R.id.homeNavShort);
        homeNavLong = root.findViewById(R.id.homeNavLong);
        // 四个home界面次级导航栏点击事件
        homeNavReco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeNavClick(1);
            }
        });
        homeNavMini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeNavClick(2);
            }
        });
        homeNavShort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeNavClick(3);
            }
        });
        homeNavLong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeNavClick(4);
            }
        });

        // 这个忘记式干嘛的了
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        // 通过ip获取位置、天气 （高德地图接口）
        new Thread(new Runnable(){
            @Override
            public void run() {
                URL url = null;
                Local local = null;
                Weather weather = null;
                try {
                    // 请求IP定位（通过IP获取位置信息）
                    url = new URL("https://restapi.amap.com/v3/ip?key=2783b54aa5662100cae8f7879d9a30fe");
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
                    String dataStr = new String(bos.toByteArray(), "utf-8");
                    bos.close();
                    connection.disconnect();
                    // 开始解析数据
                    Gson gson = new Gson();
                    local = gson.fromJson(dataStr, Local.class);

                    // 根据城市名，获取城市adcode
                    url = new URL(that.getString(R.string.host) + "api/amapcitycode/queryadcode?city_name=" + local.getCity());
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.connect();
                    in = connection.getInputStream();

                    buffer = new byte[1024];
                    len = 0;
                    bos = new ByteArrayOutputStream();
                    while((len = in.read(buffer)) != -1) {
                        bos.write(buffer, 0, len);
                    }
                    String cityadcode = new String(bos.toByteArray(), "utf-8");

                    // 根据adcode，获取天气信息
                    url = new URL("https://restapi.amap.com/v3/weather/weatherInfo?key=2783b54aa5662100cae8f7879d9a30fe&city=" + cityadcode + "&extensions=base");
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.connect();
                    in = connection.getInputStream();

                    buffer = new byte[1024];
                    len = 0;
                    bos = new ByteArrayOutputStream();
                    while((len = in.read(buffer)) != -1) {
                        bos.write(buffer, 0, len);
                    }
                    String weatherdata = new String(bos.toByteArray(), "utf-8");
                    weather = gson.fromJson(weatherdata, Weather.class);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                if (local == null || weather == null) {
                    msg.what = 1;
                    handler.sendMessage(msg);
                } else {
                    msg.what = 2;
                    String obj[] = new String[10];
                    obj[0] = local.getCity();
                    obj[1] = weather.getLives()[0].getWeather() + "  " + weather.getLives()[0].getTemperature() + "℃";
                    msg.obj = obj;
                    handler.sendMessage(msg);
                }
            }
        }).start();



        return root;
    }
}