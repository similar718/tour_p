package cn.xmzt.www.mine.activity;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityCommonVehiclesBinding;
import cn.xmzt.www.manager.IntentManager;
import cn.xmzt.www.mine.adapter.CommonVehiclesAdapter;
import cn.xmzt.www.mine.adapter.CommonVehiclesEditAdapter;
import cn.xmzt.www.mine.bean.CommonVehicleBean;
import cn.xmzt.www.mine.event.UpdateVehicleEvent;
import cn.xmzt.www.mine.model.CommonVehiclesViewModel;
import cn.xmzt.www.utils.StatusBarUtil;
import cn.xmzt.www.utils.ToastUtils;

public class CommonVehiclesActivity extends TourBaseActivity<CommonVehiclesViewModel, ActivityCommonVehiclesBinding> {

    private boolean mIsEdit = false;
    private boolean mIsMore = false;

    private int mPageNum = 1;
    private int mPageSize = 100;
    private int mTotalSize = 100;
    private String mToken;
    private Context mContext;
    private ArrayList<String> mList = new ArrayList<>();
    private String oldLicencePlate = "";

    @Override
    protected int setLayoutId() {
        return R.layout.activity_common_vehicles;
    }

    @Override
    protected CommonVehiclesViewModel createViewModel() {
        viewModel = new CommonVehiclesViewModel();
        viewModel.setIView(this);
        viewModel.mVehicleInfoList.observe(this, new Observer<List<CommonVehicleBean>>() {
            @Override
            public void onChanged(List<CommonVehicleBean> list) {
                setDatas(list);
            }
        });
        return viewModel;
    }
    @Override
    protected void initData() {
        // 设置顶部状态栏
        StatusBarUtil.setStatusBarLightMode(this,true);
        EventBus.getDefault().register(this);
        dataBinding.setModel(viewModel);
        dataBinding.setActivity(this);
        mContext = this;
        mToken = TourApplication.getToken();
        mIsEdit = getIntent().getBooleanExtra("edit",false);
        mIsMore = getIntent().getBooleanExtra("more",false);
        mList = getIntent().getStringArrayListExtra("list");
        if (mIsEdit) {
            if (!mIsMore) {
                if (mList.size() > 0) {
                    oldLicencePlate = mList.get(0);
                }
            }
        }
        getDataList();

        if (mIsEdit){
            // 编辑选择
            dataBinding.topTitleTv.setText("选择车辆");

            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
            dataBinding.rvList.setLayoutManager(layoutManager);
            mEditAdapter = new CommonVehiclesEditAdapter(this);
            dataBinding.rvList.setAdapter(mEditAdapter);
            mEditAdapter.setDatas(mListInfo);
        } else {
            // 正常显示界面
            dataBinding.topTitleTv.setText("常用车辆");

            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
            dataBinding.rvList.setLayoutManager(layoutManager);
            mAdapter = new CommonVehiclesAdapter(this);
            dataBinding.rvList.setAdapter(mAdapter);
            mAdapter.setDatas(mListInfo);
        }
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
                if (mTotalSize >= (mPageNum * mPageSize)) { // 如果当前显示数据小于总数据 表示还有下一页
                    mPageNum += 1;
                    getDataList();
                } else {
                    ToastUtils.showText(mContext,"我也是有底线的");
                    // 在不请求接口的情况下 将它finish掉 否则会一直加载
                    dataBinding.refreshLayout.finishLoadMore(true);
                }
            }
        });
    }

    private void getDataList(){
        viewModel.getsysUserPlateNumberList(mToken,mPageNum,mPageSize,CommonVehiclesActivity.this);
    }

    private CommonVehiclesAdapter mAdapter;
    private CommonVehiclesEditAdapter mEditAdapter;
    private List<CommonVehicleBean> mListInfo = new ArrayList<>();
    private List<CommonVehicleBean> mListSelectInfo = new ArrayList<>();

    public void onClick(View view) {
        switch (view.getId()) {
            case cn.xmzt.www.R.id.back_iv:// 返回
                finish();
                break;
            case cn.xmzt.www.R.id.rl_add_car:// 添加车辆
                IntentManager.getInstance().goMineAddCarActivity(this,"",false,-1);
                break;
            case cn.xmzt.www.R.id.tv_edit_sure:// 选择车辆确定
                if (!mIsMore) {
                    EventBus.getDefault().post(new UpdateVehicleEvent(2, mListSelectInfo)); // 添加到智能组队界面
                } else {
                    EventBus.getDefault().post(new UpdateVehicleEvent(3, mListSelectInfo)); // 添加到订单填写界面
                }
                onBackPressed();
                break;
            default:
                break;
        }
    }

    private int mItemPosition = -1;

    public void OnListClickListener(int position,int type){
        if (type == 0) {
            IntentManager.getInstance().goMineAddCarActivity(this, mListInfo.get(position).getPlateNumber(), mListInfo.get(position).getIsDefault()==1 ? true : false,mListInfo.get(position).getId());
        } else {
            boolean select = mEditAdapter.getItem(position).isSelect();
            if (select){ // 更改为不选
                if (mListSelectInfo.size() > 0){
                    for (int i = 0 ; i < mListSelectInfo.size(); i++){
                        if (mListSelectInfo.get(i).getPlateNumber().equals(mListInfo.get(position).getPlateNumber())){
                            mListSelectInfo.remove(i);
                            mList.remove(mListInfo.get(position).getPlateNumber());
                            break;
                        }
                    }
                }
            } else { // 更改为选择
                if (mIsMore){ // 多个选择
                    mListSelectInfo.add(mListInfo.get(position));
                    mList.add(mListInfo.get(position).getPlateNumber());
                } else { // 只有一个选择
                    if (mListSelectInfo.size() > 0){
                        mListSelectInfo.clear();
                        mListSelectInfo.add(mListInfo.get(position));
                        mList.remove(mListInfo.get(mItemPosition).getPlateNumber());
                        mList.add(mListInfo.get(position).getPlateNumber());
                        // 更新界面
                        mEditAdapter.getItem(mItemPosition).setSelect(false);
                        mEditAdapter.notifyItemChanged(mItemPosition);
                        mItemPosition = position;
                    } else {
                        mListSelectInfo.add(mListInfo.get(position));
                        mList.add(mListInfo.get(position).getPlateNumber());
                        mItemPosition = position;
                    }
                }
            }
            mEditAdapter.getItem(position).setSelect(!mEditAdapter.getItem(position).isSelect());
            mEditAdapter.notifyItemChanged(position);
            if (mListSelectInfo.size() > 0){
                if (!mIsMore){ // 表示只选中一个就可以
                    if (!oldLicencePlate.equals(mListSelectInfo.get(0).getPlateNumber())){
                        dataBinding.tvEditSure.setVisibility(View.VISIBLE);
                    } else {
                        dataBinding.tvEditSure.setVisibility(View.GONE);
                    }
                } else {
                    dataBinding.tvEditSure.setVisibility(View.VISIBLE);
                }
            } else {
                dataBinding.tvEditSure.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getOrderType(UpdateVehicleEvent event) {
        if (event.getType() == 1){
            // 刷新数据
            mPageNum = 1;
            getDataList();
        } else if (event.getType() == 4){ // 删除车辆信息
            // 删除车辆信息的处理逻辑
            if (mIsEdit){ // 选择的模式
                // 删除已经选中的删除车辆信息
                mList.remove(event.getOldLicencePlate());
            }
            // 刷新数据
            mPageNum = 1;
            getDataList();
        } else if (event.getType() == 5){ // 修改车辆信息更改 需要的数据旧的车辆信息 如果也要默认添加 就还需要新的车辆信息
            if (mIsEdit){ // 选择的模式
                if (!TextUtils.isEmpty(event.getNewLicencePlate())){ // 有更改新的
                    mList.remove(event.getOldLicencePlate()); // 移除老的消息
                    mList.add(event.getNewLicencePlate()); // 添加更新之后的
                } else { // 没有更改新的车辆
                }
            }
            mPageNum = 1;
            getDataList();
        }
    }

    private void setDatas(List<CommonVehicleBean> list){
        if (mIsEdit) {
            if (mPageNum == 1){
                mListSelectInfo.clear();
            }
            if (mList != null && mList.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    for (int j = 0; j < mList.size(); j++) {
                        if (list.get(i).getPlateNumber().equals(mList.get(j))) {
                            list.get(i).setSelect(true);
                            mListSelectInfo.add(list.get(i));
                            if (!mIsMore) {
                                mItemPosition = i;
                            }
                        }
                    }
                }
            }
        }
        if (mPageNum == 1) {
            mListInfo = list;
            if (mIsEdit) {
                mEditAdapter.setDatas(mListInfo);
            } else {
                mAdapter.setDatas(mListInfo);
            }
        } else {
            mTotalSize = mTotalSize + list.size();
            if (mIsEdit) {
                mEditAdapter.addDatas(list);
            } else {
                mAdapter.addDatas(list);
            }
            mListInfo.addAll(list);
        }
        if (mIsEdit) {
            if (mListSelectInfo.size() > 0) {
                if (!mIsMore) { // 表示只选中一个就可以
                    if (!oldLicencePlate.equals(mListSelectInfo.get(0).getPlateNumber())) {
                        dataBinding.tvEditSure.setVisibility(View.VISIBLE);
                    } else {
                        dataBinding.tvEditSure.setVisibility(View.GONE);
                    }
                } else {
                    dataBinding.tvEditSure.setVisibility(View.VISIBLE);
                }
            } else {
                dataBinding.tvEditSure.setVisibility(View.GONE);
            }
        }
    }
}
