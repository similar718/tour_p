package cn.xmzt.www.route.activity;

import androidx.lifecycle.Observer;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.bean.OftenVisitorsBean;
import cn.xmzt.www.databinding.ActivitySelectVisitorsBinding;
import cn.xmzt.www.mine.activity.AddTouristsActivity;
import cn.xmzt.www.mine.bean.TourBean;
import cn.xmzt.www.mine.event.TourEvent;
import cn.xmzt.www.route.adapter.OfenVisitorsAdapter;
import cn.xmzt.www.route.eventbus.SelectVisitorsBus;
import cn.xmzt.www.route.vm.SelectVisitorsViewModel;
import cn.xmzt.www.view.listener.ItemListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * @describe 选择出游人界面
 */
public class SelectVisitorsActivity extends TourBaseActivity<SelectVisitorsViewModel, ActivitySelectVisitorsBinding> implements ItemListener {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_select_visitors;
    }

    @Override
    protected SelectVisitorsViewModel createViewModel() {
        viewModel = new SelectVisitorsViewModel();
        viewModel.visitorsList.observe(this, new Observer<List<OftenVisitorsBean>>() {
            @Override
            public void onChanged(@Nullable List<OftenVisitorsBean> result) {
                if(result!=null&&result.size()>0){
                    dataBinding.tvNoData.setVisibility(View.GONE);
                    for(int i=0;i<selectList.size();i++){
                        OftenVisitorsBean selectoftenVisitors=selectList.get(i);
                        label:for(OftenVisitorsBean oftenVisitors:result){
                            if(oftenVisitors.getId()==selectoftenVisitors.getId()){
                                oftenVisitors.setSelect(true);
                                selectList.set(i,oftenVisitors);
                                break label;
                            }
                        }
                    }
                    setSelectData();
                    adapter.setDatas(result);
                }
            }
        });
        return viewModel;
    }
    private int crCount=0;//成人数量
    private int xhCount=0;//小孩数量
    private int type=0;//功能类型 1表示选择出游人 2表示选择预订人
    OfenVisitorsAdapter adapter =null;
    GridLayoutManager manager=null;
    @Override
    protected void initData() {
        Intent intent=getIntent();
        crCount=intent.getIntExtra("A",0);
        xhCount=intent.getIntExtra("B",0);
        type=intent.getIntExtra("C",0);
        EventBus.getDefault().register(this);
        if(type==1){
            dataBinding.tvAllCount.setText(""+(crCount+xhCount));
            dataBinding.tvSelect.setText("0");
            dataBinding.tvConfirm.setText("您需选择"+crCount+"位成人，"+xhCount+"位儿童");
        }else if(type==2){
            dataBinding.selectLayout.setVisibility(View.GONE);
            dataBinding.tvConfirm.setText("确定");
        }
        dataBinding.setVm(viewModel);
        dataBinding.setActivity(this);
        viewModel.getVisitorsList();
        manager= (GridLayoutManager) dataBinding.recyclerView.getLayoutManager();
        manager.setSpanCount(1);
        adapter = new OfenVisitorsAdapter();
        adapter.setItemListener(this);
        dataBinding.recyclerView.setAdapter(adapter);
    }
    @Subscribe(sticky = true)
    public void addVisitors(TourEvent tourEvent){
        if(tourEvent !=null){
            viewModel.getVisitorsList();
        }
    }
    /**
     * 接收选择出游人的EventBus
     * @param list
     */
    @Subscribe(sticky = true)
    public void selectOftenVisitorsList(List<OftenVisitorsBean> list){
        if(type==1){
            selectList.clear();
            selectList.addAll(list);
            for(OftenVisitorsBean visitors:list){
                if(visitors.isChildren()){
                    selectXhCount++;
                }else {
                    selectCrCount++;
                }
            }
        }

    }
    /**
     * 接收选择预订人的EventBus
     * @param visitors
     */
    @Subscribe(sticky = true)
    public void selectOftenVisitors(OftenVisitorsBean visitors){
        if(type==2){
            selectList.clear();
            if(visitors.getId()>0){
                selectList.add(visitors);
                selectCrCount=1;
            }
        }
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.rl_add_tourists:
                //添加出游人
                EventBus.getDefault().postSticky(new TourBean());
                startToActivity(AddTouristsActivity.class);
                break;
            case R.id.tv_confirm:
                //选择确认
                if("确定".equals(dataBinding.tvConfirm.getText().toString())){
                    if(selectCount==crCount+xhCount){
                        if (selectList.size() == selectCount) {
                            EventBus.getDefault().post(new SelectVisitorsBus(type, selectList));
                            onBackPressed();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
    List<OftenVisitorsBean> selectList=new ArrayList<>();
    private int selectCrCount=0;//成人数量
    private int selectXhCount=0;//小孩数量
    private int selectCount=0;//选择数量

    @Override
    public void onItemClick(View view, int position) {
        OftenVisitorsBean visitorsBean=adapter.getItem(position);
        if(view.getId()==R.id.iv_check){
            if(view.isSelected()){
                view.setSelected(false);
                List<OftenVisitorsBean> newSelectList=new ArrayList<>();
                for (int i=0;i< selectList.size();i++){
                    OftenVisitorsBean bean=selectList.get(i);
                    if (bean.getId() != visitorsBean.getId()){
                        newSelectList.add(bean);
                    }
                }
                selectList=newSelectList;
                visitorsBean.setSelect(false);
                if(visitorsBean.isChildren()){
                    selectXhCount--;
                }else {
                    selectCrCount--;
                }
            }else {
                if(visitorsBean.isChildren()){
                    if(selectXhCount<xhCount){
                        view.setSelected(true);
                        visitorsBean.setSelect(true);
                        selectXhCount++;
                        selectList.add(visitorsBean);
                    }
                }else {
                    if(selectCrCount<crCount){
                        view.setSelected(true);
                        visitorsBean.setSelect(true);
                        selectCrCount++;
                        selectList.add(visitorsBean);
                    }else {
                        if(type==2){//选择预订人是单选
                            OftenVisitorsBean mOftenVisitorsBean=selectList.get(0);
                            mOftenVisitorsBean.setSelect(false);
                            selectList.clear();
                            view.setSelected(true);
                            visitorsBean.setSelect(true);
                            selectList.add(visitorsBean);
                            adapter.notifyItemChanged(mOftenVisitorsBean);
                        }
                    }
                }
            }
            setSelectData();
        }else if(view.getId()==R.id.iv_edit){
            TourBean tourBean=new TourBean(visitorsBean.getId(),visitorsBean.getName(),visitorsBean.getTel(),visitorsBean.getIdentityCard());
            EventBus.getDefault().postSticky(tourBean);
            startToActivity(AddTouristsActivity.class);
        }
    }
    private void setSelectData(){
        selectCount=selectCrCount+selectXhCount;
        if(type==1){
            dataBinding.tvSelect.setText(""+selectCount);
            if(selectCount==0){
                dataBinding.tvConfirm.setText("您需选择"+crCount+"位成人，"+xhCount+"位儿童");
            }else if(selectCrCount<crCount&&selectXhCount<xhCount){
                dataBinding.tvConfirm.setText("您还需选择"+(crCount-selectCrCount)+"位成人，"+(xhCount-selectXhCount)+"位儿童");
            }else if(selectCrCount==crCount&&selectXhCount<xhCount){
                dataBinding.tvConfirm.setText("您还需选择"+(xhCount-selectXhCount)+"位儿童");
            }else if(selectCrCount<crCount&&selectXhCount==xhCount){
                dataBinding.tvConfirm.setText("您还需选择"+(crCount-selectCrCount)+"位成人");
            }else if(selectCrCount==crCount&&selectXhCount==xhCount){
                dataBinding.tvConfirm.setText("确定");
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
