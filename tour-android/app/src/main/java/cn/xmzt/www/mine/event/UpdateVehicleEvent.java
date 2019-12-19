package cn.xmzt.www.mine.event;

import java.util.List;

import cn.xmzt.www.mine.bean.CommonVehicleBean;

public class UpdateVehicleEvent {
    private int type; // 1 添加成功刷新常用车辆列表界面  2 添加车辆到智能组队界面 3 添加车辆到线路订单填写界面 4 删除车辆信息 5 修改车辆信息
    private List<CommonVehicleBean> licencePlate; // 2和3 需要的车辆信息
    private String oldLicencePlate;
    private String newLicencePlate;

    public UpdateVehicleEvent(int type, String oldLicencePlate, String newLicencePlate) {
        this.type = type;
        this.oldLicencePlate = oldLicencePlate;
        this.newLicencePlate = newLicencePlate;
    }

    public UpdateVehicleEvent(int type) {
        this.type = type;
    }

    public UpdateVehicleEvent(int type, List<CommonVehicleBean> licencePlate) {
        this.type = type;
        this.licencePlate = licencePlate;
    }

    public List<CommonVehicleBean> getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(List<CommonVehicleBean> licencePlate) {
        this.licencePlate = licencePlate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOldLicencePlate() {
        return oldLicencePlate;
    }

    public void setOldLicencePlate(String oldLicencePlate) {
        this.oldLicencePlate = oldLicencePlate;
    }

    public String getNewLicencePlate() {
        return newLicencePlate;
    }

    public void setNewLicencePlate(String newLicencePlate) {
        this.newLicencePlate = newLicencePlate;
    }
}
