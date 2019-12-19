package cn.xmzt.www.home.activity;

import android.widget.LinearLayout;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityPopularityMustPlayBinding;
import cn.xmzt.www.home.bean.PopularityBean;
import cn.xmzt.www.home.event.IndexEvent;
import cn.xmzt.www.home.vm.PopularityMustPlayViewModel;
import cn.xmzt.www.utils.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author tanlei
 * @date 2019/8/22
 * @describe 人气必玩
 */

public class PopularityMustPlayActivity extends TourBaseActivity<PopularityMustPlayViewModel, ActivityPopularityMustPlayBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_popularity_must_play;
    }

    @Override
    protected PopularityMustPlayViewModel createViewModel() {
        return new PopularityMustPlayViewModel();
    }

    @Override
    protected void initData() {
        StatusBarUtil.setFullScreen(this);
        StatusBarUtil.setStatusBarLightMode(this,false);
        int statusBarHeight = StatusBarUtil.getStatusBarHeight(this);
        LinearLayout.LayoutParams mLayoutParams= (LinearLayout.LayoutParams) dataBinding.titleLayout.getLayoutParams();
        mLayoutParams.topMargin=statusBarHeight;
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getIndex(IndexEvent indexEvent) {
        viewModel.setCheckData(indexEvent.getIndex());
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getPopularity(PopularityBean bean) {
        viewModel.setBean(bean);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
