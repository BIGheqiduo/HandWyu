package com.imstuding.www.handwyu.ToolAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.ExamInf;

import java.util.List;

/**
 * Created by yangkui on 2018/10/4.
 */

public class ExamAdapter extends BaseAdapter {

    private List<ExamInf> object;//考试安排对象
    private Context mContext;
    private int resourceId;//布局文件

    public ExamAdapter(Context mContext, int resourceId, List<ExamInf> object) {
        super();
        this.object = object;
        this.resourceId=resourceId;
        this.mContext = mContext;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ExamInf examInf=object.get(position);
        View view= LayoutInflater.from(mContext).inflate(resourceId,null);

        TextView item_kcmc=(TextView)view.findViewById(R.id.item_exam_kcmc);
        TextView item_detail=(TextView)view.findViewById(R.id.item_exam_detail);

        String detail=examInf.getKsrq()+"  "+examInf.getKssj()+"  在"+examInf.getKscdmc();

        item_kcmc.setText(examInf.getKcmc());
        item_detail.setText(detail);
        return view;
    }

}
