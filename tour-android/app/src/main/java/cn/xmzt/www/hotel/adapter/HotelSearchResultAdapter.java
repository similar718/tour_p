package cn.xmzt.www.hotel.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.hotel.bean.HotelSearchResultBean;

import java.util.List;

/**
 * @author tanlei
 * @date 2019/7/29
 * @describe 酒店搜索结果的适配器
 */

public class HotelSearchResultAdapter extends RecyclerView.Adapter {
    private List<HotelSearchResultBean> list;
    private Context context;
    //布局类型
    private static final int VIEW_TYPE_HOTEL = 1;
    private static final int VIEW_TYPE_SCENIC = 2;

    public HotelSearchResultAdapter(List<HotelSearchResultBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 2) {
            return VIEW_TYPE_SCENIC;
        } else {
            return VIEW_TYPE_HOTEL;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder viewHolder;
        if (i == VIEW_TYPE_HOTEL) {
            viewHolder = new ViewHolderHotel(LayoutInflater.from(context).inflate(R.layout.item_search_result_hotel, viewGroup, false));
        } else {
            viewHolder = new ViewHolderScenic(LayoutInflater.from(context).inflate(R.layout.item_search_result_scenic, viewGroup, false));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (getItemViewType(i) == VIEW_TYPE_HOTEL) {
//            ((ViewHolderHotel)viewHolder).tvHotelName.setText(i+"");
        } else {

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolderScenic extends RecyclerView.ViewHolder {
        private TextView tvScenicName;

        public ViewHolderScenic(@NonNull View itemView) {
            super(itemView);
            tvScenicName = itemView.findViewById(R.id.tv_scenic_name);
        }
    }

    public static class ViewHolderHotel extends RecyclerView.ViewHolder {
        private TextView tvHotelName, tvHotelDestance, tvHotelPrice;

        public ViewHolderHotel(@NonNull View itemView) {
            super(itemView);
            tvHotelName = itemView.findViewById(R.id.tv_hotel_name);
            tvHotelDestance = itemView.findViewById(R.id.tv_hotel_distance);
            tvHotelPrice = itemView.findViewById(R.id.tv_hotel_price);
        }
    }
}
