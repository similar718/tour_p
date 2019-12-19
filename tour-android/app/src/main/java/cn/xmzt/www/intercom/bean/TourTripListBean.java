package cn.xmzt.www.intercom.bean;


import cn.xmzt.www.home.bean.RecommendLineBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 行程列表实体
 * @author Averysk
 */
public class TourTripListBean {

    private List<RecommendLineBean> recommendLineList;
    private List<TourTripBean> tourTripList;

    public List<RecommendLineBean> getRecommendLineList() {
        return recommendLineList;
    }

    public void setRecommendLineList(List<RecommendLineBean> recommendLineList) {
        this.recommendLineList = recommendLineList;
    }

    public List<TourTripBean> getTourTripList() {
        if(tourTripList==null){
            tourTripList=new ArrayList<>();
        }
        return tourTripList;
    }

    public void setTourTripList(List<TourTripBean> tourTripList) {
        this.tourTripList = tourTripList;
    }
}
