package cn.xmzt.www.smartteam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ItemSettingDestinationSearchResultBinding;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import cn.xmzt.www.selfdrivingtools.bean.SettingDestinationBottomBean;
import cn.xmzt.www.selfdrivingtools.overlay.AMapUtil;
import cn.xmzt.www.smartteam.activity.SmartTeamSettingDestinationAndTimeActivity;

/**
 * 共享导航——设置目的地界面的类型
 */
public class SettingDestinationListSearchResultAdapter extends BaseRecycleViewAdapter<SettingDestinationBottomBean, SettingDestinationListSearchResultAdapter.ItemHolder> {
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSettingDestinationSearchResultBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_setting_destination_search_result, parent, false);
        return new ItemHolder(itemRouteBinding);
    }

    private static Context mContext;

    public SettingDestinationListSearchResultAdapter(Context context){
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
    public SettingDestinationBottomBean getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }
        return null;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemSettingDestinationSearchResultBinding itemBinding;

        public ItemHolder(ItemSettingDestinationSearchResultBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((SmartTeamSettingDestinationAndTimeActivity)mContext).OnTypeClickListener(getAdapterPosition(),4);
                }
            });
        }
        public ItemSettingDestinationSearchResultBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemSettingDestinationSearchResultBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(final int position) {
            SettingDestinationBottomBean item = datas.get(position);
            itemBinding.setItem(item);
            itemBinding.tvAddressTxt.setText(item.getItem().getTitle());
            itemBinding.tvAddressInfo.setText(item.getItem().getSnippet());
            float distance = AMapUtils.calculateLineDistance(new LatLng(item.getItem().getLatLonPoint().getLatitude(),item.getItem().getLatLonPoint().getLongitude()),new LatLng(Constants.mLatitude,Constants.mLongitude));
            itemBinding.tvDistanceInfo.setText(AMapUtil.getFriendlyLength_y((int) distance));
        }
    }
}