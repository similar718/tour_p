package cn.xmzt.www.view.floatview.intercom;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.ActivityUtils;

import cn.xmzt.www.R;
import cn.xmzt.www.intercom.activity.TeamRoomActivity;
import cn.xmzt.www.main.globals.TalkManage;
import cn.xmzt.www.utils.StatusBarUtil;
import cn.xmzt.www.view.floatview.utils.SystemUtils;

/**
 * @ClassName FloatingMagnetView
 * @Description 磁力吸附悬浮窗
 * @Author Averysk
 */
public class FloatViewMagnet extends FrameLayout {

    public static final int MARGIN_EDGE = 13;
    private float mOriginalX;
    private float mOriginalY;
    private float mOriginalRawX;
    private float mOriginalRawY;
    private float mOriginalTouchX;
    private float mOriginalTouchY;
    private IFloatingViewMagnet mMagnetViewListener;
    private static final int TOUCH_TIME_THRESHOLD = 150;//长按的时间阈值
    private long mTouchDownTime = 0;//按下的时间
    private long mDoubleTouchDownTime = 0;

    private MoveAnimator mMoveAnimator;
    private int mLeftRightAllMargin;//左右总间隙
    private int mScreenHeight;
    private int mStatusBarHeight;
    private int mNavigationBarHeight;
    private boolean isShowNavigationBar;

    private int mStatusWindowWidth;

    private boolean isHoldTalk = false; // 是否按住对讲说话了
    private boolean isCanTalk = true; // 是否能够对讲说话
    private boolean isTeamRoomMoveBottom = true; // 是否是对讲聊天页面移动到底部
    private boolean isMoved = false; // 是否已经移动
    private boolean isDown = false; // 是否按下

    public void setMagnetViewListener(IFloatingViewMagnet magnetViewListener) {
        this.mMagnetViewListener = magnetViewListener;
    }

    public FloatViewMagnet(Context context) {
        this(context, null);
    }

    public FloatViewMagnet(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatViewMagnet(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mMoveAnimator = new MoveAnimator();
        mStatusBarHeight = SystemUtils.getStatusBarHeight(getContext());
        mStatusWindowWidth = SystemUtils.getScreenWidth(getContext());
        mNavigationBarHeight=SystemUtils.getNavBarHeight(getContext());
        isShowNavigationBar=StatusBarUtil.hasNavBar(getContext());
        setClickable(true);
        updateSize();
    }
    private Handler handlerClick = new Handler();
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event == null) {
            return false;
        }
        Log.d("shiyong", "event.getAction="+event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("shiyong", "ACTION_DOWN手指按下了啦");
                downClicEvent(event);
                break;
            case MotionEvent.ACTION_MOVE:
                moveClicEvent(event);
                break;
            case MotionEvent.ACTION_UP:
                Log.d("shiyong", "ACTION_UP手指抬起了啦");
                upClickEvent(event);
                break;
            default:
                Log.d("shiyong", "default ACTION_UP手指抬起了啦");
                upClickEvent(event);
                break;
        }
        return true;
    }

    //按下事件
    private void downClicEvent(MotionEvent event) {
        isDown=true;
        isMoved=false;
        // 获取当前按下的状态
        changeOriginalTouchParams(event);
        // 更新当前按下的大小
        updateSize();
        // 停止移动
        mMoveAnimator.stop();
        if (!isCanTalk||(!TalkManage.isMeBroadcast&&TalkManage.isBroadcast)){
            return;
        }else {
            Log.d("lee", "手指按下了啦");
            setTalkStatus(3);
            // 开启倒计时长按监听
            handlerClick.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isMoved&&!isHoldTalk&&isDown) {
                        Log.d("shiyong", "postDelayed 按住很久了,那就开始说话吧");
                        onTalkClickEvent();
                        setTalkStatus(3);
                    }
                }
            }, TOUCH_TIME_THRESHOLD);
        }
    }

    // 按住事件
    private void moveClicEvent(MotionEvent event) {
        if (isHoldTalk){
            Log.d("shiyong", "ACTION_MOVE正在说话中,就不要移动了");
            return;
        }
        //图标移动的逻辑在这里
        float moveStartX = event.getX();
        float moveStartY = event.getY();
        if (Math.abs(mOriginalTouchX - moveStartX) > 3 && Math.abs(mOriginalTouchY - moveStartY) > 3) { // 如果移动量大于3才移动
            isMoved=true;
            Log.d("shiyong", "手指移动量大于3才移动哦");
            Log.d("shiyong", "ACTION_MOVE手指按下移动了啦");
            // 更新移动位置
            updateViewPosition(event);
        }else {
            if (isOnLongClickEvent()&&!isMoved) {
                Log.d("shiyong", "按住很久了,那就开始说话吧");
                if (!isCanTalk||(!TalkManage.isMeBroadcast&&TalkManage.isBroadcast)){
                    return;
                }else {
                    onTalkClickEvent();
                    setTalkStatus(3);
                }
            }
        }
    }

    // 抬起事件
    private void upClickEvent(MotionEvent event) {
        isDown=false;
        Log.d("yong", "抬起事件,isDown="+isDown);
        handlerClick.removeCallbacksAndMessages(null);
        if (isNearestBottom()){
            moveToEdgeBottom();
        }
        if (isHoldTalk){
            Log.d("yong", "已经开始说话后的抬起事件,那就结束说话吧");
            onUpClickEvent();
        } else {
            if (isOnDoubleClickEvent()) {
                Log.d("yong", "还没开始说话, 就点击抬起了两次事件,那就做为双击事件吧");
                onDoubleClickEvent();
                mDoubleTouchDownTime = 0;
            }
            else {
                mDoubleTouchDownTime =  System.currentTimeMillis();
                if (isOnClickEvent()) {
                    // 开启倒计时长按监听
                    Log.d("lee", "还没开始说话, 就抬起事件,那就做为单击事件吧");
                    onClickEvent();
                }
            }
        }
        isHoldTalk = false;
        if (!isCanTalk){
            return;
        }
        setTalkStatus(4);
    }

    protected void onClickEvent() {
        if (mMagnetViewListener != null) {
            mMagnetViewListener.onClick(this);
        }
    }

    protected void onDoubleClickEvent() {
        if (mMagnetViewListener != null&&!TalkManage.isBroadcast) {
            mMagnetViewListener.onDoubleClick(this);
        }
    }

    protected void onTalkClickEvent() {
        if (mMagnetViewListener != null) {
            handlerClick.removeCallbacksAndMessages(null);
            mMagnetViewListener.onTalkClick(this);
        }
        isHoldTalk = true;
    }

    protected void onUpClickEvent() {
        if (mMagnetViewListener != null) {
            mMagnetViewListener.onUpClick(this);
        }
        isHoldTalk = false;
    }

    protected boolean isOnClickEvent() {
        return System.currentTimeMillis() - mTouchDownTime < TOUCH_TIME_THRESHOLD;
    }

    protected boolean isOnDoubleClickEvent() {
        return System.currentTimeMillis() - mDoubleTouchDownTime < TOUCH_TIME_THRESHOLD * 2;
    }

    protected boolean isOnLongClickEvent() {
        return System.currentTimeMillis() - mTouchDownTime > TOUCH_TIME_THRESHOLD;
    }

    private void updateViewPosition(MotionEvent event) {
        setX(mOriginalX + event.getRawX() - mOriginalRawX);
        // 限制不可超出屏幕高度
        float desY = mOriginalY + event.getRawY() - mOriginalRawY;
        if (desY < mStatusBarHeight) {
            desY = mStatusBarHeight;
        }
        if (desY > mScreenHeight - getHeight() * 4 / 3) {
            desY = (float)mScreenHeight - (float)(getHeight() * 4 / 3);
        }
        setY(desY);
    }

    private void changeOriginalTouchParams(MotionEvent event) {
        mOriginalX = getX();
        mOriginalY = getY();
        mOriginalRawX = event.getRawX();
        mOriginalRawY = event.getRawY();
        mOriginalTouchX = event.getX();
        mOriginalTouchY = event.getY();
        mTouchDownTime = System.currentTimeMillis();
    }

    protected void updateSize() {
        int width=getWidth();
        if(width==0){
            width=getResources().getDimensionPixelOffset(R.dimen.dp_100);
        }
        mLeftRightAllMargin = (SystemUtils.getScreenWidth(getContext()) - width);
        mScreenHeight = SystemUtils.getScreenHeight(getContext());
    }

    public void moveToEdge() {
        float moveDistance = isNearestLeft() ? MARGIN_EDGE : mLeftRightAllMargin - MARGIN_EDGE;
        mMoveAnimator.start(moveDistance, getY());
    }

    public void moveToEdgeBottom() {
        int height=getHeight();
        int width=getWidth();
        if(height==0){
            height=getResources().getDimensionPixelOffset(R.dimen.dp_100);
            width=height;
        }
        int x = (mStatusWindowWidth - width) / 2;
        int y = mScreenHeight - height - getResources().getDimensionPixelOffset(R.dimen.dp_70);
        if(ActivityUtils.getTopActivity() instanceof TeamRoomActivity){
            y = mScreenHeight - height - getResources().getDimensionPixelOffset(R.dimen.dp_10);
            isTeamRoomMoveBottom=true;
        }else {
            isTeamRoomMoveBottom=false;
        }
        mMoveAnimator.start(x, y);
    }

    protected boolean isNearestLeft() {
        int middle = mLeftRightAllMargin / 2;
        return getX() < middle;
    }
    protected boolean isNearestBottom() {
//        return getY() > mScreenHeight - mStatusBarHeight - getHeight() * 2;
        if(!isShowNavigationBar){
            return getY() > mScreenHeight - mStatusBarHeight+mNavigationBarHeight - getHeight() * 2;
        }else {
            return getY() > mScreenHeight - mStatusBarHeight - getHeight() * 2;
        }
    }

    /*protected boolean isNearestBottom() {
        if(!isShowNavigationBar){
            int bottom=getHeight();
            return mScreenHeight < bottom+mStatusBarHeight+mNavigationBarHeight;
        }else {
            int bottom=getBottom();
            return mScreenHeight<bottom+ mStatusBarHeight;
        }
    }*/

    public void onRemove() {
        if (mMagnetViewListener != null) {
            mMagnetViewListener.onRemove(this);
        }
    }

    // 设置是否能够开启对讲
    public void setCanTalk(boolean canTalk) {
        isCanTalk = canTalk;
    }

    // 是否能够开启对讲
    public boolean getCanTalk() {
        return isCanTalk ;
    }

    // 设置是否需要自动移动到底部
    public void setNeedMoveBottom(boolean needMoveBottom) {
        if(needMoveBottom){
            moveToEdgeBottom();
        }else {
            if(isTeamRoomMoveBottom){//如果对讲聊天页面移动到底部其他页面调整位置
                moveToEdgeBottom();
            }
        }

    }

    /**
     * 设置当前对讲按钮类型(子类继承实现具体操作)
     * @param type
     *              1:正常状态, 能够对讲, 等待对讲
     *              2:禁止对讲, 不准对讲, 等待解锁
     *              3:按下事件, 更替对讲按钮背景
     *              4:抬起事件, 更新对讲按钮背景
     */
    public void setTalkStatus(int type){}

    protected class MoveAnimator implements Runnable {

        private Handler handler = new Handler(Looper.getMainLooper());
        private float destinationX;
        private float destinationY;
        private long startingTime;

        void start(float x, float y) {
            this.destinationX = x;
            this.destinationY = y;
            startingTime = System.currentTimeMillis();
            handler.post(this);
        }

        @Override
        public void run() {
            if (getRootView() == null || getRootView().getParent() == null) {
                return;
            }
            float progress = Math.min(1, (System.currentTimeMillis() - startingTime) / 400f);
            float deltaX = (destinationX - getX()) * progress;
            float deltaY = (destinationY - getY()) * progress;
            move(deltaX, deltaY);
            if (progress < 1) {
                handler.post(this);
            }
        }

        private void stop() {
            handler.removeCallbacks(this);
        }
    }

    private void move(float deltaX, float deltaY) {
        setX(getX() + deltaX);
        setY(getY() + deltaY);
    }

    // 根据手机的分辨率从 dip 的单位 转成为 px(像素)
    public int dip2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
