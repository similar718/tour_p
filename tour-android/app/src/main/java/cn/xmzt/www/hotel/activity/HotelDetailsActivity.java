package cn.xmzt.www.hotel.activity;

import com.google.android.material.tabs.TabLayout;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityHotelDetailsBinding;
import cn.xmzt.www.hotel.model.HotelDetailsViewModel;

/**
 * @author tanlei
 * @date 2019/7/30
 * @describe 从酒店详情列表中跳转的酒店详情界面
 */

public class HotelDetailsActivity extends TourBaseActivity<HotelDetailsViewModel, ActivityHotelDetailsBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_hotel_details;
    }

    @Override
    protected HotelDetailsViewModel createViewModel() {
        return new HotelDetailsViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        dataBinding.tl.addTab(dataBinding.tl.newTab().setText("设施服务"));
        dataBinding.tl.addTab(dataBinding.tl.newTab().setText("酒店介绍"));
        dataBinding.tl.addTab(dataBinding.tl.newTab().setText("酒店政策"));
        dataBinding.tl.addTab(dataBinding.tl.newTab().setText("周边交通"));
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
