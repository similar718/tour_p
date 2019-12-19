package cn.xmzt.www.selfdrivingtools.bean;

/**
 * 线路详情行程安排
 */
public class SRouteDetailPlan {
    private TourTripDetailNewBean.LineRouteListVOBean.DetailVOListBean plan;
    private TourTripDetailNewBean.LineRouteListVOBean.DetailVOListBean.FirstBean first;
    private TourTripDetailNewBean.LineRouteListVOBean.DetailVOListBean.EatBean eat;
    private int planType;
    private boolean start;
    public SRouteDetailPlan(TourTripDetailNewBean.LineRouteListVOBean.DetailVOListBean plan,TourTripDetailNewBean.LineRouteListVOBean.DetailVOListBean.FirstBean first,TourTripDetailNewBean.LineRouteListVOBean.DetailVOListBean.EatBean eat, int planType,boolean start) {
        this.plan = plan;
        this.planType = planType;
        this.first = first;
        this.start = start;
        this.eat = eat;
    }

    public TourTripDetailNewBean.LineRouteListVOBean.DetailVOListBean.EatBean getEat() {
        return eat;
    }

    public void setEat(TourTripDetailNewBean.LineRouteListVOBean.DetailVOListBean.EatBean eat) {
        this.eat = eat;
    }

    public TourTripDetailNewBean.LineRouteListVOBean.DetailVOListBean.FirstBean getFirst() {
        return first;
    }

    public void setFirst(TourTripDetailNewBean.LineRouteListVOBean.DetailVOListBean.FirstBean first) {
        this.first = first;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public TourTripDetailNewBean.LineRouteListVOBean.DetailVOListBean getPlan() {
        return plan;
    }

    public void setPlan(TourTripDetailNewBean.LineRouteListVOBean.DetailVOListBean plan) {
        this.plan = plan;
    }

    public int getPlanType() {
        return planType;
    }

    public void setPlanType(int planType) {
        this.planType = planType;
    }
}
