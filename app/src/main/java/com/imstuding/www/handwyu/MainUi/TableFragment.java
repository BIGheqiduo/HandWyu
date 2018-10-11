package com.imstuding.www.handwyu.MainUi;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.view.menu.MenuPopupHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.support.v7.widget.PopupMenu;

import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.imstuding.www.handwyu.AddCourse.AddCourseActivity;
import com.imstuding.www.handwyu.AuditClass.AuditActivity;
import com.imstuding.www.handwyu.OtherUi.OtherActivity;
import com.imstuding.www.handwyu.OtherUi.SetCurrentZc;
import com.imstuding.www.handwyu.ToolUtil.DatabaseHelper;
import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolAdapter.AbsGridAdapter;
import com.imstuding.www.handwyu.ToolUtil.MainTitle;
import com.imstuding.www.handwyu.ToolUtil.WeekTitle;
import com.imstuding.www.handwyu.WebViewDlg.LoginActivity;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.imstuding.www.handwyu.ToolUtil.DatabaseHelper.db_version;

/**
 * Created by yangkui on 2018/3/22.
 */

public class TableFragment extends Fragment {

    private MainTitle titleView;
    private ImageView imageView_menu;
    private ImageView imageView_jiangtou;
    private WeekTitle weekTitle;
    private Spinner spinner;
    private Spinner spinneryear;
    private GridView detailCource;
    private String[][] contents;
    private AbsGridAdapter secondAdapter;
    private List<String> dataList;
    private List<String> dataTermList;
    private ArrayAdapter<String> spinnerAdapter;
    private ArrayAdapter<String> spinnerTermAdapter;
    private View view;
    private Context mContext;
    private int m_zc=1;
    public static String static_term;
    public static int static_zc;
    private LinearLayout layout_zc=null;
    private boolean zc_flag=MainActivity.getArrow();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_table,container,false);
        initFragment();

        if (zc_flag){
            setArrowShow(layout_zc);
        }else {
            setArrowHide(layout_zc);
        }

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

    private void reFreshTitle(){
        if (static_zc==0){
            titleView.setTitleText("第"+1+"周");
        }else {
            titleView.setTitleText("第"+static_zc+"周");
        }
        imageView_jiangtou.setVisibility(View.VISIBLE);
        imageView_menu.setVisibility(View.VISIBLE);
    }

    private void myPopMenu(){
        imageView_menu= (ImageView)view.findViewById(R.id.main_title_menu);
        imageView_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(mContext, v);
                MenuInflater  inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.add_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_set_zc:{
                                SetCurrentZc setCurrentZc=new SetCurrentZc(mContext);
                                setCurrentZc.show();
                                break;
                            }
                            case R.id.menu_add_table:{
                                //导入课程表
                                Intent intent=new Intent();
                                MainActivity activity=(MainActivity)mContext;
                                intent.putExtra("JSESSIONID",activity.getJsessionId());
                                intent.setClass(mContext,AddCourseActivity.class);
                                startActivity(intent);
                                break;
                            }
                            case R.id.menu_all_class:{
                                spinner.setSelection(spinner.getCount()-1);
                                break;
                            }
                            case R.id.menu_add_course:{
                                Toast.makeText(mContext,"可以双击课表的空白处，这样操作更6！",Toast.LENGTH_LONG).show();
                                Intent intent=new Intent();
                                intent.setClass(mContext,OtherActivity.class);
                                intent.putExtra("msg","add_course");
                                intent.putExtra("xq", 1);
                                intent.putExtra("js", 0);
                                mContext.startActivity(intent);
                                break;
                            }
                            case R.id.menu_sub_course:{
                                Intent intent=new Intent();
                                intent.setClass(mContext,OtherActivity.class);
                                intent.putExtra("msg","subCourse");
                                mContext.startActivity(intent);
                                break;
                            }
                            case R.id.menu_audit_class:{
                                //蹭课
                                if (MainActivity.isLogin()){
                                    Intent intent=new Intent();
                                    intent.setClass(mContext,AuditActivity.class);
                                    mContext.startActivity(intent);
                                }else {
                                    Toast.makeText(mContext,"请先登录",Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent();
                                    intent.setClass(mContext,LoginActivity.class);
                                    startActivity(intent);
                                }
                                break;
                            }
                            default:
                                break;
                        }
                        return false;
                    }
                });
                //使用反射，强制显示菜单图标
                try {
                    Field field = popup.getClass().getDeclaredField("mPopup");
                    field.setAccessible(true);
                    MenuPopupHelper helper = (MenuPopupHelper) field.get(popup);
                    helper.setForceShowIcon(true);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                }
                popup.show();
            }
        });
    }

    private void initFragment(){
        myPopMenu();//设置popmenu

        titleView = (MainTitle)view.findViewById(R.id.title_table);
        imageView_menu= (ImageView) view.findViewById(R.id.main_title_menu);
        imageView_jiangtou= (ImageView) view.findViewById(R.id.main_title_jiantou);
        layout_zc= (LinearLayout) view.findViewById(R.id.layout_table_zc);

        weekTitle= (WeekTitle) view.findViewById(R.id.table_date);
        spinneryear= (Spinner) view.findViewById(R.id.year);
        spinner = (Spinner)view.findViewById(R.id.switchWeek);
        detailCource = (GridView)view.findViewById(R.id.courceDetail);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s_zc=spinner.getSelectedItem().toString();
                if (s_zc.equals("全部课程")){
                    //什么都不做
                }else {
                    s_zc=s_zc.substring(1,s_zc.length()-1);
                    setWeekTitleDate(s_zc);
                }
                setTableCourse(s_zc);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //////////////创建周数Spinner数据
        fillWeekDataList();
        spinnerAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, dataList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        //////////////创建学期Spinner数据
        fillTermDataList();
        spinnerTermAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, dataTermList);
        spinnerTermAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinneryear.setAdapter(spinnerTermAdapter);
        spinneryear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s_zc=spinner.getSelectedItem().toString();
                if (s_zc.equals("全部课程")){
                    //什么都不做
                }else {
                    s_zc=s_zc.substring(1,s_zc.length()-1);
                }
                setTableCourse(s_zc);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        reFresh();
    }

    private void reFresh(){
        setWeek();
        //setTableCourse(static_zc+"");
        reFreshTitle();
        imageView_jiangtou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zc_flag){
                    zc_flag=!zc_flag;
                    setArrowHide(layout_zc);
                }else {
                    zc_flag=!zc_flag;
                    setArrowShow(layout_zc);
                }
                MainActivity.setArrow(zc_flag);
            }
        });
    }

    private void setArrowHide(LinearLayout linearLayout){
        ViewGroup.LayoutParams lp;
        lp=linearLayout.getLayoutParams();
        lp.width=0;
        lp.height=0;
        imageView_jiangtou.setBackground(getResources().getDrawable(R.drawable.title_jiantou2));
        linearLayout.setLayoutParams(lp);
        linearLayout.setVisibility(View.INVISIBLE);
    }

    private void setArrowShow(LinearLayout linearLayout){
        ViewGroup.LayoutParams lp;
        lp=linearLayout.getLayoutParams();
        lp.height= WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width=WindowManager.LayoutParams.MATCH_PARENT;
        imageView_jiangtou.setBackground(getResources().getDrawable(R.drawable.title_jiantou1));
        layout_zc.setLayoutParams(lp);
        layout_zc.setVisibility(View.VISIBLE);
    }


    public void setWeekTitleDate(String szc){
        long t_count=Integer.parseInt(szc)-m_zc;
        Date date=new Date();

        int xq=Integer.parseInt(getWeekOfDate(date));

        if (xq==7){
            xq=0;
        }
        weekTitle.setCurrentDay(xq);
        date.setTime(date.getTime()-xq*24*60*60*1000);
        date.setTime(date.getTime()+t_count*7*24*60*60*1000);
        weekTitle.setDate(date);
        weekTitle.invalidate();
    }

    private void initCourse(int a,int b){
        contents = new String[a][b];
        for (int i=0;i<a;i++){
            for (int j=0;j<b;j++){
                contents[i][j]="";
            }
        }
    }
    //设置课程信息
    public void setTableCourse(String s_zc) {
        //清空数据
        initCourse(6,7);
        //根据数据库信息设置课程信息
        DatabaseHelper dbhelp=new DatabaseHelper(mContext,"course.db",null,db_version);
        SQLiteDatabase db=dbhelp.getReadableDatabase();
        String sql=null;
        if (s_zc.equals("全部课程")){
            sql="select * from course where year = "+getTerm();
        }else {
            sql="select * from course where zc="+s_zc+" and year="+getTerm();
        }

        Cursor cursor=db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            String jxcdmc= cursor.getString(0);
            String teaxms= cursor.getString(1);
            String xq= cursor.getString(2);
            String jcdm= cursor.getString(3);
            String kcmc= cursor.getString(4);
            String zc= cursor.getString(5);
            String jxbmc= cursor.getString(7);
            String sknrjj= cursor.getString(8);
            int i_xq=Integer.parseInt(xq);
            int section[]=getSection(jcdm);
            try{
                for (int i=0;i<section.length;i++){
                    if (section[i]!=100)
                        contents[section[i]][i_xq%7]+=kcmc+"@"+jxcdmc+"@"+xq+"@"+jcdm+"@"+zc+"@"+teaxms+"@"+jxbmc+"@"+sknrjj+"#";
                }
            }catch (Exception e){
                //什么也不做
            }

        }
        secondAdapter = new AbsGridAdapter(mContext);
        secondAdapter.setContent(contents, 6, 7);
        detailCource.setAdapter(secondAdapter);
    }

    //获取当前是第几节课
    public int[] getSection(String string){
        int count=string.length()/2;//生成节数

        int section[]=new int[count];
        for (int i=0;i<section.length;i++){
            section[i]=100;
        }//初始化

        try{
            for (int i=0,j=0;i<count*2;i+=4){
                if ((i+4)<count*2)
                    section[j++]=getOneSection(string.substring(i,i+4));
                else
                    section[j++]=getOneSection(string.substring(i,count*2));
            }
        }catch (Exception e){
            Toast.makeText(mContext,"如果看到这条消息请反馈："+e.toString(),Toast.LENGTH_SHORT).show();
        }

/*
        if (string.length()==4){
            section[0]=getOneSection(string);
            section[1]=100;
        }else if (string.length()==8){
            section[0]=getOneSection(string.substring(0,4));
            section[1]=getOneSection(string.substring(4,8));
        }else if (string.length()==6){
            section[0]=getOneSection(string.substring(0,4));
            section[1]=getHalfSection(string.substring(4,6));
        }
*/
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

    //填充周spinner信息
    public void fillWeekDataList() {
        dataList = new ArrayList<>();
        for(int i = 1; i < 25; i++) {
            dataList.add("第" + i + "周");
        }
        dataList.add("全部课程");
    }

    //填充学期spinner信息
    public void fillTermDataList() {
        dataTermList = new ArrayList<>();
        String year;
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        int month=d.getMonth()+1;
        year=sdf.format(d);

        if (month>=2&&month<=7){
                int cy=Integer.parseInt(year);
                int by=cy-1;
                for (int i=0;i<4;i++){
                    dataTermList.add( by+"-"+cy+"-2");
                    dataTermList.add( by+"-"+cy+"-1");
                    cy--;
                    by--;
                }
        }else {
            if (month>=8){
                int cy=Integer.parseInt(year);
                int by=cy-1;
                dataTermList.add( (by+1)+"-"+(cy+1)+"-1");
                for (int i=0;i<4;i++){
                    dataTermList.add( by+"-"+cy+"-2");
                    dataTermList.add( by+"-"+cy+"-1");
                    cy--;
                    by--;
                }
            }else {
                int cy=Integer.parseInt(year);
                int by=cy-1;
                dataTermList.add( by+"-"+(cy)+"-1");
                for (int i=0;i<4;i++){
                    cy--;
                    by--;
                    dataTermList.add( by+"-"+cy+"-2");
                    dataTermList.add( by+"-"+cy+"-1");
                }
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden)
            initFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        initFragment();
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"7", "1", "2", "3", "4", "5", "6"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    //设置周次
    public void setWeek(){
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
            spinner.setSelection(0);
            spinner.callOnClick();
            return;
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

        static_zc= m_zc=i_zc;
        i_zc=i_zc%24;//i_zc不能大于24，要不然会出错

        if (i_zc<=0)
            i_zc=1;
        spinner.setSelection(i_zc-1);
        spinner.callOnClick();
    }

    /**
     * <li>功能描述：时间相减得到天数
     * @param beginDateStr
     * @param endDateStr
     * @return
     * long
     * @author Administrator
     */
    public static long getDaySub(String beginDateStr,String endDateStr) {
        long day=0;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date beginDate;
        java.util.Date endDate;
        try
        {
            beginDate = format.parse(beginDateStr);
            endDate= format.parse(endDateStr);
            day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
        } catch (ParseException e)
        {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        return day;
    }

    public String getTerm(){
        String year=spinneryear.getSelectedItem().toString();
        year=year.replaceAll("\\-\\w+\\-","0");
        static_term=year;
        return year;
    }
}
