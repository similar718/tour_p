/**
  * Copyright 2019 bejson.com 
  */
package cn.xmzt.www.route.bean;

import android.text.TextUtils;

import cn.xmzt.www.utils.MathUtil;
import cn.xmzt.www.utils.TimeUtil;

import java.io.Serializable;

/**
 * 时间价格
 */
public class TimePriceDayBean {
    private String date;
    private String label;
    private double price;
    private boolean isToday=false;
    private boolean isBuy=false;//是否可购买

    private Ext ext;
    public void setDate(String date) {
         this.date = date;
     }
     public String getDate() {
        if(date==null){
            return "";
        }
         return date;
     }
    public String getDayStr() {
        if(!TextUtils.isEmpty(date)){
            return TimeUtil.stringDateToString(date,TimeUtil.FORMAT_A,"dd");
        }else {
            return "";
        }
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setPrice(double price) {
         this.price = price;
     }
     public double getPrice() {
         return price;
     }
     public String getPriceStr() {
        if(price>0){
            return "¥"+MathUtil.formatDouble(price,2);
        }else {
            return "";
        }
     }

    public boolean isToday() {
        return isToday;
    }
    public String getToday() {
        return "今天";
    }

    public void setToday(boolean today) {
        isToday = today;
    }

    public boolean isBuy() {
        if(isBuy&&ext!=null&&ext.getMaxPurchase()<=0){
            isBuy=false;
        }
        return isBuy;
    }
    public boolean isDateBuy(String assignDate) {
        if(date.equals(assignDate)&&isBuy){
            return true;
        }else {
            return false;
        }
    }
    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    public void setExt(Ext ext) {
         this.ext = ext;
     }
     public Ext getExt() {
         return ext;
     }

    public static class Ext implements Serializable {
        private String time;
        private double crPrice;//成人价
        private double jcrPrice;//成人结算价
        private double xhPrice;//小孩价
        private double jxhPrice;//小孩结算价
        private double fangPrice;//单房差
        private double carPrice;//拼车费

        private long inventory=0;//库存
        private long maxPurchase=0;//单次最大可购买量

        public String getTime() {
            if(!TextUtils.isEmpty(time)){
                return "出发日期："+time;
            }else {
                return "出发日期：";
            }
        }
        public void setTime(String time) {
            this.time = time;
        }

        public double getCrPrice() {
            return crPrice;
        }
        public String getCrPriceStr() {
            return "成人："+MathUtil.formatDouble(jcrPrice,2)+"/人";
        }

        public void setCrPrice(double crPrice) {
            this.crPrice = crPrice;
        }

        public double getJcrPrice() {
            return jcrPrice;
        }

        public void setJcrPrice(double jcrPrice) {
            this.jcrPrice = jcrPrice;
        }

        public double getXhPrice() {
            return xhPrice;
        }
        public String getXhPriceStr() {
            return "儿童："+MathUtil.formatDouble(jxhPrice,2)+"/人";
        }
        public void setXhPrice(double xhPrice) {
            this.xhPrice = xhPrice;
        }

        public double getJxhPrice() {
            return jxhPrice;
        }

        public void setJxhPrice(double jxhPrice) {
            this.jxhPrice = jxhPrice;
        }

        public double getFangPrice() {
            return fangPrice;
        }
        public String getFangPriceStr() {
            return "单房差："+MathUtil.formatDouble(fangPrice,2)+"/人";
        }

        public void setFangPrice(double fangPrice) {
            this.fangPrice = fangPrice;
        }

        public double getCarPrice() {
            return carPrice;
        }

        public void setCarPrice(double carPrice) {
            this.carPrice = carPrice;
        }

        public long getInventory() {
            return inventory;
        }
        public void setInventory(long inventory) {
            this.inventory = inventory;
        }

        public long getMaxPurchase() {
            if(maxPurchase==0){
                return inventory;
            }else if(inventory<maxPurchase){
                return inventory;
            }
            return maxPurchase;
        }
        public void setMaxPurchase(long maxPurchase) {
            this.maxPurchase = maxPurchase;
        }
    }
}