package com.imstuding.www.handwyu.WidgetSmall;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.imstuding.www.handwyu.R;

/**
 * Implementation of App Widget functionality.
 */
public class MyCourseWidgetSmall extends AppWidgetProvider {

    public static final String CHANGE_IMAGE = "com.imstuding.www.handwyu.Widget.action.CHANGE_IMAGE";
    private Intent startUpdateIntent=null;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_course_widget_small);

        //设置adapter
        Intent intent=new Intent(context,MyRemoteViewsServiceSmall.class);
        views.setRemoteAdapter(R.id.w_courceDetail_small,intent);

        // 点击列表触发事件
        Intent clickIntent = new Intent(context, MyCourseWidgetSmall.class);
        // 设置Action，方便在onReceive中区别点击事件
        clickIntent.setAction(CHANGE_IMAGE);

        clickIntent.setData(Uri.parse(clickIntent.toUri(Intent.URI_INTENT_SCHEME)));
        // Instruct the widget manager to update the widget
        PendingIntent pendingIntentTemplate = PendingIntent.getBroadcast(
                context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setPendingIntentTemplate(R.id.w_courceDetail_small,
                pendingIntentTemplate);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        if (startUpdateIntent==null)
            startUpdateIntent = new Intent(context, MyServiceSmall.class);
        context.startService(startUpdateIntent);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        //startUpdateIntent = new Intent(context, MyServiceSmall.class);
        if (startUpdateIntent==null)
            startUpdateIntent = new Intent(context, MyServiceSmall.class);
        context.startService(startUpdateIntent);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        if (startUpdateIntent==null)
            startUpdateIntent = new Intent(context, MyServiceSmall.class);
        context.stopService(startUpdateIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (startUpdateIntent==null)
            startUpdateIntent = new Intent(context, MyServiceSmall.class);
        context.startService(startUpdateIntent);
        String action = intent.getAction();
        if (action.equals(CHANGE_IMAGE)) {
            // 单击Wdiget中ListView的某一项会显示一个Toast提示。
            String content=intent.getStringExtra("content");
            if (!content.isEmpty()){
                Intent startAcIntent = new Intent();
                startAcIntent.setComponent(new ComponentName("com.imstuding.www.handwyu","com.imstuding.www.handwyu.MainUi.MainActivity"));//第一个是包名，第二个是类所在位置的全称
                startAcIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(startAcIntent);
            }
        }else if (action.equals(Intent.ACTION_SCREEN_ON)){
            //Toast.makeText(context,"开屏" , Toast.LENGTH_SHORT).show();
        }else if (action.equals(Intent.ACTION_SCREEN_OFF)){
           // Toast.makeText(context,"锁屏" , Toast.LENGTH_SHORT).show();
        }else if (action.equals(Intent.ACTION_USER_PRESENT)){
           // Toast.makeText(context,"解锁" , Toast.LENGTH_SHORT).show();
        }else {

        }
        super.onReceive(context, intent);
    }


}

