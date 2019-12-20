package cn.xmzt.www.mine.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.AdapterItemSignCategoryBinding;
import cn.xmzt.www.databinding.AdapterItemSignHeadBinding;
import cn.xmzt.www.mine.bean.SignDayBean;
import cn.xmzt.www.mine.bean.SignInBean;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;

/**
 * 签到适配器
 */
public class SignInAdapter extends BaseRecycleViewAdapter<Object, SignInAdapter.ItemHolder> {
    private final int TYPE_ITEM_HEAD = 1;//顶部
    private final int TYPE_ITEM_CATEGORY = 2;//分类
    private final int TYPE_ITEM_NUM = 3;//1到30
    private final int TYPE_ITEM_NUM_TOP = 31;//1到30的顶部圆角背景
    private final int TYPE_ITEM_NUM_BOTTOM = 32;//1到30的底部圆角背景
    private final int TYPE_ITEM_AWARD = 4;//奖品

    public SignInAdapter() {
        this.datas.add("head");
        this.datas.add("签到记录");
        this.datas.add("num_top");
        for (int i = 1; i < 31; i++) {
            this.datas.add(new SignDayBean(i + ""));
        }
        this.datas.add("num_bottom");
    }

    //
    public void setData(SignInBean mSignInBean) {
        this.datas.clear();
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return this.datas == null ? 0: datas.size();
    }

    @Override
    public Object getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        Object obj=getItem(position);
        if(obj instanceof SignDayBean){
            return TYPE_ITEM_NUM;
        }else if(obj instanceof SignInBean.AwardsEntity){
            return TYPE_ITEM_AWARD;
        }else if(obj instanceof String){
            if("head".equals(obj)){
                return TYPE_ITEM_HEAD;
            }else if("num_top".equals(obj)){
                return TYPE_ITEM_NUM_TOP;
            }else if("num_bottom".equals(obj)){
                return TYPE_ITEM_NUM_BOTTOM;
            }else{
                return TYPE_ITEM_CATEGORY;
            }
        }
        return super.getItemViewType(position);
    }
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemHolder holder=null;
        if(viewType==TYPE_ITEM_HEAD){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_item_sign_head, parent, false));
        }else if(viewType==TYPE_ITEM_CATEGORY){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_item_sign_category, parent, false));
        }else if(viewType==TYPE_ITEM_CATEGORY){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_item_sign_category, parent, false));
        }
        return holder;
    }
    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.setViewDate(position);
    }
    class ItemHolder<DB extends ViewDataBinding> extends RecyclerView.ViewHolder implements View.OnClickListener {
        private DB dbBinding;
        public ItemHolder(DB dbBinding) {
            super(dbBinding.getRoot());
            this.dbBinding = dbBinding;
            if(dbBinding instanceof AdapterItemSignHeadBinding){
                AdapterItemSignHeadBinding itemBinding= (AdapterItemSignHeadBinding) this.dbBinding;
               /* itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=getAdapterPosition();
                        setSelectedItem(position);
                    }
                });*/
            }
        }
        private void setViewDate(int position){
            if(dbBinding instanceof AdapterItemSignHeadBinding){
                AdapterItemSignHeadBinding itemBinding= (AdapterItemSignHeadBinding) this.dbBinding;
                Object obj=getItem(position);

            }else if(dbBinding instanceof AdapterItemSignHeadBinding){
                AdapterItemSignHeadBinding itemBinding= (AdapterItemSignHeadBinding) this.dbBinding;

            }else if(dbBinding instanceof AdapterItemSignCategoryBinding){
                Object obj=getItem(position);
                AdapterItemSignCategoryBinding itemBinding= (AdapterItemSignCategoryBinding) this.dbBinding;
                if(obj instanceof String){
                    itemBinding.setName((String) obj);
                }
            }
        }

        @Override
        public void onClick(View v) {
            if(itemListener!=null){
                int position=getAdapterPosition();
                itemListener.onItemClick(v,position);
            }
        }
    }
}
