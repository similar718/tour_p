package cn.xmzt.www.selfdrivingtools.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemOfflineFileManagerBinding;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.nim.uikit.common.util.file.FileUtil;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import cn.xmzt.www.selfdrivingtools.activity.OfflineFileManagerActivity;
import cn.xmzt.www.selfdrivingtools.bean.OfflineFileManagerBean;


public class OfflineFileManagerAdapter extends BaseRecycleViewAdapter<OfflineFileManagerBean, OfflineFileManagerAdapter.ItemHolder> {
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemOfflineFileManagerBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_offline_file_manager, parent, false);
        return new ItemHolder(itemRouteBinding);
    }

    private static Context mContext;
    private int mTypes = 0; // 0 正常情况下 1 编辑情况下

    public OfflineFileManagerAdapter(Context context){
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

    public void setTypes(int types) {
        mTypes = types;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.datas == null ? 0 : datas.size();
    }

    @Override
    public OfflineFileManagerBean getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }
        return null;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemOfflineFileManagerBinding itemBinding;

        public ItemHolder(ItemOfflineFileManagerBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((OfflineFileManagerActivity)mContext).OnListClickListener(getAdapterPosition(),mTypes);
                }
            });
        }
        public ItemOfflineFileManagerBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemOfflineFileManagerBinding itemBinding) {
            this.itemBinding = itemBinding;
        }

        protected void setViewDate(final int position) {
            OfflineFileManagerBean item = datas.get(position);
            if (mTypes == 1) {
                itemBinding.ivSelectStatus.setVisibility(View.VISIBLE);
            } else {
                itemBinding.ivSelectStatus.setVisibility(View.GONE);
            }
            GlideUtil.loadImgCircle(itemBinding.ivImg, item.getPhotoUrl());
            itemBinding.tvName.setText(item.getScenicName());
            itemBinding.tvNum.setText(FileUtil.formatFileSize(Long.valueOf(item.getResSize() + "")*1024));

            if (item.isSelect()){
                itemBinding.ivSelectStatus.setImageResource(R.drawable.icon_check_yx_duigou);
            } else {
                itemBinding.ivSelectStatus.setImageResource(R.drawable.icon_check_yx_un);
            }
        }
    }
}