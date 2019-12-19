package cn.xmzt.www.intercom.bean;

import cn.xmzt.www.selfdrivingtools.bean.TourTripDetailNewBean;

/**
 * 线路行程安排
 */
public class SchedulingPlanNew {
    private TourTripDetailNewBean.LineRouteListVOBean plan;
    private int planType;
    private int icon;
    private String name;
    public SchedulingPlanNew(TourTripDetailNewBean.LineRouteListVOBean plan, int planType, int icon, String name) {
        this.plan = plan;
        this.planType = planType;
        this.icon = icon;
        this.name = name;
    }

    public TourTripDetailNewBean.LineRouteListVOBean getPlan() {
        return plan;
    }

    public void setPlan(TourTripDetailNewBean.LineRouteListVOBean plan) {
        this.plan = plan;
    }

    public int getPlanType() {
        return planType;
    }

    public void setPlanType(int planType) {
        this.planType = planType;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
