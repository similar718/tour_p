package cn.xmzt.www.route.fragment;


import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseFragment;
import cn.xmzt.www.databinding.FragmentRouteBinding;
import cn.xmzt.www.popup.FilterPopupWindow;
import cn.xmzt.www.popup.SortPopupWindow;
import cn.xmzt.www.route.activity.KeywordSearchActivity;
import cn.xmzt.www.route.activity.RouteDetailActivity1;
import cn.xmzt.www.route.adapter.RouteAdapter;
import cn.xmzt.www.route.bean.FilterBean;
import cn.xmzt.www.route.bean.FilterThemeBean;
import cn.xmzt.www.route.bean.RouteFiltrateList;
import cn.xmzt.www.route.bean.RoutePage;
import cn.xmzt.www.route.vm.RouteViewModel;
import cn.xmzt.www.utils.StatusBarUtil;
import cn.xmzt.www.view.listener.ItemListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

public class RouteFrament extends BaseFragment<RouteViewModel, FragmentRouteBinding> implements ItemListener {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_route;
    }

    @Override
    protected RouteViewModel createViewModel() {
        viewModel = new RouteViewModel();
        viewModel.mRoutePage.observe(this, new Observer<RoutePage>() {
            @Override
            public void onChanged(@Nullable RoutePage page) {
                //隐藏对话框
                if(page!=null){
                    List<RoutePage.ItemsBean> items=page.getItems();
                    if (viewModel.isRefresh){
                        pageNum=1;
                        dataBinding.refreshLayout.finishRefresh();
                        adapter.setDatas(items);
                    }else {
                        dataBinding.refreshLayout.finishLoadMore();
                        if(items.size()>0){
                            pageNum=pageNum+1;
                            adapter.addDatas(items);
                        }
                    }
                    isNextPage=page.getTotalPage()>pageNum;
                    dataBinding.refreshLayout.setNoMoreData(!isNextPage);
                    if (adapter.getItemCount()>0) {
                        dataBinding.tvNoData.setVisibility(View.GONE);
                        dataBinding.recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        dataBinding.tvNoData.setVisibility(View.VISIBLE);
                        dataBinding.recyclerView.setVisibility(View.GONE);
                    }
                }else {
                    if (viewModel.isRefresh){
                        dataBinding.refreshLayout.finishRefresh();
                    }else {
                        dataBinding.refreshLayout.finishLoadMore();
                    }
                }
            }
        });
        viewModel.mRouteFiltrateList.observe(this, new Observer<RouteFiltrateList>() {
            @Override
            public void onChanged(@Nullable RouteFiltrateList mRouteFiltrateList) {
                RouteFrament.this.mRouteFiltrateList=mRouteFiltrateList;
            }
        });
        return viewModel;
    }

    private RouteAdapter adapter;
    private int pageNum=1;
    boolean isNextPage = true;

    private RouteFiltrateList mRouteFiltrateList=null;
    private int statusBarHeight=0;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            StatusBarUtil.setStatusBarLightMode(getActivity(),true);
        }
    }

    @Override
    protected void initData() {
        StatusBarUtil.setStatusBarLightMode(getActivity(),true);
        statusBarHeight= StatusBarUtil.getStatusBarHeight(getContext());
        LinearLayout.LayoutParams mLayoutParams= (LinearLayout.LayoutParams) dataBinding.titleLayout.getLayoutParams();
        mLayoutParams.topMargin=statusBarHeight;
        GridLayoutManager manager = (GridLayoutManager) dataBinding.recyclerView.getLayoutManager();
        manager.setSpanCount(1);
        adapter=new RouteAdapter();
        adapter.setItemListener(this);
        dataBinding.setAdapter(adapter);
        dataBinding.setFragment(this);
        dataBinding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                viewModel.lineSearch(false,pageNum+1);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.lineSearch(true,1);
            }
        });
        viewModel.initData();
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_search:{
                Intent mIntent = new Intent(getContext(), KeywordSearchActivity.class);
                mIntent.putExtra("A",2);//数据类型：1酒店 2线路 3景区门票 4景区工具类
                startActivity(mIntent);
                break;
            }
            case R.id.sort_rl:{
                dismissOtherPopup(1);
                if(sortPopupWindow==null||!sortPopupWindow.isShowing()){
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(dataBinding.sortIv, "rotation", 0f,-180f);
                    objectAnimator.start();
                    showSortPopupWindow();
                }
                break;
            }
            case R.id.data_rl:{
                dismissOtherPopup(2);
                if(dateDaysPopupWindow==null||!dateDaysPopupWindow.isShowing()){
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(dataBinding.ivDateDays, "rotation", 0f,-180f);
                    objectAnimator.start();
                    showDateDaysPopupWindow();
                }
                break;
            }
            case R.id.address_rl:{
                dismissOtherPopup(3);
                if(departArrivalPopup==null||!departArrivalPopup.isShowing()){
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(dataBinding.ivCfdMdd, "rotation", 0f,-180f);
                    objectAnimator.start();
                    showDepartArrivalPopup();
                }
                break;
            }
            case R.id.select_rl:
                dismissOtherPopup(4);
                showThemePopup();
                break;
            default:
                break;
        }
    }
    @Override
    public void onItemClick(View view, int position) {
        RoutePage.ItemsBean item=adapter.getItem(position);
        Intent mIntent = new Intent(getContext(), RouteDetailActivity1.class);
        mIntent.putExtra("A",item.getId());
        mIntent.putExtra("B",item.getLineName());
        startActivity(mIntent);

    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }
    @Override
    public void showLoadFail(String msg) {
        super.showLoadFail(msg);
        if (viewModel.isRefresh){
            dataBinding.refreshLayout.finishRefresh();
        }else {
            dataBinding.refreshLayout.finishLoadMore();
        }
        viewModel.isRefresh=false;
    }
    private SortPopupWindow sortPopupWindow;
    private void showSortPopupWindow(){
        if(sortPopupWindow==null){
            sortPopupWindow=new SortPopupWindow(getActivity());
            sortPopupWindow.setItemListener(new ItemListener() {
                @Override
                public void onItemClick(View view, int position) {
                    dataBinding.tvSortName.setText(sortPopupWindow.getItem(position));
                    if(position==0){
                        viewModel.sortType=null;
                        dataBinding.tvSortName.setTextColor(Color.parseColor("#333333"));
                    }else {
                        dataBinding.tvSortName.setTextColor(Color.parseColor("#24A4FF"));
                        viewModel.sortType=String.valueOf(position);
                    }
                    sortPopupWindow.setSelectPosition(position);
                    sortPopupWindow.dismiss();
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(dataBinding.sortIv, "rotation", -180f,0f);
                    objectAnimator.start();
                    viewModel.lineSearch(true,1);
                }
            });
            sortPopupWindow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sortPopupWindow.dismiss();
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(dataBinding.sortIv, "rotation", -180f,0f);
                    objectAnimator.start();
                }
            });
        }
        List<String> list = new ArrayList<>();
        list.add("综合排序");
        list.add("销量优先");
        list.add("价格从低到高");
        list.add("价格从高到低");
        sortPopupWindow.setViewData(list);
        sortPopupWindow.showAsDropDown(dataBinding.layoutLl);
    }
    private FilterPopupWindow dateDaysPopupWindow;
    private void showDateDaysPopupWindow(){
        if(dateDaysPopupWindow==null){
            dateDaysPopupWindow=new FilterPopupWindow(getActivity());
            dateDaysPopupWindow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.getId()==R.id.tv_confirm){
                        FilterBean filterDays=dateDaysPopupWindow.getSelectPositionDays();//游玩天数
                        if("不限".equals(filterDays.getData())){
                            viewModel.daySize=null;
                        }else {
                            viewModel.daySize=String.valueOf(filterDays.getData());
                        }
                        FilterBean filterDate=dateDaysPopupWindow.getSelectPositionDate();//游玩出发日期
                        if("不限".equals(filterDate.getData())){
                            viewModel.departDate=null;
                        }else {
                            viewModel.departDate=String.valueOf(filterDate.getData());
                        }
                    }else if(v.getId()==R.id.tv_retry){
                        viewModel.daySize=null;
                        viewModel.departDate=null;
                        dateDaysPopupWindow.retryDateDays();
                    }

                    if(TextUtils.isEmpty(viewModel.departDate)&&TextUtils.isEmpty(viewModel.daySize)){
                        dataBinding.tvDateDays.setTextColor(Color.parseColor("#333333"));
                        dataBinding.tvDateDays.setText("日期天数");
                    }else {
                        dataBinding.tvDateDays.setTextColor(Color.parseColor("#24A4FF"));
                        if(!TextUtils.isEmpty(viewModel.departDate)&&!TextUtils.isEmpty(viewModel.daySize)){
                            dataBinding.tvDateDays.setText(viewModel.departDate+"/"+viewModel.daySize+"天");
                        }else if(!TextUtils.isEmpty(viewModel.departDate)&&TextUtils.isEmpty(viewModel.daySize)){
                            dataBinding.tvDateDays.setText(viewModel.departDate);
                        }else {
                            dataBinding.tvDateDays.setText(viewModel.daySize+"天");
                        }
                    }
                    dateDaysPopupWindow.dismiss();
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(dataBinding.ivDateDays, "rotation", -180f,0f);
                    objectAnimator.start();
                    if(v.getId()!=R.id.fillView){
                        viewModel.lineSearch(true,1);
                    }
                }
            });
        }
        List<FilterBean> filterList=new ArrayList<>();
        if(mRouteFiltrateList!=null){
            List<String> dayArray=mRouteFiltrateList.getDayArray();
            List<String> monthArray=mRouteFiltrateList.getMonthArray();
            filterList.add(new FilterBean(FilterBean.TYPE_CATEGORY,"游玩天数"));
            for(int i=0;i<dayArray.size();i++){
                filterList.add(new FilterBean(FilterBean.TYPE_DAYS,dayArray.get(i)));
            }
            filterList.add(new FilterBean(FilterBean.TYPE_CATEGORY,"出发月份"));
            for(int i=0;i<monthArray.size();i++){
                filterList.add(new FilterBean(FilterBean.TYPE_DATE,monthArray.get(i)));
            }
            dateDaysPopupWindow.setViewData(filterList);
            dateDaysPopupWindow.showAsDropDown(dataBinding.layoutLl);
        }else {
            viewModel.filtrateList();
        }
    }
    private FilterPopupWindow departArrivalPopup;
    private void showDepartArrivalPopup(){
        if(departArrivalPopup==null){
            departArrivalPopup=new FilterPopupWindow(getActivity());
            departArrivalPopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.getId()==R.id.tv_confirm){
                        FilterBean filterDepart=departArrivalPopup.getSelectPositionDepart();//出发地
                        if("全国".equals(filterDepart.getData())){
                            viewModel.city=null;
                        }else {
                            viewModel.city=String.valueOf(filterDepart.getData());
                        }

                        FilterBean filterArrival=departArrivalPopup.getSelectPositionArrival();//目的地
                        if("全国".equals(filterArrival.getData())){
                            viewModel.arrivalCity=null;
                        }else {
                            viewModel.arrivalCity=String.valueOf(filterArrival.getData());
                        }
                    }else if(v.getId()==R.id.tv_retry){
                        viewModel.city=null;
                        viewModel.arrivalCity=null;
                        departArrivalPopup.retryDepartArrival();
                    }

                    if(TextUtils.isEmpty(viewModel.city)&&TextUtils.isEmpty(viewModel.arrivalCity)){
                        dataBinding.tvCfdMdd.setTextColor(Color.parseColor("#333333"));
                        dataBinding.tvCfdMdd.setText("出发/目的地");
                    }else {
                        dataBinding.tvCfdMdd.setTextColor(Color.parseColor("#24A4FF"));
                        if(!TextUtils.isEmpty(viewModel.city)&&!TextUtils.isEmpty(viewModel.arrivalCity)){
                            dataBinding.tvCfdMdd.setText(viewModel.city+"/"+viewModel.arrivalCity);
                        }else if(!TextUtils.isEmpty(viewModel.city)&&TextUtils.isEmpty(viewModel.arrivalCity)){
                            dataBinding.tvCfdMdd.setText(viewModel.city);
                        }else {
                            dataBinding.tvCfdMdd.setText(viewModel.arrivalCity);
                        }
                    }
                    departArrivalPopup.dismiss();
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(dataBinding.ivCfdMdd, "rotation", -180f,0f);
                    objectAnimator.start();
                    if(v.getId()!=R.id.fillView){
                        viewModel.lineSearch(true,1);
                    }
                }
            });
        }
        List<FilterBean> filterList=new ArrayList<>();
        if(mRouteFiltrateList!=null){
            filterList.add(new FilterBean(FilterBean.TYPE_CATEGORY,"出发地"));
            List<String> departArray=mRouteFiltrateList.getDepartOriginalCityArray();
            List<String> arrivalArray=mRouteFiltrateList.getArrivalOriginalCityArray();
            for(int i=0;i<departArray.size();i++){
                filterList.add(new FilterBean(FilterBean.TYPE_DEPART,departArray.get(i)));
            }
            filterList.add(new FilterBean(FilterBean.TYPE_CATEGORY,"目的地"));
            for(int i=0;i<arrivalArray.size();i++){
                filterList.add(new FilterBean(FilterBean.TYPE_ARRIVAL,arrivalArray.get(i)));
            }
            departArrivalPopup.setViewData(filterList);
            departArrivalPopup.showAsDropDown(dataBinding.layoutLl);
        }else {
            viewModel.filtrateList();
        }
    }
    private FilterPopupWindow themePopup;
    private void showThemePopup(){
        if(themePopup==null){
            themePopup=new FilterPopupWindow(getActivity());
            themePopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.getId()==R.id.tv_confirm){
                        viewModel.theme=themePopup.getSelectPositionTheme();
                    }else if(v.getId()==R.id.tv_retry){
                        viewModel.theme=null;
                        themePopup.retryTheme();
                    }
                    themePopup.dismiss();
                    if(v.getId()!=R.id.fillView){
                        viewModel.lineSearch(true,1);
                    }
                }
            });
        }
        List<FilterBean> filterList=new ArrayList<>();
        if(mRouteFiltrateList!=null){
            filterList.add(new FilterBean(FilterBean.TYPE_CATEGORY,"线路主题"));
            List<FilterThemeBean> themeArray=mRouteFiltrateList.getThemeArray();
            for(int i=0;i<themeArray.size();i++){
                filterList.add(new FilterBean(FilterBean.TYPE_THEME,themeArray.get(i)));
            }
            themePopup.setViewData(filterList);
            themePopup.showAsDropDown(dataBinding.layoutLl);
        }else {
            viewModel.filtrateList();
        }
    }

    /**
     * 关闭其他的Popup
     * @param popupType 表示不隐藏的Popup 1表示排序方式 2表示日期/天数选择 3、表示出发地/目的地选择 4、表示主题选择
     */
    private void dismissOtherPopup(int popupType){
        if(sortPopupWindow!=null&&sortPopupWindow.isShowing()){
            if(popupType!=1){
                sortPopupWindow.dismiss();
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(dataBinding.sortIv, "rotation", -180f,0f);
                objectAnimator.start();
            }
        }else if(dateDaysPopupWindow!=null&&dateDaysPopupWindow.isShowing()){
            if(popupType!=2){
                dateDaysPopupWindow.dismiss();
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(dataBinding.ivDateDays, "rotation", -180f,0f);
                objectAnimator.start();
            }
        }else if(departArrivalPopup!=null&&departArrivalPopup.isShowing()){
            if(popupType!=3){
                departArrivalPopup.dismiss();
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(dataBinding.ivCfdMdd, "rotation", -180f,0f);
                objectAnimator.start();
            }
        }else if(themePopup!=null&&themePopup.isShowing()){
            if(popupType!=4){
                themePopup.dismiss();
            }
        }
    }
}
