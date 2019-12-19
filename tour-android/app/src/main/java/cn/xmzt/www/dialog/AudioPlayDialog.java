package cn.xmzt.www.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.DialogAudioPlayBinding;
import cn.xmzt.www.intercom.api.NimUIKit;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;


/**
 * 音频播放
 * @author Averysk
 */
public class AudioPlayDialog extends Dialog {

	private Context context;
	private View.OnClickListener onClickListener;

	DialogAudioPlayBinding binding;
	public AudioPlayDialog(Context context, View.OnClickListener onClickListener) {
		super(context, R.style.dialog_audio_play);
		this.context = context;
		this.onClickListener = onClickListener;
		binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_audio_play, null, false);
		setContentView(binding.getRoot());
		initView();
	}

	protected void initView() {
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.gravity = Gravity.CENTER; // 设置重力
		//lp.y = -mScreenHeight / 10;// 设置y坐标
		//lp.width= WindowManager.LayoutParams.WRAP_CONTENT;
		dialogWindow.setAttributes(lp);
		binding.tvPlayAudioNext.setOnClickListener(onClickListener);
		setCancelable(false);
	}

    public void setViewData(IMMessage imMessage) {
		Team t = NimUIKit.getTeamProvider().getTeamById(imMessage.getSessionId());
		UserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(imMessage.getFromAccount());
		if (userInfo != null){
			// 收听到的人的头像
			binding.ivPlayAudioAvatar.loadAvatar(userInfo.getAvatar());
		}
		if (t != null){
			// 收听到的群组名称
			binding.tvPlayAudioName.setText(t.getName());
		}
		// 收听到的人员名称
		binding.tvPlayAudioIntro.setText(imMessage.getFromNick());
    }

    public void setViewData(String groupId, String userId) {
		Team t = NimUIKit.getTeamProvider().getTeamById(groupId);
		UserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(userId);
		if (userInfo != null){
			// 收听到的人的头像
			binding.ivPlayAudioAvatar.loadAvatar(userInfo.getAvatar());
			// 收听到的人员名称
			binding.tvPlayAudioIntro.setText(userInfo.getName());
		}
		if (t != null){
			// 收听到的群组名称
			binding.tvPlayAudioName.setText(t.getName());
		}
    }

    // 显示
	public void showDilog() {
		setAudioPlayStartTimer();
		show();
	}

	// 隐藏
	public void cancelDialog() {
		setAudioPlayEndTimer();
		cancel();
	}

	// 开始记时
	public void setAudioPlayStartTimer() {
		binding.timerPlayAudio.setBase(SystemClock.elapsedRealtime());
		binding.timerPlayAudio.start();
	}

	// 结束记时
	public void setAudioPlayEndTimer() {
		binding.timerPlayAudio.stop();
		binding.timerPlayAudio.setBase(SystemClock.elapsedRealtime());
	}

}
