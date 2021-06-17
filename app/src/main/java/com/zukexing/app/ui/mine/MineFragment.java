package com.zukexing.app.ui.mine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    private LinearLayout mini_login_btn;
    private String phone, user;
    private int userid;
    private House house;

    private LinearLayout mini_info_unlog;
    private LinearLayout mini_info_log;
    private ImageView mini_avater;
    private TextView mini_nickname;
    private Button mini_person_page;

    private ImageView mine_setting_btn;

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

        Gson gson = new Gson();

        mine_setting_btn = root.findViewById(R.id.mine_setting_btn);
        mini_login_btn = root.findViewById(R.id.mini_login_btn);

        mini_info_unlog = root.findViewById(R.id.mini_info_unlog);
        mini_info_log = root.findViewById(R.id.mini_info_log);
        mini_avater = root.findViewById(R.id.mini_avater);
        mini_nickname = root.findViewById(R.id.mini_nickname);
        mini_person_page = root.findViewById(R.id.mini_person_page);

        SharedPreferences settings = getContext().getSharedPreferences("User", 0);
        phone = settings.getString("phone", "");
        userid = settings.getInt("userid", 0);

        if (userid == 0) {
            // 未登录
            mini_info_unlog.setVisibility(View.VISIBLE);
            mini_info_log.setVisibility(View.GONE);

            mini_login_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Toast.makeText(MineFragment.this.getActivity(), "登录", Toast.LENGTH_SHORT).show();
                    Intent active = new Intent(MineFragment.this.getActivity(), LoginActivity.class);
//                house_detail_active.putExtra("house", new Gson().toJson(house));
                    startActivity(active);
                }
            });

        } else {
            mini_info_unlog.setVisibility(View.GONE);
            mini_info_log.setVisibility(View.VISIBLE);
            user = settings.getString("user", "");
            house = gson.fromJson(user, House.class);
            Glide.with(getContext())
                .load(getContext().getString(R.string.host) + getContext().getString(R.string.image_url_avater) + house.getUser_avater())
                .placeholder(R.drawable.ic_avater_default).error(R.drawable.ic_avater_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(mini_avater);
            mini_nickname.setText(house.getUser_nick());
        }

        mine_setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineFragment.this.getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });








        return root;
    }

    /**
     * 在fragment可见的时候，刷新数据
     */
    @Override
    public void onResume() {
        super.onResume();

        if (!isFirstLoading) {
            //如果不是第一次加载，刷新数据
            mini_info_unlog.setVisibility(View.GONE);
            mini_info_log.setVisibility(View.VISIBLE);
        }

        isFirstLoading = false;
    }
}