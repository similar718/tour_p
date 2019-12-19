package cn.xmzt.www.smartteam.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityTripSignInListBinding;
import cn.xmzt.www.smartteam.adapter.TripSignInListAdapter;
import cn.xmzt.www.smartteam.bean.TripSignInListBean;
import cn.xmzt.www.smartteam.vm.TripSignInListViewModel;
import cn.xmzt.www.utils.StatusBarUtil;
import cn.xmzt.www.utils.ToastUtils;
import retrofit2.http.GET;

public class TripSignInListActivity extends TourBaseActivity<TripSignInListViewModel, ActivityTripSignInListBinding> {

    private Context mContext;
    private String mToken = "";
    private String mDesId = ""; // 查询目的地-集合时间接口返回的id
    private boolean mIsActivity = true; // 是否在当前界面

    private TripSignInListBean mInfo = null;
    private List<TripSignInListBean.GroupMemberVOSBean> mAllSignInInfo = new ArrayList<>();
    private List<TripSignInListBean.GroupMemberVOSBean> mAlreadySignInInfo = new ArrayList<>();
    private List<TripSignInListBean.GroupMemberVOSBean> mNotSignInInfo = new ArrayList<>();

    @Override
    protected int setLayoutId() {
        return R.layout.activity_trip_sign_in_list;
    }

    @Override
    protected TripSignInListViewModel createViewModel() {
        mContext = this;
        viewModel = new TripSignInListViewModel();
        viewModel.setIView(this);
        viewModel.mSignIn.observe(this, new Observer<TripSignInListBean>() {
            @Override
            public void onChanged(TripSignInListBean tripSignInListBean) {
                mInfo = tripSignInListBean;
                setDatas();
            }
        });
        return viewModel;
    }

    @Override
    protected void initData() {
        mIsActivity = true;
        dataBinding.setViewModel(viewModel);
        dataBinding.setActivity(this);
        StatusBarUtil.setStatusBarLightMode(this,true);

        RelativeLayout.LayoutParams listParams = (RelativeLayout.LayoutParams) dataBinding.rlTopC.getLayoutParams();
        listParams.topMargin = StatusBarUtil.getStatusBarHeight(getApplicationContext());

        mDesId = getIntent().getStringExtra("desid");
        mToken = TourApplication.getToken();
        if (TextUtils.isEmpty(mToken)){
            ToastUtils.showText(mContext,"token已失效");
            return;
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        dataBinding.rvList.setLayoutManager(layoutManager);
        mAdapter = new TripSignInListAdapter(this);
        dataBinding.rvList.setAdapter(mAdapter);

        mHandler.sendEmptyMessage(HANDLER_GET_INFO);
    }

    private TripSignInListAdapter mAdapter = null;

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_iv : // 返回
                onBackPressed();
                break;
            case R.id.rl_all_sign_in : // 应签到
                if (mAllSignInInfo != null && mAllSignInInfo.size() > 0) {
                    mAdapter.setDatas(mAllSignInInfo);
                    dataBinding.rvList.setVisibility(View.VISIBLE);
                    dataBinding.tvEmpty.setVisibility(View.GONE);
                } else {
                    dataBinding.rvList.setVisibility(View.GONE);
                    dataBinding.tvEmpty.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rl_already_sign_in : // 已签到
                if (mAlreadySignInInfo != null && mAlreadySignInInfo.size() > 0) {
                    mAdapter.setDatas(mAlreadySignInInfo);
                    dataBinding.rvList.setVisibility(View.VISIBLE);
                    dataBinding.tvEmpty.setVisibility(View.GONE);
                } else {
                    dataBinding.rvList.setVisibility(View.GONE);
                    dataBinding.tvEmpty.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rl_not_sign_in : // 未签到
                if (mNotSignInInfo != null && mNotSignInInfo.size() > 0) {
                    mAdapter.setDatas(mNotSignInInfo);
                    dataBinding.rvList.setVisibility(View.VISIBLE);
                    dataBinding.tvEmpty.setVisibility(View.GONE);
                } else {
                    dataBinding.rvList.setVisibility(View.GONE);
                    dataBinding.tvEmpty.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    private void setDatas(){
        if (mInfo != null){
            // 应该签到人数
            dataBinding.tvShouldSignNum.setText(mInfo.getAnswerSigninNum()+"");
            // 已签到人数
            dataBinding.tvSignNum.setText(mInfo.getAlreadySigninNum()+"");
            // 未签到人数
            dataBinding.tvNoSignNum.setText(mInfo.getNotSigninNum()+"");

            if (mInfo.getGroupMemberVOS() != null && mInfo.getGroupMemberVOS().size() > 0){
                mAllSignInInfo = mInfo.getGroupMemberVOS();
                if (mAllSignInInfo != null && mAllSignInInfo.size() > 0) {
                    mAdapter.setDatas(mAllSignInInfo);
                    dataBinding.rvList.setVisibility(View.VISIBLE);
                    dataBinding.tvEmpty.setVisibility(View.GONE);
                } else {
                    dataBinding.rvList.setVisibility(View.GONE);
                    dataBinding.tvEmpty.setVisibility(View.VISIBLE);
                }

                mAlreadySignInInfo.clear();
                mNotSignInInfo.clear();

                for (int i = 0; i < mAllSignInInfo.size(); i++){
                    if (mAllSignInInfo.get(i).getStatus() == 2 || mAllSignInInfo.get(i).getStatus() == 3){ // 2 准时到达 3 迟到
                        mAlreadySignInInfo.add(mAllSignInInfo.get(i));
                    } else if (mAllSignInInfo.get(i).getStatus() == 1 || mAllSignInInfo.get(i).getStatus() == 4) { // 1 待签到 4 签到失败
                        mNotSignInInfo.add(mAllSignInInfo.get(i));
                    }
                }
            } else {
                dataBinding.rvList.setVisibility(View.GONE);
                dataBinding.tvEmpty.setVisibility(View.VISIBLE);
            }
        }
    }

    public void OnListClickListener(String tel,int position){
        if (!TextUtils.isEmpty(tel)) { // 电话号码不为空的情况下打电话
            callPhone(tel);
        }
    }
    /**
     * 拨打电话（直接拨打电话）
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum){
        Intent intent = new Intent(Intent.ACTION_DIAL);//ACTION_DIAL ACTION_CALL TODO
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }


    private final int HANDLER_GET_INFO = 0x0101;
    private final int UPDATE_TIME_MAP_INFO = 15 * 1000; // 15秒刷新一次

    /**
     * update information
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_GET_INFO: // get member info
                    if (mIsActivity) {
                        viewModel.getSigninInfos(mToken,mDesId);
                        mHandler.sendEmptyMessageDelayed(HANDLER_GET_INFO, UPDATE_TIME_MAP_INFO);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mIsActivity = false;
        mHandler.removeMessages(HANDLER_GET_INFO);
    }
}
