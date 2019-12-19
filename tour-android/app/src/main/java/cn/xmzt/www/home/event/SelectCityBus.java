package cn.xmzt.www.home.event;

import cn.xmzt.www.home.bean.CityBean;

import java.util.List;

public class SelectCityBus {
    private int type;
    private CityBean city=null;
    private List<CityBean> list=null;//目的地

    public SelectCityBus(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public CityBean getCity() {
        return city;
    }

    public void setCity(CityBean city) {
        this.city = city;
    }

    public List<CityBean> getList() {
        return list;
    }

    public void setList(List<CityBean> list) {
        this.list = list;
    }
}