package cn.xmzt.www.mine.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.main.activity.MainActivity;
import cn.xmzt.www.mine.bean.MyCouponBean;
import cn.xmzt.www.pay.activity.PaySuccessActivity;
import cn.xmzt.www.view.labeltext.LabelTextView;
import cn.xmzt.www.view.listener.ItemListener;

import java.util.List;

/**
 * @author tanlei
 * @date 2019/8/12
 * @describe 我的优惠券适配器
 */

public class MyCouponAdapter extends BaseAdapter {
    private Context context;
    private List<MyCouponBean> list;
    private ItemListener itemListener;
    /**
     * 优惠券状态(0:未使用,1:已使用,2:已过期)
     */
    private int state = 0;

    public void setState(int state) {
        this.state = state;
        this.notifyDataSetChanged();
    }

    public ItemListener getItemListener() {
        return itemListener;
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public MyCouponAdapter(Context context, List<MyCouponBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {

        return i;
    }

    public void onNotifyDataSetChanged(List<MyCouponBean> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (null == view) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_coupon_my, null);
            holder.tvPrice = view.findViewById(R.id.tv_price);
            holder.tv_usable = view.findViewById(R.id.tv_usable);
            holder.tv_get = view.findViewById(R.id.tv_get);
            holder.tv_start_time = view.findViewById(R.id.tv_start_time);
            holder.tv_title = view.findViewById(R.id.tv_title);
            holder.iv_down_up = view.findViewById(R.id.iv_down_up);
            holder.tv_remark = view.findViewById(R.id.tv_remark);
            holder.leftLayout = view.findViewById(R.id.leftLayout);
            holder.iv_received = view.findViewById(R.id.iv_received);
            holder.tv_end_time = view.findViewById(R.id.tv_end_time);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvPrice.setText(list.get(i).getMaxSubtract() + "");
        holder.tv_usable.setText(context.getString(R.string.satisfy) + list.get(i).getMinConsume() + context.getString(R.string.available));
        holder.tv_start_time.setText(list.get(i).getEffectDate());
        holder.tv_end_time.setText(" - " + list.get(i).getExpireDate());
        holder.tv_remark.setText(list.get(i).getDetail());
        holder.tv_title.setOriginalText(list.get(i).getTitle());
        switch (list.get(i).getType()) {
            case 1:
                holder.tv_title.setLabelText("线路");
                break;
            case 2:
                holder.tv_title.setLabelText("酒店");
                break;
            case 3:
                holder.tv_title.setLabelText("门票");
                break;
            case 0:
                holder.tv_title.setLabelText("通用");
                break;
        }
        if (list.get(i).isDetails()) {
            holder.tv_remark.setVisibility(View.VISIBLE);
            holder.iv_down_up.setImageResource(R.drawable.icon_c_up);
        } else {
            holder.tv_remark.setVisibility(View.GONE);
            holder.iv_down_up.setImageResource(R.drawable.icon_c_down);
        }
        holder.iv_down_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.get(i).setDetails(!list.get(i).isDetails());
                notifyDataSetChanged();
            }
        });
        holder.tv_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemListener!=null){
                    itemListener.onItemClick(view,i);
                }
            }
        });
        if (state == 0) {
            holder.leftLayout.setBackgroundResource(R.drawable.coupon_bg_orange_left_c);
            holder.tv_title.setStrokeColor(Color.parseColor("#24A4FF"));
            holder.iv_received.setVisibility(View.GONE);
            holder.tv_get.setVisibility(View.VISIBLE);
        } else if (state == 1) {
            holder.leftLayout.setBackgroundResource(R.drawable.coupon_gray_bg_left_c);
            holder.tv_title.setStrokeColor(Color.parseColor("#c2c2c2"));
            holder.iv_received.setVisibility(View.VISIBLE);
            holder.iv_received.setImageResource(R.drawable.icon_use);
            holder.tv_get.setVisibility(View.GONE);
        } else {
            holder.leftLayout.setBackgroundResource(R.drawable.coupon_gray_bg_left_c);
            holder.tv_title.setStrokeColor(Color.parseColor("#c2c2c2"));
            holder.iv_received.setVisibility(View.VISIBLE);
            holder.iv_received.setImageResource(R.drawable.icon_overdue);
            holder.tv_get.setVisibility(View.GONE);
        }
        return view;
    }

    public interface OnTypeClickListener {
        void onTypeClick(int position);
    }

    static class ViewHolder {
        AppCompatTextView tvPrice;
        TextView tv_usable, tv_get, tv_start_time, tv_remark, tv_end_time;
        LabelTextView tv_title;
        ImageView iv_down_up, iv_received;
        LinearLayout leftLayout;
    }
}
