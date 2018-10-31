package com.imstuding.www.handwyu.ToolUtil;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.imstuding.www.handwyu.MainUi.MainActivity;
import com.imstuding.www.handwyu.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


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
       //判断是否开启了上课提醒
        int hour= context.getSharedPreferences("userInfo", Context.MODE_PRIVATE).getInt("autoNoticeHour",7);
        int minute = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE).getInt("autoNoticeMinute",30);
        minute-=15;//由于时间不准确，所以减15分钟

        Date date=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String s_flag=sdf.format(date);

        if (getNoticeFlag()){
            Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
            if (!s_flag.equals(getDayFlag())){
                int d_hour=c.get(Calendar.HOUR_OF_DAY);
                if (d_hour>=hour&&c.get(Calendar.MINUTE)>=minute){
                    setDayFlag(s_flag);
                    NoticeUtil noticeUtil=new NoticeUtil(context);
                    if (count==0){
                        noticeUtil.sendNotice("上课提醒","今天没有课，可以放松一下啦！。","今天没课啦");
                    }else {
                        noticeUtil.sendNotice("上课提醒","今天有课上哦，不要错过了，今天有"+count+"门课。","今天有"+count+"门课");
                    }

                }
            }
       }
    }

    private void setDayFlag(String s_flag){
        context.getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit().putString("dayFlag",s_flag).commit();
    }

    private String getDayFlag(){
       return context.getSharedPreferences("userInfo", Context.MODE_PRIVATE).getString("dayFlag","2018年10月29");
    }
    private boolean getNoticeFlag(){
        return context.getSharedPreferences("userInfo", Context.MODE_PRIVATE).getBoolean("autoNotice",true);
    }

}
