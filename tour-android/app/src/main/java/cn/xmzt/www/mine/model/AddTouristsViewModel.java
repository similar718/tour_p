package cn.xmzt.www.mine.model;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.View;

import androidx.core.app.ActivityCompat;

import com.blankj.utilcode.util.ToastUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.dialog.TextTitleDialog;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.mine.activity.AddTouristsActivity;
import cn.xmzt.www.mine.bean.TourBean;
import cn.xmzt.www.mine.event.TourEvent;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.IsFastClick;
import cn.xmzt.www.utils.MatcherUtils;
import cn.xmzt.www.utils.PermissionUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static org.webrtc.ContextUtils.getApplicationContext;

/**
 * @author tanlei
 * @date 2019/8/1
 * @describe
 */

public class AddTouristsViewModel extends BaseViewModel {
    private AddTouristsActivity activity;
//    /**
//     * 用来区分是新增还是编辑
//     */
//    public ObservableBoolean isAdd = new ObservableBoolean();

    private TextTitleDialog dialog;
    private TourBean bean;
    /**
     * 证件类型 证件类型：1身份证
     */
    private int certificateType = 1;
    /**
     * 类型：1常用出游人 2酒店入住人 3取票人
     */
    private int type = 1;

    public void setActivity(AddTouristsActivity activity) {
        this.activity = activity;
    }

    public void setBean(TourBean bean) {
        this.bean = bean;
        if (bean.getId() == 0) {//新增常用出游人
//            isAdd.set(true);
            activity.dataBinding.tvTitle.setText(R.string.add_tourer);
            activity.dataBinding.tvDelete.setVisibility(View.GONE);
        } else {//编辑常用出游人
//            isAdd.set(false);
            activity.dataBinding.tvDelete.setVisibility(View.VISIBLE);
            activity.dataBinding.tvTitle.setText(R.string.edit_tourer);
            activity.dataBinding.etName.setText(bean.getName());
            activity.dataBinding.etCode.setText(bean.getIdentityCard());
            activity.dataBinding.etPhone.setText(bean.getTel());
        }
    }

    /**
     * 删除常用出游人信息
     */
    private void deleteOftenTourer() {
        ApiRepertory.getInstance().getApiService().deleteOftenTourer(TourApplication.getToken(), this.bean.getId())
                .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>() {
            @Override
            public void onNext(BaseDataBean<Object> userInfoBean) {
                if (null != userInfoBean) {
                    if (userInfoBean.isSuccess()) {
                        EventBus.getDefault().post(new TourEvent());
                        activity.finish();
                    } else {
                        cn.xmzt.www.utils.ToastUtils.showText(activity, userInfoBean.getReMsg());
                    }
                }
            }
        });
    }

    public boolean check() {
        String name = activity.dataBinding.etName.getText().toString();
        String phone = activity.dataBinding.etPhone.getText().toString();
        String code = activity.dataBinding.etCode.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showShort("请输入中文姓名");
            return false;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showShort("请输入证件号码");
            return false;
        }
        if (!TextUtils.isEmpty(code)) {
            if (certificateType == 1 && !MatcherUtils.isIdCard(code)) {
                ToastUtils.showShort("请输入格式正确的身份证号");
                return false;
            }
        }
        if (!MatcherUtils.isMobile(phone)) {
            ToastUtils.showShort("请输入有效手机号");
            return false;
        }
        return true;
    }

    /**
     * 新增常用出游人信息
     */
    private void addOftenTourer() {
        if (!check()) {
            return;
        }
        ApiRepertory.getInstance().getApiService().saveOftenTourer(TourApplication.getToken(), certificateType, activity.dataBinding.etName.getText().toString(),
                activity.dataBinding.etPhone.getText().toString(), activity.dataBinding.etCode.getText().toString(), type)
                .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>() {
            @Override
            public void onNext(BaseDataBean<Object> userInfoBean) {
                if (null != userInfoBean) {
                    if (userInfoBean.isSuccess()) {
                        EventBus.getDefault().post(new TourEvent());
                        activity.finish();
                    } else {
                        cn.xmzt.www.utils.ToastUtils.showText(activity, userInfoBean.getReMsg());
                    }
                }
            }
        });
    }

    /**
     * 编辑修改常用出游人信息
     */
    private void changeOftenTourer() {
        if (!check()) {
            return;
        }
        ApiRepertory.getInstance().getApiService().editOftenTourer(TourApplication.getToken(), this.bean.getId(), certificateType, activity.dataBinding.etName.getText().toString(),
                activity.dataBinding.etPhone.getText().toString(), activity.dataBinding.etCode.getText().toString(), type)
                .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>() {
            @Override
            public void onNext(BaseDataBean<Object> userInfoBean) {
                if (null != userInfoBean) {
                    if (userInfoBean.isSuccess()) {
                        EventBus.getDefault().post(new TourEvent());
                        activity.finish();
                    } else {
                        cn.xmzt.www.utils.ToastUtils.showText(activity, userInfoBean.getReMsg());
                    }
                }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_delete://删除
                dialog = new TextTitleDialog(activity, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if (IsFastClick.isFastClick()) {
                            deleteOftenTourer();
                        }
                    }
                });
                dialog.setTitle("确认要删除该常用出游人吗？");
                dialog.show();
                break;
            case R.id.title_back_iv:
                activity.finish();
                break;
            case R.id.iv_contacts:
                // 跳转到联系人界面
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.PICK");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setType("vnd.android.cursor.dir/phone_v2");
                    activity.startActivityForResult(intent, 1);
                }else {
                    AndPermission.with(activity).runtime().permission(
                            Manifest.permission.READ_CONTACTS
                    ).onGranted(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.PICK");
                            intent.addCategory("android.intent.category.DEFAULT");
                            intent.setType("vnd.android.cursor.dir/phone_v2");
                            activity.startActivityForResult(intent, 1);
                        }
                    }).onDenied(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> permissions) {
                            // 打开权限设置页
                            PermissionUtil.gotoAppDetailSetting(getApplicationContext());
                        }
                    }).start();
                }
                break;
            case R.id.tv_save://保存
                if (this.bean.getId() == 0) {
                    if (IsFastClick.isFastClick()) {
                        addOftenTourer();
                    }
                } else {
                    if (IsFastClick.isFastClick()) {
                        changeOftenTourer();
                    }
                }
                break;
            default:
                break;
        }
    }
}
