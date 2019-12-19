package cn.xmzt.www.selfdrivingtools.viewmodel;

import android.text.TextUtils;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.selfdrivingtools.bean.SharedNavigationMapRightInfo;

public class SharedNavigationGuideViewModel extends BaseViewModel {

    /**
     * request right view car share info
     * @return
     */
    public List<SharedNavigationMapRightInfo> getRightCarInfo(List<SharedNavigationMapRightInfo> info){
        List<SharedNavigationMapRightInfo> infos = new ArrayList<>();
        for (int i = 0; i < info.size(); i++ ){
            if (!TextUtils.isEmpty(info.get(i).getBean().numberPlate)) {
                SharedNavigationMapRightInfo bean = info.get(i);
                float distance = AMapUtils.calculateLineDistance(new LatLng(bean.getBean().latitude,bean.getBean().longitude), new LatLng(Constants.mLatitude, Constants.mLongitude));
                bean.setDistance(distance);
                bean.setType(0);
                infos.add(bean);
            }
        }
        return  infos;
    }

    /**
     * request right view member info
     * @return
     */
    public List<SharedNavigationMapRightInfo> getRightPersonInfo(List<SharedNavigationMapRightInfo> info){
        List<SharedNavigationMapRightInfo> infos = new ArrayList<>();
        for (int i = 0; i < info.size(); i++ ){
            SharedNavigationMapRightInfo bean = info.get(i);
            float distance = AMapUtils.calculateLineDistance(new LatLng(bean.getBean().latitude,bean.getBean().longitude), new LatLng(Constants.mLatitude, Constants.mLongitude));
            bean.setDistance(distance);
            bean.setType(1);
            infos.add(bean);
        }
        return infos;
    }
}
