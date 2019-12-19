package cn.xmzt.www.mine.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.mine.bean.BrowseHistoryBean;
import cn.xmzt.www.utils.TimeUtil;

import java.util.List;

/**
 * @author tanlei
 * @date 2019/8/13
 * @describe
 */

public class BrowseHistoryAdapter extends RecyclerView.Adapter<BrowseHistoryAdapter.CollectionViewHolder> {
    private Context context;
    private List<BrowseHistoryBean> list;
    private boolean isCheck;
    private OnItemClickListener onItemClickListener;
    private OnCollectionCheckListener onCollectionCheckListener;

    public void setOnCollectionCheckListener(BrowseHistoryAdapter.OnCollectionCheckListener onCollectionCheckListener) {
        this.onCollectionCheckListener = onCollectionCheckListener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setCheck(boolean check) {
        isCheck = check;
        notifyDataSetChanged();
    }

    public BrowseHistoryAdapter(Context context, List<BrowseHistoryBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CollectionViewHolder holder;
        holder = new CollectionViewHolder(LayoutInflater.from(context).inflate(R.layout.item_browse_history, viewGroup, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder collectionViewHolder, int i) {
        collectionViewHolder.tv_hotel_name.setText(list.get(i).getName());
        collectionViewHolder.tv_hotel_price.setText(list.get(i).getPrice() + "");
        GlideUtil.loadImgRadius(collectionViewHolder.iv_hotel_image, 4, list.get(i).getImage());
        long l = System.currentTimeMillis() - TimeUtil.strToDate(list.get(i).getGmtCreate(), TimeUtil.FORMAT_D).getTime();
        Log.e("lee", l + "");
        if (l > 0 && l < (1000 * 60L)) {
            collectionViewHolder.tv_date.setText("刚刚");
        } else if (l > 0 && l < (1000 * 60 * 60L)) {
            collectionViewHolder.tv_date.setText((l / (1000 * 60L)) + "分钟前");
        } else if (l >= (1000 * 60 * 60L) && l < (1000 * 60 * 60 * 24L)) {
            collectionViewHolder.tv_date.setText((l / (1000 * 60 * 60L)) + "小时前");
        } else if (l >= (1000 * 60 * 60 * 24L) && l < (1000 * 60 * 60 * 24 * 30L)) {
            collectionViewHolder.tv_date.setText((l / (1000 * 60 * 60 * 24L)) + "天前");
        } else if (l >= (1000 * 60 * 60 * 24 * 30L) && l < (1000L * 60L * 60L * 24L * 30L * 3L)) {
            collectionViewHolder.tv_date.setText((l / (1000 * 60 * 60 * 24 * 30L)) + "个月前");
        } else if (l >= (1000L * 60L * 60L * 24L * 30L * 3L) && l < (1000L * 60L * 60L * 24L * 30L * 12L)) {
            collectionViewHolder.tv_date.setText(TimeUtil.dateToStr(TimeUtil.strToDate(list.get(i).getGmtCreate(), TimeUtil.FORMAT_D).getTime(), TimeUtil.FORMAT_E));
        } else {
            collectionViewHolder.tv_date.setText(TimeUtil.dateToStr(TimeUtil.strToDate(list.get(i).getGmtCreate(), TimeUtil.FORMAT_D).getTime(), TimeUtil.FORMAT_F));
        }
        if (list.get(i).isCheck()) {
            collectionViewHolder.iv_check.setImageResource(R.drawable.scenic_spot_map_item_route_line_right_icon);
        } else {
            collectionViewHolder.iv_check.setImageResource(R.drawable.scenic_spot_map_item_route_line_right_icon_un);
        }
        if (isCheck) {
            collectionViewHolder.ll_check.setVisibility(View.VISIBLE);
        } else {
            collectionViewHolder.ll_check.setVisibility(View.GONE);
        }
        if (list.get(i).getType() == 1) {//酒店
            collectionViewHolder.tv_type.setText("酒店");
            collectionViewHolder.tv_type.setBackground(context.getResources().getDrawable(R.drawable.shapecollection_type_bg_hotel));
        } else if (list.get(i).getType() == 2) {//线路
            collectionViewHolder.tv_type.setText("线路");
            collectionViewHolder.tv_type.setBackground(context.getResources().getDrawable(R.drawable.shapecollection_type_bg_line));
        } else {//门票
            collectionViewHolder.tv_type.setText("门票");
            collectionViewHolder.tv_type.setBackground(context.getResources().getDrawable(R.drawable.shapecollection_type_bg_ticket));
        }
        if (list.get(i).getCollectionId() == 0) {//未被收藏（可被收藏的状态）
            collectionViewHolder.iv_collection.setImageResource(R.drawable.icon_collection_un);
        } else {//已经被收藏（可取消收藏的状态）
            collectionViewHolder.iv_collection.setImageResource(R.drawable.icon_collection);
        }
        collectionViewHolder.ll_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != getOnItemClickListener()) {
                    onItemClickListener.onItemClick(i);
                }
            }
        });
        collectionViewHolder.iv_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onCollectionCheckListener) {
                    onCollectionCheckListener.onCollectionCheckClick(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class CollectionViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_check, ll_layout;
        ImageView iv_check, iv_hotel_image, iv_collection;
        TextView tv_hotel_name, tv_hotel_price, tv_date, tv_type;

        public CollectionViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_check = itemView.findViewById(R.id.ll_check);
            iv_check = itemView.findViewById(R.id.iv_check);
            tv_hotel_name = itemView.findViewById(R.id.tv_hotel_name);
            tv_hotel_price = itemView.findViewById(R.id.tv_hotel_price);
            iv_hotel_image = itemView.findViewById(R.id.iv_hotel_image);
            ll_layout = itemView.findViewById(R.id.ll_layout);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_type = itemView.findViewById(R.id.tv_type);
            iv_collection = itemView.findViewById(R.id.iv_collection);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnCollectionCheckListener {
        void onCollectionCheckClick(int position);
    }
}
