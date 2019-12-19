package cn.xmzt.www.intercom.vm;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.dialog.ConfirmDialog;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.intercom.activity.GroupPersonalInfoActivity;
import cn.xmzt.www.intercom.activity.SettingTagActivity;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.intercom.bean.GroupRoomBean;
import cn.xmzt.www.intercom.event.GroupManageEvent;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.rxjava2.QzdsException;
import cn.xmzt.www.utils.DpUtil;
import cn.xmzt.www.utils.TimeUtil;
import cn.xmzt.www.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tanlei
 * @date 2019/9/7
 * @describe
 */

public class GroupPersonalInfoViewModel extends BaseViewModel {
    private GroupPersonalInfoActivity activity;
    private String teamId;
    private String userId;
    private String completePhone="";//完整手机号
    private String describe;
    private int userid = 0;

    // 是否参团
    private boolean misJoin = false;
    // 操作确认对话框
    private ConfirmDialog confirmDialog;

    public void setTeamId(String teamId, String userId) {
        this.teamId = teamId;
        this.userId = userId;
        getGroupMemberInfo();
    }

    public void setActivity(GroupPersonalInfoActivity activity) {
        this.activity = activity;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_iv:
                activity.finish();
                break;
            case R.id.ll_describe:
                Intent intent = new Intent(activity, SettingTagActivity.class);
                // TODO: 2019/9/7 需要用户id
                intent.putExtra("userId", userId);
                intent.putExtra("teamId", teamId);
                intent.putExtra("describe",describe);
                activity.startActivityForResult(intent,100);
//                activity.startActivity(intent);
                break;
            case R.id.tv_option: // 是否已经参团
                if (userid != 0) {
                    if (misJoin) { // 是否确认取消参团，相关权限将会取消
                        showDialog("是否确认取消参团，相关权限将会取消");
                    } else { // 是否确认标记为已参团，参团后将获得相关权限
                        showDialog("是否确认标记为已参团，参团后将获得相关权限");
                    }
                }
                break;
            case R.id.tv_contact_leader: { // 联系领队
                if (!TextUtils.isEmpty(completePhone)) {
                    callPhone(completePhone);
                }
                break;
            }
            case R.id.tv_contact_ta: // 群主领队联系普通成员
                if (!TextUtils.isEmpty(completePhone)) {
                    callPhone(completePhone);
                }
                break;
        }
    }
    /**
     * 拨打电话（直接拨打电话）
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum){
        Intent intent = new Intent(Intent.ACTION_DIAL);//ACTION_DIAL ACTION_CALL
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        ActivityUtils.getTopActivity().startActivity(intent);
    }
    private void showDialog(String title){
        if (confirmDialog == null){
            confirmDialog = new ConfirmDialog(activity, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmDialog.dismiss();
                    if (v.getId() == R.id.confirm_tv){
                        getUpdatePayingUser(!misJoin,userid);
                    }
                }
            });
        }
        confirmDialog.setViewData(title);
        confirmDialog.show();
    }
    private int groupType=2;//1智能组队群，2行程群
    private GroupMemberBean mGroupMemberBean=null;
    private GroupMemberBean selfGroupMemberBean=null;//我在群中的信息
    /**
     * 获取群成员信息
     */
    public void getGroupMemberInfo() {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getGroupRoomInfo(TourApplication.getToken(), teamId);
        mObservable.flatMap(new Function<BaseDataBean<GroupRoomBean>, Observable<BaseDataBean<GroupMemberBean>>>() {
            @Override
            public Observable<BaseDataBean<GroupMemberBean>> apply(BaseDataBean<GroupRoomBean> body) throws Exception {
                if (body.isSuccess()) {
                    GroupRoomBean mGroupRoomBean = body.getRel();
                    if(mGroupRoomBean!=null){
                        groupType=mGroupRoomBean.getGroupType();
                    }
                    return mService.getGroupUser(TourApplication.getToken(), teamId, userId);
                } else {
                    if(!activity.isFinishing()){
                        activity.finish();
                    }
                    throw new QzdsException(body.getReMsg(), body.getReCode());
                }
            }
        }).flatMap(new Function<BaseDataBean<GroupMemberBean>, Observable<BaseDataBean<GroupMemberBean>>>() {
            @Override
            public Observable<BaseDataBean<GroupMemberBean>> apply(BaseDataBean<GroupMemberBean> body) throws Exception {
                if (body.isSuccess()) {
                    mGroupMemberBean = body.getRel();
                    return mService.getGroupUser(TourApplication.getToken(), teamId, null);
                } else {
                    throw new QzdsException(body.getReMsg(), body.getReCode());
                }
            }
        }).compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<GroupMemberBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<GroupMemberBean> body) {
                        selfGroupMemberBean = body.getRel();
                        if (mGroupMemberBean!=null) {
                            userid = mGroupMemberBean.getUserId();
                            completePhone=mGroupMemberBean.getCompletePhone();
                            misJoin = mGroupMemberBean.getPayingUser();
                            GlideUtil.loadImgRadius(activity.dataBinding.ivHead, 4, mGroupMemberBean.getImage());
                            activity.dataBinding.tvNickName.setText(mGroupMemberBean.getNickname());
                            activity.dataBinding.tvId.setText("ID号：" + mGroupMemberBean.getUserId());
                            activity.dataBinding.tvPhone.setText(mGroupMemberBean.getPhone());

                            activity.dataBinding.ivTag.setVisibility(View.GONE);
                            activity.dataBinding.ivTagYct.setVisibility(View.GONE);
                            if (mGroupMemberBean.getRole() == 1){
                                activity.dataBinding.ivTag.setImageResource(R.drawable.bg_group_menber_info_label_qz);
                                activity.dataBinding.ivTag.setVisibility(View.VISIBLE);
                                if (mGroupMemberBean.getPayingUser()) {
                                    activity.dataBinding.tvOrderLine.setVisibility(View.VISIBLE);
                                    activity.dataBinding.llOrder.setVisibility(View.VISIBLE);
                                    activity.dataBinding.tvOrder.setText(mGroupMemberBean.getOrderId());
                                }
                            } else if (mGroupMemberBean.isLeader()){
                                activity.dataBinding.ivTag.setImageResource(R.drawable.bg_group_menber_info_label_ld);
                                activity.dataBinding.ivTag.setVisibility(View.VISIBLE);

                                if (mGroupMemberBean.getPayingUser()) {
                                    activity.dataBinding.tvOrderLine.setVisibility(View.VISIBLE);
                                    activity.dataBinding.llOrder.setVisibility(View.VISIBLE);
                                    activity.dataBinding.tvOrder.setText(mGroupMemberBean.getOrderId());
                                }

                                activity.dataBinding.tvContactLeader.setVisibility(View.GONE);
                                //普通成员联系领队
                                if(selfGroupMemberBean!=null&&selfGroupMemberBean.getRole() != 1&&!selfGroupMemberBean.isLeader()){
                                    activity.dataBinding.tvContactLeader.setVisibility(View.VISIBLE);
                                }

                            } else if (mGroupMemberBean.getPayingUser()){
                                activity.dataBinding.ivTagYct.setVisibility(View.VISIBLE);

                                activity.dataBinding.tvOrderLine.setVisibility(View.VISIBLE);
                                activity.dataBinding.llOrder.setVisibility(View.VISIBLE);
                                activity.dataBinding.tvOrder.setText(mGroupMemberBean.getOrderId());

                            } else {
                                activity.dataBinding.tvOrderLine.setVisibility(View.GONE);
                                activity.dataBinding.llOrder.setVisibility(View.GONE);
                            }
                            if (TextUtils.isEmpty(mGroupMemberBean.getDescribe())) {
                                activity.dataBinding.tvDesc.setVisibility(View.GONE);
                            } else {
                                activity.dataBinding.tvDesc.setVisibility(View.VISIBLE);
                                describe = mGroupMemberBean.getDescribe();
                                activity.dataBinding.tvDesc.setText(mGroupMemberBean.getDescribe());
                            }
                        }
                        if (null != selfGroupMemberBean) {
                            boolean isShow = false;
                            if (selfGroupMemberBean.getRole() == 1||selfGroupMemberBean.isLeader()){
                                isShow = true;
                            }
                            activity.dataBinding.tvOption.setVisibility(View.GONE);
                            activity.dataBinding.tvContactTa.setVisibility(View.GONE);
                            if (isShow&&selfGroupMemberBean.getUserId()!=userid){ // 表示你是群成员 或者群领队
                                if (misJoin){
                                    activity.dataBinding.tvOption.setVisibility(View.VISIBLE);
                                    activity.dataBinding.tvOption.setBackgroundResource(R.drawable.group_info_join);
                                } else {
                                    activity.dataBinding.tvOption.setVisibility(View.VISIBLE);
                                    activity.dataBinding.tvOption.setBackgroundResource(R.drawable.group_info_no_join);
                                }
                                //领队或群主查看群成员时，显示【联系Ta】，成员看成员无此功能；
                                if(mGroupMemberBean!=null&&!mGroupMemberBean.isLeader()&&mGroupMemberBean.getRole()!=1){
                                    activity.dataBinding.tvContactTa.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        if(groupType==1) {//智能组队群的成员
                            activity.dataBinding.tvOrderLine.setVisibility(View.GONE);
                            activity.dataBinding.llOrder.setVisibility(View.GONE);
                            activity.dataBinding.tvOption.setVisibility(View.GONE);
                            activity.dataBinding.tvContactLeader.setVisibility(View.GONE);
                            activity.dataBinding.tvContactTa.setVisibility(View.GONE);
                            if (mGroupMemberBean!=null) {
                                if(mGroupMemberBean.isLeader()){
                                    activity.dataBinding.ivTag.setImageResource(R.drawable.bg_group_menber_info_label_ld);
                                    activity.dataBinding.ivTag.setVisibility(View.VISIBLE);
                                    if (selfGroupMemberBean!=null&&!selfGroupMemberBean.isLeader()){
                                        activity.dataBinding.tvContactLeader.setVisibility(View.VISIBLE);
                                    }
                                }else {
                                    if (selfGroupMemberBean!=null&&selfGroupMemberBean.isLeader()){
                                        activity.dataBinding.tvContactTa.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }
                    }
                });
    }
    private void getUpdatePayingUser(boolean isPay,int userid) {
        ApiRepertory.getInstance().getApiService().updtGroupPayingUserState(TourApplication.getToken(), teamId, isPay,userid)
                .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
            @Override
            public void onNext(BaseDataBean<Object> verificationCodeBean) {
                if (null != verificationCodeBean) {
                    if (verificationCodeBean.isSuccess()) {
                        ToastUtils.showText(activity,"操作成功");
                        EventBus.getDefault().post(new GroupManageEvent(2));//提示上一个页面刷新
                        getGroupMemberInfo(); // 去更新界面
                    } else {
                        ToastUtils.showText(activity,verificationCodeBean.getReMsg());
                    }
                }
            }
        });
    }
}
