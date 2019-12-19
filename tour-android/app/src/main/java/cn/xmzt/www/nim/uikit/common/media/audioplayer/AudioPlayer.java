//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.xmzt.www.nim.uikit.common.media.audioplayer;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.netease.nimlib.k.b;
import com.netease.nimlib.sdk.media.player.OnPlayListener;

import java.io.File;

public final class AudioPlayer {
    public static final String TAG = "AudioPlayer";
    private MediaPlayer mPlayer;
    private String mAudioFile;
    private long mIntervalTime;
    private AudioManager audioManager;
    private OnPlayListener mListener;
    private int audioStreamType;
    private static final int WHAT_COUNT_PLAY = 0;
    private static final int WHAT_DECODE_SUCCEED = 1;
    private static final int WHAT_DECODE_FAILED = 2;
    private Handler mHandler;

    public AudioPlayer(Context var1) {
        this(var1, (String)null, (OnPlayListener)null);
    }

    public AudioPlayer(Context var1, String var2, OnPlayListener var3) {
        this.mIntervalTime = 500L;
        this.audioStreamType = 0;
        this.mHandler = new Handler() {
            public void handleMessage(Message var1) {
                switch(var1.what) {
                case 0:
                    if (AudioPlayer.this.mListener != null) {
                        AudioPlayer.this.mListener.onPlaying((long)AudioPlayer.this.mPlayer.getCurrentPosition());
                    }

                    this.sendEmptyMessageDelayed(0, AudioPlayer.this.mIntervalTime);
                    return;
                case 1:
                    AudioPlayer.this.startInner();
                default:
                    return;
                case 2:
                    b.e("AudioPlayer", "convert() error: " + AudioPlayer.this.mAudioFile);
                }
            }
        };
        this.audioManager = (AudioManager)var1.getSystemService(Context.AUDIO_SERVICE);
        this.mAudioFile = var2;
        this.mListener = var3;
    }
    OnAudioFocusChangeListener onAudioFocusChangeListener = new OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            switch(focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    if (AudioPlayer.this.isPlaying()) {
                        AudioPlayer.this.mPlayer.setVolume(0.1F, 0.1F);
                    }
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    AudioPlayer.this.stop();
                    return;
                case AudioManager.AUDIOFOCUS_LOSS:{}
                AudioPlayer.this.stop();
                return;
                case AudioManager.AUDIOFOCUS_GAIN:
                    if (AudioPlayer.this.isPlaying()) {
                        AudioPlayer.this.mPlayer.setVolume(1.0F, 1.0F);
                        return;
                    }
                default:
                    break;
            }
        }
    };
    private AudioFocusRequest mFocusRequest;
    private AudioAttributes mAudioAttributes;
    private boolean isFocusRequest = false;
    /**
     * 注册音频焦点.
     *  durationHint  AUDIOFOCUS_GAIN_TRANSIENT 暂时的焦点，很快就会放弃专注
     *                     AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK 暂时的焦点，以前的焦点所有者继续播放，声音变小
     *                     AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE  这得益于系统不播放干扰性的声音
     *                     AUDIOFOCUS_GAIN  来处理一个持续时间未知的焦点请求 播放歌曲或视频。
     *                     AUDIOFOCUS_REQUEST_FAILED
     * @return  AUDIOFOCUS_REQUEST_GRANTED：申请成功；AUDIOFOCUS_REQUEST_FAILED：申请失败；
     */
    public int requestAudioFocus() {
        int result = 0;
        if(!isFocusRequest){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (mFocusRequest == null) {
                    if (mAudioAttributes == null) {
                        mAudioAttributes = new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build();
                    }
                    mFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
                            .setAudioAttributes(mAudioAttributes)
                            .setWillPauseWhenDucked(true)
                            .setOnAudioFocusChangeListener(onAudioFocusChangeListener)
                            .build();
                }
                result = this.audioManager.requestAudioFocus(mFocusRequest);
            } else {
                result = this.audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
            }
            if(result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                isFocusRequest=true;
            }else {
                isFocusRequest=false;
            }
        }
        return result;
    }
    public final void setDataSource(String var1) {
        if (!TextUtils.equals(var1, this.mAudioFile)) {
            b.n("start play audio file" + var1);
            this.mAudioFile = var1;
        }

    }

    public final void setOnPlayListener(OnPlayListener var1) {
        this.mListener = var1;
    }

    public final OnPlayListener getOnPlayListener() {
        return this.mListener;
    }

    public final void start(int var1) {
        this.audioStreamType = var1;
        this.startPlay();
    }

    public final void stop() {
        if (this.mPlayer != null) {
            this.endPlay();
            if (this.mListener != null) {
                this.mListener.onInterrupt();
            }
        }

    }

    public final boolean isPlaying() {
        return this.mPlayer != null && this.mPlayer.isPlaying();
    }

    public final long getDuration() {
        return this.mPlayer != null ? (long)this.mPlayer.getDuration() : 0L;
    }

    public final long getCurrentPosition() {
        return this.mPlayer != null ? (long)this.mPlayer.getCurrentPosition() : 0L;
    }

    public final void seekTo(int var1) {
        this.mPlayer.seekTo(var1);
    }

    private void startPlay() {
        b.b("AudioPlayer", "start() called");
        this.endPlay();
        this.startInner();
    }

    private void endPlay() {
        this.audioManager.abandonAudioFocus(this.onAudioFocusChangeListener);
        if (this.mPlayer != null) {
            this.mPlayer.stop();
            this.mPlayer.release();
            this.mPlayer = null;
            this.mHandler.removeMessages(0);
        }

    }

    private void startInner() {
        this.mPlayer = new MediaPlayer();
        this.mPlayer.setLooping(false);
        this.mPlayer.setAudioStreamType(this.audioStreamType);
        Log.d("TAG","audioStreamType="+audioStreamType);
       /* if (this.audioStreamType == 3) {
            this.audioManager.setSpeakerphoneOn(true);
        } else {
            this.audioManager.setSpeakerphoneOn(false);
        }*/
        requestAudioFocus();
//        this.audioManager.requestAudioFocus(this.onAudioFocusChangeListener, this.audioStreamType, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
        this.mPlayer.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer var1) {
                b.b("AudioPlayer", "player:onPrepared");
                AudioPlayer.this.mHandler.sendEmptyMessage(0);
                if (AudioPlayer.this.mListener != null) {
                    AudioPlayer.this.mListener.onPrepared();
                }

            }
        });
        this.mPlayer.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer var1) {
                b.b("AudioPlayer", "player:onCompletion");
                AudioPlayer.this.endPlay();
                if (AudioPlayer.this.mListener != null) {
                    AudioPlayer.this.mListener.onCompletion();
                }

            }
        });
        this.mPlayer.setOnErrorListener(new OnErrorListener() {
            public boolean onError(MediaPlayer var1, int var2, int var3) {
                b.e("AudioPlayer", String.format("player:onOnError what:%d extra:%d", var2, var3));
                AudioPlayer.this.endPlay();
                if (AudioPlayer.this.mListener != null) {
                    AudioPlayer.this.mListener.onError(String.format("OnErrorListener what:%d extra:%d", var2, var3));
                }

                return true;
            }
        });

        try {
            if (this.mAudioFile != null) {
                this.mPlayer.setDataSource(this.mAudioFile);
                this.mPlayer.prepare();
                this.mPlayer.start();
                b.b("AudioPlayer", "player:start ok---->" + this.mAudioFile);
            } else {
                if (this.mListener != null) {
                    this.mListener.onError("no datasource");
                }

            }
        } catch (Exception var2) {
            var2.printStackTrace();
            b.e("AudioPlayer", "player:onOnError Exception\n" + var2.toString());
            this.endPlay();
            if (this.mListener != null) {
                this.mListener.onError("Exception\n" + var2.toString());
            }

        }
    }

    private void deleteOnExit() {
        File var1;
        if ((var1 = new File(this.mAudioFile)).exists()) {
            var1.deleteOnExit();
        }

    }
}
