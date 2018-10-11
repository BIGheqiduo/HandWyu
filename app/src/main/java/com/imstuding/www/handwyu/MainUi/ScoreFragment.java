package com.imstuding.www.handwyu.MainUi;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.imstuding.www.handwyu.AddCourse.AddCourseActivity;
import com.imstuding.www.handwyu.AuditClass.AuditActivity;
import com.imstuding.www.handwyu.LoadDlgUi.MyLoadDlg;
import com.imstuding.www.handwyu.OtherUi.SetCurrentZc;
import com.imstuding.www.handwyu.ScoreDetailUi.ScoreDetailDlg;
import com.imstuding.www.handwyu.ToolUtil.MainFragmentTitle;
import com.imstuding.www.handwyu.WebViewDlg.LoginActivity;
import com.imstuding.www.handwyu.OtherUi.OtherActivity;
import com.imstuding.www.handwyu.ToolUtil.MyHttpHelp;
import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolAdapter.ScoreAdapter;
import com.imstuding.www.handwyu.ToolUtil.SubJect;
import com.imstuding.www.handwyu.ToolUtil.UrlUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by yangkui on 2018/3/22.
 */

public class ScoreFragment extends Fragment {

    private List<String> dataTermList;
    private ArrayAdapter<String> spinnerTermAdapter;
    private Spinner spinner=null;
    private Button btn_score=null;
    private Button btn_score_activity=null;
    private Button btn_calculat=null;
    private Button btn_selectall=null;
    private Button btn_study=null;
    private TextView show_scorepoint=null;
    private ListView listView=null;
    private ScoreAdapter scoreAdapter=null;
    private AlertDialog alertDialog=null;
    private String url=UrlUtil.scoreUrl;
    private View view=null;
    private Context mcontext=null;
    private MyLoadDlg myLoadDlg=null;
    private MyClickListener myClickListener;
    private MainFragmentTitle titleView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_score,container,false);
        initScoreFragment();
        return view;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mcontext = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mcontext = null;
    }

    private void initScoreFragment(){
        myClickListener=new MyClickListener();

        titleView= (MainFragmentTitle) view.findViewById(R.id.title_fscore);
        titleView.setTitleText("查成绩");

        myLoadDlg=new MyLoadDlg(mcontext);
        show_scorepoint=(TextView)view.findViewById(R.id.show_scorepoint);
        btn_score=(Button)view.findViewById(R.id.btn_score);
        btn_score_activity=(Button)view.findViewById(R.id.btn_score_activity);
        btn_study=(Button)view.findViewById(R.id.study_inf);
        btn_study.setOnClickListener(myClickListener);
        btn_selectall=(Button)view.findViewById(R.id.btn_select_all);
        btn_selectall.setOnClickListener(myClickListener);
        btn_calculat=(Button)view.findViewById(R.id.calculate_score);
        btn_calculat.setOnClickListener(myClickListener);
        listView=(ListView)view.findViewById(R.id.score_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubJect subJect=(SubJect)parent.getAdapter().getItem(position);
                ScoreDetailDlg dlg=new ScoreDetailDlg(mcontext,subJect);
                dlg.show();
                //Toast.makeText(mcontext,"测试"+subJect.getCjjd(),Toast.LENGTH_SHORT).show();
            }
        });
        spinner=(Spinner)view.findViewById(R.id.spinner_date);
        btn_score.setOnClickListener(myClickListener);
        btn_score_activity.setOnClickListener(myClickListener);
        //////////////创建学期Spinner数据
        fillTermDataList();
        spinnerTermAdapter = new ArrayAdapter<String>(mcontext, android.R.layout.simple_spinner_dropdown_item, dataTermList);
        spinnerTermAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerTermAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (MainActivity.isLogin()){
                    btn_score.callOnClick();
                }else {
                    Toast.makeText(mcontext,"请先登录",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.setClass(mcontext,LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

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

    class myScoreThread extends Thread{
        Message message=new Message();
        Bundle bundle=new Bundle();

        @Override
        public void run() {
            try {
                //发送post请求
                MyHttpHelp httpHelp=new MyHttpHelp(url,"post");
                httpHelp.setHeader("Cookie","JSESSIONID="+MainActivity.getJsessionId());
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String xnxqdm=spinner.getSelectedItem().toString();
                xnxqdm=xnxqdm.replaceAll("\\-\\w+\\-","0");
                params.add(new BasicNameValuePair("xnxqdm", xnxqdm));
                params.add(new BasicNameValuePair("jhlxdm", ""));
                params.add(new BasicNameValuePair("page", "1"));
                params.add(new BasicNameValuePair("rows", "40"));
                params.add(new BasicNameValuePair("sort", "xnxqdm"));
                params.add(new BasicNameValuePair("order", "asc"));
                HttpResponse httpResponse = httpHelp.postRequire(params);

                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    // 请求和响应都成功了
                    HttpEntity entity = httpResponse.getEntity();
                    String response = EntityUtils.toString(entity, "utf-8");
                    parseJSONWithJSONObject(response);
                }
            } catch (Exception e) {
                myLoadDlg.dismiss();
                message.what=1007;
                handle.sendMessage(message);
                e.printStackTrace();
            }
        }

        private void parseJSONWithJSONObject(String jsonData) {
            ArrayList<String> subject= new ArrayList<String>();
            try {
                jsonData+=']';
                jsonData = '['+jsonData;
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONArray retArray=jsonObject.getJSONArray("rows");
                    for (int j = 0; j < retArray.length(); j++) {
                        JSONObject retobject =retArray.getJSONObject(j);
                        String array[]=new String[7];
                        String kcmc= retobject.getString("kcmc");//课程名称
                        String zcj="**";
                        String cjjd="**";
                        //如果没有进行评教这里会出问题，所以捕捉异常
                        try{
                            zcj=retobject.getString("zcj");//总成绩
                            cjjd=retobject.getString("cjjd");//绩点
                        }catch (Exception e)
                        {
                            cjjd="**";
                            zcj="**";
                        }
                        String xf=retobject.getString("xf");//学分
                        String xdfsmc=retobject.getString("xdfsmc");//修读方式
                        String kcbh=retobject.getString("kcbh");//课程编号
                        String zxs=retobject.getString("zxs");//学时
                        String cjfsmc=retobject.getString("cjfsmc");//成绩方式
                        //平时分暂时不考虑，会存在问题
                        //String pscj=retobject.getString("pscj");//平时成绩\

                        array[0]=zcj;//总成绩
                        array[1]=xf;//学分
                        array[2]=xdfsmc;//修读方式
                        array[3]=cjjd;//绩点
                        array[4]=kcbh;//课程编号
                        array[5]=zxs;//学时
                        array[6]=cjfsmc;//成绩方式

                        bundle.putStringArray(kcmc,array);
                        subject.add(kcmc);
                    }
                }
                //把数据发送出去
                message.what=1003;
                bundle.putStringArrayList("subject",subject);
                message.setData(bundle);
                handle.sendMessage(message);
            } catch (Exception e) {
                message.what=1006;
                handle.sendMessage(message);
                e.printStackTrace();
            }
        }
    }

    class myStudyInfThread extends Thread{
        Message message=new Message();
        Bundle bundle=new Bundle();

        @Override
        public void run() {
            try{
                MyHttpHelp httpHelp=new MyHttpHelp(UrlUtil.refreshScoreInfUrl,"post");
                httpHelp.setHeader("Cookie","JSESSIONID="+MainActivity.getJsessionId());
                httpHelp.setHeader("Referer","http://202.192.240.29/login!welcome.action");
                httpHelp.setHeader("Accept","text/plain, */*; q=0.01");
                httpHelp.setHeader("Accept-Encoding","gzip, deflate");
                httpHelp.setHeader("X-Requested-With","XMLHttpRequest");
                HttpResponse httpResponse = httpHelp.postRequire(null);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    // 刷新功能
                   // Toast.makeText(mcontext,"刷新成功！",Toast.LENGTH_SHORT).show();
                }else{
                   Toast.makeText(mcontext,"刷新失败！",Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                myLoadDlg.dismiss();
                message.what=1007;
                handle.sendMessage(message);
                e.printStackTrace();
            }
            try {
                MyHttpHelp httpHelp=new MyHttpHelp(UrlUtil.getScoreInfUrl,"post");
                httpHelp.setHeader("Cookie","JSESSIONID="+MainActivity.getJsessionId());
                httpHelp.setHeader("Referer","http://202.192.240.29/login!welcome.action");

                HttpResponse httpResponse = httpHelp.postRequire(null);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    // 请求和响应都成功了
                    HttpEntity entity = httpResponse.getEntity();
                    String response = EntityUtils.toString(entity, "utf-8");
                    parseJSONWithJSONObject(response);

                }
            } catch (Exception e) {
                myLoadDlg.dismiss();
                message.what=1007;
                handle.sendMessage(message);
                e.printStackTrace();
            }
        }
        private void parseJSONWithJSONObject(String jsonData) {
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String tjmc1=jsonObject.getString("tjmc1");
                    String tjmc2=jsonObject.getString("tjmc2");
                    String tjmc3=jsonObject.getString("tjmc3");
                    String tjz1=jsonObject.getString("tjz1");
                    String tjz2=jsonObject.getString("tjz2");
                    String tjz3=jsonObject.getString("tjz3");
                    bundle.putString("tjmc1",tjmc1);
                    bundle.putString("tjmc2",tjmc2);
                    bundle.putString("tjmc3",tjmc3);
                    bundle.putString("tjz1",tjz1);
                    bundle.putString("tjz2",tjz2);
                    bundle.putString("tjz3",tjz3);
                }
                //把数据发送出去
                message.what=1004;
                message.setData(bundle);
                handle.sendMessage(message);
            } catch (Exception e) {
                message.what=1006;
                bundle.putInt("retcode",0);
                message.setData(bundle);
                handle.sendMessage(message);
                e.printStackTrace();
            }
        }
    }

    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1003:{//成绩查询
                    myLoadDlg.dismiss();
                    Bundle bundle= msg.getData();
                    List<SubJect> subJectList=new LinkedList<SubJect>();
                    if (bundle.getStringArrayList("subject").size()==0){
                        //判断有没有数据
                        Toast.makeText(mcontext,"暂时没有成绩信息！",Toast.LENGTH_SHORT).show();
                    }
                    for (int i=0;i<bundle.getStringArrayList("subject").size();i++){
                        String kcmc=bundle.getStringArrayList("subject").get(i);
                        String array[]=bundle.getStringArray(kcmc);
                        String zcj=array[0];
                        String xf=array[1];
                        String xdfsmc=array[2];
                        String cjjd=array[3];
                        String kcbh=array[4];
                        String zxs=array[5];
                        String cjfsmc=array[6];

                        SubJect subJect=new SubJect(kcbh,kcmc,zcj,cjjd,zxs,xf,xdfsmc,cjfsmc,"**",true);
                        subJectList.add(subJect);
                    }
                    //设置listview的适配器
                    scoreAdapter=new ScoreAdapter(mcontext,R.layout.score_item,subJectList);
                    listView.setAdapter(scoreAdapter);
                    break;
                }
                case 1004:{//学习情况
                    Bundle bundle= msg.getData();
                    String tjmc1=bundle.getString("tjmc1");
                    String tjmc2=bundle.getString("tjmc2");
                    String tjmc3=bundle.getString("tjmc3");
                    String tjz1=bundle.getString("tjz1");
                    String tjz2=bundle.getString("tjz2");
                    String tjz3=bundle.getString("tjz3");
                    Toast.makeText(mcontext,tjmc1+":"+tjz1+"\r\n"+tjmc2+":"+tjz2+"\r\n"+tjmc3+":"+tjz3,Toast.LENGTH_LONG).show();
                    myLoadDlg.dismiss();
                    break;
                }
                case 1006: {//没有登录
                    myLoadDlg.dismiss();
                    Toast.makeText(mcontext,"请先登录",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    MainActivity.setLogin(false);
                    intent.setClass(mcontext,LoginActivity.class);
                    startActivity(intent);
                    break;
                }
                case 1007:{
                    Toast.makeText(mcontext,R.string.offline,Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    };

    class MyClickListener implements View.OnClickListener{
        List<SubJect> subJectList=null;

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_score:{
                    if (!myLoadDlg.isShowing())
                    myLoadDlg.show();
                    myScoreThread myScore=new myScoreThread();
                    myScore.start();
                    break;
                }
                case R.id.calculate_score:{
                    showSelectCal();
                    break;
                }
                case R.id.btn_score_activity:{
                    Intent intent = new Intent(mcontext, SocreActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.btn_select_all:{
                    String string= btn_selectall.getText().toString();
                    if (string.equals("全选")){
                        try{
                            scoreAdapter.selectAll();
                            btn_selectall.setText("全不选");
                        }catch (Exception e){

                        }
                    }else {
                        try{
                            scoreAdapter.notSelectAll();
                            btn_selectall.setText("全选");
                        }catch (Exception e){

                        }
                    }
                    break;
                }
                case R.id.study_inf:{
                    myLoadDlg.show();
                    myStudyInfThread studyInfThread=new myStudyInfThread();
                    studyInfThread.start();
                    break;
                }
                case R.id.layout_old_cal:{
                    try {
                        subJectList = scoreAdapter.getSelectSubject();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    float scorepoint= calculatScore(subJectList);
                    show_scorepoint.setText("绩点:"+scorepoint);
                    alertDialog.dismiss();
                    break;
                }
                case R.id.layout_new_cal:{
                    try {
                        subJectList = scoreAdapter.getSelectSubject();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    float scorepoint= newCalculateScore(subJectList);
                    show_scorepoint.setText("绩点:"+scorepoint);
                    alertDialog.dismiss();
                    break;
                }
            }
        }
        //显示选择计算方式

        public void showSelectCal(){
            AlertDialog.Builder  builder=new AlertDialog.Builder(mcontext);
            Activity activity=(Activity)mcontext;
            LayoutInflater inflater =activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.choose_calculate, null);
            builder.setView(view);
            LinearLayout lay_old= (LinearLayout) view.findViewById(R.id.layout_old_cal);
            LinearLayout lay_new= (LinearLayout) view.findViewById(R.id.layout_new_cal);
            lay_old.setOnClickListener(myClickListener);
            lay_new.setOnClickListener(myClickListener);
            builder.setCancelable(true);
            alertDialog=builder.create();
            alertDialog.show();
        }

        //旧方式计算绩点
        public float calculatScore(List<SubJect> subJectList){
            if (subJectList==null||subJectList.size()==0){
                return 0;
            }
            float sumScore=0;
            float sumxf=0;
            for (int i=0;i<subJectList.size();i++){
                SubJect subJect=subJectList.get(i);
                float txf=Float.parseFloat(subJect.getXf());
                sumxf+=txf;
                float tscore=0;//当前这门课的成绩
                try{
                    tscore=Float.parseFloat(subJect.getZcj());
                }catch (Exception e){
                    tscore=0;
                    switch (subJect.getZcj()){
                        case "不及格":{
                            tscore=50;
                            break;
                        }
                        case "及格":{
                            tscore=60;
                            break;
                        }
                        case "中等":{
                            tscore=75;
                            break;
                        }
                        case "良好":{
                            tscore=85;
                            break;
                        }
                        case "优秀":{
                            tscore=95;
                            break;
                        }
                        default:{
                            tscore=0;
                            break;
                        }
                    }
                }
                tscore= tscore-60;
                if (tscore<0){
                    sumScore+=0;
                }else{
                    tscore/=10;
                    tscore+=1;
                    sumScore+=tscore*txf;
                }
            }
            return (sumScore/sumxf);
        }

        //新方式计算绩点
        public float newCalculateScore(List<SubJect> subJectList){
            if (subJectList==null||subJectList.size()==0){
                return 0;
            }
            float sumScore=0,sumXf=0;
            for (int i=0;i<subJectList.size();i++){
                SubJect subJect=subJectList.get(i);
                try{
                    float txf=Float.parseFloat(subJect.getXf());
                    float tjd=Float.parseFloat(subJect.getCjjd());
                    sumScore+=tjd*txf;
                    sumXf+=txf;
                }catch (Exception e){
                    sumScore+=0;
                    sumXf+=0;
                }
            }
            if (sumScore==0&&sumXf==0)
                return 0.0f;

            return (sumScore/sumXf);
        }

    }

}
