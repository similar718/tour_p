package cn.xmzt.www.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.xmzt.www.R;

/**
 * 自定义dialog
 */
public class DialogUtil {

    private static final String TAG = "DialogUtil";

    private Dialog loadingDialog;
    private Context mContext;

    public DialogUtil() {
        super();
    }


//    public  DialogUtil getDialog() {
//        if (instance == null) {
//            synchronized (DialogUtil.class) {
//                if (instance == null) {
//                    instance = new DialogUtil();
//                }
//            }
//        }
//        return instance;
//    }

    /**
     * 显示等待对话框
     *
     * @param message 对话框内容
     */
    public void showProgressDialog(Context context, String message) {
        try {
            createLoadingDialog(context, message);
        }catch (Exception e){}


    }

    /**
     * 取消等待对话框
     */
    public void cancelProgressDialog() {
        try {
            cancelLoadingDialog();
        }catch (Exception e){}
    }

    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @param msg
     * @return
     */
    private void createLoadingDialog(Context context, String msg) {

        if (loadingDialog != null && loadingDialog.isShowing()) {
            return;
        }
        mContext = context;
        if (context == null) {
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_progress_custom, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.spinnerImageView);
        TextView tipTextView = (TextView) v.findViewById(R.id.message);// 提示文字
        // 获取ImageView上的动画背景
        AnimationDrawable spinner = (AnimationDrawable) spaceshipImage.getBackground();
        // 开始动画
        spinner.start();
        tipTextView.setText(msg);// 设置加载信息
        loadingDialog = new Dialog(context, R.style.AlertDialogStyle);// 创建自定义样式dialog
        loadingDialog.setCancelable(true);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.FILL_PARENT));// 设置布局
        loadingDialog.setCanceledOnTouchOutside(false);
        if(context!=null&&!((Activity) context).isFinishing()){
            loadingDialog.show();
        }

    }

    private void cancelLoadingDialog() {
        if (mContext!=null&&!((Activity) mContext).isFinishing()&&loadingDialog != null&&loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

}
