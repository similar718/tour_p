package cn.xmzt.www.route.activity;

import androidx.lifecycle.Observer;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.bean.CouponBean;
import cn.xmzt.www.databinding.ActivityCouponGetBinding;
import cn.xmzt.www.mine.event.RefreshOrderEvent;
import cn.xmzt.www.route.adapter.CouponGetAdapter;
import cn.xmzt.www.route.vm.CouponGetViewModel;
import cn.xmzt.www.view.listener.ItemListener;

import java.util.List;

/**
 * @describe 优惠券领取界面
 */
public class CouponGetActivity extends TourBaseActivity<CouponGetViewModel, ActivityCouponGetBinding> implements ItemListener {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_coupon_get;
    }

    @Override
    protected CouponGetViewModel createViewModel() {
        viewModel = new CouponGetViewModel();
        viewModel.couponList.observe(this, new Observer<List<CouponBean>>() {
            @Override
            public void onChanged(@Nullable List<CouponBean> result) {
                if(result!=null&&result.size()>0){
                    dataBinding.tvNoData.setVisibility(View.GONE);
                    adapter.setDatas(result);
                }
            }
        });
        return viewModel;
    }
    private int type;// 优惠券类型 (0:通用,1:线路,2:酒店,3:门票)
    CouponGetAdapter adapter =null;
    GridLayoutManager manager=null;
    @Override
    protected void initData() {
        Intent intent=getIntent();
        type=intent.getIntExtra("A",0);
        dataBinding.setActivity(this);
        manager= (GridLayoutManager) dataBinding.recyclerView.getLayoutManager();
        manager.setSpanCount(1);
        adapter = new CouponGetAdapter();
        adapter.setItemListener(this);
        dataBinding.recyclerView.setAdapter(adapter);
        viewModel.getCouponList(type);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }
    @Override
    public void onItemClick(View view, int position) {
        CouponBean item=adapter.getItem(position);
        viewModel.getCoupon(item.getId(),type);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(new RefreshOrderEvent());
    }
}
