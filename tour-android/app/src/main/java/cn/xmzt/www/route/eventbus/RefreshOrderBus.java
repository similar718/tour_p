package cn.xmzt.www.route.eventbus;

/**
 * 操作订单后通知相关页面刷新eventbus
 */
public class RefreshOrderBus {
    public static final int TYPE_REFRESH=1;//刷新界面
    private int type;
    private int actionType;//操作类型 1取消 2退款 3删除

    public RefreshOrderBus(int type) {
        this.type = type;
    }

    /**
     * 构造器
     * @param type
     * @param actionType 操作类型 1取消 2退款 3删除
     */
    public RefreshOrderBus(int type, int actionType) {
        this.type = type;
        this.actionType = actionType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return 操作类型 1取消 2退款 3删除
     */
    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }
}