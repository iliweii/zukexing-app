package com.zukexing.app.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zukexing.app.R;
import com.zukexing.app.pojo.House;
import com.zukexing.app.pojo.Rented;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DashboardListviewAdapter extends ArrayAdapter<Rented> {
    private int resourceId;
    private Context context;

    public DashboardListviewAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Rented> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        this.context = context;
    }

    public View getView(int position, View convertview, ViewGroup parent){
        Rented rented = getItem(position);
        Gson gson = new Gson();
        View view;
        ViewHolder viewHolder;
        if (convertview == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

            viewHolder = new ViewHolder();
            viewHolder.order_item = view.findViewById(R.id.order_item);
            viewHolder.order_item_title = view.findViewById(R.id.order_item_title);
            viewHolder.order_item_status = view.findViewById(R.id.order_item_status);
            viewHolder.order_item_image = view.findViewById(R.id.order_item_image);
            viewHolder.order_item_type = view.findViewById(R.id.order_item_type);
            viewHolder.order_item_time = view.findViewById(R.id.order_item_time);
            viewHolder.order_item_dept = view.findViewById(R.id.order_item_dept);
            viewHolder.order_item_price = view.findViewById(R.id.order_item_price);
            viewHolder.order_item_cim = view.findViewById(R.id.order_item_cim);
            view.setTag(viewHolder);
        } else {
            view = convertview;
            viewHolder = (ViewHolder)view.getTag();
        }

        List<String> image_names = gson.fromJson(rented.getHouse_img(), new TypeToken<List<String>>(){}.getType());
        String image_name = image_names.get(0);

        //设置图片圆角角度
        RoundedCorners roundedCorners= new RoundedCorners(6);
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        RequestOptions options= RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
        Glide.with(this.context)
                .load(this.context.getString(R.string.host) + this.context.getString(R.string.image_url_house) + image_name)
                .placeholder(R.drawable.house_default).error(R.drawable.house_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
                .apply(options)
                .into(viewHolder.order_item_image);

        viewHolder.order_item_title.setText(rented.getHouse_title());
        if (rented.getStatus() == 1) {
            viewHolder.order_item_status.setText("待付款");
        } else if (rented.getStatus() == 2) {
            viewHolder.order_item_status.setText("已付款待确认");
        } else if (rented.getStatus() == 3) {
            viewHolder.order_item_status.setText("预定成功");
        } else if (rented.getStatus() == 4) {
            viewHolder.order_item_status.setText("已退款");
        } else if (rented.getStatus() == 5) {
            viewHolder.order_item_status.setText("已改签");
        } else if (rented.getStatus() == 6) {
            viewHolder.order_item_status.setText("已消费");
        }
        viewHolder.order_item_type.setText(rented.getHouse_type());

        SimpleDateFormat sy1 = new SimpleDateFormat("yyyy-MM-dd");
        Date start = new Date(rented.getStart_time());
        Date end = new Date(rented.getEnd_time());
        viewHolder.order_item_time.setText(sy1.format(start) + " - " + sy1.format(end));
        viewHolder.order_item_dept.setText("押金：" + rented.getPay_d());
        viewHolder.order_item_price.setText("总价：" + (rented.getPay() - rented.getPay_d()));

        viewHolder.order_item_cim.setText("评价");
        viewHolder.order_item_cim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "评价", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    class ViewHolder {
        LinearLayout order_item;
        TextView order_item_title;
        TextView order_item_status;
        ImageView order_item_image;
        TextView order_item_type;
        TextView order_item_time;
        TextView order_item_dept;
        TextView order_item_price;
        TextView order_item_cim;
    }

}
