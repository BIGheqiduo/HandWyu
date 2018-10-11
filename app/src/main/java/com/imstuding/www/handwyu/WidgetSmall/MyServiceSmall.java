package com.imstuding.www.handwyu.WidgetSmall;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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


public class MyServiceSmall extends Service {
    private Timer timer;
    private TimerTask task;
    private MyCourseWidgetSmall receiver=null;
    final int[]  w_z={R.id.w_qi,R.id.w_yi,R.id.w_er,R.id.w_san,R.id.w_si,R.id.w_wu,R.id.w_liu};
    public MyServiceSmall() {
        receiver=new MyCourseWidgetSmall();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        if (receiver==null)
            receiver=new MyCourseWidgetSmall();
            try{
                IntentFilter filter = new IntentFilter();
                filter.addAction(Intent.ACTION_SCREEN_OFF);
                filter.addAction(Intent.ACTION_SCREEN_ON);
                filter.addAction(Intent.ACTION_USER_PRESENT);
                filter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
                filter.addAction(Intent.ACTION_DATE_CHANGED);
                filter.addAction(Intent.ACTION_BATTERY_CHANGED);
                this.registerReceiver(receiver, filter);
            }catch (Exception e ){
                e.printStackTrace();
            }

        final AppWidgetManager mgr = AppWidgetManager.getInstance(MyServiceSmall.this);
        final ComponentName cn = new ComponentName(MyServiceSmall.this,MyCourseWidgetSmall.class);
        final RemoteViews views = new RemoteViews(getPackageName(), R.layout.my_course_widget_small);
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                Date d = new Date();

                // 这句话会调用RemoteViewSerivce中RemoteViewsFactory的onDataSetChanged()方法。
                mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn),
                        R.id.w_courceDetail_small);

                views.setTextViewText(R.id.w_text_zc_small,"第"+getWeek(d)+"周");
                for (int i=0;i<7;i++){
                    int j=getWeekOfDate(d);
                    if (i==j)
                        views.setTextColor(w_z[i], Color.RED);
                    else
                        views.setTextColor(w_z[i], Color.WHITE);
                }

                AppWidgetManager awm = AppWidgetManager.getInstance(MyServiceSmall.this);
                awm.updateAppWidget(cn, views);
            }
        };
        timer.schedule(task, 0, 60000);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        task.cancel();
        timer = null;
        task = null;
        //this.unregisterReceiver(receiver);
    }

    public String getWeek(Date d){
        DatabaseHelper dbhelp=new DatabaseHelper(MyServiceSmall.this,"course.db",null,db_version);
        SQLiteDatabase db=dbhelp.getReadableDatabase();

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

    /**
     * 获取当前日期是星期几<br>
     *
     * @param date
     * @return 当前日期是星期几
     */
    public int getWeekOfDate(Date date) {
        int[] weekDays = { 0, 1, 2, 3, 4, 5, 6 };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

}
