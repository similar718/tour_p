package cn.xmzt.www.hotel.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityHotelSearchBinding;
import cn.xmzt.www.hotel.adapter.HotelSearchResultAdapter;
import cn.xmzt.www.hotel.bean.HotelSearchResultBean;
import cn.xmzt.www.hotel.model.HotelSearchViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tanlei
 * @date 2019/7/29
 * @describe 酒店搜索界面
 */

public class HotelSearchActivity extends TourBaseActivity<HotelSearchViewModel, ActivityHotelSearchBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_hotel_search;
    }

    @Override
    protected HotelSearchViewModel createViewModel() {
        return new HotelSearchViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        dataBinding.setActivity(this);
        dataBinding.rvSearchList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<HotelSearchResultBean> list = new ArrayList<>();
        list.add(new HotelSearchResultBean());
        list.add(new HotelSearchResultBean());
        list.add(new HotelSearchResultBean());
        list.add(new HotelSearchResultBean());
        list.add(new HotelSearchResultBean());
        HotelSearchResultAdapter adapter = new HotelSearchResultAdapter(list, this);
        dataBinding.rvSearchList.setAdapter(adapter);
    }

    public void onClick(View view) {
        finish();
    }
}
