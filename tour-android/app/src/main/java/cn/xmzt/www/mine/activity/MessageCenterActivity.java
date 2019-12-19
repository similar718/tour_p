package cn.xmzt.www.mine.activity;

import android.view.View;
import android.widget.FrameLayout;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityMessageCenterBinding;
import cn.xmzt.www.mine.fragment.SysActivityMSGFrament;
import cn.xmzt.www.mine.fragment.SysConsultingMSGFrament;
import cn.xmzt.www.mine.fragment.SysMSGFrament;
import cn.xmzt.www.mine.fragment.SysOrderMSGFrament;
import cn.xmzt.www.mine.model.MessageCenterViewModel;
import cn.xmzt.www.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * @describe 消息中心
 */

public class MessageCenterActivity extends TourBaseActivity<MessageCenterViewModel,ActivityMessageCenterBinding> implements ViewPager.OnPageChangeListener {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_message_center;
    }

    @Override
    protected MessageCenterViewModel createViewModel() {
        return new MessageCenterViewModel();
    }
    private int statusBarHeight=0;
    int screenW=0;
    private List<Fragment> fragments = new ArrayList<>();
    private FragmentPagerAdapter mAdapter = null;
    @Override
    protected void initData() {
        screenW=getResources().getDisplayMetrics().widthPixels;
        statusBarHeight= StatusBarUtil.getStatusBarHeight(getApplicationContext());
        FrameLayout.LayoutParams mLayoutParams= (FrameLayout.LayoutParams) dataBinding.titleLayout.getLayoutParams();
        mLayoutParams.topMargin=statusBarHeight;
        StatusBarUtil.setStatusBarLightMode(this,false);
        StatusBarUtil.setFullScreen(this);
        dataBinding.setActivity(this);
        dataBinding.setViewModel(viewModel);
        fragments.add(new SysMSGFrament());
        fragments.add(new SysOrderMSGFrament());
        fragments.add(new SysActivityMSGFrament());
        fragments.add(new SysConsultingMSGFrament());
        viewModel.getNewCount();
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

        };
        dataBinding.viewpager.setAdapter(mAdapter);
        dataBinding.viewpager.setOffscreenPageLimit(4);
        dataBinding.viewpager.addOnPageChangeListener(this);
        setTabIndex(0,false);
    }
    private void setTabIndex(int tabIndex ,boolean isOnClick){
        if(tabIndex==0){
            dataBinding.lineSystem.setVisibility(View.VISIBLE);
            dataBinding.lineOrder.setVisibility(View.INVISIBLE);
            dataBinding.lineActivity.setVisibility(View.INVISIBLE);
            dataBinding.lineConsultation.setVisibility(View.INVISIBLE);
        }else if(tabIndex==1){
            dataBinding.lineSystem.setVisibility(View.INVISIBLE);
            dataBinding.lineOrder.setVisibility(View.VISIBLE);
            dataBinding.lineActivity.setVisibility(View.INVISIBLE);
            dataBinding.lineConsultation.setVisibility(View.INVISIBLE);
            if(viewModel.ddmsgRead.get()==1){
                viewModel.markReadSysMsgTypesUpdate("2");
            }
        }else if(tabIndex==2){
            dataBinding.lineSystem.setVisibility(View.INVISIBLE);
            dataBinding.lineOrder.setVisibility(View.INVISIBLE);
            dataBinding.lineActivity.setVisibility(View.VISIBLE);
            dataBinding.lineConsultation.setVisibility(View.INVISIBLE);
            if(viewModel.hdmsgRead.get()==1){
                viewModel.markReadSysMsgTypesUpdate("3");
            }
        }else if(tabIndex==3){
            dataBinding.lineSystem.setVisibility(View.INVISIBLE);
            dataBinding.lineOrder.setVisibility(View.INVISIBLE);
            dataBinding.lineActivity.setVisibility(View.INVISIBLE);
            dataBinding.lineConsultation.setVisibility(View.VISIBLE);
        }
        if(isOnClick){
            dataBinding.viewpager.setCurrentItem(tabIndex);
        }
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:{
                onBackPressed();
                break;
            }
            case R.id.cl_system:{
                //系统消息
                setTabIndex(0,true);
                break;
            }
            case R.id.cl_order: {
                //订单消息
                setTabIndex(1,true);
                break;
            }
            case R.id.cl_activity: {
                //活动消息
                setTabIndex(2,true);
                break;
            }
            case R.id.cl_consultation: {
                //咨询服务消息
                setTabIndex(3,true);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        setTabIndex(i,false);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
