package cn.xmzt.www.hotel.activity;

import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivitySelectTouristsBinding;
import cn.xmzt.www.hotel.model.SelectTouristsViewModel;
import cn.xmzt.www.mine.activity.AddTouristsActivity;

/**
 * @author tanlei
 * @date 2019/8/1
 * @describe 选择出游人界面
 */

public class SelectTouristsActivity extends TourBaseActivity<SelectTouristsViewModel, ActivitySelectTouristsBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_select_tourists;
    }

    @Override
    protected SelectTouristsViewModel createViewModel() {
        return new SelectTouristsViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        dataBinding.setActivity(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_add_tourists:
                startToActivity(AddTouristsActivity.class);
                break;
            default:
                break;
        }
    }
}
