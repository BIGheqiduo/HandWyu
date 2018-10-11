package com.imstuding.www.handwyu.AuditClass;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.imstuding.www.handwyu.LoadDlgUi.MyLoadDlg;
import com.imstuding.www.handwyu.MainUi.MainActivity;
import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.Course;
import com.imstuding.www.handwyu.ToolUtil.MyHttpHelp;
import com.imstuding.www.handwyu.ToolUtil.UrlUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.imstuding.www.handwyu.AuditClass.AuditActivity.list_kcdm;

/**
 * Created by yangkui on 2018/8/16.
 */

public class AuditSearchDlg {

    private Context mcontext;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private AuditPostData postData;
    private Spinner audit_spinner_xnxq=null;
    private ArrayAdapter<String> spinnerTermAdapter;
    private List<String> dataTermList;
    private Spinner audit_spinner_jhlx=null;
    private Spinner audit_spinner_kkdw=null;
    private Button audit_search_cancel=null;
    private Button audit_search_yes=null;
    private EditText editText_kcmc=null;
    private EditText editText_teamc=null;
    private EditText editText_rq=null;
    private EditText editText_js=null;
    private List<Course> courseList=null;
    private ListView list_audit=null;
    private SimpleAdapter simpleAdapter=null;
    private MyLoadDlg myLoadDlg=null;

    public AuditSearchDlg(Context mcontext,AuditPostData postData,List<Course> courseList,ListView list_audit){
        this.mcontext=mcontext;
        this.postData=postData;
        this.courseList=courseList;
        this.list_audit=list_audit;
    }

    public void show(){
        builder=new AlertDialog.Builder(mcontext);
        Activity activity=(Activity)mcontext;
        LayoutInflater inflater =activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.audit_search, null);
        initDlg(view);//初始化数据
        builder.setView(view);
        builder.setCancelable(false);
        alertDialog=builder.create();
        alertDialog.show();
    }

    public void initDlg(View view){
        myLoadDlg=new MyLoadDlg(mcontext);
        audit_spinner_xnxq= (Spinner) view.findViewById(R.id.audit_spinner_xnxq);
        fillTermDataList();
        spinnerTermAdapter = new ArrayAdapter<String>(mcontext, android.R.layout.simple_spinner_dropdown_item, dataTermList);
        spinnerTermAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        audit_spinner_xnxq.setAdapter(spinnerTermAdapter);

        audit_spinner_jhlx=(Spinner) view.findViewById(R.id.audit_spinner_jhlx);
        audit_spinner_kkdw=(Spinner) view.findViewById(R.id.audit_spinner_kkdw);

        audit_spinner_xnxq.setOnItemSelectedListener(new MyItemSelectedListener());
        audit_spinner_jhlx.setOnItemSelectedListener(new MyItemSelectedListener());
        audit_spinner_kkdw.setOnItemSelectedListener(new MyItemSelectedListener());

        audit_search_cancel= (Button) view.findViewById(R.id.audit_search_cancel);
        audit_search_yes= (Button) view.findViewById(R.id.audit_search_yes);

        audit_search_cancel.setOnClickListener(new MyClickListener());
        audit_search_yes.setOnClickListener(new MyClickListener());

        editText_kcmc= (EditText) view.findViewById(R.id.audit_edit_kcmc);
        editText_teamc= (EditText) view.findViewById(R.id.audit_edit_teamc);
        editText_rq= (EditText) view.findViewById(R.id.audit_edit_rq);
        editText_js= (EditText) view.findViewById(R.id.audit_edit_js);
        //默认 postData
        String xnxqdm=audit_spinner_xnxq.getSelectedItem().toString();
        xnxqdm=xnxqdm.replaceAll("\\-\\w+\\-","0");
        postData.setXnxqdm(xnxqdm);
        postData.setJhlxdm("00");
        postData.setKkyxdm("全部");
    }

    class MyItemSelectedListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()){
                case R.id.audit_spinner_xnxq:{
                    String xnxqdm=audit_spinner_xnxq.getSelectedItem().toString();
                    xnxqdm=xnxqdm.replaceAll("\\-\\w+\\-","0");
                    postData.setXnxqdm(xnxqdm);
                    //Toast.makeText(mcontext,"0",Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.audit_spinner_jhlx:{
                    postData.setJhlxdm("0"+position);
                    //Toast.makeText(mcontext,"1",Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.audit_spinner_kkdw:{
                    postData.setKkyxdm(audit_spinner_kkdw.getSelectedItem().toString());
                    //Toast.makeText(mcontext,"2",Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    class MyClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.audit_search_cancel:{
                    alertDialog.dismiss();
                    break;
                }
                case R.id.audit_search_yes:{
                    myLoadDlg.show();
                    courseList.clear();
                    postData.setPage("1");
                    postData.setKcdm("");
                    String t_rq=editText_rq.getText().toString();
                    if (!t_rq.isEmpty()&&!t_rq.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")){
                        myLoadDlg.dismiss();
                        Toast.makeText(mcontext,t_rq+"  日期不正确",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    postData.setRq(t_rq);
                    postData.setJs(editText_js.getText().toString());
                    if (editText_kcmc.getText().toString().isEmpty()){
                        postData.setTeaxm(editText_teamc.getText().toString());
                        AuditClassThread classThread=new AuditClassThread();
                        classThread.start();
                    }else {
                        KcmcToKcdmThread kcdmThread=new KcmcToKcdmThread(editText_kcmc.getText().toString(),editText_teamc.getText().toString());
                        kcdmThread.start();
                    }
                    //Toast.makeText(mcontext,postData.getXnxqdm()+postData.getJhlxdm()+postData.getKkyxdm(),Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    }

    public void setOrUpdateSimpleAdapter(List<Map<String,String>> data){
        simpleAdapter=new SimpleAdapter(mcontext,data,R.layout.list_course_item,
                new String[]{"kcmc","js","teaxms"},new int[]{R.id.item_course_kcmc,R.id.item_course_js,R.id.item_course_teaxms});
        list_audit.setAdapter(simpleAdapter);
    }

    class AuditClassThread extends Thread{
        Message message=new Message();
        Bundle bundle=new Bundle();
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
                    }
                }
                //把数据发送出去
                if (count>0)
                    list_kcdm.add(postData.getKcdm());

                bundle.putString("name",postData.getKcmc());
                bundle.putInt("count",count);
                message.setData(bundle);
                message.what=1022;
                handle.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    kcdm= jsonObject.getString("dm");
                    kcmc=jsonObject.getString("mc");
                    //把数据发送出去
                    postData.setKcdm(kcdm);
                    postData.setTeaxm(teaxms);
                    postData.setKcmc(kcmc);
                    AuditClassThread classThread=new AuditClassThread();
                    classThread.start();
                    try {
                        classThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                //把数据发送出去
                message.what=1026;
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

    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1022:{
                    myLoadDlg.dismiss();
                    alertDialog.dismiss();
                    Bundle bundle=msg.getData();
                    int count=bundle.getInt("count");
                    String coureName=bundle.getString("name");
                    if (count==0){
                        Toast.makeText(mcontext,coureName+"  这门课没有查询到信息",Toast.LENGTH_SHORT).show();
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
                case 1026:{
                    Toast.makeText(mcontext,"没有查询到结果（错误代码：26）。",Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    };
}
