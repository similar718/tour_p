package cn.xmzt.www.dialog;

import android.app.Dialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.DialogThirdPartyMapsGuideBinding;


/**
 * 确认dialog
 */
public class ThirdPartyMapsGuideDialog extends Dialog {
	private Context mContext;

	private View.OnClickListener onClickListener;

	public ThirdPartyMapsGuideDialog(Context context) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
	}

	DialogThirdPartyMapsGuideBinding binding;

	public ThirdPartyMapsGuideDialog(Context context, View.OnClickListener onClickListener) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
		this.onClickListener = onClickListener;
		binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_third_party_maps_guide, null, false);
		setContentView(binding.getRoot());
		initView();
	}

	protected void initView() {
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.gravity = Gravity.BOTTOM; // 设置重力
		lp.width= WindowManager.LayoutParams.MATCH_PARENT;
		dialogWindow.setAttributes(lp);

		// 高德
		binding.dialogThirdPartyMapsGuideAmap.setOnClickListener(onClickListener);
		// 百度
		binding.dialogThirdPartyMapsGuideBdmap.setOnClickListener(onClickListener);
		// 腾讯
		binding.dialogThirdPartyMapsGuideTcmap.setOnClickListener(onClickListener);
		// 取消
		binding.dialogThirdPartyMapsGuideCancel.setOnClickListener(onClickListener);
		// 点击之外的地方 dismiss
		setCancelable(true);
	}
}
