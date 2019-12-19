package cn.xmzt.www.ticket.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.ticket.bean.TicketDetailBean;
import cn.xmzt.www.ticket.bean.TicketDetailListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 特价门票主页的Adapter
 */
public class TicketDetailTAdapter extends RecyclerView.Adapter {
    private List<TicketDetailBean> list;
    private Context context;

    public TicketDetailTAdapter(List<TicketDetailBean> list, Context context) {
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
        viewHolder = new ViewHolderItem(LayoutInflater.from(context).inflate(R.layout.item_ticket_detail, viewGroup, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
//        if (list.get(i).getType() == 1){ // 人气必玩
//            ((ViewHolderItem)viewHolder).rlRight.setVisibility(View.VISIBLE);
//            ((ViewHolderItem)viewHolder).tvTitle.setText("人气必玩");
//            ((ViewHolderItem)viewHolder).tvRightMore.setText("长沙全部景点");
//            ((ViewHolderItem)viewHolder).rvContent.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
//            SpecialTicketMustPlayAdapter adapter = new SpecialTicketMustPlayAdapter(list.get(i).getListmust(), context);
//            ((ViewHolderItem)viewHolder).rvContent.setAdapter(adapter);
//        } else if (list.get(i).getType() == 2){ // 精选主题
//            ((ViewHolderItem)viewHolder).rlRight.setVisibility(View.GONE);
//            ((ViewHolderItem)viewHolder).tvTitle.setText("精选主题");
//            ((ViewHolderItem)viewHolder).rvContent.setLayoutManager(new GridLayoutManager(context,5));
//            SpecialTicketTitleAdapter adapter = new SpecialTicketTitleAdapter(list.get(i).getListtitle(),context);
//            ((ViewHolderItem)viewHolder).rvContent.setAdapter(adapter);
//        } else if (list.get(i).getType() == 3){ // 热门景点
//            ((ViewHolderItem)viewHolder).rlRight.setVisibility(View.VISIBLE);
//            ((ViewHolderItem)viewHolder).tvTitle.setText("热门景点");
//            ((ViewHolderItem)viewHolder).tvRightMore.setText("附近景点");
//            ((ViewHolderItem)viewHolder).rvContent.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
//            SpecialTicketHotAdapter adapter = new SpecialTicketHotAdapter(list.get(i).getListhot(),context);
//            ((ViewHolderItem)viewHolder).rvContent.setAdapter(adapter);
//        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        ((ViewHolderItem)viewHolder).rvContent.setLayoutManager(layoutManager);
        DividerItemDecoration decor = new DividerItemDecoration(context, layoutManager.getOrientation());
        decor.setDrawable(ContextCompat.getDrawable(context,R.drawable.item_ticket_detail_recyleview_line));
        ((ViewHolderItem)viewHolder).rvContent.addItemDecoration(decor);

        List<TicketDetailListBean> listBeans = new ArrayList<>();
        listBeans.add(new TicketDetailListBean());
        listBeans.add(new TicketDetailListBean());
        TicketDetailAdapter adapter = new TicketDetailAdapter(listBeans,context);
        ((ViewHolderItem)viewHolder).rvContent.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class ViewHolderItem extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private ImageView ivLeftTop;
        private RecyclerView rvContent;
        public ViewHolderItem(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.item_ticket_detail_list_title_tv);
            ivLeftTop = itemView.findViewById(R.id.item_ticket_detail_list_title_iv);
            rvContent = itemView.findViewById(R.id.rv_ticket_detail_list);
        }
    }
}
