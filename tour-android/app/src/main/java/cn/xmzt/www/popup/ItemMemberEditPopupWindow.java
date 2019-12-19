package cn.xmzt.www.popup;

import android.app.Activity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BasePopupWindow;

/**
 * @describe 列表中成员编辑菜单
 */
public class ItemMemberEditPopupWindow extends BasePopupWindow {

    public ItemMemberEditPopupWindow(Activity context) {
        this(context, null);
    }

    public ItemMemberEditPopupWindow(Activity context, AttributeSet attrs) {
        super(context, attrs);
        setPopupWindowSize(0, 0);
        tv_other=view.findViewById(R.id.tv_other);
        tv_remove=view.findViewById(R.id.tv_remove);
    }
    private View.OnClickListener mListener;
    private TextView tv_other;
    private TextView tv_remove;
    public void setOnClickListener(View.OnClickListener mListener){
        this.mListener=mListener;
        tv_other.setOnClickListener(mListener);
        tv_remove.setOnClickListener(mListener);
    }
    public void setViewData(String other){
        tv_other.setText(other);
    }


    @Override
    protected int setPopupWindowLayoutId() {
        return R.layout.popupwindow_item_member_edit;
    }
}
