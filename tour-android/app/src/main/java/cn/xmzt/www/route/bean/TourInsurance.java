
package cn.xmzt.www.route.bean;

import cn.xmzt.www.utils.MathUtil;

/**
 * 线路订单保险
 */
public class TourInsurance {
    private int ageLimit;//年龄界限
    private double amountMultiple;//保险倍率
    private double insuranceAmount;//	线路保险金额
    private String insuranceNameOver;//线路保险名称(75)周岁以上
    private String insuranceNameUnder;//线路保险名称(75)周岁以下
    private boolean isBuy=false;

    public int getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(int ageLimit) {
        this.ageLimit = ageLimit;
    }

    public double getAmountMultiple() {
        return amountMultiple;
    }

    public void setAmountMultiple(double amountMultiple) {
        this.amountMultiple = amountMultiple;
    }

    public double getInsuranceAmount() {
        return insuranceAmount;
    }
    public String getInsuranceAmounts() {
        return "¥"+MathUtil.formatDouble(insuranceAmount,2)+"/份";
    }
    public void setInsuranceAmount(double insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    public String getInsuranceNameOver() {
        return insuranceNameOver;
    }

    public void setInsuranceNameOver(String insuranceNameOver) {
        this.insuranceNameOver = insuranceNameOver;
    }

    public String getInsuranceNameUnder() {
        return insuranceNameUnder;
    }

    public void setInsuranceNameUnder(String insuranceNameUnder) {
        this.insuranceNameUnder = insuranceNameUnder;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }
}