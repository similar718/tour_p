package cn.xmzt.www.selfdrivingtools.activity;

import android.Manifest;
import androidx.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ActivityWisdomGuideBinding;
import cn.xmzt.www.dialog.ConfirmDialog;
import cn.xmzt.www.selfdrivingtools.adapter.WisdomGuideAdapter;
import cn.xmzt.www.selfdrivingtools.bean.AdvertiseBannerBean;
import cn.xmzt.www.selfdrivingtools.bean.WisdomGuideInfo;
import cn.xmzt.www.selfdrivingtools.event.PlayVoiceTypeEvent;
import cn.xmzt.www.manager.IntentManager;
import cn.xmzt.www.selfdrivingtools.viewmodel.WisdomGuideViewModel;
import cn.xmzt.www.service.LocationService;
import cn.xmzt.www.service.PlayerService;
import cn.xmzt.www.utils.DensityUtil;
import cn.xmzt.www.utils.NetWorkUtils;
import cn.xmzt.www.utils.PermissionUtil;
import cn.xmzt.www.utils.SchemeActivityUtil;
import cn.xmzt.www.utils.StatusBarUtil;
import cn.xmzt.www.utils.ToastUtils;
import cn.xmzt.www.utils.WriteToSdUtils;
import cn.xmzt.www.view.banner.BannerLayout;
import cn.xmzt.www.view.banner.WisdomBannerAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 *  界面的问题
 */
public class WisdomGuideActivity extends TourBaseActivity<WisdomGuideViewModel, ActivityWisdomGuideBinding> {

    private  String mKeywords = ""; // 搜索关键字
    private  int mPageNum = 1;
    private  int mPageSize = 20;
    private  int mTotalSize = 0;

    private  List<WisdomGuideInfo.ItemsBean> list = new ArrayList<>();
    private  WisdomGuideAdapter adapter = null;
    WisdomBannerAdapter webBannerAdapter = null;
    List<String> bannerList = null;
    List<AdvertiseBannerBean> mBannerData = null;

    private Context mContext;


    @Override
    protected int setLayoutId() {
        return cn.xmzt.www.R.layout.activity_wisdom_guide;
    }

    @Override
    protected WisdomGuideViewModel createViewModel() {
        mContext = this;
        viewModel = new WisdomGuideViewModel();
        viewModel.setIView(this);
        this.getApplicationContext().startService(new Intent(this, LocationService.class)); // 开始定位
        viewModel.mAdvertiseBanner.observe(this, new Observer<List<AdvertiseBannerBean>>() {
            @Override
            public void onChanged(@Nullable List<AdvertiseBannerBean> advertiseBannerBeans) {
                if (advertiseBannerBeans != null){
                    mBannerData = advertiseBannerBeans;
                    setBannerData();
                } else {
                    mBannerData = viewModel.getScenicSpotBanner(mContext);
                    setBannerData();
                }
            }
        });
        // 获取数据之后 回传的地方
        viewModel.mWisdomGuideList.observe(this, new Observer<WisdomGuideInfo>() {
            @Override
            public void onChanged(@Nullable WisdomGuideInfo wisdomGuideInfo) {
                setData(wisdomGuideInfo);
            }
        });
        return viewModel;
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        // 设置顶部状态栏
        StatusBarUtil.changeStatusBarColor(this,R.color.color_24A4FF);
        dataBinding.setModel(viewModel);
        dataBinding.setActivity(this);
        // banner
        bannerList = new ArrayList<>();

        if (NetWorkUtils.isNetConnected(mContext)) {
            viewModel.getAdvertiseBanner(mContext, Constants.BannerType.ADVERTISE_BANNER_SCENIC);
        } else {
            // 获取本地banner数据 保存数据到本地
            mBannerData = viewModel.getScenicSpotBanner(mContext);
            setBannerData();
        }

        mPermissionType = 0;

        // 列表显示问题
        dataBinding.rvTourList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        // 获取服务端的数据
        // 默认开始的时候是""
        mKeywords = "";
        getDataList();
        // 讲值赋值给列表
        adapter = new WisdomGuideAdapter(list,this);
        dataBinding.rvTourList.setAdapter(adapter);

        // 上拉加载更多 下拉刷新事件处理
        dataBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPageNum = 1;
                getDataList();
            }
        });
        dataBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                // 需要判断数据可以加载几页
                if (mTotalSize > (mPageNum * mPageSize)) { // 如果当前显示数据小于总数据 表示还有下一页
                    mPageNum += 1;
                    getDataList();
                } else {
                    ToastUtils.showText(mContext,"我也是有底线的");
                    // 在不请求接口的情况下 将它finish掉 否则会一直加载
                    dataBinding.refreshLayout.finishLoadMore(true);
                }
            }
        });

        dataBinding.scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            int lastScrollY = 0;
            int h = DensityUtil.dip2px(mContext,170);
            int color = ContextCompat.getColor(mContext, R.color.color_24A4FF) & 0x00ffffff;
            int mScrollY = 0;

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (lastScrollY < h) {
                    scrollY = Math.min(h, scrollY);
                    mScrollY = scrollY > h ? h : scrollY;
                    dataBinding.clTop.setBackgroundColor(((255 * mScrollY / h) << 24) | color);
                }
                lastScrollY = scrollY;
            }
        });
        dataBinding.clTop.setBackgroundColor(0);


        mHintDialog = new ConfirmDialog(mContext, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPermissionType == 1){ // 写入权限
                    // 引导用户去设置权限
                    PermissionUtil.gotoPermission(mContext);
                    finish();
                } else if (mPermissionType == 2){ // 位置权限
                    // 引导用户去设置权限
                    PermissionUtil.gotoPermission(mContext);
                    finish();
                }
                mHintDialog.dismiss();
            }
        });
        mHintDialog.setCancelable(false);

        // 判断当前音频是否正在播放 正在播放就显示
        if (Constants.mServiceIsStart){
            // 判断当前是否有音频播放事件
            dataBinding.rlVoiceStartPlayShow.setVisibility(View.VISIBLE);
            dataBinding.tvVoiceContent.setText(Constants.mServiceIsStartConteng);
            AnimationDrawable animationDrawable;
            animationDrawable = (AnimationDrawable) dataBinding.playShowIv.getBackground();
            animationDrawable.start();
        }
    }

    private void getDataList(){
        // 获取
        if (NetWorkUtils.isNetConnected(mContext)) { // 判断当前是否有网络 有就正常执行
            viewModel.getWisdomGuideList(Constants.mCity,mKeywords,Constants.mLocation,mPageNum,mPageSize,WisdomGuideActivity.this); //请求数据
//            viewModel.getWisdomGuideList("北京",mKeywords,"116.397128,39.916527",mPageNum,mPageSize,WisdomGuideActivity.this); //请求数据
        } else { // 无网络情况下 判断当前是否是第一页数据
            if (mPageNum == 1){ // 获取本地数据
                viewModel.getScenicSpotList(mContext);
            }
        }
    }

    private void setData(WisdomGuideInfo data){
        dataBinding.refreshLayout.finishRefresh();
        if (data.getTotal() > 0){
            dataBinding.rlEmpty.setVisibility(View.GONE);
            dataBinding.rvTourList.setVisibility(View.VISIBLE);
            if (data != null && mPageNum == 1) {
                list = data.getItems();

                mTotalSize = data.getTotal();

                adapter.setData(list);
                adapter.notifyDataSetChanged();

                // 将数据缓存一页到本地数据库  方便用户在没有网络的情况下就去加载本地的
                viewModel.setAddScenicSpotListDataBase(list,getApplicationContext());

                dataBinding.refreshLayout.finishLoadMore(true);

                // show bottom info
                if (list.get(0).getDistance() <= 0.8){
                    dataBinding.rlBottomAlreadyInfo.setVisibility(View.VISIBLE);
                    dataBinding.tvAlreadyIntoScenicSpot.setText("您已经入"+list.get(0).getScenicName());
                } else {
                    dataBinding.rlBottomAlreadyInfo.setVisibility(View.GONE);
                }
            } else {
                list = data.getItems();
                adapter.addData(list);
                adapter.notifyDataSetChanged();
            }
        } else {
            // 数据判空
            dataBinding.rlEmpty.setVisibility(View.VISIBLE);
            dataBinding.rvTourList.setVisibility(View.GONE);
        }
    }

    private void setBannerData(){
        if (mBannerData == null){
            return;
        }
        if (mBannerData.size() == 0)
            return;

        for (int i = 0; i < mBannerData.size(); i++) {
            bannerList.add(mBannerData.get(i).getAdvPic());
        }
        while (bannerList.size()<3){
            if (bannerList.size() == 1){
                bannerList.add(bannerList.get(0));
                bannerList.add(bannerList.get(0));
            } else if (bannerList.size() == 2){
                bannerList.add(bannerList.get(0));
                bannerList.add(bannerList.get(1));
            }
        }
        webBannerAdapter = new WisdomBannerAdapter(this, bannerList);
        webBannerAdapter.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 顶部banner的点击事件
                if(mBannerData!=null&&mBannerData.size()>position){
                    if ( ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        AdvertiseBannerBean mAdvertiseBean=mBannerData.get(position);
                        SchemeActivityUtil.startToActivity(mContext,mAdvertiseBean.getLinkType(),mAdvertiseBean.getHomeName(),mAdvertiseBean.getAdvLink());
                    } else {
                        ToastUtils.showText(mContext,"需要读取数据权限才能进入");
                    }
                }
            }
        });
        dataBinding.banner.setAdapter(webBannerAdapter);
        // 显示指示器
        dataBinding.banner.setShowIndicator(true);
        dataBinding.banner.setAutoPlaying(true);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case cn.xmzt.www.R.id.title_back_iv:
                finish();
                break;
            case cn.xmzt.www.R.id.widom_guide_search_tv: //搜索栏点击事件
                 // 跳转到景区搜索界面
                IntentManager.getInstance().goScenicSpotSearchActivity(this);
                break;
            case cn.xmzt.www.R.id.play_show_cancel: // 取消显示 点击之后 界面不显示
                dataBinding.rlVoiceStartPlayShow.setVisibility(View.GONE);
                // 服务关闭  不是暂停 是退出
//                Intent intent = new Intent(this, PlayerService.class);
//                stopService(intent);
                Intent intent = new Intent();
                intent.putExtra("MSG", Constants.PlayerMag.STOP);
                intent.putExtra("path", "");
                intent.setClass(mContext, PlayerService.class);
                startService(intent);
                break;
            case cn.xmzt.www.R.id.rl_voice_start_play_show: // top playing info click
                if (Constants.mScenicId >0) {
                    this.getApplicationContext().startService(new Intent(this, LocationService.class)); // 开始定位
                    IntentManager.getInstance().goScenicSpotMapActivity(this, Constants.mScenicId,null);
                } else {
                    ToastUtils.showText(mContext,"获取景区编号失败");
                }
                break;
            case cn.xmzt.www.R.id.iv_hide_show: // bottom already into scenic view click hide
                dataBinding.rlBottomAlreadyInfo.setVisibility(View.GONE);
                break;
            case cn.xmzt.www.R.id.rl_bottom_already_info: // bottom already into scenic view click
                if ( ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    if (GPSUtils.isOPen(mContext)) {
                        this.getApplicationContext().startService(new Intent(this, LocationService.class)); // 开始定位
                        IntentManager.getInstance().goScenicSpotMapActivity(this, list.get(0).getId(),null);
//                    } else {
//                        ToastUtils.showText(mContext,"请打开位置权限/GPS位置信息");
//                    }
                } else {
                    ToastUtils.showText(mContext,"需要读取数据权限才能进入");
                }
                break;
            default:
                break;
        }
    }

    public void OnClickListener(WisdomGuideInfo.ItemsBean data, int tag){
        if ( ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (data.getDistance() <= 0.8f) { // 这个进入可能有直接定位您当前位置功能
//                if (GPSUtils.isOPen(mContext)) {
                    this.getApplicationContext().startService(new Intent(this, LocationService.class)); // 开始定位
                    IntentManager.getInstance().goScenicSpotMapActivity(this, data.getId(),null);
//                } else {
//                    ToastUtils.showText(mContext, "请打开位置权限/GPS位置信息");
//                }
            } else { // 这个进入当前界面不会打开定位您的当前位置功能
                this.getApplicationContext().startService(new Intent(this, LocationService.class)); // 开始定位
                IntentManager.getInstance().goScenicSpotMapActivity(this, data.getId(),null);
            }
        } else {
            ToastUtils.showText(mContext,"需要读取数据权限才能进入");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getOrderType(PlayVoiceTypeEvent event) {
        if (event.getType() == 0){ // 表示停止播放
            dataBinding.rlVoiceStartPlayShow.setVisibility(View.GONE);
        } else if (event.getType() == 1){ // 表示开始播放数据
            // 判断当前是否有音频播放事件
            dataBinding.rlVoiceStartPlayShow.setVisibility(View.VISIBLE);
            dataBinding.tvVoiceContent.setText(Constants.mServiceIsStartConteng);
            AnimationDrawable animationDrawable;
            animationDrawable = (AnimationDrawable) dataBinding.playShowIv.getBackground();
            animationDrawable.start();
        } else if (event.getType() == 2){ // 暂停中
            dataBinding.rlVoiceStartPlayShow.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private static final int WRITE_COARSE_LOCATION_REQUEST_CODE = 0x001;

    private ConfirmDialog mHintDialog = null;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mHintDialog.isShowing()){
            mHintDialog.dismiss();
        }
        //可在此继续其他操作。
        if (requestCode == WRITE_COARSE_LOCATION_REQUEST_CODE) {
            if (!verifyPermissions(grantResults)) {      // 表示有权限没有被授权  需要提醒用户重新授权
                // 弹出提示信息，说用户没有授权，可能会导致部分功能不能很好的实现 TODO
//                showMissingPermissionDialog();              //显示提示信息
            } else { // 所有的权限都同意之后开始定位
                mContext.getApplicationContext().startService(new Intent(mContext, LocationService.class));
            }
            if ( ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                // 有写入数据库的权限
                // 将Assets里面的自定义地图写入到sd卡中 方便在其他界面获取数据
                WriteToSdUtils.setAssetsDataToSd(mContext.getApplicationContext());
            }
        }
        if ( ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            // 有写入数据库的权限
            // 将Assets里面的自定义地图写入到sd卡中 方便在其他界面获取数据
            WriteToSdUtils.setAssetsDataToSd(mContext.getApplicationContext());
        } else { // 获取到没有给写入本地的数据权限
            mPermissionType = 1 ;
            // TODO 只能做提示的效果
            mHintDialog.setViewData("获取存储信息权限失败，不能正常使用");
            mHintDialog.show();
        }

        if ( ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            // 有写入数据库的权限
            // 将Assets里面的自定义地图写入到sd卡中 方便在其他界面获取数据
            mContext.getApplicationContext().startService(new Intent(mContext, LocationService.class));
        } else { // 获取到没有给写入本地的数据权限
            // TODO 只能做提示的效果
            mPermissionType = 2;
            mHintDialog.setViewData("获取位置信息权限失败，不能正常使用");
            mHintDialog.show();
        }
    }

    private int mPermissionType = 0;

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}