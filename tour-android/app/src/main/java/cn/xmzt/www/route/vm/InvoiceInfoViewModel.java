package cn.xmzt.www.route.vm;

import androidx.databinding.ObservableInt;

import cn.xmzt.www.base.BaseViewModel;

/**
 * 路线填写发票ViewModel
 */
public class InvoiceInfoViewModel extends BaseViewModel {
    /**
     * 是否开发票(0:不需要发票,1:电子发票)
     */
    public ObservableInt openInvoice= new ObservableInt(1);
    public String typeInvoice1= "不需要发票";
    public String typeInvoice2 = "电子发票";
}
