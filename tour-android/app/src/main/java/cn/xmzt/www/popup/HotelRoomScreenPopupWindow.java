package cn.xmzt.www.popup;

import android.app.Activity;
import android.util.AttributeSet;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BasePopupWindow;

/**
 * @author tanlei
 * @date 2019/7/30
 * @describe 房型更多筛选的的弹出框
 */

public class HotelRoomScreenPopupWindow extends BasePopupWindow {

    public HotelRoomScreenPopupWindow(Activity context) {
        this(context, null);
    }

    public HotelRoomScreenPopupWindow(Activity context, AttributeSet attrs) {
        super(context, attrs);
        setPopupWindowSize(0.83, 1);
    }

    @Override
    protected int setPopupWindowLayoutId() {
        return R.layout.popupwindow_hotel_room_screen;
    }

}
