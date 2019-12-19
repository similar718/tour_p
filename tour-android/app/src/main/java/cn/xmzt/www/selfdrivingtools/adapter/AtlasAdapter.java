package cn.xmzt.www.selfdrivingtools.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemScenicAtlasBinding;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import cn.xmzt.www.selfdrivingtools.activity.AtlasActivity;

import java.util.List;

/**
 * 图集Adapter
 */
public class AtlasAdapter extends BaseRecycleViewAdapter<String, AtlasAdapter.ItemHolder> {

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemScenicAtlasBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_scenic_atlas, parent, false);
        return new ItemHolder(itemRouteBinding);
    }

    private static Context mContext;

    public AtlasAdapter(Context context){
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
    public String getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }
        return null;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemScenicAtlasBinding itemBinding;

        public ItemHolder(ItemScenicAtlasBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getLayoutPosition();
                    ((AtlasActivity)mContext).OnBottomClickListener(datas.get(position),position);
                }
            });
        }

        public ItemScenicAtlasBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemScenicAtlasBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(final int position) {
            String item = datas.get(position);
            GlideUtil.loadImgRadius(itemBinding.itemAtlasContentIv,4,item);
            if (item.contains(".jpg") || item.contains(".png")){
                itemBinding.itemAtlasTypeIv.setVisibility(View.GONE);
            } else {
                itemBinding.itemAtlasTypeIv.setVisibility(View.VISIBLE);
            }
        }
    }
}