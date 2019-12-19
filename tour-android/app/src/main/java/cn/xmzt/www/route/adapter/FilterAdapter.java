package cn.xmzt.www.route.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemFilterBinding;
import cn.xmzt.www.databinding.ItemFilterCategoryBinding;
import cn.xmzt.www.route.bean.FilterBean;
import cn.xmzt.www.route.bean.FilterThemeBean;
import cn.xmzt.www.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @describe 筛选Adapter
 */
public class FilterAdapter extends BaseRecycleViewAdapter<FilterBean,FilterAdapter.ItemHolder>{
    private final int TYPE_CATEGORY = 1;
    private final int TYPE_ITEM = 2;
    @Override
    public int getItemViewType(int position) {
        FilterBean filter=getItem(position);
        if(filter.getType()==FilterBean.TYPE_CATEGORY){
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
        FilterBean filter=getItem(position);
        if(filter.getType()==FilterBean.TYPE_CATEGORY){
            return true;
        }else {
            return false;
        }
    }
    @Override
    public FilterBean getItem(int position) {
        if(position>=0&&position<datas.size()){
            return datas.get(position);
        }
        return null;
    }
    public int selectPositionDate=0;//选中日期位置
    public int selectPositionDays=0;//选中天数位置
    public int selectPositionDepart=0;//选中出发地位置
    public int selectPositionArrival=0;//选中目的地位置
    public List<FilterThemeBean> selectThemeList=new ArrayList<>();//选中主题
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
                        FilterBean item = getItem(position);
                        if(item.getType()==FilterBean.TYPE_DATE){
                            //日期
                            if(position!=selectPositionDate){
                                int temp=selectPositionDate;
                                selectPositionDate=position;
                                notifyItemChanged(selectPositionDate);
                                notifyItemChanged(temp);
                            }
                        }else if(item.getType()==FilterBean.TYPE_DAYS){
                            //天数
                            if(position!=selectPositionDays){
                                int temp=selectPositionDays;
                                selectPositionDays=position;
                                notifyItemChanged(selectPositionDays);
                                notifyItemChanged(temp);
                            }
                        }else if(item.getType()==FilterBean.TYPE_DEPART){
                            //出发地
                            if(position!=selectPositionDepart){
                                int temp=selectPositionDepart;
                                selectPositionDepart=position;
                                notifyItemChanged(selectPositionDepart);
                                notifyItemChanged(temp);
                            }
                        }else if(item.getType()==FilterBean.TYPE_ARRIVAL){
                            //目的地
                            if(position!=selectPositionArrival){
                                int temp=selectPositionArrival;
                                selectPositionArrival=position;
                                notifyItemChanged(selectPositionArrival);
                                notifyItemChanged(temp);
                            }
                        }else if(item.getType()==FilterBean.TYPE_THEME){
                            //主题
                            Object obj=item.getData();
                            if (obj instanceof FilterThemeBean){
                                FilterThemeBean filterTheme= (FilterThemeBean) obj;
                                if(filterTheme.isSelected()){
                                    selectThemeList.remove(filterTheme);
                                    filterTheme.setSelected(false);
                                }else {
                                    filterTheme.setSelected(true);
                                    selectThemeList.add(filterTheme);
                                }
                            }
                            notifyItemChanged(position);
                        }
                    }
                });
            }

        }
        protected void setViewDate(int position) {
            FilterBean item = getItem(position);
            if(dbBinding instanceof ItemFilterCategoryBinding){
                ItemFilterCategoryBinding itemBinding= (ItemFilterCategoryBinding) dbBinding;
                Object obj=item.getData();
                if(obj instanceof String){
                    itemBinding.tvName.setText((String) obj);
                }else if (obj instanceof FilterThemeBean){
                    FilterThemeBean filterTheme= (FilterThemeBean) obj;
                    itemBinding.tvName.setText(filterTheme.getLabel());
                }
            }else if(dbBinding instanceof ItemFilterBinding){
                ItemFilterBinding itemBinding= (ItemFilterBinding) dbBinding;
                Object obj=item.getData();
                if(item.getType()==FilterBean.TYPE_DATE){
                    //日期
                    if(obj instanceof String){
                        itemBinding.tvName.setText(TimeUtil.stringDateToString((String) obj,"yyyy-MM","yyyy.MM"));
                    }
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
                }else if(item.getType()==FilterBean.TYPE_DAYS){
                    //天数
                    if(obj instanceof String){
                        itemBinding.tvName.setText((String) obj+"天");
                    }
                    if(selectPositionDays==0){
                        selectPositionDays=position;
                    }
                    if(position==selectPositionDays){
                        itemBinding.itemLayout.setBackgroundResource(R.drawable.icon_checked_filter);
//                        itemBinding.ivCheck.setImageResource(R.drawable.icon_checked_filter);
                        itemBinding.tvName.setTextColor(Color.parseColor("#24A4FF"));
                    }else {
                        itemBinding.itemLayout.setBackgroundResource(R.drawable.icon_checked_filter_no);
//                        itemBinding.ivCheck.setImageResource(0);
                        itemBinding.tvName.setTextColor(Color.parseColor("#333333"));
                    }
                }else if(item.getType()==FilterBean.TYPE_DEPART){
                    //出发地
                    if(obj instanceof String){
                        itemBinding.tvName.setText((String) obj);
                    }
                    if(selectPositionDepart==0){
                        selectPositionDepart=position;
                    }
                    if(position==selectPositionDepart){
                        itemBinding.itemLayout.setBackgroundResource(R.drawable.icon_checked_filter);
//                        itemBinding.ivCheck.setImageResource(R.drawable.icon_checked_filter);
                        itemBinding.tvName.setTextColor(Color.parseColor("#24A4FF"));
                    }else {
                        itemBinding.itemLayout.setBackgroundResource(R.drawable.icon_checked_filter_no);
//                        itemBinding.ivCheck.setImageResource(0);
                        itemBinding.tvName.setTextColor(Color.parseColor("#333333"));
                    }
                }else if(item.getType()==FilterBean.TYPE_ARRIVAL){
                    //目的地
                    if(obj instanceof String){
                        itemBinding.tvName.setText((String) obj);
                    }
                    if(selectPositionArrival==0){
                        selectPositionArrival=position;
                    }
                    if(position==selectPositionArrival){
                        itemBinding.itemLayout.setBackgroundResource(R.drawable.icon_checked_filter);
//                        itemBinding.ivCheck.setImageResource(R.drawable.icon_checked_filter);
                        itemBinding.tvName.setTextColor(Color.parseColor("#24A4FF"));
                    }else {
                        itemBinding.itemLayout.setBackgroundResource(R.drawable.icon_checked_filter_no);
//                        itemBinding.ivCheck.setImageResource(0);
                        itemBinding.tvName.setTextColor(Color.parseColor("#333333"));
                    }
                }else if(item.getType()==FilterBean.TYPE_THEME){
                    if (obj instanceof FilterThemeBean){
                        FilterThemeBean filterTheme= (FilterThemeBean) obj;
                        itemBinding.tvName.setText(filterTheme.getLabel());
                        if(selectThemeList.size()==0){
                            filterTheme.setSelected(false);
                        }
                        if(filterTheme.isSelected()){
                            itemBinding.itemLayout.setBackgroundResource(R.drawable.icon_checked_filter);
//                            itemBinding.ivCheck.setImageResource(R.drawable.icon_checked_filter);
                            itemBinding.tvName.setTextColor(Color.parseColor("#24A4FF"));
                        }else {
                            itemBinding.itemLayout.setBackgroundResource(R.drawable.icon_checked_filter_no);
//                            itemBinding.ivCheck.setImageResource(0);
                            itemBinding.tvName.setTextColor(Color.parseColor("#333333"));
                        }
                    }
                }
            }
        }
    }
}