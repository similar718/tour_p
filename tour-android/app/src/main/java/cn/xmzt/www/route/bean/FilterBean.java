package cn.xmzt.www.route.bean;

/**
 * 日期天数、出发地/目的地、线路主题 筛选
 */
public class FilterBean<T> {
    public static final int TYPE_CATEGORY=0;//分类
    public static final int TYPE_DATE=1;//日期
    public static final int TYPE_DAYS=2;//天数
    public static final int TYPE_DEPART=3;//出发地
    public static final int TYPE_ARRIVAL=4;//目的地
    public static final int TYPE_THEME=5;//主题
    private int type;
    private T data;

    public FilterBean() {
    }

    public FilterBean(int type, T data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}