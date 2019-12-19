package cn.xmzt.www.popup;

import android.app.Activity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BasePopupWindow;
import cn.xmzt.www.hotel.adapter.HotelScreenAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tanlei
 * @date 2019/8/2
 * @describe 酒店价格星级筛选从底部弹出的弹出框
 */

public class HotelPriceStarBottomPopupWindow extends BasePopupWindow {
    public HotelPriceStarBottomPopupWindow(Activity context) {
        this(context, null);
    }

    public HotelPriceStarBottomPopupWindow(Activity context, AttributeSet attrs) {
        super(context, attrs);
        setPopupWindowSize(0, 1);
        RecyclerView rvPrice = view.findViewById(R.id.rv_price);
        List<String> list = new ArrayList<>();
        list.add("150以下");
        list.add("150-300");
        list.add("300-450");
        list.add("450-600");
        list.add("600-750");
        list.add("750-900");
        list.add("900以上");
        HotelScreenAdapter priceAdapter = new HotelScreenAdapter(list, context);
        rvPrice.setLayoutManager(new GridLayoutManager(context, 4));
        rvPrice.setAdapter(priceAdapter);

        RecyclerView rvStar = view.findViewById(R.id.rv_star);
        List<String> list2 = new ArrayList<>();
        list2.add("不限");
        list2.add("三星/简约型");
        list2.add("三星/舒适型");
        list2.add("四星/品质型");
        list2.add("五星/豪华型");
        HotelScreenAdapter starAdapter = new HotelScreenAdapter(list2, context);
        rvStar.setLayoutManager(new GridLayoutManager(context, 4));
        rvStar.setAdapter(starAdapter);
    }

    @Override
    protected int setPopupWindowLayoutId() {
        return R.layout.popupwindow_hotel_price_star_bottom;
    }
}
