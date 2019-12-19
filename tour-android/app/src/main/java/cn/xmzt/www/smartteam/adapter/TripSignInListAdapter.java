package cn.xmzt.www.smartteam.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemTripSignInListBinding;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import cn.xmzt.www.smartteam.activity.TripSignInListActivity;
import cn.xmzt.www.smartteam.bean.TripSignInListBean;

/**
 * 共享导航——主界面的天数
 */
public class TripSignInListAdapter extends BaseRecycleViewAdapter<TripSignInListBean.GroupMemberVOSBean, TripSignInListAdapter.ItemHolder> {
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemTripSignInListBinding itemRouteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_trip_sign_in_list, parent, false);
        return new ItemHolder(itemRouteBinding);
    }

    private static Context mContext;

    public TripSignInListAdapter(Context context){
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
    public TripSignInListBean.GroupMemberVOSBean getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }
        return null;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
        ItemTripSignInListBinding itemBinding;

        public ItemHolder(ItemTripSignInListBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });

            itemBinding.ivCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((TripSignInListActivity)mContext).OnListClickListener(datas.get(getLayoutPosition()).getTel(),getLayoutPosition());
                }
            });
        }
        public ItemTripSignInListBinding getBinding() {
            return itemBinding;
        }

        public void setBinding(ItemTripSignInListBinding itemBinding) {
            this.itemBinding = itemBinding;
        }

        protected void setViewDate(final int position) {
            TripSignInListBean.GroupMemberVOSBean item = datas.get(position);
            if (!TextUtils.isEmpty(item.getImage())) {
                GlideUtil.loadImgCircle(itemBinding.ivHeadImg, item.getImage());
            } else {
                itemBinding.ivHeadImg.setImageResource(R.drawable.icon_head_default);
            }
            if (!TextUtils.isEmpty(item.getUserName())) {
                itemBinding.tvNameContent.setText(item.getUserName());
            } else {
                itemBinding.tvNameContent.setText("默认用户");
            }
            itemBinding.tvUserStatusContent.setText(item.getSigninRemark());
            // 1 待签到 2 准时到达 3 迟到 4 签到失败
            if (item.getStatus() == 1){
                itemBinding.tvStatus.setText("待签到");
                itemBinding.tvStatus.setTextColor(mContext.getResources().getColor(R.color.color_ff9600));
            } else if (item.getStatus() == 2){
                itemBinding.tvStatus.setText("准时到达");
                itemBinding.tvStatus.setTextColor(mContext.getResources().getColor(R.color.color_4DC197));
            } else if (item.getStatus() == 3){
                itemBinding.tvStatus.setText("迟到");
                itemBinding.tvStatus.setTextColor(mContext.getResources().getColor(R.color.color_FF0C0C));
            } else if (item.getStatus() == 4){
                itemBinding.tvStatus.setText("签到失败");
                itemBinding.tvStatus.setTextColor(mContext.getResources().getColor(R.color.color_FF0C0C));
            } else {
                itemBinding.tvStatus.setText("其他情况");
                itemBinding.tvStatus.setTextColor(mContext.getResources().getColor(R.color.color_FF0C0C));
            }

            if (position == (datas.size()-1)){
                itemBinding.tvLine.setVisibility(View.GONE);
            } else {
                itemBinding.tvLine.setVisibility(View.VISIBLE);
            }
        }
    }
}