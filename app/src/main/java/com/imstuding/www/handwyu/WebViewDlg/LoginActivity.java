package com.imstuding.www.handwyu.WebViewDlg;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.imstuding.www.handwyu.LoadDlgUi.MyLoadDlg;
import com.imstuding.www.handwyu.MainUi.MainActivity;
import com.imstuding.www.handwyu.MainUi.SocreActivity;
import com.imstuding.www.handwyu.OtherUi.OtherActivity;
import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.MyHttpHelp;
import com.imstuding.www.handwyu.ToolUtil.UrlUtil;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;
import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity {

    private NotificationManager manager;
    private int notification_id;
    private EditText wyu_id=null;
    private EditText wyu_pwd=null;
    private EditText wyu_verifycode=null;
    private ImageView wyu_verpci=null;
    private Button wyu_login=null;
    private Bitmap bmVerifation=null;
    private String VERIFATIONURL= UrlUtil.verifyUrl;
    private String LOGINURL=UrlUtil.loginUrl;
    private String JSESSIONID=null;
    private SharedPreferences sharedPreferences;
    private CheckBox cbIsRememberPass=null;
    private LinearLayout main_layout=null;
    private MyLoadDlg myLoadDlg=null;
    private ImageView imageView_menu=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去除自带的title
        setContentView(R.layout.activity_login);
        initActivity();//初始化activity
        initData();//初始化数据
    }

    private void myPopMenu(){
        imageView_menu= (ImageView)findViewById(R.id.title_menu);
        imageView_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(LoginActivity.this, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.login_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_login_help:{
                                Intent intent=new Intent();
                                intent.setClass(LoginActivity.this,OtherActivity.class);
                                intent.putExtra("msg","help");
                                startActivity(intent);
                                break;
                            }
                            case R.id.menu_login_pwd:{
                                Intent intent=new Intent();
                                intent.setClass(LoginActivity.this,OtherActivity.class);
                                intent.putExtra("msg","RetrievePwd");
                                startActivity(intent);
                                break;
                            }
                            case R.id.menu_login_bug:{
                                Intent intent=new Intent();
                                intent.setClass(LoginActivity.this,OtherActivity.class);
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

    public void initData(){
        myPopMenu();
        sharedPreferences=getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        boolean isRemember=sharedPreferences.getBoolean("rememberpassword",false);
        String name=sharedPreferences.getString("name","");
        wyu_id.setText(name);
        if(isRemember){
            String password=sharedPreferences.getString("password","");
            wyu_pwd.setText(password);
            cbIsRememberPass.setChecked(true);
        }
    }

    private void initActivity(){

        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        main_layout=(LinearLayout)findViewById(R.id.main_layout);
        cbIsRememberPass=(CheckBox)findViewById(R.id.wyu_keeppwd);
        cbIsRememberPass.setChecked(true);
        wyu_id=(EditText)findViewById(R.id.wyu_id);
        wyu_pwd=(EditText)findViewById(R.id.wyu_pwd);
        wyu_verifycode=(EditText)findViewById(R.id.wyu_verifycode);
        wyu_verpci=(ImageView)findViewById(R.id.wyu_verpic);
        wyu_login=(Button)findViewById(R.id.wyu_login);
        wyu_login.setOnClickListener(new MyClickListener());
        wyu_verpci.setOnClickListener(new MyClickListener());
        main_layout.setOnClickListener(new MyClickListener());
        wyu_verpci.callOnClick();
    }

    class MyClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.wyu_login:{
                    // Toast.makeText(LoginActivity.this,"登录",Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("name",wyu_id.getText().toString());
                    if(cbIsRememberPass.isChecked()){
                        editor.putBoolean("rememberpassword",true);
                        editor.putString("password",wyu_pwd.getText().toString());
                    }else {
                        editor.remove("password");
                        //editor.clear();
                    }
                    editor.commit();
                    myLoadDlg=new MyLoadDlg(LoginActivity.this);
                    myLoadDlg.show();

                    myLoginThread loginThread = new  myLoginThread(wyu_id.getText().toString(),wyu_pwd.getText().toString(),wyu_verifycode.getText().toString());
                    loginThread.start();
                    break;
                }
                case R.id.wyu_verpic:{
                    //Toast.makeText(LoginActivity.this,"获取验证码",Toast.LENGTH_SHORT).show();
                    myVerifyThread s=new myVerifyThread();
                    s.start();
                    break;
                }
                case R.id.main_layout:{
                    InputMethodManager imm = (InputMethodManager)LoginActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
                    if (isOpen){
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    break;
                }
            }
        }
    }

    class myVerifyThread extends Thread{
        @Override
        public void run() {
            try {
                MyHttpHelp httpHelp=new MyHttpHelp(VERIFATIONURL,"get");
                HttpResponse httpResponse = httpHelp.getRequire(null);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    SaveCookies(httpResponse);//保存JSESSIONID
                    // 请求和响应都成功了
                    byte[] bytes;
                    bytes = EntityUtils.toByteArray(httpResponse.getEntity());
                    Message message=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putByteArray("verify",bytes);
                    message.setData(bundle);
                    message.what=1001;
                    handle.sendMessage(message);//获取验证码
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void SaveCookies(HttpResponse httpResponse) {
            Header[] headers = httpResponse.getHeaders("Set-Cookie");
            String headerstr=headers.toString();
            if (headers == null)
                return;

            for(int i=0;i<headers.length;i++)
            {
                String cookie=headers[i].getValue();
                String[]cookievalues=cookie.split(";");
                String[] keyPair=cookievalues[0].split("=");
                String key=keyPair[0].trim();
                String value=keyPair.length>1?keyPair[1].trim():"";
                JSESSIONID=new String(value);
            }
        }

    }


    class myReadThread extends Thread{
        String readUrl;
        public myReadThread(String xxid){
            readUrl="http://202.192.240.29/notice!readed.action?xxids="+xxid;
        }
        @Override
        public void run() {
            try {
                MyHttpHelp httpHelp=new MyHttpHelp(readUrl,"get");
                httpHelp.setHeader("Cookie","JSESSIONID="+JSESSIONID);

                HttpResponse httpResponse = httpHelp.getRequire(null);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    // 请求和响应都成功了
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
    }

    class myNoticeThread extends Thread{
        @Override
        public void run() {
            try {
                MyHttpHelp httpHelp=new MyHttpHelp("http://202.192.240.29/notice!getNotice.action?_=1533447499933","get");
                httpHelp.setHeader("Cookie","JSESSIONID="+JSESSIONID);

                HttpResponse httpResponse = httpHelp.getRequire(null);
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
            ArrayList<String> subject= new ArrayList<String>();
            ArrayList<String> xxid= new ArrayList<String>();
            Message message=new Message();
            Bundle bundle=new Bundle();

            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id=jsonObject.getString("xxid");
                    String tmp=jsonObject.get("msg").toString();
                    JSONArray jsonArray1 = new JSONArray("["+tmp+"]");
                    jsonObject=jsonArray1.getJSONObject(0);
                    String kcmc=jsonObject.getString("kcmc");

                    subject.add(kcmc);
                    xxid.add(id);
                }
                //把数据发送出去
                bundle.putStringArrayList("subject",subject);
                bundle.putStringArrayList("xxid",xxid);
                message.setData(bundle);
                message.what=1008;
                handle.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendNotice(String title,String text,String ticker){
        Notification.Builder builder = new Notification.Builder(LoginActivity.this);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setTicker(ticker);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle(title);
        builder.setContentText(text);

        Intent intent = new Intent(LoginActivity.this, SocreActivity.class);
        PendingIntent ma = PendingIntent.getActivity(LoginActivity.this,0,intent,0);
        builder.setContentIntent(ma);//设置点击过后跳转的activity

        builder.setDefaults(Notification.DEFAULT_ALL);//设置全部

        Notification notification = builder.build();//4.1以上用.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;// 点击通知的时候cancel掉
        manager.notify(notification_id,notification);
    }

    class myLoginThread extends Thread{
        private String id;
        private String pwd;
        private String verify;
        public myLoginThread(String id,String pwd,String verify){
            this.id=id;
            this.pwd=pwd;
            this.verify=verify;
        }
        @Override
        public void run() {
            try {
                MyHttpHelp httpHelp=new MyHttpHelp(LOGINURL,"post");
                httpHelp.setHeader("Cookie","JSESSIONID="+JSESSIONID);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("account", id));
                params.add(new BasicNameValuePair("pwd", pwd));
                params.add(new BasicNameValuePair("verifycode", verify));
                HttpResponse httpResponse = httpHelp.postRequire(params);
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
            try {
                jsonData+=']';
                jsonData = '['+jsonData;
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int ret=jsonObject.getInt("code");
                    String string=jsonObject.getString("message");
                    //把数据发送出去
                    Message message=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putInt("retcode",ret);
                    bundle.putString("message",string);
                    message.setData(bundle);
                    message.what=1002;
                    handle.sendMessage(message);
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
                case 1001: {//获取验证码
                    Bundle bundle= msg.getData();
                    byte bytes[]=bundle.getByteArray("verify");
                    bmVerifation = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    wyu_verpci.setImageBitmap(bmVerifation);
                    break;
                }
                case 1002:{//登录
                    Bundle bundle= msg.getData();
                    int ret=bundle.getInt("retcode");
                    if (ret==0){
                        myLoadDlg.dismiss();
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putBoolean("isLogin",true);
                        editor.putString("JSESSIONID",JSESSIONID);
                        editor.commit();
                        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();

                        myNoticeThread noticeThread=new myNoticeThread();
                        noticeThread.start();//去看看是否有新课程成绩信息

                        finish();
                    }else if (ret==-1){
                        myLoadDlg.dismiss();
                        String string=bundle.getString("message");
                        Toast.makeText(LoginActivity.this,string,Toast.LENGTH_SHORT).show();
                        wyu_verpci.callOnClick();
                    }
                    break;
                }
                case 1007:{
                    myLoadDlg.dismiss();
                    Toast.makeText(LoginActivity.this,R.string.offline,Toast.LENGTH_SHORT).show();
                    break;
                }
                case 1008:{
                    Bundle bundle= msg.getData();
                    String text=new String();
                    ArrayList<String> subject = bundle.getStringArrayList("subject");
                    ArrayList<String> xxid = bundle.getStringArrayList("xxid");

                    for (int i=0;i<subject.size();i++){
                        text+=subject.get(i)+" ";
                        //把成绩消息变成已读
                        myReadThread readThread=new myReadThread(xxid.get(i));
                        readThread.start();
                    }
                    if (subject.size()>0)
                        sendNotice("课程成绩出来了，点我去查！","以下课程已经出成绩了 "+text,"有课程成绩出来了，快去查看！");
                    // Toast.makeText(LoginActivity.this,text,Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }

    };
}
