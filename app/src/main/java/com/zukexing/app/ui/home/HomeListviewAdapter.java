package com.zukexing.app.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zukexing.app.R;
import com.zukexing.app.pojo.House;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class HomeListviewAdapter extends ArrayAdapter<House> {
    private int resourceId;
    private Context context;

    public HomeListviewAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<House> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        this.context = context;
    }

    public View getView(int position, View convertview, ViewGroup parent){
        House house = getItem(position);
        Gson gson = new Gson();
        View view;
        ViewHolder viewHolder;
        if (convertview == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

            viewHolder = new ViewHolder();
            viewHolder.house_item_image = view.findViewById(R.id.house_item_image);
            viewHolder.house_item_title = view.findViewById(R.id.house_item_title);
            viewHolder.house_item_local = view.findViewById(R.id.house_item_local);
            viewHolder.house_item_type_mini = view.findViewById(R.id.house_item_type_mini);
            viewHolder.house_item_type_short = view.findViewById(R.id.house_item_type_short);
            viewHolder.house_item_type_long = view.findViewById(R.id.house_item_type_long);
            viewHolder.house_item_price = view.findViewById(R.id.house_item_price);
            viewHolder.house_item_type = view.findViewById(R.id.house_item_type);
            viewHolder.house_item_area = view.findViewById(R.id.house_item_area);
            view.setTag(viewHolder);
        } else {
            view = convertview;
            viewHolder = (ViewHolder)view.getTag();
        }

        List<String> image_names = gson.fromJson(house.getHouse_img(), new TypeToken<List<String>>(){}.getType());
        String image_name = image_names.get(0);
        Glide.with(this.context)
                .load(this.context.getString(R.string.host) + this.context.getString(R.string.image_url_house) + image_name)
                .placeholder(R.drawable.house_default).error(R.drawable.house_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
                .into(viewHolder.house_item_image);

        viewHolder.house_item_title.setText(house.getHouse_title());
        viewHolder.house_item_local.setText(house.getHouse_local());
        String house_type = house.getRent_type();
        if (house_type.indexOf("mini") == -1) {
            viewHolder.house_item_type_mini.setText("");
            viewHolder.house_item_type_mini.setPadding(0, 0, 0, 0);
        }
        if (house_type.indexOf("short") == -1) {
            viewHolder.house_item_type_short.setText("");
            viewHolder.house_item_type_short.setPadding(0, 0, 0, 0);
        }
        if (house_type.indexOf("long") == -1) {
            viewHolder.house_item_type_long.setText("");
            viewHolder.house_item_type_long.setPadding(0, 0, 0, 0);
        }
        if (house.getShort_price() != null) {
            viewHolder.house_item_price.setText("¥" + house.getShort_price() + "/晚");
        } else if (house.getMini_price() != null) {
            viewHolder.house_item_price.setText("¥" + house.getMini_price() + "/小时");
        } else if (house.getLong_price() != null) {
            viewHolder.house_item_price.setText("¥" + house.getLong_price() + "/周");
        } else {
            viewHolder.house_item_price.setText("价格待定");
        }

        viewHolder.house_item_type.setText(house.getHouse_type());
        viewHolder.house_item_area.setText(house.getHouse_area() + "㎡");

        return view;
    }

    class ViewHolder {
        ImageView house_item_image;
        TextView house_item_title;
        TextView house_item_local;
        TextView house_item_type_mini;
        TextView house_item_type_short;
        TextView house_item_type_long;
        TextView house_item_price;
        TextView house_item_type;
        TextView house_item_area;
    }

}
