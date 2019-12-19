package cn.xmzt.www.mine.activity;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ActivityHorseMiNotificationsBinding;
import cn.xmzt.www.dialog.ScenicSpotWIFIHintDialog;
import cn.xmzt.www.mine.adapter.HorseMiNotificationsAdapter;
import cn.xmzt.www.mine.bean.HorseMiMessageBean;
import cn.xmzt.www.mine.bean.HorseMiMessageFilterBean;
import cn.xmzt.www.mine.bean.WeatherInfoBean;
import cn.xmzt.www.popup.MessageRemindFilterPopupWindow;
import cn.xmzt.www.route.vm.HorseMiNotificationsViewModel;
import cn.xmzt.www.utils.SchemeActivityUtil;
import cn.xmzt.www.utils.StatusBarUtil;
import cn.xmzt.www.utils.ToastUtils;
import cn.xmzt.www.view.ScrollGridLayoutManager;
import cn.xmzt.www.view.listener.ItemListener;

/**
 * 马小秘消息提醒
 */
public class HorseSmallMiNotificationsActivity extends TourBaseActivity<HorseMiNotificationsViewModel, ActivityHorseMiNotificationsBinding> implements ItemListener {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_horse_mi_notifications;
    }

    private Context mContext;
    private int pageNum=1;
    boolean isNextPage = true;
    @Override
    protected HorseMiNotificationsViewModel createViewModel() {
        viewModel = new HorseMiNotificationsViewModel();
        viewModel.mMessageList.observe(this, new Observer<List<HorseMiMessageBean>>() {
            @Override
            public void onChanged(@Nullable List<HorseMiMessageBean> list) {
                isNextPage=true;
                if(list!=null){
                    if (viewModel.isRefresh){
                        pageNum=1;
                        dataBinding.refreshLayout.finishRefresh();
                        adapter.setDatas(list);
                    }else {
                        dataBinding.refreshLayout.finishLoadMore();
                        if(list.size()>0){
                            pageNum=pageNum+1;
                            adapter.addDatas(list);
                        }else {
                            isNextPage=false;
                        }
                    }
                }else {
                    if (viewModel.isRefresh){
                        dataBinding.refreshLayout.finishRefresh();
                    }else {
                        dataBinding.refreshLayout.finishLoadMore();
                    }
                }
                dataBinding.refreshLayout.setNoMoreData(!isNextPage);
                if (adapter.getItemCount()>0) {
                    if (viewModel.isRefresh){
                        dataBinding.recyclerView.smoothScrollToPosition(0);
                        if(adapter.weatherInfoBean!=null){
                            dataBinding.filterLayout.setVisibility(View.INVISIBLE);
                            dataBinding.tvTitle.setVisibility(View.INVISIBLE);
//                            dataBinding.titleBarLayout.setBackgroundColor(Color.parseColor("#0075BCEA"));
                            dataBinding.titleBarLayout.setBackgroundColor(Color.argb(0, 117, 188, 234));
                        }else {
                            dataBinding.filterLayout.setVisibility(View.VISIBLE);
                            dataBinding.tvTitle.setVisibility(View.VISIBLE);
//                            dataBinding.titleBarLayout.setBackgroundColor(Color.parseColor("#75BCEA"));
                            dataBinding.titleBarLayout.setBackgroundColor(Color.argb(255, 117, 188, 234));
                        }
                        dataBinding.rootLayout.setBackgroundResource(R.drawable.horse_small_mi_bg);
                        dataBinding.tvNoData.setVisibility(View.GONE);
                        dataBinding.recyclerView.setVisibility(View.VISIBLE);
                    }
                } else {
                    dataBinding.rootLayout.setBackgroundColor(Color.parseColor("#F4F4F4"));
                    dataBinding.titleBarLayout.setBackgroundColor(Color.parseColor("#75BCEA"));
                    dataBinding.tvTitle.setVisibility(View.VISIBLE);
                    dataBinding.tvNoData.setVisibility(View.VISIBLE);
                    dataBinding.recyclerView.setVisibility(View.GONE);
                }
            }
        });
        viewModel.mMessageWeatherInfo.observe(this, new Observer<WeatherInfoBean>() {
            @Override
            public void onChanged(WeatherInfoBean weatherInfoBean) {
                if(weatherInfoBean!=null){
                    adapter.weatherInfoBean=weatherInfoBean;
                    if(adapter.getItemCount()>0){
                        adapter.notifyDataSetChanged();
                        dataBinding.filterLayout.setVisibility(View.INVISIBLE);
                        dataBinding.tvTitle.setVisibility(View.INVISIBLE);
                        dataBinding.titleBarLayout.setBackgroundColor(Color.parseColor("#0075BCEA"));
                    }
                }
            }
        });
        viewModel.mAllReadData.observe(this, new Observer<Object>() {
            @Override
            public void onChanged(Object o) {
                viewModel.getSysMsgList(true,true,1);
            }
        });
        return viewModel;
    }
    private HorseMiNotificationsAdapter adapter;
    private ScrollGridLayoutManager manager;
    private int statusBarHeight;
    @Override
    protected void initData() {
        mContext = this;
        viewModel.unreadCount=getIntent().getIntExtra("A",0);
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarLightMode(this,false);
        statusBarHeight= StatusBarUtil.getStatusBarHeight(getApplicationContext());
        FrameLayout.LayoutParams mLayoutParams= (FrameLayout.LayoutParams) dataBinding.layoutToolbar.getLayoutParams();
        mLayoutParams.topMargin=statusBarHeight;
        dataBinding.setActivity(this);
        adapter = new HorseMiNotificationsAdapter();
        adapter.setItemListener(this);
        dataBinding.recyclerView.setAdapter(adapter);
        manager=new ScrollGridLayoutManager(this, 1, GridLayoutManager.VERTICAL,false);
        dataBinding.recyclerView.setLayoutManager(manager);
        manager.setSpanCount(1);
        /**
         * 自定义RecyclerView实现对AppBarLayout的滚动效果
         */
        dataBinding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(adapter.weatherInfoBean!=null){
                    scrollH =scrollH+dy;
                    int titleBarHeight = getResources().getDimensionPixelOffset(R.dimen.dp_172);
                    float percent = (float) scrollH / titleBarHeight;
                    if (percent >= 1) {
                        percent = 1;
                        dataBinding.filterLayout.setVisibility(View.VISIBLE);
                        dataBinding.tvTitle.setVisibility(View.VISIBLE);
                    }else {
                        dataBinding.filterLayout.setVisibility(View.INVISIBLE);
                        dataBinding.tvTitle.setVisibility(View.INVISIBLE);
                    }
                    float alpha = percent * 255;
                    dataBinding.titleBarLayout.setBackgroundColor(Color.argb((int) alpha, 117, 188, 234));
                }
            }
        });
        dataBinding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                viewModel.getSysMsgList(false,false,pageNum+1);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.getSysMsgList(false,true,1);
            }
        });
        viewModel.getMsgScreeContentList();
        viewModel.getSysMsgList(true,true,1);
        viewModel.getWeatherInfo(Constants.mLatitude+"",Constants.mLongitude+"");
    }
    int scrollH = 0;
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:{
                onBackPressed();
                break;
            }
            case R.id.filterLayout:{
                //筛选
                showFilterPopupWindow();
                break;
            }
            default:
                break;
        }
    }
    @Override
    public void showLoadFail(String msg) {
        super.showLoadFail(msg);
        if (viewModel.isRefresh){
            dataBinding.refreshLayout.finishRefresh();
        }else {
            dataBinding.refreshLayout.finishLoadMore();
        }
    }

    private MessageRemindFilterPopupWindow filterPopupWindow;
    private void showFilterPopupWindow(){
        if(filterPopupWindow == null){
            filterPopupWindow = new MessageRemindFilterPopupWindow(this);
            filterPopupWindow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String msgTypeOld = TextUtils.isEmpty(viewModel.msgType) ? "" : viewModel.msgType;// 之前的类型
                    String findMonthOld = TextUtils.isEmpty(viewModel.findMonth) ? "" : viewModel.findMonth; // 之前选择的月份

                    if(v.getId() == R.id.tv_confirm){
                        HorseMiMessageFilterBean filterType = filterPopupWindow.getSelectPositionType();//消息类型
                        viewModel.msgType = filterType.getContentKey();
                        HorseMiMessageFilterBean filterDate = filterPopupWindow.getSelectPositionDate();//查询月份
                        viewModel.findMonth = filterDate.getContentKey();

                        filterPopupWindow.dismiss();
                        dataBinding.ivFilter.setImageResource(R.drawable.icon_mxm_filter_down);

                        String msgTypeNew = TextUtils.isEmpty(viewModel.msgType) ? "" : viewModel.msgType;
                        String findMonthNew = TextUtils.isEmpty(viewModel.findMonth) ? "" : viewModel.findMonth;

                        if (!msgTypeNew.equals(msgTypeOld) || !findMonthNew.equals(findMonthOld)){ // 如果当前类型与之前不一样就需要去调用
                            viewModel.getSysMsgList(true,true,1);
                        }
                    }else if(v.getId() == R.id.tv_retry){
                        filterPopupWindow.retry();
                    } else if (v.getId() == R.id.fillView){
                        filterPopupWindow.dismiss();
                        dataBinding.ivFilter.setImageResource(R.drawable.icon_mxm_filter_down);
                    }
                }
            });
        }
        if(viewModel.filterList.size()>0){
            dataBinding.ivFilter.setImageResource(R.drawable.icon_mxm_filter_up);
            filterPopupWindow.setViewData(viewModel.filterList);
            filterPopupWindow.showAsDropDown(dataBinding.filterLayout);
        }
    }



    @Override
    public void onItemClick(View view, int position) {
        if(R.id.iv_filter==view.getId()){
            if(viewModel.filterList.size()>0){
                int dp_172 = getResources().getDimensionPixelOffset(R.dimen.dp_172);
                dataBinding.recyclerView.scrollBy(0,dp_172);
                dataBinding.filterLayout.setVisibility(View.VISIBLE);
                dataBinding.tvTitle.setVisibility(View.VISIBLE);
                dataBinding.titleBarLayout.setBackgroundColor(Color.argb(255, 117, 188, 234));
                showFilterPopupWindow();
            }
        }else if(R.id.item_into_detail==view.getId()){
            HorseMiMessageBean horseMiMessage=adapter.getItem(position);
            viewModel.msgSignReadById(horseMiMessage.getId());
            horseMiMessage.setIfRead(2);
            adapter.notifyItemChanged(position);
            SchemeActivityUtil.pushToActivity(getApplicationContext(),horseMiMessage.getTitle(),horseMiMessage.getRelationId(),""+horseMiMessage.getTypes());
        } else if (R.id.tv_all_read == view.getId()){
            if(viewModel.unreadCount>0){
                showHorseSmallMiAllReadDialog();
            }else {
                ToastUtils.showText("您当前没有未读消息！");
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private ScenicSpotWIFIHintDialog mHintDialog;

    private void showHorseSmallMiAllReadDialog(){
        if (mHintDialog == null){
            mHintDialog = new ScenicSpotWIFIHintDialog(mContext, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.tv_sure){ // 全部标为已读
                        viewModel.allMsgAlreadyRead();
                    }
                    mHintDialog.dismiss();
                }
            });
        }
        mHintDialog.setContent("确定将全部信息标记为已读？","我再想想","确定");
        mHintDialog.show();
    }
}
