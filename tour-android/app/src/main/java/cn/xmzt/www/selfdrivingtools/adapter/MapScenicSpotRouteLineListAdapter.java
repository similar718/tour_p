package cn.xmzt.www.selfdrivingtools.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemMapRouteLineBinding;
import cn.xmzt.www.selfdrivingtools.activity.ScenicSpotMapActivity;
import cn.xmzt.www.selfdrivingtools.bean.ScenicSpotGuideBean;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;

import java.util.List;

/**
 * 智慧导览——带有地图的路线列表
 */
public class MapScenicSpotRouteLineListAdapter extends BaseRecycleViewAdapter<ScenicSpotGuideBean.ScenicLineListBean, MapScenicSpotRouteLineListAdapter.ItemHolder> {

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMapRouteLineBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_map_route_line, parent, false);
        return new ItemHolder(itemRouteBinding);
    }
    private static Context mContext;

    public MapScenicSpotRouteLineListAdapter(Context context){
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
    public ScenicSpotGuideBean.ScenicLineListBean getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }

        return null;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemMapRouteLineBinding itemBinding;

        public ItemHolder(ItemMapRouteLineBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ScenicSpotMapActivity)mContext).OnBottomClickListener(datas,getAdapterPosition(),1);
                }
            });
        }

        public ItemMapRouteLineBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemMapRouteLineBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(final int position) {
            ScenicSpotGuideBean.ScenicLineListBean item = datas.get(position);
            itemBinding.setItem(item);
            String data = "<font color='#24A4FF'>" + item.getSpotNum() + "</font>" + "个景点"
                    + "&nbsp;&nbsp;&nbsp;&nbsp;<font color='#e5e5e5'>|</font>&nbsp;&nbsp;&nbsp;&nbsp;" + "<font color='#24A4FF'>" +  item.getLineDistance() + "</font>" + "公里";
//                    + "&nbsp;&nbsp;&nbsp;&nbsp;<font color='#e5e5e5'>|</font>&nbsp;&nbsp;&nbsp;&nbsp;约" + "<font color='#24A4FF'>" + hour + "</font>" +"小时";
            itemBinding.tvContentLineDetail.setText(Html.fromHtml(data));
        }
    }

}