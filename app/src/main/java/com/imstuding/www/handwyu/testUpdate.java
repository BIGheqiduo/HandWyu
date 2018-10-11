package com.imstuding.www.handwyu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.imstuding.www.handwyu.LoadDlgUi.MyLoadDlg;
import com.imstuding.www.handwyu.ToolUtil.MyHttpHelp;
import com.imstuding.www.handwyu.ToolUtil.UrlUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.imstuding.www.handwyu.OtherUi.SplashActivity.isAutoUpdate;
import static com.imstuding.www.handwyu.OtherUi.SplashActivity.isShowUpdate;

/**
 * Created by yangkui on 2018/3/29.
 */

public class testUpdate {
    private String testUpdateUrl;
    private Context mcontext;
    AlertDialog.Builder builder;
    private MyLoadDlg myLoadDlg;
    private SharedPreferences sharedPreferences;
    private boolean flag;
    private boolean autoflag;
    public testUpdate(Context context,boolean flag,boolean autoflag){
        mcontext=context;
        this.flag=flag;
        this.autoflag=autoflag;
        sharedPreferences=mcontext.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        testUpdateUrl= UrlUtil.updateUrl;
        myLoadDlg=new MyLoadDlg(context);
        if (flag)
            myLoadDlg.show();
    }

    public void update(){
        myUpdateThread updateThread=new myUpdateThread(testUpdateUrl);
        updateThread.start();
    }
    public String getVersion(){
        String localVersion = null;
        try {
            PackageInfo packageInfo = mcontext.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(mcontext.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }


    private void showSimpleDialog(final String appurl,String updateString) {
        builder=new AlertDialog.Builder(mcontext);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(R.string.app_name);
        builder.setMessage(updateString);

        //监听下方button点击事件
        builder.setPositiveButton("前去下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(appurl);
                intent.setData(content_url);
                mcontext.startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(mcontext, "请尽快更新最新版本，谢谢", Toast.LENGTH_SHORT).show();
            }
        });

        //设置对话框是可取消的
        builder.setCancelable(true);
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    class myUpdateThread extends Thread{
        private String url;

        public myUpdateThread(String url){
            this.url=url+"?oldversion="+getVersion();
        }

        @Override
        public void run() {
            try {
                MyHttpHelp httpHelp=new MyHttpHelp(url,"get");
                HttpResponse httpResponse = httpHelp.getRequire(null);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    // 请求和响应都成功了
                    HttpEntity entity = httpResponse.getEntity();
                    String response = EntityUtils.toString(entity, "utf-8");
                    parseJSONWithJSONObject(response);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Message message=new Message();
                message.what=1010;
                handle.sendMessage(message);//更新
            }
        }


        private void parseJSONWithJSONObject(String jsonData) {
            boolean flag=false;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String version=jsonObject.getString("version");
                    String appurl=jsonObject.getString("appurl");
                    String testUpdateUrl=jsonObject.getString("testUpdateUrl");
                    String updateString=jsonObject.getString("updateString");
                    //把数据发送出去
                    flag=true;
                    Message message=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putString("version",version);
                    bundle.putString("appurl",appurl);
                    bundle.putString("testUpdateUrl",testUpdateUrl);
                    bundle.putString("updateString",updateString);
                    message.setData(bundle);
                    message.what=1009;
                    handle.sendMessage(message);//更新
                }
                if (flag==false){
                    Message message=new Message();
                    message.what=1010;
                    handle.sendMessage(message);//更新
                }
            } catch (Exception e) {
                Message message=new Message();
                message.what=1010;
                handle.sendMessage(message);//更新
                e.printStackTrace();
            }
        }
    }

    private boolean versionCompare(String oldversion,String newversion){
        String oArr;
        oArr= oldversion.replaceAll("\\.","");
        String nArr;
        nArr=newversion.replaceAll("\\.","");
        try{
            if (Integer.parseInt(oArr)<Integer.parseInt(nArr)){
                return false;
            }else {
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }

    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1009: {//更新
                    if (flag)
                        myLoadDlg.dismiss();
                    Bundle bundle= msg.getData();
                    String appurl=bundle.getString("appurl");
                    String version=bundle.getString("version");
                    String testUpdateUrl=bundle.getString("testUpdateUrl");
                    String updateString=bundle.getString("updateString");

                    String oversion=getVersion();
                    if (versionCompare(oversion.toString(),version.toString())){
                        isShowUpdate =false;
                        if (flag)
                            Toast.makeText(mcontext,"当前已经是最新版",Toast.LENGTH_SHORT).show();
                    }else {
                        isShowUpdate =true;
                        if (autoflag)
                            showSimpleDialog(appurl,updateString);
                    }

                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("testUpdateUrl",testUpdateUrl);

                    editor.commit();
                    break;
                }
                case 1010:{
                    myLoadDlg.dismiss();
                    //Toast.makeText(mcontext,"当前已经是最新版",Toast.LENGTH_SHORT).show();
                    break;
                }
            }

        }

    };
}
