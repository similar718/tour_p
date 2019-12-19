package cn.xmzt.www.home.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseFragment;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.databinding.FragmentHome1Binding;
import cn.xmzt.www.home.adapter.HomeAdapter;
import cn.xmzt.www.home.bean.HomeIndexBean;
import cn.xmzt.www.home.vm.HomeViewModel;
import cn.xmzt.www.manager.IntentManager;
import cn.xmzt.www.mine.activity.HorseSmallMiNotificationsActivity;
import cn.xmzt.www.mine.activity.LoginActivity;
import cn.xmzt.www.mine.event.UnreadMessageEvent;
import cn.xmzt.www.route.activity.KeywordSearchActivity;
import cn.xmzt.www.utils.DensityUtil;
import cn.xmzt.www.utils.SPUtils;
import cn.xmzt.www.utils.StatusBarUtil;
import cn.xmzt.www.utils.TimeUtil;
import cn.xmzt.www.utils.ToastUtils;
import cn.xmzt.www.view.listener.ItemListener;
import cn.xmzt.www.zxing.activity.CaptureActivity;

/**
 * @describe 首页
 */
public class HomeFragment extends BaseFragment<HomeViewModel, FragmentHome1Binding> implements ItemListener {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home1;
    }

    @Override
    protected HomeViewModel createViewModel() {
        viewModel = new HomeViewModel();
        viewModel.mutableLiveNewData.observe(this, new Observer<HomeIndexBean>() {
            @Override
            public void onChanged(@Nullable HomeIndexBean homeIndexBean) {
                if(homeIndexBean.getMsgUnRead()>0){
                    dataBinding.tvMsgNum.setVisibility(View.VISIBLE);
                    dataBinding.tvMsgNum.setText(""+homeIndexBean.getMsgUnRead());
                }else {
                    dataBinding.tvMsgNum.setVisibility(View.GONE);
                }
                EventBus.getDefault().postSticky(new UnreadMessageEvent(homeIndexBean.getMsgUnRead()));
                adapter.setData(homeIndexBean);
                dataBinding.refreshLayout.finishRefresh();
            }
        });
        viewModel.smartTeamGroup.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String groupId) {
                if (TextUtils.isEmpty(groupId)){
                    IntentManager.getInstance().goSmartTeamActivity(getActivity());
                } else {
//                    if (GPSUtils.isOPen(getActivity())) {
                        IntentManager.getInstance().goSmartTeamMapActivity(getActivity(), groupId, false);
//                    } else {
//                        cn.xmzt.www.utils.ToastUtils.showText(getActivity(),"请打开位置权限/GPS位置信息");
//                    }
                }
            }
        });
        return viewModel;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            StatusBarUtil.setFullScreen(getActivity());
            StatusBarUtil.setStatusBarLightMode(getActivity(),false);
            String sign_dialog_time= SPUtils.getString("sign_dialog_time");
            String time= TimeUtil.dateToStr("yyyy-MM-dd");
            if(!time.equals(sign_dialog_time)&&!TextUtils.isEmpty(TourApplication.getToken())){
                viewModel.postHomeNewIndex(getContext().getApplicationContext(),false);
            }
        }
    }

    private HomeAdapter adapter;
    private int statusBarHeight;
    @Override
    protected void initData() {
        StatusBarUtil.setFullScreen(getActivity());
        StatusBarUtil.setStatusBarLightMode(getActivity(),false);
//        ImmersionBar.with(getActivity()).barAlpha(0).statusBarDarkFont(false).init();
        statusBarHeight=StatusBarUtil.getStatusBarHeight(getContext());
        FrameLayout.LayoutParams mLayoutParams= (FrameLayout.LayoutParams) dataBinding.titleLayout.getLayoutParams();
        mLayoutParams.topMargin=statusBarHeight;
        dataBinding.recyclerView.setItemViewCacheSize(20);
        GridLayoutManager manager = (GridLayoutManager) dataBinding.recyclerView.getLayoutManager();
        manager.setSpanCount(2);
        adapter=new HomeAdapter();
        adapter.setItemListener(this);
        viewModel.setFragment(this);
        dataBinding.recyclerView.setAdapter(adapter);
        dataBinding.setFragment(this);
        dataBinding.refreshLayout.setEnableLoadMore(false);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getSpanCount(position);
            }
        });
        dataBinding.recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int childAdapterPosition = parent.getChildAdapterPosition(view);
                int dp_10= DensityUtil.dip2px(getContext(),10);
                if(childAdapterPosition>adapter.startJXXLIndex&&childAdapterPosition<adapter.getItemCount()-1){
                    int index=childAdapterPosition-adapter.startJXXLIndex-1;
                    FrameLayout.LayoutParams itemLayoutParams1= (FrameLayout.LayoutParams) view.findViewById(R.id.itemLayout).getLayoutParams();
                    int dp_5=DensityUtil.dip2px(getContext(),5);
                    if(index%2==0){
                        outRect.left = dp_10;
                        outRect.right = 0;

                        itemLayoutParams1.leftMargin=dp_10;
                        itemLayoutParams1.rightMargin=dp_5;
                    }else if(index%2==1){
                        outRect.left = 0;
                        outRect.right = dp_10;

                        itemLayoutParams1.leftMargin=dp_5;
                        itemLayoutParams1.rightMargin=dp_10;
                    }
                }
            }
        });
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
                scrollH =scrollH+dy;
                int titleBarHeight = dataBinding.titleLayout.getHeight()+statusBarHeight;
                float percent = (float) scrollH / titleBarHeight;
                if (percent >= 1) {
                    percent = 1;
                }
                float alpha = percent * 255;
                dataBinding.titleBarLayout.setBackgroundColor(Color.argb((int) alpha, 36, 164, 255));
            }
        });
        dataBinding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.isRefresh=true;
                viewModel.postHomeNewIndex(getContext().getApplicationContext(),false);
            }
        });
        HomeIndexBean homeIndexBean=viewModel.getHomeContentsToDataBase(getContext().getApplicationContext());
        if(homeIndexBean!=null){
            if(homeIndexBean.getMsgUnRead()>0){
                dataBinding.tvMsgNum.setVisibility(View.VISIBLE);
                dataBinding.tvMsgNum.setText(""+homeIndexBean.getMsgUnRead());
            }else {
                dataBinding.tvMsgNum.setVisibility(View.GONE);
            }
            adapter.setData(homeIndexBean);
        }
        viewModel.postHomeNewIndex(getContext().getApplicationContext(),true);
        EventBus.getDefault().register(this);
    }
    int scrollH = 0;
    public int unreadCount;
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onUnreadMessage(@NonNull UnreadMessageEvent event) {
        unreadCount=event.getUnreadCount();
        if(event.getUnreadCount()>0){
            dataBinding.tvMsgNum.setVisibility(View.VISIBLE);
            dataBinding.tvMsgNum.setText(""+event.getUnreadCount());
        }else{
            dataBinding.tvMsgNum.setVisibility(View.GONE);
        }
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_home_search:{
                Intent mIntent = new Intent(getContext(), KeywordSearchActivity.class);
                mIntent.putExtra("A",2);//数据类型：1酒店 2线路 3景区门票 4景区工具类
                startActivity(mIntent);
                break;
            }
            case R.id.tv_qr_code:{
               //二维码扫描
                if (TextUtils.isEmpty(TourApplication.getToken())) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    startCamera();
                }
                break;
            }
            case R.id.tv_msg:{
                ////马小秘系统消息
                if (TextUtils.isEmpty(TourApplication.getToken())) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
//                    startActivity(new Intent(getActivity(), MessageCenterActivity.class));
                    Intent intent = new Intent(getActivity(), HorseSmallMiNotificationsActivity.class);
                    intent.putExtra("A",unreadCount);
                    startActivity(intent);
                }
                break;
            }
            default:
                break;
        }
    }
    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.zncxLayout:{
                //智能出行
                if(!TextUtils.isEmpty(TourApplication.getToken())){
                    viewModel.getSmartTeamGroup();
                }else {
                    IntentManager.getInstance().goSmartTeamActivity(view.getContext());
                }
//                EventBus.getDefault().post(new ToRouteListBus(2));
                break;
            }
            case R.id.zhdlLayout:{
                //智慧导航
                IntentManager.getInstance().goWisdomGuideActivity(getActivity());
                break;
            }
            case R.id.mxmLayout:{
                //马小秘系统消息
                if (TextUtils.isEmpty(TourApplication.getToken())) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
//                    startActivity(new Intent(getActivity(), MessageCenterActivity.class));
                    Intent intent = new Intent(getActivity(), HorseSmallMiNotificationsActivity.class);
                    intent.putExtra("A",unreadCount);
                    startActivity(intent);
                }
                break;
            }
            default:
                break;
        }
    }
    /**
     * 检查权限
     */
    public void startCamera() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 进入这儿表示没有权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                // 提示已经禁止
                ToastUtils.showText(getActivity(), "请开启相机权限");
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 100);
            }
        } else {
            Intent intent = new Intent(getActivity(), CaptureActivity.class);
            getActivity().startActivityForResult(intent, 50);
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

