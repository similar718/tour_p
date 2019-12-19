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
import cn.xmzt.www.databinding.DialogConfirmBinding;
import cn.xmzt.www.databinding.DialogNewVersionBinding;
import cn.xmzt.www.mine.bean.AppVersionBean;


/**
 * 确认dialog
 */
public class NewVersionDialog extends Dialog {
	private Context mContext;

	private View.OnClickListener onClickListener;

	public NewVersionDialog(Context context) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
	}

	DialogNewVersionBinding binding;

	public NewVersionDialog(Context context, View.OnClickListener onClickListener) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
		this.onClickListener = onClickListener;
		binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_new_version, null, false);
		setContentView(binding.getRoot());
		initView();
	}

	protected void initView() {
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.gravity = Gravity.CENTER; // 设置重力
		//lp.y = -mScreenHeight / 10;// 设置y坐标
		dialogWindow.setAttributes(lp);

		// 确定
		binding.ivNewUpdate.setOnClickListener(onClickListener);
		// 取消
		binding.ivCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancel();
			}
		});
		// 点击之外的地方 不dismiss
		setCancelable(false);
	}
    public void setViewData(AppVersionBean appVersionBean) {
		binding.tvTitle.setText(appVersionBean.getTitle());
		binding.tvVersionTag.setText(appVersionBean.getVersion());
		binding.tvHint.setText(appVersionBean.getTagline());
		binding.tvContent.setText(appVersionBean.getContent());
    }

    public void setCancelGONE() {
		binding.ivCancel.setVisibility(View.GONE);
    }
}
