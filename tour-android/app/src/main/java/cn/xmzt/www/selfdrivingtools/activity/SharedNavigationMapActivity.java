package cn.xmzt.www.selfdrivingtools.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.blankj.utilcode.util.ActivityUtils;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.model.Team;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ActivitySharedNavigationMapBinding;
import cn.xmzt.www.dialog.GuideDialog;
import cn.xmzt.www.dialog.SharedNavigationDatePickerDialog;
import cn.xmzt.www.glide.GlideApp;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.helper.UserHelper;
import cn.xmzt.www.intercom.activity.GroupInfoActivity;
import cn.xmzt.www.intercom.activity.TeamRoomActivity;
import cn.xmzt.www.intercom.api.NimUIKit;
import cn.xmzt.www.intercom.api.model.team.TeamDataChangedObserver;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.intercom.bean.GroupRoomBean;
import cn.xmzt.www.intercom.event.BackMapAndChatStatusEvent;
import cn.xmzt.www.intercom.event.LocationShareStatusEvent;
import cn.xmzt.www.intercom.event.RefreshMyTalkGroupList;
import cn.xmzt.www.intercom.profile.TeamLocationProfile;
import cn.xmzt.www.main.activity.MainActivity;
import cn.xmzt.www.main.globals.TalkManage;
import cn.xmzt.www.roomdb.TourDatabase;
import cn.xmzt.www.roomdb.beans.GroupUserInfo;
import cn.xmzt.www.selfdrivingtools.adapter.SharedNavigationMapRightAdapter;
import cn.xmzt.www.selfdrivingtools.adapter.SharedNavigationTripDaysAdapter;
import cn.xmzt.www.selfdrivingtools.adapter.SharedNavigationTripListAdapter;
import cn.xmzt.www.selfdrivingtools.bean.GetDestinationBean;
import cn.xmzt.www.selfdrivingtools.bean.GuideMapMyInfo;
import cn.xmzt.www.selfdrivingtools.bean.MapStartEndLatLngBean;
import cn.xmzt.www.selfdrivingtools.bean.SettingDestinationTypeInfo;
import cn.xmzt.www.selfdrivingtools.bean.SharedNavigationMapLineBean;
import cn.xmzt.www.selfdrivingtools.bean.SharedNavigationMapLinesBean;
import cn.xmzt.www.selfdrivingtools.bean.SharedNavigationMapRightInfo;
import cn.xmzt.www.selfdrivingtools.bean.TourTripDetailNewBean;
import cn.xmzt.www.selfdrivingtools.event.NavigationTypeEvent;
import cn.xmzt.www.manager.IntentManager;
import cn.xmzt.www.selfdrivingtools.event.TripSignInEvent;
import cn.xmzt.www.selfdrivingtools.event.UpdateDesAndTimeEvent;
import cn.xmzt.www.selfdrivingtools.event.UpdateGroupDefaultEvent;
import cn.xmzt.www.selfdrivingtools.overlay.AMapUtil;
import cn.xmzt.www.selfdrivingtools.overlay.SharedHomeDrivingRouteOverlay;
import cn.xmzt.www.selfdrivingtools.viewmodel.SharedNavigationMapViewModel;
import cn.xmzt.www.smartteam.bean.TripSignInDetailBean;
import cn.xmzt.www.utils.BitmapUtils;
import cn.xmzt.www.utils.DensityUtil;
import cn.xmzt.www.utils.GPSUtils;
import cn.xmzt.www.utils.LocalDataUtils;
import cn.xmzt.www.utils.SPUtils;
import cn.xmzt.www.utils.StatusBarUtil;
import cn.xmzt.www.utils.TimeUtil;
import cn.xmzt.www.utils.ToastUtils;
import cn.xmzt.www.view.CenterLayoutManager;
import cn.xmzt.www.view.RoundImageView;
import cn.xmzt.www.view.ScrollGridLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * sharedNavigationMap
 */
public class SharedNavigationMapActivity extends TourBaseActivity<SharedNavigationMapViewModel, ActivitySharedNavigationMapBinding> {

    private AMap mAmap;
    // setting time dialog
    private SharedNavigationDatePickerDialog mTimerPicker;
    String mStartTime = "2019-08-23 19:47";
    String mEndTime = "2029-08-23 19:47";
    // Judging that the current interface displays shared mode default vehicles
    private boolean mIsShowCarInfo = false;
    // right list adapter
    private SharedNavigationMapRightAdapter mRightAdapter;
    // save all marker not contains my marker
    List<Marker> mAllMarker = new ArrayList<>();
    // line all marker
    List<Marker> mAllLineMarker = new ArrayList<>();
    // my marker
    Marker mMyLocationMarker = null;
    Marker mDestinationMarker = null;
    // The current marker's Infowindow
    Marker mCurrentMemMarker = null;
    Marker mCurrentOtherMarker = null;
    private int mInitMapZoom = 19;
    List<SettingDestinationTypeInfo> mTripDaysListInfo = null;
    private SharedNavigationTripDaysAdapter mDaysAdapter;
    private SharedNavigationTripListAdapter mTripListAdapter;
    // current day choice position
    private int mBottomDaysPosition = 0;

    // captain location
    private LatLng mCaptainLatLng = null;

    // judging is captain type
    private boolean mIsCaptain = false;

    // marker type
    public String CAR_TYPE = "car";
    public String PERSON_TYPE = "person";
    public String LINE_TYPE = "line";
    public String CAPTAIN_TYPE = "captain";

    RouteSearch mRouteSearch = null;
    // judging setting destination
    private boolean mIsSetDestination = false;
    // bottom is not show
    private boolean mIsBottomShow = false;

    private SharedNavigationMapActivity mContext;

    // setting destination return code
    private int REQUEST_CODE = 1001;
    // request Group id
    private String mGroupId = "";
    private int mTripId = 37395;
    // Recommand code
    private String mRefCode = "";
    // judging share interface
    private String mShareContent = "";

    // judging is share navigation my loc
    private boolean mIsShareLoc = false;

    private int mRole = 0;

    private boolean mLeader = false;
    // car id
    private boolean mIsAutoPlay = true;
    // trip detail info
    private TourTripDetailNewBean mTripDetailBean = null;

    private CenterLayoutManager mTopTripLayoutManager;
    private CenterLayoutManager mCenterLayoutManager;

    // save current destination info
    private String destination;
    private String destinationCoordinate;
    private double destinationLat;
    private double destinationLng;
    private String gatherTime;
    private long mStageId = 0;

    private boolean mIsActivity = true;

    private String mToken = "";
    private long TIME_OUT = 60 * 1000 * 5L;

    // Judge if it's the day to use this
    private List<SharedNavigationMapLineBean> mDestinationLatLngList = new ArrayList<>();
    // Full time display
    private List<SharedNavigationMapLineBean> mDestinationLatLngAllList = new ArrayList<>();
    // Get information by day based on information
    private List<SharedNavigationMapLinesBean> mDestinationList = new ArrayList<>();

    private String mMyType = "成员";

    @Override
    protected int setLayoutId() {
        return R.layout.activity_shared_navigation_map;
    }

    @Override
    protected SharedNavigationMapViewModel createViewModel() {
        mContext = this;
        viewModel = new SharedNavigationMapViewModel();
        viewModel.setIView(this);
        viewModel.mSettingDestination.observe(this, new Observer<Object>() {
            @Override
            public void onChanged(@Nullable Object info) {
                if (Constants.OK.equals(info)) {
                    // setting destination setting time interface return info
                    ToastUtils.showText(mContext, "设置成功");
                    viewModel.sendMessageTeamLocation3(mGroupId);
                    // update view
                    dataBinding.tvTimeContentC.setText(TimeUtil.getShowGatherTime(gatherTime));
                    dataBinding.tvDesContentC.setText(destination);
                    mIsSetDestination = true;
                    initTopView();
                    mCaptainLatLng = new LatLng(destinationLat, destinationLng);
//                    if (mCaptainLatLng != null) {
//                        addCaptainMarkersToMap(mContext, mCaptainLatLng);
//                    }
                    viewModel.getGroupSettingDestination(mToken,mGroupId);
                }
            }
        });
        viewModel.mGroupMemberBean.observe(this, new Observer<GuideMapMyInfo>() {
            @Override
            public void onChanged(@Nullable GuideMapMyInfo groupMemberBean) {
                setMyInfoData(groupMemberBean);
            }
        });
        viewModel.mTripDetailInfo.observe(mContext, new Observer<TourTripDetailNewBean>() {
            @Override
            public void onChanged(@Nullable TourTripDetailNewBean tourTripDetailBean) {
                // request trip detail info
                if (tourTripDetailBean != null) {
                    mTripDetailBean = tourTripDetailBean;
                    int groupMemberCounts=tourTripDetailBean.getGroupMemberCounts();
                    if(groupMemberCounts>0){
                        dataBinding.tvTitleNumber.setText("(" +groupMemberCounts + ")");
                    }
                    setData();
                }
            }
        });
        viewModel.mGetSettingDestination.observe(mContext, new Observer<GetDestinationBean>() {
            @Override
            public void onChanged(@Nullable GetDestinationBean getDestinationBean) {
                setSettingDestinationAndTimeView(getDestinationBean);
            }
        });
        viewModel.mIsShareLocationOrIsAutoplay.observe(this, new Observer<Object>() {
            @Override
            public void onChanged(@Nullable Object info) {
                if (info != null) {
                    if (Constants.OK.equals(info)) {
                        // setting destination setting time interface return info
                        if (mUserId != null && !"".equals(mUserId)) {
                            if (!mIsFirst) {
                                ToastUtils.showText(mContext, "设置成功");
                            }
                            mIsFirst = false;
                            EventBus.getDefault().post(new LocationShareStatusEvent(mGroupId, mUserId, mIsShareLoc));
                        }
                    } else {
                        ToastUtils.showText(mContext,(String)info);
                        mIsJoinService = false;
                        mIsShareLoc = !mIsShareLoc;
                        dataBinding.switchMyLocation.setChecked(mIsShareLoc);
                    }
                } else {
                    ToastUtils.showText(mContext,"设置失败");
                    mIsJoinService = false;
                    mIsShareLoc = !mIsShareLoc;
                    dataBinding.switchMyLocation.setChecked(mIsShareLoc);
                }
            }
        });
        viewModel.mGroupInfoBean.observe(this, new Observer<GroupRoomBean>() {
            @Override
            public void onChanged(GroupRoomBean groupRoomBean) {
                mTripId = groupRoomBean.getTripId();
                dataBinding.tvTitleName.setText(groupRoomBean.getGroupTitle());
                viewModel.getTripDetail(mToken, mTripId, mRefCode, mShareContent,mGroupId);
                Constants.mGroupName = groupRoomBean.getGroupTitle();
            }
        });

        viewModel.mSignInCheckData.observe(this, new Observer<TripSignInDetailBean>() {
            @Override
            public void onChanged(TripSignInDetailBean tripSignInDetailBean) {
                mInfoDetail = tripSignInDetailBean;
            }
        });

        viewModel.mSignIn.observe(this, new Observer<Object>() {
            @Override
            public void onChanged(Object o) {
                mInfoDetail.setIsSignin(1);
                // 刷新界面
                viewModel.getSignInChecked(mToken, String.valueOf(mStageId));
                // 发送EventBus 如果界面存在就刷新
                EventBus.getDefault().post(new TripSignInEvent());
            }
        });
        return viewModel;
    }

    private TripSignInDetailBean mInfoDetail = null;

    private boolean mIsFirst = true;

    private String avtor = "";
    private String nickname = "";
    private String mUserId = "";

    private void setMyInfoData(GuideMapMyInfo groupMemberBean) {
        if (groupMemberBean != null) {
            mIsAutoPlay = groupMemberBean.isAutoplay();
            if (groupMemberBean.getRole() == 1) {
                mIsCaptain = true;
                mMyType = "群主";
            } else {
                if (groupMemberBean.isLeader()) {
                    mIsCaptain = true;
                    mMyType = "领队";
                } else {
                    mIsCaptain = false;
                    mMyType = "成员";
                }
            }
            mIsShareLoc = groupMemberBean.isShareLocation();
            Constants.mMyCarInfo = groupMemberBean.getLicencePlate();
            mRole = groupMemberBean.getRole();
            mLeader = groupMemberBean.isLeader();
            avtor = groupMemberBean.getImage();
            nickname = groupMemberBean.getUserName();
            mUserId = groupMemberBean.getUserId() + "";
        }
        // set right mine info
        setRightMyInfo();
        // init share myLoc
        initShareMyLoc();

        viewModel.getGroupSettingDestination(mToken, mGroupId);

        getUserInfoToDataBase();

        // init right data
        mHandler.sendEmptyMessage(HANDLER_GET_MEMBER_INFO);

        // 分享位置
        mHandler.sendEmptyMessage(HANDLER_UPDATE_SHARE_LOCATION);
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        mIsActivity = true;
        // Initialize all member cache information
        TeamLocationProfile.getInstance().registerObserver(true);

        dataBinding.setModel(viewModel);
        dataBinding.setActivity(this);

        StatusBarUtil.setStatusBarLightMode(this, true);
        mGroupId = getIntent().getStringExtra("groupid");

        // init right list recycleview
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        dataBinding.rvPersonList.setLayoutManager(layoutManager);
        mRightAdapter = new SharedNavigationMapRightAdapter(this);
        dataBinding.rvPersonList.setAdapter(mRightAdapter);

        // init bottom day list recycleview
        mCenterLayoutManager = new CenterLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        dataBinding.rvDaysInfo.setLayoutManager(mCenterLayoutManager);
        mDaysAdapter = new SharedNavigationTripDaysAdapter(this);
        dataBinding.rvDaysInfo.setAdapter(mDaysAdapter);

        // init bottom trip list recycleview

        ScrollGridLayoutManager manager = new ScrollGridLayoutManager(this, 1, GridLayoutManager.VERTICAL,false);
        dataBinding.rvTripList.setLayoutManager(manager);
        mTripListAdapter = new SharedNavigationTripListAdapter(this);
        dataBinding.rvTripList.setAdapter(mTripListAdapter);

        // init setting time dialog
        initTimerPicker();
        // init top view
        initTopView();
        // init click
        initClick();

        initDatas();
    }

    private void initDatas(){
        // 入群后,加入或切换对讲组
        TalkManage mTalkManage = TalkManage.getInstance();
        mTalkManage.joinOrSwitchIntercomGroup(mGroupId);

        EventBus.getDefault().post(new UpdateGroupDefaultEvent(mGroupId)); // 设置默认开启或者不开启位置信息

        dataBinding.tvTitleName.setText(Constants.mGroupName);
        mToken = TourApplication.getToken();

        viewModel.getMyUser(mToken, mGroupId, "");
        // init map view
        dataBinding.mvMap.onCreate(getIntent().getExtras());
        viewModel.getGroupRoomInfo(mToken,mGroupId);

        if (mAmap == null) {
            mAmap = dataBinding.mvMap.getMap();
            mAmap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(Constants.mLatitude,Constants.mLongitude)));
            setUpMap(); // 定位
        }
        // init map info
        initMapData();

        if (!SPUtils.getBoolean("sharedGuide", false)) {
            GuideDialog dialog = new GuideDialog(this);
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_shared_guide, null);
            ImageView know = view.findViewById(R.id.iv_know);
            know.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.setContentView(view);
            dialog.show();
            SPUtils.putBoolean("sharedGuide",true);
        }
    }

    private void initClick() {
        // Do you share my location
        dataBinding.switchMyLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // share myLoc change listener
                if (b) { // share open
                    mIsShareLoc = true;
                } else { // share close
                    mIsShareLoc = false;
                }
                // deal with share loc interface
                saveSettingShareLoc();
            }
        });
        // right move
        dataBinding.rlRightContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        // 地图触摸事件
        dataBinding.mvMap.getMap().setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent event) {
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
    }

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
            } else if (!CAPTAIN_TYPE.equals(mCurrentMemMarker.getSnippet()) && !CAR_TYPE.equals(mCurrentMemMarker.getSnippet()) && !LINE_TYPE.equals(mCurrentMemMarker.getSnippet())){
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

    private boolean mIsJoinService = true;

    /**
     * deal with share loc interface
     */
    private void saveSettingShareLoc() {
        if (mIsJoinService) {
            mIsJoinService = true;
            viewModel.getIsShareLocationOrIsAutoplay(mToken, mGroupId, mIsAutoPlay, mIsShareLoc);
//        hideRightShareContentView();
        }
    }

    /**
     * init share myLoc view
     */
    private void initShareMyLoc() {
        dataBinding.switchMyLocation.setChecked(mIsShareLoc);
    }

    /**
     * setting data to view
     */
    private void setData() {
        if (mTripDetailBean != null) {
            if (mTripDetailBean.getLineRouteListVO() != null) {
                if (mTripDetailBean.getLineRouteListVO().size() > 0) {
                    // init bottom data
                    initBottomData();
                    // get line destination info
                    getDestinationInfo();
                }
            }
            // 将数据保存到本地数据库中
            saveGroupInfoAll();
        }
    }

    /**
     * 将界面中所有的数据保存到数据库
     */
    private void saveGroupInfoAll(){
        List<GroupMemberBean> groupMemberVOS = mTripDetailBean.getGroupMemberList();
        if (mTripDetailBean.getGroupMemberList() != null){
            List<GroupUserInfo> listService = new ArrayList<>();//添加服务器获取之后的所有数据
            for (int i = 0; i< groupMemberVOS.size();i++){
                GroupUserInfo info = TourDatabase.getDefault(mContext).getGroupUserInfoDao().getData(mGroupId, String.valueOf(groupMemberVOS.get(i).getUserId()));
                if (info == null){ // 表示数据库有数据就不管
                    GroupMemberBean bean = groupMemberVOS.get(i);
                    info = new GroupUserInfo();
                    info.type = bean.getRole() == 1 ? 0 : (bean.isLeader() ? 1 : 2);
                    info.groupId = mGroupId;
                    info.userId = String.valueOf(bean.getUserId());
                    info.avatar = bean.getImage();
                    info.time = 0;
                    info.latitude = 0.0;
                    info.longitude = 0.0;
                    info.nickName = !TextUtils.isEmpty(bean.getNickname()) ? bean.getNickname() : (!TextUtils.isEmpty(bean.getUserName()) ? bean.getUserName() : bean.getPhone());
                    info.numberPlate = bean.getLicencePlate();
                    // 将数据保存到数据库
                    TourDatabase.getDefault(mContext).getGroupUserInfoDao().insert(info);
                    listService.add(info);
                } else {
                    GroupMemberBean bean = groupMemberVOS.get(i);
                    info.type = bean.getRole() == 1 ? 0 : (bean.isLeader() ? 1 : 2);
                    info.groupId = mGroupId;
                    info.userId = String.valueOf(bean.getUserId());
                    info.avatar = bean.getImage();
                    info.nickName = !TextUtils.isEmpty(bean.getNickname()) ? bean.getNickname() : (!TextUtils.isEmpty(bean.getUserName()) ? bean.getUserName() : bean.getPhone());
                    info.numberPlate = bean.getLicencePlate();
                    // 将数据保存到数据库
                    TourDatabase.getDefault(mContext).getGroupUserInfoDao().update(info);
                    listService.add(info);
                }
            }
            // 获取本地数据库当前群内的消息
            List<GroupUserInfo> listDataBase = TourDatabase.getDefault(mContext).getGroupUserInfoDao().getDataAll(mGroupId);
            // 判断当前界面的数据是否跟数据库拿取的数据是一致的
            if (listService.size() != listDataBase.size()) {
                List<GroupUserInfo> listDataBase_del = new ArrayList<>(listDataBase);
                // 将服务器的数据与本地进行对比 如果本地有 服务器没有的情况就删除当前用户
                for (int k = 0; k < listService.size(); k++) { // 先是遍历服务器的数据
                    for (int i = 0; i < listDataBase.size(); i++) {
                        if (listService.get(k).userId.equals(listDataBase.get(i).userId)) {
                            listDataBase_del.remove(listDataBase.get(i));
                            break;
                        }
                    }
                }
                // 剩余的数据就是需要删除的 如果没有就没有需要删除的数据
                for (int j = 0; j < listDataBase_del.size(); j++){
                    TourDatabase.getDefault(mContext).getGroupUserInfoDao().delete(listDataBase_del.get(j));
                }
                listDataBase_del.clear();
                listDataBase.clear();
                listService.clear();
            } else {
                listDataBase.clear();
                listService.clear();
            }
        }
    }


    private void getDestinationInfo() {
        mDestinationList.clear();
        for (int i = 0; i < mTripDetailBean.getLineRouteListVO().size(); i++) {
            int position = 1;
            List<SharedNavigationMapLineBean> beans = new ArrayList<>();
            // maybe more scenic spot
            if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList() != null) {
                if (i == 0) {
                    for (int j = 0; j < mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().size(); j++) { // 集结点第一天第一个
                        if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getDataType() == 5) { // 景点
                            if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getStaging().getCoordinateVO() != null) {
                                String data = mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getStaging().getAddress();
                                if (TextUtils.isEmpty(data)) {
                                    if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getStaging().getAreaVO() != null) {
                                        data = mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getStaging().getAreaVO().getAreaName();
                                    } else {
                                        data = "无";
                                    }
                                }
                                beans.add(new SharedNavigationMapLineBean(
                                        new LatLng(mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getStaging().getCoordinateVO().getLatitude(),
                                                mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getStaging().getCoordinateVO().getLongitude()),
                                        (i + 1) + "-" + position,
                                        data, 3));// 集结点
                                position += 1;
                            }
                        }
                    }
                }
                for (int j = 0; j < mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().size(); j++) {
                    if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getDataType() == 2){ // 景点
                        if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getSpot().getCoordinateVO() != null) {
                            beans.add(new SharedNavigationMapLineBean(
                                    new LatLng(mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getSpot().getCoordinateVO().getLatitude(),
                                            mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getSpot().getCoordinateVO().getLongitude()),
                                    (i + 1) + "-" + position,
                                    mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getSpot().getTitle(), 0)); // spot
                            position += 1;
                        }
                    }

                    if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getDataType() == 4){ // 住宿
                        if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getHotel().getCoordinateVO() != null) {
                            beans.add(new SharedNavigationMapLineBean(
                                    new LatLng(mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getHotel().getCoordinateVO().getLatitude(),
                                            mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getHotel().getCoordinateVO().getLongitude()),
                                    (i + 1) + "-" + position,
                                    mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getHotel().getAccommodation(), 1));// live
                            position += 1;
                        }
                    }

                    if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getDataType() == 8){ // 餐点 早
                        if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getBreakfast().getCoordinateVO() != null) {
                            String data = mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getBreakfast().getMeal();
                            if (TextUtils.isEmpty(data)){
                                data = "自理";
                            }
                            beans.add(new SharedNavigationMapLineBean(
                                    new LatLng(mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getBreakfast().getCoordinateVO().getLatitude(),
                                            mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getBreakfast().getCoordinateVO().getLongitude()),
                                    (i + 1) + "-" + position,
                                    data, 2));// 餐点
                            position += 1;
                        }
                    }

                    if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getDataType() == 9){ // 餐点 中
                        if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getLunch().getCoordinateVO() != null) {
                            String data = mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getLunch().getMeal();
                            if (TextUtils.isEmpty(data)){
                                data = "自理";
                            }
                            beans.add(new SharedNavigationMapLineBean(
                                    new LatLng(mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getLunch().getCoordinateVO().getLatitude(),
                                            mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getLunch().getCoordinateVO().getLongitude()),
                                    (i + 1) + "-" + position,
                                    data, 2));// 餐点
                            position += 1;
                        }
                    }

                    if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getDataType() == 10){ // 餐点 晚
                        if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getDinner().getCoordinateVO() != null) {
                            String data = mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getDinner().getMeal();
                            if (TextUtils.isEmpty(data)){
                                data = "自理";
                            }
                            beans.add(new SharedNavigationMapLineBean(
                                    new LatLng(mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getDinner().getCoordinateVO().getLatitude(),
                                            mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getDinner().getCoordinateVO().getLongitude()),
                                    (i + 1) + "-" + position,
                                    data, 2));// 餐点
                            position += 1;
                        }
                    }
                }
//                for (int j = 0; j < mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().size(); j++) {
//                    if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getDataType() == 4){ // 住宿
//                        if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getHotel().getCoordinateVO() != null) {
//                            beans.add(new SharedNavigationMapLineBean(
//                                    new LatLng(mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getHotel().getCoordinateVO().getLatitude(),
//                                            mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getHotel().getCoordinateVO().getLongitude()),
//                                    (i + 1) + "-" + position,
//                                    mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getHotel().getAccommodation(), 1));// live
//                            position += 1;
//                        }
//                    }
//                }
//                for (int j = 0; j < mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().size(); j++) {
//                    if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getDataType() == 8){ // 餐点 早
//                        if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getBreakfast().getCoordinateVO() != null) {
//                            String data = mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getBreakfast().getAddress();
//                            if (TextUtils.isEmpty(data)){
//                                if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getBreakfast().getAreaVO()!= null){
//                                    data = mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getBreakfast().getAreaVO().getAreaName();
//                                } else {
//                                    data = "早餐";
//                                }
//                            }
//                            beans.add(new SharedNavigationMapLineBean(
//                                    new LatLng(mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getBreakfast().getCoordinateVO().getLatitude(),
//                                            mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getBreakfast().getCoordinateVO().getLongitude()),
//                                    (i + 1) + "-" + position,
//                                    data, 2));// 餐点
//                            position += 1;
//                        }
//                    }
//                }
            }
            mDestinationList.add(new SharedNavigationMapLinesBean(i, beans));
        }
        mDestinationLatLngAllList.clear();
        for (int all = 0; all < mDestinationList.size(); all++) {
            for (int p = 0; p < mDestinationList.get(all).getBean().size(); p++) {
                mDestinationLatLngAllList.add(mDestinationList.get(all).getBean().get(p));
            }
        }

        // surface trip line
        initSurfacePolyAllLine();

        // get interface all marker info
        List<LatLng> list = getAllMarkerLatlngData();
        // update map zoom
        setMapBounds(list);
    }

    /**
     * update view setting time
     *
     * @param bean
     */
    private void setSettingDestinationAndTimeView(GetDestinationBean bean) {
        if (bean != null) {
            destination = bean.getDestination();
            destinationCoordinate = bean.getDestinationCoordinate();
            gatherTime = bean.getGatherTime().trim();
            mStageId = bean.getId();
            if (!"".equals(destinationCoordinate) && null != destinationCoordinate) {
                destinationLng = Double.parseDouble(destinationCoordinate.split(",")[0]);
                destinationLat = Double.parseDouble(destinationCoordinate.split(",")[1]);
                mCaptainLatLng = new LatLng(destinationLat, destinationLng);
            }

            if (mStageId > 0 && !mIsCaptain){
                viewModel.getSignInChecked(mToken, String.valueOf(mStageId));
            }

            // judging setting time
            if ("".equals(gatherTime) || null == gatherTime) {
            } else {
                dataBinding.tvTimeContentC.setText(TimeUtil.getShowGatherTime(gatherTime));
                dataBinding.tvTimeContent.setText(TimeUtil.getShowGatherTime(gatherTime));
            }
            // judging setting destination
            if ("".equals(bean.getDestination()) || null == bean.getDestination()) {
                mIsSetDestination = false;
            } else {
                mIsSetDestination = true;
                dataBinding.tvDesContentC.setText(bean.getDestination());
                dataBinding.tvDesContent.setText(bean.getDestination());
            }
            // update view
            initTopView();
            // staging marker
//            if (mCaptainLatLng != null) {
//                addCaptainMarkersToMap(this, mCaptainLatLng);
//            }
        } else {
            mIsSetDestination = false;
            initTopView();
        }
    }

    /**
     * init bottom view
     */
    private void initBottomData() {
        mTripListAdapter.setData(mTripDetailBean);

        mTripDaysListInfo = LocalDataUtils.getTripDateDaysInfo(mTripDetailBean.getLineRouteListVO().size());
        mDaysAdapter.setDatas(mTripDaysListInfo);
    }

    /**
     * update view setting destination and time
     */
    private void initTopView() {
        if (mIsSetDestination) { // already setting destination
            dataBinding.rlNoSettingData.setVisibility(View.GONE);
            if (mIsCaptain) {
                dataBinding.rlSettingDataC.setVisibility(View.VISIBLE);
                dataBinding.rlSettingDataP.setVisibility(View.GONE);
                // 显示签到列表的图标
                dataBinding.ivSignListC.setVisibility(View.VISIBLE);
            } else {
                dataBinding.rlSettingDataC.setVisibility(View.GONE);
                dataBinding.rlSettingDataP.setVisibility(View.VISIBLE);
                dataBinding.ivSignListC.setVisibility(View.GONE);
            }
        } else { // noting
            dataBinding.ivSignListC.setVisibility(View.GONE);
            if (mIsCaptain) {
                dataBinding.rlNoSettingData.setVisibility(View.VISIBLE);
            } else {
                dataBinding.rlNoSettingData.setVisibility(View.GONE);
            }
            dataBinding.rlSettingDataC.setVisibility(View.GONE);
            dataBinding.rlSettingDataP.setVisibility(View.GONE);
        }
    }

    private void initMapData() {
        UiSettings uiSettings = mAmap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false); // hide zoom view
        uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER); // logo show bottom center
        uiSettings.setRotateGesturesEnabled(false); // rotate gesture
        uiSettings.setTiltGesturesEnabled(false); // tilt gesture
        mAmap.showMapText(true);
        // marker click listener
        mAmap.setOnMarkerClickListener(mMarkerListener);
//        mAmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Constants.mLatitude, Constants.mLongitude), mInitMapZoom));
        // setting show infoWindow
        mAmap.setInfoWindowAdapter(mAMapOtherAdapter);
        // interface surface captain marker
//        if (!mIsCaptain) { // current page type is not captain then show
//            if (mCaptainLatLng != null) {
//                addCaptainMarkersToMap(this, mCaptainLatLng);
//            }
//        }
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(mRouteSearchListener);

        mRouteSearchS = new RouteSearch(this);
        mRouteSearchS.setRouteSearchListener(mRouteSearchListenerS);
    }

    RouteSearch mRouteSearchS = null;

    /**
     * start search line
     */
    public void searchRouteResultS(LatLonPoint mStartPoint, LatLonPoint mEndPoint) {
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartPoint, mEndPoint);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_SHORTEST, null, null, "");
        mRouteSearchS.calculateDriveRouteAsyn(query);
    }

    /**
     * search line listener
     */
    RouteSearch.OnRouteSearchListener mRouteSearchListenerS = new RouteSearch.OnRouteSearchListener() {
        @Override
        public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
            // bus
        }

        @Override
        public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
            // driving
            if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                if (result != null && result.getPaths() != null) {
                    if (result.getPaths().size() > 0) {
                        DriveRouteResult mDriveRouteResult = result;
                        final DrivePath drivePath = mDriveRouteResult.getPaths().get(0);
                        if (drivePath == null) {
                            return;
                        }
                        int dis = (int) mDriveRouteResult.getPaths().get(0).getDistance();
                        int dur = (int) mDriveRouteResult.getPaths().get(0).getDuration();
                        String data = "距您"+AMapUtil.getFriendlyLength(dis)+" 驾车大约耗时"+AMapUtil.getFriendlyTime(dur);
                        if (mTimerPicker.isShow()){
                            mTimerPicker.showUpdate(data);
                        }
                    } else if (result != null && result.getPaths() == null) {
                    }
                } else {
                }
            } else {
            }
        }

        @Override
        public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
            // walk
        }

        @Override
        public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
            // ride
        }
    };

    /**
     * request all marker LatLng
     *
     * @return
     */
    private List<LatLng> getAllMarkerLatlngData() {
        List<LatLng> list = new ArrayList<>();
        for (int i = 0; i < mTripDetailBean.getLineRouteListVO().size(); i++) {
            if (i == 0){
                if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList() != null) {
                    for (int j = 0; j < mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().size(); j++) {
                        if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getDataType() == 5) { // 集结点
                            if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getStaging().getCoordinateVO() != null) {
                                list.add(new LatLng(mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getStaging().getCoordinateVO().getLatitude(),
                                        mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getStaging().getCoordinateVO().getLongitude())); // 第一天的集结点
                            }
                        }
                    }
                }
            }
            if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList() != null) {
                for (int j = 0; j < mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().size(); j++) {
                    if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getDataType() == 2){ // 景点
                        if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getSpot().getCoordinateVO() != null) {
                            list.add(new LatLng(mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getSpot().getCoordinateVO().getLatitude(),
                                    mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getSpot().getCoordinateVO().getLongitude())); // 景点
                        }
                    }
                    if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getDataType() == 4){ // 住宿
                        if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getHotel().getCoordinateVO() != null) {
                            list.add(new LatLng(mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getHotel().getCoordinateVO().getLatitude(),
                                    mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getHotel().getCoordinateVO().getLongitude())); // 酒店
                        }
                    }
                    if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getDataType() == 8){ // 早餐
                        if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getBreakfast().getCoordinateVO() != null) {
                            list.add(new LatLng(mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getBreakfast().getCoordinateVO().getLatitude(),
                                    mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getBreakfast().getCoordinateVO().getLongitude())); // 酒店
                        }
                    }
                    if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getDataType() == 9){ // 午餐
                        if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getLunch().getCoordinateVO() != null) {
                            list.add(new LatLng(mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getLunch().getCoordinateVO().getLatitude(),
                                    mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getLunch().getCoordinateVO().getLongitude())); // 酒店
                        }
                    }
                    if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getDataType() == 10){ // 晚餐
                        if (mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getDinner().getCoordinateVO() != null) {
                            list.add(new LatLng(mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getDinner().getCoordinateVO().getLatitude(),
                                    mTripDetailBean.getLineRouteListVO().get(i).getDetailVOList().get(j).getDinner().getCoordinateVO().getLongitude())); // 酒店
                        }
                    }
                }
            }
        }
        return list;
    }

    private void setOtherAddMaker() {
        // init marker
        if (mRightInfo == null) {
            return;
        }
        if (mRightInfo.size() < 1) {
            return;
        }
        mAllLatLng.clear();
        for (int i = 0; i < mRightInfo.size(); i++) {
            SharedNavigationMapRightInfo info = mRightInfo.get(i);
            if (info == null || info.getBean() == null) {
                continue;
            }
//            if (!mIsShowCarInfo) {
            final int isss = i;
//                if (!"".equals(mRightInfo.get(i).getBean().avatar) && null != mRightInfo.get(i).getBean().avatar) {
            if (info.getBean().time != 0 &&
                    info.getBean().latitude != 0.0f &&
                    info.getBean().longitude != 0.0f &&
                    !info.getBean().userId.equals(mUserId)) {
                mAllLatLng.add(new LatLng(info.getBean().latitude, info.getBean().longitude));
                GlideApp.with(this).asBitmap().load(mRightInfo.get(i).getBean().avatar)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                setAysnMarkerSurface(isss, info, mRightInfo.size());
//                                        addMyCarAndPersonLocationMarker(
//                                                false,
//                                                mIsShowCarInfo,
//                                                new LatLng(mRightInfo.get(isss).getBean().latitude, mRightInfo.get(isss).getBean().longitude),
//                                                mIsShowCarInfo ? 25 : 15,
//                                                resource,
//                                                getTypeStr(mRightInfo.get(isss).getBean().type),
//                                                mRightInfo.get(isss).getBean().nickName,
//                                                mRightInfo.get(isss).getBean().numberPlate,
//                                                mRightInfo.get(isss).getBean().userId,
//                                                mRightInfo.get(isss).getDistance(),
//                                                mRightInfo.get(isss).getBean().time);
                            }
                        });
            }
//                } else {
//
//                    if (mRightInfo.get(isss).getBean().time != 0 &&
//                            mRightInfo.get(isss).getBean().latitude != 0.0f &&
//                            mRightInfo.get(isss).getBean().longitude != 0.0f &&
//                            !mRightInfo.get(isss).getBean().userId.equals(mUserId)) {
//                        mAllLatLng.add(new LatLng(mRightInfo.get(isss).getBean().latitude, mRightInfo.get(isss).getBean().longitude));
//                        GlideApp.with(this).asBitmap().load(R.drawable.icon_head_default)
//                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .into(new SimpleTarget<Bitmap>() {
//                                    @Override
//                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                                        addMyCarAndPersonLocationMarker(
//                                                false,
//                                                mIsShowCarInfo,
//                                                new LatLng(mRightInfo.get(isss).getBean().latitude, mRightInfo.get(isss).getBean().longitude),
//                                                mIsShowCarInfo ? 25 : 15,
//                                                resource,
//                                                getTypeStr(mRightInfo.get(isss).getBean().type),
//                                                mRightInfo.get(isss).getBean().nickName,
//                                                mRightInfo.get(isss).getBean().numberPlate,
//                                                mRightInfo.get(isss).getBean().userId,
//                                                mRightInfo.get(isss).getDistance(),
//                                                mRightInfo.get(isss).getBean().time);
//                                    }
//                                });
//                    }
//                }
//            } else {
//                if (mRightInfo.get(i).getBean().time != 0 &&
//                        mRightInfo.get(i).getBean().latitude != 0.0f &&
//                        mRightInfo.get(i).getBean().longitude != 0.0f &&
//                        !mRightInfo.get(i).getBean().userId.equals(mUserId)) {
//                    mAllLatLng.add(new LatLng(mRightInfo.get(i).getBean().latitude, mRightInfo.get(i).getBean().longitude));
//                    addMyCarAndPersonLocationMarker(
//                            false,
//                            mIsShowCarInfo,
//                            new LatLng(mRightInfo.get(i).getBean().latitude, mRightInfo.get(i).getBean().longitude),
//                            mIsShowCarInfo ? 65 : 15,
//                            null,
//                            getTypeStr(mRightInfo.get(i).getBean().type),
//                            mRightInfo.get(i).getBean().nickName,
//                            mRightInfo.get(i).getBean().numberPlate,
//                            mRightInfo.get(i).getBean().userId,
//                            mRightInfo.get(i).getDistance(),
//                            mRightInfo.get(i).getBean().time);
//                }
//            }
        }
    }

    private void setAysnMarkerSurface(int isss,SharedNavigationMapRightInfo info,int size){
        GlideApp.with(this).asBitmap().load(TextUtils.isEmpty(info.getBean().avatar) ? R.drawable.icon_head_default : info.getBean().avatar)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        if (size > isss) {
                            addMyCarAndPersonLocationMarker(
                                    false,
                                    mIsShowCarInfo,
                                    new LatLng(info.getBean().latitude, info.getBean().longitude),
                                    mIsShowCarInfo ? 65 : 15,
                                    mIsShowCarInfo ? null : resource,
                                    getTypeStr(info.getBean().type),
                                    info.getBean().nickName,
                                    info.getBean().numberPlate,
                                    info.getBean().userId,
                                    info.getDistance(),
                                    info.getBean().time);
                        }
                    }
                });
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
     * request member type
     *
     * @param content
     * @return
     */
    private String getTypeStr(String content) {
        String info = "成员";
        if (content.contains("成员")) {
            info = mIsShowCarInfo ? " 司机" : "成员";
        } else {
            info = content;
        }
        return info;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case cn.xmzt.www.R.id.back_iv: // return
                finish();
                break;
            case cn.xmzt.www.R.id.iv_mine_location_img: // myLoc
                setMapCenterToMineLocation();
                break;
            case cn.xmzt.www.R.id.right_car_tv: // share car
                mIsShowCarInfo = true;
                mHandler.sendEmptyMessage(HANDLER_UPDATE_MEMBER_INFO);
                break;
            case cn.xmzt.www.R.id.right_person_tv: // show person
                mIsShowCarInfo = false;
                mHandler.sendEmptyMessage(HANDLER_UPDATE_MEMBER_INFO);
                break;
            case cn.xmzt.www.R.id.right_share_tv: // share imageview
                showRightShareContentView();
                break;
            case cn.xmzt.www.R.id.tv_top_go_edit_c: // top edit
                IntentManager.getInstance().goSharedNavigationSettingDestinationActivity(mContext, mTripDetailBean.getLineId(), mBottomDaysPosition == 0 ? 1 : mBottomDaysPosition, REQUEST_CODE, gatherTime);
                break;
            case cn.xmzt.www.R.id.tv_time_update_c: // update staging time
                mIsClickUpdateTime = true;
                settingTimeDialog(destinationLat,destinationLng);
                break;
            case cn.xmzt.www.R.id.rl_no_setting_data: // setting destination
                if (!mIsCaptain) {
                    ToastUtils.showText(SharedNavigationMapActivity.this, "您当前只是成员，不能进行设置时间的操作");
                } else {
                    IntentManager.getInstance().goSharedNavigationSettingDestinationActivity(mContext, mTripDetailBean.getLineId(), mBottomDaysPosition == 0 ? 1 : mBottomDaysPosition, REQUEST_CODE, gatherTime);
                }
                break;
            case cn.xmzt.www.R.id.iv_bottom_to_up: // bottom list show and hide
                if (mIsBottomShow) {
                    setHideBottomView();
                    dataBinding.ivBottomToUp.setImageResource(R.drawable.shared_navigation_bottom_toup_icon);
                } else {
                    setShowBottomView();
                    dataBinding.ivBottomToUp.setImageResource(R.drawable.shared_navigation_bottom_todown_icon);
                }
                break;
            case cn.xmzt.www.R.id.tv_top_go_here_c: // top view go here
                setGoHereOption(destinationLat, destinationLng, destination);
                break;
            case cn.xmzt.www.R.id.tv_top_go_here_p: // top view go here
                setGoHereOption(destinationLat, destinationLng, destination);
                break;
            case cn.xmzt.www.R.id.tv_top_sign_p: // 成员签到界面
                if (mStageId > 0) {
                    IntentManager.getInstance().goTripSignInActivity(mContext, String.valueOf(mStageId));
                } else {
                    ToastUtils.showText(mContext,"集结点编号空");
                }
                break;
            case cn.xmzt.www.R.id.iv_group_info: // 进入群聊天界面
                dataBinding.tvNumNotRead.setVisibility(View.GONE);
                TeamRoomActivity.start(mContext,mGroupId,true,false);
                break;
            case R.id.right_iv: // 顶部右边进入到群内详请
                GroupInfoActivity.start(mContext, mGroupId, "", 0); // 启动固定群资料页
                break;
            case R.id.rl_mine_info: // 右边我的信息的点击事件
                setShowMyMarker();
                break;
            case cn.xmzt.www.R.id.iv_all_show_img: // 全览的功能
                setAllLatLng();
                break;
            case cn.xmzt.www.R.id.iv_guide_info: // 指南
                IntentManager.getInstance().goGuideInfoActivity(mContext,mTripDetailBean.getLineId(),mTripId);
                break;
            case cn.xmzt.www.R.id.iv_sign_list_c: // 签到列表
                if (mStageId > 0) {
                    IntentManager.getInstance().goTripSignInListActivity(mContext, String.valueOf(mStageId));
                } else {
                    ToastUtils.showText(mContext,"集结点编号空");
                }
                break;
            default:
                break;
        }
    }

    private List<LatLng> mAllLatLng = new ArrayList<>(); // 获取所有用户有位置的经纬度

    /**
     * 获取所有点的集合
     */
    private void setAllLatLng(){
        if (mAllLatLng == null){ // 为空处理
            mAllLatLng = new ArrayList<>();
        }
//        if (mCaptainLatLng != null){  // 与产品确定不需要集结点的全览的情况
//            mAllLatLng.add(mCaptainLatLng);
//        }
        mAllLatLng.add(new LatLng(Constants.mLatitude,Constants.mLongitude)); // 所有用户
        // 将所有的点显示到地图界面上
        setMapBounds(mAllLatLng);
    }

    private void setShowMyMarker() {
        // 当前是点击了自己的情况
        if (mCurrentMemMarker != null) {
            mCurrentMemMarker.startAnimation();
            setNotClickedMarkerAnim();
        }
        mCurrentMemMarker = mMyLocationMarker;
        mMyLocationMarker.startAnimation();
        mMyLocationMarker.showInfoWindow();
        mMyLocationMarker.setToTop();// 显示在最顶部
        setClickedMarkerAnim();
        // 隐藏左边信息
        hideRightShareContentView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        TalkManage.isShowSelectGroup = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUpdateProgress();
        dataBinding.mvMap.onResume();
        registerTeamUpdateObserver(true);
//        StartTimer();
        if (TextUtils.isEmpty(mGroupId)){
            return;
        }
        // 获取当前群内信息
        RecentContact mRecentContact = NIMClient.getService(MsgService.class).queryRecentContact(mGroupId, SessionTypeEnum.Team);
        if (mRecentContact != null) {
            if (mRecentContact.getUnreadCount() > 0) {
                dataBinding.tvNumNotRead.setText(mRecentContact.getUnreadCount() + "");
                dataBinding.tvNumNotRead.setVisibility(View.VISIBLE);
            } else {
                dataBinding.tvNumNotRead.setVisibility(View.GONE);
            }
        } else {
            dataBinding.tvNumNotRead.setVisibility(View.GONE);
        }
        // 判断当前显示的群名称跟我现在的时候是一致的
        if (!dataBinding.tvTitleName.getText().toString().equals(Constants.mGroupName) && !TextUtils.isEmpty(Constants.mGroupName)){
            dataBinding.tvTitleName.setText(Constants.mGroupName);
        }
        // 更新右边数据 可能有更改
        setRightMyInfo();
        clearAllMarker();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataBinding.mvMap.onPause();
        EndTimer();
        mIsStartTimer = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TalkManage.isShowSelectGroup = true;
        dataBinding.mvMap.onDestroy();
        registerTeamUpdateObserver(false);
        mHandler.removeMessages(HANDLER_UPDATE_SHARE_LOCATION);
        mHandler.removeMessages(HANDLER_GET_MEMBER_INFO);
        mHandler.removeMessages(HANDLER_UPDATE_MEMBER_INFO);
        // clear all member cache information
        TeamLocationProfile.getInstance().registerObserver(false);
        EventBus.getDefault().unregister(this);
        setOnDestoryNullData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        dataBinding.mvMap.onSaveInstanceState(outState);
    }

    /**
     * set null
     */
    private void setOnDestoryNullData() {
        mAmap.clear();
        mAmap = null;
        mIsBottomShow = false;
        mRouteSearch = null;
        mBottomDaysPosition = 0;
        mTripDaysListInfo = null;
        mIsShowCarInfo = false;
        mTimerPicker = null;
        mIsCaptain = false;
        mIsActivity = false;
        mIsStartTimer = true;
        Constants.mIsOtherLocation = false;
        Constants.mMyNickName = "";
        Constants.mMyCarInfo = "";
        Constants.mGroupName = "";
    }

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
            // judging marker type
            if (LINE_TYPE.equals(marker.getSnippet())) { // line marker
                if (mCurrentMemMarker != null) {
                    if (!LINE_TYPE.equals(mCurrentMemMarker.getSnippet()) && !CAPTAIN_TYPE.equals(mCurrentMemMarker.getSnippet()) && !CAR_TYPE.equals(mCurrentMemMarker.getSnippet())) {
                        mCurrentMemMarker.startAnimation();
                        setNotClickedMarkerAnim();
                        mCurrentMemMarker = null;
                    }
                }
                mCurrentOtherMarker = marker;
                marker.showInfoWindow();
            } else if (CAPTAIN_TYPE.equals(marker.getSnippet())) { // captain marker
                if (mCurrentMemMarker != null) {
                    if (!LINE_TYPE.equals(mCurrentMemMarker.getSnippet()) && !CAPTAIN_TYPE.equals(mCurrentMemMarker.getSnippet()) && !CAR_TYPE.equals(mCurrentMemMarker.getSnippet())) {
                        mCurrentMemMarker.startAnimation();
                        setNotClickedMarkerAnim();
                        mCurrentMemMarker = null;
                    }
                }
                mCurrentOtherMarker = marker;
                marker.showInfoWindow();
            } else if (CAR_TYPE.equals(marker.getSnippet())){
                if (mCurrentMemMarker != null) {
                    if (!LINE_TYPE.equals(mCurrentMemMarker.getSnippet()) && !CAPTAIN_TYPE.equals(mCurrentMemMarker.getSnippet()) && !CAR_TYPE.equals(mCurrentMemMarker.getSnippet())) {
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
                } else { // 我的
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
            return true; // 返回:true 表示点击marker 后marker 不会移动到地图中心；返回false 表示点击marker 后marker 会自动移动到地图中心
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
     * show setting time dialog
     */
    private void settingTimeDialog(double lat,double lng){
        searchRouteResultS(new LatLonPoint(Constants.mLatitude,Constants.mLongitude),new LatLonPoint(lat,lng));
        if (!"".equals(gatherTime) && null != gatherTime) {
            // calculate time
            String time = "";
            if (gatherTime.length() > 12) {
                time = gatherTime;
            } else {
                time = TimeUtil.getYear() + "-" + gatherTime;
            }
            mTimerPicker.show(time, destination,"");
        } else {
            mTimerPicker.show(mStartTime, destination,"");
        }
    }

    /**
     * init setting time dialog
     */
    private void initTimerPicker() {
        long times = System.currentTimeMillis(); //1566564746548 1565387466036
        mStartTime = TimeUtil.long2Str(times, true);
        long time = times + (1000L * 60L * 60L * 24L * 30L * 12L * 3L);
        mEndTime = TimeUtil.long2Str(time, true);

        mTimerPicker = new SharedNavigationDatePickerDialog(this, new SharedNavigationDatePickerDialog.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
//                ToastUtils.showText(SharedNavigationMapActivity.this,"chronometerTimer = "+TimeUtil.long2Str(timestamp, true));
                // get str_type yyyy-MM-dd HH:mm
                String date = TimeUtil.long2Str(timestamp, true);
//                String dateStr = date.substring(5, date.length());
                // update time view
                gatherTime = date;

                if (!TextUtils.isEmpty(m_destination) && !TextUtils.isEmpty(m_destinationCoordinate) && m_destinationLat != 0.0 && m_destinationLng != 0.0){
                    destination = m_destination;
                    destinationCoordinate = m_destinationCoordinate;
                    destinationLat = m_destinationLat;
                    destinationLng = m_destinationLng;

                    m_destination = "";
                    m_destinationCoordinate = "";
                    m_destinationLat = 0.0;
                    m_destinationLng = 0.0;
                }
                saveSettingInfo();
            }
        }, mStartTime, mEndTime);
        // click other return
        mTimerPicker.setCancelable(true);
        // show minutes and second
        mTimerPicker.setCanShowPreciseTime(true);
        mTimerPicker.setScrollLoop(true);
        mTimerPicker.setCanShowAnim(false);
        mTimerPicker.setShowText(false);
        mTimerPicker.setShowYear(false);
    }

//    private void saveSettingInfo() {
//        // request interface save setting destination and time info
//        viewModel.setGroupSettingDestination(mToken, destination, destinationCoordinate, gatherTime, mGroupId);
//    }

    private boolean mIsClickUpdateTime = false;

    private void saveSettingInfo() {
        // request interface save setting destination and time info
        if (mIsClickUpdateTime) {
            mIsClickUpdateTime = false;
            viewModel.setGroupSettingDestination(mToken, null, null, gatherTime, mStageId, mGroupId);
        } else {
            viewModel.setGroupSettingDestination(mToken, destination, destinationCoordinate, gatherTime, null, mGroupId);
        }
    }

    /**
     * right view show
     */
    private void showRightShareContentView() {
        dataBinding.dlSharedNavigationMap.openDrawer(Gravity.RIGHT);
    }

    /**
     * click then close right view
     */
    private void hideRightShareContentView() {
        dataBinding.dlSharedNavigationMap.closeDrawer(Gravity.RIGHT);
    }

    private String getCarInfo() {
        String string = "";
        if (!"".equals(Constants.mMyCarInfo) && null != Constants.mMyCarInfo) {
            string = Constants.mMyCarInfo;
        }
        return string;
    }

    private boolean mMarkerType = false;
    private View mCarView = null;
    private View mPerView = null;
//    private Bitmap mCarViewBitmap = null;
//    private Bitmap mPerViewBitmap = null;

    /**
     * update right data
     */
    private void setShowMarkerIconInfo() {
        if (mMarkerType != mIsShowCarInfo) {
            clearAllMarker();
            mMarkerType = mIsShowCarInfo;
            if (mIsShowCarInfo){
                if (mCarView == null) {
                    mCarView = View.inflate(this, R.layout.marker_shared_navigation_car, null);
//                    mCarViewBitmap = BitmapUtils.convertViewToBitmap(mCarView);
                }
                if (mPerView == null){
                    mPerView = View.inflate(this, R.layout.marker_shared_navigation_person, null);
//                    mPerViewBitmap = BitmapUtils.convertViewToBitmap(mPerView);
                }
                mAmap.setMyLocationStyle(myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromView("".equals(getCarInfo()) ? mPerView : mCarView))); // 表示是车的方向
            } else {
                if (mPerView == null){
                    mPerView = View.inflate(this, R.layout.marker_shared_navigation_person, null);
//                    mPerViewBitmap = BitmapUtils.convertViewToBitmap(mPerView);
                }
                mAmap.setMyLocationStyle(myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromView(mPerView))); // 表示是人的方向
            }
        }
        if (mIsShowCarInfo) { // show car
            dataBinding.rightPersonTv.setVisibility(View.VISIBLE);
            dataBinding.rightCarTv.setVisibility(View.GONE);
            // default start car type
            addMyCarAndPersonLocationMarker(true, mIsShowCarInfo, new LatLng(Constants.mLatitude,Constants.mLongitude), 0, null, getTypeStr(mMyType), nickname, getCarInfo(), mUserId,0.0f,0);
            // update right data
            mRightInfo = viewModel.getRightCarInfo(mMemberInfo_All);
            // add car marker
            setOtherAddMaker();
            mRightAdapter.setDatas(mRightInfo);
            if (mRightInfo != null) {
                if (mRightInfo.size() > 0) {
                    dataBinding.tvContentNum.setText("其他车辆位置（" + mRightInfo.size() + "辆）");
                    dataBinding.rvPersonList.setVisibility(View.VISIBLE);
//                    dataBinding.rlEmpty.setVisibility(View.GONE);
                } else {
                    dataBinding.tvContentNum.setText("其他车辆位置（0辆）");
                    dataBinding.rvPersonList.setVisibility(View.GONE);
//                    dataBinding.rlEmpty.setVisibility(View.VISIBLE);
                }
            } else {
                dataBinding.tvContentNum.setText("其他车辆位置（0辆）");
                dataBinding.rvPersonList.setVisibility(View.GONE);
//                dataBinding.rlEmpty.setVisibility(View.VISIBLE);
            }
        } else {
            dataBinding.rightPersonTv.setVisibility(View.GONE);
            dataBinding.rightCarTv.setVisibility(View.VISIBLE);
            addMyCarAndPersonLocationMarker(true, mIsShowCarInfo, new LatLng(Constants.mLatitude,Constants.mLongitude), 50, null, mMyType, nickname, getCarInfo(), mUserId,0.0f,0);
            mRightInfo = viewModel.getRightPersonInfo(mMemberInfo_All);
            setOtherAddMaker();
            mRightAdapter.setDatas(mRightInfo);
            if (mRightInfo != null) {
                if (mRightInfo.size() > 0) {
                    dataBinding.tvContentNum.setText("其他人员位置（" + mRightInfo.size() + "人）");
                    dataBinding.rvPersonList.setVisibility(View.VISIBLE);
//                    dataBinding.rlEmpty.setVisibility(View.GONE);
                } else {
                    dataBinding.tvContentNum.setText("其他人员位置（0人）");
                    dataBinding.rvPersonList.setVisibility(View.GONE);
//                    dataBinding.rlEmpty.setVisibility(View.VISIBLE);
                }
            } else {
                dataBinding.tvContentNum.setText("其他人员位置（0人）");
                dataBinding.rvPersonList.setVisibility(View.GONE);
//                dataBinding.rlEmpty.setVisibility(View.VISIBLE);
            }
        }
    }

    private List<SharedNavigationMapRightInfo> mRightInfo = null;

    private int mDistance = 10000; // 10Km之外

//    /**
//     *
//     * @param isMy 是否是我自己
//     * @param isCar 是否是车辆
//     * @param latLng 经纬度信息
//     * @param radiusmarker 设置显示偏移量
//     * @param bitmapurl 用户头像
//     * @param type 用户类型 0 群主 1 领队 2 成员
//     * @param name 昵称
//     * @param carinfo 车辆信息
//     * @param userId 用户id
//     * @param distance 距离
//     * @param time 之前时间
//     */
//    private void addMyCarAndPersonLocationMarker(boolean isMy, boolean isCar, LatLng latLng, int radiusmarker, Bitmap bitmapurl, String type, String name, String carinfo, String userId,float distance,long time) {
//        // 在获取之前写逻辑
//        if (isMy) {
//            if (mMyLocationMarker != null) {
//                mMyLocationMarker.setPosition(latLng);
//                return;
//            }
//        } else {
//            if (mAllMarker != null) {
//                if (mAllMarker.size() > 0) {
//                    for (int i = 0; i < mAllMarker.size(); i++) {
//                        String con = userId;
//                        String title = mAllMarker.get(i).getTitle();
//                        String title1 = "";
//                        if (title.contains(",")){
//                            title1 = title.substring(0,title.length()-1);
//                        } else {
//                            title1 = title;
//                        }
//                        if (con.equals(title1)) {
//                            if (distance >= mDistance) { // 当前用户大于10Km
//                                if (!title.contains(",")){
//                                    mAllMarker.get(i).remove();
//                                    mAllMarker.remove(mAllMarker.get(i));
//                                    break;
//                                } else {
//                                    mAllMarker.get(i).setPosition(latLng);
//                                    return;
//                                }
//                            } else { // 当前已经小于10Km
//                                if (title.contains(",")){
//                                    mAllMarker.get(i).remove();
//                                    mAllMarker.remove(mAllMarker.get(i));
//                                    break;
//                                } else {
//                                    mAllMarker.get(i).setPosition(latLng);
//                                    return;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        if (mAmap != null) {
//            View view = null;
//            if (isCar) { // car
//                if ("".equals(carinfo)) { // me
//                    view = View.inflate(this, R.layout.marker_shared_navigation_my_person_item, null);
//                } else {
//                    if (isMy) {
//                        view = View.inflate(this, R.layout.marker_shared_navigation_car_mine_item, null);
//                    } else {
//                        view = View.inflate(this, R.layout.marker_shared_navigation_car_other_item, null);
//                    }
//                }
//            } else { // person
//                if (isMy) {
//                    view = View.inflate(this, R.layout.marker_shared_navigation_my_person_item, null);
//                } else {
//                    view = View.inflate(this, R.layout.marker_shared_navigation_person_item, null);
//                }
//            }
//            TextView tvNickName = (TextView) view.findViewById(cn.xmzt.www.R.id.maker_nick_name_tv);
//            TextView tvType = (TextView) view.findViewById(cn.xmzt.www.R.id.maker_type_tv);
//            TextView tvCar = null;
//            if (isCar) {
//                if (!"".equals(carinfo)) {
//                    tvCar = (TextView) view.findViewById(cn.xmzt.www.R.id.maker_car_info_tv);
//                }
//            }
//            ImageView iv_hint = null;
//            if (!isMy){
//                iv_hint = (ImageView) view.findViewById(cn.xmzt.www.R.id.iv_hint);
//                if (distance >= mDistance){
//                    iv_hint.setVisibility(View.VISIBLE);
//                } else {
//                    iv_hint.setVisibility(View.GONE);
//                }
//            }
//            ImageView ivBottomBg = (ImageView) view.findViewById(cn.xmzt.www.R.id.maker_share_navigation_bg_iv);
//            RoundImageView ivheadImg = (RoundImageView) view.findViewById(cn.xmzt.www.R.id.maker_head_icon);
//            tvNickName.setText(name);
//            if (type.contains("成员")) {
//                tvType.setBackgroundResource(R.drawable.marker_shared_navigation_map_captain_info_bg);
//            } else if (type.contains("司机")) {
//                tvType.setBackgroundResource(R.drawable.marker_shared_navigation_map_captain_info_bg);
//            } else {
//                tvType.setBackgroundResource(R.drawable.marker_shared_navigation_map_captain_leander_info_bg);
//            }
//            tvType.setText(type);
//            if (isCar) {
//                if (!"".equals(carinfo)) {
//                    tvCar.setText(carinfo);
//                }
//            }
//            if (!isCar) {
//                if (isMy) {
//                    ivBottomBg.setImageResource(R.drawable.shared_navigation_map_marker_mine_bottom_bg);
//                } else {
//                    ivBottomBg.setImageResource(R.drawable.shared_navigation_map_marker_bottom_notclick_bg);
//                }
//            }
//            // judging show myLoc
//            if (!isCar) {
//                if (isMy) {
//                    ivheadImg.setImageResource(R.drawable.marker_shared_navigation_not_click_mine_icon);
//                } else {
//                    if (isCar) { // judging is car type
//                        ivheadImg.setImageResource(R.drawable.marker_shared_navigation_not_click_car_icon);
//                    } else {
//                        ivheadImg.setImageBitmap(bitmapurl);
//                    }
//                }
//            }
//            Bitmap bitmap = BitmapUtils.convertViewToBitmap(view);
//            int radius = DensityUtil.dip2px(this, radiusmarker);
//            if (isMy) {
//                latLng = new LatLng(Constants.mLatitude, Constants.mLongitude);
//            }
//            MarkerOptions markerOption = null;
//            if (isMy) {
//                if (!"".equals(carinfo)) { // 车辆信息不为空的情况下
//                    markerOption = new MarkerOptions()
//                            .position(latLng)
//                            .anchor(0.5f, isCar ? 1.0f : 0.915f)
//                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
//                } else { // 车辆信息为空的情况下 人员
//                    markerOption = new MarkerOptions()
//                            .position(latLng)
//                            .anchor(0.5f, 0.915f)
//                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
//                }
//            } else { // 非我
//                markerOption = new MarkerOptions()
//                        .position(latLng)
//                        .anchor(0.5f, isCar ? 0.77f : 0.86f)
//                        .setInfoWindowOffset(0, radius)
//                        .snippet(isCar ? CAR_TYPE : PERSON_TYPE)
//                        .title((distance >= mDistance) ? userId+"," : userId) // 在超过10Km的title中添加一个逗号
//                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
//            }
//            if (isMy) {
//                mMyLocationMarker = mAmap.addMarker(markerOption);
//                mMyLocationMarker.setClickable(false);
//            } else {
//                if (!isCar) {
//                    Marker marker = mAmap.addMarker(markerOption);
//                    Animation markerAnimation = new ScaleAnimation(1.0f, 1.6f, 1.0f, 1.6f);
//                    markerAnimation.setDuration(0);
//                    markerAnimation.setFillMode(1);
//                    marker.setAnimation(markerAnimation);
//                    marker.setClickable(true);
//                    mAllMarker.add(marker);
//                } else {
//                    Marker marker = mAmap.addMarker(markerOption);
//                    marker.setClickable(true);
//                    mAllMarker.add(marker);
//                }
//            }
//        }
//    }

    /**
     *
     * @param isMy 是否是我自己
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
    private void addMyCarAndPersonLocationMarker(boolean isMy, boolean isCar, LatLng latLng, int radiusmarker, Bitmap bitmapurl, String type, String name, String carinfo, String userId,float distance,long time) {
        long update  = System.currentTimeMillis() - time;
        // 在获取之前写逻辑
        if (isMy) {
            if (mMyLocationMarker != null) { // 我的位置不进行做其他操作 只需要更新数据
                mMyLocationMarker.setPosition(latLng);
                return;
            }
        } else {
            if (mAllMarker != null) {
                if (mAllMarker.size() > 0) {
                    for (int i = 0; i < mAllMarker.size(); i++) {
                        String con = userId;
                        String title = mAllMarker.get(i).getTitle();
                        String title1 = "";
                        if (title.contains(",.")){ // 表示距离大于10Km 并且离线的用户
                            title1 = title.substring(0,title.length()-2);
                        } else if (title.contains(",")){ // 表示距离大于10Km的情况
                            title1 = title.substring(0,title.length()-1);
                        } else if (title.contains(".")){ // 表示用户离线的情况
                            title1 = title.substring(0,title.length()-1);
                        } else { // 表示用户在线也没有大于10Km的情况
                            title1 = title;
                        }
                        if (con.equals(title1)) { // 判断是否是当前的marker是否是我需要的marker 是的话进行需要的操作显示
                            // 当前用户是否是大于10Km 并且离线的情况
                            if (distance >= mDistance && update >= TIME_OUT){
                                if (!title.contains(",.")){ // 之前就是大于10Km 并且离线的情况
                                    mAllMarker.get(i).remove();
                                    mAllMarker.remove(mAllMarker.get(i));
                                    break;
                                } else { // 不是这样的情况需要移除掉然后在重新添加
                                    mAllMarker.get(i).setPosition(latLng);
                                    return;
                                }
                            } else if (distance >= mDistance && update < TIME_OUT){ // 当前用户是距离大于10Km的情况 并且在线的情况下
                                if (!title.contains(",") || title.contains(".")){ // 表示的是之前没有大于10Km的情况 现在表示大于10Km的情况
                                    mAllMarker.get(i).remove();
                                    mAllMarker.remove(mAllMarker.get(i));
                                    break;
                                } else {
                                    mAllMarker.get(i).setPosition(latLng);
                                    return;
                                }
                            } else if (distance < mDistance && update >= TIME_OUT){ // 小于10Km 并且离线的情况
                                if (title.contains(",") || !title.contains(".")){
                                    mAllMarker.get(i).remove();
                                    mAllMarker.remove(mAllMarker.get(i));
                                    break;
                                } else {
                                    mAllMarker.get(i).setPosition(latLng);
                                    return;
                                }
                            } else if (distance < mDistance && update < TIME_OUT){ // 在线的情况 并且距离小于10Km
                                if (title.contains(",") || title.contains(".")){
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
        }
        if (mAmap != null) {
            View view = null;
            if (isCar) { // car
                if ("".equals(carinfo)) { // me
                    view = View.inflate(this, R.layout.marker_shared_navigation_my_person_item, null);
                } else {
                    if (isMy) {
                        view = View.inflate(this, R.layout.marker_shared_navigation_car_mine_item, null);
                    } else {
                        view = View.inflate(this, R.layout.marker_shared_navigation_car_other_item, null);
                    }
                }
            } else { // person
                if (isMy) {
                    view = View.inflate(this, R.layout.marker_shared_navigation_my_person_item, null);
                } else {
                    view = View.inflate(this, R.layout.marker_shared_navigation_person_item, null);
                }
            }
            TextView tvNickName = (TextView) view.findViewById(cn.xmzt.www.R.id.maker_nick_name_tv); // 用户昵称的显示
            TextView tvType = (TextView) view.findViewById(cn.xmzt.www.R.id.maker_type_tv); // 用户类型的显示问题
            TextView tvCar = null; // 是车辆的情况 车辆的显示
            ImageView iv_car_bg = null; // 是车辆的情况 车辆的图片显示
            if (isCar) { // 车辆
                tvCar = (TextView) view.findViewById(cn.xmzt.www.R.id.maker_car_info_tv);
                if (!isMy){ // 非我情况
                    iv_car_bg = view.findViewById(R.id.iv_bg);
                    if (update >= TIME_OUT){ // 当前车辆离线情况
                        iv_car_bg.setBackgroundResource(R.drawable.map_marker_offline_car_head_icon);
                        tvCar.setTextColor(Color.parseColor("#ffffff"));
                        tvCar.setBackgroundResource(R.drawable.marker_shared_navigation_map_car_info_offline_bg);
                    } else {
                        iv_car_bg.setBackgroundResource(R.drawable.shared_navigation_car_other);
                        tvCar.setTextColor(Color.parseColor("#333333"));
                        tvCar.setBackgroundResource(R.drawable.marker_shared_navigation_map_car_info_bg);
                    }
                }
                if (!"".equals(carinfo)) {
                    tvCar.setText(carinfo);
                    tvCar.setTextColor(Color.parseColor("#333333"));
                    tvCar.setBackgroundResource(R.drawable.marker_shared_navigation_map_car_info_bg);
                }
            }
            ImageView iv_hint = null;
            if (!isMy){
                iv_hint = (ImageView) view.findViewById(cn.xmzt.www.R.id.iv_hint);
                if (distance >= mDistance && update < TIME_OUT){
                    iv_hint.setVisibility(View.VISIBLE);
                } else {
                    iv_hint.setVisibility(View.GONE);
                }
            }
            ImageView ivBottomBg = (ImageView) view.findViewById(cn.xmzt.www.R.id.maker_share_navigation_bg_iv);
            RoundImageView ivheadImg = (RoundImageView) view.findViewById(cn.xmzt.www.R.id.maker_head_icon);
            tvNickName.setText(name);
            if (update >= TIME_OUT && !isMy) {
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
                if (isMy) {
                    ivBottomBg.setImageResource(R.drawable.shared_navigation_map_marker_mine_bottom_bg);
                } else {
                    ivBottomBg.setImageResource(R.drawable.shared_navigation_map_marker_bottom_notclick_bg);
                }
            }
            // judging show myLoc
            if (!isCar) {
                if (isMy) {
                    ivheadImg.setImageResource(R.drawable.marker_shared_navigation_not_click_mine_icon);
                } else {
                    if (isCar) { // judging is car type
                        ivheadImg.setImageResource(R.drawable.marker_shared_navigation_not_click_car_icon);
                    } else {
                        if (update >= TIME_OUT){
                            ivheadImg.setImageResource(R.drawable.map_marker_offline_person_head_icon);
                        } else {
                            ivheadImg.setImageBitmap(bitmapurl);
                        }
                    }
                }
            }
            Bitmap bitmap = BitmapUtils.convertViewToBitmap(view);
            int radius = DensityUtil.dip2px(this, radiusmarker);
            if (isMy) {
                latLng = new LatLng(Constants.mLatitude, Constants.mLongitude);
            }
            MarkerOptions markerOption = null;
            if (isMy) {
                if (!"".equals(carinfo)) { // 车辆信息不为空的情况下
                    markerOption = new MarkerOptions()
                            .position(latLng)
                            .anchor(0.5f, isCar ? 1.0f : 0.915f)
                            .icon(BitmapDescriptorFactory.fromView(view));
                } else { // 车辆信息为空的情况下 人员
                    markerOption = new MarkerOptions()
                            .position(latLng)
                            .anchor(0.5f, 0.915f)
                            .icon(BitmapDescriptorFactory.fromView(view));
                }
            } else { // 非我
                String data = userId;
                if (distance >= mDistance && update >= TIME_OUT){
                    data += ",.";
                } else if (distance >= mDistance){
                    data += ",";
                } else if (update >= TIME_OUT){
                    data += ".";
                }
                markerOption = new MarkerOptions()
                        .position(latLng)
                        .anchor(0.5f, isCar ? 0.77f : 0.86f)
                        .setInfoWindowOffset(0, radius)
                        .snippet(isCar ? CAR_TYPE : PERSON_TYPE)
                        .title(data) // 在超过10Km的title中添加一个逗号
                        .icon(BitmapDescriptorFactory.fromView(view));
            }
            if (isMy) {
                mMyLocationMarker = mAmap.addMarker(markerOption);
                Animation markerAnimation = new ScaleAnimation(1.0f, 1.6f, 1.0f, 1.6f);
                markerAnimation.setDuration(0);
                markerAnimation.setFillMode(1);
                mMyLocationMarker.setAnimation(markerAnimation);
                mMyLocationMarker.setClickable(true);
            } else {
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
    }

    /**
     * bottom day list click
     *
     * @param position
     * @param type
     */
    public void OnBottomListClickListener(int position, int type) {
        if (type == 0) {
            if (position != mBottomDaysPosition) {
                mTripDaysListInfo.get(mBottomDaysPosition).setIs_Checked(false);
                mTripDaysListInfo.get(position).setIs_Checked(true);
                mDaysAdapter.notifyItemChanged(mBottomDaysPosition);
                mDaysAdapter.notifyItemChanged(position);
                mBottomDaysPosition = position;
                dataBinding.rvTripList.smoothScrollToPosition(position == 0 ? mTripListAdapter.day[0] : mTripListAdapter.day[position-1]);
                mCenterLayoutManager.smoothScrollToPosition(dataBinding.rvDaysInfo, new RecyclerView.State(), position);
                // Click on the day to display the day's route
                showDaysLinesInfo();
            }
        }
    }

    /**
     * clear all Marker
     */
    private void clearAllMarker() {
        for (int i = 0; i < mAllMarker.size(); i++) {
            mAllMarker.get(i).remove();
        }
        if (mMyLocationMarker != null) {
            mMyLocationMarker.remove();
            mMyLocationMarker = null;
        }
        mAllMarker.clear();
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
                    infoContent = getLayoutInflater().inflate(R.layout.infowindow_shared_navigation_car_offine_item, null);
                } else {
                    infoContent = getLayoutInflater().inflate(R.layout.infowindow_shared_navigation_car_item1, null);
                }
            } else if (PERSON_TYPE.equals(marker.getSnippet())) {
                if (marker.getTitle().contains(".")){ // 离线的情况显示的
                    infoContent = getLayoutInflater().inflate(R.layout.infowindow_shared_navigation_person_offline_item, null);
                } else {
                    infoContent = getLayoutInflater().inflate(R.layout.infowindow_shared_navigation_person_item1, null);
                }
            } else if (CAPTAIN_TYPE.equals(marker.getSnippet())) {
                infoContent = getLayoutInflater().inflate(R.layout.infowindow_shared_navigation_person_click, null);
            } else if (LINE_TYPE.equals(marker.getSnippet())) {
                if (mIsCaptain) {
                    infoContent = getLayoutInflater().inflate(R.layout.infowindow_shared_navigation_captain_click, null);
                } else {
                    infoContent = getLayoutInflater().inflate(R.layout.infowindow_shared_navigation_person_click, null);
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
                    infoContent = getLayoutInflater().inflate(R.layout.infowindow_shared_navigation_car_offine_item, null);
                } else {
                    infoContent = getLayoutInflater().inflate(R.layout.infowindow_shared_navigation_car_item1, null);
                }
            } else if (PERSON_TYPE.equals(marker.getSnippet())) {
                if (marker.getTitle().contains(".")){ // 离线的情况显示的
                    infoContent = getLayoutInflater().inflate(R.layout.infowindow_shared_navigation_person_offline_item, null);
                } else {
                    infoContent = getLayoutInflater().inflate(R.layout.infowindow_shared_navigation_person_item1, null);
                }
            } else if (CAPTAIN_TYPE.equals(marker.getSnippet())) {
                infoContent = getLayoutInflater().inflate(R.layout.infowindow_shared_navigation_person_click, null);
            } else if (LINE_TYPE.equals(marker.getSnippet())) {
                if (mIsCaptain) {
                    infoContent = getLayoutInflater().inflate(R.layout.infowindow_shared_navigation_captain_click, null);
                } else {
                    infoContent = getLayoutInflater().inflate(R.layout.infowindow_shared_navigation_person_click, null);
                }
            }
            render(marker, infoContent);
            return infoContent;
        }

        private void render(Marker marker, View view) {
            if (LINE_TYPE.equals(marker.getSnippet())) { // line type
                if (mIsCaptain) {
                    ImageView infowindow_captain_title_cancel_iv = view.findViewById(R.id.infowindow_captain_title_cancel_iv);
                    ImageView iv_setting_destination_go_here = view.findViewById(R.id.iv_setting_destination_go_here);
                    ImageView iv_setting_destination_next = view.findViewById(R.id.iv_setting_destination_next);
                    TextView infowindow_captain_title_tv = view.findViewById(R.id.infowindow_captain_title_tv);

                    String title = marker.getTitle();
                    SharedNavigationMapLineBean bean = null;
                    if (title.contains(",")) {
                        int index = title.indexOf(",");
                        int position = Integer.parseInt(title.substring(0, index));
                        if (mBottomDaysPosition == 0) {
                            bean = mDestinationLatLngAllList.get(position);
                        } else {
                            bean = mDestinationLatLngList.get(position);
                        }
                    }
                    String content = bean.getContent();
                    infowindow_captain_title_tv.setText(content);
                    iv_setting_destination_go_here.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setGoHereOption(marker.getPosition().latitude, marker.getPosition().longitude, content);
                        }
                    });
                    infowindow_captain_title_cancel_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // cancal
                            setLineCancel(marker);
                        }
                    });
                    iv_setting_destination_next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // setting next destination
                            setSettingNextDestination(content, marker.getPosition().latitude, marker.getPosition().longitude);
                            marker.hideInfoWindow();
                        }
                    });
                } else {
                    ImageView infowindow_go_here_btn = view.findViewById(R.id.infowindow_go_here_btn);
                    TextView infowindow_title_tv = view.findViewById(R.id.infowindow_title_tv);
                    String title = marker.getTitle();
                    SharedNavigationMapLineBean bean = null;
                    if (title.contains(",")) {
                        int index = title.indexOf(",");
                        int position = Integer.parseInt(title.substring(0, index));
                        if (mBottomDaysPosition == 0) {
                            bean = mDestinationLatLngAllList.get(position);
                        } else {
                            bean = mDestinationLatLngList.get(position);
                        }
                    }
                    String content = bean.getContent();
                    infowindow_title_tv.setText(content);

                    infowindow_go_here_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setGoHereOption(marker.getPosition().latitude, marker.getPosition().longitude, content);
                        }
                    });
                }
            } else if (CAPTAIN_TYPE.equals(marker.getSnippet())) {
                ImageView infowindow_go_here_btn = view.findViewById(R.id.infowindow_go_here_btn);
                TextView infowindow_title_tv = view.findViewById(R.id.infowindow_title_tv);

                infowindow_title_tv.setText(destination);

                infowindow_go_here_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setGoHereOption(marker.getPosition().latitude, marker.getPosition().longitude, destination);
                    }
                });
            } else {
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
                    TextView tv_go_here = view.findViewById(R.id.tv_go_here);
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
                            break;
                        }
                    }
                    if (CAR_TYPE.equals(marker.getSnippet())) { // judging is car
                        iv_user_head_img = view.findViewById(R.id.iv_user_head_img); // car
                        GlideUtil.setImageCircle(SharedNavigationMapActivity.this, bean.getBean().avatar, iv_user_head_img, R.drawable.icon_default);
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
                    tv_update_time.setText(TimeUtil.updateTime(bean.getBean().time));
                    tv_go_here.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setGoHereOption(marker.getPosition().latitude, marker.getPosition().longitude, "");
                        }
                    });
                }
            }
        }
    };

    /**
     * setting go here
     */
    private void setGoHereOption(double lat, double lng, String locStr) {
        if (ActivityUtils.isActivityExistsInStack(SharedNavigationGuideActivityBackUp.class)) {
            IntentManager.getInstance().goSharedNavigationGuideActivityBackUp(SharedNavigationMapActivity.this,true,0.0,0.0,0.0,0.0,"",0);
        } else {
            IntentManager.getInstance().goSharedNavigationGoHereActivityBackUp(SharedNavigationMapActivity.this, lat, lng, locStr,mGroupId);
        }
    }

    /**
     * setting cancel
     */
    private void setLineCancel(Marker marker) {
        // hide infowindow view
        marker.hideInfoWindow();
    }

    private String m_destination = "";
    private String m_destinationCoordinate = "";
    private double m_destinationLat = 0.0;
    private double m_destinationLng = 0.0;

    /**
     * setting next destination option
     */
    private void setSettingNextDestination(String content, double lat, double lng) {
        // 需要中间值
        m_destination = content;
        m_destinationCoordinate = lng + "," + lat;
        m_destinationLat = lat;
        m_destinationLng = lng;
        settingTimeDialog(m_destinationLat,m_destinationLng);
    }

    /**
     * myLoc click
     */
    private void setMapCenterToMineLocation() {
        if (mAmap != null) {
            mAmap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(Constants.mLatitude, Constants.mLongitude)));
            mAmap.moveCamera(CameraUpdateFactory.zoomTo(mInitMapZoom));
        }
    }

    private List<MapStartEndLatLngBean> mStartEndLatLng = new ArrayList<>();

    private void initSurfacePolyAllLine() {
        List<LatLng> list = new ArrayList<>();
//        List<LatLonPoint> list = new ArrayList<>(); // TODO
        for (int i = 0; i < mDestinationLatLngAllList.size(); i++) {
            list.add(mDestinationLatLngAllList.get(i).getBeans());
//            list.add(new LatLonPoint(mDestinationLatLngAllList.get(i).getBeans().latitude,mDestinationLatLngAllList.get(i).getBeans().longitude)); // TODO
            addLineMarkersToMap(this, mDestinationLatLngAllList.get(i).getBeans(), mDestinationLatLngAllList.get(i).getTitle(),
                    getMarkerTypeIcon(mDestinationLatLngAllList.get(i).getType()),
                    i);
        }
        mStartEndLatLng.clear();
        for (int j = 0; j < list.size() - 1; j++) {
            mStartEndLatLng.add(new MapStartEndLatLngBean(new LatLonPoint(list.get(j).latitude, list.get(j).longitude), new LatLonPoint(list.get(j + 1).latitude, list.get(j + 1).longitude)));
//            mStartEndLatLng.add(new MapStartEndLatLngBean(new LatLonPoint(list.get(j).getLatitude(), list.get(j).getLongitude()), new LatLonPoint(list.get(j+1).getLatitude(), list.get(j+1).getLongitude())));// TODO
        }
        for (int j = 0; j < list.size() - 1; j++) {
            searchRouteResult(new LatLonPoint(list.get(j).latitude, list.get(j).longitude), new LatLonPoint(list.get(j + 1).latitude, list.get(j + 1).longitude));
//            searchRouteResult(new LatLonPoint(list.get(j).getLatitude(), list.get(j).getLongitude()), new LatLonPoint(list.get(list.size()-1).getLatitude(), list.get(list.size()-1).getLongitude()),list);// TODO
        }
        // update map zoom
        setMapBounds(list);
    }

    private void SurfacePolyAllLine() {
        // Clear all marker information on the interface
        clearAllLineMarker();
        List<LatLng> list = new ArrayList<>();
        for (int i = 0; i < mDestinationLatLngAllList.size(); i++) {
            list.add(mDestinationLatLngAllList.get(i).getBeans());
        }

        initSurfacePolyAllLine();
        // update map zoom
        setMapBounds(list);
    }

    private int getMarkerTypeIcon(int type){
        int res = 0;
        switch (type){
            case 0: // 景点
                res = R.drawable.shared_navigation_scenic_spot_marker_icon;
                break;
            case 1: // 住宿
                res = R.drawable.shared_navigation_live_marker_icon;
                break;
            case 2: // 餐点
                res = R.drawable.shared_navigation_eat_marker_icon;
                break;
            case 3: // 集结点
                res = R.drawable.shared_navigation_staging_marker_icon;
                break;
            default:
                res = R.drawable.shared_navigation_scenic_spot_marker_icon;
                break;
        }
        return res;
    }

    private void SurfacePolyDaysLine(int day) {
        // Clear all marker information on the interface
        clearAllLineMarker();
        List<LatLng> list = new ArrayList<>();
        if (day == 1){
            SharedNavigationMapLinesBean bean = mDestinationList.get(day - 1);
            mDestinationLatLngList.clear();
            for (int p = 0; p < bean.getBean().size(); p++) {
                mDestinationLatLngList.add(bean.getBean().get(p));
                list.add(bean.getBean().get(p).getBeans());
                addLineMarkersToMap(this, bean.getBean().get(p).getBeans(), bean.getBean().get(p).getTitle(),
                        getMarkerTypeIcon(bean.getBean().get(p).getType()),
                        p);
            }
        } else {
            SharedNavigationMapLinesBean beanfore = mDestinationList.get(day - 2);
            mDestinationLatLngList.clear();
            int position = 0;
            int len = beanfore.getBean().size()-1;
            list.add(beanfore.getBean().get(len).getBeans());
            addLineMarkersToMap(this, beanfore.getBean().get(len).getBeans(), beanfore.getBean().get(len).getTitle(),
                    getMarkerTypeIcon(beanfore.getBean().get(len).getType()),
                    position);
            mDestinationLatLngList.add(beanfore.getBean().get(len));
            SharedNavigationMapLinesBean bean = mDestinationList.get(day - 1);
            for (int p = 0; p < bean.getBean().size(); p++) {
                position += 1;
                mDestinationLatLngList.add(bean.getBean().get(p));
                list.add(bean.getBean().get(p).getBeans());
                addLineMarkersToMap(this, bean.getBean().get(p).getBeans(), bean.getBean().get(p).getTitle(),
                        getMarkerTypeIcon(bean.getBean().get(p).getType()),
                        position);
            }
        }
        for (int j = 0; j < list.size() - 1; j++) {
            searchRouteResult(new LatLonPoint(list.get(j).latitude, list.get(j).longitude), new LatLonPoint(list.get(j + 1).latitude, list.get(j + 1).longitude));
        }
        // update map zoom
        setMapBounds(list);
    }

    /**
     * clear all Marker
     */
    private void clearAllLineMarker() {
        for (int i = 0; i < mAllLineMarker.size(); i++) {
            mAllLineMarker.get(i).remove();
        }
        mAllLineMarker.clear();
        for (int i = 0; i < mRouteLines.size(); i++) {
            mRouteLines.get(i).removeFromMap();
        }
        mRouteLines.clear();
        mAllLineMarker.clear();
    }

    /**
     * line marker
     */
    public void addLineMarkersToMap(Context context, LatLng latlng, String title, int res, int position) {
        if (mAmap != null) {
            View view = View.inflate(context, R.layout.marker_share_navigation_line_item, null);
            ImageView imageView = view.findViewById(cn.xmzt.www.R.id.maker_share_navigation_bg_iv);
            TextView textView = view.findViewById(cn.xmzt.www.R.id.tv_content);
            if ("".equals(title) || title == null) {
                return;
            }
            textView.setText(title);
            imageView.setImageResource(res);
            int radius = DensityUtil.dip2px(this, 10);
            Bitmap bitmap = BitmapUtils.convertViewToBitmap(view);
            MarkerOptions markerOption = new MarkerOptions()
                    .position(latlng)
                    .title(position + "," + title)
                    .snippet(LINE_TYPE)
                    .setInfoWindowOffset(0, radius)
                    .anchor(0.5f, 0.78f)
                    .icon(BitmapDescriptorFactory.fromView(view));
            Marker marker = mAmap.addMarker(markerOption);
            // save line marker
            mAllLineMarker.add(marker);
        }
    }

    /**
     * add captain marker
     */
    public void addCaptainMarkersToMap(Context context, LatLng latlng) {
//        if (mDestinationMarker != null) {
//            mDestinationMarker.remove();
//        }
//        if (mAmap != null) {
//            View view = View.inflate(context, R.layout.marker_shared_navigation_captain_item, null);
//            int radius = DensityUtil.dip2px(this, 10);
//            Bitmap bitmap = BitmapUtils.convertViewToBitmap(view);
//            MarkerOptions markerOption = new MarkerOptions()
//                    .position(latlng)
//                    .title("-1")
//                    .snippet(CAPTAIN_TYPE)
//                    .setInfoWindowOffset(0, radius)
//                    .anchor(0.5f, 0.73f)
//                    .icon(BitmapDescriptorFactory.fromView(view));
//            mDestinationMarker = mAmap.addMarker(markerOption);
//        }
    }

    /**
     * start search line
     */
    public void searchRouteResult(LatLonPoint mStartPoint, LatLonPoint mEndPoint) {
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartPoint, mEndPoint);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_SHORTEST, null, null, "");
        mRouteSearch.calculateDriveRouteAsyn(query);
    }

    /**
     * 使用途经点绘制
     */
    public void searchRouteResult(LatLonPoint mStartPoint, LatLonPoint mEndPoint, List<LatLonPoint> list) { // TODO
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartPoint, mEndPoint);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_SHORTEST, list, null, "");
        mRouteSearch.calculateDriveRouteAsyn(query);
    }

    private List<SharedHomeDrivingRouteOverlay> mRouteLines = new ArrayList<>();

    /**
     * search line listener
     */
    RouteSearch.OnRouteSearchListener mRouteSearchListener = new RouteSearch.OnRouteSearchListener() {
        @Override
        public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
            // bus
        }

        @Override
        public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
            // driving
            if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                if (result != null && result.getPaths() != null) {
                    if (result.getPaths().size() > 0) {
                        DriveRouteResult mDriveRouteResult = result;
                        final DrivePath drivePath = mDriveRouteResult.getPaths().get(0);
                        if (drivePath == null) {
                            return;
                        } // 需要进行判断当前缓存信息的问题 TODO
                        SharedHomeDrivingRouteOverlay drivingRouteOverlay = new SharedHomeDrivingRouteOverlay(
                                SharedNavigationMapActivity.this, mAmap, drivePath,
                                mDriveRouteResult.getStartPos(),
                                mDriveRouteResult.getTargetPos(), null);
                        drivingRouteOverlay.setNodeIconVisibility(false);
                        drivingRouteOverlay.setIsColorfulline(false); // TODO
                        drivingRouteOverlay.removeFromMap();
                        drivingRouteOverlay.addToMap();
//                        drivingRouteOverlay.zoomToSpan();
                        mRouteLines.add(drivingRouteOverlay);
                        // 判断当前是否是哪一个的信息
                        LatLonPoint start = mDriveRouteResult.getDriveQuery().getFromAndTo().getFrom();
                        LatLonPoint end = mDriveRouteResult.getDriveQuery().getFromAndTo().getTo();
                        int position = -1;
                        for (int i = 0; i < mStartEndLatLng.size(); i++) {
                            if (mStartEndLatLng.get(i).getStart().getLatitude() == start.getLatitude() &&
                                    mStartEndLatLng.get(i).getStart().getLongitude() == start.getLongitude() &&
                                    mStartEndLatLng.get(i).getEnd().getLatitude() == end.getLatitude() &&
                                    mStartEndLatLng.get(i).getEnd().getLongitude() == end.getLongitude()) {
                                // 获取当前的position
                                position = i;
                                break;
                            }
                        }
                        // 获取到当前绘制的路线位置 将路线位置添加到数据上面
                        if (position == -1) {
                            return;
                        }
                        int desPosition = 0;
                        for (int all = 0; all < mDestinationList.size(); all++) {
                            for (int p = 0; p < mDestinationList.get(all).getBean().size(); p++) {
                                if (desPosition == position) {
                                    mDestinationList.get(all).getBean().get(p).setHomeDrivingRouteOverlay(drivingRouteOverlay);
                                    break;
                                }
                                desPosition += 1;
                            }
                            if (desPosition == position) {
                                break;
                            }
                        }
                    } else if (result != null && result.getPaths() == null) {
                    }
                } else {
                }
            } else {
            }
        }

        @Override
        public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
            // walk
        }

        @Override
        public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
            // ride
        }
    };

    /**
     * include marker show zoom
     *
     * @param LatLngs
     */
    private void setMapBounds(List<LatLng> LatLngs) {
        LatLngBounds.Builder latlngBuilder = LatLngBounds.builder();
        for (LatLng latLng : LatLngs) {
            latlngBuilder.include(latLng);
        }
        LatLngBounds bounds = latlngBuilder.build();
        mAmap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 40));
    }

    /**
     * show bottom trip detail list anim
     */
    private void setShowBottomView() {
        android.view.animation.Animation animBottomOut = AnimationUtils.loadAnimation(SharedNavigationMapActivity.this, R.anim.map_list_bottom_in);
        animBottomOut.setDuration(240);
        dataBinding.rvTripList.setVisibility(View.VISIBLE);
        dataBinding.rvTripList.startAnimation(animBottomOut);
        mIsBottomShow = true;
    }

    /**
     * hide bottom trip detail list anim
     */
    private void setHideBottomView() {
        android.view.animation.Animation animBottomOut = AnimationUtils.loadAnimation(SharedNavigationMapActivity.this, R.anim.map_list_bottom_out);
        animBottomOut.setDuration(240);
        dataBinding.rvTripList.setVisibility(View.GONE);
        dataBinding.rvTripList.startAnimation(animBottomOut);
        mIsBottomShow = false;
    }

    /**
     * right adapter click
     *
     * @param position click position
     * @param userid   userId
     * @param type     0 list click 1 head click
     */
    public void OnTypeClickListener(int position, String userid, int type) {
        // head or list item click option
        if (type == 0) {
            // request current position LatLng
            if (mRightInfo.get(position).getBean().latitude == 0.0 && mRightInfo.get(position).getBean().longitude == 0.0){
                ToastUtils.showText(mContext,"当前用户无位置信息");
                return;
            }
            // request current position LatLng
            LatLng latLng = new LatLng(mRightInfo.get(position).getBean().latitude, mRightInfo.get(position).getBean().longitude);
            setMapCenterLocation(latLng.latitude, latLng.longitude);
            //   遍历当前所有的人员marker  找到符合要求的  显示InfoWindow
            for (int i = 0; i < mAllMarker.size(); i++){
                Marker marker = mAllMarker.get(i);
                if (latLng.longitude == marker.getPosition().longitude && latLng.latitude == marker.getPosition().latitude){
                    // 表示是当前坐标
                    if (marker.getSnippet().contains(PERSON_TYPE)){
                        if (PERSON_TYPE.equals(marker.getSnippet())) {
                            if (mCurrentMemMarker != null) {
                                mCurrentMemMarker.startAnimation();
                                setNotClickedMarkerAnim();
                            }
                            mCurrentMemMarker = marker;
                            marker.startAnimation();
                            marker.showInfoWindow();
                            marker.setToTop(); // 显示在最顶部
                            setClickedMarkerAnim();
                        }
                    } else {
                        if (mCurrentMemMarker != null) {
                            if (!LINE_TYPE.equals(mCurrentMemMarker.getSnippet()) && !CAPTAIN_TYPE.equals(mCurrentMemMarker.getSnippet()) && !CAR_TYPE.equals(mCurrentMemMarker.getSnippet())) {
                                mCurrentMemMarker.startAnimation();
                                setNotClickedMarkerAnim();
                                mCurrentMemMarker = null;
                            }
                        }
                        mCurrentOtherMarker = marker;
                        marker.showInfoWindow();
                        marker.setToTop(); // 显示在最顶部
                    }
                    break;
                }
            }

        } else if (type == 1) {
            // jump to interface
            if (!"".equals(userid) && null != userid) {
                IntentManager.getInstance().goGroupPersonalInfoActivity(this, mGroupId, userid);
            } else {
                ToastUtils.showText(this, "获取用户信息异常");
            }
        }
        // hide right view
        hideRightShareContentView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String destinationName = data.getStringExtra("destinationName");
                String stagingTime = data.getStringExtra("stagingTime");
                double destinationLat = data.getDoubleExtra("destinationLat", 0.0);
                double destinationLng = data.getDoubleExtra("destinationLng", 0.0);
                if (destinationLat == 0.0 || destinationLng == 0.0 || "".equals(destinationName) || null == destinationName) {
                    // request data failed
                    ToastUtils.showText(mContext, "获取目的地信息失败，请重试!");
                    return;
                }
                destination = destinationName;
                destinationCoordinate = destinationLng + "," + destinationLat;
                this.destinationLat = destinationLat;
                this.destinationLng = destinationLng;
                gatherTime = stagingTime;
                // save info
                saveSettingInfo();
            }
        }
    }

    // 景点语音讲解点击 跳转到景区导览界面
    public void setSpotVoiceTextClick(int spotId) {
        IntentManager.getInstance().goScenicSpotMapActivity(mContext,spotId,mGroupId);
    }

    /**
     * bottom trip detail list blue txt click 2 stage 6 live 7 scenic spot not use type
     * 10 早 11 午 12 晚
     */
    public void setBottomBlueTextClick(double lat, double lng, int position, int type) {
//        if (type == 2){ // 集结点
//        } else {
            setMapCenterLocation(lat, lng);
//        }
        setHideBottomView();
        dataBinding.ivBottomToUp.setImageResource(R.drawable.shared_navigation_bottom_toup_icon);

        for (int i = 0; i < mAllLineMarker.size(); i++) {
            if (mAllLineMarker.get(i).getPosition().latitude == lat && mAllLineMarker.get(i).getPosition().longitude == lng) {
                Marker marker = mAllLineMarker.get(i);
                if (mCurrentMemMarker != null) {
                    if (!LINE_TYPE.equals(mCurrentMemMarker.getSnippet()) && !CAPTAIN_TYPE.equals(mCurrentMemMarker.getSnippet()) && !CAR_TYPE.equals(mCurrentMemMarker.getSnippet())) {
                        mCurrentMemMarker.startAnimation();
                        setNotClickedMarkerAnim();
                        mCurrentMemMarker = null;
                    }
                }
                mCurrentOtherMarker = marker;
                marker.showInfoWindow();
                marker.setToTop(); // 显示在最顶部
                break;
            }
        }
    }

    /**
     * 将当前位置显示在地图屏幕的中间
     * @param lat
     * @param lng
     */
    private void setMapCenterLocation(double lat, double lng) {
        if (mAmap != null) {
            mAmap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(lat, lng)));
            mAmap.moveCamera(CameraUpdateFactory.zoomTo(mInitMapZoom));
        }
    }

    private final int HANDLER_GET_MEMBER_INFO = 0x0101;
    private final int HANDLER_UPDATE_MEMBER_INFO = 0x0102;
    private final int HANDLER_UPDATE_SHARE_LOCATION = 0x0103;
    private List<SharedNavigationMapRightInfo> mMemberInfo = new ArrayList<>();
    private List<SharedNavigationMapRightInfo> mMemberInfo_BackUp = new ArrayList<>();
    private List<SharedNavigationMapRightInfo> mMemberInfo_All = new ArrayList<>();
    private List<SharedNavigationMapRightInfo> mRemoveMember = new ArrayList<>();
    // 延迟加载timer
    private final int TIMER_DELAY = 100;
    // 将进度条分成100份 每5秒钟刷新一次

    private boolean mIsStartTimer = true;

    /**
     * update member information
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_GET_MEMBER_INFO: // get member info
                    if (mIsActivity) {
                        getUserInfoToDataBase();
                        if (mIsActivity) {
                            // send handler delay 5 second
                            mHandler.sendEmptyMessageDelayed(HANDLER_GET_MEMBER_INFO, Constants.UPDATE_TIME_MAP_INFO);
                            if (mIsStartTimer){
                                mIsStartTimer = false;
                                StartTimer();
                            }
                        }
                    }
                    break;
                case HANDLER_UPDATE_MEMBER_INFO: // update member info to view
                    if (mIsActivity) {
                        // add marker data and init right view
                        getUserInfoToDataBase();
                    }
                    break;
                case HANDLER_UPDATE_SHARE_LOCATION: // update share location
                    if (mIsActivity) {
                        if (mIsShareLoc) {
                            viewModel.sendMessageTeamLocation(mContext,1,mGroupId,mUserId,nickname,avtor,Constants.mMyCarInfo,mRole,mLeader);
                            mHandler.sendEmptyMessageDelayed(HANDLER_UPDATE_SHARE_LOCATION,Constants.SEND_TIME_MAP_INFO);
                        } else {
                            viewModel.sendMessageTeamLocation(mContext,2,mGroupId,mUserId,nickname,avtor,Constants.mMyCarInfo,mRole,mLeader);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

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
        // 当时使用缓存的情况下 可能这几个同时没有数据 将界面中的marker更新
        if (mMemberInfo_All.size() == 0 && mMemberInfo.size() == 0 && mMemberInfo_BackUp.size() == 0) {
            int pos1 = mAllMarker.size();
            for (int j = 0; j < pos1; j++) {
                mAllMarker.get(j).remove();
            }
            mAllMarker.clear();
        }

        if (mIsActivity) {
            // update adapter
            setShowMarkerIconInfo();
        }
    }


    private void setRightMyInfo() {
        // headimg
        GlideUtil.loadImgCircle(dataBinding.ivMineHeadImg, avtor);
        // nick name
        dataBinding.tvMineNickName.setText(nickname);
        // Mine Car info
        if ("".equals(Constants.mMyCarInfo) || null == Constants.mMyCarInfo) {
            dataBinding.tvMineTypeContent.setVisibility(View.GONE);
        } else {
            dataBinding.tvMineTypeContent.setVisibility(View.VISIBLE);
            dataBinding.tvMineTypeContent.setText(Constants.mMyCarInfo);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getOrderType(NavigationTypeEvent event) {
        if (event.getType() == 1) { // 0 guide page 1 update right view
            if (mIsActivity) {
                mHandler.sendEmptyMessage(HANDLER_UPDATE_MEMBER_INFO);
            }
        }
    }

    /**
     * 显示当前第几天的
     */
    private void showDaysLinesInfo() {
        if (mBottomDaysPosition == 0) {
            // 显示全部行程路线
            SurfacePolyAllLine();
        } else {
            // 显示当天行程路线
            SurfacePolyDaysLine(mBottomDaysPosition);
        }
    }

    LocationSource.OnLocationChangedListener mListener;
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;
    private MyLocationStyle myLocationStyle;

    /**
     * set map info
     */
    private void setUpMap() {
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
        // 每隔两秒更新一次数据
        //  如果可以就将用户的图片传到这个bitmap
        if (mPerView == null){
            mPerView = View.inflate(this, R.layout.marker_shared_navigation_person, null);
//            mPerViewBitmap = BitmapUtils.convertViewToBitmap(mPerView);
        }
        mAmap.setMyLocationStyle(myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromView(mPerView))); // 表示是人的方向
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色  取消背景圆圈
        myLocationStyle.anchor(0.5f,0.5f);
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
                    Constants.mIsOtherLocation = true;
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
                    if (!TextUtils.isEmpty(nickname)) {
                        addMyCarAndPersonLocationMarker(true, mIsShowCarInfo, new LatLng(Constants.mLatitude, Constants.mLongitude), 50, null, mMyType, nickname, getCarInfo(), mUserId, 0.0f,0);
                    }
                    if (!mIsCaptain) { // 非领队情况
                        setAutoSignInDes();
                    }
                } else {
                    if (ActivityUtils.getTopActivity() == mContext) {

                        if (GPSUtils.isOPen(mContext)){
                            ToastUtils.showText(mContext, "定位失败，当前GPS信号弱");
                        } else {
                            ToastUtils.showText(mContext, "定位失败，请打开GPS位置权限");
                        }
                        String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                        Log.e("AmapErr",errText);
                    }
                }
            }
        }
    };

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

    private void initUpdateProgress(){
        dataBinding.progressBar.setMax(100);
        dataBinding.progressBar.setProgress(0);
    }

    private int progress = 0;
    private Timer timer;
    private TimerTask timerTask;

    /**
     * 刷新进度条的timer
     */
    public void StartTimer() {
        //如果timer和timerTask已经被置null了
        if (timer == null&&timerTask==null) {
            //新建timer和timerTask
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    //每次progress加一
                    progress++;
                    //如果进度条满了的话就再置0，实现循环
                    if (progress == 101) {
                        progress = 0;
                    }
                    //设置进度条进度
                    dataBinding.progressBar.setProgress(progress);
                }
            };
            /*开始执行timer,第一个参数是要执行的任务，
            第二个参数是开始的延迟时间（单位毫秒）或者是Date类型的日期，代表开始执行时的系统时间
            第三个参数是计时器两次计时之间的间隔（单位毫秒）*/
            timer.schedule(timerTask, TIMER_DELAY, Constants.TIMER_UPDATE_PROGRESS);
        }
    }

    public void EndTimer() {
        /*这里很奇怪的是如果仅仅是把值赋成Null的话计时并没有停止，循环一次过后Progress就每次都加二了，循环两次过后就是加三
         * 如果仅仅是cancel掉的话也不能再进行调用了
         * 所以想要彻底重置timer的话需要cancel后再置null*/
        if (timer != null) {
            timer.cancel();
        }
        if (timerTask != null) {
            timerTask.cancel();
        }
        timer=null;
        timerTask=null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void LocationShareStatusEventBus(LocationShareStatusEvent event) {
        if (event.getGroupId().equals(mGroupId)) {
            mIsShareLoc = event.isStatus();
            mHandler.sendEmptyMessage(HANDLER_UPDATE_SHARE_LOCATION);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void LocationShareStatusEventBus(BackMapAndChatStatusEvent event) {
        if (event.isStatus() == 1){ // 返回逻辑
            if (mGroupId.equals(event.getGroupId())) {
                this.finish();
            } else {
                // 入群后,加入或切换对讲组
                TalkManage mTalkManage = TalkManage.getInstance();
                mTalkManage.joinOrSwitchIntercomGroup(mGroupId);
            }
        } else if (event.isStatus() == 2) {
            // 入群后,加入或切换对讲组
            TalkManage mTalkManage = TalkManage.getInstance();
            mTalkManage.joinOrSwitchIntercomGroup(mGroupId);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void LocationShareStatusEventBus(UpdateDesAndTimeEvent event) {
        if (event.getType() == 1){ // 更新了集结点 调用集结点信息
            if (event.getGroupid().equals(mGroupId)){ // 判断是否是当前群 选择更新
                viewModel.getGroupSettingDestination(mToken,mGroupId);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean isFinish = intent.getBooleanExtra("isFinish",false);
        if (isFinish){
            this.finish();
        } else {
            if (!mGroupId.equals(intent.getStringExtra("groupid"))){
                // 清除开始界面的所有数据
                mAmap.clear();
                setOnDestoryNullData();
                // 刷新界面
                mGroupId = intent.getStringExtra("groupid");
                initDatas();
            }
        }
    }

    /**
     * 注册群信息更新监听
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
            for (Team t : teams) {
                if (t.getId().equals(mGroupId)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dataBinding.tvTitleNumber.setText("(" +t.getMemberCount() + ")");
                        }
                    });
                    break;
                }
            }
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
                    EventBus.getDefault().post(new RefreshMyTalkGroupList()); // 更新对讲群列表信息
                    // 相除对讲相关业务
                    UserHelper.clearTalkBusiness(false);
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent);
                }
            }
        }
    };

    //************************************** 自动签到
    private LatLng mMyLatLng;
    private float mStageDistance = 0.0f;

    /**
     * 已经获取一次地址
     */
    private void setAutoSignInDes(){
        if (mInfoDetail != null){ // 判断签到信息不为空
            if (mInfoDetail.getIsSignin() != 1){ // 表示未签到
                if (!TextUtils.isEmpty(mInfoDetail.getDestinationCoordinate())) {
                    destinationLng = Double.parseDouble(mInfoDetail.getDestinationCoordinate().split(",")[0]);
                    destinationLat = Double.parseDouble(mInfoDetail.getDestinationCoordinate().split(",")[1]);
                    mCaptainLatLng = new LatLng(destinationLat, destinationLng);
                    mMyLatLng = new LatLng(Constants.mLatitude, Constants.mLongitude);
                }
                // 距离大小
                mStageDistance = AMapUtils.calculateLineDistance(mCaptainLatLng,mMyLatLng);
                if (mStageDistance <= Constants.DISTANCE){ // 在集结地距离之内
                    if (mInfoDetail.getNowTimeStamp() > 0 && mInfoDetail.getGatherTimeStamp() > 0) {
                        long time_60 = mInfoDetail.getNowTimeStamp() - mInfoDetail.getGatherTimeStamp();
                        if (time_60 < (1000 * 60 * 60)) { // 60分钟之内
                            viewModel.setSignIn(mToken, String.valueOf(mStageId));
                        }
                    }
                }
            }
        }
    }
    //************************************** 自动签到

}
