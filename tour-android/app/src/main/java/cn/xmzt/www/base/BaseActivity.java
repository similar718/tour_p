package cn.xmzt.www.base;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.xmzt.www.dialog.LoadingDialig;
import cn.xmzt.www.intercom.activity.TeamRoomActivity;
import cn.xmzt.www.main.activity.SchemeActivity;
import cn.xmzt.www.main.globals.FloatAudioPlayManage;
import cn.xmzt.www.main.globals.FloatIntercomManage;
import cn.xmzt.www.main.globals.TalkManage;
import cn.xmzt.www.share.ShareFunction;
import cn.xmzt.www.suspension.FloatWindowManager;
import cn.xmzt.www.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tanlei
 * @date 2019/7/27
 * @describe
 */

public class BaseActivity extends AppCompatActivity implements IView {
    public static List<BaseActivity> list = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list.add(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(this instanceof SchemeActivity){
        }else {
            if (TalkManage.isShowTalk){
                FloatIntercomManage.getInstance().showFloatView(this);
                if (this instanceof TeamRoomActivity) {
                    FloatIntercomManage.getInstance().setNeedMoveBottom(true);
                }else {
                    FloatIntercomManage.getInstance().setNeedMoveBottom(false);
                }
            }else {
                FloatIntercomManage.getInstance().hideFloatView(this);
            }
            if (TalkManage.isShowPlay){
                FloatAudioPlayManage.getInstance().showFloatView(this);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FloatWindowManager.getInstance().updateShowandHide(true);
        if(ShareFunction.getInstance().shareing){
            ShareFunction.getInstance().shareing=false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        FloatWindowManager.getInstance().updateShowandHide(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FloatIntercomManage.getInstance().hideFloatView(this);
        FloatAudioPlayManage.getInstance().hideFloatView(this);
    }

    /**
     * 不带数据的界面跳转方法
     *
     * @param c 需要跳转至的界面
     */
    protected void startToActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    public LoadingDialig mLoadingDialig;

    @Override
    public void showLoading() {
        if (mLoadingDialig == null) {
            mLoadingDialig = new LoadingDialig(this, "正在加载");
        }
        mLoadingDialig.show();
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialig != null) {
            mLoadingDialig.dismiss();
        }
    }

    @Override
    public void showLoadFail(String msg) {
        if(TextUtils.isEmpty(msg)){
            return;
        }
        if(msg.contains("timeout")){
            ToastUtils.showText(this,"网络连接超时");
        }else {
            ToastUtils.showText(this,msg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list.remove(this);
    }
}
