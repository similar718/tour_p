package cn.xmzt.www.route.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 路线筛选条件列表
 */
public class RouteFiltrateList {
    private List<String> arrivalOriginalCityArray;//目的地
    private List<String> dayArray;//天数
    private List<String> departOriginalCityArray;//出发地
    private List<String> monthArray;//出发月份
    private List<FilterThemeBean> themeArray;//主题

    public List<String> getArrivalOriginalCityArray() {
        if(arrivalOriginalCityArray==null){
            arrivalOriginalCityArray=new ArrayList<String>();
        }
        if(!arrivalOriginalCityArray.contains("全国")){
            arrivalOriginalCityArray.add(0,"全国");
        }
        return arrivalOriginalCityArray;
    }

    public void setArrivalOriginalCityArray(List<String> arrivalOriginalCityArray) {
        this.arrivalOriginalCityArray = arrivalOriginalCityArray;
    }

    public List<String> getDayArray() {
        if(dayArray==null){
            dayArray=new ArrayList<String>();
        }
        if(!dayArray.contains("不限")){
            dayArray.add(0,"不限");
        }
        return dayArray;
    }

    public void setDayArray(List<String> dayArray) {
        this.dayArray = dayArray;
    }

    public List<String> getDepartOriginalCityArray() {
        if(departOriginalCityArray==null){
            departOriginalCityArray=new ArrayList<String>();
        }
        if(!departOriginalCityArray.contains("全国")){
            departOriginalCityArray.add(0,"全国");
        }
        return departOriginalCityArray;
    }

    public void setDepartOriginalCityArray(List<String> departOriginalCityArray) {
        this.departOriginalCityArray = departOriginalCityArray;
    }

    public List<String> getMonthArray() {
        if(monthArray==null){
            monthArray=new ArrayList<String>();
        }
        if(!monthArray.contains("不限")){
            monthArray.add(0,"不限");
        }
        return monthArray;
    }

    public void setMonthArray(List<String> monthArray) {
        this.monthArray = monthArray;
    }

    public List<FilterThemeBean> getThemeArray() {
        if(themeArray==null){
            themeArray=new ArrayList<FilterThemeBean>();
        }
        return themeArray;
    }

    public void setThemeArray(List<FilterThemeBean> themeArray) {
        this.themeArray = themeArray;
    }

}
