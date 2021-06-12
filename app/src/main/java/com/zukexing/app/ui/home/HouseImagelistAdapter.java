package com.zukexing.app.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zukexing.app.R;
import com.zukexing.app.pojo.House;

import java.util.List;

public class HouseImagelistAdapter extends RecyclerView.Adapter<HouseImagelistAdapter.ViewHolder> {

    private List <String> image_names;
    private View view;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView house_image_item_img;
        TextView house_image_item_index;

        public ViewHolder(@NonNull View view) {
            super(view);

            house_image_item_img = view.findViewById(R.id.house_image_item_img);
            house_image_item_index = view.findViewById(R.id.house_image_item_index);
        }
    }

    public  HouseImagelistAdapter (List <String> image_names){
        this.image_names = image_names;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.house_image_item, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String image_name = image_names.get(position);
        Glide.with(view.getContext())
            .load(view.getContext().getString(R.string.host) + view.getContext().getString(R.string.image_url_house) + image_name)
            .placeholder(R.drawable.house_default).error(R.drawable.house_default)
            .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
//                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
            .into(holder.house_image_item_img);
        holder.house_image_item_index.setText((position + 1) + "/" + getItemCount());

    }

    @Override
    public int getItemCount() {
        return image_names.size();
    }

}