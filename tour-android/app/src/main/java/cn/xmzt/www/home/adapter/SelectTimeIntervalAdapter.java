package cn.xmzt.www.home.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemSelectDateTime1Binding;
import cn.xmzt.www.databinding.ItemSelectDateTime2Binding;
import cn.xmzt.www.home.bean.SelectDateTime;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import cn.xmzt.www.utils.TimeUtil;


public class SelectTimeIntervalAdapter extends BaseRecycleViewAdapter<SelectDateTime, SelectTimeIntervalAdapter.ItemHolder>{
    private final int TYPE_ITEM_1 = 1;//月份
    private final int TYPE_ITEM_2 = 2;//日期
    @Override
    public void setDatas(List<SelectDateTime> datas) {
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
        return this.datas == null ? 0: datas.size();
    }

    @Override
    public SelectDateTime getItem(int position) {
        if (position < 0){
            return null;
        }
        if(position < datas.size()){
            return datas.get(position);
        }
        return null;
    }
    public int selectedStartPosition=-1;
    public int selectedEndPosition=-1;
    public int todayPosition=-1;//今天的位置
    public boolean isSelected=false;//是否选好

    public void clearSelected(){
        for(SelectDateTime mSelectDateTime:datas){
            mSelectDateTime.setSelected(false);
            mSelectDateTime.setFirstSelected(false);
            mSelectDateTime.setLastSelected(false);
        }
    }
    public void setSelectedItem(int position){
        if(todayPosition>0&&position<todayPosition){
            return;
        }
        if(selectedStartPosition==-1||position<=selectedStartPosition||selectedEndPosition>0){
            clearSelected();
            selectedStartPosition=position;
            selectedEndPosition=-1;
            SelectDateTime mSelectDateTime=getItem(position);
            mSelectDateTime.setLabel("出发");
            mSelectDateTime.setLastSelected(true);
            mSelectDateTime.setFirstSelected(true);
            mSelectDateTime.setSelected(true);
            notifyDataSetChanged();
        }else {
            SelectDateTime startDateTime=getItem(selectedStartPosition);
            SelectDateTime endDateTime=getItem(position);
            int dateinterval=TimeUtil.getBetweenIntervalDays(startDateTime.getDate(),endDateTime.getDate(),"yyyy-MM-dd")+1;
            if(dateinterval<=30){
                selectedEndPosition=position;
                for(int i=0;i< datas.size();i++){
                    SelectDateTime mSelectDateTime=datas.get(i);
                    if(i==selectedStartPosition){
                        mSelectDateTime.setLabel("出发");
                        mSelectDateTime.setSelected(true);
                        mSelectDateTime.setFirstSelected(true);
                        mSelectDateTime.setLastSelected(false);
                    }else  if(i==selectedEndPosition){
                        mSelectDateTime.setSelected(true);
                        mSelectDateTime.setFirstSelected(false);
                        mSelectDateTime.setLastSelected(true);
                        mSelectDateTime.setLabel("返程");
                    }else  if(i>selectedStartPosition&&i<selectedEndPosition){
                        mSelectDateTime.setSelected(true);
                        mSelectDateTime.setFirstSelected(false);
                        mSelectDateTime.setLastSelected(false);
                    }else {
                        mSelectDateTime.setSelected(false);
                        mSelectDateTime.setFirstSelected(false);
                        mSelectDateTime.setLastSelected(false);
                    }
                }
                notifyDataSetChanged();
                isSelected=true;
            }else {
                ToastUtils.showShort("行程天数不能超过30天哦");
            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        SelectDateTime obj=getItem(position);
       if(obj.isMonth()){
            return TYPE_ITEM_1;
        }else {
            return TYPE_ITEM_2;
        }
    }
    public boolean isSingleShow(int position){
        SelectDateTime obj=getItem(position);
        if(obj.isMonth()){
            return true;
        }else {
            return false;
        }
    }
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemHolder holder=null;
        if(viewType==TYPE_ITEM_1){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_select_date_time_1, parent, false));
        }else  if(viewType==TYPE_ITEM_2){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_select_date_time_2, parent, false));
        }
        return holder;
    }
    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.setViewDate(position);
    }
    class ItemHolder<DB extends ViewDataBinding> extends RecyclerView.ViewHolder{
        private DB dbBinding;
        public ItemHolder(DB dbBinding) {
            super(dbBinding.getRoot());
            this.dbBinding = dbBinding;
            if(dbBinding instanceof ItemSelectDateTime2Binding){
                ItemSelectDateTime2Binding itemBinding= (ItemSelectDateTime2Binding) this.dbBinding;
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
        private void setViewDate(int position){
            if(dbBinding instanceof ItemSelectDateTime1Binding){
                ItemSelectDateTime1Binding itemBinding= (ItemSelectDateTime1Binding) this.dbBinding;
                SelectDateTime obj=getItem(position);
                itemBinding.setItem(obj);
            }else if(dbBinding instanceof ItemSelectDateTime2Binding){
                ItemSelectDateTime2Binding itemBinding= (ItemSelectDateTime2Binding) this.dbBinding;
                SelectDateTime obj=getItem(position);
                itemBinding.setItem(obj);
                itemBinding.itemLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                itemBinding.itemLayout1.setBackgroundResource(R.drawable.time_selector);
                itemBinding.itemLayout1.setSelected(false);
                if(obj.isToday()){
                    todayPosition=position;
                }
                itemBinding.tvDateDay.setTextColor(Color.parseColor("#999999"));
                if(position>=todayPosition){
                    itemBinding.tvDateDay.setTextColor(Color.parseColor("#333333"));
                }
                if(obj.isFirstSelected()){
                    itemBinding.itemLayout.setBackgroundResource(R.drawable.time_left_shape_translucent);
                    itemBinding.itemLayout1.setSelected(true);
                    itemBinding.tvDateDay.setTextColor(Color.parseColor("#FFFFFF"));
                }else if(obj.isLastSelected()){
                    itemBinding.itemLayout.setBackgroundResource(R.drawable.time_right_shape_translucent);
                    itemBinding.itemLayout1.setSelected(true);
                    itemBinding.tvDateDay.setTextColor(Color.parseColor("#FFFFFF"));
                }else if(obj.isSelected()){
                    itemBinding.itemLayout.setBackgroundResource(R.drawable.time_middle_shape_translucent);
                    itemBinding.itemLayout1.setBackgroundResource(R.drawable.time_middle_shape_translucent);
                }
            }
        }
    }
}