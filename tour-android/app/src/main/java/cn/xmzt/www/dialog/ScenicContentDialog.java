package cn.xmzt.www.dialog;

import android.app.Dialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.DialogScenicContentBinding;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.utils.DpUtil;


/**
 * 景区内容的dialog
 */
public class ScenicContentDialog extends Dialog {
	private Context mContext;

	private View.OnClickListener onClickListener;
	Animation anim = null;
	private AnimationDrawable animationDrawable;

	public ScenicContentDialog(Context context) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
	}

	DialogScenicContentBinding binding;

	public ScenicContentDialog(Context context, View.OnClickListener onClickListener) {
		super(context, R.style.dialog_map_content);
		this.mContext = context;
		this.onClickListener = onClickListener;
		binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_scenic_content, null, false);
		setContentView(binding.getRoot());
		initView();
	}

	protected void initView() {
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.gravity = Gravity.TOP; // 设置重力
		lp.y = DpUtil.dp2px(mContext,110);
		lp.width= WindowManager.LayoutParams.MATCH_PARENT;
		dialogWindow.setAttributes(lp);

		// 布局外面的点击事件 播放
		binding.dialogScenicContentImgRl.setOnClickListener(onClickListener);
		// 点击播放按钮的点击事件
		binding.dialogScenicContentPlayIv.setOnClickListener(onClickListener);
		//地图两个字的点击事件
		binding.dialogScenicContentMapTv.setOnClickListener(onClickListener);
		// 解说
		binding.dialogScenicContentListenIv.setOnClickListener(onClickListener);
		// 下载
		binding.dialogScenicContentDownloadIv.setOnClickListener(onClickListener);
		// 导航
		binding.dialogScenicContentGuideIv.setOnClickListener(onClickListener);
		// 点击之外的地方 dismiss
		setCancelable(true);
	}
    public void setViewData(String name,String intro,String address,String add_intro,String time,String img,int isPlaying) {
		// 需要的数据
		/**
		 *  景区的名称
		 *  景区的简介
		 *  景区的地址
		 *  景区到你现在所在的地址
		 *  景区的开放时间
		 *  景区图片
		 *  当前是否正在播放景区解说
		 */
		GlideUtil.loadImgRadius(binding.dialogScenicContentImgRiv,0,img);
		binding.dialogScenicContentTitleTv.setText(name); // 景区名称
		binding.dialogScenicContentContentTv.setText(intro); // 景区简介
		binding.dialogScenicContentAddressTv.setText(address); // 景区地址
		if (!"".equals(add_intro) && null != add_intro ) { // 主要是没有看到后台有返回这个数据
			binding.dialogScenicContentAddressDetailTv.setText("距离您直线约" + add_intro); // 景区地址详请
		}
		binding.dialogScenicContentOpenTimeTv.setText("开放时间："+time); // 开放时间
		if (isPlaying == 1){ // 正在播放中
			binding.dialogScenicContentPlayIv.setImageResource(R.drawable.play_ing);
//			binding.dialogScenicContentListenIv.setBackgroundResource(R.drawable.scenic_spot_dialog_anim);
//			// 设置动画
//			if (animationDrawable == null) {
//				animationDrawable = (AnimationDrawable) binding.dialogScenicContentListenIv.getBackground();
//			}
//			animationDrawable.start();
		} else if (isPlaying == 2) { // 暂停
			binding.dialogScenicContentPlayIv.setImageResource(R.drawable.play_);
//			binding.dialogScenicContentListenIv.setBackgroundResource(R.drawable.dialog_scenic_content_listen_pause_icon);
//			if (animationDrawable != null) {
//				animationDrawable.stop();
//			}
		} else if (isPlaying == 0){ // 没有播放
			binding.dialogScenicContentPlayIv.setImageResource(R.drawable.play_);
//			binding.dialogScenicContentListenIv.setBackgroundResource(R.drawable.dialog_scenic_content_listen_play_icon);
//			if (animationDrawable != null) {
//				animationDrawable.stop();
//			}
		}
		if (isPlaying == 1){
			if (anim == null) {
				anim = AnimationUtils.loadAnimation(mContext, R.anim.img_rotate_anim);
				LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
				anim.setInterpolator(lin);
			}
			binding.dialogScenicContentImgRiv.startAnimation(anim);
		} else {
			binding.dialogScenicContentImgRiv.clearAnimation();
		}
    }
}
