package cn.xmzt.www.popup;

import android.app.Activity;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BasePopupWindow;

/**
 * @author tanlei
 * @date 2019/8/8
 * @describe 修改性别的弹出框
 */

public class ChangeGenderPopupWindow extends BasePopupWindow implements View.OnClickListener {
    private Activity activity;
    private OnItemClickListener onItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ChangeGenderPopupWindow(Activity context) {
        this(context, null);
        activity = context;
    }

    public ChangeGenderPopupWindow(Activity context, AttributeSet attrs) {
        super(context, attrs);
        setPopupWindowSize(0, 0.94);
        TextView tv_gender_male = view.findViewById(R.id.tv_gender_male);
        TextView tv_gender_female = view.findViewById(R.id.tv_gender_female);
        TextView tv_gender_secrecy = view.findViewById(R.id.tv_gender_secrecy);
        TextView tv_gender_cancel = view.findViewById(R.id.tv_gender_cancel);
        tv_gender_cancel.setOnClickListener(this);
        tv_gender_male.setOnClickListener(this);
        tv_gender_female.setOnClickListener(this);
        tv_gender_secrecy.setOnClickListener(this);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.7f;//设置阴影透明度
        activity.getWindow().setAttributes(lp);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 1f;
        activity.getWindow().setAttributes(lp);
    }

    @Override
    protected int setPopupWindowLayoutId() {
        return R.layout.popupwindow_change_gender;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_gender_male:
                if (null != getOnItemClickListener()) {
                    onItemClickListener.onItemClick(1);
                }
                break;
            case R.id.tv_gender_female:
                if (null != getOnItemClickListener()) {
                    onItemClickListener.onItemClick(2);
                }
                break;
            case R.id.tv_gender_secrecy:
                if (null != getOnItemClickListener()) {
                    onItemClickListener.onItemClick(0);
                }
                break;
            case R.id.tv_gender_cancel:
                ChangeGenderPopupWindow.this.dismiss();
                break;
            default:
                break;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
