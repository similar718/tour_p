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
import cn.xmzt.www.databinding.DialogScenicMapGuideBinding;
import cn.xmzt.www.glide.GlideUtil;


/**
 * 景区地图导航的dialog
 */
public class ScenicMapGuideDialog extends Dialog {
	private Context mContext;

	private View.OnClickListener onClickListener;

	public ScenicMapGuideDialog(Context context) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
	}

	DialogScenicMapGuideBinding binding;

	public ScenicMapGuideDialog(Context context, View.OnClickListener onClickListener) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
		this.onClickListener = onClickListener;
		binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_scenic_map_guide, null, false);
		setContentView(binding.getRoot());
		initView();
	}

	protected void initView() {
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.gravity = Gravity.CENTER; // 设置重力
		//lp.y = -mScreenHeight / 10;// 设置y坐标
		lp.width= WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		dialogWindow.setAttributes(lp);

		// 地图导航
		binding.dialogScenicMapGuideBtnIv.setOnClickListener(onClickListener);
		// 取消
		binding.dialogCancelIv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});
		setCancelable(false);
	}
    public void setViewData(String img,String address) {
		GlideUtil.loadImgTopC(binding.dialogScenicMapGuideImgIv,img);
		binding.dialogScenicMapGuideAddressTv.setText(address);
    }
}
