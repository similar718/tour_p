package cn.xmzt.www.selfdrivingtools.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemSettingDestinationTypeBinding;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import cn.xmzt.www.selfdrivingtools.activity.SharedNavigationSettingDestinationActivity;
import cn.xmzt.www.selfdrivingtools.bean.SettingDestinationTypeInfo;

import java.util.List;

/**
 * 共享导航——设置目的地界面的类型
 */
public class SettingDestinationTypeAdapter extends BaseRecycleViewAdapter<SettingDestinationTypeInfo, SettingDestinationTypeAdapter.ItemHolder> {
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSettingDestinationTypeBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_setting_destination_type, parent, false);
        return new ItemHolder(itemRouteBinding);
    }

    private static Context mContext;

    public SettingDestinationTypeAdapter(Context context){
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
        ItemSettingDestinationTypeBinding itemBinding;

        public ItemHolder(ItemSettingDestinationTypeBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((SharedNavigationSettingDestinationActivity)mContext).OnTypeClickListener(getAdapterPosition(),0);
                }
            });
        }
        public ItemSettingDestinationTypeBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemSettingDestinationTypeBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(final int position) {
            SettingDestinationTypeInfo item = datas.get(position);
            itemBinding.setItem(item);
            if (item.isIs_Checked()){
                itemBinding.tvContent.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else {
                itemBinding.tvContent.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        }
    }
}