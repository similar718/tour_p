package cn.xmzt.www.selfdrivingtools.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ActivityUtils;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemSharedNavigationTripDetail1Binding;
import cn.xmzt.www.databinding.ItemSharedNavigationTripDetail2Binding;
import cn.xmzt.www.databinding.ItemSharedNavigationTripDetail3Binding;
import cn.xmzt.www.databinding.ItemSharedNavigationTripDetail4Binding;
import cn.xmzt.www.databinding.ItemSharedNavigationTripDetail5Binding;
import cn.xmzt.www.intercom.bean.SchedulingPlan;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import cn.xmzt.www.route.adapter.RouteDetailBannerAdapter1;
import cn.xmzt.www.selfdrivingtools.activity.SharedNavigationMapActivity;
import cn.xmzt.www.selfdrivingtools.bean.SRouteDetailPlan;
import cn.xmzt.www.selfdrivingtools.bean.TourTripDetailNewBean;
import cn.xmzt.www.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *  shared navigation trip detail info
 */
public class SharedNavigationTripListAdapter extends BaseRecycleViewAdapter<Object, SharedNavigationTripListAdapter.ItemHolder> {
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
    List<SRouteDetailPlan> planList = new ArrayList<>();
    public int[] day = null;
    // 数据类型：1行程信息 2景点信息 3交通信息 4住宿信息 5集结地信息 6开始地信息 7目的地信息 8早餐信息 9午餐信息 10晚餐信息
    public void setData(TourTripDetailNewBean mRouteDetailPage) {
        this.datas.clear();

        List<TourTripDetailNewBean.LineRouteListVOBean> lineRouteList = mRouteDetailPage.getLineRouteListVO();//trip list
        day = new int[lineRouteList.size()];
        int position = 0;
        for(int i = 0;i < lineRouteList.size(); i++){
            // 当天的数据
            TourTripDetailNewBean.LineRouteListVOBean mLineRouteListBean = lineRouteList.get(i);
            TourTripDetailNewBean.LineRouteListVOBean.DetailVOListBean.FirstBean data = new TourTripDetailNewBean.LineRouteListVOBean.DetailVOListBean.FirstBean(mLineRouteListBean.getDayNum(),mLineRouteListBean.getDate(),mLineRouteListBean.getTitle());
            planList.add(new SRouteDetailPlan(null,data,null,TYPE_ITEM_4_0,true));
            day[i] = position;
            position += 1;
            // 遍历需要显示的后台的所有数据
            for (int j = 0 ;j < mLineRouteListBean.getDetailVOList().size(); j ++) {
                planList.add(new SRouteDetailPlan(mLineRouteListBean.getDetailVOList().get(j), null, null, mLineRouteListBean.getDetailVOList().get(j).getDataType(), false));
                position++;
            }
            planList.add(new SRouteDetailPlan(null,null,null,TYPE_ITEM_4_11,false));
            position ++;
        }
        this.datas.addAll(planList);
        notifyDataSetChanged();
    }

    public SharedNavigationTripListAdapter(Context context){
        mContext = context;
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
        if(obj instanceof SRouteDetailPlan){
            SRouteDetailPlan mRouteDetailPlan= (SRouteDetailPlan) obj;
            int dataType = mRouteDetailPlan.getPlanType();
            // 1行程信息 2景点信息 3交通信息 4住宿信息 5集结地信息 6开始地信息 7目的地信息 8早餐信息 9午餐信息 10晚餐信息
            return dataType;
        }
        return super.getItemViewType(position);
    }
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemHolder holder=null;
        // 0开始 1行程信息 2景点信息 3交通信息 4住宿信息 5集结地信息 6开始地信息 7目的地信息 8早餐信息 9午餐信息 10晚餐信息 11结束
        if (viewType == TYPE_ITEM_4_0) { // 开始
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
        return holder;
    }

    private static Context mContext;

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.setViewDate(holder,position);
    }
    class ItemHolder<DB extends ViewDataBinding> extends RecyclerView.ViewHolder{
        private DB dbBinding;
        public ItemHolder(DB dbBinding) {
            super(dbBinding.getRoot());
            this.dbBinding = dbBinding;
            if(dbBinding instanceof ItemSharedNavigationTripDetail2Binding){
                ItemSharedNavigationTripDetail2Binding itemBinding= (ItemSharedNavigationTripDetail2Binding) dbBinding;
                itemBinding.contentTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Object object = getItem(getAdapterPosition());
                        if(object instanceof SRouteDetailPlan){
                            if (((SRouteDetailPlan) object).getPlanType() == TYPE_ITEM_4_5){ // 5集结地信息
                                if (((SRouteDetailPlan) object).getPlan().getStaging().getCoordinateVO()!=null) {
                                ((SharedNavigationMapActivity)mContext).setBottomBlueTextClick(((SRouteDetailPlan) object).getPlan().getStaging().getCoordinateVO().getLatitude(),((SRouteDetailPlan) object).getPlan().getStaging().getCoordinateVO().getLongitude(),getAdapterPosition(),2);
                                }
                            } else if (((SRouteDetailPlan) object).getPlanType() == TYPE_ITEM_4_4){ // 4住宿信息
                                if (((SRouteDetailPlan) object).getPlan().getHotel().getCoordinateVO() != null) {
                                    ((SharedNavigationMapActivity) mContext).setBottomBlueTextClick(((SRouteDetailPlan) object).getPlan().getHotel().getCoordinateVO().getLatitude(), ((SRouteDetailPlan) object).getPlan().getHotel().getCoordinateVO().getLongitude(), getAdapterPosition(), 6);
                                } else {
                                    ToastUtils.showText(ActivityUtils.getTopActivity(),"当前酒店无位置信息");
                                }
                            }
                        }
                    }
                });
            } else if (dbBinding instanceof ItemSharedNavigationTripDetail5Binding){
                ItemSharedNavigationTripDetail5Binding itemBinding= (ItemSharedNavigationTripDetail5Binding) dbBinding;
                itemBinding.tvScenicSpotName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Object object = getItem(getAdapterPosition());
                        if(object instanceof SRouteDetailPlan) {
                            if (((SRouteDetailPlan) object).getPlan().getSpot().getCoordinateVO() != null) {
                                double lat = ((SRouteDetailPlan) object).getPlan().getSpot().getCoordinateVO().getLatitude();
                                double lng = ((SRouteDetailPlan) object).getPlan().getSpot().getCoordinateVO().getLongitude();
                                int position = getPosition();
                                int type = 7;
                                ((SharedNavigationMapActivity) mContext).setBottomBlueTextClick(lat, lng, position, type);
                            } else {
                                ToastUtils.showText(ActivityUtils.getTopActivity(), "当前景点无位置信息");
                            }
                        }
                    }
                });
                itemBinding.tvScenicSpotVoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 语音讲解
                        Object object = getItem(getAdapterPosition());
                        if(object instanceof SRouteDetailPlan) {
                            if (((SRouteDetailPlan) object).getPlan().getSpot().getScenicId() <= 0) {
                                ToastUtils.showText(mContext, "抱歉,暂无此景区的语音讲解内容");
                            } else {
                                ((SharedNavigationMapActivity) mContext).setSpotVoiceTextClick(((SRouteDetailPlan) object).getPlan().getSpot().getScenicId());
                            }
                        }
                    }
                });
                itemBinding.banner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
                itemBinding.expandTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
                itemBinding.banner.isAutoPlay(true);
                itemBinding.banner.setIndicatorGravity(BannerConfig.RIGHT);
                itemBinding.banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
                itemBinding.banner.setImageLoader(new RouteDetailBannerAdapter1(8));
                itemBinding.banner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {

                    }
                });
            } else if (dbBinding instanceof ItemSharedNavigationTripDetail4Binding){
                ItemSharedNavigationTripDetail4Binding itemBinding= (ItemSharedNavigationTripDetail4Binding) dbBinding;
                itemBinding.tvMorningContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Object object = getItem(getAdapterPosition());
                        if(object instanceof SRouteDetailPlan) {
                            if (((SRouteDetailPlan) object).getPlan().getBreakfast().getCoordinateVO() != null) {
                                double lat = ((SRouteDetailPlan) object).getPlan().getBreakfast().getCoordinateVO().getLatitude();
                                double lng = ((SRouteDetailPlan) object).getPlan().getBreakfast().getCoordinateVO().getLongitude();
                                int position = getPosition();
                                int type = 10;
                                ((SharedNavigationMapActivity) mContext).setBottomBlueTextClick(lat, lng, position, type);
                            }
                        }
                    }
                });
                itemBinding.tvAfterContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Object object = getItem(getAdapterPosition());
                        if(object instanceof SRouteDetailPlan) {
                            if (((SRouteDetailPlan) object).getPlan().getLunch().getCoordinateVO() != null) {
                                double lat = ((SRouteDetailPlan) object).getPlan().getLunch().getCoordinateVO().getLatitude();
                                double lng = ((SRouteDetailPlan) object).getPlan().getLunch().getCoordinateVO().getLongitude();
                                int position = getPosition();
                                int type = 11;
                                ((SharedNavigationMapActivity) mContext).setBottomBlueTextClick(lat, lng, position, type);
                            }
                        }
                    }
                });
                itemBinding.tvEveningContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Object object = getItem(getAdapterPosition());
                        if(object instanceof SRouteDetailPlan) {
                            if (((SRouteDetailPlan) object).getPlan().getDinner().getCoordinateVO() != null) {
                                double lat = ((SRouteDetailPlan) object).getPlan().getDinner().getCoordinateVO().getLatitude();
                                double lng = ((SRouteDetailPlan) object).getPlan().getDinner().getCoordinateVO().getLongitude();
                                int position = getPosition();
                                int type = 12;
                                ((SharedNavigationMapActivity) mContext).setBottomBlueTextClick(lat, lng, position, type);
                            }
                        }
                    }
                });
            }
        }
        private void setViewDate(ItemHolder holder,int position){
            if(dbBinding instanceof ItemSharedNavigationTripDetail1Binding){ // 开始 0
                ItemSharedNavigationTripDetail1Binding itemBinding= (ItemSharedNavigationTripDetail1Binding) this.dbBinding;
                Object obj=getItem(position);
                if(obj instanceof SRouteDetailPlan){
                    if (((SRouteDetailPlan) obj).getFirst().getDay()>9){
                        itemBinding.tvTime.setText(((SRouteDetailPlan) obj).getFirst().getDay()+"");
                    } else {
                        itemBinding.tvTime.setText("0"+((SRouteDetailPlan) obj).getFirst().getDay());
                    }
                    itemBinding.tvTitle1.setText(((SRouteDetailPlan) obj).getFirst().getDate() +"  "+((SRouteDetailPlan) obj).getFirst().getContent());
                }
            }else if(dbBinding instanceof ItemSharedNavigationTripDetail2Binding){ // staging live 5 4
                ItemSharedNavigationTripDetail2Binding itemBinding= (ItemSharedNavigationTripDetail2Binding) this.dbBinding;
                Object obj=getItem(position);
                if(obj instanceof SRouteDetailPlan){
                    SRouteDetailPlan mSRouteDetailPlan= (SRouteDetailPlan) obj;
                    if (mSRouteDetailPlan.getPlanType() == 5){ // staging 5集结地信息
                        // icon的显示
                        itemBinding.dotImg1.setImageResource(R.drawable.shared_navigation_trip_list_jihe_icon);
                        // 名称
                        itemBinding.tvTitle1.setText("集结点");
                        // 内容
                        TourTripDetailNewBean.LineRouteListVOBean.DetailVOListBean.StagingBean mStagingBean=mSRouteDetailPlan.getPlan().getStaging();
                        if(mStagingBean!=null){
                            String address=mStagingBean.getAddress();
                            if (!TextUtils.isEmpty(address)){
                                itemBinding.contentTv.setText(address);
                            } else {
                                if (mStagingBean.getAreaVO() != null){
                                    itemBinding.contentTv.setText(mStagingBean.getAreaVO().getAreaName());
                                } else {
                                    itemBinding.contentTv.setText("无");
                                }
                            }
                        }
                        if (mSRouteDetailPlan.getPlan().getStaging().getCoordinateVO()!=null){
                            // 需要更改字体颜色 和 箭头不需要
                            itemBinding.contentTv.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.shared_navigation_trip_list_right_icon), null);
                            itemBinding.contentTv.setTextColor(Color.parseColor("#24A4FF"));
                        } else {
                            // 需要更改字体颜色 和 箭头不需要
                            itemBinding.contentTv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                            itemBinding.contentTv.setTextColor(Color.parseColor("#666666"));
                        }
                    } else if (mSRouteDetailPlan.getPlanType() == 4){ // live 4住宿信息
                        // icon的显示
                        itemBinding.dotImg1.setImageResource(R.drawable.shared_navigation_trip_list_live_icon);
                        // 名称
                        itemBinding.tvTitle1.setText("住宿");
                        // 内容
                        itemBinding.contentTv.setText(((SRouteDetailPlan) obj).getPlan().getHotel().getAccommodation());
                        // 需要更改字体颜色 和 箭头需要
                        itemBinding.contentTv.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.shared_navigation_trip_list_right_icon), null);
                        itemBinding.contentTv.setTextColor(Color.parseColor("#24A4FF"));
                    }
                }
            }else if(dbBinding instanceof ItemSharedNavigationTripDetail3Binding){ // 1 行程 3 交通 6 开始地 7 目的地
                ItemSharedNavigationTripDetail3Binding itemBinding= (ItemSharedNavigationTripDetail3Binding) this.dbBinding;
                Object obj=getItem(position);
                if(obj instanceof SRouteDetailPlan){
                    SRouteDetailPlan mSRouteDetailPlan= (SRouteDetailPlan) obj;
                    if (mSRouteDetailPlan.getPlanType() == 1){ // 行程
                        // icon的显示
                        itemBinding.dotImg1.setImageResource(R.drawable.shared_navigation_trip_list_trip_icon);
                        // 名称
                        itemBinding.tvTitle1.setText("行程");
                        // 内容
                        itemBinding.tvContent.setText(((SRouteDetailPlan) obj).getPlan().getContent().getContent());
                    } else if (mSRouteDetailPlan.getPlanType() == 3){ // 交通
                        // icon的显示
                        itemBinding.dotImg1.setImageResource(R.drawable.shared_navigation_trip_list_bus_icon);
                        // 名称
                        itemBinding.tvTitle1.setText("交通");
                        // 内容
                        itemBinding.tvContent.setText(((SRouteDetailPlan) obj).getPlan().getCar().getCar());
                    } else if (mSRouteDetailPlan.getPlanType() == 6){ // 开始地
                        // icon的显示
                        itemBinding.dotImg1.setImageResource(R.drawable.scheduling_cfd_icon);
                        // 名称
                        itemBinding.tvTitle1.setText("出发地");
                        // 内容
                        TourTripDetailNewBean.LineRouteListVOBean.DetailVOListBean.StartBean mStartBean=mSRouteDetailPlan.getPlan().getStart();
                        if(mStartBean!=null){
                            String address=mStartBean.getAddress();
                            if (!TextUtils.isEmpty(address)){
                                itemBinding.tvContent.setText(address);
                            } else {
                                if (mStartBean.getAreaVO() != null){
                                    itemBinding.tvContent.setText(mStartBean.getAreaVO().getAreaName());
                                } else {
                                    itemBinding.tvContent.setText("无");
                                }
                            }
                        }
                    } else if (mSRouteDetailPlan.getPlanType() == 7){ // 目的地
                        // icon的显示
                        itemBinding.dotImg1.setImageResource(R.drawable.scheduling_mdd_icon);
                        // 名称
                        itemBinding.tvTitle1.setText("目的地");
                        // 内容
                        TourTripDetailNewBean.LineRouteListVOBean.DetailVOListBean.EndBean mEndBean=mSRouteDetailPlan.getPlan().getEnd();
                        if(mEndBean!=null){
                            String address=mEndBean.getAddress();
                            if (!TextUtils.isEmpty(address)){
                                itemBinding.tvContent.setText(address);
                            } else {
                                if (mEndBean.getAreaVO() != null){
                                    itemBinding.tvContent.setText(mEndBean.getAreaVO().getAreaName());
                                } else {
                                    itemBinding.tvContent.setText("无");
                                }
                            }
                        }
                    }
                }
            }else if(dbBinding instanceof ItemSharedNavigationTripDetail4Binding){ // 8 9 10 eat
                ItemSharedNavigationTripDetail4Binding itemBinding= (ItemSharedNavigationTripDetail4Binding) this.dbBinding;
                Object obj=getItem(position);
                if(obj instanceof SRouteDetailPlan){
                    SRouteDetailPlan mSRouteDetailPlan= (SRouteDetailPlan) obj;
                    if (mSRouteDetailPlan.getPlanType() == 8){ // 早餐
                        String data = ((SRouteDetailPlan) obj).getPlan().getBreakfast().getMeal();
                        if (TextUtils.isEmpty(data)){
                            itemBinding.tvMorningContent.setText("自理");
                        } else {
                            itemBinding.tvMorningContent.setText(data);
                        }

                        if (mSRouteDetailPlan.getPlan().getBreakfast().getCoordinateVO() != null) {
                            // 需要更改字体颜色 和 箭头需要
                            itemBinding.tvMorningContent.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.shared_navigation_trip_list_right_icon), null);
                            itemBinding.tvMorningContent.setTextColor(Color.parseColor("#24A4FF"));
                        } else {
                            itemBinding.tvMorningContent.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                            itemBinding.tvMorningContent.setTextColor(Color.parseColor("#666666"));
                        }

                        itemBinding.rlMorning.setVisibility(View.VISIBLE);
                        itemBinding.rlLunch.setVisibility(View.GONE);
                        itemBinding.rlDinner.setVisibility(View.GONE);
                    } else if (mSRouteDetailPlan.getPlanType() == 9) { // 午餐
                        String data = ((SRouteDetailPlan) obj).getPlan().getLunch().getMeal();
                        if (TextUtils.isEmpty(data)){
                            itemBinding.tvAfterContent.setText("自理");
                        } else {
                            itemBinding.tvAfterContent.setText(data);
                        }

                        if (mSRouteDetailPlan.getPlan().getLunch().getCoordinateVO() != null) {
                            // 需要更改字体颜色 和 箭头需要
                            itemBinding.tvAfterContent.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.shared_navigation_trip_list_right_icon), null);
                            itemBinding.tvAfterContent.setTextColor(Color.parseColor("#24A4FF"));
                        } else {
                            itemBinding.tvAfterContent.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                            itemBinding.tvAfterContent.setTextColor(Color.parseColor("#666666"));
                        }

                        itemBinding.rlMorning.setVisibility(View.GONE);
                        itemBinding.rlLunch.setVisibility(View.VISIBLE);
                        itemBinding.rlDinner.setVisibility(View.GONE);
                    } else if (mSRouteDetailPlan.getPlanType() == 10){ // 晚餐
                        String data = ((SRouteDetailPlan) obj).getPlan().getDinner().getMeal();
                        if (TextUtils.isEmpty(data)){
                            itemBinding.tvEveningContent.setText("自理");
                        } else {
                            itemBinding.tvEveningContent.setText(data);
                        }

                        if (mSRouteDetailPlan.getPlan().getDinner().getCoordinateVO() != null) {
                            // 需要更改字体颜色 和 箭头需要
                            itemBinding.tvEveningContent.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.shared_navigation_trip_list_right_icon), null);
                            itemBinding.tvEveningContent.setTextColor(Color.parseColor("#24A4FF"));
                        } else {
                            itemBinding.tvEveningContent.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                            itemBinding.tvEveningContent.setTextColor(Color.parseColor("#666666"));
                        }

                        itemBinding.rlMorning.setVisibility(View.GONE);
                        itemBinding.rlLunch.setVisibility(View.GONE);
                        itemBinding.rlDinner.setVisibility(View.VISIBLE);
                    }
                }
            }else if(dbBinding instanceof ItemSharedNavigationTripDetail5Binding){ // spot 2
                ItemSharedNavigationTripDetail5Binding itemBinding= (ItemSharedNavigationTripDetail5Binding) this.dbBinding;
                Object obj=getItem(position);
                if(obj instanceof SRouteDetailPlan){
                    String address = ((SRouteDetailPlan) obj).getPlan().getSpot().getAddress();
                    itemBinding.tvTitle1.setText(TextUtils.isEmpty(address) ? "景点" : address);
                    itemBinding.tvScenicSpotName.setText(((SRouteDetailPlan) obj).getPlan().getSpot().getTitle());
                    if (((SRouteDetailPlan) obj).getPlan().getSpot().getImageArray()!= null) {
                        if (((SRouteDetailPlan) obj).getPlan().getSpot().getImageArray().size()>0){
                            itemBinding.banner.setVisibility(View.VISIBLE);
                            itemBinding.banner.setImages(((SRouteDetailPlan) obj).getPlan().getSpot().getImageArray());
                            itemBinding.banner.start();
                        } else {
                            itemBinding.banner.setVisibility(View.GONE);
                        }
                    } else {
                        itemBinding.banner.setVisibility(View.GONE);
                    }
                    itemBinding.expandTextView.setText(((SRouteDetailPlan) obj).getPlan().getSpot().getContent());
                }
            }
        }
    }
}