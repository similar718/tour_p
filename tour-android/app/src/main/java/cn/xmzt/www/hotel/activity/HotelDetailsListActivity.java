package cn.xmzt.www.hotel.activity;

import android.view.Gravity;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityHotelDetailsListBinding;
import cn.xmzt.www.hotel.GlideImageLoader;
import cn.xmzt.www.hotel.adapter.HotelListAdapter;
import cn.xmzt.www.hotel.bean.HotelListBean;
import cn.xmzt.www.hotel.model.HotelDetailsListViewModel;
import cn.xmzt.www.popup.HotelRoomScreenPopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tanlei
 * @date 2019/7/29
 * @describe 从酒店列表点击进入的酒店详情页
 */

public class HotelDetailsListActivity extends TourBaseActivity<HotelDetailsListViewModel, ActivityHotelDetailsListBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_hotel_details_list;
    }

    @Override
    protected HotelDetailsListViewModel createViewModel() {
        return new HotelDetailsListViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        dataBinding.setActivity(this);
        dataBinding.banner.setImageLoader(new GlideImageLoader());
        List<String> images = new ArrayList<>();
        images.add("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_" +
                "4000&sec=1564216863&di=2e009b334c4135bdf023104d6deba987&src=http://dingyue.nosdn.127.net/8jZqtz1mW8Cp" +
                "GNDQthJNta6IK3RL90zRKygBREoV8NSEO1541474222791compressflag.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1564226996937&di=c36dc03e31ba0f7697b04c6e6f0c2881&" +
                "imgtype=jpg&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D2278011335%2C2910610009%26fm%3D214%26gp%3D0.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1564226956669&di=abcbf25e311be30315f674d" +
                "b6a1567a3&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20180125%2Fc35e9967ad5c4375893db0abf683e4e3.jpeg");
        dataBinding.banner.setImages(images);
        dataBinding.banner.start();

        List<HotelListBean> listBeans = new ArrayList<>();
        listBeans.add(new HotelListBean());
        listBeans.add(new HotelListBean());
        listBeans.add(new HotelListBean());
        HotelListAdapter adapter = new HotelListAdapter(this, listBeans);

        dataBinding.lvHotelPeripheryList.setAdapter(adapter);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_hotel_details:
                startToActivity(HotelDetailsActivity.class);
                break;
            case R.id.ll_hot_list:
                startToActivity(PeripheryHotHotelListActivity.class);
                break;
            case R.id.tv_screen:
                HotelRoomScreenPopupWindow popupWindow = new HotelRoomScreenPopupWindow(this);
                popupWindow.showAtLocation(dataBinding.sv, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.tv_map:
                startToActivity(HotelInfoWriteActivity.class);
                break;
            default:
                break;
        }
    }
}
