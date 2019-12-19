package cn.xmzt.www.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.DialogCustomizeYusuanBinding;
import cn.xmzt.www.home.adapter.DialogCustomizeAdapter;
import cn.xmzt.www.home.bean.CustomizeLiveDialogBean;
import cn.xmzt.www.utils.LocalDataUtils;
import cn.xmzt.www.view.listener.ItemListener;


/**
 * 定制界面的人均预算dialog
 */
public class CustomizeYuSuanDialog extends Dialog {
	private Context mContext;

	private ItemListener itemListener;

	public CustomizeYuSuanDialog(Context context) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
	}

	DialogCustomizeYusuanBinding binding;
	DialogCustomizeAdapter mAdapter;

	public CustomizeYuSuanDialog(Context context, ItemListener itemListener) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
		this.itemListener = itemListener;
		binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_customize_yusuan, null, false);
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

		mAdapter = new DialogCustomizeAdapter();
		mAdapter.setItemListener(itemListener);
		binding.rvList.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
		binding.rvList.setAdapter(mAdapter);

		setCancelable(true);
	}

    public void setViewData(String data) {
		mAdapter.setDatas(LocalDataUtils.getCustomizeYuSuanAllData(data));
    }
	public CustomizeLiveDialogBean getItem(int position) {
		dismiss();
		return  mAdapter.getItem(position);
	}
}
