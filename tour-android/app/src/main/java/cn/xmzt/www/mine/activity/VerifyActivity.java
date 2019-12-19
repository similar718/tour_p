package cn.xmzt.www.mine.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityVerifyBinding;
import cn.xmzt.www.mine.model.VerifyViewModel;

/**
 * 注销验证的界面
 */
public class VerifyActivity extends TourBaseActivity<VerifyViewModel,ActivityVerifyBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_verify;
    }

    @Override
    protected VerifyViewModel createViewModel() {
        return new VerifyViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
    }
//    @BindView(R.id.phone_et)
//    EditText mPhoneEt;//手机号码
//    @BindView(R.id.verification_code_et)
//    EditText verificationCodeEt;//验证码
//    @BindView(R.id.title_name_tv)
//    TextView mTitleNameTv;//标题
//    @BindView(R.id.get_verification_code_tv)
//    TextView mGetVerificationCodeTv;//获取验证码
//
//    private Context mContext;
//    private String phone;//手机号码
//    private String verificationCode;//验证码
//    private CustomDialog dialog, exceptionDialog;//
//    private int dialogWidth;//dialog宽度
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_verify);
//        ButterKnife.bind(this);
//        init();
//    }
//
//
//    @OnClick({R.id.title_back_iv, R.id.logout_tv, R.id.get_verification_code_tv})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.title_back_iv://返回
//                finish();
//                break;
//
//            case R.id.get_verification_code_tv://获取验证码
//                if (phone.length() == 11){
//                    getVerificationCode(phone);
//                } else {
////                    ToastUtils.show(getString(R.string.common_account_format_error_tips));
//                }
//                break;
//
//            case R.id.logout_tv://注銷
//                verificationCode = verificationCodeEt.getText().toString();
//                if (StringUtils.isEmpty(phone) || phone.length() != 11){
////                    ToastUtils.show(getString(R.string.common_account_format_error_tips));
//                    break;
//                }
//                if (StringUtils.isEmpty(verificationCode)) {
////                    ToastUtils.show(getString(R.string.common_verification_code_empty_tips));
//                    break;
//                }
//
//                View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_logout, null);
//                TextView tipsTv = mView.findViewById(R.id.tips_tv);
//                TextView confirmTv = mView.findViewById(R.id.confirm_tv);
//                TextView cancelTv = mView.findViewById(R.id.cancel_tv);
//                tipsTv.setText("注销账号会清空所有信息和数据，你是否确认注销？");
//                confirmTv.setOnClickListener(this);
//                cancelTv.setOnClickListener(this);
//                dialog = new CustomDialog(mContext, dialogWidth, 0, mView, R.style.CustomDialog);
//                dialog.show();
//                break;
//        }
//    }
//
//
//        @Override
//        public void onClick(View view) {
//            int id = view.getId();
//            switch (id){
//                case R.id.confirm_tv://
//                    dialog.dismiss();
//                    logout(phone, verificationCode);
//                    break;
//
//                case R.id.cancel_tv://
//                    dialog.dismiss();
//                    break;
//
//            }
//        }
//
//    private void init(){
//        mContext = this;
//        mTitleNameTv.setText("绑定手机号");
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
//
//        WindowManager wm = (WindowManager) this
//                .getSystemService(Context.WINDOW_SERVICE);
//        dialogWidth = (int)(wm.getDefaultDisplay().getWidth() * 0.8);
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
////
////                    }
////
////                    @Override
////                    public void onComplete() {
////                    }
////                });
//    }
//
//    /**
//     * 注销
//     * @param phone：手机号码
//     * @param verificationCode：验证码
//     */
//    private void logout(String phone, String verificationCode){
//        showProgress();
//        ApiStrategy.getApiService().logout(Constants.token, phone, verificationCode)
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
//

}

