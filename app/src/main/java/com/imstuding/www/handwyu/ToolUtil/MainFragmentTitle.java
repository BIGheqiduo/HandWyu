package com.imstuding.www.handwyu.ToolUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.imstuding.www.handwyu.R;

/**
 * Created by yangkui on 2018/10/7.
 */

public class MainFragmentTitle extends FrameLayout {
    private TextView titleText;

    public MainFragmentTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title_main_fragment, this);
        titleText = (TextView) findViewById(R.id.fragment_title_text);
    }
    /**
     * 设置标题
     * @param text
     */
    public void setTitleText(String text) {
        titleText.setText(text);
    }

}
