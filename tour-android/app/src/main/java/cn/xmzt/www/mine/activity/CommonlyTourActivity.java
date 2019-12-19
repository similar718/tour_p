package cn.xmzt.www.mine.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityCommonlyTourBinding;
import cn.xmzt.www.mine.event.TourEvent;
import cn.xmzt.www.mine.model.CommonlyTourViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author tanlei
 * @date 2019/8/14
 * @describe 常用出游人
 */

public class CommonlyTourActivity extends TourBaseActivity<CommonlyTourViewModel, ActivityCommonlyTourBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_commonly_tour;
    }

    @Override
    protected CommonlyTourViewModel createViewModel() {
        return new CommonlyTourViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getTourEvent(TourEvent event) {
        dataBinding.refreshLayout.autoRefresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
