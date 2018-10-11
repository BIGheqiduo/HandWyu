package com.imstuding.www.handwyu.MainUi;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.imstuding.www.handwyu.OtherUi.GetConfig;
import com.imstuding.www.handwyu.ToolUtil.BottomNavigationViewHelper;
import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.MainTitle;
import com.imstuding.www.handwyu.ToolUtil.MyFillListNotice;
import com.imstuding.www.handwyu.ToolUtil.MyNotice;
import com.imstuding.www.handwyu.ToolUtil.TitleView;
import com.imstuding.www.handwyu.ToolUtil.UrlUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TableFragment tableFragment;
    private ScoreFragment scoreFragment;
    private HomeFragment homeFragment=null;
    private Notice_Fragment notice_fragment=null;
    private static Context static_context;
    private MyFillListNotice fillListNotice;
    private Fragment top;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initActivity();

    }
    /*
    * 初始化activity
    * */
    private void initActivity(){
        static_context =getApplicationContext();
        //初始化fragment
        initPage();
        fillListNotice=new MyFillListNotice(MainActivity.this);

        GetNoticeThread noticeThread=new GetNoticeThread(fillListNotice.getMaxId());
        noticeThread.start();

        GetConfig getConfig=new  GetConfig(MainActivity.this);
        getConfig.update();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);//取消滑动风格
        ColorStateList csl=getResources().getColorStateList(R.color.navi_text);
        navigation.setItemIconTintList( csl);
        navigation.setItemTextColor(csl);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_notice:{
                        if(notice_fragment==null)
                            notice_fragment=new Notice_Fragment();
                        switchFragment(notice_fragment);
                        return true;
                    }
                    case R.id.navigation_table:{
                        if (tableFragment==null)
                            tableFragment=new TableFragment();
                        switchFragment(tableFragment);
                        return true;
                    }
                    case R.id.navigation_score:{
                        if(scoreFragment==null)
                            scoreFragment=new ScoreFragment();
                        switchFragment(scoreFragment);
                        return true;
                    }
                    case R.id.navigation_find: {
                        if(homeFragment==null)
                            homeFragment=new HomeFragment();
                        switchFragment(homeFragment);
                        return true;
                    }
                }
                return false;
            }
        });
        navigation.setSelectedItemId(R.id.navigation_notice);
    }

    public void initPage(){
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        top = new Notice_Fragment();
        notice_fragment=(Notice_Fragment) top;
        transaction.replace(R.id.content,top);
        transaction.commitAllowingStateLoss();
    }

    private void switchFragment(Fragment to) {
        if (top != to) {
            FragmentManager fragmentManager=getFragmentManager();
            FragmentTransaction transaction=fragmentManager.beginTransaction();
            if (!to.isAdded()) { // 先判断是否被add过
                transaction.hide(top).add(R.id.content, to).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(top).show(to).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            }
            top = to;
        }
    }


    public static void setJessionId(String string){
        static_context.getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit().putString("JSESSIONID",string).commit();
    }

    public static String getJsessionId(){
        return static_context.getSharedPreferences("userInfo", Context.MODE_PRIVATE).getString("JSESSIONID","");
    }

    public static boolean isLogin(){
        return static_context.getSharedPreferences("userInfo", Context.MODE_PRIVATE).getBoolean("isLogin",false);
    }

    public static void setLogin(boolean flag){
       static_context.getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit().putBoolean("isLogin",flag).commit();
    }

    public static void setArrow(boolean flag){
        static_context.getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit().putBoolean("isArrow",flag).commit();
    }
    public static boolean getArrow(){
        return static_context.getSharedPreferences("userInfo", Context.MODE_PRIVATE).getBoolean("isArrow",true);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    class GetNoticeThread extends Thread {
        private String id;
        GetNoticeThread(String string){
            id=string;
        }

        @Override
        public void run() {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(UrlUtil.getnotceUrl);
                httpPost.setHeader("Accept", "*/*");
                httpPost.setHeader("Connection", "keep-alive");
                httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id", id));
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
            List<MyNotice> noticelist=new LinkedList<>();
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id=jsonObject.getString("id");
                    String title=jsonObject.getString("title");
                    String time=jsonObject.getString("time");
                    String content=jsonObject.getString("content");
                    noticelist.add(new MyNotice(id,title,content,time,Boolean.parseBoolean("true")));
                }
                fillListNotice.setNoticeData(noticelist);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
