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
import cn.xmzt.www.databinding.DialogAccountWrittenOffBinding;
import cn.xmzt.www.databinding.DialogConfirmBinding;


/**
 * 注销dialog
 */
public class AccountWrittenOffDialog extends Dialog {
	private Context mContext;

	private View.OnClickListener onClickListener;

	public AccountWrittenOffDialog(Context context) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
	}

	DialogAccountWrittenOffBinding binding;

	public AccountWrittenOffDialog(Context context, View.OnClickListener onClickListener) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
		this.onClickListener = onClickListener;
		binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_account_written_off, null, false);
		setContentView(binding.getRoot());
		initView();
	}

	protected void initView() {
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.gravity = Gravity.CENTER; // 设置重力
		lp.width= WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		//lp.y = -mScreenHeight / 10;// 设置y坐标
		dialogWindow.setAttributes(lp);

		// 确定
		binding.confirmTv.setOnClickListener(onClickListener);
		// 取消
		binding.cancelTv.setOnClickListener(onClickListener);
		// 点击之外的地方 dismiss
		setCancelable(true);
	}
    public void setViewData(String title) {
		binding.tipsTv.setText(title);
    }
    public void setViewData(String title,String confirm) {
		binding.tipsTv.setText(title);
		binding.confirmTv.setText(confirm);
    }
    public void setCancelGONE() {
		binding.cancelTv.setVisibility(View.GONE);
    }
}
