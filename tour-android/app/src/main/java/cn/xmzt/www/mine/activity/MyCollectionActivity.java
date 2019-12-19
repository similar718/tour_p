package cn.xmzt.www.mine.activity;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityMyCollectionBinding;
import cn.xmzt.www.mine.model.MyCollectionViewModel;

/**
 * 我的收藏
 */

public class MyCollectionActivity extends TourBaseActivity<MyCollectionViewModel, ActivityMyCollectionBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_collection;
    }

    @Override
    protected MyCollectionViewModel createViewModel() {
        return new MyCollectionViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
    }
//    @BindView(R.id.title_name_tv)
//    TextView mTitleNameTv;
//    @BindView(R.id.all_tv)
//    TextView mAllTv;//全部文本
//    @BindView(R.id.route_tv)
//    TextView mRouteTv;//线路文本
//    @BindView(R.id.ticket_tv)
//    TextView mTicketTv;//门票
//    @BindView(R.id.hotel_tv)
//    TextView mHotelTv;//酒店文本
//    @BindView(R.id.operation_tv)
//    TextView mOperationTv;//编辑与完成操作
//    @BindView(R.id.all_v)
//    View mAllV;//全部下划线
//    @BindView(R.id.route_v)
//    View mRouteV;//线路下划线
//    @BindView(R.id.ticket_v)
//    View mTicketV;//门票下划线
//    @BindView(R.id.hotel_v)
//    View mHotelV;//酒店下划线
//    @BindView(R.id.viewpager)
//    ViewPager mViewPager;
//
//
//
//    private Context mContext;
//    private String mTitles[] = {
//            "全部", "线路", "门票", "酒店"};
//
//    private List<Fragment> mFragments;
//    private TabFragmentAdapter mTabFragmentAdapter;
//    private int index = 0;//
//    private int operation = 0;//操作 0为编辑 1为完成
//    private MyCollectionFragment allCollectionFragment, routeCollectionFragment,
//            ticketCollectionFragment, hotelCollectionFragment;//全部、路径、门票、酒店
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_collection);
//        ButterKnife.bind(this);
//        init();
//    }
//
//
//
//
//    @OnClick({R.id.title_back_iv, R.id.all_rl, R.id.route_rl, R.id.ticket_rl, R.id.hotel_rl, R.id.operation_tv})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.title_back_iv://返回
//                finish();
//                break;
//
//            case R.id.all_rl://全部
//                mViewPager.setCurrentItem(0, false);
//                break;
//
//            case R.id.route_rl://线路
//                mViewPager.setCurrentItem(1, false);
//                break;
//
//            case R.id.ticket_rl://门票
//                mViewPager.setCurrentItem(2, false);
//                break;
//
//            case R.id.hotel_rl://酒店
//                mViewPager.setCurrentItem(3, false);
//                break;
//
//            case R.id.operation_tv://编辑与完成操作
//                if (operation == 0){
//                    mOperationTv.setText("完成");
//                    operation = 1;
//                    showFragmentBottom();
//                } else {
//                    mOperationTv.setText("编辑");
//                    operation = 0;
//                    hideFragmentBottom();
//                }
//                break;
//        }
//    }
//
//    private void init(){
//        mContext = this;
//        mTitleNameTv.setText("绑定手机号");
//        mFragments = new ArrayList<>();
//
//        allCollectionFragment = new MyCollectionFragment();
//        Bundle bundle_1 = new Bundle();
//        bundle_1.putString("text", mTitles[0]);
//        bundle_1.putInt("type", 1);
//        allCollectionFragment.setArguments(bundle_1);
//        mFragments.add(allCollectionFragment);
//
//        routeCollectionFragment = new MyCollectionFragment();
//        Bundle bundle_2 = new Bundle();
//        bundle_2.putString("text", mTitles[1]);
//        bundle_2.putInt("type", 1);
//        routeCollectionFragment.setArguments(bundle_2);
//        mFragments.add(routeCollectionFragment);
//
//
//        ticketCollectionFragment = new MyCollectionFragment();
//        Bundle bundle_3 = new Bundle();
//        bundle_3.putString("text", mTitles[2]);
//        bundle_3.putInt("type", 2);
//        ticketCollectionFragment.setArguments(bundle_3);
//        mFragments.add(ticketCollectionFragment);
//
//        hotelCollectionFragment = new MyCollectionFragment();
//        Bundle bundle_4 = new Bundle();
//        bundle_4.putString("text", mTitles[3]);
//        bundle_4.putInt("type", 3);
//        hotelCollectionFragment.setArguments(bundle_4);
//        mFragments.add(hotelCollectionFragment);
//
//
//        mTabFragmentAdapter = new TabFragmentAdapter(mFragments, mTitles, getSupportFragmentManager(), mContext);
//        mViewPager.setOffscreenPageLimit(6);// 设置预加载Fragment个数
//        mViewPager.setAdapter(mTabFragmentAdapter);
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
//                        mRouteTv.setTypeface(Typeface.DEFAULT);
//                        mTicketTv.setTypeface(Typeface.DEFAULT);
//                        mHotelTv.setTypeface(Typeface.DEFAULT);
//                        mAllV.setVisibility(View.VISIBLE);
//                        mRouteV.setVisibility(View.GONE);
//                        mTicketV.setVisibility(View.GONE);
//                        mHotelV.setVisibility(View.GONE);
//                        hideFragmentBottom();
//                        break;
//
//                    case 1:
//                        mAllTv.setTypeface(Typeface.DEFAULT);
//                        mRouteTv.setTypeface(Typeface.DEFAULT_BOLD);
//                        mTicketTv.setTypeface(Typeface.DEFAULT);
//                        mHotelTv.setTypeface(Typeface.DEFAULT);
//                        mAllV.setVisibility(View.GONE);
//                        mRouteV.setVisibility(View.VISIBLE);
//                        mTicketV.setVisibility(View.GONE);
//                        mHotelV.setVisibility(View.GONE);
//                        hideFragmentBottom();
//                        break;
//
//                    case 2:
//                        mAllTv.setTypeface(Typeface.DEFAULT);
//                        mRouteTv.setTypeface(Typeface.DEFAULT);
//                        mTicketTv.setTypeface(Typeface.DEFAULT_BOLD);
//                        mHotelTv.setTypeface(Typeface.DEFAULT);
//                        mAllV.setVisibility(View.GONE);
//                        mRouteV.setVisibility(View.GONE);
//                        mTicketV.setVisibility(View.VISIBLE);
//                        mHotelV.setVisibility(View.GONE);
//                        hideFragmentBottom();
//                        break;
//
//                    case 3:
//                        mAllTv.setTypeface(Typeface.DEFAULT);
//                        mRouteTv.setTypeface(Typeface.DEFAULT);
//                        mTicketTv.setTypeface(Typeface.DEFAULT);
//                        mHotelTv.setTypeface(Typeface.DEFAULT_BOLD);
//                        mAllV.setVisibility(View.GONE);
//                        mRouteV.setVisibility(View.GONE);
//                        mTicketV.setVisibility(View.GONE);
//                        mHotelV.setVisibility(View.VISIBLE);
//                        hideFragmentBottom();
//                        break;
//                }
//                mOperationTv.setText("编辑");
//                operation = 0;
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//
//            }
//        });
//
//        mViewPager.setCurrentItem(index);// 设置当前显示标签页为第一页
//    }
//
//    /**
//     * 显示Fragment底部的“全选”与“取消收藏”
//     */
//    private void showFragmentBottom(){
//        //allCollectionFragment.getUserVisibleHint()
//        if (allCollectionFragment.getUserVisibleHint()){
//            allCollectionFragment.showBottom();
//        } else if (routeCollectionFragment.getUserVisibleHint()){
//            routeCollectionFragment.showBottom();
//        } else if (ticketCollectionFragment.getUserVisibleHint()){
//            ticketCollectionFragment.showBottom();
//        } else if (hotelCollectionFragment.getUserVisibleHint()){
//            hotelCollectionFragment.showBottom();
//        }
//    }
//
//    /**
//     * 显示隐藏Fragment底部的“全选”与“取消收藏”
//     */
//    private void hideFragmentBottom(){
//        if (allCollectionFragment.getUserVisibleHint()){
//            allCollectionFragment.hideBottom();
//        } else if (routeCollectionFragment.getUserVisibleHint()){
//            routeCollectionFragment.hideBottom();
//        } else if (ticketCollectionFragment.getUserVisibleHint()){
//            ticketCollectionFragment.hideBottom();
//        } else if (hotelCollectionFragment.getUserVisibleHint()){
//            hotelCollectionFragment.hideBottom();
//        }
//    }
//

}
