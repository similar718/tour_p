package cn.xmzt.www.mine.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivitySettingPayPwdBinding;
import cn.xmzt.www.mine.event.ChangeSecurityEvent;
import cn.xmzt.www.mine.model.SettingPayPwdViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 设置或修改支付密码
 */
public class SettingPayPwdActivity extends TourBaseActivity<SettingPayPwdViewModel, ActivitySettingPayPwdBinding> {
    private boolean setOrChange;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_setting_pay_pwd;
    }

    @Override
    protected SettingPayPwdViewModel createViewModel() {
        return new SettingPayPwdViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
        if (null != getIntent()) {
            setOrChange = getIntent().getBooleanExtra("setOrChange", false);
        }
        viewModel.setSetOrChange(setOrChange);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setSecurityPhoneNumber(ChangeSecurityEvent event) {
        dataBinding.etPayPhone.setText(event.getPhoneNumber());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    //    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_setting_pay_pwd);
//        ButterKnife.bind(this);
//    }
}
