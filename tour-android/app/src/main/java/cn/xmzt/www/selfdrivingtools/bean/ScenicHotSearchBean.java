package cn.xmzt.www.selfdrivingtools.bean;

public class ScenicHotSearchBean {
    /**
     * id : 1
     * keyword : 故宫
     * sort : 100
     * clickCount : 0
     */
    private int id;
    private String keyword;
    private int sort;
    private int clickCount;

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
