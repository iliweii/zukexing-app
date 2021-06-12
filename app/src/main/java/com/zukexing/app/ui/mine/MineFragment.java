package com.zukexing.app.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.zukexing.app.R;
import com.zukexing.app.ui.home.HomeFragment;
import com.zukexing.app.ui.home.HouseDetailActivity;
import com.zukexing.app.ui.login.LoginActivity;

public class MineFragment extends Fragment {

    private MineViewModel mineViewModel;

    private LinearLayout mini_login_btn;

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

        mini_login_btn = root.findViewById(R.id.mini_login_btn);

        mini_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MineFragment.this.getActivity(), "登录", Toast.LENGTH_SHORT).show();
                Intent active = new Intent(MineFragment.this.getActivity(), LoginActivity.class);
//                house_detail_active.putExtra("house", new Gson().toJson(house));
                startActivity(active);
            }
        });

        return root;
    }
}