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
import cn.xmzt.www.R;
import cn.xmzt.www.ticket.activity.SpecialTicketActivity;
import cn.xmzt.www.ticket.bean.SpecialTicketBean;

import java.util.List;

/**
 * 特价门票主页--精选主题Adapter
 */
public class SpecialTicketTitleAdapter extends RecyclerView.Adapter {
    private static List<SpecialTicketBean.SubjectListBean> list;
    private static Context context;

    public SpecialTicketTitleAdapter(List<SpecialTicketBean.SubjectListBean> list, Context context) {
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
        viewHolder = new ViewHolderItem(LayoutInflater.from(context).inflate(R.layout.item_special_ticket_title_land, viewGroup, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ViewHolderItem) viewHolder).tvTitle.setText(list.get(i).getSubjectName());
        Glide.with(context).load(list.get(i).getIconUrl()).into(((ViewHolderItem) viewHolder).ivImg);
     }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolderItem extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private ImageView ivImg;

        public ViewHolderItem(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.special_ticket_title_tv);
            ivImg = itemView.findViewById(R.id.special_ticket_title_iv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((SpecialTicketActivity) context).OnClickListener(list.get(getAdapterPosition()),2);
                }
            });
        }
    }
}
