package com.imstuding.www.handwyu.ToolUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imstuding.www.handwyu.R;
import com.jauker.widget.BadgeView;

import java.util.List;

/**
 * Created by yangkui on 2018/9/6.
 */

public class MyNoticeAdapter extends BaseAdapter {
    private Context mContext;
    private int resourceId;//布局文件
    private List<MyNotice> object;
    private TextView wyunotice_title;
    private TextView wyunotice_time;
    private TextView wyunotice_content;

    public MyNoticeAdapter(Context mContext, int resourceId, List<MyNotice> object) {
        this.resourceId=resourceId;
        this.mContext = mContext;
        this.object = object;

    }

    @Override
    public int getCount() {
        return object.size();
    }

    @Override
    public Object getItem(int position) {
        return object.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view= LayoutInflater.from(mContext).inflate(resourceId,null);
        wyunotice_title= (TextView) view.findViewById(R.id.item_wyunotice_title);
        wyunotice_time= (TextView) view.findViewById(R.id.item_wyunotice_time);
        wyunotice_content= (TextView) view.findViewById(R.id.item_wyunotice_content);

        wyunotice_title.setText(object.get(position).getTitle());
        wyunotice_time.setText(object.get(position).getTime());
        wyunotice_content.setText(object.get(position).getContent());

        if (object.get(position).isFlag()){
            BadgeView badge = new BadgeView(mContext);
            badge.setTargetView(wyunotice_title);
            badge.setBadgeGravity(Gravity.RIGHT|Gravity.TOP);
            badge.setText("未读");
        }
        return view;
    }


}
