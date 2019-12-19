package cn.xmzt.www.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import cn.xmzt.www.R;
import cn.xmzt.www.databinding.DialogScenicSpotListBinding;
import cn.xmzt.www.selfdrivingtools.adapter.DialogScenicSpotListAdapter;

import java.util.ArrayList;


/**
 * 景区地图导航的dialog
 */
public class ScenicSpotListDialog extends BottomSheetDialog {
	private Context mContext;

	private View.OnClickListener onClickListener;
	DialogScenicSpotListAdapter mAdaptr;

	public ScenicSpotListDialog(Context context) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
	}

	DialogScenicSpotListBinding binding;

	public ScenicSpotListDialog(Context context, View.OnClickListener onClickListener) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
		this.onClickListener = onClickListener;
		binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_scenic_spot_list, null, false);
		setContentView(binding.getRoot());
		initView();
	}

	protected void initView() {
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.gravity = Gravity.BOTTOM; // 设置重力
		//lp.y = -mScreenHeight / 10;// 设置y坐标
		lp.width= WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		dialogWindow.setAttributes(lp);

		mAdaptr = new DialogScenicSpotListAdapter(mContext);
		binding.rvList.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
		binding.rvList.setAdapter(mAdaptr);
		// 取消
		binding.ivCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});
		setCancelable(true);
	}
    public void setViewData(ArrayList<String> list) {
		mAdaptr.setDatas(list);
    }
}
