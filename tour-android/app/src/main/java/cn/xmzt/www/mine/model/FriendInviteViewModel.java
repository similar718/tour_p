package cn.xmzt.www.mine.model;

import androidx.databinding.ObservableBoolean;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.mine.activity.FriendInviteActivity;
import cn.xmzt.www.mine.adapter.FriendInviteAdapter;
import cn.xmzt.www.mine.bean.ShapeBitmapBean;
import cn.xmzt.www.mine.bean.ShareBean;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.share.ShareFunction;
import cn.xmzt.www.utils.DonwloadSaveImgUtils;
import cn.xmzt.www.utils.ToastUtils;
import cn.xmzt.www.view.banner.FriendBannerAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.schedulers.Schedulers;

/**
 * @author tanlei
 * @date 2019/8/16
 * @describe
 */

public class FriendInviteViewModel extends BaseViewModel {
    private FriendInviteActivity activity;

    public ObservableBoolean leftOrRight = new ObservableBoolean();
    private List<ShareBean> list = new ArrayList<>();
    private FriendInviteAdapter adapter;
    /**
     * 类型：1=链接分享 2=海报分享
     */
    private int type = 1;
    private List<String> bannerList = new ArrayList<>();
    private FriendBannerAdapter webBannerAdapter;
    /**
     * 左边选中项
     */
    private int leftCheckPosition = 0;

    public void setActivity(FriendInviteActivity activity) {
        this.activity = activity;
        adapter = new FriendInviteAdapter(list, activity);
        activity.dataBinding.rv.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        activity.dataBinding.rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new FriendInviteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (leftCheckPosition != position) {
                    leftCheckPosition = position;
                    for (ShareBean shareBean : list) {
                        shareBean.setCheck(false);
                    }
                    list.get(leftCheckPosition).setCheck(true);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        webBannerAdapter = new FriendBannerAdapter(activity, bannerList);
        activity.dataBinding.banner.setAdapter(webBannerAdapter);
        getSysConfigShare();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_iv:
                activity.finish();
                break;
            case R.id.link_share_rl://链接分享
                leftOrRight.set(!leftOrRight.get());
                setCheckText();
                type = 1;
                getSysConfigShare();
                break;
            case R.id.poster_share_rl://海报分享
                leftOrRight.set(!leftOrRight.get());
                setCheckText();
                leftCheckPosition = 0;
                type = 2;
                getSysConfigShare();
                break;
            case R.id.ll_left_friend://左边分享链接到微信好友
                String url = list.get(leftCheckPosition).getUrl() + "&p=" + TourApplication.getUser().getRefCode();
                ShareFunction.getInstance().shareWeb(activity, list.get(leftCheckPosition).getTitle(), list.get(leftCheckPosition).getImage(),
                        list.get(leftCheckPosition).getContent(), url,
                        ShareFunction.SHARE_WEIXIN);
                break;
            case R.id.ll_left_circle://左边分享链接到到朋友圈
                ShareFunction.getInstance().shareWeb(activity, list.get(leftCheckPosition).getTitle(), list.get(leftCheckPosition).getImage(),
                        list.get(leftCheckPosition).getContent(), list.get(leftCheckPosition).getUrl() + "&p=" + TourApplication.getUser().getRefCode(),
                        ShareFunction.SHARE_WEIXIN_CIRCLE);
                break;
            case R.id.ll_right_friend://右边分享微信好友
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Bitmap bitmap = Glide.with(activity).asBitmap().load(bannerList.get(activity.dataBinding.banner.getCurrentIndex())).submit().get();
                            String url = list.get(activity.dataBinding.banner.getCurrentIndex()).getUrl() + "&p=" + TourApplication.getUser().getRefCode();
                            EventBus.getDefault().post(new ShapeBitmapBean(bitmap, url,ShareFunction.SHARE_WEIXIN));
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.ll_image_load://右边下载图片
                DonwloadSaveImgUtils.donwloadImg(activity, bannerList.get(activity.dataBinding.banner.getCurrentIndex()));
                break;
            case R.id.ll_right_circle://右边分享到朋友圈
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Bitmap bitmap = Glide.with(activity).asBitmap().load(bannerList.get(activity.dataBinding.banner.getCurrentIndex())).submit().get();
                            String url = list.get(activity.dataBinding.banner.getCurrentIndex()).getUrl() + "&p=" + TourApplication.getUser().getRefCode();
                            EventBus.getDefault().post(new ShapeBitmapBean(bitmap, url,ShareFunction.SHARE_WEIXIN_CIRCLE));
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
        }
    }

    private void setCheckText() {
        if (leftOrRight.get()) {
            activity.dataBinding.linkShareTv.setTypeface(Typeface.MONOSPACE);
            activity.dataBinding.posterShareTv.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            activity.dataBinding.linkShareTv.setTypeface(Typeface.DEFAULT_BOLD);
            activity.dataBinding.posterShareTv.setTypeface(Typeface.MONOSPACE);
        }
    }

    /**
     * 获取邀请好友时的链接分享或者海报分享list
     */
    private void getSysConfigShare() {
        ApiRepertory.getInstance().getApiService().getShareInfo(type)
                .subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<List<ShareBean>>>(mView) {
            @Override
            public void onNext(BaseDataBean<List<ShareBean>> verificationCodeBean) {
                if (null != verificationCodeBean) {
                    if (verificationCodeBean.isSuccess()) {
                        if (type == 1) {
                            list.clear();
                            list.addAll(verificationCodeBean.getRel());
                            list.get(0).setCheck(true);
                            adapter.notifyDataSetChanged();
                        } else {
                            list.clear();
                            list.addAll(verificationCodeBean.getRel());
                            bannerList.clear();
                            for (ShareBean shareBean : verificationCodeBean.getRel()) {
                                bannerList.add(shareBean.getImage());
                            }
                            webBannerAdapter.setDatas(bannerList);
                            webBannerAdapter.notifyDataSetChanged();
                        }
                    } else {
                        ToastUtils.showText(activity, verificationCodeBean.getReMsg());
                    }
                }
            }
        });
    }
}
