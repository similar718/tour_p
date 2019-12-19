package cn.xmzt.www.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import cn.xmzt.www.config.Constants;

public class LocationService extends Service {
    private static final String TAG = "LocationService";
    //声明AMapLocationClient类对象
    AMapLocationClient mLocationClient;
    // 声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption;
//    public List<LatLng> points = new ArrayList<>();

    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getPosition();
    }

    public void getPosition() {
        //初始化定位
        mLocationClient = new AMapLocationClient(this.getApplicationContext());
        // 初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        // 设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        // 获取一次定位结果： //该方法默认为false。
        mLocationOption.setOnceLocation(false);
        mLocationOption.setOnceLocationLatest(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        // 设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        // 启动定位
        mLocationClient.startLocation();
    }

    private int mNum = 0;

    // 声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation == null) {
                Log.i(TAG, "amapLocation is null!");
                return;
            }
            if (amapLocation.getErrorCode() != 0) {
                Log.i(TAG, "amapLocation has exception errorCode:" + amapLocation.getErrorCode());
                return;
            }
            if (Constants.mIsOtherLocation){ // 如果其他地方有定位就不去赋值了
                return;
            }
            // TODO  需要判断当前获取的定位是有的 并且不是定位在海外情况
            if (amapLocation.getLongitude() != 0 && amapLocation.getLatitude() !=0 && amapLocation.getLatitude()>0 && amapLocation.getLongitude()>0 && !"".equals(amapLocation.getCity()) && null != amapLocation.getCity()){
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
                if (mNum < 3){
                    mNum ++ ;// 定位次数 设置三次
                } else {
                    mNum = 0;
                    mLocationClient.stopLocation(); // 满了三次就停止定位
                    stopSelf();
                }
            }
//            Double longitude = amapLocation.getLongitude();//获取经度
//            Double latitude = amapLocation.getLatitude();//获取纬度
//            points.add(new LatLng(latitude,longitude));
//            //存入数据库
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date date = new Date(amapLocation.getTime());
//            String time = df.format(date);
//
//            String longitudestr = String.valueOf(longitude);
//            String latitudestr = String.valueOf(latitude);
//            Log.i(TAG, "longitude:" + longitude + ",latitude：" + latitude +" city : "+amapLocation.getCity());
        }
    };
}
