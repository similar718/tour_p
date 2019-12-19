package cn.xmzt.www.route.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemSortPopupBinding;

import java.util.List;


public class SortPopupAdapter extends BaseRecycleViewAdapter<String, SortPopupAdapter.ItemHolder>{

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSortPopupBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_sort_popup, parent, false);
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
        ItemSortPopupBinding itemBinding;
        public ItemHolder(ItemSortPopupBinding itemBinding) {
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
                itemBinding.tvSort.setTextColor(Color.parseColor("#24A4FF"));
            }else {
                itemBinding.ivSelect.setImageResource(0);
                itemBinding.tvSort.setTextColor(Color.parseColor("#333333"));
            }
        }
    }

}