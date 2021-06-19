package com.zukexing.app.ui.dashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zukexing.app.R;
import com.zukexing.app.pojo.House;
import com.zukexing.app.pojo.Rented;
import com.zukexing.app.ui.home.HomeFragment;
import com.zukexing.app.ui.home.HomeListviewAdapter;
import com.zukexing.app.ui.home.HouseDetailActivity;
import com.zukexing.app.ui.login.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    private ListView dashboard_listview;

    private List<Rented> renteds;
    private DashboardListviewAdapter dashboardListviewAdapter;

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case 1:
                String obj = (String) msg.obj;
                Gson gson = new Gson();
                Type rentedListType = new TypeToken<ArrayList<Rented>>(){}.getType();
                renteds = gson.fromJson(obj, rentedListType);
                // 设置主页 数据
                dashboardListviewAdapter = new DashboardListviewAdapter(getContext(), R.layout.dashboard_listview_item, renteds);
                dashboard_listview.setAdapter(dashboardListviewAdapter);
                setListViewHeightBasedOnChildren(dashboard_listview);
                break;
        }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        dashboard_listview = root.findViewById(R.id.dashboard_listview);

        SharedPreferences settings = getContext().getSharedPreferences("User", 0);
        String phone = settings.getString("phone", "");
        int userid = settings.getInt("userid", 0);

        // 如果没有登录，跳转到登录界面
        if (userid == 0) {
            Intent intent = new Intent();
            intent.setClass(DashboardFragment.this.getActivity(), LoginActivity.class);
            startActivity(intent);

            return root;
        }

        new Thread(new Runnable(){
            @Override
            public void run() {
                String rentedJson = null;
                try {
                    URL url = new URL(getString(R.string.host) + "api/rented/list?userId=" + userid);
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
                    rentedJson = new String(bos.toByteArray(), "utf-8");
                    bos.close();
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                if (rentedJson != null ) {
                    msg.what = 1;
                    msg.obj = rentedJson;
                    handler.sendMessage(msg);
                }
            }
        }).start();

        dashboard_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Rented rented = renteds.get(position);
//                Toast.makeText(HomeFragment.this.getActivity(), rented.getHouse_title(), Toast.LENGTH_SHORT).show();
                Intent rented_detail_active = new Intent(DashboardFragment.this.getActivity(), OrderDetailActivity.class);
                rented_detail_active.putExtra("rented", new Gson().toJson(rented));
                startActivity(rented_detail_active);
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