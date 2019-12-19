package cn.xmzt.www.intercom.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemGroupDriverBinding;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;

import java.util.List;


public class SelectDriverAdapter extends BaseRecycleViewAdapter<GroupMemberBean, SelectDriverAdapter.ItemHolder> {

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_group_driver, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.setViewDate(position);
    }

    public void setDatas(List datas) {
        super.setDatas(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.datas == null ? 0 : datas.size();
    }
    public int selectPosition=-1;
    public GroupMemberBean getSelectItem() {
        return getItem(selectPosition);
    }
    @Override
    public GroupMemberBean getItem(int position) {
        if(position>=0&&position<datas.size()){
            return datas.get(position);
        }
        return null;
    }
    class ItemHolder<DB extends ViewDataBinding> extends RecyclerView.ViewHolder{
        private DB dbBinding;
        public ItemHolder(DB dbBinding) {
            super(dbBinding.getRoot());
            this.dbBinding = dbBinding;
            if(dbBinding instanceof ItemGroupDriverBinding){
                ItemGroupDriverBinding itemBinding= (ItemGroupDriverBinding) dbBinding;
                itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position=getAdapterPosition();
                        if(itemListener!=null){
                            itemListener.onItemClick(view,position);
                        }
                    }
                });
            }

        }
        protected void setViewDate(int position) {
            GroupMemberBean driver = getItem(position);
            if(dbBinding instanceof ItemGroupDriverBinding){
                ItemGroupDriverBinding itemBinding= (ItemGroupDriverBinding) dbBinding;
                itemBinding.setItem(driver);
                if(driver.isSelect()){
                    itemBinding.ivCheck.setVisibility(View.VISIBLE);
                }else {
                    itemBinding.ivCheck.setVisibility(View.GONE);
                }
            }
        }
    }
}