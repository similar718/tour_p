package cn.xmzt.www.ticket.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.ticket.bean.SpecialTicketResultBean;

import java.util.List;

/**
 * 特价门票主页的Adapter
 */
public class SpecialTicketResultAdapter extends RecyclerView.Adapter {
    private List<SpecialTicketResultBean> list;
    private Context context;

    public SpecialTicketResultAdapter(List<SpecialTicketResultBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder viewHolder;
        viewHolder = new ViewHolderItem(LayoutInflater.from(context).inflate(R.layout.item_special_ticket, viewGroup, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (list.get(i).getType() == 1){ // 人气必玩
            ((ViewHolderItem)viewHolder).rlRight.setVisibility(View.VISIBLE);
            ((ViewHolderItem)viewHolder).tvTitle.setText("人气必玩");
            ((ViewHolderItem)viewHolder).tvRightMore.setText(list.get(i).getTitle()+"全部景点");
            ((ViewHolderItem)viewHolder).rvContent.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            SpecialTicketMustPlayAdapter adapter = new SpecialTicketMustPlayAdapter(list.get(i).getListmust(), context);
            ((ViewHolderItem)viewHolder).rvContent.setAdapter(adapter);
        } else if (list.get(i).getType() == 2){ // 精选主题
            ((ViewHolderItem)viewHolder).rlRight.setVisibility(View.GONE);
            ((ViewHolderItem)viewHolder).tvTitle.setText("精选主题");
            ((ViewHolderItem)viewHolder).rvContent.setLayoutManager(new GridLayoutManager(context,5));
            SpecialTicketTitleAdapter adapter = new SpecialTicketTitleAdapter(list.get(i).getListtitle(),context);
            ((ViewHolderItem)viewHolder).rvContent.setAdapter(adapter);
        } else if (list.get(i).getType() == 3){ // 热门景点
            ((ViewHolderItem)viewHolder).rlRight.setVisibility(View.VISIBLE);
            ((ViewHolderItem)viewHolder).tvTitle.setText("热门景点");
            ((ViewHolderItem)viewHolder).tvRightMore.setText("附近景点");
            ((ViewHolderItem)viewHolder).rvContent.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            SpecialTicketHotAdapter adapter = new SpecialTicketHotAdapter(list.get(i).getListhot(),context);
            ((ViewHolderItem)viewHolder).rvContent.setAdapter(adapter);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolderItem extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvRightMore;
        private RelativeLayout rlRight;
        private RecyclerView rvContent;

        public ViewHolderItem(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.item_special_ticket_title_tv);
            rlRight = itemView.findViewById(R.id.item_special_ticket_right_more_rl);
            tvRightMore = itemView.findViewById(R.id.item_special_ticket_right_more_tv);
            rvContent = itemView.findViewById(R.id.rv_ticket_list);
        }
    }
}
