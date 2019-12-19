package cn.xmzt.www.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.DialogScenicSpotWifiHintBinding;

/**
 * WIFI情况下没有下载提示用户下载
 */
public class ScenicSpotWIFIHintDialog extends Dialog {

	private Context mContext;

	public ScenicSpotWIFIHintDialog(Context context) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
	}

	DialogScenicSpotWifiHintBinding binding;
	View.OnClickListener listener;

	public ScenicSpotWIFIHintDialog(Context context, View.OnClickListener onClickListener) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
		this.listener = onClickListener;
		binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_scenic_spot_wifi_hint, null, false);
		setContentView(binding.getRoot());
		initView();
	}

	protected void initView() {
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.gravity = Gravity.CENTER; // 设置重力
		lp.width= WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		dialogWindow.setAttributes(lp);
		setCancelable(false);

		binding.tvCancel.setOnClickListener(listener);
		binding.tvSure.setOnClickListener(listener);
	}

	public void setContent(String title,String leftContent,String rightContent){
		binding.tvTitle.setText(title);
		binding.tvCancel.setText(leftContent);
		binding.tvSure.setText(rightContent);
	}
}
