package cn.xmzt.www.selfdrivingtools.activity;

import android.Manifest;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ActivityScenicSpotSearchResultBinding;
import cn.xmzt.www.selfdrivingtools.adapter.ScenicSpotSearchResultAdapter;
import cn.xmzt.www.selfdrivingtools.bean.WisdomGuideInfo;
import cn.xmzt.www.manager.IntentManager;
import cn.xmzt.www.selfdrivingtools.viewmodel.ScenicSpotSearchViewModel;
import cn.xmzt.www.service.LocationService;
import cn.xmzt.www.utils.StatusBarUtil;
import cn.xmzt.www.utils.ToastUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;


/**
 * 景点搜索结果界面
 */
public class ScenicSpotSearchResultActivity extends TourBaseActivity<ScenicSpotSearchViewModel, ActivityScenicSpotSearchResultBinding> {

    private  String mKeywords = ""; // 搜索关键字
    private  int mPageNum = 1;
    private  int mPageSize = 20;
    private  int mTotalSize = 0;

    private  List<WisdomGuideInfo.ItemsBean> list = new ArrayList<>();

    ScenicSpotSearchResultAdapter adapter = null;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_scenic_spot_search_result;
    }

    @Override
    protected ScenicSpotSearchViewModel createViewModel() {
        viewModel = new ScenicSpotSearchViewModel();
        viewModel.setIView(this);
        // 搜索的时候回传的数据显示
        viewModel.mKeyWordsSearchList.observe(this, new Observer<WisdomGuideInfo>() {
            @Override
            public void onChanged(@Nullable WisdomGuideInfo wisdomGuideInfo) {
                setData(wisdomGuideInfo);
            }
        });
        return viewModel;
    }

    @Override
    protected void initData() {
        // 设置顶部状态栏
        StatusBarUtil.setStatusBarLightMode(this,true);
        dataBinding.setModel(viewModel);
        dataBinding.setActivity(this);
        mKeywords = getIntent().getStringExtra("keyword");

        viewModel.getKeyWordsSearchList(Constants.mCity,mKeywords,Constants.mLocation,mPageNum,mPageSize);

        // 列表显示问题
        dataBinding.rvSearchResult.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        // 讲值赋值给列表
        adapter = new ScenicSpotSearchResultAdapter(list,this);
        dataBinding.rvSearchResult.setAdapter(adapter);

        dataBinding.tvTitle.setText(mKeywords); // 界面title赋值

        // 写入数据库
        viewModel.setAddSearchHistoryInfo(mKeywords,Constants.KeywordsType.FUNTYPES_SCENIC,System.currentTimeMillis(),getApplicationContext());

        // 上拉加载更多 下拉刷新事件处理
        dataBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPageNum = 1;
                getDataList();
            }
        });
        dataBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                // 需要判断数据可以加载几页
                if (mTotalSize > (mPageNum * mPageSize)) { // 如果当前显示数据小于总数据 表示还有下一页
                    mPageNum += 1;
                    getDataList();
                } else {
                    ToastUtils.showText(ScenicSpotSearchResultActivity.this,"我也是有底线的");
                    // 在不请求接口的情况下 将它finish掉 否则会一直加载
                    dataBinding.refreshLayout.finishLoadMore(true);
                }
            }
        });
    }

    private void getDataList(){
        // 获取
        viewModel.getKeyWordsSearchList(Constants.mCity,mKeywords,Constants.mLocation,mPageNum,mPageSize);
    }

    private void setData(WisdomGuideInfo data){
        if (data != null && mPageNum == 1) {
            if (data.getItems().size()>0) {
                dataBinding.refreshLayout.setVisibility(View.VISIBLE);
                dataBinding.rlEmpty.setVisibility(View.GONE);
                list = data.getItems();
                mTotalSize = data.getTotal();
                // 搜索之后的显示
                adapter.setData(list);
                adapter.notifyDataSetChanged();

                dataBinding.refreshLayout.finishRefresh(true);
            } else {
                // 显示空界面
                dataBinding.refreshLayout.setVisibility(View.GONE);
                dataBinding.rlEmpty.setVisibility(View.VISIBLE);
            }
        } else if (data != null && mPageNum > 1){
            dataBinding.refreshLayout.setVisibility(View.VISIBLE);
            dataBinding.rlEmpty.setVisibility(View.GONE);
            list = data.getItems();
            // 搜索之后的显示
            adapter.addDatas(list);
            adapter.notifyDataSetChanged();

            dataBinding.refreshLayout.finishLoadMore(true);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case cn.xmzt.www.R.id.title_back_iv:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 进入到景区导览地图界面的地址
     * @param data
     * @param tag
     */
    public void OnClickListener(WisdomGuideInfo.ItemsBean data, int tag){
        if ( ContextCompat.checkSelfPermission(ScenicSpotSearchResultActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            this.getApplicationContext().startService(new Intent(this, LocationService.class)); // 开始定位
            IntentManager.getInstance().goScenicSpotMapActivity(this,data.getId(),null);
        } else {
            ToastUtils.showText(ScenicSpotSearchResultActivity.this,"需要读取数据权限才能进入");
        }
    }

}
