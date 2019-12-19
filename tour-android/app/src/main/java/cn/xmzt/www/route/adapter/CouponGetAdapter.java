package cn.xmzt.www.route.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.bean.CouponBean;
import cn.xmzt.www.databinding.ItemCouponGetBinding;
import cn.xmzt.www.utils.MathUtil;

import java.util.List;


public class CouponGetAdapter extends BaseRecycleViewAdapter<CouponBean, CouponGetAdapter.ItemHolder>{

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCouponGetBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_coupon_get, parent, false);
        return new ItemHolder(itemRouteBinding);
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
    public CouponBean getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }

        return null;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        ItemCouponGetBinding itemBinding;
        public ItemHolder(ItemCouponGetBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.tvGet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemListener!=null){
                        int position=getAdapterPosition();
                        itemListener.onItemClick(view,position);
                    }
                }
            });
            itemBinding.ivDownUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemListener!=null){
                        int position=getAdapterPosition();
                        CouponBean mCouponBean=getItem(position);
                        if(itemBinding.ivDownUp.isSelected()){
                            itemBinding.ivDownUp.setSelected(false);
                            itemBinding.tvRemark.setVisibility(View.GONE);
                            itemBinding.topLayout.setBackgroundResource(R.drawable.coupon_bg_white_left_c);
                            if(mCouponBean.isReceived()){//已经领取
                                itemBinding.leftLayout.setBackgroundResource(R.drawable.coupon_gray_bg_left_c);
                            }else {
                                itemBinding.leftLayout.setBackgroundResource(R.drawable.coupon_bg_orange_left_c);
                            }
                        }else {
                            itemBinding.ivDownUp.setSelected(true);
                            itemBinding.tvRemark.setVisibility(View.VISIBLE);
                            itemBinding.topLayout.setBackgroundResource(R.drawable.coupon_bg_white_top_left_c);
                            if(mCouponBean.isReceived()){//已经领取
                                itemBinding.leftLayout.setBackgroundResource(R.drawable.coupon_gray_bg_top_left_c);
                            }else {
                                itemBinding.leftLayout.setBackgroundResource(R.drawable.coupon_bg_orange_top_left_c);
                            }
                        }
                    }
                }
            });
        }
        protected void setViewDate(int position) {
            CouponBean item = datas.get(position);
            itemBinding.setItem(item);
            String price=MathUtil.formatDouble(item.getMaxSubtract(),2);
            itemBinding.tvPrice.setText(price);
            String minConsume=MathUtil.formatDouble(item.getMinConsume(),2);
            itemBinding.tvUsable.setText("满"+minConsume+"元可用");
            itemBinding.tvTime.setText(item.getBeginDate()+"-"+item.getEndDate());
            itemBinding.tvRemark.setText("领取说明："+item.getDetail());
            if(itemBinding.ivDownUp.isSelected()){
                itemBinding.topLayout.setBackgroundResource(R.drawable.coupon_bg_white_left_c);
                if(item.isReceived()){//已经领取
                    itemBinding.leftLayout.setBackgroundResource(R.drawable.coupon_gray_bg_left_c);
                    itemBinding.ivReceived.setVisibility(View.VISIBLE);
                    itemBinding.tvGet.setVisibility(View.GONE);
                }else {
                    itemBinding.leftLayout.setBackgroundResource(R.drawable.coupon_bg_orange_left_c);
                    itemBinding.ivReceived.setVisibility(View.GONE);
                    itemBinding.tvGet.setVisibility(View.VISIBLE);
                }
            }else {
                itemBinding.topLayout.setBackgroundResource(R.drawable.coupon_bg_white_top_left_c);
                if(item.isReceived()){//已经领取
                    itemBinding.leftLayout.setBackgroundResource(R.drawable.coupon_gray_bg_top_left_c);
                    itemBinding.ivReceived.setVisibility(View.VISIBLE);
                    itemBinding.tvGet.setVisibility(View.GONE);
                }else {
                    itemBinding.leftLayout.setBackgroundResource(R.drawable.coupon_bg_orange_top_left_c);
                    itemBinding.ivReceived.setVisibility(View.GONE);
                    itemBinding.tvGet.setVisibility(View.VISIBLE);
                }
            }
        }
    }

}