package cn.xmzt.www.pay.eventbus;

/**
 * 支付结果后通知相关页面eventbus
 */
public class PayResultBus {
    private boolean success;
    private int resultCode;

    public PayResultBus() {
    }

    public PayResultBus(boolean success, int resultCode) {
        this.success = success;
        this.resultCode = resultCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}