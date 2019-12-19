package cn.xmzt.www.selfdrivingtools.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemSharedNavigationTripDetailScenicSpotBinding;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import cn.xmzt.www.route.adapter.RouteDetailBannerAdapter1;
import cn.xmzt.www.selfdrivingtools.activity.SharedNavigationMapActivity;
import cn.xmzt.www.selfdrivingtools.bean.TourTripDetailBean;
import cn.xmzt.www.utils.ToastUtils;

import com.blankj.utilcode.util.ActivityUtils;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.List;

/**
 * shared navigation——setting destination interface type
 */
public class SharedNavigationTripListScenicSpotAdapter extends BaseRecycleViewAdapter<TourTripDetailBean.LineRouteListBean.SpotsListBean, SharedNavigationTripListScenicSpotAdapter.ItemHolder> {
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSharedNavigationTripDetailScenicSpotBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_shared_navigation_trip_detail_scenic_spot, parent, false);
        return new ItemHolder(itemRouteBinding);
    }

    private Context mContext;

    public SharedNavigationTripListScenicSpotAdapter(Context context){
        mContext = context;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.setViewDate(holder,position);
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
    public TourTripDetailBean.LineRouteListBean.SpotsListBean getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }
        return null;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemSharedNavigationTripDetailScenicSpotBinding itemBinding;

        public ItemHolder(ItemSharedNavigationTripDetailScenicSpotBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.tvScenicSpotName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (datas.get(getPosition()).getCoordinate() != null) {
                        double lat = datas.get(getPosition()).getCoordinate().getLatitude();
                        double lng = datas.get(getPosition()).getCoordinate().getLongitude();
                        int position = getPosition();
                        int type = 7;
                        ((SharedNavigationMapActivity) mContext).setBottomBlueTextClick(lat, lng, position, type);
                    } else {
                        ToastUtils.showText(ActivityUtils.getTopActivity(),"当前景点无位置信息");
                    }
                }
            });
            itemBinding.tvScenicSpotVoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 语音讲解
                    if (datas.get(getAdapterPosition()).getScenicId() <= 0){
                        ToastUtils.showText(mContext,"抱歉,暂无此景区的语音讲解内容");
                    } else {
                        ((SharedNavigationMapActivity) mContext).setSpotVoiceTextClick(datas.get(getAdapterPosition()).getScenicId());
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
        }
        public ItemSharedNavigationTripDetailScenicSpotBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemSharedNavigationTripDetailScenicSpotBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(ItemHolder holder, final int position) {
            TourTripDetailBean.LineRouteListBean.SpotsListBean item = datas.get(position);
            itemBinding.setItem(item);
            if (item.getImages()!= null) {
                if (item.getImages().size()>0){
                    itemBinding.banner.setVisibility(View.VISIBLE);
                    itemBinding.banner.setImages(item.getImages());
                    itemBinding.banner.start();
                } else {
                    itemBinding.banner.setVisibility(View.GONE);
                }
            } else {
                itemBinding.banner.setVisibility(View.GONE);
            }
            itemBinding.expandTextView.setText(item.getContent());
        }
    }
}