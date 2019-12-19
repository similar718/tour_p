package cn.xmzt.www.selfdrivingtools.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemScenicSpotSearchResultLineBinding;
import cn.xmzt.www.selfdrivingtools.activity.ScenicSpotSearchActivity;
import cn.xmzt.www.selfdrivingtools.bean.WisdomGuideInfo;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import cn.xmzt.www.utils.TextViewUtil;

import java.util.List;

/**
 * 智慧导览——搜索一边输入一边获取数据
 */
public class ScenicSpotSearchResultLineListAdapter extends BaseRecycleViewAdapter<WisdomGuideInfo.ItemsBean, ScenicSpotSearchResultLineListAdapter.ItemHolder> {

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemScenicSpotSearchResultLineBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_scenic_spot_search_result_line, parent, false);
        return new ItemHolder(itemRouteBinding);
    }
    private static Context mContext;

    public ScenicSpotSearchResultLineListAdapter(Context context){
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
    public WisdomGuideInfo.ItemsBean getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }
        return null;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemScenicSpotSearchResultLineBinding itemBinding;
        long textSize = 0;
        public ItemHolder(ItemScenicSpotSearchResultLineBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ScenicSpotSearchActivity)mContext).OnClickListener(datas.get(getAdapterPosition()),getAdapterPosition());
                }
            });
        }
        public ItemScenicSpotSearchResultLineBinding getBinding() {
            return itemBinding;
        }
        public void setBinding(ItemScenicSpotSearchResultLineBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(final int position) {
            WisdomGuideInfo.ItemsBean item = datas.get(position);
            itemBinding.setItem(item);
            // 文字显示问题
            // 当position为0时  显示的文字为
            if (position == 0){ // 搜索结果情况
                itemBinding.itemScenicSpotSearchResultLineNumTv.setVisibility(View.VISIBLE);
                itemBinding.itemScenicSpotSearchResultLineIv.setVisibility(View.VISIBLE);
                itemBinding.itemScenicSpotSearchResultLineIv1.setVisibility(View.INVISIBLE);
                // 获取前几个字段为搜索字段
                textSize = item.getId();
                itemBinding.itemScenicSpotSearchResultLineNumTv.setText(item.getArea());
            } else { // 搜索列表
                itemBinding.itemScenicSpotSearchResultLineNumTv.setVisibility(View.GONE);
                itemBinding.itemScenicSpotSearchResultLineIv.setVisibility(View.INVISIBLE);
                itemBinding.itemScenicSpotSearchResultLineIv1.setVisibility(View.VISIBLE);
            }
            // 需要显示蓝色的文字
            String content = datas.get(0).getCity();
            String lineName=item.getScenicName();
            lineName=lineName.replace("<em>", "");
            lineName=lineName.replace("</em>", "");
            // 判断当前文字里面有没有含有这个字符
            if (lineName.contains(content)){
                String[] data = lineName.split(content);
                if (data.length>1)
                    TextViewUtil.setTextViewForeNumColor(lineName,data[0].length(),(data[0].length()+content.length()),itemBinding.itemScenicSpotSearchResultLineKeywordsTv);
            } else { // 不包含直接显示
                itemBinding.itemScenicSpotSearchResultLineKeywordsTv.setText(lineName);
            }
        }
    }

}