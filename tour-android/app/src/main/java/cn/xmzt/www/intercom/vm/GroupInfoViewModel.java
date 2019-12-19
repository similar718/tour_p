package cn.xmzt.www.intercom.vm;

import android.content.Intent;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.dialog.GroupAddDelayDaysDialog;
import cn.xmzt.www.dialog.TextTitleDialog;
import cn.xmzt.www.helper.UserHelper;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.intercom.activity.GroupCarListActivity;
import cn.xmzt.www.intercom.activity.GroupInfoActivity;
import cn.xmzt.www.intercom.activity.GroupManageActivity;
import cn.xmzt.www.intercom.activity.GroupMemberListActivity;
import cn.xmzt.www.intercom.activity.GroupQRcodeActivity;
import cn.xmzt.www.intercom.activity.MessageRemindActivity;
import cn.xmzt.www.intercom.activity.TransferVehicleActivity;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.intercom.bean.GroupRoomBean;
import cn.xmzt.www.intercom.event.AudioListenStatusEvent;
import cn.xmzt.www.intercom.event.GroupManageEvent;
import cn.xmzt.www.intercom.event.LocationShareStatusEvent;
import cn.xmzt.www.intercom.event.RefreshMyTalkGroupList;
import cn.xmzt.www.intercom.event.RefreshSchedulingListBus;
import cn.xmzt.www.main.activity.MainActivity;
import cn.xmzt.www.roomdb.TourDatabase;
import cn.xmzt.www.roomdb.beans.GroupUserInfo;
import cn.xmzt.www.route.activity.RouteDetailActivity1;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.rxjava2.QzdsException;
import cn.xmzt.www.manager.IntentManager;
import cn.xmzt.www.utils.TimeUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * 行程列表Model
 */
public class GroupInfoViewModel extends BaseViewModel {
    private GroupInfoActivity activity;
    public ObservableBoolean isAutoAnswer = new ObservableBoolean();
    public ObservableBoolean isShareLocation = new ObservableBoolean();
    public ObservableBoolean isDissolutionShow = new ObservableBoolean();

    public MutableLiveData<List<GroupMemberBean>> memberList;//成员列表
    public GroupRoomBean mGroupRoomBean;//群基本信息
    public GroupMemberBean selfGroupMemberBean;//自己在群组中的信息
    public MutableLiveData<List<GroupMemberBean>> vehicleInfo;// 群中车辆的信息

    public GroupInfoViewModel() {
        memberList = new MutableLiveData<List<GroupMemberBean>>();
        vehicleInfo = new MutableLiveData<List<GroupMemberBean>>();
    }

    public void setActivity(GroupInfoActivity activity) {
        this.activity = activity;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                activity.finish();
                break;
            case R.id.ll_transfer_vehicle://转让车辆
                //转让车辆
                Intent intent4 = new Intent(activity, TransferVehicleActivity.class);
                intent4.putExtra("A",groupId);
                intent4.putExtra("B",selfGroupMemberBean.getUserId());
                intent4.putExtra("C",1); //0自己更换司机 1转让车辆
                intent4.putExtra("D",selfGroupMemberBean.getLicencePlate());
                activity.startActivity(intent4);
                break;
            case R.id.ll_product_line://线路产品
                Intent mIntent = new Intent(activity, RouteDetailActivity1.class);
                mIntent.putExtra("A", mGroupRoomBean.getLineId());
                mIntent.putExtra("B", mGroupRoomBean.getLineName());
                activity.startActivity(mIntent);
                break;
            case R.id.ll_qr_code://群二维码
                Intent intent = new Intent(activity, GroupQRcodeActivity.class);
                intent.putExtra("teamId", groupId + "");
                intent.putExtra("name", mGroupRoomBean.getGroupTitle());
                activity.startActivity(intent);
                break;
            case R.id.ll_group_manage://群管理
                Intent intent2 = new Intent(activity, GroupManageActivity.class);
                intent2.putExtra("A", mGroupRoomBean.getGroupId());
                intent2.putExtra("B", mGroupRoomBean.getGroupTitle());
                activity.startActivity(intent2);
                break;
            case R.id.ll_group_name://群名称
                IntentManager.getInstance().goGroupEditorActivity(activity,selfGroupMemberBean.getNickname(), 3, true, mGroupRoomBean.getGroupTitle(), mGroupRoomBean.getGroupId());
                break;
            case R.id.ll_my_group_nikename://我在本群昵称
                IntentManager.getInstance().goGroupEditorActivity(activity, selfGroupMemberBean.getNickname(),2, true, selfGroupMemberBean.getNickname(), mGroupRoomBean.getGroupId());
                break;
            case R.id.tv_group_introduce://群介绍  //是群主第三个参数为true  普通成为为false
            case R.id.tv1:
                IntentManager.getInstance().goGroupEditorActivity(activity, selfGroupMemberBean.getNickname(),1, true, mGroupRoomBean.getIntro(), mGroupRoomBean.getGroupId());
                break;
            case R.id.iv_automatic_answer://自动接听开关
                getIsShareLocationOrISAutoplay(groupId,!selfGroupMemberBean.isAutoplay(),selfGroupMemberBean.isShareLocation());
                break;
            case R.id.ll_message://消息提醒
                Intent intent1 = new Intent(activity, MessageRemindActivity.class);
                intent1.putExtra("teamId", mGroupRoomBean.getGroupId());
                activity.startActivity(intent1);
                break;
            case R.id.iv_share_location://分享位置开关
                getIsShareLocationOrISAutoplay(groupId,selfGroupMemberBean.isAutoplay(),!selfGroupMemberBean.isShareLocation());
                break;
            case R.id.tv_logout_group://退出群聊
                if(selfGroupMemberBean!=null){
                   if(selfGroupMemberBean.getTripStatus()==2){
                       if(selfGroupMemberBean.getRole()==1){
                           ToastUtils.showShort("出行中不能解散群");
                       }else {
                           ToastUtils.showShort("出行中不能退出群");
                       }
                   }else {
                       showHintDialog();
                   }
                }
                break;
            case R.id.tv_all_member://查看全部成员
                Intent intent3 = new Intent(activity, GroupMemberListActivity.class);
                intent3.putExtra("A", mGroupRoomBean.getGroupId());
                activity.startActivity(intent3);
                break;
            case R.id.tv_all_car://查看全部车辆
                Intent intent5 = new Intent(activity, GroupCarListActivity.class);
                intent5.putExtra("A", groupId);
                if(selfGroupMemberBean!=null){
                    if(selfGroupMemberBean.getRole()==1||selfGroupMemberBean.isLeader()){
                        intent5.putExtra("B", true);
                    }
                }
                activity.startActivity(intent5);
                break;
            case R.id.dissolution_layout://自动延长解散群时间
               showGroupAddDelayDaysDialog();
                break;
        }
    }

    public String groupId=null;
    /**
     * 获取群信息、成员列表
     */
    public void getGroupRoomInfo() {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getGroupRoomInfo(TourApplication.getToken(), groupId);
        mObservable.flatMap(new Function<BaseDataBean<GroupRoomBean>, Observable<BaseDataBean<GroupMemberBean>>>() {
            @Override
            public Observable<BaseDataBean<GroupMemberBean>> apply(BaseDataBean<GroupRoomBean> body) throws Exception {
                if (body.isSuccess()) {
                    mGroupRoomBean = body.getRel();
                    return mService.getGroupUser(TourApplication.getToken(), groupId, null);
                } else {
                    if(!activity.isFinishing()){
                        activity.finish();
                    }
                    throw new QzdsException(body.getReMsg(), body.getReCode());
                }
            }
        }).flatMap(new Function<BaseDataBean<GroupMemberBean>, Observable<BaseDataBean<List<GroupMemberBean>>>>() {
            @Override
            public Observable<BaseDataBean<List<GroupMemberBean>>> apply(BaseDataBean<GroupMemberBean> body) throws Exception {
                if (body.isSuccess()) {
                    selfGroupMemberBean = body.getRel();
                    return mService.getMemberList(groupId, TourApplication.getToken(), null, null, null, null, null, null, null);
                } else {
                    throw new QzdsException(body.getReMsg(), body.getReCode());
                }
            }
        }).compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<GroupMemberBean>>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<List<GroupMemberBean>> body) {
                        if (body.isSuccess()) {
                            getVehicleInfo(TourApplication.getToken(),groupId);
                            List<GroupMemberBean> list = body.getRel();
                            if (list == null) {
                                list = new ArrayList<>();
                            }
                            memberList.setValue(list);
                            activity.dataBinding.topC.setText("对讲聊天("+list.size()+")");
                            activity.dataBinding.tvName.setText(mGroupRoomBean.getLineName());
                            activity.dataBinding.tvGroupId.setText("ID:" + groupId);
                            activity.dataBinding.tvDes.setText(mGroupRoomBean.getGroupTitle());
                            activity.dataBinding.tvGroupIntroduce.setText(mGroupRoomBean.getIntro());
                            String dissolveDate=TimeUtil.stringDateToString(mGroupRoomBean.getDissolveDate(),"yyyy-MM-dd HH:mm:ss","yyyy年MM月dd日HH点");
                            activity.dataBinding.tvDissolution.setText(dissolveDate);
                            activity.dataBinding.tvNikename.setText(selfGroupMemberBean.getNickname());
                            if(selfGroupMemberBean.getDriver()){
                                activity.dataBinding.llTransferVehicle.setVisibility(View.VISIBLE);
                            }else {
                                activity.dataBinding.llTransferVehicle.setVisibility(View.GONE);
                            }
                            if(selfGroupMemberBean.getRole()==1||selfGroupMemberBean.isLeader()){
                                activity.dataBinding.llGroupManage.setVisibility(View.VISIBLE);
                            }else {
                                activity.dataBinding.llGroupManage.setVisibility(View.GONE);
                            }
                            //1待出行 2出行中 3已结束
                            if(selfGroupMemberBean.getTripStatus()==3){
                                activity.dataBinding.llQrCode.setVisibility(View.GONE);
                            }else if(selfGroupMemberBean.getTripStatus()==2){
                                if(selfGroupMemberBean.getRole()==1||selfGroupMemberBean.isLeader()){
                                    activity.dataBinding.llQrCode.setVisibility(View.VISIBLE);
                                }else {
                                    activity.dataBinding.llQrCode.setVisibility(View.GONE);
                                }
                            }else {
                                activity.dataBinding.llQrCode.setVisibility(View.VISIBLE);
                            }
                            if(selfGroupMemberBean.getRole()==1){
                                activity.dataBinding.tvLogoutGroup.setText("解散群聊");
                                activity.dataBinding.llGroupName.setEnabled(true);
                                activity.dataBinding.tv1.setEnabled(true);
                                activity.dataBinding.tvGroupIntroduce.setEnabled(true);
                                activity.dataBinding.dissolutionLayout.setEnabled(true);
                                isDissolutionShow.set(true);
                                activity.dataBinding.tv1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hotel_info_more, 0);
                            }else {
                                activity.dataBinding.tv1.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0);
                                activity.dataBinding.tvLogoutGroup.setText("退出群聊");
                                activity.dataBinding.llGroupName.setEnabled(false);
                                activity.dataBinding.tv1.setEnabled(false);
                                activity.dataBinding.tvGroupIntroduce.setEnabled(false);
                                activity.dataBinding.dissolutionLayout.setEnabled(false);
                                isDissolutionShow.set(false);
                            }
                            isShareLocation.set(selfGroupMemberBean.isShareLocation());
                            isAutoAnswer.set(selfGroupMemberBean.isAutoplay());
                        } else {
                            mView.showLoadFail(null);
                        }
                    }
                });
    }

    private void getVehicleInfo(String token,String groupId){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getVehicleInfo(token,groupId);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<GroupMemberBean>>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<List<GroupMemberBean>> body) {
                        List<GroupMemberBean> vehicleList=body.getRel();
                        if(vehicleList==null){
                            vehicleList=new ArrayList<GroupMemberBean>();
                        }
                        vehicleInfo.setValue(vehicleList);
                    }
                });
    }

    /**
     * 添加车辆
     */
    public void addDriver(String licencePlate,int userId) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.addDriver(groupId,licencePlate,userId);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<String>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<String> body) {
                        if(body.isSuccess()){
                            ToastUtils.showShort("设置车辆成功");
                            getVehicleInfo(TourApplication.getToken(),groupId);
                            saveDataBase(groupId, String.valueOf(userId),licencePlate);
                        }else {
                            mView.showLoadFail(body.getReMsg());
                        }
                    }
                });
    }


    /**
     * 将修改的车辆信息添加到数据库
     * @param groupId
     * @param userid
     * @param licencePlate
     */
    private void saveDataBase(String groupId,String userid,String licencePlate){
        GroupUserInfo info = TourDatabase.getDefault(ActivityUtils.getTopActivity()).getGroupUserInfoDao().getData(groupId,userid);
        if (info != null){
            info.numberPlate = licencePlate;
            TourDatabase.getDefault(ActivityUtils.getTopActivity()).getGroupUserInfoDao().update(info);
        }
    }

    /**
     * 退群(主动退群)
     * @param groupId  群id
     * @param userId  用户ID
     * @param isRemoveGroup  是否解散群
     * @return
     */
    public void leaveGroup(String groupId,String userId,boolean isRemoveGroup) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = null;
        if(isRemoveGroup){
            mObservable = mService.removeGroup(groupId);
        }else {
            mObservable = mService.leaveGroup(groupId,userId);
        }
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> body) {
                        if(body.isSuccess()){
                            // 相除对讲相关业务
                            UserHelper.clearTalkBusiness(false);
                            EventBus.getDefault().post(new RefreshSchedulingListBus(1));
                            EventBus.getDefault().post(new RefreshMyTalkGroupList()); // 更新对讲群列表信息
                            Intent intent = new Intent();
                            intent.setClass(activity, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            // 删除本地数据库内的所有数据
                            TourDatabase.getDefault(activity).getGroupUserInfoDao().deleteGroupInfo(groupId);

                            activity.startActivity(intent);
                            activity.finish();
                        }else {
                            ToastUtils.showShort(body.getReMsg());
                        }
                    }
                });
    }

    /**
     * 延长解散日期
     * @param dissolveDate      dissolveDate 延长天数
     */
    public void getExtendGroupDissolveDate(int dissolveDate){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getExtendGroupDissolveDate(TourApplication.getToken(),groupId,dissolveDate);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> body) {
                        if(body.isSuccess()){
                            String date=mGroupRoomBean.getDissolveDate();
                            EventBus.getDefault().post(new GroupManageEvent(24));//提示上一个页面刷新
                            if(date!=null){
                                long dateTimes=TimeUtil.strToDate(date,"yyyy-MM-dd HH:mm:ss").getTime();
                                long dissolveDateTimes=dateTimes+dissolveDate*24*60*60*1000;
                                mGroupRoomBean.setDissolveDate(TimeUtil.dateToStr(dissolveDateTimes,"yyyy-MM-dd HH:mm:ss"));

                                String dissolveDate=TimeUtil.stringDateToString(mGroupRoomBean.getDissolveDate(),"yyyy-MM-dd HH:mm:ss","yyyy年MM月dd日HH点");
                                activity.dataBinding.tvDissolution.setText(dissolveDate);
                            }
                            ToastUtils.showShort("设置延长解散日期成功");
                        }else {
                            ToastUtils.showShort("设置延长解散日期失败");
                        }
                    }
                });
    }
    /**
     * 是否分享位置-是否自动播放
     * @param groupId
     * @param isAutoplay  是否自动播放
     * @param isShareLoc 是否分享位置
     */
    public void getIsShareLocationOrISAutoplay(String groupId,boolean isAutoplay,boolean isShareLoc){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getIsShareLocationOrISAutoplay(TourApplication.getToken(),groupId,isAutoplay,isShareLoc);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> body) {
                        if(body.isSuccess()){
                            selfGroupMemberBean.setAutoplay(isAutoplay);
                            isAutoAnswer.set(isAutoplay);
                            selfGroupMemberBean.setShareLocation(isShareLoc);
                            isShareLocation.set(isShareLoc);
                            EventBus.getDefault().post(new LocationShareStatusEvent(groupId,""+selfGroupMemberBean.getUserId(),isShareLoc));
                            EventBus.getDefault().post(new AudioListenStatusEvent(groupId,""+selfGroupMemberBean.getUserId(),isAutoplay));
                        }
                    }
                });
    }

    private TextTitleDialog hintDialog;
    private void showHintDialog(){
        if(hintDialog==null){
            hintDialog = new TextTitleDialog(activity, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hintDialog.dismiss();
                    if(selfGroupMemberBean.getRole()==1){
                        leaveGroup(groupId,""+selfGroupMemberBean.getUserId(),true);
                    }else {
                        leaveGroup(groupId,""+selfGroupMemberBean.getUserId(),false);
                    }

                }
            });
        }
        if(selfGroupMemberBean.getRole()==1){
            hintDialog.setTitle("确定解散群聊？");
        }else {
            hintDialog.setTitle("确定退出群聊？");
        }
        hintDialog.show();
    }
    private GroupAddDelayDaysDialog groupAddDelayDaysDialog;
    private void showGroupAddDelayDaysDialog(){
        if(groupAddDelayDaysDialog==null){
            groupAddDelayDaysDialog = new GroupAddDelayDaysDialog(activity, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()){
                        case R.id.tv_cancel:
                            break;
                        case R.id.tv_add_5:
                            getExtendGroupDissolveDate(5);
                            break;
                        case R.id.tv_add_10:
                            getExtendGroupDissolveDate(10);
                            break;
                        default:
                            break;
                    }
                    groupAddDelayDaysDialog.cancel();
                }
            });
        }
        groupAddDelayDaysDialog.show();;
    }
}
