package cn.xmzt.www.selfdrivingtools.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.selfdrivingtools.activity.WisdomGuideActivity;
import cn.xmzt.www.selfdrivingtools.bean.WisdomGuideInfo;

import java.util.List;

/**
 * 智慧景区导览列表 Adapter
 */
public class WisdomGuideAdapter extends RecyclerView.Adapter {
    private static List<WisdomGuideInfo.ItemsBean> list;
    private static Context context;

    public WisdomGuideAdapter(List<WisdomGuideInfo.ItemsBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public static void setData(List<WisdomGuideInfo.ItemsBean> data){
        list = data;
    }

    public static void addData(List<WisdomGuideInfo.ItemsBean> data){
        if (list == null){
            list = data;
        } else {
            for (WisdomGuideInfo.ItemsBean item:data) {
                list.add(item);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder viewHolder;
        viewHolder = new ViewHolderItem(LayoutInflater.from(context).inflate(cn.xmzt.www.R.layout.item_wisdom_guide, viewGroup, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ViewHolderItem) viewHolder).tvTopLeft.setText(list.get(i).getExplainPointCount()+"个讲解点");

        GlideUtil.loadImgC(((ViewHolderItem) viewHolder).ivImg,list.get(i).getPhotoUrl());
        ((ViewHolderItem) viewHolder).tvTitle.setText(list.get(i).getScenicName());
        if (list.get(i).getGrade() == 4 || list.get(i).getGrade() == 5){
            ((ViewHolderItem) viewHolder).tvLevel.setVisibility(View.VISIBLE);
            ((ViewHolderItem) viewHolder).tvLevel.setText("【" + list.get(i).getGrade() + "A】");
        } else{
            ((ViewHolderItem) viewHolder).tvLevel.setVisibility(View.GONE);
            ((ViewHolderItem) viewHolder).tvLevel.setText("【" + list.get(i).getGrade() + "A】");
        }
        ((ViewHolderItem) viewHolder).tvAddress.setText(list.get(i).getScenicAddress());
        ((ViewHolderItem) viewHolder).tvPlayNum.setText("播放量："+list.get(i).getListenCount()+"次");
        ((ViewHolderItem) viewHolder).tvLocation.setText("距离"+list.get(i).getDistance()+"km");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolderItem extends RecyclerView.ViewHolder {
        private TextView tvTopLeft,tvTitle,tvLevel,tvAddress,tvPlayNum,tvLocation;
        private ImageView ivImg;

        public ViewHolderItem(@NonNull View itemView) {
            super(itemView);
            tvTopLeft = itemView.findViewById(cn.xmzt.www.R.id.item_top_left_tv);
            tvTitle = itemView.findViewById(cn.xmzt.www.R.id.item_title_tv);
            tvLevel = itemView.findViewById(cn.xmzt.www.R.id.item_level_tv);
            tvAddress = itemView.findViewById(cn.xmzt.www.R.id.item_address_tv);
            tvPlayNum = itemView.findViewById(cn.xmzt.www.R.id.item_play_num_tv);
            tvLocation = itemView.findViewById(cn.xmzt.www.R.id.item_location_tv);

            ivImg = itemView.findViewById(cn.xmzt.www.R.id.item_special_ticket_hot_img_iv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((WisdomGuideActivity) context).OnClickListener(list.get(getAdapterPosition()),2);
                }
            });
        }
    }
}
