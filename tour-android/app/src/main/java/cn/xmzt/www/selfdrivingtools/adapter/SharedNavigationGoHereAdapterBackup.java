package cn.xmzt.www.selfdrivingtools.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemSharedNavigationGoHereBackBinding;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import cn.xmzt.www.selfdrivingtools.activity.SharedNavigationGoHereActivityBackUp;
import cn.xmzt.www.selfdrivingtools.bean.SharedNavigationGoHereBeanBack;
import cn.xmzt.www.selfdrivingtools.overlay.AMapUtil;

/**
 * 共享导航——设置目的地界面的类型
 */
public class SharedNavigationGoHereAdapterBackup extends BaseRecycleViewAdapter<SharedNavigationGoHereBeanBack, SharedNavigationGoHereAdapterBackup.ItemHolder> {
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSharedNavigationGoHereBackBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_shared_navigation_go_here_back, parent, false);
        return new ItemHolder(itemRouteBinding);
    }

    private static Context mContext;

    public SharedNavigationGoHereAdapterBackup(Context context){
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
    public SharedNavigationGoHereBeanBack getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }
        return null;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemSharedNavigationGoHereBackBinding itemBinding;

        public ItemHolder(ItemSharedNavigationGoHereBackBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((SharedNavigationGoHereActivityBackUp)mContext).OnPlanClickListener(getLayoutPosition());
                }
            });
        }
        public ItemSharedNavigationGoHereBackBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemSharedNavigationGoHereBackBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(final int position) {
            SharedNavigationGoHereBeanBack item = datas.get(position);
            itemBinding.setItem(item);
            itemBinding.tv1.setText(position == 0 ? "方案一" : (position == 1 ? "方案二" : "方案三"));
            // 算出时间和距离
            int dis = (int) item.getItem().getAllLength();
            int dur = (int) item.getItem().getAllTime();
            itemBinding.tv2.setText(AMapUtil.getFriendlyTime(dur));
            itemBinding.tv3.setText(AMapUtil.getFriendlyLength(dis));
            itemBinding.tv4.setText("￥"+ Math.round(item.getItem().getTollCost())); // 将价格转为int类型的
            if ((position+1) == datas.size()){
                itemBinding.tvRightLine.setVisibility(View.GONE);
            } else {
                itemBinding.tvRightLine.setVisibility(View.VISIBLE);
            }
        }
    }
}