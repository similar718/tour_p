package cn.xmzt.www.intercom.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivitySchedulingDetailBinding;
import cn.xmzt.www.intercom.adapter.SchedulingDetailAdapter;
import cn.xmzt.www.intercom.vm.SchedulingDetailViewModel;
import cn.xmzt.www.manager.IntentManager;
import cn.xmzt.www.route.activity.RouteDetailActivity1;
import cn.xmzt.www.selfdrivingtools.bean.TourTripDetailNewBean;
import cn.xmzt.www.share.ShareFunction;
import cn.xmzt.www.view.listener.ItemListener;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

/**
 * 行程详情
 */
public class SchedulingDetailActivity extends TourBaseActivity<SchedulingDetailViewModel, ActivitySchedulingDetailBinding> implements ItemListener {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_scheduling_detail;
    }

    @Override
    protected SchedulingDetailViewModel createViewModel() {
        viewModel = new SchedulingDetailViewModel();
        viewModel.tripDetail.observe(this, new Observer<TourTripDetailNewBean>() {
            @Override
            public void onChanged(@Nullable TourTripDetailNewBean result) {
                SchedulingDetailActivity.this.result=result;
                routeId=""+result.getLineId();
                lineName=result.getLineName();
                groupId = result.getGroupId();
                if(!TextUtils.isEmpty(lineName)){
                    dataBinding.tvTitle.setText(lineName);
                }
                adapter.setData(result);
            }
        });
        return viewModel;
    }
    private int tripId;// 行程id
    private String routeId;// 线路id
    private String groupId;// 群id
    private String lineName;
    private TourTripDetailNewBean result;
    SchedulingDetailAdapter adapter =null;
    GridLayoutManager manager=null;
    @Override
    protected void initData() {
        Intent intent=getIntent();
        tripId=intent.getIntExtra("A",0);
        groupId=intent.getStringExtra("B");
        dataBinding.setActivity(this);
        manager= (GridLayoutManager) dataBinding.recyclerView.getLayoutManager();
        manager.setSpanCount(1);
        adapter = new SchedulingDetailAdapter();
        adapter.groupId=groupId;
        adapter.setItemListener(this);
        dataBinding.recyclerView.setAdapter(adapter);
        viewModel.getTripDetail(tripId,groupId);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_route_detail:{
                //进入线路详情
                Intent mIntent = new Intent(this, RouteDetailActivity1.class);
                mIntent.putExtra("A",result.getLineId());
                mIntent.putExtra("B",result.getLineName());
                startActivity(mIntent);
                break;
            }
            case R.id.tv_group:
                //进入/组建行程群
//                if (GPSUtils.isOPen(this)) {
//                TeamRoomActivity.start(this,groupId,true);
                    IntentManager.getInstance().goSharedNavigationMapActivity(this,groupId);
//                } else {
//                    cn.xmzt.www.utils.ToastUtils.showText(this,"请打开位置权限/GPS位置信息");
//                }
                break;
            case R.id.iv_share:{
                // 分享
                String url="https://g.xmzt.cn/g/g?t=5&i="+groupId+"&p="+ TourApplication.getRefCode();
                ShareFunction.getInstance().showWebWeChatShareAction(this, "我发现了一个不错的自驾游线路，想邀请你同游",result.getPhotoUrl(),
                        result.getViaInfo(),url);

                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.iv_avatar_more:{
                //进入成员列表
                Intent intent=new Intent(this,GroupMemberListActivity.class);
                intent.putExtra("A",groupId);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }
}
