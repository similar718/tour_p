package cn.xmzt.www.mine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemCommonVehiclesEditBinding;
import cn.xmzt.www.mine.activity.CommonVehiclesActivity;
import cn.xmzt.www.mine.bean.CommonVehicleBean;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;

/**
 * 共享导航——设置目的地界面的类型
 */
public class CommonVehiclesEditAdapter extends BaseRecycleViewAdapter<CommonVehicleBean, CommonVehiclesEditAdapter.ItemHolder> {
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCommonVehiclesEditBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_common_vehicles_edit, parent, false);
        return new ItemHolder(itemRouteBinding);
    }

    private static Context mContext;

    public CommonVehiclesEditAdapter(Context context){
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
    public CommonVehicleBean getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }
        return null;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemCommonVehiclesEditBinding itemBinding;

        public ItemHolder(ItemCommonVehiclesEditBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((CommonVehiclesActivity)mContext).OnListClickListener(getAdapterPosition(),1);
                }
            });
            itemBinding.itemCarInfoEditIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((CommonVehiclesActivity)mContext).OnListClickListener(getAdapterPosition(),0);
                }
            });
            itemBinding.iconIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((CommonVehiclesActivity)mContext).OnListClickListener(getAdapterPosition(),1);
                }
            });
        }
        public ItemCommonVehiclesEditBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemCommonVehiclesEditBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(final int position) {
            CommonVehicleBean item = datas.get(position);
            if (item.getIsDefault()==1){
                itemBinding.itemCarInfoDefaultIv.setVisibility(View.VISIBLE);
            } else {
                itemBinding.itemCarInfoDefaultIv.setVisibility(View.GONE);
            }
            itemBinding.itemCarInfoTv.setText(item.getPlateNumber());
            if (item.isSelect()){
                itemBinding.iconIv.setImageResource(R.drawable.common_vehicle_choice_selected);
            } else {
                itemBinding.iconIv.setImageResource(R.drawable.common_vehicle_choice_select);
            }
        }
    }
}