package cn.xmzt.www.selfdrivingtools.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemSuggestAndFeedBackNaviBinding;
import cn.xmzt.www.selfdrivingtools.activity.ScenicFeedBackActivity;
import cn.xmzt.www.selfdrivingtools.bean.SuggestAndFeedBackNaviBean;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;

import java.util.List;

/**
 * 建议与反馈问题类型的adapter
 */
public class SuggestAndFeedBackNaviAdapter extends BaseRecycleViewAdapter<SuggestAndFeedBackNaviBean, SuggestAndFeedBackNaviAdapter.ItemHolder> {

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSuggestAndFeedBackNaviBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_suggest_and_feed_back_navi, parent, false);
        return new ItemHolder(itemRouteBinding);
    }
    private static Context mContext;

    public SuggestAndFeedBackNaviAdapter(Context context){
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
    public SuggestAndFeedBackNaviBean getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }

        return null;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemSuggestAndFeedBackNaviBinding itemBinding;

        public ItemHolder(ItemSuggestAndFeedBackNaviBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ScenicFeedBackActivity)mContext).OnNaviClickListener(getAdapterPosition(),1);
                }
            });
        }

        public ItemSuggestAndFeedBackNaviBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemSuggestAndFeedBackNaviBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(final int position) {
            SuggestAndFeedBackNaviBean item = datas.get(position);
            itemBinding.setItem(item);
            itemBinding.iv1.setImageResource(item.getIcon());
            if (item.isIs_Checked()){
                itemBinding.tv1.setTextColor(Color.parseColor("#333333"));
                itemBinding.tv1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else {
                itemBinding.tv1.setTextColor(Color.parseColor("#999999"));
                itemBinding.tv1.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        }
    }

}