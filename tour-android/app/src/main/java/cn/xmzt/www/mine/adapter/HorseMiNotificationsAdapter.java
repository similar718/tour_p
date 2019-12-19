package cn.xmzt.www.mine.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemHorsesmallmi0Binding;
import cn.xmzt.www.databinding.ItemHorsesmallmi16Binding;
import cn.xmzt.www.databinding.ItemHorsesmallmi1Binding;
import cn.xmzt.www.databinding.ItemHorsesmallmi2Binding;
import cn.xmzt.www.databinding.ItemHorsesmallmi3Binding;
import cn.xmzt.www.databinding.ItemHorsesmallmi4Binding;
import cn.xmzt.www.databinding.ItemHorsesmallmi5Binding;
import cn.xmzt.www.databinding.ItemHorsesmallmi6Binding;
import cn.xmzt.www.databinding.ItemHorsesmallmi7Binding;
import cn.xmzt.www.databinding.ItemHorsesmallmi8Binding;
import cn.xmzt.www.databinding.ItemHorsesmallmiOrderBinding;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.mine.bean.HorseMiMessageBean;
import cn.xmzt.www.mine.bean.WeatherInfoBean;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import cn.xmzt.www.utils.DpUtil;


public class HorseMiNotificationsAdapter extends BaseRecycleViewAdapter<HorseMiMessageBean, HorseMiNotificationsAdapter.ItemHolder>{
    private final int TYPE_ITEM_0 = 0;//
    private final int TYPE_ITEM_1 = 1;//
    private final int TYPE_ITEM_2 = 2;//
    private final int TYPE_ITEM_3 = 3;//
    private final int TYPE_ITEM_4 = 4;//
    private final int TYPE_ITEM_5 = 5;//
    private final int TYPE_ITEM_6 = 6;//
    private final int TYPE_ITEM_7 = 7;//
    private final int TYPE_ITEM_8 = 8;//
    private final int TYPE_ITEM_9 = 9;//
    private final int TYPE_ITEM_16 = 16;//
    private final int TYPE_ITEM_NULL = 20;//
    public WeatherInfoBean weatherInfoBean=null;

    @Override
    public void setDatas(List<HorseMiMessageBean> datas) {
        super.setDatas(datas);
        notifyDataSetChanged();
    }

    public void addDatas(List<HorseMiMessageBean> datas) {
        if (this.datas == null) {
            setDatas(datas);
        } else {
            this.datas.addAll(datas);
            notifyDataSetChanged();
        }
    }
    /**
     * 消息类型
     * 1 行程创建成功
     * 2 明日出行提醒
     * 3 早睡提醒
     * 4 集结地更新提醒
     * 5 车辆检查提醒
     * 6 出行清单提醒
     * 7 xx市美食指南
     * 8 xxx酒店介绍提醒
     * 9 订单提醒
     * 10 群即将解散提醒
     */
    @Override
    public int getItemViewType(int position) {
        HorseMiMessageBean horseMiMessage = getItem(position);
        if(horseMiMessage==null){
            if (position == (getItemCount()-1) && datas.size() <= 2){
                return TYPE_ITEM_NULL;
            } else {
                return TYPE_ITEM_0;
            }
        }else if(horseMiMessage.getTypes()==1){
            return TYPE_ITEM_1;
        }else if(horseMiMessage.getTypes()==2){
            return TYPE_ITEM_2;
        }else if(horseMiMessage.getTypes()==5){
            return TYPE_ITEM_3;
        }else if(horseMiMessage.getTypes()==6){
            return TYPE_ITEM_4;
        }else if(horseMiMessage.getTypes()==3){
            return TYPE_ITEM_5;
        }else if(horseMiMessage.getTypes()==4){
            return TYPE_ITEM_6;
        }else if(horseMiMessage.getTypes()==8){
            return TYPE_ITEM_7;
        }else if(horseMiMessage.getTypes()==7){
            return TYPE_ITEM_8;
        }else if(horseMiMessage.getTypes()==9){
            return TYPE_ITEM_9;
        }else if(horseMiMessage.getTypes()==10){
            return TYPE_ITEM_1;
        }else {
            return TYPE_ITEM_16;
        }
    }
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemHolder holder=null;
        if(viewType==TYPE_ITEM_0){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_horsesmallmi_0, parent, false));
        }else if(viewType==TYPE_ITEM_1){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_horsesmallmi_1, parent, false));
        }else if(viewType==TYPE_ITEM_2){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_horsesmallmi_2, parent, false));
        }else if(viewType==TYPE_ITEM_3){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_horsesmallmi_3, parent, false));
        }else if(viewType==TYPE_ITEM_4){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_horsesmallmi_4, parent, false));
        }else if(viewType==TYPE_ITEM_5){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_horsesmallmi_5, parent, false));
        }else if(viewType==TYPE_ITEM_6){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_horsesmallmi_6, parent, false));
        }else if(viewType==TYPE_ITEM_7){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_horsesmallmi_7, parent, false));
        }else if(viewType==TYPE_ITEM_8){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_horsesmallmi_8, parent, false));
        }else if(viewType==TYPE_ITEM_9){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_horsesmallmi_order, parent, false));
        }else if(viewType==TYPE_ITEM_16){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_horsesmallmi_16, parent, false));
        }else if(viewType==TYPE_ITEM_NULL){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_horsesmallmi_null, parent, false));
        }
        return holder;
    }
    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.setViewDate(position);
    }
    @Override
    public int getItemCount() {
        if(datas!=null&&datas.size()>0){
            if(weatherInfoBean!=null){
                if (datas.size() > 2){
                    return datas.size() + 1;
                } else {
                    return datas.size()+2;
                }
            }else {
                return datas.size();
            }
        }
        return 0;
    }

    @Override
    public HorseMiMessageBean getItem(int position) {
        if(weatherInfoBean!=null){
            int index=position-1;
            if(index >= 0 && index < datas.size()){
                return datas.get(index);
            } else {
                return null;
            }
        }else {
            if(position >= 0&&position<datas.size()){
                return datas.get(position);
            }
        }
        return null;
    }
    class ItemHolder<DB extends ViewDataBinding> extends RecyclerView.ViewHolder{
        private DB dbBinding;
        public ItemHolder(DB dbBinding) {
            super(dbBinding.getRoot());
            this.dbBinding = dbBinding;
            if(dbBinding instanceof ItemHorsesmallmi1Binding){
                ItemHorsesmallmi1Binding itemBinding= (ItemHorsesmallmi1Binding) this.dbBinding;
                itemBinding.itemIntoDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
                itemBinding.ivFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
                itemBinding.tvAllRead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
            }else if(dbBinding instanceof ItemHorsesmallmi2Binding){
                ItemHorsesmallmi2Binding itemBinding= (ItemHorsesmallmi2Binding) this.dbBinding;
                itemBinding.ivFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
                itemBinding.tvAllRead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
            }else if(dbBinding instanceof ItemHorsesmallmi3Binding){
                ItemHorsesmallmi3Binding itemBinding= (ItemHorsesmallmi3Binding) this.dbBinding;
                itemBinding.itemIntoDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
                itemBinding.ivFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
                itemBinding.tvAllRead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
            } else if(dbBinding instanceof ItemHorsesmallmi4Binding){
                ItemHorsesmallmi4Binding itemBinding= (ItemHorsesmallmi4Binding) this.dbBinding;
                itemBinding.itemIntoDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
                itemBinding.ivFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
                itemBinding.tvAllRead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
            }else if(dbBinding instanceof ItemHorsesmallmi5Binding){
                ItemHorsesmallmi5Binding itemBinding= (ItemHorsesmallmi5Binding) this.dbBinding;
                itemBinding.itemIntoDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
                itemBinding.ivFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
                itemBinding.tvAllRead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
            }else if(dbBinding instanceof ItemHorsesmallmi6Binding){
                ItemHorsesmallmi6Binding itemBinding= (ItemHorsesmallmi6Binding) this.dbBinding;
                itemBinding.itemIntoDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
                itemBinding.ivFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
                itemBinding.tvAllRead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
            }else if(dbBinding instanceof ItemHorsesmallmi7Binding){
                ItemHorsesmallmi7Binding itemBinding= (ItemHorsesmallmi7Binding) this.dbBinding;
                itemBinding.itemIntoDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
                itemBinding.ivFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
                itemBinding.tvAllRead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
            }else if(dbBinding instanceof ItemHorsesmallmi8Binding){
                ItemHorsesmallmi8Binding itemBinding= (ItemHorsesmallmi8Binding) this.dbBinding;
                itemBinding.itemIntoDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
                itemBinding.ivFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
                itemBinding.tvAllRead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
            }else if(dbBinding instanceof ItemHorsesmallmiOrderBinding){
                ItemHorsesmallmiOrderBinding itemBinding= (ItemHorsesmallmiOrderBinding) this.dbBinding;
                itemBinding.itemIntoDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
                itemBinding.ivFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
                itemBinding.tvAllRead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
            }else if(dbBinding instanceof ItemHorsesmallmi16Binding){
                ItemHorsesmallmi16Binding itemBinding= (ItemHorsesmallmi16Binding) this.dbBinding;
                itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
                itemBinding.ivFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
                itemBinding.tvAllRead.setOnClickListener(new View.OnClickListener() {
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

            if(dbBinding instanceof ItemHorsesmallmi0Binding){
                ItemHorsesmallmi0Binding itemBinding= (ItemHorsesmallmi0Binding) this.dbBinding;
                if(weatherInfoBean!=null){
                    itemBinding.tvTq.setText(weatherInfoBean.getTemperatureRange());
                    itemBinding.tvTqDes.setText(weatherInfoBean.getSituation());
                    GlideUtil.loadImage(itemBinding.ivTqImg,weatherInfoBean.getIcon());
                    itemBinding.tvTqCity.setText(weatherInfoBean.getAreaName());
                    itemBinding.tvTqCityType.setText(weatherInfoBean.getAreaTypeName());
                    itemBinding.tvTqHint.setText(weatherInfoBean.getRemark());
                }
            }else if(dbBinding instanceof ItemHorsesmallmi1Binding){
                ItemHorsesmallmi1Binding itemBinding= (ItemHorsesmallmi1Binding) this.dbBinding;
                HorseMiMessageBean item=getItem(position);
                itemBinding.itemTime.setText(item.getTimeFormat());
                itemBinding.ivFilter.setVisibility(View.GONE);
                itemBinding.tvAllRead.setVisibility(View.GONE);
                if(position==1&&weatherInfoBean!=null){
                    itemBinding.ivFilter.setVisibility(View.VISIBLE);
                    itemBinding.tvAllRead.setVisibility(View.VISIBLE);
                }
                itemBinding.itemTitle.setText(item.getTitle());
                itemBinding.itemContent.setText(item.getContent());
                if(item.getIfRead()==1){//未读
                    itemBinding.ivNotRead.setVisibility(View.VISIBLE);
                }else {
                    itemBinding.ivNotRead.setVisibility(View.GONE);
                }
                List<HorseMiMessageBean.MsgDetail> tourMsgDetails =item.getTourMsgDetails();
                itemBinding.tripLayout.setVisibility(View.GONE);
                itemBinding.tripGoDateLayout.setVisibility(View.GONE);
                for(int i=0;i<tourMsgDetails.size();i++){
                    HorseMiMessageBean.MsgDetail msgDetail=tourMsgDetails.get(i);
                    if(i==0){
                        itemBinding.tripLayout.setVisibility(View.VISIBLE);
                        itemBinding.tvTrip.setText(msgDetail.getMsgKey());
                        itemBinding.tvTripName.setText(msgDetail.getMsgValue());
                    }else if(i==1){
                        itemBinding.tripGoDateLayout.setVisibility(View.VISIBLE);
                        itemBinding.tvTripGo.setText(msgDetail.getMsgKey());
                        itemBinding.tvTripGoDate.setText(msgDetail.getMsgValue());
                    }
                }
            }else if(dbBinding instanceof ItemHorsesmallmi2Binding){
                ItemHorsesmallmi2Binding itemBinding= (ItemHorsesmallmi2Binding) this.dbBinding;
                HorseMiMessageBean item=getItem(position);
                itemBinding.itemTime.setText(item.getTimeFormat());
                itemBinding.ivFilter.setVisibility(View.GONE);
                itemBinding.tvAllRead.setVisibility(View.GONE);
                if(position==1&&weatherInfoBean!=null){
                    itemBinding.ivFilter.setVisibility(View.VISIBLE);
                    itemBinding.tvAllRead.setVisibility(View.VISIBLE);
                }
                itemBinding.itemTitle.setText(item.getTitle());
                List<HorseMiMessageBean.MsgDetail> tourMsgDetails =item.getTourMsgDetails();
                itemBinding.tripLayout.setVisibility(View.GONE);
                itemBinding.mddLayout.setVisibility(View.GONE);
                itemBinding.xcqdLayout.setVisibility(View.GONE);
                for(int i=0;i<tourMsgDetails.size();i++){
                    HorseMiMessageBean.MsgDetail msgDetail=tourMsgDetails.get(i);
                    if(i==0){
                        itemBinding.tripLayout.setVisibility(View.VISIBLE);
                        itemBinding.tvTrip.setText(msgDetail.getMsgKey());
                        itemBinding.tvTripName.setText(msgDetail.getMsgValue());
                    }else if(i==1){
                        itemBinding.mddLayout.setVisibility(View.VISIBLE);
                        itemBinding.tvMdd.setText(msgDetail.getMsgKey());
                        itemBinding.tvMddName.setText(msgDetail.getMsgValue());
                    }else if(i==2){
                        itemBinding.xcqdLayout.setVisibility(View.VISIBLE);
                        itemBinding.tvXcqd.setText(msgDetail.getMsgKey());
                        itemBinding.tvXcqdName.setText(msgDetail.getMsgValue());
                    }
                }
            }else if(dbBinding instanceof ItemHorsesmallmi3Binding){
                ItemHorsesmallmi3Binding itemBinding= (ItemHorsesmallmi3Binding) this.dbBinding;
                HorseMiMessageBean item=getItem(position);
                itemBinding.itemTime.setText(item.getTimeFormat());
                itemBinding.ivFilter.setVisibility(View.GONE);
                itemBinding.tvAllRead.setVisibility(View.GONE);
                if(position==1&&weatherInfoBean!=null){
                    itemBinding.ivFilter.setVisibility(View.VISIBLE);
                    itemBinding.tvAllRead.setVisibility(View.VISIBLE);
                }
                itemBinding.itemTitle.setText(item.getTitle());
                itemBinding.itemContent.setText(item.getContent());
                if(item.getIfRead()==1){//未读
                    itemBinding.ivNotRead.setVisibility(View.VISIBLE);
                }else {
                    itemBinding.ivNotRead.setVisibility(View.GONE);
                }
                List<HorseMiMessageBean.MsgDetail> tourMsgDetails =item.getTourMsgDetails();
                itemBinding.tripLayout.setVisibility(View.GONE);
                itemBinding.tripGoDateLayout.setVisibility(View.GONE);
                itemBinding.allLengthLayout.setVisibility(View.GONE);
                for(int i=0;i<tourMsgDetails.size();i++){
                    HorseMiMessageBean.MsgDetail msgDetail=tourMsgDetails.get(i);
                    if(i==0){
                        itemBinding.tripLayout.setVisibility(View.VISIBLE);
                        itemBinding.tvTrip.setText(msgDetail.getMsgKey());
                        itemBinding.tvTripName.setText(msgDetail.getMsgValue());
                    }else if(i==1){
                        itemBinding.tripGoDateLayout.setVisibility(View.VISIBLE);
                        itemBinding.tvTripGo.setText(msgDetail.getMsgKey());
                        itemBinding.tvTripGoDate.setText(msgDetail.getMsgValue());
                    }else if(i==2){
                        itemBinding.allLengthLayout.setVisibility(View.VISIBLE);
                        itemBinding.tvAllLength.setText(msgDetail.getMsgKey());
                        itemBinding.tvAllLengthV.setText(msgDetail.getMsgValue());
                    }
                }
            }else if(dbBinding instanceof ItemHorsesmallmi4Binding){
                ItemHorsesmallmi4Binding itemBinding= (ItemHorsesmallmi4Binding) this.dbBinding;
                HorseMiMessageBean item=getItem(position);
                itemBinding.itemTime.setText(item.getTimeFormat());
                itemBinding.ivFilter.setVisibility(View.GONE);
                itemBinding.tvAllRead.setVisibility(View.GONE);
                if(position==1&&weatherInfoBean!=null){
                    itemBinding.ivFilter.setVisibility(View.VISIBLE);
                    itemBinding.tvAllRead.setVisibility(View.VISIBLE);
                }
                itemBinding.itemTitle.setText(item.getTitle());
                itemBinding.itemContent.setText(item.getContent());
                if(item.getIfRead()==1){//未读
                    itemBinding.ivNotRead.setVisibility(View.VISIBLE);
                }else {
                    itemBinding.ivNotRead.setVisibility(View.GONE);
                }
            }else if(dbBinding instanceof ItemHorsesmallmi5Binding){
                ItemHorsesmallmi5Binding itemBinding= (ItemHorsesmallmi5Binding) this.dbBinding;
                HorseMiMessageBean item=getItem(position);
                itemBinding.itemTime.setText(item.getTimeFormat());
                itemBinding.ivFilter.setVisibility(View.GONE);
                itemBinding.tvAllRead.setVisibility(View.GONE);
                if(position==1&&weatherInfoBean!=null){
                    itemBinding.ivFilter.setVisibility(View.VISIBLE);
                    itemBinding.tvAllRead.setVisibility(View.VISIBLE);
                }
                itemBinding.itemTitle.setText(item.getTitle());
                itemBinding.itemContent.setText(item.getContent());
                List<HorseMiMessageBean.MsgDetail> tourMsgDetails =item.getTourMsgDetails();
                itemBinding.itemLanguageContent.setVisibility(View.GONE);
                itemBinding.itemLanguageContentAuthor.setVisibility(View.GONE);
                if(tourMsgDetails.size()==1){
                    itemBinding.itemLanguageContent.setVisibility(View.VISIBLE);
                    itemBinding.itemLanguageContentAuthor.setVisibility(View.VISIBLE);
                    HorseMiMessageBean.MsgDetail msgDetail1=tourMsgDetails.get(0);
                    itemBinding.itemLanguageContent.setText(msgDetail1.getMsgKey());
                    itemBinding.itemLanguageContentAuthor.setText(msgDetail1.getMsgValue());
                }
            }else if(dbBinding instanceof ItemHorsesmallmi6Binding){
                ItemHorsesmallmi6Binding itemBinding= (ItemHorsesmallmi6Binding) this.dbBinding;
                HorseMiMessageBean item=getItem(position);
                itemBinding.itemTime.setText(item.getTimeFormat());
                itemBinding.ivFilter.setVisibility(View.GONE);
                itemBinding.tvAllRead.setVisibility(View.GONE);
                if(position==1&&weatherInfoBean!=null){
                    itemBinding.ivFilter.setVisibility(View.VISIBLE);
                    itemBinding.tvAllRead.setVisibility(View.VISIBLE);
                }
                itemBinding.itemTitle.setText(item.getTitle());
                itemBinding.itemContent.setText(item.getContent());
                if(item.getIfRead()==1){//未读
                    itemBinding.ivNotRead.setVisibility(View.VISIBLE);
                }else {
                    itemBinding.ivNotRead.setVisibility(View.GONE);
                }
                List<HorseMiMessageBean.MsgDetail> tourMsgDetails =item.getTourMsgDetails();
                itemBinding.rlTripStageAddress.setVisibility(View.GONE);
                itemBinding.rlTripStageTime.setVisibility(View.GONE);
                for(int i=0;i<tourMsgDetails.size();i++){
                    HorseMiMessageBean.MsgDetail msgDetail=tourMsgDetails.get(i);
                    if(i==0){
                        itemBinding.rlTripStageAddress.setVisibility(View.VISIBLE);
                        itemBinding.tvTripStageAddressKey.setText(msgDetail.getMsgKey());
                        itemBinding.tvTripStageAddress.setText(msgDetail.getMsgValue());
                    }else if(i==1){
                        itemBinding.rlTripStageTime.setVisibility(View.VISIBLE);
                        itemBinding.tvTripStageTimeKey.setText(msgDetail.getMsgKey());
                        itemBinding.tvTripStageTime.setText(msgDetail.getMsgValue());
                    }
                }
            }else if(dbBinding instanceof ItemHorsesmallmi7Binding){
                ItemHorsesmallmi7Binding itemBinding= (ItemHorsesmallmi7Binding) this.dbBinding;
                HorseMiMessageBean item=getItem(position);
                itemBinding.itemTime.setText(item.getTimeFormat());
                itemBinding.ivFilter.setVisibility(View.GONE);
                itemBinding.tvAllRead.setVisibility(View.GONE);
                if(position==1&&weatherInfoBean!=null){
                    itemBinding.ivFilter.setVisibility(View.VISIBLE);
                    itemBinding.tvAllRead.setVisibility(View.VISIBLE);
                }
                itemBinding.itemTitle.setText(item.getTitle());
                itemBinding.itemContent.setText(item.getContent());
                if(item.getIfRead()==1){//未读
                    itemBinding.ivNotRead.setVisibility(View.VISIBLE);
                }else {
                    itemBinding.ivNotRead.setVisibility(View.GONE);
                }
                List<HorseMiMessageBean.MsgDetail> tourMsgDetails =item.getTourMsgDetails();
                itemBinding.rlTripLive.setVisibility(View.GONE);
                for(int i=0;i<tourMsgDetails.size();i++){
                    HorseMiMessageBean.MsgDetail msgDetail=tourMsgDetails.get(i);
                    if(i==0){
                        itemBinding.rlTripLive.setVisibility(View.VISIBLE);
                        itemBinding.tvTripLiveKey.setText(msgDetail.getMsgKey());
                        itemBinding.tvTripLive.setText(msgDetail.getMsgValue());
                    }
                }
            }else if(dbBinding instanceof ItemHorsesmallmi8Binding){
                ItemHorsesmallmi8Binding itemBinding= (ItemHorsesmallmi8Binding) this.dbBinding;
                HorseMiMessageBean item=getItem(position);
                itemBinding.itemTime.setText(item.getTimeFormat());
                itemBinding.ivFilter.setVisibility(View.GONE);
                itemBinding.tvAllRead.setVisibility(View.GONE);
                if(position==1&&weatherInfoBean!=null){
                    itemBinding.ivFilter.setVisibility(View.VISIBLE);
                    itemBinding.tvAllRead.setVisibility(View.VISIBLE);
                }
                itemBinding.itemTitle.setText(item.getTitle());
                itemBinding.itemContent.setText(item.getContent());
                if(item.getIfRead()==1){//未读
                    itemBinding.ivNotRead.setVisibility(View.VISIBLE);
                }else {
                    itemBinding.ivNotRead.setVisibility(View.GONE);
                }

            }else if(dbBinding instanceof ItemHorsesmallmiOrderBinding){
                ItemHorsesmallmiOrderBinding itemBinding= (ItemHorsesmallmiOrderBinding) this.dbBinding;
                HorseMiMessageBean item=getItem(position);
                itemBinding.itemTime.setText(item.getTimeFormat());
                itemBinding.ivFilter.setVisibility(View.GONE);
                itemBinding.tvAllRead.setVisibility(View.GONE);
                if(position==1&&weatherInfoBean!=null){
                    itemBinding.ivFilter.setVisibility(View.VISIBLE);
                    itemBinding.tvAllRead.setVisibility(View.VISIBLE);
                }
                itemBinding.itemTitle.setText(item.getTitle());
                itemBinding.itemContent.setText(item.getContent());
                if(item.getIfRead()==1){//未读
                    itemBinding.ivNotRead.setVisibility(View.VISIBLE);
                }else {
                    itemBinding.ivNotRead.setVisibility(View.GONE);
                }
                List<HorseMiMessageBean.MsgDetail> tourMsgDetails =item.getTourMsgDetails();
                itemBinding.order1Layout.setVisibility(View.GONE);
                itemBinding.order2Layout.setVisibility(View.GONE);
                itemBinding.order3Layout.setVisibility(View.GONE);
                itemBinding.order4Layout.setVisibility(View.GONE);
                for(int i=0;i<tourMsgDetails.size();i++){
                    HorseMiMessageBean.MsgDetail msgDetail=tourMsgDetails.get(i);
                    if(i==0){
                        itemBinding.order1Layout.setVisibility(View.VISIBLE);
                        itemBinding.tvOrder1Key.setText(msgDetail.getMsgKey());
                        itemBinding.tvOrder1.setText(msgDetail.getMsgValue());
                    }else if(i==1){
                        itemBinding.order2Layout.setVisibility(View.VISIBLE);
                        itemBinding.tvOrder2Key.setText(msgDetail.getMsgKey());
                        itemBinding.tvOrder2.setText(msgDetail.getMsgValue());
                    }else if(i==2){
                        itemBinding.order3Layout.setVisibility(View.VISIBLE);
                        itemBinding.tvOrder3Key.setText(msgDetail.getMsgKey());
                        itemBinding.tvOrder3.setText(msgDetail.getMsgValue());
                    }else if(i==3){
                        itemBinding.order4Layout.setVisibility(View.VISIBLE);
                        itemBinding.tvOrder4Key.setText(msgDetail.getMsgKey());
                        itemBinding.tvOrder4.setText(msgDetail.getMsgValue());
                    }

                }
            }else if(dbBinding instanceof ItemHorsesmallmi16Binding){
                ItemHorsesmallmi16Binding itemBinding= (ItemHorsesmallmi16Binding) this.dbBinding;
                HorseMiMessageBean item = getItem(position);
                itemBinding.itemTime.setText(item.getTimeFormat());
                itemBinding.ivFilter.setVisibility(View.GONE);
                itemBinding.tvAllRead.setVisibility(View.GONE);
                if(position==1&&weatherInfoBean!=null){
                    itemBinding.ivFilter.setVisibility(View.VISIBLE);
                    itemBinding.tvAllRead.setVisibility(View.VISIBLE);
                }
                itemBinding.itemMessageActivityTitle.setText(item.getTitle());
                itemBinding.itemMessageActivityContent.setText(item.getContent());
                if(item.getIfRead()==1){//未读
                    itemBinding.ivNotRead.setVisibility(View.VISIBLE);
                }else {
                    itemBinding.ivNotRead.setVisibility(View.GONE);
                }
            }
        }
    }
}