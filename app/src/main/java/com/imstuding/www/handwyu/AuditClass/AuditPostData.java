package com.imstuding.www.handwyu.AuditClass;

/**
 * Created by yangkui on 2018/8/16.
 */

public class AuditPostData {
    private String xnxqdm=null;//学年学期
    private String kcdm=null;//课程代码
    private String kcmc=null;//课程名称
    private String kkyxdm=null;//开课单位代码
    private String teaxm=null;//教师名称
    private String jhlxdm=null;//计划类型代码
    private String rq=null;
    private String js=null;
    private String page=null;//页数

    public AuditPostData(String xnxqdm, String kcdm, String kkyxdm, String teaxm, String jhlxdm,String kcmc,String rq,String js,String page) {
        this.xnxqdm = xnxqdm;
        this.kcdm = kcdm;
        this.kkyxdm = kkyxdm;
        this.teaxm = teaxm;
        this.jhlxdm = jhlxdm;
        this.kcmc=kcmc;
        this.rq=rq;
        this.js=js;
        this.page=page;
    }

    public String getRq() {
        return rq;
    }

    public void setRq(String rq) {
        this.rq = rq;
    }

    public String getJs() {
        return js;
    }

    public void setJs(String js) {
        this.js = js;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getKcmc() {
        return kcmc;
    }

    public void setKcmc(String kcmc) {
        this.kcmc = kcmc;
    }

    public String getXnxqdm() {
        return xnxqdm;
    }

    public void setXnxqdm(String xnxqdm) {
        this.xnxqdm = xnxqdm;
    }

    public String getKcdm() {
        return kcdm;
    }

    public void setKcdm(String kcdm) {
        this.kcdm = kcdm;
    }

    public String getKkyxdm() {
        return kkyxdm;
    }

    public void setKkyxdm(String kkyxdm) {
        if (kkyxdm.equals("全部")){
            this.kkyxdm="";
        }else {
            try{
                this.kkyxdm = "000"+kkyxdm.substring(0,2);
            }catch (Exception e ){
                this.kkyxdm="";
            }
        }

    }

    public String getTeaxm() {
        return teaxm;
    }

    public void setTeaxm(String teaxm) {
        this.teaxm = teaxm;
    }

    public String getJhlxdm() {
        return jhlxdm;
    }

    public void setJhlxdm(String jhlxdm) {
        if (jhlxdm.equals("00")){
            this.jhlxdm="";
        }
        else {
            this.jhlxdm = jhlxdm;
        }

    }
}
