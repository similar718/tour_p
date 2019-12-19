package cn.xmzt.www.selfdrivingtools.bean;

public class MapListBean {
    ScenicSpotGuideBean beans ;
    int type = 0;// 0 景点 1 路线 2 其他

    public MapListBean(ScenicSpotGuideBean beans, int type) {
        this.beans = beans;
        this.type = type;
    }

    public ScenicSpotGuideBean getBeans() {
        return beans;
    }

    public void setBeans(ScenicSpotGuideBean beans) {
        this.beans = beans;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
