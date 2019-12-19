package cn.xmzt.www.intercom.activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.databinding.ActivitySchedulingListBinding;
import cn.xmzt.www.dialog.GuideDialog;
import cn.xmzt.www.dialog.TextTitleDialog;
import cn.xmzt.www.intercom.adapter.SchedulingOverListAdapter;
import cn.xmzt.www.intercom.bean.TourTripBean;
import cn.xmzt.www.intercom.bean.TourTripListBean;
import cn.xmzt.www.intercom.event.IctercomSpeakingEvent;
import cn.xmzt.www.intercom.vm.SchedulingListViewModel;
import cn.xmzt.www.manager.IntentManager;
import cn.xmzt.www.utils.GPSUtils;
import cn.xmzt.www.utils.SPUtils;
import cn.xmzt.www.view.listener.ItemListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 已结束的行程列表
 */
public class SchedulingOverListActivity extends TourBaseActivity<SchedulingListViewModel, ActivitySchedulingListBinding> implements ItemListener {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_scheduling_list;
    }

    @Override
    protected SchedulingListViewModel createViewModel() {
        viewModel = new SchedulingListViewModel();
        viewModel.isFinish=1;
        viewModel.mutableLiveData.observe(this, new Observer<TourTripListBean>() {
            @Override
            public void onChanged(@Nullable TourTripListBean page) {
                if (isRefresh){
                    pageNum=1;
                    dataBinding.refreshLayout.finishRefresh();
                }else {
                    dataBinding.refreshLayout.finishLoadMore();
                }
                int size=page.getTourTripList().size();
                if(size<20){
                    dataBinding.refreshLayout.setNoMoreData(true);
                }
                adapter.setData(page,isRefresh);
                if(adapter.getItemCount()==0){
                    dataBinding.tvNoData.setVisibility(View.VISIBLE);
                }
            }
        });
        viewModel.result.observe(this, new Observer<BaseDataBean<Object>>() {
            @Override
            public void onChanged(@Nullable BaseDataBean<Object> result) {
                isRefresh=true;
                viewModel.getTourTripList(1);
            }
        });
        return viewModel;
    }
    private SchedulingOverListAdapter adapter;
    private int pageNum=1;
    public boolean isRefresh = true;
    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        dataBinding.setActivity(this);
        GridLayoutManager manager = (GridLayoutManager) dataBinding.recyclerView.getLayoutManager();
        manager.setSpanCount(1);
        adapter = new SchedulingOverListAdapter();
        dataBinding.recyclerView.setAdapter(adapter);
        adapter.setItemListener(this);
        dataBinding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isRefresh=false;
                viewModel.getTourTripList(pageNum+1);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh=true;
                viewModel.getTourTripList(1);
            }
        });
        viewModel.getTourTripList(1);
    }
    @Override
    public void showLoadFail(String msg) {
        super.showLoadFail(msg);
        if (isRefresh){
            dataBinding.refreshLayout.finishRefresh();
        }else {
            dataBinding.refreshLayout.finishLoadMore();
        }
        isRefresh=false;
    }
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onTalkback(IctercomSpeakingEvent mIctercomSpeakingEvent) {
        if(mIctercomSpeakingEvent!=null){
            adapter.setCurrentIntercomGroup(mIctercomSpeakingEvent.getGroupId(),mIctercomSpeakingEvent.isSpeaking());
        }
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_edit:
                //编辑 完成编辑
                if(adapter.isEdit()){
                    adapter.setEdit(false);
                    dataBinding.tvEdit.setText("编辑");
                    dataBinding.bottomLayout.setVisibility(View.GONE);
                }else {
                    adapter.setEdit(true);
                    dataBinding.tvEdit.setText("完成");
                    dataBinding.bottomLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.all_CheckBox:
                if(adapter.isAllSelect()){
                    dataBinding.ivCheckBox.setSelected(false);
                    adapter.setAllSelect(false);
                }else {
                    dataBinding.ivCheckBox.setSelected(true);
                    adapter.setAllSelect(true);
                }
                break;
            case R.id.tv_del:
                //删除选中的行程
                if(adapter.selectList.size()>0){
                    showHintDialog();
                }else {
                    ToastUtils.showShort("请选择您要删除的行程");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        TourTripBean mTourTripBean=adapter.getItem(position);
        switch (view.getId()) {
            case R.id.group_enter_layout:
                //进入群聊天页面
//                if (GPSUtils.isOPen(this)) {
                    EventBus.getDefault().postSticky(mTourTripBean);
                    IntentManager.getInstance().goSharedNavigationMapActivity(this,mTourTripBean.getGroupId());
//                } else {
//                    cn.xmzt.www.utils.ToastUtils.showText(this,"请打开位置权限/GPS位置信息");
//                }
                break;
            case R.id.cl_travel_info:
                //进入行程详情
                EventBus.getDefault().postSticky(mTourTripBean);
                Intent intent = new Intent(this, SchedulingDetailActivity.class);
                intent.putExtra("A", mTourTripBean.getId());
                intent.putExtra("B", mTourTripBean.getGroupId());
                startActivity(intent);
                break;
            case R.id.iv_check:
                //选择
                if(mTourTripBean.isSelect()){
                    adapter.selectList.remove(mTourTripBean);
                    mTourTripBean.setSelect(false);
                }else {
                    mTourTripBean.setSelect(true);
                    adapter.selectList.add(mTourTripBean);
                }
                adapter.notifyItemChanged(position);
                if(adapter.isAllSelect()){
                    dataBinding.ivCheckBox.setSelected(true);
                }else {
                    dataBinding.ivCheckBox.setSelected(false);
                }
                break;
            default:
                break;
        }
    }

    private TextTitleDialog hintDialog;
    private void showHintDialog(){
        if(hintDialog==null){
            hintDialog = new TextTitleDialog(this, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hintDialog.dismiss();
                    viewModel.deleteGroup(adapter.getGroupIds());
                }
            });
        }
        hintDialog.setTitle("确定要删除行程？");
        hintDialog.show();;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
