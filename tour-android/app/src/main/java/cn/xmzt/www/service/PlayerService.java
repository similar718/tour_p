package cn.xmzt.www.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.text.TextUtils;

import cn.xmzt.www.config.Constants;
import cn.xmzt.www.selfdrivingtools.event.PlayVoiceTypeEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

public class PlayerService extends Service implements Runnable,
        MediaPlayer.OnCompletionListener {
    /* 定于一个多媒体对象 */
    public static MediaPlayer mMediaPlayer = null;
    // 是否单曲循环
//    private static boolean isLoop = false;
    // 用户操作
    private int MSG;
    // WiFi锁
    private WifiManager.WifiLock wifiLock;
    AudioManager audioManager;

    private MyBinder mBinder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mMediaPlayer = new MediaPlayer();
        /* 监听播放是否完成 */
        mMediaPlayer.setOnCompletionListener(this);

        // 设置音量，参数分别表示左右声道声音大小，取值范围为0~1
//        mMediaPlayer.setVolume(1.0f, 1.0f);

        // 设置设备进入锁状态模式-可在后台播放或者缓冲音乐-CPU一直工作
//        mMediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
        // 当播放的时候一直让屏幕变亮
//        mMediaPlayer.setScreenOnWhilePlaying(true);

        // 如果你使用wifi播放流媒体，你还需要持有wifi锁
//        wifiLock = ((WifiManager) getApplicationContext()
//                .getSystemService(Context.WIFI_SERVICE))
//                .createWifiLock(WifiManager.WIFI_MODE_FULL, "wifilock");
//        wifiLock.acquire();

        // 处理音频焦点-处理多个程序会来竞争音频输出设备
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        // 设置播放错误监听
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                mediaPlayer.reset();
                Constants.mServiceIsStart = false;
                EventBus.getDefault().post(new PlayVoiceTypeEvent(0));
                return false;
            }
        });

        // 异步准备Prepared完成监听
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // 开始播放
                mMediaPlayer.start();
                Constants.mServiceIsStart = true;
                EventBus.getDefault().post(new PlayVoiceTypeEvent(1));
            }
        });
    }

    private void registerAudioFocusRequest(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 征对于Android 8.0
            AudioFocusRequest audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK).setOnAudioFocusChangeListener(focusChangeListener).build();
            audioFocusRequest.acceptsDelayedFocusGain();
            audioManager.requestAudioFocus(audioFocusRequest);
        } else {
            // 小于Android 8.0
            int result = audioManager.requestAudioFocus(focusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
            if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                // could not get audio focus.
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            Constants.mServiceIsStart = false;
            EventBus.getDefault().post(new PlayVoiceTypeEvent(0));
        }
        EventBus.getDefault().unregister(this);
        System.out.println("service onDestroy");
    }
    /*启动service时执行的方法*/
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*得到从startService传来的动作，后是默认参数，这里是我自定义的常量*/
        if (intent == null){
            return super.onStartCommand(intent, flags, startId);
        }
        MSG = intent.getIntExtra("MSG", Constants.PlayerMag.PLAY_MAG);
        // 当前需要播放的文件路径
        String path = intent.getStringExtra("path");
        if (MSG == Constants.PlayerMag.PLAY_MAG) {
            playMusic(path);
        }
        if (MSG == Constants.PlayerMag.PAUSE) {
            if (mMediaPlayer.isPlaying()) {// 正在播放
                mMediaPlayer.pause();// 暂停
                Constants.mServiceIsStart = false;
                EventBus.getDefault().post(new PlayVoiceTypeEvent(2));
            } else {// 没有播放
                mMediaPlayer.start();
                Constants.mServiceIsStart = true;
                EventBus.getDefault().post(new PlayVoiceTypeEvent(1));
            }
        }
        if (MSG == Constants.PlayerMag.STOP){
            mMediaPlayer.pause();
            Constants.mServiceIsStart = false;
            //  清除数据
            playMusic("");
            EventBus.getDefault().post(new PlayVoiceTypeEvent(0));
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressWarnings("static-access")
    public void playMusic(String path) {
        try {
            if (!TextUtils.isEmpty(path)) {
                registerAudioFocusRequest();
            } else {
                audioManager.abandonAudioFocus(focusChangeListener);
            }
            mMediaPlayer.setVolume(0.5f, 0.5f);
            /* 重置多媒体 */
            mMediaPlayer.reset();
            /* 读取mp3文件 获取文件路径 */
            mMediaPlayer.setDataSource(path);
            /* 准备播放 */
            mMediaPlayer.prepare();
            /* 开始播放 */
            mMediaPlayer.start();
            Constants.mServiceIsStart = true;
            EventBus.getDefault().post(new PlayVoiceTypeEvent(1));
            /* 是否单曲循环 */
//            mMediaPlayer.setLooping(isLoop);
            new Thread(this).start();
        } catch (IOException e) {
            audioManager.abandonAudioFocus(focusChangeListener);
        } catch (Exception e){
            audioManager.abandonAudioFocus(focusChangeListener);
        }
    }

    // 刷新进度条
    @Override
    public void run() {
        int CurrentPosition = 0;// 设置默认进度条当前位置
        int total = mMediaPlayer.getDuration();//
        while (mMediaPlayer != null && CurrentPosition < total) {
            try {
                Thread.sleep(1000);
                if (mMediaPlayer != null) {
                    CurrentPosition = mMediaPlayer.getCurrentPosition();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        /* 播放完当前歌曲，自动播放下一首 */
        mMediaPlayer.pause();
        Constants.mServiceIsStart = false;
        EventBus.getDefault().post(new PlayVoiceTypeEvent(0));
        EventBus.getDefault().post(new PlayVoiceTypeEvent(5));
        //  清除数据
        playMusic("");
    }

    /**
     * 监听当前播放音频的时候出现的情况
     */
    AudioManager.OnAudioFocusChangeListener focusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    // 获取audio focus
                    if (mMediaPlayer == null) {
                        mMediaPlayer = new MediaPlayer();
                    } else if (!mMediaPlayer.isPlaying()) {
//                        mMediaPlayer.start();
//                        Constants.mServiceIsStart = true;
//                        EventBus.getDefault().post(new PlayVoiceTypeEvent(1));
//                        mMediaPlayer.setVolume(1.0f, 1.0f);
                    } else if (mMediaPlayer.isPlaying()){
                        mMediaPlayer.setVolume(0.5f, 0.5f);
                    }
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    // 失去audio focus很长一段时间，必须停止所有的audio播放，清理资源
                    if (mMediaPlayer != null) {
                        if (mMediaPlayer.isPlaying()) {
                            mMediaPlayer.pause();
                        }
                    }
//                    Constants.mServiceIsStart = false;
//                    EventBus.getDefault().post(new PlayVoiceTypeEvent(0));
//                    mMediaPlayer.release();
//                    mMediaPlayer = null;
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    // 暂时失去audio focus，但是很快就会重新获得，在此状态应该暂停所有音频播放，但是不能清除资源
                    if (mMediaPlayer != null) {
                        if (mMediaPlayer.isPlaying()) {
                            mMediaPlayer.pause();
                        }
                    }
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    // 暂时失去 audio focus，但是允许持续播放音频(以很小的声音)，不需要完全停止播放。
                    if (mMediaPlayer != null) {
                        if (mMediaPlayer.isPlaying()) {
                            mMediaPlayer.setVolume(0.1f, 0.1f);
                        }
                    }
                    break;
            }
        }
    };

    public class MyBinder extends Binder {

        /**
         * 播放音乐
         */
        public void playMusic() {
            if (!mMediaPlayer.isPlaying()) {
                //如果还没开始播放，就开始
                mMediaPlayer.start();
            }
        }

        /**
         * 暂停播放
         */
        public void pauseMusic() {
            if (mMediaPlayer.isPlaying()) {
                //如果还没开始播放，就开始
                mMediaPlayer.pause();
            }
        }

        /**
         * reset
         */
        public void resetMusic() {
            if (!mMediaPlayer.isPlaying()) {
                //如果还没开始播放，就开始
                mMediaPlayer.reset();
            }
        }

        /**
         * 关闭播放器
         */
        public void closeMedia() {
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            }
        }

        /**
         * 获取歌曲长度
         **/
        public int getProgress() {
            return mMediaPlayer.getDuration();
        }

        /**
         * 获取播放位置
         */
        public int getPlayPosition() {
            return mMediaPlayer.getCurrentPosition();
        }
        /**
         * 播放指定位置
         */
        public void seekToPositon(int msec) {
            mMediaPlayer.seekTo(msec);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getOrderType(PlayVoiceTypeEvent event) {
        if (event.getType() == 0){ // 表示停止播放

        } else if (event.getType() == 1){ // 表示开始播放数据

        } else if (event.getType() == 2){ // 表示当前是暂停的状态

        }
    }
}
