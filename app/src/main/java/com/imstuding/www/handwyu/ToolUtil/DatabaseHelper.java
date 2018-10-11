package com.imstuding.www.handwyu.ToolUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    //每改一次数据库增加一
    public static int db_version=3;
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //课程信息表
        db.execSQL("create table course(" +
                "jxcdmc text," +
                "teaxms text," +
                "xq text," +
                "jcdm text," +
                "kcmc text," +
                "zc text," +
                "year text,"+
                "jxbmc text,"+
                "sknrjj text)");
        //日期表
        db.execSQL("create table week(" +
                "xq text," +
                "rq text," +
                "zc text)");

        //成绩表
        db.execSQL("create table score(" +
                "kcmc text," +//课程名称
                "zcj text," +//总成绩
                "xf text," +//学分
                "xdfsmc text," +//修读方式
                "cjjd text," +//绩点
                "kcbh text," +//课程编号
                "zxs text," +//学时
                "cjfsmc text," +//成绩方式
                "pscj text)");//平时成绩

        db.execSQL("create table notice(" +
                "id int," +     //通知的id
                "title text," +  //通知的标题
                "content text," +    //通知的内容
                "time text," +       //时间
                "flag text)");       //是否已读
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i=oldVersion+1;i<=newVersion;i++){
            switch (i) {
                case 1:{
                    //什么也不做
                    break;
                }
                case 2:{
                    //第二个版本的数据库增加两个字段。
                    String sql1="ALTER TABLE course ADD jxbmc text";
                    String sql2="ALTER TABLE course ADD sknrjj text";
                    db.execSQL(sql1);
                    db.execSQL(sql2);

                    db.execSQL("create table score(" +
                            "kcmc text," +//课程名称
                            "zcj text," +//总成绩
                            "xf text," +//学分
                            "xdfsmc text," +//修读方式
                            "cjjd text," +//绩点
                            "kcbh text," +//课程编号
                            "zxs text," +//学时
                            "cjfsmc text," +//成绩方式
                            "pscj text)");//平时成绩
                    break;
                }
                case 3:{
                    db.execSQL("create table notice(" +
                            "id int," +     //通知的id
                            "title text," +  //通知的标题
                            "content text," +    //通知的内容
                            "time text," +       //时间
                            "flag text)");       //是否已读

                    break;
                }
                default:
                    break;
            }

        }

    }
}
