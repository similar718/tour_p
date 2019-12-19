package cn.xmzt.www.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.DialogShowMemberLocationHintBinding;

/**
 * 系统检测到，您有正在出行的队伍， 是否需要在景区地图显示成员位置？
 */
public class ShowMemberLocationHintDialog extends Dialog {

	private Context mContext;
	private boolean mIsShowNear = false;

	public ShowMemberLocationHintDialog(Context context) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
	}

	DialogShowMemberLocationHintBinding binding;

	public ShowMemberLocationHintDialog(Context context, setShowMemberLocationHintListener onClickListener) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
		this.listener = onClickListener;
		binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_show_member_location_hint, null, false);
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
		setCancelable(false);

		binding.tvShowStatus.setOnClickListener(new View.OnClickListener() { // 近期不再显示
			@Override
			public void onClick(View v) {
				if (mIsShowNear){ // 改为未选中
					mIsShowNear = false;
					Drawable drawable = mContext.getResources().getDrawable(R.drawable.scenic_spot_map_item_route_line_right_icon_un);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
					binding.tvShowStatus.setCompoundDrawables(drawable,null, null, null);
				} else { // 改为选中
					mIsShowNear = true;
					Drawable drawable = mContext.getResources().getDrawable(R.drawable.scenic_spot_map_item_route_line_right_icon);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
					binding.tvShowStatus.setCompoundDrawables(drawable,null, null, null);
				}
			}
		});
		binding.tvCancel.setOnClickListener(new View.OnClickListener() { // 不显示
			@Override
			public void onClick(View v) {
				dismiss();
				listener.cancelShowMember(mIsShowNear);
			}
		});
		binding.tvSure.setOnClickListener(new View.OnClickListener() { // 显示成员
			@Override
			public void onClick(View v) {
				dismiss();
				listener.sureShowMember(mIsShowNear);
			}
		});
	}

    public void setViewData(boolean data) {
		mIsShowNear = data;
    }

    private setShowMemberLocationHintListener listener;

    public interface setShowMemberLocationHintListener{
		void sureShowMember(boolean show); // show 表示近期是否显示
		void cancelShowMember(boolean show);
	}
}
