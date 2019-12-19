package cn.xmzt.www.popup;

import android.app.Activity;
import android.util.AttributeSet;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BasePopupWindow;

/**
 * @author tanlei
 * @date 2019/8/2
 * @describe 费用明细弹出框
 */

public class CostDetailsPopupWindow extends BasePopupWindow {

    public CostDetailsPopupWindow(Activity context) {
        this(context, null);
    }

    public CostDetailsPopupWindow(Activity context, AttributeSet attrs) {
        super(context, attrs);
        setPopupWindowSize(0.83, 1);
    }

    @Override
    protected int setPopupWindowLayoutId() {
        return R.layout.popupwindow_cost_details;
    }
}
