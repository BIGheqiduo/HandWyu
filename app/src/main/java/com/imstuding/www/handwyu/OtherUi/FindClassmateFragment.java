package com.imstuding.www.handwyu.OtherUi;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.imstuding.www.handwyu.LoadDlgUi.MyLoadDlg;
import com.imstuding.www.handwyu.OtherDlg.ClassmateDetailDlg;
import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.Classmate;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by yangkui on 2018/8/15.
 */

public class FindClassmateFragment extends Fragment {
    private Context mcontext;
    private View view;
    private AutoCompleteTextView classmate_input=null;
    private Button classmate_search=null;
    private RadioGroup radioGroup=null;
    private RadioButton classmate_name=null;
    private RadioButton classmate_id=null;
    private int class_flag=0;
    private ListView list_classmate_result;
    private SimpleAdapter simpleAdapter;
    private List<Classmate> classmateList;
    private MyLoadDlg myLoadDlg=null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mcontext=getActivity();
        view=inflater.inflate(R.layout.fragment_classmate,container,false);
        initFragment(view);
        return view;
    }

    private void initFragment(View view){
        myLoadDlg=new MyLoadDlg(mcontext);
        classmateList=new LinkedList<Classmate>();
        class_flag=1;
        classmate_input= (AutoCompleteTextView) view.findViewById(R.id.classmate_input);
        classmate_search= (Button) view.findViewById(R.id.classmate_search);
        radioGroup= (RadioGroup) view.findViewById(R.id.class_choose);
        classmate_name= (RadioButton) view.findViewById(R.id.classmate_name);
        classmate_id= (RadioButton) view.findViewById(R.id.classmate_id);

        list_classmate_result= (ListView) view.findViewById(R.id.list_classmate_result);

        list_classmate_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClassmateDetailDlg classmateDetailDlg =new ClassmateDetailDlg(mcontext,classmateList.get(position));
                classmateDetailDlg.show();
            }
        });
        classmate_id.setChecked(true);
        classmate_name.setOnClickListener(new MyClickListener());
        classmate_id.setOnClickListener(new MyClickListener());
        classmate_search.setOnClickListener(new MyClickListener());
    }

    class FindClassmateThread extends Thread{
        private String keyword;
        private String mode;
        public FindClassmateThread (String keyword,String mode){
            this.keyword=keyword;
            this.mode=mode;
      }
        @Override
        public void run() {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://www.imstuding.com:8080/LostCard/FindClassmate.do");
                httpPost.setHeader("Accept", "*/*");
                httpPost.setHeader("Connection", "keep-alive");
                httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("keyword", keyword));
                params.add(new BasicNameValuePair("mode", mode));
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
            int count=0;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String stu_num=jsonObject.getString("stu_num");
                    String stu_class=jsonObject.getString("stu_class");
                    String stu_name=jsonObject.getString("stu_name");
                    String stu_college=jsonObject.getString("stu_college");
                    String stu_prof=jsonObject.getString("stu_prof");
                    //把数据发送出去

                    Classmate classmate=new Classmate(stu_name,stu_num,stu_class,stu_college,stu_prof);
                    classmateList.add(classmate);
                    count++;
                }
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putInt("count",count);
                message.setData(bundle);
                message.what=1012;
                handle.sendMessage(message);//获取验证码

            } catch (Exception e) {
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putString("err","未知错误，去反馈吧！");
                message.setData(bundle);
                message.what=1013;
                handle.sendMessage(message);//获取验证码
                e.printStackTrace();
            }
        }
    }

    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1012:{
                    myLoadDlg.dismiss();
                    Bundle bundle= msg.getData();
                    int  count=bundle.getInt("count");

                    List<Map<String,String>> data=new ArrayList<Map<String,String>>();
                    for (int i=0;i<classmateList.size();i++){
                        Map<String,String> map=new HashMap<String, String>();
                        map.put("stu_num","学号："+classmateList.get(i).getStu_num());
                        map.put("stu_name",classmateList.get(i).getStu_name());
                        map.put("stu_class","班级："+classmateList.get(i).getStu_class());
                        data.add(map);
                    }
                    setOrUpdateSimpleAdapter(data);

                    if (count==0){
                        Toast.makeText(mcontext,"没有查到结果！请注意是否输入有误。",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(mcontext,"查到"+count+"个结果！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case 1013:{
                    myLoadDlg.dismiss();
                    Bundle bundle= msg.getData();
                    String  err=bundle.getString("err");
                    Toast.makeText(mcontext,err,Toast.LENGTH_SHORT).show();
                    break;
                }
            }

        }

    };

    public void setOrUpdateSimpleAdapter(List<Map<String,String>> data){
        simpleAdapter=new SimpleAdapter(mcontext,data,R.layout.list_classmate_item,
                new String[]{"stu_name","stu_num","stu_class"},new int[]{R.id.item_classmate_name,R.id.item_classmate_id,R.id.item_classmate_class});
        list_classmate_result.setAdapter(simpleAdapter);
    }

    class MyClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.classmate_search:{
                    if (!myLoadDlg.isShowing())
                        myLoadDlg.show();
                    classmateList.clear();//清空上一次的数据
                    String word=classmate_input.getText().toString();
                    FindClassmateThread classmateThread=new FindClassmateThread(word,class_flag+"");
                    classmateThread.start();
                    break;
                }
                case R.id.classmate_id:{
                    class_flag=1;
                    classmate_input.setHint("输入学号：如3115008888");
                    break;
                }
                case R.id.classmate_name:{
                    class_flag=2;
                    classmate_input.setHint("输入姓名：如张三");
                    break;
                }
            }
        }
    }

}
