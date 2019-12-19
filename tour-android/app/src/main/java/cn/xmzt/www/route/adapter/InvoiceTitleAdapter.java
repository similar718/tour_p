package cn.xmzt.www.route.adapter;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.bean.InvoiceTitleBean;
import cn.xmzt.www.databinding.ItemInvoiceTitleAddBinding;
import cn.xmzt.www.databinding.ItemInvoiceTitleBinding;
import cn.xmzt.www.route.activity.AddInvoiceTitleActivity;

import java.util.List;


public class InvoiceTitleAdapter extends BaseRecycleViewAdapter<InvoiceTitleBean, InvoiceTitleAdapter.ItemHolder>{
    private final int TYPE_ITEM_HEAD = 1;
    private final int TYPE_ITEM = 2;
    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_ITEM_HEAD;
        }
        return TYPE_ITEM;
    }
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_ITEM_HEAD){
            return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_invoice_title_add, parent, false));
        }else {
            return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_invoice_title, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.setViewDate(position);
    }

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
        return this.datas == null ? 1 : datas.size()+1;
    }

    @Override
    public InvoiceTitleBean getItem(int position) {
        int index=position-1;
        if(index>=0&&index<datas.size()){
            return datas.get(index);
        }
        return null;
    }
    public int selectPosition=0;
    class ItemHolder<DB extends ViewDataBinding> extends RecyclerView.ViewHolder{
        private DB dbBinding;
        public ItemHolder(DB dbBinding) {
            super(dbBinding.getRoot());
            this.dbBinding = dbBinding;
            if(dbBinding instanceof ItemInvoiceTitleAddBinding){
                ItemInvoiceTitleAddBinding itemBinding= (ItemInvoiceTitleAddBinding) dbBinding;
                itemBinding.addLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //添加发票抬头
                        Intent intent=new Intent(view.getContext(), AddInvoiceTitleActivity.class);
                        view.getContext().startActivity(intent);
                    }
                });
            }else if(dbBinding instanceof ItemInvoiceTitleBinding){
                ItemInvoiceTitleBinding itemBinding= (ItemInvoiceTitleBinding) dbBinding;
                itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position=getAdapterPosition();
                        InvoiceTitleBean mInvoiceTitleBean1=getItem(selectPosition);
                        InvoiceTitleBean mInvoiceTitleBean2=getItem(position);
                        if(mInvoiceTitleBean1!=null){
                            mInvoiceTitleBean1.setSelect(false);
                            notifyItemChanged(selectPosition);
                        }
                        if(mInvoiceTitleBean2!=null){
                            mInvoiceTitleBean2.setSelect(true);
                            notifyItemChanged(position);
                        }
                        selectPosition=position;
                    }
                });
                itemBinding.ivEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //编辑发票抬头
                        int position=getAdapterPosition();
                        InvoiceTitleBean mInvoiceTitleBean=getItem(position);
                        if(mInvoiceTitleBean!=null){
                            Intent intent=new Intent(view.getContext(), AddInvoiceTitleActivity.class);
                            intent.putExtra("A",mInvoiceTitleBean);
                            view.getContext().startActivity(intent);
                        }
                    }
                });
            }

        }
        protected void setViewDate(int position) {
            InvoiceTitleBean item = getItem(position);
            if(dbBinding instanceof ItemInvoiceTitleAddBinding){
                ItemInvoiceTitleAddBinding itemBinding= (ItemInvoiceTitleAddBinding) dbBinding;
                if(getItemCount()>1){
                    itemBinding.tvNoData.setVisibility(View.GONE);
                }else {
                    itemBinding.tvNoData.setVisibility(View.VISIBLE);
                }
            }else if(dbBinding instanceof ItemInvoiceTitleBinding){
                ItemInvoiceTitleBinding itemBinding= (ItemInvoiceTitleBinding) dbBinding;
                itemBinding.setItem(item);
            }
        }
    }
}