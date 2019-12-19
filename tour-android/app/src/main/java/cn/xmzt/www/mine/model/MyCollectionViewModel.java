package cn.xmzt.www.mine.model;

import android.content.Intent;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.mine.activity.MyCollectionActivity;
import cn.xmzt.www.mine.adapter.MyCollectionAdapter;
import cn.xmzt.www.mine.bean.CollectionBean;
import cn.xmzt.www.route.activity.RouteDetailActivity1;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.ToastUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tanlei
 * @date 2019/8/13
 * @describe
 */

public class MyCollectionViewModel extends BaseViewModel implements MyCollectionAdapter.OnItemClickListener {
    private MyCollectionActivity activity;
    private List<CollectionBean> list = new ArrayList<>();
    private MyCollectionAdapter adapter;
    /**
     * 是否在编辑状态的标志
     */
    private boolean isEdit;
    /**
     * 收藏的类型 0全部 1酒店 2线路 3门票
     */
    private int type;
    /**
     * 优惠券的页码
     */
    private int pageNum = 1;
    /**
     * 一页的数据个数
     */
    private int pageSize = 20;

    public void setActivity(MyCollectionActivity activity) {
        this.activity = activity;
        adapter = new MyCollectionAdapter(activity, list);
        adapter.setCheck(isEdit);
        adapter.setOnItemClickListener(this);
        activity.dataBinding.rvCollection.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        activity.dataBinding.rvCollection.setAdapter(adapter);
//        activity.dataBinding.refreshLayout.autoRefresh();
        getCollectionInfo();
        activity.dataBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                getCollectionInfo();
            }
        });
        activity.dataBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                getCollectionInfo();
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
            case R.id.operation_tv://编辑或者收藏
                isEdit = !isEdit;
                adapter.setCheck(isEdit);
                if (isEdit) {
                    activity.dataBinding.titleNameTv.setText("编辑收藏");
                    activity.dataBinding.operationTv.setText("完成");
                    activity.dataBinding.llBottom.setVisibility(View.VISIBLE);
                } else {
                    cleanOrAllCheck(false);
                    activity.dataBinding.titleNameTv.setText("我的收藏");
                    activity.dataBinding.operationTv.setText("编辑");
                    activity.dataBinding.llBottom.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_cancel_collection://取消收藏
                StringBuilder sb = new StringBuilder();
                for (CollectionBean collectionBean : list) {
                    if (collectionBean.isCheck()) {
                        sb.append(collectionBean.getId()).append(",");
                    }
                }
                sb.substring(0, sb.toString().length() - 1);
                Log.e("lee", sb.substring(0, sb.toString().length() - 1));
                deleteCollectionInfo(sb.substring(0, sb.toString().length() - 1));
                break;
            case R.id.all_rl:
                type = 0;
                setCheckButton(0);
                break;
            case R.id.route_rl:
                type = 2;
                setCheckButton(1);
                break;
            case R.id.ticket_rl:
                type = 3;
                setCheckButton(2);
                break;
            case R.id.hotel_rl:
                type = 1;
                setCheckButton(3);
                break;
        }
    }

    private void setCheckButton(int position) {
        activity.dataBinding.allTv.setTypeface(Typeface.MONOSPACE);
        activity.dataBinding.allV.setVisibility(View.GONE);
        activity.dataBinding.routeTv.setTypeface(Typeface.MONOSPACE);
        activity.dataBinding.routeV.setVisibility(View.GONE);
        activity.dataBinding.ticketTv.setTypeface(Typeface.MONOSPACE);
        activity.dataBinding.ticketV.setVisibility(View.GONE);
        activity.dataBinding.hotelTv.setTypeface(Typeface.MONOSPACE);
        activity.dataBinding.hotelV.setVisibility(View.GONE);
        switch (position) {
            case 0:
                activity.dataBinding.allTv.setTypeface(Typeface.DEFAULT_BOLD);
                activity.dataBinding.allV.setVisibility(View.VISIBLE);
                break;
            case 1:
                activity.dataBinding.routeTv.setTypeface(Typeface.DEFAULT_BOLD);
                activity.dataBinding.routeV.setVisibility(View.VISIBLE);
                break;
            case 2:
                activity.dataBinding.ticketTv.setTypeface(Typeface.DEFAULT_BOLD);
                activity.dataBinding.ticketV.setVisibility(View.VISIBLE);
                break;
            case 3:
                activity.dataBinding.hotelTv.setTypeface(Typeface.DEFAULT_BOLD);
                activity.dataBinding.hotelV.setVisibility(View.VISIBLE);
                break;
        }
        pageNum = 1;
        getCollectionInfo();
//        activity.dataBinding.refreshLayout.autoRefresh();
    }

    /**
     * 删除收藏信息
     */
    public void deleteCollectionInfo(String ids) {
        ApiRepertory.getInstance().getApiService().cancelCollection(TourApplication.getToken(), ids)
                .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>() {
            @Override
            public void onNext(BaseDataBean<Object> userInfoBean) {
                if (null != userInfoBean) {
                    if (userInfoBean.isSuccess()) {
                        ToastUtils.showText(activity,"取消收藏成功");
                        pageNum = 1;
                        getCollectionInfo();
//                        activity.dataBinding.refreshLayout.autoRefresh();
                    } else {
                        ToastUtils.showText(activity, userInfoBean.getReMsg());
                    }
                }
            }
        });
    }

    /**
     * 获取收藏信息
     */
    public void getCollectionInfo() {
        ApiRepertory.getInstance().getApiService().getCollectionInfo(TourApplication.getToken(), type, pageNum, pageSize)
                .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<List<CollectionBean>>>() {
            @Override
            public void onNext(BaseDataBean<List<CollectionBean>> userInfoBean) {
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
     * 判断是否全选了
     *
     * @return
     */
    private boolean isAllCheck() {
        for (CollectionBean collectionBean : list) {
            if (!collectionBean.isCheck()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 清空或全选选中
     */
    private void cleanOrAllCheck(boolean isCheck) {
        for (CollectionBean collectionBean : list) {
            collectionBean.setCheck(isCheck);
        }
    }

    @Override
    public void onItemClick(int position) {
        if (isEdit) {
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
}
