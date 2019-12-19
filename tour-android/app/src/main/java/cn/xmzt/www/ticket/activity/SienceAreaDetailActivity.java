package cn.xmzt.www.ticket.activity;

import com.google.android.material.tabs.TabLayout;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivitySienceAreaDetailBinding;
import cn.xmzt.www.ticket.model.SienceAreaDetailViewModel;

public class SienceAreaDetailActivity extends TourBaseActivity<SienceAreaDetailViewModel, ActivitySienceAreaDetailBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_sience_area_detail;
    }

    @Override
    protected SienceAreaDetailViewModel createViewModel() {
        return new SienceAreaDetailViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        dataBinding.tl.addTab(dataBinding.tl.newTab().setText("景区须知"));
        dataBinding.tl.addTab(dataBinding.tl.newTab().setText("景区介绍"));
        dataBinding.tl.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}