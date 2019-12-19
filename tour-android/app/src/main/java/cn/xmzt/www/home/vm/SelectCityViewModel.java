package cn.xmzt.www.home.vm;


import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.res.AssetManager;
import androidx.databinding.ObservableInt;
import android.text.TextUtils;
import android.util.Log;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.home.adapter.SelectedCityAdapter;
import cn.xmzt.www.home.bean.CityBean;
import cn.xmzt.www.home.bean.CityLetterGroupBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.rxjava2.AsyncUtil;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.rxjava2.QzdsException;
import cn.xmzt.www.utils.FastJsonUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import retrofit2.http.Query;

/**
 * 选择城市
 */
public class SelectCityViewModel extends BaseViewModel {
    private static final String tag="SelectCityViewModel";
    //如果编码不对用下面的高度api查询
    //https://restapi.amap.com/v3/config/district?keywords=北京&subdistrict=2&key=<用户的key>

    /**
     * 是否选中的项 0不显示 1显示
     */
    public ObservableInt showSelect= new ObservableInt(0);
    public MutableLiveData<List<Object>> cityList=new MutableLiveData<List<Object>>();
    private CityBean mCityBean=null;//当前城市
    public List<CityBean> selectList=new ArrayList<>();//已选择的城市
   /* public void getCityList(Context context) {
        mView.showLoading();
        AsyncUtil.async(new Function<String,List>() {
            @Override
            public List apply(String o) throws Exception {
                String jsonList=getJson("city/city.json",context);
                List<CityLetterGroupBean> list= FastJsonUtil.parseArray(jsonList,CityLetterGroupBean.class);
                List list1=new ArrayList();
                for(int i=0;i<list.size();i++){
                    CityLetterGroupBean group= list.get(i);
                    list1.add(group.getInitial());
                    List<CityBean> cityList=group.getList();
                    list1.addAll(cityList);
                    for(int j=0;j<cityList.size();j++){
                        CityBean cityBean=cityList.get(j);
                        if(!TextUtils.isEmpty(Constants.mCity)&&(cityBean.getName().contains(Constants.mCity)||Constants.mCity.contains(cityBean.getName()))){
                            mCityBean=cityBean;
                            list1.add(0,cityBean);
                        }
                    }
                }
                if(mCityBean==null){
                    mCityBean=new CityBean();
                    mCityBean.setName("暂无定位信息");
                    list1.add(0,mCityBean);
                }
                return list1;
            }
        }, new Consumer<List>() {
            @Override
            public void accept(List list) throws Exception {
                mView.hideLoading();
                if(selectList.size()>0){
                    for(int i=0;i<list.size();i++){
                        Object obj=list.get(i);
                        if(obj instanceof CityBean){
                            CityBean cityBean= (CityBean) obj;
                            for(CityBean selectCity:selectList){
                                if(cityBean.getName().equals(selectCity.getName())){
                                    cityBean.setSelect(true);
                                    if(selectedAdapter!=null){
                                        selectedAdapter.addData(cityBean);
                                    }
                                    if(selectType==0){
                                        radioPosition=i;
                                    }
                                }
                            }
                        }
                    }
                }
                cityList.setValue(list);
            }
        });
    }*/

    /**
     * 获取本地的城市数据
     * @param context
     */
   private void getLocalCityList(Context context) {
       mView.showLoading();
       AsyncUtil.async(new Function<String,List>() {
           @Override
           public List apply(String o) throws Exception {
               String jsonList=getJson("city/cityserver.json",context);
               List<CityBean> list= FastJsonUtil.parseArray(jsonList,CityBean.class);
               return changeCityList(list);
           }
       }, new Consumer<List>() {
           @Override
           public void accept(List list) throws Exception {
               mView.hideLoading();
               if(selectedAdapter!=null){
                   selectedAdapter.notifyDataSetChanged();
               }
               cityList.setValue(list);

           }
       });
   }
    public List changeCityList(List<CityBean> list){
        List list1=new ArrayList();
        for(int i=0;i<list.size();i++){
            CityBean cityBean= list.get(i);
            String pinYin=cityBean.getAreaNamePinYin();
            String firstChar=pinYin.substring(0,1);
            if(i==0){
                list1.add(firstChar.toUpperCase());
            }else if(i<list.size()){
                CityBean prevCityBean= list.get(i-1);
                String prevPinYin=prevCityBean.getAreaNamePinYin();
                if(!prevPinYin.startsWith(firstChar)){
                    list1.add(firstChar.toUpperCase());
                }
            }
            for(CityBean selectCity:selectList){
                if(cityBean.getAreaName().equals(selectCity.getAreaName())){
                    cityBean.setSelect(true);
                    if(selectedAdapter!=null){
                        selectedAdapter.addDataNoUi(cityBean);
                    }
                    if(selectType==0){
                        radioPosition=list1.size();
                    }
                    //跳出selectList的for循环
                    break;
                }
            }
            if(!TextUtils.isEmpty(Constants.mCity)&&(cityBean.getAreaName().contains(Constants.mCity)||Constants.mCity.contains(cityBean.getAreaName()))){
                mCityBean=cityBean;
                list1.add(0,cityBean);
                if(radioPosition!=-1){
                    radioPosition=radioPosition+1;
                }
            }
            list1.add(cityBean);
        }
        if(mCityBean==null){
            mCityBean=new CityBean();
            mCityBean.setAreaName("暂无定位信息");
            list1.add(0,mCityBean);
        }
        return list1;
    }
    /**
     * 获取城市数据
     */
    public void getCityList(Context context) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getSortArea("2");
        mObservable.map(new Function<BaseDataBean<List<CityBean>>,List>() {
            @Override
            public List apply(BaseDataBean<List<CityBean>> body) throws Exception {
                List<CityBean> list=body.getRel();
                if(body.isSuccess()&&list!=null){
                    return changeCityList(list);
                }else {
                    String jsonList=getJson("city/cityserver.json",context);
                    List<CityBean> localList= FastJsonUtil.parseArray(jsonList,CityBean.class);
                    return changeCityList(localList);
                }
            }
        }).compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<List>(mView) {
                    @Override
                    public void onNext(List list) {
                        if(selectedAdapter!=null){
                            selectedAdapter.notifyDataSetChanged();
                        }
                        if(list!=null&&list.size()>0){
                            cityList.setValue(list);
                        }else {
                            getLocalCityList(context);
                        }
                    }
                });
    }
    public int selectType;//如果0表示选择出发的 1表示选择目的地
    public int radioPosition=-1;//选择出发地时的单选的选中项
    public SelectedCityAdapter selectedAdapter =null;
    /**
     * 读取assets本地json
     * @param fileName
     * @param context
     * @return
     */
    public static String getJson(String fileName,Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            Log.e(tag,"",e);
        }
        return stringBuilder.toString();
    }
}
