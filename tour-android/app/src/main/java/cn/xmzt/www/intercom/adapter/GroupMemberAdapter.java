package cn.xmzt.www.intercom.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemGroupMemberBinding;
import cn.xmzt.www.databinding.ItemGroupMemberHeadBinding;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;

import java.util.List;


public class GroupMemberAdapter extends BaseRecycleViewAdapter<Object, GroupMemberAdapter.ItemHolder> {
    private final int TYPE_ITEM_HEAD = 1;
    private final int TYPE_ITEM = 2;
    @Override
    public int getItemViewType(int position) {
        Object obj=getItem(position);
        if(obj instanceof String){
            return TYPE_ITEM_HEAD;
        }
        return TYPE_ITEM;
    }
    public boolean isSingleShow(int position){
        return false;
    }
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_ITEM_HEAD){
            return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_group_member_head, parent, false));
        }else{
            return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_group_member, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.setViewDate(position);
    }

    public int startQZIndex=0;//开始群群主开始位置
    public int startLDIndex=0;//开始群领队开始位置
    public int startCYIndex=0;//开始群成员开始位置

    public int qzSize=0;//群主数
    public int ldSize=0;//群领队数
    public int cySize=0;//群成员数
    /**
     * 设置列表数据
     * @param datas0 群主
     * @param datas1 群领队
     * @param datas2 群成员
     */
    public void setDatas(List datas0,List datas1,List datas2) {
        this.datas.clear();
        qzSize=datas0.size();
        ldSize=datas1.size();
        cySize=datas2.size();
        if(qzSize>0){
            this.datas.add("群主");
            startQZIndex=1;
            this.datas.addAll(datas0);
        }
        if(ldSize>0){
            this.datas.add("群领队");
            startLDIndex=this.datas.size();
            this.datas.addAll(datas1);
        }
        if(cySize>0){
            this.datas.add("群成员");
            startCYIndex=this.datas.size();
            this.datas.addAll(datas2);
        }
        notifyDataSetChanged();
    }
    /**
     * 添加列表数据
     * @param member
     */
    public void addData(GroupMemberBean member) {
        if(member.getRole()==1){
            if(qzSize>0){
                datas.add(1,member);
            }else {
                this.datas.add("群主");
                startQZIndex=1;
                datas.add(1,member);
            }
            qzSize=qzSize+1;
            if(startLDIndex>0){
                startLDIndex=startLDIndex+1;
            }
            if(startCYIndex>0){
                startCYIndex=startCYIndex+1;
            }
        }else if(member.isLeader()){
            if(startLDIndex>0){
                datas.add(startLDIndex,member);
            }else {
                this.datas.add("群领队");
                if(qzSize>0){
                    startLDIndex=qzSize+2;
                }else {
                    startLDIndex=1;
                }
                datas.add(startLDIndex,member);
            }
            ldSize=ldSize+1;
            if(startCYIndex>0){
                startCYIndex=startCYIndex+1;
            }
        }else {
            if(startCYIndex>0){
                datas.add(startLDIndex,member);
            }else {
                this.datas.add("群成员");
                if(qzSize>0&&ldSize>0){
                    startLDIndex=qzSize+3;
                }else if(qzSize>0||ldSize>0){
                    startLDIndex=qzSize+2;
                }else {
                    startLDIndex=1;
                }
                datas.add(startLDIndex,member);
            }
            cySize=cySize+1;
        }
        notifyDataSetChanged();
    }
    public String getSelectDriverIds() {
        StringBuilder sb=new StringBuilder();
        for(Object obj:datas){
            if(obj instanceof GroupMemberBean){
                GroupMemberBean carBean=(GroupMemberBean)obj;
                if(carBean.isSelect()){
                    if(sb.length()>0){
                        sb.append(",");
                        sb.append(""+carBean.getUserId());
                    }else {
                        sb.append(""+carBean.getUserId());
                    }
                }
            }
        }
        return sb.toString();
    }
    public void selectAll(boolean isSelectAll) {
        for(Object obj:datas){
            if(obj instanceof GroupMemberBean){
                GroupMemberBean memberBean=(GroupMemberBean)obj;
                if(memberBean.getRole()!=1){
                    memberBean.setSelect(isSelectAll);
                }
            }
        }
        notifyDataSetChanged();
    }
    public boolean isSelectAll() {
        boolean isSelectAll=true;
        for(Object obj:datas){
            if(obj instanceof GroupMemberBean){
                GroupMemberBean memberBean=(GroupMemberBean)obj;
                if(memberBean.getRole()!=1){
                    if(!memberBean.isSelect()){
                        isSelectAll=false;
                        break;
                    }
                }
            }
        }
        return isSelectAll;
    }
    @Override
    public int getItemCount() {
        return this.datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        if(position>=0&&position<datas.size()){
            return datas.get(position);
        }
        return null;
    }
    public int selectPosition=0;
    class ItemHolder<DB extends ViewDataBinding> extends RecyclerView.ViewHolder{
        private DB dbBinding;
        int dp_4=8;
        public ItemHolder(DB dbBinding) {
            super(dbBinding.getRoot());
            this.dbBinding = dbBinding;
            dp_4=dbBinding.getRoot().getResources().getDimensionPixelOffset(R.dimen.dp_4);
            if(dbBinding instanceof ItemGroupMemberBinding){
                ItemGroupMemberBinding itemBinding= (ItemGroupMemberBinding) dbBinding;
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
            Object item = getItem(position);
            if(dbBinding instanceof ItemGroupMemberHeadBinding){
                ItemGroupMemberHeadBinding itemBinding= (ItemGroupMemberHeadBinding) dbBinding;
                if(item instanceof String){
                    itemBinding.setName((String) item);
                    if(position==0){
                        itemBinding.vSpace.setVisibility(View.GONE);
                    }else {
                        itemBinding.vSpace.setVisibility(View.VISIBLE);
                    }
                }
            }else if(dbBinding instanceof ItemGroupMemberBinding){
                ItemGroupMemberBinding itemBinding= (ItemGroupMemberBinding) dbBinding;
                if(item instanceof GroupMemberBean){
                    GroupMemberBean member=(GroupMemberBean)item;
                    itemBinding.setItem(member);
                    if(member.getRole()==1){
                        itemBinding.ivEdit.setVisibility(View.GONE);
                    }else {
                        itemBinding.ivEdit.setVisibility(View.VISIBLE);
                    }
                    LinearLayout.LayoutParams mLayoutParams= (LinearLayout.LayoutParams) itemBinding.vLine.getLayoutParams();
                    if(startQZIndex==position||startLDIndex==position||startCYIndex==position){
                        mLayoutParams.leftMargin=0;
                    }
                    if(member.isLeader()){
                        itemBinding.ivEdit.setImageResource(R.drawable.icon_cancel_leader);
                    }else {
                        itemBinding.ivEdit.setImageResource(R.drawable.icon_set_leader);
                    }
                }
            }
        }
    }
}