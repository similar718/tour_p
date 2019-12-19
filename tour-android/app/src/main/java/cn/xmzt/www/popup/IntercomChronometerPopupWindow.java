package cn.xmzt.www.popup;

import android.app.Activity;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BasePopupWindow;
import cn.xmzt.www.nim.uikit.common.util.sys.ScreenUtil;

/**
 * 对讲按钮上面的时间计时表
 * @author Averysk
 */
public class IntercomChronometerPopupWindow extends BasePopupWindow {

    private Chronometer timerAudioRecord; // 记录时间
    private ImageView iv_left; // 左边动画
    private ImageView iv_right; // 右边动画
    @Override
    protected int setPopupWindowLayoutId() {
        return R.layout.popupwindow_intercom_chronometer;
    }

    public IntercomChronometerPopupWindow(Activity context) {
        this(context, null);
    }

    public IntercomChronometerPopupWindow(Activity context, AttributeSet attrs) {
        super(context, attrs);
        setPopupWindowSize(0, 0);
        initView();
    }

    // 初始化
    private void initView() {
        timerAudioRecord = view.findViewById(R.id.timer_audio_record);
        iv_left = view.findViewById(R.id.iv_left);
        iv_right = view.findViewById(R.id.iv_right);
    }

    // 开始记时
    public void setTimerStart() {
        timerAudioRecord.setBase(SystemClock.elapsedRealtime());
        timerAudioRecord.start();
    }

    // 停止记时
    public void setTimerHide() {
        timerAudioRecord.stop();
        timerAudioRecord.setBase(SystemClock.elapsedRealtime());
    }

    // 显示窗口
    public void showPop(View anchor){
        setTimerStart();
        int width=anchor.getResources().getDimensionPixelOffset(R.dimen.dp_130);
        int dp_5=anchor.getResources().getDimensionPixelOffset(R.dimen.dp_5);//anchor控件的左右margin
        int xoff=(width-anchor.getWidth())/2-dp_5;
        this.showAsDropDown(anchor,-xoff, -ScreenUtil.dip2px(20+100) , Gravity.CENTER_HORIZONTAL);
    }

    // 隐藏窗口
    public void holePop(){
        setTimerHide();
        setCurrentRecordAmplitude(0);
        this.dismiss();
    }

    /**
     * 设置当前录音的振幅
     * @param amplitude
     */
    public void setCurrentRecordAmplitude(long amplitude) {
        if(iv_left==null||iv_right==null){
            return;
        }
        long amplitudeSize=amplitude/100;
        if(amplitudeSize>60){
            iv_left.setImageResource(R.drawable.ic_chat_record_status_1_5);
            iv_right.setImageResource(R.drawable.ic_chat_record_status_2_5);
        }else if (amplitudeSize>40){
            iv_left.setImageResource(R.drawable.ic_chat_record_status_1_4);
            iv_right.setImageResource(R.drawable.ic_chat_record_status_2_4);
        }else if (amplitudeSize>30){
            iv_left.setImageResource(R.drawable.ic_chat_record_status_1_3);
            iv_right.setImageResource(R.drawable.ic_chat_record_status_2_3);
        }else if (amplitudeSize>20){
            iv_left.setImageResource(R.drawable.ic_chat_record_status_1_2);
            iv_right.setImageResource(R.drawable.ic_chat_record_status_2_2);
        }else {
            iv_left.setImageResource(R.drawable.ic_chat_record_status_1);
            iv_right.setImageResource(R.drawable.ic_chat_record_status_2);
        }
    }
}
