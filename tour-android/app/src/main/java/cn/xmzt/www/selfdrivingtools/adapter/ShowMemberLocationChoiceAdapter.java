package cn.xmzt.www.selfdrivingtools.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemShowMemberLocationChoiceBinding;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.intercom.bean.MyTalkGroupBean;
import cn.xmzt.www.intercom.bean.MyTalkGroupsBean;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import cn.xmzt.www.selfdrivingtools.activity.ScenicSpotMapActivity;

/**
 * 选择显示群内消息
 */
public class ShowMemberLocationChoiceAdapter extends BaseRecycleViewAdapter<MyTalkGroupBean, ShowMemberLocationChoiceAdapter.ItemHolder> {

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemShowMemberLocationChoiceBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_show_member_location_choice, parent, false);
        return new ItemHolder(itemRouteBinding);
    }
    private static Context mContext;

    public ShowMemberLocationChoiceAdapter(Context context){
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
    public MyTalkGroupBean getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }
        return null;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemShowMemberLocationChoiceBinding itemBinding;
        public ItemHolder(ItemShowMemberLocationChoiceBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ScenicSpotMapActivity)mContext).OnChangeGroupInfo(datas.get(getAdapterPosition()));
                }
            });
        }
        public ItemShowMemberLocationChoiceBinding getBinding() {
            return itemBinding;
        }
        public void setBinding(ItemShowMemberLocationChoiceBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(final int position) {
            MyTalkGroupBean item = datas.get(position);
            itemBinding.setItem(item);
            GlideUtil.loadImgCircle(itemBinding.ivGroupHead,item.getAvatar()); // 头像
            //群内名称
            itemBinding.tvGroupName.setText(item.getTitle());
            // 群内成员数量
            itemBinding.tvGroupNum.setText("共"+item.getMemberNum()+"位成员");
            // 是否是最后一行
            if (position == (datas.size()-1)){
                itemBinding.tvLine.setVisibility(View.GONE);
            } else {
                itemBinding.tvLine.setVisibility(View.VISIBLE);
            }
            // 是否选中
            if (item.isSelect()){
                itemBinding.ivSelect.setVisibility(View.VISIBLE);
            } else {
                itemBinding.ivSelect.setVisibility(View.GONE);
            }
        }
    }
}