package cn.xmzt.www.route.bean;

/**
 * 搜索关键字
 */
public class SearchKeywordBean{
    private int id;
    private String keyword;//关键字
    private int sort;//排序
    private int clickCount;//点击次数
    private int type;//类型 (1酒店 2线路 3门票)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }
}
