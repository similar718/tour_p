package cn.xmzt.www.intercom.activity;

import android.content.Intent;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityGroupPersonalInfoBinding;
import cn.xmzt.www.intercom.vm.GroupPersonalInfoViewModel;

/**
 * @author tanlei
 * @date 2019/9/7
 * @describe 群个人信息
 */

public class GroupPersonalInfoActivity extends TourBaseActivity<GroupPersonalInfoViewModel, ActivityGroupPersonalInfoBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_group_personal_info;
    }

    @Override
    protected GroupPersonalInfoViewModel createViewModel() {
        return new GroupPersonalInfoViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
        if (getIntent() != null) {
            viewModel.setTeamId(getIntent().getStringExtra("teamId"),getIntent().getStringExtra("userId"));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK){
            String content = "";
            if (data != null){
                content = data.getStringExtra("content");
                dataBinding.tvDesc.setText(content);
            }
            if (dataBinding.tvDesc.getVisibility() == View.GONE){
                dataBinding.tvDesc.setVisibility(View.VISIBLE);
            }
        }
    }
}
