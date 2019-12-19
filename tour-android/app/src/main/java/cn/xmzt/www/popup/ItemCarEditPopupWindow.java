package cn.xmzt.www.popup;

import android.app.Activity;
import android.util.AttributeSet;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BasePopupWindow;

/**
 * @describe 列表中车辆编辑菜单
 */
public class ItemCarEditPopupWindow extends BasePopupWindow {

    public ItemCarEditPopupWindow(Activity context) {
        this(context, null);
    }

    public ItemCarEditPopupWindow(Activity context, AttributeSet attrs) {
        super(context, attrs);
        setPopupWindowSize(0, 0);
    }
    private View.OnClickListener mListener;
    public void setOnClickListener(View.OnClickListener mListener){
        this.mListener=mListener;
        view.findViewById(R.id.tv_change_driver).setOnClickListener(mListener);
        view.findViewById(R.id.tv_remove_car).setOnClickListener(mListener);
    }
    @Override
    protected int setPopupWindowLayoutId() {
        return R.layout.popupwindow_item_car_edit;
    }
}
