package cn.xmzt.www.hotel.activity;

import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityHotelHistoryListBinding;
import cn.xmzt.www.hotel.adapter.HotelHistoryListAdapter;
import cn.xmzt.www.hotel.bean.HotelHistoryListBean;
import cn.xmzt.www.hotel.model.HotelHistoryListViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tanlei
 * @date 2019/7/29
 * @describe 酒店浏览历史界面
 */

public class HotelHistoryListActivity extends TourBaseActivity<HotelHistoryListViewModel, ActivityHotelHistoryListBinding> {

    @Override
    protected int setLayoutId() {
        return R.layout.activity_hotel_history_list;
    }

    @Override
    protected HotelHistoryListViewModel createViewModel() {
        return new HotelHistoryListViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        dataBinding.setActivity(this);
        List<HotelHistoryListBean> listBeans = new ArrayList<>();
        listBeans.add(new HotelHistoryListBean());
        listBeans.add(new HotelHistoryListBean());
        listBeans.add(new HotelHistoryListBean());
        listBeans.add(new HotelHistoryListBean());
        listBeans.add(new HotelHistoryListBean());
        listBeans.add(new HotelHistoryListBean());
        listBeans.add(new HotelHistoryListBean());
        listBeans.add(new HotelHistoryListBean());
        listBeans.add(new HotelHistoryListBean());
        listBeans.add(new HotelHistoryListBean());

        HotelHistoryListAdapter adapter = new HotelHistoryListAdapter(this, listBeans);
        dataBinding.lvHistoryHotel.setAdapter(adapter);
    }

    public void onClick(View view){
        finish();
    }
}
