package cn.xmzt.www.route.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemRouteBinding;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.route.bean.RoutePage;

import java.util.List;


public class RouteAdapter extends BaseRecycleViewAdapter<RoutePage.ItemsBean, RouteAdapter.ItemHolder>{

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemRouteBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_, parent, false);
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

    @Override
    public int getItemCount() {
        return this.datas == null ? 0 : datas.size();
    }

    @Override
    public RoutePage.ItemsBean getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }

        return null;
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemRouteBinding itemBinding;

        public ItemHolder(ItemRouteBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemListener!=null){
                       int position = getAdapterPosition();
                        itemListener.onItemClick(view,position);
                    }
                }
            });
        }

        public ItemRouteBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemRouteBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(final int position) {
            RoutePage.ItemsBean item = datas.get(position);

            String lineName=item.getLineName();
            lineName=lineName.replace("<em>", "<font color='#FF6000'>");
            lineName=lineName.replace("</em>", "</font>");
            itemBinding.titleTv.setText(Html.fromHtml(lineName));
            GlideUtil.loadImgRoundRadius(itemBinding.ivPhoto,item.getPhotoUrl(),4);
            itemBinding.desTv.setText(item.getIntro());
            itemBinding.tvProvider.setText(item.getOfficeNames());
            String departTime=item.getDepartTime();
            if(!TextUtils.isEmpty(departTime)){
                itemBinding.dateTv.setText(departTime.substring(0,10)+"出发");
            }else {
                itemBinding.dateTv.setText("");
            }
            itemBinding.priceTv.setText(item.getPrices());
            itemBinding.tvLineType.setText(item.getLineTypeName());
            if(TextUtils.isEmpty(item.getLineTypeName())){
                itemBinding.tvLineType.setVisibility(View.GONE);
            }else {
                itemBinding.tvLineType.setVisibility(View.VISIBLE);
            }
        }
    }

}