package cn.xmzt.www.home.adapter;

import android.content.res.Resources;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemCitySelectedBinding;
import cn.xmzt.www.home.bean.CityBean;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;


public class SelectedCityAdapter extends BaseRecycleViewAdapter<CityBean, SelectedCityAdapter.ItemHolder> {
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCitySelectedBinding itemBinding=DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_city_selected, parent, false);
        return new ItemHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.setViewDate(position);
    }

    public void addData(CityBean data) {
        if(!this.datas.contains(data)){
            this.datas.add(data);
            notifyDataSetChanged();
        }
    }

    /**
     * 在子线程中添加
     * @param data
     */
    public void addDataNoUi(CityBean data) {
        if(!this.datas.contains(data)){
            this.datas.add(data);
        }
    }
    public void removeData(CityBean data) {
        this.datas.remove(data);
        notifyDataSetChanged();
    }
    public void clearData() {
        this.datas.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.datas == null ? 0 : datas.size();
    }

    @Override
    public CityBean getItem(int position) {
        if(position>=0&&position<datas.size()){
            return datas.get(position);
        }
        return null;
    }
    class ItemHolder extends RecyclerView.ViewHolder{
        private ItemCitySelectedBinding dbBinding;
        private Resources resources;
        public ItemHolder(ItemCitySelectedBinding dbBinding) {
            super(dbBinding.getRoot());
            this.dbBinding = dbBinding;
            resources=dbBinding.getRoot().getResources();
            dbBinding.ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(itemListener!=null){
                        itemListener.onItemClick(view,position);
                    }
                }
            });

        }
        protected void setViewDate(int position) {
            dbBinding.setItem(getItem(position));
            RecyclerView.LayoutParams mLayoutParams= (RecyclerView.LayoutParams) dbBinding.itemLayout.getLayoutParams();
            if(position==0){
                mLayoutParams.leftMargin=0;
                mLayoutParams.rightMargin=resources.getDimensionPixelOffset(R.dimen.dp_7);
            }else if(position==getItemCount()-1){
                mLayoutParams.leftMargin=resources.getDimensionPixelOffset(R.dimen.dp_7);
                mLayoutParams.rightMargin=0;
            }else{
                mLayoutParams.leftMargin=resources.getDimensionPixelOffset(R.dimen.dp_7);
                mLayoutParams.rightMargin=resources.getDimensionPixelOffset(R.dimen.dp_7);
            }

        }
    }
}