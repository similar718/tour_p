package cn.xmzt.www.popup;

import android.app.Activity;
import android.util.AttributeSet;
import android.widget.ListView;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BasePopupWindow;
import cn.xmzt.www.hotel.adapter.HotelSortAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tanlei
 * @date 2019/8/2
 * @describe 酒店排序的弹出框
 */

public class HotelSortPopupWindow extends BasePopupWindow {
    private final HotelSortAdapter adapter;

    public HotelSortPopupWindow(Activity context) {
        this(context, null);
    }

    public HotelSortPopupWindow(Activity context, AttributeSet attrs) {
        super(context, attrs);
        setPopupWindowSize(0, 1);
        List<String> list = new ArrayList<>();
        list.add("综合排序");
        list.add("销量优先");
        list.add("距离优先");
        list.add("价格从低到高");
        list.add("价格从高到低");
        ListView lv = view.findViewById(R.id.lv_sort);
        adapter = new HotelSortAdapter(list, context);
        lv.setAdapter(adapter);
    }

    /**
     * 设置默认选中那个
     *
     * @param position
     */
    public void setSelectPosition(int position) {
        adapter.setSelectPosition(position);
    }

    @Override
    protected int setPopupWindowLayoutId() {
        return R.layout.popupwindow_hotel_sort;
    }
}
