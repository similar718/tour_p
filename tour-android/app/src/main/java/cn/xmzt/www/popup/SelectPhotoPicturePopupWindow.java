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
 * @describe 选择相册的图片或拍照的弹出框
 */

public class SelectPhotoPicturePopupWindow extends BasePopupWindow implements View.OnClickListener {
    private Activity activity;
    private ChangeGenderPopupWindow.OnItemClickListener onItemClickListener;

    public ChangeGenderPopupWindow.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(ChangeGenderPopupWindow.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public SelectPhotoPicturePopupWindow(Activity context) {
        this(context, null);
        activity = context;
    }

    public SelectPhotoPicturePopupWindow(Activity context, AttributeSet attrs) {
        super(context, attrs);
        setPopupWindowSize(0, 0.94);
        TextView tv_take_picture = view.findViewById(R.id.tv_take_picture);
        TextView tv_photo = view.findViewById(R.id.tv_photo);
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        tv_take_picture.setOnClickListener(this);
        tv_photo.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
    }

    @Override
    protected int setPopupWindowLayoutId() {
        return R.layout.popupwindow_select_photo;
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_take_picture:
                if (null != getOnItemClickListener()) {
                    onItemClickListener.onItemClick(0);
                }
                break;
            case R.id.tv_photo:
                if (null != getOnItemClickListener()) {
                    onItemClickListener.onItemClick(1);
                }
                break;
            case R.id.tv_cancel:
                SelectPhotoPicturePopupWindow.this.dismiss();
                break;
            default:
                break;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
