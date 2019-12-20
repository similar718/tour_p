package cn.xmzt.www.route.activity;

import androidx.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityRouteTimePersonsPickerBinding;
import cn.xmzt.www.dialog.RoutePickDetailDialog;
import cn.xmzt.www.route.adapter.RouteTimePersonsAdapter;
import cn.xmzt.www.route.bean.PersonCountBean;
import cn.xmzt.www.route.bean.TimePriceDayBean;
import cn.xmzt.www.route.bean.TimePriceMonthBean;
import cn.xmzt.www.route.vm.RouteTimePersonsViewModel;
import cn.xmzt.www.utils.DensityUtil;
import cn.xmzt.www.utils.MathUtil;
import cn.xmzt.www.view.listener.ItemListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 线路时间人数选择
 */
public class RouteTimePersonsActivity extends TourBaseActivity<RouteTimePersonsViewModel, ActivityRouteTimePersonsPickerBinding> implements ItemListener {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_route_time_persons_picker;
    }
    private List<TimePriceMonthBean> timePriceMonthList;
    @Override
    protected RouteTimePersonsViewModel createViewModel() {
        viewModel = new RouteTimePersonsViewModel();
        viewModel.setIView(this);
        viewModel.timePriceMonthList.observe(this, new Observer<List<TimePriceMonthBean>>() {
            @Override
            public void onChanged(@Nullable List<TimePriceMonthBean> timePriceMonthList) {
                if(timePriceMonthList!=null&&timePriceMonthList.size()>0){
                    RouteTimePersonsActivity.this.timePriceMonthList=timePriceMonthList;
                    String day=adapter.mPersonCountBean.getTime();
                    String latelyDayBuy="";
                    for(int i=0;i<timePriceMonthList.size();i++){
                        TimePriceMonthBean mTimePriceMonthBean=timePriceMonthList.get(i);
                        if(TextUtils.isEmpty(latelyDayBuy)){
                            latelyDayBuy=mTimePriceMonthBean.getLatelyDayBuy();
                        }
                        if(TextUtils.isEmpty(day)){
                            day=latelyDayBuy;
                        }else {
                            if(day.contains(mTimePriceMonthBean.getMonth())&&!mTimePriceMonthBean.isDateBuy(day)){
                                day=latelyDayBuy;
                                adapter.mPersonCountBean.setTime("");
                                ToastUtils.showShort("您之前选中的日期已不能购买");
                            }
                        }

                        if(i==0){
                            if(day.contains(mTimePriceMonthBean.getMonth())){
                                setSelectMonth(i);
                            }
                            dataBinding.tvMonth1.setText(mTimePriceMonthBean.getMonthOfYear());
                            dataBinding.tvPrice1.setText(mTimePriceMonthBean.getFloorPriceStr());
                        }else if(i==1){
                            if(day.contains(mTimePriceMonthBean.getMonth())){
                                setSelectMonth(i);
                            }
                            dataBinding.tvMonth2.setText(mTimePriceMonthBean.getMonthOfYear());
                            dataBinding.tvPrice2.setText(mTimePriceMonthBean.getFloorPriceStr());
                        }else if(i==2){
                            if(day.contains(mTimePriceMonthBean.getMonth())){
                                setSelectMonth(i);
                            }
                            dataBinding.tvMonth3.setText(mTimePriceMonthBean.getMonthOfYear());
                            dataBinding.tvPrice3.setText(mTimePriceMonthBean.getFloorPriceStr());
                        }else if(i==3){
                            if(day.contains(mTimePriceMonthBean.getMonth())){
                                setSelectMonth(i);
                            }
                            dataBinding.tvMonth4.setText(mTimePriceMonthBean.getMonthOfYear());
                            dataBinding.tvPrice4.setText(mTimePriceMonthBean.getFloorPriceStr());
                        }else if(i==4){
                            if(day.contains(mTimePriceMonthBean.getMonth())){
                                setSelectMonth(i);
                            }
                            dataBinding.tvMonth5.setText(mTimePriceMonthBean.getMonthOfYear());
                            dataBinding.tvPrice5.setText(mTimePriceMonthBean.getFloorPriceStr());
                        }else if(i==5){
                            if(day.contains(mTimePriceMonthBean.getMonth())){
                                setSelectMonth(i);
                            }
                            dataBinding.tvMonth6.setText(mTimePriceMonthBean.getMonthOfYear());
                            dataBinding.tvPrice6.setText(mTimePriceMonthBean.getFloorPriceStr());
                        }
                    }
                    if(TextUtils.isEmpty(day)){
                        setSelectMonth(0);
                    }
                    setAmount();
                }
            }
        });
        return viewModel;
    }
    RouteTimePersonsAdapter adapter =null;
    GridLayoutManager manager=null;
    private int routeId;
    private String lineName;
    private String photoUrl;
    private PersonCountBean mPersonCountBean=null;
    @Override
    protected void initData() {
        dataBinding.setActivity(this);
        Intent intent=getIntent();
        routeId=intent.getIntExtra("A",0);
        lineName=intent.getStringExtra("B");
        photoUrl=intent.getStringExtra("C");
        mPersonCountBean= (PersonCountBean) intent.getSerializableExtra("D");

        dataBinding.setVm(viewModel);
        adapter = new RouteTimePersonsAdapter();
        if(mPersonCountBean!=null){
            adapter.mPersonCountBean.setCrCount(mPersonCountBean.getCrCount());
            adapter.mPersonCountBean.setXhCount(mPersonCountBean.getXhCount());
            adapter.mPersonCountBean.setFangCount(mPersonCountBean.getFangCount());
            adapter.mPersonCountBean.setTime(mPersonCountBean.getTime());
            adapter.mPersonCountBean.setExt(mPersonCountBean.getExt());
        }
        adapter.setItemListener(this);
        dataBinding.setAdapter(adapter);
        manager= (GridLayoutManager) dataBinding.recyclerView.getLayoutManager();
        manager.setSpanCount(7);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                boolean isSingleShow=adapter.isSingleShow(position);
                return isSingleShow ? manager.getSpanCount() : 1;
            }
        });
        dataBinding.recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int padding = DensityUtil.dip2px(getApplicationContext(), 10);
                int childAdapterPosition = parent.getChildAdapterPosition(view);
                if (childAdapterPosition >= 0&&childAdapterPosition<7) {
                    outRect.top = padding;
                }
            }
        });
        viewModel.getLineTimePrices(routeId);
    }

    /**
     * 设置选择月
     * @param index
     */
    private void setSelectMonth(int index){
        dataBinding.tvMonth1.setSelected(false);
        dataBinding.tvMonth2.setSelected(false);
        dataBinding.tvMonth3.setSelected(false);
        dataBinding.tvMonth4.setSelected(false);
        dataBinding.tvMonth5.setSelected(false);
        dataBinding.tvMonth6.setSelected(false);

        dataBinding.tvPrice1.setSelected(false);
        dataBinding.tvPrice2.setSelected(false);
        dataBinding.tvPrice3.setSelected(false);
        dataBinding.tvPrice4.setSelected(false);
        dataBinding.tvPrice5.setSelected(false);
        dataBinding.tvPrice6.setSelected(false);

        dataBinding.tvLine1.setVisibility(View.GONE);
        dataBinding.tvLine2.setVisibility(View.GONE);
        dataBinding.tvLine3.setVisibility(View.GONE);
        dataBinding.tvLine4.setVisibility(View.GONE);
        dataBinding.tvLine5.setVisibility(View.GONE);
        dataBinding.tvLine6.setVisibility(View.GONE);
        if(timePriceMonthList!=null&&index<timePriceMonthList.size()){
            TimePriceMonthBean mTimePriceMonthBean=timePriceMonthList.get(index);
            adapter.setData(mTimePriceMonthBean,viewModel.currentTime);
        }
        if(index==0){
            dataBinding.tvMonth1.setSelected(true);
            dataBinding.tvPrice1.setSelected(true);
            dataBinding.tvLine1.setVisibility(View.VISIBLE);
        }else if(index==1){
            dataBinding.tvMonth2.setSelected(true);
            dataBinding.tvPrice2.setSelected(true);
            dataBinding.tvLine2.setVisibility(View.VISIBLE);
        }else if(index==2){
            dataBinding.tvMonth3.setSelected(true);
            dataBinding.tvPrice3.setSelected(true);
            dataBinding.tvLine3.setVisibility(View.VISIBLE);
        }else if(index==3){
            dataBinding.tvMonth4.setSelected(true);
            dataBinding.tvPrice4.setSelected(true);
            dataBinding.tvLine4.setVisibility(View.VISIBLE);
        }else if(index==4){
            dataBinding.tvMonth5.setSelected(true);
            dataBinding.tvPrice5.setSelected(true);
            dataBinding.tvLine5.setVisibility(View.VISIBLE);
        }else if(index==5){
            dataBinding.tvMonth6.setSelected(true);
            dataBinding.tvPrice6.setSelected(true);
            dataBinding.tvLine6.setVisibility(View.VISIBLE);
        }
    }

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
                if(!TextUtils.isEmpty(adapter.mPersonCountBean.getTime())){
                    toRouteFillinOrderActivity();
                }else {
                    ToastUtils.showShort("暂无可选日期");
                }
                break;
            case R.id.layout_month1:
                setSelectMonth(0);
                break;
            case R.id.layout_month2:
                setSelectMonth(1);
                break;
            case R.id.layout_month3:
                setSelectMonth(2);
                break;
            case R.id.layout_month4:
                setSelectMonth(3);
                break;
            case R.id.layout_month5:
                setSelectMonth(4);
                break;
            case R.id.layout_month6:
                setSelectMonth(5);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(adapter.mPersonCountBean);
    }

    private void toRouteFillinOrderActivity(){
        Intent intent = new Intent(this, RouteFillinOrderActivity.class);
        intent.putExtra("A",routeId);
        intent.putExtra("B",lineName);
        intent.putExtra("C",photoUrl);
        int count=adapter.mPersonCountBean.getCrCount();
        if(count>0){
            intent.putExtra("D",adapter.mPersonCountBean);
            startActivity(intent);
            onBackPressed();
        }else {
            ToastUtils.showShort("请选择出发日期和人数");
        }
    }
    private RoutePickDetailDialog pickDetailDialog;
    private void showPickDetailDialog(){
        if(pickDetailDialog==null){
            pickDetailDialog=new RoutePickDetailDialog(RouteTimePersonsActivity.this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickDetailDialog.cancel();
                    if (v.getId()==R.id.tv_reserve){
                        toRouteFillinOrderActivity();
                    }
                }
            });
        }
        TimePriceDayBean.Ext ext= adapter.mPersonCountBean.getExt();
        pickDetailDialog.setViewData(ext,adapter.mPersonCountBean,0,null);
        pickDetailDialog.show();
    }
    public void setAmount() {
        TimePriceDayBean.Ext ext= adapter.mPersonCountBean.getExt();
        if(ext!=null){
            double amount=adapter.mPersonCountBean.getCrCount()*ext.getJcrPrice()
                    +adapter.mPersonCountBean.getXhCount()*ext.getJxhPrice()
                    +adapter.mPersonCountBean.getFangCount()*ext.getFangPrice();

            dataBinding.tvAmount.setText(MathUtil.formatDouble(amount,2));
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        int vId=view.getId();
        switch (vId) {
            case R.id.btn_cr_add :{
                int crCount=adapter.mPersonCountBean.getCrCount();
                int xhCount=adapter.mPersonCountBean.getXhCount();
                long maxPurchase=adapter.mPersonCountBean.getMaxPurchase();
                if(crCount+xhCount<maxPurchase){
                    adapter.mPersonCountBean.setCrCount(crCount+1);
                    adapter.notifyItemChanged(adapter.getItemCount()-1);
                    setAmount();
                }else {
                    ToastUtils.showShort("单次不能购买太多哦！");
                }
                break;
            }
            case R.id.btn_cr_minus :{
                int xhCount=adapter.mPersonCountBean.getXhCount();
                int crCount=adapter.mPersonCountBean.getCrCount();
                int fangCount=adapter.mPersonCountBean.getFangCount();
                if(crCount>1){
                    if(fangCount>=xhCount+crCount){
                        adapter.mPersonCountBean.setFangCount(fangCount-1);
                    }
                    adapter.mPersonCountBean.setCrCount(crCount-1);
                    adapter.notifyItemChanged(adapter.getItemCount()-1);
                    setAmount();
                }
                break;
            }
            case R.id.btn_xh_add :{
                int crCount=adapter.mPersonCountBean.getCrCount();
                int xhCount=adapter.mPersonCountBean.getXhCount();
                long maxPurchase=adapter.mPersonCountBean.getMaxPurchase();
                if(crCount+xhCount<maxPurchase){
                    adapter.mPersonCountBean.setXhCount(xhCount+1);
                    adapter.notifyItemChanged(adapter.getItemCount()-1);
                    setAmount();
                }else {
                    ToastUtils.showShort("单次不能购买太多哦！");
                }
                break;
            }
            case R.id.btn_xh_minus :{
                int xhCount=adapter.mPersonCountBean.getXhCount();
                int crCount=adapter.mPersonCountBean.getCrCount();
                int fangCount=adapter.mPersonCountBean.getFangCount();
                if(xhCount>0){
                    if(fangCount>=xhCount+crCount){
                        adapter.mPersonCountBean.setFangCount(fangCount-1);
                    }
                    adapter.mPersonCountBean.setXhCount(xhCount-1);
                    adapter.notifyItemChanged(adapter.getItemCount()-1);
                    setAmount();
                }
                break;
            }
            case R.id.btn_fang_add :{
                int xhCount=adapter.mPersonCountBean.getXhCount();
                int crCount=adapter.mPersonCountBean.getCrCount();
                int count=adapter.mPersonCountBean.getFangCount();
                if(count<xhCount+crCount){
                    adapter.mPersonCountBean.setFangCount(count+1);
                    adapter.notifyItemChanged(adapter.getItemCount()-1);
                    setAmount();
                }else {
                   ToastUtils.showShort("单房差选择上限不能大于成人+儿童总和");
                }
                break;
            }
            case R.id.btn_fang_minus :{
                int count=adapter.mPersonCountBean.getFangCount();
                if(count>0){
                    adapter.mPersonCountBean.setFangCount(count-1);
                    adapter.notifyItemChanged(adapter.getItemCount()-1);
                    setAmount();
                }
                break;
            }
        }
    }
}
