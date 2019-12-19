package cn.xmzt.www.mine.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityBindContactBinding;
import cn.xmzt.www.mine.model.BindContactViewModel;

/**
 * 绑定社交账号
 */
public class BindContactActivity extends TourBaseActivity<BindContactViewModel,ActivityBindContactBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_bind_contact;
    }

    @Override
    protected BindContactViewModel createViewModel() {
        return new BindContactViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
    }
//    @BindView(R.id.title_name_tv)
//    TextView mTitleNameTv;//标题
//
//
//    private Context mContext;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_bind_contact);
//        ButterKnife.bind(this);
//        init();
//    }
//
//
//    @OnClick({R.id.title_back_iv})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.title_back_iv://返回
//                finish();
//                break;
//        }
//    }
//
//    private void init(){
//        mContext = this;
//        mTitleNameTv.setText("绑定社交号");
//    }
}
