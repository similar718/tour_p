package cn.xmzt.www.intercom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.intercom.bean.MyTalkGroupBean;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * 首页菜单 推荐路线
 * Created by Averysk.
 */
public class TalkGroupListAdapter extends RecyclerView.Adapter<TalkGroupListAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<MyTalkGroupBean> mDatas = new ArrayList<>();

    private OnItemClickListener mClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View view, RecyclerView.ViewHolder vh);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    public TalkGroupListAdapter(Context context, List<MyTalkGroupBean> datas) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    public void refreshData(List<MyTalkGroupBean> datas) {
        mDatas = datas;
        this.notifyDataSetChanged();
    }

    /**
     * 创建ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_talk_group, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onItemClick(viewHolder.getAdapterPosition(), v, viewHolder);
                }
            }
        });
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        MyTalkGroupBean entity = mDatas.get(i);
        GlideUtil.loadImgTopC(viewHolder.iv_avatar,entity.getAvatar());
        viewHolder.tv_title.setText(entity.getTitle());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_avatar;
        TextView tv_title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
            tv_title = itemView.findViewById(R.id.tv_title);
        }


    }
}
