package com.imstuding.www.handwyu.CourseDetailUi;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.imstuding.www.handwyu.AddCourse.SendWidgetRefresh;
import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.Course;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.imstuding.www.handwyu.MainUi.TableFragment.DEL_UPDATE_DLG;

/**
 * Created by yangkui on 2018/10/27.
 */

public class DelCourseDlg {
    private Context mcontext;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private SimpleAdapter simpleAdapter;
    private ListView list_course;
    private List<Course> courseList;
    private DelCourseBroadcastReceiver mbcr;
    private List<Map<String,String>> data;
    public DelCourseDlg(Context context,List<Course> courseList){
        this.courseList=courseList;
        mcontext=context;
        regsterDelBroadcast();
    }

    public void show(){
        builder=new AlertDialog.Builder(mcontext);
        Activity activity=(Activity)mcontext;
        LayoutInflater inflater =activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.course_list_del, null);
        initDlg(view);//初始化数据
        builder.setView(view);
        builder.setCancelable(true);
        alertDialog=builder.create();
        alertDialog.show();
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                unRegsterDelBroadcast();
            }
        });
    }



    public void initDlg(View view){
        list_course= (ListView) view.findViewById(R.id.course_detail_list);
       data=new ArrayList<Map<String,String>>();
        for (int i=0;i<courseList.size();i++){
            Map<String,String> map=new HashMap<String, String>();
            map.put("kcmc",courseList.get(i).getKcmc());
            map.put("js",courseList.get(i).getJxcdmc()+"，"+courseList.get(i).getJs());
            map.put("teaxms",courseList.get(i).getTeaxms());
            data.add(map);
        }

        setOrUpdateSimpleAdapter(data);
        list_course.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                WheatherDeleteCourse wheatherDeleteCourse=new WheatherDeleteCourse(mcontext,courseList,position);
                wheatherDeleteCourse.show();
            }
        });
    }

    public void setOrUpdateSimpleAdapter(List<Map<String,String>> data){
        simpleAdapter=new SimpleAdapter(mcontext,data,R.layout.list_course_item,
                new String[]{"kcmc","js","teaxms"},new int[]{R.id.item_course_kcmc,R.id.item_course_js,R.id.item_course_teaxms});
        list_course.setAdapter(simpleAdapter);
    }

    class DelCourseBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int position = intent.getIntExtra("position",-1);
            if (position==-1){
                Toast.makeText(mcontext,"有问题，错误代码0x01",Toast.LENGTH_SHORT).show();
                return;
            }
            data.remove(position);
            setOrUpdateSimpleAdapter(data);
        }
    }

    private void regsterDelBroadcast(){
        mbcr = new DelCourseBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DEL_UPDATE_DLG);
        mcontext.registerReceiver(mbcr, filter);// 注册
    }

    private void unRegsterDelBroadcast(){
        mcontext.unregisterReceiver(mbcr);
        mbcr = null;
    }




}
