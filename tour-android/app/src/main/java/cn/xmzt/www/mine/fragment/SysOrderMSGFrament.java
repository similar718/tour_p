package cn.xmzt.www.mine.fragment;


import androidx.lifecycle.Observer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseFragment;
import cn.xmzt.www.databinding.FragmentSysMsgBinding;
import cn.xmzt.www.mine.adapter.MessageCenterOrderAdapter;
import cn.xmzt.www.mine.bean.MessageBean;
import cn.xmzt.www.mine.model.SysMsgViewModel;
import cn.xmzt.www.view.listener.ItemListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

/**
 * 系统订单消息
 */
public class SysOrderMSGFrament extends BaseFragment<SysMsgViewModel, FragmentSysMsgBinding> implements ItemListener {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sys_msg;
    }

    @Override
    protected SysMsgViewModel createViewModel() {
        viewModel = new SysMsgViewModel();
        viewModel.setType("2");
        viewModel.mMessageList.observe(this, new Observer<List<MessageBean>>() {
            @Override
            public void onChanged(@Nullable List<MessageBean> list) {
                //隐藏对话框
                if (isRefresh){

                    dataBinding.refreshLayout.finishRefresh();
                    if(list.size()>0){
                        pageNum=1;
                        adapter.setDatas(list);
                    }
                }else {
                    dataBinding.refreshLayout.finishLoadMore();
                    if(list.size()>0){
                        pageNum=pageNum+1;
                        adapter.addDatas(list);
                    }else {
                        isNextPage=false;
                        dataBinding.refreshLayout.setNoMoreData(!isNextPage);
                    }
                }
                if (adapter.getItemCount()>0) {
                    dataBinding.tvNoData.setVisibility(View.GONE);
                    dataBinding.recyclerView.setVisibility(View.VISIBLE);
                } else {
                    dataBinding.tvNoData.setVisibility(View.VISIBLE);
                    dataBinding.recyclerView.setVisibility(View.GONE);
                }
            }
        });
        return viewModel;
    }
    private MessageCenterOrderAdapter adapter;
    private int pageNum=1;
    boolean isRefresh = true;
    boolean isNextPage = true;
    @Override
    protected void initData() {
        GridLayoutManager manager = (GridLayoutManager) dataBinding.recyclerView.getLayoutManager();
        manager.setSpanCount(1);

        adapter = new MessageCenterOrderAdapter();
        dataBinding.recyclerView.setAdapter(adapter);
        dataBinding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if(isNextPage){
                    isRefresh=false;
                    viewModel.getSysMsgList(pageNum+1);
                }else {
                    refreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh=true;
                viewModel.getSysMsgList(1);
            }
        });
        viewModel.getSysMsgList(1);
    }
    @Override
    public void onItemClick(View view, int position) {
       /* RoutePage.ItemsBean item=adapter.getItem(position);
        Intent mIntent = new Intent(getContext(), RouteDetailActivity1.class);
        mIntent.putExtra("A",item.getId());
        mIntent.putExtra("B",item.getLineName());
        startActivity(mIntent);*/

    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }
    @Override
    public void showLoadFail(String msg) {
        super.showLoadFail(msg);
    }
}
