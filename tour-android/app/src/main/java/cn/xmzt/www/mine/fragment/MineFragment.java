package cn.xmzt.www.mine.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseFragment;
import cn.xmzt.www.databinding.FragmentMineBinding;
import cn.xmzt.www.dialog.GuideDialog;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.mine.event.CashSuccessEvent;
import cn.xmzt.www.mine.event.ImagePhotoEvent;
import cn.xmzt.www.mine.event.LogOutEvent;
import cn.xmzt.www.mine.event.LoginEvent;
import cn.xmzt.www.mine.event.NicknameEvent;
import cn.xmzt.www.mine.event.RefreshOrderEvent;
import cn.xmzt.www.mine.event.RefreshOrderReadEvent;
import cn.xmzt.www.mine.event.SignEvent;
import cn.xmzt.www.mine.event.UnreadMessageEvent;
import cn.xmzt.www.mine.model.MineViewModel;
import cn.xmzt.www.utils.SPUtils;
import cn.xmzt.www.utils.StatusBarUtil;


public class MineFragment extends BaseFragment<MineViewModel, FragmentMineBinding> {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected MineViewModel createViewModel() {
        return new MineViewModel();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            StatusBarUtil.setStatusBarLightMode(getActivity(), false);
        }
        if (hidden){
            if (viewModel.bean == null){
                viewModel.getUserBasicInfo();
            }
        }
    }

    @Override
    protected void initData() {
        StatusBarUtil.setStatusBarLightMode(getActivity(), false);
        int statusBarHeight = StatusBarUtil.getStatusBarHeight(getContext());

        FrameLayout.LayoutParams mLayoutParams = (FrameLayout.LayoutParams) dataBinding.notLoggedInInfoRl.getLayoutParams();
        mLayoutParams.topMargin = statusBarHeight;
        FrameLayout.LayoutParams mLayoutParams1 = (FrameLayout.LayoutParams) dataBinding.userInfoRl.getLayoutParams();
        mLayoutParams1.topMargin = statusBarHeight;

        EventBus.getDefault().register(this);
        dataBinding.setModel(viewModel);
        viewModel.setMineFragment(this);
        viewModel.getUserBasicInfo();
        Glide.with(this).load(R.drawable.mine_bg_sign_animation).into(dataBinding.ivSign);

        if (!SPUtils.getBoolean("guideLogin", false)) {
            GuideDialog dialog = new GuideDialog(getActivity());
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_login_guide, null);
            ImageView ivKnow = view.findViewById(R.id.iv_i_know);
            ivKnow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.setContentView(view);
            dialog.show();
            SPUtils.putBoolean("guideLogin", true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void changeNickName(NicknameEvent nicknameEvent) {
        dataBinding.nameTv.setText(nicknameEvent.getNickName());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            viewModel.getUserBasicInfo();
            // 相当于onResume()方法
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void logOutEvent(LogOutEvent event) {
        dataBinding.tv1.setVisibility(View.GONE);
        dataBinding.tv2.setVisibility(View.GONE);
        dataBinding.tv3.setVisibility(View.GONE);
        dataBinding.tv4.setVisibility(View.GONE);
        dataBinding.setModel(viewModel);
        viewModel.setMineFragment(this);
        viewModel.getUserBasicInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshOrder(RefreshOrderEvent event) {
        viewModel.getUserBasicInfo();
    }
    @Subscribe
    public void refreshOrderRead(RefreshOrderReadEvent event) {
        if(viewModel.isHaveOrderUnreadCount){
            viewModel.getUserBasicInfo();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginEvent(LoginEvent event) {
        dataBinding.setModel(viewModel);
        viewModel.setMineFragment(this);
        viewModel.getUserBasicInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void SignInSuccessEvent(SignEvent event) {
        viewModel.getUserBasicInfo();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onUnreadMessage(@NonNull UnreadMessageEvent event) {
        viewModel.unreadCount=event.getUnreadCount();
        if(event.getUnreadCount()>0){
            dataBinding.tvMsgNum.setVisibility(View.VISIBLE);
            dataBinding.tvMsgNum.setText(""+event.getUnreadCount());
        }else{
            dataBinding.tvMsgNum.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void changePhoto(ImagePhotoEvent event) {
        GlideUtil.loadImgRadius(dataBinding.avatarIv, 0, event.getImageUrl());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cashSuccess(CashSuccessEvent event) {
        viewModel.getUserBasicInfo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
