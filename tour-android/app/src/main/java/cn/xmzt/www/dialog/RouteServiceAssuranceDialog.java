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
import cn.xmzt.www.databinding.DialogRouteServiceAssurancelBinding;


/**
 * 线路服务保障Dialog
 */
public class RouteServiceAssuranceDialog extends Dialog {
	private Context context;
	DialogRouteServiceAssurancelBinding binding;
	public RouteServiceAssuranceDialog(Context context) {
		super(context, R.style.dialog_custom);
		this.context = context;
		this.context = context;
		binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_route_service_assurancel, null, false);
		setContentView(binding.getRoot());
		initView();
	}

	protected void initView() {
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.gravity = Gravity.BOTTOM; // 设置重力
		//lp.y = -mScreenHeight / 10;// 设置y坐标
		lp.width= WindowManager.LayoutParams.MATCH_PARENT;
		int heightPixels=getContext().getResources().getDisplayMetrics().heightPixels;
		lp.height= heightPixels/100*90;
		dialogWindow.setAttributes(lp);

		binding.ivCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancel();
			}
		});
		setCancelable(false);
	}
}
