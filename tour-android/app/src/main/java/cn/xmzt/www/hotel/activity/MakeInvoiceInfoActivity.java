package cn.xmzt.www.hotel.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityMakeInvoiceInfoBinding;
import cn.xmzt.www.hotel.model.MakeInvoiceInfoViewModel;

/**
 * @author tanlei
 * @date 2019/8/1
 * @describe 开票信息界面
 */

public class MakeInvoiceInfoActivity extends TourBaseActivity<MakeInvoiceInfoViewModel,ActivityMakeInvoiceInfoBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_make_invoice_info;
    }

    @Override
    protected MakeInvoiceInfoViewModel createViewModel() {
        return new MakeInvoiceInfoViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
    }
}
