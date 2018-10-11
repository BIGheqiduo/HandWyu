package com.imstuding.www.handwyu.WidgetSmall;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.imstuding.www.handwyu.MainUi.TableFragment.getDaySub;
import static com.imstuding.www.handwyu.ToolUtil.DatabaseHelper.db_version;

/**
 * Created by yangkui on 2018/8/21.
 */

public class MyRemoteViewsFactorySmall implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private String[][] contents;
    private int columnTotal;
    private int rowTotal;
    private RemoteViews rv=null;
    private DatabaseHelper dbhelp;
    private SQLiteDatabase db;

    /*
     * 构造函数
     */
    public MyRemoteViewsFactorySmall(Context context, Intent intent) {
        columnTotal=7;
        rowTotal=6;
        mContext = context;

    }


    @Override
    public void onCreate() {
        dbhelp=new DatabaseHelper(mContext,"course.db",null,db_version);
        db=dbhelp.getReadableDatabase();

        String zc=getWeek();
        setTableCourse(zc);
    }

    @Override
    public void onDataSetChanged() {
        String zc=getWeek();
        setTableCourse(zc);

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return rowTotal*columnTotal;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        rv = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_grid_item_small);

        // 设置要显示的内容
        if( !getItem(position).equals("")) {
            rv.setTextColor(R.id.coure_text_small,Color.WHITE);
            rv.setTextViewText(R.id.coure_text_small, (CharSequence) getItem(position));
            //变换颜色

            int prand=position%7;
            int rand = getWeekOfDate(new Date());

            switch( prand ) {
                case 0:
                    if (prand==rand){
                        rv.setTextColor(R.id.coure_text_small,Color.parseColor("#FFFFFF"));
                        rv.setInt(R.id.coure_text_small,"setBackgroundResource",R.drawable.bg_red);
                    }else {
                        rv.setInt(R.id.coure_text_small,"setBackgroundResource",R.drawable.bg_12);
                    }
                    break;
                case 1:
                    if (prand==rand){
                        rv.setTextColor(R.id.coure_text_small,Color.parseColor("#FFFFFF"));
                        rv.setInt(R.id.coure_text_small,"setBackgroundResource",R.drawable.bg_red);
                    }else {
                        rv.setInt(R.id.coure_text_small,"setBackgroundResource",R.drawable.bg_13);
                    }
                    break;
                case 2:
                    if (prand==rand){
                        rv.setTextColor(R.id.coure_text_small,Color.parseColor("#FFFFFF"));
                        rv.setInt(R.id.coure_text_small,"setBackgroundResource",R.drawable.bg_red);
                    }else {
                        rv.setInt(R.id.coure_text_small,"setBackgroundResource",R.drawable.bg_14);
                    }
                    break;
                case 3:
                    if (prand==rand){
                        rv.setTextColor(R.id.coure_text_small,Color.parseColor("#FFFFFF"));
                        rv.setInt(R.id.coure_text_small,"setBackgroundResource",R.drawable.bg_red);
                    }else {
                        rv.setInt(R.id.coure_text_small,"setBackgroundResource",R.drawable.bg_15);
                    }
                    break;
                case 4:
                    if (prand==rand){
                        rv.setTextColor(R.id.coure_text_small,Color.parseColor("#FFFFFF"));
                        rv.setInt(R.id.coure_text_small,"setBackgroundResource",R.drawable.bg_red);
                    }else {
                        rv.setInt(R.id.coure_text_small,"setBackgroundResource",R.drawable.bg_16);
                    }
                    break;
                case 5:
                    if (prand==rand){
                        rv.setTextColor(R.id.coure_text_small,Color.parseColor("#FFFFFF"));
                        rv.setInt(R.id.coure_text_small,"setBackgroundResource",R.drawable.bg_red);
                    }else {
                        rv.setInt(R.id.coure_text_small,"setBackgroundResource",R.drawable.bg_17);
                    }
                    break;
                case 6:
                    if (prand==rand){
                        rv.setTextColor(R.id.coure_text_small,Color.parseColor("#FFFFFF"));
                        rv.setInt(R.id.coure_text_small,"setBackgroundResource",R.drawable.bg_red);
                    }else {
                        rv.setInt(R.id.coure_text_small,"setBackgroundResource",R.drawable.bg_18);
                    }
                    break;
            }


        }else {
            rv.setTextViewText(R.id.coure_text_small, "");
            rv.setInt(R.id.coure_text_small,"setBackgroundResource",R.drawable.grid_item_bg);
        }

        // 填充Intent，填充在AppWdigetProvider中创建的PendingIntent
        Intent intent = new Intent();
        // 传入点击行的数据
        intent.putExtra("content", (CharSequence) getItem(position));
        rv.setOnClickFillInIntent(R.id.coure_text_small, intent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return rv;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public Object getItem(int position) {
        //求余得到二维索引
        int column = position % columnTotal;
        //求商得到二维索引
        int row = position / columnTotal;
        return contents[row][column];
    }

    private void initCourse(int a,int b){
        contents = new String[a][b];
        for (int i=0;i<a;i++){
            for (int j=0;j<b;j++){
                contents[i][j]="";
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

    public void setTableCourse(String s_zc) {
        //清空数据
        initCourse(6,7);
        //根据数据库信息设置课程信息
        String sql="select jxcdmc,xq,jcdm,kcmc from course where zc="+s_zc+" and year="+getTerm();
        Cursor cursor=db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            String jxcdmc= cursor.getString(0);
            String xq= cursor.getString(1);
            String jcdm= cursor.getString(2);
            String kcmc= cursor.getString(3);

            int i_xq=Integer.parseInt(xq);
            int section[]=getSection(jcdm);
            for (int i=0;i<section.length;i++){
                if (section[i]!=100)
                    contents[section[i]][i_xq%7]+=kcmc+"@"+jxcdmc;
            }
        }
    }

    //获取当前是第几节课
    public int[] getSection(String string){
        int count=string.length()/2;//生成节数

        int section[]=new int[count];
        for (int i=0;i<count;i++){
            section[i]=100;
        }//初始化

        for (int i=0,j=0;i<count*2;i+=4){
            if ((i+4)<count*2)
                section[j++]=getOneSection(string.substring(i,i+4));
            else
                section[j++]=getOneSection(string.substring(i,count*2));
        }
        return section;
    }

    public int getHalfSection(String string){
        int section=0;
        switch (string){
            case "01":{
                section=0;
                break;
            }
            case "02":{
                section=0;
                break;
            }
            case "03":{
                section=1;
                break;
            }
            case "04":{
                section=1;
                break;
            }
            case "05":{
                section=2;
                break;
            }
            case "06":{
                section=2;
                break;
            }
            case "07":{
                section=3;
                break;
            }
            case "08":{
                section=3;
                break;
            }
            case "09":{
                section=4;
                break;
            }
            case "10":{
                section=4;
                break;
            }
            case "11":{
                section=5;
                break;
            }
            case "12":{
                section=5;
                break;
            }
            default:{
                section=0;
                break;
            }
        }
        return section;
    }

    public int getOneSection(String string){
        int section=0;
        switch (string){
            case "0102":{
                section=0;
                break;
            }
            case "0304":{
                section=1;
                break;
            }
            case "0506":{
                section=2;
                break;
            }
            case "0708":{
                section=3;
                break;
            }
            case "0910":{
                section=4;
                break;
            }
            case "1112":{
                section=5;
                break;
            }
            default:{
                section=getHalfSection(string);
                break;
            }
        }
        return section;
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
