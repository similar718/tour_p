package cn.xmzt.www.home.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemCityBinding;
import cn.xmzt.www.databinding.ItemCityCurrentBinding;
import cn.xmzt.www.databinding.ItemCityGroupBinding;
import cn.xmzt.www.home.bean.CityBean;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;

import java.util.List;


public class SelectCityAdapter extends BaseRecycleViewAdapter<Object, SelectCityAdapter.ItemHolder> {
    private final int TYPE_CURRENT_CITY = 1;
    private final int TYPE_GROUP = 2;
    private final int TYPE_CITY = 3;
    private int selectType=0;
    public void setSelectType(int selectType) {
        this.selectType = selectType;
    }

    @Override
    public int getItemViewType(int position) {
        Object obj=getItem(position);
        if(position == 0){
            return TYPE_CURRENT_CITY;
        }else if(obj instanceof String){
            return TYPE_GROUP;
        }else {
            return TYPE_CITY;
        }
    }
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_CURRENT_CITY){
            return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_city_current, parent, false));
        }else if(viewType==TYPE_GROUP){
            return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_city_group, parent, false));
        }else {
            return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_city, parent, false));
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

    @Override
    public int getItemCount() {
        return this.datas == null ? 0 : datas.size();
    }
    public int getItemPosition(String leter) {
        int leterPosition=0;
        for(int i=0;i<datas.size();i++){
            Object obj=datas.get(i);
            if(leter.equals(obj)){
                leterPosition=i;
                break;
            }
        }
        return leterPosition;
    }
    @Override
    public Object getItem(int position) {
        if(position>=0&&position<datas.size()){
            return datas.get(position);
        }
        return null;
    }
    class ItemHolder<DB extends ViewDataBinding> extends RecyclerView.ViewHolder{
        private DB dbBinding;
        public ItemHolder(DB dbBinding) {
            super(dbBinding.getRoot());
            this.dbBinding = dbBinding;
            if(dbBinding instanceof ItemCityCurrentBinding){
                ItemCityCurrentBinding itemBinding= (ItemCityCurrentBinding) dbBinding;
                itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position=getAdapterPosition();
                        if(itemListener!=null){
                            itemListener.onItemClick(view,position);
                        }
                    }
                });
            }else if(dbBinding instanceof ItemCityBinding){
                ItemCityBinding itemBinding= (ItemCityBinding) dbBinding;
                itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position=getAdapterPosition();
                        if(itemListener!=null){
                            itemListener.onItemClick(view,position);
                        }
                    }
                });
            }

        }
        protected void setViewDate(int position) {
            Object obj = getItem(position);
            if(dbBinding instanceof ItemCityCurrentBinding){
                ItemCityCurrentBinding itemBinding= (ItemCityCurrentBinding) dbBinding;
                if(obj instanceof CityBean){
                    CityBean cityBean=(CityBean) obj;
                    itemBinding.setItem(cityBean);
                    if("暂无定位信息".equals(cityBean.getAreaName())){
                        if(selectType==0){
                            itemBinding.ivCheck.setImageResource(R.drawable.icon_check_yx_un);
                        }else {
                            itemBinding.ivCheck.setImageResource(R.drawable.icon_check_fx_un);
                        }
                    }else {
                        if(selectType==0){
                            if(cityBean.isSelect()){
                                itemBinding.ivCheck.setImageResource(R.drawable.icon_check_yx);
                            }else {
                                itemBinding.ivCheck.setImageResource(R.drawable.icon_check_yx_un);
                            }
                        }else {
                            if(cityBean.isSelect()){
                                itemBinding.ivCheck.setImageResource(R.drawable.icon_check_fx);
                            }else {
                                itemBinding.ivCheck.setImageResource(R.drawable.icon_check_fx_un);
                            }
                        }
                    }
                }
            }else if(dbBinding instanceof ItemCityBinding){
                ItemCityBinding itemBinding= (ItemCityBinding) dbBinding;
                if(obj instanceof CityBean){
                    CityBean cityBean=(CityBean) obj;
                    itemBinding.setItem(cityBean);
                    if(selectType==0){
                        if(cityBean.isSelect()){
                            itemBinding.ivCheck.setImageResource(R.drawable.icon_check_yx);
                        }else {
                            itemBinding.ivCheck.setImageResource(R.drawable.icon_check_yx_un);
                        }
                    }else {
                        if(cityBean.isSelect()){
                            itemBinding.ivCheck.setImageResource(R.drawable.icon_check_fx);
                        }else {
                            itemBinding.ivCheck.setImageResource(R.drawable.icon_check_fx_un);
                        }
                    }
                }
            }else if(dbBinding instanceof ItemCityGroupBinding){
                ItemCityGroupBinding itemBinding= (ItemCityGroupBinding) dbBinding;
                itemBinding.setName((String) obj);
            }
        }
    }
}