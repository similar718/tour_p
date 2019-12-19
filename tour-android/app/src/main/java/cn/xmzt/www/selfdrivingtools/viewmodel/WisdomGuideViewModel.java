package cn.xmzt.www.selfdrivingtools.viewmodel;

import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.roomdb.beans.ScenicContents;
import cn.xmzt.www.selfdrivingtools.activity.WisdomGuideActivity;
import cn.xmzt.www.selfdrivingtools.bean.AdvertiseBannerBean;
import cn.xmzt.www.selfdrivingtools.bean.WisdomGuideInfo;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.roomdb.TourDatabase;
import cn.xmzt.www.roomdb.beans.ScenicSpotList;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.FastJsonUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class WisdomGuideViewModel extends BaseViewModel {

    public MutableLiveData<WisdomGuideInfo> mWisdomGuideList;
    public MutableLiveData<List<AdvertiseBannerBean>> mAdvertiseBanner;

    public WisdomGuideViewModel() {
        mWisdomGuideList = new MutableLiveData<WisdomGuideInfo>();
        mAdvertiseBanner = new MutableLiveData<List<AdvertiseBannerBean>>();
    }

    public void getWisdomGuideList(String city, String keyword, String location, int pageNum, int pageSize, WisdomGuideActivity context){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getWisdomGuideList(city,keyword,location,pageNum,pageSize);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<WisdomGuideInfo>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<WisdomGuideInfo> body) {
                        if (body.isSuccess()) {
                            mWisdomGuideList.setValue(body.getRel());
                            if (pageNum == 1) {
                                context.dataBinding.refreshLayout.finishRefresh(true);
                            } else {
                                context.dataBinding.refreshLayout.finishLoadMore(true);
                            }
                        } else {
                            // 获取数据出现问题的情况获取本地数据库数据
                            getScenicSpotList(context.getApplicationContext());
                            if (pageNum == 1){
                                context.dataBinding.refreshLayout.finishRefresh(true);
                            } else {
                                context.dataBinding.refreshLayout.finishLoadMore(true);
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        // 获取数据出现问题的情况获取本地数据库数据
                        getScenicSpotList(context.getApplicationContext());
                        if (pageNum == 1){
                            context.dataBinding.refreshLayout.finishRefresh(true);
                        } else {
                            context.dataBinding.refreshLayout.finishLoadMore(true);
                        }
                    }
                });
    }

    public void getAdvertiseBanner(Context context,int type){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getAdvertiseBanner(type);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<AdvertiseBannerBean>>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<List<AdvertiseBannerBean>> body) {
                        if (body.isSuccess()) {
                            mAdvertiseBanner.setValue(body.getRel());
                            // 将Banner数据缓存到本地数据库  方便用户在没有网络的情况下就去加载本地的
                            setAddScenicSpotBannerDataBase(body.getRel(), context);
                        }
                    }
                });
    }

    /**
     *  将数据缓存到本地 TODO 不知道图片要不要缓存 目前没有缓存图片到本地
     * @param itemsBean 缓存的数据
     * @param context 上下文
     */
    public void setAddScenicSpotListDataBase(List<WisdomGuideInfo.ItemsBean> itemsBean, Context context){
        List<ScenicSpotList> list = new ArrayList<>();
        for (int i = 0;i<itemsBean.size(); i++){
            ScenicSpotList data = new ScenicSpotList();
            data.id = itemsBean.get(i).getId();
            data.scenicName = itemsBean.get(i).getScenicName();
            data.photoUrl = itemsBean.get(i).getPhotoUrl();
            data.scenicAddress = itemsBean.get(i).getScenicAddress();
            data.grade = itemsBean.get(i).getGrade();
            data.coordinate = itemsBean.get(i).getCoordinate();
            data.city = itemsBean.get(i).getCity();
            data.area = itemsBean.get(i).getArea();
            data.explainPointCount = itemsBean.get(i).getExplainPointCount();
            data.listenCount = itemsBean.get(i).getListenCount();
            data.distance = itemsBean.get(i).getDistance();

            list.add(data);
        }
        TourDatabase.getDefault(context).getScenicSpotListDao().insertAll(list);
    }

    /**
     *  获取本地数据
     * @param context 上下文
     */
    public List<WisdomGuideInfo.ItemsBean> getScenicSpotList(Context context){
        // 用来承载数据库信息的list文件
        List<WisdomGuideInfo.ItemsBean> listData = new ArrayList<>();
        List<ScenicSpotList> list = TourDatabase.getDefault(context).getScenicSpotListDao().getDataAll();
        if (list == null){
            return null;
        } else {
            if (list.size() > 0){
                for (int i = 0;i<list.size(); i++){
                    WisdomGuideInfo.ItemsBean itemsBean = new WisdomGuideInfo.ItemsBean();
                    itemsBean.setId(list.get(i).id);
                    itemsBean.setScenicName(list.get(i).scenicName);
                    itemsBean.setPhotoUrl(list.get(i).photoUrl);
                    itemsBean.setScenicAddress(list.get(i).scenicAddress);
                    itemsBean.setGrade(list.get(i).grade);
                    itemsBean.setCoordinate(list.get(i).coordinate);
                    itemsBean.setCity(list.get(i).city);
                    itemsBean.setArea(list.get(i).area);
                    itemsBean.setExplainPointCount(list.get(i).explainPointCount);
                    itemsBean.setDistance(list.get(i).distance);
                    listData.add(itemsBean);
                }
                WisdomGuideInfo info = new WisdomGuideInfo();
                info.setItems(listData);
                info.setTotal(listData.size());
                info.setTotalPage(1);
                mWisdomGuideList.setValue(info);
                return listData;
            } else {
                return null;
            }
        }
    }


    /**
     *  将数据Banner缓存到本地 不知道图片要不要缓存 目前没有缓存图片到本地
     * @param itemsBean 缓存的数据
     * @param context 上下文
     */
    public void setAddScenicSpotBannerDataBase(List<AdvertiseBannerBean> itemsBean, Context context){
        // 拿到数据将实体类转化为json数据
        String jsonStr = FastJsonUtil.objToJson(itemsBean);
        int id = 0;
        // 查询当前数据有无在数据库有信息
        ScenicContents info =  new ScenicContents();
        info.id = id;
        info.funTypes = Constants.KeywordsType.TYPE_SCENIC_BANNER;
        info.content = jsonStr;
        TourDatabase.getDefault(context).getScenicContentDao().insert(info);
    }

    /**
     *  获取本地数据
     * @param context 上下文
     */
    public List<AdvertiseBannerBean> getScenicSpotBanner(Context context){
        // 查询当前在数据库里面是否有缓存信息
        ScenicContents info = TourDatabase.getDefault(context).getScenicContentDao().getScenicContents(0,Constants.KeywordsType.TYPE_SCENIC_BANNER);
        if (info == null){
            return null;
        } else {
            List<AdvertiseBannerBean> bean = null;
            try {
                bean = FastJsonUtil.parseArray(info.content,AdvertiseBannerBean.class);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return bean;
        }
    }
}
