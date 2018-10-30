package com.imstuding.www.handwyu.OtherUi;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.imstuding.www.handwyu.AddCourse.AddCourseFragment;
import com.imstuding.www.handwyu.AddCourse.SubCourseFragment;
import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.ToolUtil.TitleView;
import com.imstuding.www.handwyu.VolunteerDlg.VolunteerFragment;
import com.imstuding.www.handwyu.VolunteerDlg.VolunteerLoginFragment;
import com.imstuding.www.handwyu.WebViewDlg.WebViewFragment;

import java.lang.reflect.Field;

public class OtherActivity extends AppCompatActivity {

    private ExamFragment examFragment=null;
    private SecondClassScoreFragment secondClassScoreFragment=null;
    private WebViewFragment webViewFragment =null;
    private MoreFragment moreFragment=null;
    private HelpFragment helpFragment=null;
    private AboutMeFragment aboutMeFragment=null;
    private BugFragment bugFragment=null;
    private UpdateExplainFragment updateExplainFragment =null;
    private VolunteerFragment volunteerFragment=null;
    private VolunteerLoginFragment volunteerLoginFragment=null;
    private AddCourseFragment addCourseFragment=null;
    private IntranetFragment intranetFragment=null;
    private SubCourseFragment subCourseFragment=null;
    private FindClassmateFragment classmateFragment=null;
    private WyuNoticeFragment wyuNoticeFragment=null;
    private ReadNoticeFragment readNoticeFragment=null;
    private RetrievePwdFragment retrievePwdFragment=null;
    private AutoNoticeFragment autoNoticeFragment=null;
    private String msg="exam";
    public TitleView titleView;
    public ImageView imageView_menu;
    public ImageView imageView_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_other);
        try {
            Intent intent=getIntent();
            msg=intent.getStringExtra("msg");
        }catch (Exception e){

        }
        initActivity();
        selectFragment(msg);
    }

    private void initActivity(){
        myPopMenu();
    }

    private void myPopMenu(){
        imageView_back= (ImageView) findViewById(R.id.title_back);
        imageView_menu= (ImageView) findViewById(R.id.title_menu);
        titleView= (TitleView) findViewById(R.id.title_other);
        imageView_menu.setVisibility(View.INVISIBLE);
    }

    public void selectFragment(String string){
        switch (string){
            case "exam":{
                titleView.setTitleText("考试安排");
                imageView_menu.setVisibility(View.INVISIBLE);
                examFragment=new ExamFragment();
                setFragment(examFragment);
                break;
            }
            case "second":{
                titleView.setTitleText("第二课堂学分");
                imageView_menu.setVisibility(View.INVISIBLE);
                secondClassScoreFragment=new SecondClassScoreFragment();
                setFragment(secondClassScoreFragment);
                break;
            }
            case "webView":{
                titleView.setTitleText("掌上邑大");
                imageView_menu.setVisibility(View.VISIBLE);
                Bundle bundle=new Bundle();
                String url=getIntent().getStringExtra("url");
                bundle.putString("url",url);

                webViewFragment =new WebViewFragment();
                webViewFragment.setArguments(bundle);
                setFragment(webViewFragment);
                break;
            }
            case "more":{
                titleView.setTitleText("更多");
                imageView_menu.setVisibility(View.INVISIBLE);
                moreFragment=new MoreFragment();
                setFragment(moreFragment);
                break;
            }
            case "help":{
                titleView.setTitleText("帮助");
                imageView_menu.setVisibility(View.INVISIBLE);
                helpFragment=new HelpFragment();
                setFragment(helpFragment);
                break;
            }
            case "about":{
                titleView.setTitleText("关于");
                imageView_menu.setVisibility(View.INVISIBLE);
                aboutMeFragment=new AboutMeFragment();
                setFragment(aboutMeFragment);
                break;
            }
            case "bug":{
                titleView.setTitleText("反馈bug");
                imageView_menu.setVisibility(View.INVISIBLE);
                bugFragment=new BugFragment();
                setFragment(bugFragment);
                break;
            }
            case "update_explain":{
                titleView.setTitleText("更新历史");
                imageView_menu.setVisibility(View.INVISIBLE);
                updateExplainFragment =new UpdateExplainFragment();
                setFragment(updateExplainFragment);
                break;
            }
            case "volunteer":{
                titleView.setTitleText("江门义工");
                imageView_menu.setVisibility(View.INVISIBLE);
                volunteerFragment=new VolunteerFragment();
                setFragment(volunteerFragment);
                break;
            }
            case "volunteer_login":{
                titleView.setTitleText("义工登录");
                imageView_menu.setVisibility(View.INVISIBLE);
                volunteerLoginFragment=new VolunteerLoginFragment();
                setFragment(volunteerLoginFragment);
                break;
            }
            case "add_course":{
                titleView.setTitleText("添加课程");
                imageView_menu.setVisibility(View.INVISIBLE);
                addCourseFragment=new AddCourseFragment();
                try {
                    Bundle bundle=new Bundle();
                    int xq=getIntent().getIntExtra("xq",1);
                    int js=getIntent().getIntExtra("js",1);
                    bundle.putInt("xq",xq);
                    bundle.putInt("js",js);
                    addCourseFragment.setArguments(bundle);
                }catch (Exception e){

                }
                setFragment(addCourseFragment);
                break;
            }
            case "intranet":{
                titleView.setTitleText("内网功能");
                imageView_menu.setVisibility(View.INVISIBLE);
                intranetFragment=new IntranetFragment();
                setFragment(intranetFragment);
                break;
            }
            case "subCourse":{
                titleView.setTitleText("删除课程");
                imageView_menu.setVisibility(View.INVISIBLE);
                subCourseFragment=new SubCourseFragment();
                setFragment(subCourseFragment);
                break;
            }
            case "classmate":{
                titleView.setTitleText("查找同学");
                imageView_menu.setVisibility(View.INVISIBLE);
                classmateFragment=new FindClassmateFragment();
                setFragment(classmateFragment);
                break;
            }
            case "wyuNotice":{
                titleView.setTitleText("掌邑通知");
                imageView_menu.setVisibility(View.VISIBLE);
                wyuNoticeFragment=new WyuNoticeFragment();
                setFragment(wyuNoticeFragment);
                break;
            }
            case "readNotice":{
                Bundle bundle=null;
                try {
                    Intent intent=getIntent();
                    bundle = intent.getExtras();
                }catch (Exception e){

                }
                titleView.setTitleText("掌邑通知");
                imageView_menu.setVisibility(View.INVISIBLE);

                readNoticeFragment=new ReadNoticeFragment();
                readNoticeFragment.setArguments(bundle);
                setFragment(readNoticeFragment);
                break;
            }
            case "RetrievePwd":{
                titleView.setTitleText("找回密码");
                imageView_menu.setVisibility(View.INVISIBLE);
                retrievePwdFragment=new RetrievePwdFragment();
                setFragment(retrievePwdFragment);
                break;
            }
            case "AutoNotice":{
                titleView.setTitleText("上课提醒");
                imageView_menu.setVisibility(View.INVISIBLE);
                autoNoticeFragment=new AutoNoticeFragment();
                setFragment(autoNoticeFragment);
                break;
            }
        }
    }

    public void setFragment(Fragment fragment){
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.other_framelayout,fragment);
        transaction.commit();
    }

}
