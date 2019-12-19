package cn.xmzt.www.selfdrivingtools.activity;

import androidx.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ActivityScenicSpotDetailBinding;
import cn.xmzt.www.selfdrivingtools.adapter.ScenicSpotDetailNearbyAdapter;
import cn.xmzt.www.selfdrivingtools.bean.ScenicSpotDetailBean;
import cn.xmzt.www.selfdrivingtools.event.PlayVoiceTypeEvent;
import cn.xmzt.www.manager.IntentManager;
import cn.xmzt.www.selfdrivingtools.viewmodel.ScenicSpotDetailViewModel;
import cn.xmzt.www.hotel.GlideImageLoader;
import cn.xmzt.www.service.PlayerService;
import cn.xmzt.www.share.ShareFunction;
import cn.xmzt.www.utils.DensityUtil;
import cn.xmzt.www.utils.IntegerToTimeUtils;
import cn.xmzt.www.utils.IsFastClick;
import cn.xmzt.www.utils.NetWorkUtils;
import cn.xmzt.www.utils.StatusBarUtil;
import cn.xmzt.www.utils.ToastUtils;
import cn.xmzt.www.view.ExpandableMoreTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 *  scenic spot detail
 */
public class ScenicSpotDetailActivity extends TourBaseActivity<ScenicSpotDetailViewModel, ActivityScenicSpotDetailBinding> {

    private long mScenicId = 0;
    private int mScenicSpotId = 0;
    private ScenicSpotDetailBean mInfo = null;
    private ScenicSpotDetailNearbyAdapter mNearByAdapter = null;
    private int mPlayID = -2;
    private boolean mIsPlay = false;
    private String mPlayPath = "";
    private boolean mIsPlayService = false; // service is null
    Timer mLocationTimer = null;
    TimerTask timerTask = null;
    private boolean mIsStartTimer = false;
    IntegerToTimeUtils utils = null;
    private int mPlayLength = -1;
    private int mVoiceLength = -1;

    private String mScenicSpotName = "";
    private String mScenicSpotBannerUrl = "";
    private String mScenicSpotIntro = "";
    private String mScenicSpotPlay_Path = "";
    private double mLat = 0.0;
    private double mLng = 0.0;
    private String mFileVoicePath = "";

    private boolean mIsClickImages = false;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_scenic_spot_detail;
    }

    @Override
    protected ScenicSpotDetailViewModel createViewModel() {
        viewModel = new ScenicSpotDetailViewModel();
        viewModel.setIView(this);

        viewModel.mScenicSpotDetailInfo.observe(this, new Observer<ScenicSpotDetailBean>() {
            @Override
            public void onChanged(@Nullable ScenicSpotDetailBean scenicSpotDetailBean) {
                if (scenicSpotDetailBean != null) {
                    mInfo = scenicSpotDetailBean;
                    setData();
                }
            }
        });
        viewModel.mScenicSpotDetailErrorInfo.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                setErrorInfo();
            }
        });
        utils = new IntegerToTimeUtils();
        return viewModel;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsClickImages = false;
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);

        StatusBarUtil.setStatusBarLightMode(this,false);
        FrameLayout.LayoutParams listParams = (FrameLayout.LayoutParams) dataBinding.clTop.getLayoutParams();
        listParams.topMargin = StatusBarUtil.getStatusBarHeight(getApplicationContext());
        dataBinding.setModel(viewModel);
        dataBinding.setActivity(this);
        // 获取传递过来的景区id 或者不用传  直接选择
        mScenicId = getIntent().getLongExtra("scenicid",0);
        mScenicSpotId = getIntent().getIntExtra("scenicSpotId",0);
        mScenicSpotName = getIntent().getStringExtra("name");
        mScenicSpotBannerUrl = getIntent().getStringExtra("bannerurl");
        mScenicSpotIntro = getIntent().getStringExtra("intro");
        mScenicSpotPlay_Path = getIntent().getStringExtra("play_path");
        mFileVoicePath = getIntent().getStringExtra("filepath");
        // 获取上一个界面传递过来的经纬度信息
        mLat = getIntent().getDoubleExtra("lat",0.0);
        mLng = getIntent().getDoubleExtra("lng",0.0);

        // 滑动事件监听
        dataBinding.scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            int lastScrollY = 0;
            int h = DensityUtil.dip2px(ScenicSpotDetailActivity.this,44);
            int color = ContextCompat.getColor(ScenicSpotDetailActivity.this, R.color.color_FF_FF_FF) & 0x00ffffff;
            int mScrollY = 0;

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (lastScrollY < h) {
                    scrollY = Math.min(h, scrollY);
                    mScrollY = scrollY > h ? h : scrollY;
                    dataBinding.flTop.setBackgroundColor(((255 * mScrollY / h) << 24) | color);
                }
                float percent = mScrollY / h;
                if (percent >= 1) {
                    percent = 1;
                }
                if (percent <= 0.1) {
                    percent = 0;
                }
                if (percent > 0.5) {
                    StatusBarUtil.setStatusBarLightMode(ScenicSpotDetailActivity.this,true);
                    dataBinding.titleContentTv.setTextColor(ContextCompat.getColor(ScenicSpotDetailActivity.this,R.color.color_333333));
                    // 顶部白色
                    dataBinding.titleBackIv.setImageResource(R.drawable.back_gray);
                    dataBinding.titleShareIv.setImageResource(R.drawable.scenic_detail_share_icon_bg);
                    dataBinding.titleImageIv.setImageResource(R.drawable.scenic_detail_image_icon_bg);
                    // 不加粗
                    dataBinding.titleContentTv.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                } else {
                    StatusBarUtil.setStatusBarLightMode(ScenicSpotDetailActivity.this,false);
                    dataBinding.titleContentTv.setTextColor(ContextCompat.getColor(ScenicSpotDetailActivity.this,R.color.white));
                    // 顶部透明
                    dataBinding.titleBackIv.setImageResource(R.drawable.scenic_detail_back_icon);
                    dataBinding.titleShareIv.setImageResource(R.drawable.scenic_detail_share_icon);
                    dataBinding.titleImageIv.setImageResource(R.drawable.scenic_detail_image_icon);
                    // 加粗
                    dataBinding.titleContentTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                }
                lastScrollY = scrollY;
            }
        });
        dataBinding.clTop.setBackgroundColor(0);

        // 设置附近景点的数据
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        dataBinding.rvTourList.setLayoutManager(layoutManager);
        DividerItemDecoration decor = new DividerItemDecoration(this, layoutManager.getOrientation());
        decor.setDrawable(ContextCompat.getDrawable(this, R.drawable.item_ticket_detail_recyleview_line));
        dataBinding.rvTourList.addItemDecoration(decor);
        mNearByAdapter = new ScenicSpotDetailNearbyAdapter(this);
        dataBinding.rvTourList.setAdapter(mNearByAdapter);
        dataBinding.expandTextView.setOnExpandStateChangeListener(new ExpandableMoreTextView.OnExpandStateChangeListener() {
            @Override
            public void onExpandStateChanged(TextView textView, boolean isExpanded) {

            }
        });

        dataBinding.sbVoice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mIsPlayService){ // 表示服务不为空的情况
                    if (b){ // 为true时，表示用户操作
                        mIsSbVoice = true;
                    }
                    // 设置滑动控件的播放
                    if (mIsSbVoice) {
                        mIsSbVoice = false;
                        PlayerService.mMediaPlayer.seekTo(i);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        getServiceData();
    }

    /**
     * 获取服务器数据
     */
    private void getServiceData(){
        if (NetWorkUtils.isNetConnected(this)){
            viewModel.getScenicSpotData(mScenicSpotId);
        } else {

        }
    }

    private void initSetData(){
        // 判断当前播放音频 获取当前播放进去
        if (mScenicSpotId == Constants.mScenicSpotId){
            // 当前服务是否正在播放
            if (PlayerService.mMediaPlayer != null && Constants.mServiceIsStart){ // 至少传过来的时候在播放视频文件
                mIsPlayService = true;
                if (PlayerService.mMediaPlayer.isPlaying()){
                    mIsPlay = true; // 当前仍在播放
                } else {
                    mIsPlay = false; // 当前已经停止播放
                }
            }
            // 获取当前播放文件的长度
            if (mIsPlay){
                dataBinding.ivPlayImg.setImageResource(R.drawable.scenic_detail_playing_icon);
                // 音频的总长度
                mVoiceLength = PlayerService.mMediaPlayer.getDuration();
                // 音频的当前播放长度
                mPlayLength = PlayerService.mMediaPlayer.getCurrentPosition();
                dataBinding.sbVoice.setMax(mVoiceLength);
                mIsSbVoice = false;
                dataBinding.sbVoice.setProgress(mPlayLength);
                // 需要每隔一秒钟更新一次 设置一个timer
                setTimerPer1SecondsOptions();
                mHandler.sendEmptyMessage(1);
            } else {
                dataBinding.ivPlayImg.setImageResource(R.drawable.scenic_detail_play_icon);
                dataBinding.sbVoice.setMax(100);
                dataBinding.sbVoice.setProgress(0);
                mIsSbVoice = false;
                dataBinding.tvStatTime.setText("00:00");
                dataBinding.tvEndTime.setText("00:00");
            }
        } else {
            dataBinding.ivPlayImg.setImageResource(R.drawable.scenic_detail_play_icon);
            dataBinding.sbVoice.setMax(100);
            dataBinding.sbVoice.setProgress(0);
            mIsSbVoice = false;
            dataBinding.tvStatTime.setText("00:00");
            dataBinding.tvEndTime.setText("00:00");
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case cn.xmzt.www.R.id.title_back_iv:// 返回
                if (mIsPlayService) {
//                    Intent intent = new Intent(this, PlayerService.class);
//                    stopService(intent);
                    removeTimer();
                }
                finish();
                break;
            case cn.xmzt.www.R.id.title_image_iv:// 图集
                if (!mIsClickImages) {
                    mIsClickImages = true;
                    ArrayList<String> list = new ArrayList<>();
                    if (mInfo != null) {
                        if (mInfo.getAtlasLists() != null) {
                            for (ScenicSpotDetailBean.AtlasListsBean bean : mInfo.getAtlasLists()) {
                                list.add(bean.getUrl());
                            }
                        } else {
                            list.add(mScenicSpotBannerUrl);
                        }
                    } else {
                        list.add(mScenicSpotBannerUrl);
                    }
                    // 图集
                    IntentManager.getInstance().goAtlasActivity(ScenicSpotDetailActivity.this, list);
                }
                break;
            case cn.xmzt.www.R.id.title_share_iv:// 分享
                // 分享
                if (mInfo!=null) {
                    if (TourApplication.getUser() != null) {
                        String url = "https://g.xmzt.cn/g/g?t=4&i=" + mScenicId + "&p=" + TourApplication.getUser().getRefCode();
                        ShareFunction.getInstance().showWebShareAction(this, mInfo.getName(), mInfo.getPhotoUrl(),
                                mInfo.getDescription(), url, ShareFunction.SHARE_WEIXIN);
                    } else {
                        ToastUtils.showText(this, "用户未登录，分享失败");
                    }
                } else {
                    ToastUtils.showText(this,"获取景点信息失败，分享失败");
                }
                break;
            case cn.xmzt.www.R.id.iv_play_img:// 播放按钮
                if (IsFastClick.isFastClick()) {
                    setPlayVoiceData();
                }
                break;
            default:
                break;
        }
    }

    private void setData(){
        // 设置顶部title
        dataBinding.titleContentTv.setText(mInfo.getName());
        // 设置banner
        dataBinding.banner.setImageLoader(new GlideImageLoader());
        List<String> images = new ArrayList<>();
        if (mInfo.getAtlasLists()!= null) {
            if (mInfo.getAtlasLists().size()>0){
                for (int i = 0; i <mInfo.getAtlasLists().size();i++){
                    images.add(mInfo.getAtlasLists().get(i).getUrl());
                }
                dataBinding.banner.setImages(images);
                dataBinding.banner.start();
            }
        }
        // 设置文字
        dataBinding.expandTextView.setText(mInfo.getDescription());

        mNearByAdapter.setDatas(mInfo.getScenicSpotList());
        mPlayPath = mScenicSpotPlay_Path;
        mLat = mInfo.getLonLat().getLatitude();
        mLng = mInfo.getLonLat().getLongitude();

        initSetData();
    }

    private void setErrorInfo(){
        // 设置顶部title
        dataBinding.titleContentTv.setText(mScenicSpotName);
        // 设置banner
        dataBinding.banner.setImageLoader(new GlideImageLoader());
        List<String> images = new ArrayList<>();
        images.add(mScenicSpotBannerUrl);
        dataBinding.banner.setImages(images);
        dataBinding.banner.start();
        // 设置文字
        dataBinding.expandTextView.setText(mScenicSpotIntro);
        mPlayPath = mScenicSpotPlay_Path;
        initSetData();
    }

    /**
     * 附近景点的点击事件
     * @param position
     */
    public void OnNearByClickListener(int position){
        mScenicSpotId = mInfo.getScenicSpotList().get(position).getId();
        mPlayPath = mInfo.getScenicSpotList().get(position).getScenicResource().getResUrl();
        mScenicSpotName = mInfo.getScenicSpotList().get(position).getName();
        mScenicSpotBannerUrl = mInfo.getScenicSpotList().get(position).getPhotoUrl();
        mScenicSpotIntro = mInfo.getScenicSpotList().get(position).getDescription();
        mScenicSpotPlay_Path = mInfo.getScenicSpotList().get(position).getScenicResource().getResUrl();

        // 当前界面有的timer进行delete
        removeTimer();
        // 开始加载这一页的数据
        getServiceData();

        // 滑动到顶部并更改顶部样式显示
        dataBinding.scrollView.scrollTo(0,0);
        int h = DensityUtil.dip2px(ScenicSpotDetailActivity.this,44);
        int color = ContextCompat.getColor(ScenicSpotDetailActivity.this, R.color.color_FF_FF_FF) & 0x00ffffff;
        int mScrollY = 0;
        dataBinding.flTop.setBackgroundColor(((255 * mScrollY/h) << 24) | color);
        StatusBarUtil.setStatusBarLightMode(ScenicSpotDetailActivity.this,false);
        dataBinding.titleContentTv.setTextColor(ContextCompat.getColor(ScenicSpotDetailActivity.this,R.color.white));
        // 顶部透明
        dataBinding.titleBackIv.setImageResource(R.drawable.scenic_detail_back_icon);
        dataBinding.titleShareIv.setImageResource(R.drawable.scenic_detail_share_icon);
        dataBinding.titleImageIv.setImageResource(R.drawable.scenic_detail_image_icon);
        // 加粗
        dataBinding.titleContentTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }

    private void setPlayVoiceData(){
        String scenic = "";
        if (mInfo != null) {
            scenic = mInfo.getName();
        } else {
            scenic = mScenicSpotName;
        }
        if (mPlayPath.contains("https")){ // 表示是网络文件
            String path = mFileVoicePath + "/" + mScenicSpotId + ".mp3";
            File file = new File(path);
            if (file.exists()){
                mPlayPath = path;
            } else {
                // 判断当前有无网络
                if (!NetWorkUtils.isNetConnected(ScenicSpotDetailActivity.this)) {
                    ToastUtils.showText(ScenicSpotDetailActivity.this, "你当前播放的文件是网络文件，请打开您的网络。");
                    return;
                }
            }
        }
        String content = "正在为您播放“"+scenic+"”语音导游";
        Constants.mServiceIsStartConteng = content;
        if (Constants.mScenicId != mScenicId){
            Constants.mScenicId = mScenicId;
        }
        if (Constants.mScenicSpotId != mScenicSpotId){
            Constants.mScenicSpotId = mScenicSpotId;
        }
        if (Constants.mVoiceLatitude != mLat){
            Constants.mVoiceLatitude = mLat;
        }
        if (Constants.mVoiceLongitude != mLng){
            Constants.mVoiceLongitude = mLng;
        }
        if (mIsPlayService){ // 表示当前服务启动 并且播放的是当前自己界面上的音频文件
            if (PlayerService.mMediaPlayer.isPlaying()){
                Constants.mServiceIsStartConteng = "暂停 播放“" + scenic + "”语音导游";
                setPlayVoice(Constants.PlayerMag.PAUSE,mPlayPath); // 当前正在播放的时候暂停
                dataBinding.ivPlayImg.setImageResource(R.drawable.scenic_detail_play_icon);
            } else {
                setPlayVoice(Constants.PlayerMag.PLAY_MAG,mPlayPath); // 当前暂停 正在播放
                dataBinding.ivPlayImg.setImageResource(R.drawable.scenic_detail_playing_icon);
            }
        } else { // 没有播放的情况下 播放
            setPlayVoice(Constants.PlayerMag.PLAY_MAG,mPlayPath); // 当前暂停 正在播放
            dataBinding.ivPlayImg.setImageResource(R.drawable.scenic_detail_playing_icon);
        }
    }

    private void setPlayVoice(int action,String path){
        if ("".equals(path) || path == null){
            ToastUtils.showText(ScenicSpotDetailActivity.this,"获取音频文件失败");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("MSG", action);
        intent.putExtra("path", path);
        intent.setClass(ScenicSpotDetailActivity.this, PlayerService.class);
        startService(intent);
        mIsPlayService = true;
        if (!mIsStartTimer){
            setTimerPer1SecondsOptions();
        }
        mIsPlay = true;
        if (mInfo != null) {
            mPlayID = mInfo.getId();
        } else {
            mPlayID = mScenicSpotId;
        }
    }

    /**
     * 每5秒获取一次定位操作
     */
    private void setTimerPer1SecondsOptions(){
        mIsStartTimer = true;
        // 开始的时候设置开始时间和结束时间

        mLocationTimer = new Timer();
        mLocationTimer.schedule(timerTask = new TimerTask() {
            @Override
            public void run() {
                if (PlayerService.mMediaPlayer != null) { // 播放控件不为空
                    if (PlayerService.mMediaPlayer.isPlaying()) {
                        // 音频的总长度
                        mVoiceLength = PlayerService.mMediaPlayer.getDuration();
                        // 音频的当前播放长度
                        mPlayLength = PlayerService.mMediaPlayer.getCurrentPosition();
                        dataBinding.sbVoice.setMax(mVoiceLength);
                        mIsSbVoice = false;
                        dataBinding.sbVoice.setProgress(mPlayLength);
                        mHandler.sendEmptyMessage(1);
                    }
                }
            }
        },500,1000); // 0.5秒之后 每隔1秒更新一次进度条
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1 : // 更新界面数据
                    dataBinding.tvStatTime.setText(utils.stringForTime(mPlayLength));
                    dataBinding.tvEndTime.setText(utils.stringForTime(mVoiceLength));
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(1);
        EventBus.getDefault().unregister(this);

        removeTimer();
    }

    /**
     * 移出掉当前界面的timer
     */
    private void removeTimer(){
        if (timerTask != null){
            timerTask.cancel();
            timerTask = null;
        }
        if (mLocationTimer != null){
            mLocationTimer.cancel();
            mLocationTimer.purge();
            mLocationTimer = null;
        }
        mIsStartTimer = false;
        mIsPlayService = false;
        mIsSbVoice = false;
    }
    private boolean mIsSbVoice = false;

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getOrderType(PlayVoiceTypeEvent event) {
        if (event.getType() == 0){ // 表示停止播放
            dataBinding.ivPlayImg.setImageResource(R.drawable.scenic_detail_play_icon);
            mIsSbVoice = false;
            dataBinding.sbVoice.setProgress(0);
            dataBinding.tvStatTime.setText("00:00");
        } else if (event.getType() == 1){ // 表示开始播放数据
            dataBinding.ivPlayImg.setImageResource(R.drawable.scenic_detail_playing_icon);
        } else if (event.getType() == 2) { // 表示暂停
            dataBinding.ivPlayImg.setImageResource(R.drawable.scenic_detail_play_icon);
        } else if (event.getType() == 5){ // 播放完之后的操作
            dataBinding.ivPlayImg.setImageResource(R.drawable.scenic_detail_play_icon);
            mIsSbVoice = false;
            dataBinding.sbVoice.setProgress(0);
            dataBinding.tvStatTime.setText("00:00");
        }
    }
}