package cn.xmzt.www.route.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.bean.OftenVisitorsBean;
import cn.xmzt.www.databinding.ActivityRouteFillInOrderBinding;
import cn.xmzt.www.dialog.RoutePickDetailDialog;
import cn.xmzt.www.dialog.SelectCarTypeDialog;
import cn.xmzt.www.mine.bean.CommonVehicleBean;
import cn.xmzt.www.mine.bean.MyCouponBean;
import cn.xmzt.www.mine.event.UpdateVehicleEvent;
import cn.xmzt.www.pay.activity.ChoosePayActivity;
import cn.xmzt.www.route.adapter.RouteFillinOrderAdapter;
import cn.xmzt.www.route.bean.CarForm;
import cn.xmzt.www.route.bean.LineOrderForm;
import cn.xmzt.www.route.bean.OrderInvoiceForm;
import cn.xmzt.www.route.bean.PersonCountBean;
import cn.xmzt.www.route.bean.TimePriceMonthBean;
import cn.xmzt.www.route.eventbus.MyCouponBus;
import cn.xmzt.www.route.eventbus.SelectVisitorsBus;
import cn.xmzt.www.route.vm.RouteFillinOrderViewModel;
import cn.xmzt.www.rxjava2.AsyncUtil;
import cn.xmzt.www.utils.MathUtil;
import cn.xmzt.www.view.listener.ItemListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 线路填写订单
 */
public class RouteFillinOrderActivity extends TourBaseActivity<RouteFillinOrderViewModel, ActivityRouteFillInOrderBinding> implements ItemListener {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_route_fill_in_order;
    }
    private List<TimePriceMonthBean> timePriceMonthList;
    String orderCode=null;
    @Override
    protected RouteFillinOrderViewModel createViewModel() {
        viewModel = new RouteFillinOrderViewModel();
        viewModel.result.observe(this, new Observer<BaseDataBean<String>>() {
            @Override
            public void onChanged(@Nullable BaseDataBean<String> result) {
                orderCode=result.getRel();
                //调到支付页面
                Intent intent=new Intent(RouteFillinOrderActivity.this, ChoosePayActivity.class);
                intent.putExtra("A",orderCode);
                startActivity(intent);
                finish();
            }
        });
        /**
         * 监听优惠券
         */
        viewModel.couponList.observe(this, new Observer<List<MyCouponBean>>() {
            @Override
            public void onChanged(@Nullable List<MyCouponBean> result) {
                adapter.tourInsurance=viewModel.tourInsurance;
                AsyncUtil.async(new Function<String,List<MyCouponBean>>() {
                    @Override
                    public List<MyCouponBean> apply(String o) throws Exception {
                        List<MyCouponBean> result1=new ArrayList<>();
                        double amount=adapter.getAmount();
                        for(int i=0;i<result.size();i++){
                            MyCouponBean myCouponBean=result.get(i);
                            if(amount>=myCouponBean.getMinConsume()){
                                myCouponBean.setUsable(true);
                                result1.add(myCouponBean);
                            }
                        }
                        Collections.sort(result1, new Comparator<MyCouponBean>() {
                            public int compare(MyCouponBean arg0, MyCouponBean arg1) {
                                if(arg0.getMaxSubtract()<arg1.getMaxSubtract()){
                                    return 1;
                                }else {
                                    return -1;
                                }
                            }
                        });
                        return result1;
                    }
                }, new Consumer<List<MyCouponBean>>() {
                    @Override
                    public void accept(List<MyCouponBean> list) throws Exception {
                        if(list.size()>0){
                            MyCouponBean myCouponBean=list.get(0);
                            if(myCouponBean!=null){
                                yhqPrice=myCouponBean.getMaxSubtract();
                            }else {
                                yhqPrice=0;
                            }
                            adapter.setMyCouponBean(myCouponBean,list.size());
                            setAmount();
                        }else {
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
        viewModel.mCarInfo.observe(this, new Observer<CommonVehicleBean>() {
            @Override
            public void onChanged(CommonVehicleBean commonVehicleBean) {
                if (commonVehicleBean != null) {
                    if (!TextUtils.isEmpty(commonVehicleBean.getPlateNumber())) {
                        CarForm carForm = new CarForm();
                        carForm.setCarNumber(commonVehicleBean.getPlateNumber());
                        adapter.setMyCarBean(carForm);
                    }
                }
            }
        });
        return viewModel;
    }
    RouteFillinOrderAdapter adapter =null;
    GridLayoutManager manager=null;
    private int routeId;//线路id
    private String lineName;//线路名称
    private String photoUrl;//线路方面图片
    public PersonCountBean mPersonCountBean=null;//选择的时间和人数
    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        dataBinding.setActivity(this);
        Intent intent=getIntent();
        routeId=intent.getIntExtra("A",0);
        lineName=intent.getStringExtra("B");
        photoUrl=intent.getStringExtra("C");
        mPersonCountBean= (PersonCountBean) intent.getSerializableExtra("D");
        dataBinding.setVm(viewModel);
        adapter = new RouteFillinOrderAdapter();
        adapter.setItemListener(this);
        dataBinding.setAdapter(adapter);
        adapter.setKeyboardLayout(dataBinding.keyboardLayout);
        adapter.setData(lineName,photoUrl,mPersonCountBean);
        setAmount();
        manager= (GridLayoutManager) dataBinding.recyclerView.getLayoutManager();
        manager.setSpanCount(1);
        dataBinding.recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                adapter.hideSoftKeyboardUtil();
                return false;
            }
        });
        viewModel.getData(routeId);
        viewModel.getSysUserPlateSelectDefault();
    }

    /**
     * 接收选择出游人的EventBus
     * @param selectVisitorsBus
     */
    @Subscribe
    public void selectVisitorsBus(SelectVisitorsBus selectVisitorsBus){
        List<OftenVisitorsBean> list=selectVisitorsBus.getSelectList();
        if(list!=null&&list.size()>0){
            if(selectVisitorsBus.getType()==1){//选出的出游人
                //排序
                Collections.sort(list, new Comparator<OftenVisitorsBean>() {
                    public int compare(OftenVisitorsBean arg0, OftenVisitorsBean arg1) {
                        if(arg0.isChildren()&&!arg1.isChildren()){
                            return 1;
                        }else {
                            return -1;
                        }
                    }
                });
                adapter.selectOftenVisitorsList(list);
            }else if(selectVisitorsBus.getType()==2){//选出的预订人
                adapter.selectOftenVisitors(list.get(0));
            }
            adapter.mPersonCountBean.setBigAgeCount(0);
        }
        setAmount();
    }
    @Subscribe
    public void selectOrderInvoiceForm(OrderInvoiceForm mOrderInvoiceForm){
        if(mOrderInvoiceForm.getInvoiceTitleId()>0){
            adapter.setOrderInvoiceForm(mOrderInvoiceForm);
        }
    }
    @Subscribe
    public void selectMyCoupon(MyCouponBus myCouponBus){
        if(myCouponBus!=null){
            MyCouponBean myCouponBean=myCouponBus.getMyCouponBean();
            if(myCouponBean!=null){
                yhqPrice=myCouponBean.getMaxSubtract();
            }else {
                yhqPrice=0;
            }
            adapter.setMyCouponBean(myCouponBus.getMyCouponBean(),myCouponBus.getSize());
            setAmount();
        }
    }
    private double yhqPrice;
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_detail:
                //选择明显
                showPickDetailDialog();
                break;
            case R.id.tv_reserve:
                //立即预订
                submitOrder();
                break;
            default:
                break;
        }
    }

    private void submitOrder(){
        LineOrderForm mLineOrderForm=adapter.getLineOrderForm();
        if(mLineOrderForm!=null){
            mLineOrderForm.setLineId(routeId);
            viewModel.postOrderLine(mLineOrderForm);
        }
    }
    private RoutePickDetailDialog pickDetailDialog;
    private void showPickDetailDialog(){
        if(pickDetailDialog==null){
            pickDetailDialog=new RoutePickDetailDialog(RouteFillinOrderActivity.this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickDetailDialog.cancel();
                    if (v.getId()==R.id.tv_reserve){
                        submitOrder();
                    }
                }
            });
        }
        pickDetailDialog.setViewData(adapter.mPersonCountBean.getExt(),adapter.mPersonCountBean,yhqPrice,adapter.tourInsurance);
        pickDetailDialog.show();
    }
    public void setAmount() {
        double amount=adapter.getAmount();
        dataBinding.tvAmount.setText(MathUtil.formatDouble(amount-yhqPrice,2));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onItemClick(View view, int adapterPosition) {
        if(view.getId()==R.id.car_type_layout){
            Object obj = adapter.getItem(adapterPosition);
            if (obj instanceof CarForm) {
                CarForm mCarForm = (CarForm) obj;
                showSelectCarTypeDialog(mCarForm,adapterPosition);
            }
        }else if(view.getId()==R.id.iv_check_insurance){
            setAmount();
        }
    }
    private SelectCarTypeDialog carTypeDialog;
    private CarForm selectCarTypeCarForm;
    private int adapterPosition;
    private void showSelectCarTypeDialog(CarForm mCarForm,int adapterPosition){
        selectCarTypeCarForm=mCarForm;
        this.adapterPosition=adapterPosition;
        if(carTypeDialog==null){
            carTypeDialog=new SelectCarTypeDialog(this);
            carTypeDialog.setItemListener(new ItemListener() {
                @Override
                public void onItemClick(View view, int position) {
                    selectCarTypeCarForm.setNumberType(position);
                    carTypeDialog.setSelectPosition(position);
                    adapter.notifyItemChanged(RouteFillinOrderActivity.this.adapterPosition);
                    carTypeDialog.cancel();
                }
            });
        }
        carTypeDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void LocationShareStatusEventBus(UpdateVehicleEvent event) { // 添加车辆
        if (event.getType() == 3){ // 添加车辆信息 调用车辆添加接口
            List<CarForm> list = new ArrayList<>();
            for (int i = 0 ; i < event.getLicencePlate().size(); i++){
                CarForm info = new CarForm();
                info.setCarNumber(event.getLicencePlate().get(i).getPlateNumber());
                list.add(info);
            }
            adapter.setMyCarBeanList(list);
        }
    }
}
