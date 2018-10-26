package com.imstuding.www.handwyu.WidgetNotice;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.widget.RemoteViews;


import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.imstuding.www.handwyu.MainUi.TableFragment.getDaySub;
import static com.imstuding.www.handwyu.ToolUtil.DatabaseHelper.db_version;

public class MyServiceNotice extends Service {
    private Timer timer;
    private TimerTask task;
    private int count=0;
    public MyServiceNotice() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

        final AppWidgetManager mgr = AppWidgetManager.getInstance(MyServiceNotice.this);
        final ComponentName cn = new ComponentName(MyServiceNotice.this,MyCourseWidgetNotice.class);
        final RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_course_notice);
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {

                getListNotice();
                if (count!=0){
                    views.setTextViewText(R.id.widget_notice_kcsl,"今天有"+count+"节课");
                }else {
                    views.setTextViewText(R.id.widget_notice_kcsl,"今天没有课");
                }

                // 这句话会调用RemoteViewSerivce中RemoteViewsFactory的onDataSetChanged()方法。
                mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn),
                       R.id.widget_notice_list);

                AppWidgetManager awm = AppWidgetManager.getInstance(MyServiceNotice.this);
                awm.updateAppWidget(cn, views);
            }
        };

        timer.schedule(task, 0, 60000);
        super.onCreate();
    }

    public void getListNotice(){
        DatabaseHelper dbhelp=new DatabaseHelper(getApplicationContext(),"course.db",null,db_version);
        SQLiteDatabase db=dbhelp.getReadableDatabase();
        String zc=getWeek();
        if (zc==null){
            return;
        }
        count=0;
        String xq= getWeekOfDate(new Date());
        try{
            Cursor cursor= db.rawQuery("select * from course where zc=? and xq=? and year=?",new String[]{zc,xq,getTerm()});
            while (cursor.moveToNext()){
                count++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getTerm(){
        String year,term;
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        int month=d.getMonth()+1;
        year=sdf.format(d);
        if (month>=2&&month<=7){
            int cy=Integer.parseInt(year);
            int by=cy-1;
            term=by+"02";
        }else {
            if (month>=8){
                int cy=Integer.parseInt(year);
                term=cy+"01";
            }else {
                int cy=Integer.parseInt(year);
                int by=cy-1;
                term=by+"01";
            }
        }
        return term;
    }

    //设置周次
    public String getWeek(){
        DatabaseHelper dbhelp=new DatabaseHelper(getApplicationContext(),"course.db",null,db_version);
        SQLiteDatabase db=dbhelp.getReadableDatabase();
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String n_rq=sdf.format(d);
        String xq=null;
        String o_rq=null;
        String zc=null;
        try{
            Cursor cursor= db.rawQuery("select * from week",null);
            while (cursor.moveToNext()){
                xq = cursor.getString(0);
                o_rq = cursor.getString(1);
                zc = cursor.getString(2);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        long countDay=getDaySub(o_rq,n_rq);
        int countweek= (int) (countDay/7);
        int extreeday=(int)(countDay%7);
        int i_zc=Integer.parseInt(zc);
        int i_xq=Integer.parseInt(xq)%7;
        if (i_xq+extreeday>6){
            i_zc = i_zc+countweek;
            i_zc++;
        }else {
            i_zc = i_zc+countweek;
        }

        return i_zc+"";
    }

    public String getWeekOfDate(Date date) {
        int[] weekDays = { 7, 1, 2, 3, 4, 5, 6 };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w]+"";
    }

}
