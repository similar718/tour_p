package cn.xmzt.www.mine.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityVerificationSecurityPhoneBinding;
import cn.xmzt.www.mine.model.VerificationSecurityPhoneViewModel;

/**
 * 验证安全手机号码
 */

public class VerificationSecurityPhoneActivity extends TourBaseActivity<VerificationSecurityPhoneViewModel,ActivityVerificationSecurityPhoneBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_verification_security_phone;
    }

    @Override
    protected VerificationSecurityPhoneViewModel createViewModel() {
        return new VerificationSecurityPhoneViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
    }
//    @BindView(R.id.phone_et)
//    EditText mPhoneEt;//手机号码
//    @BindView(R.id.verification_code_et)
//    EditText mVerificationCodeEt;//验证码
//    @BindView(R.id.title_name_tv)
//    TextView mTitleNameTv;//标题
//    @BindView(R.id.get_verification_code_tv)
//    TextView mGetVerificationCodeTv;//获取验证码
//
//
//
//    private Context mContext;
//    private String phone;//手机号码
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_verification_phone);
//        ButterKnife.bind(this);
//        init();
//    }
//
//
//    @OnClick({R.id.title_back_iv, R.id.get_verification_code_tv, R.id.ask_iv, R.id.next_tv})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.title_back_iv://返回
//                finish();
//                break;
//
//            case R.id.get_verification_code_tv://获取验证码
//                getVerificationCode(phone);
//                break;
//
//            case R.id.ask_iv://问号
//                startActivity(new Intent(mContext, ExplainActivity.class));
//                break;
//
//            case R.id.next_tv://下一步
//                String verificationCode = mVerificationCodeEt.getText().toString();
//                if (StringUtils.isBlank(verificationCode)){
////                    ToastUtils.show("验证码不能为空");
//                    break;
//                }
//                verificationPhone(phone, verificationCode);
//                break;
//
//        }
//    }
//
//    private void init(){
//        mContext = this;
//        mTitleNameTv.setText("验证手机号");
//
//        phone = Constants.phone;
//        StringBuilder sb = new StringBuilder();
//        for (int j = 0; j < phone.length(); j ++) {
//            char c = phone.charAt(j);
//            if (j >= 3 && j <= 6) {
//                sb.append('x');
//            } else {
//                sb.append(c);
//            }
//        }
//        mPhoneEt.setText(sb.toString());
//    }
//
//    /**
//     * 获取验证码
//     * @param phone：手机号码
//     */
//    private void getVerificationCode(String phone){
//        showProgress();
////        ApiStrategy.getApiService().getVerificationCode(phone)
////                .subscribeOn(Schedulers.io())
////                .observeOn(AndroidSchedulers.mainThread())
////                .subscribe(new Observer<VerificationCodeBean>() {
////                    @Override
////                    public void onSubscribe(Disposable d) {
////
////                    }
////
////                    @Override
////                    public void onNext(VerificationCodeBean verificationCodeBean) {
////                        dismissProgress();
////                        CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(mContext, mGetVerificationCodeTv, 60000, 1000);
////                        countDownTimerUtils.start();
////                    }
////
////                    @Override
////                    public void onError(Throwable e) {
////                        ToastUtils.show("");
////                    }
////
////                    @Override
////                    public void onComplete() {
////                    }
////                });
//    }
//
//
//    /**
//     * 验证手机号码
//     * @param phone：手机号码
//     * @param verificationCode：验证码
//     */
//    private void verificationPhone(String phone, String verificationCode ){
//        showProgress();
//        ApiStrategy.getApiService().verificationPhone(phone, verificationCode)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<BaseBean>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(BaseBean baseBean) {
//                        dismissProgress();
////                        ToastUtils.show(baseBean.getReMsg());
//                        if (baseBean.getReCode().equals(Constants.OK)){
//                            startActivity(new Intent(mContext, com.jm.selfdriving.mine.activity1.BindPhoneActivity.class));
//                            finish();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
////                        ToastUtils.show("");
//                    }
//
//                    @Override
//                    public void onComplete() {
//                    }
//                });
//    }
}
