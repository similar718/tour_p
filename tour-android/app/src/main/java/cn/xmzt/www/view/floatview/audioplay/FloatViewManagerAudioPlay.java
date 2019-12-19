package cn.xmzt.www.view.floatview.audioplay;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.core.view.ViewCompat;

import cn.xmzt.www.view.floatview.utils.EnContext;


/**
 * 音频播放
 * @ClassName FloatViewManagerAudioPlay
 * @Description 悬浮窗管理器
 * @Author Averysk
 */
public class FloatViewManagerAudioPlay implements IFloatingView {

    private FloatView mEnFloatingView;
    private static volatile FloatViewManagerAudioPlay mInstance;
    private FrameLayout mContainer;

    private FloatViewManagerAudioPlay() {
    }

    public static FloatViewManagerAudioPlay get() {
        if (mInstance == null) {
            synchronized (FloatViewManagerAudioPlay.class) {
                if (mInstance == null) {
                    mInstance = new FloatViewManagerAudioPlay();
                }
            }
        }
        return mInstance;
    }

    @Override
    public FloatViewManagerAudioPlay remove() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (mEnFloatingView == null) {
                    return;
                }
                if (ViewCompat.isAttachedToWindow(mEnFloatingView) && mContainer != null) {
                    mContainer.removeView(mEnFloatingView);
                }
                mEnFloatingView = null;
            }
        });
        return this;
    }

    private void ensureMiniPlayer(Context context) {
        synchronized (this) {
            if (mEnFloatingView != null) {
                return;
            }
            mEnFloatingView = new FloatView(context.getApplicationContext());
            mEnFloatingView.setLayoutParams(getParams());
            addViewToWindow(mEnFloatingView);
        }
    }

    @Override
    public FloatViewManagerAudioPlay add() {
        ensureMiniPlayer(EnContext.get());
        return this;
    }

    @Override
    public FloatViewManagerAudioPlay attach(Activity activity) {
        attach(getActivityRoot(activity));
        return this;
    }

    @Override
    public FloatViewManagerAudioPlay attach(FrameLayout container) {
        if (container == null || mEnFloatingView == null) {
            mContainer = container;
            return this;
        }
        if (mEnFloatingView.getParent() == container) {
            return this;
        }
        if (mContainer != null && mEnFloatingView.getParent() == mContainer) {
            mContainer.removeView(mEnFloatingView);
        }
        mContainer = container;
        container.addView(mEnFloatingView);
        return this;
    }

    @Override
    public FloatViewManagerAudioPlay detach(Activity activity) {
        detach(getActivityRoot(activity));
        return this;
    }

    @Override
    public FloatViewManagerAudioPlay detach(FrameLayout container) {
        if (mEnFloatingView != null && container != null && ViewCompat.isAttachedToWindow(mEnFloatingView)) {
            container.removeView(mEnFloatingView);
        }
        if (mContainer == container) {
            mContainer = null;
        }
        return this;
    }

    @Override
    public FloatView getView() {
        return mEnFloatingView;
    }

    @Override
    public FloatViewManagerAudioPlay layoutParams(ViewGroup.LayoutParams params) {
        if (mEnFloatingView != null) {
            mEnFloatingView.setLayoutParams(params);
        }
        return this;
    }

    @Override
    public FloatViewManagerAudioPlay listener(IFloatingViewMagnet magnetViewListener) {
        if (mEnFloatingView != null) {
            mEnFloatingView.setMagnetViewListener(magnetViewListener);
        }
        return this;
    }

    private void addViewToWindow(final FloatView view) {
        if (mContainer == null) {
            return;
        }
        mContainer.addView(view);
    }

    private FrameLayout.LayoutParams getParams() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, params.bottomMargin);
        return params;
    }

    private FrameLayout getActivityRoot(Activity activity) {
        if (activity == null) {
            return null;
        }
        try {
            return (FrameLayout) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}