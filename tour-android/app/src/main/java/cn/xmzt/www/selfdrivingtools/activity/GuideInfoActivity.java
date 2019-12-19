package cn.xmzt.www.selfdrivingtools.activity;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityGuideInfoBinding;
import cn.xmzt.www.manager.IntentManager;
import cn.xmzt.www.selfdrivingtools.adapter.MsgGuideInfoAdapter;
import cn.xmzt.www.selfdrivingtools.bean.MsgGuideListInfo;
import cn.xmzt.www.selfdrivingtools.viewmodel.GuideInfoViewModel;
import cn.xmzt.www.utils.SchemeActivityUtil;
import cn.xmzt.www.utils.StatusBarUtil;
import cn.xmzt.www.utils.ToastUtils;

public class GuideInfoActivity extends TourBaseActivity<GuideInfoViewModel, ActivityGuideInfoBinding> {

    @Override
    protected int setLayoutId() {
        return R.layout.activity_guide_info;
    }

    @Override
    protected GuideInfoViewModel createViewModel() {
        viewModel = new GuideInfoViewModel();
        viewModel.setIView(this);
        viewModel.mGuideInfoList.observe(this, new Observer<List<MsgGuideListInfo>>() {
            @Override
            public void onChanged(List<MsgGuideListInfo> msgGuideListInfos) {
                // 获取到数据
                mListInfo = msgGuideListInfos;
                setData();
            }
        });
        return viewModel;
    }

    private int lineId = 0;
    private int tripId = 0;
    private List<MsgGuideListInfo> mListInfo;
    private MsgGuideInfoAdapter mCarAdapter ;
    private Context mContext;

    @Override
    protected void initData() {
        mContext = this;
        // 设置顶部状态栏
        StatusBarUtil.setStatusBarLightMode(this,true);
        dataBinding.setModel(viewModel);
        dataBinding.setActivity(this);

        lineId = getIntent().getIntExtra("lineId",0);
        tripId = getIntent().getIntExtra("tripId",0);
        viewModel.getMsgGuideList(lineId,tripId);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        dataBinding.rvList.setLayoutManager(layoutManager);
        mCarAdapter = new MsgGuideInfoAdapter(this);
        dataBinding.rvList.setAdapter(mCarAdapter);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case cn.xmzt.www.R.id.back_iv:// 返回
                finish();
                break;
            default:
                break;
        }
    }

    private void setData(){
        if (mListInfo != null){
            if (mListInfo.size()>0){
                mCarAdapter.setDatas(mListInfo);
            }
        }
    }

    /**
     * 指南列表的点击事件
     * @param position 列表位置
     * @param type 点击类型
     */
    public void OnListClickListener(int position,int type){
        if (type == 1){ // 1美食指南
            MsgGuideListInfo horseMiMessage = mListInfo.get(position);
            SchemeActivityUtil.pushToActivity(getApplicationContext(),horseMiMessage.getTitle(),""+horseMiMessage.getId(),"7");
        } else if (type == 2){ // 2酒店入住指南
            MsgGuideListInfo horseMiMessage = mListInfo.get(position);
            SchemeActivityUtil.pushToActivity(getApplicationContext(),horseMiMessage.getTitle(),""+horseMiMessage.getId(),"8");
        } else if (type == 3){ // 3检查车辆
            IntentManager.getInstance().goCarCheckInfoActivity(this);
        } else if (type == 4){ // 4 出行清单
            MsgGuideListInfo horseMiMessage = mListInfo.get(position);
            SchemeActivityUtil.pushToActivity(getApplicationContext(),horseMiMessage.getTitle(),""+horseMiMessage.getId(),"6");
        }
    }
}
