package cn.xmzt.www.selfdrivingtools.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import cn.xmzt.www.R;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ItemSettingDestinationSearchBinding;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import cn.xmzt.www.selfdrivingtools.activity.SharedNavigationSettingDestinationActivity;
import cn.xmzt.www.selfdrivingtools.bean.SettingDestinationBottomBean;
import cn.xmzt.www.selfdrivingtools.overlay.AMapUtil;
import cn.xmzt.www.utils.TextViewUtil;

import java.util.List;

/**
 * 共享导航——设置目的地界面的类型
 */
public class SettingDestinationListSearchAdapter extends BaseRecycleViewAdapter<SettingDestinationBottomBean, SettingDestinationListSearchAdapter.ItemHolder> {
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSettingDestinationSearchBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_setting_destination_search, parent, false);
        return new ItemHolder(itemRouteBinding);
    }

    private static Context mContext;

    public SettingDestinationListSearchAdapter(Context context){
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
        ItemSettingDestinationSearchBinding itemBinding;

        public ItemHolder(ItemSettingDestinationSearchBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((SharedNavigationSettingDestinationActivity)mContext).OnTypeClickListener(getAdapterPosition(),3);
                }
            });
        }
        public ItemSettingDestinationSearchBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemSettingDestinationSearchBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(final int position) {
            SettingDestinationBottomBean item = datas.get(position);
            itemBinding.setItem(item);
            // 需要显示蓝色的文字
            String content = item.getData();
            // 判断当前文字里面有没有含有这个字符
            if (item.getItem().getTitle().contains(content)){
                String[] data = item.getItem().getTitle().split(content);
                if (data.length>1)
                    TextViewUtil.setTextViewForeNumColor(item.getItem().getTitle(),data[0].length(),(data[0].length()+content.length()),itemBinding.tvAddressTxt);
            } else { // 不包含直接显示
                itemBinding.tvAddressTxt.setText(item.getItem().getTitle());
            }
            itemBinding.tvAddressInfo.setText(item.getItem().getSnippet());
            float distance = AMapUtils.calculateLineDistance(new LatLng(item.getItem().getLatLonPoint().getLatitude(),item.getItem().getLatLonPoint().getLongitude()),new LatLng(Constants.mLatitude,Constants.mLongitude));
            itemBinding.tvDistanceInfo.setText(AMapUtil.getFriendlyLength_y((int) distance));
        }
    }
}