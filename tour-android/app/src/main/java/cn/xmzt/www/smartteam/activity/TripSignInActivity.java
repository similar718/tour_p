package cn.xmzt.www.smartteam.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.lifecycle.Observer;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ActivityTripSignInBinding;
import cn.xmzt.www.selfdrivingtools.event.TripSignInEvent;
import cn.xmzt.www.selfdrivingtools.overlay.AMapUtil;
import cn.xmzt.www.smartteam.bean.TripSignInDetailBean;
import cn.xmzt.www.smartteam.vm.TripSignInViewModel;
import cn.xmzt.www.utils.IsFastClick;
import cn.xmzt.www.utils.StatusBarUtil;
import cn.xmzt.www.utils.TextViewUtil;
import cn.xmzt.www.utils.TimeUtil;
import cn.xmzt.www.utils.ToastUtils;

public class TripSignInActivity extends TourBaseActivity<TripSignInViewModel, ActivityTripSignInBinding> {

    private Context mContext;
    private String mToken = "";
    private String mDesId = ""; // 查询目的地-集合时间接口返回的id

    private TripSignInDetailBean mInfoDetail;

    private boolean mIsSign = false; // 是否已经签到
    private boolean mIsActivity = true; // 是否在当前界面

    private double destinationLng = 0.0; // 集结点纬度
    private double destinationLat = 0.0; // 集结点经度
    private LatLng mCaptainLatLng; // 集结点坐标
    private LatLng mMyLatLng; // 集结点坐标
    private int mDistance; // 集结点坐标
    /**
     *  1 已签到时间之前
     *  2.已签到时间之后
     *  3.未签到距离大于150m时间之前 10分钟之内
     *  9.未签到距离大于150m时间之前 10分钟之前
     *  4.未签到距离大于150m时间之后
     *  5.未签到距离小于150m时间之前 10分钟之前
     *  6.未签到距离小于150m时间之前 10分钟之内
     *  7.未签到距离小于150m时间之后 60分钟之内
     *  8.未签到距离小于150m时间之后 60分钟之后
     */
    private int mSignInType = 0;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_trip_sign_in;
    }

    @Override
    protected TripSignInViewModel createViewModel() {
        mContext = this;
        viewModel = new TripSignInViewModel();
        viewModel.setIView(this);
        viewModel.mSignInCheckData.observe(this, new Observer<TripSignInDetailBean>() {
            @Override
            public void onChanged(TripSignInDetailBean tripSignInDetailBean) {
                mInfoDetail = tripSignInDetailBean;
                setDatas();
            }
        });
        viewModel.mSignIn.observe(this, new Observer<Object>() {
            @Override
            public void onChanged(Object o) {
                // 刷新界面
                viewModel.getSignInChecked(mToken,mDesId);
            }
        });
        return viewModel;
    }

    @Override
    protected void initData() {
        dataBinding.setViewModel(viewModel);
        dataBinding.setActivity(this);
        EventBus.getDefault().register(this);
        StatusBarUtil.setStatusBarLightMode(this,true);

        RelativeLayout.LayoutParams listParams = (RelativeLayout.LayoutParams) dataBinding.rlTopC.getLayoutParams();
        listParams.topMargin = StatusBarUtil.getStatusBarHeight(getApplicationContext());

        mIsActivity = true;
        mDesId = getIntent().getStringExtra("desid");

        mToken = TourApplication.getToken();
        if (TextUtils.isEmpty(mToken)){
            ToastUtils.showText(mContext,"token已失效");
            return;
        }
        mHandler.sendEmptyMessage(HANDLER_GET_INFO);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_iv : // 返回
                onBackPressed();
                break;
            case R.id.iv_sign_in_refresh : // 刷新
                viewModel.getSignInChecked(mToken,mDesId);
                break;
            case R.id.iv_sign_in_btn : // 签到
                if (IsFastClick.isFastClick()) {
                    if (mSignInType == 5 || mSignInType == 6 || mSignInType == 7) { // 表示可以签到
//                        if (mSignInType == 7){ // 签到动画黄色
//                            dataBinding.lavAnim.setAnimation(R.raw.sign_in_yellow_click_data);
//                            dataBinding.lavAnim.setRepeatCount(1);
//                            dataBinding.lavAnim.playAnimation();
//                        } else { // 签到动画蓝色
//                            dataBinding.lavAnim.setAnimation(R.raw.sign_in_blue_click_data);
//                            dataBinding.lavAnim.setRepeatCount(1);
//                            dataBinding.lavAnim.playAnimation();
//                        }
                        viewModel.setSignIn(mToken, mDesId);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void setDatas(){
        if (mInfoDetail != null){
            // 是否签到
            mIsSign = mInfoDetail.getIsSignin() == 0 ? false : true;
            // 分离出集结点的经纬度
            if (!TextUtils.isEmpty(mInfoDetail.getDestinationCoordinate())) {
                destinationLng = Double.parseDouble(mInfoDetail.getDestinationCoordinate().split(",")[0]);
                destinationLat = Double.parseDouble(mInfoDetail.getDestinationCoordinate().split(",")[1]);
                mCaptainLatLng = new LatLng(destinationLat, destinationLng);
                mMyLatLng = new LatLng(Constants.mLatitude, Constants.mLongitude);
            }
            // 距离大小 向上取整
            mDistance = (int) Math.ceil(AMapUtils.calculateLineDistance(mCaptainLatLng,mMyLatLng));
        }
        updateSignInView();
    }

    private long Time = 60 * 1000;

    private void updateSignInView(){
        if (mIsSign){ // 已经签到
            if (mInfoDetail.getSigninTimeStamp()>0 && mInfoDetail.getGatherTimeStamp() > 0) { // 签到时间大于0 并且集结时间大于0
                long time = mInfoDetail.getSigninTimeStamp() - mInfoDetail.getGatherTimeStamp() - Time; // 如果集结点时间大于签到时间就表示在之前签到
                if (time <= 0){ // 集结地时间之前签到
                    updateView(R.drawable.trip_signin_ok_icon,
                            "准时到达",
                            View.GONE,
                            "",
                            R.drawable.trip_sign_in_normal_bg,
                            TimeUtil.getSignInTime(mInfoDetail.getSigninTimeStamp()),
                            "签到时间",
                            R.drawable.trip_sign_in_already_btn,
                            "集结时间前完成签到视为准时到达",false,false,false,true,false,0);
                    mSignInType = 1;
                } else { // 集结地时间之后签到
                    updateView(R.drawable.trip_signin_late_icon,
                            "迟到",
                            View.GONE,
                            "",
                            R.drawable.trip_sign_in_not_normal_bg,
                            TimeUtil.getSignInLateTime(time),
                            "迟到",
                            R.drawable.trip_sign_in_already_btn,
                            "集结时间后完成签到视为迟到",true,true,false,false,false,0);
                    mSignInType = 2;
                }
            } else if (mInfoDetail.getGatherTimeStamp() > 0){ // 没有签到的时间戳
                updateNotSignInView();
            }
        } else { // 未签到
            updateNotSignInView();
        }
    }

    /**
     * 未签到的界面更新显示
     */
    private void updateNotSignInView(){
        // 时间优先
        long curTime = mInfoDetail.getNowTimeStamp() - Time; // 当前时间
        long time =  curTime - mInfoDetail.getGatherTimeStamp(); // 如果集结点时间大于签到时间就表示在之前签到
        if (time <= 0) { // 时间之前
            /**
             *  3.未签到距离大于150m时间之前 10分钟之内
             *  9.未签到距离大于150m时间之前 10分钟之前
             *  5.未签到距离小于150m时间之前 10分钟之前
             *  6.未签到距离小于150m时间之前 10分钟之内
             */
            long time_10 = mInfoDetail.getGatherTimeStamp() - curTime;
            if (time_10 > (1000*60*10)) { // 10分钟之前
                if (mDistance > Constants.DISTANCE) { // 距离离集结点大于150m 不在范围内
                    updateView(R.drawable.trip_signin_father_icon,
                            "不在签到范围内",
                            View.VISIBLE,
                            "距离集结点 " + AMapUtil.getFriendlyLength((int) mDistance),
                            R.drawable.trip_sign_in_normal_bg,
                            TimeUtil.dateDiff(curTime,mInfoDetail.getGatherTimeStamp()),
                            "倒计时",
                            R.drawable.trip_sign_in_immediately_no_btn,
                            "签到仅记录时间和位置",true,false,false,false,true,R.raw.sign_in_data_blue);
                    mSignInType = 9;
                } else {
                    updateView(R.drawable.trip_signin_normal_advance_icon,
                            "你已在签到范围内",
                            View.GONE,
                            "",
                            R.drawable.trip_sign_in_normal_bg,
                            TimeUtil.dateDiff(curTime,mInfoDetail.getGatherTimeStamp()),
                            "倒计时",
                            R.drawable.trip_sign_in_immediately_btn,
                            "签到仅记录时间和位置",true,false,false,false,true,R.raw.sign_in_data_blue);
                    mSignInType = 5;
                }
            } else { //10分钟之内
                if (mDistance > Constants.DISTANCE) { // 距离离集结点大于150m 不在范围内
                    updateView(R.drawable.trip_signin_father_icon,
                            "不在签到范围内",
                            View.VISIBLE,
                            "距离集结点 " + AMapUtil.getFriendlyLength((int) mDistance),
                            R.drawable.trip_sign_in_normal_bg,
                            TimeUtil.dateDiff(curTime,mInfoDetail.getGatherTimeStamp()),
                            "倒计时",
                            R.drawable.trip_sign_in_immediately_no_btn,
                            "签到仅记录时间和位置",true,false,true,false,true,R.raw.sign_in_data_blue);
                    mSignInType = 3;
                } else {
                    updateView(R.drawable.trip_signin_normal_advance_icon,
                            "你已在签到范围内",
                            View.GONE,
                            "",
                            R.drawable.trip_sign_in_normal_bg,
                            TimeUtil.dateDiff(curTime,mInfoDetail.getGatherTimeStamp()),
                            "倒计时",
                            R.drawable.trip_sign_in_immediately_btn,
                            "签到仅记录时间和位置",true,false,true,false,true,R.raw.sign_in_data_blue);
                    mSignInType = 6;
                }
            }
        } else {
            /**
             *  4.未签到距离大于150m时间之后
             *  7.未签到距离小于150m时间之后 60分钟之内
             *  8.未签到距离小于150m时间之后 60分钟之后
             */
            long time_60 = curTime - mInfoDetail.getGatherTimeStamp();
            if (time_60 < (1000*60*60)){ // 未签到距离小于150m时间之后 60分钟之内
                if (mDistance > Constants.DISTANCE) { // 距离离集结点大于150m 不在范围内
                    updateView(R.drawable.trip_signin_father_icon,
                            "不在签到范围内",
                            View.VISIBLE,
                            "距离集结点 " + AMapUtil.getFriendlyLength((int) mDistance),
                            R.drawable.trip_sign_in_not_normal_bg,
                            TimeUtil.getSignInLateTime(time_60),
                            "迟到",
                            R.drawable.trip_sign_in_immediately_no_btn,
                            "签到仅记录时间和位置",true,true,false,false,true,R.raw.sign_no_data_yellow);
                    mSignInType = 4;
                } else {
                    updateView(R.drawable.trip_signin_normal_advance_icon,
                            "你已在签到范围内",
                            View.GONE,
                            "",
                            R.drawable.trip_sign_in_not_normal_bg,
                            TimeUtil.getSignInLateTime(time_60),
                            "迟到",
                            R.drawable.trip_sign_in_immediately_btn,
                            "签到仅记录时间和位置",true,true,false,false,true,R.raw.sign_no_data_yellow);
                    mSignInType = 7;
                }
            } else { // 未签到距离小于150m时间之后 60分钟之后
                updateView(R.drawable.trip_signin_late_60_icon,
                        "未在指定时间内完成签到",
                        View.GONE,
                        "",
                        R.drawable.trip_sign_in_not_normal_bg,
                        "60分钟以上",
                        "迟到",
                        R.drawable.trip_sign_in_failed_btn,
                        "迟到60分钟以上视为签到失败",true,true,false,false,false,0);
                mSignInType = 8;
            }
        }
    }

    /**
     * 更新界面布局
     * @param topStatusImage 顶部状态图片
     * @param topStatusTxt 顶部状态提示语
     * @param topDistanceShow 顶部距离布局是否显示
     * @param topDistanceStr 顶部距离显示文字
     * @param centerBgImage 中间背景图片
     * @param centerTimeStr 中间时间文本
     * @param centerTimeHintStr 中间时间提示文本
     * @param signInBtnImage 签到btn的图片
     * @param bottomStr 底部提示语
     * @param isText 时间中是否有文字
     * @param isLate 是否已经迟到
     * @param isTimeRed 是否时间字段为红色
     * @param isShowTimeTextSize 是否正常时间
     */
    private void updateView(int topStatusImage, String topStatusTxt, int topDistanceShow, String topDistanceStr, int centerBgImage, String centerTimeStr, String centerTimeHintStr, int signInBtnImage, String bottomStr,
                            boolean isText, boolean isLate, boolean isTimeRed, boolean isShowTimeTextSize,boolean isAnim,int anim){
        dataBinding.ivSignInStatus.setImageResource(topStatusImage); // 状态图片
        dataBinding.tvSignInStatus.setText(topStatusTxt); // 顶部状态语言提示
        dataBinding.tvSignInStatusDistance.setVisibility(topDistanceShow); // 距离问题隐藏
        dataBinding.tvSignInStatusDistance.setText(topDistanceStr); // 距离问题隐藏
        dataBinding.ivSignInRefresh.setVisibility(topDistanceShow); // 距离刷新隐藏
//        dataBinding.rlStatus.setBackground(getResources().getDrawable(centerBgImage)); // 中间签到状态背景图片
        dataBinding.ivStatus.setImageResource(centerBgImage); // 中间签到状态背景图片
        if (isShowTimeTextSize){
            dataBinding.tvTimeDetail.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_32));
        } else {
            dataBinding.tvTimeDetail.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_14));
        }
        if (isText){
            TextViewUtil.setTextViewSignSize(dataBinding.tvTimeDetail, mContext,centerTimeStr,isTimeRed ? true : isLate);
        } else {
            dataBinding.tvTimeDetail.setText(centerTimeStr); // 签到时间的显示
        }
        if (isLate){
            dataBinding.tvTimeDetail.setTextColor(getResources().getColor(R.color.color_FF0C0C)); // 签到时间的显示颜色
            dataBinding.tvTimeDetailHint.setTextColor(getResources().getColor(R.color.color_FF0C0C_50)); // 签到时间提示的显示颜色
        } else if (isTimeRed){
            dataBinding.tvTimeDetail.setTextColor(getResources().getColor(R.color.color_FF0C0C)); // 签到时间的显示颜色
            dataBinding.tvTimeDetailHint.setTextColor(getResources().getColor(R.color.color_24A4FF_50)); // 签到时间提示的显示颜色
        } else {
            dataBinding.tvTimeDetail.setTextColor(getResources().getColor(R.color.color_24A4FF)); // 签到时间的显示颜色
            dataBinding.tvTimeDetailHint.setTextColor(getResources().getColor(R.color.color_24A4FF_50)); // 签到时间提示的显示颜色
        }
        dataBinding.tvTimeDetailHint.setText(centerTimeHintStr); // 签到时间提示的显示
        dataBinding.ivSignInBtn.setImageResource(signInBtnImage); // 签到按钮
        dataBinding.tvBottomHintContent.setText(bottomStr); // 底部提示文字显示内容
        if (isAnim){
            dataBinding.ivStatus.setVisibility(View.INVISIBLE);
            dataBinding.lavAnim.setVisibility(View.VISIBLE);
            dataBinding.lavAnim.setAnimation(anim);
            dataBinding.lavAnim.setRepeatCount(-1);
            dataBinding.lavAnim.playAnimation();
        } else {
            dataBinding.ivStatus.setVisibility(View.VISIBLE);
            dataBinding.lavAnim.setVisibility(View.INVISIBLE);
        }
    }

    private final int HANDLER_GET_INFO = 0x0101;
    private final int UPDATE_TIME_MAP_INFO = 60 * 1000; // 60秒刷新一次

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
                        viewModel.getSignInChecked(mToken,mDesId);
                        if (!mIsSign){
                            mHandler.sendEmptyMessageDelayed(HANDLER_GET_INFO, UPDATE_TIME_MAP_INFO);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void TripSignInEventBus(TripSignInEvent event) {
        viewModel.getSignInChecked(mToken,mDesId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
        mIsActivity = false;
        mHandler.removeMessages(HANDLER_GET_INFO);
    }
}
