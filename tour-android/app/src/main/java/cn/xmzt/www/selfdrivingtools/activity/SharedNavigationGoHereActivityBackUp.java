package cn.xmzt.www.selfdrivingtools.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapRestrictionInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.autonavi.tbt.TrafficFacilityInfo;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ActivitySharedNavigationGoHereBackBinding;
import cn.xmzt.www.manager.IntentManager;
import cn.xmzt.www.selfdrivingtools.adapter.SharedNavigationGoHereAdapterBackup;
import cn.xmzt.www.selfdrivingtools.bean.SharedNavigationGoHereBeanBack;
import cn.xmzt.www.selfdrivingtools.event.GuideMinTypeEvent;
import cn.xmzt.www.selfdrivingtools.overlay.AMapUtil;
import cn.xmzt.www.selfdrivingtools.overlay.SharedGoHereDrivingRouteOverlayBackUp;
import cn.xmzt.www.selfdrivingtools.overlay.WalkRouteOverlay;
import cn.xmzt.www.selfdrivingtools.viewmodel.SharedNavigationGoHereViewModel;
import cn.xmzt.www.utils.GPSUtils;
import cn.xmzt.www.utils.SPUtils;
import cn.xmzt.www.utils.ToastUtils;

public class SharedNavigationGoHereActivityBackUp extends TourBaseActivity<SharedNavigationGoHereViewModel, ActivitySharedNavigationGoHereBackBinding> implements AMapNaviListener {

    AMap mAmap;

    LatLonPoint mSearchLatLng = null;
    LatLonPoint mMyLatLng = null;
    double mLat = 0.0;
    double mLng = 0.0;
    String mLocStr = "";
    boolean mIsDrivingRoute = true;

    //通过经纬度去获取具体的地址信息
    GeocodeSearch geocoderSearch;

    RouteSearch mRouteSearch = null;
    WalkRouteResult mWalkRouteResult = null;

    SharedNavigationGoHereAdapterBackup mAdapter = null;
    List<SharedNavigationGoHereBeanBack> mListInfo = new ArrayList<>();

    private boolean mIsEmpty = false;

    private Context mContext;

    private String mGroupId = "";

    private boolean congestion, cost, hightspeed, avoidhightspeed;
    /**
     * 导航对象(单例)
     */
    private AMapNavi mAMapNavi;

    private List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
    /**
     * 终点坐标集合［建议就一个终点］
     */
    private List<NaviLatLng> endList = new ArrayList<NaviLatLng>();
    /**
     * 保存当前算好的路线
     */
    private SparseArray<RouteOverLay> routeOverlays = new SparseArray<RouteOverLay>();

    /**
     * 当前用户选中的路线，在下个页面进行导航
     */
    private int routeIndex;
    /**
     * 路线的权值，重合路线情况下，权值高的路线会覆盖权值低的路线
     **/
    private int zindex = 1;
    /**
     * 路线计算成功标志位
     */
    private boolean calculateSuccess = false;



    @Override
    protected int setLayoutId() {
        return R.layout.activity_shared_navigation_go_here_back;
    }

    @Override
    protected SharedNavigationGoHereViewModel createViewModel() {
        viewModel = new SharedNavigationGoHereViewModel();
        viewModel.setIView(this);
        return viewModel;
    }

    @Override
    public void showLoading() {
        // 显示正在加载中的动画
        dataBinding.rlLoading.setVisibility(View.VISIBLE);
        dataBinding.rvGuideList.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        // 显示正在加载中的动画
        dataBinding.rlLoading.setVisibility(View.GONE);
        dataBinding.rvGuideList.setVisibility(View.VISIBLE);
    }


    @Override
    protected void initData() {
        mContext = this;
        dataBinding.setModel(viewModel);
        dataBinding.setActivity(this);
        EventBus.getDefault().register(this);
        // 获取上一个界面传递过来的数据
        mLocStr = getIntent().getStringExtra("locStr");
        mLat = getIntent().getDoubleExtra("lat",0.0);
        mLng = getIntent().getDoubleExtra("lng",0.0);
        mGroupId = getIntent().getStringExtra("groupId");

        // 初始化地图控件
        dataBinding.mvMap.onCreate(getIntent().getExtras());
        // 初始化list界面
        mAdapter = new SharedNavigationGoHereAdapterBackup(this);
        dataBinding.rvGuideList.setLayoutManager(new GridLayoutManager(this,3));
        dataBinding.rvGuideList.setAdapter(mAdapter);

        if (mAmap == null) {
            mAmap = dataBinding.mvMap.getMap();
        }
        // 路线搜索监听事件
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(mRouteSearchListener);

        // 逆地理将经纬度转为地址信息
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(mGeocodeSearch);

        if (mLat == 0.0 || mLng == 0.0){
            ToastUtils.showText(this,"获取信息失败");
            return;
        }
        mSearchLatLng = new LatLonPoint(mLat, mLng);
        mMyLatLng = new LatLonPoint(Constants.mLatitude,Constants.mLongitude);
        if ("".equals(mLocStr) || null == mLocStr) {
            // 根据经纬度来获取地址信息 去这里的信息 有的话直接拿 没有的话 就逆地理显示
            getAddress(mSearchLatLng);
        } else {
            dataBinding.tvGoLocation.setText(mLocStr);
        }

        initClickListener();

        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);

        // 开始计算驾车的情况的路线
        int strategyFlag = 0;
        try {

            hightspeed = SPUtils.getInt(SPUtils.GUIDE_GSYX)==1;
            avoidhightspeed = SPUtils.getInt(SPUtils.GUIDE_DBGS)==1;
            cost = SPUtils.getInt(SPUtils.GUIDE_DBSF)==1;
            congestion = SPUtils.getInt(SPUtils.GUIDE_DBYD)==1;

            strategyFlag = mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, true);
//            strategy = mAMapNavi.strategyConvert(
//                    mDialogDbyd == 1 ? true : false, // 躲避拥堵
//                    mDialogDbgs == 1 ? true : false, // 躲避高速
//                    mDialogDbsf == 1 ? true : false, // 躲避收费
//                    mDialogGsyx == 1 ? true : false, // 高速优先
//                    false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (strategyFlag >= 0) {
            startList.add(new NaviLatLng(Constants.mLatitude,Constants.mLongitude));
            endList.add(new NaviLatLng(mLat,mLng));
            mAMapNavi.calculateDriveRoute(startList, endList, null, strategyFlag);
        }
    }
    private void initClickListener(){
        dataBinding.rgRoute.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_car://驾车
                        if (!mIsDrivingRoute) { // 做重复点击的处理
                            mIsDrivingRoute = true;
                            int key = routeOverlays.keyAt(mPlanPosition);
                            mAMapNavi.selectRouteId(key);
                            selectRouteSurface();
                        }
                        break;
                    case R.id.rb_work://步行
                        if (mIsDrivingRoute) { // 做重复点击的处理
                            mIsDrivingRoute = false;
                            selectRouteSurface();
                            mAMapNavi.selectRouteId(-1);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private boolean mIsFirstClickCar = true;
    private boolean mIsFirstClickWalk = true;

    private void selectRouteSurface(){
        if (mIsDrivingRoute){ // 驾车
            if (mIsFirstClickCar) {
                showLoading();
                searchRouteResultCar(mMyLatLng, mSearchLatLng);
            } else {
                // 从缓存中获取数据
                getCacheCarInfo();
            }
        } else { // 步行
            if (mIsFirstClickWalk) {
                showLoading();
                searchRouteResultWalk(mMyLatLng, mSearchLatLng);
            } else {
                // 从缓存中获取数据
                getCacheWalkInfo();
            }
        }
    }

    /**
     * 方案选择
     * @param position
     */
    public void OnPlanClickListener(int position){
        if (mPlanPosition != position) {
            mListInfo.get(mPlanPosition).setIs_checked(false);
            mAdapter.notifyItemChanged(mPlanPosition);
            mListInfo.get(position).setIs_checked(true);
            mAdapter.notifyItemChanged(position);
            mPlanPosition = position;
            getCacheCarInfo();
            int key = routeOverlays.keyAt(position);
            mAMapNavi.selectRouteId(key);
        }
    }

    private int mPlanPosition = 0;

    private void getCacheCarInfo(){
        mAmap.clear();
        if (mListInfo != null){
            if (mListInfo.size()>0){
                int key = routeOverlays.keyAt(mPlanPosition);
                final AMapNaviPath drivePath = routeOverlays.get(key).getAMapNaviPath();
                mIsFirstClickCar = false;
                mIsEmpty = false;
                showEmpty();
                SharedGoHereDrivingRouteOverlayBackUp drivingRouteOverlay = new SharedGoHereDrivingRouteOverlayBackUp(
                        SharedNavigationGoHereActivityBackUp.this, mAmap, drivePath,
                        new NaviLatLng(Constants.mLatitude,Constants.mLongitude),
                        new NaviLatLng(mLat,mLng), null,true);
                drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                drivingRouteOverlay.setIsColorfulline(false);//是否用颜色展示交通拥堵情况，默认true
                drivingRouteOverlay.removeFromMap();
                drivingRouteOverlay.addToMap();
                drivingRouteOverlay.zoomToSpan();
                dataBinding.rvGuideList.setVisibility(View.VISIBLE);
                dataBinding.tvWorkContent.setVisibility(View.GONE);
                dataBinding.ivGoGuide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 进入导航界面
                        goGuideActivity();
                    }
                });
            }
        }
    }

    private void getCacheWalkInfo(){
        mAmap.clear();
        if (mWalkRouteResult != null) {
            final WalkPath walkPath = mWalkRouteResult.getPaths().get(0);
            mIsFirstClickWalk = false;
            if (walkPath == null) {
                mIsEmpty = true;
                showEmpty();
                return;
            }
            mIsEmpty = false;
            showEmpty();
            WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                    SharedNavigationGoHereActivityBackUp.this, mAmap, walkPath,
                    mWalkRouteResult.getStartPos(),
                    mWalkRouteResult.getTargetPos());
            walkRouteOverlay.removeFromMap();
            walkRouteOverlay.addToMap();
            walkRouteOverlay.zoomToSpan();
            dataBinding.tvWorkContent.setVisibility(View.VISIBLE);
            int dis = (int) walkPath.getDistance();
            int dur = (int) walkPath.getDuration();
            String des = AMapUtil.getFriendlyTime(dur) + "   " + AMapUtil.getFriendlyLength(dis);
            dataBinding.tvWorkContent.setText(des);
            dataBinding.rvGuideList.setVisibility(View.GONE);
            dataBinding.ivGoGuide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 进入导航界面
                    goGuideActivity();
                }
            });
        } else {
            mIsEmpty = true;
            showEmpty();
        }
    }

    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:// 返回
                finish();
                break;
            case R.id.iv_go_guide:// 开始导航
                // 进入导航界面
                goGuideActivity();
                break;
            default:
                break;
        }
    }

    /**
     * 进入导航界面
     */
    private void goGuideActivity(){
        if (!GPSUtils.isOPen(mContext)){
            //定位依然没有打开的处理
            ToastUtils.showText(mContext,"需要打开GPS才能正常使用");
            return;
        }
        // 进入导航界面
        IntentManager.getInstance().goSharedNavigationGuideActivityBackUp(SharedNavigationGoHereActivityBackUp.this, mIsDrivingRoute, mMyLatLng.getLatitude(), mMyLatLng.getLongitude(), mSearchLatLng.getLatitude(), mSearchLatLng.getLongitude(),mGroupId,routeOverlays.keyAt(mPlanPosition));
//        this.finish();
    }

    /**
     * 开始搜索路径规划方案 驾车
     */
    public void searchRouteResultCar(LatLonPoint mStartPoint, LatLonPoint mEndPoint) {
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartPoint, mEndPoint);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, 10, null, null, "");
        // 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式 大于等于10 的时候是多路径规划，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
        mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
    }
    /**
     * 开始搜索路径规划方案 步行
     */
    public void searchRouteResultWalk(LatLonPoint mStartPoint, LatLonPoint mEndPoint) {
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartPoint, mEndPoint);
        RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo);
        mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
    }

    /**
     * 路径规划搜索监听
     */
    RouteSearch.OnRouteSearchListener mRouteSearchListener = new RouteSearch.OnRouteSearchListener() {
        @Override
        public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
            // 公交路径
        }

        @Override
        public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
            // 驾车路径
//            mAmap.clear();// 清理地图上的所有覆盖物
//            if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
//                if (result != null && result.getPaths() != null) {
//                    if (result.getPaths().size() > 0) {
//                        mDriveRouteResult = result;
//                        final DrivePath drivePath = mDriveRouteResult.getPaths().get(0);
//                        if(drivePath == null) {
//                            mIsEmpty = true;
//                            showEmpty();
//                            return;
//                        }
//                        mIsFirstClickCar = false;
//                        mIsEmpty = false;
//                        showEmpty();
//                        mListInfo.clear();
//                        for (int i = 0; i < mDriveRouteResult.getPaths().size(); i++){ // 好像每次都只有一个数据结果 需要做缓存 下一次直接切换
//                            mListInfo.add(new SharedNavigationGoHereBean(mDriveRouteResult.getPaths().get(i),i == 0 ? true : false));
//                        }
//                        mAdapter.setDatas(mListInfo);
//                        hideLoading();
//                        SharedGoHereDrivingRouteOverlay drivingRouteOverlay = new SharedGoHereDrivingRouteOverlay(
//                                SharedNavigationGoHereActivityBackUp.this, mAmap, drivePath,
//                                mDriveRouteResult.getStartPos(),
//                                mDriveRouteResult.getTargetPos(), null,true);
//                        drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
//                        drivingRouteOverlay.setIsColorfulline(false);//是否用颜色展示交通拥堵情况，默认true
//                        drivingRouteOverlay.removeFromMap();
//                        drivingRouteOverlay.addToMap();
//                        drivingRouteOverlay.zoomToSpan();
//                        dataBinding.rvGuideList.setVisibility(View.VISIBLE);
//                        dataBinding.tvWorkContent.setVisibility(View.GONE);
//                        dataBinding.ivGoGuide.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                // 进入导航界面
//                                goGuideActivity();
//                            }
//                        });
//                    } else if (result != null && result.getPaths() == null) {
//                        mIsEmpty = true;
//                        showEmpty();
//                    }
//                } else {
//                    mIsEmpty = true;
//                    showEmpty();
//                }
//            } else {
//                mIsEmpty = true;
//                showEmpty();
//            }
        }

        @Override
        public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
            // 步行路径
            mAmap.clear();// 清理地图上的所有覆盖物
            if (errorCode == AMapException.CODE_AMAP_SUCCESS) { // 需要做缓存 下一次直接切换
                if (result != null && result.getPaths() != null) {
                    if (result.getPaths().size() > 0) {
                        mWalkRouteResult = result;
                        final WalkPath walkPath = mWalkRouteResult.getPaths().get(0);
                        mIsFirstClickWalk = false;
                        hideLoading();
                        if(walkPath == null) {
                            mIsEmpty = true;
                            showEmpty();
                            return;
                        }
                        mIsEmpty = false;
                        showEmpty();
                        WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                                SharedNavigationGoHereActivityBackUp.this, mAmap, walkPath,
                                mWalkRouteResult.getStartPos(),
                                mWalkRouteResult.getTargetPos());
                        walkRouteOverlay.removeFromMap();
                        walkRouteOverlay.addToMap();
                        walkRouteOverlay.zoomToSpan();
                        dataBinding.tvWorkContent.setVisibility(View.VISIBLE);
                        int dis = (int) walkPath.getDistance();
                        int dur = (int) walkPath.getDuration();
                        String des = AMapUtil.getFriendlyTime(dur)+"   "+AMapUtil.getFriendlyLength(dis);
                        dataBinding.tvWorkContent.setText(des);
                        dataBinding.rvGuideList.setVisibility(View.GONE);
                        dataBinding.ivGoGuide.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 进入导航界面
                                goGuideActivity();
                            }
                        });
                    } else if (result != null && result.getPaths() == null) {
                        mIsEmpty = true;
                        showEmpty();
                        mIsFirstClickWalk = false;
                        hideLoading();
                    }
                } else {
                    mIsEmpty = true;
                    showEmpty();
                    mIsFirstClickWalk = false;
                    hideLoading();
                }
            } else {
                mIsEmpty = true;
                showEmpty();
                mIsFirstClickWalk = false;
                hideLoading();
            }
        }

        @Override
        public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
            // 骑行路径
        }
    };

    private void showEmpty(){
        if (mIsEmpty) {
            dataBinding.rlEmpty.setVisibility(View.VISIBLE);
        } else {
            dataBinding.rlEmpty.setVisibility(View.GONE);
        }
    }
    /**
     * 逆地理搜索监听
     */
    GeocodeSearch.OnGeocodeSearchListener mGeocodeSearch = new GeocodeSearch.OnGeocodeSearchListener() {
        @Override
        public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
            if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                if (result != null && result.getRegeocodeAddress() != null
                        && result.getRegeocodeAddress().getFormatAddress() != null) {
                    RegeocodeAddress amapLocation = result.getRegeocodeAddress();
                    String adress = amapLocation.getFormatAddress().split(amapLocation.getTownship())[1];
                    PoiItem item = new PoiItem("",mSearchLatLng,adress,adress); //PointID
                    item.setProvinceName(amapLocation.getProvince()); //省
                    item.setCityName(amapLocation.getCity()); //市
                    item.setAdName(amapLocation.getDistrict()); //区
                    // 将得到的数据赋值
                    dataBinding.tvGoLocation.setText(adress);
//                    mInfoList.add(mSearchInfo);
//                    mBottomNearByAdapter.setDatas(mInfoList);
                } else {
                    // ToastUtil.show(AMapMapActivity.this, "对不起，我们没有搜到相关数据");
                }
            }
        }

        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        dataBinding.mvMap.onResume();

//        selectRouteSurface();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataBinding.mvMap.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataBinding.mvMap.onDestroy();
        EventBus.getDefault().unregister(this);
        setOnDestoryNullData();

        startList.clear();
        endList.clear();
        routeOverlays.clear();
        /**
         * 当前页面只是展示地图，activity销毁后不需要再回调导航的状态
         */
        mAMapNavi.removeAMapNaviListener(this);
        mAMapNavi.destroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        dataBinding.mvMap.onSaveInstanceState(outState);
    }

    /**
     * 需要还原之后才能在下一次进来的时候正常显示
     */
    private void setOnDestoryNullData() {
        mAmap.clear();
        mAmap = null;
        mIsDrivingRoute = false;
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    private void drawRoutes(int routeId, AMapNaviPath path) {
        calculateSuccess = true;
        mAmap.moveCamera(CameraUpdateFactory.changeTilt(0));
        RouteOverLay routeOverLay = new RouteOverLay(mAmap, path, this);
//        routeOverLay.setTrafficLine(false);
//        routeOverLay.zoomToSpan();
//        routeOverLay.setWayPointBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.map_line_info_green));
//        routeOverLay.addToMap();
        routeOverlays.put(routeId, routeOverLay);
        if (routeOverlays.size() == mAMapNavi.getNaviPaths().size()){
            mIsFirstClickCar = false;
            mIsEmpty = false;
            showEmpty();
            mListInfo.clear();
            for (int i = 0; i < routeOverlays.size(); i++){ // 好像每次都只有一个数据结果 需要做缓存 下一次直接切换
//            for (int i = routeOverlays.size()-1; i >= 0; i--){ // 好像每次都只有一个数据结果 需要做缓存 下一次直接切换
                int key = routeOverlays.keyAt(i);
                mListInfo.add(new SharedNavigationGoHereBeanBack(routeOverlays.get(key).getAMapNaviPath(),i == 0 ? true : false));
            }
            mAdapter.setDatas(mListInfo);
            hideLoading();
            SharedGoHereDrivingRouteOverlayBackUp drivingRouteOverlay = new SharedGoHereDrivingRouteOverlayBackUp(
                    SharedNavigationGoHereActivityBackUp.this, mAmap, mListInfo.get(0).getItem(),
                    new NaviLatLng(Constants.mLatitude,Constants.mLongitude),
                    new NaviLatLng(mLat,mLng), null,true);
            drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
            drivingRouteOverlay.setIsColorfulline(false);//是否用颜色展示交通拥堵情况，默认true
            drivingRouteOverlay.removeFromMap();
            drivingRouteOverlay.addToMap();
            drivingRouteOverlay.zoomToSpan();
            int key = routeOverlays.keyAt(0);
            mAMapNavi.selectRouteId(key);
            dataBinding.rvGuideList.setVisibility(View.VISIBLE);
            dataBinding.tvWorkContent.setVisibility(View.GONE);
            dataBinding.ivGoGuide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 进入导航界面
                    goGuideActivity();
                }
            });
        }
    }

    @Override
    public void onCalculateRouteFailure(int i) {
        calculateSuccess = false;
        ToastUtils.makeText(getApplicationContext(), "计算路线失败，errorcode＝" + i, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
    //清空上次计算的路径列表。
        routeOverlays.clear();
        HashMap<Integer, AMapNaviPath> paths = mAMapNavi.getNaviPaths();
        for (int i = 0; i < ints.length; i++) {
            AMapNaviPath path = paths.get(ints[i]);
            if (path != null) {
                drawRoutes(ints[i], path);
            }
        }
    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getGuideMinTypeEventType(GuideMinTypeEvent event) {
        if (event.getType() == 1){ // stop navigation
            moveTaskToBack(true);
        }
    }

}