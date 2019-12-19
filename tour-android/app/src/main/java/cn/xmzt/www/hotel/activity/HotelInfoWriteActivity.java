package cn.xmzt.www.hotel.activity;

import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityHotelInfoWriteBinding;
import cn.xmzt.www.dialog.TextTitleDialog;
import cn.xmzt.www.hotel.model.HotelInfoWriteViewModel;

/**
 * @author tanlei
 * @date 2019/7/31
 * @describe 酒店信息填写界面
 */

public class HotelInfoWriteActivity extends TourBaseActivity<HotelInfoWriteViewModel, ActivityHotelInfoWriteBinding> {
    TextTitleDialog dialog;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_hotel_info_write;
    }

    @Override
    protected HotelInfoWriteViewModel createViewModel() {
        return new HotelInfoWriteViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        dataBinding.setActivity(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_submit_order:
                startToActivity(HotelOrderActivity.class);
                break;
            case R.id.iv_select_tourists:
                startToActivity(SelectTouristsActivity.class);
                break;
            case R.id.iv_mail_list:
                dialog = new TextTitleDialog(this, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.setTitle("允许【自驾游】使用通讯录");
                dialog.show();
                break;
            default:
                break;
        }
    }
}
