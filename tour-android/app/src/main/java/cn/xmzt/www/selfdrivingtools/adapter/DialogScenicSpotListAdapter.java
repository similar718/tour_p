package cn.xmzt.www.selfdrivingtools.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemScenicSpotListBinding;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import cn.xmzt.www.selfdrivingtools.activity.ScenicFeedBackActivity;

import java.util.List;

/**
 * 景点选择
 */
public class DialogScenicSpotListAdapter extends BaseRecycleViewAdapter<String, DialogScenicSpotListAdapter.ItemHolder>{
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemScenicSpotListBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_scenic_spot_list, parent, false);
        return new ItemHolder(itemRouteBinding);
    }

    private static Context mContext;

    public DialogScenicSpotListAdapter(Context context){
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
        ItemScenicSpotListBinding itemBinding;

        public ItemHolder(ItemScenicSpotListBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ScenicFeedBackActivity)mContext).OnTypeClickListener(getLayoutPosition());
                }
            });
        }
        public ItemScenicSpotListBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemScenicSpotListBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(final int position) {
            String item = datas.get(position);
            itemBinding.tvAddressInfo.setText(item);
        }
    }
}