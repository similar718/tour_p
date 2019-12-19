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
import cn.xmzt.www.databinding.DialogInvoiceTypeBinding;


/**
 * 发票类型 1公司 2个人
 */
public class InvoiceTypeDialog extends Dialog {
	private Context context;
	private View.OnClickListener onClickListener;
	public InvoiceTypeDialog(Context context) {
		super(context, R.style.dialog_custom);
		this.context = context;
	}
	DialogInvoiceTypeBinding binding;
	public InvoiceTypeDialog(Context context, View.OnClickListener onClickListener) {
		super(context, R.style.dialog_custom);
		this.context = context;
		this.onClickListener = onClickListener;
		binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_invoice_type, null, false);
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
