package com.imstuding.www.handwyu.OtherUi;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.imstuding.www.handwyu.LoadDlgUi.MyLoadDlg;
import com.imstuding.www.handwyu.ToolAdapter.ExamAdapter;
import com.imstuding.www.handwyu.ToolUtil.ExamDetailDlg;
import com.imstuding.www.handwyu.ToolUtil.ExamInf;
import com.imstuding.www.handwyu.WebViewDlg.LoginActivity;
import com.imstuding.www.handwyu.MainUi.MainActivity;
import com.imstuding.www.handwyu.ToolUtil.MyHttpHelp;
import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.UrlUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Created by yangkui on 2018/3/27.
 */

public class ExamFragment extends Fragment {

    private Context mcontext=null;
    private View view=null;
    private List<String> dataTermList;
    private ArrayAdapter<String> spinnerTermAdapter;
    private Spinner spinner=null;
    private Button btn_exam=null;
    private ListView list_exam=null;
    private MyLoadDlg myLoadDlg=null;
    private List<ExamInf> examInfList;//考试安排对象
    private ExamAdapter examAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mcontext=getActivity();
        view=inflater.inflate(R.layout.fragment_exam,container,false);
        initFragment(view);
        return view;
    }

    public void initFragment(View view){
        examInfList=new LinkedList<ExamInf>();
        myLoadDlg=new MyLoadDlg(mcontext);
        list_exam= (ListView) view.findViewById(R.id.exam_list);

        list_exam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExamDetailDlg examDetailDlg =new ExamDetailDlg(mcontext,examInfList.get(position));
                examDetailDlg.show();
                //Toast.makeText(mcontext,position+"",Toast.LENGTH_SHORT).show();
            }
        });

        btn_exam= (Button) view.findViewById(R.id.btn_exam);
        btn_exam.setOnClickListener(new MyClickListener());
        //////////////创建学期Spinner数据
        spinner= (Spinner) view.findViewById(R.id.spinner_exam_date);
        fillTermDataList();
        spinnerTermAdapter = new ArrayAdapter<String>(mcontext, android.R.layout.simple_spinner_dropdown_item, dataTermList);
        spinnerTermAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerTermAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                btn_exam.callOnClick();
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


    class MyClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_exam:{
                    myLoadDlg.show();
                    examInfList.clear();
                    myExamThread examThread=new myExamThread();
                    examThread.start();
                    break;
                }
            }
        }
    }


    class myExamThread extends Thread{
        Message message=new Message();
        Bundle bundle=new Bundle();

        @Override
        public void run() {
            try {
                MyHttpHelp httpHelp=new MyHttpHelp(UrlUtil.examUrl,"post");
                httpHelp.setHeader("Cookie","JSESSIONID="+ MainActivity.getJsessionId());
                httpHelp.setHeader("Referer","http://202.192.240.29/xsksap!ksapList.action");
                httpHelp.setHeader("Accept-Encoding","gzip, deflate");
                httpHelp.setHeader("Accept","application/json, text/javascript, */*; q=0.01");
                httpHelp.setHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
                httpHelp.setHeader("Accept-Language","zh-CN,zh;q=0.9");
                httpHelp.setHeader("X-Requested-With","XMLHttpRequest");
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String xnxqdm=spinner.getSelectedItem().toString();
                xnxqdm=xnxqdm.replaceAll("\\-\\w+\\-","0");
                params.add(new BasicNameValuePair("xnxqdm", xnxqdm));
                params.add(new BasicNameValuePair("ksaplxdm", ""));
                params.add(new BasicNameValuePair("page", "1"));
                params.add(new BasicNameValuePair("rows", "20"));
                params.add(new BasicNameValuePair("sort", "zc,xq,jcdm2"));
                params.add(new BasicNameValuePair("order", "asc"));

                HttpResponse httpResponse = httpHelp.postRequire(params);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    // 请求和响应都成功了
                    HttpEntity entity = httpResponse.getEntity();
                    String response = parseGzip(entity);
                    parseJSONWithJSONObject(response);

                }
            } catch (Exception e) {
                myLoadDlg.dismiss();
                message.what=1007;
                handle.sendMessage(message);
                e.printStackTrace();
            }
        }

        /**
         * 解Gzip压缩
         *
         *
         * @throws IllegalStateException
         */
        private String parseGzip(HttpEntity entity) throws Exception {
            InputStream in = entity.getContent();
            GZIPInputStream gzipInputStream = new GZIPInputStream(in);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    gzipInputStream, HTTP.UTF_8));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine())!= null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }

        private void parseJSONWithJSONObject(String jsonData) {

            Message message=new Message();
            Bundle bundle=new Bundle();
            try {
                jsonData+=']';
                jsonData = '['+jsonData;
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONArray retArray=jsonObject.getJSONArray("rows");
                    for (int j = 0; j < retArray.length(); j++) {
                        JSONObject retobject =retArray.getJSONObject(j);
                        String kcmc= retobject.getString("kcmc");
                        String jkteaxms=retobject.getString("jkteaxms");
                        String ksaplxmc=retobject.getString("ksaplxmc");
                        String kscdmc=retobject.getString("kscdmc");
                        String zc= retobject.getString("zc");
                        String xq=retobject.getString("xq");
                        String ksrq=retobject.getString("ksrq");
                        String kssj=retobject.getString("kssj");
                        examInfList.add(new ExamInf(kcmc,jkteaxms,ksaplxmc,kscdmc,zc ,xq,ksrq,kssj));
                    }
                }
                //把数据发送出去
                message.what=1009;
                bundle.putInt("ret",1);
                message.setData(bundle);
                handle.sendMessage(message);
            } catch (Exception e) {
                myLoadDlg.dismiss();
                message.what=1006;
                handle.sendMessage(message);
                e.printStackTrace();
            }
        }
    }

    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1009: {
                    myLoadDlg.dismiss();
                    Bundle bundle= msg.getData();
                    int ret= bundle.getInt("ret");
                    if (ret==1&&examInfList.size()!=0){
                        Toast.makeText(mcontext,"查询到"+examInfList.size()+"个考试安排",Toast.LENGTH_SHORT).show();
                        examAdapter=new ExamAdapter(mcontext,R.layout.list_exam_item,examInfList);
                        list_exam.setAdapter(examAdapter);
                    }else {
                        Toast.makeText(mcontext,"暂时没有考试安排",Toast.LENGTH_SHORT).show();
                        list_exam.setAdapter(examAdapter);
                    }
                    break;
                }
                case 1006:{
                    myLoadDlg.dismiss();
                    Toast.makeText(mcontext,"请先登录",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.setClass(mcontext,LoginActivity.class);
                    startActivity(intent);
                    break;
                }
                case 1007:{
                    myLoadDlg.dismiss();
                    Toast.makeText(mcontext,R.string.offline,Toast.LENGTH_SHORT).show();
                    break;
                }

            }
        }
    };

}
