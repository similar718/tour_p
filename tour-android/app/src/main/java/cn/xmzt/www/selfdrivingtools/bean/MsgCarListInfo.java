package cn.xmzt.www.selfdrivingtools.bean;

public class MsgCarListInfo {
    /**
     * id : 1
     * serialNumber : 1
     * checkItem : 发动机润滑（机）油
     * checkStandard : 油量应在机油尺上限与下限刻度范围内
     * mainPoint : 1.未发动引擎前抽岀机油尺检査油量。不足者应添加，但不得超过上限刻度。
     2.检查机油油质是否正常，若有异状时，应予换新
     3.添注机油后应稍待约五分钟后再检视油尺方为正确(不可立即抽出机油尺检视，以免受假相所蒙而多加)。
     4.检査有否泄漏机油状况，有者应予修理。检査油量是否足够往返里程用量，不足时应予加足，油箱盖是否完备。
     5.检查有否泄、漏现象，有者应予立即修理
     */

    private int id;
    private int serialNumber;
    private String checkItem;
    private String checkStandard;
    private String mainPoint;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getCheckItem() {
        return checkItem;
    }

    public void setCheckItem(String checkItem) {
        this.checkItem = checkItem;
    }

    public String getCheckStandard() {
        return checkStandard;
    }

    public void setCheckStandard(String checkStandard) {
        this.checkStandard = checkStandard;
    }

    public String getMainPoint() {
        return mainPoint;
    }

    public void setMainPoint(String mainPoint) {
        this.mainPoint = mainPoint;
    }
}
