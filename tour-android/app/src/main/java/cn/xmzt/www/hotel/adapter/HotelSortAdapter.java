package cn.xmzt.www.hotel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.xmzt.www.R;

import java.util.List;

/**
 * @author tanlei
 * @date 2019/8/2
 * @describe 酒店排序的适配器
 */

public class HotelSortAdapter extends BaseAdapter {
    private List<String> sortList;
    private Context context;
    private int selectPosition;

    public HotelSortAdapter(List<String> sortList, Context context) {
        this.sortList = sortList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return sortList.size();
    }

    @Override
    public Object getItem(int i) {
        return sortList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (null == view) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_hotel_sort, null);
            holder.ivSelect = view.findViewById(R.id.iv_select);
            holder.tvSort = view.findViewById(R.id.tv_sort);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvSort.setText(sortList.get(i));
        if (i == selectPosition) {
            holder.tvSort.setTextColor(context.getResources().getColor(R.color.color_24A4FF));
            holder.ivSelect.setVisibility(View.VISIBLE);
        } else {
            holder.tvSort.setTextColor(context.getResources().getColor(R.color.color_66_66_66));
            holder.ivSelect.setVisibility(View.GONE);
        }
        return view;
    }

    static class ViewHolder {
        TextView tvSort;
        ImageView ivSelect;
    }
}
