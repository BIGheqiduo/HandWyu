package com.imstuding.www.handwyu.OtherUi;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.imstuding.www.handwyu.MainUi.MainActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.MyHttpHelp;
import com.imstuding.www.handwyu.ToolUtil.UrlUtil;

public class SplashActivity extends Activity {
    private Handler handler = new Handler();
    private Runnable runnable;
    private Button jump_btn=null;
    private Bitmap bmVerifation=null;
    private ImageView splash_ad=null;
    private SharedPreferences sharedPreferences;
    private String JSESSIONID=null;
    private String m_adFlag;
    private String m_Register;
    private String id="123456";
    private int showAdTime =0;
    private static final int MY_PERMISSIONS_REQUEST_GET_IMEI = 1;
    public static boolean isAutoUpdate;
    public static boolean isShowUpdate;
    public static boolean isupdateview;
    static String static_version=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(R.layout.activity_splash);

        //初始化活动
        initActivity();
        //更新广告
        setBackGroundAd();



        handler.postDelayed(runnable = new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                testPermission();
                finish();

            }
        }, showAdTime);


    }

    public void initActivity(){
        jump_btn=(Button)findViewById(R.id.jump_btn);
        splash_ad=(ImageView)findViewById(R.id.splash_ad);
        sharedPreferences=getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        isShowUpdate=false;
        isupdateview=true;
        isAutoUpdate =getSharedPreferences("userInfo", Context.MODE_PRIVATE).getBoolean("autoUpdate",true);
        m_adFlag =sharedPreferences.getString("adFlag","");
        showAdTime =sharedPreferences.getInt("showAdTime",2000);
        m_Register =sharedPreferences.getString("register","false");
        JSESSIONID=sharedPreferences.getString("JSESSIONID","");
        id=sharedPreferences.getString("name","123456");
        static_version=getVersion();
        //测试是否登录
        myTestLoginThread testLoginThread=new myTestLoginThread();
        testLoginThread.start();

        //统计用户打开次数
        myHobbyThread hobbyThread=new myHobbyThread();
        hobbyThread.start();


        jump_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                testPermission();
                finish();
                if (runnable != null)
                    handler.removeCallbacks(runnable);
            }
        });
    }

    public String getVersion(){
        String localVersion = null;
        try {
            PackageInfo packageInfo = getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    public void testPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_GET_IMEI);
        } else
        {
            registeToMysql();
        }
    }

    public void registeToMysql(){
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        if (m_Register.equals(imei)){
            //Toast.makeText(SplashActivity.this,"已注册",Toast.LENGTH_SHORT).show();
        }else {
            myRegisterThread registerThread=new myRegisterThread(imei);
            registerThread.start();
        }

    }

    public void setBackGroundAd(){
        try {
            String path=getApplicationContext().getFilesDir().getPath();
            bmVerifation=BitmapFactory.decodeFile(path+"/"+ m_adFlag);
            if (bmVerifation==null){//如果第一次访问就去获取。否则显示广告
                sharedPreferences.edit().putString("adFlag","").commit();
            }else {
                splash_ad.setImageBitmap(bmVerifation);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sharedPreferences.edit().putString("adFlag","").commit();
        }


    }

    class myRegisterThread extends Thread {
        private String imei;
        myRegisterThread(String string){
            imei=string;
        }

        @Override
        public void run() {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(UrlUtil.registerUrl);
                httpPost.setHeader("Accept", "*/*");
                httpPost.setHeader("Connection", "keep-alive");
                httpPost.setHeader("Accept-Encoding", "identity");
                httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userImei", imei));
                httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                HttpResponse httpResponse = httpClient.execute(httpPost);
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
                jsonData+=']';
                jsonData = '['+jsonData;
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String ret=jsonObject.getString("ret");
                    String msg=jsonObject.getString("msg");
                    //把数据发送出去
                    Message message=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putString("ret",ret);
                    bundle.putString("msg",msg);
                    message.setData(bundle);
                    message.what=1005;
                    handle.sendMessage(message);//获取验证码
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_GET_IMEI) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                registeToMysql();
            } else {
                Toast.makeText(SplashActivity.this, "请不要禁止权限，谢谢", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    class myTestLoginThread extends Thread{
        @Override
        public void run() {
            try {
                MyHttpHelp httpHelp=new MyHttpHelp("http://202.192.240.29/xxzyxx!xxzyList.action","post");
                httpHelp.setHeader("Cookie","JSESSIONID="+JSESSIONID);
                httpHelp.setHeader("Referer","http://202.192.240.29/login!welcome.action");

                HttpResponse httpResponse = httpHelp.postRequire(null);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    // 请求和响应都成功了
                    HttpEntity entity = httpResponse.getEntity();
                    String response = EntityUtils.toString(entity, "utf-8");
                    parseJSONWithJSONObject(response);

                }
            } catch (Exception e) {
                Message message=new Message();
                Bundle bundle=new Bundle();
                message.what=1007;
                bundle.putInt("retcode",0);
                message.setData(bundle);
                handle.sendMessage(message);
                e.printStackTrace();
            }
        }
        private void parseJSONWithJSONObject(String jsonData) {
            Message message=new Message();
            Bundle bundle=new Bundle();
            try {
                JSONArray jsonArray = new JSONArray(jsonData);

                //把数据发送出去
                message.what=1007;
                bundle.putInt("retcode",1);
                message.setData(bundle);
                handle.sendMessage(message);
            } catch (Exception e) {
                message.what=1007;
                bundle.putInt("retcode",0);
                message.setData(bundle);
                handle.sendMessage(message);
                e.printStackTrace();
            }
        }
    }


    class myHobbyThread extends Thread{
        @Override
        public void run() {
            try {
                MyHttpHelp httpHelp=new MyHttpHelp(UrlUtil.countOpenUrl,"post");
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("id", id));
                params.add(new BasicNameValuePair("count", "1"));
                params.add(new BasicNameValuePair("version",static_version));

                HttpResponse httpResponse = httpHelp.postRequire(params);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    // 请求和响应都成功了
                    HttpEntity entity = httpResponse.getEntity();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1005:{//注册
                    Bundle bundle= msg.getData();
                    String ret=bundle.getString("ret");
                    String mgs=bundle.getString("msg");

                    if (ret.equals("true")){
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("register",mgs);
                        editor.commit();
                    }
                    break;
                }
                case 1007:{
                    Bundle bundle= msg.getData();
                    int ret = bundle.getInt("retcode");
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    if (ret==1){
                        editor.putBoolean("isLogin",true);
                    }else if (ret==0){
                        editor.putString("JSESSIONID","123456789");
                        editor.putBoolean("isLogin",false);
                    }
                    editor.commit();
                    break;
                }
            }

        }

    };
}