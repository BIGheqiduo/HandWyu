package com.imstuding.www.handwyu.OtherUi;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.imstuding.www.handwyu.ToolUtil.UrlUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by yangkui on 2018/9/9.
 */

public class GetConfig {
    private Context mContext;
    public UrlUtil util;
    private String testAdUrl =null;
    private Bitmap bmVerifation=null;
    private String m_adFlag;
    private SharedPreferences sharedPreferences;
    public GetConfig(Context context){
        mContext=context;
        //初始化配置文件
        util=new UrlUtil(mContext);
        sharedPreferences=mContext.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        m_adFlag =sharedPreferences.getString("adFlag","");

        testAdUrl =UrlUtil.testAdUrl;
    }

    public void update(){
        myTestAdThread testAdThread=new myTestAdThread();
        testAdThread.start();
    }
    class myUpdateAdThread extends Thread {
        private String adUrl;
        public myUpdateAdThread(String url){
            adUrl=url;
        }
        @Override
        public void run() {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(adUrl);
                httpPost.setHeader("Accept", "*/*");
                httpPost.setHeader("Connection", "keep-alive");
                httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
                HttpResponse httpResponse = httpClient.execute(httpPost);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    // 请求和响应都成功了
                    byte[] bytes;
                    bytes = EntityUtils.toByteArray(httpResponse.getEntity());
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putByteArray("AD", bytes);
                    message.setData(bundle);
                    message.what = 1003;
                    handle.sendMessage(message);//获取验证码
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class myTestAdThread extends Thread {
        @Override
        public void run() {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(testAdUrl);
                httpGet.setHeader("Accept", "*/*");
                httpGet.setHeader("Connection", "keep-alive");
                httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
                HttpResponse httpResponse = httpClient.execute(httpGet);
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
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String adFlag=jsonObject.getString("adFlag");
                    String adUrl=jsonObject.getString("adUrl");
                    int showAdTime=jsonObject.getInt("showAdTime");
                    String testAdUrl=jsonObject.getString("testAdUrl");
                    boolean redPaper=jsonObject.getBoolean("redPaper");
                    String libraryUrl=jsonObject.getString("libraryUrl");
                    String busUrl=jsonObject.getString("busUrl");
                    String schoolDateUrl=jsonObject.getString("schoolDateUrl");
                    String youDaoUrl=jsonObject.getString("youDaoUrl");

                    //把数据发送出去
                    Message message=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putString("adFlag",adFlag);
                    bundle.putString("adUrl",adUrl);
                    bundle.putInt("showAdTime",showAdTime);
                    bundle.putString("testAdUrl",testAdUrl);
                    bundle.putBoolean("redPaper",redPaper);
                    bundle.putString("libraryUrl",libraryUrl);
                    bundle.putString("busUrl",busUrl);
                    bundle.putString("schoolDateUrl",schoolDateUrl);
                    bundle.putString("youDaoUrl",youDaoUrl);

                    message.setData(bundle);
                    message.what=1004;
                    handle.sendMessage(message);//获取验证码
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1003: {//获取广告
                    Bundle bundle = msg.getData();
                    byte bytes[] = bundle.getByteArray("AD");
                    bmVerifation = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    try {
                        File file = new File(mContext.getFilesDir(), "/" + m_adFlag);
                        FileOutputStream out = new FileOutputStream(file);
                        bmVerifation.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("adFlag", m_adFlag);
                        editor.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 1004: {//测试是否更新广告
                    Bundle bundle = msg.getData();
                    String adFlag = bundle.getString("adFlag");
                    String adUrl = bundle.getString("adUrl");
                    int showAdTime = bundle.getInt("showAdTime");
                    String testAdUrl = bundle.getString("testAdUrl");
                    boolean redPaper = bundle.getBoolean("redPaper");
                    String libraryUrl = bundle.getString("libraryUrl");
                    String busUrl = bundle.getString("busUrl");
                    String schoolDateUrl = bundle.getString("schoolDateUrl");
                    String youDaoUrl = bundle.getString("youDaoUrl");


                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if (!m_adFlag.equals(adFlag)) {
                        m_adFlag = adFlag;
                        myUpdateAdThread thread = new myUpdateAdThread(adUrl);
                        thread.start();//更换广告
                    }
                    editor.putInt("showAdTime", showAdTime);
                    editor.putBoolean("redPaper", redPaper);

                    util.setUpdateUrl(libraryUrl, busUrl, schoolDateUrl, youDaoUrl);
                    editor.commit();
                    break;
                }
            }
        }
    };
}
