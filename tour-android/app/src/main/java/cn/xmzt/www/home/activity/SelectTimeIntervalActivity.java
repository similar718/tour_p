package cn.xmzt.www.home.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivitySelectTimeIntervalBinding;
import cn.xmzt.www.home.adapter.SelectTimeIntervalAdapter;
import cn.xmzt.www.home.bean.SelectDateTime;
import cn.xmzt.www.home.event.CustomizeRefreshBus;
import cn.xmzt.www.home.event.SelectDateIntervalBus;
import cn.xmzt.www.home.vm.SelectTimeIntervalViewModel;
import cn.xmzt.www.utils.TimeUtil;
import cn.xmzt.www.view.listener.ItemListener;

/**
 * 选择时间间隔
 */
public class SelectTimeIntervalActivity extends TourBaseActivity<SelectTimeIntervalViewModel, ActivitySelectTimeIntervalBinding> implements ItemListener {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_select_time_interval;
    }
    @Override
    protected SelectTimeIntervalViewModel createViewModel() {
        viewModel = new SelectTimeIntervalViewModel();
        viewModel.dateTimeList.observe(this, new Observer<List<SelectDateTime>>() {
            @Override
            public void onChanged(@Nullable List<SelectDateTime> dateTimeList) {
                adapter.setDatas(dateTimeList);
            }
        });
        return viewModel;
    }
    SelectTimeIntervalAdapter adapter =null;
    GridLayoutManager manager=null;
    int type=1;//1添加私人定制 2修改私人定制
    @Override
    protected void initData() {
        Intent intent=getIntent();
        viewModel.startDate=intent.getStringExtra("A");
        viewModel.endDate=intent.getStringExtra("B");
        viewModel.intervalDays= TimeUtil.getBetweenIntervalDays(viewModel.startDate, viewModel.endDate,"yyyy-MM-dd")+1;
        type=intent.getIntExtra("C",1);
        dataBinding.setActivity(this);
        adapter = new SelectTimeIntervalAdapter();
        adapter.setItemListener(this);
        dataBinding.recyclerView.setAdapter(adapter);
        manager= (GridLayoutManager) dataBinding.recyclerView.getLayoutManager();
        manager.setSpanCount(7);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                boolean isSingleShow=adapter.isSingleShow(position);
                return isSingleShow ? manager.getSpanCount() : 1;
            }
        });
        viewModel.getDateTimeList();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onItemClick(View view, int position) {
        if (position == adapter.selectedStartPosition || position == adapter.selectedEndPosition){
            return;
        }
        SelectDateTime mSelectDateTime=adapter.getItem(position);
        if(mSelectDateTime.getDateDay()>0){
            adapter.setSelectedItem(position);
            if(adapter.isSelected){
                //选好了
                SelectDateTime mStartDateTime=adapter.getItem(adapter.selectedStartPosition);
                SelectDateTime mEndDateTime=adapter.getItem(adapter.selectedEndPosition);
                SelectDateIntervalBus selectDateIntervalBus=new SelectDateIntervalBus(type,mStartDateTime.getDate(),mEndDateTime.getDate());
                EventBus.getDefault().post(selectDateIntervalBus);
                onBackPressed();
            }
        }
    }
}
