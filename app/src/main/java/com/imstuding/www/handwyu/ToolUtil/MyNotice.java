package com.imstuding.www.handwyu.ToolUtil;

/**
 * Created by yangkui on 2018/9/6.
 */

public class MyNotice {

    private String id;
    private String title;
    private String content;
    private String time;
    private boolean flag;

    public MyNotice(String id, String title,String content,String time,boolean flag) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
        this.flag=flag;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
