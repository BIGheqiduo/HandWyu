package com.imstuding.www.handwyu.OtherUi;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextClock;
import android.widget.TimePicker;
import android.widget.Toast;

import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.UrlUtil;

import org.w3c.dom.Text;

/**
 * Created by yangkui on 2018/10/29.
 */

public class AutoNoticeFragment extends Fragment {

    private Context mcontext;
    private View view;
    private LinearLayout layout_auto_notice;
    private Switch auto_notice_switch;
    private boolean autoNotice;
    private LinearLayout layout_notice_set_time;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mcontext=getActivity();
        view=inflater.inflate(R.layout.fragment_auto_notice,container,false);
        initFragment(view);
        return view;
    }

    public void initFragment(View view){
        layout_auto_notice= (LinearLayout) view.findViewById(R.id.layout_auto_notice);
        auto_notice_switch= (Switch) view.findViewById(R.id.auto_notice_switch);

        autoNotice=mcontext.getSharedPreferences("userInfo", Context.MODE_PRIVATE).getBoolean("autoNotice",true);
        if (autoNotice){
            auto_notice_switch.setChecked(true);
        }else {
            auto_notice_switch.setChecked(false);
        }
        layout_auto_notice.setOnClickListener(new MyClickListener());

        layout_notice_set_time= (LinearLayout) view.findViewById(R.id.layout_notice_set_time);
        layout_notice_set_time.setOnClickListener(new MyClickListener());
    }

    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_auto_notice:{
                    if (auto_notice_switch.isChecked()){
                        mcontext.getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit().putBoolean("autoNotice",false).commit();
                        auto_notice_switch.setChecked(false);
                    }else {
                        mcontext.getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit().putBoolean("autoNotice",true).commit();
                        auto_notice_switch.setChecked(true);
                    }
                    break;
                }
                case R.id.layout_notice_set_time:{
                    SetAutoNoticeTimeDlg autoNoticeTimeDlg=new SetAutoNoticeTimeDlg(mcontext);
                    autoNoticeTimeDlg.show();
                    break;
                }
            }
        }
    }
}
