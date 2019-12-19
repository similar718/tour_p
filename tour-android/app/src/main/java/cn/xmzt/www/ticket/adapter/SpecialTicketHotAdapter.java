package cn.xmzt.www.ticket.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import cn.xmzt.www.R;
import cn.xmzt.www.ticket.activity.SpecialTicketActivity;
import cn.xmzt.www.ticket.bean.SpecialTicketBean;
import cn.xmzt.www.view.RoundedCorners;

import java.util.List;

/**
 * 特价门票主页--热门景点Adapter
 */
public class SpecialTicketHotAdapter extends RecyclerView.Adapter {
    private static List<SpecialTicketBean.HotBean> list;
    public static Context context;

    public SpecialTicketHotAdapter(List<SpecialTicketBean.HotBean> list, Context context) {
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
        viewHolder = new ViewHolderItem(LayoutInflater.from(context).inflate(R.layout.item_special_ticket_hot, viewGroup, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ViewHolderItem) viewHolder).tvPrice.setText(list.get(i).getMinOnlineOrderPrice()+"");
        ((ViewHolderItem) viewHolder).tvTitle.setText(list.get(i).getScenicName());
        ((ViewHolderItem) viewHolder).tvMYD.setText("98%满意度");
        ((ViewHolderItem) viewHolder).tvAddress.setText(list.get(i).getScenicAddress());
        Glide.with(context).load(list.get(i).getPhotoUrl())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(context)))
                .into(((ViewHolderItem) viewHolder).ivImg);
     }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolderItem extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvPrice,tvMYD,tvAddress;
        private ImageView ivImg;

        public ViewHolderItem(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.item_special_ticket_hot_name_tv);
            ivImg = itemView.findViewById(R.id.item_special_ticket_hot_img_iv);
            tvPrice = itemView.findViewById(R.id.item_special_ticket_hot_price_tv);
            tvMYD = itemView.findViewById(R.id.item_special_ticket_hot_my_tv);
            tvAddress = itemView.findViewById(R.id.item_special_ticket_hot_address_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((SpecialTicketActivity) context).OnClickListener(list.get(getAdapterPosition()),3);
                }
            });
        }
    }
}
