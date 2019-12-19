package cn.xmzt.www.selfdrivingtools.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.xmzt.www.selfdrivingtools.activity.ScenicSpotMapActivity;
import cn.xmzt.www.selfdrivingtools.bean.ScenicSpotMapTypeListBean;

import java.util.List;

/**
 * 带有地图的智慧导览类型列表 Adapter
 */
public class ScenicSpotMapTypeListAdapter extends RecyclerView.Adapter {
    private static List<ScenicSpotMapTypeListBean> list;
    private static Context context;

    public ScenicSpotMapTypeListAdapter(List<ScenicSpotMapTypeListBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setData(List<ScenicSpotMapTypeListBean> data){
        list = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder viewHolder;
        viewHolder = new ViewHolderItem(LayoutInflater.from(context).inflate(cn.xmzt.www.R.layout.item_scenic_spot_map_type, viewGroup, false));
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        com.bumptech.glide.Glide.with(context).load(list.get(i).getImg_icon()).into(((ViewHolderItem) viewHolder).ivImg);
        ((ViewHolderItem) viewHolder).tvTitle.setText(list.get(i).getTitle());
        // 选中的字体颜色更改
        ((ViewHolderItem) viewHolder).tvTitle.setTextColor(list.get(i).getIs_selected() ? Color.parseColor("#666666") : Color.parseColor("#999999"));
        // 选中的字体风格加粗
        ((ViewHolderItem) viewHolder).tvTitle.setTypeface(list.get(i).getIs_selected() ? Typeface.defaultFromStyle(Typeface.BOLD) : Typeface.defaultFromStyle(Typeface.NORMAL));
        ((ViewHolderItem) viewHolder).tvBottomLine.setVisibility(list.get(i).getIs_selected() ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolderItem extends RecyclerView.ViewHolder {
        private TextView tvBottomLine,tvTitle;
        private ImageView ivImg;

        public ViewHolderItem(@NonNull View itemView) {
            super(itemView);
            tvBottomLine = itemView.findViewById(cn.xmzt.www.R.id.tv_type_bottom_line);
            tvTitle = itemView.findViewById(cn.xmzt.www.R.id.tv_type_content);

            ivImg = itemView.findViewById(cn.xmzt.www.R.id.iv_type_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ScenicSpotMapActivity) context).OnClickListener(list.get(getAdapterPosition()),getAdapterPosition());
                }
            });
        }
    }
}
