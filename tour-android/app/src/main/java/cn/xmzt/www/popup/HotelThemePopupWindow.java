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
 * @describe 酒店主题筛选弹出框
 */

public class HotelThemePopupWindow extends BasePopupWindow {
    public HotelThemePopupWindow(Activity context) {
        this(context, null);
    }

    public HotelThemePopupWindow(Activity context, AttributeSet attrs) {
        super(context, attrs);
        setPopupWindowSize(0, 1);
        RecyclerView rv = view.findViewById(R.id.rv);
        List<String> strings = new ArrayList<>();
        strings.add("不限");
        strings.add("休闲度假");
        strings.add("都市观光");
        strings.add("精品酒店");
        strings.add("亲子时刻");
        strings.add("主题乐园");
        strings.add("购物便利");
        HotelScreenAdapter hotelScreenAdapter = new HotelScreenAdapter(strings, context);
        rv.setLayoutManager(new GridLayoutManager(context, 4));
        rv.setAdapter(hotelScreenAdapter);
    }

    @Override
    protected int setPopupWindowLayoutId() {
        return R.layout.popupwindow_hotel_theme;
    }
}
