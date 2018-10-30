package com.imstuding.www.handwyu.WebViewDlg;

import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.imstuding.www.handwyu.R;
import com.imstuding.www.handwyu.SchoolCard.SchoolCardActivity;
import com.imstuding.www.handwyu.ToolUtil.TitleView;

import java.lang.reflect.Field;

public class SayLoveWallActivity extends AppCompatActivity implements ViewTreeObserver.OnScrollChangedListener {

    private WebView webView=null;
    public TitleView titleView;
    public ImageView imageView_menu;
    public ImageView imageView_back;
    private String url="http://www.imstuding.com/saylovewall/index.php";
    private ProgressBar pg1;
    private long firstTime=0;
    private SwipeRefreshLayout srl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_say_love_wall);
        initActivity();
    }

    private void myPopMenu(){
        imageView_back= (ImageView) findViewById(R.id.title_back);
        imageView_menu= (ImageView) findViewById(R.id.title_menu);
        titleView= (TitleView) findViewById(R.id.title_lovewebview);

        titleView.setTitleText("邑大表白");

        imageView_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(SayLoveWallActivity.this, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.web_saylove_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.web_menu_update:{
                                webView.reload();
                                break;
                            }
                            case R.id.web_menu_copy:{
                                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                // 将文本内容放到系统剪贴板里。
                                cm.setText(webView.getUrl());
                                Toast.makeText(getApplicationContext(), "复制成功，可以发给朋友们了。", Toast.LENGTH_LONG).show();
                                break;
                            }
                            case R.id.web_menu_qq:{
                                try{
                                    shareQQ();
                                }catch (Exception e){
                                    Toast.makeText(getApplicationContext(), "分享失败，请复制链接分享吧！", Toast.LENGTH_LONG).show();
                                }

                                break;
                            }
                            case R.id.web_menu_wechat:{
                                try{
                                    shareWechat();
                                }catch (Exception e){
                                    Toast.makeText(getApplicationContext(), "分享失败，请复制链接分享吧！", Toast.LENGTH_LONG).show();
                                }

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

    public void shareWechat(){
        Intent intent = new Intent(Intent.ACTION_SEND); // 地址
        ComponentName componentName = new ComponentName(
                "com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(componentName);
        intent.putExtra(Intent.EXTRA_TEXT,
                this.getString(R.string.share_from)+webView.getUrl());
        intent.setType("text/plain");
        this.startActivity(Intent.createChooser(intent, "分享"));
    }

    public void shareQQ(){
        Intent intent = new Intent(Intent.ACTION_SEND); // 地址
        ComponentName component = new ComponentName(
                "com.tencent.mobileqq",
                "com.tencent.mobileqq.activity.JumpActivity");
        intent.setComponent(component);
        intent.putExtra(Intent.EXTRA_TEXT,
                this.getString(R.string.share_from)+webView.getUrl());
        intent.setType("text/plain");
        this.startActivity(Intent.createChooser(intent, "分享"));
    }

    public void initActivity(){
        myPopMenu();
        pg1 = (ProgressBar)findViewById(R.id.love_progressBar);

        webView= (WebView) findViewById(R.id.love_webview);

        if (webView!=null){
                CookieSyncManager.createInstance(this);  //Create a singleton CookieSyncManager within a context
                CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
                cookieManager.removeAllCookie();// Removes all cookies.
                CookieSyncManager.getInstance().sync(); // forces sync manager to sync now
                webView.setWebChromeClient(null);
                webView.setWebViewClient(null);
                webView.getSettings().setJavaScriptEnabled(false);
                webView.clearCache(true);
        }
        //支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(false);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(false);
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(false);
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO 自动生成的方法存根
                if(newProgress==100){
                    pg1.setVisibility(View.GONE);//加载完网页进度条消失
                    srl.setRefreshing(false);
                }
                else{
                    pg1.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    pg1.setProgress(newProgress);//设置进度值
                }

            }
        });

        webView.getViewTreeObserver().addOnScrollChangedListener(this);

        srl = (SwipeRefreshLayout)findViewById(R.id.saylove_srl);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (webView.getScrollY()==0){
                    webView.reload();
                }
            }
        });


        webView.loadUrl(url);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (webView.canGoBack()){
                webView.goBack();
            }else {
                if (secondTime - firstTime >= 2000) {
                    Toast.makeText(SayLoveWallActivity.this, "请再按一次退出邑大表白哦", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;
                } else {
                    return super.onKeyDown(keyCode, event);
                }
            }
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            webView.clearCache(true);
            webView.clearFormData();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        super.onDestroy();
    }


    @Override
    public void onScrollChanged() {
        if (webView.getScrollY() == 0) {
            srl.setEnabled(true);
        } else {
            srl.setEnabled(false);
        }
    }

}
