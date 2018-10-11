package com.imstuding.www.handwyu.OtherDlg;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.Classmate;
import com.imstuding.www.handwyu.ToolUtil.Course;

/**
 * Created by yangkui on 2018/8/15.
 */

public class ClassmateDetailDlg {
    private Context mcontext;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private Classmate classmate;
    private TextView T_num;
    private TextView T_name;
    private TextView T_class;
    private TextView T_college;
    private TextView T_prof;

    public ClassmateDetailDlg(Context mcontext, Classmate classmate){
        this.mcontext=mcontext;
        this.classmate=classmate;
    }

    public void show(){
        builder=new AlertDialog.Builder(mcontext);
        Activity activity=(Activity)mcontext;
        LayoutInflater inflater =activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.classmate_detail, null);
        initDlg(view);//初始化数据
        builder.setView(view);
        builder.setCancelable(true);
        alertDialog=builder.create();
        alertDialog.show();
    }
    public void initDlg(View view){
        try{
            T_num= (TextView) view.findViewById(R.id.classmate_detail_num);
            T_name= (TextView) view.findViewById(R.id.classmate_detail_name);
            T_class= (TextView) view.findViewById(R.id.classmate_detail_class);
            T_college= (TextView) view.findViewById(R.id.classmate_detail_college);
            T_prof= (TextView) view.findViewById(R.id.classmate_detail_prof);

            T_num.setText(classmate.getStu_num());
            T_name.setText(classmate.getStu_name());
            T_class.setText(classmate.getStu_class());
            T_college.setText(classmate.getStu_college());
            T_prof.setText(classmate.getStu_prof());

        }catch (Exception e){
            T_num.setText("NULL");
            T_name.setText("NULL");
            T_class.setText("NULL");
            T_college.setText("NULL");
            T_prof.setText("NULL");

        }

    }
}
