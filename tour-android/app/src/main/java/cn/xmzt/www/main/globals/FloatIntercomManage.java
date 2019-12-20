package cn.xmzt.www.main.globals;

import android.app.Activity;

import cn.xmzt.www.view.floatview.intercom.FloatView;
import cn.xmzt.www.view.floatview.intercom.FloatViewManager;
import cn.xmzt.www.view.floatview.intercom.IFloatingViewMagnet;

public class FloatIntercomManage {

    public static FloatIntercomManage getInstance() {
        return InstanceHolder.instance;
    }

    private static class InstanceHolder {
        private final static FloatIntercomManage instance = new FloatIntercomManage();
    }

    public void initFloatView(){
        FloatViewManager.get().add();
    }

    public void showFloatView(Activity activity){
        FloatViewManager.get().attach(activity);
    }

    public void hideFloatView(Activity activity) {
        FloatViewManager.get().detach(activity);
    }

    public void listenerFloatView(IFloatingViewMagnet listener){
        FloatViewManager.get().listener(listener);
    }

    public void setNeedMoveBottom(boolean needMoveBottom){
        FloatViewManager.get().getView().setNeedMoveBottom(needMoveBottom);
    }

    public FloatView getView(){
        return FloatViewManager.get().getView();
    }

    public void setTalkStatus(int type){
        FloatViewManager.get().getView().setTalkStatus(type);
    }
    public void setCanTalk(boolean canTalk){
        FloatViewManager.get().getView().setCanTalk(canTalk);
    }
}
