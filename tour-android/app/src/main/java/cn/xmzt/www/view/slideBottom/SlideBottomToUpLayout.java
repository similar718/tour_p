package cn.xmzt.www.view.slideBottom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

import cn.xmzt.www.R;

/**
 * 这个不行 因为暂用就界面的透明部分 不能这样使用
 */
public class SlideBottomToUpLayout extends LinearLayout {

    // 设置控件的滑动事件
    public void setShortSlideListener(ShortSlideListener listener) {
        this.shortSlideListener = listener;
    }

    private ShortSlideListener shortSlideListener;

    private int downY;

    private int moveY;

    private int movedDis;

    private int movedMaxDis;

    private int movedMiddleDis;

    private int mShowPosition = 0;// 1 停留在中间部分 2 停留在顶部 0 停留在开始地方

    private View childView;

    private float hideWeight = 0.25f;

    private int slide_type = 0; // 当前滑动的状态  控件停留在哪里 1 停留在中间部分 2 停留在顶部 0 停留在开始地方

    public void setHideWeight(float hideWeight) {
        if (hideWeight <= 0 || hideWeight > 1)
            throw new IllegalArgumentException("hideWeight should belong (0f,1f]");
        this.hideWeight = hideWeight;
    }

    private Scroller mScroller;

    private boolean arriveTop = false;

    private boolean arriveMiddle = false;

    // 开始显示的高度
    private float visibilityHeight;

    public void setVisibilityHeight(float visibilityHeight) {
        this.visibilityHeight = visibilityHeight;
    }

    public SlideBottomToUpLayout(@NonNull Context context) {
        super(context);
    }

    public SlideBottomToUpLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public SlideBottomToUpLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }


    private void initAttrs(Context context, AttributeSet attrs) {
        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SlideBottomLayout);
        // 获取设置的属性信息
        visibilityHeight = ta.getDimension(R.styleable.SlideBottomLayout_handler_height, 0);
        ta.recycle();

        initConfig(context);
    }

    /**
     * 设置当前控件的背景颜色 TODO 当前默认的是透明的背景
     * @param context
     */
    private void initConfig(Context context) {
        if (mScroller == null)
            mScroller = new Scroller(context);
        this.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 0 || getChildAt(0) == null) {
            throw new RuntimeException("there have no child-View in the SlideBottomLayout！");
        }
        if (getChildCount() > 1) {
            throw new RuntimeException("there just alow one child-View in the SlideBottomLayout!");
        }
        childView = getChildAt(0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取整体显示最高高度显示大小
        movedMaxDis = (int) (childView.getMeasuredHeight() - visibilityHeight);
        // 获取整体显示中间高度显示大小
        movedMiddleDis = (int) (childView.getMeasuredHeight() - visibilityHeight)/2;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        // 判断当前是显示到界面的中间位置 还是显示到界面的顶部位置
        if (mShowPosition == 1 ) {
            childView.layout(0, movedMiddleDis, childView.getMeasuredWidth(), childView.getMeasuredHeight() + movedMiddleDis);
        } else {
            childView.layout(0, movedMaxDis, childView.getMeasuredWidth(), childView.getMeasuredHeight() + movedMaxDis);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float dy = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (touchActionDown(dy))
                    return true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (touchActionMove(dy))
                    return true;
                break;
            case MotionEvent.ACTION_UP:
                if (touchActionUp(dy))
                    return true;
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller == null)
            mScroller = new Scroller(getContext());
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }

    // 手指抬起的事件操作
    public boolean touchActionUp(float eventY) {
        // 判断当前 movedDis 值
        if (movedDis > movedMaxDis * hideWeight) {
            switchVisible();
        } else {
            if (shortSlideListener != null) {
                shortSlideListener.onShortSlide(eventY);
            } else {
                hide();
            }
        }
        return true;
    }

    public boolean touchActionMove(float eventY) {
        moveY = (int) eventY;
        //the dy is sum of the move distance,  the value > 0 means scroll up, the value < 0 means scroll down.
        final int dy = downY - moveY;
        if (dy > 0) {               //scroll up
            movedDis += dy;
            // 判断当前在哪里显示
            if (arriveMiddle){ // 当前显示在中间
                // 大于 就显示到顶部
                if (movedDis > movedMiddleDis)
                    movedDis = movedMiddleDis;
                // 小于显示在中间
                if (movedDis < movedMiddleDis) {
                    scrollBy(0, dy);
                    downY = moveY;
                    return true;
                }
            } else { // 从底部开始往上面滑动
                if (movedDis > movedMaxDis)
                    movedDis = movedMaxDis;

                if (movedDis > movedMiddleDis)
                    movedDis = movedMiddleDis;

                if (movedDis < movedMiddleDis) {
                    scrollBy(0, dy);
                    downY = moveY;
                    return true;
                }
            }
        } else {                //scroll down
            movedDis += dy;
            // 判断当前是在顶部  还是在中部
            if (arriveMiddle){ // 判断在中间位置
                if (movedDis < 0) movedDis = 0;
            } else if (arriveTop){ // 顶部
                if (movedDis < 0 && movedDis < -movedMiddleDis) movedDis = 0;
                else movedDis = movedMiddleDis;
            }
            if (movedDis > 0) {
                scrollBy(0, dy);
            }
            downY = moveY;
            return true;
        }
        return false;
    }

    // 手指按下的操作
    public boolean touchActionDown(float eventY) {
        downY = (int) eventY;
        //Whether custom this gesture.
        // 当前界面没有显示在顶部 并且滑动小于中间的位置
        if (!arriveTop && downY < movedMiddleDis){
            return false;
        } else
            return true;
    }

    public void show() {
        scroll2TopImmediate();
    }

    /**
     * 显示在中间位置
     */
    public void showCenter() {
        scroll2MiddleImmediate();
    }


    public void hide() {
        scroll2BottomImmediate();
    }


    public boolean switchVisible() {
        if (arriveTop()){ // 显示在顶部
            if (movedDis == movedMiddleDis){
                showCenter();
                return true;
            } else if (movedDis == 0){
                hide();
                return false;
            } else if (movedDis == movedMaxDis){
                show();
                return true;
            }
        } else{
            if (movedDis == movedMiddleDis){
                showCenter();
                return true;
            } else if (movedDis == 0){
                hide();
                return false;
            } else if (movedDis == movedMaxDis){
                show();
                return true;
            }
        }
//
//        if (arriveTop())
//            hide();
//        else
//            show();
        return arriveTop();
    }

    public boolean arriveTop() {
        return this.arriveTop;
    }

    public boolean arriveMiddle() {
        return this.arriveMiddle;
    }

    public void scroll2TopImmediate() {
        mScroller.startScroll(0, getScrollY(), 0, (movedMaxDis - getScrollY()));
        invalidate();
        movedDis = movedMaxDis;
        arriveMiddle = false;
        arriveTop = true;
        mShowPosition = 2;
    }

    public void scroll2MiddleImmediate() {
        mScroller.startScroll(0, getScrollY(), 0, (movedMiddleDis - getScrollY()));
        invalidate();
        movedDis = movedMiddleDis;
        arriveMiddle = true;
        arriveTop = false;
        mShowPosition = 1;
    }

    public void scroll2BottomImmediate() {
        mScroller.startScroll(0, getScrollY(), 0, -getScrollY());
        postInvalidate();
        movedDis = 0;
        arriveTop = false;
        arriveMiddle = false;
        mShowPosition = 0;
    }
}
