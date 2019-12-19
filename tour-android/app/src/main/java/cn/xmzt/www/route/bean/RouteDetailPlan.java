package cn.xmzt.www.route.bean;

/**
 * 线路详情行程安排
 */
public class RouteDetailPlan {
    private RouteDetailPage.DayLineTripInfo plan;
    private int planType;
    private int icon;
    private String name;
    public RouteDetailPlan(RouteDetailPage.DayLineTripInfo plan, int planType, int icon, String name) {
        this.plan = plan;
        this.planType = planType;
        this.icon = icon;
        this.name = name;
    }

    public RouteDetailPage.DayLineTripInfo getPlan() {
        return plan;
    }

    public void setPlan(RouteDetailPage.DayLineTripInfo plan) {
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
