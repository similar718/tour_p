package cn.xmzt.www.popup;

import android.app.Activity;
import android.util.AttributeSet;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BasePopupWindow;

/**
 * @author tanlei
 * @date 2019/8/1
 * @describe 重新发送电子发票的弹出框
 */

public class AgainSendBillPopupWindow extends BasePopupWindow {
    public AgainSendBillPopupWindow(Activity context) {
        this(context, null);
    }

    public AgainSendBillPopupWindow(Activity context, AttributeSet attrs) {
        super(context, attrs);
        setPopupWindowSize(0.33, 1);
    }

    @Override
    protected int setPopupWindowLayoutId() {
        return R.layout.popupwindow_again_send_bill;
    }
}
