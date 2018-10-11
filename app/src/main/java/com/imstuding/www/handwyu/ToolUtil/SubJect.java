package com.imstuding.www.handwyu.ToolUtil;

/**
 * Created by yangkui on 2018/1/26.
 */

public class SubJect {
    private String kcbh;//课程编号
    private String kcmc;//课程名称
    private String zcj;//总成绩
    private String cjjd;//绩点
    private String zxs;//学时
    private String xf;//学分
    private String xdfsmc;//修读方式名称
    private String cjfsmc;//成绩方式名称
    private String pscj;//平时成绩
    private boolean check;

    public SubJect(String kcbh, String kcmc, String zcj, String cjjd, String zxs, String xf, String xdfsmc, String cjfsmc, String pscj, boolean check) {
        this.kcbh = kcbh;
        this.kcmc = kcmc;
        this.zcj = zcj;
        this.cjjd = cjjd;
        this.zxs = zxs;
        this.xf = xf;
        this.xdfsmc = xdfsmc;
        this.cjfsmc = cjfsmc;
        this.pscj = pscj;
        this.check = check;
    }

    public void setKcbh(String kcbh) {
        this.kcbh = kcbh;
    }

    public void setZxs(String zxs) {
        this.zxs = zxs;
    }

    public void setCjfsmc(String cjfsmc) {
        this.cjfsmc = cjfsmc;
    }

    public void setPscj(String pscj) {
        this.pscj = pscj;
    }

    public String getKcbh() {
        return kcbh;
    }

    public String getZxs() {
        return zxs;
    }

    public String getCjfsmc() {
        return cjfsmc;
    }

    public String getPscj() {
        return pscj;
    }

    public String getCjjd() {
        return cjjd;
    }

    public void setCjjd(String cjjd) {
        this.cjjd = cjjd;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getKcmc() {
        return kcmc;
    }

    public void setKcmc(String kcmc) {
        this.kcmc = kcmc;
    }

    public String getXdfsmc() {
        return xdfsmc;
    }

    public String getZcj() {
        return zcj;
    }

    public void setZcj(String zcj) {
        this.zcj = zcj;
    }

    public String getXf() {

        return xf;
    }

    public void setXf(String xf) {
        this.xf = xf;
    }

    public void setXdfsmc(String xdfsmc) {
        this.xdfsmc = xdfsmc;
    }

}
