package cn.xmzt.www.route.eventbus;

import cn.xmzt.www.mine.bean.MyCouponBean;

/**
 * 优惠券eventbus
 */
public class MyCouponBus {
    private int size;
    private MyCouponBean myCouponBean;

    public MyCouponBus() {
    }
    public MyCouponBus(int size, MyCouponBean myCouponBean) {
        this.size = size;
        this.myCouponBean = myCouponBean;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public MyCouponBean getMyCouponBean() {
        return myCouponBean;
    }

    public void setMyCouponBean(MyCouponBean myCouponBean) {
        this.myCouponBean = myCouponBean;
    }
}