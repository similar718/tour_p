package cn.xmzt.www.intercom.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseFragment;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.ToRouteListBus;
import cn.xmzt.www.databinding.FragmentSchedulingListBinding;
import cn.xmzt.www.dialog.GuideDialog;
import cn.xmzt.www.home.bean.RecommendLineBean;
import cn.xmzt.www.intercom.activity.SchedulingDetailActivity;
import cn.xmzt.www.intercom.activity.SchedulingOverListActivity;
import cn.xmzt.www.intercom.adapter.SchedulingListAdapter;
import cn.xmzt.www.intercom.bean.TourTripBean;
import cn.xmzt.www.intercom.bean.TourTripListBean;
import cn.xmzt.www.intercom.event.IctercomSpeakingEvent;
import cn.xmzt.www.intercom.event.RefreshSchedulingListBus;
import cn.xmzt.www.intercom.vm.SchedulingListViewModel;
import cn.xmzt.www.main.globals.TalkManage;
import cn.xmzt.www.manager.IntentManager;
import cn.xmzt.www.mine.activity.LoginActivity;
import cn.xmzt.www.route.activity.RouteDetailActivity1;
import cn.xmzt.www.share.ShareFunction;
import cn.xmzt.www.utils.GPSUtils;
import cn.xmzt.www.utils.SPUtils;
import cn.xmzt.www.utils.StatusBarUtil;
import cn.xmzt.www.view.listener.ItemListener;

public class SchedulingListFragment extends BaseFragment<SchedulingListViewModel, FragmentSchedulingListBinding> implements ItemListener {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_scheduling_list;
    }

    @Override
    protected SchedulingListViewModel createViewModel() {
        viewModel = new SchedulingListViewModel();
        viewModel.mutableLiveData.observe(this, new Observer<TourTripListBean>() {
            @Override
            public void onChanged(@Nullable TourTripListBean page) {
                int size=page.getTourTripList().size();
                if(adapter!=null){
                    if (isRefresh){
                        pageNum=1;
                        dataBinding.refreshLayout.finishRefresh();
                    }else {
                        dataBinding.refreshLayout.finishLoadMore();
                        if(size>0){
                            pageNum=pageNum+1;
                        }
                    }
                    if(size<20){
                        dataBinding.refreshLayout.setNoMoreData(true);
                    }
                    adapter.setData(page,isRefresh);
                    if (size > 0){
                        if (!SPUtils.getBoolean("SchedulingListFragment", false)) {
                            GuideDialog dialog = new GuideDialog(getActivity());
                            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_scheduling_list_fragment, null);
                            ImageView know = view.findViewById(R.id.iv_know);
                            know.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.setContentView(view);
                            dialog.show();
                            SPUtils.putBoolean("SchedulingListFragment", true);
                        }
                    }
                }
            }
        });
        return viewModel;
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            StatusBarUtil.setStatusBarLightMode(getActivity(),true);
//            ImmersionBar.with(getActivity()).barAlpha(0).statusBarDarkFont(true).init();
            showFirstGuidePage();
        }
    }

    private void showFirstGuidePage(){
        // 判断当前界面有无显示录音按钮的显示  显示就显示
        if (TalkManage.isShowTalk) {
            if (!SPUtils.getBoolean("SchedulingListFragmentVoice_1", false)) {
                GuideDialog dialog = new GuideDialog(getActivity());
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_scheduling_list_fragment_voice_1, null);
                ImageView know = view.findViewById(R.id.iv_know);
                know.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        showFirstGuidePage2();
                    }
                });
                dialog.setContentView(view);
                dialog.show();
                SPUtils.putBoolean("SchedulingListFragmentVoice_1", true);
            }
        }
    }
    private void showFirstGuidePage2(){
        if (!SPUtils.getBoolean("SchedulingListFragmentVoice_2", false)) {
            GuideDialog dialog = new GuideDialog(getActivity());
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_scheduling_list_fragment_voice_2, null);
            ImageView know = view.findViewById(R.id.iv_know);
            know.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    showFirstGuidePage3();
                }
            });
            dialog.setContentView(view);
            dialog.show();
            SPUtils.putBoolean("SchedulingListFragmentVoice_2", true);
        }
    }
    private void showFirstGuidePage3(){
        if (!SPUtils.getBoolean("SchedulingListFragmentVoice_3", false)) {
            GuideDialog dialog = new GuideDialog(getActivity());
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_scheduling_list_fragment_voice_3, null);
            ImageView know = view.findViewById(R.id.iv_know);
            know.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.setContentView(view);
            dialog.show();
            SPUtils.putBoolean("SchedulingListFragmentVoice_3", true);
        }
    }

    private SchedulingListAdapter adapter;
    private boolean isInitData=false;
    private int statusBarHeight=0;
    private int pageNum=1;
    public boolean isRefresh = true;
    @Override
    protected void initData() {
        StatusBarUtil.setStatusBarLightMode(getActivity(),true);
//        ImmersionBar.with(getActivity()).barAlpha(0).statusBarDarkFont(true).init();
        statusBarHeight= StatusBarUtil.getStatusBarHeight(getContext());
        LinearLayout.LayoutParams mLayoutParams= (LinearLayout.LayoutParams) dataBinding.titleLayout.getLayoutParams();
        mLayoutParams.topMargin=statusBarHeight;
        isInitData=true;
        EventBus.getDefault().register(this);
        GridLayoutManager manager = (GridLayoutManager) dataBinding.recyclerView.getLayoutManager();
        manager.setSpanCount(1);
        adapter=new SchedulingListAdapter();
        dataBinding.recyclerView.setAdapter(adapter);
        adapter.setItemListener(this);
        dataBinding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isRefresh = false;
                viewModel.getTourTripList(pageNum+1);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                viewModel.getTourTripList(1);
            }
        });

        viewModel.getTourTripList(1);
        if(!SPUtils.iSLoginLive()){
            LoginActivity.start(getActivity());
        }
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
        if(mIctercomSpeakingEvent!=null&&adapter!=null){
            adapter.setCurrentIntercomGroup(mIctercomSpeakingEvent.getGroupId(),mIctercomSpeakingEvent.isSpeaking());
        }
    }
    @Subscribe
    public void onRefresh(RefreshSchedulingListBus refresh) {
        if(isInitData){
            if(viewModel!=null&&refresh!=null&&refresh.getType()==1){
                isRefresh=true;
                viewModel.getTourTripList(1);
            }
        }
    }
    @Override
    public void onItemClick(View view, int position) {
        Object obj=adapter.getItem(position);
        switch (view.getId()) {
            case R.id.tv_invite_friends:
                // 分享
                if(obj instanceof TourTripBean){
                    TourTripBean mTourTripBean= (TourTripBean) obj;
                    String url="https://g.xmzt.cn/g/g?t=5&i="+mTourTripBean.getGroupId()+"&p="+ TourApplication.getRefCode();
                    ShareFunction.getInstance().showWebWeChatShareAction(getActivity(), "我发现了一个不错的自驾游线路，想邀请你同游",mTourTripBean.getPhotoUrl(),
                            mTourTripBean.getViaInfo(),url);
                }
                break;
            case R.id.group_enter_layout:
//                if (GPSUtils.isOPen(getContext())) {
                    //进入群聊天页面
                    if(obj instanceof TourTripBean){
                        TourTripBean mTourTripBean= (TourTripBean) obj;
                        EventBus.getDefault().postSticky(mTourTripBean);
//                    TeamRoomActivity.start(getContext(),mTourTripBean.getGroupId(),true); TODO
                        IntentManager.getInstance().goSharedNavigationMapActivity(getContext(),mTourTripBean.getGroupId());
                    }
//                } else {
//                    cn.xmzt.www.utils.ToastUtils.showText(getContext(),"请打开位置权限/GPS位置信息");
//                }
                break;
            case R.id.cl_travel_info:
                //进入行程详情
                if(obj instanceof TourTripBean){
                    TourTripBean mTourTripBean= (TourTripBean) obj;
                    EventBus.getDefault().postSticky(mTourTripBean);
                    Intent intent = new Intent(getContext(), SchedulingDetailActivity.class);
                    intent.putExtra("A", mTourTripBean.getId());
                    intent.putExtra("B", mTourTripBean.getGroupId());
                    startActivity(intent);
                }
                break;
            case R.id.tv_new_route:
                //进入线路列表
                EventBus.getDefault().post(new ToRouteListBus(1));
                break;
            case R.id.tv_finished_xc:
                //到 已结束的行程 页面
                startActivity(new Intent(getActivity(), SchedulingOverListActivity.class));
                break;
            case R.id.itemLayout:
                //线路详情
                if(obj instanceof RecommendLineBean){
                    RecommendLineBean mRecommendLineBean= (RecommendLineBean) obj;
                    Intent mIntent = new Intent(getContext(), RouteDetailActivity1.class);
                    mIntent.putExtra("A",mRecommendLineBean.getLineId());
                    mIntent.putExtra("B",mRecommendLineBean.getLineName());
                    startActivity(mIntent);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
