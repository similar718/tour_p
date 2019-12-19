package cn.xmzt.www.selfdrivingtools.activity;

import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import cn.xmzt.www.R;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ActivityAkeySosBinding;
import cn.xmzt.www.selfdrivingtools.adapter.ASosAdapter;
import cn.xmzt.www.selfdrivingtools.bean.AKeySOSBean;
import cn.xmzt.www.selfdrivingtools.viewmodel.AKeySOSViewModel;
import cn.xmzt.www.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @describe 一键救援
 */
public class AKeySOSActivity extends TourBaseActivity<AKeySOSViewModel, ActivityAkeySosBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_akey_sos;
    }

    List<AKeySOSBean> mList = new ArrayList<>();
    private String mPhoneNum = "";
    @Override
    protected AKeySOSViewModel createViewModel() {
        viewModel = new AKeySOSViewModel();
        viewModel.sosList.observe(this, new Observer<List<AKeySOSBean>>() {
            @Override
            public void onChanged(@Nullable List<AKeySOSBean> result) {
                mList = result;
                mAdapter.setDatas(mList);
                dataBinding.rvList.setFocusable(false);
            }
        });
        viewModel.mSosData.observe(this, new Observer<Object>() {
            @Override
            public void onChanged(Object data) {
                mPhoneNum = (String) data;
            }
        });
        return viewModel;
    }
    ASosAdapter mAdapter = null;
    private AMap mAmap;
    @Override
    protected void initData() {
        dataBinding.setActivity(this);
        dataBinding.rvList.setLayoutManager(new GridLayoutManager(this,2));
        mAdapter = new ASosAdapter(this);
        // 初始化地图控件
        dataBinding.mvMap.onCreate(getIntent().getExtras());

        dataBinding.rvList.setAdapter(mAdapter);
        viewModel.getAKeySOSList();
        dataBinding.tvAddress.setText("    "+ Constants.mAddress);
        if (mAmap == null) {
            mAmap = dataBinding.mvMap.getMap();
            initMapData();
        }
        viewModel.getInfo(TourApplication.getToken());
    }
    private int mInitMapZoom = 17;
    private void initMapData(){
        UiSettings uiSettings = mAmap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false); // hide zoom view
        uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER); // logo show bottom center
        uiSettings.setRotateGesturesEnabled(false); // rotate gesture
        uiSettings.setTiltGesturesEnabled(false); // tilt gesture
        uiSettings.setAllGesturesEnabled(false);
        mAmap.showMapText(true);
        mAmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Constants.mLatitude, Constants.mLongitude), mInitMapZoom));
        mAmap.addMarker(new MarkerOptions().position(new LatLng(Constants.mLatitude, Constants.mLongitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.akey_sos_marker)));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    public void onSosClickListener(int position){
        if (position == 0) {
            if (null != mPhoneNum && !"".equals(mPhoneNum)) {
                callPhone(mPhoneNum);
            } else {
                ToastUtils.showText(this,"抱歉，您暂没有可联系的领队");
            }
        } else {
            callPhone(mList.get(position).getPhone());
        }
    }

    /**
     * 拨打电话（直接拨打电话）
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum){
        Intent intent = new Intent(Intent.ACTION_DIAL);//ACTION_DIAL ACTION_CALL
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        dataBinding.mvMap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataBinding.mvMap.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAmap.clear();
        dataBinding.mvMap.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        dataBinding.mvMap.onSaveInstanceState(outState);
    }
}
