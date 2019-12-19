package cn.xmzt.www.selfdrivingtools.activity;

import android.text.TextUtils;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityCarCheckInfoBinding;
import cn.xmzt.www.dialog.CarCheckDetailDialog;
import cn.xmzt.www.selfdrivingtools.adapter.CarCheckInfoAdapter;
import cn.xmzt.www.selfdrivingtools.bean.MsgCarListInfo;
import cn.xmzt.www.selfdrivingtools.viewmodel.CarCheckInfoViewModel;
import cn.xmzt.www.utils.StatusBarUtil;

public class CarCheckInfoActivity extends TourBaseActivity<CarCheckInfoViewModel, ActivityCarCheckInfoBinding> {

    @Override
    protected int setLayoutId() {
        return R.layout.activity_car_check_info;
    }

    @Override
    protected CarCheckInfoViewModel createViewModel() {
        viewModel = new CarCheckInfoViewModel();
        viewModel.setIView(this);
        viewModel.mGuideInfoList.observe(this, new Observer<List<MsgCarListInfo>>() {
            @Override
            public void onChanged(List<MsgCarListInfo> msgGuideListInfos) {
                mCarInfo = msgGuideListInfos;
                mCarAdapter.setDatas(mCarInfo);
            }
        });
        return viewModel;
    }

    private CarCheckInfoAdapter mCarAdapter ;

    @Override
    protected void initData() {
        // 设置顶部状态栏
        StatusBarUtil.setStatusBarLightMode(this,true);
        dataBinding.setModel(viewModel);
        dataBinding.setActivity(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        dataBinding.rvList.setLayoutManager(layoutManager);
        mCarAdapter = new CarCheckInfoAdapter(this);
        dataBinding.rvList.setAdapter(mCarAdapter);

        viewModel.getMsgGuideList();
    }

    private List<MsgCarListInfo> mCarInfo = new ArrayList<>();


    public void onClick(View view) {
        switch (view.getId()) {
            case cn.xmzt.www.R.id.back_iv:// 返回
                finish();
                break;
            default:
                break;
        }
    }

    private int mDialogPosition = -1;

    public void OnListClickListener(int position,String data){
        mDialogPosition = position;
        showDetailDialog(mCarInfo.get(mDialogPosition),mDialogPosition,mCarInfo.size());
    }

    private CarCheckDetailDialog mDialog;
    private void showDetailDialog(MsgCarListInfo info,int position,int allsize){
        if (mDialog == null){
            mDialog = new CarCheckDetailDialog(this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.tv_pre){ // 上一条
                        if (mDialogPosition > 0){
                            mDialogPosition--;
                            mDialog.setViewData(mCarInfo.get(mDialogPosition),mDialogPosition,mCarInfo.size());
                        }
                    } else if (v.getId() == R.id.tv_next){ // 下一条
                        if (mDialogPosition < (mCarInfo.size()-1)){
                            mDialogPosition++;
                            mDialog.setViewData(mCarInfo.get(mDialogPosition),mDialogPosition,mCarInfo.size());
                        }
                    }
                }
            });
        }
        mDialog.setViewData(info,position,allsize);
        mDialog.show();
    }

}
