package cn.xmzt.www.route.vm;

import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import androidx.databinding.ObservableField;
import android.text.Editable;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.roomdb.TourDatabase;
import cn.xmzt.www.roomdb.beans.KeyWords;
import cn.xmzt.www.route.bean.SearchKeywordBean;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.rxjava2.QzdsException;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * @describe 关键字搜索
 */
public class KeywordSearchViewModel extends BaseViewModel {
    public MutableLiveData<List<String>> recommendSearchList;
    public MutableLiveData<List<String>> hotSearchList;
    public KeywordSearchViewModel() {
        recommendSearchList = new MutableLiveData<List<String>>();
        hotSearchList = new MutableLiveData<List<String>>();
    }
    /**
     * 当前是否为搜索界面
     */
    public ObservableField<String> keyword = new ObservableField("");

    public void keywordAfterTextChanged(Editable s) {
        keyword.set(s.toString());
    }

    /**
     * 获取历史搜索关键字标签列表
     * @param context
     * @return
     */
    public List<String> historyTagList(Context context){
        List<KeyWords> keywordList= TourDatabase.getDefault(context).getKeyWordsDao().getDataTopNum(Constants.KeywordsType.TYPE_ROUTE_SEARCH,6);
        //点击搜索历史
        List<String> list=new ArrayList<>();
        for(int i=0;i<keywordList.size();i++){
            list.add(keywordList.get(i).name);
        }
        return list;
    }
    private List<String> searchKeywordToStrList(List<SearchKeywordBean> keywordList){
        List<String> list=new ArrayList<>();
        for(int i=0;i<keywordList.size();i++){
            list.add(keywordList.get(i).getKeyword());
        }
        return list;
    }
    private List<SearchKeywordBean> recommendSearchKeywordList=null;
    /**
     * @param type 优惠券类型 (0:通用,1:线路,2:酒店,3:门票)
     */
    public void searchKeywordList(String type) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.recommendSearchKeywordList(type,null,6);

        mObservable.flatMap(new Function<BaseDataBean<List<SearchKeywordBean>>, Observable<BaseDataBean<List<SearchKeywordBean>>>>() {
                    @Override
                    public Observable<BaseDataBean<List<SearchKeywordBean>>> apply(BaseDataBean<List<SearchKeywordBean>> body) throws Exception {
                        if(body.isSuccess()){
                            recommendSearchKeywordList=body.getRel();
                            return mService.hotSearchKeywordList(type,null,1,6);
                        }else {
                            throw new QzdsException(body.getReMsg(),body.getReCode());
                        }
                    }
                }).compose(ComposeUtil.compose())
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<SearchKeywordBean>>>() {
                    @Override
                    public void onNext(BaseDataBean<List<SearchKeywordBean>> body) {
                        if(recommendSearchKeywordList!=null){
                            recommendSearchList.setValue(searchKeywordToStrList(recommendSearchKeywordList));
                        }
                        if(body.isSuccess()){
                            hotSearchList.setValue(searchKeywordToStrList(body.getRel()));
                        }else {
                            mView.showLoadFail(body.getReMsg());
                        }
                    }
                });
    }
}
