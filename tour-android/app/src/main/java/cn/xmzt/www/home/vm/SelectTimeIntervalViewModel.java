package cn.xmzt.www.home.vm;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.home.bean.SelectDateTime;
import cn.xmzt.www.rxjava2.AsyncUtil;
import cn.xmzt.www.utils.TimeUtil;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 选择时间间隔
 */
public class SelectTimeIntervalViewModel extends BaseViewModel {
    public SelectTimeIntervalViewModel() {

    }

    public MutableLiveData<List<SelectDateTime>> dateTimeList=new MutableLiveData<List<SelectDateTime>>();
    public void getDateTimeList() {
        mView.showLoading();
        AsyncUtil.async(new Function<String,List>() {
            @Override
            public List apply(String o) throws Exception {
                List<SelectDateTime> list=new ArrayList();
                List<SelectDateTime> list1 =getDateTimeList(0);
                List<SelectDateTime> list2 =getDateTimeList(1);
                List<SelectDateTime> list3 =getDateTimeList(2);
                List<SelectDateTime> list4 =getDateTimeList(3);
                List<SelectDateTime> list5 =getDateTimeList(4);
                List<SelectDateTime> list6 =getDateTimeList(5);
                list.addAll(list1);
                list.addAll(list2);
                list.addAll(list3);
                list.addAll(list4);
                list.addAll(list5);
                list.addAll(list6);
                return list;
            }
        }, new Consumer<List>() {
            @Override
            public void accept(List list) throws Exception {
                mView.hideLoading();
                dateTimeList.setValue(list);
            }
        });
    }
    public String startDate="";//开始日期
    public String endDate="";//结束日期
    public int intervalDays=8;//间隔天数

    public List<SelectDateTime> getDateTimeList(int index) {
        String month =TimeUtil.getLast12MonthsDate(index,"yyyy-MM");
        int week = TimeUtil.getWeek(month+"-01","yyyy-MM-dd");
        List<SelectDateTime> list=new ArrayList();
        list.add(new SelectDateTime(month,true));
        for(int i=0;i<week;i++){
            list.add(new SelectDateTime());
        }
        int days =TimeUtil.getLastMonthsDay(index);
        for(int i=1;i<=days;i++){
            String date="";
            if(i<10){
                date=month+"-0"+i;
            }else {
                date=month+"-"+i;
            }
            SelectDateTime mSelectDateTime=new SelectDateTime(date,days,i);
            if(TimeUtil.isStrTimeToday(date,"yyyy-MM-dd")){
                mSelectDateTime.setToday(true);
            }
            int betweenIntervalDays=TimeUtil.getBetweenIntervalDays(startDate,date,"yyyy-MM-dd");
            if(betweenIntervalDays>=0&&betweenIntervalDays<intervalDays){
                mSelectDateTime.setSelected(true);
                if(betweenIntervalDays==0){
                    mSelectDateTime.setFirstSelected(true);
                }
                if(betweenIntervalDays==intervalDays-1){
                    mSelectDateTime.setLastSelected(true);
                }
            }
            list.add(mSelectDateTime);
        }
        return list;
    }

}
