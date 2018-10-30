package com.imstuding.www.handwyu.ToolUtil;

/**
 * Created by yangkui on 2018/3/30.
 */

public class Course {
    private String kcmc;//课程名称
    private String jxcdmc;//教室
    private String teaxms;//教师
    private String zc;//周次
    private String js;//星期几第几节
    private String jxbmc;//教学班级
    private String sknrjj;//上课内容
    private String jcdm;
    private String xq;

    public Course(String kcmc, String jxcdmc, String teaxms, String zc, String js,String jxbmc,String sknrjj,String jcdm,String xq) {
        this.kcmc = kcmc;
        this.jxcdmc = jxcdmc;
        this.teaxms = teaxms;
        this.zc = zc;
        this.js = js;
        this.jxbmc=jxbmc;
        this.sknrjj=sknrjj;
        this.jcdm=jcdm;
        this.xq=xq;
    }

    public String getXq() {
        return xq;
    }

    public void setXq(String xq) {
        this.xq = xq;
    }

    public String getJcdm() {
        return jcdm;
    }

    public void setJcdm(String jcdm) {
        this.jcdm = jcdm;
    }

    public String getJxbmc() {
        return jxbmc;
    }

    public void setJxbmc(String jxbmc) {
        this.jxbmc = jxbmc;
    }

    public String getSknrjj() {
        return sknrjj;
    }

    public void setSknrjj(String sknrjj) {
        this.sknrjj = sknrjj;
    }

    public String getKcmc() {
        return kcmc;
    }

    public String getJxcdmc() {
        return jxcdmc;
    }

    public String getTeaxms() {
        return teaxms;
    }

    public String getZc() {
        return zc;
    }

    public String getJs() {
        return js;
    }

    public void setKcmc(String kcmc) {
        this.kcmc = kcmc;
    }

    public void setJxcdmc(String jxcdmc) {
        this.jxcdmc = jxcdmc;
    }

    public void setTeaxms(String teaxms) {
        this.teaxms = teaxms;
    }

    public void setZc(String zc) {
        this.zc = zc;
    }

    public void setJs(String js) {
        this.js = js;
    }
}
