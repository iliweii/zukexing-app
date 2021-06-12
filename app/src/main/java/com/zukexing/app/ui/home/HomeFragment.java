package com.zukexing.app.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.reflect.TypeToken;
import com.zukexing.app.R;
import com.google.gson.Gson;
import com.zukexing.app.pojo.House;
import com.zukexing.app.pojo.Local;
import com.zukexing.app.pojo.Weather;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView homeTopCity;
    private TextView homeTopWeather;
    private EditText homeTopSearchEdit;
    private HomeFragment that;

    private List<House> houses;
    private HomeListviewAdapter homeListviewAdapter;
    private ListView homeListview;

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    homeTopWeather.setText("");
                    Toast.makeText(HomeFragment.this.getActivity(), "网络连接失败，请检查网络状态", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    String obj[] = (String[]) msg.obj;
                    homeTopCity.setText(obj[0]);
                    homeTopWeather.setText(obj[1]);
                    break;
            }
        }
    };

    private Handler handler2 = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String obj[] = (String[]) msg.obj;
                    Gson gson = new Gson();
                    Type houseListType = new TypeToken<ArrayList<House>>(){}.getType();
                    houses = gson.fromJson(obj[0], houseListType);
                    // 设置主页 数据
                    homeListviewAdapter = new HomeListviewAdapter(that.getContext(), R.layout.home_listview_item, houses);
                    homeListview.setAdapter(homeListviewAdapter);
                    setListViewHeightBasedOnChildren(homeListview);
                    break;
            }
        }
    };

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

        homeListview = root.findViewById(R.id.homeListview);


        // 暂时性请求主线程网络数据而不卡顿 （后面没用到）
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
                    url = new URL("https://restapi.amap.com/v3/ip?key=" + that.getString(R.string.amap_key));
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
                    url = new URL("https://restapi.amap.com/v3/weather/weatherInfo?key=" + that.getString(R.string.amap_key) + "&city=" + cityadcode + "&extensions=base");
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

//        取消了 view pager设计，因为view pager 数据怎么都加载不进去
//        // 设置主页面view pager
//        homeViewPager = root.findViewById(R.id.homeViewPager);
//        homeViewNav = root.findViewById(R.id.homeViewNav);
//        ArrayList<String> title = new ArrayList<String>();
//        title.add(this.getString(R.string.home_nav_reco));
//        title.add(this.getString(R.string.home_nav_mini));
//        title.add(this.getString(R.string.home_nav_short));
//        title.add(this.getString(R.string.home_nav_long));
//
//        final List<View> viewList = new ArrayList<View>();
//        View view1 = getLayoutInflater().inflate(R.layout.viewpager_reco,null);
//        View view2 = getLayoutInflater().inflate(R.layout.viewpager_mini,null);
//        View view3 = getLayoutInflater().inflate(R.layout.viewpager_short,null);
//        View view4 = getLayoutInflater().inflate(R.layout.viewpager_long,null);
//        viewList.add(view1);
//        viewList.add(view2);
//        viewList.add(view3);
//        viewList.add(view4);
//
//        ViewPager vp = (ViewPager) homeViewPager;
//        //设置tab的模式
//        // homeViewNav.setTabMode(TabLayout.MODE_FIXED);不可滚动的tab
//        //添加tab选项卡
//        for (int i = 0; i < title.size(); i++) {
//            homeViewNav.addTab(homeViewNav.newTab().setText(title.get(i)));
//        }
//        //把TabLayout和ViewPager关联起来
//        homeViewNav.setupWithViewPager(homeViewPager);
//        //实例化adapter
//        mAdapter = new HomeViewPagerAdapter(this.getContext(), viewList, title);
//        //给ViewPager绑定Adapter
//        homeViewPager.setAdapter(mAdapter);
//
//
//
//        // 在这里获取view pager 里面的id
////        home_listview_reco = root.findViewById(R.id.home_listview_reco);
//        home_listview_reco = viewpager_reco_root.findViewById(R.id.home_listview_reco);


        // 获取“推荐”页面数据
        new Thread(new Runnable(){
            @Override
            public void run() {
                String houseJson = null;
                try {
                    URL url = new URL(that.getString(R.string.host) + "api/house/reco?page=1");
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
                    houseJson = new String(bos.toByteArray(), "utf-8");
                    bos.close();
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                if (houseJson != null ) {
                    msg.what = 1;
                    String obj[] = new String[10];
                    obj[0] = houseJson;
                    msg.obj = obj;
                    handler2.sendMessage(msg);
                }
            }
        }).start();

        homeListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                House house = houses.get(position);
//                Toast.makeText(HomeFragment.this.getActivity(), house.getHouse_title(), Toast.LENGTH_SHORT).show();
                Intent house_detail_active = new Intent(HomeFragment.this.getActivity(), HouseDetailActivity.class);
                house_detail_active.putExtra("house", new Gson().toJson(house));
                startActivity(house_detail_active);
            }
        });

        return root;
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += (listItem.getMeasuredHeight() + 100);

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);
    }

}