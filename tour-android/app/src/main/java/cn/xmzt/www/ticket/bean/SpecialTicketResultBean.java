package cn.xmzt.www.ticket.bean;

import java.util.ArrayList;
import java.util.List;

public class SpecialTicketResultBean {
    private int type;// 用来显示多布局列表显示 1 人气必玩 2 精选主题 3 热门景点 目前就三种
    private String title;
    private List<SpecialTicketBean.HotBean> listhot = new ArrayList<>();
    private List<SpecialTicketMustPlayBean.ItemsBean> listmust = new ArrayList<>();
    private List<SpecialTicketBean.SubjectListBean> listtitle = new ArrayList<>();

    public SpecialTicketResultBean(int type, String title, List<SpecialTicketBean.HotBean> listhot, List<SpecialTicketMustPlayBean.ItemsBean> listmust, List<SpecialTicketBean.SubjectListBean> listtitle) {
        this.type = type;
        this.title = title;
        this.listhot = listhot;
        this.listmust = listmust;
        this.listtitle = listtitle;
    }

    public SpecialTicketResultBean() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<SpecialTicketBean.HotBean> getListhot() {
        return listhot;
    }

    public void setListhot(List<SpecialTicketBean.HotBean> listhot) {
        this.listhot = listhot;
    }

    public List<SpecialTicketMustPlayBean.ItemsBean> getListmust() {
        return listmust;
    }

    public void setListmust(List<SpecialTicketMustPlayBean.ItemsBean> listmust) {
        this.listmust = listmust;
    }

    public List<SpecialTicketBean.SubjectListBean> getListtitle() {
        return listtitle;
    }

    public void setListtitle(List<SpecialTicketBean.SubjectListBean> listtitle) {
        this.listtitle = listtitle;
    }
}
