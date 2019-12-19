package cn.xmzt.www.selfdrivingtools.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemMapScenicSpotListBinding;
import cn.xmzt.www.selfdrivingtools.activity.ScenicSpotMapActivity;
import cn.xmzt.www.selfdrivingtools.bean.ScenicSpotGuideBean;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;

import java.util.List;

/**
 *  智慧导览——带有地图的景点列表
 */
public class MapScenicSpotListDetailAdapter extends BaseRecycleViewAdapter<ScenicSpotGuideBean.ScenicSpotListBean, MapScenicSpotListDetailAdapter.ItemHolder> {

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMapScenicSpotListBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_map_scenic_spot_list, parent, false);
        return new ItemHolder(itemRouteBinding);
    }

    private static Context mContext;

    public MapScenicSpotListDetailAdapter(Context context){
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
    public ScenicSpotGuideBean.ScenicSpotListBean getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }
        return null;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemMapScenicSpotListBinding itemBinding;

        public ItemHolder(ItemMapScenicSpotListBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ScenicSpotMapActivity)mContext).OnBottomClickListener(datas,getAdapterPosition(),0);
                }
            });
            itemBinding.ivDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ScenicSpotMapActivity)mContext).OnBottomClickListener(datas,getAdapterPosition(),3);
                }
            });
            itemBinding.ivIntro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ScenicSpotMapActivity)mContext).OnBottomClickListener(datas,getAdapterPosition(),4);
                }
            });
        }

        public ItemMapScenicSpotListBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemMapScenicSpotListBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(final int position) {
            ScenicSpotGuideBean.ScenicSpotListBean item = datas.get(position);
            itemBinding.setItem(item);
            // 解说
//            itemBinding.ivIntro.setImageResource(item.getPlayType() == 0 ? R.drawable.scenic_spot_map_listen_icon : item.getPlayType() == 1 ? R.drawable.scenic_spot_map_listening_icon : R.drawable.scenic_spot_map_listen_pause_icon );
        }
    }

}