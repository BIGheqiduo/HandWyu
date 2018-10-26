package com.imstuding.www.handwyu.ToolUtil;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.imstuding.www.handwyu.MainUi.MainActivity;
import com.imstuding.www.handwyu.R;

import java.util.Calendar;


/**
 * Created by yangkui on 2018/10/24.
 */

public class NoticeUtil {

    private Context context;
    private NotificationManager manager;
    private int notification_id;

    public NoticeUtil(Context context){
        this.context=context;
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private void sendNotice(String title,String text,String ticker){
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setTicker(ticker);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle(title);
        builder.setContentText(text);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent ma = PendingIntent.getActivity(context,0,intent,0);
        builder.setContentIntent(ma);//设置点击过后跳转的activity

        builder.setDefaults(Notification.DEFAULT_ALL);//设置全部

        Notification notification = builder.build();//4.1以上用.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;// 点击通知的时候cancel掉
        manager.notify(notification_id,notification);
    }

    public void Notice(int count){
       /* Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        NoticeUtil noticeUtil=new NoticeUtil(context);
        noticeUtil.sendNotice("上课提醒","今天有课上哦，不要错过了","今天有"+c.get(Calendar.HOUR_OF_DAY)+"门课");
        */
        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        if (getDayFlag()){
            if (c.get(Calendar.HOUR_OF_DAY)>=7){
                setDayFlag(false);
                NoticeUtil noticeUtil=new NoticeUtil(context);
                noticeUtil.sendNotice("上课提醒","今天有课上哦，不要错过了，今天有"+count+"门课。","今天有"+count+"门课");
            }
        }else {
            if (c.get(Calendar.HOUR_OF_DAY)<7){
                setDayFlag(true);
            }
        }
    }

    private void setDayFlag(boolean flag){
        context.getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit().putBoolean("dayFlag",flag).commit();
    }

    private boolean getDayFlag(){
       return context.getSharedPreferences("userInfo", Context.MODE_PRIVATE).getBoolean("dayFlag",true);
    }

}
