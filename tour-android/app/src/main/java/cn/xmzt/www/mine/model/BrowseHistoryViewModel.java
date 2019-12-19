package cn.xmzt.www.mine.model;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.mine.activity.BrowseHistoryActivity;
import cn.xmzt.www.mine.adapter.BrowseHistoryAdapter;
import cn.xmzt.www.mine.bean.BrowseHistoryBean;
import cn.xmzt.www.route.activity.RouteDetailActivity1;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.ToastUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * @author tanlei
 * @date 2019/8/14
 * @describe
 */

public class BrowseHistoryViewModel extends BaseViewModel implements BrowseHistoryAdapter.OnItemClickListener, BrowseHistoryAdapter.OnCollectionCheckListener {
    private BrowseHistoryActivity activity;
    private BrowseHistoryAdapter adapter;
    /**
     * 当前状态是否为可编辑状态
     */
    public ObservableBoolean historyEdit = new ObservableBoolean();
    /**
     * 历史记录类型 0全部 1酒店 2线路 3门票
     */
    public ObservableInt type = new ObservableInt(0);
    /**
     * 优惠券的页码
     */
    private int pageNum = 1;
    /**
     * 一页的数据个数
     */
    private int pageSize = 20;
    private List<BrowseHistoryBean> list = new ArrayList<>();

    public void setActivity(BrowseHistoryActivity activity) {
        this.activity = activity;
        adapter = new BrowseHistoryAdapter(activity, list);
        adapter.setCheck(historyEdit.get());
        adapter.setOnItemClickListener(this);
        adapter.setOnCollectionCheckListener(this);
        activity.dataBinding.rvCollection.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        activity.dataBinding.rvCollection.setAdapter(adapter);
//        activity.dataBinding.refreshLayout.autoRefresh();
        getHistoryRecord();
        activity.dataBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                getHistoryRecord();
            }
        });
        activity.dataBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                getHistoryRecord();
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_iv:
                activity.finish();
                break;
            case R.id.ll_all://全选按钮
                if (isAllCheck()) {
                    cleanOrAllCheck(false);
                    activity.dataBinding.ivAllCheck.setImageResource(R.drawable.scenic_spot_map_item_route_line_right_icon_un);
                } else {
                    cleanOrAllCheck(true);
                    activity.dataBinding.ivAllCheck.setImageResource(R.drawable.scenic_spot_map_item_route_line_right_icon);
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.operation_tv://编辑或者完成
                historyEdit.set(!historyEdit.get());
                adapter.setCheck(historyEdit.get());
                if (!historyEdit.get()) {
                    cleanOrAllCheck(false);
                }
                break;
            case R.id.tv_cancel_collection://删除记录
                StringBuilder sb = new StringBuilder();
                for (BrowseHistoryBean browseHistoryBean : list) {
                    if (browseHistoryBean.isCheck()) {
                        sb.append(browseHistoryBean.getId()).append(",");
                    }
                }
                sb.substring(0, sb.toString().length() - 1);
                Log.e("lee", sb.substring(0, sb.toString().length() - 1));
                deleteHistoryInfo(sb.substring(0, sb.toString().length() - 1));
                break;
            case R.id.all_rl:
                type.set(0);
                pageNum = 1;
                setTextType();
                break;
            case R.id.route_rl:
                type.set(2);
                pageNum = 1;
                setTextType();
                break;
            case R.id.ticket_rl:
                type.set(3);
                pageNum = 1;
                setTextType();
                break;
            case R.id.hotel_rl:
                type.set(1);
                pageNum = 1;
                setTextType();
                break;
        }
    }

    /**
     * 设置选中的文本框加粗
     */
    private void setTextType() {
        activity.dataBinding.allTv.setTypeface(Typeface.MONOSPACE);
        activity.dataBinding.hotelTv.setTypeface(Typeface.MONOSPACE);
        activity.dataBinding.routeTv.setTypeface(Typeface.MONOSPACE);
        activity.dataBinding.ticketTv.setTypeface(Typeface.MONOSPACE);

        activity.dataBinding.allTv.setTextColor(Color.parseColor("#666666"));
        activity.dataBinding.hotelTv.setTextColor(Color.parseColor("#666666"));
        activity.dataBinding.routeTv.setTextColor(Color.parseColor("#666666"));
        activity.dataBinding.ticketTv.setTextColor(Color.parseColor("#666666"));
        switch (type.get()) {
            case 0:
                activity.dataBinding.allTv.setTypeface(Typeface.DEFAULT_BOLD);
                activity.dataBinding.allTv.setTextColor(Color.parseColor("#333333"));
                break;
            case 1:
                activity.dataBinding.hotelTv.setTypeface(Typeface.DEFAULT_BOLD);
                activity.dataBinding.hotelTv.setTextColor(Color.parseColor("#333333"));
                break;
            case 2:
                activity.dataBinding.routeTv.setTypeface(Typeface.DEFAULT_BOLD);
                activity.dataBinding.routeTv.setTextColor(Color.parseColor("#333333"));
                break;
            case 3:
                activity.dataBinding.ticketTv.setTypeface(Typeface.DEFAULT_BOLD);
                activity.dataBinding.ticketTv.setTextColor(Color.parseColor("#333333"));
                break;
        }
        getHistoryRecord();
//        activity.dataBinding.refreshLayout.autoRefresh();
    }

    /**
     * 取消收藏信息
     */
    private void deleteCollectionInfo(String ids, int position) {
        ApiRepertory.getInstance().getApiService().cancelCollection(TourApplication.getToken(), ids)
                .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>() {
            @Override
            public void onNext(BaseDataBean<Object> userInfoBean) {
                if (null != userInfoBean) {
                    if (userInfoBean.isSuccess()) {
                        list.get(position).setCollectionId(0);
                        adapter.notifyDataSetChanged();
                        ToastUtils.showText(activity,"取消收藏成功");
                    } else {
                        ToastUtils.showText(activity, userInfoBean.getReMsg());
                    }
                }
            }
        });
    }

    /**
     * 添加收藏信息
     *
     * @param dataid
     */
    private void addCollectionInfo(int dataid, int position) {
        ApiRepertory.getInstance().getApiService().addCollectionInfo(TourApplication.getToken(), dataid, list.get(position).getType())
                .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<Long>>() {
            @Override
            public void onNext(BaseDataBean<Long> userInfoBean) {
                if (null != userInfoBean && null != userInfoBean.getRel()) {
                    if (userInfoBean.isSuccess()) {
                        ToastUtils.showText(activity,"收藏成功");
                        if (userInfoBean.getRel() instanceof Long) {
                            list.get(position).setCollectionId(userInfoBean.getRel());
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        ToastUtils.showText(activity, userInfoBean.getReMsg());
                    }
                }
            }
        });
    }

    /**
     * 获取浏览历史记录
     */
    private void getHistoryRecord() {
        ApiRepertory.getInstance().getApiService().getHistoryRecord(TourApplication.getToken(), type.get(), pageNum, pageSize)
                .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<List<BrowseHistoryBean>>>() {
            @Override
            public void onNext(BaseDataBean<List<BrowseHistoryBean>> userInfoBean) {
                if (null != userInfoBean && null != userInfoBean.getRel()) {
                    if (userInfoBean.isSuccess()) {
                        activity.dataBinding.refreshLayout.finishRefresh(true);
                        activity.dataBinding.refreshLayout.finishLoadMore(true);
                        if (userInfoBean.getRel().size() > 0) {
                            if (pageNum == 1) {
                                list.clear();
                            }
                            activity.dataBinding.tvNoData.setVisibility(View.GONE);
                            activity.dataBinding.rvCollection.setVisibility(View.VISIBLE);
                            list.addAll(userInfoBean.getRel());
                            adapter.notifyDataSetChanged();
                        } else {
                            if (pageNum == 1) {
                                activity.dataBinding.tvNoData.setVisibility(View.VISIBLE);
                                activity.dataBinding.rvCollection.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        ToastUtils.showText(activity, userInfoBean.getReMsg());
                    }
                }
            }
        });
    }

    /**
     * 删除历史记录
     */
    public void deleteHistoryInfo(String ids) {
        ApiRepertory.getInstance().getApiService().deleteHistory(TourApplication.getToken(), ids)
                .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>() {
            @Override
            public void onNext(BaseDataBean<Object> userInfoBean) {
                if (null != userInfoBean) {
                    if (userInfoBean.isSuccess()) {
                        pageNum = 1;
                        ToastUtils.showText(activity,"历史记录删除成功");
                        getHistoryRecord();
//                        activity.dataBinding.refreshLayout.autoRefresh();
                    } else {
                        ToastUtils.showText(activity, userInfoBean.getReMsg());
                    }
                }
            }
        });
    }

    /**
     * 判断是否全选了
     *
     * @return
     */
    private boolean isAllCheck() {
        for (BrowseHistoryBean browseHistoryBean : list) {
            if (!browseHistoryBean.isCheck()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 清空或全选选中
     */
    private void cleanOrAllCheck(boolean isCheck) {
        for (BrowseHistoryBean browseHistoryBean : list) {
            browseHistoryBean.setCheck(isCheck);
        }
    }

    @Override
    public void onItemClick(int position) {
        if (historyEdit.get()) {
            list.get(position).setCheck(!list.get(position).isCheck());
            adapter.notifyDataSetChanged();
            if (isAllCheck()) {
                activity.dataBinding.ivAllCheck.setImageResource(R.drawable.scenic_spot_map_item_route_line_right_icon);
            } else {
                activity.dataBinding.ivAllCheck.setImageResource(R.drawable.scenic_spot_map_item_route_line_right_icon_un);
            }
        } else {
            Intent mIntent = new Intent(activity, RouteDetailActivity1.class);
            mIntent.putExtra("A",list.get(position).getDataid());
            mIntent.putExtra("B",list.get(position).getName());
            activity.startActivity(mIntent);
        }
    }

    //收藏或取消收藏
    @Override
    public void onCollectionCheckClick(int position) {
        if (list.get(position).getCollectionId() == 0) {//添加收藏
            addCollectionInfo(list.get(position).getDataid(), position);
        } else {//取消收藏
            deleteCollectionInfo(list.get(position).getCollectionId() + "", position);
        }
    }
}
