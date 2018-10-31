package com.imstuding.www.handwyu.ToolUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.imstuding.www.handwyu.LoadDlgUi.MyLoadDlg;
import com.imstuding.www.handwyu.MainUi.MainActivity;
import com.imstuding.www.handwyu.R;

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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Created by yangkui on 2018/10/30.
 */

public class GetScoreByNameDlg extends Thread {

    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private Context mcontext;
    private String m_jxjhdm;
    private String m_kcmc;
    private List<PoorSubJect> poorSubJectList;
    private TextView T_kcbh;//课程编号
    private TextView T_kcmc;//课程名称
    private TextView T_zcj;//总成绩
    private TextView T_cjjd;//绩点
    private TextView T_zxs;//总学时
    private TextView T_xf;//学分
    private TextView T_xdfsmc;//修读方式名称
    private TextView T_cjfsmc;//成绩方式名称
    private TextView T_pscj;//平时成绩
    private TextView detail_title;//课程详情
    private SubJect subJect;
    private int count=0;
    private MyLoadDlg myLoadDlg=null;
    public GetScoreByNameDlg(Context context, SubJect subJect){
        this.mcontext=context;
        this.m_kcmc=subJect.getKcmc();
        this.subJect=subJect;
        this.poorSubJectList=new LinkedList<PoorSubJect>();
        myLoadDlg=new MyLoadDlg(mcontext);
    }


    public void show(){
        builder=new AlertDialog.Builder(mcontext);
        Activity activity=(Activity)mcontext;
        LayoutInflater inflater =activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.poor_score_detail, null);
        initDlg(view);//初始化数据
        builder.setView(view);
        builder.setCancelable(true);
        alertDialog=builder.create();
        alertDialog.show();
        GetScoreByNameThread getScoreByNameThread=new GetScoreByNameThread();
        getScoreByNameThread.start();
        myLoadDlg.show();
    }

    public void initDlg(View view){
        try{
            detail_title= (TextView) view.findViewById(R.id.detail_title);
            T_kcbh= (TextView) view.findViewById(R.id.score_detail_kcbh);
            T_kcmc= (TextView) view.findViewById(R.id.score_detail_kcmc);
            T_zcj= (TextView) view.findViewById(R.id.score_detail_zcj);
            T_cjjd= (TextView) view.findViewById(R.id.score_detail_cjjd);
            T_zxs= (TextView) view.findViewById(R.id.score_detail_zxs);
            T_xf= (TextView) view.findViewById(R.id.score_detail_xf);
            T_xdfsmc= (TextView) view.findViewById(R.id.score_detail_xdfsmc);
            T_cjfsmc= (TextView) view.findViewById(R.id.score_detail_cjfsmc);
            T_pscj= (TextView) view.findViewById(R.id.score_detail_pscj);

            T_kcbh.setText(subJect.getKcbh());
            T_kcmc.setText(subJect.getKcmc());
            T_zcj.setText(subJect.getZcj());
            T_cjjd.setText(subJect.getCjjd());
            T_zxs.setText(subJect.getZxs());
            T_xf.setText(subJect.getXf());
            T_xdfsmc.setText(subJect.getXdfsmc());
            T_cjfsmc.setText(subJect.getCjfsmc());
            T_pscj.setText(subJect.getPscj());

        }catch (Exception e){
            T_kcbh.setText("NULL");
            T_kcmc.setText("NULL");
            T_zcj.setText("NULL");
            T_cjjd.setText("NULL");
            T_zxs.setText("NULL");
            T_xf.setText("NULL");
            T_xdfsmc.setText("NULL");
            T_cjfsmc.setText("NULL");
            T_pscj.setText("NULL");
        }


    }


    class GetScoreByNameThread extends Thread{
        @Override
        public void run() {
            try {
                MyHttpHelp httpHelp=new MyHttpHelp("http://202.192.240.29/xsjxjhxx!getDataList1.action","post");
                httpHelp.setHeader("Cookie","JSESSIONID="+ MainActivity.getJsessionId());
                httpHelp.setHeader("Referer","http://202.192.240.29/xsjxjhxx!xsjxjhList.action?lx=01");
                httpHelp.setHeader("Accept-Encoding","gzip, deflate");

                HttpResponse httpResponse = httpHelp.postRequire(null);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    // 请求和响应都成功了
                    HttpEntity entity = httpResponse.getEntity();
                    String response = parseGzip(entity);
                    parseJSONWithJSONObject(response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.run();
        }

        private void parseJSONWithJSONObject(String jsonData) {
            try {
                jsonData+=']';
                jsonData = '['+jsonData;
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONArray retArray=jsonObject.getJSONArray("rows");
                    for (int j = 0; j < retArray.length(); j++) {
                        JSONObject retobject =retArray.getJSONObject(j);
                        String jxjhdm=retobject.getString("jxjhdm");//jxjhdm=J08022015
                        String jhlxmc=retobject.getString("jhlxmc");//培养方案
                        if (jhlxmc.equals("培养方案")){
                            m_jxjhdm=jxjhdm;
                            GetScoreThread getScoreThread=new GetScoreThread();
                            getScoreThread.start();
                            return;
                        }
                    }
                }
                //把数据发送出去
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

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

    class GetScoreThread extends Thread{
        @Override
        public void run() {
            try {
                MyHttpHelp httpHelp=new MyHttpHelp("http://202.192.240.29/xsjxjhxx!getDataList.action","post");
                httpHelp.setHeader("Cookie","JSESSIONID="+ MainActivity.getJsessionId());
                //httpHelp.setHeader("Referer","http://202.192.240.29/xsjxjhxx!xsxxjhMain.action?jxjhdm=J08022015&jhlxdm=01&jhfxdm=");
                httpHelp.setHeader("Accept-Encoding","gzip, deflate");
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("jxjhdm", m_jxjhdm));
                params.add(new BasicNameValuePair("jhfxdm", ""));
                params.add(new BasicNameValuePair("primarySort", "rwdm asc"));
                params.add(new BasicNameValuePair("page", "1"));
                params.add(new BasicNameValuePair("rows", "150"));
                params.add(new BasicNameValuePair("sort", "xnxqdm1"));
                params.add(new BasicNameValuePair("order", "asc"));

                HttpResponse httpResponse = httpHelp.postRequire(params);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    // 请求和响应都成功了
                    HttpEntity entity = httpResponse.getEntity();
                    String response = parseGzip(entity);
                    parseJSONWithJSONObject(response);
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            super.run();
        }

        private void parseJSONWithJSONObject(String jsonData) {
            try {
                jsonData+=']';
                jsonData = '['+jsonData;
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONArray retArray=jsonObject.getJSONArray("rows");
                    for (int j = 0; j < retArray.length(); j++) {
                        JSONObject retobject =retArray.getJSONObject(j);
                        String kcmc= retobject.getString("ckcmc");//课程名称
                        String zcj=retobject.getString("zcj");//总成绩
                        String xf=retobject.getString("cxf");//学分
                        String xdfsmc=retobject.getString("cxdfsmc");//修读方式
                        String kcdm=retobject.getString("ckcdm");//课程代码
                        String zxs=retobject.getString("czxs");//学时
                        if (kcmc.equals(m_kcmc)){
                            poorSubJectList.add(new PoorSubJect(kcdm,kcmc,zcj,zxs,xf,xdfsmc));
                        }
                    }
                }
                Message message=new Message();
                message.what=1024;
                handle.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1024:{
                    if (poorSubJectList.size()>1){
                        Toast.makeText(mcontext,"发现有相同名称的课程，请点击标题切换课程！(课程详情)",Toast.LENGTH_SHORT).show();
                        detail_title.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                count++;
                                int t=count%poorSubJectList.size();
                                setDlgData(poorSubJectList.get(t));
                            }
                        });
                    }
                    setDlgData(poorSubJectList.get(0));
                    myLoadDlg.dismiss();
                    break;
                }
            }
        }
    };

    private void setDlgData(PoorSubJect poorSubJect){
        T_zcj.setText(poorSubJect.getZcj());
        T_cjjd.setText("**");
        T_xf.setText(poorSubJect.getXf());
    }

}
