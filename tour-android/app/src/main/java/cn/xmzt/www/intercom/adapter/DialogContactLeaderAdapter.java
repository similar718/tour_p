package cn.xmzt.www.intercom.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemContactLeaderBinding;
import cn.xmzt.www.databinding.ItemScenicSpotListBinding;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.intercom.bean.GroupLeaderBean;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import cn.xmzt.www.selfdrivingtools.activity.ScenicFeedBackActivity;

/**
 * 联系领队Dialog Adapter
 */
public class DialogContactLeaderAdapter extends BaseRecycleViewAdapter<GroupLeaderBean, DialogContactLeaderAdapter.ItemHolder>{
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemContactLeaderBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_contact_leader, parent, false);
        return new ItemHolder(itemBinding);
    }

    private static Context mContext;

    public DialogContactLeaderAdapter(Context context){
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
    public GroupLeaderBean getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }
        return null;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemContactLeaderBinding itemBinding;

        public ItemHolder(ItemContactLeaderBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemListener!=null){
                        itemListener.onItemClick(view,getAdapterPosition());
                    }
                }
            });
        }
        public ItemContactLeaderBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemContactLeaderBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(final int position) {
            GroupLeaderBean item = datas.get(position);
            itemBinding.setItem(item);
            GlideUtil.loadImgCircle(itemBinding.ivAvatar,item.getAvatar());
        }
    }
}