package com.imstuding.www.handwyu.ToolUtil;

/**
 * Created by yangkui on 2018/8/20.
 */

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by yangkui on 2018/8/20.
 */

import com.imstuding.www.handwyu.R;

public class TitleView extends FrameLayout {
    private TextView titleText;
    private ImageView back;
    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title_view, this);
        titleText = (TextView) findViewById(R.id.title_text);
        back = (ImageView) findViewById(R.id.title_back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) getContext()).finish();
            }
        });
    }
    /**
     * 设置标题
     * @param text
     */
    public void setTitleText(String text) {
        titleText.setText(text);
    }
    /**
     * 隐藏返回按钮
     */
    public void hideBackImage(){
        back.setVisibility(View.GONE);
    }

}
