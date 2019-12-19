package cn.xmzt.www.home.event;

public class SelectDateIntervalBus {
    private int type;//1表示添加定制 2表示修改定制
    private String startDate="";
    private String endDate="";

    public SelectDateIntervalBus(int type, String startDate, String endDate) {
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}