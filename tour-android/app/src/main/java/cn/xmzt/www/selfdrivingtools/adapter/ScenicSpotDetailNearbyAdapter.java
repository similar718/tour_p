package cn.xmzt.www.selfdrivingtools.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemScenicSpotDetailNearbyBinding;
import cn.xmzt.www.selfdrivingtools.activity.ScenicSpotDetailActivity;
import cn.xmzt.www.selfdrivingtools.bean.ScenicSpotDetailBean;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;

import java.util.List;

/**
 *  智慧导览——带有地图的景点列表
 */
public class ScenicSpotDetailNearbyAdapter extends BaseRecycleViewAdapter<ScenicSpotDetailBean.ScenicSpotListBean, ScenicSpotDetailNearbyAdapter.ItemHolder> {

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemScenicSpotDetailNearbyBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_scenic_spot_detail_nearby, parent, false);
        return new ItemHolder(itemRouteBinding);
    }

    private static Context mContext;

    public ScenicSpotDetailNearbyAdapter(Context context){
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
    public ScenicSpotDetailBean.ScenicSpotListBean getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }
        return null;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemScenicSpotDetailNearbyBinding itemBinding;

        public ItemHolder(ItemScenicSpotDetailNearbyBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ScenicSpotDetailActivity)mContext).OnNearByClickListener(getAdapterPosition());
                }
            });
        }

        public ItemScenicSpotDetailNearbyBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemScenicSpotDetailNearbyBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(final int position) {
            ScenicSpotDetailBean.ScenicSpotListBean item = datas.get(position);
            itemBinding.setItem(item);
          }
    }

}