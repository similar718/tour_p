package cn.xmzt.www.selfdrivingtools.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemCarCheckInfoBinding;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import cn.xmzt.www.selfdrivingtools.activity.CarCheckInfoActivity;
import cn.xmzt.www.selfdrivingtools.bean.MsgCarListInfo;

/**
 * 共享导航——设置目的地界面的类型
 */
public class CarCheckInfoAdapter extends BaseRecycleViewAdapter<MsgCarListInfo, CarCheckInfoAdapter.ItemHolder> {
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCarCheckInfoBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_car_check_info, parent, false);
        return new ItemHolder(itemRouteBinding);
    }

    private static Context mContext;

    public CarCheckInfoAdapter(Context context){
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
    public MsgCarListInfo getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }
        return null;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemCarCheckInfoBinding itemBinding;

        public ItemHolder(ItemCarCheckInfoBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    ((CarCheckInfoActivity)mContext).OnListClickListener(getAdapterPosition());
                }
            });
            itemBinding.itemContentDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((CarCheckInfoActivity)mContext).OnListClickListener(getAdapterPosition(),datas.get(getAdapterPosition()).getMainPoint());
                }
            });
        }
        public ItemCarCheckInfoBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemCarCheckInfoBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(final int position) {
            MsgCarListInfo item = datas.get(position);
            itemBinding.itemTitle.setText(item.getCheckItem());
            itemBinding.itemContent.setText(item.getCheckStandard());
            if (position == (datas.size()-1)){
                itemBinding.tvLeftLine.setVisibility(View.GONE);
            } else {
                itemBinding.tvLeftLine.setVisibility(View.VISIBLE);
            }
        }
    }
}