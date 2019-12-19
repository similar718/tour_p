package cn.xmzt.www.mine.model;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.dialog.TextTitleDialog;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.mine.activity.AddTouristsActivity;
import cn.xmzt.www.mine.activity.CommonlyTourActivity;
import cn.xmzt.www.mine.adapter.CommonlyTourAdapter;
import cn.xmzt.www.mine.bean.TourBean;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.ToastUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tanlei
 * @date 2019/8/14
 * @describe
 */

public class CommonlyTourViewModel extends BaseViewModel implements CommonlyTourAdapter.OnEditOrDeleteClickListener {
    private CommonlyTourActivity activity;
    /**
     * 1常用出游人 2酒店入住人 3取票人
     */
    private static final int type = 1;
    private List<TourBean> list = new ArrayList<>();
    private CommonlyTourAdapter adapter;
    private TextTitleDialog dialog;

    public void setActivity(CommonlyTourActivity activity) {
        this.activity = activity;
        adapter = new CommonlyTourAdapter(activity, list);
        adapter.setOnEditOrDeleteClickListener(this);
        activity.dataBinding.rvCollection.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        activity.dataBinding.rvCollection.setAdapter(adapter);
//        activity.dataBinding.refreshLayout.autoRefresh();
        getOftenTourer();
        //禁用上啦加载（因为接口未做分页处理）
        activity.dataBinding.refreshLayout.setEnableLoadMore(false);
        activity.dataBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getOftenTourer();
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_iv:
                activity.finish();
                break;
            case R.id.add_often_tourer_tv://添加常用出游人
                EventBus.getDefault().postSticky(new TourBean());
                activity.startActivity(new Intent(activity, AddTouristsActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 获取常用出游人信息
     */
    private void getOftenTourer() {
        ApiRepertory.getInstance().getApiService().getOftenTourer(TourApplication.getToken(), type)
                .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<List<TourBean>>>() {
            @Override
            public void onNext(BaseDataBean<List<TourBean>> userInfoBean) {
                if (null != userInfoBean) {
                    if (userInfoBean.isSuccess()) {
                        activity.dataBinding.refreshLayout.finishRefresh(true);
                        list.clear();
                        list.addAll(userInfoBean.getRel());
                        adapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.showText(activity, userInfoBean.getReMsg());
                    }
                }
            }
        });
    }

    /**
     * 删除常用出游人信息
     */
    private void deleteOftenTourer(int id, int position) {
        ApiRepertory.getInstance().getApiService().deleteOftenTourer(TourApplication.getToken(), id)
                .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>() {
            @Override
            public void onNext(BaseDataBean<Object> userInfoBean) {
                if (null != userInfoBean) {
                    if (userInfoBean.isSuccess()) {
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.showText(activity, userInfoBean.getReMsg());
                    }
                }
            }
        });
    }

    @Override
    public void onEditOrDeleteClick(int position, int type) {
        if (type == 0) {//编辑
            EventBus.getDefault().postSticky(list.get(position));
            activity.startActivity(new Intent(activity, AddTouristsActivity.class));
        } else {//删除
            dialog = new TextTitleDialog(activity, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    deleteOftenTourer(list.get(position).getId(), position);
                }
            });
            dialog.setTitle("确定删除该常用出游人？");
            dialog.show();
        }
    }
}
