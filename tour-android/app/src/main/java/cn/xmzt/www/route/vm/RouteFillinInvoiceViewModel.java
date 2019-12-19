package cn.xmzt.www.route.vm;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import android.text.Editable;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;

/**
 * 路线填写发票ViewModel
 */
public class RouteFillinInvoiceViewModel extends BaseViewModel {
    /**
     * 1 不需要
     * 2 电子发票
     */
    public ObservableInt selected= new ObservableInt(1);
    public ObservableField<String> email= new ObservableField("");

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_unneed:
                selected.set(1);
                break;
            case R.id.tv_e_invoice:
                selected.set(2);
                break;
            case R.id.tv_submit:

                break;
            default:
                break;
        }
    }

    public void afterTextChanged(Editable s) {
        email.set(s.toString().trim());
    }
}
