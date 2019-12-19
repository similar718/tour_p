package cn.xmzt.www.route.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemCarTypeBinding;

import java.util.Arrays;
import java.util.List;


public class SelectCarTypeAdapter extends BaseRecycleViewAdapter<String, SelectCarTypeAdapter.ItemHolder>{
    public SelectCarTypeAdapter(Context mContext) {
       datas= Arrays.asList(mContext.getResources().getStringArray(R.array.carTypeArray));
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCarTypeBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_car_type, parent, false);
        return new ItemHolder(itemBinding);
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

    @Override
    public int getItemCount() {
        return this.datas == null ? 0 : datas.size();
    }

    @Override
    public String getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }

        return null;
    }
    private int selectPosition=0;
    /**
     * 设置默认选中那个
     * @param position
     */
    public void setSelectPosition(int position) {
        if(position!=selectPosition){
            int temp=selectPosition;
            selectPosition=position;
            notifyItemChanged(selectPosition);
            notifyItemChanged(temp);
        }
    }
    class ItemHolder extends RecyclerView.ViewHolder {
        ItemCarTypeBinding itemBinding;
        public ItemHolder(ItemCarTypeBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemListener!=null){
                       int position = getAdapterPosition();
                        itemListener.onItemClick(view,position);
                    }
                }
            });
        }
        protected void setViewDate(int position) {
            String item = datas.get(position);
            itemBinding.setName(item);
            if(selectPosition==position){
                itemBinding.ivSelect.setImageResource(R.drawable.icon_hotel_sort_select);
                itemBinding.tvTypeName.setTextColor(Color.parseColor("#39B9FF"));
            }else {
                itemBinding.ivSelect.setImageResource(0);
                itemBinding.tvTypeName.setTextColor(Color.parseColor("#333333"));
            }
        }
    }

}