package com.imstuding.www.handwyu.AuditClass;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.imstuding.www.handwyu.AddCourse.SendWidgetRefresh;
import com.imstuding.www.handwyu.CourseDetailUi.CourseDetailDlg;
import com.imstuding.www.handwyu.LoadDlgUi.MyLoadDlg;
import com.imstuding.www.handwyu.MainUi.MainActivity;
import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.Course;
import com.imstuding.www.handwyu.ToolUtil.DatabaseHelper;
import com.imstuding.www.handwyu.ToolUtil.MyHttpHelp;
import com.imstuding.www.handwyu.ToolUtil.TitleView;
import com.imstuding.www.handwyu.ToolUtil.UrlUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.imstuding.www.handwyu.ToolUtil.DatabaseHelper.db_version;

public class AuditActivity extends AppCompatActivity {

    private Button up_page=null;
    private Button audit_search=null;
    private Button down_page=null;
    private ListView list_audit=null;
    private AuditPostData postData;
    private List<Course> courseList=null;
    private SimpleAdapter simpleAdapter=null;
    private MyLoadDlg myLoadDlg=null;
    public TitleView titleView;
    public ImageView imageView_menu;
    public ImageView imageView_back;

    public static List<String> list_kcdm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_audit);
        initActivity();
        myLoadDlg.show();
        AuditClassThread classThread=new AuditClassThread();
        classThread.start();
    }

    private void myPopMenu(){
        imageView_back= (ImageView) findViewById(R.id.title_back);
        imageView_menu= (ImageView) findViewById(R.id.title_menu);
        titleView= (TitleView) findViewById(R.id.title_audit);

        imageView_menu.setVisibility(View.INVISIBLE);
        titleView.setTitleText("蹭课");
    }


    private void initActivity(){
        myPopMenu();
        myLoadDlg=new MyLoadDlg(AuditActivity.this);
        courseList=new LinkedList<Course>();
        list_kcdm=new LinkedList<String>();

        postData=new AuditPostData(getTerm(),"","","","","","","","1");//分配内存

        up_page= (Button) findViewById(R.id.audit_up_page);
        audit_search= (Button) findViewById(R.id.audit_search);
        down_page= (Button) findViewById(R.id.audit_down_page);
        list_audit= (ListView) findViewById(R.id.audit_class);
        registerForContextMenu(list_audit);//注册长按事件的上下文menu
        list_audit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CourseDetailDlg courseDetailDlg=new CourseDetailDlg(AuditActivity.this,courseList.get(position));
                courseDetailDlg.show();
            }
        });
        up_page.setOnClickListener(new MyClickListener());
        audit_search.setOnClickListener(new MyClickListener());
        down_page.setOnClickListener(new MyClickListener());
    }

    class MyClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.audit_up_page:{
                   // Toast.makeText(getApplicationContext(),"上一页",Toast.LENGTH_SHORT).show();
                    int page=Integer.parseInt(postData.getPage()) ;
                    page-=1;
                    courseList.clear();//清空上一次数据
                    myLoadDlg.show();
                    if (page<=0){
                        page=1;
                        Toast.makeText(AuditActivity.this,"已经到达顶页了！",Toast.LENGTH_SHORT).show();
                    }

                    if (list_kcdm.size()==0){
                        postData.setPage(page+"");
                        postData.setKcdm("");
                        AuditClassThread classThread=new AuditClassThread();
                        classThread.start();
                    }

                    for (int i=0;i<list_kcdm.size();i++){
                        worker_Thread worker_thread=new worker_Thread(page,i);
                        worker_thread.start();
                    }

                    break;
                }
                case R.id.audit_search:{
                    AuditSearchDlg searchDlg=new AuditSearchDlg(AuditActivity.this,postData,courseList,list_audit);
                    searchDlg.show();
                    //Toast.makeText(getApplicationContext(),"查询",Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.audit_down_page:{
                   // Toast.makeText(getApplicationContext(),"下一页",Toast.LENGTH_SHORT).show();
                    myLoadDlg.show();
                    int page=Integer.parseInt(postData.getPage()) ;
                    page+=1;
                    courseList.clear();//清空上一次数据
                    if (list_kcdm.size()==0){
                        postData.setPage(page+"");
                        postData.setKcdm("");
                        AuditClassThread classThread=new AuditClassThread();
                        classThread.start();
                    }

                    for (int i=0;i<list_kcdm.size();i++){
                        worker_Thread worker_thread=new worker_Thread(page,i);
                        worker_thread.start();
                    }


                    break;
                }
            }
        }
    }


    class worker_Thread extends Thread{
        private int page;
        private int index;
        public worker_Thread(int page,int index){
            this.page=page;
            this.index=index;
       }
        @Override
        public void run() {
            postData.setPage(page+"");
            postData.setKcdm(list_kcdm.get(index));
            AuditClassThread classThread=new AuditClassThread();
            classThread.start();
            try {
                classThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //长按listview事件对应的事件，context菜单点击的监视器
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //info.id得到listview中选择的条目绑定的id
        String id = String.valueOf(info.id);
        ListView lv=(ListView)info.targetView.getParent();
        SimpleAdapter simpleAdapter=(SimpleAdapter)lv.getAdapter();
        Map<String,String> data= (Map<String,String>)simpleAdapter.getItem(Integer.parseInt(id));
        switch (item.getItemId()) {
            case R.id.menu_audit_class:{
                courseList.clear();
                myLoadDlg.show();
                KcmcToKcdmThread kcdmThread=new KcmcToKcdmThread(data.get("kcmc"),data.get("teaxms"));
                kcdmThread.start();
                //Toast.makeText(AuditActivity.this,"测试导入"+data.get("teaxms")+data.get("kcmc"),Toast.LENGTH_SHORT).show();
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.audit_class) {
            MenuInflater inflater = AuditActivity.this.getMenuInflater();
            menu.setHeaderTitle("你想干啥？");
            inflater.inflate(R.menu.audit_class_menu, menu);
        }
        setMenuIconEnable(menu,true);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    //通过反射来设置菜单图标
    public void setMenuIconEnable(ContextMenu menu,boolean enable){
        try {
            Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);
            //MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)
            m.invoke(menu, enable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOrUpdateSimpleAdapter(List<Map<String,String>> data){
        simpleAdapter=new SimpleAdapter(AuditActivity.this,data,R.layout.list_item_audit,
                new String[]{"kcmc","js","teaxms"},new int[]{R.id.item_audit_kcmc,R.id.item_audit_js,R.id.item_audit_teaxms});
        list_audit.setAdapter(simpleAdapter);
    }

    class AuditClassThread extends Thread{
        Message message=new Message();
        Bundle bundle=new Bundle();
        private boolean flag=false;
        public AuditClassThread(boolean flag){
            this.flag=flag;
        }
        public AuditClassThread(){
            this.flag=false;
        }
        @Override
        public void run() {
            try {
                //发送post请求
                MyHttpHelp httpHelp=new MyHttpHelp(UrlUtil.auditUrl,"post");
                httpHelp.setHeader("Cookie","JSESSIONID="+ MainActivity.getJsessionId());
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("xnxqdm", postData.getXnxqdm()));
                params.add(new BasicNameValuePair("teaxm", postData.getTeaxm()));
                params.add(new BasicNameValuePair("kcdm", postData.getKcdm()));
                params.add(new BasicNameValuePair("kkyxdm", postData.getKkyxdm()));
                params.add(new BasicNameValuePair("jhlxdm", postData.getJhlxdm()));
                params.add(new BasicNameValuePair("page", postData.getPage()));
                params.add(new BasicNameValuePair("jcdm", postData.getJs()));
                params.add(new BasicNameValuePair("rq", postData.getRq()));

                params.add(new BasicNameValuePair("rows", "60"));
                params.add(new BasicNameValuePair("sort", "kxh"));
                params.add(new BasicNameValuePair("order", "asc"));
                params.add(new BasicNameValuePair("queryParams[primarySort]", " dgksdm asc"));

                params.add(new BasicNameValuePair("xqdm", ""));
                params.add(new BasicNameValuePair("kkjysdm", ""));
                params.add(new BasicNameValuePair("gnqdm", ""));
                params.add(new BasicNameValuePair("xq", ""));
                params.add(new BasicNameValuePair("zc", ""));
                params.add(new BasicNameValuePair("kcrwdm", ""));
                params.add(new BasicNameValuePair("jzwdm", ""));

                HttpResponse httpResponse = httpHelp.postRequire(params);

                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    // 请求和响应都成功了
                    HttpEntity entity = httpResponse.getEntity();
                    String response = EntityUtils.toString(entity, "utf-8");
                    parseJSONWithJSONObject(response);
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        private void parseJSONWithJSONObject(String jsonData) {
            DatabaseHelper dbhelp=new DatabaseHelper(getApplicationContext(),"course.db",null,db_version);
            SQLiteDatabase db=dbhelp.getWritableDatabase();
            int count=0;
            try {
                jsonData+=']';
                jsonData = '['+jsonData;
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONArray retArray=jsonObject.getJSONArray("rows");
                    for (int j = 0; j < retArray.length(); j++) {
                        JSONObject retobject =retArray.getJSONObject(j);
                        String jxcdmc= retobject.getString("jxcdmc");
                        String teaxms= retobject.getString("teaxms");
                        String xq= retobject.getString("xq");
                        String jcdm= retobject.getString("jcdm");
                        String kcmc= retobject.getString("kcmc");
                        String zc= retobject.getString("zc");
                        String jxbmc= retobject.getString("jxbmc");
                        String sknrjj= retobject.getString("sknrjj");
                        String js="星期"+xq+"，"+"第"+jcdm+"小节";

                        Course course=new Course(kcmc,jxcdmc,teaxms,zc,js,jxbmc,sknrjj);
                        courseList.add(i,course);
                        count++;
                        if (flag){
                            try{
                                db.execSQL("insert into course values(?,?,?,?,?,?,?,?,?)",new String[]{jxcdmc,teaxms,xq,jcdm,kcmc,zc,getTerm(),jxbmc,sknrjj});
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {

                        }
                    }
                }
                //把数据发送出去
                if (flag){
                    //刷新小部件窗体
                    SendWidgetRefresh.widgetRefresh(AuditActivity.this);
                }
                bundle.putInt("count",count);
                message.setData(bundle);
                message.what=1023;
                handle.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
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


    class KcmcToKcdmThread extends Thread{
        Message message=new Message();
        Bundle bundle=new Bundle();
        private String  kcmc;
        private String  teaxms;
        public KcmcToKcdmThread(String  kcmc,String teaxms){
            this.kcmc=kcmc;
            this.teaxms=teaxms;
        }
        @Override
        public void run() {
            try {
                //发送post请求
                MyHttpHelp httpHelp=new MyHttpHelp(UrlUtil.getKcdmUrl,"post");
                httpHelp.setHeader("Cookie","JSESSIONID="+ MainActivity.getJsessionId());
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("q", kcmc));

                HttpResponse httpResponse = httpHelp.postRequire(params);

                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    // 请求和响应都成功了
                    HttpEntity entity = httpResponse.getEntity();
                    String response = EntityUtils.toString(entity, "utf-8");
                    parseJSONWithJSONObject(response);
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        private void parseJSONWithJSONObject(String jsonData) {
            String kcdm=null;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {//重复的只要第一个
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    kcdm= jsonObject.getString("dm");
                    if (kcmc.equals(jsonObject.getString("mc"))){
                        //kcmc=jsonObject.getString("mc");
                        //把数据发送出去
                        bundle.putString("kcdm",kcdm);//课程代码
                        bundle.putString("kcmc",kcmc);//课程名称
                        bundle.putString("teaxms",teaxms);//教师
                        message.setData(bundle);
                        message.what=1024;
                        handle.sendMessage(message);
                    }
                }

            } catch (Exception e) {
                //把数据发送出去
                bundle.putString("kcdm",kcdm);
                message.setData(bundle);
                message.what=1024;
                handle.sendMessage(message);
                e.printStackTrace();
            }
        }

    }

    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1023:{
                    myLoadDlg.dismiss();
                    Bundle bundle=msg.getData();
                    int count=bundle.getInt("count");
                    if (count==0){
                       Toast.makeText(AuditActivity.this,"没有查到结果！",Toast.LENGTH_SHORT).show();
                        //break;
                    }
                    try{
                        List<Map<String,String>> data=new ArrayList<Map<String,String>>();
                        for (int i=0;i<courseList.size();i++){
                            Map<String,String> map=new HashMap<String, String>();
                            map.put("kcmc",courseList.get(i).getKcmc());
                            map.put("js",courseList.get(i).getJs());
                            map.put("teaxms",courseList.get(i).getTeaxms());
                            data.add(map);

                        }
                        setOrUpdateSimpleAdapter(data);
                    }catch (Exception e){

                    }

                    break;
                }

                case 1024:{
                    myLoadDlg.dismiss();
                    Bundle bundle=msg.getData();
                    postData.setKcdm(bundle.getString("kcdm"));
                    postData.setKcmc(bundle.getString("kcmc"));
                    postData.setTeaxm(bundle.getString("teaxms"));

                    AuditClassThread classThread=new AuditClassThread(true);
                    classThread.start();


                    break;
                }
            }
        }
    };

}
