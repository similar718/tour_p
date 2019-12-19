/**
  * Copyright 2019 bejson.com 
  */
package cn.xmzt.www.route.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 线路选择出发人数的数量
 */
public class PersonCountBean implements Serializable {
    public final String unit="份";
    private String time;//时间
    private int crCount=1;//选择成人数量（默认是1）
    private int xhCount;//选择小孩数量
    private int fangCount;//选择单房差数量
    private int bigAgeCount;//大龄人数
    private TimePriceDayBean.Ext ext;//价格

    public PersonCountBean() {
    }
    public PersonCountBean(TimePriceDayBean.Ext ext) {
        this.ext = ext;
    }

    public String getTime() {
        if(time==null){
            return "";
        }
        return time;
    }
    public String getCFTime() {
        if(!TextUtils.isEmpty(time)){
            return "出发日期："+time;
        }else {
            return "出发日期：暂无可选日期";
        }
    }
    public void setTime(String time) {
        this.time = time;
    }

    public int getCrCount() {
        return crCount;
    }
    public String getCrCountStr() {
        if(crCount==0){
            crCount=1;
        }
        return crCount+"";
    }

    public void setCrCount(int crCount) {
        this.crCount = crCount;
    }

    public int getXhCount() {
        return xhCount;
    }
    public String getXhCountStr() {
        return xhCount+"";
    }
    public void setXhCount(int xhCount) {
        this.xhCount = xhCount;
    }

    public int getFangCount() {
        return fangCount;
    }
    public String getFangCountStr() {
        return fangCount+"";
    }
    public void setFangCount(int fangCount) {
        this.fangCount = fangCount;
    }

    public int getBigAgeCount() {
        return bigAgeCount;
    }

    public void setBigAgeCount(int bigAgeCount) {
        this.bigAgeCount = bigAgeCount;
    }

    public TimePriceDayBean.Ext getExt() {
        if(ext==null){
            ext=new TimePriceDayBean.Ext();
        }
        return ext;
    }

    public void setExt(TimePriceDayBean.Ext ext) {
        this.ext = ext;
    }

    /**
     * 单次最大可购买量
     * @return
     */
    public long getMaxPurchase() {
        if(ext!=null){
            return ext.getMaxPurchase();
        }
        return 0;
    }
}