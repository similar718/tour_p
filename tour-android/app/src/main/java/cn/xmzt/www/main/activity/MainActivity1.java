package cn.xmzt.www.main.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseFragment;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityMain1Binding;
import cn.xmzt.www.home.fragment.HomeFragment;
import cn.xmzt.www.intercom.fragment.SchedulingListFragment;
import cn.xmzt.www.main.vm.MainViewModel;
import cn.xmzt.www.mine.fragment.MineFragment;
import cn.xmzt.www.nim.uikit.support.permission.MPermission;
import cn.xmzt.www.route.fragment.RouteFrament;
import cn.xmzt.www.selfdrivingtools.fragment.ElectricGuideFragment;

import java.util.ArrayList;

public class MainActivity1 extends TourBaseActivity<MainViewModel, ActivityMain1Binding> implements ViewPager.OnPageChangeListener {
    private FragmentStatePagerAdapter mAdapter;
    /**
     * 四个个Fragment（页面）
     */
    private HomeFragment mHomeFragment;
    private RouteFrament mRouteFrament;
    private ElectricGuideFragment mElectricGuideFragment;
    private SchedulingListFragment mSchedulingListFragment;
    private MineFragment mMineFragment;
    private ArrayList<BaseFragment> mTabs = new ArrayList<BaseFragment>();
    private ArrayList<LottieAnimationView> animTabs = new ArrayList<LottieAnimationView>();
    private ArrayList<TextView> nameTabs = new ArrayList<TextView>();

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main1;
    }
    @Override
    protected MainViewModel createViewModel() {
        viewModel = new MainViewModel();
        return viewModel;
    }
    @Override
    protected void initData() {
        requestBasicPermission();
//        toActivity(GuideActivity.class); //跳到启动页面或者引导页面
        initView();
    }
    protected void initView() {
        initTabIndicator();
        initFragments();

        dataBinding.mViewPager.setAdapter(mAdapter);
        dataBinding.mViewPager.setOffscreenPageLimit(5);
        dataBinding.mViewPager.addOnPageChangeListener(this);
    }
    /**
     * 初始化TabBar
     */
    private void initTabIndicator() {
        animTabs.add(dataBinding.ivTabHome);
        animTabs.add(dataBinding.ivTabRoute);
        animTabs.add(dataBinding.ivTabSelfDriving);
        animTabs.add(dataBinding.ivTabXc);
        animTabs.add(dataBinding.ivTabUser);

        nameTabs.add(dataBinding.tvTabHome);
        nameTabs.add(dataBinding.tvTabRoute);
        nameTabs.add(dataBinding.tvTabSelfDriving);
        nameTabs.add(dataBinding.tvTabXc);
        nameTabs.add(dataBinding.tvTabUser);

        dataBinding.ivTabHome.playAnimation();
        dataBinding.tvTabHome.setTextColor(Color.parseColor("#40BAFF"));
    }

    private final int BASIC_PERMISSION_REQUEST_CODE = 100;
    /**
     * 基本权限管理
     */
    private final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private void requestBasicPermission() {
        MPermission.printMPermissionResult(true, this, BASIC_PERMISSIONS);
        MPermission.with(MainActivity1.this)
                .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(BASIC_PERMISSIONS)
                .request();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tabLayout1: {
                setCurrentTab(0);
                break;
            }
            case R.id.tabLayout2: {
                setCurrentTab(1);
                break;
            }
            case R.id.tabLayout3: {
                setCurrentTab(2);
                break;
            }
            case R.id.tabLayout4: {
                setCurrentTab(3);
                break;
            }
            case R.id.tabLayout5: {
                setCurrentTab(4);
                break;
            }
            default: {
                break;
            }
        }
    }




    /**
     * 初始化主页面的四个主页面Fragment
     */
    private void initFragments() {
        if (mTabs != null && mTabs.size() == 0) {
            mHomeFragment = new HomeFragment();
            mRouteFrament = new RouteFrament();
            mElectricGuideFragment= new ElectricGuideFragment();
            mSchedulingListFragment= new SchedulingListFragment();
            mMineFragment= new MineFragment();
            mTabs.add(mHomeFragment);
            mTabs.add(mRouteFrament);
            mTabs.add(mElectricGuideFragment);
            mTabs.add(mSchedulingListFragment);
            mTabs.add(mMineFragment);
        }
        mAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTabs.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mTabs.get(arg0);
            }

            @Override
            public int getItemPosition(Object object) {
                if (object instanceof HomeFragment) {
                    mHomeFragment = (HomeFragment) object;
                    mTabs.set(0, mHomeFragment);
                } else if (object instanceof RouteFrament) {
                    mRouteFrament = (RouteFrament) object;
                    mTabs.set(1, mRouteFrament);
                } else if (object instanceof ElectricGuideFragment) {
                    mElectricGuideFragment = (ElectricGuideFragment) object;
                    mTabs.set(2, mElectricGuideFragment);
                } else if (object instanceof SchedulingListFragment) {
                    mSchedulingListFragment = (SchedulingListFragment) object;
                    mTabs.set(3, mSchedulingListFragment);
                }else if (object instanceof MineFragment) {
                    mMineFragment = (MineFragment) object;
                    mTabs.set(4, mMineFragment);
                }
                return super.getItemPosition(object);
            }
        };
    }

    /**
     * 重置TabBar的图标文字颜色
     */
    private void resetTabsIconAlpha() {
        for (int i = 0; i < animTabs.size(); i++) {
            animTabs.get(i).setFrame(0);
            nameTabs.get(i).setTextColor(Color.parseColor("#666666"));
        }
    }


    /**
     * 设置当前的TabBar
     *
     * @param index 表示第几个tabbar
     */
    private void setCurrentTab(int index) {
        resetTabsIconAlpha();
        animTabs.get(index).playAnimation();
        nameTabs.get(index).setTextColor(Color.parseColor("#40BAFF"));
        dataBinding.mViewPager.setCurrentItem(index, false);
    }

    @Override
    public void onPageSelected(int arg0) {
        setCurrentTab(arg0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}