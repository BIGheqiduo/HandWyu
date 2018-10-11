package com.imstuding.www.handwyu.OtherUi;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.imstuding.www.handwyu.LoadDlgUi.MyLoadDlg;
import com.imstuding.www.handwyu.MainUi.MainActivity;
import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.MyHttpHelp;
import com.imstuding.www.handwyu.ToolUtil.UrlUtil;
import com.imstuding.www.handwyu.WebViewDlg.LoginActivity;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangkui on 2018/10/4.
 */

public class RetrievePwdFragment  extends Fragment {

    private Context mcontext;
    private View view;
    private LinearLayout layout_retrieve;//retrieve_newpwd_layout
    private EditText text_id;
    private EditText text_pz;
    private EditText text_verify;
    private EditText text_newpwd;
    private Button btn;
    private ImageView wyu_verpci=null;
    private Bitmap bmVerifation=null;
    private String T_JSESSIONID;
    private LinearLayout m_layout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mcontext=getActivity();
        view=inflater.inflate(R.layout.fragment_getpwd,container,false);
        initFragment(view);
        return view;
    }

    public void initFragment(View view){
        m_layout= (LinearLayout) view.findViewById(R.id.main_layout);
        text_newpwd= (EditText) view.findViewById(R.id.retrieve_newpwd);
        text_verify= (EditText) view.findViewById(R.id.retrieve_verifycode);
        text_id= (EditText) view.findViewById(R.id.retrieve_id);
        text_pz= (EditText) view.findViewById(R.id.retrieve_voucher);
        wyu_verpci= (ImageView) view.findViewById(R.id.retrieve_verpic);
        btn= (Button) view.findViewById(R.id.retrieve_submit);
        layout_retrieve= (LinearLayout) view.findViewById(R.id.retrieve_newpwd_layout);
        layout_retrieve.setVisibility(View.INVISIBLE);
        wyu_verpci.setOnClickListener(new MyClickListener());
        btn.setOnClickListener(new MyClickListener());
        m_layout.setOnClickListener(new MyClickListener());
        myVerifyThread s=new myVerifyThread();
        s.start();
    }

    class MyClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.retrieve_submit:{
                    String id=text_id.getText().toString();
                    String pz=text_pz.getText().toString();
                    String verify=text_verify.getText().toString();
                    myGetPwdThread getPwdThread=new myGetPwdThread(id,pz,verify);
                    getPwdThread.start();
                    break;
                }
                case R.id.retrieve_verpic:{
                    myVerifyThread s=new myVerifyThread();
                    s.start();
                    break;
                }
                case R.id.main_layout:{
                    InputMethodManager imm = (InputMethodManager)mcontext.getSystemService(Context.INPUT_METHOD_SERVICE);
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
                MyHttpHelp httpHelp=new MyHttpHelp(UrlUtil.verifyUrl,"get");
                HttpResponse httpResponse = httpHelp.getRequire(null);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    getCookies(httpResponse);//保存JSESSIONID
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

        public void getCookies(HttpResponse httpResponse) {
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
                T_JSESSIONID=new String(value);
            }
        }

    }

    class myGetPwdThread extends Thread{
        private String id;
        private String pz;
        private String verify;
        public myGetPwdThread(String id,String pz,String verify){
            this.id=id;
            this.pz=pz;
            this.verify=verify;
        }
        @Override
        public void run() {
            try {
                MyHttpHelp httpHelp=new MyHttpHelp("http://202.192.240.29/login!fetchMm.action","post");
                httpHelp.setHeader("Cookie","JSESSIONID="+T_JSESSIONID);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("account", id));
                params.add(new BasicNameValuePair("mmtip", pz));
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
                message.what=1003;
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
                    String msg=jsonObject.getString("msg");
                    String status=jsonObject.getString("status");
                    //把数据发送出去
                    Message message=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putString("msg",msg);
                    bundle.putString("status",status);
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
            switch (msg.what) {
                case 1001: {//获取验证码
                    Bundle bundle = msg.getData();
                    byte bytes[] = bundle.getByteArray("verify");
                    bmVerifation = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    wyu_verpci.setImageBitmap(bmVerifation);
                    break;
                }
                case 1002:{
                    Bundle bundle = msg.getData();
                    String smsg = bundle.getString("msg");
                    String status=bundle.getString("status");
                    if (status.equals("y")&&!smsg.isEmpty()){
                        layout_retrieve.setVisibility(View.VISIBLE);
                        text_newpwd.setText(smsg);
                        MainActivity.setJessionId("123456789");
                    }else {
                        Toast.makeText(mcontext,smsg,Toast.LENGTH_SHORT).show();
                    }
                    wyu_verpci.callOnClick();
                    break;
                }
                case 1003:{
                    Toast.makeText(mcontext,"网络问题，请稍后再试！",Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    };

}
