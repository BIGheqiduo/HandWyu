package com.imstuding.www.handwyu.ToolUtil;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.imstuding.www.handwyu.R;

/**
 * Created by yangkui on 2018/10/4.
 */

public class ExamDetailDlg {
    private Context mcontext;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private ExamInf examInf;
    private TextView T_kcmc;//考试名称
    private TextView T_jkteaxms;//监考老师
    private TextView T_ksaplxmc;//安排类型
    private TextView T_kscdmc;//考试地点
    private TextView T_zc;//周次
    private TextView T_xq;//星期
    private TextView T_ksrq;//考试日期
    private TextView T_kssj;//考试时间


    public ExamDetailDlg(Context mcontext, ExamInf examInf){
        this.mcontext=mcontext;
        this.examInf=examInf;
    }

    public void show(){
        builder=new AlertDialog.Builder(mcontext);
        Activity activity=(Activity)mcontext;
        LayoutInflater inflater =activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.exam_detail, null);
        initDlg(view);//初始化数据
        builder.setView(view);
        builder.setCancelable(true);
        alertDialog=builder.create();
        alertDialog.show();
    }
    public void initDlg(View view){
        try{
            T_kcmc= (TextView) view.findViewById(R.id.exam_detail_kcmc);
            T_jkteaxms= (TextView) view.findViewById(R.id.exam_detail_jkteaxms);
            T_ksaplxmc= (TextView) view.findViewById(R.id.exam_detail_ksaplxmc);
            T_kscdmc= (TextView) view.findViewById(R.id.exam_detail_kscdmc);
            T_zc= (TextView) view.findViewById(R.id.exam_detail_zc);
            T_xq= (TextView) view.findViewById(R.id.exam_detail_xq);
            T_ksrq= (TextView) view.findViewById(R.id.exam_detail_ksrq);
            T_kssj= (TextView) view.findViewById(R.id.exam_detail_kssj);


            T_kcmc.setText(examInf.getKcmc());
            T_jkteaxms.setText(examInf.getJkteaxms());
            T_ksaplxmc.setText(examInf.getKsaplxmc());
            T_kscdmc.setText(examInf.getKscdmc());
            T_zc.setText(examInf.getZc());
            T_xq.setText(examInf.getXq());
            T_ksrq.setText(examInf.getKsrq());
            T_kssj.setText(examInf.getKssj());


        }catch (Exception e){
            T_kcmc.setText("NULL");
            T_jkteaxms.setText("NULL");
            T_ksaplxmc.setText("NULL");
            T_kscdmc.setText("NULL");
            T_zc.setText("NULL");
            T_xq.setText("NULL");
            T_ksrq.setText("NULL");
            T_kssj.setText("NULL");
        }
    }
}
