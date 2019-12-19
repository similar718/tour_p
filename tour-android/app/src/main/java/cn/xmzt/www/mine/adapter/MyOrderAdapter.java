package cn.xmzt.www.mine.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.mine.bean.MyOrderBean;

import java.util.List;

/**
 * @author tanlei
 * @date 2019/8/14
 * @describe
 */

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyOrderViewHolder> {
    private List<MyOrderBean> list;
    private Context context;
    private OnLeftOrRightClickListener onLeftOrRightClickListener;
    public static final int LEFT_CLICK = 1;
    public static final int RIGHT_CLICK = 2;

    public void setOnLeftOrRightClickListener(OnLeftOrRightClickListener onLeftOrRightClickListener) {
        this.onLeftOrRightClickListener = onLeftOrRightClickListener;
    }

    public MyOrderAdapter(List<MyOrderBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyOrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        MyOrderViewHolder holder = new MyOrderViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_order, viewGroup, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderViewHolder myOrderViewHolder, int i) {
        myOrderViewHolder.tv_order_name.setText(list.get(i).getOrderName());
        myOrderViewHolder.tv_order_price.setText(list.get(i).getPirceCount() + "");
        switch (list.get(i).getOrderType()) {
            case 1://线路
                myOrderViewHolder.iv_icon.setImageResource(R.drawable.icon_order_line);
                myOrderViewHolder.tv_type.setText("线路");
                myOrderViewHolder.tv_address.setVisibility(View.GONE);
                myOrderViewHolder.tv_date.setText(list.get(i).getBeginDate() + " 至 " + list.get(i).getEndDate());
                break;
            case 2://门票
                myOrderViewHolder.iv_icon.setImageResource(R.drawable.icon_order_ticket);
                myOrderViewHolder.tv_type.setText("门票");
                myOrderViewHolder.tv_address.setText(list.get(i).getBeginDate() + "当日");
                myOrderViewHolder.tv_address.setVisibility(View.VISIBLE);
                myOrderViewHolder.tv_date.setText("数量 " + (int) list.get(i).getProductCount());
                break;
            case 3://酒店
                myOrderViewHolder.iv_icon.setImageResource(R.drawable.icon_order_hotel);
                myOrderViewHolder.tv_type.setText("酒店");
                myOrderViewHolder.tv_address.setText(list.get(i).getDetailAddress());
                myOrderViewHolder.tv_address.setVisibility(View.VISIBLE);
                myOrderViewHolder.tv_date.setText(list.get(i).getBeginDate() + "至" + list.get(i).getEndDate() + "  " + list.get(i).getNumber() + "晚/" + list.get(i).getProductCount() + "间");
                break;
        }
        myOrderViewHolder.tv_left.setVisibility(View.VISIBLE);
        myOrderViewHolder.tv_right.setVisibility(View.VISIBLE);
        myOrderViewHolder.tv_refund.setVisibility(View.GONE);
        switch (list.get(i).getRefundState()) {
            case 1:
                myOrderViewHolder.tv_refund.setVisibility(View.VISIBLE);
                myOrderViewHolder.tv_refund.setText("退款中");
                myOrderViewHolder.tv_left.setVisibility(View.GONE);
                myOrderViewHolder.tv_right.setVisibility(View.GONE);
                myOrderViewHolder.line.setVisibility(View.GONE);
                setRefund(list.get(i).getState(),myOrderViewHolder.tv_order_type);
                break;
            case 2:
                myOrderViewHolder.tv_refund.setVisibility(View.VISIBLE);
                myOrderViewHolder.tv_refund.setText("退款失败");
                myOrderViewHolder.tv_left.setVisibility(View.GONE);
                myOrderViewHolder.tv_right.setVisibility(View.GONE);
                myOrderViewHolder.line.setVisibility(View.GONE);
                setRefund(list.get(i).getState(),myOrderViewHolder.tv_order_type);
                break;
            case 3:
                myOrderViewHolder.tv_refund.setVisibility(View.VISIBLE);
                myOrderViewHolder.tv_refund.setText("退款成功");
                myOrderViewHolder.tv_left.setText("删除");
                myOrderViewHolder.tv_right.setText("再次预定");
                myOrderViewHolder.line.setVisibility(View.VISIBLE);
                setRefund(list.get(i).getState(),myOrderViewHolder.tv_order_type);
                break;
        }
        switch (list.get(i).getOrdState()) {
            case 1://待支付
                myOrderViewHolder.tv_order_type.setText("待支付");
                myOrderViewHolder.tv_left.setText("取消");
                myOrderViewHolder.tv_right.setText("立即支付");
                break;
            case 2://已取消
                myOrderViewHolder.tv_order_type.setText("已取消");
                myOrderViewHolder.tv_left.setText("删除");
                myOrderViewHolder.tv_right.setText("再次预定");
                break;
            case 3://待确认
                myOrderViewHolder.tv_order_type.setText("待确认");
                myOrderViewHolder.tv_right.setVisibility(View.GONE);
                myOrderViewHolder.tv_left.setText("退款");
                break;
            case 4://预定失败
                myOrderViewHolder.tv_order_type.setText("预定失败");
                break;
            case 6://待出行
                myOrderViewHolder.tv_order_type.setText("待出行");
                myOrderViewHolder.tv_right.setVisibility(View.GONE);
                myOrderViewHolder.tv_left.setText("退款");
                break;
//            case 7://退款中
//                myOrderViewHolder.tv_order_type.setText("退款中");
//                break;
//            case 8://退款失败
//                myOrderViewHolder.tv_order_type.setText("退款失败");
//                break;
//            case 9://退款成功
//                myOrderViewHolder.tv_order_type.setText("退款成功");
//                myOrderViewHolder.tv_left.setText("删除");
//                myOrderViewHolder.tv_right.setText("再次预定");
//                break;
            case 10://已成交
                myOrderViewHolder.tv_order_type.setText("已成交");
                myOrderViewHolder.tv_left.setText("删除");
                myOrderViewHolder.tv_right.setText("再次预定");
                break;
            case 11://已关闭
                myOrderViewHolder.tv_order_type.setText("已关闭");
                myOrderViewHolder.tv_left.setText("删除");
                myOrderViewHolder.tv_right.setText("再次预定");
                break;
        }
        myOrderViewHolder.tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onLeftOrRightClickListener) {
                    onLeftOrRightClickListener.onLeftOrRightClick(i, LEFT_CLICK);
                }
            }
        });
        myOrderViewHolder.tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onLeftOrRightClickListener) {
                    onLeftOrRightClickListener.onLeftOrRightClick(i, RIGHT_CLICK);
                }
            }
        });
        myOrderViewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onLeftOrRightClickListener) {
                    onLeftOrRightClickListener.onLeftOrRightClick(i, 3);
                }
            }
        });
    }

    private void setRefund(int state,TextView tv) {
        switch (state) {
            case 0://初始化
                tv.setText("初始化");
                break;
            case 10://待支付
                tv.setText("待支付");
                break;
            case 30://已取消
                tv.setText("已取消");
                break;
            case 40://待确认
                tv.setText("待确认");
                break;
            case 50://预定失败
                tv.setText("预定失败");
                break;
            case 60://待出行
                tv.setText("待出行");
                break;
            case 100://已成交
                tv.setText("已成交");
                break;
            case 110://已关闭
                tv.setText("已关闭");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyOrderViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_icon;
        private TextView tv_type, tv_order_name, tv_order_price, tv_order_type, tv_address, tv_date, tv_left, tv_right, line, tv_refund;
        private ConstraintLayout constraintLayout;

        public MyOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_order_name = itemView.findViewById(R.id.tv_order_name);
            tv_order_price = itemView.findViewById(R.id.tv_order_price);
            tv_order_type = itemView.findViewById(R.id.tv_order_type);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_left = itemView.findViewById(R.id.tv_left);
            tv_right = itemView.findViewById(R.id.tv_right);
            line = itemView.findViewById(R.id.line);
            tv_refund = itemView.findViewById(R.id.tv_refund);
            constraintLayout = itemView.findViewById(R.id.cl_layout);
        }
    }

    public interface OnLeftOrRightClickListener {
        void onLeftOrRightClick(int position, int leftOrRight);
    }

}
