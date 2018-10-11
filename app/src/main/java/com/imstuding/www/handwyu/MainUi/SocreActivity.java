package com.imstuding.www.handwyu.MainUi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.imstuding.www.handwyu.LoadDlgUi.MyLoadDlg;
import com.imstuding.www.handwyu.OtherUi.OtherActivity;
import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ScoreDetailUi.ScoreDetailDlg;
import com.imstuding.www.handwyu.ToolAdapter.ScoreAdapter;
import com.imstuding.www.handwyu.ToolUtil.DatabaseHelper;
import com.imstuding.www.handwyu.ToolUtil.MyHttpHelp;
import com.imstuding.www.handwyu.ToolUtil.SubJect;
import com.imstuding.www.handwyu.ToolUtil.TitleView;
import com.imstuding.www.handwyu.ToolUtil.UrlUtil;
import com.imstuding.www.handwyu.WebViewDlg.LoginActivity;

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

import static com.imstuding.www.handwyu.ToolUtil.DatabaseHelper.db_version;

public class SocreActivity extends AppCompatActivity {

    private List<String> dataTermList;
    private ArrayAdapter<String> spinnerTermAdapter;
    private Spinner begin_spinner=null;
    private Spinner end_spinner=null;
    private Button act_btn_calculat=null;
    private Button act_btn_selectall=null;
    private Button act_btn_score=null;
    private AlertDialog alertDialog=null;
    private String url= UrlUtil.scoreUrl;
    private MyLoadDlg myLoadDlg=null;
    private Context mcontext=null;
    private ListView listView=null;
    private ScoreAdapter scoreAdapter=null;
    List<SubJect> subJectList=null;
    private TextView show_scorepoint=null;
    public TitleView titleView;
    public ImageView imageView_menu;
    public ImageView imageView_back;
    private MyClickListener myClickListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_socre);
        mcontext=SocreActivity.this;
        initScoreActivity();
    }

    private void myPopMenu(){
        imageView_back= (ImageView) findViewById(R.id.title_back);
        imageView_menu= (ImageView) findViewById(R.id.title_menu);
        titleView= (TitleView) findViewById(R.id.title_score);

        titleView.setTitleText("高级查询");

        imageView_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(mcontext, v);
                MenuInflater  inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.act_score_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.act_menu_score_load:{
                                myLoadDlg.show();
                                myAllScoreThread allScoreThread =new myAllScoreThread();
                                allScoreThread.start();
                                break;
                            }
                            case R.id.act_menu_score_search:{
                                subJectList.clear();
                                //subJectList=new LinkedList<SubJect>();//在这里分配内存存放成绩信息
                                DatabaseHelper dbhelp=new DatabaseHelper(mcontext,"course.db",null,db_version);
                                SQLiteDatabase db=dbhelp.getWritableDatabase();
                                String sql="select * from score";
                                Cursor cursor=db.rawQuery(sql,null);
                                while (cursor.moveToNext()){
                                    String kcmc= cursor.getString(0);
                                    String zcj= cursor.getString(1);
                                    String xf= cursor.getString(2);
                                    String xdfsmc= cursor.getString(3);
                                    String cjjd= cursor.getString(4);
                                    String kcbh= cursor.getString(5);
                                    String zxs= cursor.getString(6);
                                    String cjfsmc= cursor.getString(7);
                                    String pscj= cursor.getString(8);
                                    SubJect subJect=new SubJect(kcbh,kcmc,zcj,cjjd,zxs,xf,xdfsmc,cjfsmc,"**",true);
                                    subJectList.add(subJect);
                                }
                                //设置listview的适配器
                                scoreAdapter=new ScoreAdapter(mcontext,R.layout.score_item,subJectList);
                                listView.setAdapter(scoreAdapter);
                                break;
                            }
                            case R.id.act_menu_score_bug:{
                                Intent intent=new Intent();
                                intent.setClass(mcontext,OtherActivity.class);
                                intent.putExtra("msg","bug");
                                startActivity(intent);
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

    private void initScoreActivity(){
        myPopMenu();
        myClickListener=new MyClickListener();
        subJectList=new LinkedList<SubJect>();//在这里分配内存存放成绩信息
        myLoadDlg=new MyLoadDlg(SocreActivity.this);

        show_scorepoint= (TextView) findViewById(R.id.act_show_scorepoint);
        begin_spinner= (Spinner) findViewById(R.id.begin_spinner_date);
        end_spinner= (Spinner) findViewById(R.id.end_spinner_date);
        act_btn_calculat= (Button) findViewById(R.id.act_calculate_score);
        act_btn_selectall= (Button) findViewById(R.id.act_btn_select_all);
        act_btn_score= (Button) findViewById(R.id.act_btn_score);

        act_btn_calculat.setOnClickListener(myClickListener);
        act_btn_selectall.setOnClickListener(myClickListener);
        act_btn_score.setOnClickListener(myClickListener);

        listView=(ListView)findViewById(R.id.act_score_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubJect subJect=(SubJect)parent.getAdapter().getItem(position);
                ScoreDetailDlg dlg=new ScoreDetailDlg(mcontext,subJect);
                dlg.show();
                //Toast.makeText(mcontext,"测试"+subJect.getCjjd(),Toast.LENGTH_SHORT).show();
            }
        });

        fillTermDataList();
        spinnerTermAdapter = new ArrayAdapter<String>(SocreActivity.this, android.R.layout.simple_spinner_dropdown_item, dataTermList);
        spinnerTermAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        begin_spinner.setAdapter(spinnerTermAdapter);
        end_spinner.setAdapter(spinnerTermAdapter);
    }

    class myScoreThread extends Thread{
        Message message=new Message();
        Bundle bundle=new Bundle();
        String xnxqdm=null;
        public myScoreThread(String xnxqdm){
            this.xnxqdm=xnxqdm;
        }

        @Override
        public void run() {
            try {
                //发送post请求
                MyHttpHelp httpHelp=new MyHttpHelp(url,"post");
                httpHelp.setHeader("Cookie","JSESSIONID="+MainActivity.getJsessionId());
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                //String xnxqdm=begin_spinner.getSelectedItem().toString();
                //xnxqdm=xnxqdm.replaceAll("\\-\\w+\\-","0");
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

    //查询所有成绩
    class myAllScoreThread extends Thread{
        Message message=new Message();
        Bundle bundle=new Bundle();

        @Override
        public void run() {
            try {
                //发送post请求
                MyHttpHelp httpHelp=new MyHttpHelp(url,"post");
                httpHelp.setHeader("Cookie","JSESSIONID="+MainActivity.getJsessionId());
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("xnxqdm", ""));
                params.add(new BasicNameValuePair("jhlxdm", ""));
                params.add(new BasicNameValuePair("page", "1"));
                params.add(new BasicNameValuePair("rows", "400"));
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
            int count=0;//用来统计课程数
            DatabaseHelper dbhelp=new DatabaseHelper(getApplicationContext(),"course.db",null,db_version);
            SQLiteDatabase db=dbhelp.getWritableDatabase();
            try{
                db.execSQL("delete from score");//缓存之前，先删除上一次的缓存
            }catch (Exception e){
                e.printStackTrace();
            }

            try {
                jsonData+=']';
                jsonData = '['+jsonData;
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONArray retArray=jsonObject.getJSONArray("rows");
                    for (int j = 0; j < retArray.length(); j++) {
                        JSONObject retobject =retArray.getJSONObject(j);
                        count++;
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
                        try{
                            db.execSQL("insert into score values(?,?,?,?,?,?,?,?,?)",new String[]{kcmc,zcj,xf,xdfsmc,cjjd,kcbh,zxs,cjfsmc,"**"});
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                message.what=1006;
                handle.sendMessage(message);
                e.printStackTrace();
            }
            bundle.putInt("kcs",count);
            message.setData(bundle);
            message.what=1008;
            handle.sendMessage(message);
        }
    }

    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1003:{//成绩查询
                    myLoadDlg.dismiss();

                    Bundle bundle= msg.getData();

                    if (bundle.getStringArrayList("subject").size()==0){
                        //判断有没有数据
                        Toast.makeText(mcontext,"有部分学年没有暂时没有成绩信息！",Toast.LENGTH_SHORT).show();
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
                case 1006: {//没有登录
                    myLoadDlg.dismiss();
                    Toast.makeText(mcontext,"请先登录",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.setClass(mcontext,LoginActivity.class);
                    startActivity(intent);
                    break;
                }
                case 1007:{
                    Toast.makeText(mcontext,R.string.offline,Toast.LENGTH_SHORT).show();
                    break;
                }
                case 1008:{
                    myLoadDlg.dismiss();
                    Bundle bundle= msg.getData();
                    int count=bundle.getInt("kcs");
                    Toast.makeText(mcontext,"已经缓存了"+count+"科的课程成绩，可以点击查询所有缓存成绩查询！",Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    };

    class MyClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.act_btn_score:{
                    if (!myLoadDlg.isShowing())
                        myLoadDlg.show();
                    subJectList.clear();
                    //subJectList=new LinkedList<SubJect>();//在这里分配内存存放成绩信息

                    String begin_date=begin_spinner.getSelectedItem().toString();
                    begin_date=begin_date.replaceAll("\\-\\w+\\-","0");
                    String end_date=end_spinner.getSelectedItem().toString();
                    end_date=end_date.replaceAll("\\-\\w+\\-","0");

                    if (end_date.endsWith(begin_date)){
                        myScoreThread myScore=new myScoreThread(end_date);
                        myScore.start();
                        break;
                    }

                    int b_d=Integer.parseInt(begin_date);//把“201701”转为数字201701
                    int e_d=Integer.parseInt(end_date);//同上
                    if (b_d>e_d){//进行一下转换
                        String tmp=begin_date;
                        begin_date=end_date;
                        end_date=tmp;
                    }
                    int b_i=Integer.parseInt(begin_date.substring(0,4));
                    out:for (int i=b_i;!begin_date.equals(end_date);){
                        String xnxqdm=new String();
                        for (int j=0;j<2;j++){
                            if (begin_date.endsWith("1")){
                                xnxqdm=i+"0"+1;
                                myScoreThread myScore=new myScoreThread(xnxqdm);
                                myScore.start();

                                if (xnxqdm.equals(end_date)){
                                    break out;
                                }

                                begin_date=i+"0"+2;
                            }else {
                                xnxqdm=i+"0"+2;
                                myScoreThread myScore=new myScoreThread(xnxqdm);
                                myScore.start();

                                if (xnxqdm.equals(end_date) &&
                                        Integer.parseInt(xnxqdm)<=Integer.parseInt(end_date)){
                                    break out;
                                }
                                i++;
                                begin_date=i+"0"+1;
                                break;
                            }
                        }
                    }
                    if (end_date.endsWith("1")){
                        myScoreThread myScore=new myScoreThread(end_date);
                        myScore.start();
                    }
                    break;
                }
                case R.id.act_calculate_score:{
                    showSelectCal();
                    break;
                }
                case R.id.act_btn_select_all:{
                    String string= act_btn_selectall.getText().toString();
                    if (string.equals("全选")){
                        try{
                            scoreAdapter.selectAll();
                            act_btn_selectall.setText("全不选");
                        }catch (Exception e){

                        }
                    }else {
                        try{
                            scoreAdapter.notSelectAll();
                            act_btn_selectall.setText("全选");
                        }catch (Exception e){

                        }
                    }
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
            AlertDialog.Builder  builder=new AlertDialog.Builder(SocreActivity.this);
            Activity activity=SocreActivity.this;
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

        //计算绩点
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
