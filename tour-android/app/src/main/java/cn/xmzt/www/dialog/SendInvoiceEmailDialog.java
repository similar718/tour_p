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
import cn.xmzt.www.databinding.DialogSendInvoiceBinding;


/**
 * 发送发票Email
 */
public class SendInvoiceEmailDialog extends Dialog {
	private Context context;
	private View.OnClickListener onClickListener;
	public SendInvoiceEmailDialog(Context context) {
		super(context, R.style.dialog_custom);
		this.context = context;
	}
	DialogSendInvoiceBinding binding;
	public SendInvoiceEmailDialog(Context context, View.OnClickListener onClickListener) {
		super(context, R.style.dialog_custom);
		this.context = context;
		this.onClickListener = onClickListener;
		binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_send_invoice, null, false);
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
		binding.tvSend.setOnClickListener(onClickListener);
		binding.ivCancel.setOnClickListener(onClickListener);
		binding.ivDel.setOnClickListener(onClickListener);
		setCancelable(false);
	}

	public void setViewData(String email,String title) {
		if(email!=null){
			binding.etEmail.setText(email);
			binding.etEmail.setSelection(email.length());
		}
		binding.tvTitle.setText(title);
	}
	public void clear(){
		binding.etEmail.setText("");
	}

	public String getEmail() {
		if(binding!=null){
			return binding.etEmail.getText().toString();
		}
		return "";
	}
}
