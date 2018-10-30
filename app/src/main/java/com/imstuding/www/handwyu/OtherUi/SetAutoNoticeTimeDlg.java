package com.imstuding.www.handwyu.OtherUi;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.imstuding.www.handwyu.R;

/**
 * Created by yangkui on 2018/10/28.
 */

public class SetAutoNoticeTimeDlg {

    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private Context mcontext;
    private Button btn_yes;
    private Button btn_cancel;
    private TimePicker auto_notice_timepicker;

    public SetAutoNoticeTimeDlg(Context context){
        this.mcontext=context;
    }

    public void show(){
        builder=new AlertDialog.Builder(mcontext);
        Activity activity=(Activity)mcontext;
        LayoutInflater inflater =activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.set_auto_notice_dlg, null);
        initDlg(view);//初始化数据
        builder.setView(view);
        builder.setCancelable(true);
        alertDialog=builder.create();
        alertDialog.show();
    }

    public void initDlg(View view){
        btn_yes= (Button) view.findViewById(R.id.waining_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour=auto_notice_timepicker.getCurrentHour();
                int minute=auto_notice_timepicker.getCurrentMinute();
                mcontext.getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit().putInt("autoNoticeHour",hour).commit();
                mcontext.getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit().putInt("autoNoticeMinute",minute).commit();
                Toast.makeText(mcontext,"已设置提醒时间为"+hour+"时"+minute+"分",Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        btn_cancel= (Button) view.findViewById(R.id.waining_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        auto_notice_timepicker= (TimePicker) view.findViewById(R.id.auto_notice_timepicker);
        auto_notice_timepicker.setIs24HourView(true);

        int hour= mcontext.getSharedPreferences("userInfo", Context.MODE_PRIVATE).getInt("autoNoticeHour",7);
        int minute = mcontext.getSharedPreferences("userInfo", Context.MODE_PRIVATE).getInt("autoNoticeMinute",30);
        auto_notice_timepicker.setCurrentHour(hour);
        auto_notice_timepicker.setCurrentMinute(minute);

    }

}

