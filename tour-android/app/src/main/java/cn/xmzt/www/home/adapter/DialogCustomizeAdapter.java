package cn.xmzt.www.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemCustomizeDialogAdapterBinding;
import cn.xmzt.www.home.bean.CustomizeLiveDialogBean;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;

public class DialogCustomizeAdapter extends BaseRecycleViewAdapter<CustomizeLiveDialogBean, DialogCustomizeAdapter.ItemHolder> {
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCustomizeDialogAdapterBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_customize_dialog_adapter, parent, false);
        return new ItemHolder(itemRouteBinding);
    }
    public DialogCustomizeAdapter(){
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.setViewDate(position);
    }
    @Override
    public void setDatas(List datas) {
        super.setDatas(datas);
        mCurrentPosition = -1;
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
    public CustomizeLiveDialogBean getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }
        return null;
    }

    private int mCurrentPosition = -1;

    class ItemHolder extends RecyclerView.ViewHolder {
        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemCustomizeDialogAdapterBinding itemBinding;

        public ItemHolder(ItemCustomizeDialogAdapterBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 如果当前已经被选中
                    int position = getAdapterPosition();
                    CustomizeLiveDialogBean item =  getItem(position);
                    if (item.isSelect()){
                        return;
                    } else {
                        if (mCurrentPosition != -1){
                            CustomizeLiveDialogBean currentItem=getItem(mCurrentPosition);
                            currentItem.setSelect(false);
                            notifyItemChanged(mCurrentPosition);
                        }
                        mCurrentPosition = position;
                        item.setSelect(true);
                        notifyItemChanged(mCurrentPosition);
                        if(itemListener!=null){
                            itemListener.onItemClick(view,position);
                        }
                    }
                }
            });
        }
        public ItemCustomizeDialogAdapterBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemCustomizeDialogAdapterBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(final int position) {
            CustomizeLiveDialogBean item =  getItem(position);
            itemBinding.setItem(item);
            if(mCurrentPosition==-1&&item.isSelect()){
                mCurrentPosition=position;
            }
        }
    }
}