package cn.xmzt.www.route.activity;

import androidx.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.base.WebActivity;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ActivityRouteDetail1Binding;
import cn.xmzt.www.dialog.RouteServiceAssuranceDialog;
import cn.xmzt.www.mine.activity.LoginActivity;
import cn.xmzt.www.route.adapter.RouteDetailAdapter;
import cn.xmzt.www.route.adapter.RouteDetailBannerAdapter;
import cn.xmzt.www.route.bean.PersonCountBean;
import cn.xmzt.www.route.bean.RouteDetailPage;
import cn.xmzt.www.route.vm.RouteDetailViewModel;
import cn.xmzt.www.share.ShareFunction;
import cn.xmzt.www.utils.DensityUtil;
import cn.xmzt.www.utils.SPUtils;
import cn.xmzt.www.utils.StatusBarUtil;
import cn.xmzt.www.utils.TimeUtil;
import cn.xmzt.www.view.ScrollGridLayoutManager;
import cn.xmzt.www.view.listener.ItemListener;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * 线路详情
 */
public class RouteDetailActivity1 extends TourBaseActivity<RouteDetailViewModel, ActivityRouteDetail1Binding> implements ItemListener {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_route_detail1;
    }

    @Override
    protected RouteDetailViewModel createViewModel() {
        dataBinding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int toolbarHeight = dataBinding.toolbar.getHeight();
                int dy = Math.abs(verticalOffset);
                float percent = (float) dy / toolbarHeight;
                if (percent >= 1) {
                    percent = 1;
                }
                float alpha = percent * 255;
                dataBinding.tvTitle.setAlpha(percent);
                dataBinding.toolbar.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
//                dataBinding.layoutToolbar.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                ViewGroup.LayoutParams mLayoutParams=dataBinding.toolbar.getLayoutParams();
                if (percent > 0.6) {
                    StatusBarUtil.setStatusBarLightMode(RouteDetailActivity1.this,true);
//                    mLayoutParams.height=getResources().getDimensionPixelOffset(R.dimen.title_bar_height);
                    mLayoutParams.height=getResources().getDimensionPixelOffset(R.dimen.title_bar_height)+statusBarHeight;
                    dataBinding.ivBack.setImageResource(R.drawable.back_gray);
                    dataBinding.ivShare.setImageResource(R.drawable.icon_share_gray_route);
                    if(viewModel.isCollection.get()==0){
                        viewModel.isCollection.set(2);
                    }else if(viewModel.isCollection.get()==1){
                        viewModel.isCollection.set(3);
                    }
                    dataBinding.ivBack.setAlpha(percent);
                    dataBinding.ivShare.setAlpha(percent);
                    dataBinding.ivCollection.setAlpha(percent);

                } else {
                    mLayoutParams.height=getResources().getDimensionPixelOffset(R.dimen.title_bar_height)+statusBarHeight;
                    StatusBarUtil.setStatusBarLightMode(RouteDetailActivity1.this,false);
                    dataBinding.ivBack.setImageResource(R.drawable.back_bac);
                    dataBinding.ivShare.setImageResource(R.drawable.icon_share_gray_white);
                    if(viewModel.isCollection.get()==2){
                        viewModel.isCollection.set(0);
                    }else if(viewModel.isCollection.get()==3){
                        viewModel.isCollection.set(1);
                    }

                    dataBinding.ivBack.setAlpha(1 - percent);
                    dataBinding.ivShare.setAlpha(1 - percent);
                    dataBinding.ivCollection.setAlpha(1 - percent);
                }
            }
        });
        viewModel = new RouteDetailViewModel();
        viewModel.setIView(this);
        viewModel.mRouteDetailPage.observe(this, new Observer<RouteDetailPage>() {
            @Override
            public void onChanged(@Nullable RouteDetailPage mRouteDetailPage) {
                boolean isCollection=mRouteDetailPage.isHasCollection();
                if(isCollection){
                    viewModel.isCollection.set(1);
                }else {
                    viewModel.isCollection.set(0);
                }
                photoUrl=mRouteDetailPage.getPhotoUrl();
                days=mRouteDetailPage.getDays();
                String routeName=mRouteDetailPage.getLineName();
                if(!TextUtils.isEmpty(routeName)){
                    dataBinding.tvTitle.setText(routeName);
                    RouteDetailActivity1.this.routeName=routeName;
                }
                routeIntro=mRouteDetailPage.getIntro();
                dataBinding.setRouteDetail(mRouteDetailPage);
                resourceList=mRouteDetailPage.getResourceList();
                dataBinding.banner.setImages(resourceList);
                dataBinding.banner.start();
                adapter.setData(mRouteDetailPage);
                manager.scrollToPosition(0);
            }
        });
        return viewModel;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent!=null){
            int routeId = intent.getIntExtra("A", 0);
            String routeName = intent.getStringExtra("B");
            if(routeId>0&&this.routeId!=routeId){
                this.routeId=routeId;
                this.routeName=routeName;
                dataBinding.tvTitle.setText(routeName);
                viewModel.getRouteDetail(TourApplication.getToken(), routeId);
            }
        }

    }

    RouteDetailAdapter adapter = null;
    ScrollGridLayoutManager manager = null;
    private List<RouteDetailPage.ResourceListBean> resourceList;//路宣传图或视频
    private int routeId;
    private String routeName;
    private String routeIntro;//线路宣传语（简介）
    private String photoUrl;//线路封面图片
    private int days=0;//出游天数
    private int statusBarHeight;
    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        StatusBarUtil.setStatusBarLightMode(this,false);
        statusBarHeight= StatusBarUtil.getStatusBarHeight(getApplicationContext());
        ViewGroup.LayoutParams mLayoutParams=dataBinding.toolbar.getLayoutParams();
        mLayoutParams.height=getResources().getDimensionPixelOffset(R.dimen.title_bar_height)+statusBarHeight;
        CoordinatorLayout.LayoutParams listParams = (CoordinatorLayout.LayoutParams) dataBinding.listLayout.getLayoutParams();
        listParams.topMargin=-statusBarHeight;

        dataBinding.setActivity(this);
        Intent intent = getIntent();
        routeId = intent.getIntExtra("A", 0);
        routeName = intent.getStringExtra("B");
        dataBinding.setVm(viewModel);
        dataBinding.tvTitle.setText(routeName);
        dataBinding.banner.setImageLoader(new RouteDetailBannerAdapter());
        selectTab(1);
        adapter = new RouteDetailAdapter();
        adapter.setItemListener(this);
        dataBinding.setAdapter(adapter);
        dataBinding.banner.isAutoPlay(false);
        dataBinding.banner.setIndicatorGravity(BannerConfig.RIGHT);
        dataBinding.banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        TextView numIndicator = dataBinding.banner.findViewById(R.id.numIndicator);
        numIndicator.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        numIndicator.setTextColor(Color.WHITE);
        numIndicator.setBackground(null);
        RelativeLayout.LayoutParams mumLayoutParams=(RelativeLayout.LayoutParams)numIndicator.getLayoutParams();
        mumLayoutParams.setMargins(DensityUtil.dip2px(getApplicationContext(),8),
                DensityUtil.dip2px(getApplicationContext(),8),
                DensityUtil.dip2px(getApplicationContext(),8),
                DensityUtil.dip2px(getApplicationContext(),28));
        dataBinding.banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if(position<resourceList.size()){
                    RouteDetailPage.ResourceListBean resource=resourceList.get(position);
                    if("2".equals(resource.getType())){
                        Intent intent=new Intent(RouteDetailActivity1.this,VideoPlayActivity.class);
                        intent.putExtra("url",resource.getUrl());
                        intent.putExtra("thumb",resource.getThumb());
                        startActivity(intent);
                    }else {
                        Intent intent=new Intent(RouteDetailActivity1.this,RoutePhotoViewActivity.class);
                        intent.putExtra("A",position);
                        intent.putStringArrayListExtra("B",viewModel.resourcetoStringArrayList(resourceList));
                        startActivity(intent);
                    }
                }
            }
        });
        manager=new ScrollGridLayoutManager(this, 2, GridLayoutManager.VERTICAL,false);
        dataBinding.recyclerView.setLayoutManager(manager);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                boolean isSingleShow = adapter.isSingleShow(position);
                return isSingleShow ? manager.getSpanCount() : 1;
            }
        });
        dataBinding.recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int padding = DensityUtil.dip2px(getApplicationContext(), 10);
                int childAdapterPosition = parent.getChildAdapterPosition(view);
                int recommendsFirstIndex = adapter.getItemCount() - adapter.recommendsSize-1;
                int index = childAdapterPosition - recommendsFirstIndex;
                if (index >= 0) {
                    if (index < 2) {
                        outRect.top = 2 * padding;
                    }
                    if (index % 2 == 0) {
                        outRect.left = padding;
                        outRect.right = padding / 2;
                    } else {
                        outRect.left = padding / 2;
                        outRect.right = padding;
                    }
                }
            }
        });
        dataBinding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_DRAGGING){
                    //正在用手指滚动
                    if(scrollY<=0&&isFirstStopScroll){
//                        dataBinding.recyclerView.setNestedScrollingEnabled(true);
                        isFirstStopScroll=false;
                    }else {
                        isFirstStopScroll=true;
//                        dataBinding.recyclerView.setNestedScrollingEnabled(false);
                    }
                }
                /*if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    //停止滚动
                }else if(newState==RecyclerView.SCROLL_STATE_DRAGGING){
                    //正在被外部拖拽,一般为用户正在用手指滚动
                }else if(newState==RecyclerView.SCROLL_STATE_SETTLING){
                    //自动滚动开始
                }*/
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollY=dy;
                scrollH=scrollH+dy;
                int firstVisibleItemPosition=manager.findFirstVisibleItemPosition();//获取第一个可见的项
                if(firstVisibleItemPosition==adapter.startindex_drtj){
                    viewModel.selected.set(1);
                    selectTab(1);
                }else if(firstVisibleItemPosition==adapter.startindex_xc){
                    viewModel.selected.set(2);
                    selectTab(2);
                    dataBinding.appbar.setExpanded(false,true);
                }else if(firstVisibleItemPosition==adapter.startindex_fy){
                    viewModel.selected.set(3);
                    selectTab(3);
                    dataBinding.appbar.setExpanded(false,true);
                }else if(firstVisibleItemPosition==adapter.startindex_xz){
                    viewModel.selected.set(4);
                    selectTab(4);
                    dataBinding.appbar.setExpanded(false,true);
                }
            }
        });
        viewModel.getRouteDetail(TourApplication.getToken(), routeId);
    }
    private int scrollH=0;
    private int scrollY=0;
    private boolean isFirstStopScroll=false;
    private PersonCountBean mPersonCountBean=null;
    @Subscribe
    public void selectPersonCountBean(PersonCountBean mPersonCountBean){
        if(mPersonCountBean!=null){
            this.mPersonCountBean=mPersonCountBean;
            dataBinding.setPersonCount(mPersonCountBean);
            String cyTime=mPersonCountBean.getTime();
            long startTimeLong= TimeUtil.strToDate(cyTime,"yyyy-MM-dd").getTime();
            String startTime=TimeUtil.dateToStr(startTimeLong,"M月d日");
            long endTimeLong=startTimeLong+days*24*60*60*1000;
            String endTime=TimeUtil.dateToStr(endTimeLong,"M月d日");
            dataBinding.tvSjd.setText(startTime+"-"+endTime);
        }
    }

    /**
     * 改变tabbar的选中颜色
     * @param index
     */
    private void selectTab(int index){
        dataBinding.tvTab1.setTextColor(Color.parseColor("#666666"));
        dataBinding.tvTab2.setTextColor(Color.parseColor("#666666"));
        dataBinding.tvTab3.setTextColor(Color.parseColor("#666666"));
        dataBinding.tvTab4.setTextColor(Color.parseColor("#666666"));
        dataBinding.tvTab1.getPaint().setFakeBoldText(false);
        dataBinding.tvTab2.getPaint().setFakeBoldText(false);
        dataBinding.tvTab3.getPaint().setFakeBoldText(false);
        dataBinding.tvTab4.getPaint().setFakeBoldText(false);
        if(index==1){
            dataBinding.tvTab1.setTextColor(Color.parseColor("#333333"));
            dataBinding.tvTab1.getPaint().setFakeBoldText(true);
        }else if(index==2){
            dataBinding.tvTab2.setTextColor(Color.parseColor("#333333"));
            dataBinding.tvTab2.getPaint().setFakeBoldText(true);
        }else if(index==3){
            dataBinding.tvTab3.setTextColor(Color.parseColor("#333333"));
            dataBinding.tvTab3.getPaint().setFakeBoldText(true);
        }else if(index==4){
            dataBinding.tvTab4.setTextColor(Color.parseColor("#333333"));
            dataBinding.tvTab4.getPaint().setFakeBoldText(true);
        }
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:{
                onBackPressed();
                break;
            }
            case R.id.layout1:{
                //产品特色
                viewModel.selected.set(1);
                selectTab(1);
                dataBinding.appbar.setExpanded(false,true);
                dataBinding.recyclerView.smoothScrollToPosition(adapter.startindex_drtj);
                break;
            }
            case R.id.layout2: {
                //行程
                viewModel.selected.set(2);
                selectTab(2);
                dataBinding.appbar.setExpanded(false,true);
                dataBinding.recyclerView.smoothScrollToPosition(adapter.startindex_xc);
                break;
            }
            case R.id.layout3: {
                //费用
                viewModel.selected.set(3);
                selectTab(3);
                dataBinding.appbar.setExpanded(false,true);
                dataBinding.recyclerView.smoothScrollToPosition(adapter.startindex_fy);
                break;
            }
            case R.id.layout4: {
                //须知
                viewModel.selected.set(4);
                selectTab(4);
                dataBinding.appbar.setExpanded(false,true);
                dataBinding.recyclerView.smoothScrollToPosition(adapter.startindex_xz);
                break;
            }
            case R.id.ivShare:{
                // 分享
                ShareFunction.getInstance().showWebShareAction(this, routeName,photoUrl,
                        routeIntro,"https://g.xmzt.cn/g/g?t=2&i="+routeId+"&p="+TourApplication.getRefCode(), ShareFunction.SHARE_WEIXIN);
                break;
            }

            case R.id.ivCollection:{
                // 收藏
                viewModel.collection(TourApplication.getToken(), routeId);
                break;
            }

            case R.id.fwbzLayout: {
                //服务保障
                showServiceDialog();
                break;
            }
            case R.id.previewLayout:{
                //线路预览
                if(dataBinding.getRouteDetail()!=null){
                    Intent intent = new Intent(this, RoutePreviewMapActivity.class);
                    intent.putExtra("A", dataBinding.getRouteDetail().getId());
                    intent.putExtra("B", dataBinding.getRouteDetail().getLineName());
                    startActivity(intent);
                }
                break;
            }
            case R.id.xzsjrqLayout: {
                //选择出发时间与人群
                if(SPUtils.iSLoginLive()){
                    Intent intent = new Intent(this, RouteTimePersonsActivity.class);
                    intent.putExtra("A",routeId);
                    if(dataBinding.getRouteDetail()!=null){
                        intent.putExtra("B",dataBinding.getRouteDetail().getLineName());
                        intent.putExtra("C",dataBinding.getRouteDetail().getPhotoUrl());
                    }
                    if(mPersonCountBean!=null){
                        intent.putExtra("D",mPersonCountBean);
                    }
                    startActivityForResult(intent,1);
                }else {
                    startToActivity(LoginActivity.class);
                }

                break;
            }
            case R.id.lqLayout:{
                //领券
                Intent intent = new Intent(this, CouponGetActivity.class);
                intent.putExtra("A",1);
                startActivity(intent);
                break;
            }
            case R.id.tvBackTop:
                //返回顶部
                dataBinding.appbar.setExpanded(true,true);
                manager.scrollToPosition(0);
                break;
            case R.id.tvKF:{
                //客服
                callPhone(Constants.CLIENTTELNUM);
//                Intent intent = new Intent(this, WebActivity.class);
//                intent.putExtra("title", "客服");
//                intent.putExtra("url", " http://kefu.xmzt.cn:80/im/text/1Akh0B.html");
//                startActivity(intent);
                break;
            }
            case R.id.tvReserve:{
                //立即预定
                if(SPUtils.iSLoginLive()){
                    Intent intent = new Intent();
                    intent.putExtra("A",routeId);
                    if(dataBinding.getRouteDetail()!=null){
                        intent.putExtra("B",dataBinding.getRouteDetail().getLineName());
                        intent.putExtra("C",dataBinding.getRouteDetail().getPhotoUrl());
                    }
                    if(mPersonCountBean!=null){
                        intent.putExtra("D",mPersonCountBean);
                        intent.setClass(this, RouteFillinOrderActivity.class);
                    }else {
                        intent.setClass(this, RouteTimePersonsActivity.class);
                    }
                    startActivityForResult(intent,1);
                }else {
                    startToActivity(LoginActivity.class);
                }
                break;
            }
            default:
                break;
        }
    }

    /**
     * 拨打电话（直接拨打电话）
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum){
        Intent intent = new Intent(Intent.ACTION_DIAL);//ACTION_DIAL ACTION_CALL
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }


    private RouteServiceAssuranceDialog serviceDialog;
    private void showServiceDialog(){
        if(serviceDialog==null){
            serviceDialog=new RouteServiceAssuranceDialog(this);
        }
        serviceDialog.show();
    }
    @Override
    public void onItemClick(View view, int position) {
        Object obj=adapter.getItem(position);
        if(obj instanceof RouteDetailPage.RecommendsBean){
            RouteDetailPage.RecommendsBean mRecommendsBean= (RouteDetailPage.RecommendsBean) obj;
            routeId = mRecommendsBean.getId();
            routeName = mRecommendsBean.getLineName();
            dataBinding.tvTitle.setText(routeName);
            viewModel.getRouteDetail(TourApplication.getToken(), routeId);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
