/**
  * Copyright 2019 bejson.com 
  */
package cn.xmzt.www.route.bean;

import android.text.TextUtils;

import cn.xmzt.www.utils.MathUtil;
import cn.xmzt.www.utils.TimeUtil;

import java.util.List;

/**
 * 时间价格
 */
public class TimePriceMonthBean {
    private String month;//月份
    private double floorPrice;//当月最低价
    List<TimePriceDayBean> list;

    public String getMonth() {
        return month;
    }
    public String getMonthOfYear() {
        if(!TextUtils.isEmpty(month)){
            return TimeUtil.stringDateToString(month,"yyyy-MM","MM月");
        }else {
            return "";
        }
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getFloorPrice() {
        return floorPrice;
    }
    public String getFloorPriceStr() {
        if(floorPrice>0){
            return "¥"+MathUtil.formatDouble(floorPrice,2)+"起";
        }else {
            return "暂无";
        }
    }

    public void setFloorPrice(double floorPrice) {
        this.floorPrice = floorPrice;
    }

    /**
     * 获取最近能购买的那天
     * @return
     */
    public String getLatelyDayBuy() {
        String latelyDayBuy="";//最近能购买的那天
        if(list!=null){
            for (int i=0;i<list.size();i++){
                TimePriceDayBean mTimePriceDayBean=list.get(i);
                if(mTimePriceDayBean.isBuy()){
                    latelyDayBuy=mTimePriceDayBean.getDate();
                    break;
                }
            }
        }
        return latelyDayBuy;
    }
    public boolean isDateBuy(String assignDate) {
        boolean isDateBuy=false;
        if(list!=null){
            for (TimePriceDayBean mTimePriceDayBean:list){
                if(mTimePriceDayBean.isDateBuy(assignDate)){
                    isDateBuy=true;
                    break;
                }
            }
        }
        return isDateBuy;
    }

    public List<TimePriceDayBean> getList() {
        return list;
    }

    public void setList(List<TimePriceDayBean> list) {
        this.list = list;
    }
}