package cn.xmzt.www.selfdrivingtools.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseFragment;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.ToRouteListBus;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ActivityElectricGuideBinding;
import cn.xmzt.www.selfdrivingtools.activity.AKeySOSActivity;
import cn.xmzt.www.selfdrivingtools.event.PlayVoiceTypeEvent;
import cn.xmzt.www.manager.IntentManager;
import cn.xmzt.www.selfdrivingtools.viewmodel.ElectricGuideViewModel;
import cn.xmzt.www.utils.GPSUtils;
import cn.xmzt.www.utils.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * self driving tools
 */
public class ElectricGuideFragment extends BaseFragment<ElectricGuideViewModel, ActivityElectricGuideBinding> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_electric_guide;
    }

    @Override
    protected ElectricGuideViewModel createViewModel() {
        viewModel = new ElectricGuideViewModel();
        viewModel.smartTeamGroup.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String groupId) {
                if (TextUtils.isEmpty(groupId)){
                    IntentManager.getInstance().goSmartTeamActivity(getActivity());
                } else {
//                    if (GPSUtils.isOPen(getActivity())) {
                        IntentManager.getInstance().goSmartTeamMapActivity(getActivity(), groupId, false);
//                    } else {
//                        cn.xmzt.www.utils.ToastUtils.showText(getActivity(),"请打开位置权限/GPS位置信息");
//                    }
                }
            }
        });
        viewModel.setIView(this);
        return viewModel;
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            StatusBarUtil.setStatusBarLightMode(getActivity(),false);
        } else {
            setPlayAnim();
        }
    }
    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        StatusBarUtil.setStatusBarLightMode(getActivity(),false);
        dataBinding.setModel(viewModel);
        dataBinding.setActivity(this);
        setPlayAnim();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case cn.xmzt.www.R.id.rl_wisdom_work:// 智能出行
                //智能出行
                if(!TextUtils.isEmpty(TourApplication.getToken())){
                    viewModel.getSmartTeamGroup();
                }else {
                    IntentManager.getInstance().goSmartTeamActivity(view.getContext());
                }
//                EventBus.getDefault().post(new ToRouteListBus(2));
                break;
            case cn.xmzt.www.R.id.rl_wisdom: // 智慧景区导览
                IntentManager.getInstance().goWisdomGuideActivity(getActivity());
                break;
            case cn.xmzt.www.R.id.rl_sos: // 一键救援
                IntentManager.getInstance().startActivity(getActivity(), AKeySOSActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getOrderType(PlayVoiceTypeEvent event) {
        if (event.getType() == 0){ // 表示停止播放
        } else if (event.getType() == 1){ // 表示开始播放数据
        } else if (event.getType() == 2){ // 暂停中
        }
        setPlayAnim();
    }

    private void setPlayAnim(){
        if (Constants.mServiceIsStart){ // 开启动画
            dataBinding.playIv.setVisibility(View.VISIBLE);
            AnimationDrawable animationDrawable;
            animationDrawable = (AnimationDrawable) dataBinding.playIv.getBackground();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    animationDrawable.start();
                }
            });
        } else { // 停止动画
            dataBinding.playIv.setVisibility(View.INVISIBLE);
            AnimationDrawable animationDrawable;
            animationDrawable = (AnimationDrawable) dataBinding.playIv.getBackground();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    animationDrawable.stop();
                }
            });
        }
    }
}
