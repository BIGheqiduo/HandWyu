package com.imstuding.www.handwyu.MainUi;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.imstuding.www.handwyu.CourseDetailUi.CourseDetailDlg;
import com.imstuding.www.handwyu.Dictionaty.DictionaryActivity;
import com.imstuding.www.handwyu.ToolUtil.Course;
import com.imstuding.www.handwyu.ToolUtil.MainFragmentTitle;
import com.imstuding.www.handwyu.ToolUtil.MyFillListNotice;
import com.imstuding.www.handwyu.WebViewDlg.LoginActivity;
import com.imstuding.www.handwyu.VolunteerDlg.CheckVolunteerState;
import com.imstuding.www.handwyu.OtherUi.OtherActivity;
import com.imstuding.www.handwyu.SchoolCard.SchoolCardActivity;
import com.imstuding.www.handwyu.OtherUi.SplashActivity;
import com.imstuding.www.handwyu.WebViewDlg.SayLoveWallActivity;
import com.imstuding.www.handwyu.WebViewDlg.WebViewActivity;
import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.DatabaseHelper;

import com.imstuding.www.handwyu.ToolUtil.UrlUtil;
import com.imstuding.www.handwyu.testUpdate;
import com.jauker.widget.BadgeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.imstuding.www.handwyu.ToolUtil.DatabaseHelper.db_version;

/**
 * Created by yangkui on 2018/3/27.
 */

public class Notice_Fragment extends Fragment {

    private int count=0;
    private TextView title_notice=null;
    private ListView list_notice=null;
    private SimpleAdapter simpleAdapter=null;
    private Button notice_examplan=null;
    private Button notice_saylove=null;
    private LinearLayout notice_autor =null;
    private Button notice_more=null;
    private Button notice_volunteer=null;
    private Button notice_search_book=null;
    private Button notice_school_card=null;
    private Button notice_bus=null;
   // private LinearLayout layout_autor_notice=null;
    private View view=null;
    private Context mContext=null;
    private List<Course> courseList;
    private MyFillListNotice fillListNotice;
    private BadgeView badge;
    private MyClickListener myClickListener;
    private MainFragmentTitle titleView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_notice,container,false);
        fillListNotice=new MyFillListNotice(mContext);
        initFragment(view);
        return view;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            reFresh();
        }
    }

    private void reFresh(){
        setListNotice();
        setTitleNotice();
    }

    public void initFragment(View view){
        myClickListener=new MyClickListener();
        courseList=new LinkedList<>();

        titleView= (MainFragmentTitle) view.findViewById(R.id.title_notice);
        titleView.setTitleText("通知");

        notice_school_card=(Button)view.findViewById(R.id.notice_school_card);
        notice_bus=(Button)view.findViewById(R.id.notice_bus);
        notice_search_book=(Button)view.findViewById(R.id.notice_search_book);
        notice_volunteer=(Button)view.findViewById(R.id.notice_volunteer);
        notice_examplan= (Button) view.findViewById(R.id.notice_examplan);
        notice_saylove=(Button) view.findViewById(R.id.notice_saylove);
        notice_autor = (LinearLayout) view.findViewById(R.id.notice_autor);
        notice_more=(Button) view.findViewById(R.id.notice_more);

        notice_bus.setOnClickListener(myClickListener);
        notice_examplan.setOnClickListener(myClickListener);
        notice_saylove.setOnClickListener(myClickListener);
        notice_autor.setOnClickListener(myClickListener);
        notice_more.setOnClickListener(myClickListener);
        notice_volunteer.setOnClickListener(myClickListener);
        notice_search_book.setOnClickListener(myClickListener);
        notice_school_card.setOnClickListener(myClickListener);
        //测试更新
         if (SplashActivity.isupdateview){
            testUpdate update=new testUpdate(mContext,false,SplashActivity.isAutoUpdate);
            update.update();
            SplashActivity.isupdateview=false;
        }

        title_notice=(TextView)view.findViewById(R.id.textview_notice);
        list_notice= (ListView) view.findViewById(R.id.list_notice);

        list_notice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CourseDetailDlg courseDetailDlg=new CourseDetailDlg(mContext,courseList.get(position));
                courseDetailDlg.show();
            }
        });

        badge = new BadgeView(mContext);
        badge.setTargetView(notice_autor);
        reFresh();
    }

    //更新listview里面的数据
    public void setOrUpdateSimpleAdapter(List<Map<String,String>> data){
        simpleAdapter=new SimpleAdapter(getActivity(),data,R.layout.list_notice_item,
                new String[]{"jcdm","kcmc","jxcdmc"},new int[]{R.id.item_notice_jcdm,R.id.item_notice_kcmc,R.id.item_notice_jxcdmc});
        list_notice.setAdapter(simpleAdapter);
    }

    //获得周次
    public String getWeek(){
        DatabaseHelper dbhelp=new DatabaseHelper(mContext,"course.db",null,db_version);
        SQLiteDatabase db=dbhelp.getReadableDatabase();
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String n_rq=sdf.format(d);
        String xq=null;
        String o_rq=null;
        String zc=null;
        boolean flag=false;
        try{
            Cursor cursor= db.rawQuery("select * from week",null);
            while (cursor.moveToNext()){
                flag=true;
                xq = cursor.getString(0);
                o_rq = cursor.getString(1);
                zc = cursor.getString(2);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if (flag==false){

            return null;
        }

        long countDay=TableFragment.getDaySub(o_rq,n_rq);
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

    public void setTitleNotice(){
        String xq=TableFragment.getWeekOfDate(new Date());
        int i_xq=Integer.parseInt(xq);
        String s_week[]=new String[]{"err","一","二","三","四","五","六","日"};
        title_notice.setText("今天周"+s_week[i_xq]+"有"+count+"节课");
    }

    public void setListNotice(){
        DatabaseHelper dbhelp=new DatabaseHelper(mContext,"course.db",null,db_version);
        SQLiteDatabase db=dbhelp.getReadableDatabase();
        String jxcdmc,kcmc,jcdm;
        String zc=getWeek();
        if (zc==null){
            return;
        }
        count=0;
        String xq=TableFragment.getWeekOfDate(new Date());
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
                String js="星期"+xq+"，"+"第"+jcdm+"小节";

                Course course=new Course(kcmc,jxcdmc,teaxms,zc,js,jxbmc,sknrjj,jcdm,xq);
                courseList.add(count,course);

                Map<String,String> map=new HashMap<String, String>();
                map.put("kcmc",kcmc);
                map.put("jcdm",jcdm);
                map.put("jxcdmc",jxcdmc);
                data.add(map);
                count++;
            }
            myOrder(data);
            setOrUpdateSimpleAdapter(data);
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

    class MyClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.notice_examplan:{
                    if (MainActivity.isLogin()){
                        Intent intent=new Intent();
                        intent.setClass(mContext,OtherActivity.class);
                        intent.putExtra("msg","exam");
                        startActivity(intent);
                    }else {
                        Toast.makeText(mContext,"请先登录",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent();
                        intent.setClass(mContext,LoginActivity.class);
                        startActivity(intent);
                    }
                    break;
                }
                case R.id.notice_saylove:{
                    /*Intent intent=new Intent();
                    intent.setClass(mContext,DictionaryActivity.class);
                    startActivity(intent);*/
                    Intent intent=new Intent();
                    intent.setClass(mContext,SayLoveWallActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.notice_autor:{
                    Intent intent=new Intent();
                    intent.setClass(mContext,OtherActivity.class);
                    intent.putExtra("msg","wyuNotice");
                    //intent.putExtra("url",UrlUtil.noticeUserUrl);
                    startActivity(intent);
                    break;
                }
                case R.id.notice_more:{
                    Intent intent=new Intent();
                    intent.setClass(mContext,OtherActivity.class);
                    intent.putExtra("msg","more");
                    startActivity(intent);
                    break;
                }
                case R.id.notice_volunteer:{
                    CheckVolunteerState checkVolunteerState=new CheckVolunteerState(mContext);
                    break;
                }
                case R.id.notice_search_book:{
                    Intent intent=new Intent();
                    intent.setClass(mContext,WebViewActivity.class);
                    intent.putExtra("url",UrlUtil.libraryUrl);
                    startActivity(intent);
                    break;
                }
                case R.id.notice_bus:{
                    Intent intent=new Intent();
                    intent.setClass(mContext,WebViewActivity.class);
                    intent.putExtra("url",UrlUtil.busUrl);
                    startActivity(intent);
                    break;
                }
                case R.id.notice_school_card:{
                    Intent intent =new Intent();
                    intent.setClass(mContext, SchoolCardActivity.class);
                    startActivity(intent);
                    break;
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

    @Override
    public void onResume() {
        int count=fillListNotice.getNoticeCount();
        if (count>0){
            badge.setBadgeCount(count);
        }else {
            badge.setBadgeCount(0);
        }
        super.onResume();
    }

}
