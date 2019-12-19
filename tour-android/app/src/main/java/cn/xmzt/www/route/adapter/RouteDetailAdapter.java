package cn.xmzt.www.route.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.xmzt.www.R;
import cn.xmzt.www.base.WebActivity;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ItemRouteDetail1Binding;
import cn.xmzt.www.databinding.ItemRouteDetail2Binding;
import cn.xmzt.www.databinding.ItemRouteDetail3Binding;
import cn.xmzt.www.databinding.ItemRouteDetail41Binding;
import cn.xmzt.www.databinding.ItemRouteDetail42Binding;
import cn.xmzt.www.databinding.ItemRouteDetail43Binding;
import cn.xmzt.www.databinding.ItemRouteDetail44Binding;
import cn.xmzt.www.databinding.ItemRouteDetail5Binding;
import cn.xmzt.www.databinding.ItemRouteDetail6Binding;
import cn.xmzt.www.databinding.ItemRouteDetail7Binding;
import cn.xmzt.www.databinding.ItemRouteDetailFloorBinding;
import cn.xmzt.www.route.bean.RouteDetailCategory;
import cn.xmzt.www.route.bean.RouteDetailPage;


public class RouteDetailAdapter extends BaseRecycleViewAdapter<Object, RouteDetailAdapter.ItemHolder>{
    private final int TYPE_ITEM_1 = 1;//分类标题
    private final int TYPE_ITEM_2 = 2;//达人推荐
    private final int TYPE_ITEM_3 = 3;//热门景点
    private final int TYPE_ITEM_4_1 = 41;//行程起点
    private final int TYPE_ITEM_4_2 = 42;//行程安排
    private final int TYPE_ITEM_4_3 = 43;//行程餐点
    private final int TYPE_ITEM_4_4 = 44;//行程景点
    private final int TYPE_ITEM_5 = 5;//费用
    private final int TYPE_ITEM_6 = 6;//须知
    private final int TYPE_ITEM_7 = 7;//更多推荐
    private final int TYPE_ITEM_8 = 8;//分类floor
    Map<String,String> map=new HashMap();//须知
    public int recommendsSize=0;
    public int startindex_drtj=0;
    public int startindex_xc=0;
    public int startindex_fy=0;
    public int startindex_xz=0;

    public void setData(RouteDetailPage mRouteDetailPage) {
        this.datas.clear();
        List<RouteDetailPage.LightspotListBean> lightspotList=mRouteDetailPage.getLightspotList();//亮点列表
        if(lightspotList!=null&&lightspotList.size()>0){
            this.datas.add(new RouteDetailCategory("达人推荐",R.drawable.icon_route_drtj));
            this.datas.addAll(lightspotList);
            this.datas.add("达人推荐floor");
        }
        List<RouteDetailPage.HotSpotListBean> hotSpotList=mRouteDetailPage.getHotSpotList();//热门景点列表
        if(hotSpotList!=null&&hotSpotList.size()>0){
            this.datas.add(new RouteDetailCategory("热门景点",R.drawable.icon_route_rmjd));
            this.datas.addAll(hotSpotList);
            this.datas.add("热门景点floor");
        }

        List<RouteDetailPage.DayLineTrip> lineTripList=mRouteDetailPage.getLineRouteListVO();//行程列表
        startindex_xc=this.datas.size();
        if(lineTripList!=null&&lineTripList.size()>0){
            this.datas.add(new RouteDetailCategory("行程安排",R.drawable.icon_route_xingcheng_anpai));
        }
        for(int i=0;i<lineTripList.size();i++){
            RouteDetailPage.DayLineTrip dayLineTrip=lineTripList.get(i);
            this.datas.add(dayLineTrip);
            this.datas.addAll(dayLineTrip.getDetailVOList());

            /*planList.add(new RouteDetailPlan(mLineRouteListBean,1,R.drawable.icon_route_xc_1,"起点"));
            planList.add(new RouteDetailPlan(mLineRouteListBean,2,R.drawable.icon_route_xc_2,"交通"));
            planList.add(new RouteDetailPlan(mLineRouteListBean,3,R.drawable.icon_route_xc_3,"行程"));
            planList.add(new RouteDetailPlan(mLineRouteListBean,4,R.drawable.icon_route_xc_4,"餐点"));
            planList.add(new RouteDetailPlan(mLineRouteListBean,5,R.drawable.icon_route_xc_5,"住宿"));
            if(mLineRouteListBean.isSpotsList()){
                planList.add(new RouteDetailPlan(mLineRouteListBean,6,R.drawable.icon_route_xc_6,"景点"));
            }*/
        }
        if(lineTripList!=null&&lineTripList.size()>0){
            this.datas.add("行程安排floor");
        }
        startindex_fy=this.datas.size();
        RouteDetailPage.CostBean cost=mRouteDetailPage.getCost();//费用
        if(cost!=null){
            this.datas.add(new RouteDetailCategory("费用",R.drawable.icon_route_feiyong));
            this.datas.add(cost);
        }
        String driveInformation=Constants.getXzUrl(4);//自驾须知
        String backRule=Constants.getXzUrl(5);//退改规则
        String safetyInstruction=Constants.getXzUrl(6);//安全须知
        map.clear();
        if(!TextUtils.isEmpty(driveInformation)){
            map.put("A",driveInformation);
        }
        if(!TextUtils.isEmpty(backRule)){
            map.put("B",backRule);
        }
        if(!TextUtils.isEmpty(safetyInstruction)){
            map.put("C",safetyInstruction);
        }
        startindex_xz=this.datas.size();
        if(map.size()>0){
            this.datas.add(new RouteDetailCategory("须知",R.drawable.icon_route_xz));
            this.datas.add(map);
            this.datas.add("须知floor");
        }
        List<RouteDetailPage.RecommendsBean> recommends=mRouteDetailPage.getRecommends();//推荐列表
        recommendsSize=0;
        if(recommends!=null&&recommends.size()>0){
            recommendsSize=recommends.size();
            this.datas.add(new RouteDetailCategory("更多推荐",0));
            this.datas.addAll(recommends);
        }
        this.datas.add("已经到底了哟~");
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
        if(obj instanceof RouteDetailCategory){
            return TYPE_ITEM_1;
        }else if(obj instanceof RouteDetailPage.LightspotListBean){
            return TYPE_ITEM_2;
        }else if(obj instanceof RouteDetailPage.HotSpotListBean){
            return TYPE_ITEM_3;
        }else if(obj instanceof RouteDetailPage.DayLineTrip){
            return TYPE_ITEM_4_1;
        }else if(obj instanceof RouteDetailPage.DayLineTripInfo){
            RouteDetailPage.DayLineTripInfo mDayLineTripInfo= (RouteDetailPage.DayLineTripInfo) obj;
            /**
             * 数据类型：1行程信息 2景点信息 3交通信息 4住宿信息 5集结地信息
             * 6开始地信息 7目的地信息 8早餐信息 9午餐信息 10晚餐信息
             */

              /*planList.add(new RouteDetailPlan(mLineRouteListBean,1,R.drawable.icon_route_xc_1,"起点"));
            planList.add(new RouteDetailPlan(mLineRouteListBean,2,R.drawable.icon_route_xc_2,"交通"));
            planList.add(new RouteDetailPlan(mLineRouteListBean,3,R.drawable.icon_route_xc_3,"行程"));
            planList.add(new RouteDetailPlan(mLineRouteListBean,4,R.drawable.icon_route_xc_4,"餐点"));
            planList.add(new RouteDetailPlan(mLineRouteListBean,5,R.drawable.icon_route_xc_5,"住宿"));
            if(mLineRouteListBean.isSpotsList()){
                planList.add(new RouteDetailPlan(mLineRouteListBean,6,R.drawable.icon_route_xc_6,"景点"));
            }*/
            if(mDayLineTripInfo.getDataType()==8||mDayLineTripInfo.getDataType()==9||mDayLineTripInfo.getDataType()==10){
                //8早餐信息 9午餐信息 10晚餐信息
                return TYPE_ITEM_4_3;
            }else if(mDayLineTripInfo.getDataType()==2){
                //2景点信息
                return TYPE_ITEM_4_4;
            }else {
                //1行程信息 3交通信息 4住宿信息 5集结地信息 6开始地信息 7目的地信息
                return TYPE_ITEM_4_2;
            }
        }else if(obj instanceof RouteDetailPage.CostBean){
            return TYPE_ITEM_5;
        }else if(obj instanceof Map){
            return TYPE_ITEM_6;
        }else if(obj instanceof RouteDetailPage.RecommendsBean){
            return TYPE_ITEM_7;
        }else if(obj instanceof String){
            return TYPE_ITEM_8;
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
        if(viewType==TYPE_ITEM_1){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_detail_1, parent, false));
        }else  if(viewType==TYPE_ITEM_2){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_detail_2, parent, false));
        }else  if(viewType==TYPE_ITEM_3){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_detail_3, parent, false));
        }else  if(viewType==TYPE_ITEM_4_1){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_detail_4_1, parent, false));
        }else  if(viewType==TYPE_ITEM_4_2){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_detail_4_2, parent, false));
        }else  if(viewType==TYPE_ITEM_4_3){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_detail_4_3, parent, false));
        }else  if(viewType==TYPE_ITEM_4_4){
            ItemRouteDetail44Binding dbBinding=DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_detail_4_4, parent, false);
            holder=new ItemHolder(dbBinding);
        }else  if(viewType==TYPE_ITEM_5){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_detail_5, parent, false));
        }else  if(viewType==TYPE_ITEM_6){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_detail_6, parent, false));
        }else  if(viewType==TYPE_ITEM_7){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_detail_7, parent, false));
        }else  if(viewType==TYPE_ITEM_8){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_detail_floor, parent, false));
        }
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
            dp_16=dbBinding.getRoot().getResources().getDimensionPixelOffset(R.dimen.dp_16);
            this.dbBinding = dbBinding;
            if(dbBinding instanceof ItemRouteDetail3Binding){
                ItemRouteDetail3Binding itemBinding= (ItemRouteDetail3Binding) dbBinding;
                itemBinding.banner.isAutoPlay(false);
                itemBinding.banner.setIndicatorGravity(BannerConfig.RIGHT);
                itemBinding.banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
                itemBinding.banner.setImageLoader(new RouteDetailBannerAdapter1(8));
                itemBinding.banner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {

                    }
                });
            }else if(dbBinding instanceof ItemRouteDetail44Binding){
                ItemRouteDetail44Binding itemBinding= (ItemRouteDetail44Binding) dbBinding;
                itemBinding.banner.isAutoPlay(false);
                itemBinding.banner.setIndicatorGravity(BannerConfig.RIGHT);
                itemBinding.banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
                itemBinding.banner.setImageLoader(new RouteDetailBannerAdapter1(8));
                itemBinding.banner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {

                    }
                });
            }else if(dbBinding instanceof ItemRouteDetail6Binding){
                ItemRouteDetail6Binding itemBinding= (ItemRouteDetail6Binding) this.dbBinding;
                itemBinding.xzTV1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //服务保障
                        Intent intent = new Intent(v.getContext(), WebActivity.class);
                        intent.putExtra("title", "自驾注意事项说明");
                        intent.putExtra("url", Constants.getXzUrl(4));
                        v.getContext().startActivity(intent);
                    }
                });
                itemBinding.xzTV2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //服务保障
                        Intent intent = new Intent(v.getContext(), WebActivity.class);
                        intent.putExtra("title", "线路退改规则说明");
                        intent.putExtra("url", Constants.getXzUrl(5));
                        v.getContext().startActivity(intent);
                    }
                });
                itemBinding.xzTV3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //服务保障
                        Intent intent = new Intent(v.getContext(), WebActivity.class);
                        intent.putExtra("title", "安全须知");
                        intent.putExtra("url", Constants.getXzUrl(6));
                        v.getContext().startActivity(intent);
                    }
                });
            }else if(dbBinding instanceof ItemRouteDetail7Binding){
                ItemRouteDetail7Binding itemBinding= (ItemRouteDetail7Binding) this.dbBinding;
                itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //服务保障
                        if(itemListener!=null){
                            int position=getAdapterPosition();
                            itemListener.onItemClick(v,position);
                        }
                    }
                });
            }
        }
        private void setViewDate(int position){
            if(dbBinding instanceof ItemRouteDetail1Binding){
                ItemRouteDetail1Binding itemBinding= (ItemRouteDetail1Binding) this.dbBinding;
                Object obj=getItem(position);
                if(obj instanceof RouteDetailCategory){
                    RouteDetailCategory category= (RouteDetailCategory) obj;
                    itemBinding.setItem(category);
                    itemBinding.tvName.setCompoundDrawablesWithIntrinsicBounds(category.getIcon(),0,0,0);
                }
            }else if(dbBinding instanceof ItemRouteDetail2Binding){
                ItemRouteDetail2Binding itemBinding= (ItemRouteDetail2Binding) this.dbBinding;
                Object obj=getItem(position);
                if(obj instanceof RouteDetailPage.LightspotListBean){
                    itemBinding.setItem((RouteDetailPage.LightspotListBean) obj);
                }
            }else if(dbBinding instanceof ItemRouteDetail3Binding){
                ItemRouteDetail3Binding itemBinding= (ItemRouteDetail3Binding) this.dbBinding;
                Object obj=getItem(position);
                if(obj instanceof RouteDetailPage.HotSpotListBean){
                    RouteDetailPage.HotSpotListBean mHotSpotListBean=(RouteDetailPage.HotSpotListBean) obj;
                    itemBinding.setItem(mHotSpotListBean);
                    List<String> images =mHotSpotListBean.getImages();
                    itemBinding.banner.setVisibility(View.VISIBLE);
                    itemBinding.banner.setImages(images);
                    itemBinding.banner.start();
                    if(images.size()==0){
                        itemBinding.banner.setVisibility(View.GONE);
                    }
                }
            }else if(dbBinding instanceof ItemRouteDetail41Binding){
                ItemRouteDetail41Binding itemBinding= (ItemRouteDetail41Binding) this.dbBinding;
                Object obj=getItem(position);
                if(obj instanceof RouteDetailPage.DayLineTrip){
                    itemBinding.setItem((RouteDetailPage.DayLineTrip) obj);
                }
            }else if(dbBinding instanceof ItemRouteDetail42Binding){
                ItemRouteDetail42Binding itemBinding= (ItemRouteDetail42Binding) this.dbBinding;
                Object obj=getItem(position);
                Object nextObj=getItem(position+1);
                if(nextObj!=null && nextObj instanceof RouteDetailPage.DayLineTripInfo){
                    itemBinding.tvContent1.setBackgroundResource(R.drawable.bg_time_line);
                }else {
                    itemBinding.tvContent1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                if(obj instanceof RouteDetailPage.DayLineTripInfo){
                    RouteDetailPage.DayLineTripInfo mDayLineTripInfo=(RouteDetailPage.DayLineTripInfo) obj;
                    //1行程信息 3交通信息 4住宿信息 5集结地信息 6开始地信息 7目的地信息
                    if(mDayLineTripInfo.getDataType()==1){
                        itemBinding.dotImg1.setImageResource(R.drawable.icon_route_xc_3);
                        itemBinding.tvTitle1.setText("行程");
                        RouteDetailPage.LineTripContent mLineTripContent=mDayLineTripInfo.getContent();
                        if(mLineTripContent!=null){
                            itemBinding.tvContent1.setText(mLineTripContent.getContent());
                        }
                    }else if(mDayLineTripInfo.getDataType()==3){
                        itemBinding.dotImg1.setImageResource(R.drawable.icon_route_xc_2);
                        itemBinding.tvTitle1.setText("交通");
                        RouteDetailPage.LineTripCar mLineTripCar=mDayLineTripInfo.getCar();
                        if(mLineTripCar!=null){
                            itemBinding.tvContent1.setText(mLineTripCar.getCar());
                        }
                    }else if(mDayLineTripInfo.getDataType()==4){
                        itemBinding.dotImg1.setImageResource(R.drawable.icon_route_xc_5);
                        itemBinding.tvTitle1.setText("住宿");
                        RouteDetailPage.LineTripHotel mLineTripHotel=mDayLineTripInfo.getHotel();
                        if(mLineTripHotel!=null){
                            itemBinding.tvContent1.setText(mLineTripHotel.getAccommodation());
                        }
                    }else if(mDayLineTripInfo.getDataType()==5){
                        itemBinding.dotImg1.setImageResource(R.drawable.shared_navigation_trip_list_jihe_icon);
                        itemBinding.tvTitle1.setText("集结点");
                        RouteDetailPage.LineTripAddress mLineTripAddress=mDayLineTripInfo.getStaging();
                        if(mLineTripAddress!=null){
                            String address=mLineTripAddress.getAddress();
                            if(!TextUtils.isEmpty(address)){
                                itemBinding.tvContent1.setText(address);
                            }else {
                                RouteDetailPage.AreaBean mAreaBean=mLineTripAddress.getAreaVO();
                                if(mAreaBean!=null){
                                    itemBinding.tvContent1.setText(mAreaBean.getAreaName());
                                }
                            }
                        }
                    }else if(mDayLineTripInfo.getDataType()==6){
                        itemBinding.dotImg1.setImageResource(R.drawable.scheduling_cfd_icon);
                        itemBinding.tvTitle1.setText("出发地");
                        RouteDetailPage.LineTripAddress mLineTripAddress=mDayLineTripInfo.getStart();
                        if(mLineTripAddress!=null){
                            String address=mLineTripAddress.getAddress();
                            if(!TextUtils.isEmpty(address)){
                                itemBinding.tvContent1.setText(address);
                            }else {
                                RouteDetailPage.AreaBean mAreaBean=mLineTripAddress.getAreaVO();
                                if(mAreaBean!=null){
                                    itemBinding.tvContent1.setText(mAreaBean.getAreaName());
                                }
                            }
                        }
                    }else if(mDayLineTripInfo.getDataType()==7){
                        itemBinding.dotImg1.setImageResource(R.drawable.scheduling_mdd_icon);
                        itemBinding.tvTitle1.setText("目的地");
                        RouteDetailPage.LineTripAddress mLineTripAddress=mDayLineTripInfo.getEnd();
                        if(mLineTripAddress!=null){
                            String address=mLineTripAddress.getAddress();
                            if(!TextUtils.isEmpty(address)){
                                itemBinding.tvContent1.setText(address);
                            }else {
                                RouteDetailPage.AreaBean mAreaBean=mLineTripAddress.getAreaVO();
                                if(mAreaBean!=null){
                                    itemBinding.tvContent1.setText(mAreaBean.getAreaName());
                                }
                            }
                        }
                    }
                }
            }else if(dbBinding instanceof ItemRouteDetail43Binding){
                ItemRouteDetail43Binding itemBinding= (ItemRouteDetail43Binding) this.dbBinding;
                Object obj=getItem(position);
                Object nextObj=getItem(position+1);
                if(nextObj!=null && nextObj instanceof RouteDetailPage.DayLineTripInfo){
                    itemBinding.contentLayout.setBackgroundResource(R.drawable.bg_time_line);
                }else {
                    itemBinding.contentLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                if(obj instanceof RouteDetailPage.DayLineTripInfo){
                    RouteDetailPage.DayLineTripInfo mDayLineTripInfo=(RouteDetailPage.DayLineTripInfo) obj;
                    //8早餐信息 9午餐信息 10晚餐信息
                    itemBinding.tvTitle1.setText("餐饮");
                    itemBinding.tvLabel.setVisibility(View.GONE);
                    itemBinding.tvLabe2.setVisibility(View.GONE);
                    itemBinding.tvLabe3.setVisibility(View.GONE);
                    RouteDetailPage.LineTripMeal lineTripMeal=null;
                    if(mDayLineTripInfo.getDataType()==8){
                        itemBinding.tvLabel.setVisibility(View.VISIBLE);
                        lineTripMeal=mDayLineTripInfo.getBreakfast();
                        if(lineTripMeal!=null){
                            itemBinding.tvLabel.setOriginalText(lineTripMeal.getMeal());
                        }
                    }else if(mDayLineTripInfo.getDataType()==9){
                        itemBinding.tvLabe2.setVisibility(View.VISIBLE);
                        lineTripMeal=mDayLineTripInfo.getLunch();
                        if(lineTripMeal!=null){
                            itemBinding.tvLabe2.setOriginalText(lineTripMeal.getMeal());
                        }
                    }else if(mDayLineTripInfo.getDataType()==10){
                        itemBinding.tvLabe3.setVisibility(View.VISIBLE);
                        lineTripMeal=mDayLineTripInfo.getDinner();
                        if(lineTripMeal!=null){
                            itemBinding.tvLabe3.setOriginalText(lineTripMeal.getMeal());
                        }
                    }
                }
            }else if(dbBinding instanceof ItemRouteDetail44Binding){
                ItemRouteDetail44Binding itemBinding= (ItemRouteDetail44Binding) this.dbBinding;
                Object obj=getItem(position);
                Object nextObj=getItem(position+1);
                if(nextObj!=null && nextObj instanceof RouteDetailPage.DayLineTripInfo){
                    itemBinding.contentLayout.setBackgroundResource(R.drawable.bg_time_line);
                }else {
                    itemBinding.contentLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                if(obj instanceof RouteDetailPage.DayLineTripInfo){
                    RouteDetailPage.DayLineTripInfo mDayLineTripInfo=(RouteDetailPage.DayLineTripInfo) obj;
                    if(mDayLineTripInfo.getDataType()==2){
                        //2景点信息
                        RouteDetailPage.LineTripSpot lineTripSpot=mDayLineTripInfo.getSpot();
                        List<String> images =lineTripSpot.getImageArray();
                        itemBinding.banner.setVisibility(View.VISIBLE);
                        if(images!=null&&images.size()>0){
                            itemBinding.banner.setImages(images);
                            itemBinding.banner.start();
                        }else {
                            itemBinding.banner.setVisibility(View.GONE);
                        }
                    }
                }
            }else if(dbBinding instanceof ItemRouteDetail5Binding){
                ItemRouteDetail5Binding itemBinding= (ItemRouteDetail5Binding) this.dbBinding;
                Object obj=getItem(position);
                if(obj instanceof RouteDetailPage.CostBean){
                    itemBinding.setItem((RouteDetailPage.CostBean) obj);
                }
            }else if(dbBinding instanceof ItemRouteDetail6Binding){
                ItemRouteDetail6Binding itemBinding= (ItemRouteDetail6Binding) this.dbBinding;
                Object obj=getItem(position);

            }else if(dbBinding instanceof ItemRouteDetail7Binding){
                ItemRouteDetail7Binding itemBinding= (ItemRouteDetail7Binding) this.dbBinding;
                Object obj=getItem(position);
                if(obj instanceof RouteDetailPage.RecommendsBean){
                    itemBinding.setItem((RouteDetailPage.RecommendsBean) obj);
                }
            }else if(dbBinding instanceof ItemRouteDetailFloorBinding){
                ItemRouteDetailFloorBinding itemBinding= (ItemRouteDetailFloorBinding) this.dbBinding;
                Object obj=getItem(position);

                if(position==getItemCount()-1){
                    itemBinding.floorView.setVisibility(View.VISIBLE);
                    itemBinding.spaceView.setVisibility(View.GONE);
                    itemBinding.spaceWhiteView.setVisibility(View.GONE);
                }else {
                    itemBinding.floorView.setVisibility(View.GONE);
                    itemBinding.spaceView.setVisibility(View.VISIBLE);
                    if("达人推荐floor".equals(obj)){
                        itemBinding.spaceWhiteView.setVisibility(View.VISIBLE);
                    }else if("热门景点floor".equals(obj)){
                        itemBinding.spaceWhiteView.setVisibility(View.GONE);
                    }else if("行程安排floor".equals(obj)){
                        itemBinding.spaceWhiteView.setVisibility(View.GONE);
                    }else if("须知floor".equals(obj)){
                        itemBinding.spaceWhiteView.setVisibility(View.GONE);
                    }else {
                        itemBinding.spaceWhiteView.setVisibility(View.GONE);
                    }
                }
            }
        }
    }
}