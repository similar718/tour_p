package cn.xmzt.www.mine.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityMyScoreBinding;
import cn.xmzt.www.mine.model.MyScoreViewModel;
import cn.xmzt.www.utils.StatusBarUtil;

/**
 * 我的积分
 */

public class MyScoreActivity extends TourBaseActivity<MyScoreViewModel, ActivityMyScoreBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_score;
    }

    @Override
    protected MyScoreViewModel createViewModel() {
        return new MyScoreViewModel();
    }

    @Override
    protected void initData() {
        StatusBarUtil.setStatusBarLightMode(this,true);
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
        viewModel.getUserScore();
    }
//    @BindView(R.id.title_name_tv)
//    TextView mNameTv;
//    @BindView(R.id.score_tv)
//    TextView scoreTv;//积分
//    @BindView(R.id.all_tv)
//    TextView mAllTv;//全部文本
//    @BindView(R.id.get_score_tv)
//    TextView mGetScoreTv;//积分获取
//    @BindView(R.id.use_score_tv)
//    TextView mUseScoreTv;//积分消耗
//    @BindView(R.id.all_v)
//    View mAllV;//全部下划线
//    @BindView(R.id.get_score_v)
//    View mGetScoreV;//积分获取
//    @BindView(R.id.use_score_v)
//    View mUseScoreV;//积分消耗
//    @BindView(R.id.viewpager)
//    ViewPager mViewPager;
//
//    private Context mContext;
//    private int dialogWidth;//dialog宽度
//
//    private String mTitles[] = {
//            "全部", "积分获取", "积分消耗"};
//
//    private List<Fragment> mFragments;
//    private TabFragmentAdapter mTabFragmentAdapter;
//    private AllScoreFragment allScoreFragment;//全部积分
//    private GetScoreFragment getScoreFragment;//积分获取
//    private UseScoreFragment useScoreFragment;//消耗积分
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_score1);
//        ButterKnife.bind(this);
//        init();
//    }
//
//    private void init() {
//        mContext = this;
//        mPresenter = new ScorePresenter();
//        mPresenter.attachView(this);
//        mPresenter.getMyScore(Constants.token);
//        mNameTv.setText("我的积分");
//        mFragments = new ArrayList<>();
//
//        WindowManager wm = (WindowManager) this
//                .getSystemService(Context.WINDOW_SERVICE);
//        dialogWidth = (int)(wm.getDefaultDisplay().getWidth() * 0.8);
//
//        Bundle bundle = new Bundle();
//        allScoreFragment = new AllScoreFragment();
//        bundle.putString("text", mTitles[0]);
//        bundle.putInt("type", 0);
//        allScoreFragment.setArguments(bundle);
//        mFragments.add(allScoreFragment);
//
//        getScoreFragment = new GetScoreFragment();
//        bundle.putString("text", mTitles[1]);
//        bundle.putInt("type", 1);
//        getScoreFragment.setArguments(bundle);
//        mFragments.add(getScoreFragment);
//
//        useScoreFragment = new UseScoreFragment();
//        bundle.putString("text", mTitles[2]);
//        bundle.putInt("type", 2);
//        useScoreFragment.setArguments(bundle);
//        mFragments.add(useScoreFragment);
//
//
//        mTabFragmentAdapter = new TabFragmentAdapter(mFragments, mTitles, getSupportFragmentManager(), mContext);
//        mViewPager.setOffscreenPageLimit(6);// 设置预加载Fragment个数
//        mViewPager.setAdapter(mTabFragmentAdapter);
//        mViewPager.setCurrentItem(0);// 设置当前显示标签页为第一页
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//                switch (i){
//                    case 0:
//                        mAllTv.setTypeface(Typeface.DEFAULT_BOLD);
//                        mGetScoreTv.setTypeface(Typeface.DEFAULT);
//                        mUseScoreTv.setTypeface(Typeface.DEFAULT);
//                        mAllV.setVisibility(View.VISIBLE);
//                        mGetScoreV.setVisibility(View.GONE);
//                        mUseScoreV.setVisibility(View.GONE);
//                        break;
//
//                    case 1:
//                        mAllTv.setTypeface(Typeface.DEFAULT);
//                        mGetScoreTv.setTypeface(Typeface.DEFAULT_BOLD);
//                        mUseScoreTv.setTypeface(Typeface.DEFAULT);
//                        mAllV.setVisibility(View.GONE);
//                        mGetScoreV.setVisibility(View.VISIBLE);
//                        mUseScoreV.setVisibility(View.GONE);
//                        break;
//
//                    case 2:
//                        mAllTv.setTypeface(Typeface.DEFAULT);
//                        mGetScoreTv.setTypeface(Typeface.DEFAULT);
//                        mUseScoreTv.setTypeface(Typeface.DEFAULT_BOLD);
//                        mAllV.setVisibility(View.GONE);
//                        mGetScoreV.setVisibility(View.GONE);
//                        mUseScoreV.setVisibility(View.VISIBLE);
//                        break;
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//
//            }
//        });
//    }
//
//
//    @OnClick({R.id.title_back_iv, R.id.score_rule_tv, R.id.all_rl, R.id.get_score_rl, R.id.use_score_rl})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.title_back_iv://返回
//                finish();
//                break;
//
//            case R.id.score_rule_tv://积分规则
//                mPresenter.getScoreRule(1);
//                break;
//
//            case R.id.all_rl://全部
//                mViewPager.setCurrentItem(0, false);
//                break;
//
//            case R.id.get_score_rl://积分获取
//                mViewPager.setCurrentItem(1, false);
//                break;
//
//            case R.id.use_score_rl://积分消耗
//                mViewPager.setCurrentItem(2, false);
//                break;
//
//
//        }
//    }
//
//    @Override
//    public void showLoading() {
//        showProgress();
//    }
//
//    @Override
//    public void hideLoading() {
//        dismissProgress();
//    }
//
//    @Override
//    public void onError(Throwable throwable) {
//
//    }
//
//    @Override
//    public void getMyScoreCallback(ScoreBean scoreBean) {
//        if (scoreBean.getRel() == null){
//            scoreTv.setText("0");
//        } else {
//            scoreTv.setText(scoreBean.getRel().getTotalIntegral() + "");
//        }
//    }
//
//    @Override
//    public void getScoreDetailCallback(ScoreDetailBean scoreDetailBean) {
//
//    }
//
//    @Override
//    public void getScoreRuleCallback(ScoreRuleBean scoreRuleBean) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_score_rule, null);
//        WebView webView = view.findViewById(R.id.web_view);
//        webView.loadData(scoreRuleBean.getRel().getContent(), "text/html;charset=utf-8", "utf-8");
//        CustomDialog dialog = new CustomDialog(mContext, dialogWidth, 0, view, R.style.CustomDialog);
//        dialog.show();
//    }
}
