package cn.xmzt.www.suspension;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import cn.xmzt.www.R;

/**
 * @author Averysk
 * @date 2019/9/4
 * @describe
 */

public class FloatAudioPlayLayout extends FrameLayout {
    private final WindowManager mWindowManager;
    private WindowManager.LayoutParams mWmParams;
    private Context mContext;

    protected Chronometer timerPlayAudio; // 语音播放时间
    protected ImageView ivPlayAudioAvatar; // 语音播放对象头像
    protected TextView tvPlayAudioName; // 语音播放对象名称
    protected TextView tvPlayAudioIntro; // 语音播放对象简介
    protected TextView tvPlayAudioNext; // 语音播放对象下一条

    private ItemClickListener itemClickListener;

    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FloatAudioPlayLayout(Context context) {
        this(context, null);
        mContext = context;
    }

    public FloatAudioPlayLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.layout_intercom_audio_play, this);
        //浮动窗口按钮
        timerPlayAudio = findViewById(R.id.timer_play_audio);
        ivPlayAudioAvatar = findViewById(R.id.iv_play_audio_avatar);
        tvPlayAudioName = findViewById(R.id.tv_play_audio_name);
        tvPlayAudioIntro = findViewById(R.id.tv_play_audio_intro);
        tvPlayAudioNext = findViewById(R.id.tv_play_audio_next);
        tvPlayAudioNext.setVisibility(View.GONE);
    }


    /**
     * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
     *
     * @param params 小悬浮窗的参数
     */
    public void setParams(WindowManager.LayoutParams params) {
        mWmParams = params;
    }

    public interface ItemClickListener {
        void onNextClick();
    }

}
