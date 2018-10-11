package com.imstuding.www.handwyu.ToolUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wan on 2016/10/12.
 * 自定义标题栏，用来显示周一到周日
 */
public class WeekTitle extends View {

    //保存当前的日期
    private int day;
    private Date m_date=new Date();
    private Paint mPaint;

    private String[] days = {"日","一", "二", "三", "四", "五", "六"};

    public WeekTitle(Context context) {
        super(context);
    }

    public WeekTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        day = 0;
        mPaint = new Paint();
    }

    /**
     * 重写测量函数，否则在设置wrap_content的时候默认为match_parent
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setCurrentDay(int day)
    {
        this.day = day;
    }

    public void setDate(Date date){
        this.m_date=date;
    }

    public int isHighDisplay(){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        /*if (screenHeight>=1441||screenWidth>=1080){
            return true;
        }else if(screenHeight<=1440||screenWidth<=720){
            return false;
        }*/

        if (screenHeight>=1920||screenWidth>=1080) {
            return 1;
        }else if((screenHeight>900&&screenHeight<1920)||(screenWidth>500&&screenWidth<1080)){
            return 2;
        }else if(screenHeight<=900||screenWidth<=500){
            return 3;
        }

        return 1;
    }

    public void onDraw(Canvas canvas) {
        int sel=isHighDisplay();
        if (sel==1){
            highOnDraw(canvas);
        }else if(sel==2) {
            midOndraw(canvas);
        }else if(sel==3){
            lowOnDraw(canvas);
        }
        //调用父类的绘图方法
        super.onDraw(canvas);
    }

    public void midOndraw(Canvas canvas){
//获得当前View的宽度
        int width = getWidth();
        float offset = width / 8;
        float currentPosition = 1.25f*offset;
        //设置要绘制的字体
        mPaint.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));

        mPaint.setColor(Color.rgb(80, 80, 80));
        midDrawDate(canvas,currentPosition,offset);
        mPaint.setTextSize(17);
        for(int i = 0; i < 7 ; i++)
        {
            if (day==i){
                mPaint.setColor(Color.rgb(255, 80, 80));
                canvas.drawText("周"+days[i], currentPosition, 15, mPaint);
                mPaint.setColor(Color.rgb(80, 80, 80));
            }else {
                canvas.drawText("周"+days[i], currentPosition, 15, mPaint);
            }

            currentPosition += offset;
        }
    }

    public void midDrawDate(Canvas canvas,float currentPosition,float offset){
        mPaint.setColor(Color.rgb(0, 0, 0));
        mPaint.setTextSize(30);
        canvas.drawText((m_date.getMonth()+1)+"月",0,35,mPaint);
        mPaint.setTextSize(16);
        mPaint.setColor(Color.rgb(80, 80, 80));
        int month=m_date.getMonth();
        //m_date.setTime(m_date.getTime()-24*3*60*60*1000);
        for (int i=0;i<7;i++){
            m_date.setTime(m_date.getTime()+24*i*60*60*1000);
            if (month!=m_date.getMonth()){
                month=m_date.getMonth();
                canvas.drawText((m_date.getMonth()+1)+"月", currentPosition, 40, mPaint);
            }else {
                canvas.drawText(m_date.getDate()+"日", currentPosition, 40, mPaint);
            }

            m_date.setTime(m_date.getTime()-24*i*60*60*1000);
            currentPosition += offset;
        }

    }

    public void highOnDraw(Canvas canvas){
        //获得当前View的宽度
        int width = getWidth();
        float offset = width / 8;
        float currentPosition = 1.25f*offset;
        //设置要绘制的字体
        mPaint.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));

        mPaint.setColor(Color.rgb(80, 80, 80));
        highDrawDate(canvas,currentPosition,offset);
        mPaint.setTextSize(35);
        for(int i = 0; i < 7 ; i++)
        {
            if (day==i){
                mPaint.setColor(Color.rgb(255, 80, 80));
                canvas.drawText("周"+days[i], currentPosition, 35, mPaint);
                mPaint.setColor(Color.rgb(80, 80, 80));
            }else {
                canvas.drawText("周"+days[i], currentPosition, 35, mPaint);
            }

            currentPosition += offset;
        }
    }

    public void highDrawDate(Canvas canvas,float currentPosition,float offset){
        mPaint.setColor(Color.rgb(0, 0, 0));
        mPaint.setTextSize(60);
        canvas.drawText((m_date.getMonth()+1)+"月",0,50,mPaint);
        mPaint.setTextSize(32);
        mPaint.setColor(Color.rgb(80, 80, 80));
        int month=m_date.getMonth();
        //m_date.setTime(m_date.getTime()-24*3*60*60*1000);
        for (int i=0;i<7;i++){
            m_date.setTime(m_date.getTime()+24*i*60*60*1000);
            if (month!=m_date.getMonth()){
                month=m_date.getMonth();
                canvas.drawText((m_date.getMonth()+1)+"月", currentPosition, 75, mPaint);
            }else {
                canvas.drawText(m_date.getDate()+"日", currentPosition, 75, mPaint);
            }

            m_date.setTime(m_date.getTime()-24*i*60*60*1000);
            currentPosition += offset+2;
        }

    }

    public void lowOnDraw(Canvas canvas){
        //获得当前View的宽度
        int width = getWidth();
        float offset = width / 8;
        float currentPosition = 1.25f*offset;
        //设置要绘制的字体
        mPaint.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));

        mPaint.setColor(Color.rgb(80, 80, 80));
        lowDrawDate(canvas,currentPosition,offset);
        mPaint.setTextSize(12);
        for(int i = 0; i < 7 ; i++)
        {
            if (day==i){
                mPaint.setColor(Color.rgb(255, 80, 80));
                canvas.drawText("周"+days[i], currentPosition, 10, mPaint);
                mPaint.setColor(Color.rgb(80, 80, 80));
            }else {
                canvas.drawText("周"+days[i], currentPosition, 10, mPaint);
            }

            currentPosition += offset;
        }
    }

    public void lowDrawDate(Canvas canvas,float currentPosition,float offset){
        mPaint.setColor(Color.rgb(0, 0, 0));
        mPaint.setTextSize(20);
        canvas.drawText((m_date.getMonth()+1)+"月",0,20,mPaint);
        mPaint.setTextSize(12);
        mPaint.setColor(Color.rgb(80, 80, 80));
        int month=m_date.getMonth();
        //m_date.setTime(m_date.getTime()-24*3*60*60*1000);
        for (int i=0;i<7;i++){
            m_date.setTime(m_date.getTime()+24*i*60*60*1000);
            if (month!=m_date.getMonth()){
                month=m_date.getMonth();
                canvas.drawText((m_date.getMonth()+1)+"月", currentPosition, 25, mPaint);
            }else {
                canvas.drawText(m_date.getDate()+"日", currentPosition, 25, mPaint);
            }

            m_date.setTime(m_date.getTime()-24*i*60*60*1000);
            currentPosition += offset;
        }

    }


}
