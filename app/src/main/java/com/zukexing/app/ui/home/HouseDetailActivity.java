package com.zukexing.app.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zukexing.app.R;
import com.zukexing.app.pojo.House;
import com.zukexing.app.ui.login.LoginActivity;
import com.zukexing.app.ui.mine.VerifiedActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.ClipData.newIntent;
import static android.view.View.GONE;

public class HouseDetailActivity extends AppCompatActivity {

    private ImageView house_detail_back;
    private ImageView house_detail_like;
    private ImageView house_detail_share;
    private RecyclerView house_image_list;
    private TextView house_detail_price1;
    private TextView house_detail_price2;
    private TextView house_detail_price3;
    private TextView house_detail_select1;
    private TextView house_detail_select2;
    private TextView house_detail_select3;
    private TextView house_detail_type;
    private TextView house_detail_area;
    private TextView house_detail_floor;
    private TextView house_detail_dir;
    private TextView house_detail_title;
    private TextView house_detail_local;
    private TextView house_detail_intro;
    private TextView house_attr_bingxiang;
    private TextView house_attr_xiyiji;
    private TextView house_attr_reshuiqi;
    private TextView house_attr_kuandai;
    private TextView house_attr_shafa;
    private TextView house_attr_youyanji;
    private TextView house_attr_ranqizao;
    private TextView house_attr_kezuofan;
    private TextView house_attr_dianshi;
    private TextView house_attr_kongtiao;
    private TextView house_attr_yigui;
    private TextView house_attr_chaung;
    private TextView house_attr_weishengjian;
    private TextView house_attr_yangtai;
    private TextView house_attr_nuanqi;
    private TextView house_attr_chaunghu;
    private TextView house_attr_dianti;
    private TextView house_attr_canju;
    private TextView house_attr_chuju;
    private TextView house_attr_xidiyongju;
    private TextView house_attr_wifi;
    private TextView house_attr_chuifengji;
    private Button house_book_btn;

    private TextView house_detail_username;
    private ImageView house_detail_avater;
    private Button house_detail_chatbtn;

    private SharedPreferences settings;
    private House user;

    private int RentType = 0;
    private String houseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_detail);

        // ?????????????????????
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        SharedPreferences settings = getBaseContext().getSharedPreferences("User", 0);
        String phone = settings.getString("phone", "");
        int userid = settings.getInt("userid", 0);

        // ??????????????????????????????????????????
        if (userid == 0) {
            Intent intent = new Intent();
            intent.setClass(HouseDetailActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        String userjson = settings.getString("user", "");
        Gson gson = new Gson();
        user = gson.fromJson(userjson, House.class);

        // ??????intent ???????????????
        Intent intent = getIntent();
        houseData = intent.getStringExtra("house");
        House house = gson.fromJson(houseData, House.class);

//        System.out.println(houseData);
        house_detail_back = this.findViewById(R.id.house_detail_back);
        house_detail_like = this.findViewById(R.id.house_detail_like);
        house_detail_share = this.findViewById(R.id.house_detail_share);
        house_image_list = this.findViewById(R.id.house_image_list);
        house_detail_price1 = this.findViewById(R.id.house_detail_price1);
        house_detail_price2 = this.findViewById(R.id.house_detail_price2);
        house_detail_price3 = this.findViewById(R.id.house_detail_price3);
        house_detail_select1 = this.findViewById(R.id.house_detail_select1);
        house_detail_select2 = this.findViewById(R.id.house_detail_select2);
        house_detail_select3 = this.findViewById(R.id.house_detail_select3);
        house_detail_type = this.findViewById(R.id.house_detail_type);
        house_detail_area = this.findViewById(R.id.house_detail_area);
        house_detail_floor = this.findViewById(R.id.house_detail_floor);
        house_detail_dir = this.findViewById(R.id.house_detail_dir);
        house_detail_title = this.findViewById(R.id.house_detail_title);
        house_detail_local = this.findViewById(R.id.house_detail_local);
        house_detail_intro = this.findViewById(R.id.house_detail_intro);
        house_attr_bingxiang = this.findViewById(R.id.house_attr_bingxiang);
        house_attr_xiyiji = this.findViewById(R.id.house_attr_xiyiji);
        house_attr_reshuiqi = this.findViewById(R.id.house_attr_reshuiqi);
        house_attr_kuandai = this.findViewById(R.id.house_attr_kuandai);
        house_attr_shafa = this.findViewById(R.id.house_attr_shafa);
        house_attr_youyanji = this.findViewById(R.id.house_attr_youyanji);
        house_attr_ranqizao = this.findViewById(R.id.house_attr_ranqizao);
        house_attr_kezuofan = this.findViewById(R.id.house_attr_kezuofan);
        house_attr_dianshi = this.findViewById(R.id.house_attr_dianshi);
        house_attr_kongtiao = this.findViewById(R.id.house_attr_kongtiao);
        house_attr_yigui = this.findViewById(R.id.house_attr_yigui);
        house_attr_chaung = this.findViewById(R.id.house_attr_chaung);
        house_attr_weishengjian = this.findViewById(R.id.house_attr_weishengjian);
        house_attr_yangtai = this.findViewById(R.id.house_attr_yangtai);
        house_attr_nuanqi = this.findViewById(R.id.house_attr_nuanqi);
        house_attr_chaunghu = this.findViewById(R.id.house_attr_chuanghu);
        house_attr_dianti = this.findViewById(R.id.house_attr_dianti);
        house_attr_canju = this.findViewById(R.id.house_attr_canju);
        house_attr_chuju = this.findViewById(R.id.house_attr_chuju);
        house_attr_xidiyongju = this.findViewById(R.id.house_attr_xidiyongju);
        house_attr_wifi = this.findViewById(R.id.house_attr_wifi);
        house_attr_chuifengji = this.findViewById(R.id.house_attr_chuifengji);
        house_book_btn = this.findViewById(R.id.house_book_btn);
        house_detail_username = this.findViewById(R.id.house_detail_username);
        house_detail_avater = this.findViewById(R.id.house_detail_avater);
        house_detail_chatbtn = this.findViewById(R.id.house_detail_chatbtn);

//        TransitionManager.beginDelayedTransition((ViewGroup) getWindow().getDecorView());

        house_detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        house_detail_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HouseDetailActivity.this, "??????", Toast.LENGTH_SHORT).show();
            }
        });
        house_detail_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HouseDetailActivity.this, "??????", Toast.LENGTH_SHORT).show();
            }
        });

        List<String> image_names = gson.fromJson(house.getHouse_img(), new TypeToken<List<String>>(){}.getType());
        // ???setAdapter()???????????????LayoutManager
        // ???setAdapter()???????????????LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        house_image_list.setLayoutManager(layoutManager);
        HouseImagelistAdapter houseImagelistAdapter = new HouseImagelistAdapter(image_names);
        house_image_list.setAdapter(houseImagelistAdapter);

        if (house.getShort_price() != null) {
            house_detail_price1.setText("???" + house.getShort_price() + "/???");
            house_detail_select1.setText("????????????????????????");
            RentType = 1;
        } else {
            house_detail_price1.setVisibility(GONE);
            house_detail_select1.setVisibility(GONE);
        }
        if (house.getLong_price() != null) {
            house_detail_price2.setText("???" + house.getLong_price() + "/???");
            house_detail_select2.setText("?????????");
        } else {
            house_detail_price2.setVisibility(GONE);
            house_detail_select2.setVisibility(GONE);
        }
        if (house.getMini_price() != null) {
            house_detail_price3.setText("???" + house.getMini_price() + "/??????");
            house_detail_select3.setText("?????????");
        } else {
            house_detail_price3.setVisibility(GONE);
            house_detail_select3.setVisibility(GONE);
        }

        if (house.getShort_price() != null) {
            house_detail_select1.setText("????????????????????????");
            house_book_btn.setText("???????????????????????????");
            RentType = 1;
        } else if (house.getShort_price() == null && house.getLong_price() != null) {
            house_detail_price2.setTextSize(18);
            house_detail_select2.setText("????????????????????????");
            house_book_btn.setText("???????????????????????????");
            RentType = 2;
        } else if (house.getShort_price() == null && house.getLong_price() == null) {
            house_detail_price3.setTextSize(18);
            house_detail_select3.setText("????????????????????????");
            house_book_btn.setText("???????????????????????????");
            RentType = 3;
        }

        house_detail_price1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                house_detail_select1.setText("????????????????????????");
                house_detail_select2.setText("?????????");
                house_detail_select3.setText("?????????");
                house_detail_price1.setTextSize(18);
                house_detail_price2.setTextSize(12);
                house_detail_price3.setTextSize(12);
                house_book_btn.setText("???????????????????????????");
                RentType = 1;
            }
        });

        house_detail_price2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                house_detail_select1.setText("?????????");
                house_detail_select2.setText("????????????????????????");
                house_detail_select3.setText("?????????");
                house_detail_price1.setTextSize(12);
                house_detail_price2.setTextSize(18);
                house_detail_price3.setTextSize(12);
                house_book_btn.setText("???????????????????????????");
                RentType = 2;
            }
        });

        house_detail_price3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                house_detail_select1.setText("?????????");
                house_detail_select2.setText("?????????");
                house_detail_select3.setText("????????????????????????");
                house_detail_price1.setTextSize(12);
                house_detail_price2.setTextSize(12);
                house_detail_price3.setTextSize(18);
                house_book_btn.setText("???????????????????????????");
                RentType = 3;
            }
        });

        house_detail_type.setText(house.getHouse_type());
        house_detail_area.setText(house.getHouse_area() + "???");
        house_detail_floor.setText(house.getHouse_floor() + "???");
        house_detail_dir.setText(house.getHouse_dir());
        house_detail_title.setText(house.getHouse_title());
        house_detail_local.setText(house.getHouse_local());
        house_detail_intro.setText(house.getHouse_intro());

        if (house.getBingxiang() == 1) {
            house_attr_bingxiang.setText("?????????");
        } else {
            house_attr_bingxiang.setText("????????");
            house_attr_bingxiang.setTextColor(getResources().getColor(R.color.gray_8f));
        }
        if (house.getXiyiji() == 1) {
            house_attr_xiyiji.setText("????????????");
        } else {
            house_attr_xiyiji.setText("???????????");
            house_attr_xiyiji.setTextColor(getResources().getColor(R.color.gray_8f));
        }
        if (house.getReshuiqi() == 1) {
            house_attr_reshuiqi.setText("????????????");
        } else {
            house_attr_reshuiqi.setText("???????????");
            house_attr_reshuiqi.setTextColor(getResources().getColor(R.color.gray_8f));
        }
        if (house.getKuandai() == 1) {
            house_attr_kuandai.setText("?????????");
        } else {
            house_attr_kuandai.setText("????????");
            house_attr_kuandai.setTextColor(getResources().getColor(R.color.gray_8f));
        }if (house.getShafa() == 1) {
            house_attr_shafa.setText("?????????");
        } else {
            house_attr_shafa.setText("????????");
            house_attr_shafa.setTextColor(getResources().getColor(R.color.gray_8f));
        }
        if (house.getYouyanji() == 1) {
            house_attr_youyanji.setText("????????????");
        } else {
            house_attr_youyanji.setText("???????????");
            house_attr_youyanji.setTextColor(getResources().getColor(R.color.gray_8f));
        }
        if (house.getRanqizao() == 1) {
            house_attr_ranqizao.setText("????????????");
        } else {
            house_attr_ranqizao.setText("???????????");
            house_attr_ranqizao.setTextColor(getResources().getColor(R.color.gray_8f));
        }
        if (house.getKezuofan() == 1) {
            house_attr_kezuofan.setText("????????????");
        } else {
            house_attr_kezuofan.setText("???????????");
            house_attr_kezuofan.setTextColor(getResources().getColor(R.color.gray_8f));
        }if (house.getDianshi() == 1) {
            house_attr_dianshi.setText("?????????");
        } else {
            house_attr_dianshi.setText("????????");
            house_attr_dianshi.setTextColor(getResources().getColor(R.color.gray_8f));
        }if (house.getKongtiao() == 1) {
            house_attr_kongtiao.setText("?????????");
        } else {
            house_attr_kongtiao.setText("????????");
            house_attr_kongtiao.setTextColor(getResources().getColor(R.color.gray_8f));
        }
        if (house.getYigui() == 1) {
            house_attr_yigui.setText("?????????");
        } else {
            house_attr_yigui.setText("????????");
            house_attr_yigui.setTextColor(getResources().getColor(R.color.gray_8f));
        }if (house.getChaung() == 1) {
            house_attr_chaung.setText("??????");
        } else {
            house_attr_chaung.setText("?????");
            house_attr_chaung.setTextColor(getResources().getColor(R.color.gray_8f));
        }
        if (house.getWeishengjian() == 1) {
            house_attr_weishengjian.setText("????????????");
        } else {
            house_attr_weishengjian.setText("???????????");
            house_attr_weishengjian.setTextColor(getResources().getColor(R.color.gray_8f));
        }
        if (house.getYangtai() == 1) {
            house_attr_yangtai.setText("?????????");
        } else {
            house_attr_yangtai.setText("????????");
            house_attr_yangtai.setTextColor(getResources().getColor(R.color.gray_8f));
        }
        if (house.getNuanqi() == 1) {
            house_attr_nuanqi.setText("?????????");
        } else {
            house_attr_nuanqi.setText("????????");
            house_attr_nuanqi.setTextColor(getResources().getColor(R.color.gray_8f));
        }
        if (house.getChuanghu() == 1) {
            house_attr_chaunghu.setText("?????????");
        } else {
            house_attr_chaunghu.setText("????????");
            house_attr_chaunghu.setTextColor(getResources().getColor(R.color.gray_8f));
        }
        if (house.getDianti() == 1) {
            house_attr_dianti.setText("?????????");
        } else {
            house_attr_dianti.setText("????????");
            house_attr_dianti.setTextColor(getResources().getColor(R.color.gray_8f));
        }
        if (house.getCanju() == 1) {
            house_attr_canju.setText("?????????");
        } else {
            house_attr_canju.setText("????????");
            house_attr_canju.setTextColor(getResources().getColor(R.color.gray_8f));
        }
        if (house.getChuju() == 1) {
            house_attr_chuju.setText("?????????");
        } else {
            house_attr_chuju.setText("????????");
            house_attr_chuju.setTextColor(getResources().getColor(R.color.gray_8f));
        }
        if (house.getXidiyongju() == 1) {
            house_attr_xidiyongju.setText("???????????????");
        } else {
            house_attr_xidiyongju.setText("??????????????");
            house_attr_xidiyongju.setTextColor(getResources().getColor(R.color.gray_8f));
        }
        if (house.getWifi() == 1) {
            house_attr_wifi.setText("WIFI???");
        } else {
            house_attr_wifi.setText("WIFI??");
            house_attr_wifi.setTextColor(getResources().getColor(R.color.gray_8f));
        }
        if (house.getChuifengji() == 1) {
            house_attr_chuifengji.setText("????????????");
        } else {
            house_attr_chuifengji.setText("???????????");
            house_attr_chuifengji.setTextColor(getResources().getColor(R.color.gray_8f));
        }

        String avater_name = house.getUser_avater();
        Glide.with(getBaseContext())
                .load(getBaseContext().getString(R.string.host) + getBaseContext().getString(R.string.image_url_avater) + avater_name)
                .placeholder(R.drawable.ic_avater_default).error(R.drawable.ic_avater_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(house_detail_avater);
        String real_name = house.getReal_name();
        String real_sex = house.getUser_sex();
        if (real_sex.equals("???")) {
            house_detail_username.setText(real_name.substring(0, 1) + "??????");
        } else {
            house_detail_username.setText(real_name.substring(0, 1) + "??????");
        }
        String user_phone = house.getUser_phone();
        house_detail_chatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: +86" + user_phone));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        house_book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ?????????????????????????????????????????????
                if(user.getIs_real() == 0) {
                    Intent intent = new Intent();
                    intent.setClass(HouseDetailActivity.this, VerifiedActivity.class);
                    startActivity(intent);
                    return;
                }
                // ???????????????????????????
                Intent intent1 = new Intent();
                intent1.setClass(HouseDetailActivity.this, PostOrderActivity.class);
                intent1.putExtra("house", houseData);
                intent1.putExtra("type", RentType);
                startActivity(intent1);
//                showDatePickDlg();

            }
        });


    }

    protected void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(HouseDetailActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                HouseDetailActivity.this.mEditText.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                System.out.println(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMaxDate(new Date().getTime() + 30*24*60*60*1000);  ///< ???????????????????????????
        datePicker.setMinDate(new Date().getTime());  ///< ??????????????????????????????????????????????????????long???????????????????????????
        datePickerDialog.show();

    }
}