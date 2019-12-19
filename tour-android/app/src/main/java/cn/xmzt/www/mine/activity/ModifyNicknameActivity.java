package cn.xmzt.www.mine.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityModifyNicknameBinding;
import cn.xmzt.www.mine.event.NicknameEvent;
import cn.xmzt.www.mine.model.ModifyNicknameViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author tanlei
 * @date 2019/8/5
 * @describe 修改昵称
 */

public class ModifyNicknameActivity extends TourBaseActivity<ModifyNicknameViewModel, ActivityModifyNicknameBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_modify_nickname;
    }

    @Override
    protected ModifyNicknameViewModel createViewModel() {
        return new ModifyNicknameViewModel();
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getNickName(NicknameEvent nicknameEvent) {
        viewModel.setNickName(nicknameEvent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

//    @BindView(R.id.delete_iv)
//    ImageView deleteIv;//删除图标
//    @BindView(R.id.nickname_et)
//    EditText nicknameEt;//昵称
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_modify_nickname);
//        ButterKnife.bind(this);
//        init();
//
//    }
//
//    private void init(){
//        nicknameEt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                int length = editable.toString().length();
//                if (length > 0){
//                    deleteIv.setVisibility(View.VISIBLE);
//                } else {
//                    deleteIv.setVisibility(View.GONE);
//                }
//            }
//        });
//    }
//
//    @OnClick({R.id.title_back_iv, R.id.delete_iv, R.id.save_tv})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.title_back_iv:
//                finish();
//                break;
//
//            case R.id.delete_iv://删除图标
//                nicknameEt.setText("");
//                break;
//
//            case R.id.save_tv://保存
//                String nickname = nicknameEt.getText().toString();
//                if (StringUtils.isBlank(nickname)){
////                    ToastUtils.show(getString(R.string.mna_modify_nickname_tips));
//                    break;
//                }
//                showProgress();
//                ApiStrategy.getApiService().modifyNickname(Constants.token, nickname)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Observer<ModifyNicknameBean>() {
//                            @Override
//                            public void onSubscribe(Disposable d) {
//                                dismissProgress();
//                            }
//
//                            @Override
//                            public void onNext(ModifyNicknameBean modifyNicknameBean) {
////                                ToastUtils.show(modifyNicknameBean.getReMsg());
//                                Intent intent = new Intent();
//                                Bundle bundle = new Bundle();
//                                bundle.putString("nickname", nicknameEt.getText().toString());
//                                intent.putExtras(bundle);
//                                setResult(RESULT_OK, intent);
//                                MineEvent mineEvent = new MineEvent();
//                                mineEvent.setFreshNickname(true);
//                                mineEvent.setNickname(nicknameEt.getText().toString());
//                                EventBus.getDefault().post(mineEvent);
//                                finish();
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//
//
//                            }
//
//                            @Override
//                            public void onComplete() {
//
//                            }
//                        });
//                break;
//        }
//    }
//

}
