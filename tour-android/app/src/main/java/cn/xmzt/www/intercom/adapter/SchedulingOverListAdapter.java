package cn.xmzt.www.intercom.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemSchedulingOverGroupBinding;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.intercom.bean.TourTripBean;
import cn.xmzt.www.intercom.bean.TourTripListBean;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.ArrayList;
import java.util.List;


public class SchedulingOverListAdapter extends BaseRecycleViewAdapter<TourTripBean, SchedulingOverListAdapter.ItemHolder> {
    public void setData(TourTripListBean tripListBean,boolean isRefresh) {
        if(isRefresh){
            this.datas.clear();
        }
        List<TourTripBean> tourTripList=tripListBean.getTourTripList();//行程列表
        if(tourTripList!=null){
            this.datas.addAll(tourTripList);
        }
        notifyDataSetChanged();
    }
    public List<TourTripBean> selectList = new ArrayList<TourTripBean>();
    public boolean isAllSelect(){
        return this.datas.size()==selectList.size();
    }
    public String getGroupIds(){
        StringBuilder sb=new StringBuilder();
        if(selectList.size()>0){
            for(int i=0;i<selectList.size();i++){
                TourTripBean mTourTripBean=datas.get(i);
                if(sb.length()>0){
                    sb.append(",") ;
                    sb.append(mTourTripBean.getGroupId()) ;
                }else {
                    sb.append(mTourTripBean.getGroupId()) ;
                }
            }
        }
        return sb.toString();
    }

    /**
     * 全选、全消
     * @param isAllSelect
     */
    public void setAllSelect(boolean isAllSelect){
        if(isAllSelect){
            selectList.clear();
            for(int i=0;i<datas.size();i++){
                TourTripBean mTourTripBean=datas.get(i);
                mTourTripBean.setSelect(true);
                selectList.add(mTourTripBean);
            }
        }else {
            for(TourTripBean mTourTripBean:selectList){
                mTourTripBean.setSelect(false);
            }
            selectList.clear();
        }
        notifyDataSetChanged();
    }
    private boolean isEdit=false;

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit=edit;
        if(!edit){
            for(TourTripBean mTourTripBean:selectList){
                mTourTripBean.setSelect(false);
            }
            selectList.clear();
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
            TourTripBean mTourTripBean=datas.get(i);
            if(mTourTripBean.getGroupId().equals(groupId)){
                mTourTripBean.setSpeaking(isSpeaking);
                speakingPosition=i;
                break;
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
    public TourTripBean getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }
        return null;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemHolder holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_scheduling_over_group, parent, false));
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
            if(dbBinding instanceof ItemSchedulingOverGroupBinding){
                ItemSchedulingOverGroupBinding itemBinding= (ItemSchedulingOverGroupBinding) this.dbBinding;
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
                itemBinding.ivCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=getAdapterPosition();
                        if(itemListener!=null){
                            itemListener.onItemClick(v,position);
                        }
                    }
                });
            }
        }
        private void setViewDate(int position){
            Object obj=getItem(position);
            if(dbBinding instanceof ItemSchedulingOverGroupBinding){
                ItemSchedulingOverGroupBinding itemBinding= (ItemSchedulingOverGroupBinding) this.dbBinding;
                if(obj instanceof TourTripBean){
                    TourTripBean mTourTripBean= (TourTripBean) obj;
                    GlideUtil.loadImgRadius(itemBinding.ivPhoto,4,mTourTripBean.getPhotoUrl());
                    RecentContact recentContact=mTourTripBean.getRecentContact();
                    int unreadCount=0;
                    if(recentContact!=null){
                        unreadCount=recentContact.getUnreadCount();
                    }
                    itemBinding.setItem(mTourTripBean);

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
                    itemBinding.ivCheck.setSelected(mTourTripBean.isSelect());
                    if(isEdit){
                        itemBinding.ivCheck.setVisibility(View.VISIBLE);
                    }else {
                        itemBinding.ivCheck.setVisibility(View.GONE);
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
            }
        }
    }
}