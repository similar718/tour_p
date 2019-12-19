package cn.xmzt.www.hotel.activity;

import android.view.Gravity;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityHotelOrderBinding;
import cn.xmzt.www.hotel.model.HotelOrderViewModel;
import cn.xmzt.www.popup.AgainSendBillPopupWindow;
import cn.xmzt.www.popup.CostDetailsPopupWindow;

/**
 * @author tanlei
 * @date 2019/8/1
 * @describe 酒店订单界面(待支付，已完成，待出行共用此界面)
 */

public class HotelOrderActivity extends TourBaseActivity<HotelOrderViewModel, ActivityHotelOrderBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_hotel_order;
    }

    @Override
    protected HotelOrderViewModel createViewModel() {
        return new HotelOrderViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        dataBinding.setActivity(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_again_bill:
                AgainSendBillPopupWindow popupWindow = new AgainSendBillPopupWindow(this);
                popupWindow.showAtLocation(dataBinding.rlLayout, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.ll_draw_bill:
                startToActivity(MakeInvoiceInfoActivity.class);
                break;
            case R.id.iv_cost_details:
                CostDetailsPopupWindow popupWindow1 = new CostDetailsPopupWindow(this);
                popupWindow1.showAtLocation(dataBinding.rlLayout, Gravity.BOTTOM, 0, 0);
                break;
            default:
                break;
        }
    }
}
