package cn.xmzt.www.mine.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemMessageTypeOrderBinding;
import cn.xmzt.www.mine.bean.MessageBean;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import cn.xmzt.www.utils.SchemeActivityUtil;

import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


public class MessageCenterOrderAdapter extends BaseRecycleViewAdapter<MessageBean, MessageCenterOrderAdapter.ItemHolder> {

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMessageTypeOrderBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_message_type_order, parent, false);
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
    public MessageBean getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }

        return null;
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemMessageTypeOrderBinding itemBinding;

        public ItemHolder(ItemMessageTypeOrderBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    MessageBean mb=getItem(position);
                    String url=mb.getUrl();
                    if(!TextUtils.isEmpty(url)){
                        SchemeActivityUtil.startToActivity(view.getContext(),4,"",url);
                    }
                }
            });
        }

        protected void setViewDate(final int position) {
            MessageBean item = datas.get(position);
            itemBinding.setItem(item);
        }
    }

}