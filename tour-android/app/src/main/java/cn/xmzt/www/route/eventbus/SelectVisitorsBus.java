package cn.xmzt.www.route.eventbus;

import cn.xmzt.www.bean.OftenVisitorsBean;

import java.util.List;

/**
 * 选出的出游人eventbus
 */
public class SelectVisitorsBus {
    private int type;//1表示选择出游人 2表示选择预订人
    private List<OftenVisitorsBean> selectList;

    public SelectVisitorsBus() {
    }
    public SelectVisitorsBus(int type, List<OftenVisitorsBean> selectList) {
        this.type = type;
        this.selectList = selectList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<OftenVisitorsBean> getSelectList() {
        return selectList;
    }

    public void setSelectList(List<OftenVisitorsBean> selectList) {
        this.selectList = selectList;
    }
}