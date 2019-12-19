package cn.xmzt.www.selfdrivingtools.bean;

import java.util.ArrayList;
import java.util.List;

public class SharedNavigationMapLinesBean {
    int position ;
    List<SharedNavigationMapLineBean> bean = new ArrayList<>();

    public SharedNavigationMapLinesBean(int position, List<SharedNavigationMapLineBean> bean) {
        this.position = position;
        this.bean = bean;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<SharedNavigationMapLineBean> getBean() {
        return bean;
    }

    public void setBean(List<SharedNavigationMapLineBean> bean) {
        this.bean = bean;
    }
}
