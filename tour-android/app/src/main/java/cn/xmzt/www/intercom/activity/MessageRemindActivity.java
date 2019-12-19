package cn.xmzt.www.intercom.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityMessageRemindBinding;
import cn.xmzt.www.intercom.vm.MessageRemindViewModel;

/**
 * @author tanlei
 * @date 2019/9/5
 * @describe 消息提醒界面
 */

public class MessageRemindActivity extends TourBaseActivity<MessageRemindViewModel, ActivityMessageRemindBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_message_remind;
    }

    @Override
    protected MessageRemindViewModel createViewModel() {
        return new MessageRemindViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
        if (getIntent() != null) {
            viewModel.setTeamId(getIntent().getStringExtra("teamId"));
        }
    }
}
