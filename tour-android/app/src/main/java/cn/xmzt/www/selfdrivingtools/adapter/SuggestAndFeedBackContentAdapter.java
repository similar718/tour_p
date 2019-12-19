package cn.xmzt.www.selfdrivingtools.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemSuggestAndFeedBackContentBinding;
import cn.xmzt.www.selfdrivingtools.activity.ScenicFeedBackActivity;
import cn.xmzt.www.selfdrivingtools.bean.ScenicFeedBackTypeBean;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;

import java.util.List;

/**
 * 建议与反馈问题内容的adapter
 */
public class SuggestAndFeedBackContentAdapter extends BaseRecycleViewAdapter<ScenicFeedBackTypeBean.ChildrenBean, SuggestAndFeedBackContentAdapter.ItemHolder> {

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSuggestAndFeedBackContentBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_suggest_and_feed_back_content, parent, false);
        return new ItemHolder(itemRouteBinding);
    }
    private static Context mContext;

    public SuggestAndFeedBackContentAdapter(Context context){
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
    public ScenicFeedBackTypeBean.ChildrenBean getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }

        return null;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemSuggestAndFeedBackContentBinding itemBinding;

        public ItemHolder(ItemSuggestAndFeedBackContentBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ScenicFeedBackActivity)mContext).OnNaviClickListener(getAdapterPosition(),2);
                }
            });
        }

        public ItemSuggestAndFeedBackContentBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemSuggestAndFeedBackContentBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(final int position) {
            ScenicFeedBackTypeBean.ChildrenBean item = datas.get(position);
            itemBinding.setItem(item);
//            itemBinding.iv1.setImageResource(item.isIs_Checked()?R.drawable.scenic_feed_back_selected:R.drawable.scenic_feed_back_select);
        }
    }

}