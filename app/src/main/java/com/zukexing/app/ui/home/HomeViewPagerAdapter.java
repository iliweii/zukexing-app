package com.zukexing.app.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.zukexing.app.R;

import java.util.ArrayList;
import java.util.List;

public class HomeViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<Integer> mData;
    private ArrayList<String> mTitle;

    public HomeViewPagerAdapter(Context context, ArrayList<Integer> list, ArrayList<String> title) {
        mContext = context;
        mData = list;
        mTitle = title;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(mContext, mData.get(position),null);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container,position,object); 这一句要删除，否则报错
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle.get(position);//页卡标题
    }
}