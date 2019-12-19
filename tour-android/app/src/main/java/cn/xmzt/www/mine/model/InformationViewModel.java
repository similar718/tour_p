package cn.xmzt.www.mine.model;

import android.content.Intent;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import android.view.Gravity;
import android.view.View;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.mine.activity.InformationActivity;
import cn.xmzt.www.mine.activity.ModifyNicknameActivity;
import cn.xmzt.www.mine.bean.ImageUrlBean;
import cn.xmzt.www.intercom.bean.UserBasicInfoBean;
import cn.xmzt.www.mine.event.ImagePhotoEvent;
import cn.xmzt.www.mine.event.NicknameEvent;
import cn.xmzt.www.popup.ChangeGenderPopupWindow;
import cn.xmzt.www.popup.SelectPhotoPicturePopupWindow;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author tanlei
 * @date 2019/8/7
 * @describe 修改用户个人信息界面
 */

public class InformationViewModel extends BaseViewModel {
    public InformationActivity activity;

    /**
     * 头像路劲
     */
    public ObservableField<String> headUrl = new ObservableField<>();
    /**
     * 昵称
     */
    public ObservableField<String> name = new ObservableField<>();
    /**
     * 用户id
     */
    public ObservableInt id = new ObservableInt();
    /**
     * 性别
     */
    public ObservableInt gender = new ObservableInt();
    /**
     * 年龄
     */
    public ObservableInt age = new ObservableInt();

    public void setActivity(InformationActivity activity) {
        this.activity = activity;
    }

    /**
     * 上传图片
     */
    public void uploadImage(List<File> list) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型
        //2.获取图片，创建请求体
        File file = list.get(0);
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);//表单类型
        builder.addFormDataPart("imgs", file.getName(), body); //添加图片数据，body创建的请求体
        ApiRepertory.getInstance().getApiService().uplodImgsByFile(builder.build().parts(),"user")
                .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<List<ImageUrlBean>>>() {
            @Override
            public void onNext(BaseDataBean<List<ImageUrlBean>> userInfoBean) {
                if (null != userInfoBean && null != userInfoBean.getRel()) {
                    if (userInfoBean.isSuccess()) {
                        uploadHead(userInfoBean.getRel().get(0).getUrl());
                    }
                }
            }
        });
    }

    public void uploadHead(String fileName) {
        ApiRepertory.getInstance().getApiService().uploadHead(fileName, TourApplication.getToken())
                .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>() {
            @Override
            public void onNext(BaseDataBean<Object> userInfoBean) {
                if (userInfoBean.isSuccess()) {
                    EventBus.getDefault().postSticky(new ImagePhotoEvent(fileName));
                    GlideUtil.loadImgRadius(activity.dataBinding.ivAvatar, 0, fileName);
                }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nickname_tv://修改昵称
                //使用EventBus传递昵称至修改昵称界面
                EventBus.getDefault().postSticky(new NicknameEvent(name.get()));
                activity.startActivity(new Intent(activity, ModifyNicknameActivity.class));
                break;
            case R.id.title_back_iv://返回
                activity.finish();
                break;
            case R.id.gender_ll: //选择性别
                ChangeGenderPopupWindow popupWindow = new ChangeGenderPopupWindow(activity);
                popupWindow.setOnItemClickListener(new ChangeGenderPopupWindow.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {//(0：保密，1：男 2：女)
                        ApiRepertory.getInstance().getApiService().modifyGender(TourApplication.getToken(), position).subscribeOn(Schedulers.io())
                                .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                            @Override
                            public void onNext(BaseDataBean<Object> verificationCodeBean) {
                                if (null != verificationCodeBean) {
                                    if (verificationCodeBean.isSuccess()) {
                                        ToastUtils.showText(activity,"性别修改成功");
                                        gender.set(position);
                                        switch (gender.get()) {
                                            case 0:
                                                activity.dataBinding.genderTv.setText("保密");
                                                break;
                                            case 1:
                                                activity.dataBinding.genderTv.setText("男");
                                                break;
                                            case 2:
                                                activity.dataBinding.genderTv.setText("女");
                                                break;
                                        }
                                        popupWindow.dismiss();
                                    } else {
                                        ToastUtils.showText(activity, verificationCodeBean.getReMsg());
                                    }
                                }
                            }
                        });
                    }
                });
                popupWindow.showAtLocation(activity.dataBinding.llLayout, Gravity.BOTTOM, 0, 0);
                break;

            case R.id.avatar_rl://修改头像
                SelectPhotoPicturePopupWindow photoPicturePopupWindow = new SelectPhotoPicturePopupWindow(activity);
                photoPicturePopupWindow.setOnItemClickListener(new ChangeGenderPopupWindow.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        switch (position) {
                            case 0:
                                activity.startCamera();
                                photoPicturePopupWindow.dismiss();
                                break;
                            case 1:
                                activity.startAlbum();
                                photoPicturePopupWindow.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });
                photoPicturePopupWindow.showAtLocation(activity.dataBinding.llLayout, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.age_ll://修改年龄
                Calendar startDate = Calendar.getInstance();
                Calendar endDate = Calendar.getInstance();
                //正确设置方式 原因：注意事项有说明
                startDate.setTime(new Date(System.currentTimeMillis()));
                endDate.setTime(new Date(System.currentTimeMillis()));
                startDate.add(Calendar.YEAR, -100);
                //时间选择器
                TimePickerView pvTime = new TimePickerBuilder(activity, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        int oldYear = calendar.get(Calendar.YEAR);
                        int year = endDate.get(Calendar.YEAR);
                        ApiRepertory.getInstance().getApiService().modifyAge(TourApplication.getToken(), year - oldYear).subscribeOn(Schedulers.io())
                                .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                            @Override
                            public void onNext(BaseDataBean<Object> verificationCodeBean) {
                                if (null != verificationCodeBean) {
                                    if (verificationCodeBean.isSuccess()) {
                                        ToastUtils.showText(activity,"年龄修改成功");
                                        age.set(year - oldYear);
                                    } else {
                                        ToastUtils.showText(activity, verificationCodeBean.getReMsg());
                                    }
                                }
                            }
                        });
                    }
                }).setDate(endDate).setRangDate(startDate, endDate).build();
                //起始终止年月日设定
                pvTime.show();
                break;
            default:
                break;
        }
    }

    /**
     * 获取用户基本信息
     */
    public void getUserBasicInfo() {
        ApiRepertory.getInstance().getApiService().getUserBasicInfo(TourApplication.getToken())
                .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<UserBasicInfoBean>>() {
            @Override
            public void onNext(BaseDataBean<UserBasicInfoBean> userInfoBean) {
                if (null != userInfoBean && null != userInfoBean.getRel()) {

                    TourApplication.setUser(userInfoBean.getRel());
                    id.set(userInfoBean.getRel().getUserId());
                    name.set(userInfoBean.getRel().getUsername());
                    headUrl.set(userInfoBean.getRel().getHead());
                    gender.set(userInfoBean.getRel().getGender());
                    age.set(userInfoBean.getRel().getAge());
                    switch (userInfoBean.getRel().getGender()) {
                        case 0:
                            activity.dataBinding.genderTv.setText("保密");
                            break;
                        case 1:
                            activity.dataBinding.genderTv.setText("男");
                            break;
                        case 2:
                            activity.dataBinding.genderTv.setText("女");
                            break;
                    }
                    switch (userInfoBean.getRel().getGrade().getCode()) {
                        case 1:
                            activity.dataBinding.levelTv.setText("普通会员");
                            break;
                        case 2:
                            activity.dataBinding.levelTv.setText("白银会员");
                            break;
                        case 3:
                            activity.dataBinding.levelTv.setText("黄金会员");
                            break;
                        case 4:
                            activity.dataBinding.levelTv.setText("白金会员");
                            break;
                        case 5:
                            activity.dataBinding.levelTv.setText("钻石会员");
                            break;
                        case 6:
                            activity.dataBinding.levelTv.setText("黑金会员");
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }
}
