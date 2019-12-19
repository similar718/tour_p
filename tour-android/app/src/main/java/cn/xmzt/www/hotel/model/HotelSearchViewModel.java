package cn.xmzt.www.hotel.model;

import androidx.databinding.ObservableBoolean;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;

/**
 * @author tanlei
 * @date 2019/7/29
 * @describe
 */

public class HotelSearchViewModel extends BaseViewModel {
    /**
     * 当前是否为搜索界面
     */
    public ObservableBoolean isSearch = new ObservableBoolean(false);


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_hotel_search://搜索
                isSearch.set(true);
                break;
            case R.id.iv_search_delete://删除搜索框中内容
                isSearch.set(false);
                break;
            default:
                break;
        }
    }

}
