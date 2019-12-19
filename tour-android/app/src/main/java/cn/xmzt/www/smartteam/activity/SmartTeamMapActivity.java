package cn.xmzt.www.smartteam.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ActivitySmartTeamMapBinding;
import cn.xmzt.www.dialog.InviteFriendsToJoinUsDialog;
import cn.xmzt.www.dialog.SharedNavigationDatePickerDialog;
import cn.xmzt.www.glide.GlideApp;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.helper.UserHelper;
import cn.xmzt.www.intercom.activity.TeamRoomActivity;
import cn.xmzt.www.intercom.api.NimUIKit;
import cn.xmzt.www.intercom.api.model.team.TeamDataChangedObserver;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.intercom.event.BackMapAndChatStatusEvent;
import cn.xmzt.www.intercom.event.LocationShareStatusEvent;
import cn.xmzt.www.intercom.event.RefreshMyTalkGroupList;
import cn.xmzt.www.intercom.profile.TeamLocationProfile;
import cn.xmzt.www.main.activity.MainActivity;
import cn.xmzt.www.main.globals.TalkManage;
import cn.xmzt.www.mine.bean.CommonVehicleBean;
import cn.xmzt.www.mine.event.UpdateVehicleEvent;
import cn.xmzt.www.roomdb.TourDatabase;
import cn.xmzt.www.roomdb.beans.GroupUserInfo;
import cn.xmzt.www.selfdrivingtools.activity.SharedNavigationGuideActivityBackUp;
import cn.xmzt.www.selfdrivingtools.bean.SettingDestinationTypeInfo;
import cn.xmzt.www.selfdrivingtools.bean.SharedNavigationMapRightInfo;
import cn.xmzt.www.selfdrivingtools.event.NavigationTypeEvent;
import cn.xmzt.www.manager.IntentManager;
import cn.xmzt.www.selfdrivingtools.event.TripSignInEvent;
import cn.xmzt.www.selfdrivingtools.event.UpdateDesAndTimeEvent;
import cn.xmzt.www.selfdrivingtools.event.UpdateGroupDefaultEvent;
import cn.xmzt.www.selfdrivingtools.overlay.AMapUtil;
import cn.xmzt.www.share.ShareFunction;
import cn.xmzt.www.smartteam.adapter.SmartTeamCarAndPersonAdapter;
import cn.xmzt.www.smartteam.adapter.SmartTeamTypeAdapter;
import cn.xmzt.www.smartteam.bean.SmartTeamInfoBean;
import cn.xmzt.www.smartteam.bean.TripSignInDetailBean;
import cn.xmzt.www.smartteam.vm.SmartTeamMapViewModel;
import cn.xmzt.www.utils.BitmapUtils;
import cn.xmzt.www.utils.DensityUtil;
import cn.xmzt.www.utils.GPSUtils;
import cn.xmzt.www.utils.LocalDataUtils;
import cn.xmzt.www.utils.StatusBarUtil;
import cn.xmzt.www.utils.TimeUtil;
import cn.xmzt.www.utils.ToastUtils;
import cn.xmzt.www.view.CenterLayoutManager;
import cn.xmzt.www.view.RoundImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * SmartTeamMap
 */
public class SmartTeamMapActivity extends TourBaseActivity<SmartTeamMapViewModel, ActivitySmartTeamMapBinding> {

    // 整体地图控件
    private AMap mAmap;
    // setting time dialog
    private SharedNavigationDatePickerDialog mTimerPicker;
    // 默认开始的时间
    String mStartTime = "2019-08-23 19:47";
    // 默认结束的时间
    String mEndTime = "2029-08-23 19:47";
    // Judging that the current interface displays shared mode default vehicles
    private boolean mIsShowCarInfo = false;
    // 底部人员Adapter
    private SmartTeamCarAndPersonAdapter mRightAdapter;
    // 保存所有人员marker
    List<Marker> mAllMarker = new ArrayList<>();
    // 我的marker
    Marker mMyLocationMarker = null;
    Marker mMyLocationMarker_backup = null;
    // 目的地的marker
    Marker mDestinationMarker = null;
    // 当前用户maker
    Marker mCurrentMemMarker = null;
    // 当前其他marker
    Marker mCurrentOtherMarker = null;
    // 初始化地图zoom
    private int mInitMapZoom = 19;
    // 底部人员/车辆信息
    List<SettingDestinationTypeInfo> mTripDaysListInfo = null;
    // 底部类型人员/车辆Adapter
    private SmartTeamTypeAdapter mTypeAdapter;
    // 底部类型数据
    private List<SettingDestinationTypeInfo> mBottomType = new ArrayList<>();
    // 目的地的经纬度
    private LatLng mCaptainLatLng = null;

    // 判断是否领队或者群主——当前只有群主 群主显示为领队
    private boolean mIsCaptain = false;

    // marker type
    public String CAR_TYPE = "car";
    public String PERSON_TYPE = "person";
    public String CAPTAIN_TYPE = "captain";

    // 判断是否设置了目的度
    private boolean mIsSetDestination = false;

    private SmartTeamMapActivity mContext;

    // setting destination return code
    private int REQUEST_CODE = 1001;
    // 群id
    private String mGroupId = "";
    // 是否是从创建队伍进入
    private boolean mIsCreate = false;
    // 是否自动播放
    private boolean mIsAutoPlay = true;
    // 是否分享位置
    private boolean mIsShareLoc = true;
    // 角色
    private int mRole = 0;
    // 是否是领队
    private boolean mLeader = false;

    // 底部人员点击自动滑动到中间
    private CenterLayoutManager mBottomManager;

    // 目的地信息 名称
    private String destination;
    // 目的地信息 经纬度文本
    private String destinationCoordinate;
    // 目的地信息 经纬度Lat
    private double destinationLat;
    // 目的地信息 经纬度Lng
    private double destinationLng;
    // 目的地信息 时间信息
    private String gatherTime;
    // 集结地信息编号
    private long mStageId = 0;
    // 是否在当前界面
    private boolean mIsActivity = true;
    // 是否有Token
    private String mToken = "";
    // 我的类型名称
    private String mMyType = "成员";

    // 上一个界面传递过来的参数名称
    private String groupidStr = "groupid";
    private String isCreateStr = "isCreate";

    private InviteFriendsToJoinUsDialog mInviteFriendsToJoinUsDialog;

    private String mDialogCode = "";
    private String mTeamPwdcard = "";

    private String mCheckedUserid = "";
    @Override
    protected int setLayoutId() {
        return R.layout.activity_smart_team_map;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean isFinish = intent.getBooleanExtra("isFinish",false);
        if (isFinish){
            this.finish();
        } else {
            if (!mGroupId.equals(intent.getStringExtra(groupidStr))){
                // 清除开始界面的所有数据
                mAmap.clear();
                setOnDestoryNullData();
                // 刷新界面
                mGroupId = intent.getStringExtra(groupidStr);
                mIsCreate = intent.getBooleanExtra(isCreateStr,false);
                initDatas();
            }
        }
    }

    @Override
    protected SmartTeamMapViewModel createViewModel() {
        mContext = this;
        viewModel = new SmartTeamMapViewModel();
        viewModel.setIView(this);
        // 设置目的地接口改变
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
                    viewModel.getSmartTeamGroupInfo(mToken,mGroupId,false);
                }
            }
        });
        // 获取群内详请
        viewModel.mGroupInfo.observe(mContext, new Observer<SmartTeamInfoBean>() {
            @Override
            public void onChanged(SmartTeamInfoBean smartTeamInfoBean) {
                // 判断有数据的情况
                if (!TextUtils.isEmpty(smartTeamInfoBean.getGroupId())) {
                    mGroupId = smartTeamInfoBean.getGroupId();
                }
                // 获取了群内信息  更新界面
                setHaveData(smartTeamInfoBean);
            }
        });
        viewModel.mCheckPosition.observe(mContext, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mBottomPosition = integer;
            }
        });
        viewModel.result.observe(mContext, new Observer<BaseDataBean<String>>() {
            @Override
            public void onChanged(BaseDataBean<String> stringBaseDataBean) {
                // 添加车辆成功
                // 刷新一下数据
                if (mAddCarPage) {
                    clearAllMarker();
                    getUserInfoToDataBase();
                }
            }
        });
        viewModel.mCarInfo.observe(mContext, new Observer<CommonVehicleBean>() {
            @Override
            public void onChanged(CommonVehicleBean commonVehicleBean) {
                if (commonVehicleBean != null){
                    if (!TextUtils.isEmpty(commonVehicleBean.getPlateNumber())){
                        mMyPlateNumber = commonVehicleBean.getPlateNumber();
                        viewModel.smartTeamAddOrUpdtOrDelDriver(1,mGroupId,mMyPlateNumber);
                    }
                }
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

    private String mMyPlateNumber = "";

    private void setHaveData(SmartTeamInfoBean data){
        if (data != null){
            // 数据不为空的情况
            // 顶部内容
            Constants.mGroupName = data.getGroupTitle(); // 群名称
            dataBinding.tvTitleName.setText(Constants.mGroupName);
            // 邀请队伍口令设置
            if (!TextUtils.isEmpty(data.getTeamPwdcard())){
                String content = data.getTeamPwdcard();
                mTeamPwdcard = content;
                mDialogCode = content;
                if (mIsCreate) {
                    mIsCreate = false;
                    showInviteFriendsToJoinUsDialog();
                }
            } else {
                ToastUtils.showText(mContext,"获取队伍口令失败");
            }
            // 设置我的信息
            avtor = data.getImage(); // 头像
            Constants.mMyNickName = data.getNickname(); // 昵称
            mUserId = String.valueOf(TourApplication.getUser().getUserId());// 我的用户id
            Constants.mMyCarInfo = data.getLicencePlate();// 我的车辆信息
            if (TextUtils.isEmpty(Constants.mMyCarInfo)){ // 如果我的车辆为空
                // 请求是否有默认车辆的信息
                viewModel.getSysUserPlateSelectDefault();
            }
            // 是否自动监听
            mIsAutoPlay = data.isAutoplay();
            // 是否分享位置
            mIsShareLoc = data.isShareLocation();
            mRole = data.getRole();
            mLeader = data.isLeader();
            // 是否是群主
            if (data.getRole() == 1){
                mIsCaptain = true;
                mMyType = "领队";
            } else if (data.isLeader()) {
                mIsCaptain = true;
                mMyType = "领队";
            } else {
                mIsCaptain = false;
                mMyType = "成员";
            }
            // 群内全部用户 将数据保存到数据库  如果数据库有的话就不替换
            groupMemberVOS = data.getGroupMemberVOS();
            if(groupMemberVOS!=null&&groupMemberVOS.size()>0){
                dataBinding.tvTitleNumber.setText("(" + groupMemberVOS.size() + ")");
            }
            // 设置目的地界面的显示
            setSettingDestinationAndTimeView(data.getDestination(),data.getDestinationCoordinate(),data.getGatherTime());
            mStageId = data.getDesId();
            if (mStageId > 0 && !mIsCaptain){ // 有数据并且不是领队 才请求数据
                viewModel.getSignInChecked(mToken, String.valueOf(mStageId));
            }
            saveGroupInfoAll();
        } else {
            ToastUtils.showText(mContext,"获取队伍详请信息失败");
        }
        // 分享位置
        mHandler.sendEmptyMessage(HANDLER_UPDATE_SHARE_LOCATION);
    }

    private List<GroupMemberBean> groupMemberVOS = new ArrayList<>();

    private void saveGroupInfoAll(){
        if (groupMemberVOS != null){
            List<GroupUserInfo> listService = new ArrayList<>();//添加服务器获取之后的所有数据
            for (int i = 0; i< groupMemberVOS.size();i++){
                GroupUserInfo info = TourDatabase.getDefault(mContext).getGroupUserInfoDao().getData(mGroupId, String.valueOf(groupMemberVOS.get(i).getUserId()));
                if (info == null){ // 表示数据库有数据就不管
                    GroupMemberBean bean = groupMemberVOS.get(i);
                    info = new GroupUserInfo();
                    info.type = (bean.isLeader() ? 1 : 2);
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
                    info.type = (bean.isLeader() ? 1 : 2);
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
        // 获取我的信息
        requestMyInfo();
        // 获取群内信息
        setMyInfoData();
    }

    /**
     * 显示邀请好友的dialog
     */
    private void showInviteFriendsToJoinUsDialog(){
        if (mInviteFriendsToJoinUsDialog == null){
            mInviteFriendsToJoinUsDialog = new InviteFriendsToJoinUsDialog(mContext, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.dialog_invite_tv) { // 立即邀请
                        String tettle = Constants.mMyNickName + "邀请你加入自驾队伍";
                        String description = "口令" + mTeamPwdcard + "，打开小马在途app，找到【工具菜单>智能出行>加入队伍】，然后输入下方口令，即可加入";
                        String url = "https://weixin.xmzt.cn/#/pages/groupInvite/index?groupId="+mGroupId+"&refCode="+TourApplication.getUser().getRefCode()+"&refName="+Constants.mMyNickName;
                        ShareFunction.getInstance().shareWeb(mContext, tettle, avtor, description, url, ShareFunction.SHARE_WEIXIN);
                    }
                    mInviteFriendsToJoinUsDialog.cancel();
                }
            });
        }
        mInviteFriendsToJoinUsDialog.setViewData(mDialogCode);
        mInviteFriendsToJoinUsDialog.show();
    }

    private String avtor = ""; // 头像
    private String mUserId = ""; // 用户id
    private SharedNavigationMapRightInfo mMyBean = null;

    /**
     * 当前界面获取信息
     */
    private void setMyInfoData() {
        getUserInfoToDataBase();
        // init right data
        mHandler.sendEmptyMessage(HANDLER_GET_MEMBER_INFO);
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
        mGroupId = getIntent().getStringExtra(groupidStr);
        mIsCreate = getIntent().getBooleanExtra(isCreateStr,false);

        // 底部人员信息
        mBottomManager = new CenterLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        dataBinding.rvCarPersonList.setLayoutManager(mBottomManager);
        mRightAdapter = new SmartTeamCarAndPersonAdapter(this);
        dataBinding.rvCarPersonList.setAdapter(mRightAdapter);

        // 底部类型信息
        mBottomType = LocalDataUtils.getSmartTeamTypeInfo();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        dataBinding.rvTypeList.setLayoutManager(layoutManager);
        mTypeAdapter = new SmartTeamTypeAdapter(this);
        dataBinding.rvTypeList.setAdapter(mTypeAdapter);
        mTypeAdapter.setDatas(mBottomType);
        // init setting time dialog
        initTimerPicker();
        // init top view
        initTopView();
        // init click
        initClick();
        // 刷新数据的问题
        initDatas();
    }

    private void initDatas(){
        // 入群后,加入或切换对讲组
        TalkManage mTalkManage = TalkManage.getInstance();
        mTalkManage.joinOrSwitchIntercomGroup(mGroupId);

        EventBus.getDefault().post(new UpdateGroupDefaultEvent(mGroupId)); // 设置默认开启或者不开启位置信息

        mToken = TourApplication.getToken();

        // 请求群内详请
        viewModel.getSmartTeamGroupInfo(mToken,mGroupId,true);

        // init map view
        dataBinding.mvMap.onCreate(getIntent().getExtras());

        if (mAmap == null) {
            mAmap = dataBinding.mvMap.getMap();
            mAmap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(Constants.mLatitude,Constants.mLongitude)));
            setUpMap(); // 定位
        }
        // init map info
        initMapData();
    }

    private void initClick() {
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
            } else if (!CAPTAIN_TYPE.equals(mCurrentMemMarker.getSnippet()) && !CAR_TYPE.equals(mCurrentMemMarker.getSnippet())){
                mCurrentMemMarker.startAnimation();
                setNotClickedMarkerAnim();
                mCurrentMemMarker.hideInfoWindow();
                mCurrentMemMarker = null;
            }
        } else if (mCurrentOtherMarker != null){
            mCurrentOtherMarker.hideInfoWindow();
            mCurrentOtherMarker = null;
        }
        if (mBottomPosition != -1){
            mRightAdapter.getItem(mBottomPosition).setCheck(false);
            mRightAdapter.notifyItemChanged(mBottomPosition);
            mBottomPosition = -1;
            mCheckedUserid = "";
        }
    }

    /**
     * update view setting time
     */
    private void setSettingDestinationAndTimeView(String destinationS,String destinationCoordinateS,String gatherTimeS) {
            destination = destinationS;
            destinationCoordinate = destinationCoordinateS;
            gatherTime = gatherTimeS.trim();
            if (!"".equals(destinationCoordinate) && null != destinationCoordinate) {
                destinationLng = Double.parseDouble(destinationCoordinate.split(",")[0]);
                destinationLat = Double.parseDouble(destinationCoordinate.split(",")[1]);
                mCaptainLatLng = new LatLng(destinationLat, destinationLng);
            }
            // judging setting time
            if ("".equals(gatherTime) || null == gatherTime) {
            } else {
                dataBinding.tvTimeContentC.setText(TimeUtil.getShowGatherTime(gatherTime));
                dataBinding.tvTimeContent.setText(TimeUtil.getShowGatherTime(gatherTime));
            }
            // judging setting destination
            if ("".equals(destinationS) || null == destinationS) {
                mIsSetDestination = false;
            } else {
                mIsSetDestination = true;
                dataBinding.tvDesContentC.setText(destinationS);
                dataBinding.tvDesContent.setText(destinationS);
            }
            // update view
            initTopView();
            // staging marker
//            if (mCaptainLatLng != null) {
//                addCaptainMarkersToMap(this, mCaptainLatLng);
//            }
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
        // setting show infoWindow
        mAmap.setInfoWindowAdapter(mAMapOtherAdapter);
        // interface surface captain marker
//        if (!mIsCaptain) { // current page type is not captain then show
//            if (mCaptainLatLng != null) {
//                addCaptainMarkersToMap(this, mCaptainLatLng);
//            }
//        }

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
            final int isss = i;
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
                            }
                        });
            }
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
            case cn.xmzt.www.R.id.tv_top_go_edit_c: // top edit
                IntentManager.getInstance().goSmartTeamSettingDestinationAndTimeActivity(mContext,gatherTime,REQUEST_CODE);
                break;
            case cn.xmzt.www.R.id.tv_time_update_c: // update staging time
                mIsClickUpdateTime = true;
                settingTimeDialog(destinationLat,destinationLng);
                break;
            case cn.xmzt.www.R.id.rl_no_setting_data: // setting destination
                IntentManager.getInstance().goSmartTeamSettingDestinationAndTimeActivity(mContext,gatherTime,REQUEST_CODE);
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
            case cn.xmzt.www.R.id.iv_group_info: // top view go here
                dataBinding.tvNumNotRead.setVisibility(View.GONE);
                TeamRoomActivity.start(mContext,mGroupId,true,true);
                break;
            case R.id.right_iv: // 顶部右边进入到群内详请
                mAddCarPage = true;
                IntentManager.getInstance().goSmartTeamGroupInfoActivity(this,mGroupId);
                break;
            case cn.xmzt.www.R.id.iv_invite_friends_btn: // 邀请好友
                showInviteFriendsToJoinUsDialog();
                break;
            case cn.xmzt.www.R.id.iv_all_show_img: // 全览的功能
                setAllLatLng();
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

    private boolean mIsClickUpdateTime = false;

    @Override
    protected void onResume() {
        super.onResume();
        dataBinding.mvMap.onResume();
        registerTeamUpdateObserver(true);
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
        // 刷新一下数据
        if (mAddCarPage) {
            clearAllMarker();
            getUserInfoToDataBase();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        TalkManage.isShowSelectGroup = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataBinding.mvMap.onPause();
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
        mTripDaysListInfo = null;
        mIsShowCarInfo = false;
        mTimerPicker = null;
        mIsCaptain = false;
        mIsActivity = false;
        Constants.mMyNickName = "";
        Constants.mMyCarInfo = "";
        Constants.mGroupName = "";
        mAddCarPage = false;
        mCheckedUserid = "";
        mBottomPosition = -1;
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
            if (mMyLocationMarker_backup == marker){
                return false;
            }
            // judging marker type
            if (CAPTAIN_TYPE.equals(marker.getSnippet())) { // captain marker
                if (mCurrentMemMarker != null) {
                    if (!CAPTAIN_TYPE.equals(mCurrentMemMarker.getSnippet()) && !CAR_TYPE.equals(mCurrentMemMarker.getSnippet())) {
                        mCurrentMemMarker.startAnimation();
                        setNotClickedMarkerAnim();
                        mCurrentMemMarker = null;
                    }
                }
                mCurrentOtherMarker = marker;
                marker.showInfoWindow();
            } else if (CAR_TYPE.equals(marker.getSnippet())){
                if (mCurrentMemMarker != null) {
                    if (!CAPTAIN_TYPE.equals(mCurrentMemMarker.getSnippet()) && !CAR_TYPE.equals(mCurrentMemMarker.getSnippet())) {
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
                // get str_type yyyy-MM-dd HH:mm
                String date = TimeUtil.long2Str(timestamp, true);
//                String dateStr = date.substring(5, date.length());
                // update time view
                gatherTime = date;
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

    private void saveSettingInfo() {
        // request interface save setting destination and time info
        if (mIsClickUpdateTime) {
            mIsClickUpdateTime = false;
            viewModel.setGroupSettingDestination(mToken, null, null, gatherTime, mStageId, mGroupId);
        } else {
            viewModel.setGroupSettingDestination(mToken, destination, destinationCoordinate, gatherTime, null, mGroupId);
        }
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
    private Bitmap mCarViewBitmap = null;
    private Bitmap mPerViewBitmap = null;

    private void requestMyInfo(){
        mMyBean = new SharedNavigationMapRightInfo();
        mMyBean.setDistance(0.0f);
        GroupUserInfo info = TourDatabase.getDefault(mContext).getGroupUserInfoDao().getData(mGroupId,mUserId);
        mMyBean.setBean(info);
    }

    private boolean mIsAllShow = true;
    /**
     * update right data
     */
    private void setShowMarkerIconInfo() {
        if (mMarkerType != mIsShowCarInfo) {
            mIsAllShow = true;
            clearAllMarker();
            mMarkerType = mIsShowCarInfo;
            if (mIsShowCarInfo){
                if (mCarViewBitmap == null) {
                    mCarView = View.inflate(this, R.layout.marker_shared_navigation_car, null);
                    mCarViewBitmap = BitmapUtils.convertViewToBitmap(mCarView);
                }
                if (mPerViewBitmap == null){
                    mPerView = View.inflate(this, R.layout.marker_shared_navigation_person, null);
                    mPerViewBitmap = BitmapUtils.convertViewToBitmap(mPerView);
                }
                mAmap.setMyLocationStyle(myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap("".equals(getCarInfo()) ? mPerViewBitmap : mCarViewBitmap))); // 表示是车的方向
            } else {
                if (mPerViewBitmap == null){
                    mPerView = View.inflate(this, R.layout.marker_shared_navigation_person, null);
                    mPerViewBitmap = BitmapUtils.convertViewToBitmap(mPerView);
                }
                mAmap.setMyLocationStyle(myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(mPerViewBitmap))); // 表示是人的方向
            }
        }
        if (mIsShowCarInfo) { // show car
            // default start car type
            addMyCarAndPersonLocationMarker(true, mIsShowCarInfo, new LatLng(Constants.mLatitude,Constants.mLongitude), 0, null, getTypeStr(mMyType), Constants.mMyNickName, getCarInfo(), mUserId,0.0f,0);
            // update right data
            mRightInfo = viewModel.getRightCarInfo(mMemberInfo_All,mMyBean,mCheckedUserid);
            // add car marker
            setOtherAddMaker();
            mRightAdapter.setDatas(mRightInfo);
        } else {
            addMyCarAndPersonLocationMarker(true, mIsShowCarInfo, new LatLng(Constants.mLatitude,Constants.mLongitude), 50, null, mMyType, Constants.mMyNickName, getCarInfo(), mUserId,0.0f,0);
            mRightInfo = viewModel.getRightPersonInfo(mMemberInfo_All,mMyBean,mCheckedUserid);
            setOtherAddMaker();
            mRightAdapter.setDatas(mRightInfo);
        }
        if (mIsAllShow){
            setAllLatLng();
            mIsAllShow = false;
        }
    }

    private List<SharedNavigationMapRightInfo> mRightInfo = null;

    private int mDistance = 10000; // 10Km之外
    private long TIME_OUT = 60 * 1000 * 5L;

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
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                } else { // 车辆信息为空的情况下 人员
                    markerOption = new MarkerOptions()
                            .position(latLng)
                            .anchor(0.5f, 0.915f)
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
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
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
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
            }
            render(marker, infoContent);
            return infoContent;
        }

        private void render(Marker marker, View view) {
            if (CAPTAIN_TYPE.equals(marker.getSnippet())) { // 集结点的marker显示问题
                ImageView infowindow_go_here_btn = view.findViewById(R.id.infowindow_go_here_btn);
                TextView infowindow_title_tv = view.findViewById(R.id.infowindow_title_tv);

                infowindow_title_tv.setText(destination);

                infowindow_go_here_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setGoHereOption(marker.getPosition().latitude, marker.getPosition().longitude, destination);
                    }
                });
            } else { // 人员和车辆的显示问题
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
            IntentManager.getInstance().goSharedNavigationGuideActivityBackUp(mContext,true,0.0,0.0,0.0,0.0,"",0);
        } else {
            IntentManager.getInstance().goSharedNavigationGoHereActivityBackUp(mContext, lat, lng, locStr, mGroupId);
        }
    }

    /**
     * setting next destination option
     */
    private void setSettingNextDestination(String content, double lat, double lng) {
        destination = content;
        destinationCoordinate = lng + "," + lat;
        destinationLat = lat;
        destinationLng = lng;
        settingTimeDialog(destinationLat,destinationLng);
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

    /**
     * add captain marker
     */
    public void addCaptainMarkersToMap(Context context, LatLng latlng) {
        if (mDestinationMarker != null) {
            mDestinationMarker.remove();
        }
        if (mAmap != null) {
            View view = View.inflate(context, R.layout.marker_shared_navigation_captain_item, null);
            int radius = DensityUtil.dip2px(this, 10);
            Bitmap bitmap = BitmapUtils.convertViewToBitmap(view);
            MarkerOptions markerOption = new MarkerOptions()
                    .position(latlng)
                    .title("-1")
                    .snippet(CAPTAIN_TYPE)
                    .setInfoWindowOffset(0, radius)
                    .anchor(0.5f, 0.73f)
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
            mDestinationMarker = mAmap.addMarker(markerOption);
        }
    }

    private List<LatLng> mAllLatLng = new ArrayList<>();

    /**
     * 获取所有点的集合
     */
    private void setAllLatLng(){
        if (mAllLatLng == null){
            mAllLatLng = new ArrayList<>();
        }
//        if (mCaptainLatLng != null){  // 与产品确定不需要集结点的全览的情况
//            mAllLatLng.add(mCaptainLatLng);
//        }
        mAllLatLng.add(new LatLng(Constants.mLatitude,Constants.mLongitude));
        // 将所有的点显示到地图界面上
        setMapBounds(mAllLatLng);
    }

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

    private int mBottomPosition = -1;

    public void OnBottomListClickListener(String userid,int position,int type){
        // head or list item click option
        if (type == 0) { // 人员列表的点击事件
            if (mCheckedUserid.equals(userid)){
                return;
            }
            if (mBottomPosition != -1) {
                mRightInfo.get(mBottomPosition).setCheck(false);
                mRightAdapter.notifyItemChanged(mBottomPosition);
            }
            mCheckedUserid = userid;
            mBottomPosition = position;
            mRightInfo.get(mBottomPosition).setCheck(true);
            mRightAdapter.notifyItemChanged(mBottomPosition);

//            hideShowMarker();// 隐藏当前显示marker

            // request current position LatLng
            if (mRightInfo.get(position).getBean().latitude == 0.0 && mRightInfo.get(position).getBean().longitude == 0.0){
                ToastUtils.showText(mContext,"当前用户无位置信息");
                return;
            }
            // 当前是点击了自己的情况
            if (mUserId.equals(mRightInfo.get(position).getBean().userId)){
                setMapCenterToMineLocation();
                if (mCurrentMemMarker != null) {
                    mCurrentMemMarker.startAnimation();
                    setNotClickedMarkerAnim();
                }
                mCurrentMemMarker = mMyLocationMarker;
                mMyLocationMarker.startAnimation();
                mMyLocationMarker.showInfoWindow();
                mMyLocationMarker.setToTop(); // 显示在最顶部
                setClickedMarkerAnim();
                return;
            }

            LatLng latLng = new LatLng(mRightInfo.get(position).getBean().latitude,mRightInfo.get(position).getBean().longitude);
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
                            if (!CAPTAIN_TYPE.equals(mCurrentMemMarker.getSnippet()) && !CAR_TYPE.equals(mCurrentMemMarker.getSnippet())) {
                                mCurrentMemMarker.startAnimation();
                                setNotClickedMarkerAnim();
                                mCurrentMemMarker = null;
                            } else if (!PERSON_TYPE.equals(mCurrentMemMarker.getSnippet())){
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

        } else if (type == 1) { // 人员列表头像的点击事件
            // jump to interface
            if (!"".equals(userid) && null != userid) {
                IntentManager.getInstance().goGroupPersonalInfoActivity(this, mGroupId, userid);
            } else {
                ToastUtils.showText(this, "获取用户信息异常");
            }
        } else if (type == 2){ // 表示是底部类型点击事件
            mCheckedUserid = "";
            mBottomPosition = -1;
            if (mBottomTypeNum != position){
                mBottomType.get(mBottomTypeNum).setIs_Checked(false);
                mTypeAdapter.notifyItemChanged(mBottomTypeNum);
                mBottomType.get(position).setIs_Checked(true);
                mTypeAdapter.notifyItemChanged(position);
                mBottomTypeNum = position;
                if (mBottomTypeNum == 0){
                    mIsShowCarInfo = false;
                } else {
                    mIsShowCarInfo = true;
                }
                mHandler.sendEmptyMessage(HANDLER_UPDATE_MEMBER_INFO);
            }
        } else if (type == 3){ // 添加车辆的点击事件
            mAddCarPage = true;
            // TODO 在常用车辆中去选择车辆
            IntentManager.getInstance().goCommonVehiclesActivity(mContext,true,false,new ArrayList<>());
//            IntentManager.getInstance().goSmartTeamAddCarActivity(mContext,mGroupId,Constants.mMyCarInfo);
        }
    }

    private boolean mAddCarPage = false;
    private int mBottomTypeNum = 0;

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
                            mHandler.sendEmptyMessageDelayed(HANDLER_GET_MEMBER_INFO, Constants.UPDATE_TIME_MAP_INFO);
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
                            viewModel.sendMessageTeamLocation(mContext,1,mGroupId,mUserId,Constants.mMyNickName,avtor,Constants.mMyCarInfo,mRole,mLeader);
                            mHandler.sendEmptyMessageDelayed(HANDLER_UPDATE_SHARE_LOCATION,Constants.SEND_TIME_MAP_INFO);
                        } else {
                            viewModel.sendMessageTeamLocation(mContext,2,mGroupId,mUserId,Constants.mMyNickName,avtor,Constants.mMyCarInfo,mRole,mLeader);
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getOrderType(NavigationTypeEvent event) {
        if (event.getType() == 1) { // 0 guide page 1 update right view
            if (mIsActivity) {
                mHandler.sendEmptyMessage(HANDLER_UPDATE_MEMBER_INFO);
            }
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
        if (mPerViewBitmap == null){
            mPerView = View.inflate(this, R.layout.marker_shared_navigation_person, null);
            mPerViewBitmap = BitmapUtils.convertViewToBitmap(mPerView);
        }
        mAmap.setMyLocationStyle(myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(mPerViewBitmap))); // 表示是人的方向
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
                    Constants.mLongitude = amapLocation.getLongitude();
                    Constants.mLatitude = amapLocation.getLatitude();
                    Constants.mCity = amapLocation.getCity();
                    Constants.mAddress = amapLocation.getAddress();
                    Constants.mLocation = amapLocation.getLongitude() + "," + amapLocation.getLatitude();
                    String mCityCode=amapLocation.getAdCode();
                    if(mCityCode!=null){
                        mCityCode=mCityCode.substring(0,4)+"00";
                        Constants.mCityCode = mCityCode;
                    }
                    if (!TextUtils.isEmpty(Constants.mMyNickName)) {
                        addMyCarAndPersonLocationMarker(true, mIsShowCarInfo, new LatLng(Constants.mLatitude,Constants.mLongitude), 50, null, mMyType, Constants.mMyNickName, getCarInfo(), mUserId, 0.0f,0);
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
                        String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                        Log.e("AmapErr", errText);
                    }
                }
            }
        }
    };

    private void getUserInfoToDataBase(){
        // 获取我的信息
        requestMyInfo();
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

    private String getTypeStr(int type){
        String info = "成员";
        if (type == 0){
            info = "领队";
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
                viewModel.getSmartTeamGroupInfo(mToken,mGroupId,false);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void LocationShareStatusEventBus(UpdateVehicleEvent event) { // 添加车辆
        if (event.getType() == 2){ // 添加车辆信息 调用车辆添加接口
            mAddCarPage = true;
            viewModel.smartTeamAddOrUpdtOrDelDriver(1,mGroupId,event.getLicencePlate().get(0).getPlateNumber());
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
