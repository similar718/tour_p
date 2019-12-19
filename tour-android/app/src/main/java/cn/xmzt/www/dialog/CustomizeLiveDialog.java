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
import androidx.recyclerview.widget.LinearLayoutManager;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.DialogCustomizeLiveBinding;
import cn.xmzt.www.home.adapter.DialogCustomizeAdapter;
import cn.xmzt.www.home.bean.CustomizeLiveDialogBean;
import cn.xmzt.www.utils.LocalDataUtils;
import cn.xmzt.www.view.listener.ItemListener;


/**
 * 定制界面的设置住宿dialog
 */
public class CustomizeLiveDialog extends Dialog implements View.OnClickListener {
	private Context mContext;

	private ItemListener itemListener;

	public CustomizeLiveDialog(Context context) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
	}

	DialogCustomizeLiveBinding binding;
	DialogCustomizeAdapter mAdapter;

	public CustomizeLiveDialog(Context context,ItemListener itemListener) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
		this.itemListener = itemListener;
		binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_customize_live, null, false);
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

		binding.tvTitleTopJiudian.setOnClickListener(this);
		binding.tvTitleTopMinsu.setOnClickListener(this);

		setCancelable(true);
	}

    public void setViewData(int type,String data) { // 1 酒店 2 民宿 data 表示已选哪一种
		if (type == 1){
			mAdapter.setDatas(LocalDataUtils.getCustomizeJiuDianAllData(data));
			binding.tvTitleTopJiudian.setTextColor(Color.parseColor("#333333"));
			binding.tvTitleTopMinsu.setTextColor(Color.parseColor("#999999"));
		} else if (type == 2){
			mAdapter.setDatas(LocalDataUtils.getCustomizeMinSuAllData(data));
			binding.tvTitleTopJiudian.setTextColor(Color.parseColor("#999999"));
			binding.tvTitleTopMinsu.setTextColor(Color.parseColor("#333333"));
		}else {
			mAdapter.setDatas(LocalDataUtils.getCustomizeJiuDianAllData(data));
			binding.tvTitleTopJiudian.setTextColor(Color.parseColor("#333333"));
			binding.tvTitleTopMinsu.setTextColor(Color.parseColor("#999999"));
		}
    }

	public CustomizeLiveDialogBean getItem(int position) {
		dismiss();
		return  mAdapter.getItem(position);
	}

	@Override
	public void onClick(View v) {
		if(R.id.tv_title_top_jiudian==v.getId()){
			setViewData(1,"");
		}else if(R.id.tv_title_top_minsu==v.getId()){
			setViewData(2,"");
		}
	}
}
