package com.imstuding.www.handwyu.CourseDetailUi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.imstuding.www.handwyu.AddCourse.SendWidgetRefresh;
import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.Course;
import com.imstuding.www.handwyu.ToolUtil.DatabaseHelper;

import java.util.List;

import static com.imstuding.www.handwyu.MainUi.TableFragment.DEL_UPDATE_DLG;
import static com.imstuding.www.handwyu.MainUi.TableFragment.DEL_UPDATE_TABLE;
import static com.imstuding.www.handwyu.ToolUtil.DatabaseHelper.db_version;

/**
 * Created by yangkui on 2018/10/27.
 */

public class WheatherDeleteCourse {
    private Context mcontext;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private List<Course> courseList;
    private TextView T_kcmc;
    private Button btn_cancel;
    private Button btn_yes;
    private int position=-1;
    public WheatherDeleteCourse(Context mcontext, List<Course> courseList,int position) {
        this.mcontext = mcontext;
        this.courseList=courseList;
        this.position=position;

    }

    public void show() {
        builder = new AlertDialog.Builder(mcontext);
        Activity activity = (Activity) mcontext;
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.wheather_course, null);
        initDlg(view);//初始化数据
        builder.setView(view);
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void initDlg(View view) {
        try {
            final Course course=courseList.get(position);
            T_kcmc = (TextView) view.findViewById(R.id.wheather_kcmc);
            btn_cancel= (Button) view.findViewById(R.id.wheather_cancel);
            btn_yes= (Button) view.findViewById(R.id.wheather_yes);

            String string="是否删除第"+course.getZc()+"周,"+course.getJs()+","+course.getTeaxms()+"老师的"+course.getKcmc()+" ?";
            T_kcmc.setText(string);
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteCourse(course.getKcmc(),course.getZc(),course.getJcdm(),course.getXq(),course.getTeaxms());
                    //发广播
                    Intent intent=new Intent(DEL_UPDATE_TABLE);
                    intent.putExtra("zc",Integer.parseInt(course.getZc()));
                    mcontext.sendBroadcast(intent);

                    Intent intent2=new Intent(DEL_UPDATE_DLG);
                    intent2.putExtra("position",position);
                    mcontext.sendBroadcast(intent2);

                    //发送广播更新widget
                    SendWidgetRefresh.widgetRefresh(mcontext);

                    courseList.remove(position);
                    Toast.makeText(mcontext,"删除成功！",Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            });

        } catch (Exception e) {
            T_kcmc.setText("NULL");
        }
    }

    public void deleteCourse(String kcmc,String zc,String jcdm,String xq,String teaxms){
        DatabaseHelper dbhelp=new DatabaseHelper(mcontext,"course.db",null,db_version);
        SQLiteDatabase db=dbhelp.getWritableDatabase();
        db.execSQL("delete from course where kcmc = ? and zc = ? and jcdm = ? and xq =? and teaxms = ?",new String[]{kcmc,zc,jcdm,xq,teaxms});
    }
}