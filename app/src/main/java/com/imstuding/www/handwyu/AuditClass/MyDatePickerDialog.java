package com.imstuding.www.handwyu.AuditClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.imstuding.www.handwyu.R;

import static com.imstuding.www.handwyu.AuditClass.AuditSearchDlg.DATE_FILL_DLG;

/**
 * Created by yangkui on 2018/10/30.
 */

public class MyDatePickerDialog {

    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private Context mcontext;
    private Button btn_yes;
    private Button btn_cancel;
    private DatePicker datePicker;

    public MyDatePickerDialog(Context context){
        this.mcontext=context;
    }

    public void show(){
        builder=new AlertDialog.Builder(mcontext);
        Activity activity=(Activity)mcontext;
        LayoutInflater inflater =activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.my_datepicker_dlg, null);
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
                int year= datePicker.getYear();
                int month=datePicker.getMonth()+1;
                int day=datePicker.getDayOfMonth();
                String s_month=new String();
                if (month>=1&&month<=9){
                    s_month = "0"+month;
                }else {
                    s_month= ""+month;
                }

                //发广播
                Intent intent=new Intent(DATE_FILL_DLG);
                intent.putExtra("date",year+"-"+s_month+"-"+day);
                mcontext.sendBroadcast(intent);

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

        datePicker= (DatePicker) view.findViewById(R.id.datePicker);
    }

}

