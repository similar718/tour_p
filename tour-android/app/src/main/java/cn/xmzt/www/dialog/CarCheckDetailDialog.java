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
import cn.xmzt.www.databinding.DialogCarCheckDetailBinding;
import cn.xmzt.www.selfdrivingtools.bean.MsgCarListInfo;


/**
 * 景区离线dialog
 */
public class CarCheckDetailDialog extends Dialog {

	private Context mContext;

	private View.OnClickListener listener;

	public CarCheckDetailDialog(Context context) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
	}

	DialogCarCheckDetailBinding binding;

	public CarCheckDetailDialog(Context context, View.OnClickListener onClickListener) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
		this.listener = onClickListener;
		binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_car_check_detail, null, false);
		setContentView(binding.getRoot());
		initView();
		binding.tvPre.setOnClickListener(listener);
		binding.tvNext.setOnClickListener(listener);
	}

	protected void initView() {
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.gravity = Gravity.CENTER; // 设置重力
		lp.width= WindowManager.LayoutParams.WRAP_CONTENT;
		dialogWindow.setAttributes(lp);
		setCancelable(true);
	}

    public void setViewData(MsgCarListInfo info, int position, int allsize) {
		String data = info.getMainPoint();
		if (data.contains("\n")){
			if (!data.contains("\n\n")){
				data = data.replace("\n","\n\n");
			}
		}
		binding.tvContent.setText(data);
		binding.tvTitle.setText(info.getCheckItem());
		binding.tvCheckContent.setText(info.getCheckStandard());

		binding.tvPre.setBackgroundResource(R.drawable.shape_rectangle_blue_radius_4);
		binding.tvNext.setBackgroundResource(R.drawable.shape_rectangle_blue_radius_4);
		if (position == 0){ // 第一个
			binding.tvPre.setBackgroundResource(R.drawable.marker_shared_navigation_map_car_info_offline_bg);
		} else if (position == (allsize-1)) { // 最后一条
			binding.tvNext.setBackgroundResource(R.drawable.marker_shared_navigation_map_car_info_offline_bg);
		}
    }
}
