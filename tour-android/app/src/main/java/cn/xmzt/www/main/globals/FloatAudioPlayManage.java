package cn.xmzt.www.main.globals;

import android.app.Activity;
import android.widget.FrameLayout;

import cn.xmzt.www.view.floatview.audioplay.FloatViewManagerAudioPlay;
import cn.xmzt.www.view.floatview.audioplay.IFloatingViewMagnet;
import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * 音频播放管理器
 * @author Averysk
 */
public class FloatAudioPlayManage {

    public static FloatAudioPlayManage getInstance() {
        return InstanceHolder.instance;
    }

    private static class InstanceHolder {
        private final static FloatAudioPlayManage instance = new FloatAudioPlayManage();
    }

    public void initFloatView(){
        FloatViewManagerAudioPlay.get().add();
    }

    public void showFloatView(Activity activity){
        FloatViewManagerAudioPlay.get().attach(activity);
    }

    public void showFloatView(FrameLayout container){
        FloatViewManagerAudioPlay.get().attach(container);
    }

    public void showFloatView(Activity activity, String groupId, String userId){
        FloatViewManagerAudioPlay.get().detach(activity);
        FloatViewManagerAudioPlay.get().attach(activity);
        setTimerStart();
        setViewData(groupId, userId);
    }

    public void showFloatView(Activity activity, IMMessage imMessage){
        FloatViewManagerAudioPlay.get().detach(activity);
        FloatViewManagerAudioPlay.get().attach(activity);
        setTimerStart();
        setViewData(imMessage);
    }

    public void hideFloatView(Activity activity) {
        setTimerStop();
        FloatViewManagerAudioPlay.get().detach(activity);
    }

    public void setViewData(String groupId, String userId){
        FloatViewManagerAudioPlay.get().getView().setViewData(groupId, userId);
    }

    public void setViewData(IMMessage imMessage){
        FloatViewManagerAudioPlay.get().getView().setViewData(imMessage);
    }

    public void setTimerStart(){
        FloatViewManagerAudioPlay.get().getView().setTimerStart();
    }

    public void setTimerStop(){
        FloatViewManagerAudioPlay.get().getView().setTimerStop();
    }

    public void listenerFloatView(IFloatingViewMagnet magnetViewListener){
        FloatViewManagerAudioPlay.get().listener(magnetViewListener);
    }
}
