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
import cn.xmzt.www.databinding.DialogMakeInvoiceServiceBinding;


/**
 * 开票项目服务选择
 */
public class MakeInvoiceServiceDialog extends Dialog {
	private Context context;
	private View.OnClickListener onClickListener;
	public MakeInvoiceServiceDialog(Context context) {
		super(context, R.style.dialog_custom);
		this.context = context;
	}
	DialogMakeInvoiceServiceBinding binding;
	public MakeInvoiceServiceDialog(Context context, View.OnClickListener onClickListener) {
		super(context, R.style.dialog_custom);
		this.context = context;
		this.onClickListener = onClickListener;
		binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_make_invoice_service, null, false);
		setContentView(binding.getRoot());
		initView();
	}

	protected void initView() {
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.gravity = Gravity.BOTTOM; // 设置重力
		//lp.y = -mScreenHeight / 10;// 设置y坐标
		lp.width= WindowManager.LayoutParams.MATCH_PARENT;
		dialogWindow.setAttributes(lp);
		binding.tvItem1.setOnClickListener(onClickListener);
		binding.tvItem2.setOnClickListener(onClickListener);
		setCancelable(false);
	}
}
