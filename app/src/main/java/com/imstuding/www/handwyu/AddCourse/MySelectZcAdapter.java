package com.imstuding.www.handwyu.AddCourse;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.imstuding.www.handwyu.R;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by yangkui on 2018/10/26.
 */

public class MySelectZcAdapter extends BaseAdapter {

    private Context mContext;

    private String[] contents;

    private int rowTotal;

    private int columnTotal;

    private int positionTotal;

    private String array[];

    public MySelectZcAdapter(Context context) {
        this.mContext = context;
        array=new String[20];
        for (int i=0;i<20;i++){
            array[i]="";
        }
    }

    public int getCount() {
        return positionTotal;
    }

    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {
        return contents[position];
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        if( convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.myzc_grib_item, null);
        }

        final TextView textView = (TextView)convertView.findViewById(R.id.text);
        textView.setText((String)getItem(position));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (array[position].equals("")){
                    array[position]=textView.getText().toString();
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_14));
                }else {
                    array[position]="";
                    textView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_11));
                }
            }
        });

        return convertView;
    }
    /**
     * 设置内容、行数、列数
     */
    public void setContent(String[] contents, int row, int column) {
        this.contents = contents;
        this.rowTotal = row;
        this.columnTotal = column;
        positionTotal = rowTotal * columnTotal;
    }

    public String [] getArray(){
        return array;
    }

}
