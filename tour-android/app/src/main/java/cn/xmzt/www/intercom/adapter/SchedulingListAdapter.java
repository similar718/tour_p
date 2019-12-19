package cn.xmzt.www.intercom.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemSchedulingGroupBinding;
import cn.xmzt.www.databinding.ItemSchedulingHotRecommendBinding;
import cn.xmzt.www.databinding.ItemSchedulingRecommendRouteBinding;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.home.bean.RecommendLineBean;
import cn.xmzt.www.intercom.bean.TourTripBean;
import cn.xmzt.www.intercom.bean.TourTripListBean;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.List;


public class SchedulingListAdapter extends BaseRecycleViewAdapter<Object, SchedulingListAdapter.ItemHolder> {
    private final int TYPE_ITEM_1 = 1;//行程item
    private final int TYPE_ITEM_2 = 2;//推荐
    private final int TYPE_ITEM_3 = 3;//热门线路


    public int tourTripSize=0;
    public int recommendSize=0;
    public void setData(TourTripListBean tripListBean,boolean isRefresh) {
        if(isRefresh){
            this.datas.clear();
        }
        List<TourTripBean> tourTripList=tripListBean.getTourTripList();//行程列表
        if(tourTripList!=null){
            int size=tourTripList.size();
            if(isRefresh){
                this.datas.addAll(tourTripList);
                tourTripSize=size;
            }else {
                this.datas.addAll(tourTripSize,tourTripList);
                tourTripSize=tourTripSize+size;
            }
        }else {
            tourTripSize=0;
        }
        if(isRefresh){
            this.datas.add("推荐线路");
            List<RecommendLineBean> recommendLineList=tripListBean.getRecommendLineList();//推荐列表
            if(recommendLineList!=null){
                recommendSize=recommendLineList.size();
                this.datas.addAll(recommendLineList);
            }else {
                recommendSize=0;
            }
        }
        notifyDataSetChanged();
    }
    private int speakingPosition=-1;
    /**
     * @param groupId 群id
     * @param isSpeaking 是否正在讲话中
     */
    public void setCurrentIntercomGroup(String groupId,boolean isSpeaking) {
        for(int i=0;i<datas.size();i++){
            Object obj=getItem(i);
            if(obj instanceof TourTripBean){
                TourTripBean mTourTripBean= (TourTripBean) obj;
                if(mTourTripBean.getGroupId().equals(groupId)){
                    mTourTripBean.setSpeaking(isSpeaking);
                    speakingPosition=i;
                    break;
                }
            }
        }
        if(speakingPosition>=0&&speakingPosition<getItemCount()){
            notifyItemChanged(speakingPosition);
        }
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
    @Override
    public int getItemViewType(int position) {
        Object obj=getItem(position);
        if(obj instanceof TourTripBean){
            return TYPE_ITEM_1;
        }else if(obj instanceof RecommendLineBean){
            return TYPE_ITEM_3;
        }else{
            return TYPE_ITEM_2;
        }
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemHolder holder=null;
        if(viewType==TYPE_ITEM_1){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_scheduling_group, parent, false));
        }else  if(viewType==TYPE_ITEM_2){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_scheduling_hot_recommend, parent, false));
        }else  if(viewType==TYPE_ITEM_3){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_scheduling_recommend_route, parent, false));
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
            if(dbBinding instanceof ItemSchedulingGroupBinding){
                ItemSchedulingGroupBinding itemBinding= (ItemSchedulingGroupBinding) this.dbBinding;
                itemBinding.tvInviteFriends.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
                itemBinding.clTravelInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
                itemBinding.groupEnterLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
            }else if(dbBinding instanceof ItemSchedulingRecommendRouteBinding){
                ItemSchedulingRecommendRouteBinding itemBinding= (ItemSchedulingRecommendRouteBinding) this.dbBinding;
                itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
            }else if(dbBinding instanceof ItemSchedulingHotRecommendBinding){
                ItemSchedulingHotRecommendBinding itemBinding= (ItemSchedulingHotRecommendBinding) this.dbBinding;
                itemBinding.tvNewRoute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
                itemBinding.tvFinishedXc.setOnClickListener(new View.OnClickListener() {
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
            Object obj=getItem(position);
            if(dbBinding instanceof ItemSchedulingGroupBinding){
                ItemSchedulingGroupBinding itemBinding= (ItemSchedulingGroupBinding) this.dbBinding;
                if(obj instanceof TourTripBean){
                    TourTripBean mTourTripBean= (TourTripBean) obj;
                    RecentContact recentContact=mTourTripBean.getRecentContact();
                    int unreadCount=0;
                    if(recentContact!=null){
                        unreadCount=recentContact.getUnreadCount();
                    }
                    GlideUtil.loadImgCRadius8(itemBinding.ivPhoto,mTourTripBean.getPhotoUrl());
                    itemBinding.tvTitle.setText(mTourTripBean.getLineName());
                    itemBinding.tvStatus.setText(mTourTripBean.getStateName());
                    if(mTourTripBean.getState()==2){
                        itemBinding.tvStatus.setBackgroundResource(R.drawable.shape_bottom_orange_red_radius_8);
                    }else {
                        itemBinding.tvStatus.setBackgroundResource(R.drawable.shape_bottom_blue_radius_8);
                    }
                    itemBinding.tvDate.setText(mTourTripBean.getStartEndDate());
                    itemBinding.tvDepartureSite.setText(mTourTripBean.getDepartSiteStr());
                    String creatorName=mTourTripBean.getCreatorName();
                    itemBinding.tvCreator.setText(creatorName);
                    if(TextUtils.isEmpty(creatorName)){
                        itemBinding.tvCreator.setVisibility(View.GONE);
                    }else {
                        itemBinding.tvCreator.setVisibility(View.VISIBLE);
                    }
                    if(unreadCount>0){
                        itemBinding.ivUnreadMsg.setVisibility(View.VISIBLE);
                    }else {
                        itemBinding.ivUnreadMsg.setVisibility(View.GONE);
                    }
                    if(mTourTripBean.isSpeaking()){
                        itemBinding.tvGroupEnter.setVisibility(View.INVISIBLE);
                        itemBinding.ivVoicePlayback.setVisibility(View.VISIBLE);
                    }else {
                        itemBinding.tvGroupEnter.setVisibility(View.VISIBLE);
                        itemBinding.ivVoicePlayback.setVisibility(View.INVISIBLE);
                    }
                    itemBinding.tvLineType.setText(mTourTripBean.getLineTypeName());
                    if(TextUtils.isEmpty(mTourTripBean.getLineTypeName())){
                        itemBinding.tvLineType.setVisibility(View.GONE);
                    }else {
                        itemBinding.tvLineType.setVisibility(View.VISIBLE);
                    }
                }
            }else if(dbBinding instanceof ItemSchedulingRecommendRouteBinding){
                ItemSchedulingRecommendRouteBinding itemBinding= (ItemSchedulingRecommendRouteBinding) this.dbBinding;
                if(obj instanceof RecommendLineBean){
                    RecommendLineBean recommendLine= (RecommendLineBean) obj;
                    GlideUtil.loadImgC(itemBinding.ivPhoto,recommendLine.getPhotoUrl());
                    itemBinding.titleTv.setText(recommendLine.getLineName());
                    itemBinding.desTv.setText(recommendLine.getIntro());
                    itemBinding.dateTv.setText(recommendLine.getDepartTimes());
                    itemBinding.priceTv.setText(recommendLine.getMinPriceStr());

                    itemBinding.tvProvider.setText(recommendLine.getOfficeNames());
                    itemBinding.tvLineType.setText(recommendLine.getLineTypeName());
                    if(TextUtils.isEmpty(recommendLine.getLineTypeName())){
                        itemBinding.tvLineType.setVisibility(View.GONE);
                    }else {
                        itemBinding.tvLineType.setVisibility(View.VISIBLE);
                    }
                }
            }else if(dbBinding instanceof ItemSchedulingHotRecommendBinding){
                ItemSchedulingHotRecommendBinding itemBinding= (ItemSchedulingHotRecommendBinding) this.dbBinding;
                if(tourTripSize>0){
                    itemBinding.noDataLayout.setVisibility(View.GONE);
                }else {
                    itemBinding.noDataLayout.setVisibility(View.VISIBLE);
                }
                if(recommendSize>0){
                    itemBinding.routeLayout.setVisibility(View.VISIBLE);
                }else {
                    itemBinding.routeLayout.setVisibility(View.GONE);
                }
            }
        }
    }
}