package cn.xmzt.www.selfdrivingtools.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemAkeySosListBinding;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import cn.xmzt.www.selfdrivingtools.activity.AKeySOSActivity;
import cn.xmzt.www.selfdrivingtools.bean.AKeySOSBean;

import java.util.List;

/**
 *  智慧导览——带有地图的景点列表
 */
public class ASosAdapter extends BaseRecycleViewAdapter<AKeySOSBean, ASosAdapter.ItemHolder> {

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemAkeySosListBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_akey_sos_list, parent, false);
        return new ItemHolder(itemRouteBinding);
    }
    private static Context mContext;

    public ASosAdapter(Context context){
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
    public AKeySOSBean getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }
        return null;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemAkeySosListBinding itemBinding;

        public ItemHolder(ItemAkeySosListBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    ((AKeySOSActivity)mContext).onSosClickListener(position);
                }
            });
        }

        public ItemAkeySosListBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemAkeySosListBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(final int position) {
            AKeySOSBean item = datas.get(position);
            itemBinding.ivIcon.setImageResource(item.getSrc());
            itemBinding.tvName.setText(item.getTitle());
            itemBinding.tvDes.setText(item.getSubTitle());
        }
    }

}