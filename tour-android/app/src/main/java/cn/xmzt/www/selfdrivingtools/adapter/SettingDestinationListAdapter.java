package cn.xmzt.www.selfdrivingtools.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemSettingDestinationNearLocationBinding;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import cn.xmzt.www.selfdrivingtools.activity.SharedNavigationSettingDestinationActivity;
import cn.xmzt.www.selfdrivingtools.bean.SettingDestinationBottomBean;

import java.util.List;

/**
 * 共享导航——设置目的地界面的类型
 */
public class SettingDestinationListAdapter extends BaseRecycleViewAdapter<SettingDestinationBottomBean, SettingDestinationListAdapter.ItemHolder> {
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSettingDestinationNearLocationBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_setting_destination_near_location, parent, false);
        return new ItemHolder(itemRouteBinding);
    }

    private static Context mContext;

    public SettingDestinationListAdapter(Context context){
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
        ItemSettingDestinationNearLocationBinding itemBinding;

        public ItemHolder(ItemSettingDestinationNearLocationBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((SharedNavigationSettingDestinationActivity)mContext).OnTypeClickListener(getAdapterPosition(),1);
                }
            });
        }
        public ItemSettingDestinationNearLocationBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemSettingDestinationNearLocationBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(final int position) {
            SettingDestinationBottomBean item = datas.get(position);
            itemBinding.setItem(item.getItem());
            if (position == 0){
                itemBinding.tvAddressTxt.setText("[位置]");
                itemBinding.tvAddressTxt.setTextColor(Color.parseColor("#49BEFF"));
                itemBinding.tvAddressInfo.setText(item.getItem().getTitle());
            } else {
                itemBinding.tvAddressTxt.setText(item.getItem().getTitle());
                itemBinding.tvAddressTxt.setTextColor(Color.parseColor("#333333"));
                itemBinding.tvAddressInfo.setText(item.getItem().getSnippet());
            }
        }
    }
}