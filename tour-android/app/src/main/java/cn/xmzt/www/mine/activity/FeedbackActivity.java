package cn.xmzt.www.mine.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityFeedbackBinding;
import cn.xmzt.www.mine.model.FeedbackViewModel;

/**
 * @author tanlei
 * @date 2019/8/28
 * @describe 意见反馈
 */

public class FeedbackActivity extends TourBaseActivity<FeedbackViewModel, ActivityFeedbackBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected FeedbackViewModel createViewModel() {
        return new FeedbackViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
    }
}
