package cn.xmzt.www.intercom.activity;

import android.text.TextUtils;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivitySettingTagBinding;
import cn.xmzt.www.intercom.vm.SettingTagViewModel;

/**
 * @author tanlei
 * @date 2019/9/7
 * @describe 设置标签界面
 */

public class SettingTagActivity extends TourBaseActivity<SettingTagViewModel, ActivitySettingTagBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_setting_tag;
    }

    @Override
    protected SettingTagViewModel createViewModel() {
        return new SettingTagViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
        if (getIntent() != null) {
            viewModel.setUserId(getIntent().getStringExtra("userId"), getIntent().getStringExtra("teamId"));
            if (!TextUtils.isEmpty(getIntent().getStringExtra("describe"))) {
                dataBinding.et.setText(getIntent().getStringExtra("describe"));
            }
        }
    }
}
