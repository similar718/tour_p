package cn.xmzt.www.intercom.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemGroupCarBinding;
import cn.xmzt.www.databinding.ItemGroupCarHeadBinding;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;

import java.util.List;


public class GroupCarAdapter extends BaseRecycleViewAdapter<Object, GroupCarAdapter.ItemHolder> {
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
            return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_group_car_head, parent, false));
        }else{
            return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_group_car, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.setViewDate(position);
    }

    public boolean isMultiEdit=false;//是否多选编辑
    public boolean isLeader=false;//是否有领队
    public int otherIndex=0;//其他车辆位置
    /**
     * 设置列表数据
     * @param datas1 领队
     * @param datas2 其他
     */
    public void setDatas(List datas1,List datas2) {
        this.datas.clear();
        if(datas1.size()>0){
            isLeader=true;
            this.datas.add("领队车辆");
            this.datas.addAll(datas1);
        }
        if(datas2.size()>0){
            this.datas.add("其它车辆");
            otherIndex=this.datas.size();
            this.datas.addAll(datas2);
        }

        notifyDataSetChanged();
    }
    /**
     * 设置列表数据
     * @param mCarBean 车辆
     */
    public void addData(GroupMemberBean mCarBean) {
        if(mCarBean.isLeader()){
            if(isLeader){
                datas.add(1,mCarBean);
            }else {
                this.datas.add(0,"领队车辆");
                datas.add(1,mCarBean);
            }
        }else {
            if(otherIndex>0){
                datas.add(otherIndex,mCarBean);
            }else {
                this.datas.add("其它车辆");
                otherIndex=this.datas.size();
                datas.add(otherIndex,mCarBean);
            }
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
                GroupMemberBean carBean=(GroupMemberBean)obj;
                carBean.setSelect(isSelectAll);
            }
        }
        notifyDataSetChanged();
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
            if(dbBinding instanceof ItemGroupCarBinding){
                ItemGroupCarBinding itemBinding= (ItemGroupCarBinding) dbBinding;
                /*itemBinding.tvChangeDriver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position=getAdapterPosition();
                        if(itemListener!=null){
                            itemListener.onItemClick(view,position);
                        }
                    }
                });*/
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
            if(dbBinding instanceof ItemGroupCarHeadBinding){
                ItemGroupCarHeadBinding itemBinding= (ItemGroupCarHeadBinding) dbBinding;
                if(item instanceof String){
                    itemBinding.setName((String) item);
                    if(position==0){
                        itemBinding.vSpace.setVisibility(View.GONE);
                    }else {
                        itemBinding.vSpace.setVisibility(View.VISIBLE);
                    }
                }
            }else if(dbBinding instanceof ItemGroupCarBinding){
                ItemGroupCarBinding itemBinding= (ItemGroupCarBinding) dbBinding;
                if(item instanceof GroupMemberBean){
                    GroupMemberBean carBean=(GroupMemberBean)item;
                    itemBinding.setItem(carBean);
//                    ViewGroup.LayoutParams d=itemBinding.ivEdit.getLayoutParams();
                    if(isMultiEdit){
                        itemBinding.ivEdit.setPadding(dp_4,dp_4,dp_4,dp_4);
                        if(carBean.isSelect()){
                            itemBinding.ivEdit.setImageResource(R.drawable.icon_check_yx_duigou);
                        }else {
                            itemBinding.ivEdit.setImageResource(R.drawable.icon_check_yx_un);
                        }
                    }else {
                        itemBinding.ivEdit.setPadding(0,0,0,0);
                        itemBinding.ivEdit.setImageResource(R.drawable.icon_edit_group);
                    }
                }
            }
        }
    }
}