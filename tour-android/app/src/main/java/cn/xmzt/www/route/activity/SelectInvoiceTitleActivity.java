package cn.xmzt.www.route.activity;

import androidx.lifecycle.Observer;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.bean.InvoiceTitleBean;
import cn.xmzt.www.bean.TypeEventBus;
import cn.xmzt.www.databinding.ActivitySelectInvoiceTitleBinding;
import cn.xmzt.www.route.adapter.InvoiceTitleAdapter;
import cn.xmzt.www.route.vm.SelectInvoiceTitleViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * @describe 选择发票抬头
 */
public class SelectInvoiceTitleActivity extends TourBaseActivity<SelectInvoiceTitleViewModel, ActivitySelectInvoiceTitleBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_select_invoice_title;
    }

    @Override
    protected SelectInvoiceTitleViewModel createViewModel() {
        viewModel = new SelectInvoiceTitleViewModel();
        viewModel.invoiceTitleList.observe(this, new Observer<List<InvoiceTitleBean>>() {
            @Override
            public void onChanged(@Nullable List<InvoiceTitleBean> result) {
                if(result!=null){
                    adapter.setDatas(result);
                }
            }
        });
        return viewModel;
    }
    InvoiceTitleAdapter adapter =null;
    GridLayoutManager manager=null;
    private int type=0;//1表示线路订单填写发票
    @Override
    protected void initData() {
        Intent intent=getIntent();
        type=intent.getIntExtra("A",0);
        EventBus.getDefault().register(this);
        dataBinding.setActivity(this);
        viewModel.getInvoiceTitleList();
        manager= (GridLayoutManager) dataBinding.recyclerView.getLayoutManager();
        manager.setSpanCount(1);
        adapter = new InvoiceTitleAdapter();
        dataBinding.recyclerView.setAdapter(adapter);
    }
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void refreshList(@NonNull TypeEventBus typeEventBus){
        if(typeEventBus.getType()==TypeEventBus.TYPE_REFRESH){
            viewModel.getInvoiceTitleList();
        }
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_confirm:
                //选择确认
                InvoiceTitleBean mInvoiceTitleBean=adapter.getItem(adapter.selectPosition);
                if(mInvoiceTitleBean!=null){
                    EventBus.getDefault().post(mInvoiceTitleBean);
                    onBackPressed();
                }else {
                    ToastUtils.showShort("请先选择发票抬头");
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
