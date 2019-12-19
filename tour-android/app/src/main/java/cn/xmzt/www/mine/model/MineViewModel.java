package cn.xmzt.www.mine.model;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableDouble;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.blankj.utilcode.util.ActivityUtils;

import org.greenrobot.eventbus.EventBus;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.manager.IntentManager;
import cn.xmzt.www.mine.activity.BrowseHistoryActivity;
import cn.xmzt.www.mine.activity.CommonlyTourActivity;
import cn.xmzt.www.mine.activity.FriendInviteActivity;
import cn.xmzt.www.mine.activity.HorseSmallMiNotificationsActivity;
import cn.xmzt.www.mine.activity.InformationActivity;
import cn.xmzt.www.mine.activity.LoginActivity;
import cn.xmzt.www.mine.activity.MyCollectionActivity;
import cn.xmzt.www.mine.activity.MyCouponActivity;
import cn.xmzt.www.mine.activity.MyOrderActivity;
import cn.xmzt.www.mine.activity.MyScoreActivity;
import cn.xmzt.www.mine.activity.MyWalletActivity;
import cn.xmzt.www.mine.activity.RefundOrderActivity;
import cn.xmzt.www.mine.activity.SettingActivity;
import cn.xmzt.www.mine.activity.SignInActivity;
import cn.xmzt.www.mine.bean.UserInfoBean;
import cn.xmzt.www.mine.event.OrderTypeEvent;
import cn.xmzt.www.mine.fragment.MineFragment;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.NetWorkUtils;

/**
 * @author tanlei
 * @date 2019/8/6
 * @describe
 */

public class MineViewModel extends BaseViewModel {
    private MineFragment mineFragment;
    /**
     * 是否登录的标志值
     */
    public ObservableBoolean isLogin = new ObservableBoolean();
    /**
     * 用户id
     */
    public ObservableInt id = new ObservableInt();
    /**
     * 昵称
     */
    public ObservableField<String> name = new ObservableField<>();
    /**
     * 头像路劲
     */
    public ObservableField<String> headUrl = new ObservableField<>();
    /**
     * 优惠券数量
     */
    public ObservableInt couponQuantity = new ObservableInt();
    /**
     * 余额
     */
    public ObservableDouble balance = new ObservableDouble();
    public UserInfoBean bean;

    public void setMineFragment(MineFragment mineFragment) {
        this.mineFragment = mineFragment;
        if (TextUtils.isEmpty(TourApplication.getToken())) {
            isLogin.set(false);
        } else {
            isLogin.set(true);
        }
    }
    public int unreadCount;
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.avatar_iv://头像
                startActivity(InformationActivity.class);
                break;
            case R.id.login_tv://去登录
                startActivity(LoginActivity.class);
                break;
            case R.id.mine_setting_ll://设置
                if (TextUtils.isEmpty(TourApplication.getToken())) {
                    startActivity(LoginActivity.class);
                } else {
                    startActivity(SettingActivity.class);
                }
                break;
            case R.id.mine_integration_ll://我的积分
                if (TextUtils.isEmpty(TourApplication.getToken())) {
                    startActivity(LoginActivity.class);
                } else {
                    startActivity(MyScoreActivity.class);
                }
                break;
            case R.id.coupon_ll://优惠券
                if (TextUtils.isEmpty(TourApplication.getToken())) {
                    startActivity(LoginActivity.class);
                } else {
                    startActivity(MyCouponActivity.class);
                }
                break;
            case R.id.mine_collect_ll://我的收藏
                if (TextUtils.isEmpty(TourApplication.getToken())) {
                    startActivity(LoginActivity.class);
                } else {
                    startActivity(MyCollectionActivity.class);
                }
                break;
            case R.id.mine_browsing_history_ll://浏览历史
                if (TextUtils.isEmpty(TourApplication.getToken())) {
                    startActivity(LoginActivity.class);
                } else {
                    startActivity(BrowseHistoryActivity.class);
                }
                break;
            case R.id.mine_linkman_ll://常用出游人
                if (TextUtils.isEmpty(TourApplication.getToken())) {
                    startActivity(LoginActivity.class);
                } else {
                    startActivity(CommonlyTourActivity.class);
                }
                break;
            case R.id.balance_ll://我的余额（钱包界面）
                if (TextUtils.isEmpty(TourApplication.getToken())) {
                    startActivity(LoginActivity.class);
                } else {
                    startActivity(MyWalletActivity.class);
                }
                break;
            case R.id.mine_invite_ll://邀请好友
                if (TextUtils.isEmpty(TourApplication.getToken())) {
                    startActivity(LoginActivity.class);
                } else {
                    startActivity(FriendInviteActivity.class);
                }
                break;
            case R.id.all_order_ll://全部订单
                if (TextUtils.isEmpty(TourApplication.getToken())) {
                    startActivity(LoginActivity.class);
                } else {
                    startActivity(MyOrderActivity.class);
                    EventBus.getDefault().postSticky(new OrderTypeEvent(0));
                }
                break;
            case R.id.unpaid_ll://待支付订单
                if (TextUtils.isEmpty(TourApplication.getToken())) {
                    startActivity(LoginActivity.class);
                } else {
                    startActivity(MyOrderActivity.class);
                    EventBus.getDefault().postSticky(new OrderTypeEvent(1));
                }
                break;
            case R.id.to_be_travelled_ll://待出行订单
                if (TextUtils.isEmpty(TourApplication.getToken())) {
                    startActivity(LoginActivity.class);
                } else {
                    startActivity(MyOrderActivity.class);
                    EventBus.getDefault().postSticky(new OrderTypeEvent(3));
                }
                break;
            case R.id.unevaluated_ll://退款订单界面
                if (TextUtils.isEmpty(TourApplication.getToken())) {
                    startActivity(LoginActivity.class);
                } else {
                    startActivity(RefundOrderActivity.class);
                }
                break;
            case R.id.sign_in_iv://签到

                break;
            case R.id.message_iv://消息中心
                // startActivity(MessageCenterActivity.class);
                Intent intent = new Intent(mineFragment.getActivity(), HorseSmallMiNotificationsActivity.class);
                intent.putExtra("A",unreadCount);
                mineFragment.startActivity(intent);
                break;
            case R.id.tv://签到
                if (NetWorkUtils.isNetConnected(mineFragment.getActivity())) {
                    if (bean != null) {
                        if (bean.isSign()) {
                            startActivity(SignInActivity.class);
                        } else {
                            goSignIn();
                        }
                    } else {
                        getUserBasicInfo();
                    }
                }
                break;
            case R.id.mine_linkcar_ll:// 常用车辆
                IntentManager.getInstance().goCommonVehiclesActivity(ActivityUtils.getTopActivity(),false,false,null);
                break;
            default:
                break;
        }
    }

    /**
     * 去签到
     */
    private void goSignIn() {
        ApiRepertory.getInstance().getApiService().signIn(TourApplication.getToken())
                .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>() {
            @Override
            public void onNext(BaseDataBean<Object> objectBaseDataBean) {
                if (objectBaseDataBean.isSuccess()) {
                    bean.setSign(true);
                    mineFragment.dataBinding.ivSign.setVisibility(View.GONE);
                    mineFragment.dataBinding.tv.setText("签到" + (bean.getContinuouNum() + 1) + "天");
                } else {
                    Toast.makeText(mineFragment.getActivity(), objectBaseDataBean.getReMsg(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * 跳转界面
     *
     * @param c
     */
    public void startActivity(Class c) {
        mineFragment.startActivity(new Intent(mineFragment.getActivity(), c));
    }

    public boolean isHaveOrderUnreadCount=false;//是否有订单未读条数
    /**
     * 获取用户个人中心数据
     */
    public void getUserBasicInfo() {
        ApiRepertory.getInstance().getApiService().getUserInfo(TourApplication.getToken())
                .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<UserInfoBean>>() {
            @Override
            public void onNext(BaseDataBean<UserInfoBean> userInfoBean) {
                if (null != userInfoBean && null != userInfoBean.getRel()) {
                    bean = userInfoBean.getRel();
                    id.set(userInfoBean.getRel().getUserId());
                    name.set(userInfoBean.getRel().getUsername());
                    headUrl.set(userInfoBean.getRel().getHead());
                    couponQuantity.set(userInfoBean.getRel().getCouponQuantity());
                    balance.set(userInfoBean.getRel().getBalance());
                    if (userInfoBean.getRel().isSign()) {//今日已签到
                        mineFragment.dataBinding.ivSign.setVisibility(View.GONE);
                        mineFragment.dataBinding.tv.setText("签到" + userInfoBean.getRel().getContinuouNum() + "天");
                    } else {//今日未签到
                        mineFragment.dataBinding.ivSign.setVisibility(View.VISIBLE);
                        mineFragment.dataBinding.tv.setText("签到");
                    }
                    isHaveOrderUnreadCount=false;
                    if (userInfoBean.getRel().getOrderHintCount().getAllCount() != 0) {
                        isHaveOrderUnreadCount=true;
                        mineFragment.dataBinding.tv1.setVisibility(View.VISIBLE);
                        mineFragment.dataBinding.tv1.setText(userInfoBean.getRel().getOrderHintCount().getAllCount() + "");
                    } else {
                        mineFragment.dataBinding.tv1.setVisibility(View.GONE);
                    }
                    if (userInfoBean.getRel().getOrderHintCount().getUnpaidCount() != 0) {
                        isHaveOrderUnreadCount=true;
                        mineFragment.dataBinding.tv2.setVisibility(View.VISIBLE);
                        mineFragment.dataBinding.tv2.setText(userInfoBean.getRel().getOrderHintCount().getUnpaidCount() + "");
                    } else {
                        mineFragment.dataBinding.tv2.setVisibility(View.GONE);
                    }
                    if (userInfoBean.getRel().getOrderHintCount().getToTravelCount() != 0) {
                        isHaveOrderUnreadCount=true;
                        mineFragment.dataBinding.tv3.setVisibility(View.VISIBLE);
                        mineFragment.dataBinding.tv3.setText(userInfoBean.getRel().getOrderHintCount().getToTravelCount() + "");
                    } else {
                        mineFragment.dataBinding.tv3.setVisibility(View.GONE);
                    }
                    if (userInfoBean.getRel().getOrderHintCount().getRefundState() != 0) {
                        isHaveOrderUnreadCount=true;
                        mineFragment.dataBinding.tv4.setVisibility(View.VISIBLE);
                        mineFragment.dataBinding.tv4.setText(userInfoBean.getRel().getOrderHintCount().getRefundState() + "");
                    } else {
                        mineFragment.dataBinding.tv4.setVisibility(View.GONE);
                    }
                    switch (userInfoBean.getRel().getGrade().getCode()) {
                        case 1:
                            mineFragment.dataBinding.levelIv.setImageResource(R.drawable.level_1);
                            mineFragment.dataBinding.levelTv.setText("普通会员");
                            break;
                        case 2:
                            mineFragment.dataBinding.levelIv.setImageResource(R.drawable.level_2);
                            mineFragment.dataBinding.levelTv.setText("白银会员");
                            break;
                        case 3:
                            mineFragment.dataBinding.levelIv.setImageResource(R.drawable.level_3);
                            mineFragment.dataBinding.levelTv.setText("黄金会员");
                            break;
                        case 4:
                            mineFragment.dataBinding.levelIv.setImageResource(R.drawable.level_4);
                            mineFragment.dataBinding.levelTv.setText("白金会员");
                            break;
                        case 5:
                            mineFragment.dataBinding.levelIv.setImageResource(R.drawable.level_5);
                            mineFragment.dataBinding.levelTv.setText("钻石会员");
                            break;
                        case 6:
                            mineFragment.dataBinding.levelIv.setImageResource(R.drawable.level_6);
                            mineFragment.dataBinding.levelTv.setText("黑金会员");
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }
}
