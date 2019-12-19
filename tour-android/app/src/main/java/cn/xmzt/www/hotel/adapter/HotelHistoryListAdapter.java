package cn.xmzt.www.hotel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import cn.xmzt.www.R;
import cn.xmzt.www.hotel.bean.HotelHistoryListBean;

import java.util.List;

/**
 * @author tanlei
 * @date 2019/7/29
 * @describe 酒店列表的适配器（使用MVVM的话无需次适配器）
 */

public class HotelHistoryListAdapter extends BaseAdapter {
    private Context context;
    private List<HotelHistoryListBean> list;

    public HotelHistoryListAdapter(Context context, List<HotelHistoryListBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_history_hotel_list, null,true);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        return view;
    }

    static class ViewHolder {

    }
}
