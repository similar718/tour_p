package cn.xmzt.www.home.bean;

import java.util.List;

/**
 * 城市
 */
public class CityLetterGroupBean {
    private String initial;
    private List<CityBean> list;

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public List<CityBean> getList() {
        return list;
    }

    public void setList(List<CityBean> list) {
        this.list = list;
    }
}
