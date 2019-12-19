package cn.xmzt.www.selfdrivingtools.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.GroundOverlay;
import com.amap.api.maps.model.GroundOverlayOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.blankj.utilcode.util.ActivityUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import cn.xmzt.www.R;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ActivityScenicSpotMapBinding;
import cn.xmzt.www.dialog.ConfirmDialog;
import cn.xmzt.www.dialog.GuideDialog;
import cn.xmzt.www.dialog.ScenicContentDialog;
import cn.xmzt.www.dialog.ScenicMapOfflineDialog;
import cn.xmzt.www.dialog.ScenicSpotWIFIHintDialog;
import cn.xmzt.www.dialog.ShowMemberLocationChoiceDialog;
import cn.xmzt.www.dialog.ShowMemberLocationHintDialog;
import cn.xmzt.www.dialog.ShowMemberLocationOpenDialog;
import cn.xmzt.www.dialog.ThirdPartyMapsGuideDialog;
import cn.xmzt.www.glide.GlideApp;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.helper.UserHelper;
import cn.xmzt.www.intercom.api.NimUIKit;
import cn.xmzt.www.intercom.api.model.team.TeamDataChangedObserver;
import cn.xmzt.www.intercom.bean.MyTalkGroupBean;
import cn.xmzt.www.intercom.event.LocationShareStatusEvent;
import cn.xmzt.www.intercom.profile.TeamLocationProfile;
import cn.xmzt.www.main.activity.MainActivity;
import cn.xmzt.www.main.globals.TalkManage;
import cn.xmzt.www.nim.uikit.common.util.file.FileUtil;
import cn.xmzt.www.popup.ScenicSpotMapAllTypePopupWindow;
import cn.xmzt.www.popup.ScenicSpotMapRightAddPopupWindow;
import cn.xmzt.www.roomdb.TourDatabase;
import cn.xmzt.www.roomdb.beans.GroupUserInfo;
import cn.xmzt.www.rxjava2.AsyncUtil;
import cn.xmzt.www.selfdrivingtools.adapter.LabelAdapter;
import cn.xmzt.www.selfdrivingtools.adapter.MapScenicSpotListDetailAdapter;
import cn.xmzt.www.selfdrivingtools.adapter.MapScenicSpotOtherListAdapter;
import cn.xmzt.www.selfdrivingtools.adapter.MapScenicSpotRouteLineListAdapter;
import cn.xmzt.www.selfdrivingtools.bean.FileInfo;
import cn.xmzt.www.intercom.bean.MyTalkGroupsBean;
import cn.xmzt.www.selfdrivingtools.bean.ScenicSpotGuideBean;
import cn.xmzt.www.selfdrivingtools.bean.ScenicSpotMapTypeListBean;
import cn.xmzt.www.selfdrivingtools.bean.ScenicVoicePackageBean;
import cn.xmzt.www.selfdrivingtools.event.PlayVoiceTypeEvent;
import cn.xmzt.www.manager.IntentManager;
import cn.xmzt.www.selfdrivingtools.map.ThirdPartyMapsGuide;
import cn.xmzt.www.selfdrivingtools.overlay.AMapUtil;
import cn.xmzt.www.selfdrivingtools.viewmodel.ScenicSpotMapViewModel;
import cn.xmzt.www.service.LocationService;
import cn.xmzt.www.service.PlayerService;
import cn.xmzt.www.share.ShareFunction;
import cn.xmzt.www.utils.BitmapUtils;
import cn.xmzt.www.utils.DensityUtil;
import cn.xmzt.www.utils.DownloadUtil;
import cn.xmzt.www.utils.DpUtil;
import cn.xmzt.www.utils.GPSUtils;
import cn.xmzt.www.utils.LocalDataUtils;
import cn.xmzt.www.utils.MD5Utils;
import cn.xmzt.www.utils.NetWorkUtils;
import cn.xmzt.www.utils.SPUtils;
import cn.xmzt.www.utils.StatusBarUtil;
import cn.xmzt.www.utils.TimeUtil;
import cn.xmzt.www.utils.ToastUtils;
import cn.xmzt.www.view.CenterLayoutManager;
import cn.xmzt.www.view.RoundImageView;

import com.netease.nimlib.sdk.team.model.Team;
import com.yinglan.scrolllayout.ScrollLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.xmzt.www.view.listener.TextChangedListener;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class ScenicSpotMapActivity extends TourBaseActivity<ScenicSpotMapViewModel, ActivityScenicSpotMapBinding> {

    // 类型列表数据
    private List<ScenicSpotMapTypeListBean> mTypeList = new ArrayList<>();
    // 类型列表adapter
    private LabelAdapter mTypeAdapter = null;
    private int lastLabelIndex;
    private CenterLayoutManager centerLayoutManager;
    // 底部景点显示adapter
    private MapScenicSpotListDetailAdapter mBottomScenicSpotAdapter = null;
    // 底部其他类型的adapter
    private MapScenicSpotOtherListAdapter mBottomOtherAdapter = null;
    // 底部景点搜索事件
    private List<ScenicSpotGuideBean.ScenicSpotListBean> mSpotListSearchList = new ArrayList<>();
    // 底部路线类型的adapter
    private MapScenicSpotRouteLineListAdapter mBottomLineAdapter = null;
    // 景点所有数据
    private ScenicSpotGuideBean mInfo = null;
    // 景点音频包数据
    private ScenicVoicePackageBean mPackageInfo = null;

    private AMap mAmap;
    private GroundOverlay groundoverlay;

    private MarkerOptions markerOption;
    // 顶部右边的点击之后的popupwindow
    ScenicSpotMapRightAddPopupWindow mPopRightAdd;
    // 全部类型的点击之后的popupwindow
    ScenicSpotMapAllTypePopupWindow mPopAllType;
    /*******存储所有Marker**********/
    List<Marker> mAllMarker = new ArrayList<>();
    Marker mLocationMarker = null;
    // 当前路线里面类型的marker的显示保存
    List<Marker> mAllLineMarker = new ArrayList<>();
    // 当前路线的显示保存
    Polyline mAllLinePolyline = null;
    // 音频文件信息
    List<FileInfo> mVoiceList = null;
    private Boolean mIsAutoPlayerVoice = false;

    private int mInitMapZoom = 13;
    private int mMinMapZoom = 13;
    private int mClickMapZoom = 18;

    private ScenicSpotGuideBean.ScenicServicePointListBean mBottomListOneData = null;

    private boolean mIsActivity = true;

    // 景点marker
    private Marker mCurrentMarker = null;
    // 其他marker
    private Marker mCurrentOtherMarker = null;

    private int mMapBgWidth = 1000;
    private int mMapBgHeight = 1000;

    private Context mContext;
    private String mToken = "";

    private String mUpGroupId = null;

    @Override
    protected int setLayoutId() {
        return cn.xmzt.www.R.layout.activity_scenic_spot_map;
    }

    @Override
    protected ScenicSpotMapViewModel createViewModel() {
        mContext = this;
        viewModel = new ScenicSpotMapViewModel();
        viewModel.setIView(this);
        viewModel.mScenicSpotMap.observe(this, new Observer<ScenicSpotGuideBean>() {
            @Override
            public void onChanged(@Nullable ScenicSpotGuideBean scenicSpotGuideBean) {
                // 获取得到所有的数据
                if (scenicSpotGuideBean != null) {
                    mInfo = scenicSpotGuideBean;
                    setData();
                } else {
                    if (NetWorkUtils.isNetConnected(mContext)) {
                        ToastUtils.showText(mContext, "数据获取异常");
                    } else {
                        ToastUtils.showText(mContext, "网络异常");
                    }
                }
            }
        });
        viewModel.mScenicSpotVoicePackage.observe(this, new Observer<ScenicVoicePackageBean>() {
            @Override
            public void onChanged(@Nullable ScenicVoicePackageBean scenicVoicePackageBean) {
                if (scenicVoicePackageBean != null) {
                    mPackageInfo = scenicVoicePackageBean;
                    // 获取音频文件信息
                    if (mPackageInfo != null && !TextUtils.isEmpty(mPackageInfo.getResUrl())) {
                        mVoiceList = viewModel.getLocalFileAllVoice(mPackageInfo.getResUrl());
                        if (mVoiceList != null && mVoiceList.size() > 1){ // TODO 如果已经下载将数据添加到数据库 供离线的时候使用
                            if (!viewModel.getIsScenicVoicePackageToDataBase(mContext, mPackageInfo)) {
                                viewModel.setAddScenicVoicePackageToDataBase(mContext, mPackageInfo);
                            }
                        }
                    }
                    //  语音包获取之后操作
                    /**
                     *  判断当前景区是否有zip文件
                     *  1. 判断当前是否是WIFI的情况下
                     *      是 就默认去下载当前语音包
                     *      否 就不管
                     *  2. 下载语音包的情况
                     *      判断当前数据库是否有下载文件的数据
                     *          有就不进行下载
                     *          无就进行下载
                     */
                    if (!TextUtils.isEmpty(mPackageInfo.getResUrl())) { // 判断当前给的url是否是空值
                        String filename = MD5Utils.getMd5Value(mPackageInfo.getResUrl());
                        mVoiceFileName = Constants.SCENIC_VOICE_FILE_PATH + filename;
                        if (NetWorkUtils.getNetworkType(mContext) == 1) { // 当前网络是WiFi的情况
                            if (!viewModel.getIsScenicVoicePackageToDataBase(mContext, mPackageInfo)) { // 判断当前音频文件是否已经下载了 未下载的情况
                                mDownLoadType = 0;
//                                downFileNotUI(mPackageInfo.getResUrl());
                            } else { // 已经下载了
                                mDownLoadType = 3;
                            }
                        } else {
                            if (!viewModel.getIsScenicVoicePackageToDataBase(mContext, mPackageInfo)) { // 判断当前音频文件是否已经下载了 未下载的情况
                                mDownLoadType = 0;
                            } else { // 已经下载了
                                mDownLoadType = 3;
                            }
                        }
                    } else { // 路径都没有表示没有下载
                        mDownLoadType = 0;
                    }
                    if (mDownLoadType == 0){ // 没有下载的情况弹出下载提示框
                        if (NetWorkUtils.getNetworkType(mContext) == 1){ // WIFI 情况下
                            mHandler.sendEmptyMessageDelayed(MSG_DOWNLOAD_WIFI_HINT,300);
                        }
                    }
                } else {
                    if (NetWorkUtils.isNetConnected(mContext)) {
                        ToastUtils.showText(mContext, "数据包获取异常");
                    } else {
                        ToastUtils.showText(mContext, "网络异常");
                    }
                }
            }
        });
        viewModel.mPlayOperation.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                // 表示当前播放操作成功
            }
        });
        viewModel.mUserGroupSetups.observe(this, new Observer<MyTalkGroupsBean>() {
            @Override
            public void onChanged(MyTalkGroupsBean scenicSpotChangeGroupBean) {
                if (scenicSpotChangeGroupBean != null) { // 有数据的处理
                    mChangeGroupInfo = scenicSpotChangeGroupBean;
                    if (scenicSpotChangeGroupBean.getGroupUserSetups() != null){
                        mChangeGroupList = scenicSpotChangeGroupBean.getGroupUserSetups();
                    }
                    if (!TextUtils.isEmpty(scenicSpotChangeGroupBean.getGroupId())){ // 判断获取到的数据是否有已经设置了的群信息
                        if (scenicSpotChangeGroupBean.getIsPosition() == 2){ // 并且设置了开启
                            mIsOpenGroupLocationTmp = true;
                        } else { // 设置了关闭
                            mIsOpenGroupLocationTmp = false;
                            mIsOptionCloseLocation = true; // 第一次的时候设置一次
                        }
                        // 切换成当前群
                        changeGroupAllData(mChangeGroupInfo.getGroupId());
                    }
                } else {
                    // 无数据的处理
                    ToastUtils.showText(mContext,"暂无可出行的队伍");
                }
            }
        });
        viewModel.mSetGroupSetups.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String groupId) {
                // 表示设置成功
            }
        });
        // 权限问题
        setPermission();
        return viewModel;
    }

    private ScrollLayout.OnScrollChangedListener mOnScrollChangedListener = new ScrollLayout.OnScrollChangedListener() {
        @Override
        public void onScrollProgressChanged(float currentProgress) {
            if (dataBinding.tvListMore.getVisibility() == View.VISIBLE) {
                if (mBottomType == 0) {
                    dataBinding.tvListMore.setVisibility(View.GONE);
                    dataBinding.tvListMoreLine.setVisibility(View.GONE);
                    setBottomPadding(false);
                } else {
                    dataBinding.tvListMore.setVisibility(View.VISIBLE);
                    dataBinding.tvListMoreLine.setVisibility(View.VISIBLE);
                    setBottomPadding(true);
                }
            }
        }

        @Override
        public void onScrollFinished(ScrollLayout.Status currentStatus) {
            if (currentStatus.equals(ScrollLayout.Status.EXIT)) { // 底部
                Drawable drawable = mContext.getResources().getDrawable(R.drawable.scenic_spot_bottom_to_t);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                dataBinding.tvListMore.setCompoundDrawables(null, drawable, null, null);
                dataBinding.tvScenicSpotNum.setCompoundDrawables(null, drawable, null, null);
                dataBinding.tvListMore.setText("上滑查看更多结果");
                if (dataBinding.tvListMore.getVisibility() == View.GONE) {
                    dataBinding.tvListMore.setVisibility(View.VISIBLE);
                    dataBinding.tvListMoreLine.setVisibility(View.VISIBLE);
                    setBottomPadding(true);
                }
            }
            if (currentStatus.equals(ScrollLayout.Status.OPENED)) { // 中间
                Drawable drawable = mContext.getResources().getDrawable(R.drawable.scenic_spot_bottom_line);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                dataBinding.tvListMore.setCompoundDrawables(null, drawable, null, null);
                dataBinding.tvScenicSpotNum.setCompoundDrawables(null, drawable, null, null);
                if (mBottomType == 0) {
                    dataBinding.tvListMore.setText("全部信息（" + mInfo.getScenicSpotList().size() + "）");
                } else if (mBottomType == 1) {
                    dataBinding.tvListMore.setText("全部信息（" + mInfo.getScenicLineList().size() + "）");
                } else if (mBottomType == 2) {
                    dataBinding.tvListMore.setText("全部信息（" + mOtherInfo.size() + "）");
                } else {
                    dataBinding.tvListMore.setText("上滑查看更多结果");
                }
            }
            if (currentStatus.equals(ScrollLayout.Status.CLOSED)) { // 顶部
                Drawable drawable = mContext.getResources().getDrawable(R.drawable.scenic_spot_bottom_to_b);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                dataBinding.tvListMore.setCompoundDrawables(null, drawable, null, null);
                dataBinding.tvScenicSpotNum.setCompoundDrawables(null, drawable, null, null);
                if (mBottomType == 0) {
                    dataBinding.tvListMore.setText("全部信息（" + mInfo.getScenicSpotList().size() + "）");
                } else if (mBottomType == 1) {
                    dataBinding.tvListMore.setText("全部信息（" + mInfo.getScenicLineList().size() + "）");
                } else if (mBottomType == 2) {
                    dataBinding.tvListMore.setText("全部信息（" + mOtherInfo.size() + "）");
                } else {
                    dataBinding.tvListMore.setText("上滑查看更多结果");
                }
            }
        }

        @Override
        public void onChildScroll(int top) {
        }
    };

    @Override
    protected void initData() {
        mIsActivity = true;
        EventBus.getDefault().register(this);
        // 设置顶部状态栏
        StatusBarUtil.setStatusBarLightMode(this, true);
        // Initialize all member cache information
        TeamLocationProfile.getInstance().registerObserver(true);
        dataBinding.setModel(viewModel);
        dataBinding.setActivity(this);
        mUpGroupId = getIntent().getStringExtra("groupid");
        // 初始化地图控件
        dataBinding.mvMap.onCreate(getIntent().getExtras());
        if (mAmap == null) {
            mAmap = dataBinding.mvMap.getMap();
            mAmap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(Constants.mLatitude,Constants.mLongitude)));
        }
        // 初始化控件
        mPopRightAdd = new ScenicSpotMapRightAddPopupWindow(this);
        if (TourApplication.getUser()!= null) {
            mUserId = String.valueOf(TourApplication.getUser().getUserId());
        }

        /**
         app:allowHorizontalScroll="true"
         app:exitOffset="150dp"
         app:isSupportExit="true"
         app:maxOffset="400dp"
         app:minOffset="50dp"
         android:visibility="visible"
         app:mode="exit">

         //是否支持横向滚动
         app:exitOffset="0dp"              //最低部退出状态时可看到的高度，0为不可见
         app:isSupportExit="true"	      //是否支持下滑退出，支持会有下滑到最底部时的回调
         app:maxOffset="260dp"             //打开状态时内容显示区域的高度
         app:minOffset="50dp"              //关闭状态时最上方预留高度
         app:mode="open">                  //默认位置状态，关闭、打开、底部
         */
//        int height = (getResources().getDisplayMetrics().heightPixels - DpUtil.dp2px(mContext,105))/2;
//        int scrollHeight = dataBinding.rlBottomContent.getHeight();
//        int px_10 = DpUtil.dp2px(mContext,10);
//        int px_50 = DpUtil.dp2px(mContext,50);
//        int px_150 = DpUtil.dp2px(mContext,150); // 为什么一定要是150 其他手机都可以正常使用 有些手机适配有问题
//        dataBinding.rlBottomContent.setMinOffset(px_10);//关闭状态时最上方预留高度
//        dataBinding.rlBottomContent.setMaxOffset(height);//打开状态时内容显示区域的高度
//        dataBinding.rlBottomContent.setExitOffset(px_150); //最低部退出状态时可看到的高度，0为不可见
//        dataBinding.rlBottomContent.setToExit();
//        dataBinding.rlBottomContent.setIsSupportExit(true);
//        dataBinding.rlBottomContent.setAllowHorizontalScroll(true);

        /**设置 setting*/
        dataBinding.rlBottomContent.setOnScrollChangedListener(mOnScrollChangedListener);

        dataBinding.etSearch.addTextChangedListener(mTextWatcher);

        mTypeList = new ArrayList<>();
        //横向的RecycleView
        centerLayoutManager = new CenterLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        dataBinding.rvTypeList.setLayoutManager(centerLayoutManager);
        mTypeAdapter = new LabelAdapter(mTypeList, this);
        dataBinding.rvTypeList.setAdapter(mTypeAdapter);

        // 底部抽屉式 景点rv
        dataBinding.rvSpotList.setLayoutManager(new GridLayoutManager(this, 2));
        mBottomScenicSpotAdapter = new MapScenicSpotListDetailAdapter(this);
        dataBinding.rvSpotList.setAdapter(mBottomScenicSpotAdapter);

        // 底部抽屉式 线路类型的rv
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        dataBinding.rvBottomListLine.setLayoutManager(layoutManager);
        DividerItemDecoration decor = new DividerItemDecoration(this, layoutManager.getOrientation());
        decor.setDrawable(ContextCompat.getDrawable(this, R.drawable.item_ticket_detail_recyleview_line));
        dataBinding.rvBottomListLine.addItemDecoration(decor);
        mBottomLineAdapter = new MapScenicSpotRouteLineListAdapter(this);
        dataBinding.rvBottomListLine.setAdapter(mBottomLineAdapter);

        // 底部抽屉式 其他类型的rv
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration decor1 = new DividerItemDecoration(this, layoutManager1.getOrientation());
        decor1.setDrawable(ContextCompat.getDrawable(this, R.drawable.item_ticket_detail_recyleview_line));
        dataBinding.rvBottomListOther.setLayoutManager(layoutManager1);
        dataBinding.rvBottomListOther.addItemDecoration(decor1);
        mBottomOtherAdapter = new MapScenicSpotOtherListAdapter(this);
        dataBinding.rvBottomListOther.setAdapter(mBottomOtherAdapter);

        // 获取景点信息
        if (NetWorkUtils.isNetConnected(mContext)) {
            viewModel.getScenicSpotGuideInfo(getIntent().getLongExtra("id", 0), Constants.mLatitude, Constants.mLongitude, getApplicationContext());
        } else {
            // 获取数据
            ScenicSpotGuideBean scenicSpotGuideBean = viewModel.getScenicContentsToDataBase(mContext, getIntent().getLongExtra("id", 0));
            if (scenicSpotGuideBean != null) {
                mInfo = scenicSpotGuideBean;
                setData();
            }
        }
        // 获取景点音频数据包 有无网络都应该获取
        viewModel.getScenicVoicePackage(getIntent().getLongExtra("id", 0), getApplicationContext());
        // 部分点击事件
        initClickListener();

        dataBinding.switchAutomatic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // 自动更改状态的事件监听
                if (b) { // 自动播放事件打开
                    mIsAutoPlayerVoice = true;
                } else { // 自动事件关闭
                    mIsAutoPlayerVoice = false;
                }
                if (mIsAutoPlayerVoice) {
                    ToastUtils.showText(mContext, "自动播放已开启，\n将自动为您播放附近景点的语音讲解");
                } else {
                    ToastUtils.showText(mContext, "自动播放已关闭");
                }
                if (!getIsContain() && b) { // 判断是否在当前区域内
                    ToastUtils.showText(mContext, "您当前不在景区内");
                    dataBinding.switchAutomatic.setChecked(false);
                }
            }
        });
        mToken = TourApplication.getToken();
        if (!TextUtils.isEmpty(mToken)){
            if (!TextUtils.isEmpty(mUpGroupId)) {
                viewModel.setUserGroupOrPosition(mToken, mUpGroupId, 2);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            viewModel.getUserGroupSetups(mToken,mUpGroupId);
        }
        // 获取全部人员
        mHandler.sendEmptyMessage(HANDLER_GET_MEMBER_INFO);
    }

    private boolean mIsAutoDataBase = false;

    private void getAutoPlaying(){
        if (getIsContain()){
            // 判断当前是否是WIFI状态下 大于1的情况就是移动网络
            if (NetWorkUtils.getNetworkType(mContext) > 1){
                showConfirmDialog("是否继续使用2G/3G/4G流量进行自动播放");
            } else {
                dataBinding.switchAutomatic.setChecked(true);
                mIsAutoPlayerVoice = true;
            }
        } else {
            dataBinding.switchAutomatic.setChecked(false);
            mIsAutoPlayerVoice = false;
        }
    }

    private void showConfirmDialog(String content){
        if (confirmDialog == null){
            confirmDialog = new ConfirmDialog(mContext, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.confirm_tv){
                        // 确定按钮
                        if (mIsClosePersonLocation) {
                            mIsClosePersonLocation = false;
                            mIsOptionCloseLocation = true;
                            mIsOpenGroupLocationTmp = false;
                            changeGroupAllData(mGroupId);
                            if (mShowMemberLocationChoiceDialog.isShowing()) {
                                mShowMemberLocationChoiceDialog.dismiss();
                            }
                        } else {
                            mIsAutoDataBase = true;
                            dataBinding.switchAutomatic.setChecked(true);
                            mIsAutoPlayerVoice = true;
                        }
                    } else {
                        // 取消按钮
                        if (mIsClosePersonLocation) {
                            mIsClosePersonLocation = false;
                            if (mShowMemberLocationChoiceDialog.isShowing()) {
                                mShowMemberLocationChoiceDialog.dismiss();
                            }
                        } else {
                            mIsAutoDataBase = false;
                            dataBinding.switchAutomatic.setChecked(false);
                            mIsAutoPlayerVoice = false;
                        }
                    }
                    confirmDialog.dismiss();
                }
            });
        }
        confirmDialog.setViewData(content);
        confirmDialog.show();
    }
    private ConfirmDialog confirmDialog = null;
    Timer mLocationTimer = null;
    TimerTask mTimerTask = null;

    /**
     * 每5秒获取一次定位操作
     */
    private void setTimerPer5SecondsOptions() {
        mLocationTimer = new Timer();
        mLocationTimer.schedule(mTimerTask = new TimerTask() {
            @Override
            public void run() {
                // 绘制自己定位的maker
                if (getIsContain()) {
                    // 获取当前得到的经纬度信息 绘制当前自己位置的maker 判断当前是否应该自定播放最近的音频文件
                    if (mIsActivity) {
                        // 判断是否自动播放 判断当前是否在播放中
                        setAutoPlayerNextScenicSpotVoiceData();
                    }
                }
            }
        }, 500, 5000); // 0.5秒之后 每隔5秒操作一次
    }

    public void onClick(View view) {
        hideInputView();
        switch (view.getId()) {
            case cn.xmzt.www.R.id.scenic_back_iv: // 返回事件
                finish();
                break;
            case cn.xmzt.www.R.id.scenic_right_add_iv: // 顶部右边的点击事件
                // popupwindow的显示
                if (null == mPopRightAdd) {
                    if (mPopRightAdd.isShowing()) {
                        mPopRightAdd.dismiss();
                    } else {
                        mPopRightAdd.showAsDropDown(dataBinding.scenicRightAddIv, 0, 0, Gravity.BOTTOM);
                    }
                } else {
                    if (!mPopRightAdd.isShowing()) { // 不显示的情况才去调用显示
                        mPopRightAdd.showAsDropDown(dataBinding.scenicRightAddIv, 0, 0, Gravity.BOTTOM);
                    }
                }
                break;
            case cn.xmzt.www.R.id.rl_all_type: // 全部类型显示
                // popupwindow的显示
                if (null == mPopAllType) {
                    mPopAllType = new ScenicSpotMapAllTypePopupWindow(this, mTypeList);
                    if (mPopAllType.isShowing()) {
                        mPopAllType.dismiss();
                    } else {
                        mPopAllType.showAsDropDown(dataBinding.lineTv, 0, 0, Gravity.BOTTOM);
                    }
                } else {
                    if (!mPopAllType.isShowing()) { // 不显示的情况才去调用显示
                        mPopAllType.showAsDropDown(dataBinding.lineTv, 0, 0, Gravity.BOTTOM);
                    }
                }
                break;
            case cn.xmzt.www.R.id.tv_list_more: // 底部查看显示更多结果
                dataBinding.rlBottomContent.setToOpen();
                break;
            case cn.xmzt.www.R.id.iv_right_center_detail: // 中部显示景区详请
                if (mMapViewIsShow) { //显示需要隐藏
                    setHideMapView();
                    showScenicContentDialog();
                }
                break;
            case cn.xmzt.www.R.id.iv_mine_location: // 我的事件点击
                // 当前我的经纬度 将地图移动到我的中心点
                setMyLocationData();
                break;
            case cn.xmzt.www.R.id.play_show_cancel: // 播放语音界面的取消事件
                // 点击之后 界面不显示
                dataBinding.rlVoiceStartPlayShow.setVisibility(View.GONE);
                // 服务关闭  不是暂停 是退出
//                Intent intent = new Intent(this, PlayerService.class);
//                stopService(intent);
                Intent intent = new Intent();
                intent.putExtra("MSG", Constants.PlayerMag.STOP);
                intent.putExtra("path", "");
                intent.setClass(mContext, PlayerService.class);
                startService(intent);
                mIsStop = true;
                // 将position设为-2
                break;
            case cn.xmzt.www.R.id.item_map_scenic_spot_offline_iv: // list里面的离线事件
                if (NetWorkUtils.isNetConnected(mContext)) {
                    if (mVoiceList != null && mVoiceList.size()>1){
                        mDownLoadType = 3;
                    } else {
                        if (mPackageInfo != null) {
                            if (viewModel.getIsScenicVoicePackageToDataBase(mContext, mPackageInfo)) {
                                viewModel.deleteScenicVoicePackageToDataBase(mContext,mPackageInfo);
                            }
                        }
                        mDownLoadType = 0;
                    }
                    showScenicMapOfflineDialog(mDownLoadType,0);
                } else {
                    ToastUtils.showText(mContext, "您的网络异常");
                }
                break;
            case cn.xmzt.www.R.id.rl_voice_start_play_show: // 当前播放的控件
                if (Constants.mScenicSpotId  == 0){ // 当前界面播放的是景区详请
                    setHideMapView();
                    showScenicContentDialog();
                } else { // 表示是其他marker的播放事件
                    showPlayingMarkerInfoWindow();
                }
                break;
            case cn.xmzt.www.R.id.delete_iv: // 删除当前搜索内容
                if (!TextUtils.isEmpty(dataBinding.etSearch.getText().toString())) {
                    dataBinding.etSearch.setText("");
                }
                break;
            case cn.xmzt.www.R.id.rl_left_group: // 群位置消息开启
                if (mChangeGroupList.size() > 0 ) { // 判断当前是否有获取到群信息
                    if (!TextUtils.isEmpty(mGroupId) && mIsOpenGroupLocation){ // 判断该当前是否有正在开启位置的群 有 直接进行切换
                        showMemberLocationChoiceDialog(); // 没有做是否只有一个的处理是因为当前只有一个群的时候还是可以打开这个选择dialog，可能他需要将功能关闭
                    } else { // 无 显示是否开启群成员位置
                        showMemberLocationOpenDialog();
                    }
                } else { // 获取到群信息为空
                    if (TextUtils.isEmpty(mToken)){ // 没有登录的情况
                        ToastUtils.showText(mContext, "请先登录");
                    } else { // 已经登录没有群信息
                        ToastUtils.showText(mContext, "暂无可出行的队伍，无法开启成员位置");
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 点击我的事件
     */
    private void setMyLocationData() {
        if (mAmap != null && getIsContain()) {
            if (mIsLocationSuccess) {
                LatLng latLng = new LatLng(Constants.mLatitude, Constants.mLongitude);
                mAmap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                // mAmap.moveCamera(CameraUpdateFactory.zoomTo(mClickMapZoom));
            } else {
                ToastUtils.showText(mContext, "定位失败，请打开GPS位置权限");
            }
        } else {
            ToastUtils.showText(mContext, "您当前不在景区内");
        }
    }

    /**
     * 界面类型的点击事件
     *
     * @param bean     传递过来的数据
     * @param position 传递过来的位置
     */
    public void OnClickListener(ScenicSpotMapTypeListBean bean, int position) {
        hideInputView();
        if (mPopAllType != null) {
            if (mPopAllType.isShowing())
                mPopAllType.dismiss();
        }
        if (position == 0){
            if (!TextUtils.isEmpty(dataBinding.etSearch.getText().toString())) {
                dataBinding.etSearch.setText("");
                return;
            }
        }
        if (!bean.getIs_selected()) { // 点击第一次
            for (int i = 0; i < mTypeList.size(); i++) {
                if (position == i) {
                    mTypeList.get(i).setIs_selected(true);
                    // 底部显示list的数据更新
                    bottomSetData(mTypeList.get(i).getId());
                    // marker的更新 这里是默认的加载第一条信息
                    addMarker(mTypeList.get(i).getId(), 0);
                } else {
                    mTypeList.get(i).setIs_selected(false);
                }
            }
            if (position != lastLabelIndex) {
                ScenicSpotMapTypeListBean lastBean = mTypeList.get(lastLabelIndex);
                lastBean.setIs_selected(false);
                mTypeAdapter.notifyItemChanged(lastLabelIndex, LabelAdapter.UPDATE_STATE);

                centerLayoutManager.smoothScrollToPosition(dataBinding.rvTypeList, new RecyclerView.State(), position);
                bean.setIs_selected(true);
                mTypeAdapter.notifyItemChanged(position, LabelAdapter.UPDATE_STATE);
            }
            lastLabelIndex = position;
            mAmap.moveCamera(CameraUpdateFactory.zoomTo(mMinMapZoom));
            if (position == 1) {
                if (dataBinding.rlBottomContent.getCurrentStatus() == ScrollLayout.Status.CLOSED || dataBinding.rlBottomContent.getCurrentStatus() == ScrollLayout.Status.EXIT) { // 打开的情况
                    dataBinding.rlBottomContent.setToOpen();
                    dataBinding.tvListMore.setVisibility(View.VISIBLE);
                }
            }
        } else { // 第二次点击操作
            // 需要底部显示的列表弹出一半进行界面的显示 如果底部界面当前处于显示状态就关闭 并且开始加载界面的marker
            if (dataBinding.rlBottomContent.getCurrentStatus() == ScrollLayout.Status.CLOSED || dataBinding.rlBottomContent.getCurrentStatus() == ScrollLayout.Status.OPENED) { // 打开的情况
                dataBinding.rlBottomContent.setToExit();
                dataBinding.tvListMore.setVisibility(View.VISIBLE);
            } else if (dataBinding.rlBottomContent.getCurrentStatus() == ScrollLayout.Status.EXIT) {
                dataBinding.rlBottomContent.setToOpen();
            }
        }
    }

    LatLngBounds mBounds = null;

    /**
     * 往地图上添加一个groundoverlay覆盖物
     */
    private void addOverlayToMap() {
        UiSettings uiSettings = mAmap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false); // 不显示控制zoom的按钮
        uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER); // logo显示在底部居中的位置
        uiSettings.setRotateGesturesEnabled(false); // 禁止地图旋转首饰
        uiSettings.setTiltGesturesEnabled(false); // 禁止倾斜手势
        ScenicSpotGuideBean.MapBgCoordinateListBean northEast = mInfo.getMapBgCoordinateList().get(0);
        ScenicSpotGuideBean.MapBgCoordinateListBean southWest = mInfo.getMapBgCoordinateList().get(1);
        // 加载自定义地图 TODO 更改了背景 *********************** 需要做成在线的
        /**
         * CustomMapStyleOptions.setStyleId(java.lang.String styleId)
         * 设置底图自定义样式对应的styleID，id从官网获取。
         */
//        CustomMapStyleOptions customMapStyleOptions = new CustomMapStyleOptions();
//        customMapStyleOptions.setStyleDataPath(Constants.ASSETS_MAP_PATH + "style.data");
//        customMapStyleOptions.setStyleExtraPath(Constants.ASSETS_MAP_PATH + "style_extra.data");
//        mAmap.setCustomMapStyle(customMapStyleOptions);
        // 加载自定义地图 TODO 更改了背景 ***********************
        mAmap.showMapText(false);// 是否显示地图上面的文字
        // 设置背景所在的区域
        mBounds = new LatLngBounds.Builder()
                .include(new LatLng(northEast.getLatitude(), northEast.getLongitude()))
                .include(new LatLng(southWest.getLatitude(), southWest.getLongitude())).build();
        //   需要设置滑动范围
        mAmap.setMapStatusLimits(mBounds);
        // 设置焦点到景点的中心位置
        mAmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng((northEast.getLatitude() + southWest.getLatitude()) / 2.0,
                (northEast.getLongitude() + southWest.getLongitude()) / 2.0), mInitMapZoom));
        if (!mMapBgImg.contains("http")) {
            AsyncUtil.async(new Function<String, Bitmap>() {
                @Override
                public Bitmap apply(String o) throws Exception {
                    Bitmap bitmap = null;
                    try {
                        bitmap = Glide.with(mContext)
                                .asBitmap()
                                .load(mMapBgImg)
                                .into(mMapBgWidth, mMapBgHeight)
                                .get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return bitmap;
                }
            }, new Consumer<Bitmap>() {
                @Override
                public void accept(Bitmap bitmap) throws Exception {
                    if (bitmap != null) {
                        groundoverlay = mAmap.addGroundOverlay(new GroundOverlayOptions()
                                .anchor(0.5f, 0.5f)
                                .transparency(0.1f)
                                .image(BitmapDescriptorFactory.fromBitmap(bitmap))
                                .positionFromBounds(mBounds));
                    }
                }
            });
        } else {
            AsyncUtil.async(new Function<String, Bitmap>() {
                @Override
                public Bitmap apply(String o) throws Exception {
                    Bitmap bitmap = null;
                    try {
                        bitmap = Glide.with(mContext)
                                .asBitmap()
                                .load(mMapBgImg)
                                .into(mMapBgWidth, mMapBgHeight)
                                .get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return bitmap;
                }
            }, new Consumer<Bitmap>() {
                @Override
                public void accept(Bitmap bitmap) throws Exception {
                    if (bitmap != null) {
                        groundoverlay = mAmap.addGroundOverlay(new GroundOverlayOptions()
                                .anchor(0.5f, 0.5f)
                                .transparency(0.1f)
                                .image(BitmapDescriptorFactory.fromBitmap(bitmap))
                                .positionFromBounds(mBounds));
                        // 保存到本地
                        saveBitmap(bitmap);
                    }
                }
            });
        }
        // 初始化的情况都默认传0  表示请求景点 position不起作用  只有在type为1 的时候posi tion才会有作用
        addMarker(0, 0);
        // marker 点击事件监听
        mAmap.setOnMarkerClickListener(mMarkerListener);
        // 设置显示的infoWindow
        mAmap.setInfoWindowAdapter(mAMapAllAdapter);

        if (!getIsContain()) {//您当前不在景区范围内 提示用户
            ToastUtils.showText(mContext, "您当前不在景区范围内");
        }
    }

    private String mMapBgImg = "";

    private void setData() {
        // 获取到的数据 进行显示在界面上
        mMapBgWidth = getResources().getDisplayMetrics().widthPixels;
        mMapBgHeight = mMapBgWidth;
        // 顶部title更新
        dataBinding.topTitleTv.setText(mInfo.getScenicName());
        String path = Constants.ASSETS_IMG_PATH + MD5Utils.getMd5Value(mInfo.getMapBgUrl())+ ".png";
        File file = new File(path);
        if (file.exists()) {
            mMapBgImg = path;
        } else {
            mMapBgImg = mInfo.getMapBgUrl();
        }
        if (mAmap != null) {
            addOverlayToMap();
            if (getIsContain()) { // 未在景区范围内不进行定位显示
                setUpMapLocationMine();
            }
        }
        // 其他列表数据
        if (mInfo.getScenicServicePointList() != null) {
            if (mInfo.getScenicServicePointList().size() > 0) {
                List<ScenicSpotGuideBean.ScenicServicePointListBean.PointListBean> listother = mInfo.getScenicServicePointList().get(0).getPointList();
                listother.get(0).setIs_Checked(true);
                mBottomOtherAdapter.setDatas(listother);
            }
            if (mInfo.getScenicSpotList().size() > 0) {
                // 景点列表数据
                mBottomScenicSpotAdapter.setDatas(mInfo.getScenicSpotList());
            }
            if (mInfo.getScenicLineList().size() > 0) {
                // 路线列表数据
                List<ScenicSpotGuideBean.ScenicLineListBean> list = mInfo.getScenicLineList();
                list.get(0).setIs_Checked(true);
                mBottomLineAdapter.setDatas(list);
            }
        }

        // 类型数据的获取
        mTypeList = viewModel.getTypeList(mInfo);
        mTypeAdapter.setData(mTypeList);
        // 界面默认是先显示dialog控件 等dialog消失的时候 界面的控件出现
        showScenicContentDialog();

        dataBinding.setItem(mInfo);

        if (mVoiceList != null && mVoiceList.size()>1){
            mDownLoadType = 3;
        } else {
            if (mPackageInfo != null) {
                if (viewModel.getIsScenicVoicePackageToDataBase(mContext, mPackageInfo)) {
                    viewModel.deleteScenicVoicePackageToDataBase(mContext,mPackageInfo);
                }
            }
            mDownLoadType = 0;
        }
        getAutoPlaying();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataBinding.mvMap.onResume();
        // 用户在当前群内的监听，可能被移除当前群
        registerTeamUpdateObserver(true);
        mIsActivity = true;
        mIsClickImages = false;
        if (mLocationTimer == null) {
            // 在数据加载成功之后 每5秒去获取一次经纬度 看看是否有可以自动播放的音频文件
            setTimerPer5SecondsOptions();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataBinding.mvMap.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (mLocationTimer != null) {
            mLocationTimer.cancel();
            mLocationTimer.purge();
            mLocationTimer = null;
        }
        mVoicePlayerPosition = -2;
        mInfoWindowAnim = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 群内监听
        registerTeamUpdateObserver(false);
        // 需要将这个置空 下一次进来才会正常加载
        setOnDestoryNullData();
        dataBinding.mvMap.onDestroy();
        // clear all member cache information 群内位置信息的监听
        TeamLocationProfile.getInstance().registerObserver(false);
        // TODO 双击的情况下不显示群对讲信息
        TalkManage.isShowSelectGroup = true;
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        dataBinding.mvMap.onSaveInstanceState(outState);
    }

    /**
     * 往地图上添加marker
     */
    public void addMarkersToMap(Context context, LatLng latlng, String title, int icon, int i,boolean isSpot) {
        if (mAmap != null) {
            View view = View.inflate(context, cn.xmzt.www.R.layout.marker_show, null);
            TextView textView = (TextView) view.findViewById(cn.xmzt.www.R.id.tv_marker_show);
            ImageView imageView = (ImageView) view.findViewById(cn.xmzt.www.R.id.iv_marker_show);
            imageView.setImageResource(icon);
            if ("".equals(title) || title == null) {
                textView.setVisibility(View.GONE);
            } else {
                textView.setVisibility(View.VISIBLE);
            }
            textView.setText(title);
            Bitmap bitmap = BitmapUtils.convertViewToBitmap(view);
            int radius = DensityUtil.dip2px(context, isSpot ? 200 : 145); // TODO 60 是正常情况 200  仅仅对景区景点的设置底部问题 145 对除了景区的其他类型的显示
            markerOption = new MarkerOptions()
                    .position(latlng)
                    .title(i + "")
                    .snippet(isSpot ? SPOT_TYPE : OTHER_TYPE)
                    .setInfoWindowOffset(0, radius) //  需要慢慢调试情况
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
            Marker marker = mAmap.addMarker(markerOption);
            // 将marker保存 方便后面清除
            mAllMarker.add(marker);
            // 释放资源
            bitmap.recycle();
        }
    }

    /**
     * 往地图上添加线路marker
     */
    public void addLineMarkersToMap(Context context, LatLng latlng, String title) {
        if (mAmap != null) {
            View view = View.inflate(context, R.layout.marker_show_line, null);
            TextView textView = (TextView) view.findViewById(cn.xmzt.www.R.id.line_tv);
            if ("".equals(title) || title == null) {
                return;
            }
            textView.setText(title);
            Bitmap bitmap = BitmapUtils.convertViewToBitmap(view);
            markerOption = new MarkerOptions()
                    .position(latlng)
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
            Marker marker = mAmap.addMarker(markerOption);
            // 将marker保存 方便后面清除
            mAllLineMarker.add(marker);
        }
    }

    private boolean mIsClickImages = false;

    /**
     * 内部点击事件 eg.popupwindow的点击事件
     */
    private void initClickListener() {
        // pop 点击事件
        mPopRightAdd.setMapRightAddPopupWindowOnClick(new ScenicSpotMapRightAddPopupWindow.ScenicSpotMapRightAddPopupWindowOnClick() {
            @Override
            public void imagesClick() {
                if (mInfo != null) {
                    if (mInfo.getScenicSpotList() != null) {
                        if (!mIsClickImages) {
                            mIsClickImages = true;
                            ArrayList<String> list = new ArrayList<>();
                            for (ScenicSpotGuideBean.ScenicSpotListBean bean : mInfo.getScenicSpotList()) {
                                list.add(bean.getPhotoUrl());
                            }
                            // 图集
                            IntentManager.getInstance().goAtlasActivity(mContext, list);
                            mPopRightAdd.dismiss();
                        }
                    } else {
//                        ToastUtils.showText(mContext, "获取数据异常");
                    }
                } else {
//                    ToastUtils.showText(mContext, "获取数据异常");
                }
            }

            @Override
            public void shareClick() {
                // 分享
                if (mInfo != null) {
                    if (TourApplication.getUser() != null) {
                        String url = "https://g.xmzt.cn/g/g?t=4&i=" + mInfo.getScenicId() + "&p=" + TourApplication.getUser().getRefCode();
                        ShareFunction.getInstance().showWebShareAction(ScenicSpotMapActivity.this, mInfo.getScenicName(), mInfo.getPhotoUrl(),
                                mInfo.getIntro(), url, ShareFunction.SHARE_WEIXIN);
                    } else {
                        ToastUtils.showText(mContext, "用户未登录，分享失败");
                    }
                } else {
                    ToastUtils.showText(mContext, "获取景区信息失败，分享失败");
                }
                mPopRightAdd.dismiss();
            }

            @Override
            public void feedBackClick() {
                if (mInfo != null) {
                    if (mInfo.getScenicName() != null) {
                        ArrayList<String> list = new ArrayList<>();
                        for (int i = 0;i<mInfo.getScenicSpotList().size();i++){
                            list.add(mInfo.getScenicSpotList().get(i).getName());
                        }
                        // 反馈 跳转到建议与反馈界面
                        IntentManager.getInstance().goScenicFeedBackActivity(mContext, getIntent().getLongExtra("id", 0), mInfo.getScenicName(),list);
                        mPopRightAdd.dismiss();
                    } else {
                        ToastUtils.showText(mContext, "获取数据异常");
                    }
                } else {
                    ToastUtils.showText(mContext, "获取数据异常");
                }
            }
        });

        dataBinding.mvMap.getMap().setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent event) {
                if (dataBinding.rlBottomContent.getCurrentStatus() == ScrollLayout.Status.OPENED || dataBinding.rlBottomContent.getCurrentStatus() == ScrollLayout.Status.CLOSED) {
                    dataBinding.rlBottomContent.setToExit();
                    dataBinding.tvListMore.setVisibility(View.VISIBLE);
                }
                // 点击地图的时候隐藏其他已经显示的infowindow
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        DownX = event.getX();//float DownX
                        DownY = event.getY();//float DownY
                        moveX = 0;
                        moveY = 0;
                        currentMS = System.currentTimeMillis();//long currentMS     获取系统时间
                        break;
                    case MotionEvent.ACTION_MOVE:
                        moveX += Math.abs(event.getX() - DownX);//X轴距离
                        moveY += Math.abs(event.getY() - DownY);//y轴距离
                        DownX = event.getX();
                        DownY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        long moveTime = System.currentTimeMillis() - currentMS;//移动时间
                        //判断是否继续传递信号
                        if(moveTime>200&&(moveX>20||moveY>20)){ // 滑动事件
                        } else {
                            hideShowMarker();
                        }
                        break;
                }
            }
        });
        dataBinding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    //点击搜索要做的操作
                    hideInputView();
                    // 更新界面
                    updateSearch(dataBinding.etSearch.getText().toString());
                }
                return false;
            }
        });
        dataBinding.etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) { // 景点搜索框获取到焦点的时候 如果底部状态没有在最顶部就显示在最顶部
                    if (dataBinding.rlBottomContent.getCurrentStatus() != ScrollLayout.Status.CLOSED){
                        dataBinding.rlBottomContent.setToClosed();
                    }
                } else {
                }
            }
        });
    }

    private void updateSearch(String keyword){
        if (TextUtils.isEmpty(keyword)){
            mBottomScenicSpotAdapter.setDatas(mInfo.getScenicSpotList());
            dataBinding.tvEmpty.setVisibility(View.GONE);
        } else {
            mSpotListSearchList.clear();
            for (int i = 0;i < mInfo.getScenicSpotList().size(); i++){
                ScenicSpotGuideBean.ScenicSpotListBean bean = mInfo.getScenicSpotList().get(i);
                if (bean.getName().contains(keyword)){
                    mSpotListSearchList.add(bean);
                }
            }
            if (mSpotListSearchList.size() == 0){
                dataBinding.tvEmpty.setVisibility(View.VISIBLE);
            } else {
                dataBinding.tvEmpty.setVisibility(View.GONE);
            }
            mBottomScenicSpotAdapter.setDatas(mSpotListSearchList);
        }
    }

    private float DownX = 0;
    private float moveX = 0;
    private float DownY = 0;
    private float moveY = 0;
    private long currentMS = 0;

    private void hideShowMarker(){
        if (mCurrentMarker != null) {
            mCurrentMarker.hideInfoWindow();
            mCurrentMarker = null;
        } else if (mCurrentOtherMarker != null){
            mCurrentOtherMarker.hideInfoWindow();
            mCurrentOtherMarker = null;
        } else if (mCurrentMemMarker != null) {
            if (PERSON_TYPE.equals(mCurrentMemMarker.getSnippet())) {
                mCurrentMemMarker.startAnimation();
                setNotClickedMarkerAnim();
                mCurrentMemMarker.hideInfoWindow();
                mCurrentMemMarker = null;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // TODO 双击的情况下不显示群对讲信息
        TalkManage.isShowSelectGroup = false;
        if (Constants.mServiceIsStart && Constants.mScenicId == (mInfo == null ? getIntent().getLongExtra("id", 0) : mInfo.getScenicId())) { // 开启动画
            // 判断当前是否有音频播放事件
            dataBinding.rlVoiceStartPlayShow.setVisibility(View.VISIBLE);
            dataBinding.tvVoiceContent.setText(Constants.mServiceIsStartConteng);
            AnimationDrawable animationDrawable;
            animationDrawable = (AnimationDrawable) dataBinding.playShowIv.getBackground();
            animationDrawable.start();
        }
    }

    private int mBottomType = 0; // 0 景点 1 路线 2 其他

    /**
     * 当前界面底部list显示的数据更新之类的
     *
     * @param type 传递一个数据类型
     */
    private void bottomSetData(int type) {
        // 分为 0 景点 1 路线 其他
        if (type == 0) { // 景点
            mBottomType = 0;
            dataBinding.itemLayout.setVisibility(View.VISIBLE);
            dataBinding.rvBottomListLine.setVisibility(View.GONE);
            dataBinding.rvBottomListOther.setVisibility(View.GONE);
            showListMore();
        } else if (type == 1) { // 路线
            mBottomType = 1;
            dataBinding.itemLayout.setVisibility(View.GONE);
            dataBinding.rvBottomListLine.setVisibility(View.VISIBLE);
            dataBinding.rvBottomListOther.setVisibility(View.GONE);
            showListMore();
        } else { // 其他
            mBottomType = 2;
            dataBinding.itemLayout.setVisibility(View.GONE);
            dataBinding.rvBottomListLine.setVisibility(View.GONE);
            dataBinding.rvBottomListOther.setVisibility(View.VISIBLE);

            List<ScenicSpotGuideBean.ScenicServicePointListBean.PointListBean> listBeans = null;
            for (int i = 0; i < mInfo.getScenicServicePointList().size(); i++) {
                if (type == mInfo.getScenicServicePointList().get(i).getType()) {
                    listBeans = mInfo.getScenicServicePointList().get(i).getPointList();
                    // 获取到当前信息就跳出循环
                    break;
                }
            }
            // 方面下面点击时候使用
            mOtherInfo = listBeans;
            for (int i = 0;i< mOtherInfo.size();i++){
                mOtherInfo.get(i).setIs_Checked(false);
            }
            listBeans.get(0).setIs_Checked(true);
            mBottomOtherAdapter.setDatas(listBeans);
            showListMore();
        }
    }

    private void showListMore() {
        if (mBottomType != 0) {
            if (dataBinding.tvListMore.getVisibility() == View.GONE) {
                dataBinding.tvListMore.setVisibility(View.VISIBLE);
                dataBinding.tvListMoreLine.setVisibility(View.VISIBLE);
                setBottomPadding(true);
            }
        } else {
            if (dataBinding.rlBottomContent.getCurrentStatus() != ScrollLayout.Status.EXIT) {
                if (dataBinding.tvListMore.getVisibility() == View.VISIBLE) {
                    dataBinding.tvListMore.setVisibility(View.GONE);
                    dataBinding.tvListMoreLine.setVisibility(View.GONE);
                    setBottomPadding(false);
                }
            }
        }
        if (dataBinding.rlBottomContent.getCurrentStatus() != ScrollLayout.Status.EXIT) {
            if (mBottomType == 1) {
                dataBinding.tvListMore.setText("全部信息（" + mInfo.getScenicLineList().size() + "）");
            } else if (mBottomType == 2) {
                dataBinding.tvListMore.setText("全部信息（" + mOtherInfo.size() + "）");
            } else {
                dataBinding.tvListMore.setText("上滑查看更多结果");
            }
        }
    }

    private void setBottomPadding(boolean flag) {
        if (flag) { // 显示
            dataBinding.rlB.setPadding(0, mBottomType != 0 ? DpUtil.dp2px(mContext, 50) : 0, 0, 0);
        } else { // 隐藏
            dataBinding.rlB.setPadding(0, 0, 0, 0);
        }
    }

    /**
     * 将原来地图上有的marker清空 并且添加新类型的marker
     *
     * @param type     传递一个景区类型
     * @param position 主要用作绘制路线的情况
     */
    private void addMarker(int type, int position) {
        // 清除所有的路线 不管是不是类型1 都需要清空路线
        clearAllLinePolyLine();
        // 清除所有的marker
        clearAllMarker();
        int icon = LocalDataUtils.getMarkerIcon(type);

        if (type == 0) { // 设置所有景点的marker
            // 设置整个景区的景点mark
            for (int i = 0; i < mInfo.getScenicSpotList().size(); i++) {
                // 添加景点的marker
                addMarkersToMap(this, new LatLng(mInfo.getScenicSpotList().get(i).getLonLat().getLatitude(),
                                mInfo.getScenicSpotList().get(i).getLonLat().getLongitude()), mInfo.getScenicSpotList().get(i).getName(),
                        icon, i,true);
            }

            if (mBottomType == 0 && mIsShowPlayingMarker){
                mIsShowPlayingMarker = false;
                showPlayingMarkerInfoWindow();
            }

        } else if (type == 1) { // 设置所有路线的marker
            // 设置整个景区的景点mark
            for (int i = 0; i < mInfo.getScenicSpotList().size(); i++) {
                // 添加景点的marker
                addMarkersToMap(this, new LatLng(mInfo.getScenicSpotList().get(i).getLonLat().getLatitude(),
                                mInfo.getScenicSpotList().get(i).getLonLat().getLongitude()), mInfo.getScenicSpotList().get(i).getName(),
                        icon, i,true);
            }
            // 设置整个景区的景点mark
            for (int i = 0; i < mInfo.getScenicLineList().get(position).getCoordinateList().size(); i++) { // 默认的显示的是第一条路径
                // 添加景点的路线marker
                addLineMarkersToMap(this, new LatLng(mInfo.getScenicLineList().get(position).getCoordinateList().get(i).getLatitude(),
                                mInfo.getScenicLineList().get(position).getCoordinateList().get(i).getLongitude()),
                        mInfo.getScenicLineList().get(position).getCoordinateList().get(i).getLabel());
            }
            setRouteInfo(mInfo.getScenicLineList().get(position).getCoordinateList());
        } else { // 其他
            List<ScenicSpotGuideBean.ScenicServicePointListBean.PointListBean> pointList = new ArrayList<>();
            for (int i = 0; i < mInfo.getScenicServicePointList().size(); i++) {
                if (type == mInfo.getScenicServicePointList().get(i).getType()) {
                    pointList = mInfo.getScenicServicePointList().get(i).getPointList();
                }
            }
            // 设置整个景区的其他mark
            for (int i = 0; i < pointList.size(); i++) {
                // 添加景点其他的marker
                addMarkersToMap(this, new LatLng(pointList.get(i).getLonLat().getLatitude(),
                                pointList.get(i).getLonLat().getLongitude()), pointList.get(i).getName(),
                        icon, i,false);
            }
        }
    }

    List<ScenicSpotGuideBean.ScenicServicePointListBean.PointListBean> mOtherInfo = null;

    /**
     * 绘制景区的路线
     */
    private void setRouteInfo(List<ScenicSpotGuideBean.ScenicLineListBean.CoordinateListBean> data) {
        List<LatLng> list = listToLatLng(data);
        mAllLinePolyline = mAmap.addPolyline((new PolylineOptions())
                //集合数据
                .addAll(list)
                //线的宽度
                .width(20)
                .setDottedLine(false) // 关闭虚线
                .geodesic(true)
                //颜色
                .color(Color.argb(255, 36, 164, 255)));
    }

    /**
     * 将后台返回的数据转为坐标点
     *
     * @param data
     * @return
     */
    private List<LatLng> listToLatLng(List<ScenicSpotGuideBean.ScenicLineListBean.CoordinateListBean> data) {
        List<LatLng> info = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            info.add(new LatLng(data.get(i).getLatitude(), data.get(i).getLongitude()));
        }
        return info;
    }

    /**
     * 清除所有Marker
     */
    private void clearAllMarker() {
        for (Marker marker : mAllMarker) {
            marker.remove();
        }
        for (Marker marker : mAllLineMarker) {
            marker.remove();
        }
        mAllMarker.clear();
        mAllLineMarker.clear();
    }

    /**
     * 清除所有线路PolyLine
     */
    private void clearAllLinePolyLine() {
        if (mAllLinePolyline != null)
            mAllLinePolyline.remove();
    }

    /**
     * 界面底部列表的点击事件
     *
     * @param data     当前列表的所有数据
     * @param position 点击当前的位置
     * @param type     点击的类型 0 景点 1 路线 2 其他 3 详请 4 解说
     */
    public void OnBottomClickListener(List data, int position, int type) {
        LatLng latLng = null;
        if (type == 0) {
            // 景点
            List<ScenicSpotGuideBean.ScenicSpotListBean> list = data;
            // 在界面上面显示
            Marker marker = mAllMarker.get(position);
            // 可以实现连接到地图 只是不会更换中心点
            marker.showInfoWindow();
            marker.setToTop();
            mCurrentMarker = marker;
            latLng = new LatLng(list.get(position).getLonLat().getLatitude(), list.get(position).getLonLat().getLongitude());
        } else if (type == 1) {
            // 路线
            List<ScenicSpotGuideBean.ScenicLineListBean> list = data;
            // 更换路线 界面绘制
            addMarker(1, position);
            // 地图adapter的更新
            List<ScenicSpotGuideBean.ScenicLineListBean> listBeans = mInfo.getScenicLineList();
            for (int i = 0; i < listBeans.size(); i++) {
                if (position == i) {
                    listBeans.get(i).setIs_Checked(true);
                } else {
                    listBeans.get(i).setIs_Checked(false);
                }
            }
            mBottomLineAdapter.setDatas(listBeans);
        } else if (type == 2) {
            // 其他
            List<ScenicSpotGuideBean.ScenicServicePointListBean.PointListBean> list = data;
            // 在界面上面显示
            Marker marker = mAllMarker.get(position);
            // 可以实现连接到地图 只是不会更换中心点
            marker.showInfoWindow();
            marker.setToTop();
            mCurrentOtherMarker = marker;
            latLng = new LatLng(list.get(position).getLonLat().getLatitude(), list.get(position).getLonLat().getLongitude());

            List<ScenicSpotGuideBean.ScenicServicePointListBean.PointListBean> listBeans = mOtherInfo;
            for (int i = 0; i < listBeans.size(); i++) {
                if (position == i) {
                    listBeans.get(i).setIs_Checked(true);
                } else {
                    listBeans.get(i).setIs_Checked(false);
                }
            }
            mBottomOtherAdapter.setDatas(listBeans);
        } else if (type == 3) {
            // 详请
            List<ScenicSpotGuideBean.ScenicSpotListBean> list = data;
            setClickDetail(list.get(position), position);
            latLng = new LatLng(list.get(position).getLonLat().getLatitude(), list.get(position).getLonLat().getLongitude());
        } else if (type == 4) {
            // 解说
            List<ScenicSpotGuideBean.ScenicSpotListBean> list = data;
            // 在界面上面显示 这里要更新界面上的数据 不知道这样是否可以实现
            Marker marker = mAllMarker.get(position);
            // 可以实现连接到地图 只是不会更换中心点
            marker.showInfoWindow();
            marker.setToTop();
            mCurrentMarker = marker;
            latLng = new LatLng(list.get(position).getLonLat().getLatitude(), list.get(position).getLonLat().getLongitude());
        }
        if (type != 1) {
            mAmap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        }
        if (dataBinding.rlBottomContent.getCurrentStatus() == ScrollLayout.Status.OPENED || dataBinding.rlBottomContent.getCurrentStatus() == ScrollLayout.Status.CLOSED){
            dataBinding.rlBottomContent.setToExit();
        }
    }

    /**
     * 详请的点击事件
     *
     * @param bean
     */
    private void setClickDetail(ScenicSpotGuideBean.ScenicSpotListBean bean, int position) {
        if (!TextUtils.isEmpty(mVoiceFileName)) {
            String filePath = mVoiceFileName + "/" + bean.getId() + ".mp3";
            String filePathCurrent = mVoiceFileName + "/" + Constants.mScenicSpotId + ".mp3";
            // 详请 进入到新的界面  详请界面
            if (mVoiceList != null && mVoiceList.size() > 1) {
                IntentManager.getInstance().goScenicSpotDetailActivity(mContext,
                        mInfo.getScenicId(), // 景区编号
                        bean.getId(), // 景点id
                        bean.getName(), // 景点名称
                        bean.getPhotoUrl(), // 景点banner
                        bean.getDescription(), // 景点简介
                        filePath,// 景点音频地址
                        bean.getLonLat().getLatitude(),//经纬度
                        bean.getLonLat().getLongitude(),
                        mVoiceFileName
                );
            } else {
                IntentManager.getInstance().goScenicSpotDetailActivity(mContext,
                        mInfo.getScenicId(), // 景区编号
                        bean.getId(), // 景点id
                        bean.getName(), // 景点名称
                        bean.getPhotoUrl(), // 景点banner
                        bean.getDescription(), // 景点简介
                        bean.getScenicResource().getResUrl(),// 景点音频地址
                        bean.getLonLat().getLatitude(),//经纬度
                        bean.getLonLat().getLongitude(),
                        TextUtils.isEmpty(mVoiceFileName) ? "" : mVoiceFileName
                );
            }
        } else {
            IntentManager.getInstance().goScenicSpotDetailActivity(mContext,
                    mInfo.getScenicId(), // 景区编号
                    bean.getId(), // 景点id
                    bean.getName(), // 景点名称
                    bean.getPhotoUrl(), // 景点banner
                    bean.getDescription(), // 景点简介
                    bean.getScenicResource().getResUrl(),// 景点音频地址
                    bean.getLonLat().getLatitude(),//经纬度
                    bean.getLonLat().getLongitude(),
                    TextUtils.isEmpty(mVoiceFileName) ? "" : mVoiceFileName
            );
        }
        mIsActivity = false;
    }

    /**
     * 解说的点击事件
     *
     * @param bean
     */
    private void setClickListen(ScenicSpotGuideBean.ScenicSpotListBean bean, int position) {
        // 首先 判断当前是否在播放状态中
        if (mVoicePlayerPosition == -2) { // 表示当前没有播放文件
            playVoiceData(position);
        } else { // 表示这个界面有发过播放事件
            if (mVoicePlayerPosition == position) { // 判断当前是否是正在播放的控件点击事件
                if (PlayerService.mMediaPlayer.isPlaying()) { // 判断当前是否正在播放
                    // 需要暂停事件
                    pauseVoiceData();
                } else {
                    // 播放
                    playVoiceData(position);
                }
            } else { // 表示点击的不是当前数据的播放事件
                playVoiceData(position);
            }
        }
    }
    /**
     * 解说的点击事件
     */
    private void setClickListenAll() {
        if (dataBinding.rlBottomContent.getCurrentStatus() == ScrollLayout.Status.CLOSED || dataBinding.rlBottomContent.getCurrentStatus() == ScrollLayout.Status.EXIT) { // 打开的情况
            dataBinding.rlBottomContent.setToOpen();
            dataBinding.tvListMore.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置marker的点击事件
     */
    AMap.OnMarkerClickListener mMarkerListener = new AMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            if (mCurrentMemMarker == marker) {
                return false;
            }
            // 判断当前点击是否是人员的marker
            if (PERSON_TYPE.equals(marker.getSnippet())) {
                if (mCurrentMemMarker != null) {
                    mCurrentMemMarker.startAnimation();
                    setNotClickedMarkerAnim();
                }
                mCurrentMemMarker = marker;
                marker.startAnimation();
                marker.showInfoWindow();
                setClickedMarkerAnim();
            } else {
                if (mCurrentMemMarker != null) {
                    mCurrentMemMarker.startAnimation();
                    setNotClickedMarkerAnim();
                }
                if (mBottomType < 2) {
                    mCurrentMarker = marker; // 景点
                } else { // 其他
                    mCurrentOtherMarker = marker;
                }
                if (marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                } else {
                    marker.showInfoWindow();
                }
            }
            return true; // 返回:true 表示点击marker 后marker 不会移动到地图中心；返回false 表示点击marker 后marker 会自动移动到地图中心
        }
    };

    AnimationDrawable mInfoWindowAnim = null;

    /**
     *  获取infowindow应该显示为位置
     * @return type 0 表示不做更改 1 顶部 2 顶部左边 3 顶部右边 4 右边 5 左边 6 底部左边 7 顶部右边 8 底部
     */
    private int showInfoWindowSpotLTRB(Marker marker){
        Point markerPoint = mAmap.getProjection().toScreenLocation(marker.getPosition()); // 返回一个从地图位置转换来的屏幕位置
        int type = 0;
        int screen_X = getResources().getDisplayMetrics().widthPixels;
        int screen_Y = getResources().getDisplayMetrics().heightPixels;
        int marker_x = markerPoint.x;
        int marker_y = markerPoint.y;
        int view_x = DpUtil.dp2px(mContext,152)/2; // infowindow的宽度
        int view_y = DpUtil.dp2px(mContext,108)/2; // infowindow的高度
        int top_y = DpUtil.dp2px(mContext,103);
        int Bottom_y = DpUtil.dp2px(mContext,150);
        if (((screen_X-marker_x) < view_x)){ // 横向判断当前marker位置是否是在右边 表示在右边
            // 当前已经在右边了 判断有没有在顶部
            if ((screen_Y - marker_y) < (view_y + Bottom_y)){ // 当前在底部 右边底部
                type = 7;
            } else if ((marker_y-top_y) < view_y){ // 当前在顶部 右边顶部
                type = 3;
            } else { // 右边
                type = 4;
            }
        } else if (marker_x < view_x){ // 当前在左边
            if ((screen_Y - marker_y) < (view_y + Bottom_y)){ // 当前在底部 左边底部
                type = 6;
            } else if ((marker_y-top_y) < view_y){ // 当前在顶部 左边顶部
                type = 2;
            } else { // 左边
                type = 5;
            }
        } else if ((marker_y-top_y) < view_y){ // 当前在顶部 顶部
            type = 1;
        } else if ((screen_Y - marker_y) < (view_y + Bottom_y)){ // 当前在底部 底部
            type = 8;
        } else { //不做操作
            type = 0;
        }
        return type;
    }
    /**
     *  获取infowindow应该显示为位置
     * @return type 0 表示不做更改 1 顶部 2 顶部左边 3 顶部右边 4 右边 5 左边 6 底部左边 7 顶部右边 8 底部
     */
    private int showInfoWindowOtherLTRB(Marker marker){
        Point markerPoint = mAmap.getProjection().toScreenLocation(marker.getPosition()); // 返回一个从地图位置转换来的屏幕位置
        int type = 0;
        int screen_X = getResources().getDisplayMetrics().widthPixels;
        int screen_Y = getResources().getDisplayMetrics().heightPixels;
        int marker_x = markerPoint.x;
        int marker_y = markerPoint.y;
        int view_x = DpUtil.dp2px(mContext,140)/2; // infowindow的宽度
        int view_y = DpUtil.dp2px(mContext,42)/2; // infowindow的高度
        int top_y = DpUtil.dp2px(mContext,103);
        int Bottom_y = DpUtil.dp2px(mContext,150);
        if (((screen_X-marker_x) < view_x)){ // 横向判断当前marker位置是否是在右边 表示在右边
            // 当前已经在右边了 判断有没有在顶部
            if ((screen_Y - marker_y) < (view_y + Bottom_y)){ // 当前在底部 右边底部
                type = 7;
            } else if ((marker_y-top_y) < view_y){ // 当前在顶部 右边顶部
                type = 3;
            } else { // 右边
                type = 4;
            }
        } else if (marker_x < view_x){ // 当前在左边
            if ((screen_Y - marker_y) < (view_y + Bottom_y)){ // 当前在底部 左边底部
                type = 6;
            } else if ((marker_y-top_y) < view_y){ // 当前在顶部 左边顶部
                type = 2;
            } else { // 左边
                type = 5;
            }
        } else if ((marker_y-top_y) < view_y){ // 当前在顶部 顶部
            type = 1;
        } else if ((screen_Y - marker_y) < (view_y + Bottom_y)){ // 当前在底部 底部
            type = 8;
        } else { //不做操作
            type = 0;
        }
        return type;
    }


    /**
     * 设置marker点击之后显示的界面
     */
    AMap.InfoWindowAdapter mAMapAllAdapter = new AMap.InfoWindowAdapter() {
        @Override
        public View getInfoWindow(Marker marker) {
            View infoContent = getLayoutInflater().inflate(R.layout.marker_click_other_type, null);
            if (marker.getSnippet().equals(PERSON_TYPE)){ // 人员marker的显示
                infoContent = getLayoutInflater().inflate(R.layout.infowindow_scenic_spot_person, null);
                if (marker.getTitle().contains(".")){
                    infoContent = getLayoutInflater().inflate(R.layout.infowindow_scenic_spot_person_offline, null);
                }
            } else if (marker.getSnippet().equals(OTHER_TYPE)){ // 其他marker的点击显示
                infoContent = getLayoutInflater().inflate(R.layout.marker_click_other_type, null);
            } else if (marker.getSnippet().equals(SPOT_TYPE)){ // 景点marker的点击显示
                infoContent = getLayoutInflater().inflate(R.layout.marker_click_spot_type, null);
            }
            render(marker, infoContent);
            return infoContent;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View infoContent = getLayoutInflater().inflate(R.layout.marker_click_other_type, null);
            if (marker.getSnippet().equals(PERSON_TYPE)){ // 人员marker的显示
                infoContent = getLayoutInflater().inflate(R.layout.infowindow_scenic_spot_person, null);
                if (marker.getTitle().contains(".")){
                    infoContent = getLayoutInflater().inflate(R.layout.infowindow_scenic_spot_person_offline, null);
                }
            } else if (marker.getSnippet().equals(OTHER_TYPE)){ // 其他marker的点击显示
                infoContent = getLayoutInflater().inflate(R.layout.marker_click_other_type, null);
            } else if (marker.getSnippet().equals(SPOT_TYPE)){ // 景点marker的点击显示
                infoContent = getLayoutInflater().inflate(R.layout.marker_click_spot_type, null);
            }
            render(marker, infoContent);
            return infoContent;
        }

        private void render(Marker marker, View view) {
            if (marker.getSnippet().equals(OTHER_TYPE)) { // 其他
                // 界面的绘制加数据 当前是景点显示的infowindow 60 是正常情况 145
                RelativeLayout rl_all_info = view.findViewById(R.id.rl_all_info);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(rl_all_info.getLayoutParams());
                int view_x = DpUtil.dp2px(mContext, 184); // infowindow的宽度
                int bottom = DpUtil.dp2px(mContext, 100); // 正常情况下底部偏移量问题
                int padding_view = DpUtil.dp2px(mContext, 34) * 2; // 正常情况下底部偏移量问题
                // 判断当前是上下左右哪一个地方的显示
                int type = showInfoWindowOtherLTRB(marker); // 1 marker 在屏幕的左边 2 marker 在屏幕的上面 3 marker 在屏幕的右边 4 marker 在屏幕的底部 5 左上  6 左下 7 右上  8  右下
                if (type == 1) { // top // 需要显示在顶部中间位置
                    lp.setMargins(0, 0, 0, 0);
                    rl_all_info.setPadding(DpUtil.dp2px(mContext,22),DpUtil.dp2px(mContext,38),DpUtil.dp2px(mContext,22),DpUtil.dp2px(mContext,10));
                    rl_all_info.setBackgroundResource(R.drawable.marker_other_top_center);
                } else if (type == 2) { // topleft // 顶部左边 需要显示在顶部最左边位置
                    lp.setMargins(view_x - padding_view, 0, 0, 0);
                    rl_all_info.setPadding(DpUtil.dp2px(mContext,22),DpUtil.dp2px(mContext,38),DpUtil.dp2px(mContext,22),DpUtil.dp2px(mContext,10));
                    rl_all_info.setBackgroundResource(R.drawable.marker_other_top_left);
                } else if (type == 3) { // topright // 顶部右边 需要显示在顶部最右边位置
                    lp.setMargins(0, 0, view_x - padding_view, 0);
                    rl_all_info.setPadding(DpUtil.dp2px(mContext,22),DpUtil.dp2px(mContext,38),DpUtil.dp2px(mContext,22),DpUtil.dp2px(mContext,10));
                    rl_all_info.setBackgroundResource(R.drawable.marker_other_top_right);
                } else if (type == 4) { // right // 右边 需要显示在右边中间位置
                    lp.setMargins(0, 0, view_x, bottom / 2);
                    rl_all_info.setPadding(DpUtil.dp2px(mContext,16),DpUtil.dp2px(mContext,28),DpUtil.dp2px(mContext,42),DpUtil.dp2px(mContext,24));
                    rl_all_info.setBackgroundResource(R.drawable.marker_other_right_center);
                } else if (type == 5) { // left // 左边 需要显示在左边中间位置
                    lp.setMargins(view_x, 0, 0, bottom / 2);
                    rl_all_info.setPadding(DpUtil.dp2px(mContext,42),DpUtil.dp2px(mContext,28),DpUtil.dp2px(mContext,16),DpUtil.dp2px(mContext,24));
                    rl_all_info.setBackgroundResource(R.drawable.marker_other_left_center);
                } else if (type == 6) { // leftBotttom // 底部左边 需要显示在底部最左边位置
                    lp.setMargins(view_x - padding_view, 0, 0, bottom);
                    rl_all_info.setPadding(DpUtil.dp2px(mContext,22),DpUtil.dp2px(mContext,18),DpUtil.dp2px(mContext,22),DpUtil.dp2px(mContext,34));
                    rl_all_info.setBackgroundResource(R.drawable.marker_other_bottom_left);
                } else if (type == 7) { // rightBottom // 底部右边 需要显示在底部最右边位置
                    lp.setMargins(0, 0, view_x - padding_view, bottom);
                    rl_all_info.setPadding(DpUtil.dp2px(mContext,22),DpUtil.dp2px(mContext,18),DpUtil.dp2px(mContext,22),DpUtil.dp2px(mContext,34));
                    rl_all_info.setBackgroundResource(R.drawable.marker_other_bottom_right);
                } else if (type == 8) { // Bottom // 底部 需要显示在底部中间位置
                    lp.setMargins(0, 0, 0, bottom);
                    rl_all_info.setPadding(DpUtil.dp2px(mContext,22),DpUtil.dp2px(mContext,18),DpUtil.dp2px(mContext,22),DpUtil.dp2px(mContext,34));
                    rl_all_info.setBackgroundResource(R.drawable.marker_other_bottom_center);
                } else { // 正常情况下 显示在底部中间位置
                    lp.setMargins(0, 0, 0, bottom);
                    rl_all_info.setPadding(DpUtil.dp2px(mContext,22),DpUtil.dp2px(mContext,18),DpUtil.dp2px(mContext,22),DpUtil.dp2px(mContext,34));
                    rl_all_info.setBackgroundResource(R.drawable.marker_other_bottom_center);
                }
                // 设置属性
                rl_all_info.setLayoutParams(lp);

                // 界面的绘制加数据
                TextView tvTitle = view.findViewById(R.id.tv_marker_click_title);
                TextView tvPosition = view.findViewById(R.id.tv_marker_click_position);
                int period = Integer.parseInt(marker.getTitle());
                ScenicSpotGuideBean.ScenicServicePointListBean.PointListBean bean = mOtherInfo.get(period);
                tvTitle.setText(bean.getName());

                // 获取当前marker的经纬度
                LatLng markerLatLng = marker.getPosition();
                // 获取当前我的经纬度
                LatLng myLatLng = new LatLng(Constants.mLatitude, Constants.mLongitude);
                // 计算当前两个经纬度的距离问题
                float distance = AMapUtils.calculateLineDistance(markerLatLng, myLatLng);
                tvPosition.setText("距离" + AMapUtil.getFriendlyLength((int) distance));
            } else if (marker.getSnippet().equals(SPOT_TYPE)){ // 景点 // 界面的绘制加数据 当前是景点显示的infowindow 60 是正常情况 200  仅仅对景区景点的设置底部问题 带有箭头的正常情况下65
                RelativeLayout rl_all_info = view.findViewById(R.id.rl_all_info);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(rl_all_info.getLayoutParams());
                int view_x = DpUtil.dp2px(mContext,188); // infowindow的宽度
                int bottom = DpUtil.dp2px(mContext,155); // 正常情况下底部偏移量问题
                int padding_view = DpUtil.dp2px(mContext,34)*2; // 正常情况下底部偏移量问题
                // 判断当前是上下左右哪一个地方的显示
                int type = showInfoWindowSpotLTRB(marker); // 1 marker 在屏幕的左边 2 marker 在屏幕的上面 3 marker 在屏幕的右边 4 marker 在屏幕的底部 5 左上  6 左下 7 右上  8  右下
                if (type == 1) { // top // 需要显示在顶部中间位置
                    lp.setMargins(0,0, 0,  0);
                    rl_all_info.setPadding(DpUtil.dp2px(mContext,18),DpUtil.dp2px(mContext,38),DpUtil.dp2px(mContext,18),DpUtil.dp2px(mContext,10));
                    rl_all_info.setBackgroundResource(R.drawable.marker_spot_top_center);
                } else if (type == 2 ){ // topleft // 顶部左边 需要显示在顶部最左边位置
                    lp.setMargins(view_x-padding_view,0, 0,  0);
                    rl_all_info.setPadding(DpUtil.dp2px(mContext,18),DpUtil.dp2px(mContext,38),DpUtil.dp2px(mContext,18),DpUtil.dp2px(mContext,10));
                    rl_all_info.setBackgroundResource(R.drawable.marker_spot_top_left);
                } else if (type == 3 ){ // topright // 顶部右边 需要显示在顶部最右边位置
                    lp.setMargins(0,0, view_x-padding_view,  0);
                    rl_all_info.setPadding(DpUtil.dp2px(mContext,18),DpUtil.dp2px(mContext,38),DpUtil.dp2px(mContext,18),DpUtil.dp2px(mContext,10));
                    rl_all_info.setBackgroundResource(R.drawable.marker_spot_top_right);
                } else if (type == 4 ){ // right // 右边 需要显示在右边中间位置
                    lp.setMargins(0,0, view_x,  bottom/2);
                    rl_all_info.setPadding(DpUtil.dp2px(mContext,8),DpUtil.dp2px(mContext,28),DpUtil.dp2px(mContext,32),DpUtil.dp2px(mContext,20));
                    rl_all_info.setBackgroundResource(R.drawable.marker_spot_right_center);
                } else if (type == 5 ){ // left // 左边 需要显示在左边中间位置
                    lp.setMargins(view_x,0, 0,  bottom/2);
                    rl_all_info.setPadding(DpUtil.dp2px(mContext,32),DpUtil.dp2px(mContext,28),DpUtil.dp2px(mContext,8),DpUtil.dp2px(mContext,20));
                    rl_all_info.setBackgroundResource(R.drawable.marker_spot_left_center);
                } else if (type == 6 ){ // leftBotttom // 底部左边 需要显示在底部最左边位置
                    lp.setMargins(view_x-padding_view,0, 0,  bottom);
                    rl_all_info.setPadding(DpUtil.dp2px(mContext,18),DpUtil.dp2px(mContext,18),DpUtil.dp2px(mContext,18),DpUtil.dp2px(mContext,26));
                    rl_all_info.setBackgroundResource(R.drawable.marker_spot_bottom_left);
                } else if (type == 7 ){ // rightBottom // 底部右边 需要显示在底部最右边位置
                    lp.setMargins(0,0, view_x-padding_view,  bottom);
                    rl_all_info.setPadding(DpUtil.dp2px(mContext,18),DpUtil.dp2px(mContext,18),DpUtil.dp2px(mContext,18),DpUtil.dp2px(mContext,26));
                    rl_all_info.setBackgroundResource(R.drawable.marker_spot_bottom_right);
                } else if (type == 8 ){ // Bottom // 底部 需要显示在底部中间位置
                    lp.setMargins(0,0, 0,  bottom);
                    rl_all_info.setPadding(DpUtil.dp2px(mContext,18),DpUtil.dp2px(mContext,18),DpUtil.dp2px(mContext,18),DpUtil.dp2px(mContext,26));
                    rl_all_info.setBackgroundResource(R.drawable.marker_spot_bottom_center);
                } else { // 正常情况下 显示在底部中间位置
                    lp.setMargins(0,0, 0,  bottom);
                    rl_all_info.setPadding(DpUtil.dp2px(mContext,18),DpUtil.dp2px(mContext,18),DpUtil.dp2px(mContext,18),DpUtil.dp2px(mContext,26));
                    rl_all_info.setBackgroundResource(R.drawable.marker_spot_bottom_center);
                }
                // 设置属性
                rl_all_info.setLayoutParams(lp);

                // 音频播放点击事件
                RelativeLayout rlImg = view.findViewById(R.id.rl_img);
                // 景点图片显示 圆形图片显示
                RoundImageView ivScenicSpotImg = view.findViewById(R.id.iv_scenic_spot_img);
                // 播放的图片
                ImageView ivPlayImg = view.findViewById(R.id.iv_play_img);
                // 景点名称
                TextView tvTitle = view.findViewById(R.id.tv_title);
                // 景点距离
                TextView tvPosition = view.findViewById(R.id.tv_position);
                // 解说
                ImageView ivListen = view.findViewById(R.id.iv_intro);
                // 详请
                ImageView ivDetail = view.findViewById(R.id.iv_detail);
                int period = Integer.parseInt(marker.getTitle());
                ScenicSpotGuideBean.ScenicSpotListBean bean = mInfo.getScenicSpotList().get(period);
                tvTitle.setText(bean.getName());

                // 获取当前marker的经纬度
                LatLng markerLatLng = marker.getPosition();
                // 获取当前我的经纬度
                LatLng myLatLng = new LatLng(Constants.mLatitude,Constants.mLongitude);
                // 计算当前两个经纬度的距离问题
                float distance = AMapUtils.calculateLineDistance(markerLatLng, myLatLng);
                tvPosition.setText("距离" + AMapUtil.getFriendlyLength((int) distance));

                GlideUtil.loadImgRadius(ivScenicSpotImg, 0, bean.getPhotoUrl());

                // 更新当前界面
                if (mVoicePlayerPosition == period) { // 表示当前播放的就是这个音频文件
                    if (PlayerService.mMediaPlayer.isPlaying()) { // 当前状态 播放中
                        // 景点图片上面的播放按钮
                        ivPlayImg.setImageResource(R.drawable.play_ing);
                        // 解说
                        ivListen.setBackgroundResource(R.drawable.scenic_spot_infowindow_anim);
                        mInfoWindowAnim = (AnimationDrawable) ivListen.getBackground();
                        mInfoWindowAnim.start();
                    } else { // 表示这里开始暂停播放
                        // 景点图片上面的播放按钮
                        ivPlayImg.setImageResource(R.drawable.play_);
                        // 解说
                        ivListen.setBackgroundResource(R.drawable.scenic_spot_map_listen_pause_icon);
                        if (mInfoWindowAnim != null) {
                            mInfoWindowAnim.stop();
                        }
                    }
                } else { // 表示当前需要播放这个问题
                    // 在没有的情况中判断
                    if (mInfo.getScenicSpotList().get(period).getId() == Constants.mScenicSpotId) {
                        mVoicePlayerPosition = period;
                        if (PlayerService.mMediaPlayer.isPlaying()) { // 当前状态 播放中
                            // 景点图片上面的播放按钮
                            ivPlayImg.setImageResource(R.drawable.play_ing);
                            // 解说
                            ivListen.setBackgroundResource(R.drawable.scenic_spot_infowindow_anim);
                            mInfoWindowAnim = (AnimationDrawable) ivListen.getBackground();
                            mInfoWindowAnim.start();
                        } else { // 表示这里开始暂停播放
                            // 景点图片上面的播放按钮
                            ivPlayImg.setImageResource(R.drawable.play_);
                            // 解说
                            ivListen.setBackgroundResource(R.drawable.scenic_spot_map_listen_pause_icon);
                            if (mInfoWindowAnim != null) {
                                mInfoWindowAnim.stop();
                            }
                        }
                    } else {
                        // 景点图片上面的播放按钮
                        ivPlayImg.setImageResource(R.drawable.play_);
                        // 解说
                        ivListen.setBackgroundResource(R.drawable.scenic_spot_map_listen_icon);
                        if (mInfoWindowAnim != null) {
                            mInfoWindowAnim.stop();
                        }
                    }
                }

                // 点击事件
                // 播放点击事件
                rlImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 更新当前界面
                        if (mVoicePlayerPosition == period) { // 表示当前播放的就是这个音频文件
                            if (!PlayerService.mMediaPlayer.isPlaying()) { // 当前播放的文件是否暂停事件 表示这里开始播放
                                // 景点图片上面的播放按钮
                                ivPlayImg.setImageResource(R.drawable.play_ing);
                                // 解说
                                ivListen.setBackgroundResource(R.drawable.scenic_spot_infowindow_anim);
                                mInfoWindowAnim = (AnimationDrawable) ivListen.getBackground();
                                mInfoWindowAnim.start();
                            } else { // 表示这里开始暂停播放
                                // 景点图片上面的播放按钮
                                ivPlayImg.setImageResource(R.drawable.play_);
                                // 解说
                                ivListen.setBackgroundResource(R.drawable.scenic_spot_map_listen_pause_icon);
                                if (mInfoWindowAnim != null) {
                                    mInfoWindowAnim.stop();
                                }
                            }
                        } else { // 表示当前需要播放这个问题
                            // 景点图片上面的播放按钮
                            ivPlayImg.setImageResource(R.drawable.play_ing);
                            // 解说
                            ivListen.setBackgroundResource(R.drawable.scenic_spot_infowindow_anim);
                            mInfoWindowAnim = (AnimationDrawable) ivListen.getBackground();
                            mInfoWindowAnim.start();
                        }
                        // 点击当前按钮 开始播放音频文件
                        setClickListen(bean, period);
                    }
                });
                // 播放点击事件
                ivPlayImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 更新当前界面
                        if (mVoicePlayerPosition == period) { // 表示当前播放的就是这个音频文件
                            if (!PlayerService.mMediaPlayer.isPlaying()) { // 当前播放的文件是否暂停事件 表示这里开始播放
                                // 景点图片上面的播放按钮
                                ivPlayImg.setImageResource(R.drawable.play_ing);
                                // 解说
                                ivListen.setBackgroundResource(R.drawable.scenic_spot_infowindow_anim);
                                mInfoWindowAnim = (AnimationDrawable) ivListen.getBackground();
                                mInfoWindowAnim.start();
                            } else { // 表示这里开始暂停播放
                                // 景点图片上面的播放按钮
                                ivPlayImg.setImageResource(R.drawable.play_);
                                // 解说
                                ivListen.setBackgroundResource(R.drawable.scenic_spot_map_listen_pause_icon);
                                if (mInfoWindowAnim != null) {
                                    mInfoWindowAnim.stop();
                                }
                            }
                        } else { // 表示当前需要播放这个问题
                            // 景点图片上面的播放按钮
                            ivPlayImg.setImageResource(R.drawable.play_ing);
                            // 解说
                            ivListen.setBackgroundResource(R.drawable.scenic_spot_infowindow_anim);
                            mInfoWindowAnim = (AnimationDrawable) ivListen.getBackground();
                            mInfoWindowAnim.start();
                        }
                        setClickListen(bean, period);
                    }
                });
                // 解说
                ivListen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 更新当前界面
                        if (mVoicePlayerPosition == period) { // 表示当前播放的就是这个音频文件
                            if (!PlayerService.mMediaPlayer.isPlaying()) { // 当前播放的文件是否暂停事件 表示这里开始播放
                                // 景点图片上面的播放按钮
                                ivPlayImg.setImageResource(R.drawable.play_ing);
                                // 解说
                                ivListen.setBackgroundResource(R.drawable.scenic_spot_infowindow_anim);
                                mInfoWindowAnim = (AnimationDrawable) ivListen.getBackground();
                                mInfoWindowAnim.start();
                            } else { // 表示这里开始暂停播放
                                // 景点图片上面的播放按钮
                                ivPlayImg.setImageResource(R.drawable.play_);
                                // 解说
                                ivListen.setBackgroundResource(R.drawable.scenic_spot_map_listen_pause_icon);
                                if (mInfoWindowAnim != null) {
                                    mInfoWindowAnim.stop();
                                }
                            }
                        } else { // 表示当前需要播放这个问题
                            // 景点图片上面的播放按钮
                            ivPlayImg.setImageResource(R.drawable.play_ing);
                            // 解说
                            ivListen.setBackgroundResource(R.drawable.scenic_spot_infowindow_anim);
                            mInfoWindowAnim = (AnimationDrawable) ivListen.getBackground();
                            mInfoWindowAnim.start();
                        }
                        setClickListen(bean, period);
                    }
                });
                // 详请
                ivDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        marker.hideInfoWindow();
                        setClickDetail(bean, period);
                    }
                });
            } else if (marker.getSnippet().equals(PERSON_TYPE)){ // 人员// 界面的绘制加数据 当前是景点显示的infowindow 0 是正常情况 225  仅仅对景区景点的设置底部问题 带有箭头的正常情况下0
                RelativeLayout rl_all_info = view.findViewById(R.id.rl_all_info);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(rl_all_info.getLayoutParams());
                int view_x = DpUtil.dp2px(mContext,200); // infowindow的宽度
                int view_lr = DpUtil.dp2px(mContext,208); // infowindow的宽度
                int bottom = DpUtil.dp2px(mContext,110); // 正常情况下底部偏移量问题
                int bottom_t = DpUtil.dp2px(mContext,125); // 由于人员marker放大导致显示位置需要进行增加 显示在顶部的正常位置
                int bottom_lr = DpUtil.dp2px(mContext,70); // 由于人员marker放大导致显示位置需要进行增加 左右的中间位置
                int padding_view = DpUtil.dp2px(mContext,40)*2; // 箭头右边的距离 40 41 都可以
                // 内容区的距离调整
                int padding_4 = DpUtil.dp2px(mContext,4);
                int padding_3 = DpUtil.dp2px(mContext,3);
                int padding_11 = DpUtil.dp2px(mContext,11);
                // 判断当前是上下左右哪一个地方的显示
                int type = showInfoWindowLTRBPerson(marker); // 1 marker 在屏幕的左边 2 marker 在屏幕的上面 3 marker 在屏幕的右边 4 marker 在屏幕的底部 5 左上  6 左下 7 右上  8  右下
                if (type == 1) { // top // 需要显示在顶部中间位置
                    lp.setMargins(0,0, 0,  0);
                    rl_all_info.setBackgroundResource(R.drawable.marker_person_top_center);
                    rl_all_info.setPadding(0,padding_11,0,0);
                } else if (type == 2 ){ // topleft // 顶部左边 需要显示在顶部最左边位置
                    lp.setMargins(view_x-padding_view,0, 0,  0);
                    rl_all_info.setBackgroundResource(R.drawable.marker_person_top_left);
                    rl_all_info.setPadding(0,padding_11,0,0);
                } else if (type == 3 ){ // topright // 顶部右边 需要显示在顶部最右边位置
                    lp.setMargins(0,0, view_x-padding_view,  0);
                    rl_all_info.setBackgroundResource(R.drawable.marker_person_top_right);
                    rl_all_info.setPadding(0,padding_11,0,0);
                } else if (type == 4 ){ // right // 右边 需要显示在右边中间位置
                    lp.setMargins(0,0, view_lr,  bottom_lr);
                    rl_all_info.setBackgroundResource(R.drawable.marker_person_right_center);
                    rl_all_info.setPadding(0,padding_3,padding_4,0);
                } else if (type == 5 ){ // left // 左边 需要显示在左边中间位置
                    lp.setMargins(view_lr,0, 0,  bottom_lr);
                    rl_all_info.setBackgroundResource(R.drawable.marker_person_left_center);
                    rl_all_info.setPadding(padding_4,padding_3,0,0);
                } else if (type == 6 ){ // leftBotttom // 底部左边 需要显示在底部最左边位置
                    lp.setMargins(view_x-padding_view,0, 0,  bottom_t);
                    rl_all_info.setBackgroundResource(R.drawable.marker_person_bottom_left);
                    rl_all_info.setPadding(0,0,0,0);
                } else if (type == 7 ){ // rightBottom // 底部右边 需要显示在底部最右边位置
                    lp.setMargins(0,0, view_x-padding_view,  bottom_t);
                    rl_all_info.setBackgroundResource(R.drawable.marker_person_bottom_right);
                    rl_all_info.setPadding(0,0,0,0);
                } else if (type == 8 ){ // Bottom // 底部 需要显示在底部中间位置
                    lp.setMargins(0,0, 0,  bottom_t);
                    rl_all_info.setBackgroundResource(R.drawable.marker_person_bottom_center);
                    rl_all_info.setPadding(0,0,0,0);
                } else { // 正常情况下 显示在底部中间位置
                    lp.setMargins(0,0, 0,  bottom_t);
                    rl_all_info.setBackgroundResource(R.drawable.marker_person_bottom_center);
                    rl_all_info.setPadding(0,0,0,0);
                }
                // 设置属性
                rl_all_info.setLayoutParams(lp);

                String title = marker.getTitle();
                if (title.contains(".")) { // 表示用户离线的情况
                    title = title.substring(0, title.length() - 1);
                }
                GroupUserInfo bean = null;
                for (int i = 0; i < mMemberInfo_All.size(); i++) {
                    if (title.equals(mMemberInfo_All.get(i).userId)) {
                        bean = mMemberInfo_All.get(i);
                        break; // 获取到当前信息 跳出循环
                    }
                }
                if (marker.getTitle().contains(".")){
                    // 离线状态
                    // 成员昵称
                    TextView maker_nick_name_tv = view.findViewById(R.id.maker_nick_name_tv);
                    // 成员更新时间
                    TextView tv_update_time = view.findViewById(R.id.tv_update_time);
                    maker_nick_name_tv.setText(bean.nickName);
                    tv_update_time.setText(TimeUtil.updateTimeSmart(bean.time));
                } else {
                    // 在线状态
                    // 成员类型
                    TextView infowindow_type_tv = view.findViewById(R.id.infowindow_type_tv);
                    // 成员昵称
                    TextView maker_nick_name_tv = view.findViewById(R.id.maker_nick_name_tv);
                    // 成员距离
                    TextView tv_update_distance = view.findViewById(R.id.tv_update_distance);
                    // 成员更新时间
                    TextView tv_update_time = view.findViewById(R.id.tv_update_time);

                    String typeStr = getTypeStr(bean.type);
                    infowindow_type_tv.setText(typeStr);
                    if (typeStr.contains("成员")) {
                        infowindow_type_tv.setBackgroundResource(R.drawable.marker_shared_navigation_map_captain_info_bg);
                    } else {
                        infowindow_type_tv.setBackgroundResource(R.drawable.marker_shared_navigation_map_captain_leander_info_bg);
                    }
                    maker_nick_name_tv.setText(bean.nickName);
                    float distance = AMapUtils.calculateLineDistance(marker.getPosition(), new LatLng(Constants.mLatitude, Constants.mLongitude));
                    tv_update_distance.setText("离我" + AMapUtil.getFriendlyLength((int) distance));
                    tv_update_time.setText(TimeUtil.updateTimeSmart(bean.time));
                }
            }
        }
    };

    // 景区dialog的显示
    private ScenicContentDialog mScenicContentDialog;
    // 地图dialog的显示
//    private ScenicMapGuideDialog mScenicMapGuideDialog;
    // 离线dialog的显示
    private ScenicMapOfflineDialog mScenicMapOfflineDialog;
    // 第三方地图导航显示
    private ThirdPartyMapsGuideDialog mThirdPartyMapsGuideDialog;

    /**
     * 显示刚进入到界面出现的dialog
     */
    private void showScenicContentDialog() {
        if (mScenicContentDialog == null) {
            mScenicContentDialog = new ScenicContentDialog(this, new View.OnClickListener() {
                @Override
                public void onClick(View v) { // 界面的点击事件
                    boolean isCancel = true;
                    if (v.getId() == R.id.dialog_scenic_content_img_rl) {  // 布局外面的点击事件 播放
                        isCancel = false;
                        setClickListen(null, -1);
                        // 需要更新界面
                    } else if (v.getId() == R.id.dialog_scenic_content_play_iv) { // 点击播放按钮的点击事件
                        isCancel = false;
                        setClickListen(null, -1);
                    } else if (v.getId() == R.id.dialog_scenic_content_map_tv) { //地图两个字的点击事件
                        isCancel = false;
//                        showScenicMapGuideDialog();//表示有景区地图的界面中显示
                        setMapGuideOption();
                    } else if (v.getId() == R.id.dialog_scenic_content_listen_iv) { // 解说
                        // 需要更新界面
                        isCancel = true;
                        setClickListenAll();
                    } else if (v.getId() == R.id.dialog_scenic_content_download_iv) { // 下载
                        if (NetWorkUtils.isNetConnected(mContext)) {
                            isCancel = false;
                            if (mVoiceList != null && mVoiceList.size()>1){
                                mDownLoadType = 3;
                            } else {
                                if (mPackageInfo != null) {
                                    if (viewModel.getIsScenicVoicePackageToDataBase(mContext, mPackageInfo)) {
                                        viewModel.deleteScenicVoicePackageToDataBase(mContext,mPackageInfo);
                                    }
                                }
                                mDownLoadType = 0;
                            }
                            showScenicMapOfflineDialog(mDownLoadType,0);
                        } else {
                            ToastUtils.showText(mContext, "您的网络异常");
                        }
                    } else if (v.getId() == R.id.dialog_scenic_content_guide_iv) { // 导航
                        isCancel = false;
//                        showScenicMapGuideDialog();//表示有景区地图的界面中显示
                        setMapGuideOption();
                    }
                    if (isCancel) {
                        mScenicContentDialog.cancel();
                        if (!mMapViewIsShow) {
                            setShowMapView();
                        }
                    }
                }
            });
            // 当前dialog消失的时候需要将地图上面的图标显示出来
            mScenicContentDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if (!mMapViewIsShow) {
                        setShowMapView();
                        if (!SPUtils.getBoolean("scenic", false)) {
                            GuideDialog dialog = new GuideDialog(ScenicSpotMapActivity.this);
                            View view = LayoutInflater.from(ScenicSpotMapActivity.this).inflate(R.layout.dialog_guide_scenic_spot1, null);
                            ImageView know = view.findViewById(R.id.iv_know);
                            know.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.setContentView(view);
                            dialog.show();
                            SPUtils.putBoolean("scenic", true);
                        }
                        if (mIsFristShowMemberLocation) { // 只在第一次的时候显示
                            mIsFristShowMemberLocation = false;
                            if (!mIsOpenGroupLocation) { // 当前没有开启群位置就会弹出
                                if (mChangeGroupList.size() > 0) { // 需要检测有出行中的队伍
                                    String oldtime = SPUtils.getString(SPUtils.SHOW_TIME_NEAR);
                                    String newtime = String.valueOf(System.currentTimeMillis());
                                    if (!TimeUtil.isSameData(newtime, oldtime)) { // 判断近期是否显示  如果时间是同一天的情况就不显示这个dialog
                                        mHandler.sendEmptyMessageDelayed(MSG_SHOW_MEMBER_LOC_HINT, MSG_SHOW_MEMBER_LOC_HINT_TIME);
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }
        // 判断当前播放的position
        int isPlaying = 0;
        if (mVoicePlayerPosition == -1) { // 当前正在播放
            if (PlayerService.mMediaPlayer.isPlaying()) {
                isPlaying = 1;
            } else {
                isPlaying = 2;
            }
        }
        mScenicContentDialog.setViewData(mInfo.getScenicName(), mInfo.getIntro(), mInfo.getScenicAddress(), mInfo.getDistance(), mInfo.getOpenTime(), mInfo.getPhotoUrl(), isPlaying);
        mScenicContentDialog.show();
        if (!SPUtils.getBoolean("guideScenic", false)) {
            GuideDialog dialog = new GuideDialog(this);
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_guide_scenic_spot, null);
            ImageView ivKnow = view.findViewById(R.id.iv_know);
            ivKnow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.setContentView(view);
            dialog.show();
            SPUtils.putBoolean("guideScenic", true);
        }
    }

//    /**
//     * 显示地图导航的dialog
//     */
//    private void showScenicMapGuideDialog() {
//        if (mScenicMapGuideDialog == null) {
//            mScenicMapGuideDialog = new ScenicMapGuideDialog(this, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) { // 界面的点击事件
//                    mScenicMapGuideDialog.cancel();
//                    if (v.getId() == R.id.dialog_scenic_map_guide_btn_iv) {  // 地图导航
//                        // 第三方地图的导航事件
//                        setMapGuideOption();
//                    }
//                }
//            });
//        }
//        mScenicMapGuideDialog.setViewData(mMapBgImg, mInfo.getScenicAddress());// 地图地址  地点
//        mScenicMapGuideDialog.show();
//    }

    /**
     * 设置第三方地图导航的操作
     */
    private void setMapGuideOption() {
        if (mThirdPartyMapsGuideDialog == null) {
            mThirdPartyMapsGuideDialog = new ThirdPartyMapsGuideDialog(this, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mThirdPartyMapsGuideDialog.dismiss();
                    if (view.getId() == R.id.dialog_third_party_maps_guide_cancel) {
                        // 取消
                    } else if (view.getId() == R.id.dialog_third_party_maps_guide_amap) {
                        // 高德
                        ThirdPartyMapsGuide.goToGaoDeMap(mContext, mInfo.getLonLat().getLongitude(), mInfo.getLonLat().getLatitude());
                    } else if (view.getId() == R.id.dialog_third_party_maps_guide_bdmap) {
                        // 百度
                        ThirdPartyMapsGuide.goToBaiduActivity(mContext, mInfo.getScenicAddress(), mInfo.getLonLat().getLongitude(), mInfo.getLonLat().getLatitude());
                    } else if (view.getId() == R.id.dialog_third_party_maps_guide_tcmap) {
                        // 腾讯
                        ThirdPartyMapsGuide.goToTencentMap(mContext, mInfo.getScenicAddress(), mInfo.getLonLat().getLongitude(), mInfo.getLonLat().getLatitude());
                    }
                }
            });
        }
        mThirdPartyMapsGuideDialog.show();
    }

    /**
     * 显示地图导航的dialog 需要请求接口之后才有具体大小
     */
    private void showScenicMapOfflineDialog(int twotype,int progress) {
        if (mScenicMapOfflineDialog == null) {
            mScenicMapOfflineDialog = new ScenicMapOfflineDialog(this, new View.OnClickListener() {
                @Override
                public void onClick(View v) { // 界面的点击事件
                    if (v.getId() == R.id.dialog_scenic_map_offline_one_iv) {  // 手绘地图下载
                        ToastUtils.showText(mContext, "已下载");
                    } else if (v.getId() == R.id.dialog_scenic_map_offline_two_iv) { // 语音离线下载
                        if (mPackageInfo != null && !TextUtils.isEmpty(mPackageInfo.getResUrl())) {
                            if (!viewModel.getIsScenicVoicePackageToDataBase(mContext, mPackageInfo)) { // 当前未下载
                                downFileNotUI(mPackageInfo.getResUrl());
                            } else {
                                ToastUtils.showText(mContext, "已下载");
                            }
                        } else {
                            ToastUtils.showText(mContext, "当前景区无音频文件");
                        }
                    }
                }
            });
        }
        String twoStr = formatSize();
        mScenicMapOfflineDialog.setViewData("8.19M", twoStr, 3, twotype, progress);// 地图地址  地点
        mScenicMapOfflineDialog.show();
    }

    /**
     * 根据文件大小自动转化为KB, MB, GB
     *
     * @return
     */
    private String formatSize() {
        if (mPackageInfo == null) {
//            return "8.19M";
            return "无音频文件";
        }
        return FileUtil.formatFileSize(Long.valueOf(mPackageInfo.getResSize() + "")*1024);
    }


    /**
     * 判断当前界面的地图上面的控件是否显示
     */
    private boolean mMapViewIsShow = false;

    /**
     * 地图界面上的控件显示时的动画
     */
    private void setShowMapView() {
        Animation animLeftIn = AnimationUtils.loadAnimation(mContext, R.anim.map_list_left_in);
        animLeftIn.setDuration(240);
        Animation animRightIn = AnimationUtils.loadAnimation(mContext, R.anim.map_list_right_in);
        animRightIn.setDuration(240);
        Animation animBottomIn = AnimationUtils.loadAnimation(mContext, R.anim.map_list_bottom_in);
        animRightIn.setDuration(240);
        // 左边 我的控件 自动控件  右边 详请控件
        dataBinding.ivMineLocation.setVisibility(View.VISIBLE);
        dataBinding.ivMineLocation.startAnimation(animLeftIn);

        dataBinding.rlLeftAutomatic.setVisibility(View.VISIBLE);
        dataBinding.rlLeftAutomatic.startAnimation(animLeftIn);

        dataBinding.rlLeftGroup.setVisibility(View.VISIBLE);
        dataBinding.rlLeftGroup.startAnimation(animLeftIn);

        dataBinding.ivRightCenterDetail.setVisibility(View.VISIBLE);
        dataBinding.ivRightCenterDetail.startAnimation(animRightIn);

        dataBinding.rlBottomContent.setVisibility(View.VISIBLE);
        dataBinding.rlBottomContent.startAnimation(animBottomIn);
        mMapViewIsShow = true;
    }

    /**
     * 地图上面的控件消失时的动画
     */
    private void setHideMapView() {
        Animation animLeftIn = AnimationUtils.loadAnimation(mContext, R.anim.map_list_left_out);
        animLeftIn.setDuration(240);
        Animation animRightIn = AnimationUtils.loadAnimation(mContext, R.anim.map_list_right_out);
        animRightIn.setDuration(240);
        Animation animBottomIn = AnimationUtils.loadAnimation(mContext, R.anim.map_list_bottom_out);
        animRightIn.setDuration(240);
        // 左边 我的控件 自动控件  右边 详请控件
        dataBinding.ivMineLocation.setVisibility(View.GONE);
        dataBinding.ivMineLocation.startAnimation(animLeftIn);

        dataBinding.rlLeftAutomatic.setVisibility(View.GONE);
        dataBinding.rlLeftAutomatic.startAnimation(animLeftIn);

        dataBinding.rlLeftGroup.setVisibility(View.GONE);
        dataBinding.rlLeftGroup.startAnimation(animLeftIn);

        dataBinding.ivRightCenterDetail.setVisibility(View.GONE);
        dataBinding.ivRightCenterDetail.startAnimation(animRightIn);

        dataBinding.rlBottomContent.setVisibility(View.GONE);
        dataBinding.rlBottomContent.startAnimation(animBottomIn);
        mMapViewIsShow = false;
    }

    /**
     * 需要还原之后才能在下一次进来的时候正常显示
     */
    private void setOnDestoryNullData() {
        if (PlayerService.mMediaPlayer != null) {
            if (PlayerService.mMediaPlayer.getDuration() > 0 && !Constants.mServiceIsStart) { // 表示只是在暂停的情况下退出当前界面 需要做情况处理
                Intent intent = new Intent();
                intent.putExtra("MSG", Constants.PlayerMag.STOP);
                intent.putExtra("path", "");
                intent.setClass(mContext, PlayerService.class);
                startService(intent);
                Constants.mScenicId = 0; // 景区编号
                Constants.mScenicSpotId = 0; // 景点编号
                Constants.mVoiceLatitude = 0.0; // 当前播放音频文件的经纬度
                Constants.mVoiceLongitude = 0.0; // 当前播放音频文件的经纬度
                mIsStop = true;
            }
        }
        mAmap.clear();
        mAmap = null;
        mScenicContentDialog = null;
        mScenicMapOfflineDialog = null;
        mMapViewIsShow = false;
        mVoicePlayerPosition = -2;
        mVoicePlayerScenicSpotId = 0;
        mIsAutoPlayerVoice = false;
        mPlayerPath = "";
        mIsActivity = false;
        mCurrentMarker = null;
        mIsOpenGroupLocation = false;
        mIsOpenGroupLocationTmp = false;
        mGroupId = "";
        mUpGroupId = "";
        mGroupNickName = "";
        mGroupRole = 0;
        mGroupCarInfo = "";
        mGroupAvtor = "";
        mGroupgroupId = "";
        mGroupLeader = false;
        Constants.mIsOtherLocation = false;
        mHandler.removeMessages(MSG_DOWNLOAD_WIFI_HINT);
        mHandler.removeMessages(MSG_PLAY_UPDATE_SPOT_UI);
        mHandler.removeMessages(MSG_SHOW_MEMBER_LOC_HINT);
        mHandler.removeMessages(HANDLER_GET_MEMBER_INFO);
        mHandler.removeMessages(HANDLER_UPDATE_MEMBER_INFO);
        mHandler.removeMessages(HANDLER_UPDATE_SHARE_LOCATION);
    }

    // 定位需要的权限
    private String[] mPermission = {
            //SDK在Android 6.0下需要进行运行检测的权限如下：
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    private final int WRITE_COARSE_LOCATION_REQUEST_CODE = 0x001;

    private void setPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
        ) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, mPermission, WRITE_COARSE_LOCATION_REQUEST_CODE);//自定义的code
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //可在此继续其他操作。
        if (requestCode == WRITE_COARSE_LOCATION_REQUEST_CODE) {
            if (!verifyPermissions(grantResults)) {      // 表示有权限没有被授权  需要提醒用户重新授权
                // 弹出提示信息，说用户没有授权，可能会导致部分功能不能很好的实现
//                showMissingPermissionDialog();              //显示提示信息
            } else { // 所有的权限都同意之后开始定位
                getApplicationContext().startService(new Intent(this, LocationService.class));
            }
        }
    }

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

    // 当前用户播放位置 -1 表示播放景区音频 > -1 表示播放景区景点音频 -2 表示当前没有播放事件
    private int mVoicePlayerPosition = -2;

    private int mVoicePlayerScenicSpotId = 0;

    public String mPlayerPath = "";

    public void playMusic(int action) {
        if ("".equals(mPlayerPath) || mPlayerPath == null) {
            ToastUtils.showText(this, "当前景区没有音频文件");
            mVoicePlayerPosition = -2; // 表示当前没有音频数据播放
            mVoicePlayerScenicSpotId = 0;
            return;
        }
        if (mPlayerPath.contains("https") && !NetWorkUtils.isNetConnected(mContext)) {
            ToastUtils.showText(mContext, "网络异常");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("MSG", action);
        intent.putExtra("path", mPlayerPath);
        intent.setClass(mContext, PlayerService.class);
        startService(intent);
        if (action == Constants.PlayerMag.PLAY_MAG && NetWorkUtils.isNetConnected(mContext)) {
            // 请求播放操作 增加播放景区数量
            viewModel.getPlayOperation(mInfo.getScenicId());
            // 需要显示界面动画
//            mHandler.sendEmptyMessage(MSG_PLAY_UPDATE_UI);
        }
    }

    /**
     * 播放音频文件
     */
    private void playVoiceData(int position) {
        if (mVoicePlayerPosition == position) {
            pauseVoiceData();
        } else {
            if (mVoiceList != null && mVoiceList.size() > 1 && !TextUtils.isEmpty(mVoiceFileName)) { // 表示本地有数据 可以播放本地数据 可能只有zip文件 没有解压
                // 播放本地数据
                mPlayerPath = position == -1 ? mVoiceFileName + "/scenic.mp3" : mVoiceFileName + "/"+mInfo.getScenicSpotList().get(position).getId()+".mp3";
            } else {
                if (position == -1) {
                    if (mInfo.getNarrateRes() != null)
                        mPlayerPath = mInfo.getNarrateRes().getResUrl();
                    else mPlayerPath = null;
                } else {
                    if (mInfo.getScenicSpotList().get(position).getScenicResource() != null)
                        mPlayerPath = mInfo.getScenicSpotList().get(position).getScenicResource().getResUrl();
                    else mPlayerPath = null;
                }
            }
            if (position == -1){
                mVoicePlayerScenicSpotId = 0;
            } else {
                mVoicePlayerScenicSpotId = mInfo.getScenicSpotList().get(position).getId();
            }
            mVoicePlayerPosition = position;
            // 播放一个新的
            playMusic(Constants.PlayerMag.PLAY_MAG);
        }
    }

    /**
     * 暂停播放文件
     */
    private void pauseVoiceData() {
        playMusic(Constants.PlayerMag.PAUSE);
//        mHandler.sendEmptyMessage(MSG_PLAY_UPDATE_PAUSE_UI);
    }

    private int mDistance = 50;

    /**
     * 设置自动播放下一个景点的音频数据
     * 需要查看当前最近的景点 如果当前最近的景点依旧是当前播放的这个 就不进行重复播放，如果不是当前这个景点在50米之内，就播放其他的景点音频
     */
    private void setAutoPlayerNextScenicSpotVoiceData() {
//        if (mVoicePlayerPosition == -2) { //表示当前没有音频播放
//            return;
//        }
        // 判断是否是自动播放 如果当前正在属于播放中边不执行
//        if (!mIsAutoPlayerVoice || PlayerService.mMediaPlayer.isPlaying())
        if (!mIsAutoPlayerVoice) { // 如果打开就正常自动播放
            return;
        }
        if (Constants.mServiceIsStart){
            return;
        }
        // 获取当前自己的位置 经纬度
        LatLng MyLatLng = new LatLng(Constants.mLatitude, Constants.mLongitude);

        List<DistanceData> list = new ArrayList<>();

        // 需要根据自己的位置去遍历所有的景点的距离
        for (int i = 0; i < mInfo.getScenicSpotList().size(); i++) {
            ScenicSpotGuideBean.ScenicSpotListBean bean = mInfo.getScenicSpotList().get(i);
            // 获取当前景点的位置
            LatLng latLng = new LatLng(bean.getLonLat().getLatitude(), bean.getLonLat().getLongitude());
            // 对比着两个地方的的距离
            float distance = AMapUtils.calculateLineDistance(latLng, MyLatLng);
            // 如果距离小于50m 并且不是当前播放的视频文件
            if (distance < mDistance && mVoicePlayerScenicSpotId != mInfo.getScenicSpotList().get(i).getId()) {
                // 表示用户离开上一个景点到下一个景点了 这个时候我们需要将这个数据给存储起来
                list.add(new DistanceData(distance, i,bean));
            }
        }
        // 遍历完后 使用冒泡的算法将数据排列出来 我这边获取最近的一个进行播放数据
        List<DistanceData> data = bubbleSort(list); // 冒泡算法获取得到的列表数据
        if (data != null) {
            if (data.size() >= 1) {
                int position = -1;
                mVoicePlayerScenicSpotId = data.get(0).getId().getId();
                for (int i = 0;i<mInfo.getScenicSpotList().size();i++){
                    if (mVoicePlayerScenicSpotId == mInfo.getScenicSpotList().get(i).getId()){
                        position = i;
                        break;
                    }
                }
                playVoiceData(position); // 就播放当前获取的数据的第一个 表示当前是距离播放之后最近的一个数据
            }
        }
    }

    public List<DistanceData> bubbleSort(List<DistanceData> arrays) {
        if (arrays.size() == 0)
            return arrays;
        if (arrays.size() == 1)
            return arrays;
        int length = arrays.size() - 1;
        for (int i = 0; i < length; i++) {
            for (int j = 1; j <= length - i; j++) {
                if (arrays.get(j - 1).distance > arrays.get(j).distance) {
                    swap(arrays, j - 1, j);
                }
            }
        }
        return arrays;
    }

    private void swap(List<DistanceData> arrays, int arg1, int arg2) {
        DistanceData temp = arrays.get(arg1);
        arrays.set(arg1, arrays.get(arg2));
        arrays.set(arg2, temp);
    }

    /**
     * 距离和景点的类  方便冒泡法
     */
    private class DistanceData {
        float distance = 0;
        int position = 0;
        ScenicSpotGuideBean.ScenicSpotListBean id = null;

        public DistanceData(float distance, int position,ScenicSpotGuideBean.ScenicSpotListBean id) {
            this.distance = distance;
            this.position = position;
            this.id = id;
        }

        public ScenicSpotGuideBean.ScenicSpotListBean getId() {
            return id;
        }

        public void setId(ScenicSpotGuideBean.ScenicSpotListBean id) {
            this.id = id;
        }

        public float getDistance() {
            return distance;
        }

        public void setDistance(float distance) {
            this.distance = distance;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    // 开始播放
    private final int MSG_PLAY_UPDATE_SPOT_UI = 0x03;
    private final int MSG_SHOW_MEMBER_LOC_HINT = 0x04;
    private final int MSG_DOWNLOAD_WIFI_HINT = 0x05;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_PLAY_UPDATE_SPOT_UI: // 更新景点ui显示
                    // 更新景点的dialog
                    if (mVoicePlayerPosition == -1) {
                        if (PlayerService.mMediaPlayer.isPlaying()) { // 判断当前是否正在播放
                            mScenicContentDialog.setViewData(mInfo.getScenicName(), mInfo.getIntro(), mInfo.getScenicAddress(), mInfo.getDistance(), mInfo.getOpenTime(), mInfo.getPhotoUrl(), 1);
                        } else {
                            mScenicContentDialog.setViewData(mInfo.getScenicName(), mInfo.getIntro(), mInfo.getScenicAddress(), mInfo.getDistance(), mInfo.getOpenTime(), mInfo.getPhotoUrl(), 2);
                        }
                    } else {
                        mScenicContentDialog.setViewData(mInfo.getScenicName(), mInfo.getIntro(), mInfo.getScenicAddress(), mInfo.getDistance(), mInfo.getOpenTime(), mInfo.getPhotoUrl(), 0);
                    }
                    break;
                case MSG_SHOW_MEMBER_LOC_HINT:
                    if (mShowMemberLocationOpenDialog != null && mShowMemberLocationOpenDialog.isShowing()){ // 确定打开位置dialog正在显示 这个提示dialog不显示
                        return;
                    }
                    if (mShowMemberLocationChoiceDialog != null && mShowMemberLocationChoiceDialog.isShowing()){ // 选择群的dialog正在显示 这个提示也不显示
                        return;
                    }
                    if (mIsOpenGroupLocation){ // 如果当前已经开启了位置也不显示提示dialog
                        return;
                    }
                    if (!mGroupId.equals(mChangeGroupInfo.getGroupId()) && !TextUtils.isEmpty(mGroupId)){ // 如果当前显示的群编号跟系统默认的群编号不一致也不进行显示 并且群编号不为空的情况下
                        return;
                    }
                    showMemberLocationHintDialog();
                    break;

                case HANDLER_GET_MEMBER_INFO: // get member info 持续更新
                    if (mIsActivity) {
                        getUserInfoToDataBase();
                        if (mIsActivity) {
                            mHandler.sendEmptyMessageDelayed(HANDLER_GET_MEMBER_INFO, Constants.UPDATE_TIME_MAP_INFO);
                        }
                    }
                    break;
                case HANDLER_UPDATE_MEMBER_INFO: // update member info to view 只更新一次
                    if (mIsActivity) {
                        // add marker data and init right view
                        getUserInfoToDataBase();
                    }
                    break;
                case MSG_DOWNLOAD_WIFI_HINT: // update member info to view 只更新一次
                    if (mIsActivity && mScenicContentDialog != null) {
                        // add marker data and init right view
                       showScenicSpotWIFIHintDialog();
                    } else {
                        mHandler.sendEmptyMessageDelayed(MSG_DOWNLOAD_WIFI_HINT,300);
                    }
                    break;
                case HANDLER_UPDATE_SHARE_LOCATION: // update share location 发送我的位置
                    if (mIsActivity) {
                        if (mIsShareLoc) { // 是否分享位置
                            if (mGroupgroupId.equals(mGroupId)) { // 是否跟当前群编号一致
                                if (mIsOpenGroupLocation && !TextUtils.isEmpty(mGroupId)) { // 是否开启成员位置 并且 群编号不为空
                                    viewModel.sendMessageTeamLocation(mContext, 1, mGroupId, mUserId, mGroupNickName, mGroupAvtor, mGroupCarInfo, mGroupRole, mGroupLeader);
                                }
                            }
                            mHandler.sendEmptyMessageDelayed(HANDLER_UPDATE_SHARE_LOCATION, Constants.SEND_TIME_MAP_INFO);
                        } else { // 如果关闭就不发
//                            viewModel.sendMessageTeamLocation(mContext,2,mGroupId,mUserId,mGroupNickName,mGroupAvtor,mGroupCarInfo,mGroupRole,mGroupLeader);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private boolean mIsStop = false;

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getOrderType(PlayVoiceTypeEvent event) {
        if (event.getType() == 0) { // 表示停止播放
            if (mIsActivity) {
                dataBinding.rlVoiceStartPlayShow.setVisibility(View.GONE);
                mHandler.sendEmptyMessage(MSG_PLAY_UPDATE_SPOT_UI);
                if (mCurrentMarker != null) {
                    int period = Integer.parseInt(mCurrentMarker.getTitle());
                    if (period == mVoicePlayerPosition) {
                        mCurrentMarker.hideInfoWindow();
                        mCurrentMarker.showInfoWindow();
                        mCurrentMarker.setToTop();
                    }
                }
                if (mIsStop) {
                    mVoicePlayerPosition = -2;
                    Constants.mScenicSpotId = 0;
                    mIsStop = false;
                }
            } else {
                dataBinding.rlVoiceStartPlayShow.setVisibility(View.GONE);
            }
        } else if (event.getType() == 1) { // 表示开始播放数据
            if (mIsActivity) {
                // 判断当前是否有音频播放事件
                dataBinding.rlVoiceStartPlayShow.setVisibility(View.VISIBLE);
                String scenic = mVoicePlayerPosition == -1 ? mInfo.getScenicName() : mInfo.getScenicSpotList().get(mVoicePlayerPosition).getName();
                String content = "正在为您播放“" + scenic + "”语音导游";
                Constants.mServiceIsStartConteng = content;
                if (Constants.mScenicId != mInfo.getScenicId()) {
                    Constants.mScenicId = mInfo.getScenicId();
                }
                if (mVoicePlayerPosition != -1 && Constants.mScenicSpotId != mInfo.getScenicSpotList().get(mVoicePlayerPosition).getId()) {
                    Constants.mScenicSpotId = mInfo.getScenicSpotList().get(mVoicePlayerPosition).getId();
                    Constants.mVoiceLatitude = mInfo.getScenicSpotList().get(mVoicePlayerPosition).getLonLat().getLatitude();
                    Constants.mVoiceLongitude = mInfo.getScenicSpotList().get(mVoicePlayerPosition).getLonLat().getLongitude();
                } else if (mVoicePlayerPosition == -1) {
                    Constants.mScenicSpotId = 0;
                }
                dataBinding.tvVoiceContent.setText(Constants.mServiceIsStartConteng);
                AnimationDrawable animationDrawable;
                animationDrawable = (AnimationDrawable) dataBinding.playShowIv.getBackground();
                animationDrawable.start();
                mHandler.sendEmptyMessage(MSG_PLAY_UPDATE_SPOT_UI);

                // 更新正在播放的marker的InfoWindow
                showPlayingMarkerInfoWindow();
            } else {
                // 判断当前是否有音频播放事件
                dataBinding.rlVoiceStartPlayShow.setVisibility(View.VISIBLE);
                dataBinding.tvVoiceContent.setText(Constants.mServiceIsStartConteng);
                AnimationDrawable animationDrawable;
                animationDrawable = (AnimationDrawable) dataBinding.playShowIv.getBackground();
                animationDrawable.start();
            }
//        } else if (event.getType() == 1 && !mIsActivity) { // 表示开始播放数据
//            // 判断当前是否有音频播放事件
//            dataBinding.rlVoiceStartPlayShow.setVisibility(View.VISIBLE);
//            dataBinding.tvVoiceContent.setText(Constants.mServiceIsStartConteng);
//            AnimationDrawable animationDrawable;
//            animationDrawable = (AnimationDrawable) dataBinding.playShowIv.getBackground();
//            animationDrawable.start();
        } else if (event.getType() == 2) { // 表示当前动画是暂停的状态
            if (mIsActivity) {
                // 判断当前是否有音频播放事件
                dataBinding.rlVoiceStartPlayShow.setVisibility(View.VISIBLE);
                String scenic = mVoicePlayerPosition == -1 ? mInfo.getScenicName() : mInfo.getScenicSpotList().get(mVoicePlayerPosition).getName();
                String content = "暂停 播放“" + scenic + "”语音导游";
                Constants.mServiceIsStartConteng = content;
                dataBinding.tvVoiceContent.setText(Constants.mServiceIsStartConteng);
                AnimationDrawable animationDrawable;
                animationDrawable = (AnimationDrawable) dataBinding.playShowIv.getBackground();
                animationDrawable.stop();
                mHandler.sendEmptyMessage(MSG_PLAY_UPDATE_SPOT_UI);
                if (mIsActivity) {
                    if (mCurrentMarker != null) {
                        int period = Integer.parseInt(mCurrentMarker.getTitle());
                        if (period == mVoicePlayerPosition) {
                            mCurrentMarker.hideInfoWindow();
                            mCurrentMarker.showInfoWindow();
                            mCurrentMarker.setToTop();
                        }
                    }
                    if (mIsStop) {
                        mVoicePlayerPosition = -2;
                        Constants.mScenicSpotId = 0;
                        mIsStop = false;
                    }
                }
            } else {
                // 判断当前是否有音频播放事件
                dataBinding.rlVoiceStartPlayShow.setVisibility(View.VISIBLE);
                dataBinding.tvVoiceContent.setText(Constants.mServiceIsStartConteng);
                AnimationDrawable animationDrawable;
                animationDrawable = (AnimationDrawable) dataBinding.playShowIv.getBackground();
                animationDrawable.stop();
            }
        } else if (event.getType() == 5){ // 播放完之后的操作
            mVoicePlayerPosition = -2;
            Constants.mScenicSpotId = 0;
        }
    }

    private void showPlayingMarkerInfoWindow(){
        // 显示marker对应的infowindow
        if (mBottomType < 2){
            LatLng latLng = new LatLng(Constants.mVoiceLatitude, Constants.mVoiceLongitude);
            // 移动到当前地图界面屏幕的中间位置
            mAmap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
            for (int i = 0 ; i < mAllMarker.size();i++){
                Marker marker = mAllMarker.get(i);
                if (marker.getPosition().latitude == Constants.mVoiceLatitude && marker.getPosition().longitude == Constants.mVoiceLongitude){
                    marker.showInfoWindow();
                    marker.setToTop();
                    mCurrentMarker = marker;
                }
            }
        } else {
            mIsShowPlayingMarker = true;
            OnClickListener(mTypeList.get(0),0);
        }
    }

    private boolean mIsShowPlayingMarker = false;

    /**
     * 判断我当前的位置是否在景区内部
     *
     * @return
     */
    private boolean getIsContain() {
        if (mBounds != null)
            return mBounds.contains(new LatLng(Constants.mLatitude, Constants.mLongitude));
        return false;
    }

    /**
     * 判断我当前的位置是否在景区内部
     *
     * @return
     */
    private boolean getIsContain(double lat,double lng) {
        if (mBounds != null)
            return mBounds.contains(new LatLng(lat, lng));
        return false;
    }

    /**
     * 删除整个文件夹 或者文件
     *
     * @param file
     */
    public void deleteDir(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
            }

            for (int index = 0; index < childFiles.length; index++) {
                deleteDir(childFiles[index]);
            }
        }
        file.delete();
    }

    public void saveBitmap(Bitmap bmp) {
        try { // 获取SDCard指定目录下
            String path = MD5Utils.getMd5Value(mInfo.getMapBgUrl());
            String sdCardDir = Constants.ASSETS_IMG_PATH;
            File dirFile = new File(sdCardDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            String bitmapName = path + ".png";
            File file = new File(sdCardDir, bitmapName);
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    LocationSource.OnLocationChangedListener mListener;
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;
    private MyLocationStyle myLocationStyle;

    /**
     * set map info
     */
    private void setUpMapLocationMine() {
        mAmap.setLocationSource(mLocationSource);// setting location listener
        mAmap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        mAmap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        mAmap.setMyLocationType(AMap.MAP_TYPE_NORMAL);

        // 设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        // 获取一次定位结果： //该方法默认为false。
        mLocationOption.setOnceLocation(false);
        mLocationOption.setOnceLocationLatest(false);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);

        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();

        // 如果要设置定位的默认状态，可以在此处进行设置
        myLocationStyle = new MyLocationStyle();
        // 如果可以就将用户的图片传到这个bitmap
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_scenic_spot_mine_location_icon));

        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色

        myLocationStyle.anchor(0.5f,0.576f);
//        myLocationStyle.strokeColor(Color.argb(19, 73, 190, 255));// 设置圆形的边框颜色 rgba(73, 190, 255, 19)
//        myLocationStyle.radiusFillColor(Color.argb(19, 73, 190, 255));// 设置圆形的填充颜色
        mAmap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER));
    }

    LocationSource mLocationSource = new LocationSource() {
        @Override
        public void activate(OnLocationChangedListener onLocationChangedListener) {
            mListener = onLocationChangedListener;
            if (mlocationClient == null) {
                mlocationClient = new AMapLocationClient(mContext);
                mLocationOption = new AMapLocationClientOption();
                mlocationClient.setLocationListener(mapLocationListener);
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                mlocationClient.setLocationOption(mLocationOption);
                mlocationClient.startLocation();
            }
        }
        @Override
        public void deactivate() {
            mListener = null;
            if (mlocationClient != null) {
                mlocationClient.stopLocation();
                mlocationClient.onDestroy();
            }
            mlocationClient = null;
        }
    };

    AMapLocationListener mapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (mListener != null && amapLocation != null) {
                if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                    mListener.onLocationChanged(amapLocation);// show system blue spot
                    Constants.mIsOtherLocation = false;
                    mIsLocationSuccess = true;
                    Constants.mLongitude = amapLocation.getLongitude();
                    Constants.mLatitude = amapLocation.getLatitude();
                    Constants.mCity = amapLocation.getCity();
                    Constants.mAddress = amapLocation.getAddress();
                    Constants.mLocation = amapLocation.getLongitude() + "," + amapLocation.getLatitude();
                    String mCityCode = amapLocation.getAdCode();
                    if (mCityCode != null) {
                        mCityCode = mCityCode.substring(0, 4) + "00";
                        Constants.mCityCode = mCityCode;
                    }
                } else {
                    if (ActivityUtils.getTopActivity() == mContext) {
                        if (GPSUtils.isOPen(mContext)){
                            ToastUtils.showText(mContext, "定位失败，当前GPS信号弱");
                        } else {
                            ToastUtils.showText(mContext, "定位失败，请打开GPS位置权限");
                        }
                        String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                        mIsLocationSuccess = false;
                    }
                }
            }
        }
    };

    private boolean mIsLocationSuccess = false;

    /**
     * input text listener
     */
    TextChangedListener mTextWatcher = new TextChangedListener() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String data = dataBinding.etSearch.getText().toString();
            if ("".equals(data) || null == data){
                // 更新Adapter
                updateSearch("");
                return;
            }
            updateSearch(data);
        }
    };


    private boolean hideInputView() {
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive(dataBinding.etSearch)) {
            inputMethodManager.hideSoftInputFromWindow(ScenicSpotMapActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            return true;
        }
        return false;
    }

    private int mDownLoadType = 0;

    private String mVoiceFileName = "";

    /**
     * 文件下载
     *
     * @param url
     */
    public void downFileNotUI(String url) {
        if (TextUtils.isEmpty(mVoiceFileName)) {
            String filename = MD5Utils.getMd5Value(url);
            mVoiceFileName = Constants.SCENIC_VOICE_FILE_PATH + filename;
        }
        String fileZipName = url.substring(url.lastIndexOf("/")+1);
        if (!fileZipName.contains(".zip")){
            fileZipName += ".zip";
        }
        File file = new File(mVoiceFileName);
        if (file.exists()){
            deleteDir(file);
        }
        String finalFileZipName = fileZipName;
        String finalFileZipName1 = fileZipName;
        new Thread(new Runnable() {
            @Override
            public void run() {
                DownloadUtil.get().download(url, mVoiceFileName, finalFileZipName1, new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess(File file) {
                        //下载完成进行相关逻辑操作
                        String filepath = mVoiceFileName;
                        String filepath1 = mVoiceFileName+"/"+finalFileZipName;
                        try {
                            UnZipFolder(filepath1, filepath); // 解压
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mDownLoadType = 3;
                        if (mScenicMapOfflineDialog != null) {
                            if (mScenicMapOfflineDialog.isShowing()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showScenicMapOfflineDialog(3,0);
                                    }
                                });
                            }
                        }
                    }
                    @Override
                    public void onDownloading(int progress) {
                        mDownLoadType = 2;
                        if (mScenicMapOfflineDialog != null) {
                            if (mScenicMapOfflineDialog.isShowing()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showScenicMapOfflineDialog(2,progress);
                                    }
                                });
                            }
                        }
                    }
                    @Override
                    public void onDownloadFailed(Exception e) {
                        mDownLoadType = 0;
                        //下载异常进行相关提示操作
                        if (mScenicMapOfflineDialog != null) {
                            if (mScenicMapOfflineDialog.isShowing()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showScenicMapOfflineDialog(0,0);
                                    }
                                });
                            }
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * DeCompress the ZIP to the path
     * @param zipFileString  name of ZIP
     * @param outPathString   path to be unZIP
     * @throws Exception
     */
    private void UnZipFolder(String zipFileString, String outPathString) throws Exception {
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry zipEntry;
        String szName = "";
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                // get the folder name of the widget
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + szName);
                folder.mkdirs();
            } else {

                File file = new File(outPathString + File.separator + szName);
                file.createNewFile();
                // get the output stream of the file
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // read (len) bytes into buffer
                while ((len = inZip.read(buffer)) != -1) {
                    // write (len) byte from buffer at the position 0
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        inZip.close();
        // 保存数据库
        viewModel.setAddScenicVoicePackageToDataBase(mContext,mPackageInfo);
        // 解压成功之后删除当前zip文件
        new File(zipFileString).delete();
        // 将数据获取到
        mVoiceList = viewModel.getLocalFileAllVoice(mPackageInfo.getResUrl());
    }

    private boolean mIsShowNear = false;
    /**
     * 系统检测到，您有正在出行的队伍， 是否需要在景区地图显示成员位置？
     */
    private ShowMemberLocationHintDialog mShowMemberLocationHintDialog;
    private int MSG_SHOW_MEMBER_LOC_HINT_TIME = 3000; // 消失3秒后显示
    private boolean mIsFristShowMemberLocation = true;
    private void showMemberLocationHintDialog(){
        try {
            if (mShowMemberLocationHintDialog == null){
                mShowMemberLocationHintDialog = new ShowMemberLocationHintDialog(mContext, new ShowMemberLocationHintDialog.setShowMemberLocationHintListener() {
                    @Override
                    public void sureShowMember(boolean show) { // 显示成员 近期是否不再显示
                        mIsOpenGroupLocation = true;
                        mIsOpenGroupLocationTmp = true;
                        mIsShowNear = show;
                        if (mIsShowNear){
                            SPUtils.putString(SPUtils.SHOW_TIME_NEAR, String.valueOf(System.currentTimeMillis()));
                        }
                        if (mChangeGroupList.size()>1) { // 多个行程群进入选择界面
                            // 显示选择
                            showMemberLocationChoiceDialog();
                        } else { // 只有一个行程群
                            changeGroupAllData(mChangeGroupList.get(0).getGroupId());
                        }
                    }

                    @Override
                    public void cancelShowMember(boolean show) {
                        mIsOpenGroupLocation = false;
                        mIsOpenGroupLocationTmp = false;
                        mIsShowNear = show;
                        // 不显示的点击
                        if (mIsShowNear){
                            SPUtils.putString(SPUtils.SHOW_TIME_NEAR, String.valueOf(System.currentTimeMillis()));
                        }
                    }
                });
            }
            mShowMemberLocationHintDialog.setViewData(mIsShowNear);
            mShowMemberLocationHintDialog.show();
        } catch (Exception e){}
    }

    /**
     * 是否开启成员位置？
     */
    private ShowMemberLocationOpenDialog mShowMemberLocationOpenDialog;
    private void showMemberLocationOpenDialog(){
        if (mShowMemberLocationOpenDialog == null){
            mShowMemberLocationOpenDialog = new ShowMemberLocationOpenDialog(mContext, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.tv_cancel: // 暂不开启
                            mIsOpenGroupLocation = false;
                            mIsOpenGroupLocationTmp = false;
                            break;
                        case R.id.tv_sure: // 开启
                            mIsOpenGroupLocation = true;
                            mIsOpenGroupLocationTmp = true;
                            break;
                        default:
                            break;
                    }
                    mShowMemberLocationOpenDialog.dismiss();
                    if (mIsOpenGroupLocation){
                        if (mChangeGroupList.size()>1) { // 多个行程群进入选择界面
                            // 显示选择
                            showMemberLocationChoiceDialog();
                        } else { // 只有一个行程群
                            changeGroupAllData(mChangeGroupList.get(0).getGroupId());
                        }
                    }
                }
            });
        }
        mShowMemberLocationOpenDialog.show();
    }

    /**
     * 更换群dialog
     */
    private ShowMemberLocationChoiceDialog mShowMemberLocationChoiceDialog;
    private int mShowMemberLocationChoiceType = 1; // 1 表示之前没有选择过 2 表示之前选择过
    public boolean mIsOpenGroupLocation = false;
    public boolean mIsOpenGroupLocationTmp = false;
    private List<MyTalkGroupBean> mChangeGroupList = new ArrayList();
    private MyTalkGroupsBean mChangeGroupInfo = null;
    private void showMemberLocationChoiceDialog(){
        mIsOpenGroupLocationTmp = mIsOpenGroupLocation;
        if (mShowMemberLocationChoiceDialog == null){
            mShowMemberLocationChoiceDialog = new ShowMemberLocationChoiceDialog(mContext, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.iv_cancel){
                        mShowMemberLocationChoiceDialog.dismiss();
                    } else if (v.getId() == R.id.iv_open_location){ // 关闭成员位置的点击
                        // 弹出提示框
                        mIsClosePersonLocation = true;
                        showConfirmDialog("确定要关闭成员位置？");
                    }
                }
            });
        }
        if (TextUtils.isEmpty(mGroupId)){ // 如果之前没有选择过就显示没有选择过的情况
            mShowMemberLocationChoiceType = 1;
        } else { // 如果之前有选择过就显示选择过的情况
            if (mIsOptionCloseLocation) { // 是否操作关闭位置
                mIsOptionCloseLocation = false;
                mShowMemberLocationChoiceType = 1;
            } else {
                mShowMemberLocationChoiceType = 2;
            }
        }
        mShowMemberLocationChoiceDialog.setViewData(mChangeGroupList,mShowMemberLocationChoiceType,mIsOpenGroupLocation);
        mShowMemberLocationChoiceDialog.show();
    }

    private boolean mIsClosePersonLocation = false;
    private boolean mIsOptionCloseLocation = false;

    /**
     * 群列表的点击事件
     * @param bean
     */
    public void OnChangeGroupInfo(MyTalkGroupBean bean){
        mShowMemberLocationChoiceDialog.dismiss();
        changeGroupAllData(bean.getGroupId());
    }

    /**
     * 开始切换
     */
    private void changeGroupAllData(String groupId){
        if (TextUtils.isEmpty(groupId)){
            return;
        }
        if (groupId.equals(mGroupId) && mIsOpenGroupLocationTmp == mIsOpenGroupLocation){ // 如果是当前群，并且开启位置都相同，表示未作更改
            return;
        }
        // 清除界面上所有的人员marker
        clearAllPersonMarker();
        mIsOpenGroupLocation = mIsOpenGroupLocationTmp;
        // 排序
        mChangeGroupList.get(0).setSelect(false);// 将之前选中的设置为未选中
        List<MyTalkGroupBean> list = new ArrayList(mChangeGroupList);
        mChangeGroupList.clear();
        for (int i = 0 ; i < list.size(); i++){
            if (groupId.equals(list.get(i).getGroupId())){
                mChangeGroupList.add(list.get(i)); // 将已经选中的数据排在第一个
                list.remove(list.get(i));
                break;
            }
        }
        mChangeGroupList.addAll(list); // 将剩余的数据添加到list中

        mGroupInfo = mChangeGroupList.get(0);
        mGroupId = mGroupInfo.getGroupId();
        // 切换对讲功能的群
        // 入群后,加入或切换对讲组
        TalkManage mTalkManage = TalkManage.getInstance();
        mTalkManage.joinOrSwitchIntercomGroup(mGroupId);

        if (!mIsOpenGroupLocation){ // 如果当前是属于不开启的状态 就清空掉群编号 并且 调用设置好的数据接口
            if (!TextUtils.isEmpty(mGroupId)) { // 保证当前用户是有显示成员的情况
                viewModel.setUserGroupOrPosition(mToken, (TextUtils.isEmpty(mGroupId) ? null : mGroupId), 1);
                if (!mGroupId.equals(mChangeGroupInfo.getGroupId())) { // 如果这里不是之前已经设置好的就进行设置
                    // 调用设置接口
                    ToastUtils.showText(mContext,"成员位置已关闭");
                } else {
                    mChangeGroupInfo.setGroupId(""); // 将原来设置的值设为空 方便下一次调用接口
                }
                mGroupId = "";
                mGroupInfo = null;
                dataBinding.tvGroupNum.setText("开启");
                return;
            }
        }
        mIsOptionCloseLocation = false;
        mChangeGroupList.get(0).setSelect(true);
        if (!mGroupId.equals(mChangeGroupInfo.getGroupId())) { // 如果这里不是之前已经设置好的就进行设置
            // 调用设置接口
            viewModel.setUserGroupOrPosition(mToken, (TextUtils.isEmpty(mGroupId) ? null : mGroupId), 2);
            ToastUtils.showText(mContext, "操作成功");
        } else {
            mChangeGroupInfo.setGroupId(""); // 将原来设置的值设为空 方便下一次调用接口
        }
        // 更换群操作
        if (mIsOpenGroupLocation) { // 表示开启了成员信息
            if (!mStartLocationInfo) { // 当前信息只需要设置一次就可以一直更新
                mStartLocationInfo = true;
                mHandler.sendEmptyMessage(HANDLER_GET_MEMBER_INFO); // 开始获取群信息
                // EventBus.getDefault().post(new LocationShareStatusEvent(mGroupId,mUserId,true)); // 发送位置
                mHandler.sendEmptyMessage(HANDLER_UPDATE_SHARE_LOCATION);// 开始发送我的位置信息
            }
            // 双击不能切换群 设置为不能双击切换 在 OnStart中调用了关闭 onDestroy中调用了开启
        }
        if (mGroupInfo != null) {
            int total_num = mGroupInfo.getMemberNum();
            int num = getIsContain() ? 1 : 0; // 判断我是否在当前景区内 在就显示1
            String data = "<font color='#24A4FF'>" + num + "</font>" + "/" + total_num;
            dataBinding.tvGroupNum.setText(Html.fromHtml(data));
        }
    }

    private void clearAllPersonMarker(){
        for (Marker marker : mAllPersonMarker) {
            marker.remove();
        }
        mAllPersonMarker.clear();
    }

    MyTalkGroupBean mGroupInfo = null;
    private boolean mStartLocationInfo = false;
    //*************************************************添加人员marker代码 start
    private String mGroupId = ""; // 显示群成员的群id
    private boolean mIsShareLoc = true; // 是否将我的位置分享给当前群的人员
    private List<GroupUserInfo> mCurrentGroupInfoList = new ArrayList<>();
    private List<GroupUserInfo> mMemberInfo_All = new ArrayList<>();
    private String mUserId = ""; // 需要在开始的时候初始化
    private String mGroupNickName = "";// 在当前群内的昵称
    private String mGroupAvtor = "" ;// 在当前群内的头像
    private String mGroupgroupId = "" ;// 在当前群内的我的群的id
    private String mGroupCarInfo = "" ;// 在当前群内的车辆信息
    private int mGroupRole = 0 ;// 在当前群内的角色
    private boolean mGroupLeader = false ;// 在当前群内的领队
    List<Marker> mAllPersonMarker = new ArrayList<>(); // 界面中所有人员的marker
    private Marker mCurrentMemMarker = null;

    private String PERSON_TYPE = "person";
    private String OTHER_TYPE = "other";
    private String SPOT_TYPE = "spot";

    private final int HANDLER_GET_MEMBER_INFO = 0x0101;
    private final int HANDLER_UPDATE_MEMBER_INFO = 0x0102;
    private final int HANDLER_UPDATE_SHARE_LOCATION = 0x0103;

    /**
     * 获取数据库中当前群内所有的数据 如果不在景区内 是否还需要进行marker的绘制显示
     */
    private void getUserInfoToDataBase(){
        if (!mIsOpenGroupLocation) {
            return;
        }
        if (TextUtils.isEmpty(mGroupId)){
            return;
        }
        List<GroupUserInfo> list = TourDatabase.getDefault(mContext).getGroupUserInfoDao().getDataAll(mGroupId);
        if (list != null){
            if (list.size() > 0){
                int size = list.size();
                for (int i = 0 ; i < size ; i++){
                    GroupUserInfo userInfo = list.get(i);
                    if (!TextUtils.isEmpty(userInfo.userId) && !TextUtils.isEmpty(userInfo.groupId)) {
                        if (!mUserId.equals(userInfo.userId)) {
                            if (getIsContain(userInfo.latitude,userInfo.longitude) && ((System.currentTimeMillis() - userInfo.time) < Constants.TIME_OUT)) {  // 判断坐标在景区内才进行显示添加 并且在线
                                mCurrentGroupInfoList.add(userInfo);
                            }
                        } else {
                            // 当前群内我的信息
                            if (userInfo != null && TextUtils.isEmpty(mGroupNickName) && !mGroupgroupId.equals(mGroupId)){
                                // 我的信息进行赋值
                                mGroupNickName = userInfo.nickName;
                                mGroupAvtor = userInfo.avatar;
                                mGroupgroupId = userInfo.groupId;
                                mGroupCarInfo = userInfo.numberPlate;
                                mGroupRole = userInfo.type == 0 ? 1 : 0; // 1: 队长  2: 成员 0 群主
                                mGroupLeader = userInfo.type == 1 ? true : false;
                            }
                        }
                    }
                }
                // 添加到界面
                updateRightListInfo(mCurrentGroupInfoList);
            }
        }
        if (mGroupInfo != null && mIsOpenGroupLocation) {
            int total_num = mGroupInfo.getMemberNum(); // zhushi
            int num = getIsContain() ? (mCurrentGroupInfoList.size()+1) : (mCurrentGroupInfoList.size());
            String data = "<font color='#24A4FF'>" + num + "</font>" + "/" + ((list.size() == 0 ) ? total_num : list.size());
            dataBinding.tvGroupNum.setText(Html.fromHtml(data));
        }
        mCurrentGroupInfoList.clear();
    }

    /**
     * 更新界面 和对比界面中的marker的变化
     * @param list
     */
    private void updateRightListInfo(List<GroupUserInfo> list){
        // 将数据库的数据放到MMemberInfo中 // 数据更新
        mMemberInfo_All = new ArrayList<>(list);
        // 将没有的maker进行移除掉
        int pos = mAllPersonMarker.size();
        List<Marker> mMarkerList = new ArrayList<>(mAllPersonMarker);
        for (int k = 0; k < mMemberInfo_All.size(); k++) { // 遍历当前所有用户
            for (int j = 0; j < pos; j++) {
                String title = mAllPersonMarker.get(j).getTitle();
                if (title.contains(".")){ // 表示用户离线的情况
                    title = title.substring(0,title.length()-1);
                }
                if (mMemberInfo_All.get(k).userId.equals(title)) { //表示有上一次的用户的数据
                    // 不会进行移除的操作
                    mMarkerList.remove(mAllPersonMarker.get(j));
                    break;
                }
            }
        }
        // 上面获取的mMarkerList剩余的数据是需要被移除的marker数据
        for (int i = 0; i < mMarkerList.size(); i++) {
            mMarkerList.get(i).remove();
            mAllPersonMarker.remove(mMarkerList.get(i));
        }
        // 当时使用缓存的情况下 可能这几个同时没有数据 将界面中的marker更新
        if (mMemberInfo_All.size() == 0 && mCurrentGroupInfoList.size() == 0) {
            int pos1 = mAllPersonMarker.size();
            for (int j = 0; j < pos1; j++) {
                mAllPersonMarker.get(j).remove();
            }
            mAllPersonMarker.clear();
        }
        if (mIsActivity) {
            // update adapter
            setShowMarkerIconInfo();
        }
    }

    /**
     * 添加marker到界面上
     */
    private void setShowMarkerIconInfo() {
        if (mMemberInfo_All == null) {
            return;
        }
        if (mMemberInfo_All.size() < 1) {
            return;
        }
        for (int i = 0; i < mMemberInfo_All.size(); i++) {
            GroupUserInfo info = mMemberInfo_All.get(i);
            if (info == null) {
                continue;
            }
            if (info.time != 0 &&
                    info.latitude != 0.0f &&
                    info.longitude != 0.0f &&
                    !info.userId.equals(mUserId)) { // 判断当前用户信息
                setAysnMarkerSurface(i, info, mMemberInfo_All.size());
            }
        }
    }

    private void setAysnMarkerSurface(int isss,GroupUserInfo info,int size){
        GlideApp.with(this).asBitmap().load(TextUtils.isEmpty(info.avatar) ? R.drawable.icon_head_default : info.avatar)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        if (size > isss) {
                            addMyCarAndPersonLocationMarker(
                                    new LatLng(info.latitude, info.longitude),
                                    resource,
                                    getTypeStr(info.type),
                                    info.nickName,
                                    info.userId,
                                    info.time);
                        }
                    }
                });
    }

    /**
     * @param latLng 经纬度信息
     * @param bitmapurl 用户头像
     * @param type 用户类型 0 群主 1 领队 2 成员
     * @param name 昵称
     * @param userId 用户id
     * @param time 之前时间
     */
    private void addMyCarAndPersonLocationMarker(LatLng latLng, Bitmap bitmapurl, String type, String name, String userId,long time) {
        long update  = System.currentTimeMillis() - time;
        // 在获取之前写逻辑
        if (mAllPersonMarker != null) {
            if (mAllPersonMarker.size() > 0) {
                for (int i = 0; i < mAllPersonMarker.size(); i++) {
                    String con = userId;
                    String title = mAllPersonMarker.get(i).getTitle();
                    String title1 = "";
                    if (title.contains(".")) { // 表示用户离线的情况
                        title1 = title.substring(0, title.length() - 1);
                    } else { // 表示用户在线也没有大于10Km的情况
                        title1 = title;
                    }
                    if (con.equals(title1)) { // 判断是否是当前的marker是否是我需要的marker 是的话进行需要的操作显示
                        // 离线的情况
                        if (update >= Constants.TIME_OUT) { // 离线的情况
                            if (!title.contains(".")) {
                                mAllPersonMarker.get(i).remove();
                                mAllPersonMarker.remove(mAllPersonMarker.get(i));
                                break;
                            } else {
                                mAllPersonMarker.get(i).setPosition(latLng);
                                return;
                            }
                        } else if (update < Constants.TIME_OUT) { // 在线的情况
                            if (title.contains(".")) {
                                mAllPersonMarker.get(i).remove();
                                mAllPersonMarker.remove(mAllPersonMarker.get(i));
                                break;
                            } else {
                                mAllPersonMarker.get(i).setPosition(latLng);
                                return;
                            }
                        }
                    }
                }
            }
        }
        if (mAmap != null) {
            View view = View.inflate(this, R.layout.marker_scenic_spot_person_item, null); // 人员显示的marker
            RoundImageView ivheadImg = (RoundImageView) view.findViewById(cn.xmzt.www.R.id.maker_head_icon);
            // judging show myLoc
            if (update >= Constants.TIME_OUT) {
                ivheadImg.setImageResource(R.drawable.map_marker_offline_person_head_icon);
            } else {
                ivheadImg.setImageBitmap(bitmapurl);
            }
            Bitmap bitmap = BitmapUtils.convertViewToBitmap(view);

            int radius = DensityUtil.dip2px(this, 110);
            MarkerOptions markerOption = null;

            String data = userId;
            if (update >= Constants.TIME_OUT) {
                data += ".";
            }
            markerOption = new MarkerOptions()
                    .position(latLng)
                    .anchor(0.5f,0.708f)
                    .setInfoWindowOffset(0, radius)
                    .snippet(PERSON_TYPE)
                    .title(data) // 在超过10Km的title中添加一个逗号
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
            Marker marker = mAmap.addMarker(markerOption);
            com.amap.api.maps.model.animation.Animation markerAnimation = new ScaleAnimation(1.0f, 1.6f, 1.0f, 1.6f);
            markerAnimation.setDuration(0);
            markerAnimation.setFillMode(1);
            marker.setAnimation(markerAnimation);
            marker.setClickable(true);
            mAllPersonMarker.add(marker);
        }
    }

    /**
     *  获取infowindow应该显示为位置
     * @return type 0 表示不做更改 1 顶部 2 顶部左边 3 顶部右边 4 右边 5 左边 6 底部左边 7 顶部右边 8 底部
     */
    private int showInfoWindowLTRBPerson(Marker marker){
        Point markerPoint = mAmap.getProjection().toScreenLocation(marker.getPosition()); // 返回一个从地图位置转换来的屏幕位置
        int type = 0;
        int screen_X = getResources().getDisplayMetrics().widthPixels;
        int screen_Y = getResources().getDisplayMetrics().heightPixels;
        int marker_x = markerPoint.x;
        int marker_y = markerPoint.y;
        int view_x = DpUtil.dp2px(mContext,164)/2; // infowindow的宽度
        int view_y = DpUtil.dp2px(mContext,70)/2; // infowindow的高度
        int top_y = DpUtil.dp2px(mContext,103);
        int Bottom_y = DpUtil.dp2px(mContext,150);
        if (((screen_X-marker_x) < view_x)){ // 横向判断当前marker位置是否是在右边 表示在右边
            // 当前已经在右边了 判断有没有在顶部
            if ((screen_Y - marker_y) < (view_y + Bottom_y)){ // 当前在底部 右边底部
                type = 7;
            } else if ((marker_y-top_y) < view_y){ // 当前在顶部 右边顶部
                type = 3;
            } else { // 右边
                type = 4;
            }
        } else if (marker_x < view_x){ // 当前在左边
            if ((screen_Y - marker_y) < (view_y + Bottom_y)){ // 当前在底部 左边底部
                type = 6;
            } else if ((marker_y-top_y) < view_y){ // 当前在顶部 左边顶部
                type = 2;
            } else { // 左边
                type = 5;
            }
        } else if ((marker_y-top_y) < view_y){ // 当前在顶部 顶部
            type = 1;
        } else if ((screen_Y - marker_y) < (view_y + Bottom_y)){ // 当前在底部 底部
            type = 8;
        } else { //不做操作
            type = 0;
        }
        return type;
    }

    /**
     * setting original animation
     */
    private void setClickedMarkerAnim() {
        if (mCurrentMemMarker != null) {
            com.amap.api.maps.model.animation.Animation markerAnimation = new ScaleAnimation(1.0f, 1.0f, 1.0f, 1.0f); // update original view
            markerAnimation.setDuration(0);  //set anim time
            markerAnimation.setFillMode(1);
            mCurrentMemMarker.setAnimation(markerAnimation);
        }
    }

    /**
     * click then big view marker
     */
    private void setNotClickedMarkerAnim() {
        if (mCurrentMemMarker != null) {
            com.amap.api.maps.model.animation.Animation markerAnimation = new ScaleAnimation(1.0f, 1.6f, 1.0f, 1.6f); //click then big view marker
            markerAnimation.setDuration(0);
            markerAnimation.setFillMode(1);
            mCurrentMemMarker.setAnimation(markerAnimation);
        }
    }

    /**
     * 根据类型获取群成员类型文本
     * @param type
     * @return
     */
    private String getTypeStr(int type){
        String info = "成员";
        if (type == 0){
            info = "群主";
        } else if (type == 1){
            info = "领队";
        } else {
            info = "成员";
        }
        return info;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void LocationShareStatusEventBus(LocationShareStatusEvent event) {
        if (event.getGroupId().equals(mGroupId)) {
            mIsShareLoc = event.isStatus();
            // 向群内发送我的位置信息
            mHandler.sendEmptyMessage(HANDLER_UPDATE_SHARE_LOCATION);
        }
    }

    /**
     * 注册群信息更新监听
     *
     * @param register
     */
    public void registerTeamUpdateObserver(boolean register) {
        // 监听群数据更新监听
        NimUIKit.getTeamChangedObservable().registerTeamDataChangedObserver(teamDataChangedObserver, register);
    }

    /**
     * 群资料变动通知和移除群的通知（包括自己退群和群被解散）
     */
    TeamDataChangedObserver teamDataChangedObserver = new TeamDataChangedObserver() {
        @Override
        public void onUpdateTeams(List<Team> teams) {
        }
        @Override
        public void onRemoveTeam(Team t) {
            if (t == null) {
                return;
            }
            String teamId = t.getId();
            if (teamId != null && teamId.equals(mGroupId)) {
                boolean isMyTeam=t.isMyTeam();
                if(!isMyTeam){
                    // 相除对讲相关业务
                    UserHelper.clearTalkBusiness(false);
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent);
                }
            }
        }
    };
    //*************************************************添加人员marker代码 end

    // ***************************************************提示WIFI下载问题
    private ScenicSpotWIFIHintDialog mScenicSpotWIFIHintDialog = null;
    private void showScenicSpotWIFIHintDialog(){
        if (mScenicSpotWIFIHintDialog == null){
            mScenicSpotWIFIHintDialog = new ScenicSpotWIFIHintDialog(mContext, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.tv_cancel){
                        mScenicSpotWIFIHintDialog.dismiss();
                    } else if (v.getId() == R.id.tv_sure){
                        mScenicSpotWIFIHintDialog.dismiss();
                        showScenicMapOfflineDialog(mDownLoadType,0);
                    }
                }
            });
        }
        mScenicSpotWIFIHintDialog.show();
    }
    // ***************************************************提示WIFI下载问题
}
