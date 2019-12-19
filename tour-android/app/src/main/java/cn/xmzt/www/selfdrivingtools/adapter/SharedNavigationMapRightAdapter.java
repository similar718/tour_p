package cn.xmzt.www.selfdrivingtools.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemSharedNavigationMapPersonCarBinding;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import cn.xmzt.www.selfdrivingtools.activity.SharedNavigationMapActivity;
import cn.xmzt.www.selfdrivingtools.bean.SharedNavigationMapRightInfo;
import cn.xmzt.www.selfdrivingtools.overlay.AMapUtil;
import cn.xmzt.www.utils.TimeUtil;

import java.util.List;

/**
 * shared navigation——main page right list
 */
public class SharedNavigationMapRightAdapter extends BaseRecycleViewAdapter<SharedNavigationMapRightInfo, SharedNavigationMapRightAdapter.ItemHolder> {
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSharedNavigationMapPersonCarBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_shared_navigation_map_person_car, parent, false);
        return new ItemHolder(itemRouteBinding);
    }

    private static Context mContext;

    public SharedNavigationMapRightAdapter(Context context){
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

    class ItemHolder extends RecyclerView.ViewHolder {
        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemSharedNavigationMapPersonCarBinding itemBinding;

        public ItemHolder(ItemSharedNavigationMapPersonCarBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getLayoutPosition();
                    ((SharedNavigationMapActivity)mContext).OnTypeClickListener(position,datas.get(position).getBean().userId,0);
                }
            });
            itemBinding.ivHeadImgItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getLayoutPosition();
                    ((SharedNavigationMapActivity)mContext).OnTypeClickListener(position,datas.get(position).getBean().userId,0);
                }
            });
        }
        public ItemSharedNavigationMapPersonCarBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemSharedNavigationMapPersonCarBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(final int position) {
            SharedNavigationMapRightInfo item = datas.get(position);
            itemBinding.setItem(item);
            long time = System.currentTimeMillis(); // 当前时间
            long updateTime = item.getBean().time;
            if ((time-updateTime) > TIME_OUT){ // 更新的时间已经超过了三分钟
                if (item.getType() == 0) { // car share  头像更换
                    // 头像更换
                    itemBinding.ivHeadImgItem.setImageResource(R.drawable.shared_navigation_right_offline_car_icon);
                    // 昵称和更换昵称字体颜色
                    itemBinding.tvNickNameItem.setVisibility(View.GONE);// 人员的昵称的控件不进行显示
                    itemBinding.tvNickNameItemCar.setVisibility(View.VISIBLE);
                    itemBinding.tvNickNameItemCar.setText("司机：" + item.getBean().nickName);
                    itemBinding.tvNickNameItemCar.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimensionPixelSize(R.dimen.text_size_14));
                    itemBinding.tvNickNameItemCar.setTypeface(Typeface.DEFAULT_BOLD);
                    itemBinding.tvNickNameItemCar.setTextColor(Color.parseColor("#DADADA"));
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
                    itemBinding.ivDistanceItem.setVisibility(View.GONE);
                    itemBinding.ivDistanceItemCar.setVisibility(View.VISIBLE);
                    itemBinding.ivDistanceItemCar.setText(TimeUtil.updateTime(updateTime));
                    if (updateTime == 0){
                        itemBinding.ivDistanceItemCar.setVisibility(View.GONE);
                    } else {
                        itemBinding.ivDistanceItemCar.setVisibility(View.VISIBLE);
                    }
                } else {
                    // 头像更换
                    itemBinding.ivHeadImgItem.setImageResource(R.drawable.shared_navigation_right_offline_person_icon);
                    // 昵称和更换昵称字体颜色
                    itemBinding.tvNickNameItemCar.setVisibility(View.GONE);// 人员的昵称的控件不进行显示
                    itemBinding.tvNickNameItem.setVisibility(View.VISIBLE);
                    itemBinding.tvNickNameItem.setText(item.getBean().nickName);
                    itemBinding.tvNickNameItem.setTextColor(Color.parseColor("#DADADA"));
                    itemBinding.tvNickNameItem.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimensionPixelSize(R.dimen.text_size_16));
                    itemBinding.tvNickNameItem.setTypeface(Typeface.DEFAULT);
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
                    itemBinding.ivDistanceItemCar.setVisibility(View.GONE);
                    itemBinding.ivDistanceItem.setVisibility(View.VISIBLE);
                    itemBinding.ivDistanceItem.setText(TimeUtil.updateTime(updateTime));
                    if (updateTime == 0){
                        itemBinding.ivDistanceItem.setVisibility(View.GONE);
                    } else {
                        itemBinding.ivDistanceItem.setVisibility(View.VISIBLE);
                    }
                }
            } else { // 当前在线用户
                if (item.getType() == 0) { // car share
                    itemBinding.ivHeadImgItem.setImageResource(R.drawable.shared_navigation_person_list_head_img);
                    // 昵称和更换昵称字体颜色
                    itemBinding.tvNickNameItem.setVisibility(View.GONE);// 人员的昵称的控件不进行显示
                    itemBinding.tvNickNameItemCar.setVisibility(View.VISIBLE);
                    itemBinding.tvNickNameItemCar.setText("司机：" + item.getBean().nickName);
                    itemBinding.tvNickNameItemCar.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimensionPixelSize(R.dimen.text_size_14));
                    itemBinding.tvNickNameItemCar.setTypeface(Typeface.DEFAULT_BOLD);
                    itemBinding.tvNickNameItemCar.setTextColor(Color.parseColor("#333333"));

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
                    itemBinding.ivDistanceItem.setVisibility(View.GONE);
                    itemBinding.ivDistanceItemCar.setVisibility(View.VISIBLE);
                    itemBinding.ivDistanceItemCar.setText("距离" + AMapUtil.getFriendlyLength((int) item.getDistance()));
                    if (item.getBean().longitude == 0.0f && item.getBean().latitude == 0.0f){
                        itemBinding.ivDistanceItemCar.setVisibility(View.GONE);
                    } else {
                        itemBinding.ivDistanceItemCar.setVisibility(View.VISIBLE);
                    }
                } else if (item.getType() == 1) { // person share
                    // 头像更换
                    GlideUtil.setImageCircle(mContext, item.getBean().avatar, itemBinding.ivHeadImgItem, R.drawable.icon_head_default);
                    // 昵称和更换昵称字体颜色
                    itemBinding.tvNickNameItemCar.setVisibility(View.GONE);// 人员的昵称的控件不进行显示
                    itemBinding.tvNickNameItem.setVisibility(View.VISIBLE);
                    itemBinding.tvNickNameItem.setText(item.getBean().nickName);
                    itemBinding.tvNickNameItem.setTextColor(Color.parseColor("#333333"));
                    itemBinding.tvNickNameItem.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimensionPixelSize(R.dimen.text_size_16));
                    itemBinding.tvNickNameItem.setTypeface(Typeface.DEFAULT);
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
                    itemBinding.ivDistanceItemCar.setVisibility(View.GONE);
                    itemBinding.ivDistanceItem.setVisibility(View.VISIBLE);
                    itemBinding.ivDistanceItem.setText("距离" + AMapUtil.getFriendlyLength((int) item.getDistance()));
                    if (item.getBean().longitude == 0.0f && item.getBean().latitude == 0.0f){
                        itemBinding.ivDistanceItem.setVisibility(View.GONE);
                    } else {
                        itemBinding.ivDistanceItem.setVisibility(View.VISIBLE);
                    }
                }
                if (item.getDistance() > 10000) { // 10 km 字体改颜色 右边距离
                    itemBinding.ivDistanceItemCar.setTextColor(Color.parseColor("#F41A25"));
                    itemBinding.ivDistanceItem.setTextColor(Color.parseColor("#F41A25"));
                } else {
                    itemBinding.ivDistanceItemCar.setTextColor(Color.parseColor("#999999"));
                    itemBinding.ivDistanceItem.setTextColor(Color.parseColor("#999999"));
                }
            }
        }
    }
    private long TIME_OUT = 60 * 1000 * 5L;


    private String getTypeStr(int type){
        String info = "成员";
        if (type == 0){
            info = "群主";
        } else if (type == 1){
            info = "领队";
        } else {
            info = "成员";
        }
        return info;
    }
}