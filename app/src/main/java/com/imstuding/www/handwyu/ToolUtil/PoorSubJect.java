package com.imstuding.www.handwyu.ToolUtil;

/**
 * Created by yangkui on 2018/10/30.
 */

public class PoorSubJect {
    private String kcdm;//课程编号
    private String kcmc;//课程名称
    private String zcj;//总成绩
    private String zxs;//学时
    private String xf;//学分
    private String xdfsmc;//修读方式名称

    public PoorSubJect(String kcdm, String kcmc, String zcj, String zxs, String xf, String xdfsmc) {
        this.kcdm = kcdm;
        this.kcmc = kcmc;
        this.zcj = zcj;
        this.zxs = zxs;
        this.xf = xf;
        this.xdfsmc = xdfsmc;
    }

    public String getKcdm() {
        return kcdm;
    }

    public void setKcdm(String kcdm) {
        this.kcdm = kcdm;
    }

    public String getKcmc() {
        return kcmc;
    }

    public void setKcmc(String kcmc) {
        this.kcmc = kcmc;
    }

    public String getZcj() {
        return zcj;
    }

    public void setZcj(String zcj) {
        this.zcj = zcj;
    }

    public String getZxs() {
        return zxs;
    }

    public void setZxs(String zxs) {
        this.zxs = zxs;
    }

    public String getXf() {
        return xf;
    }

    public void setXf(String xf) {
        this.xf = xf;
    }

    public String getXdfsmc() {
        return xdfsmc;
    }

    public void setXdfsmc(String xdfsmc) {
        this.xdfsmc = xdfsmc;
    }
}
