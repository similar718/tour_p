package cn.xmzt.www.route.bean;

/**
 * 线路详情分类
 */
public class RouteDetailCategory {
    private String name;
    private int icon;

    public RouteDetailCategory(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
