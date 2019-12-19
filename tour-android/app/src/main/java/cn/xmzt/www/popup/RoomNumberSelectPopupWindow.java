package cn.xmzt.www.popup;

import android.app.Activity;
import android.util.AttributeSet;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BasePopupWindow;

/**
 * @author tanlei
 * @date 2019/7/31
 * @describe 房间数量选择的弹出框
 */

public class RoomNumberSelectPopupWindow extends BasePopupWindow {
    public RoomNumberSelectPopupWindow(Activity context) {
        this(context, null);
    }

    public RoomNumberSelectPopupWindow(Activity context, AttributeSet attrs) {
        super(context, attrs);
        setPopupWindowSize(0.52, 1);
    }

    @Override
    protected int setPopupWindowLayoutId() {
        return R.layout.popupwindow_number_room_select;
    }
}
