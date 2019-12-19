package cn.xmzt.www.home.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivitySelectedLineBinding;
import cn.xmzt.www.home.bean.ArticleBean;
import cn.xmzt.www.home.vm.SelectedLineViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author tanlei
 * @date 2019/8/23
 * @describe 精选线路
 */

public class SelectedLineActivity extends TourBaseActivity<SelectedLineViewModel, ActivitySelectedLineBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_selected_line;
    }

    @Override
    protected SelectedLineViewModel createViewModel() {
        return new SelectedLineViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getArticleBean(ArticleBean bean) {
        viewModel.setBean(bean);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
