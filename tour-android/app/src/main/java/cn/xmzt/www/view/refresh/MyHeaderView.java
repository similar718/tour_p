package cn.xmzt.www.view.refresh;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import cn.xmzt.www.R;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.util.SmartUtil;

/**
 * SmartRefreshLayout的自定义Footer
 */
public class MyHeaderView extends LinearLayout implements RefreshHeader {
    protected int mBackgroundColor;
    private ImageView mProgressView;//刷新动画视图
    private AnimationDrawable refreshingAnim;

    public MyHeaderView(Context context) {
        this(context, null, 0);
    }

    public MyHeaderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        setGravity(Gravity.CENTER_HORIZONTAL);
        mProgressView = new ImageView(context);
        mProgressView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        LayoutParams mLayoutParams=new LayoutParams(SmartUtil.dp2px(60),SmartUtil.dp2px(60));
        mProgressView.setLayoutParams(mLayoutParams);
        mProgressView.setImageResource(R.drawable.anim_pull_refreshing_2);
        setOrientation(LinearLayout.VERTICAL);
        addView(new View(context),SmartUtil.dp2px(60),SmartUtil.dp2px(60));
        addView(mProgressView);
    }
    @NonNull
    public View getView() {
        return this;//真实的视图就是自己，不能返回null
    }
    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return mSpinnerStyle = SpinnerStyle.Translate;
    }

    protected SpinnerStyle mSpinnerStyle;
    @Override
    public void onStartAnimator(RefreshLayout layout, int headHeight, int maxDragHeight) {
    }
    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        return 500;//延迟500毫秒之后再弹回
    }
    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case None:
                Log.e("shiyong" ,"None");
            case PullDownToRefresh://下拉和回弹
                Log.e("shiyong" ,"PullDownToRefresh");
                mProgressView.setImageResource(R.drawable.anim_pull_refreshing_2);

                break;
            case Refreshing:
                Log.e("shiyong" ,"Refreshing");
                mProgressView.setImageResource(R.drawable.anim_pull_refreshing);
                refreshingAnim = (AnimationDrawable) mProgressView.getDrawable();
                refreshingAnim.start();
                break;
            case ReleaseToRefresh://下拉到
                Log.e("shiyong" ,"ReleaseToRefresh");
                break;
        }
    }
    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
        mRefreshKernel = kernel;
        mRefreshKernel.requestDrawBackgroundFor(this, mBackgroundColor);
    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
        super.setBackgroundColor(Color.parseColor("#24A4FF"));
    }
    public MyHeaderView setPrimaryColorId(@ColorRes int colorId) {
        final View thisView = this;
        setPrimaryColor(ContextCompat.getColor(thisView.getContext(), colorId));
        return self();
    }
    @SuppressWarnings("unchecked")
    protected MyHeaderView self() {
        return this;
    }

    protected RefreshKernel mRefreshKernel;
    protected boolean mSetAccentColor;
    protected boolean mSetPrimaryColor;

    @Override
    public void setPrimaryColors(@ColorInt int ... colors) {
        if (colors.length > 0) {
            final View thisView = this;
            if (!(thisView.getBackground() instanceof BitmapDrawable) && !mSetPrimaryColor) {
                setPrimaryColor(colors[0]);
                mSetPrimaryColor = false;
            }
            if (!mSetAccentColor) {
                if (colors.length > 1) {
                    setAccentColor(colors[1]);
//                } else {
//                    setAccentColor(colors[0] == 0xffffffff ? 0xff666666 : 0xffffffff);
                }
                mSetAccentColor = false;
            }
        }
    }
    public MyHeaderView setAccentColor(@ColorInt int accentColor) {
        mSetAccentColor = true;
        return self();
    }
    public MyHeaderView setPrimaryColor(@ColorInt int primaryColor) {
        mSetPrimaryColor = true;
        mBackgroundColor = primaryColor;
        if (mRefreshKernel != null) {
            mRefreshKernel.requestDrawBackgroundFor(this, primaryColor);
        }
        return self();
    }
}
