package cn.xmzt.www.smartteam.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.databinding.ItemSmartTeamMapPersonCarBinding;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import cn.xmzt.www.selfdrivingtools.bean.SharedNavigationMapRightInfo;
import cn.xmzt.www.selfdrivingtools.overlay.AMapUtil;
import cn.xmzt.www.smartteam.activity.SmartTeamMapActivity;
import cn.xmzt.www.utils.TimeUtil;

public class SmartTeamCarAndPersonAdapter extends BaseRecycleViewAdapter<SharedNavigationMapRightInfo, SmartTeamCarAndPersonAdapter.ItemHolder> {
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSmartTeamMapPersonCarBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_smart_team_map_person_car, parent, false);
        return new ItemHolder(itemRouteBinding);
    }

    private static Context mContext;

    public SmartTeamCarAndPersonAdapter(Context context){
        mContext = context;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.setViewDate(position);
    }
    @Override
    public void setDatas(List datas) {
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
        return this.datas == null ? 0 : datas.size();
    }

    @Override
    public SharedNavigationMapRightInfo getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }
        return null;
    }

    private long TIME_OUT = 60 * 1000 * 5L;

    class ItemHolder extends RecyclerView.ViewHolder {
        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemSmartTeamMapPersonCarBinding itemBinding;

        public ItemHolder(ItemSmartTeamMapPersonCarBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((SmartTeamMapActivity)mContext).OnBottomListClickListener(datas.get(getLayoutPosition()).getBean().userId,getLayoutPosition(),0);
                }
            });
            itemBinding.ivHeadImgItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((SmartTeamMapActivity)mContext).OnBottomListClickListener(datas.get(getLayoutPosition()).getBean().userId,getLayoutPosition(),0);
                }
            });
            itemBinding.ivAddCar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((SmartTeamMapActivity)mContext).OnBottomListClickListener(datas.get(getLayoutPosition()).getBean().userId,getLayoutPosition(),3);
                }
            });
        }
        public ItemSmartTeamMapPersonCarBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemSmartTeamMapPersonCarBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(final int position) {
            SharedNavigationMapRightInfo item = datas.get(position);
            if (item.isCheck()){
                itemBinding.ivHeadImgItemBg.setVisibility(View.VISIBLE);
                if (item.getType() == 0){
                    itemBinding.ivHeadImgItemBg.setBackgroundResource(R.drawable.smart_team_bottom_checked_bg);
                } else {
                    itemBinding.ivHeadImgItemBg.setBackgroundResource(R.drawable.smart_team_bottom_checked_person_bg);
                }
            } else {
                itemBinding.ivHeadImgItemBg.setVisibility(View.INVISIBLE);
            }
            if (item.getBean() == null){
                return;
            }
            if (item.getBean().userId.equals(String.valueOf(TourApplication.getUserId()))){ // 我的信息
                itemBinding.ivHeadImgItem.setImageResource(R.drawable.smart_team_map_me_icon);
                itemBinding.tvNickNameItem.setText(item.getBean().nickName);
                itemBinding.tvNickNameItem.setTextColor(Color.parseColor("#333333"));
                if (item.getType() == 0) {
                    if (TextUtils.isEmpty(item.getBean().numberPlate)){
                        // 显示添加车辆的信息
                        itemBinding.tvTypeContentItem.setVisibility(View.GONE);
                        itemBinding.ivAddCar.setVisibility(View.VISIBLE);
                    } else {
                        itemBinding.ivAddCar.setVisibility(View.GONE);
                        itemBinding.tvTypeContentItem.setVisibility(View.VISIBLE);
                        itemBinding.tvTypeContentItem.setText(item.getBean().numberPlate);
                        itemBinding.tvTypeContentItem.setBackgroundResource(R.drawable.activity_shared_navigation_map_car_info_bg);
                        itemBinding.tvTypeContentItem.setTextColor(Color.parseColor("#333333"));
                    }
                } else {
                    itemBinding.ivAddCar.setVisibility(View.GONE);
                    itemBinding.tvTypeContentItem.setVisibility(View.GONE);
                }
                itemBinding.ivDistanceItemCar.setVisibility(View.GONE);
                if (item.getBean().type == 2) {
                    itemBinding.tvTypeItem.setVisibility(View.GONE);
                } else {
                    itemBinding.tvTypeItem.setText(getTypeStr(item.getBean().type));
                    itemBinding.tvTypeItem.setBackgroundResource(R.drawable.marker_shared_navigation_map_captain_leander_info_bg);
                    itemBinding.tvTypeItem.setVisibility(View.VISIBLE);
                }
            } else {
                itemBinding.ivAddCar.setVisibility(View.GONE);
                itemBinding.ivDistanceItemCar.setVisibility(View.VISIBLE);
                long time = System.currentTimeMillis(); // 当前时间
                long updateTime = item.getBean().time;
                if ((time - updateTime) > TIME_OUT) { // 更新的时间已经超过了三分钟
                    if (item.getType() == 0) { // car share  头像更换
                        // 头像更换
                        itemBinding.ivHeadImgItem.setImageResource(R.drawable.shared_navigation_right_offline_car_icon);
                        // 昵称和更换昵称字体颜色
                        itemBinding.tvNickNameItem.setText(item.getBean().nickName);
                        itemBinding.tvNickNameItem.setTextColor(Color.parseColor("#DADADA"));
                        // 车牌 和 车牌背景 车牌字体颜色
                        itemBinding.tvTypeContentItem.setVisibility(View.VISIBLE);
                        itemBinding.tvTypeContentItem.setText(item.getBean().numberPlate);
                        itemBinding.tvTypeContentItem.setBackgroundResource(R.drawable.activity_shared_navigation_map_car_info_gray_bg);
                        itemBinding.tvTypeContentItem.setTextColor(Color.parseColor("#ffffff"));
                        // 领队字符 更改背景
                        if (item.getBean().type == 2) {
                            itemBinding.tvTypeItem.setVisibility(View.GONE);
                        } else {
                            itemBinding.tvTypeItem.setText(getTypeStr(item.getBean().type));
                            itemBinding.tvTypeItem.setBackgroundResource(R.drawable.activity_shared_navigation_map_captain_info_gray_bg);
                            itemBinding.tvTypeItem.setVisibility(View.VISIBLE);
                        }
                        // 显示更新时间
                        itemBinding.ivDistanceItemCar.setVisibility(View.VISIBLE);
                        itemBinding.ivDistanceItemCar.setText(TimeUtil.updateTimeSmart(updateTime));
                    } else {
                        // 头像更换
                        itemBinding.ivHeadImgItem.setImageResource(R.drawable.shared_navigation_right_offline_person_icon);
                        // 昵称和更换昵称字体颜色
                        itemBinding.tvNickNameItem.setText(item.getBean().nickName);
                        itemBinding.tvNickNameItem.setTextColor(Color.parseColor("#DADADA"));
                        // 车辆
                        itemBinding.tvTypeContentItem.setVisibility(View.GONE);
                        // 领队字符 更改背景
                        if (item.getBean().type == 2) {
                            itemBinding.tvTypeItem.setVisibility(View.GONE);
                        } else {
                            itemBinding.tvTypeItem.setText(getTypeStr(item.getBean().type));
                            itemBinding.tvTypeItem.setBackgroundResource(R.drawable.activity_shared_navigation_map_captain_info_gray_bg);
                            itemBinding.tvTypeItem.setVisibility(View.VISIBLE);
                        }
                        // 显示更新时间
                        itemBinding.ivDistanceItemCar.setVisibility(View.VISIBLE);
                        itemBinding.ivDistanceItemCar.setText(TimeUtil.updateTimeSmart(updateTime));
                    }
                    if (updateTime == 0){
                        itemBinding.ivDistanceItemCar.setVisibility(View.GONE);
                    } else {
                        itemBinding.ivDistanceItemCar.setVisibility(View.VISIBLE);
                    }
                } else { // 当前在线用户
                    if (item.getType() == 0) { // car share
                        itemBinding.ivHeadImgItem.setImageResource(R.drawable.shared_navigation_person_list_head_img);
                        // 昵称和更换昵称字体颜色
                        itemBinding.tvNickNameItem.setVisibility(View.VISIBLE);
                        itemBinding.tvNickNameItem.setText(item.getBean().nickName);
                        itemBinding.tvNickNameItem.setTextColor(Color.parseColor("#333333"));

                        // 车牌 和 车牌背景 车牌字体颜色
                        itemBinding.tvTypeContentItem.setVisibility(View.VISIBLE);
                        itemBinding.tvTypeContentItem.setText(item.getBean().numberPlate);
                        itemBinding.tvTypeContentItem.setBackgroundResource(R.drawable.activity_shared_navigation_map_car_info_bg);
                        itemBinding.tvTypeContentItem.setTextColor(Color.parseColor("#333333"));
                        // 领队字符 更改背景
                        if (item.getBean().type == 2) {
                            itemBinding.tvTypeItem.setText(getTypeStr(item.getBean().type));
                            itemBinding.tvTypeItem.setBackgroundResource(R.drawable.marker_shared_navigation_map_captain_info_bg);
                            itemBinding.tvTypeItem.setVisibility(View.GONE);
                        } else {
                            itemBinding.tvTypeItem.setText(getTypeStr(item.getBean().type));
                            itemBinding.tvTypeItem.setBackgroundResource(R.drawable.marker_shared_navigation_map_captain_leander_info_bg);
                            itemBinding.tvTypeItem.setVisibility(View.VISIBLE);
                        }
                        // 显示距离
                        itemBinding.ivDistanceItemCar.setVisibility(View.VISIBLE);
                        itemBinding.ivDistanceItemCar.setText("距离" + AMapUtil.getFriendlyLength((int) item.getDistance()));
                    } else if (item.getType() == 1) { // person share
                        // 头像更换
                        GlideUtil.setImageCircle(mContext, item.getBean().avatar, itemBinding.ivHeadImgItem, R.drawable.icon_head_default);
                        // 昵称和更换昵称字体颜色
                        itemBinding.tvNickNameItem.setVisibility(View.VISIBLE);
                        itemBinding.tvNickNameItem.setText(item.getBean().nickName);
                        itemBinding.tvNickNameItem.setTextColor(Color.parseColor("#333333"));
                        // 车辆
                        itemBinding.tvTypeContentItem.setVisibility(View.GONE);
                        // 领队字符 更改背景
                        if (item.getBean().type == 2) {
                            itemBinding.tvTypeItem.setText(getTypeStr(item.getBean().type));
                            itemBinding.tvTypeItem.setBackgroundResource(R.drawable.marker_shared_navigation_map_captain_info_bg);
                            itemBinding.tvTypeItem.setVisibility(View.GONE);
                        } else {
                            itemBinding.tvTypeItem.setText(getTypeStr(item.getBean().type));
                            itemBinding.tvTypeItem.setBackgroundResource(R.drawable.marker_shared_navigation_map_captain_leander_info_bg);
                            itemBinding.tvTypeItem.setVisibility(View.VISIBLE);
                        }
                        // 显示更新时间
                        itemBinding.ivDistanceItemCar.setVisibility(View.VISIBLE);
                        itemBinding.ivDistanceItemCar.setText("距离" + AMapUtil.getFriendlyLength((int) item.getDistance()));
                    }
                    if (item.getBean().longitude == 0.0f && item.getBean().latitude == 0.0f){
                        itemBinding.ivDistanceItemCar.setVisibility(View.GONE);
                    } else {
                        itemBinding.ivDistanceItemCar.setVisibility(View.VISIBLE);
                    }
                    if (item.getDistance() > 10000) { // 10 km 字体改颜色 右边距离
                        itemBinding.ivDistanceItemCar.setTextColor(Color.parseColor("#F41A25"));
                    } else {
                        itemBinding.ivDistanceItemCar.setTextColor(Color.parseColor("#999999"));
                    }
                }
            }
        }
    }
    private String getTypeStr(int type){
        String info = "成员";
        if (type == 0){
            info = "领队";
        } else if (type == 1){
            info = "领队";
        } else {
            info = "成员";
        }
        return info;
    }
}