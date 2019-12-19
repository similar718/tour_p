package cn.xmzt.www.suspension;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.xmzt.www.R;

/**
 * @author tanlei
 * @date 2019/9/4
 * @describe
 */

public class FloatLayout extends FrameLayout {
    private final WindowManager mWindowManager;
    private long startTime;
    private float mTouchStartX;
    private float mTouchStartY;
    private WindowManager.LayoutParams mWmParams;
    private Context mContext;
    private long endTime;
    private LinearLayout llAudioRecord;
    protected Chronometer timerAudioRecord; // 记录时间
    private ImageView ivAudioRecord; // 对讲按键
    private LongClickListener longClickListener;
    private boolean isCanTalk = true; // 是否能够对讲

    public LongClickListener getLongClickListener() {
        return longClickListener;
    }

    public void setLongClickListener(LongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public FloatLayout(Context context) {
        this(context, null);
        mContext = context;
    }

    public FloatLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.layout_intercom_audio_record, this);
        //浮动窗口按钮
        llAudioRecord = findViewById(R.id.ll_audio_record);
        timerAudioRecord = findViewById(R.id.timer_audio_record);
        ivAudioRecord = findViewById(R.id.iv_audio_record);
    }

    Handler handler = new Handler();

    boolean istalk;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获取相对屏幕的坐标，即以屏幕左上角为原点
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        //下面的这些事件，跟图标的移动无关，为了区分开拖动和点击事件
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                startTime = System.currentTimeMillis();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("lee", "说话不准移动");
                        //llAudioRecord.setVisibility(View.VISIBLE);
                        if (getLongClickListener() != null) {
                            if (isCanTalk){
                                longClickListener.onTalkClick();
                                ivAudioRecord.setBackgroundResource(R.drawable.ic_intercom_processing);
                            }
                        }
                        istalk = true;
                    }
                }, 200);
                break;
            case MotionEvent.ACTION_MOVE:
                //图标移动的逻辑在这里
                float mMoveStartX = event.getX();
                float mMoveStartY = event.getY();
                if (Math.abs(mTouchStartX - mMoveStartX) > 3 && Math.abs(mTouchStartY - mMoveStartY) > 3) {// 如果移动量大于3才移动
                    handler.removeCallbacksAndMessages(null);
                    // 更新浮动窗口位置参数
                    endTime = System.currentTimeMillis();
//                    if (istalk) {
//                        return false;
//                    }
                    if (endTime - startTime > 200) {
                        Log.e("lee", "说话不准移动");
                        //llAudioRecord.setVisibility(View.VISIBLE);
                        if (getLongClickListener() != null) {
                            if (isCanTalk){
                                longClickListener.onTalkClick();
                            }
                        }
                        istalk = true;
                    } else {
                        startTime = System.currentTimeMillis();
                        mWmParams.x = (int) (x - mTouchStartX);
                        mWmParams.y = (int) (y - mTouchStartY);
                        mWindowManager.updateViewLayout(this, mWmParams);
                        Log.e("lee", "ACTION_MOVE");
                    }
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                handler.removeCallbacksAndMessages(null);
                llAudioRecord.setVisibility(View.INVISIBLE);
                if (istalk) {
                    if (getLongClickListener() != null) {
                        if (isCanTalk){
                            longClickListener.onUpClick();
                            ivAudioRecord.setBackgroundResource(R.drawable.ic_intercom_waiting);
                        }
                    }
                    istalk = false;
                }
                Log.e("lee", "ACTION_UP");
                break;
        }
        return true;
    }

    /**
     * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
     *
     * @param params 小悬浮窗的参数
     */
    public void setParams(WindowManager.LayoutParams params) {
        mWmParams = params;
    }

    public interface LongClickListener {
        void onUpClick();

        void onTalkClick();
    }

    // 对讲组准备中(讲话方)
    public void setAudioRecordPreparing() {
        llAudioRecord.setVisibility(View.VISIBLE);
    }

    // 对讲组我正在发言中(讲话方)
    public void setAudioRecordMeSpeaking() {
        llAudioRecord.setVisibility(View.VISIBLE);
    }

    // 对讲组弹出框开始记时(讲话方)
    public void setAudioRecordTimerShow() {
        llAudioRecord.setVisibility(View.VISIBLE);
    }

    // 对讲组弹出框开始记时(讲话方)
    public void setAudioRecordTimerStart() {
        llAudioRecord.setVisibility(View.VISIBLE);
        timerAudioRecord.setBase(SystemClock.elapsedRealtime());
        timerAudioRecord.start();
    }

    // 对讲组弹出框隐藏(讲话方)
    public void setAudioRecordTimerHide() {
        llAudioRecord.setVisibility(View.INVISIBLE);
        timerAudioRecord.stop();
        timerAudioRecord.setBase(SystemClock.elapsedRealtime());
    }

    // 设置是否能够开启对讲
    public void setCanTalk(boolean canTalk) {
        isCanTalk = canTalk;
        if (!isCanTalk){
            ivAudioRecord.setBackgroundResource(R.drawable.ic_intercom_prohibit);
        } else {
            ivAudioRecord.setBackgroundResource(R.drawable.ic_intercom_waiting);
        }
    }
}
