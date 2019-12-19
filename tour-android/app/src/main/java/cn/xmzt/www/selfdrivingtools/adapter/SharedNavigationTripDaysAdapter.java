package cn.xmzt.www.selfdrivingtools.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemSharedNavigationDaysTypeBinding;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import cn.xmzt.www.selfdrivingtools.activity.SharedNavigationMapActivity;
import cn.xmzt.www.selfdrivingtools.bean.SettingDestinationTypeInfo;

import java.util.List;

/**
 * 共享导航——主界面的天数
 */
public class SharedNavigationTripDaysAdapter extends BaseRecycleViewAdapter<SettingDestinationTypeInfo, SharedNavigationTripDaysAdapter.ItemHolder> {
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSharedNavigationDaysTypeBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_shared_navigation_days_type, parent, false);
        return new ItemHolder(itemRouteBinding);
    }

    private static Context mContext;

    public SharedNavigationTripDaysAdapter(Context context){
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
    public SettingDestinationTypeInfo getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }
        return null;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemSharedNavigationDaysTypeBinding itemBinding;

        public ItemHolder(ItemSharedNavigationDaysTypeBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((SharedNavigationMapActivity)mContext).OnBottomListClickListener(getAdapterPosition(),0);
                }
            });
        }
        public ItemSharedNavigationDaysTypeBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemSharedNavigationDaysTypeBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(final int position) {
            SettingDestinationTypeInfo item = datas.get(position);
            itemBinding.setItem(item);
            if (position == 0){
                itemBinding.tvLine.setVisibility(View.VISIBLE);
            } else {
                itemBinding.tvLine.setVisibility(View.GONE);
            }
//            if (item.isIs_Checked()){
//                itemBinding.tvContent.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//            } else {
//                itemBinding.tvContent.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
//            }
        }
    }
}