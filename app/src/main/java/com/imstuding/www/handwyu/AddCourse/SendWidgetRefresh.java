package com.imstuding.www.handwyu.AddCourse;

import android.content.Context;
import android.content.Intent;

import com.imstuding.www.handwyu.WidgetBig.MyCourseWidgetBig;
import com.imstuding.www.handwyu.WidgetNotice.MyCourseWidgetNotice;
import com.imstuding.www.handwyu.WidgetSmall.MyCourseWidgetSmall;

import static com.imstuding.www.handwyu.WidgetSmall.MyCourseWidgetSmall.WIDGET_UPDATE;

/**
 * Created by yangkui on 2018/10/24.
 */

public class SendWidgetRefresh {

    public static void widgetRefresh(Context mcontext){
        try{
            Intent sendIntent1=new Intent(mcontext, MyCourseWidgetSmall.class);
            sendIntent1.setAction(WIDGET_UPDATE);
            mcontext.sendBroadcast(sendIntent1);

            Intent sendIntent2=new Intent(mcontext, MyCourseWidgetBig.class);
            sendIntent2.setAction(WIDGET_UPDATE);
            mcontext.sendBroadcast(sendIntent2);

            Intent sendIntent3=new Intent(mcontext, MyCourseWidgetNotice.class);
            sendIntent3.setAction(WIDGET_UPDATE);
            mcontext.sendBroadcast(sendIntent3);

        }catch (Exception e){

        }
    }
}
