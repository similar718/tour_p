package cn.xmzt.www.route.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemCouponUnUseBinding;
import cn.xmzt.www.databinding.ItemCouponUseBinding;
import cn.xmzt.www.mine.bean.MyCouponBean;
import cn.xmzt.www.utils.MathUtil;

import java.util.List;


public class CouponUseAdapter extends BaseRecycleViewAdapter<MyCouponBean, CouponUseAdapter.ItemHolder>{
    private final int TYPE_ITEM_HEAD = 1;
    private final int TYPE_ITEM = 2;
    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_ITEM_HEAD;
        }
        return TYPE_ITEM;
    }
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_ITEM_HEAD){
            return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_coupon_un_use, parent, false));
        }else {
            return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_coupon_use, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.setViewDate(position);
    }

    public int disabledIndex=0;//不可用的位置
    public int usableSize=0;//可用优惠券数量

    /**
     * 设置列表数据
     * @param datas1 可用优惠券
     * @param datas2 不可用优惠券
     */
    public void setDatas(List datas1,List datas2) {
        super.setDatas(datas1);
        if(datas1!=null){
            usableSize=datas1.size();
        }
        disabledIndex=usableSize+1;
        this.datas.addAll(datas2);
        notifyDataSetChanged();
    }
    public void addDatas(List datas1,List datas2) {
        this.datas.addAll(disabledIndex,datas1);
        disabledIndex=disabledIndex+datas1.size();
        this.datas.addAll(datas2);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(this.datas == null){
            return 0;
        }else {
            return this.datas.size()>0 ? datas.size()+1 : 0;
        }
    }

    @Override
    public MyCouponBean getItem(int position) {
        int index=position-1;
        if(index>=0&&index<datas.size()){
            return datas.get(index);
        }
        return null;
    }
    public int selectPosition=0;
    class ItemHolder<DB extends ViewDataBinding> extends RecyclerView.ViewHolder{
        private DB dbBinding;
        public ItemHolder(DB dbBinding) {
            super(dbBinding.getRoot());
            this.dbBinding = dbBinding;
            if(dbBinding instanceof ItemCouponUnUseBinding){
                ItemCouponUnUseBinding itemBinding= (ItemCouponUnUseBinding) dbBinding;
                itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position=getAdapterPosition();
                        if(position!=selectPosition){
                            int temp=selectPosition;
                            selectPosition=position;
                            notifyItemChanged(selectPosition);
                            notifyItemChanged(temp);
                        }
                    }
                });
            }else if(dbBinding instanceof ItemCouponUseBinding){
                ItemCouponUseBinding itemBinding= (ItemCouponUseBinding) dbBinding;
                itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position=getAdapterPosition();
                        MyCouponBean item = getItem(position);
                        if(item!=null&&!item.isUsable()){
                            return;
                        }
                        if(position!=selectPosition){
                            int temp=selectPosition;
                            selectPosition=position;
                            notifyItemChanged(selectPosition);
                            notifyItemChanged(temp);
                        }
                    }
                });
            }

        }
        protected void setViewDate(int position) {
            MyCouponBean item = getItem(position);
            if(dbBinding instanceof ItemCouponUnUseBinding){
                ItemCouponUnUseBinding itemBinding= (ItemCouponUnUseBinding) dbBinding;
                if(selectPosition==position){
                    itemBinding.ivCheck.setSelected(true);
                }else {
                    itemBinding.ivCheck.setSelected(false);
                }
            }else if(dbBinding instanceof ItemCouponUseBinding){
                ItemCouponUseBinding itemBinding= (ItemCouponUseBinding) dbBinding;
                String maxSubtract=MathUtil.formatDouble(item.getMaxSubtract(),2);
                itemBinding.tvPrice.setText(maxSubtract);
                String minConsume=MathUtil.formatDouble(item.getMinConsume(),2);
                itemBinding.tvUsable.setText("满"+minConsume+"元可用");
                itemBinding.tvTime.setText(item.getEffectDate()+"-"+item.getExpireDate());
                itemBinding.tvTitle.setText(item.getTitle()==null?"":item.getTitle());
                if(item.isUsable()){
                    itemBinding.ivCheck.setImageResource(R.drawable.checkbox_yx_selector);
                    if(selectPosition==position){
                        itemBinding.ivCheck.setSelected(true);
                    }else {
                        itemBinding.ivCheck.setSelected(false);
                    }
                }else {
                    itemBinding.ivCheck.setSelected(false);
                    itemBinding.ivCheck.setImageResource(R.drawable.icon_coupon_disable);
                }
                if(position<disabledIndex){
                    itemBinding.leftLayout.setBackgroundResource(R.drawable.coupon_bg_orange_left_c);
                    itemBinding.tvTypeName.setText("优惠券");
                    if(position==1){
                        itemBinding.tvTypeName.setVisibility(View.VISIBLE);
                    }else {
                        itemBinding.tvTypeName.setVisibility(View.GONE);
                    }
                }else {
                    itemBinding.leftLayout.setBackgroundResource(R.drawable.coupon_gray_bg_left_c);
                    itemBinding.tvTypeName.setText("本产品不可使用优惠券");
                    if(position==disabledIndex){
                        itemBinding.tvTypeName.setVisibility(View.VISIBLE);
                    }else {
                        itemBinding.tvTypeName.setVisibility(View.GONE);
                    }
                }
            }
        }
    }
}