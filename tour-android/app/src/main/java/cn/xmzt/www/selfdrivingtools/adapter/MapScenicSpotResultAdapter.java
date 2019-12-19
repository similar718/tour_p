package cn.xmzt.www.selfdrivingtools.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemMapScenicSpotBinding;
import cn.xmzt.www.selfdrivingtools.bean.MapListBean;
import cn.xmzt.www.selfdrivingtools.bean.ScenicSpotGuideBean;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;

import java.util.List;

/**
 *  智慧导览——带有地图的景点列表
 */
public class MapScenicSpotResultAdapter extends BaseRecycleViewAdapter<MapListBean, MapScenicSpotResultAdapter.ItemHolder> {

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMapScenicSpotBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_map_scenic_spot, parent, false);
        return new ItemHolder(itemRouteBinding);
    }
    private static Context mContext;

    public MapScenicSpotResultAdapter(Context context){
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
    public MapListBean getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }
        return null;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemMapScenicSpotBinding itemBinding;

        public ItemHolder(ItemMapScenicSpotBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemListener!=null){
                        int position = getAdapterPosition();
                        itemListener.onItemClick(view,position);
                    }
                }
            });
        }

        public ItemMapScenicSpotBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemMapScenicSpotBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(final int position) {
            if (datas.get(position).getType() == 0){ // 表示得到的数据是景点的列表
                // 界面上面的变化 显示顶部文字和离线 搜索
                itemBinding.itemMapScenicSpotTopRl.setVisibility(View.VISIBLE);
                itemBinding.itemMapScenicSpotSearchRl.setVisibility(View.VISIBLE);
                itemBinding.rvSpotList.setBackgroundResource(R.color.color_F2F3F4);
                itemBinding.rvSpotList.setPadding(0,0,20,0);

                ScenicSpotGuideBean item = datas.get(position).getBeans();
                itemBinding.setItem(item);
                itemBinding.rvSpotList.setLayoutManager(new GridLayoutManager(mContext,2));
                MapScenicSpotListDetailAdapter adapter = new MapScenicSpotListDetailAdapter(mContext);
                adapter.setDatas(item.getScenicSpotList());
                itemBinding.rvSpotList.setAdapter(adapter);
            } else {
                // 界面上面的变化 不显示顶部文字和离线 搜索
                itemBinding.itemMapScenicSpotTopRl.setVisibility(View.GONE);
                itemBinding.itemMapScenicSpotSearchRl.setVisibility(View.GONE);
                itemBinding.rvSpotList.setBackgroundResource(R.color.color_FF_FF_FF);
                itemBinding.rvSpotList.setPadding(0,0,0,0);
                if (datas.get(position).getType() == 1){ // 表示得到的数据是路线的列表
                    ScenicSpotGuideBean item = datas.get(position).getBeans();
                    itemBinding.setItem(item);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                    itemBinding.rvSpotList.setLayoutManager(layoutManager);
                    DividerItemDecoration decor = new DividerItemDecoration(mContext, layoutManager.getOrientation());
                    decor.setDrawable(ContextCompat.getDrawable(mContext,R.drawable.item_ticket_detail_recyleview_line));
                    itemBinding.rvSpotList.addItemDecoration(decor);
                    MapScenicSpotRouteLineListAdapter adapter = new MapScenicSpotRouteLineListAdapter(mContext);
                    adapter.setDatas(item.getScenicLineList());
                    itemBinding.rvSpotList.setAdapter(adapter);
                }
                if (datas.get(position).getType() == 2){ // 表示得到的数据是其他的列表
                    ScenicSpotGuideBean item = datas.get(position).getBeans();
                    itemBinding.setItem(item);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                    itemBinding.rvSpotList.setLayoutManager(layoutManager);
                    DividerItemDecoration decor = new DividerItemDecoration(mContext, layoutManager.getOrientation());
                    decor.setDrawable(ContextCompat.getDrawable(mContext,R.drawable.item_ticket_detail_recyleview_line));
                    itemBinding.rvSpotList.addItemDecoration(decor);
                    MapScenicSpotOtherListAdapter adapter = new MapScenicSpotOtherListAdapter(mContext);
                    adapter.setDatas(item.getScenicServicePointList().get(0).getPointList());
                    itemBinding.rvSpotList.setAdapter(adapter);
                }
            }
        }
    }

}