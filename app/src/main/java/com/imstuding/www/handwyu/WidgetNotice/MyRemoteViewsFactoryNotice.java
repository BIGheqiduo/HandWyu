package com.imstuding.www.handwyu.WidgetNotice;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.imstuding.www.handwyu.MainUi.TableFragment;
import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.Course;
import com.imstuding.www.handwyu.ToolUtil.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.imstuding.www.handwyu.MainUi.TableFragment.getDaySub;
import static com.imstuding.www.handwyu.ToolUtil.DatabaseHelper.db_version;

/**
 * Created by yangkui on 2018/10/21.
 */

public class MyRemoteViewsFactoryNotice implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private RemoteViews rv=null;
    private List<Course> courseList;
    private int count=0;

    /*
     * 构造函数
     */
    public MyRemoteViewsFactoryNotice(Context context, Intent intent) {
        mContext = context;
        courseList=new LinkedList<>();
        getListNotice();
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        //String zc=getWeek();
        getListNotice();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        rv = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_notice_item);
        Course course=courseList.get(position);
        rv.setTextViewText(R.id.item_notice_jcdm,course.getJs());
        rv.setTextViewText(R.id.item_notice_kcmc,course.getKcmc());
        rv.setTextViewText(R.id.item_notice_jxcdmc,course.getJxcdmc());
        Intent intent = new Intent();
        // 传入点击行的数据
        //intent.putExtra("content", (CharSequence) getItem(position));
        rv.setOnClickFillInIntent(R.id.widget_notice_list_item, intent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return rv;
    }

    @Override
    public int getViewTypeCount() {
        return count;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    public void getListNotice(){
        DatabaseHelper dbhelp=new DatabaseHelper(mContext,"course.db",null,db_version);
        SQLiteDatabase db=dbhelp.getReadableDatabase();
        String jxcdmc,kcmc,jcdm;
        String zc=getWeek();
        if (zc==null){
            return;
        }
        count=0;
        String xq= TableFragment.getWeekOfDate(new Date());
        List<Map<String,String>> data=new ArrayList<Map<String,String>>();
        try{
            Cursor cursor= db.rawQuery("select * from course where zc=? and xq=? and year=?",new String[]{zc,xq,getTerm()});
            while (cursor.moveToNext()){
                jxcdmc= cursor.getString(0);
                String teaxms= cursor.getString(1);
                xq= cursor.getString(2);
                jcdm= cursor.getString(3);
                kcmc= cursor.getString(4);
                zc= cursor.getString(5);
                String jxbmc= cursor.getString(7);
                String sknrjj= cursor.getString(8);
                String js=jcdm;

                Course course=new Course(kcmc,jxcdmc,teaxms,zc,js,jxbmc,sknrjj);
                courseList.add(count,course);

                Map<String,String> map=new HashMap<String, String>();
                map.put("kcmc",kcmc);
                map.put("jcdm",jcdm);
                map.put("jxcdmc",jxcdmc);
                data.add(map);
                count++;
            }
            myOrder(data);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void myOrder(List<Map<String,String>> src){
        for (int i=0;i<src.size()-1;i++){
            for (int j=i+1;j<src.size();j++){
                int number1= Integer.parseInt(src.get(i).get("jcdm").substring(0,4)) ;
                int number2= Integer.parseInt(src.get(j).get("jcdm").substring(0,4)) ;
                Map<String,String> map1=src.get(i);
                Map<String,String> map2=src.get(j);
                if (number1>number2){
                    src.set(i,map2);
                    src.set(j,map1);
                    Course tmp=courseList.get(i);//课程也要排序
                    courseList.set(i,courseList.get(j));
                    courseList.set(j,tmp);
                }
            }
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
        DatabaseHelper dbhelp=new DatabaseHelper(mContext,"course.db",null,db_version);
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



}
