package cn.xmzt.www.popup;

import android.app.Activity;
import android.util.AttributeSet;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BasePopupWindow;

/**
 * @describe 标题栏成员编辑菜单
 */
public class MemberEditPopupWindow extends BasePopupWindow {

    public MemberEditPopupWindow(Activity context) {
        this(context, null);
    }

    public MemberEditPopupWindow(Activity context, AttributeSet attrs) {
        super(context, attrs);
        setPopupWindowSize(0, 0);
    }
    private View.OnClickListener mListener;
    public void setOnClickListener(View.OnClickListener mListener){
        this.mListener=mListener;
        view.findViewById(R.id.tv_add).setOnClickListener(mListener);
        view.findViewById(R.id.tv_remove).setOnClickListener(mListener);
    }
    @Override
    protected int setPopupWindowLayoutId() {
        return R.layout.popupwindow_member_edit;
    }
}
