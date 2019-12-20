package cn.xmzt.www.home.vm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.home.activity.PopularityMustPlayActivity;
import cn.xmzt.www.home.bean.MustPlayBean;
import cn.xmzt.www.home.bean.PopularityBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.route.activity.RouteDetailActivity1;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.BitmapUtils;
import cn.xmzt.www.utils.ToastUtils;
import cn.xmzt.www.view.banner.BannerLayout;
import cn.xmzt.www.view.banner.MustPalyBannerAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.ObservableField;

/**
 * @author tanlei
 * @date 2019/8/22
 * @describe
 */

public class PopularityMustPlayViewModel extends BaseViewModel {
    private PopularityMustPlayActivity activity;
    private int pageNum = 1;
    private int pageSize = 20;
    private List<String> bannerList = new ArrayList<>();
    private MustPalyBannerAdapter webBannerAdapter;
    private List<MustPlayBean> list;
    /**
     * 景点名称
     */
    public ObservableField<String> name = new ObservableField<>();
    /**
     * 景点介绍
     */
    public ObservableField<String> details = new ObservableField<>();
    /**
     * 景点价格
     */
    public ObservableField<String> price = new ObservableField<>();
    /**
     * 景点类型
     */
    public ObservableField<String> lineTypeStr = new ObservableField<>();

    private PopularityBean bean;

    public void setBean(PopularityBean bean) {
        this.bean = bean;
        if (bean.getType() == 1) {
            activity.dataBinding.tvDel.setText("热门出发地");
            activity.dataBinding.tvSubDel.setText("从这里出发开始奇妙自驾游之路");
        } else {
            activity.dataBinding.tvDel.setText("热门目的地");
            activity.dataBinding.tvSubDel.setText("大家都爱去的那些城市");
        }
        getPopularityList();
    }

    public void setActivity(PopularityMustPlayActivity activity) {
        this.activity = activity;
        webBannerAdapter = new MustPalyBannerAdapter(activity, bannerList);
        activity.dataBinding.banner.setAdapter(webBannerAdapter);
        webBannerAdapter.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent mIntent = new Intent(activity, RouteDetailActivity1.class);
                mIntent.putExtra("A", list.get(position).getId());
                mIntent.putExtra("B", list.get(position).getLineName());
                activity.startActivity(mIntent);
            }
        });
    }

    public void setCheckData(int position) {
        name.set(list.get(position).getLineName());
        details.set(list.get(position).getLanguage());
        price.set(list.get(position).getMinPrice() + "");
        lineTypeStr.set(list.get(position).getLineType() == 1 ? "自由出行" : "跟团出行");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapUtils.GetUrlBitmap(list.get(position).getPhotoUrl(), 5);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (bitmap != null) {
                            activity.dataBinding.ivBg.setImageBitmap(bitmap);
                        }
                    }
                });
            }
        }).start();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_iv:
                activity.finish();
                break;
        }
    }

    private void getPopularityList() {
        ApiRepertory.getInstance().getApiService().getPopularityList(this.bean.getCitycode(), pageNum, pageSize, this.bean.getType())
                .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<List<MustPlayBean>>>(mView) {
            @Override
            public void onNext(BaseDataBean<List<MustPlayBean>> verificationCodeBean) {
                if (null != verificationCodeBean) {
                    if (verificationCodeBean.isSuccess()) {
                        PopularityMustPlayViewModel.this.list = verificationCodeBean.getRel();
                        for (MustPlayBean mustPlayBean : verificationCodeBean.getRel()) {
                            bannerList.add(mustPlayBean.getPhotoUrl());
                        }
                        if (list.size() > 0) {
                            setCheckData(0);
                        }
                        webBannerAdapter.setDatas(bannerList);
                        webBannerAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.showText(activity, verificationCodeBean.getReMsg());
                    }
                }

            }
        });
    }
}
