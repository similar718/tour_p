package cn.xmzt.www.smartteam.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemSmartTeamMapTypeBinding;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import cn.xmzt.www.selfdrivingtools.bean.SettingDestinationTypeInfo;
import cn.xmzt.www.smartteam.activity.SmartTeamMapActivity;

/**
 * 共享导航——主界面的天数
 */
public class SmartTeamTypeAdapter extends BaseRecycleViewAdapter<SettingDestinationTypeInfo, SmartTeamTypeAdapter.ItemHolder> {
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSmartTeamMapTypeBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_smart_team_map_type, parent, false);
        return new ItemHolder(itemRouteBinding);
    }

    private static Context mContext;

    public SmartTeamTypeAdapter(Context context){
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
    public SettingDestinationTypeInfo getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }
        return null;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemSmartTeamMapTypeBinding itemBinding;

        public ItemHolder(ItemSmartTeamMapTypeBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((SmartTeamMapActivity)mContext).OnBottomListClickListener("",getLayoutPosition(),2);
                }
            });
        }
        public ItemSmartTeamMapTypeBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemSmartTeamMapTypeBinding itemBinding) {
            this.itemBinding = itemBinding;
        }
        protected void setViewDate(final int position) {
            SettingDestinationTypeInfo item = datas.get(position);
            itemBinding.tvTypeContent.setText(item.getContent());
            itemBinding.tvTypeContent.setTextColor(item.isIs_Checked() ? Color.parseColor("#24A4FF") : Color.parseColor("#666666"));
            itemBinding.tvTypeContentLine.setVisibility(item.isIs_Checked() ? View.VISIBLE : View.GONE);
        }
    }
}