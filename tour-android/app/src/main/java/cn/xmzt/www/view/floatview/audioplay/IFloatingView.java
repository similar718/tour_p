package cn.xmzt.www.view.floatview.audioplay;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by Averysk
 */

public interface IFloatingView {

    FloatViewManagerAudioPlay remove();

    FloatViewManagerAudioPlay add();

    FloatViewManagerAudioPlay attach(Activity activity);

    FloatViewManagerAudioPlay attach(FrameLayout container);

    FloatViewManagerAudioPlay detach(Activity activity);

    FloatViewManagerAudioPlay detach(FrameLayout container);

    FloatView getView();

    FloatViewManagerAudioPlay layoutParams(ViewGroup.LayoutParams params);

    FloatViewManagerAudioPlay listener(IFloatingViewMagnet magnetViewListener);

}
