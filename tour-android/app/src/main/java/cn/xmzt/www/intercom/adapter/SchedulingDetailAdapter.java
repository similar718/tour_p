package cn.xmzt.www.intercom.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemSchedulingDetail1Binding;
import cn.xmzt.www.databinding.ItemSharedNavigationTripDetail1Binding;
import cn.xmzt.www.databinding.ItemSharedNavigationTripDetail2Binding;
import cn.xmzt.www.databinding.ItemSharedNavigationTripDetail3Binding;
import cn.xmzt.www.databinding.ItemSharedNavigationTripDetail4Binding;
import cn.xmzt.www.databinding.ItemSharedNavigationTripDetail5Binding;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.intercom.bean.SchedulingPlan;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import cn.xmzt.www.route.adapter.RouteDetailBannerAdapter1;
import cn.xmzt.www.route.bean.RouteDetailPage;
import cn.xmzt.www.selfdrivingtools.bean.SRouteDetailPlan;
import cn.xmzt.www.selfdrivingtools.bean.TourTripDetailNewBean;

import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.List;


public class SchedulingDetailAdapter extends BaseRecycleViewAdapter<Object, SchedulingDetailAdapter.ItemHolder> {
    private final int TYPE_ITEM_1 = 12;//头部
    private final int TYPE_ITEM_4_0 = 0;// 0 开始
    private final int TYPE_ITEM_4_1 = 1;//1行程信息
    private final int TYPE_ITEM_4_2 = 2;//2景点信息
    private final int TYPE_ITEM_4_3 = 3;//3交通信息
    private final int TYPE_ITEM_4_4 = 4;//4住宿信息
    private final int TYPE_ITEM_4_5 = 5;//5集结地信息
    private final int TYPE_ITEM_4_6 = 6;//6开始地信息
    private final int TYPE_ITEM_4_7 = 7;//7目的地信息
    private final int TYPE_ITEM_4_8 = 8;//8早餐信息
    private final int TYPE_ITEM_4_9 = 9;//8早餐信息
    private final int TYPE_ITEM_4_10 = 10;//8早餐信息
    private final int TYPE_ITEM_4_11 = 11;// 11 结束
    public String groupId;
    public void setData(TourTripDetailNewBean tripDetailBean) {
        this.datas.clear();
        this.datas.add(tripDetailBean);

        List<TourTripDetailNewBean.LineRouteListVOBean> lineRouteList = tripDetailBean.getLineRouteListVO();//行程列表
        for(int i = 0;i < lineRouteList.size(); i++){//行程安排
            // 当天的数据
            TourTripDetailNewBean.LineRouteListVOBean mLineRouteListBean = lineRouteList.get(i);
            TourTripDetailNewBean.LineRouteListVOBean.DetailVOListBean.FirstBean data = new TourTripDetailNewBean.LineRouteListVOBean.DetailVOListBean.FirstBean(mLineRouteListBean.getDayNum(),mLineRouteListBean.getDate(),mLineRouteListBean.getTitle());
            this.datas.add(new SchedulingPlan(null,data,null,TYPE_ITEM_4_0,true));
//            TourTripDetailNewBean.LineRouteListVOBean.DetailVOListBean.EatBean eatBean = new TourTripDetailNewBean.LineRouteListVOBean.DetailVOListBean.EatBean();
//            // 遍历需要显示的后台的所有数据
//            for (int j = 0 ;j < mLineRouteListBean.getDetailVOList().size(); j ++){
//                if (mLineRouteListBean.getDetailVOList().get(j).getDataType() == 8){
//                    eatBean.setBreakfast(mLineRouteListBean.getDetailVOList().get(j).getBreakfast().getMeal());
//                } else if (mLineRouteListBean.getDetailVOList().get(j).getDataType() == 9){
//                    eatBean.setBreakfast(mLineRouteListBean.getDetailVOList().get(j).getLunch().getMeal());
//                } else if (mLineRouteListBean.getDetailVOList().get(j).getDataType() == 10){
//                    eatBean.setBreakfast(mLineRouteListBean.getDetailVOList().get(j).getDinner().getMeal());
//                }
//            }
//            boolean mIsAddEatInfo = false;
            // 遍历需要显示的后台的所有数据
            for (int j = 0 ;j < mLineRouteListBean.getDetailVOList().size(); j ++){
//                if (mLineRouteListBean.getDetailVOList().get(j).getDataType() == 8 || mLineRouteListBean.getDetailVOList().get(j).getDataType() == 9 || mLineRouteListBean.getDetailVOList().get(j).getDataType() == 10){
//                    if (!mIsAddEatInfo){
//                        this.datas.add(new SchedulingPlan(null,null,eatBean,TYPE_ITEM_4_8,false));
//                        mIsAddEatInfo = true;
//                    }
//                } else {
                    this.datas.add(new SchedulingPlan(mLineRouteListBean.getDetailVOList().get(j),null,null,mLineRouteListBean.getDetailVOList().get(j).getDataType(),false));
//                }
            }
            this.datas.add(new SchedulingPlan(null,null,null,TYPE_ITEM_4_11,false));
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
    @Override
    public int getItemViewType(int position) {
        Object obj=getItem(position);
        if(obj instanceof TourTripDetailNewBean){
            return TYPE_ITEM_1;
        }else if(obj instanceof SchedulingPlan){
            SchedulingPlan mSchedulingPlan= (SchedulingPlan) obj;
            return mSchedulingPlan.getPlanType();
        }
        return super.getItemViewType(position);
    }
    public boolean isSingleShow(int position){
        Object obj=getItem(position);
        if(obj instanceof RouteDetailPage.RecommendsBean){
            return false;
        }else {
            return true;
        }
    }
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemHolder holder=null;
//        private final int TYPE_ITEM_4_0 = 0;// 0 开始
//        private final int TYPE_ITEM_4_1 = 1;//1行程信息
//        private final int TYPE_ITEM_4_2 = 2;//2景点信息
//        private final int TYPE_ITEM_4_3 = 3;//3交通信息
//        private final int TYPE_ITEM_4_4 = 4;//4住宿信息
//        private final int TYPE_ITEM_4_5 = 5;//5集结地信息
//        private final int TYPE_ITEM_4_6 = 6;//6开始地信息
//        private final int TYPE_ITEM_4_7 = 7;//7目的地信息
//        private final int TYPE_ITEM_4_8 = 8;//8早餐信息
//        private final int TYPE_ITEM_4_11 = 11;// 11 结束
        if(viewType==TYPE_ITEM_1) {
            holder = new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_scheduling_detail_1, parent, false));
        } else if (viewType == TYPE_ITEM_4_0) { // 开始
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_shared_navigation_trip_detail_1, parent, false));
        } else if (viewType == TYPE_ITEM_4_1){ // 1行程信息
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_shared_navigation_trip_detail_3, parent, false));
        } else if (viewType == TYPE_ITEM_4_2){ // 2景点信息
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_shared_navigation_trip_detail_5, parent, false));
        } else if (viewType == TYPE_ITEM_4_3){ // 3交通信息
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_shared_navigation_trip_detail_3, parent, false));
        } else if (viewType == TYPE_ITEM_4_4){ // 4住宿信息
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_shared_navigation_trip_detail_2, parent, false));
        } else if (viewType == TYPE_ITEM_4_5){ // 5集结地信息
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_shared_navigation_trip_detail_2, parent, false));
        } else if (viewType == TYPE_ITEM_4_6){ // 6开始地信息
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_shared_navigation_trip_detail_3, parent, false));
        } else if (viewType == TYPE_ITEM_4_7){ // 7目的地信息
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_shared_navigation_trip_detail_3, parent, false));
        } else if (viewType == TYPE_ITEM_4_8){ // 8早餐信息
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_shared_navigation_trip_detail_4, parent, false));
        } else if (viewType == TYPE_ITEM_4_9){ // 9午餐信息
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_shared_navigation_trip_detail_4, parent, false));
        } else if (viewType == TYPE_ITEM_4_10){ // 10晚餐信息
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_shared_navigation_trip_detail_4, parent, false));
        } else if (viewType == TYPE_ITEM_4_11){ // 结束
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_shared_navigation_trip_detail_6, parent, false));
        }

//        if(viewType==TYPE_ITEM_1){
//            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_scheduling_detail_1, parent, false));
//        }else if(viewType==TYPE_ITEM_4_1){
//            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_scheduling_detail_4_1, parent, false));
//        }else if(viewType==TYPE_ITEM_4_2){
//            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_scheduling_detail_4_2, parent, false));
//        }else if(viewType==TYPE_ITEM_4_3){
//            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_scheduling_detail_4_3, parent, false));
//        }else if(viewType==TYPE_ITEM_4_4){
//            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_scheduling_detail_4_4, parent, false));
//        }else{
//            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_scheduling_detail_4_5, parent, false));
//        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.setViewDate(position);
    }
    class ItemHolder<DB extends ViewDataBinding> extends RecyclerView.ViewHolder{
        private DB dbBinding;
        private int dp_16=0;
        public ItemHolder(DB dbBinding) {
            super(dbBinding.getRoot());
            this.dbBinding = dbBinding;
            dp_16=dbBinding.getRoot().getResources().getDimensionPixelOffset(R.dimen.dp_16);
            if(dbBinding instanceof ItemSchedulingDetail1Binding){
                ItemSchedulingDetail1Binding itemBinding= (ItemSchedulingDetail1Binding) this.dbBinding;
                itemBinding.ivAvatarMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //更多成员列表
                        if(itemListener!=null){
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
                itemBinding.avatarPileList.setGroupId(groupId);
            }else if(dbBinding instanceof ItemSharedNavigationTripDetail5Binding){
                ItemSharedNavigationTripDetail5Binding itemBinding= (ItemSharedNavigationTripDetail5Binding) this.dbBinding;
                itemBinding.banner.isAutoPlay(false);
                itemBinding.banner.setIndicatorGravity(BannerConfig.RIGHT);
                itemBinding.banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
                itemBinding.banner.setImageLoader(new RouteDetailBannerAdapter1(8));
                itemBinding.banner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {

                    }
                });
            }
        }
        private void setViewDate(int position){
            if(dbBinding instanceof ItemSchedulingDetail1Binding){
                ItemSchedulingDetail1Binding itemBinding= (ItemSchedulingDetail1Binding) this.dbBinding;
                Object obj=getItem(position);
                if(obj instanceof TourTripDetailNewBean){
                    TourTripDetailNewBean tripDetailBean= (TourTripDetailNewBean) obj;
                    itemBinding.setItem(tripDetailBean);
                    GlideUtil.loadImgRoundRadius(itemBinding.ivPhotoUrl,tripDetailBean.getPhotoUrl(),4);
                    List<GroupMemberBean> groupMemberList=tripDetailBean.getGroupMemberList();
                    /*if(groupMemberList!=null&&groupMemberList.size()>0){
                        itemBinding.ivAvatarMore.setVisibility(View.VISIBLE);
                    }else {
                        itemBinding.ivAvatarMore.setVisibility(View.GONE);
                    }*/
                    try {
                        itemBinding.avatarPileList.setDataList(tripDetailBean.getGroupMemberList());
                    }catch (Exception e){}
                }
            }else if(dbBinding instanceof ItemSharedNavigationTripDetail1Binding){ // 开始 0
                ItemSharedNavigationTripDetail1Binding itemBinding= (ItemSharedNavigationTripDetail1Binding) this.dbBinding;
                Object obj=getItem(position);
                if(obj instanceof SchedulingPlan){
                    if (((SchedulingPlan) obj).getFirst().getDay()>9){
                        itemBinding.tvTime.setText(((SchedulingPlan) obj).getFirst().getDay()+"");
                    } else {
                        itemBinding.tvTime.setText("0"+((SchedulingPlan) obj).getFirst().getDay());
                    }
                    itemBinding.tvTitle1.setText(((SchedulingPlan) obj).getFirst().getDate() +"  "+((SchedulingPlan) obj).getFirst().getContent());
                }
            }else if(dbBinding instanceof ItemSharedNavigationTripDetail2Binding){ // staging live 5 4
                ItemSharedNavigationTripDetail2Binding itemBinding= (ItemSharedNavigationTripDetail2Binding) this.dbBinding;
                Object obj=getItem(position);
                if(obj instanceof SchedulingPlan){
                    SchedulingPlan mSchedulingPlan= (SchedulingPlan) obj;
                    if (mSchedulingPlan.getPlanType() == 5){ // staging 5集结地信息
                        // icon的显示
                        itemBinding.dotImg1.setImageResource(R.drawable.shared_navigation_trip_list_jihe_icon);
                        // 名称
                        itemBinding.tvTitle1.setText("集结点");
                        // 内容
                        TourTripDetailNewBean.LineRouteListVOBean.DetailVOListBean.StagingBean mStagingBean=mSchedulingPlan.getPlan().getStaging();
                        if(mStagingBean!=null){
                            String address=mStagingBean.getAddress();
                            if (!TextUtils.isEmpty(address)){
                                itemBinding.contentTv.setText(address);
                            } else {
                                if (mStagingBean.getAreaVO() != null){
                                    itemBinding.contentTv.setText(mStagingBean.getAreaVO().getAreaName());
                                }
                            }
                        }
                        // 需要更改字体颜色 和 箭头不需要
                        itemBinding.contentTv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        itemBinding.contentTv.setTextColor(Color.parseColor("#666666"));
                    } else if (((SchedulingPlan) obj).getPlanType() == 4){ // live 4住宿信息
                        // icon的显示
                        itemBinding.dotImg1.setImageResource(R.drawable.shared_navigation_trip_list_live_icon);
                        // 名称
                        itemBinding.tvTitle1.setText("住宿");
                        // 内容
                        itemBinding.contentTv.setText(((SchedulingPlan) obj).getPlan().getHotel().getAccommodation());
                        // 需要更改字体颜色 和 箭头需要
                        itemBinding.contentTv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        itemBinding.contentTv.setTextColor(Color.parseColor("#666666"));
                    }
                }
            }else if(dbBinding instanceof ItemSharedNavigationTripDetail3Binding){ // 1 行程 3 交通 6 开始地 7 目的地
                ItemSharedNavigationTripDetail3Binding itemBinding= (ItemSharedNavigationTripDetail3Binding) this.dbBinding;
                Object obj=getItem(position);
                if(obj instanceof SchedulingPlan){
                    SchedulingPlan mSchedulingPlan= (SchedulingPlan) obj;
                    if (mSchedulingPlan.getPlanType() == 1){ // 行程
                        // icon的显示
                        itemBinding.dotImg1.setImageResource(R.drawable.shared_navigation_trip_list_trip_icon);
                        // 名称
                        itemBinding.tvTitle1.setText("行程");
                        // 内容
                        itemBinding.tvContent.setText(((SchedulingPlan) obj).getPlan().getContent().getContent());
                    } else if (mSchedulingPlan.getPlanType() == 3){ // 交通
                        // icon的显示
                        itemBinding.dotImg1.setImageResource(R.drawable.shared_navigation_trip_list_bus_icon);
                        // 名称
                        itemBinding.tvTitle1.setText("交通");
                        // 内容
                        itemBinding.tvContent.setText(((SchedulingPlan) obj).getPlan().getCar().getCar());
                    } else if (mSchedulingPlan.getPlanType() == 6){ // 开始地
                        // icon的显示
                        itemBinding.dotImg1.setImageResource(R.drawable.scheduling_cfd_icon);
                        // 名称
                        itemBinding.tvTitle1.setText("出发地");
                        // 内容
                        TourTripDetailNewBean.LineRouteListVOBean.DetailVOListBean.StartBean mStartBean=mSchedulingPlan.getPlan().getStart();
                        if(mStartBean!=null){
                            String address=mStartBean.getAddress();
                            if (!TextUtils.isEmpty(address)){
                                itemBinding.tvContent.setText(address);
                            } else {
                                if (mStartBean.getAreaVO() != null){
                                    itemBinding.tvContent.setText(mStartBean.getAreaVO().getAreaName());
                                }
                            }
                        }
                    } else if (mSchedulingPlan.getPlanType() == 7){ // 目的地
                        // icon的显示
                        itemBinding.dotImg1.setImageResource(R.drawable.scheduling_mdd_icon);
                        // 名称
                        itemBinding.tvTitle1.setText("目的地");
                        // 内容
                        TourTripDetailNewBean.LineRouteListVOBean.DetailVOListBean.EndBean mEndBean=mSchedulingPlan.getPlan().getEnd();
                        if(mEndBean!=null){
                            String address=mEndBean.getAddress();
                            if (!TextUtils.isEmpty(address)){
                                itemBinding.tvContent.setText(address);
                            } else {
                                if (mEndBean.getAreaVO() != null){
                                    itemBinding.tvContent.setText(mEndBean.getAreaVO().getAreaName());
                                }
                            }
                        }
                    }
                }
            }else if(dbBinding instanceof ItemSharedNavigationTripDetail4Binding){ // 8 9 10 eat
                ItemSharedNavigationTripDetail4Binding itemBinding= (ItemSharedNavigationTripDetail4Binding) this.dbBinding;
                Object obj=getItem(position);
                if(obj instanceof SchedulingPlan){
                    SchedulingPlan mSRouteDetailPlan= (SchedulingPlan) obj;
                    if (mSRouteDetailPlan.getPlanType() == 8){ // 早餐
                        String data = ((SchedulingPlan) obj).getPlan().getBreakfast().getMeal();
                        if (TextUtils.isEmpty(data)){
                            itemBinding.tvMorningContent.setText("自理");
                        } else {
                            itemBinding.tvMorningContent.setText(data);
                        }
                        itemBinding.rlMorning.setVisibility(View.VISIBLE);
                        itemBinding.rlLunch.setVisibility(View.GONE);
                        itemBinding.rlDinner.setVisibility(View.GONE);
                    } else if (mSRouteDetailPlan.getPlanType() == 9) { // 午餐
                        String data = ((SchedulingPlan) obj).getPlan().getLunch().getMeal();
                        if (TextUtils.isEmpty(data)){
                            itemBinding.tvAfterContent.setText("自理");
                        } else {
                            itemBinding.tvAfterContent.setText(data);
                        }
                        itemBinding.rlMorning.setVisibility(View.GONE);
                        itemBinding.rlLunch.setVisibility(View.VISIBLE);
                        itemBinding.rlDinner.setVisibility(View.GONE);
                    } else if (mSRouteDetailPlan.getPlanType() == 10){ // 晚餐
                        String data = ((SchedulingPlan) obj).getPlan().getDinner().getMeal();
                        if (TextUtils.isEmpty(data)){
                            itemBinding.tvEveningContent.setText("自理");
                        } else {
                            itemBinding.tvEveningContent.setText(data);
                        }
                        itemBinding.rlMorning.setVisibility(View.GONE);
                        itemBinding.rlLunch.setVisibility(View.GONE);
                        itemBinding.rlDinner.setVisibility(View.VISIBLE);
                    }
                }
            }else if(dbBinding instanceof ItemSharedNavigationTripDetail5Binding){ // spot 2
                ItemSharedNavigationTripDetail5Binding itemBinding= (ItemSharedNavigationTripDetail5Binding) this.dbBinding;
                Object obj=getItem(position);
                if(obj instanceof SchedulingPlan){
                    String address = ((SchedulingPlan) obj).getPlan().getSpot().getAddress();
                    itemBinding.tvTitle1.setText(TextUtils.isEmpty(address) ? "景点" : address);
                    itemBinding.tvScenicSpotVoice.setVisibility(View.GONE);
                    itemBinding.tvScenicSpotName.setText(((SchedulingPlan) obj).getPlan().getSpot().getTitle());
                    itemBinding.tvScenicSpotName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    itemBinding.tvScenicSpotName.setTextColor(Color.parseColor("#666666"));
                    if (((SchedulingPlan) obj).getPlan().getSpot().getImageArray()!= null) {
                        if (((SchedulingPlan) obj).getPlan().getSpot().getImageArray().size()>0){
                            itemBinding.banner.setVisibility(View.VISIBLE);
                            itemBinding.banner.setImages(((SchedulingPlan) obj).getPlan().getSpot().getImageArray());
                            itemBinding.banner.start();
                        } else {
                            itemBinding.banner.setVisibility(View.GONE);
                        }
                    } else {
                        itemBinding.banner.setVisibility(View.GONE);
                    }
                    itemBinding.expandTextView.setText(((SchedulingPlan) obj).getPlan().getSpot().getContent());
                }
            }

//            if(dbBinding instanceof ItemSchedulingDetail41Binding){
//                ItemSchedulingDetail41Binding itemBinding= (ItemSchedulingDetail41Binding) this.dbBinding;
//                Object obj=getItem(position);
//                if(obj instanceof SchedulingPlan){
//                    itemBinding.setItem((SchedulingPlan) obj);
//                }
//            }else if(dbBinding instanceof ItemSchedulingDetail42Binding){
//                ItemSchedulingDetail42Binding itemBinding= (ItemSchedulingDetail42Binding) this.dbBinding;
//                Object obj=getItem(position);
//                if(obj instanceof SchedulingPlan){
//                    SchedulingPlan plan= (SchedulingPlan) obj;
//                    itemBinding.setItem(plan);
//                    ViewGroup.LayoutParams mLayoutParams=itemBinding.tvContent1.getLayoutParams();
//                    String content=plan.getPlan().getStaging().getAreaName();
//                    if(TextUtils.isEmpty(content)){
//                        content="";
//                        mLayoutParams.height = dp_16;
//                    }else {
//                        mLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                    }
//                }
//            }else if(dbBinding instanceof ItemSchedulingDetail43Binding){
//                ItemSchedulingDetail43Binding itemBinding= (ItemSchedulingDetail43Binding) this.dbBinding;
//                Object obj=getItem(position);
//                if(obj instanceof SchedulingPlan){
//                    itemBinding.setItem((SchedulingPlan) obj);
//                }
//            }else if(dbBinding instanceof ItemSchedulingDetail44Binding){
//                ItemSchedulingDetail44Binding itemBinding= (ItemSchedulingDetail44Binding) this.dbBinding;
//                Object obj=getItem(position);
//                if(obj instanceof SchedulingPlan){
//                    SchedulingPlan plan= (SchedulingPlan) obj;
//                    itemBinding.setItem(plan);
//                    ViewGroup.LayoutParams mLayoutParams=itemBinding.tvContent1.getLayoutParams();
//                    String content=plan.getPlan().getAccommodation();
//                    if(TextUtils.isEmpty(content)){
//                        content="";
//                        mLayoutParams.height = dp_16;
//                    }else {
//                        mLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                    }
//                }
//            }else if(dbBinding instanceof ItemSchedulingDetail45Binding){
//                ItemSchedulingDetail45Binding itemBinding= (ItemSchedulingDetail45Binding) this.dbBinding;
//                Object obj=getItem(position);
//                if(obj instanceof TourTripDetailNewBean.LineRouteListBean.SpotsListBean){
//                    TourTripDetailNewBean.LineRouteListBean.SpotsListBean mSpotsListBean= (TourTripDetailNewBean.LineRouteListBean.SpotsListBean) obj;
//                    itemBinding.setItem(mSpotsListBean);
//                    List<String> images =mSpotsListBean.getImages();
//                    itemBinding.banner.setVisibility(View.VISIBLE);
//                    if(images!=null&&images.size()>0){
//                        itemBinding.banner.setImages(images);
//                        itemBinding.banner.start();
//                    }else {
//                        itemBinding.banner.setVisibility(View.GONE);
//                    }
//                    Object obj1=getItem(position+1);
//                    if(obj1 instanceof TourTripDetailNewBean.LineRouteListBean.SpotsListBean){
//                        itemBinding.contentLayout.setBackgroundResource(R.drawable.bg_time_line);
//                    }else {
//                        itemBinding.contentLayout.setBackgroundColor(Color.WHITE);
//                    }
//                    /*if(position==getItemCount()-1){
//                        itemBinding.contentLayout.setBackgroundColor(Color.WHITE);
//                    }else {
//                        itemBinding.contentLayout.setBackgroundResource(R.drawable.bg_time_line);
//                    }*/
//                }
//            }
        }
    }
}