package cn.xmzt.www.selfdrivingtools.adapter;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemAkeySosBinding;
import cn.xmzt.www.databinding.ItemAkeySosFreeBinding;
import cn.xmzt.www.databinding.ItemAkeySosHeadBinding;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import cn.xmzt.www.selfdrivingtools.bean.AKeySOSBean;

import java.util.List;


public class AKeySOSAdapter extends BaseRecycleViewAdapter<Object, AKeySOSAdapter.ItemHolder> {
    private final int TYPE_ITEM_HEAD = 1;
    private final int TYPE_ITEM = 2;
    private final int TYPE_ITEM_FREE = 3;
    @Override
    public int getItemViewType(int position) {
        Object item = getItem(position);
        if(item instanceof AKeySOSBean){
            AKeySOSBean mAKeySOSBean= (AKeySOSBean) item;
            if(mAKeySOSBean.getType()==0){
                return TYPE_ITEM_FREE;
            }else {
                return TYPE_ITEM;
            }
        }
        return TYPE_ITEM_HEAD;
    }
    public boolean isSingleShow(int position){
        int itemviewtype=getItemViewType(position);
        if(itemviewtype==TYPE_ITEM_FREE){
            return false;
        }
        return true;
    }
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_ITEM_HEAD){
            return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_akey_sos_head, parent, false));
        }else if(viewType==TYPE_ITEM){
            return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_akey_sos, parent, false));
        }else{
            return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_akey_sos_free, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.setViewDate(position);
    }


    /**
     * 设置列表数据
     * @param datas 可用优惠券
     */
    public void setDatas(List datas) {
        super.setDatas(datas);
        notifyDataSetChanged();
    }
    public void addDatas(List datas) {
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        if(position>=0&&position<datas.size()){
            return datas.get(position);
        }
        return null;
    }
    public int selectPosition=0;
    class ItemHolder<DB extends ViewDataBinding> extends RecyclerView.ViewHolder{
        private DB dbBinding;
        public ItemHolder(DB dbBinding) {
            super(dbBinding.getRoot());
            this.dbBinding = dbBinding;
            if(dbBinding instanceof ItemAkeySosBinding){
                ItemAkeySosBinding itemBinding= (ItemAkeySosBinding) dbBinding;
                itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position=getAdapterPosition();
                        Object item = getItem(position);
                        if(item instanceof AKeySOSBean){
                            AKeySOSBean mAKeySOSBean= (AKeySOSBean) item;
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            Uri data = Uri.parse("tel:" + mAKeySOSBean.getPhone());
                            intent.setData(data);
                            view.getContext().startActivity(intent);
                        }
                    }
                });
            }else if(dbBinding instanceof ItemAkeySosFreeBinding){
                ItemAkeySosFreeBinding itemBinding= (ItemAkeySosFreeBinding) dbBinding;
                itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position=getAdapterPosition();
                        Object item = getItem(position);
                        if(item instanceof AKeySOSBean){
                            AKeySOSBean mAKeySOSBean= (AKeySOSBean) item;
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            Uri data = Uri.parse("tel:" + mAKeySOSBean.getPhone());
                            intent.setData(data);
                            view.getContext().startActivity(intent);
                        }
                    }
                });
            }

        }
        protected void setViewDate(int position) {
            Object item = getItem(position);
            if(dbBinding instanceof ItemAkeySosHeadBinding){
                ItemAkeySosHeadBinding itemBinding= (ItemAkeySosHeadBinding) dbBinding;
                if(item instanceof String){
                    itemBinding.tvName.setText((String)item);
                }
            }else if(dbBinding instanceof ItemAkeySosBinding){
                ItemAkeySosBinding itemBinding= (ItemAkeySosBinding) dbBinding;
                if(item instanceof AKeySOSBean){
                    AKeySOSBean mAKeySOSBean= (AKeySOSBean) item;
                    itemBinding.tvName.setText(mAKeySOSBean.getTitle());
                    itemBinding.tvDes.setText(mAKeySOSBean.getSubTitle());
                    if(mAKeySOSBean.getType()==2){
                        itemBinding.ivIcon.setImageResource(R.drawable.icon_akey_sos_gfkf);
                    }else {
                        itemBinding.ivIcon.setImageResource(R.drawable.icon_akey_sos_xcld);
                    }
                }
            }else if(dbBinding instanceof ItemAkeySosFreeBinding){
                ItemAkeySosFreeBinding itemBinding= (ItemAkeySosFreeBinding) dbBinding;
                if(item instanceof AKeySOSBean){
                    AKeySOSBean mAKeySOSBean= (AKeySOSBean) item;
                    itemBinding.tvName.setText(mAKeySOSBean.getTitle());
                    itemBinding.tvDes.setText(mAKeySOSBean.getSubTitle());
                }
            }
        }
    }
}