package cn.xmzt.www.intercom.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemGroupMemberListAddBinding;
import cn.xmzt.www.databinding.ItemGroupMemberListBinding;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;

import java.util.List;


public class GroupMemberListAdapter extends BaseRecycleViewAdapter<GroupMemberBean, GroupMemberListAdapter.ItemHolder> {
    private final int TYPE_ITEM = 1;
    private final int TYPE_ITEM_ADD = 2;
    @Override
    public int getItemViewType(int position) {
       if(position==getItemCount()-1){
           return TYPE_ITEM_ADD;
       }else {
           return TYPE_ITEM;
       }
    }
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_ITEM_ADD){
            return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_group_member_list_add, parent, false));
        }else {
            return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_group_member_list, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.setViewDate(position);
    }
    /**
     * 设置列表数据
     * @param datas0 群主
     * @param datas1 群领队
     * @param datas2 群成员
     */
    public void setDatas(List datas0,List datas1,List datas2) {
        this.datas.addAll(datas0);
        this.datas.addAll(datas1);
        this.datas.addAll(datas2);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.datas == null ? 1 : datas.size()+1;
    }

    @Override
    public GroupMemberBean getItem(int position) {
        if(position>=0&&position<datas.size()){
            return datas.get(position);
        }
        return null;
    }
    public int selectPosition=0;
    class ItemHolder<DB extends ViewDataBinding> extends RecyclerView.ViewHolder{
        private DB dbBinding;
        public ItemHolder(DB dbBinding) {
            super(dbBinding.getRoot());
            this.dbBinding = dbBinding;
            if(dbBinding instanceof ItemGroupMemberListAddBinding){
                ItemGroupMemberListAddBinding itemBinding= (ItemGroupMemberListAddBinding) dbBinding;
                itemBinding.ivAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
            }else if(dbBinding instanceof ItemGroupMemberListBinding){
                ItemGroupMemberListBinding itemBinding= (ItemGroupMemberListBinding) dbBinding;
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
            GroupMemberBean item = getItem(position);
            if(dbBinding instanceof ItemGroupMemberListBinding){
                ItemGroupMemberListBinding itemBinding= (ItemGroupMemberListBinding) dbBinding;
                itemBinding.setItem(item);
            }
        }
    }
}