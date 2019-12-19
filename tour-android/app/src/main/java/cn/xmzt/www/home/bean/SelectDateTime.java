/**
  * Copyright 2019 bejson.com 
  */
package cn.xmzt.www.home.bean;

/**
 * 选择日期时间
 */
public class SelectDateTime {
    private String date;
    private int monthDays;//这个月的天数
    private int dateDay;
    private String label;
    private boolean isToday=false;
    private boolean isMonth=false;//是否是月份 true表示是月份 false表示是日期
    private boolean isSelected=false;//是否选中
    private boolean isFirstSelected=false;//是否第一一个选中
    private boolean isLastSelected=false;//是否最后一个选中

    public SelectDateTime() {
    }

    public SelectDateTime(String date,boolean isMonth) {
        this.date = date;
        this.isMonth = isMonth;
    }
    public SelectDateTime(String date,int monthDays,int dateDay) {
        this.date = date;
        this.monthDays = monthDays;
        this.dateDay = dateDay;
    }

    public String getDate() {
        if(date==null){
            date="";
        }
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMonthDays() {
        return monthDays;
    }

    public void setMonthDays(int monthDays) {
        this.monthDays = monthDays;
    }

    public void setDateDay(int dateDay) {
        this.dateDay = dateDay;
    }

    public int getDateDay() {
        return dateDay;
    }
    public String getDateDays() {
        if(isToday){
            return "今天";
        }else if(dateDay>0){
            return dateDay+"";
        }
        return "";
    }

    public String getLabel() {
        if(label==null){
            label="";
        }
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }

    public boolean isMonth() {
        return isMonth;
    }

    public void setMonth(boolean month) {
        isMonth = month;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isFirstSelected() {
        return isFirstSelected;
    }

    public void setFirstSelected(boolean firstSelected) {
        isFirstSelected = firstSelected;
    }

    public boolean isLastSelected() {
        return isLastSelected;
    }

    public void setLastSelected(boolean lastSelected) {
        isLastSelected = lastSelected;
    }
}