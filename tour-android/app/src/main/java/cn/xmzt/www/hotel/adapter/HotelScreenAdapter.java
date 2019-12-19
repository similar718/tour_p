package cn.xmzt.www.hotel.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.xmzt.www.R;

import java.util.List;

/**
 * @author tanlei
 * @date 2019/8/2
 * @describe
 */

public class HotelScreenAdapter extends RecyclerView.Adapter<HotelScreenAdapter.HotelThemeViewHolder> {
    private List<String> list;
    private Context context;

    public HotelScreenAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HotelThemeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        HotelThemeViewHolder viewHolder = new HotelThemeViewHolder(LayoutInflater.from(context).inflate(R.layout.item_hotel_theme, viewGroup, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HotelThemeViewHolder hotelThemeViewHolder, int i) {
        hotelThemeViewHolder.tvTag.setText(list.get(i));
        if (i % 2 == 0) {
            hotelThemeViewHolder.ivTag.setVisibility(View.VISIBLE);
            hotelThemeViewHolder.tvTag.setTextColor(context.getResources().getColor(R.color.color_24A4FF));
            hotelThemeViewHolder.tvTag.setBackgroundResource(R.drawable.shape_hotel_stroke_1_radius_4_blue);
        } else {
            hotelThemeViewHolder.ivTag.setVisibility(View.GONE);
            hotelThemeViewHolder.tvTag.setTextColor(context.getResources().getColor(R.color.color_33_33_33));
            hotelThemeViewHolder.tvTag.setBackgroundResource(R.drawable.shape_hotel_stroke_1_radius_4_gary);
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    static class HotelThemeViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTag;
        private ImageView ivTag;

        public HotelThemeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTag = itemView.findViewById(R.id.tv_tag);
            ivTag = itemView.findViewById(R.id.iv_tag);
        }
    }
}
