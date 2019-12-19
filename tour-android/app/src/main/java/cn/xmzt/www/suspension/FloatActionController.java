package cn.xmzt.www.suspension;

import android.graphics.Bitmap;
import android.view.View;

/**
 * @author tanlei
 * @date 2019/9/4
 * @describe
 */

public class FloatActionController {
    private FloatActionController() {

    }
    // 静态内部类
    private static class LittleMonkProviderHolder {
        private static final FloatActionController sInstance = new FloatActionController();
    }

    public static FloatActionController getInstance() {
        return LittleMonkProviderHolder.sInstance;
    }

    public void showRecordView() {
        FloatWindowManager.getInstance().showRecordView();
    }

    public void hideRecordView() {
        FloatWindowManager.getInstance().hideRecordView();
    }

    public void showAudioRecordTimer() {
        FloatWindowManager.getInstance().setAudioRecordShow();
    }

    public void startAudioRecordTimer() {
        FloatWindowManager.getInstance().setAudioRecordStartTimer();
    }

    public void hideAudioRecordTimer() {
        FloatWindowManager.getInstance().setAudioRecordHide();
    }

    public void setAudioRecordCanTalk(boolean canTalk) {
        FloatWindowManager.getInstance().setAudioRecordCanTalk(canTalk);
    }

    public void showNavigationView(View view) {
        FloatWindowManager.getInstance().showNavigationView(view);
    }

    public void updateNavigationView(Bitmap bitmap, int icontype, String content) {
        FloatWindowManager.getInstance().updateNavigationView(bitmap,icontype,content);
    }

    public void updateShowAndHide(boolean content) {
        FloatWindowManager.getInstance().updateShowandHide(content);
    }

    public void hideNavigationView() {
        FloatWindowManager.getInstance().hideNavigationView();
    }

    public void showMessageView(View view){
        FloatWindowManager.getInstance().showMessageView(view);
    }

    public void hideMessageView(){
        FloatWindowManager.getInstance().hideMessageView();
    }

}
