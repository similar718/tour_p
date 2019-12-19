package cn.xmzt.www.hotel.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityPeripheryHotHotelListBinding;
import cn.xmzt.www.hotel.adapter.HotelListAdapter;
import cn.xmzt.www.hotel.bean.HotelListBean;
import cn.xmzt.www.hotel.model.PeripheryHotHotelListViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tanlei
 * @date 2019/7/30
 * @describe 周边热销酒店列表界面
 */

public class PeripheryHotHotelListActivity extends TourBaseActivity<PeripheryHotHotelListViewModel, ActivityPeripheryHotHotelListBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_periphery_hot_hotel_list;
    }

    @Override
    protected PeripheryHotHotelListViewModel createViewModel() {
        return new PeripheryHotHotelListViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);

        List<HotelListBean> listBeans = new ArrayList<>();
        listBeans.add(new HotelListBean());
        listBeans.add(new HotelListBean());
        listBeans.add(new HotelListBean());
        listBeans.add(new HotelListBean());
        listBeans.add(new HotelListBean());
        listBeans.add(new HotelListBean());
        listBeans.add(new HotelListBean());
        listBeans.add(new HotelListBean());
        HotelListAdapter adapter = new HotelListAdapter(this, listBeans);
        dataBinding.lvHotList.setAdapter(adapter);
    }
}
