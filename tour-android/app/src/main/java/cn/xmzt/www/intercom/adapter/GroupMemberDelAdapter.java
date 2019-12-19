package cn.xmzt.www.intercom.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemGroupMemberDelBinding;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;

import java.util.List;


public class GroupMemberDelAdapter extends BaseRecycleViewAdapter<GroupMemberBean, GroupMemberDelAdapter.ItemHolder>{

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_group_member_del, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.setViewDate(position);
    }

    /**
     * 设置列表数据
     * @param datas 群成员
     */
    public void setDatas(List datas) {
        super.setDatas(datas);
        notifyDataSetChanged();
    }

    public String getSelectIds() {
        StringBuilder sb=new StringBuilder();
        for(GroupMemberBean memberBean:datas){
            if(memberBean.isSelect()){
                if(sb.length()>0){
                    sb.append(",");
                    sb.append(""+memberBean.getUserId());
                }else {
                    sb.append(""+memberBean.getUserId());
                }
            }
        }
        return sb.toString();
    }
    public void selectAll(boolean isSelectAll) {
        for(GroupMemberBean memberBean:datas){
            if(memberBean.getRole()!=1){
                memberBean.setSelect(isSelectAll);
            }
        }
        notifyDataSetChanged();
    }
    public boolean isSelectAll() {
        boolean isSelectAll=true;
        for(GroupMemberBean memberBean:datas){
            if(!memberBean.isSelect()){
                isSelectAll=false;
                break;
            }
        }
        return isSelectAll;
    }
    @Override
    public int getItemCount() {
        return this.datas == null ? 0 : datas.size();
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
        int dp_4=8;
        public ItemHolder(DB dbBinding) {
            super(dbBinding.getRoot());
            this.dbBinding = dbBinding;
            dp_4=dbBinding.getRoot().getResources().getDimensionPixelOffset(R.dimen.dp_4);
            if(dbBinding instanceof ItemGroupMemberDelBinding){
                ItemGroupMemberDelBinding itemBinding= (ItemGroupMemberDelBinding) dbBinding;
                itemBinding.ivEdit.setOnClickListener(new View.OnClickListener() {
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
            GroupMemberBean member = getItem(position);
            if(dbBinding instanceof ItemGroupMemberDelBinding){
                ItemGroupMemberDelBinding itemBinding= (ItemGroupMemberDelBinding) dbBinding;
                itemBinding.setItem(member);
                itemBinding.ivEdit.setPadding(dp_4,dp_4,dp_4,dp_4);
                if(member.isSelect()){
                    itemBinding.ivEdit.setImageResource(R.drawable.icon_check_yx_duigou);
                }else {
                    itemBinding.ivEdit.setImageResource(R.drawable.icon_check_yx_un);
                }
            }
        }
    }
}