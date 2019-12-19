package cn.xmzt.www.route.adapter;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemRouteOrder0Binding;
import cn.xmzt.www.databinding.ItemRouteOrder1Binding;
import cn.xmzt.www.databinding.ItemRouteOrder2Binding;
import cn.xmzt.www.databinding.ItemRouteOrder3Binding;
import cn.xmzt.www.databinding.ItemRouteOrder4Binding;
import cn.xmzt.www.databinding.ItemRouteOrder5Binding;
import cn.xmzt.www.databinding.ItemRouteOrder6Binding;
import cn.xmzt.www.databinding.ItemRouteOrder71Binding;
import cn.xmzt.www.databinding.ItemRouteOrder7Binding;
import cn.xmzt.www.route.activity.InvoiceInfoActivity;
import cn.xmzt.www.route.activity.RouteFillinInvoiceActivity;
import cn.xmzt.www.route.bean.CarForm;
import cn.xmzt.www.route.bean.ContactForm;
import cn.xmzt.www.route.bean.CostBean;
import cn.xmzt.www.route.bean.OrderInvoiceInfo;
import cn.xmzt.www.route.bean.RouteDetailCategory;
import cn.xmzt.www.route.bean.RouteOrderDetailBean;
import cn.xmzt.www.utils.MathUtil;

import java.util.List;


public class RouteOrderAdapter extends BaseRecycleViewAdapter<Object, RouteOrderAdapter.ItemHolder>{
    private final int TYPE_ITEM_0 = 10;//头部
    private final int TYPE_ITEM_1 = 1;//线路
    private final int TYPE_ITEM_2 = 2;//分类
    private final int TYPE_ITEM_3 = 3;//费用
    private final int TYPE_ITEM_4 = 4;//出游人
    private final int TYPE_ITEM_5 = 5;//车辆信息
    private final int TYPE_ITEM_6 = 6;//预定人、联系人
    private final int TYPE_ITEM_7 = 7;//有发票
    private final int TYPE_ITEM_7_1 = 71;//没有发票
    private final int TYPE_ITEM_NULL = 11;//没有发票

    private int endCrVisitorIndex=0;//成人出游人结束位置
    private int endXhVisitorIndex=0;//小孩出游人结束位置
    private int singleRoomIndex=0;//单房差位置

    private int endCarIndex=0;//车辆信息结束位置

    public int orderState;//订单状态(0:初始化,10.待支付、20.订单超时、30.已取消、40.待确认、50.预定失败、60.待出行、100.已成交(已完成)、110.已关闭)
    public int refundState;//退款状态(1.退款中、2.退款失败、3.退款成功)(只有待出行才有退款状态)
    public long remainTime;//支付剩余时间单位秒
    public RouteOrderDetailBean.OrderBean mOrderBean;
    public RouteOrderDetailBean.Product mProduct;
    public RouteOrderDetailBean.Common common;
    /**
     * @param mRouteOrderDetail 线路订单
     */
    public void setData(RouteOrderDetailBean mRouteOrderDetail) {
        this.datas.clear();
        mOrderBean=mRouteOrderDetail.getHead();
        mProduct=mRouteOrderDetail.getItem().getProduct();
        orderState=mOrderBean.getOrderState();
        refundState=mOrderBean.getRefundState();
        remainTime=mOrderBean.getExpireTimestamp()/1000-mOrderBean.getCurrentTimestamp()/1000;
        this.datas.add(mOrderBean);
        common=mRouteOrderDetail.getItem().getCommon();
        common.setDepartDate(mOrderBean.getDepartDate());
        common.setOrderCreateTime(mOrderBean.getOrderCreateTime());
        this.datas.add(common);
        this.datas.add(new RouteDetailCategory("出行人信息",0));
        CostBean costAdult=mRouteOrderDetail.getItem().getProduct().getAdult();//成人费用
        if(costAdult!=null&&costAdult.getQuantity()>0){
            costAdult.setCostType(1);
            this.datas.add(costAdult);
            this.datas.addAll(costAdult.getVisitorList());
            endCrVisitorIndex=this.datas.size()-1;
        }

        CostBean costChildren=mRouteOrderDetail.getItem().getProduct().getChildren();//小孩费用
        if(costChildren!=null&&costChildren.getQuantity()>0){
            costChildren.setCostType(2);
            this.datas.add(costChildren);
            this.datas.addAll(costChildren.getVisitorList());
            endXhVisitorIndex=this.datas.size()-1;
        }

        CostBean costSingleRoom=mRouteOrderDetail.getItem().getProduct().getSingleRoom();//单房差费用
        if(costSingleRoom!=null&&costSingleRoom.getQuantity()>0){
            costSingleRoom.setCostType(3);
            this.datas.add(costSingleRoom);
            singleRoomIndex=this.datas.size()-1;
        }

        List<CarForm> carList=mRouteOrderDetail.getItem().getProduct().getCarList();//车辆信息
        if(carList.size()>0){
            this.datas.add(new RouteDetailCategory("车辆信息",0));
            this.datas.addAll(carList);
            endCarIndex=this.datas.size()-1;
        }
        ContactForm contact=mOrderBean.getContact();
        if(contact!=null){
            contact.setContact(true);
            this.datas.add(contact);
        }
        OrderInvoiceInfo invoice=mOrderBean.getInvoice();
        this.datas.add(invoice);
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        Object obj=getItem(position);
       if(obj instanceof RouteOrderDetailBean.OrderBean){
            return TYPE_ITEM_0;
        }else if(obj instanceof RouteOrderDetailBean.Common){
            return TYPE_ITEM_1;
        }else if(obj instanceof RouteDetailCategory){
           return TYPE_ITEM_2;
       }else if(obj instanceof CostBean){
           return TYPE_ITEM_3;
       }else if(obj instanceof ContactForm){
           ContactForm mContactForm= (ContactForm) obj;
           if(mContactForm.isContact()){
               return TYPE_ITEM_6;
           }else {
               return TYPE_ITEM_4;
           }
       }else if(obj instanceof CarForm){
           return TYPE_ITEM_5;
       }else if(obj instanceof OrderInvoiceInfo){
           if (orderState == 30 || orderState == 110 || refundState == 1 ){ // 30 已取消 110 已关闭 60出行状态中1退款中
               return TYPE_ITEM_NULL;
           }
           OrderInvoiceInfo invoice= (OrderInvoiceInfo) obj;
           if(invoice.getTitleType()>0){
               return TYPE_ITEM_7;
           }else {
               return TYPE_ITEM_7_1;
           }
       }
       return TYPE_ITEM_NULL;
    }
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("shyong","viewType="+viewType);
        ItemHolder holder=null;
        if(viewType==TYPE_ITEM_0){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_order_0, parent, false));
        }else if(viewType==TYPE_ITEM_1){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_order_1, parent, false));
        }else if(viewType==TYPE_ITEM_2){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_order_2, parent, false));
        }else if(viewType==TYPE_ITEM_3){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_order_3, parent, false));
        }else if(viewType==TYPE_ITEM_4){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_order_4, parent, false));
        }else if(viewType==TYPE_ITEM_5){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_order_5, parent, false));
        }else if(viewType==TYPE_ITEM_6){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_order_6, parent, false));
        }else if(viewType==TYPE_ITEM_7){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_order_7, parent, false));
        }else if(viewType==TYPE_ITEM_7_1){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_order_7_1, parent, false));
        }else if(viewType==TYPE_ITEM_NULL){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_null, parent, false));
        }
        return holder;
    }
    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.setViewDate(position);
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
    class ItemHolder<DB extends ViewDataBinding> extends RecyclerView.ViewHolder{
        private DB dbBinding;
        public ItemHolder(DB dbBinding) {
            super(dbBinding.getRoot());
            this.dbBinding = dbBinding;
            if(dbBinding instanceof ItemRouteOrder0Binding){
                ItemRouteOrder0Binding itemBinding= (ItemRouteOrder0Binding) this.dbBinding;
                itemBinding.tvCostDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            //费用明细
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
                itemBinding.tvRefundStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            //退款详情
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });

            }else if(dbBinding instanceof ItemRouteOrder1Binding){
                ItemRouteOrder1Binding itemBinding= (ItemRouteOrder1Binding) this.dbBinding;
                itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            //费用明细
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });
            }else if(dbBinding instanceof ItemRouteOrder7Binding){
                ItemRouteOrder7Binding itemBinding= (ItemRouteOrder7Binding) this.dbBinding;
                itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //发票详情
                        Object obj=getItem(getAdapterPosition());
                        if(obj instanceof OrderInvoiceInfo){
                            OrderInvoiceInfo orderInvoiceInfo= (OrderInvoiceInfo) obj;
                            Intent intent=new Intent();
                            intent.setClass(v.getContext(), InvoiceInfoActivity.class);
                            intent.putExtra("A",orderInvoiceInfo);//线路订单发票信息
                            intent.putExtra("B",mOrderBean.getOpenInvoice());//是否开发票(0:不需要发票,1:电子发票)
                            v.getContext().startActivity(intent);
                        }

                    }
                });
                itemBinding.tvSendEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemListener!=null){
                            //重新发送电子发票
                            itemListener.onItemClick(v,getAdapterPosition());
                        }
                    }
                });

            }else if(dbBinding instanceof ItemRouteOrder71Binding){
                ItemRouteOrder71Binding itemBinding= (ItemRouteOrder71Binding) this.dbBinding;
                itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //发票详情
                        Intent intent = new Intent();
                        intent.setClass(v.getContext(), RouteFillinInvoiceActivity.class);
                        intent.putExtra("A", 1);//线路订单填写发票
                        v.getContext().startActivity(intent);
                    }
                });
            }
        }
        private void setViewDate(int position){
            if(dbBinding instanceof ItemRouteOrder0Binding){
                ItemRouteOrder0Binding itemBinding= (ItemRouteOrder0Binding) this.dbBinding;
                Object obj=getItem(position);
                if(obj instanceof RouteOrderDetailBean.OrderBean){
                    RouteOrderDetailBean.OrderBean orderBean=(RouteOrderDetailBean.OrderBean) obj;
                    itemBinding.setItem(orderBean);
                    itemBinding.tvOrderId.setText("订单号："+orderBean.getOrderId());
                    String orderAmount=MathUtil.formatDouble(orderBean.getOrderAmount(),2);
                    itemBinding.tvPrice.setText(orderAmount);
                    if(orderState==0){
                        itemBinding.tvStatus.setText("初始化");
                        itemBinding.tvHint.setText("");
                    }else if(orderState==10){
                        itemBinding.tvStatus.setText("待支付");
                        itemBinding.tvHint.setText("订单将为您保留24小时，请尽快完成支付");//操作 继续支付，取消
                    }else if(orderState==20){
                        itemBinding.tvStatus.setText("订单超时");
                        itemBinding.tvHint.setText("您的订单由于超时未支付已自动取消，欢迎再次预定！");//操作 删除 再次预定
                    }else if(orderState==30){
                        itemBinding.tvStatus.setText("已取消");
                        itemBinding.tvHint.setText("您的订单已取消，欢迎再次预定！");//操作 删除 再次预定
                    }else if(orderState==40){
                        itemBinding.tvStatus.setText("待确认");
                        itemBinding.tvHint.setText("预定信息已提交，请等待客服确认，将在1小时内完成！");//操作 退款
                    }else if(orderState==50){
                        itemBinding.tvStatus.setText("预定失败");
                        itemBinding.tvHint.setText("抱歉，因库存不足，本次预订失败，退款将原路返回");//操作 再次预定
                    }else if(orderState==60){
                        itemBinding.tvStatus.setText("待出行");
                        itemBinding.tvHint.setText("请在规定时间内前往消费，祝您旅途愉快！");//操作 退款
                    }else if(orderState==100){
                        itemBinding.tvStatus.setText("已完成");
                        itemBinding.tvHint.setText("您的订单已完成，现在可以发表评价了哦。");//操作 删除 再次预定
                    }else if(orderState==110){
                        itemBinding.tvStatus.setText("已关闭");
                        itemBinding.tvHint.setText("您的退款申请已通过，退款金额将原路返回！");//操作 删除 再次预定
                    }
                    itemBinding.tvRefundStatus.setVisibility(View.VISIBLE);
                    if(refundState==1){ //退款状态(1.退款中、2.退款失败、3.退款成功)(只有待出行才有退款状态)
                        itemBinding.tvRefundStatus.setText("退款中");
                        itemBinding.tvHint.setText("您的退款申请已提交，客服会尽快处理！");
                    }else if(refundState==2){
                        itemBinding.tvRefundStatus.setText("退款失败");
                        itemBinding.tvHint.setText("您的退款申请未能通过，如有疑问，请联系客服！");
                    }else if(refundState==3){
                        itemBinding.tvRefundStatus.setText("退款成功");
                        itemBinding.tvHint.setText("您的退款申请已通过，退款金额将原路返回！");//操作 删除
                    }else {
                        itemBinding.tvRefundStatus.setVisibility(View.GONE);
                    }
                }
            }else if(dbBinding instanceof ItemRouteOrder1Binding){
                ItemRouteOrder1Binding itemBinding= (ItemRouteOrder1Binding) this.dbBinding;
                Object obj=getItem(position);
                if(obj instanceof RouteOrderDetailBean.Common){
                    itemBinding.setItem((RouteOrderDetailBean.Common) obj);
                }
            }else if(dbBinding instanceof ItemRouteOrder2Binding){
                ItemRouteOrder2Binding itemBinding= (ItemRouteOrder2Binding) this.dbBinding;
                Object obj=getItem(position);
                if(obj instanceof RouteDetailCategory){
                    itemBinding.setItem((RouteDetailCategory) obj);
                }
            }else if(dbBinding instanceof ItemRouteOrder3Binding){
                ItemRouteOrder3Binding itemBinding= (ItemRouteOrder3Binding) this.dbBinding;
                ViewGroup.LayoutParams spaceLayoutParams=itemBinding.vspace.getLayoutParams();
                Object obj=getItem(position);
                if(obj instanceof CostBean){
                    CostBean costBean=(CostBean) obj;
                    itemBinding.setItem(costBean);
                    if(costBean.getCostType()==3){
                        spaceLayoutParams.height=itemBinding.vspace.getResources().getDimensionPixelOffset(R.dimen.dp_14);
                        itemBinding.itemLayout.setBackgroundResource(R.drawable.route_list_item_bg_bottom_c_shape);
                    }else {
                        spaceLayoutParams.height=itemBinding.vspace.getResources().getDimensionPixelOffset(R.dimen.dp_10);
                        itemBinding.itemLayout.setBackgroundColor(Color.WHITE);
                    }
                }
            }else if(dbBinding instanceof ItemRouteOrder4Binding){
                ItemRouteOrder4Binding itemBinding= (ItemRouteOrder4Binding) this.dbBinding;
                Object obj=getItem(position);
                ViewGroup.LayoutParams spaceLayoutParams=itemBinding.vspace.getLayoutParams();
                itemBinding.viewLine.setBackgroundColor(Color.WHITE);
                if((endCrVisitorIndex==position&&endXhVisitorIndex<=0&&singleRoomIndex<=0)||(endXhVisitorIndex==position&&singleRoomIndex<=0)){
                    //判断是否是出行人信息的最后一条
                    spaceLayoutParams.height=itemBinding.vspace.getResources().getDimensionPixelOffset(R.dimen.dp_14);
                    itemBinding.itemLayout.setBackgroundResource(R.drawable.route_list_item_bg_bottom_c_shape);
                }else {
                    spaceLayoutParams.height=itemBinding.vspace.getResources().getDimensionPixelOffset(R.dimen.dp_10);
                    itemBinding.itemLayout.setBackgroundColor(Color.WHITE);
                    if(endCrVisitorIndex==position||endXhVisitorIndex==position){
                        itemBinding.viewLine.setBackgroundColor(Color.parseColor("#E5E5E5"));
                    }
                }
                if(obj instanceof ContactForm){
                    itemBinding.setItem((ContactForm) obj);
                }
            }else if(dbBinding instanceof ItemRouteOrder5Binding){
                ItemRouteOrder5Binding itemBinding= (ItemRouteOrder5Binding) this.dbBinding;
                Object obj=getItem(position);
                if(endCarIndex==position){
                    itemBinding.itemLayout.setBackgroundResource(R.drawable.route_list_item_bg_bottom_c_shape);
                }else {
                    itemBinding.itemLayout.setBackgroundColor(Color.WHITE);
                }
                if(obj instanceof CarForm){
                    itemBinding.setItem((CarForm) obj);
                }
            }else if(dbBinding instanceof ItemRouteOrder6Binding){
                ItemRouteOrder6Binding itemBinding= (ItemRouteOrder6Binding) this.dbBinding;
                Object obj=getItem(position);
                if(obj instanceof ContactForm){
                    itemBinding.setItem((ContactForm) obj);
                }
            }else if(dbBinding instanceof ItemRouteOrder7Binding){
                ItemRouteOrder7Binding itemBinding= (ItemRouteOrder7Binding) this.dbBinding;
                Object obj=getItem(position);
                if(obj instanceof OrderInvoiceInfo) {
                    itemBinding.setItem((OrderInvoiceInfo) obj);
                    if (((OrderInvoiceInfo) obj).getSendTimes() > 0) {
                        itemBinding.tvSendEmail.setText("重发电子发票");
                    } else {
                        itemBinding.tvSendEmail.setText("发送电子发票");
                    }
                }
            }
        }
    }
}