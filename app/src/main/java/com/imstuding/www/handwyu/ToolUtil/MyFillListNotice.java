package com.imstuding.www.handwyu.ToolUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import static com.imstuding.www.handwyu.ToolUtil.DatabaseHelper.db_version;

/**
 * Created by yangkui on 2018/9/7.
 */

public class MyFillListNotice {
    private Context mContext;
    private DatabaseHelper dbhelp;
    public MyFillListNotice(Context mContext){
        this.mContext=mContext;
        dbhelp =new DatabaseHelper(mContext,"course.db",null,db_version);
    }

    public String getMaxId(){
        String id=new String();
        SQLiteDatabase db=dbhelp.getReadableDatabase();
        String sql="select max(id) from notice ";
        Cursor cursor=db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            id=cursor.getString(0);
        }
        if (id==null)
            id="0";
        return id;
    }

    public void fillList(List<MyNotice> object){
        SQLiteDatabase db=dbhelp.getReadableDatabase();
        String sql="select * from notice ";
        Cursor cursor=db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            String id= cursor.getString(0);
            String title= cursor.getString(1);
            String context= cursor.getString(2);
            String time= cursor.getString(3);
            String flag= cursor.getString(4);

            object.add(new MyNotice(id,title,context,time,Boolean.parseBoolean(flag)));
        }
    }

    public void setNoticeData(List<MyNotice> object){
        SQLiteDatabase db=dbhelp.getWritableDatabase();
        for (int i=0;i<object.size();i++){
            db.execSQL("insert into notice values(?,?,?,?,?)",new String[]{object.get(i).getId(),object.get(i).getTitle(),object.get(i).getContent(),object.get(i).getTime(),object.get(i).isFlag()+""});
        }
        db.close();
    }

    public int getNoticeCount(){
        int count=0;
        SQLiteDatabase db=dbhelp.getReadableDatabase();
        String sql="select flag from notice ";
        Cursor cursor=db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            String flag= cursor.getString(0);
            if (flag.equals("true")){
                count++;
            }
        }
        return count;
    }
    public void setRead(int position){
        SQLiteDatabase db=dbhelp.getWritableDatabase();
        db.execSQL("update notice set flag=? where id=?",new String[]{"false",position+""});
        db.close();
    }
}
