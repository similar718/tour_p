package cn.xmzt.www.selfdrivingtools.activity;

import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.blankj.utilcode.util.ActivityUtils;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ActivitySharedNavigationSettingDestinationBinding;
import cn.xmzt.www.dialog.SettingDestinationSearchResultDialog;
import cn.xmzt.www.dialog.SharedNavigationDatePickerDialog;
import cn.xmzt.www.selfdrivingtools.adapter.SettingDestinationListAdapter;
import cn.xmzt.www.selfdrivingtools.adapter.SettingDestinationListSearchAdapter;
import cn.xmzt.www.selfdrivingtools.adapter.SettingDestinationListTripAdapter;
import cn.xmzt.www.selfdrivingtools.adapter.SettingDestinationTypeAdapter;
import cn.xmzt.www.selfdrivingtools.bean.GetTripDaysInfo;
import cn.xmzt.www.selfdrivingtools.bean.SettingDestinationBottomBean;
import cn.xmzt.www.selfdrivingtools.bean.SettingDestinationSureInfo;
import cn.xmzt.www.selfdrivingtools.bean.SettingDestinationTypeInfo;
import cn.xmzt.www.selfdrivingtools.overlay.AMapUtil;
import cn.xmzt.www.selfdrivingtools.viewmodel.SharedNavigationSettingDestinationViewModel;
import cn.xmzt.www.utils.LocalDataUtils;
import cn.xmzt.www.utils.StatusBarUtil;
import cn.xmzt.www.utils.TimeUtil;
import cn.xmzt.www.utils.ToastUtils;
import cn.xmzt.www.view.listener.TextChangedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * setting destination
 */
public class SharedNavigationSettingDestinationActivity extends TourBaseActivity<SharedNavigationSettingDestinationViewModel, ActivitySharedNavigationSettingDestinationBinding> {

    private AMap mAmap;
    // type Adapter
    SettingDestinationTypeAdapter mTypeAdapter = null;
    // type data
    List<SettingDestinationTypeInfo> mTypeInfo = null;
    // near by location data
    List<SettingDestinationBottomBean> mInfoList = new ArrayList<>();
    List<GetTripDaysInfo.PointListBean> mTripList = new ArrayList<>();
    List<SettingDestinationBottomBean> mSearchList = new ArrayList<>();

    //page center Marker
    Marker locationMarker;

    PoiSearch poiSearch = null;
    String keyWord = "";
    // Get specific address information by latitude and longitude
    GeocodeSearch geocoderSearch;

    LatLonPoint mSearchLatLng = null;

    PoiItem mSearchInfo = null;
    int currentPage = 0;
    PoiSearch.Query query = null;
    SettingDestinationSureInfo mSendInfo = null;
    private int mInitMapZoom = 15;

    SettingDestinationListAdapter mBottomNearByAdapter = null;
    SettingDestinationListTripAdapter mBottomTodayTripAdapter = null;
    SettingDestinationListSearchAdapter mSearchListAdapter = null;

    LocationSource.OnLocationChangedListener mListener;
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;

    PoiItem mLocationInfo = null;

    SettingDestinationSearchResultDialog mDialog = null;

    private int mLineId = 0;

    private SharedNavigationSettingDestinationActivity mContext = null;

    private GetTripDaysInfo mTripInfo = null;

    private int mDayNum = 0;

    private String mStagingTime = "";

    @Override
    protected int setLayoutId() {
        return R.layout.activity_shared_navigation_setting_destination;
    }

    @Override
    protected SharedNavigationSettingDestinationViewModel createViewModel() {
        mContext = this;
        viewModel = new SharedNavigationSettingDestinationViewModel();
        viewModel.setIView(this);
        
        viewModel.mTripInfo.observe(mContext, new Observer<GetTripDaysInfo>() {
            @Override
            public void onChanged(@Nullable GetTripDaysInfo info) {
                if (info != null) {
                    mTripInfo = info;
                    setDayInfo();
                }
            }
        });
        return viewModel;
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        dataBinding.setActivity(this);

        StatusBarUtil.setStatusBarLightMode(this,true);

        mLineId = getIntent().getIntExtra("lineId",0);
        mDayNum = getIntent().getIntExtra("daynum",0);

        mStagingTime = getIntent().getStringExtra("stagingTime");
        if (mLineId == 0){
            ToastUtils.showText(mContext,"获取线路失败");
        } else {
            viewModel.getTripPoint(TourApplication.getToken(),mLineId);
        }
        // init map
        dataBinding.mvMap.onCreate(getIntent().getExtras());

        if (mAmap == null) {
            mAmap = dataBinding.mvMap.getMap();
            setUpMap();
        }

        // search listener
        dataBinding.etSearch.addTextChangedListener(mTextWatcher);

        // init bottom type
        dataBinding.rvTypeList.setLayoutManager(new GridLayoutManager(this,2));
        mTypeAdapter = new SettingDestinationTypeAdapter(this);
        mTypeInfo = LocalDataUtils.getSettingDestinationTypeInfo();
        mTypeAdapter.setDatas(mTypeInfo);
        dataBinding.rvTypeList.setAdapter(mTypeAdapter);

        // init near by view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        dataBinding.rvLocationList.setLayoutManager(layoutManager);
        mBottomNearByAdapter = new SettingDestinationListAdapter(this);
        dataBinding.rvLocationList.setAdapter(mBottomNearByAdapter);

        // init day trip view
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        dataBinding.rvTripList.setLayoutManager(layoutManager1);
        mBottomTodayTripAdapter = new SettingDestinationListTripAdapter(this);
        dataBinding.rvTripList.setAdapter(mBottomTodayTripAdapter);

        // search list
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        dataBinding.rvSearchList.setLayoutManager(layoutManager2);
        mSearchListAdapter = new SettingDestinationListSearchAdapter(this);
        dataBinding.rvSearchList.setAdapter(mSearchListAdapter);

        initMapData();

        initTimerPicker();

        initClick();
    }

    private void initClick(){
        dataBinding.etSearch.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);//获取当前界面可视部分
                int screenHeight = mContext.getWindow().getDecorView().getRootView().getHeight();//获取屏幕高度
                int heiDifference = screenHeight - rect.bottom;//获取键盘高度，键盘没有弹出时，高度为0，键盘弹出时，高度为正数
                if (heiDifference == 0) {
                    //todo:键盘没有弹出时
                } else {
                    //todo：键盘弹出时
                }
            }
        });
    }

    private boolean hideInputView() {
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive(dataBinding.etSearch)) {
            inputMethodManager.hideSoftInputFromWindow(mContext.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            return true;
        }
        return false;
    }

    private boolean isSettingAdapter = false;

    private void setDayInfo(){
        if (mTripInfo != null){
            if (mSearchInfo != null){
                isSettingAdapter = true;
                for (int i = 0; i < mTripInfo.getPointList().size(); i++){
                    if (mDayNum == mTripInfo.getPointList().get(i).getDayNum()) {
                        if (!"".equals(mTripInfo.getPointList().get(i).getTitle()) && null != mTripInfo.getPointList().get(i).getTitle()) { // If I get something without title, I won't add it to the interface.
                            mTripList.add(mTripInfo.getPointList().get(i));
                        }
                    }
                }
                mBottomTodayTripAdapter.setDatas(mTripList);
            }
        }
    }

    /**
     * set map info
     */
    private void setUpMap() {
        mAmap.setLocationSource(mLocationSource);// setting location listener
        mAmap.getUiSettings().setMyLocationButtonEnabled(false);
        mAmap.setMyLocationEnabled(true);
        mAmap.setMyLocationType(AMap.MAP_TYPE_NORMAL);
        mAmap.moveCamera(CameraUpdateFactory.zoomTo(mInitMapZoom));

        mLocationOption.setOnceLocation(true);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
    }

    LocationSource mLocationSource = new LocationSource() {
        @Override
        public void activate(OnLocationChangedListener onLocationChangedListener) {
            mListener = onLocationChangedListener;
            if (mlocationClient == null) {
                mlocationClient = new AMapLocationClient(SharedNavigationSettingDestinationActivity.this);
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
                    LatLng latLng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                    String adress = amapLocation.getAddress().split(amapLocation.getDistrict())[1];
                    PoiItem item = new PoiItem("",new LatLonPoint(amapLocation.getLatitude(),amapLocation.getLongitude()),amapLocation.getAoiName(),adress); //PointID
                    item.setProvinceName(amapLocation.getProvince()); //province
                    item.setCityName(amapLocation.getCity()); // city
                    item.setAdName(amapLocation.getDistrict()); // area
                    mLocationInfo = item;
                    if (locationMarker == null) {
                        addGrowMarker(latLng);
//                        addMarkerInScreenCenter();
                        searchNeayBy(latLng);
                    }
                } else {
                    if (ActivityUtils.getTopActivity() == mContext) {
                        ToastUtils.showText(SharedNavigationSettingDestinationActivity.this, "定位失败，请打开位置权限");
                        String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                        Log.e("AmapErr", errText);
                    }
                }
            }
        }
    };
    /**
     * add marker
     */
    public void addGrowMarker(LatLng latlng) {
        if(locationMarker == null) {
            locationMarker = mAmap.addMarker(new MarkerOptions().position(latlng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.item_ticket_detail_recyleview_line))
                    .anchor(0.5f, 0.5f));
            mAmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
        } else {
            // add Marker,need set title,if you not set title then can hide marker
            MarkerOptions options = new MarkerOptions();
            options.title("").position(latlng).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.dialog_scenic_content_address_icon)));
            Marker marker = mAmap.addMarker(options);
        }
    }

    private void initMapData(){
        UiSettings uiSettings = mAmap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER);
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setTiltGesturesEnabled(false);
        mAmap.showMapText(true);
        mAmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Constants.mLatitude, Constants.mLongitude), mInitMapZoom));

        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(mGeocodeSearch);

        mAmap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition position) {

			}

			@Override
			public void onCameraChangeFinish(CameraPosition postion) {
//				startJumpAnimation();
				if (mBottomTypePosition == 0) {
                    mSearchLatLng = new LatLonPoint(postion.target.latitude, postion.target.longitude);
                    if ("".equals(mSearchStr) || null == mSearchStr) {
                        getAddress(mSearchLatLng);
                    } else {
                        mSearchStr = "";
                    }
                    searchNeayBy(postion.target);
                }
			}
		});
//        addMarkerInScreenCenter();
        searchNeayBy(new LatLng(Constants.mLatitude,Constants.mLongitude));

        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(mRouteSearchListener);
    }

    RouteSearch mRouteSearch = null;
    private String mSearchStr = "";

    /**
	 * page center marker jump
	 */
//	public void startJumpAnimation() {
//		if (locationMarker != null ) {
//			final LatLng latLng = locationMarker.getPosition();
//			Point point =  mAmap.getProjection().toScreenLocation(latLng);
//		} else {
//			Log.e("amap","screenMarker is null");
//		}
//	}
    /**
     * page center add Marker
     */
//    private void addMarkerInScreenCenter() {
//        if (locationMarker == null) {
//            locationMarker = mAmap.addMarker(new MarkerOptions().zIndex(2)
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.shared_navigation_setting_destination_maker_icon)));
//        }
//        locationMarker.setAnchor(0.5f, 1.0f);
//        LatLng latLng = mAmap.getCameraPosition().target;
//        Point screenPosition = mAmap.getProjection().toScreenLocation(latLng);
//        locationMarker.setPositionByPixels(screenPosition.x, screenPosition.y);
//        locationMarker.setClickable(false);
//    }

    public void onClick(View view) {
        switch (view.getId()) {
            case cn.xmzt.www.R.id.title_back_iv: // return
                finish();
                break;
            case cn.xmzt.www.R.id.iv_search: // sure or search
                String data = dataBinding.etSearch.getText().toString();
                if ("".equals(data) || null == data){
                    // return have data
//                    finishMapPageBackDate();
                } else { // start search
                    if (mSearchList != null) {
                        if (mSearchList.size()>0) {
                            showScenicContentDialog();
                        } else{
                            ToastUtils.showText(SharedNavigationSettingDestinationActivity.this,"搜索数据为空");
                        }
                    } else {
                        ToastUtils.showText(SharedNavigationSettingDestinationActivity.this,"搜索数据为空");
                    }
                    dataBinding.etSearch.setText("");
                }
                break;
            case cn.xmzt.www.R.id.tv_search_content: // start search
                if (mSearchList != null) {
                    if (mSearchList.size()>0) {
                        showScenicContentDialog();
                    } else{
                        ToastUtils.showText(SharedNavigationSettingDestinationActivity.this,"搜索数据为空");
                    }
                } else {
                    ToastUtils.showText(SharedNavigationSettingDestinationActivity.this,"搜索数据为空");
                }
                dataBinding.etSearch.setText("");
                break;
            case cn.xmzt.www.R.id.delete_iv: // search et set empty
                dataBinding.etSearch.setText(""); // set empty
                // 判断当前界面的键盘是否显示  显示就隐藏
                hideInputView();
                break;
            case cn.xmzt.www.R.id.iv_my_location: // click myLoc
                setMapCenterToMineLocation();
                break;
            default:
                break;
        }
    }

    private int mBottomNearbyPosition = 0;
    private int mBottomTodayTripPosition = 0;
    private int mBottomTypePosition = 0;

    /**
     * list click
     * @param position
     * @param type 0
     */
    public void OnTypeClickListener(int position,int type){
        if (type != 0){ // type = 1 bottom list click
            if (type == 1) { // near by
//                if (position != mBottomNearbyPosition) {
//                    mInfoList.get(mBottomNearbyPosition).setIs_checked(false);
//                    mBottomNearByAdapter.notifyItemChanged(mBottomNearbyPosition);
//                    mInfoList.get(position).setIs_checked(true);
//                    mBottomNearByAdapter.notifyItemChanged(position);
//                    mBottomNearbyPosition = position;
                    // maybe send info
                    mSendInfo = new SettingDestinationSureInfo(mInfoList.get(position).getItem().getTitle(),
                            mInfoList.get(position).getItem().getLatLonPoint().getLatitude(),
                            mInfoList.get(position).getItem().getLatLonPoint().getLongitude());
                    mDestinationStr = mInfoList.get(position).getItem().getTitle();
                    settingTimeDialog(mInfoList.get(position).getItem().getLatLonPoint().getLatitude(),
                            mInfoList.get(position).getItem().getLatLonPoint().getLongitude());
//                }
            } else if (type == 2){ // days trip
//                if (position != mBottomTodayTripPosition) {
//                    mTripList.get(mBottomTodayTripPosition).setIs_checked(false);
//                    mBottomTodayTripAdapter.notifyItemChanged(mBottomTodayTripPosition);
//                    mTripList.get(position).setIs_checked(true);
//                    mBottomTodayTripAdapter.notifyItemChanged(position);
//                    mBottomTodayTripPosition = position;
                    // maybe send info
                    mSendInfo = new SettingDestinationSureInfo(mTripList.get(position).getTitle(),
                            mTripList.get(position).getCoordinate().getLatitude(),
                            mTripList.get(position).getCoordinate().getLongitude());

                    mDestinationStr = mInfoList.get(position).getItem().getTitle();
                    settingTimeDialog(mTripList.get(position).getCoordinate().getLatitude(),
                            mTripList.get(position).getCoordinate().getLongitude());
//                }
            } else if (type == 3){ // search result page click
                hideInputView();
                showNearByInfo();
                // request click data
                SettingDestinationBottomBean bean = mSearchList.get(position);
                // maybe send info
                mSendInfo = new SettingDestinationSureInfo(bean.getItem().getTitle(),
                        bean.getItem().getLatLonPoint().getLatitude(),
                        bean.getItem().getLatLonPoint().getLongitude());
                // search set null
                dataBinding.etSearch.setText("");
                // move to map center ——search nearby if not more nearby type jump to nearby
                mSearchStr = "search";
                mSearchInfo = mSearchList.get(position).getItem();
                setMapCenterLocation(mSendInfo.getLat(),mSendInfo.getLng());
            } else if (type == 4){ // search dialog result page click
                if (mDialog.isShowing()){
                    mDialog.dismiss();
                }
                showNearByInfo();
                SettingDestinationBottomBean bean = mSearchList.get(position);
                // maybe send info
                mSendInfo = new SettingDestinationSureInfo(bean.getItem().getTitle(),
                        bean.getItem().getLatLonPoint().getLatitude(),
                        bean.getItem().getLatLonPoint().getLongitude());
                // move to map center—— search nearby if not more nearby type jump to nearby
                mSearchStr = "search";
                mSearchInfo = mSearchList.get(position).getItem();
                setMapCenterLocation(mSendInfo.getLat(),mSendInfo.getLng());
            }
        } else { // 0 type click option
            if (position != mBottomTypePosition){
                for (int i = 0; i<mTypeInfo.size();i++){
                    mTypeInfo.get(i).setIs_Checked(false);
                }
                mTypeInfo.get(position).setIs_Checked(true);
                mTypeAdapter.setDatas(mTypeInfo);
                mBottomTypePosition = position;
                setBottomShowList();
            }
        }
    }

    private void setBottomShowList(){
        if (mBottomTypePosition == 1){
            dataBinding.rvLocationList.setVisibility(View.GONE);
            if (mTripList.size() >0) {
                dataBinding.rvTripList.setVisibility(View.VISIBLE);
                dataBinding.rlEmpty.setVisibility(View.GONE);
            } else {
                // 显示行程null数据
                dataBinding.rvTripList.setVisibility(View.GONE);
                dataBinding.rlEmpty.setVisibility(View.VISIBLE);
            }
        } else {
            dataBinding.rvLocationList.setVisibility(View.VISIBLE);
            dataBinding.rvTripList.setVisibility(View.GONE);
            dataBinding.rlEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataBinding.mvMap.onResume();
//        addMarkerInScreenCenter();
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
        mAmap = null;
        isSettingAdapter = false;
    }

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
            dataBinding.tvSearchContent.setText(data);
            if ("".equals(data) || null == data){
                mIsSearch = false;
                setSearchView();
                dataBinding.ivSearch.setVisibility(View.GONE);
                return;
            }
            dataBinding.ivSearch.setVisibility(View.VISIBLE);
            mIsSearch = true;
            keyWord = data;
            searchNeayBy(new LatLng(mSearchLatLng.getLatitude(),mSearchLatLng.getLongitude()));
            setSearchView();
        }
    };
    private boolean mIsSearch = false;

    private void setSearchView(){
        if (mIsSearch){
            dataBinding.llSearch.setVisibility(View.VISIBLE);
        } else {
            dataBinding.llSearch.setVisibility(View.GONE);
        }
    }

    GeocodeSearch.OnGeocodeSearchListener mGeocodeSearch = new GeocodeSearch.OnGeocodeSearchListener() {
        @Override
        public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
            if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                if (result != null && result.getRegeocodeAddress() != null
                        && result.getRegeocodeAddress().getFormatAddress() != null) {
                    RegeocodeAddress amapLocation = result.getRegeocodeAddress();
                    String adress = amapLocation.getFormatAddress().split(amapLocation.getTownship())[1];
                    PoiItem item = new PoiItem("",mSearchLatLng,adress,adress); //PointID
                    item.setProvinceName(amapLocation.getProvince()); //province
                    item.setCityName(amapLocation.getCity()); //city
                    item.setAdName(amapLocation.getDistrict()); //area
                    mSearchInfo = item;
                    if (!isSettingAdapter) {
                        setDayInfo();
                    }
//                    mInfoList.add(new SettingDestinationBottomBean(mSearchInfo,false));
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

    /**
     * pass latlng get address
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }

    private void searchNeayBy(LatLng lng){
        keyWord = dataBinding.etSearch.getText().toString().trim();
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(30);// set per page more return poiitem num
        query.setPageNum(currentPage);// set search first page
        LatLonPoint point = new LatLonPoint(lng.latitude,lng.longitude);
        if (point != null) {
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(mPoiSearch);
            String data = dataBinding.etSearch.getText().toString().trim();
            if ("".equals(data) || null == data) {
                poiSearch.setBound(new PoiSearch.SearchBound(point, 5000, true)); // without this setting,you may not be able to search for results
            }
            poiSearch.searchPOIAsyn();// async search info
        }
    }

    PoiSearch.OnPoiSearchListener mPoiSearch = new PoiSearch.OnPoiSearchListener() {
        @Override
        public void onPoiSearched(PoiResult result, int rcode) {
            if (rcode == 1000) {
                String data = dataBinding.etSearch.getText().toString().trim();
                if ("".equals(data) || null == data) {
                    mInfoList.clear();
                    if (mSearchInfo != null) {
                        mInfoList.add(new SettingDestinationBottomBean(mSearchInfo, true));
                    }
                    for (int i = 0; i < result.getPois().size(); i++) {
                        mInfoList.add(new SettingDestinationBottomBean(result.getPois().get(i), false));
                    }
                    mBottomNearByAdapter.setDatas(mInfoList);
                    mSendInfo = new SettingDestinationSureInfo(mInfoList.get(0).getItem().getTitle(),mInfoList.get(0).getItem().getLatLonPoint().getLatitude(),mInfoList.get(0).getItem().getLatLonPoint().getLongitude());
                } else {
                    mSearchList.clear();
                    for (int i = 0; i < result.getPois().size(); i++) {
                        mSearchList.add(new SettingDestinationBottomBean(result.getPois().get(i), false,data));
                    }
                    mSearchListAdapter.setDatas(mSearchList);
                }
            }
        }
        @Override
        public void onPoiItemSearched(PoiItem poiItem, int i) {

        }
    };

    /**
     * page center to myLoc
     */
    private void setMapCenterToMineLocation(){
        if (mAmap != null){
            mAmap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(Constants.mLatitude, Constants.mLongitude)));
            mAmap.moveCamera(CameraUpdateFactory.zoomTo(mInitMapZoom));
        }
    }

    private void finishMapPageBackDate(String stagingTime) {
        Intent intent = this.getIntent();
        intent.putExtra("destinationLat", mSendInfo.getLat());
        intent.putExtra("destinationLng",  mSendInfo.getLng());
        intent.putExtra("stagingTime",  stagingTime);
        intent.putExtra("destinationName", mSendInfo.getTitleName());
        this.setResult(RESULT_OK, intent);
        this.finish();
    }

    /**
     * show dialog
     */
    private void showScenicContentDialog() {
        if (mDialog == null) {
            mDialog = new SettingDestinationSearchResultDialog(this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
        }
        mDialog.setViewData(mSearchList);
        mDialog.show();
    }

    private void setMapCenterLocation(double lat,double lng){
        if (mAmap != null){
            mAmap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(lat, lng)));
            mAmap.moveCamera(CameraUpdateFactory.zoomTo(mInitMapZoom));
        }
    }

    private void showNearByInfo(){
        if (0 != mBottomTypePosition){
            for (int i = 0; i<mTypeInfo.size();i++){
                mTypeInfo.get(i).setIs_Checked(false);
            }
            mTypeInfo.get(0).setIs_Checked(true);
            mTypeAdapter.setDatas(mTypeInfo);
            mBottomTypePosition = 0;
            setBottomShowList();
        }
    }

    private SharedNavigationDatePickerDialog mTimerPicker;
    String mStartTime = "2019-08-23 19:47";
    String mEndTime = "2029-08-23 19:47";
    /**
     * init setting time dialog
     */
    private void initTimerPicker() {
        long times = System.currentTimeMillis(); //1566564746548 1565387466036
        mStartTime = TimeUtil.long2Str(times, true);
        long time = times+(1000L*60L*60L*24L*30L*12L*3L);
        mEndTime = TimeUtil.long2Str(time, true);

        mTimerPicker = new SharedNavigationDatePickerDialog(this, new SharedNavigationDatePickerDialog.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                // get str_type yyyy-MM-dd HH:mm
                String date = TimeUtil.long2Str(timestamp, true);
//                String dateStr = date.substring(5,date.length());
                // update time view
                finishMapPageBackDate(date);
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

    private String mDestinationStr = "";

    /**
     * show setting time dialog
     */
    private void settingTimeDialog(double lat,double lng){
        searchRouteResult(new LatLonPoint(Constants.mLatitude,Constants.mLongitude),new LatLonPoint(lat,lng));
        if (!"".equals(mStagingTime) && null != mStagingTime){
            // 算出时间
            String time = "";
            if (mStagingTime.length() > 12) {
                time = mStagingTime;
            } else {
                time = TimeUtil.getYear() + "-" + mStagingTime;
            }
            mTimerPicker.show(time,mDestinationStr,"");
        } else {
            mTimerPicker.show(mStartTime,mDestinationStr,"");
        }
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
}
