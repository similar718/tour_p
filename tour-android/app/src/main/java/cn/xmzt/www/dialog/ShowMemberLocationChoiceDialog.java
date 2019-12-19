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

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.DialogShowMemberLocationChoiceBinding;
import cn.xmzt.www.intercom.bean.MyTalkGroupBean;
import cn.xmzt.www.intercom.bean.MyTalkGroupsBean;
import cn.xmzt.www.selfdrivingtools.adapter.ShowMemberLocationChoiceAdapter;

/**
 * 请选择需要展示的队伍
 */
public class ShowMemberLocationChoiceDialog extends Dialog {

	private Context mContext;
	private ShowMemberLocationChoiceAdapter mAdapter;

	private View.OnClickListener listener;

	public ShowMemberLocationChoiceDialog(Context context) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
	}

	DialogShowMemberLocationChoiceBinding binding;

	public ShowMemberLocationChoiceDialog(Context context, View.OnClickListener onClickListener) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
		this.listener = onClickListener;
		binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_show_member_location_choice, null, false);
		setContentView(binding.getRoot());
		initView();
	}

	protected void initView() {
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.gravity = Gravity.CENTER; // 设置重力
		lp.width= WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		dialogWindow.setAttributes(lp);
		setCancelable(true);

		mAdapter = new ShowMemberLocationChoiceAdapter(mContext);
		binding.rvList.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
		binding.rvList.setAdapter(mAdapter);

		binding.ivCancel.setOnClickListener(listener);

//		binding.ivOpenLocation.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				boolean m = ((ScenicSpotMapActivity)mContext).mIsOpenGroupLocation;
//				if (m){
//					((ScenicSpotMapActivity)mContext).mIsOpenGroupLocation = false;
//					binding.ivOpenLocation.setImageResource(R.drawable.icon_group_off);
//				} else {
//					((ScenicSpotMapActivity)mContext).mIsOpenGroupLocation = true;
//					binding.ivOpenLocation.setImageResource(R.drawable.icon_group_on);
//				}
//			}
//		});

		binding.ivOpenLocation.setOnClickListener(listener);
	}

    public void setViewData(List<MyTalkGroupBean> beans, int type, boolean isOpenLocation) {
		mAdapter.setDatas(beans);
		if (type == 1){ // 之前未选择 ，提示请选择
			binding.tvNotSelect.setVisibility(View.VISIBLE);
			binding.rlSelect.setVisibility(View.GONE);
		} else if (type == 2) { // 之前已选择 显示是否开启群成员位置
			binding.tvNotSelect.setVisibility(View.GONE);
			binding.rlSelect.setVisibility(View.VISIBLE);
			if (isOpenLocation){
				binding.ivOpenLocation.setImageResource(R.drawable.icon_group_on);
			} else {
				binding.ivOpenLocation.setImageResource(R.drawable.icon_group_off);
			}
		}
    }

}
