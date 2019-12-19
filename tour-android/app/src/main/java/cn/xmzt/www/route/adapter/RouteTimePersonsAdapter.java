package cn.xmzt.www.route.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemRouteTimeCountBinding;
import cn.xmzt.www.databinding.ItemRouteTimePriceBinding;
import cn.xmzt.www.route.bean.PersonCountBean;
import cn.xmzt.www.route.bean.TimePriceDayBean;
import cn.xmzt.www.route.bean.TimePriceMonthBean;
import cn.xmzt.www.utils.TimeUtil;

import java.util.List;


public class RouteTimePersonsAdapter extends BaseRecycleViewAdapter<Object, RouteTimePersonsAdapter.ItemHolder>{
    private final int TYPE_ITEM_1 = 1;//时间价格
    private final int TYPE_ITEM_2 = 2;//其他
    private double floorPrice;//当月最低价
    public String currentTime="";
    public void setData(TimePriceMonthBean mTimePriceMonthBean,String currentTime) {
        this.datas.clear();
        if(!TextUtils.isEmpty(currentTime)){
            this.currentTime=currentTime;
        }
        floorPrice=mTimePriceMonthBean.getFloorPrice();
        List<TimePriceDayBean> list=mTimePriceMonthBean.getList();
        if(list!=null&&list.size()>0){
            TimePriceDayBean mTimePriceDayBean=list.get(0);
            int week = TimeUtil.getWeek(mTimePriceDayBean.getDate(),TimeUtil.FORMAT_A);
            for(int i=0;i<week;i++){
                this.datas.add(new TimePriceDayBean());
            }
            this.datas.addAll(list);
            this.datas.add(mPersonCountBean);
            String selectTime = mPersonCountBean.getTime();
            if(TextUtils.isEmpty(selectTime)){
                for(int j=0;j<list.size();j++) {
                    TimePriceDayBean timePriceDay = list.get(j);
                    TimePriceDayBean.Ext ext=timePriceDay.getExt();
                    if(ext!=null&&timePriceDay.getPrice()>0&&timePriceDay.isBuy()){
                        selectedPosition=week+j;
                        mPersonCountBean.setExt(ext);
                        mPersonCountBean.setTime(timePriceDay.getDate());
                        break;
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return this.datas == null ? 0: datas.size();
    }

    @Override
    public Object getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }
        return null;
    }
    public int selectedPosition=-1;
    private void setSelectedItem(int position){
        Object obj1=getItem(position);
        if(obj1 instanceof  TimePriceDayBean){
            TimePriceDayBean mTimePriceDayBean1= (TimePriceDayBean) obj1;
            TimePriceDayBean.Ext ext=mTimePriceDayBean1.getExt();
            if(ext!=null&&mTimePriceDayBean1.getPrice()>0&&mTimePriceDayBean1.isBuy()){
                mPersonCountBean.setExt(ext);
                mPersonCountBean.setTime(mTimePriceDayBean1.getDate());
                int temp=selectedPosition;
                selectedPosition=position;
                notifyItemChanged(selectedPosition);
                if(temp!=-1){
                    notifyItemChanged(temp);
                }
                notifyItemChanged(getItemCount()-1);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object obj=getItem(position);
       if(obj instanceof TimePriceDayBean){
            return TYPE_ITEM_1;
        }else if(obj instanceof PersonCountBean){
            return TYPE_ITEM_2;
        }
        return super.getItemViewType(position);
    }
    public boolean isSingleShow(int position){
        Object obj=getItem(position);
        if(obj instanceof PersonCountBean){
            return true;
        }else {
            return false;
        }
    }
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemHolder holder=null;
        if(viewType==TYPE_ITEM_1){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_time_price, parent, false));
        }else  if(viewType==TYPE_ITEM_2){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_time_count, parent, false));
        }
        return holder;
    }
    public PersonCountBean mPersonCountBean=new PersonCountBean();
    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.setViewDate(position);
    }
    class ItemHolder<DB extends ViewDataBinding> extends RecyclerView.ViewHolder implements View.OnClickListener {
        private DB dbBinding;
        public ItemHolder(DB dbBinding) {
            super(dbBinding.getRoot());
            this.dbBinding = dbBinding;
            if(dbBinding instanceof ItemRouteTimePriceBinding){
                ItemRouteTimePriceBinding itemBinding= (ItemRouteTimePriceBinding) this.dbBinding;
                itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=getAdapterPosition();
                        setSelectedItem(position);
                    }
                });
            }else if(dbBinding instanceof ItemRouteTimeCountBinding){
                ItemRouteTimeCountBinding itemBinding= (ItemRouteTimeCountBinding) this.dbBinding;
                itemBinding.btnCrAdd.setOnClickListener(this);
                itemBinding.btnCrMinus.setOnClickListener(this);
                itemBinding.btnXhAdd.setOnClickListener(this);
                itemBinding.btnXhMinus.setOnClickListener(this);
                itemBinding.btnFangAdd.setOnClickListener(this);
                itemBinding.btnFangMinus.setOnClickListener(this);
            }
        }
        private void setViewDate(int position){
            if(dbBinding instanceof ItemRouteTimePriceBinding){
                ItemRouteTimePriceBinding itemBinding= (ItemRouteTimePriceBinding) this.dbBinding;
                Object obj=getItem(position);
                if(obj instanceof TimePriceDayBean){
                    TimePriceDayBean mTimePriceDayBean=(TimePriceDayBean)obj;
                    itemBinding.setItem(mTimePriceDayBean);
                    if(floorPrice<=mTimePriceDayBean.getPrice()&&mTimePriceDayBean.getPrice()>0&&mTimePriceDayBean.isBuy()){
                        itemBinding.tvPrice.setTextColor(Color.parseColor("#FF9600"));
                        itemBinding.tvHoliday.setTextColor(Color.parseColor("#333333"));
                    }else {
                        itemBinding.tvPrice.setTextColor(Color.parseColor("#DADADA"));
                        itemBinding.tvHoliday.setTextColor(Color.parseColor("#DADADA"));
                    }
                    if(mTimePriceDayBean.getPrice()>0&&mTimePriceDayBean.isBuy()){
                        itemBinding.tvTime.setTextColor(Color.parseColor("#333333"));
                    }else {
                        itemBinding.tvTime.setTextColor(Color.parseColor("#DADADA"));
                    }
                    if(currentTime.equals(mTimePriceDayBean.getDate())){
                        itemBinding.tvTime.setText("今天");
                        mTimePriceDayBean.setToday(true);
                    }else {
                        mTimePriceDayBean.setToday(false);
                        itemBinding.tvTime.setText(mTimePriceDayBean.getDayStr());
                    }
                    String selectTime = mPersonCountBean.getTime();
                    if(!TextUtils.isEmpty(selectTime)&&selectTime.equals(mTimePriceDayBean.getDate())){
                        itemBinding.itemLayout.setSelected(true);
                        itemBinding.tvTime.setTextColor(Color.parseColor("#FFFFFF"));
                        itemBinding.tvPrice.setTextColor(Color.parseColor("#FFFFFF"));
                        itemBinding.tvHoliday.setTextColor(Color.parseColor("#FFFFFF"));
                        if(selectedPosition==-1){
                            selectedPosition=position;
                        }
                    }else {
                        itemBinding.itemLayout.setSelected(false);
                    }
                }
            }else if(dbBinding instanceof ItemRouteTimeCountBinding){
                ItemRouteTimeCountBinding itemBinding= (ItemRouteTimeCountBinding) this.dbBinding;
                itemBinding.setItemCount(mPersonCountBean);
            }
        }

        @Override
        public void onClick(View v) {
            if(itemListener!=null){
                int position=getAdapterPosition();
                itemListener.onItemClick(v,position);
            }
        }
    }
}