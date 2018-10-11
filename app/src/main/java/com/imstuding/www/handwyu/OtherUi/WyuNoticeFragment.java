package com.imstuding.www.handwyu.OtherUi;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.imstuding.www.handwyu.MainUi.MainActivity;
import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.MyFillListNotice;
import com.imstuding.www.handwyu.ToolUtil.MyNotice;
import com.imstuding.www.handwyu.ToolUtil.MyNoticeAdapter;
import com.imstuding.www.handwyu.ToolUtil.UrlUtil;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yangkui on 2018/9/6.
 */


public class WyuNoticeFragment extends Fragment {

    private Context mcontext;
    private View view;
    private ListView listView;
    private MyNoticeAdapter myNoticeAdapter=null;
    private List<MyNotice> object;
    private MyFillListNotice fillListNotice;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mcontext=getActivity();
        view=inflater.inflate(R.layout.fragment_wyunotice,container,false);
        setHasOptionsMenu(true);
        myPopMenu();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initFragment(view);
    }

    private void myPopMenu(){
        OtherActivity otherActivity=(OtherActivity)mcontext;
        otherActivity.imageView_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popup = new PopupMenu(mcontext, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.wyunotice_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.wyu_menu_read:{
                                int count=Integer.parseInt(fillListNotice.getMaxId());
                                for (int i=1;i<=count;i++)
                                    fillListNotice.setRead(i);
                                // Toast.makeText(mcontext,"全部标记为已读",Toast.LENGTH_SHORT).show();
                                initFragment(view);
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

    public void initFragment(View view){
        initData();
        myNoticeAdapter=new MyNoticeAdapter(mcontext,R.layout.list_wyunotice_item,object);
        listView= (ListView) view.findViewById(R.id.list_wyu_notice);
        listView.setAdapter(myNoticeAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int t_id=0;
                try{
                    t_id=Integer.parseInt(object.get(position).getId());
                }catch (Exception e){
                    
                }
                fillListNotice.setRead(t_id);
                Bundle bundle=new Bundle();
                bundle.putString("title",object.get(position).getTitle());
                bundle.putString("time",object.get(position).getTime());
                bundle.putString("content",object.get(position).getContent());

                Intent intent=new Intent();
                intent.setClass(mcontext,OtherActivity.class);
                intent.putExtra("msg","readNotice");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
       // Toast.makeText(mcontext,"最大的id是"+fillListNotice.getMaxId()+",未读消息数为"+fillListNotice.getNoticeCount(),Toast.LENGTH_SHORT).show();
    }

    private void initData(){
        fillListNotice=new MyFillListNotice(mcontext);
        object=new LinkedList<MyNotice>();
        fillListNotice.fillList(object);
        myOrder(object);
    }

    public void myOrder(List<MyNotice> object){
        int size=object.size();
        for (int i=0;i<size;i++){
            for (int j=i;j<size;j++){
                if (Integer.parseInt(object.get(i).getId()) <Integer.parseInt(object.get(j).getId())){
                    MyNotice tmp=object.get(j);
                    object.set(j,object.get(i));
                    object.set(i,tmp);
                }
            }
        }
    }

}