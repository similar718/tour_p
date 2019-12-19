package cn.xmzt.www.selfdrivingtools.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemMsgGuideInfoBinding;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import cn.xmzt.www.selfdrivingtools.activity.GuideInfoActivity;
import cn.xmzt.www.selfdrivingtools.bean.MsgGuideListInfo;

/**
 * 共享导航——设置目的地界面的类型
 */
public class MsgGuideInfoAdapter extends BaseRecycleViewAdapter<MsgGuideListInfo, MsgGuideInfoAdapter.ItemHolder> {
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMsgGuideInfoBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_msg_guide_info, parent, false);
        return new ItemHolder(itemRouteBinding);
    }

    private static Context mContext;

    public MsgGuideInfoAdapter(Context context){
        mContext = context;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.setViewDate(position);
    }
    @Override
    public void setDatas(List datas) {
        super.setDatas(datas);
        notifyDataSetChanged();
    }

    public void addDatas(List datas) {
        if (this.datas == null) {
            setDatas(datas);
        } else {
            this.datas.addAll(datas);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return this.datas == null ? 0 : datas.size();
    }

    @Override
    public MsgGuideListInfo getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }
        return null;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemMsgGuideInfoBinding itemBinding;

        public ItemHolder(ItemMsgGuideInfoBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((GuideInfoActivity)mContext).OnListClickListener(getAdapterPosition(),datas.get(getAdapterPosition()).getType());
                }
            });
        }
        public ItemMsgGuideInfoBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemMsgGuideInfoBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(final int position) {
            MsgGuideListInfo info = datas.get(position);
            if (info.getType() == 1){ // 1美食指南
                itemBinding.rlBg.setBackgroundResource(R.drawable.guide_info_check_food_bg);
                itemBinding.ivImg.setImageResource(R.drawable.guide_info_check_food_icon);
                itemBinding.tvTitle.setText(info.getTitle());
                itemBinding.tvContent.setText(info.getContent());
                itemBinding.llBottom.setVisibility(View.GONE);
            } else if (info.getType() == 2){ // 2酒店入住指南
                itemBinding.rlBg.setBackgroundResource(R.drawable.guide_info_check_live_bg);
                itemBinding.ivImg.setImageResource(R.drawable.guide_info_check_live_icon);
                itemBinding.tvTitle.setText(info.getTitle());
                itemBinding.tvContent.setText(info.getContent());
                itemBinding.llBottom.setVisibility(View.VISIBLE);
                String data = info.getLiveTime() + (info.getLiveType() == 1 ? "之前" : "之后") +"可办理入住";
                itemBinding.tvOption.setText(data);
            } else if (info.getType() == 3){ // 3检查车辆
                itemBinding.rlBg.setBackgroundResource(R.drawable.guide_info_check_car_bg);
                itemBinding.ivImg.setImageResource(R.drawable.guide_info_check_car_icon);
                itemBinding.tvTitle.setText(info.getTitle());
                itemBinding.tvContent.setText(info.getContent());
                itemBinding.llBottom.setVisibility(View.GONE);
            } else if (info.getType() == 4){ // 4 出行清单
                itemBinding.rlBg.setBackgroundResource(R.drawable.guide_info_check_list_bg);
                itemBinding.ivImg.setImageResource(R.drawable.guide_info_check_list_icon);
                itemBinding.tvTitle.setText(info.getTitle());
                itemBinding.tvContent.setText(info.getContent());
                itemBinding.llBottom.setVisibility(View.GONE);
            }
        }
    }
}