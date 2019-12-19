package cn.xmzt.www.home.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.databinding.ItemCustomizeBinding;
import cn.xmzt.www.databinding.ItemCustomizeHeadBinding;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.home.bean.CustomizeBean;
import cn.xmzt.www.intercom.bean.UserBasicInfoBean;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;

import java.util.List;


public class CustomizeAdapter extends BaseRecycleViewAdapter<CustomizeBean, CustomizeAdapter.ItemHolder> {
    private final int TYPE_HEAD = 1;
    private final int TYPE_ITEM = 2;
    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_HEAD;
        }else {
            return TYPE_ITEM;
        }
    }
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemHolder holder=null;
        if(viewType==TYPE_HEAD){
            holder= new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_customize_head_new, parent, false));
        }else {
            holder= new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_customize, parent, false));
        }
        return holder;
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
        if(this.datas == null||this.datas.size()==0){
            return 0;
        }else {
            return datas.size()+1;
        }
    }

    @Override
    public CustomizeBean getItem(int position) {
        int index=position-1;
        if(index>=0&&index<datas.size()){
            return datas.get(index);
        }
        return null;
    }

    class ItemHolder<DB extends ViewDataBinding> extends RecyclerView.ViewHolder {
        private DB dbBinding;
        public ItemHolder(DB dbBinding) {
            super(dbBinding.getRoot());
            this.dbBinding = dbBinding;
            if(dbBinding instanceof ItemCustomizeBinding){
                ItemCustomizeBinding itemBinding= (ItemCustomizeBinding) dbBinding;
                itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
            }
        }
        protected void setViewDate(int position) {
            if(dbBinding instanceof ItemCustomizeBinding){
                ItemCustomizeBinding itemBinding= (ItemCustomizeBinding) dbBinding;
                CustomizeBean customizeBean=getItem(position);
                itemBinding.setItem(customizeBean);
            }
        }
    }

}