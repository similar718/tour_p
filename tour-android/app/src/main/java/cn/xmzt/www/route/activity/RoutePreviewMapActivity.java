package cn.xmzt.www.route.activity;

import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ActivityRoutePreviewMapBinding;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.route.adapter.RoutePreViewTripListAdapter;
import cn.xmzt.www.route.adapter.RoutePreviewDaysAdapter;
import cn.xmzt.www.route.vm.RoutePreviewViewModel;
import cn.xmzt.www.selfdrivingtools.bean.MapStartEndLatLngBean;
import cn.xmzt.www.selfdrivingtools.bean.SettingDestinationTypeInfo;
import cn.xmzt.www.selfdrivingtools.bean.SharedNavigationMapLineBean;
import cn.xmzt.www.selfdrivingtools.bean.SharedNavigationMapLinesBean;
import cn.xmzt.www.selfdrivingtools.bean.TourTripDetailNewBean;
import cn.xmzt.www.selfdrivingtools.overlay.SharedHomeDrivingRouteOverlay;
import cn.xmzt.www.utils.DensityUtil;
import cn.xmzt.www.utils.LocalDataUtils;
import cn.xmzt.www.utils.StatusBarUtil;
import cn.xmzt.www.view.CenterLayoutManager;
import cn.xmzt.www.view.ScrollGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 线路预览
 */
public class RoutePreviewMapActivity extends TourBaseActivity<RoutePreviewViewModel, ActivityRoutePreviewMapBinding> implements AMap.InfoWindowAdapter,AMap.OnMarkerClickListener,AMapLocationListener,LocationSource {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding.mapView.onCreate(savedInstanceState);// 此方法必须重写
    }
    @Override
    protected int setLayoutId() {
        return R.layout.activity_route_preview_map;
    }

    @Override
    protected RoutePreviewViewModel createViewModel() {
        viewModel = new RoutePreviewViewModel();
        viewModel.routePreviewMap.observe(this, new Observer<List<TourTripDetailNewBean.LineRouteListVOBean>>() {
            @Override
            public void onChanged(@Nullable List<TourTripDetailNewBean.LineRouteListVOBean> routePreviewBean) {
                // 获取得到线路数据
                if (routePreviewBean != null) {
                    if (routePreviewBean.size()>0){
                        mListData = routePreviewBean;
                        initBottomData();
                        getDestinationInfo();
                    }
                }
            }
        });
        return viewModel;
    }
    List<TourTripDetailNewBean.LineRouteListVOBean> mListData;
    private AMap mAmap=null;
    private UiSettings mUiSettings=null;
    private int lineId=0;
    private String lineName = "";
    @Override
    protected void initData() {
        StatusBarUtil.setStatusBarLightMode(this,true);
        Intent intent=getIntent();
        lineId=intent.getIntExtra("A",0);
        lineName = intent.getStringExtra("B");
        dataBinding.tvTopTxt.setText(lineName);
        dataBinding.setActivity(this);
        if (mAmap == null) {
            mAmap = dataBinding.mapView.getMap();
            //设置缩放级别
            mAmap.moveCamera(CameraUpdateFactory.zoomTo(15));
            mAmap.setOnMarkerClickListener(this);
            mAmap.setInfoWindowAdapter(this);
        }
        // 设置焦点到中心位置
        LatLng cLatLng=new LatLng(Constants.mLatitude,Constants.mLongitude);
        mAmap.moveCamera(CameraUpdateFactory.newLatLng(cLatLng));

        viewModel.getRoutedetail((long) lineId);

        // 设置定位监听
        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.showMyLocation(true);
        //连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.strokeColor(Color.argb(19, 73, 190, 255));// 设置圆形的边框颜色 rgba(73, 190, 255, 19)
        myLocationStyle.radiusFillColor(Color.argb(19, 73, 190, 255));// 设置圆形的填充颜色
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.shared_navigation_map_marker_mine_bottom_bg)));
        mAmap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        mUiSettings=mAmap.getUiSettings();
        mUiSettings.setMyLocationButtonEnabled(false);//设置默认定位按钮是否显示，非必需设置。
        mAmap.setMyLocationEnabled(true);
        mAmap.showBuildings(true);

        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(mRouteSearchListener);

        // init bottom day list recycleview
        mCenterLayoutManager = new CenterLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        dataBinding.rvDaysInfo.setLayoutManager(mCenterLayoutManager);
        mDaysAdapter = new RoutePreviewDaysAdapter(this);
        dataBinding.rvDaysInfo.setAdapter(mDaysAdapter);

        // init bottom trip list recycleview
        ScrollGridLayoutManager manager = new ScrollGridLayoutManager(this, 1, GridLayoutManager.VERTICAL,false);
        dataBinding.rvTripList.setLayoutManager(manager);
        mTripListAdapter = new RoutePreViewTripListAdapter(this);
        dataBinding.rvTripList.setAdapter(mTripListAdapter);

        initClick();
    }

    RouteSearch mRouteSearch = null;

    /**
     * start search line
     */
    public void searchRouteResult(LatLonPoint mStartPoint, LatLonPoint mEndPoint) {
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartPoint, mEndPoint);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_SHORTEST, null, null, "");
        mRouteSearch.calculateDriveRouteAsyn(query);
    }

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
                        } // 需要进行判断当前缓存信息的问题
                        SharedHomeDrivingRouteOverlay drivingRouteOverlay = new SharedHomeDrivingRouteOverlay(
                                RoutePreviewMapActivity.this, mAmap, drivePath,
                                mDriveRouteResult.getStartPos(),
                                mDriveRouteResult.getTargetPos(), null);
                        drivingRouteOverlay.setNodeIconVisibility(false);
                        drivingRouteOverlay.setIsColorfulline(false); // TODO
                        drivingRouteOverlay.removeFromMap();
                        drivingRouteOverlay.addToMap();
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

    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = LayoutInflater.from(this).inflate(R.layout.activity_route_preview_map_marker_spot, null);
        render(marker, infoWindow);
        return infoWindow;
    }
    /**
     * 自定义infowinfow窗口
     */
    public void render(Marker marker, View view) {
        ImageView iv_del=view.findViewById(R.id.iv_del);
        ImageView iv_scenic_spot=view.findViewById(R.id.iv_scenic_spot);
        TextView tv_scenic_spot_name=view.findViewById(R.id.tv_scenic_spot_name);
        tv_scenic_spot_name.setText(marker.getTitle());
        if (TextUtils.isEmpty(marker.getSnippet())){
            iv_scenic_spot.setImageResource(R.drawable.icon_default);
        } else {
            GlideUtil.loadImage(iv_scenic_spot,marker.getSnippet());
        }
        iv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marker.hideInfoWindow();
                mCurrentOtherMarker = null;
            }
        });
    }
    @Override
    public View getInfoContents(Marker marker) {
        View infoWindow = LayoutInflater.from(this).inflate(R.layout.activity_route_preview_map_marker_spot, null);
        render(marker, infoWindow);
        return infoWindow;
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        mCurrentOtherMarker = marker;
        return true;
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back: // 返回事件
                onBackPressed();
                break;
            case R.id.iv_bottom_to_up: // 顶部向上按键
                if (mIsBottomShow) {
                    setHideBottomView();
                    dataBinding.ivBottomToUp.setImageResource(R.drawable.shared_navigation_bottom_toup_icon);
                } else {
                    setShowBottomView();
                    dataBinding.ivBottomToUp.setImageResource(R.drawable.shared_navigation_bottom_todown_icon);
                }
                break;
            default:
                break;
        }
    }

    private boolean mIsBottomShow = false;

    /**
     * show bottom trip detail list anim
     */
    private void setShowBottomView() {
        android.view.animation.Animation animBottomOut = AnimationUtils.loadAnimation(RoutePreviewMapActivity.this, R.anim.map_list_bottom_in);
        animBottomOut.setDuration(240);
        dataBinding.rvTripList.setVisibility(View.VISIBLE);
        dataBinding.rvTripList.startAnimation(animBottomOut);
        mIsBottomShow = true;
    }

    /**
     * hide bottom trip detail list anim
     */
    private void setHideBottomView() {
        android.view.animation.Animation animBottomOut = AnimationUtils.loadAnimation(RoutePreviewMapActivity.this, R.anim.map_list_bottom_out);
        animBottomOut.setDuration(240);
        dataBinding.rvTripList.setVisibility(View.GONE);
        dataBinding.rvTripList.startAnimation(animBottomOut);
        mIsBottomShow = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataBinding.mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataBinding.mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TODO 需要将这个置空 下一次进来才会正常加载
        mAmap.clear();
        mRouteSearch = null;
        mAmap = null;
        dataBinding.mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        dataBinding.mapView.onSaveInstanceState(outState);
    }
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                // 设置显示的焦点，即当前地图显示为当前位置
//                mAmap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
            }
        }
    }

    /**
     * bottom trip detail list blue txt click 2 stage 6 live 7 scenic spot not use type
     * 10 早 11 午 12 晚
     */
    public void setBottomBlueTextClick(double lat, double lng, int position, int type) {
        setMapCenterLocation(lat, lng);
        setHideBottomView();
        dataBinding.ivBottomToUp.setImageResource(R.drawable.shared_navigation_bottom_toup_icon);
        for (int i = 0; i < mAllLineMarker.size(); i++) {
            if (mAllLineMarker.get(i).getPosition().latitude == lat && mAllLineMarker.get(i).getPosition().longitude == lng) {
                Marker marker = mAllLineMarker.get(i);
                mCurrentOtherMarker = marker;
                marker.showInfoWindow();
                marker.setToTop(); // 显示在最顶部
                break;
            }
        }
    }

    private List<Marker> mAllLineMarker = new ArrayList<>();

    private int mInitMapZoom = 19;
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

    /**
     * init bottom view
     */
    private void initBottomData() {
        mTripListAdapter.setData(mListData);

        mTripDaysListInfo = LocalDataUtils.getTripDateDaysInfo(mListData.size());
        mDaysAdapter.setDatas(mTripDaysListInfo);
    }

    private int mBottomDaysPosition = 0;
    private RoutePreviewDaysAdapter mDaysAdapter;
    private RoutePreViewTripListAdapter mTripListAdapter;
    private Marker mCurrentOtherMarker = null;
    List<SettingDestinationTypeInfo> mTripDaysListInfo = null;

    private CenterLayoutManager mCenterLayoutManager;

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

    // Full time display
    private List<SharedNavigationMapLineBean> mDestinationLatLngAllList = new ArrayList<>();
    private List<MapStartEndLatLngBean> mStartEndLatLng = new ArrayList<>();
    // Get information by day based on information
    private List<SharedNavigationMapLinesBean> mDestinationList = new ArrayList<>();
    // Judge if it's the day to use this
    private List<SharedNavigationMapLineBean> mDestinationLatLngList = new ArrayList<>();

    private void initSurfacePolyAllLine() {
        List<LatLng> list = new ArrayList<>();
        for (int i = 0; i < mDestinationLatLngAllList.size(); i++) {
            list.add(mDestinationLatLngAllList.get(i).getBeans());
            addLineMarkersToMap(this, mDestinationLatLngAllList.get(i).getBeans(), mDestinationLatLngAllList.get(i).getTitle(),
                    mDestinationLatLngAllList.get(i).getContent(),
                    mDestinationLatLngAllList.get(i).getUrl(),
                    getMarkerTypeIcon(mDestinationLatLngAllList.get(i).getType()));
        }
        mStartEndLatLng.clear();
        for (int j = 0; j < list.size() - 1; j++) {
            mStartEndLatLng.add(new MapStartEndLatLngBean(new LatLonPoint(list.get(j).latitude, list.get(j).longitude), new LatLonPoint(list.get(j + 1).latitude, list.get(j + 1).longitude)));
        }
        for (int j = 0; j < list.size() - 1; j++) {
            searchRouteResult(new LatLonPoint(list.get(j).latitude, list.get(j).longitude), new LatLonPoint(list.get(j + 1).latitude, list.get(j + 1).longitude));
        }
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
                        bean.getBean().get(p).getContent(),
                        bean.getBean().get(p).getUrl(),
                        getMarkerTypeIcon(bean.getBean().get(p).getType()));
            }
        } else {
            SharedNavigationMapLinesBean beanfore = mDestinationList.get(day - 2);
            mDestinationLatLngList.clear();
            int position = 0;
            int len = beanfore.getBean().size()-1;
            list.add(beanfore.getBean().get(len).getBeans());
            addLineMarkersToMap(this, beanfore.getBean().get(len).getBeans(), beanfore.getBean().get(len).getTitle(),
                    beanfore.getBean().get(len).getContent(),
                    beanfore.getBean().get(len).getUrl(),
                    getMarkerTypeIcon(beanfore.getBean().get(len).getType()));
            mDestinationLatLngList.add(beanfore.getBean().get(len));
            SharedNavigationMapLinesBean bean = mDestinationList.get(day - 1);
            for (int p = 0; p < bean.getBean().size(); p++) {
                position += 1;
                mDestinationLatLngList.add(bean.getBean().get(p));
                list.add(bean.getBean().get(p).getBeans());
                addLineMarkersToMap(this, bean.getBean().get(p).getBeans(), bean.getBean().get(p).getTitle(),
                        bean.getBean().get(p).getContent(),
                        bean.getBean().get(p).getUrl(),
                        getMarkerTypeIcon(bean.getBean().get(p).getType()));
            }
        }
        for (int j = 0; j < list.size() - 1; j++) {
            searchRouteResult(new LatLonPoint(list.get(j).latitude, list.get(j).longitude), new LatLonPoint(list.get(j + 1).latitude, list.get(j + 1).longitude));
        }
        // update map zoom
        setMapBounds(list);
    }

    /**
     * line marker
     */
    public void addLineMarkersToMap(Context context, LatLng latlng, String title,String content,String url, int res) {
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
            MarkerOptions markerOption = new MarkerOptions()
                    .position(latlng)
                    .title(content)
                    .snippet(url)
                    .setInfoWindowOffset(0, radius)
                    .anchor(0.5f, 0.78f)
                    .icon(BitmapDescriptorFactory.fromView(view));
            Marker marker = mAmap.addMarker(markerOption);
//            marker.setObject();
            // save line marker
            mAllLineMarker.add(marker);
        }
    }

    private int getMarkerTypeIcon(int type) {
        int res = 0;
        switch (type) {
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

    private List<SharedHomeDrivingRouteOverlay> mRouteLines = new ArrayList<>();
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

    private void getDestinationInfo() {
        mDestinationList.clear();
        for (int i = 0; i < mListData.size(); i++) {
            int position = 1;
            List<SharedNavigationMapLineBean> beans = new ArrayList<>();
            // maybe more scenic spot
            if (mListData.get(i).getDetailVOList() != null) {
                if (i == 0) {
                    for (int j = 0; j < mListData.get(i).getDetailVOList().size(); j++) { // 集结点第一天第一个
                        if (mListData.get(i).getDetailVOList().get(j).getDataType() == 5) { // 集结点
                            if (mListData.get(i).getDetailVOList().get(j).getStaging().getCoordinateVO() != null) {
                                String data = mListData.get(i).getDetailVOList().get(j).getStaging().getAddress();
                                if (TextUtils.isEmpty(data)) {
                                    if (mListData.get(i).getDetailVOList().get(j).getStaging().getAreaVO() != null) {
                                        data = mListData.get(i).getDetailVOList().get(j).getStaging().getAreaVO().getAreaName();
                                    } else {
                                        data = "无";
                                    }
                                }
                                beans.add(new SharedNavigationMapLineBean(
                                        new LatLng(mListData.get(i).getDetailVOList().get(j).getStaging().getCoordinateVO().getLatitude(),
                                                mListData.get(i).getDetailVOList().get(j).getStaging().getCoordinateVO().getLongitude()),
                                        (i + 1) + "-" + position,
                                        data,"", 3));// 集结点
                                position += 1;
                            }
                        }
                    }
                }
                for (int j = 0; j < mListData.get(i).getDetailVOList().size(); j++) {
                    if (mListData.get(i).getDetailVOList().get(j).getDataType() == 2){ // 景点
                        if (mListData.get(i).getDetailVOList().get(j).getSpot().getCoordinateVO() != null) {
                            beans.add(new SharedNavigationMapLineBean(
                                    new LatLng(mListData.get(i).getDetailVOList().get(j).getSpot().getCoordinateVO().getLatitude(),
                                            mListData.get(i).getDetailVOList().get(j).getSpot().getCoordinateVO().getLongitude()),
                                    (i + 1) + "-" + position,
                                    mListData.get(i).getDetailVOList().get(j).getSpot().getTitle(),
                                    mListData.get(i).getDetailVOList().get(j).getSpot().getImageArray() != null ? mListData.get(i).getDetailVOList().get(j).getSpot().getImageArray().get(0) : "",
                                    0)); // spot
                            position += 1;
                        }
                    }

                    if (mListData.get(i).getDetailVOList().get(j).getDataType() == 4){ // 住宿
                        if (mListData.get(i).getDetailVOList().get(j).getHotel().getCoordinateVO() != null) {
                            beans.add(new SharedNavigationMapLineBean(
                                    new LatLng(mListData.get(i).getDetailVOList().get(j).getHotel().getCoordinateVO().getLatitude(),
                                            mListData.get(i).getDetailVOList().get(j).getHotel().getCoordinateVO().getLongitude()),
                                    (i + 1) + "-" + position,
                                    mListData.get(i).getDetailVOList().get(j).getHotel().getAccommodation(), "",1));// live
                            position += 1;
                        }
                    }

                    if (mListData.get(i).getDetailVOList().get(j).getDataType() == 8){ // 餐点 早
                        if (mListData.get(i).getDetailVOList().get(j).getBreakfast().getCoordinateVO() != null) {
                            String data = mListData.get(i).getDetailVOList().get(j).getBreakfast().getMeal();
                            if (TextUtils.isEmpty(data)){
                                data = "自理";
                            }
                            beans.add(new SharedNavigationMapLineBean(
                                    new LatLng(mListData.get(i).getDetailVOList().get(j).getBreakfast().getCoordinateVO().getLatitude(),
                                            mListData.get(i).getDetailVOList().get(j).getBreakfast().getCoordinateVO().getLongitude()),
                                    (i + 1) + "-" + position,
                                    data, "",2));// 餐点
                            position += 1;
                        }
                    }

                    if (mListData.get(i).getDetailVOList().get(j).getDataType() == 9){ // 餐点 中
                        if (mListData.get(i).getDetailVOList().get(j).getLunch().getCoordinateVO() != null) {
                            String data = mListData.get(i).getDetailVOList().get(j).getLunch().getMeal();
                            if (TextUtils.isEmpty(data)){
                                data = "自理";
                            }
                            beans.add(new SharedNavigationMapLineBean(
                                    new LatLng(mListData.get(i).getDetailVOList().get(j).getLunch().getCoordinateVO().getLatitude(),
                                            mListData.get(i).getDetailVOList().get(j).getLunch().getCoordinateVO().getLongitude()),
                                    (i + 1) + "-" + position,
                                    data, "",2));// 餐点
                            position += 1;
                        }
                    }

                    if (mListData.get(i).getDetailVOList().get(j).getDataType() == 10){ // 餐点 晚
                        if (mListData.get(i).getDetailVOList().get(j).getDinner().getCoordinateVO() != null) {
                            String data = mListData.get(i).getDetailVOList().get(j).getDinner().getMeal();
                            if (TextUtils.isEmpty(data)){
                                data = "自理";
                            }
                            beans.add(new SharedNavigationMapLineBean(
                                    new LatLng(mListData.get(i).getDetailVOList().get(j).getDinner().getCoordinateVO().getLatitude(),
                                            mListData.get(i).getDetailVOList().get(j).getDinner().getCoordinateVO().getLongitude()),
                                    (i + 1) + "-" + position,
                                    data, "",2));// 餐点
                            position += 1;
                        }
                    }
                }
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
     * request all marker LatLng
     *
     * @return
     */
    private List<LatLng> getAllMarkerLatlngData() {
        List<LatLng> list = new ArrayList<>();
        for (int i = 0; i < mListData.size(); i++) {
            if (i == 0){
                if (mListData.get(i).getDetailVOList() != null) {
                    for (int j = 0; j < mListData.get(i).getDetailVOList().size(); j++) {
                        if (mListData.get(i).getDetailVOList().get(j).getDataType() == 5) { // 集结点
                            if (mListData.get(i).getDetailVOList().get(j).getStaging().getCoordinateVO() != null) {
                                list.add(new LatLng(mListData.get(i).getDetailVOList().get(j).getStaging().getCoordinateVO().getLatitude(),
                                        mListData.get(i).getDetailVOList().get(j).getStaging().getCoordinateVO().getLongitude())); // 第一天的集结点
                            }
                        }
                    }
                }
            }
            if (mListData.get(i).getDetailVOList() != null) {
                for (int j = 0; j < mListData.get(i).getDetailVOList().size(); j++) {
                    if (mListData.get(i).getDetailVOList().get(j).getDataType() == 2){ // 景点
                        if (mListData.get(i).getDetailVOList().get(j).getSpot().getCoordinateVO() != null) {
                            list.add(new LatLng(mListData.get(i).getDetailVOList().get(j).getSpot().getCoordinateVO().getLatitude(),
                                    mListData.get(i).getDetailVOList().get(j).getSpot().getCoordinateVO().getLongitude())); // 景点
                        }
                    }
                    if (mListData.get(i).getDetailVOList().get(j).getDataType() == 4){ // 住宿
                        if (mListData.get(i).getDetailVOList().get(j).getHotel().getCoordinateVO() != null) {
                            list.add(new LatLng(mListData.get(i).getDetailVOList().get(j).getHotel().getCoordinateVO().getLatitude(),
                                    mListData.get(i).getDetailVOList().get(j).getHotel().getCoordinateVO().getLongitude())); // 酒店
                        }
                    }
                    if (mListData.get(i).getDetailVOList().get(j).getDataType() == 8){ // 早餐
                        if (mListData.get(i).getDetailVOList().get(j).getBreakfast().getCoordinateVO() != null) {
                            list.add(new LatLng(mListData.get(i).getDetailVOList().get(j).getBreakfast().getCoordinateVO().getLatitude(),
                                    mListData.get(i).getDetailVOList().get(j).getBreakfast().getCoordinateVO().getLongitude())); // 酒店
                        }
                    }
                    if (mListData.get(i).getDetailVOList().get(j).getDataType() == 9){ // 午餐
                        if (mListData.get(i).getDetailVOList().get(j).getLunch().getCoordinateVO() != null) {
                            list.add(new LatLng(mListData.get(i).getDetailVOList().get(j).getLunch().getCoordinateVO().getLatitude(),
                                    mListData.get(i).getDetailVOList().get(j).getLunch().getCoordinateVO().getLongitude())); // 酒店
                        }
                    }
                    if (mListData.get(i).getDetailVOList().get(j).getDataType() == 10){ // 晚餐
                        if (mListData.get(i).getDetailVOList().get(j).getDinner().getCoordinateVO() != null) {
                            list.add(new LatLng(mListData.get(i).getDetailVOList().get(j).getDinner().getCoordinateVO().getLatitude(),
                                    mListData.get(i).getDetailVOList().get(j).getDinner().getCoordinateVO().getLongitude())); // 酒店
                        }
                    }
                }
            }
        }
        return list;
    }

    private void initClick() {
        // 地图触摸事件
        dataBinding.mapView.getMap().setOnMapTouchListener(new AMap.OnMapTouchListener() {
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
        if (mCurrentOtherMarker != null){
            mCurrentOtherMarker.hideInfoWindow();
            mCurrentOtherMarker = null;
        }
    }
}
