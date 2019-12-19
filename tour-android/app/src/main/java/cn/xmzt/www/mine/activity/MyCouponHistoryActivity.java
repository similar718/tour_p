package cn.xmzt.www.mine.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityMyCouponHistoryBinding;
import cn.xmzt.www.mine.model.MyCouponHistoryViewModel;

/**
 * 优惠券历史记录
 */

public class MyCouponHistoryActivity extends TourBaseActivity<MyCouponHistoryViewModel, ActivityMyCouponHistoryBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_coupon_history;
    }

    @Override
    protected MyCouponHistoryViewModel createViewModel() {
        return new MyCouponHistoryViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
    }

//    @BindView(R.id.title_name_tv)
//    TextView titleNameTv;
//    @BindView(R.id.used_tv)
//    TextView mUsedTv;//已使用
//    @BindView(R.id.expired_tv)
//    TextView mExpiredTv;//已过期文本
//    @BindView(R.id.used_v)
//    View mUsedV;//已使用下划线
//    @BindView(R.id.expired_v)
//    View mExpiredV;//已过期下划线
//
//    @BindView(R.id.viewpager)
//    ViewPager mViewPager;
//
//    private Context mContext;
//
//    private String mTitles[] = {
//            "全部", "待支付", "待出行", "待评价", "已退款"};
//
//    private List<Fragment> mFragments;
//    private TabFragmentAdapter mTabFragmentAdapter;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_coupon_history_record);
//        ButterKnife.bind(this);
//        init();
//    }
//
//    private void init() {
//        mContext = this;
//        titleNameTv.setText(getString(R.string.cra_title));
//
//        mFragments = new ArrayList<>();
//
//        for (int i = 0; i < 2; i++) {
//            UsedFragment fragment = new UsedFragment();
//            Bundle bundle = new Bundle();
//            bundle.putString("text", mTitles[i]);
//            fragment.setArguments(bundle);
//            mFragments.add(fragment);
//        }
//
//        mTabFragmentAdapter = new TabFragmentAdapter(mFragments, mTitles, getSupportFragmentManager(), mContext);
////        mViewPager.setOffscreenPageLimit(mFragments.size());// 设置预加载Fragment个数
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
//                        mUsedTv.setTypeface(Typeface.DEFAULT_BOLD);
//                        mExpiredTv.setTypeface(Typeface.DEFAULT);
//                        mUsedV.setVisibility(View.VISIBLE);
//                        mExpiredV.setVisibility(View.GONE);
//                        break;
//
//                    case 1:
//                        mUsedTv.setTypeface(Typeface.DEFAULT);
//                        mExpiredTv.setTypeface(Typeface.DEFAULT_BOLD);
//                        mUsedV.setVisibility(View.GONE);
//                        mExpiredV.setVisibility(View.VISIBLE);
//                        break;
//
//
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//
//            }
//        });
//    }

}
