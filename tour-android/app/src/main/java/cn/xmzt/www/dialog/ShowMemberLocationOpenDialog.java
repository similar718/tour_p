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
import cn.xmzt.www.databinding.DialogShowMemberLocationOpenBinding;

/**
 * 是否开启成员位置？
 */
public class ShowMemberLocationOpenDialog extends Dialog {

	private Context mContext;

	public ShowMemberLocationOpenDialog(Context context) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
	}

	DialogShowMemberLocationOpenBinding binding;

	private View.OnClickListener listener;

	public ShowMemberLocationOpenDialog(Context context, View.OnClickListener onClickListener) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
		this.listener = onClickListener;
		binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_show_member_location_open, null, false);
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
		setCancelable(true);

		binding.tvCancel.setOnClickListener(listener);
		binding.tvSure.setOnClickListener(listener);
	}
}
