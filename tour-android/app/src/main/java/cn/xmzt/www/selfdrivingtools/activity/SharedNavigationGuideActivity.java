package cn.xmzt.www.selfdrivingtools.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.BroadcastMode;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ActivitySharedNavigationGuideBinding;
import cn.xmzt.www.glide.GlideApp;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.roomdb.TourDatabase;
import cn.xmzt.www.roomdb.beans.GroupUserInfo;
import cn.xmzt.www.selfdrivingtools.bean.SharedNavigationMapRightInfo;
import cn.xmzt.www.selfdrivingtools.event.NavigationTypeEvent;
import cn.xmzt.www.selfdrivingtools.overlay.AMapUtil;
import cn.xmzt.www.selfdrivingtools.overlay.NaviUtil;
import cn.xmzt.www.selfdrivingtools.viewmodel.SharedNavigationGuideViewModel;
import cn.xmzt.www.suspension.FloatActionController;
import cn.xmzt.www.suspension.FloatPermissionManager;
import cn.xmzt.www.utils.BitmapUtils;
import cn.xmzt.www.utils.DensityUtil;
import cn.xmzt.www.utils.ErrorInfo;
import cn.xmzt.www.utils.GPSUtils;
import cn.xmzt.www.utils.SPUtils;
import cn.xmzt.www.utils.StatusBarUtil;
import cn.xmzt.www.utils.TextViewUtil;
import cn.xmzt.www.utils.TimeUtil;
import cn.xmzt.www.utils.ToastUtils;
import cn.xmzt.www.view.RoundImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 *  share navigation guide
 */
public class SharedNavigationGuideActivity extends TourBaseActivity<SharedNavigationGuideViewModel, ActivitySharedNavigationGuideBinding> implements AMapNaviViewListener, AMapNaviListener {

    protected AMapNavi mAMapNavi;
    protected NaviLatLng mEndLatlng = new NaviLatLng(40.084894,116.603039);
    protected NaviLatLng mStartLatlng = new NaviLatLng(39.825934,116.342972);
    protected final List<NaviLatLng> sList = new ArrayList<NaviLatLng>();
    protected final List<NaviLatLng> eList = new ArrayList<NaviLatLng>();
    protected List<NaviLatLng> mWayPointList = new ArrayList<NaviLatLng>();

    private int mDialogDbyd = 0; // 躲避拥堵
    private int mDialogDbsf = 0; // 躲避收费
    private int mDialogDbgs = 0; // 躲避高速
    private int mDialogGsyx = 1; // 高速优先
    private int mDialogGuideVoicePlay = 0; // 导航语音播报 0 详细播报 1 简介播报 2 静音
    private int mDialogGuideShow = 0; // 导航视角 0 车头朝北 1 地图朝北 2 3D
    private int mDialogDayNight = 1; // 日夜模式 0 自动 1 白天 2 夜间

    private boolean isDriving = true;

    private Context mContext;

    private AMap mAmap;

    private String mGroupId = "";
    private int routeId = -1;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_shared_navigation_guide;
    }

    @Override
    protected SharedNavigationGuideViewModel createViewModel() {
        viewModel = new SharedNavigationGuideViewModel();
        viewModel.setIView(this);
        return viewModel;
    }

    @Override
    protected void initData() {
        mContext = this;
        EventBus.getDefault().register(this);
        dataBinding.setModel(viewModel);
        dataBinding.setActivity(this);

        if (!GPSUtils.isOPen(mContext)){
            //定位依然没有打开的处理
            ToastUtils.showText(mContext,"需要打开GPS才能正常使用");
            finish();
        }
        mUserId = String.valueOf(TourApplication.getUser().getUserId());
        mIsActivity = true;
        mDestroyActivity = false;

        isDriving = getIntent().getBooleanExtra("isDriving",true);
        double slat = getIntent().getDoubleExtra("slat",0.0);
        double slng = getIntent().getDoubleExtra("slng",0.0);
        double elat = getIntent().getDoubleExtra("elat",0.0);
        double elng = getIntent().getDoubleExtra("elng",0.0);
        mGroupId = getIntent().getStringExtra("groupId");
        routeId = getIntent().getIntExtra("routeId",0);
        mStartLatlng = new NaviLatLng(slat,slng);
        mEndLatlng = new NaviLatLng(elat,elng);

        mDialogGsyx = SPUtils.getInt(SPUtils.GUIDE_GSYX);
        mDialogDbgs = SPUtils.getInt(SPUtils.GUIDE_DBGS);
        mDialogDbsf = SPUtils.getInt(SPUtils.GUIDE_DBSF);
        mDialogDbyd = SPUtils.getInt(SPUtils.GUIDE_DBYD);
        mDialogGuideVoicePlay = SPUtils.getInt(SPUtils.GUIDE_VOICE_MODE);
        mDialogGuideShow = SPUtils.getInt(SPUtils.GUIDE_SHOW_MODE);
        mDialogDayNight = SPUtils.getInt(SPUtils.GUIDE_DAY_NIGHT_MODE);

        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);
        if (mAmap == null) {
            mAmap = dataBinding.naviView.getMap();
            dataBinding.naviView.setOnMarkerClickListener(mMarkerListener);
//            mAmap.setOnMarkerClickListener(mMarkerListener);
            // setting show infoWindow
            mAmap.setInfoWindowAdapter(mAMapOtherAdapter);
            // 添加marker
            mHandler.sendEmptyMessage(HANDLER_GET_MEMBER_INFO);
        }
        mAMapNavi.setUseInnerVoice(true);

        mAMapNavi.setEmulatorNaviSpeed(75);

        dataBinding.naviView.onCreate(getIntent().getExtras());
        dataBinding.naviView.setAMapNaviViewListener(this);
        dataBinding.naviView.setTrafficLine(true);

        dataBinding.naviView.setLazyNextTurnTipView(dataBinding.amapToIv);

        // show day or night page
        setDayNightMode();
        // voice
        setGuideVoiceMode();
        // guide show
        setGuideShowMode();

        initClickListener();

        updateMapView(true);

        dataBinding.amapLukuangIv.setChecked(true);

        initDialogData();

        mAMapNavi.startNavi(AMapNavi.GPSNaviMode);
    }

    private final int HANDLER_GET_MEMBER_INFO = 0x0101;
    private final int HANDLER_UPDATE_MEMBER_INFO = 0x0102;
    private long mEndClickTime = 0;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                if ((System.currentTimeMillis() - mEndClickTime) >= 5000){
                    setContinueGuideOptions();
                }
            }
            if (msg.what == 2){
                if ((System.currentTimeMillis() - mEndClickTime) >= 10000){
                    if (mIsBottomShow) {
                        setHideBottomView();
                    }
                    setContinueGuideOptions();
                }
            }
            if (msg.what == 3){
                if ((System.currentTimeMillis() - mEndClickTime) >= 10000){
                    if (mIsExitShow) {
                        setShowHideInfoExit();
                    }
                    setContinueGuideOptions();
                }
            }
            if (msg.what == HANDLER_UPDATE_MEMBER_INFO) { // update member info to view
                if (!mDestroyActivity) {
                    // add marker data and init right view
                    getUserInfoToDataBase();
                }
            }
            if (msg.what == HANDLER_GET_MEMBER_INFO){
                if (!mDestroyActivity) {
                    getUserInfoToDataBase();
                    if (!mDestroyActivity) {
                        mHandler.sendEmptyMessageDelayed(HANDLER_GET_MEMBER_INFO, Constants.UPDATE_TIME_MAP_INFO);
                    }
                }
            }
        }
    };


    private float DownX = 0;
    private float moveX = 0;
    private float DownY = 0;
    private float moveY = 0;
    private long currentMS = 0;


    private void hideShowMarker(){
        if (mCurrentMemMarker != null) {
            if (PERSON_TYPE.equals(mCurrentMemMarker.getSnippet())) {
                mCurrentMemMarker.startAnimation();
                setNotClickedMarkerAnim();
                mCurrentMemMarker.hideInfoWindow();
                mCurrentMemMarker = null;
            }
        } else if (mCurrentOtherMarker != null){
            mCurrentOtherMarker.hideInfoWindow();
            mCurrentOtherMarker = null;
        }
    }

    private void initClickListener(){
        // map click
        dataBinding.naviView.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent event) {
                mEndClickTime = System.currentTimeMillis();
                mHandler.sendEmptyMessageDelayed(1,5000);
                updateMapView(false);

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

        // continue guide
        dataBinding.amapContinueGuideTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContinueGuideOptions();
            }
        });
        // all show
        dataBinding.amapAllShowIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataBinding.naviView.isRouteOverviewNow()) { // judging is all show
                    setContinueGuideOptions();
                } else {
                    mEndClickTime = System.currentTimeMillis();
                    mHandler.sendEmptyMessageDelayed(1,5000);
                    setAllShowOptions();
                }
            }
        });
        // traffic
        dataBinding.amapLukuangIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEndClickTime = System.currentTimeMillis();
                mHandler.sendEmptyMessageDelayed(1,5000);
                if (dataBinding.naviView.isTrafficLine()) { // traffic is show
                    dataBinding.naviView.setTrafficLine(false);
                    dataBinding.amapLukuangIv.setChecked(false);
                } else {
                    dataBinding.naviView.setTrafficLine(true);
                    dataBinding.amapLukuangIv.setChecked(true);
                }
            }
        });
        // zoom click
        dataBinding.rlZoomAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEndClickTime = System.currentTimeMillis();
                mHandler.sendEmptyMessageDelayed(1,5000);
                // Reduce relative to maps
                dataBinding.naviView.zoomIn();
            }
        });
        dataBinding.rlZoomReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEndClickTime = System.currentTimeMillis();
                mHandler.sendEmptyMessageDelayed(1,5000);
                // add relative to maps
                dataBinding.naviView.zoomOut();
            }
        });
        // setting
        dataBinding.amapSettingIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEndClickTime = System.currentTimeMillis();
                mHandler.sendEmptyMessageDelayed(2,10000);
                // init show
                initDialogData();
                // show bottom dialog
                setShowBottomView();
            }
        });
        // exit
        dataBinding.amapExitIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEndClickTime = System.currentTimeMillis();
                mHandler.sendEmptyMessageDelayed(3,10000);
                // show anim
                setShowHideExitInfo();
            }
        });

        // myLoc
        dataBinding.amapMyIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContinueGuideOptions();
//                mEndClickTime = System.currentTimeMillis();
//                mHandler.sendEmptyMessageDelayed(1,5000);
//                dataBinding.naviView.getMap().moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(Constants.mLatitude, Constants.mLongitude)));
            }
        });

        // 车辆和人员切换
        dataBinding.amapTypeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEndClickTime = System.currentTimeMillis();
                mHandler.sendEmptyMessageDelayed(1,5000);
                mIsShowCarInfo = !mIsShowCarInfo;
                clearAllMarker();
                updateType();
                mHandler.sendEmptyMessage(HANDLER_UPDATE_MEMBER_INFO);
            }
        });

        // refresh
        dataBinding.amapRefreshIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRefreshOption();
            }
        });
        // cancel
        dataBinding.amapExitGuideCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsExitShow){
                    setShowHideInfoExit();
                }
            }
        });
        // exit guide
        dataBinding.amapExitGuideTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedNavigationGuideActivity.this.finish();
            }
        });

        // dialog other click
        dataBinding.rlDialogAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // judging is show
                if (mIsBottomShow){
                    // hide dialog view
                    setHideBottomView();
                    // continue
                    setContinueGuideOptions();
                }
            }
        });
        // dialog click
        dataBinding.llDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEndClickTime = System.currentTimeMillis();
                mHandler.sendEmptyMessageDelayed(2,10000);
            }
        });

        // dialog content——guide voice
        dataBinding.rgDialogVoiceSetting.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (!mIsBottomShow){
                    return;
                }
                mEndClickTime = System.currentTimeMillis();
                mHandler.sendEmptyMessageDelayed(2,10000);
                switch (i){
                    case R.id.rb_dialog_voice_detail: // detail
                        if (mDialogGuideVoicePlay != 0){
                            mDialogGuideVoicePlay = 0;
                        }
                        break;
                    case R.id.rb_dialog_voice_intro: // intro
                        if (mDialogGuideVoicePlay != 1){
                            mDialogGuideVoicePlay = 1;
                        }
                        break;
                    case R.id.rb_dialog_voice_nothing: // noting
                        if (mDialogGuideVoicePlay != 2){
                            mDialogGuideVoicePlay = 2;
                        }
                        break;
                    default:
                        break;
                }
                SPUtils.putInt(SPUtils.GUIDE_VOICE_MODE,mDialogGuideVoicePlay);
                setGuideVoiceMode();
            }
        });
        // dialog content——guide show
        dataBinding.rgDialogShowSetting.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (!mIsBottomShow){
                    return;
                }
                mEndClickTime = System.currentTimeMillis();
                mHandler.sendEmptyMessageDelayed(2,10000);
                switch (i){
                    case R.id.rb_dialog_car_to_north: // car to north
                        if (mDialogGuideShow != 0){
                            mDialogGuideShow = 0;
                        }
                        break;
                    case R.id.rb_dialog_map_to_north: // map to north
                        if (mDialogGuideShow != 1){
                            mDialogGuideShow = 1;
                        }
                        break;
                    case R.id.rb_dialog_3d: // 3D
                        if (mDialogGuideShow != 2){
                            mDialogGuideShow = 2;
                        }
                        break;
                    default:
                        break;
                }
                SPUtils.putInt(SPUtils.GUIDE_SHOW_MODE,mDialogGuideShow);
                setGuideShowMode();
            }
        });
        // dialog content——day night mode
        dataBinding.rgDialogWhiteBlackSetting.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (!mIsBottomShow){
                    return;
                }
                mEndClickTime = System.currentTimeMillis();
                mHandler.sendEmptyMessageDelayed(2,10000);
                switch (i){
                    case R.id.rb_dialog_auto: // auto
                        if (mDialogDayNight != 0){
                            mDialogDayNight = 0;
                        }
                        break;
                    case R.id.rb_dialog_day: // day
                        if (mDialogDayNight != 1){
                            mDialogDayNight = 1;
                        }
                        break;
                    case R.id.rb_dialog_night: // night
                        if (mDialogDayNight != 2){
                            mDialogDayNight = 2;
                        }
                        break;
                    default:
                        break;
                }
                SPUtils.putInt(SPUtils.GUIDE_DAY_NIGHT_MODE,mDialogDayNight);
                setDayNightMode();
            }
        });
        // dialog content——Route preference——躲避拥堵
        dataBinding.rbDialogDbyd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mIsBottomShow){
                    return;
                }
                if (mDialogDbyd == 1){
                    mDialogDbyd = 0;
                    dataBinding.rbDialogDbyd.setChecked(false);
                } else {
                    mDialogDbyd = 1;
                    dataBinding.rbDialogDbyd.setChecked(true);
                }
                SPUtils.putInt(SPUtils.GUIDE_DBYD,mDialogDbyd);
                setRefreshOption();
            }
        });
        // dialog content——Route preference——躲避收费
        dataBinding.rbDialogDbsf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mIsBottomShow){
                    return;
                }
                if (mDialogDbsf == 1){
                    mDialogDbsf = 0;
                    dataBinding.rbDialogDbsf.setChecked(false);
                } else {
                    if (mDialogGsyx == 1){
                        mDialogGsyx = 0;
                        dataBinding.rbDialogGsyx.setChecked(false);
                    }
                    mDialogDbsf = 1;
                    dataBinding.rbDialogDbsf.setChecked(true);
                }
                SPUtils.putInt(SPUtils.GUIDE_DBSF,mDialogDbsf);
                SPUtils.putInt(SPUtils.GUIDE_GSYX,mDialogGsyx);
                setRefreshOption();
            }
        });
        // dialog content——Route preference——躲避高速
        dataBinding.rbDialogDbgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mIsBottomShow){
                    return;
                }
                if (mDialogDbgs == 1){
                    mDialogDbgs = 0;
                    dataBinding.rbDialogDbgs.setChecked(false);
                } else {
                    if (mDialogGsyx == 1){
                        mDialogGsyx = 0;
                        dataBinding.rbDialogGsyx.setChecked(false);
                    }
                    mDialogDbgs = 1;
                    dataBinding.rbDialogDbgs.setChecked(true);
                }
                SPUtils.putInt(SPUtils.GUIDE_DBGS,mDialogDbgs);
                SPUtils.putInt(SPUtils.GUIDE_GSYX,mDialogGsyx);
                setRefreshOption();
            }
        });
        // dialog content——Route preference——躲避高速
        dataBinding.rbDialogGsyx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mIsBottomShow){
                    return;
                }
                if (mDialogGsyx == 1){
                    mDialogGsyx = 0;
                    dataBinding.rbDialogGsyx.setChecked(false);
                } else {
                    if (mDialogDbgs == 1){
                        mDialogDbgs = 0;
                        dataBinding.rbDialogDbgs.setChecked(false);
                    }
                    if (mDialogDbsf == 1){
                        mDialogDbsf = 0;
                        dataBinding.rbDialogDbsf.setChecked(false);
                    }
                    mDialogGsyx = 1;
                    dataBinding.rbDialogGsyx.setChecked(true);
                }
                SPUtils.putInt(SPUtils.GUIDE_DBGS,mDialogDbgs);
                SPUtils.putInt(SPUtils.GUIDE_GSYX,mDialogGsyx);
                SPUtils.putInt(SPUtils.GUIDE_DBSF,mDialogDbsf);
                setRefreshOption();
            }
        });
        // min click
        dataBinding.amapMinIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMinSetting();
            }
        });
    }

    private void updateType(){
        if (mIsShowCarInfo){
            if (mIsNight) { // 晚上
                dataBinding.amapTypeIv.setImageResource(R.drawable.guide_person_icon_black);
            } else { // 白天
                dataBinding.amapTypeIv.setImageResource(R.drawable.guide_person_icon_white);
            }
        } else {
            if (mIsNight) { // 晚上
                dataBinding.amapTypeIv.setImageResource(R.drawable.guide_car_icon_black);
            } else { // 白天
                dataBinding.amapTypeIv.setImageResource(R.drawable.guide_car_icon_white);
            }
        }
    }

    /**
     * set min
     */
    private void setMinSetting(){
        if (FloatPermissionManager.getInstance().applyFloatWindow(SharedNavigationGuideActivity.this)){
            // 权限成功
            TourApplication.playClickVoiceMIN();
            moveTaskToBack(true);
            // start system window
            View view = LayoutInflater.from(this).inflate(R.layout.float_guide_window_layout,null);
            FloatActionController.getInstance().showNavigationView(view);

            if ((mBitmap != null || mIconTpye != 0) && null != mDistance) {
                FloatActionController.getInstance().updateNavigationView(mBitmap, mIconTpye, mDistance);
            }
        } else {
            ToastUtils.showText(this,"悬浮框权限失败，请稍后再试");
            return;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        FloatActionController.getInstance().hideNavigationView();//not show float view
    }

    /**
     * set voice type
     */
    private void setGuideVoiceMode(){
        if (mDialogGuideVoicePlay == 0){
            // detail system default
            startSpeaking();
            mAMapNavi.setBroadcastMode(BroadcastMode.DETAIL);
        } else if (mDialogGuideVoicePlay == 1){
            // intro
            startSpeaking();
            mAMapNavi.setBroadcastMode(BroadcastMode.CONCISE);
        } else if (mDialogGuideVoicePlay == 2){
            // nothing
            stopSpeaking();
        }
    }

    /**
     * stop voice
     */
    public void stopSpeaking(){
        mAMapNavi.stopSpeak();
    }
    /**
     * start voice
     */
    public void startSpeaking(){
        mAMapNavi.startSpeak();
    }

    /**
     * set day night mode
     */
    private void setDayNightMode(){
        AMapNaviViewOptions options = dataBinding.naviView.getViewOptions();
        options.setLayoutVisible(false);
        if (mDialogDayNight > 1) {
            options.setNaviNight(true); // night mode
        } else {
            options.setNaviNight(false); // auto and day
        }
        if (mDialogGuideShow == 2){ // Map mode is 2D mode when inclination angle is set and the range of value is [0-60] inclination angle is 0.
            options.setTilt(1);
        } else { // 2D
            options.setTilt(0);
        }
        dataBinding.naviView.setViewOptions(options);

        // update view
        if (mDialogDayNight > 1){
            // night
            mIsNight = true;
        } else {
            mIsNight = false;
        }
        setDayNightModeUpdateView();
        clearAllMarker();
        mHandler.sendEmptyMessage(HANDLER_UPDATE_MEMBER_INFO);
    }

    /**
     * setting inclination angle
     */
    private void setGuideShowMode(){
        // default setting
        dataBinding.naviView.setNaviMode(AMapNaviView.CAR_UP_MODE);
        if (mDialogGuideShow == 0) { // car to north
            dataBinding.naviView.setNaviMode(AMapNaviView.CAR_UP_MODE);
        } else if (mDialogGuideShow == 1){ // map to north
            dataBinding.naviView.setNaviMode(AMapNaviView.NORTH_UP_MODE);
        } else if (mDialogGuideShow == 2){ // 3D
            setDayNightMode();
        }
    }

    /**
     * show all show mode
     */
    private void setAllShowOptions(){
        dataBinding.naviView.displayOverview();
    }

    /**
     * continue mode
     */
    private void setContinueGuideOptions(){
        dataBinding.naviView.recoverLockMode();
        updateMapView(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataBinding.naviView.onResume();
        mIsActivity = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataBinding.naviView.onPause();
        mIsActivity = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        dataBinding.naviView.onDestroy();
        //since 1.6.0 no more naviview destroy
        mAMapNavi.stopNavi();
        mAMapNavi.destroy();
        mAmap.clear();

        if (dataBinding.naviView != null) {
            dataBinding.naviView.onDestroy();
        }
        mDestroyActivity = true;
        // data null
        setDataToEmpty();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getOrderType(NavigationTypeEvent event) {
        if (event.getType() == 0){ // stop navigation
            FloatActionController.getInstance().hideNavigationView();
            finish();
        } else if (event.getType() == 2){ // 关闭导航声音
            stopSpeaking();
        } else if (event.getType() == 3){ // 开启导航声音
            startSpeaking();
        }
    }

    /**
     * set null
     */
    private void setDataToEmpty (){
        mDialogDbyd = 0; // 躲避拥堵
        mDialogDbsf = 0; // 躲避收费
        mDialogDbgs = 0; // 躲避高速
        mDialogGsyx = 1; // 高速优先
        mDialogGuideVoicePlay = 1; // 导航语音播报 0 详细播报 1 简介播报 2 静音
        mDialogGuideShow = 0; // 导航视角 0 车头朝北 1 地图朝北 2 3D
        mDialogDayNight = 1; // 日夜模式 0 自动 1 白天 2 夜间
    }

    /**
     * refresh option
     */
    private void setRefreshOption(){
        if (isDriving) {
            //init success
            /**
             * function: int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute); options:
             *
             * @congestion 躲避拥堵
             * @avoidhightspeed 不走高速
             * @cost 避免收费
             * @hightspeed 高速优先
             * @multipleroute 多路径
             *
             *  说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
             *  注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true
             */
            int strategy = 0;
            try {
                //再次强调，最后一个参数为true时代表多路径，否则代表单路径
//                strategy = mAMapNavi.strategyConvert(
//                        mDialogDbyd == 1 ? true : false, // 躲避拥堵
//                        mDialogDbgs == 1 ? true : false, // 躲避高速
//                        mDialogDbsf == 1 ? true : false, // 躲避收费
//                        mDialogGsyx == 1 ? true : false, // 高速优先
//                        true);
                strategy = mAMapNavi.strategyConvert(
                        false, // 躲避拥堵
                        false, // 躲避高速
                        false, // 躲避收费
                        false, // 高速优先
                        true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            sList.add(mStartLatlng);
            eList.add(mEndLatlng);
            mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);
        } else {
            mAMapNavi.calculateWalkRoute(mStartLatlng,mEndLatlng);
        }
    }

    @Override
    public void onInitNaviFailure() {
//        Toast.makeText(this, "init navi Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInitNaviSuccess() {
        setRefreshOption();
    }

    @Override
    public void onStartNavi(int type) {
        //start guide
    }

    @Override
    public void onTrafficStatusUpdate() {
        //
    }

    @Override
    public void onLocationChange(AMapNaviLocation location) {
        //current loc change
        dataBinding.amapCarInfoTv.setText((int)location.getSpeed()+""); // set current car speed
    }

    @Override
    public void onGetNavigationText(int type, String text) {
        //voice type and content
    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onEndEmulatorNavi() {
        //end guide emulator
    }

    @Override
    public void onArriveDestination() {
        //arrival destination
    }

    @Override
    public void onCalculateRouteFailure(int errorInfo) {
        //line cal failed
        Log.e("dm", "--------------------------------------------");
        Log.i("dm", "路线计算失败：错误码=" + errorInfo + ",Error Message= " + ErrorInfo.getError(errorInfo));
        Log.i("dm", "错误码详细链接见：http://lbs.amap.com/api/android-navi-sdk/guide/tools/errorcode/");
        Log.e("dm", "--------------------------------------------");
//        Toast.makeText(this, "errorInfo：" + errorInfo + ",Message：" + ErrorInfo.getError(errorInfo), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReCalculateRouteForYaw() {
        //偏航后重新计算路线回调
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        //拥堵后重新计算路线回调
    }

    @Override
    public void onArrivedWayPoint(int wayID) {
        //到达途径点
    }

    @Override
    public void onGpsOpenStatus(boolean enabled) {
        //GPS开关状态回调
    }

    @Override
    public void onNaviSetting() {
        //底部导航设置点击回调
    }

    @Override
    public void onNaviMapMode(int naviMode) {
        //导航态车头模式，0:车头朝上状态；1:正北朝上模式。
    }

    @Override
    public void onNaviCancel() {
        finish();
    }


    @Override
    public void onNaviTurnClick() {
        //转弯view的点击回调
    }

    @Override
    public void onNextRoadClick() {
        //下一个道路View点击回调
    }


    @Override
    public void onScanViewButtonClick() {
        //全览按钮点击回调
    }

    @Deprecated
    @Override
    public void onNaviInfoUpdated(AMapNaviInfo naviInfo) {
        //过时
    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapCameraInfos) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] amapServiceAreaInfos) {

    }

    private Bitmap mBitmap;
    private int mIconTpye;
    private String mDistance;

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {
        //导航过程中的信息更新，请看NaviInfo的具体说明
        /**
         * 更新路口转向图标 TODO  使用高德地图系统提供的view类
         */
//        if(naviInfo.getIconBitmap() != null){
//            dataBinding.amapToIv.setImageBitmap(naviInfo.getIconBitmap());
//        }else{
//            if (naviInfo.getIconType() == 2){
//                dataBinding.amapToIv.setImageResource(R.drawable.amap_to_left_icon);
//            } else if (naviInfo.getIconType() == 3){
//                dataBinding.amapToIv.setImageResource(R.drawable.amap_to_right_icon);
//            }else if (naviInfo.getIconType() == 4){
//                dataBinding.amapToIv.setImageResource(R.drawable.amap_to_left_front_icon);
//            }else if (naviInfo.getIconType() == 5){
//                dataBinding.amapToIv.setImageResource(R.drawable.amap_to_right_front_icon);
//            }else if (naviInfo.getIconType() == 6){
//                dataBinding.amapToIv.setImageResource(R.drawable.amap_to_back_icon);
//            }else if (naviInfo.getIconType() == 8){
//                dataBinding.amapToIv.setImageResource(R.drawable.amap_to_back_icon);
//            }else if (naviInfo.getIconType() == 9){
//                dataBinding.amapToIv.setImageResource(R.drawable.amap_to_straight_icon);
//            } else {
//                dataBinding.amapToIv.setImageResource(R.drawable.amap_to_straight_icon);
//            }
//        }

//        if (naviInfo.getIconBitmap() != null){
//            mBinder.updateView(naviInfo.getIconBitmap());
//        } else {
//            mBinder.updateView(naviInfo.getIconType());
//        }

        mBitmap = naviInfo.getIconBitmap();
        mIconTpye = naviInfo.getIconType();
        mDistance = NaviUtil.formatKM(naviInfo.getCurStepRetainDistance());
        FloatActionController.getInstance().updateNavigationView(mBitmap,mIconTpye,mDistance);
        /**
         * update view
         */
        dataBinding.amapRoudeTv.setText(naviInfo.getNextRoadName()); // next road
        dataBinding.amapDistanceTv.setText(NaviUtil.formatKM_no(naviInfo.getCurStepRetainDistance())); // distance
        dataBinding.amapMeterTv.setText(naviInfo.getCurStepRetainDistance() > 1000 ? "公里" : "米");

        dataBinding.amapCarInfoTv.setText(naviInfo.getCurrentSpeed()+""); // set current speed

        dataBinding.amapShengyuDistanceTv.setText(NaviUtil.formatKM_no(naviInfo.getPathRetainDistance()));
        dataBinding.tvBottomT.setText(naviInfo.getPathRetainDistance() > 1000 ? "公里" : "米");
        mPathRetainTime = naviInfo.getPathRetainTime();
        TextViewUtil.setTextViewSize(dataBinding.amapShengyuTimeTv,SharedNavigationGuideActivity.this,AMapUtil.getFriendlyTime(mPathRetainTime),mIsNight);
        String time = AMapUtil.convertToTime((long)System.currentTimeMillis()+((long)naviInfo.getPathRetainTime()*1000L));
        String time1 = time.split(" ")[1];
        dataBinding.amapEstimateArrivalTimeTv.setText("预计"+time1.substring(0,time1.length()-3)+"到达");
    }

    private int mPathRetainTime = 0;

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {
        //已过时
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
        //已过时
    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
        //显示转弯回调
    }

    @Override
    public void hideCross() {
        //隐藏转弯回调
    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] laneInfos, byte[] laneBackgroundInfo, byte[] laneRecommendedInfo) {
        //显示车道信息

    }

    @Override
    public void hideLaneInfo() {
        //隐藏车道信息
    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
//        mAMapNavi.startNavi(NaviType.EMULATOR); // TODO 更改为GPS信息
        for (int i = 0; i < ints.length;i++){
            if (ints[i] == routeId){
                mAMapNavi.startNavi(NaviType.GPS);
                //多路径算路成功回调
            }
        }
    }

    @Override
    public void notifyParallelRoad(int i) {
        if (i == 0) {
//            Toast.makeText(this, "当前在主辅路过渡", Toast.LENGTH_SHORT).show();
            Log.d("wlx", "当前在主辅路过渡");
            return;
        }
        if (i == 1) {
//            Toast.makeText(this, "当前在主路", Toast.LENGTH_SHORT).show();

            Log.d("wlx", "当前在主路");
            return;
        }
        if (i == 2) {
//            Toast.makeText(this, "当前在辅路", Toast.LENGTH_SHORT).show();

            Log.d("wlx", "当前在辅路");
        }
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {
        //更新交通设施信息
    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
        //更新巡航模式的统计信息
    }


    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {
        //更新巡航模式的拥堵信息
    }

    @Override
    public void onPlayRing(int i) {

    }


    @Override
    public void onLockMap(boolean isLock) {
        //锁地图状态发生变化时回调
    }

    @Override
    public void onNaviViewLoaded() {
        Log.d("wlx", "导航页面加载成功");
        Log.d("wlx", "请不要使用AMapNaviView.getMap().setOnMapLoadedListener();会overwrite导航SDK内部画线逻辑");
    }

    @Override
    public void onMapTypeChanged(int i) {

    }

    @Override
    public void onNaviViewShowMode(int i) {

    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }


    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {

    }


    private boolean mIsNight = false;
    /**
     * update page setting day and night mode
     */
    private void setDayNightModeUpdateView(){
        // update dialog
        if (mIsNight) { // night
            StatusBarUtil.changeStatusBarColor(SharedNavigationGuideActivity.this,R.color.black_guide_status_bg);
            StatusBarUtil.setStatusBarLightMode(SharedNavigationGuideActivity.this,false);
            dataBinding.llDialog.setBackground(getResources().getDrawable(R.drawable.dialog_black_bg));
            // title update txt color
            dataBinding.tvDialogLineContent1.setTextColor(getResources().getColor(R.color.color_C8CBD5));// Route preference setting
            dataBinding.tvDialogVoiceContent1.setTextColor(getResources().getColor(R.color.color_C8CBD5));// guide voice
            dataBinding.tvDialogGuideContent1.setTextColor(getResources().getColor(R.color.color_C8CBD5));// guide show
            dataBinding.tvDialogDayNightModeContent1.setTextColor(getResources().getColor(R.color.color_C8CBD5));// day night
            // radioButton update bg color
            dataBinding.rbDialogVoiceDetail.setTextColor(getResources().getColor(R.color.amap_guide_dialog_black_btn_selector)); // detail
            dataBinding.rbDialogVoiceIntro.setTextColor(getResources().getColor(R.color.amap_guide_dialog_black_btn_selector)); // intro
            dataBinding.rbDialogVoiceNothing.setTextColor(getResources().getColor(R.color.amap_guide_dialog_black_btn_selector)); // nothing

            dataBinding.rbDialogCarToNorth.setTextColor(getResources().getColor(R.color.amap_guide_dialog_black_btn_selector));// car to north
            dataBinding.rbDialogMapToNorth.setTextColor(getResources().getColor(R.color.amap_guide_dialog_black_btn_selector));// map to north
            dataBinding.rbDialog3d.setTextColor(getResources().getColor(R.color.amap_guide_dialog_black_btn_selector)); // 3D

            dataBinding.rbDialogAuto.setTextColor(getResources().getColor(R.color.amap_guide_dialog_black_btn_selector)); // auto
            dataBinding.rbDialogDay.setTextColor(getResources().getColor(R.color.amap_guide_dialog_black_btn_selector)); // day
            dataBinding.rbDialogNight.setTextColor(getResources().getColor(R.color.amap_guide_dialog_black_btn_selector)); // night

            dataBinding.tvDialogGuideContentBottomLine1.setBackgroundColor(getResources().getColor(R.color.color_131519));
            dataBinding.tvDialogLineContentBottomLine1.setBackgroundColor(getResources().getColor(R.color.color_131519));
            dataBinding.tvDialogVoiceContentBottomLine1.setBackgroundColor(getResources().getColor(R.color.color_131519));
        } else { // day
            StatusBarUtil.changeStatusBarColor(SharedNavigationGuideActivity.this,R.color.white_guide_status_bg);
            StatusBarUtil.setStatusBarLightMode(SharedNavigationGuideActivity.this,true);

            dataBinding.llDialog.setBackground(getResources().getDrawable(R.drawable.dialog_white_bg));
            // title update txt color
            dataBinding.tvDialogLineContent1.setTextColor(getResources().getColor(R.color.color_000000));// Route preference setting
            dataBinding.tvDialogVoiceContent1.setTextColor(getResources().getColor(R.color.color_000000));// guide voice
            dataBinding.tvDialogGuideContent1.setTextColor(getResources().getColor(R.color.color_000000));// guide show
            dataBinding.tvDialogDayNightModeContent1.setTextColor(getResources().getColor(R.color.color_000000));// day night mode
            // radioButton update txt color
            dataBinding.rbDialogVoiceDetail.setTextColor(getResources().getColor(R.color.amap_guide_dialog_white_btn_selector)); // detail
            dataBinding.rbDialogVoiceIntro.setTextColor(getResources().getColor(R.color.amap_guide_dialog_white_btn_selector)); // intro
            dataBinding.rbDialogVoiceNothing.setTextColor(getResources().getColor(R.color.amap_guide_dialog_white_btn_selector)); // nothing

            dataBinding.rbDialogCarToNorth.setTextColor(getResources().getColor(R.color.amap_guide_dialog_white_btn_selector));// car to north
            dataBinding.rbDialogMapToNorth.setTextColor(getResources().getColor(R.color.amap_guide_dialog_white_btn_selector));// map to north
            dataBinding.rbDialog3d.setTextColor(getResources().getColor(R.color.amap_guide_dialog_white_btn_selector)); // 3D

            dataBinding.rbDialogAuto.setTextColor(getResources().getColor(R.color.amap_guide_dialog_white_btn_selector)); // auto
            dataBinding.rbDialogDay.setTextColor(getResources().getColor(R.color.amap_guide_dialog_white_btn_selector)); // day
            dataBinding.rbDialogNight.setTextColor(getResources().getColor(R.color.amap_guide_dialog_white_btn_selector)); // night

            dataBinding.tvDialogGuideContentBottomLine1.setBackgroundColor(getResources().getColor(R.color.color_e5e5e5));
            dataBinding.tvDialogLineContentBottomLine1.setBackgroundColor(getResources().getColor(R.color.color_e5e5e5));
            dataBinding.tvDialogVoiceContentBottomLine1.setBackgroundColor(getResources().getColor(R.color.color_e5e5e5));
        }
        //update page icon
        if (mIsNight){
            // car speed
            dataBinding.amapCarInfoIv.setImageResource(R.drawable.amap_guide_carinfo_black_icon);
            //traffic
            dataBinding.amapLukuangIv.setButtonDrawable(R.drawable.amap_lukuang_black_selector);
            // min
            dataBinding.amapMinIv.setImageResource(R.drawable.amap_guide_min_black_icon);
            // refresh
            dataBinding.amapRefreshIv.setImageResource(R.drawable.amap_guide_refresh_black_bg);
            // zoom
            dataBinding.llZoomContent.setBackgroundResource(R.drawable.amap_guide_zoom_black_bg);
            dataBinding.ivZoomAdd.setImageResource(R.drawable.amap_guide_zoom_add_black_bg);
            dataBinding.ivZoomReduce.setImageResource(R.drawable.amap_guide_zoom_reduce_black_bg);
            // show top bg
            dataBinding.rlTopContent.setBackgroundResource(R.drawable.amap_guide_top_black_bg);
            //all show
            dataBinding.amapAllShowIv.setImageResource(R.drawable.amap_guide_allshow_black_icon);
            // voice
            dataBinding.amapVoiceIv.setImageResource(R.drawable.amap_guide_voice_black_bg);
            // bottom bg
            dataBinding.rlBottomContent.setBackgroundResource(R.drawable.amap_guide_bottom_black_bg);
            // bottom exit
            dataBinding.amapExitIv.setImageResource(R.drawable.amap_guide_exit_black_icon);
            // bottom setting
            dataBinding.amapSettingIv.setImageResource(R.drawable.amap_guide_setting_black_icon);
            // bottom txt color
            // cancel
            dataBinding.amapExitGuideCancelTv.setTextColor(getResources().getColor(R.color.color_C8CBD5));
            // 预计09:56到达
            dataBinding.amapEstimateArrivalTimeTv.setTextColor(getResources().getColor(R.color.color_939CA4));
            // 剩余
            dataBinding.tvShengyu1.setTextColor(getResources().getColor(R.color.color_D9DBDE));
            // 1.7
            dataBinding.amapShengyuDistanceTv.setTextColor(getResources().getColor(R.color.color_D9DBDE));
            // meter
            dataBinding.tvBottomT.setTextColor(getResources().getColor(R.color.color_D9DBDE));
            // 10
            dataBinding.amapShengyuTimeTv.setTextColor(getResources().getColor(R.color.color_D9DBDE));
            TextViewUtil.setTextViewSize(dataBinding.amapShengyuTimeTv,SharedNavigationGuideActivity.this,AMapUtil.getFriendlyTime(mPathRetainTime),mIsNight);
            // minute
            dataBinding.tvMinute1.setTextColor(getResources().getColor(R.color.color_D9DBDE));
            // bottom line color
            dataBinding.amapExitRightLineTv.setBackgroundColor(getResources().getColor(R.color.color_333333));
            dataBinding.amapSettingLeftLineTv.setBackgroundColor(getResources().getColor(R.color.color_333333));
            dataBinding.amapExitCancelCenterLineTv.setBackgroundColor(getResources().getColor(R.color.color_333333));
            // 我的位置
            dataBinding.amapMyIv.setImageResource(R.drawable.amap_guide_my_white_icon_black);
            // 更新类型
            updateType();
        } else {
            // car speed
            dataBinding.amapCarInfoIv.setImageResource(R.drawable.amap_guide_carinfo_white_icon);
            //traffic
            dataBinding.amapLukuangIv.setButtonDrawable(R.drawable.amap_lukuang_white_selector);
            // min
            dataBinding.amapMinIv.setImageResource(R.drawable.amap_guide_min_white_icon);
            // refresh
            dataBinding.amapRefreshIv.setImageResource(R.drawable.amap_guide_refresh_white_bg);
            // zoom
            dataBinding.llZoomContent.setBackgroundResource(R.drawable.amap_guide_zoom_white_bg);
            dataBinding.ivZoomAdd.setImageResource(R.drawable.amap_guide_zoom_add_white_bg);
            dataBinding.ivZoomReduce.setImageResource(R.drawable.amap_guide_zoom_reduce_white_bg);
            // top bg
            dataBinding.rlTopContent.setBackgroundResource(R.drawable.amap_guide_top_white_bg);
            //all show
            dataBinding.amapAllShowIv.setImageResource(R.drawable.amap_guide_allshow_white_icon);
            // voice
            dataBinding.amapVoiceIv.setImageResource(R.drawable.amap_guide_voice_white_bg);
            // bottom bg
            dataBinding.rlBottomContent.setBackgroundResource(R.drawable.amap_guide_bottom_white_bg);
            // bottom exit
            dataBinding.amapExitIv.setImageResource(R.drawable.amap_guide_exit_white_icon);
            // bottom setting
            dataBinding.amapSettingIv.setImageResource(R.drawable.amap_guide_setting_white_icon);
            // bottom txt color
            // cancel
            dataBinding.amapExitGuideCancelTv.setTextColor(getResources().getColor(R.color.color_333333));
            // 预计09:56到达
            dataBinding.amapEstimateArrivalTimeTv.setTextColor(getResources().getColor(R.color.color_666666));
            // 剩余
            dataBinding.tvShengyu1.setTextColor(getResources().getColor(R.color.color_333333));
            // 1.7
            dataBinding.amapShengyuDistanceTv.setTextColor(getResources().getColor(R.color.color_333333));
            // meter
            dataBinding.tvBottomT.setTextColor(getResources().getColor(R.color.color_333333));
            // 10
            dataBinding.amapShengyuTimeTv.setTextColor(getResources().getColor(R.color.color_333333));
            TextViewUtil.setTextViewSize(dataBinding.amapShengyuTimeTv,SharedNavigationGuideActivity.this,AMapUtil.getFriendlyTime(mPathRetainTime),mIsNight);
            // minute
            dataBinding.tvMinute1.setTextColor(getResources().getColor(R.color.color_333333));
            // bottom line color
            dataBinding.amapExitRightLineTv.setBackgroundColor(getResources().getColor(R.color.color_e5e5e5));
            dataBinding.amapSettingLeftLineTv.setBackgroundColor(getResources().getColor(R.color.color_e5e5e5));
            dataBinding.amapExitCancelCenterLineTv.setBackgroundColor(getResources().getColor(R.color.color_e5e5e5));
            // 我的位置
            dataBinding.amapMyIv.setImageResource(R.drawable.amap_guide_my_white_icon);
            // 更新类型
            updateType();
        }
    }

    private void updateMapView(boolean isLockCar){
        if (isLockCar){ // current is lock
            dataBinding.amapLukuangIv.setVisibility(View.GONE); // traffic
//            dataBinding.amapMinIv.setVisibility(View.GONE); // min
            dataBinding.amapRefreshIv.setVisibility(View.GONE); // refresh
            dataBinding.amapAllShowIv.setVisibility(View.VISIBLE); // all show
            dataBinding.rlBottomTxt.setVisibility(View.VISIBLE); // 剩余1.7公里 10分钟 预计上午9.59到达
            dataBinding.amapContinueGuideTv.setVisibility(View.GONE);//continue
            dataBinding.llZoomContent.setVisibility(View.GONE); // zoom
        } else { // current not lock
            dataBinding.amapLukuangIv.setVisibility(View.VISIBLE); // traffic
//            dataBinding.amapMinIv.setVisibility(View.VISIBLE); // min
            if (isDriving) {
                dataBinding.amapRefreshIv.setVisibility(View.VISIBLE); // refresh
            } else {
                dataBinding.amapRefreshIv.setVisibility(View.GONE); // refresh
            }
            dataBinding.amapAllShowIv.setVisibility(View.GONE); // all show
            dataBinding.rlBottomTxt.setVisibility(View.GONE); // 剩余1.7公里 10分钟 预计上午9.59到达
            dataBinding.amapContinueGuideTv.setVisibility(View.VISIBLE);//continue
            dataBinding.llZoomContent.setVisibility(View.VISIBLE); // zoom
        }
    }

    private void initDialogData(){
        dataBinding.rbDialogDbyd.setChecked(mDialogDbyd == 1 ? true : false); // 躲避拥堵
        dataBinding.rbDialogDbsf.setChecked(mDialogDbsf == 1 ? true : false); // 躲避收费
        dataBinding.rbDialogDbgs.setChecked(mDialogDbgs == 1 ? true : false); // 躲避高速
        dataBinding.rbDialogGsyx.setChecked(mDialogGsyx == 1 ? true : false); // 高速优先

        // radioButton
        dataBinding.rbDialogVoiceDetail.setChecked(mDialogGuideVoicePlay == 0 ? true : false); // detail
        dataBinding.rbDialogVoiceIntro.setChecked(mDialogGuideVoicePlay == 1 ? true : false); // intro
        dataBinding.rbDialogVoiceNothing.setChecked(mDialogGuideVoicePlay == 2 ? true : false); // nothing

        dataBinding.rbDialogCarToNorth.setChecked(mDialogGuideShow == 0 ? true : false);// car to north
        dataBinding.rbDialogMapToNorth.setChecked(mDialogGuideShow == 1 ? true : false); // map to north
        dataBinding.rbDialog3d.setChecked(mDialogGuideShow == 2 ? true : false);  // 3D

        dataBinding.rbDialogAuto.setChecked(mDialogDayNight == 0 ? true : false);  // auto
        dataBinding.rbDialogDay.setChecked(mDialogDayNight == 1 ? true : false); // day
        dataBinding.rbDialogNight.setChecked(mDialogDayNight == 2 ? true : false); // night
    }

    private boolean mIsBottomShow = false;

    /**
     * show bottom dialog view
     */
    private void setShowBottomView() {
        android.view.animation.Animation animBottomOut = AnimationUtils.loadAnimation(SharedNavigationGuideActivity.this, R.anim.map_list_bottom_in);
        animBottomOut.setDuration(240);
        dataBinding.rlDialogAll.setVisibility(View.VISIBLE);
        dataBinding.rlDialogAll.startAnimation(animBottomOut);
        mIsBottomShow = true;
    }

    /**
     * hide bottom dialog view
     */
    private void setHideBottomView() {
        android.view.animation.Animation animBottomOut = AnimationUtils.loadAnimation(SharedNavigationGuideActivity.this, R.anim.map_list_bottom_out);
        animBottomOut.setDuration(240);
        dataBinding.rlDialogAll.setVisibility(View.GONE);
        dataBinding.rlDialogAll.startAnimation(animBottomOut);
        mIsBottomShow = false;
    }

    private boolean mIsExitShow = false;

    /**
     * show exit info hide normal guide info
     */
    private void setShowHideExitInfo() {
        android.view.animation.Animation animInFromLeft = AnimationUtils.loadAnimation(SharedNavigationGuideActivity.this, R.anim.in_from_left);
        animInFromLeft.setDuration(500);
        dataBinding.rlExit.setVisibility(View.VISIBLE);
        dataBinding.rlExit.startAnimation(animInFromLeft);

        android.view.animation.Animation animOutToRight = AnimationUtils.loadAnimation(SharedNavigationGuideActivity.this, R.anim.out_to_right);
        animOutToRight.setDuration(500);
        dataBinding.rlNormal.setVisibility(View.GONE);
        dataBinding.rlNormal.startAnimation(animOutToRight);

        mIsExitShow = true;
    }
    /**
     * show normal guide info hide exit info
     */
    private void setShowHideInfoExit() {
        android.view.animation.Animation animInFromRight = AnimationUtils.loadAnimation(SharedNavigationGuideActivity.this, R.anim.in_from_right);
        animInFromRight.setDuration(500);
        dataBinding.rlNormal.setVisibility(View.VISIBLE);
        dataBinding.rlNormal.startAnimation(animInFromRight);

        android.view.animation.Animation animOutToLeft = AnimationUtils.loadAnimation(SharedNavigationGuideActivity.this, R.anim.out_to_left);
        animOutToLeft.setDuration(500);
        dataBinding.rlExit.setVisibility(View.GONE);
        dataBinding.rlExit.startAnimation(animOutToLeft);

        mIsExitShow = false;
    }

    @Override
    public void onBackPressed() {
        if (mIsBottomShow){
            setHideBottomView();
        } else {
            if (mIsExitShow) {
                setShowHideInfoExit();
            } else {
                setShowHideExitInfo();
            }
        }
    }

    private boolean mIsShowCarInfo = false;

    private boolean mIsActivity = false;
    private boolean mDestroyActivity = false;

    private String mUserId = "";
    // 保存所有人员marker
    List<Marker> mAllMarker = new ArrayList<>();
    private int mDistanceMarker = 10000; // 10Km之外
    private long TIME_OUT = 60 * 1000 * 5L;
    public String CAR_TYPE = "car";
    public String PERSON_TYPE = "person";

    private void getUserInfoToDataBase(){
        List<GroupUserInfo> list = TourDatabase.getDefault(mContext).getGroupUserInfoDao().getDataAll(mGroupId);
        List<SharedNavigationMapRightInfo> memberLsit = new ArrayList<>();
        if (list != null){
            if (list.size() > 0){
                int size = list.size();
                for (int i = 0 ; i < size ; i++){
                    GroupUserInfo userInfo = list.get(i);
                    SharedNavigationMapRightInfo info = new SharedNavigationMapRightInfo(mIsShowCarInfo ? 0 : 1);
                    info.setSendId(userInfo.userId);
                    info.setBean(userInfo);
                    if (!TextUtils.isEmpty(userInfo.userId) && !TextUtils.isEmpty(userInfo.groupId)) {
                        if (!mUserId.equals(userInfo.userId)) {
                            memberLsit.add(info);
                        }
                    }
                }
                // 添加到界面
                updateRightListInfo(memberLsit);
            }
        }
    }

    private void updateRightListInfo(List<SharedNavigationMapRightInfo> list){
        // 将数据库的数据放到MMemberInfo中 // 数据更新
        mMemberInfo_All = new ArrayList<>(list);
        // 将没有的maker进行移除掉
        int pos = mAllMarker.size();
        List<Marker> mMarkerList = new ArrayList<>(mAllMarker);
        for (int k = 0; k < mMemberInfo_All.size(); k++) { // 遍历当前所有用户
            for (int j = 0; j < pos; j++) {
                String title = mAllMarker.get(j).getTitle();
                if (title.contains(",.")){ // 表示距离大于10Km 并且离线的用户
                    title = title.substring(0,title.length()-2);
                } else if (title.contains(",")){ // 表示距离大于10Km的情况
                    title = title.substring(0,title.length()-1);
                } else if (title.contains(".")){ // 表示用户离线的情况
                    title = title.substring(0,title.length()-1);
                }
                if (mMemberInfo_All.get(k).getBean().userId.equals(title)) { //表示有上一次的用户的数据
                    // 不会进行移除的操作
                    mMarkerList.remove(mAllMarker.get(j));
                    break;
                }
            }
        }
        // 上面获取的mMarkerList剩余的数据是需要被移除的marker数据
        for (int i = 0; i < mMarkerList.size(); i++) {
            mMarkerList.get(i).remove();
            mAllMarker.remove(mMarkerList.get(i));
        }
        if (mIsActivity) {
            // update adapter
            setShowMarkerIconInfo();
        }
    }

    private boolean mIsAllShow = true;

    List<SharedNavigationMapRightInfo> mMemberInfo_All = new ArrayList<>();

    List<SharedNavigationMapRightInfo> mRightInfo = new ArrayList<>();

    /**
     * update right data
     */
    private void setShowMarkerIconInfo() {
        if (mIsAllShow != mIsShowCarInfo) {
            mIsAllShow = mIsShowCarInfo;
            clearAllMarker();
        }
        if (mIsShowCarInfo) { // show car
            mRightInfo.clear();
            // update right data
            mRightInfo = viewModel.getRightCarInfo(mMemberInfo_All);
            // add car marker
            setOtherAddMaker();
        } else {
            mRightInfo = viewModel.getRightPersonInfo(mMemberInfo_All);
            setOtherAddMaker();
        }
    }

    /**
     * @param isCar 是否是车辆
     * @param latLng 经纬度信息
     * @param radiusmarker 设置显示偏移量
     * @param bitmapurl 用户头像
     * @param type 用户类型 0 群主 1 领队 2 成员
     * @param name 昵称
     * @param carinfo 车辆信息
     * @param userId 用户id
     * @param distance 距离
     * @param time 之前时间
     */
    private void addMyCarAndPersonLocationMarker(boolean isCar, LatLng latLng, int radiusmarker, Bitmap bitmapurl, String type, String name, String carinfo, String userId,float distance,long time) {
        long update = System.currentTimeMillis() - time;
        // 在获取之前写逻辑
        if (mAllMarker != null) {
            if (mAllMarker.size() > 0) {
                for (int i = 0; i < mAllMarker.size(); i++) {
                    String con = userId;
                    String title = mAllMarker.get(i).getTitle();
                    String title1 = "";
                    if (title.contains(",.")) { // 表示距离大于10Km 并且离线的用户
                        title1 = title.substring(0, title.length() - 2);
                    } else if (title.contains(",")) { // 表示距离大于10Km的情况
                        title1 = title.substring(0, title.length() - 1);
                    } else if (title.contains(".")) { // 表示用户离线的情况
                        title1 = title.substring(0, title.length() - 1);
                    } else { // 表示用户在线也没有大于10Km的情况
                        title1 = title;
                    }
                    if (con.equals(title1)) { // 判断是否是当前的marker是否是我需要的marker 是的话进行需要的操作显示
                        // 当前用户是否是大于10Km 并且离线的情况
                        if (distance >= mDistanceMarker && update >= TIME_OUT) {
                            if (!title.contains(",.")) { // 之前就是大于10Km 并且离线的情况
                                mAllMarker.get(i).remove();
                                mAllMarker.remove(mAllMarker.get(i));
                                break;
                            } else { // 不是这样的情况需要移除掉然后在重新添加
                                mAllMarker.get(i).setPosition(latLng);
                                return;
                            }
                        } else if (distance >= mDistanceMarker && update < TIME_OUT) { // 当前用户是距离大于10Km的情况 并且在线的情况下
                            if (!title.contains(",") || title.contains(".")) { // 表示的是之前没有大于10Km的情况 现在表示大于10Km的情况
                                mAllMarker.get(i).remove();
                                mAllMarker.remove(mAllMarker.get(i));
                                break;
                            } else {
                                mAllMarker.get(i).setPosition(latLng);
                                return;
                            }
                        } else if (distance < mDistanceMarker && update >= TIME_OUT) { // 小于10Km 并且离线的情况
                            if (title.contains(",") || !title.contains(".")) {
                                mAllMarker.get(i).remove();
                                mAllMarker.remove(mAllMarker.get(i));
                                break;
                            } else {
                                mAllMarker.get(i).setPosition(latLng);
                                return;
                            }
                        } else if (distance < mDistanceMarker && update < TIME_OUT) { // 在线的情况 并且距离小于10Km
                            if (title.contains(",") || title.contains(".")) {
                                mAllMarker.get(i).remove();
                                mAllMarker.remove(mAllMarker.get(i));
                                break;
                            } else {
                                mAllMarker.get(i).setPosition(latLng);
                                return;
                            }
                        }
                    }
                }
            }
        }
        if (mAmap != null) {
            View view = null;
            if (isCar) { // car
                view = View.inflate(this, R.layout.marker_shared_navigation_car_other_item, null);
            } else { // person
                view = View.inflate(this, R.layout.marker_shared_navigation_person_item, null);
            }
            TextView tvNickName = (TextView) view.findViewById(cn.xmzt.www.R.id.maker_nick_name_tv); // 用户昵称的显示
            RelativeLayout rl_bg = (RelativeLayout) view.findViewById(cn.xmzt.www.R.id.rl_bg); // 用户昵称的显示
            if (mIsNight){ // 晚上
                tvNickName.setTextColor(Color.parseColor("#B7C9E6"));
                rl_bg.setBackgroundResource(R.drawable.marker_black_bg);
            } else { // 白天
                tvNickName.setTextColor(Color.parseColor("#999999"));
                if (isCar){
                    rl_bg.setBackgroundResource(R.drawable.shared_navigation_marker_car_bg);
                } else {
                    rl_bg.setBackgroundResource(R.drawable.shared_navigation_marker_person_bg);
                }
            }
            TextView tvType = (TextView) view.findViewById(cn.xmzt.www.R.id.maker_type_tv); // 用户类型的显示问题
            TextView tvCar = null; // 是车辆的情况 车辆的显示
            ImageView iv_car_bg = null; // 是车辆的情况 车辆的图片显示
            if (isCar) { // 车辆
                tvCar = (TextView) view.findViewById(cn.xmzt.www.R.id.maker_car_info_tv);
                iv_car_bg = view.findViewById(R.id.iv_bg);
                if (update >= TIME_OUT) { // 当前车辆离线情况
                    tvCar.setTextColor(Color.parseColor("#ffffff"));
                    tvCar.setBackgroundResource(R.drawable.marker_shared_navigation_map_car_info_offline_bg);
                    if (mIsNight){ // 晚上
                        iv_car_bg.setBackgroundResource(R.drawable.marker_car_black_icon);
                    } else { // 白天
                        iv_car_bg.setBackgroundResource(R.drawable.map_marker_offline_car_head_icon);
                    }
                } else {
                    tvCar.setTextColor(Color.parseColor("#333333"));
                    tvCar.setBackgroundResource(R.drawable.marker_shared_navigation_map_car_info_bg);
                    if (mIsNight){ // 晚上
                        iv_car_bg.setBackgroundResource(R.drawable.marker_car_black_icon);
                    } else { // 白天
                        iv_car_bg.setBackgroundResource(R.drawable.shared_navigation_car_other);
                    }
                }
                tvCar.setText(carinfo);
            }
            ImageView iv_hint = null;
            iv_hint = (ImageView) view.findViewById(cn.xmzt.www.R.id.iv_hint);
            if (distance >= mDistanceMarker && update < TIME_OUT) {
                iv_hint.setVisibility(View.VISIBLE);
            } else {
                iv_hint.setVisibility(View.GONE);
            }
            ImageView ivBottomBg = (ImageView) view.findViewById(cn.xmzt.www.R.id.maker_share_navigation_bg_iv);
            RoundImageView ivheadImg = (RoundImageView) view.findViewById(cn.xmzt.www.R.id.maker_head_icon);
            tvNickName.setText(name);
            if (update >= TIME_OUT) {
                type = "离线";
                tvType.setBackgroundResource(R.drawable.marker_shared_navigation_map_offline_type_bg);
                tvNickName.setTextColor(Color.parseColor("#999999"));
            } else {
                if (type.contains("成员")) {
                    tvType.setBackgroundResource(R.drawable.marker_shared_navigation_map_captain_info_bg);
                } else if (type.contains("司机")) {
                    tvType.setBackgroundResource(R.drawable.marker_shared_navigation_map_captain_info_bg);
                } else {
                    tvType.setBackgroundResource(R.drawable.marker_shared_navigation_map_captain_leander_info_bg);
                }
            }
            tvType.setText(type);
            if (!isCar) {
                ivBottomBg.setImageResource(R.drawable.shared_navigation_map_marker_bottom_notclick_bg);
            }
            // judging show myLoc
            if (!isCar) {
                if (isCar) { // judging is car type
                    ivheadImg.setImageResource(R.drawable.marker_shared_navigation_not_click_car_icon);
                } else {
                    if (update >= TIME_OUT) {
                        ivheadImg.setImageResource(R.drawable.map_marker_offline_person_head_icon);
                    } else {
                        ivheadImg.setImageBitmap(bitmapurl);
                    }
                }
            }
            Bitmap bitmap = BitmapUtils.convertViewToBitmap(view);
            int radius = DensityUtil.dip2px(this, radiusmarker);
            MarkerOptions markerOption = null;
            String data = userId;
            if (distance >= mDistanceMarker && update >= TIME_OUT) {
                data += ",.";
            } else if (distance >= mDistanceMarker) {
                data += ",";
            } else if (update >= TIME_OUT) {
                data += ".";
            }
            markerOption = new MarkerOptions()
                    .position(latLng)
                    .anchor(0.5f, isCar ? 0.77f : 0.86f)
                    .setInfoWindowOffset(0, radius)
                    .snippet(isCar ? CAR_TYPE : PERSON_TYPE)
                    .title(data) // 在超过10Km的title中添加一个逗号
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
            if (!isCar) {
                Marker marker = mAmap.addMarker(markerOption);
                Animation markerAnimation = new ScaleAnimation(1.0f, 1.6f, 1.0f, 1.6f);
                markerAnimation.setDuration(0);
                markerAnimation.setFillMode(1);
                marker.setAnimation(markerAnimation);
                marker.setClickable(true);
                mAllMarker.add(marker);
            } else {
                Marker marker = mAmap.addMarker(markerOption);
                marker.setClickable(true);
                mAllMarker.add(marker);
            }
        }
    }


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

    /**
     * clear all Marker
     */
    private void clearAllMarker() {
        for (int i = 0; i < mAllMarker.size(); i++) {
            mAllMarker.get(i).remove();
        }
        mAllMarker.clear();
    }

    private void setOtherAddMaker() {
        // init marker
        if (mRightInfo == null) {
            return;
        }
        if (mRightInfo.size() < 1) {
            return;
        }
        for (int i = 0; i < mRightInfo.size(); i++) {
            if (!mIsShowCarInfo) {
                final int isss = i;
                if (!"".equals(mRightInfo.get(i).getBean().avatar) && null != mRightInfo.get(i).getBean().avatar) {
                    if (mRightInfo.get(isss).getBean().time != 0 &&
                            mRightInfo.get(isss).getBean().latitude != 0.0f &&
                            mRightInfo.get(isss).getBean().longitude != 0.0f &&
                            !mRightInfo.get(isss).getBean().userId.equals(mUserId)) { // 判断当前用户信息
                        GlideApp.with(this).asBitmap().load(mRightInfo.get(i).getBean().avatar)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        if (mRightInfo.size() > isss) {
                                            addMyCarAndPersonLocationMarker(
                                                    mIsShowCarInfo,
                                                    new LatLng(mRightInfo.get(isss).getBean().latitude, mRightInfo.get(isss).getBean().longitude),
                                                    mIsShowCarInfo ? 25 : 15,
                                                    resource,
                                                    getTypeStr(mRightInfo.get(isss).getBean().type),
                                                    mRightInfo.get(isss).getBean().nickName,
                                                    mRightInfo.get(isss).getBean().numberPlate,
                                                    mRightInfo.get(isss).getBean().userId,
                                                    mRightInfo.get(isss).getDistance(),
                                                    mRightInfo.get(isss).getBean().time);
                                        }
                                    }
                                });
                    }
                } else {
                    if (mRightInfo.get(isss).getBean().time != 0 &&
                            mRightInfo.get(isss).getBean().latitude != 0.0f &&
                            mRightInfo.get(isss).getBean().longitude != 0.0f &&
                            !mRightInfo.get(isss).getBean().userId.equals(mUserId)) {
                        GlideApp.with(this).asBitmap().load(R.drawable.icon_head_default)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        if (mRightInfo.size() > isss) {
                                            addMyCarAndPersonLocationMarker(
                                                    mIsShowCarInfo,
                                                    new LatLng(mRightInfo.get(isss).getBean().latitude, mRightInfo.get(isss).getBean().longitude),
                                                    mIsShowCarInfo ? 25 : 15,
                                                    resource,
                                                    getTypeStr(mRightInfo.get(isss).getBean().type),
                                                    mRightInfo.get(isss).getBean().nickName,
                                                    mRightInfo.get(isss).getBean().numberPlate,
                                                    mRightInfo.get(isss).getBean().userId,
                                                    mRightInfo.get(isss).getDistance(),
                                                    mRightInfo.get(isss).getBean().time);
                                        }
                                    }
                                });
                    }
                }
            } else {
                if (mRightInfo.get(i).getBean().time != 0 &&
                        mRightInfo.get(i).getBean().latitude != 0.0f &&
                        mRightInfo.get(i).getBean().longitude != 0.0f &&
                        !mRightInfo.get(i).getBean().userId.equals(mUserId)) {
                    addMyCarAndPersonLocationMarker(
                            mIsShowCarInfo,
                            new LatLng(mRightInfo.get(i).getBean().latitude, mRightInfo.get(i).getBean().longitude),
                            mIsShowCarInfo ? 65 : 15,
                            null,
                            getTypeStr(mRightInfo.get(i).getBean().type),
                            mRightInfo.get(i).getBean().nickName,
                            mRightInfo.get(i).getBean().numberPlate,
                            mRightInfo.get(i).getBean().userId,
                            mRightInfo.get(i).getDistance(),
                            mRightInfo.get(i).getBean().time);
                }
            }
        }
    }

    private Marker mCurrentMemMarker = null;
    private Marker mCurrentOtherMarker = null;

    // marker click
    AMap.OnMarkerClickListener mMarkerListener = new AMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            if (mCurrentMemMarker == marker) {
                return false;
            }
            if (mCurrentOtherMarker == marker){
                return false;
            }
            if (CAR_TYPE.equals(marker.getSnippet())){
                if (mCurrentMemMarker != null) {
                    if (!CAR_TYPE.equals(mCurrentMemMarker.getSnippet())) {
                        mCurrentMemMarker.startAnimation();
                        setNotClickedMarkerAnim();
                        mCurrentMemMarker = null;
                    }
                }
                mCurrentOtherMarker = marker;
                marker.showInfoWindow();
            } else {
                if (PERSON_TYPE.equals(marker.getSnippet())) {
                    if (mCurrentMemMarker != null) {
                        mCurrentMemMarker.startAnimation();
                        setNotClickedMarkerAnim();
                    }
                    mCurrentMemMarker = marker;
                    marker.startAnimation();
                    marker.showInfoWindow();
                    setClickedMarkerAnim();
                }
            }
            return false;
        }
    };

    /**
     * setting original animation
     */
    private void setClickedMarkerAnim() {
        if (mCurrentMemMarker != null) {
            Animation markerAnimation = new ScaleAnimation(1.0f, 1.0f, 1.0f, 1.0f); // update original view
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
            Animation markerAnimation = new ScaleAnimation(1.0f, 1.6f, 1.0f, 1.6f); //click then big view marker
            markerAnimation.setDuration(0);
            markerAnimation.setFillMode(1);
            mCurrentMemMarker.setAnimation(markerAnimation);
        }
    }

    /**
     * init infowindow
     */
    AMap.InfoWindowAdapter mAMapOtherAdapter = new AMap.InfoWindowAdapter() {
        @Override
        public View getInfoWindow(Marker marker) {
            View infoContent = null;
            if (CAR_TYPE.equals(marker.getSnippet())) { // judging is car
                if (marker.getTitle().contains(".")){ // 离线的情况显示的
                    if (mIsNight) { // 晚上
                        infoContent = getLayoutInflater().inflate(R.layout.infowindow_guide_car_offine_item_black, null);
                    } else { // 白天
                        infoContent = getLayoutInflater().inflate(R.layout.infowindow_guide_car_offine_item_white, null);
                    }
                } else {
                    if (mIsNight) { // 晚上
                        infoContent = getLayoutInflater().inflate(R.layout.infowindow_guide_car_item_black, null);
                    } else { // 白天
                        infoContent = getLayoutInflater().inflate(R.layout.infowindow_guide_car_item_white, null);
                    }
                }
            } else if (PERSON_TYPE.equals(marker.getSnippet())) {
                if (marker.getTitle().contains(".")){ // 离线的情况显示的
                    if (mIsNight) { // 晚上
                        infoContent = getLayoutInflater().inflate(R.layout.infowindow_guide_person_offline_item_black, null);
                    } else { // 白天
                        infoContent = getLayoutInflater().inflate(R.layout.infowindow_guide_person_offline_item_white, null);
                    }
                } else {
                    if (mIsNight) { // 晚上
                        infoContent = getLayoutInflater().inflate(R.layout.infowindow_guide_person_item_black, null);
                    } else { // 白天
                        infoContent = getLayoutInflater().inflate(R.layout.infowindow_guide_person_item_white, null);
                    }
                }
            }
            render(marker, infoContent);
            return infoContent;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View infoContent = null;
            if (CAR_TYPE.equals(marker.getSnippet())) { // judging is car
                if (marker.getTitle().contains(".")){ // 离线的情况显示的
                    if (mIsNight) { // 晚上
                        infoContent = getLayoutInflater().inflate(R.layout.infowindow_guide_car_offine_item_black, null);
                    } else { // 白天
                        infoContent = getLayoutInflater().inflate(R.layout.infowindow_guide_car_offine_item_white, null);
                    }
                } else {
                    if (mIsNight) { // 晚上
                        infoContent = getLayoutInflater().inflate(R.layout.infowindow_guide_car_item_black, null);
                    } else { // 白天
                        infoContent = getLayoutInflater().inflate(R.layout.infowindow_guide_car_item_white, null);
                    }
                }
            } else if (PERSON_TYPE.equals(marker.getSnippet())) {
                if (marker.getTitle().contains(".")){ // 离线的情况显示的
                    if (mIsNight) { // 晚上
                        infoContent = getLayoutInflater().inflate(R.layout.infowindow_guide_person_offline_item_black, null);
                    } else { // 白天
                        infoContent = getLayoutInflater().inflate(R.layout.infowindow_guide_person_offline_item_white, null);
                    }
                } else {
                    if (mIsNight) { // 晚上
                        infoContent = getLayoutInflater().inflate(R.layout.infowindow_guide_person_item_black, null);
                    } else { // 白天
                        infoContent = getLayoutInflater().inflate(R.layout.infowindow_guide_person_item_white, null);
                    }
                }
            }
            render(marker, infoContent);
            return infoContent;
        }

        private void render(Marker marker, View view) {
            if (marker.getTitle().contains(".")) { // 离线的情况显示的
                // 再去判断是否是车辆信息 还是人员信息
                TextView maker_nick_name_tv = view.findViewById(R.id.maker_nick_name_tv);
                TextView tv_update_time = view.findViewById(R.id.tv_update_time);
                TextView maker_car_info_tv = null;
                //获取信息
                String title = marker.getTitle();
                if (title.contains(",.")) { // 表示距离大于10Km 并且离线的用户
                    title = title.substring(0, title.length() - 2);
                } else if (title.contains(",")) { // 表示距离大于10Km的情况
                    title = title.substring(0, title.length() - 1);
                } else if (title.contains(".")) { // 表示用户离线的情况
                    title = title.substring(0, title.length() - 1);
                }
                SharedNavigationMapRightInfo bean = null;
                for (int i = 0; i < mRightInfo.size(); i++) {
                    if (title.equals(mRightInfo.get(i).getBean().userId)) {
                        bean = mRightInfo.get(i);
                        break; // 获取到当前信息 跳出循环
                    }
                }
                maker_nick_name_tv.setText(bean.getBean().nickName);
                tv_update_time.setText(TimeUtil.updateTimeSmart(bean.getBean().time));
                if (CAR_TYPE.equals(marker.getSnippet())) { // 是车辆信息
                    maker_car_info_tv = view.findViewById(R.id.maker_car_info_tv);
                    maker_car_info_tv.setText(bean.getBean().numberPlate);
                }
            } else {
                TextView maker_nick_name_tv = view.findViewById(R.id.maker_nick_name_tv);
                TextView tv_update_distance = view.findViewById(R.id.tv_update_distance);
                TextView tv_update_time = view.findViewById(R.id.tv_update_time);
                ImageView iv_user_head_img = null;
                TextView infowindow_type_tv = null;
                TextView maker_car_info_tv = null;
                String title = marker.getTitle();
                if (title.contains(",.")) { // 表示距离大于10Km 并且离线的用户
                    title = title.substring(0, title.length() - 2);
                } else if (title.contains(",")) { // 表示距离大于10Km的情况
                    title = title.substring(0, title.length() - 1);
                } else if (title.contains(".")) { // 表示用户离线的情况
                    title = title.substring(0, title.length() - 1);
                }
                SharedNavigationMapRightInfo bean = null;
                for (int i = 0; i < mRightInfo.size(); i++) {
                    if (title.equals(mRightInfo.get(i).getBean().userId)) {
                        bean = mRightInfo.get(i);
                        break; // 获取到当前信息 跳出循环
                    }
                }
                if (CAR_TYPE.equals(marker.getSnippet())) { // judging is car
                    iv_user_head_img = view.findViewById(R.id.iv_user_head_img); // car
                    GlideUtil.setImageCircle(mContext, bean.getBean().avatar, iv_user_head_img, R.drawable.icon_default);
                    maker_car_info_tv = view.findViewById(R.id.maker_car_info_tv);
                    maker_car_info_tv.setText(bean.getBean().numberPlate);
                } else if (PERSON_TYPE.equals(marker.getSnippet())) {
                    infowindow_type_tv = view.findViewById(R.id.infowindow_type_tv); // person
                    String typeStr = getTypeStr(bean.getBean().type);
                    infowindow_type_tv.setText(typeStr);
                    if (typeStr.contains("成员")) {
                        infowindow_type_tv.setBackgroundResource(R.drawable.marker_shared_navigation_map_captain_info_bg);
                    } else {
                        infowindow_type_tv.setBackgroundResource(R.drawable.marker_shared_navigation_map_captain_leander_info_bg);
                    }
                }
                maker_nick_name_tv.setText(bean.getBean().nickName);
                // include latlng calculate distance
                float distance = AMapUtils.calculateLineDistance(marker.getPosition(), new LatLng(Constants.mLatitude, Constants.mLongitude));
                tv_update_distance.setText("离我" + AMapUtil.getFriendlyLength((int) distance));
                tv_update_time.setText(TimeUtil.updateTimeSmart(bean.getBean().time));
            }
        }
    };

}
