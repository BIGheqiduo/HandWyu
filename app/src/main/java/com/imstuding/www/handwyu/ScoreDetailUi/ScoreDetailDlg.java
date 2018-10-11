package com.imstuding.www.handwyu.ScoreDetailUi;

/**
 * Created by yangkui on 2018/6/21.
 */
import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.SubJect;


public class ScoreDetailDlg {

    private Context mcontext;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private SubJect subject;
    private TextView T_kcbh;//课程编号
    private TextView T_kcmc;//课程名称
    private TextView T_zcj;//总成绩
    private TextView T_cjjd;//绩点
    private TextView T_zxs;//总学时
    private TextView T_xf;//学分
    private TextView T_xdfsmc;//修读方式名称
    private TextView T_cjfsmc;//成绩方式名称
    private TextView T_pscj;//平时成绩

    public ScoreDetailDlg(Context mcontext, SubJect subject){
        this.mcontext=mcontext;
        this.subject=subject;
    }

    public void show(){
        builder=new AlertDialog.Builder(mcontext);
        Activity activity=(Activity)mcontext;
        LayoutInflater inflater =activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.score_detail, null);
        initDlg(view);//初始化数据
        builder.setView(view);
        builder.setCancelable(true);
        alertDialog=builder.create();
        alertDialog.show();
    }
    public void initDlg(View view){
        try{
            T_kcbh= (TextView) view.findViewById(R.id.score_detail_kcbh);
            T_kcmc= (TextView) view.findViewById(R.id.score_detail_kcmc);
            T_zcj= (TextView) view.findViewById(R.id.score_detail_zcj);
            T_cjjd= (TextView) view.findViewById(R.id.score_detail_cjjd);
            T_zxs= (TextView) view.findViewById(R.id.score_detail_zxs);
            T_xf= (TextView) view.findViewById(R.id.score_detail_xf);
            T_xdfsmc= (TextView) view.findViewById(R.id.score_detail_xdfsmc);
            T_cjfsmc= (TextView) view.findViewById(R.id.score_detail_cjfsmc);
            T_pscj= (TextView) view.findViewById(R.id.score_detail_pscj);

            T_kcbh.setText(subject.getKcbh());
            T_kcmc.setText(subject.getKcmc());
            T_zcj.setText(subject.getZcj());
            T_cjjd.setText(subject.getCjjd());
            T_zxs.setText(subject.getZxs());
            T_xf.setText(subject.getXf());
            T_xdfsmc.setText(subject.getXdfsmc());
            T_cjfsmc.setText(subject.getCjfsmc());
            T_pscj.setText(subject.getPscj());

        }catch (Exception e){
            T_kcbh.setText("NULL");
            T_kcmc.setText("NULL");
            T_zcj.setText("NULL");
            T_cjjd.setText("NULL");
            T_zxs.setText("NULL");
            T_xf.setText("NULL");
            T_xdfsmc.setText("NULL");
            T_cjfsmc.setText("NULL");
            T_pscj.setText("NULL");
        }
    }
}
