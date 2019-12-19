package cn.xmzt.www.mine.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityAccountSecurityBinding;
import cn.xmzt.www.mine.event.SecurityEvent;
import cn.xmzt.www.mine.model.AccountSecurityViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 账号安全界面
 */
public class AccountSecurityActivity extends TourBaseActivity<AccountSecurityViewModel, ActivityAccountSecurityBinding> {
    public static final String TAG = "AccountSecurityActivity";

    @Override
    protected int setLayoutId() {
        return R.layout.activity_account_security;
    }

    @Override
    protected AccountSecurityViewModel createViewModel() {
        return new AccountSecurityViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
        viewModel.accountSecurity();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void set(SecurityEvent event) {
        viewModel.accountSecurity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    //    @BindView(R.id.title_name_tv)
//    TextView nameTv;
//
//    private Context mContext;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_account_security);
//        ButterKnife.bind(this);
//        init();
//    }
//
//
//
//    @OnClick({R.id.title_back_iv, R.id.bind_phone_rl, R.id.bind_contact_rl, R.id.modification_login_pwd_rl,
//            R.id.modification_pay_pwd_rl, R.id.close_account_rl})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.title_back_iv://返回
//                finish();
//                break;
//
//            case R.id.bind_phone_rl://绑定手机号
//                startActivity(new Intent(AccountSecurityActivity.this, ChangePhoneActivity.class));
//                break;
//
//            case R.id.bind_contact_rl://绑定社交号
//                startActivity(new Intent(AccountSecurityActivity.this,BindContactActivity.class));
//                break;
//
//            case R.id.modification_login_pwd_rl://修改登录密码
//                startActivity(new Intent(AccountSecurityActivity.this,ModificationLoginPwdActivity.class));
//                break;
//
//
//            case R.id.modification_pay_pwd_rl://设置支付密码
//                startActivity(new Intent(AccountSecurityActivity.this,ModificationPayPwdActivity.class));
//                break;
//
//            case R.id.close_account_rl://永久注销账号
//                startActivity(new Intent(AccountSecurityActivity.this,CloseAccountActivity.class));
//                break;
//
//        }
//    }
//
//    private void init(){
//        mContext = this;
//        nameTv.setText("账号与安全");
//    }
}
