package com.zukexing.app.ui.mine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.zukexing.app.R;
import com.zukexing.app.pojo.House;
import com.zukexing.app.ui.home.HomeFragment;
import com.zukexing.app.ui.home.HouseDetailActivity;
import com.zukexing.app.ui.login.LoginActivity;

public class MineFragment extends Fragment {

    private MineViewModel mineViewModel;
    //是否第一次加载
    private boolean isFirstLoading = true;

    private LinearLayout mine_login_btn;
    private String phone, user;
    private int userid;
    private House house;

    private LinearLayout mine_info_unlog;
    private LinearLayout mine_info_log;
    private ImageView mine_avater;
    private TextView mine_nickname;
    private Button mine_person_page;
    private LinearLayout mine_history;
    private LinearLayout mine_like;
    private LinearLayout mine_order;
    private LinearLayout mine_myhouse;
    private TextView mine_report;
    private TextView mine_csd;

    private ImageView mine_setting_btn;
    private LinearLayout mine_userinfo;

    private SharedPreferences mSharedPreferences;

    private Gson gson;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mineViewModel =
                new ViewModelProvider(this).get(MineViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mine, container, false);
//        final TextView textView = root.findViewById(R.id.text_mine);
//        mineViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        mSharedPreferences = getActivity().getSharedPreferences("User", 0);

        gson = new Gson();

        mine_setting_btn = root.findViewById(R.id.mine_setting_btn);
        mine_login_btn = root.findViewById(R.id.mine_login_btn);

        mine_info_unlog = root.findViewById(R.id.mine_info_unlog);
        mine_info_log = root.findViewById(R.id.mine_info_log);
        mine_avater = root.findViewById(R.id.mine_avater);
        mine_nickname = root.findViewById(R.id.mine_nickname);
        mine_person_page = root.findViewById(R.id.mine_person_page);
        mine_userinfo = root.findViewById(R.id.mine_userinfo);

        mine_history = root.findViewById(R.id.mine_history);
        mine_like = root.findViewById(R.id.mine_like);
        mine_order = root.findViewById(R.id.mine_order);
        mine_myhouse = root.findViewById(R.id.mine_myhouse);
        mine_report = root.findViewById(R.id.mine_report);
        mine_csd = root.findViewById(R.id.mine_csd);

        mine_setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineFragment.this.getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });

        mine_userinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineFragment.this.getActivity(), UserInfoActivity.class);
                startActivity(intent);
            }
        });

        mine_person_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MineFragment.this.getActivity(), "个人主页", Toast.LENGTH_SHORT).show();
            }
        });
        mine_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MineFragment.this.getActivity(), "浏览历史", Toast.LENGTH_SHORT).show();
            }
        });
        mine_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MineFragment.this.getActivity(), "收藏关注", Toast.LENGTH_SHORT).show();
            }
        });
        mine_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MineFragment.this.getActivity(), "全部订单", Toast.LENGTH_SHORT).show();
            }
        });
        mine_myhouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MineFragment.this.getActivity(), "我的房产", Toast.LENGTH_SHORT).show();
            }
        });
        mine_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MineFragment.this.getActivity(), "举报中心", Toast.LENGTH_SHORT).show();
            }
        });
        mine_csd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MineFragment.this.getActivity(), "在线客服", Toast.LENGTH_SHORT).show();
            }
        });



        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences settings = getContext().getSharedPreferences("User", 0);
        phone = settings.getString("phone", "");
        userid = settings.getInt("userid", 0);

        if (userid == 0) {
            // 未登录
            mine_info_unlog.setVisibility(View.VISIBLE);
            mine_info_log.setVisibility(View.GONE);

            mine_login_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Toast.makeText(MineFragment.this.getActivity(), "登录", Toast.LENGTH_SHORT).show();
                    Intent active = new Intent(MineFragment.this.getActivity(), LoginActivity.class);
//                house_detail_active.putExtra("house", new Gson().toJson(house));
                    startActivity(active);
                }
            });

        } else {
            mine_info_unlog.setVisibility(View.GONE);
            mine_info_log.setVisibility(View.VISIBLE);
            user = settings.getString("user", "");
            house = gson.fromJson(user, House.class);
            Glide.with(getContext())
                    .load(getContext().getString(R.string.host) + getContext().getString(R.string.image_url_avater) + house.getUser_avater())
                    .placeholder(R.drawable.ic_avater_default).error(R.drawable.ic_avater_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(mine_avater);
            mine_nickname.setText(house.getUser_nick());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences settings = getContext().getSharedPreferences("User", 0);
        phone = settings.getString("phone", "");
        userid = settings.getInt("userid", 0);

        if (userid == 0) {
            // 未登录
            mine_info_unlog.setVisibility(View.VISIBLE);
            mine_info_log.setVisibility(View.GONE);

            mine_login_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Toast.makeText(MineFragment.this.getActivity(), "登录", Toast.LENGTH_SHORT).show();
                    Intent active = new Intent(MineFragment.this.getActivity(), LoginActivity.class);
//                house_detail_active.putExtra("house", new Gson().toJson(house));
                    startActivity(active);
                }
            });

        } else {
            mine_info_unlog.setVisibility(View.GONE);
            mine_info_log.setVisibility(View.VISIBLE);
            user = settings.getString("user", "");
            house = gson.fromJson(user, House.class);
            Glide.with(getContext())
                    .load(getContext().getString(R.string.host) + getContext().getString(R.string.image_url_avater) + house.getUser_avater())
                    .placeholder(R.drawable.ic_avater_default).error(R.drawable.ic_avater_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(mine_avater);
            mine_nickname.setText(house.getUser_nick());
        }
    }
}