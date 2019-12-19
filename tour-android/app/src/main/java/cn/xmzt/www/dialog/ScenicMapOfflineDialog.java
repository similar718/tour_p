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
import cn.xmzt.www.databinding.DialogScenicMapOfflineBinding;


/**
 * 景区离线dialog
 */
public class ScenicMapOfflineDialog extends Dialog {

	private Context mContext;

	private View.OnClickListener onClickListener;

	public ScenicMapOfflineDialog(Context context) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
	}

	DialogScenicMapOfflineBinding binding;

	public ScenicMapOfflineDialog(Context context, View.OnClickListener onClickListener) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
		this.onClickListener = onClickListener;
		binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_scenic_map_offline, null, false);
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

		// 手绘地图下载
		binding.dialogScenicMapOfflineOneIv.setOnClickListener(onClickListener);
		// 语音离线下载
		binding.dialogScenicMapOfflineTwoIv.setOnClickListener(onClickListener);
		// 取消
		binding.dialogCancelIv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});
		setCancelable(false);
	}
    public void setViewData(String oneSize,String twoSize,int oneType,int twoType,int progress) {
        // 需要的数据 手绘地图的大小  离线语音包的大小 手绘地图下载的状态  没有下/等待下载/下载中/下载完成  语音包的下载状态  跟前面一样
		binding.dialogScenicMapOfflineSizeTv.setText(oneSize);
		binding.dialogScenicMapOfflineVoiceSizeTv.setText(twoSize);
//		if (oneType == 0){ // 未开始下载
//			binding.dialogScenicMapOfflineOneIv.setImageResource(R.drawable.dialog_scenic_map_download_no_icon);
//		} else if (oneType == 1){ // 等待下载中
//			binding.dialogScenicMapOfflineOneIv.setImageResource(R.drawable.dialog_scenic_map_download_start_icon);
//		} else if (oneType == 2){ // 下载中
//			binding.dialogScenicMapOfflineOneIv.setImageResource(R.drawable.dialog_scenic_map_download_ing_icon);
//		} else if (oneType == 3){ // 下载完成
			binding.dialogScenicMapOfflineOneIv.setImageResource(R.drawable.dialog_scenic_map_download_finish_icon);// 只要加载到界面就已经下载好了
//		}
		if (twoType == 0){ // 未开始下载
			binding.dialogScenicMapOfflineTwoIv.setImageResource(R.drawable.dialog_scenic_map_download_no_icon);
			binding.dialogScenicMapOfflineTwoIv.setVisibility(View.VISIBLE);
			binding.cpvProgress.setVisibility(View.GONE);
			binding.tvDownloadProgress.setVisibility(View.GONE);
		} else if (twoType == 1){ // 等待下载中
			binding.dialogScenicMapOfflineTwoIv.setImageResource(R.drawable.dialog_scenic_map_download_start_icon);
			binding.dialogScenicMapOfflineTwoIv.setVisibility(View.GONE);
			binding.cpvProgress.setProgress(progress);
			binding.cpvProgress.setVisibility(View.VISIBLE);
			binding.tvDownloadProgress.setVisibility(View.VISIBLE);
			binding.tvDownloadProgress.setText(progress+"%");
		} else if (twoType == 2){ // 下载中
			binding.dialogScenicMapOfflineTwoIv.setImageResource(R.drawable.dialog_scenic_map_download_ing_icon);
			binding.dialogScenicMapOfflineTwoIv.setVisibility(View.GONE);
			binding.cpvProgress.setProgress(progress);
			binding.cpvProgress.setVisibility(View.VISIBLE);
			binding.tvDownloadProgress.setVisibility(View.VISIBLE);
			binding.tvDownloadProgress.setText(progress+"%");
		} else if (twoType == 3){ // 下载完成
			binding.dialogScenicMapOfflineTwoIv.setImageResource(R.drawable.dialog_scenic_map_download_finish_icon);
			binding.dialogScenicMapOfflineTwoIv.setVisibility(View.VISIBLE);
			binding.cpvProgress.setVisibility(View.GONE);
			binding.tvDownloadProgress.setVisibility(View.GONE);
		}
    }
}
