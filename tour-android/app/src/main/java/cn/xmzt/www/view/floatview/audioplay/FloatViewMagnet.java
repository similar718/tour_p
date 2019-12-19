package cn.xmzt.www.view.floatview.audioplay;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @ClassName FloatingMagnetView
 * @Description 磁力吸附悬浮窗
 * @Author Averysk
 */
public class FloatViewMagnet extends FrameLayout {

    private IFloatingViewMagnet mMagnetViewListener;

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
        setClickable(true);
    }

    protected void onClickEvent(View view) {
        if (mMagnetViewListener != null) {
            mMagnetViewListener.onClick(view);
        }
    }

    public void onRemove() {
        if (mMagnetViewListener != null) {
            mMagnetViewListener.onRemove(this);
        }
    }
}
