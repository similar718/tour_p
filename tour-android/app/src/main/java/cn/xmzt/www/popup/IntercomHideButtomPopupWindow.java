package cn.xmzt.www.popup;

import android.app.Activity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BasePopupWindow;
import cn.xmzt.www.nim.uikit.common.util.sys.ScreenUtil;

/**
 * 对讲按钮下面的隐藏按钮
 * @author Averysk
 */
public class IntercomHideButtomPopupWindow extends BasePopupWindow {

    private Button btnIntercomHide; // 记录时间

    @Override
    protected int setPopupWindowLayoutId() {
        return R.layout.popupwindow_intercom_hide_buttom;
    }

    public IntercomHideButtomPopupWindow(Activity context) {
        this(context, null);
    }

    public IntercomHideButtomPopupWindow(Activity context, AttributeSet attrs) {
        super(context, attrs);
        setPopupWindowSize(0, 0);
        initView();
        //弹出动画
        setAnimationStyle(R.style.popup_window_anim_style2);
    }

    // 初始化
    private void initView() {
        btnIntercomHide = view.findViewById(R.id.btn_intercom_hide);

    }

    public void setBtnIntercomHideListener(View.OnClickListener onClickCancelListener){
        btnIntercomHide.setOnClickListener(onClickCancelListener);
    }

    // 显示窗口
    public void showPop(View anchor){
        //this.showAsDropDown(anchor, 50, ScreenUtil.dip2px(50) , Gravity.CENTER);
        this.showAsDropDown(anchor, 50, ScreenUtil.dip2px(10));
    }

    // 隐藏窗口
    public void holePop(){
        this.dismiss();
    }

}
