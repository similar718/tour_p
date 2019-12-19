package cn.xmzt.www.ticket.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.ticket.activity.TicketDetailActivity;
import cn.xmzt.www.ticket.bean.TicketDetailListBean;

import java.util.List;

/**
 * 特价门票主页--精选主题Adapter
 */
public class TicketDetailAdapter extends RecyclerView.Adapter {
    private static List<TicketDetailListBean> list;
    private static Context context;

    public TicketDetailAdapter(List<TicketDetailListBean> list, Context context) {
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
        viewHolder = new ViewHolderItem(LayoutInflater.from(context).inflate(R.layout.item_ticket_detail_list, viewGroup, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
//        ((ViewHolderItem) viewHolder).tvTitle.setText(list.get(i).getHomeName());
//        Glide.with(context).load(list.get(i).getAdvPic()).into(((ViewHolderItem) viewHolder).ivImg);
     }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolderItem extends RecyclerView.ViewHolder {
        private TextView tvPrice,tvYuDing,tvReXiao,tvTitle,tvJiuJie,tvYuDingTime,tvTiaoJianTui,tvSuiShiTui,tvRuYuan,tvDingPiao;
        public ViewHolderItem(@NonNull View itemView) {
            super(itemView);
            tvPrice = itemView.findViewById(R.id.item_ticket_detail_list_price_tv);
            tvYuDing = itemView.findViewById(R.id.item_ticket_detail_list_yuding_tv); // 预定按钮
            tvReXiao = itemView.findViewById(R.id.item_ticket_detail_list_left_tv); // 热销
            tvTitle = itemView.findViewById(R.id.item_ticket_detail_list_title_tv);
            tvJiuJie = itemView.findViewById(R.id.item_ticket_detail_list_second_tv);  // 酒节
            tvYuDingTime = itemView.findViewById(R.id.item_ticket_detail_list_yuding_time_tv);  // 预定时间
            tvTiaoJianTui = itemView.findViewById(R.id.item_ticket_detail_list_tjt_tv);  // 条件退
            tvSuiShiTui = itemView.findViewById(R.id.item_ticket_detail_list_tjt_tv);  // 随时退 两个只能存一个
            tvRuYuan = itemView.findViewById(R.id.item_ticket_detail_list_ru_tv);  // 入园
            tvDingPiao = itemView.findViewById(R.id.item_ticket_detail_list_ru_read_tv);  // 订票须知

            tvYuDing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((TicketDetailActivity) context).OnClickListener(list.get(getAdapterPosition()),1);
                }
            });

            tvDingPiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((TicketDetailActivity) context).OnClickListener(list.get(getAdapterPosition()),2);
                }
            });


        }
    }
}
