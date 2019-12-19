package cn.xmzt.www.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.DialogCustomizeLeaderTypeBinding;


/**
 * 定制界面的设置领队类型dialog
 */
public class CustomizeLeaderTypeDialog extends Dialog {
	private Context mContext;

	private View.OnClickListener onClickListener;

	public CustomizeLeaderTypeDialog(Context context) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
	}

	DialogCustomizeLeaderTypeBinding binding;

	public CustomizeLeaderTypeDialog(Context context, View.OnClickListener onClickListener) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
		this.onClickListener = onClickListener;
		binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_customize_leader_type, null, false);
		setContentView(binding.getRoot());
		initView();
	}

	protected void initView() {
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.gravity = Gravity.BOTTOM; // 设置重力
		//lp.y = -mScreenHeight / 10;// 设置y坐标
		lp.width= WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		dialogWindow.setAttributes(lp);

		binding.rlLeader.setOnClickListener(onClickListener);
		binding.rlNoLeader.setOnClickListener(onClickListener);
		setCancelable(true);
	}

    public void setViewData(int type) {
		if (type == 1){
			binding.tvLeader.setTextColor(Color.parseColor("#999999"));
			binding.ivLeader.setVisibility(View.INVISIBLE);
			binding.tvNoLeader.setTextColor(Color.parseColor("#24a4ff"));
			binding.ivNoLeader.setVisibility(View.VISIBLE);
		} else if (type == 2){
			binding.tvLeader.setTextColor(Color.parseColor("#24a4ff"));
			binding.ivLeader.setVisibility(View.VISIBLE);
			binding.tvNoLeader.setTextColor(Color.parseColor("#999999"));
			binding.ivNoLeader.setVisibility(View.INVISIBLE);
		}else {
			binding.tvLeader.setTextColor(Color.parseColor("#999999"));
			binding.ivLeader.setVisibility(View.INVISIBLE);
			binding.tvNoLeader.setTextColor(Color.parseColor("#24a4ff"));
			binding.ivNoLeader.setVisibility(View.VISIBLE);
		}
    }

}
