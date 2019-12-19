package cn.xmzt.www.selfdrivingtools.adapter;

import android.app.Activity;
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

import cn.xmzt.www.R;
import cn.xmzt.www.selfdrivingtools.activity.ScenicSpotMapActivity;
import cn.xmzt.www.selfdrivingtools.bean.ScenicSpotMapTypeListBean;

import java.util.List;

/**
 * 带有地图的智慧导览类型列表 滑动中更新的问题
 */
public class LabelAdapter extends RecyclerView.Adapter<LabelAdapter.ViewHolderItem> {
    private List<ScenicSpotMapTypeListBean> list;
    private Activity activity;
    private LayoutInflater inflater;


    public LabelAdapter(List<ScenicSpotMapTypeListBean> list, Activity activity) {
        this.list = list;
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<ScenicSpotMapTypeListBean> data){
        list = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderItem(inflater.inflate(R.layout.item_scenic_spot_map_type, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderItem holder, int position1) {
        com.bumptech.glide.Glide.with(activity).load(list.get(position1).getImg_icon()).into(holder.ivImg);
        holder.tvTitle.setText(list.get(position1).getTitle());
        // 选中的字体颜色更改
        holder.tvTitle.setTextColor(list.get(position1).getIs_selected() ? Color.parseColor("#666666") : Color.parseColor("#999999"));
        // 选中的字体风格加粗
        holder.tvTitle.setTypeface(list.get(position1).getIs_selected() ? Typeface.defaultFromStyle(Typeface.BOLD) : Typeface.defaultFromStyle(Typeface.NORMAL));
        holder.tvBottomLine.setVisibility(list.get(position1).getIs_selected() ? View.VISIBLE : View.INVISIBLE);
    }

    public static final int UPDATE_STATE = 101;
    public static final int UPDATE_NAME = 102;

    @Override
    public void onBindViewHolder(@NonNull ViewHolderItem holder, int position, @NonNull List<Object> payloads) {
        //list为空时，必须调用两个参数的onBindViewHolder(@NonNull LabelHolder holder, int position)
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else if (payloads.get(0) instanceof Integer) {
            int payLoad = (int) payloads.get(0);
            switch (payLoad) {
                case UPDATE_STATE:
                    // 选中的字体颜色更改
                    holder.tvTitle.setTextColor(list.get(position).getIs_selected() ? Color.parseColor("#666666") : Color.parseColor("#999999"));
                    // 选中的字体风格加粗
                    holder.tvTitle.setTypeface(list.get(position).getIs_selected() ? Typeface.defaultFromStyle(Typeface.BOLD) : Typeface.defaultFromStyle(Typeface.NORMAL));
                    holder.tvBottomLine.setVisibility(list.get(position).getIs_selected() ? View.VISIBLE : View.INVISIBLE);
                    break;
                case UPDATE_NAME:
                    // 选中的字体颜色更改
                    holder.tvTitle.setTextColor(list.get(position).getIs_selected() ? Color.parseColor("#666666") : Color.parseColor("#999999"));
                    // 选中的字体风格加粗
                    holder.tvTitle.setTypeface(list.get(position).getIs_selected() ? Typeface.defaultFromStyle(Typeface.BOLD) : Typeface.defaultFromStyle(Typeface.NORMAL));
                    holder.tvBottomLine.setVisibility(list.get(position).getIs_selected() ? View.VISIBLE : View.INVISIBLE);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    public class ViewHolderItem extends RecyclerView.ViewHolder {
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
                    ((ScenicSpotMapActivity) activity).OnClickListener(list.get(getAdapterPosition()),getAdapterPosition());
                }
            });
        }
    }
}
