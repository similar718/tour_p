package cn.xmzt.www.mine.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityBrowseHistoryBinding;
import cn.xmzt.www.mine.model.BrowseHistoryViewModel;

/**
 * @author tanlei
 * @date 2019/8/14
 * @describe 浏览历史界面
 */

public class BrowseHistoryActivity extends TourBaseActivity<BrowseHistoryViewModel,ActivityBrowseHistoryBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_browse_history;
    }

    @Override
    protected BrowseHistoryViewModel createViewModel() {
        return new BrowseHistoryViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
    }
}
