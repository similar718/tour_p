package cn.xmzt.www.route.activity;

import android.content.Intent;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityCouponUseBinding;
import cn.xmzt.www.mine.bean.MyCouponBean;
import cn.xmzt.www.route.adapter.CouponUseAdapter;
import cn.xmzt.www.route.eventbus.MyCouponBus;
import cn.xmzt.www.route.vm.CouponUseViewModel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

/**
 * @describe 优惠券使用界面
 */
public class CouponUseActivity extends TourBaseActivity<CouponUseViewModel, ActivityCouponUseBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_coupon_use;
    }

    @Override
    protected CouponUseViewModel createViewModel() {
        viewModel = new CouponUseViewModel();
        viewModel.couponList.observe(this, new Observer<List<MyCouponBean>>() {
            @Override
            public void onChanged(@Nullable List<MyCouponBean> result) {
                if(result.size()<pageSize){
                    isNextPage=false;
                }else {
                    isNextPage=true;
                }
                List<MyCouponBean> result1=new ArrayList<>();
                List<MyCouponBean> result2=new ArrayList<>();
                for(int i=0;i<result.size();i++){
                    MyCouponBean myCouponBean=result.get(i);
                    if(amount>=myCouponBean.getMinConsume()){
                        myCouponBean.setUsable(true);
                        result1.add(myCouponBean);
                        if(myCouponBean.getCouponId()==couponId){
                            adapter.selectPosition=result1.size();
                        }
                    }else {
                        myCouponBean.setUsable(false);
                        result2.add(myCouponBean);
                    }
                }
                if (isRefresh){
                    pageNum=1;
                    dataBinding.refreshLayout.finishRefresh();
                    adapter.setDatas(result1,result2);
                }else {
                    dataBinding.refreshLayout.finishLoadMore();
                    if(result.size()>0){
                        pageNum=pageNum+1;
                        adapter.addDatas(result1,result2);
                    }
                }
                dataBinding.refreshLayout.setNoMoreData(!isNextPage);
                if (adapter.getItemCount()>0) {
                    dataBinding.tvNoData.setVisibility(View.GONE);
                    dataBinding.tvConfirm.setVisibility(View.VISIBLE);
                    dataBinding.recyclerView.setVisibility(View.VISIBLE);
                } else {
                    dataBinding.tvNoData.setVisibility(View.VISIBLE);
                    dataBinding.tvConfirm.setVisibility(View.GONE);
                    dataBinding.recyclerView.setVisibility(View.GONE);
                }
            }
        });
        return viewModel;
    }
    private int pageNum=1;
    private int pageSize=15;
    private int type;// 优惠券类型 (0:通用,1:线路,2:酒店,3:门票)
    private double amount;// 优惠券类型 (0:通用,1:线路,2:酒店,3:门票)
    //是否是刷新
    boolean isRefresh = true;
    boolean isNextPage = true;

    CouponUseAdapter adapter =null;
    GridLayoutManager manager=null;
    private int couponId=0;
    @Override
    protected void initData() {
        Intent intent=getIntent();
        type=intent.getIntExtra("A",0);
        amount=intent.getDoubleExtra("B",0);
        couponId=intent.getIntExtra("C",0);
        dataBinding.setActivity(this);
        manager= (GridLayoutManager) dataBinding.recyclerView.getLayoutManager();
        manager.setSpanCount(1);
        adapter = new CouponUseAdapter();
        dataBinding.recyclerView.setAdapter(adapter);
        viewModel.getUseCouponList(1,pageSize,type);
        dataBinding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isRefresh=false;
                if(viewModel!=null){
                    viewModel.getUseCouponList(pageNum+1,pageSize,type);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh=true;
                if(viewModel!=null){
                    if(viewModel!=null){
                        viewModel.getUseCouponList(1,pageSize,type);
                    }
                }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_confirm:
                if(adapter.selectPosition>=0){
                    MyCouponBean myCouponBean=adapter.getItem(adapter.selectPosition);
                    EventBus.getDefault().post(new MyCouponBus(adapter.usableSize,myCouponBean));
                    finish();
                }else {
                    ToastUtils.showShort("请先选择优惠券");
                }
                break;
            default:
                break;
        }
    }
}
