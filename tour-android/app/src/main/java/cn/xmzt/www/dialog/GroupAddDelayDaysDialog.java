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
import cn.xmzt.www.databinding.DialogGroupSetDelayTimeBinding;

/**
 * 设置添加延期天数dialog
 */
public class GroupAddDelayDaysDialog extends Dialog {

	private Context context;
	private View.OnClickListener onClickListener;

	public GroupAddDelayDaysDialog(Context context) {
		super(context, R.style.dialog_custom);
		this.context = context;
	}
	DialogGroupSetDelayTimeBinding binding;
	public GroupAddDelayDaysDialog(Context context, View.OnClickListener onClickListener) {
		super(context, R.style.dialog_custom);
		this.context = context;
		this.onClickListener = onClickListener;
		binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_group_set_delay_time, null, false);
		setContentView(binding.getRoot());
		initView();
	}

	protected void initView() {
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.gravity = Gravity.BOTTOM; // 设置重力
		lp.width= WindowManager.LayoutParams.MATCH_PARENT;
		dialogWindow.setAttributes(lp);
		binding.tvAdd5.setOnClickListener(onClickListener);
		binding.tvAdd10.setOnClickListener(onClickListener);
		binding.tvCancel.setOnClickListener(onClickListener);
		setCancelable(false);
	}

    public void setViewData() {

    }
}
