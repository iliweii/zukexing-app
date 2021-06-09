//package com.zukexing.app.ui.home;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//
//import java.util.List;
//
//public class RecoAdapter extends ArrayAdapter<House> {
//    private int resourceId;
//    public RecoAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<House> objects) {
//        super(context, textViewResourceId, objects);
//        resourceId=textViewResourceId;
//    }
//    public View getView(int position, View convertview, ViewGroup parent){
//        House house = getItem(position);
//
//        View view;
//        ViewHolder viewHolder;
//        if(convertview==null){
//            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
//
//            viewHolder=new ViewHolder();
//            viewHolder.house_select = view.findViewById(R.id.house_select);
//            view.setTag(viewHolder);
//        }else {
//            view = convertview;
//            viewHolder = (ViewHolder)view.getTag();
//        }
//        viewHolder.house_select.setText(house.getHouseName());
//        return view;
//    }
//    class ViewHolder {
//        TextView house_select;
//    }
//}
