package cn.xmzt.www.route.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.bean.OftenVisitorsBean;
import cn.xmzt.www.databinding.ItemOfenVisitorsBinding;

import java.util.List;


public class OfenVisitorsAdapter extends BaseRecycleViewAdapter<OftenVisitorsBean, OfenVisitorsAdapter.ItemHolder>{

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemOfenVisitorsBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_ofen_visitors, parent, false);
        return new ItemHolder(itemRouteBinding);
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
    public void notifyItemChanged(OftenVisitorsBean mOftenVisitorsBean) {
        for(int i=0;i<datas.size();i++){
            OftenVisitorsBean visitorsBean=datas.get(i);
            if(visitorsBean.getId()==mOftenVisitorsBean.getId()){
                notifyItemChanged(i);
                break;
            }
        }
    }
    @Override
    public int getItemCount() {
        return this.datas == null ? 0 : datas.size();
    }

    @Override
    public OftenVisitorsBean getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }

        return null;
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemOfenVisitorsBinding itemBinding;

        public ItemHolder(ItemOfenVisitorsBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemListener!=null){
                        int position=getAdapterPosition();
                        itemListener.onItemClick(itemBinding.ivCheck,position);
                    }
                }
            });
            itemBinding.ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemListener!=null){
                       int position = getAdapterPosition();
                        itemListener.onItemClick(view,position);
                    }
                }
            });
        }

        public ItemOfenVisitorsBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemOfenVisitorsBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(int position) {
            OftenVisitorsBean item = datas.get(position);
            itemBinding.setItem(item);
            itemBinding.ivCheck.setSelected(item.isSelect());
        }
    }

}