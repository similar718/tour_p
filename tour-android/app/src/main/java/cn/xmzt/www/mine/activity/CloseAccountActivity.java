package cn.xmzt.www.mine.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityCloseAccountBinding;
import cn.xmzt.www.mine.model.CloseAccountViewModel;

/**
 * 注销账号
 */
public class CloseAccountActivity extends TourBaseActivity<CloseAccountViewModel, ActivityCloseAccountBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_close_account;
    }

    @Override
    protected CloseAccountViewModel createViewModel() {
        return new CloseAccountViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
        if (TourApplication.getUser() != null) {
            if (TourApplication.getUser().getTel().contains("****")){
                dataBinding.tvAccount.setText("将" + TourApplication.getUser().getTel() + "所绑定的账户注销");
            } else {
                String tel = TourApplication.getUser().getTel();
                tel = tel.substring(0,3) + "****" + tel.substring(7,tel.length());
                dataBinding.tvAccount.setText("将" + tel + "所绑定的账户注销");
            }
        }
    }
//    @BindView(R.id.title_name_tv)
//    TextView mTitleNameTv;//标题
//
//
//    private Context mContext;
//    private CustomDialog dialog, exceptionDialog;//dialog点击下一步时的弹出框；exceptionDialog异常弹出框
//    private int dialogWidth;//dialog宽度
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_close_account);
//        ButterKnife.bind(this);
//        init();
//    }
//
//
//
//    @OnClick({R.id.title_back_iv, R.id.close_account_next_btn})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.title_back_iv://返回
//                finish();
//                break;
//
//            case R.id.close_account_next_btn://下一步
//                View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_default, null);
//                TextView tipsTv = mView.findViewById(R.id.tips_tv);
//                TextView confirmTv = mView.findViewById(R.id.confirm_tv);
//                TextView cancelTv = mView.findViewById(R.id.cancel_tv);
//                tipsTv.setText("确定要注销账号吗？");
//                confirmTv.setOnClickListener(this);
//                cancelTv.setOnClickListener(this);
//                dialog = new CustomDialog(mContext, dialogWidth, 0, mView, R.style.CustomDialog);
//                dialog.show();
//                break;
//        }
//    }
//
//    private void init(){
//        mContext = this;
//        mTitleNameTv.setText("注销账号");
//        WindowManager wm = (WindowManager) this
//                .getSystemService(Context.WINDOW_SERVICE);
//        dialogWidth = (int)(wm.getDefaultDisplay().getWidth() * 0.8);
//    }
//
//
//    @Override
//    public void onClick(View view) {
//        int id = view.getId();
//        switch (id){
//            case R.id.confirm_tv://点击下一步时弹出框确定
//                dialog.dismiss();
//                check();
//                break;
//
//            case R.id.cancel_tv://点击下一步时弹出框取消
//                dialog.dismiss();
//
//                break;
//
//
//            case R.id.have_title_confirm_tv://异常弹出框确定
//                exceptionDialog.dismiss();
//                check();
//                break;
//
//            case R.id.have_title_cancel_tv://异常弹出框取消
//                exceptionDialog.dismiss();
//                break;
//
//        }
//    }
//
//
//    /**
//     * 注销检查
//     */
//    private void check(){
//        showProgress();
//        ApiStrategy.getApiService().logoutCheck(Constants.token)
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
//                        if (baseBean.getReCode().equals(Constants.OK)){
////                            ToastUtils.show(baseBean.getReMsg());
//                            startActivity(new Intent(mContext, VerifyActivity.class));
//                            finish();
//                        } else {
//                            View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_have_text_itle, null);
//                            TextView titleTv = mView.findViewById(R.id.title_tv);
//                            TextView tipsTv = mView.findViewById(R.id.tips_tv);
//                            TextView confirmTv = mView.findViewById(R.id.have_title_confirm_tv);
//                            TextView cancelTv = mView.findViewById(R.id.have_title_cancel_tv);
//                            titleTv.setText("注销异常");
//                            tipsTv.setText(baseBean.getReMsg());
//                            confirmTv.setOnClickListener(CloseAccountActivity.this::onClick);
//                            cancelTv.setOnClickListener(CloseAccountActivity.this::onClick);
//                            exceptionDialog = new CustomDialog(mContext, dialogWidth, 0, mView, R.style.CustomDialog);
//                            exceptionDialog.show();
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
