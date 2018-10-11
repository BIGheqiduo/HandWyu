package com.imstuding.www.handwyu.ToolUtil;

/**
 * Created by yangkui on 2018/8/15.
 */

public class Classmate {
    private String stu_name;
    private String stu_num;
    private String stu_class;
    private String stu_college;
    private String stu_prof;

    public Classmate(String stu_name, String stu_num, String stu_class, String stu_college, String stu_prof) {
        this.stu_name = stu_name;
        this.stu_num = stu_num;
        this.stu_class = stu_class;
        this.stu_college = stu_college;
        this.stu_prof = stu_prof;
    }

    public String getStu_name() {
        return stu_name;
    }

    public void setStu_name(String stu_name) {
        this.stu_name = stu_name;
    }

    public String getStu_num() {
        return stu_num;
    }

    public void setStu_num(String stu_num) {
        this.stu_num = stu_num;
    }

    public String getStu_class() {
        return stu_class;
    }

    public void setStu_class(String stu_class) {
        this.stu_class = stu_class;
    }

    public String getStu_college() {
        return stu_college;
    }

    public void setStu_college(String stu_college) {
        this.stu_college = stu_college;
    }

    public String getStu_prof() {
        return stu_prof;
    }

    public void setStu_prof(String stu_prof) {
        this.stu_prof = stu_prof;
    }
}
