package cn.xmzt.www.ticket.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import cn.xmzt.www.R;
import cn.xmzt.www.view.RoundedCorners;

import java.util.List;

/**
 * 景区介绍底部显示的图片列表
 */
public class SienceIntroduceImgAdapter extends RecyclerView.Adapter {
    private static List<String> list;
    private static Context context;

    public SienceIntroduceImgAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder viewHolder;
        viewHolder = new ViewHolderItem(LayoutInflater.from(context).inflate(R.layout.item_sience_introduce_img_list, viewGroup, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Glide.with(context).load(list.get(i))
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(context,8)))
                .into(((ViewHolderItem) viewHolder).ivImg);
     }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolderItem extends RecyclerView.ViewHolder {
        private ImageView ivImg;
        public ViewHolderItem(@NonNull View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.item_sience_introduce_img_iv);
        }
    }
}
