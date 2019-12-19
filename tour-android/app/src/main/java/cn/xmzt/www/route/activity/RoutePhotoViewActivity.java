package cn.xmzt.www.route.activity;

import android.content.Intent;
import androidx.viewpager.widget.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityRoutePhotoViewBinding;
import cn.xmzt.www.route.adapter.RoutePhotoViewAdapter;
import cn.xmzt.www.route.vm.RoutePhotoViewModel;
import cn.xmzt.www.utils.DonwloadSaveImgUtils;
import cn.xmzt.www.utils.NetWorkUtils;
import cn.xmzt.www.utils.StatusBarUtil;
import cn.xmzt.www.utils.ToastUtils;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.view.BannerViewPager;

import java.util.ArrayList;

/**
 * 线路图片预览
 */
public class RoutePhotoViewActivity extends TourBaseActivity<RoutePhotoViewModel, ActivityRoutePhotoViewBinding> {

    private boolean mIsShowRight = false;
    @Override
    protected int setLayoutId() {
        return R.layout.activity_route_photo_view;
    }

    @Override
    protected RoutePhotoViewModel createViewModel() {
        viewModel = new RoutePhotoViewModel();
        return viewModel;
    }
    private int position;
    private ArrayList urlList;
    private RoutePhotoViewAdapter adapter;
    private int statusBarHeight;
    @Override
    protected void initData() {
        Intent intent=getIntent();
        position=intent.getIntExtra("A",0);
        urlList=intent.getStringArrayListExtra("B");//传多张图片
        String url=intent.getStringExtra("C");//传单张图片
        mIsShowRight = intent.getBooleanExtra("D",false);//传单张图片
        if(urlList==null){
            urlList=new ArrayList();
        }
        if(!TextUtils.isEmpty(url)){
            urlList.add(url);
        }
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarLightMode(this,false);

        if (mIsShowRight){
            dataBinding.titleDownloadIv.setVisibility(View.VISIBLE);
        } else {
            dataBinding.titleDownloadIv.setVisibility(View.GONE);
        }

        statusBarHeight= StatusBarUtil.getStatusBarHeight(getApplicationContext());
        RelativeLayout.LayoutParams mLayoutParams= (RelativeLayout.LayoutParams) dataBinding.titleLayout.getLayoutParams();
        mLayoutParams.topMargin=statusBarHeight;
        dataBinding.setActivity(this);
        dataBinding.banner.setImageLoader(new RoutePhotoViewAdapter());
        dataBinding.banner.isAutoPlay(false);
//        dataBinding.banner.setIndicatorGravity(BannerConfig.RIGHT);
        dataBinding.banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        dataBinding.banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
            }
        });
        dataBinding.banner.setImages(urlList);
        BannerViewPager viewPager = (BannerViewPager) dataBinding.banner.findViewById(R.id.bannerViewPager);
        dataBinding.banner.start();
        viewPager.setCurrentItem(position+1);

        dataBinding.banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                // 下载的时候需要当前位置的url数据
                position = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:{
                onBackPressed();
                break;
            }
            case R.id.title_download_iv:{ // 下载
                if (NetWorkUtils.isNetConnected(this)) {
                    String path = (String) urlList.get(position);
                    DonwloadSaveImgUtils.donwloadImg(RoutePhotoViewActivity.this, path);
                } else {
                    ToastUtils.showText(this,"网络异常");
                }
                break;
            }
            default:
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void OnClickListener(){
//        RoutePhotoViewActivity.this.finish();
    }
}
