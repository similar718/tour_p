package cn.xmzt.www.mine.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemFilterBinding;
import cn.xmzt.www.databinding.ItemFilterCategoryBinding;
import cn.xmzt.www.mine.bean.HorseMiMessageFilterBean;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;

/**
 * @describe 筛选Adapter
 */
public class MessageFilterAdapter extends BaseRecycleViewAdapter<HorseMiMessageFilterBean, MessageFilterAdapter.ItemHolder> {
    private final int TYPE_CATEGORY = 1;
    private final int TYPE_ITEM = 2;
    @Override
    public int getItemViewType(int position) {
        HorseMiMessageFilterBean filter=getItem(position);
        if(filter.getContentType()==HorseMiMessageFilterBean.TYPE_CATEGORY){
            return TYPE_CATEGORY;
        }else {
            return TYPE_ITEM;
        }
    }
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_CATEGORY){
            return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_filter_category, parent, false));
        }else {
            return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_filter, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.setViewDate(position);
    }

    public void setDatas(List datas) {
        super.setDatas(datas);
        notifyDataSetChanged();
    }
    public void addDatas(List datas) {
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.datas == null ? 0 : datas.size();
    }
    public boolean isSingleShow(int position){
        HorseMiMessageFilterBean filter=getItem(position);
        if(filter.getContentType()==HorseMiMessageFilterBean.TYPE_CATEGORY){
            return true;
        }else {
            return false;
        }
    }
    @Override
    public HorseMiMessageFilterBean getItem(int position) {
        if(position>=0&&position<datas.size()){
            return datas.get(position);
        }
        return null;
    }
    public int selectPositionDate=0;//选中日期位置
    public int selectPositionType=0;//选中出发地位置
    class ItemHolder<DB extends ViewDataBinding> extends RecyclerView.ViewHolder{
        private DB dbBinding;
        public ItemHolder(DB dbBinding) {
            super(dbBinding.getRoot());
            this.dbBinding = dbBinding;
            if(dbBinding instanceof ItemFilterBinding){
                ItemFilterBinding itemBinding= (ItemFilterBinding) dbBinding;
                itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position=getAdapterPosition();
                        HorseMiMessageFilterBean item = getItem(position);
                        if(item.getContentType()==HorseMiMessageFilterBean.TYPE_DATE){
                            //日期
                            if(position!=selectPositionDate){
                                int temp=selectPositionDate;
                                selectPositionDate=position;
                                notifyItemChanged(selectPositionDate);
                                notifyItemChanged(temp);
                            }
                        }else if(item.getContentType()==HorseMiMessageFilterBean.TYPE_MSG_TYPE){
                            //类型
                            if(position!=selectPositionType){
                                int temp=selectPositionType;
                                selectPositionType=position;
                                notifyItemChanged(selectPositionType);
                                notifyItemChanged(temp);
                            }
                        }
                    }
                });
            }

        }
        protected void setViewDate(int position) {
            HorseMiMessageFilterBean item = getItem(position);
            if(dbBinding instanceof ItemFilterCategoryBinding){
                ItemFilterCategoryBinding itemBinding= (ItemFilterCategoryBinding) dbBinding;
                itemBinding.tvName.setText(item.getContentValue());
            }else if(dbBinding instanceof ItemFilterBinding){
                ItemFilterBinding itemBinding= (ItemFilterBinding) dbBinding;
                if(item.getContentType()==HorseMiMessageFilterBean.TYPE_DATE){
                    //日期
                    itemBinding.tvName.setText(item.getContentValue());
                    if(selectPositionDate==0){
                        selectPositionDate=position;
                    }
                    if(position==selectPositionDate){
                        itemBinding.itemLayout.setBackgroundResource(R.drawable.icon_checked_filter);
//                        itemBinding.ivCheck.setImageResource(R.drawable.icon_checked_filter);
                        itemBinding.tvName.setTextColor(Color.parseColor("#24A4FF"));
                    }else {
                        itemBinding.itemLayout.setBackgroundResource(R.drawable.icon_checked_filter_no);
//                        itemBinding.ivCheck.setImageResource(0);
                        itemBinding.tvName.setTextColor(Color.parseColor("#333333"));
                    }
                }else if(item.getContentType()==HorseMiMessageFilterBean.TYPE_MSG_TYPE){
                    //类型
                    itemBinding.tvName.setText(item.getContentValue());
                    if(selectPositionType==0){
                        selectPositionType=position;
                    }
                    if(position==selectPositionType){
                        itemBinding.itemLayout.setBackgroundResource(R.drawable.icon_checked_filter);
//                        itemBinding.ivCheck.setImageResource(R.drawable.icon_checked_filter);
                        itemBinding.tvName.setTextColor(Color.parseColor("#24A4FF"));
                    }else {
                        itemBinding.itemLayout.setBackgroundResource(R.drawable.icon_checked_filter_no);
//                        itemBinding.ivCheck.setImageResource(0);
                        itemBinding.tvName.setTextColor(Color.parseColor("#333333"));
                    }
                }
            }
        }
    }
}