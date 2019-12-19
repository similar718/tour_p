package cn.xmzt.www.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityCustomizeBinding;
import cn.xmzt.www.dialog.GuideDialog;
import cn.xmzt.www.dialog.TextTitleDialog;
import cn.xmzt.www.home.adapter.CustomizeAdapter;
import cn.xmzt.www.home.bean.CustomizeBean;
import cn.xmzt.www.home.event.CustomizeRefreshBus;
import cn.xmzt.www.home.vm.CustomizeViewModel;
import cn.xmzt.www.utils.SPUtils;
import cn.xmzt.www.utils.StatusBarUtil;
import cn.xmzt.www.view.listener.ItemListener;

/**
 * @describe 私人定制列表
 */
public class CustomizeActivity extends TourBaseActivity<CustomizeViewModel, ActivityCustomizeBinding> implements ItemListener {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_customize;
    }

    @Override
    protected CustomizeViewModel createViewModel() {
        viewModel = new CustomizeViewModel();
        viewModel.customizeList.observe(this, new Observer<List<CustomizeBean>>() {
            @Override
            public void onChanged(@Nullable List<CustomizeBean> result) {
                if (result.size() < pageSize) {
                    isNextPage = false;
                } else {
                    isNextPage = true;
                }
                if (isRefresh) {
                    pageNum = 1;
                    dataBinding.refreshLayout.finishRefresh();
                    adapter.setDatas(result);
                } else {
                    dataBinding.refreshLayout.finishLoadMore();
                    if (result.size() > 0) {
                        pageNum = pageNum + 1;
                        adapter.addDatas(result);
                    }
                }
                if(adapter.getItemCount()>0){
                    dataBinding.tvNoData.setVisibility(View.GONE);
                    dataBinding.viewDateLine.setVisibility(View.VISIBLE);
                    dataBinding.titleBarLayout.setBackgroundResource(R.color.color_F4_F4_F4);
                }else {
                    dataBinding.tvNoData.setVisibility(View.VISIBLE);
                    dataBinding.viewDateLine.setVisibility(View.GONE);
                    dataBinding.titleBarLayout.setBackgroundResource(R.color.color_FF_FF_FF);
                }
                dataBinding.refreshLayout.setNoMoreData(!isNextPage);
            }
        });
        return viewModel;
    }

    private int pageNum = 1;
    private int pageSize = 15;
    //是否是刷新
    boolean isRefresh = true;
    boolean isNextPage = true;

    CustomizeAdapter adapter = null;
    GridLayoutManager manager = null;
    private int statusBarHeight;
    @Override
    protected void initData() {
        Intent intent = getIntent();
        EventBus.getDefault().register(this);
        dataBinding.setActivity(this);
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarLightMode(this,true);
        manager = (GridLayoutManager) dataBinding.recyclerView.getLayoutManager();
        manager.setSpanCount(1);
        adapter = new CustomizeAdapter();
        adapter.setItemListener(this);
        dataBinding.recyclerView.setAdapter(adapter);
        viewModel.getCustomizeList(1, pageSize);
        dataBinding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isRefresh = false;
                if (viewModel != null) {
                    viewModel.getCustomizeList(pageNum + 1, pageSize);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                if (viewModel != null) {
                    viewModel.getCustomizeList(1, pageSize);
                }
            }
        });
        /**
         * 自定义RecyclerView实现对AppBarLayout的滚动效果
         */
        dataBinding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int scrollH = 0;
            int dp20=getResources().getDimensionPixelOffset(R.dimen.dp_20);
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollH =scrollH+dy;
                int titleBarHeight = dataBinding.titleBarLayout.getHeight();
                float percent = (float) scrollH / titleBarHeight;
                if (percent >= 1) {
                    percent = 1;
                }
                RelativeLayout.LayoutParams mLayoutParams= (RelativeLayout.LayoutParams) dataBinding.viewDateLine.getLayoutParams();
                if(scrollH<=dp20){
                    mLayoutParams.topMargin=dp20-scrollH;
                    dataBinding.viewDateLine.setLayoutParams(mLayoutParams);
                    dataBinding.viewDateLine.invalidate();
                }else {
                    mLayoutParams.topMargin=0;
                    dataBinding.viewDateLine.setLayoutParams(mLayoutParams);
                    dataBinding.viewDateLine.invalidate();
                }

                float alpha = percent * 255;
                dataBinding.titleBarLayout.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
            }
        });
//        if (!SPUtils.getBoolean("custom", false)) {
//            GuideDialog dialog = new GuideDialog(this);
//            View view = LayoutInflater.from(this).inflate(R.layout.dialog_personal_tailor, null);
//            ImageView ivKnow = view.findViewById(R.id.iv_know);
//            dialog.setContentView(view);
//            ivKnow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialog.dismiss();
//                }
//            });
//            dialog.show();
//            SPUtils.putBoolean("custom",true);
//        }
    }

    @Subscribe
    public void refreshBus(@NonNull CustomizeRefreshBus refresh) {
        isRefresh = true;
        if (viewModel != null) {
            viewModel.getCustomizeList(1, pageSize);
        }
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
    public void onItemClick(View view, int position) {
        CustomizeBean mCustomizeBean=adapter.getItem(position);
        if(mCustomizeBean!=null){
            Intent intent=new Intent(this,CustomizeInfoActivity.class);
            intent.putExtra("A",mCustomizeBean.getId());
            startActivity(intent);
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}

