package cn.xmzt.www.selfdrivingtools.viewmodel;

import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.blankj.utilcode.util.ToastUtils;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.selfdrivingtools.bean.ScenicHotSearchBean;
import cn.xmzt.www.selfdrivingtools.bean.WisdomGuideInfo;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.roomdb.TourDatabase;
import cn.xmzt.www.roomdb.beans.KeyWords;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.NetWorkUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ScenicSpotSearchViewModel extends BaseViewModel {

    public MutableLiveData<WisdomGuideInfo> mKeyWordsSearchList;
    public MutableLiveData<List<ScenicHotSearchBean>> mScenicHotSearchList;

    public ScenicSpotSearchViewModel() {
        mKeyWordsSearchList = new MutableLiveData<WisdomGuideInfo>();
        mScenicHotSearchList = new MutableLiveData<List<ScenicHotSearchBean>>();
    }

    /**
     * 景区搜索通过关键字
     * @param city 当前定位城市
     * @param keyword 搜索关键字
     * @param location 经纬度，用英文逗号隔开
     * @param pageNum 当前第几页（默认1，从1开始）
     * @param pageSize 每页大小（默认20）
     * @return
     */
    public void getKeyWordsSearchList(String city, String keyword,String location,int pageNum,int pageSize){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getKeyWordsSearchList(city,keyword,location,pageNum,pageSize);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<WisdomGuideInfo>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<WisdomGuideInfo> body) {
                        if (body.isSuccess()) {
                            mKeyWordsSearchList.setValue(body.getRel());
                        } else {
                            ToastUtils.showShort(body.getReMsg());
                        }
                    }
                });
    }

    /**
     * 景区热门搜索
     * @param limit 返回条数
     * @param type  类型(1: 城市 11: 景区)
     * @return
     */
    public void getHotSearchList(Context context,int limit,int type){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getHotSearchList(limit,type);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<ScenicHotSearchBean>>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<List<ScenicHotSearchBean>> body) {
                        if (body.isSuccess()) {
                            mScenicHotSearchList.setValue(body.getRel());
                        } else {
                            ToastUtils.showShort(body.getReMsg());
                        }
                    }
                });
    }

    /**
     * 获取本地数据库搜索历史前10条信息
     * @param type 历史搜索类型
     * @param num 历史搜索数目
     * @param context
     * @return
     */
    public List<String> getSearchHistoryInfo(int type, int num, Context context){
        List<String> listData = new ArrayList<>();
        List<KeyWords> list = TourDatabase.getDefault(context).getKeyWordsDao().getDataTopNum(type,num);
        // 本地没有历史数据
        if (list == null){
            return null;
        } else { // 有历史数据
            if (list.size() > 0){
                for (int i = 0 ; i< list.size(); i++){
                    listData.add(list.get(i).name);
                }
            } else {
                return null;
            }
        }
        return listData;
    }
    /**
     *  删除当前景区所有的历史记录
     * @param type 历史搜索类型
     * @param context
     * @return
     */
    public int setDeleteSearchHistoryInfo(int type, Context context){
        int is_success = TourDatabase.getDefault(context).getKeyWordsDao().deleteFunTypes(type);
        return is_success;
    }
    /**
     *  添加景区搜索到数据库
     * @param name 名称
     * @param type 搜索类型 1001
     * @param time 时间
     * @param context
     * @return
     */
    public boolean setAddSearchHistoryInfo(String name,int type,long time, Context context){
        long num = 0;
        // 过来之后表示成功搜索 添加到数据库 先要判断数据库里面时候有当前类型的当前关键字  有的话就更新时间
        KeyWords keyWords = TourDatabase.getDefault(context).getKeyWordsDao().getKeyWords(name, Constants.KeywordsType.FUNTYPES_SCENIC);
        if (keyWords == null){
            keyWords = new KeyWords();
            keyWords.name = name;
            keyWords.funTypes = Constants.KeywordsType.FUNTYPES_SCENIC;
            keyWords.time = System.currentTimeMillis(); // 获取当前时间戳
            num = TourDatabase.getDefault(context).getKeyWordsDao().insert(keyWords);
        } else {
            keyWords.time = System.currentTimeMillis(); // 更新当前时间
            num = TourDatabase.getDefault(context).getKeyWordsDao().update(keyWords);
        }
        return num > 0;
    }
}
