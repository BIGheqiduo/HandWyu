package com.imstuding.www.handwyu.ToolUtil;

/**
 * Created by yangkui on 2018/10/4.
 */

public class ExamInf {
    private String kcmc;//课程名称
    private String jkteaxms;//监考老师
    private String ksaplxmc;//安排类型
    private String kscdmc;//考试地点
    private String zc;//周次
    private String xq;//星期
    private String ksrq;//考试日期
    private String kssj;//考试时间

    public ExamInf(String kcmc, String jkteaxms, String ksaplxmc, String kscdmc, String zc, String xq, String ksrq, String kssj) {
        this.kcmc = kcmc;
        this.jkteaxms = jkteaxms;
        this.ksaplxmc = ksaplxmc;
        this.kscdmc = kscdmc;
        this.zc = zc;
        this.xq = xq;
        this.ksrq = ksrq;
        this.kssj = kssj;
    }

    public String getKcmc() {
        return kcmc;
    }

    public String getJkteaxms() {
        return jkteaxms;
    }

    public String getKsaplxmc() {
        return ksaplxmc;
    }

    public String getKscdmc() {
        return kscdmc;
    }

    public String getZc() {
        return zc;
    }

    public String getXq() {
        return xq;
    }

    public String getKsrq() {
        return ksrq;
    }

    public String getKssj() {
        return kssj;
    }

    public void setKcmc(String kcmc) {
        this.kcmc = kcmc;
    }

    public void setJkteaxms(String jkteaxms) {
        this.jkteaxms = jkteaxms;
    }

    public void setKsaplxmc(String ksaplxmc) {
        this.ksaplxmc = ksaplxmc;
    }

    public void setKscdmc(String kscdmc) {
        this.kscdmc = kscdmc;
    }

    public void setZc(String zc) {
        this.zc = zc;
    }

    public void setXq(String xq) {
        this.xq = xq;
    }

    public void setKsrq(String ksrq) {
        this.ksrq = ksrq;
    }

    public void setKssj(String kssj) {
        this.kssj = kssj;
    }
}
