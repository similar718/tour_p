package cn.xmzt.www.mine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.mine.bean.ShareBean;
import cn.xmzt.www.utils.DpUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author tanlei
 * @date 2019/8/16
 * @describe
 */

public class FriendInviteAdapter extends RecyclerView.Adapter<FriendInviteAdapter.FriendInviteViewHolder> {
    private List<ShareBean> list;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public FriendInviteAdapter(List<ShareBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public FriendInviteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new FriendInviteViewHolder(LayoutInflater.from(context).inflate(R.layout.item_friend_invite, null));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendInviteViewHolder friendInviteViewHolder, int i) {
        friendInviteViewHolder.tv_title.setText(list.get(i).getTitle());
        friendInviteViewHolder.tv_details.setText(list.get(i).getContent());
        GlideUtil.loadImage(friendInviteViewHolder.iv, list.get(i).getImage());
        if (list.get(i).isCheck()) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DpUtil.dp2px(context,97));
            friendInviteViewHolder.ll_layout.setLayoutParams(params);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params1.setMargins(DpUtil.dp2px(context,10),DpUtil.dp2px(context,4),DpUtil.dp2px(context,10),DpUtil.dp2px(context,4));
            friendInviteViewHolder.ll_layout_group.setLayoutParams(params1);
            friendInviteViewHolder.tv_title.setTextSize(14);
            friendInviteViewHolder.tv_details.setTextSize(12);
//            friendInviteViewHolder.ll_layout.setBackgroundResource(R.drawable.shape_friend_invite_bg_check);
        } else {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DpUtil.dp2px(context,86));
            friendInviteViewHolder.ll_layout.setLayoutParams(params);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params1.setMargins(DpUtil.dp2px(context,20),DpUtil.dp2px(context,4),DpUtil.dp2px(context,20),DpUtil.dp2px(context,4));
            friendInviteViewHolder.ll_layout_group.setLayoutParams(params1);
            friendInviteViewHolder.tv_title.setTextSize(13);
            friendInviteViewHolder.tv_details.setTextSize(11);
//            friendInviteViewHolder.ll_layout.setBackgroundResource(R.drawable.shape_friend_invite_bg);
        }
        friendInviteViewHolder.ll_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getOnItemClickListener() != null) {
                    onItemClickListener.onItemClick(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class FriendInviteViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_details;
        ImageView iv;
        LinearLayout ll_layout,ll_layout_group;

        public FriendInviteViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_details = itemView.findViewById(R.id.tv_details);
            iv = itemView.findViewById(R.id.iv);
            ll_layout = itemView.findViewById(R.id.ll_layout);
            ll_layout_group = itemView.findViewById(R.id.ll_layout_group);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
