package cn.xmzt.www.popup;

import android.app.Activity;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BasePopupWindow;
import cn.xmzt.www.nim.uikit.common.util.sys.ScreenUtil;

/**
 * 对讲按钮上面的群聊按钮
 * @author Averysk
 */
public class IntercomGroupButtomPopupWindow extends BasePopupWindow {

    private Button btnIntercomGroup; // 记录时间

    @Override
    protected int setPopupWindowLayoutId() {
        return R.layout.popupwindow_intercom_group_buttom;
    }

    public IntercomGroupButtomPopupWindow(Activity context) {
        this(context, null);
    }

    public IntercomGroupButtomPopupWindow(Activity context, AttributeSet attrs) {
        super(context, attrs);
        setPopupWindowSize(0, 0);
        initView();
    }

    // 初始化
    private void initView() {
        btnIntercomGroup = view.findViewById(R.id.btn_intercom_group);

    }

    public void setBtnIntercomGroupListener(View.OnClickListener onClickCancelListener){
        btnIntercomGroup.setOnClickListener(onClickCancelListener);
    }

    // 显示窗口
    public void showPop(View anchor){
        //this.showAtLocation(anchor, Gravity.TOP, 0, -ScreenUtil.dip2px(100+100));
        this.showAsDropDown(anchor, 50, -ScreenUtil.dip2px(60+100) , Gravity.CENTER);
    }

    // 隐藏窗口
    public void holePop(){
        this.dismiss();
    }

}
