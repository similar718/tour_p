package cn.xmzt.www.hotel.activity;

import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityHotelListBinding;
import cn.xmzt.www.hotel.adapter.HotelListAdapter;
import cn.xmzt.www.hotel.bean.HotelListBean;
import cn.xmzt.www.hotel.model.HotelListViewModel;
import cn.xmzt.www.popup.HotelPriceStarTopPopupWindow;
import cn.xmzt.www.popup.HotelSortPopupWindow;
import cn.xmzt.www.popup.HotelThemePopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tanlei
 * @date 2019/7/27
 * @describe 酒店列表界面
 */

public class HotelListActivity extends TourBaseActivity<HotelListViewModel, ActivityHotelListBinding> {
    private HotelSortPopupWindow popupWindow;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_hotel_list;
    }

    @Override
    protected HotelListViewModel createViewModel() {
        return new HotelListViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        dataBinding.setActivity(this);
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
        dataBinding.lvHotelList.setAdapter(adapter);

        dataBinding.lvHotelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startToActivity(HotelDetailsListActivity.class);
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_history:
                startToActivity(HotelHistoryListActivity.class);
                break;
            case R.id.ll_sort:
                if (null == popupWindow) {
                    popupWindow = new HotelSortPopupWindow(this);
                    popupWindow.setSelectPosition(1);
                    if (popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    } else {
                        popupWindow.showAsDropDown(dataBinding.llSort, 0, 0, Gravity.BOTTOM);
                    }
                } else {
                    popupWindow.showAsDropDown(dataBinding.llSort, 0, 0, Gravity.BOTTOM);
                }
                break;
            case R.id.ll_price:
                HotelPriceStarTopPopupWindow hotelPriceStarTopPopupWindow = new HotelPriceStarTopPopupWindow(this);
                hotelPriceStarTopPopupWindow.showAsDropDown(dataBinding.llSort, 0, 0, Gravity.BOTTOM);
                break;
            case R.id.ll_theme:
                HotelThemePopupWindow hotelThemePopupWindow = new HotelThemePopupWindow(this);
                hotelThemePopupWindow.showAsDropDown(dataBinding.llSort, 0, 0, Gravity.BOTTOM);
                break;
            default:
                break;
        }
    }
}
