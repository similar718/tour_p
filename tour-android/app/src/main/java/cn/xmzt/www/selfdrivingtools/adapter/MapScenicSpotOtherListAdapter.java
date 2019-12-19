package cn.xmzt.www.selfdrivingtools.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemMapOtherBinding;
import cn.xmzt.www.selfdrivingtools.activity.ScenicSpotMapActivity;
import cn.xmzt.www.selfdrivingtools.bean.ScenicSpotGuideBean;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;

import java.util.List;

/**
 * 智慧导览——带有地图的其他类型的列表（eg: 停车场）
 */
public class MapScenicSpotOtherListAdapter extends BaseRecycleViewAdapter<ScenicSpotGuideBean.ScenicServicePointListBean.PointListBean, MapScenicSpotOtherListAdapter.ItemHolder> {

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMapOtherBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_map_other, parent, false);
        return new ItemHolder(itemRouteBinding);
    }

    private static Context mContext;

    public MapScenicSpotOtherListAdapter(Context context){
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
    public ScenicSpotGuideBean.ScenicServicePointListBean.PointListBean getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }
        return null;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemMapOtherBinding itemBinding;

        public ItemHolder(ItemMapOtherBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ScenicSpotMapActivity)mContext).OnBottomClickListener(datas,getAdapterPosition(),2);
                }
            });
        }

        public ItemMapOtherBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemMapOtherBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(final int position) {
            ScenicSpotGuideBean.ScenicServicePointListBean.PointListBean item = datas.get(position);
            itemBinding.setItem(item);
        }
    }

}