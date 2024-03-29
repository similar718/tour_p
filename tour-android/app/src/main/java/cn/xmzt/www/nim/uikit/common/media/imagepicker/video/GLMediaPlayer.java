package cn.xmzt.www.nim.uikit.common.media.imagepicker.video;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.Surface;

import cn.xmzt.www.intercom.common.util.log.LogUtil;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * http://developer.android.com/intl/zh-cn/reference/android/media/MediaPlayer.html
 * status
 */
public class GLMediaPlayer {

    private static final String TAG = "YXMediaPlayer";

    private List<PlayerObserver> observers = new LinkedList<>();

    private int pendingSeek;

    private Handler handler;

    private MediaPlayer mPlayer;

    private int playerWidth;

    private int playerHeight;


    private Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            handler.removeCallbacks(progressRunnable);

            int current = getCurrentPosition();
            int duration = getDuration();

            if (mStatus == PlayerState.PLAYER_STARTED){
                notifyProgress(current, duration);
                handler.postDelayed(progressRunnable, 500);
            }
        }
    };

    public GLMediaPlayer(Handler mainHandler) {
        this.handler = mainHandler;
    }

    // in ms
    public void pendingSeek(int position) {
        pendingSeek = position;
    }

    public interface PlayerState {
        int PLAYER_INIT                = -1;
        int PLAYER_ERROR               = 0;
        int PLAYER_IDLE                = 1 << 0; // 1
        int PLAYER_INITIALIZED         = 1 << 1; // 2
        int PLAYER_PREPARING           = 1 << 2; // 4
        int PLAYER_PREPARED            = 1 << 3; // 8
        int PLAYER_STARTED             = 1 << 4; // 16
        int PLAYER_SEEKING             = 1 << 5; // 32
        int PLAYER_PAUSED              = 1 << 6; // 64
        int PLAYER_STOPPED             = 1 << 7; // 128
        int PLAYER_PLAYBACK_COMPLETE   = 1 << 8; // 256
        int PLAYER_PENDING_DESTROY     = 1 << 9; // 512
    }

    private int mStatus = PlayerState.PLAYER_INIT;

    public void create(){
        mStatus = PlayerState.PLAYER_INIT;
        mPlayer = new MediaPlayer();
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mStatus == PlayerState.PLAYER_PREPARING){
                    if (pendingSeek != 0){
                        seekTo(pendingSeek);
                    } else {
                        mStatus = PlayerState.PLAYER_PREPARED;
                        notifyChanged();
                        start();
                    }

                } else if (mStatus == PlayerState.PLAYER_PENDING_DESTROY){
                    destroy();
                }
            }
        });

        mPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                if (width == 0 || height == 0) {
                    return;
                }

                if (playerWidth != width || playerHeight != height){
                    playerWidth = width;
                    playerHeight = height;
                    notifySizeChanged(playerWidth, playerHeight);
                }
            }
        });

        mPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {

            }
        });

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mStatus = PlayerState.PLAYER_PLAYBACK_COMPLETE;
                notifyChanged();
            }
        });

        mPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                if (mStatus == PlayerState.PLAYER_PREPARING){
                    pendingSeek = 0;
                    mStatus = PlayerState.PLAYER_PREPARED;
                    notifyChanged();
                    start();
                } else if (mStatus == PlayerState.PLAYER_PENDING_DESTROY){
                    destroy();
                }
            }
        });

        mPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                LogUtil.i(TAG, "onInfo:" + what + ", extra:" + extra);
                return false;
            }
        });

        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                LogUtil.i(TAG, "onError:" + what + ", extra:" + extra);

                if (mStatus == PlayerState.PLAYER_PENDING_DESTROY){
                    return true;
                }

                if (mStatus != PlayerState.PLAYER_ERROR){
                    mStatus = PlayerState.PLAYER_ERROR;
                    notifyChanged();
                }

                return true;
            }
        });

    }

    private void seekTo(int pendingSeek) {
        if (mPlayer == null) return;

        if (mStatus == PlayerState.PLAYER_PREPARING){
            mPlayer.seekTo(pendingSeek);
        }
    }

    public void setSurface(Surface surface){
        if (getPlayer() != null){
            getPlayer().setSurface(surface);
        }
    }

    public void setDataSource(Context context, Uri uri) {
        if (mStatus == PlayerState.PLAYER_IDLE){
            try {
                mPlayer.setDataSource(context, uri);
                mStatus = PlayerState.PLAYER_INITIALIZED;
                notifyChanged();
            } catch (IOException e) {
                e.printStackTrace();
                mStatus = PlayerState.PLAYER_ERROR;
                notifyChanged();
            }

        }
    }

    public void reset(){
        if (mPlayer == null) return;

        if (mStatus == PlayerState.PLAYER_STOPPED || mStatus == PlayerState.PLAYER_ERROR || mStatus == PlayerState.PLAYER_INIT){
            mPlayer.reset();
            mStatus = PlayerState.PLAYER_IDLE;
            notifyChanged();
        }
    }

    public MediaPlayer getPlayer() {
        return mPlayer;
    }

    public void prepare() {
        if (mPlayer == null) return;

        if (mStatus == PlayerState.PLAYER_INITIALIZED || mStatus == PlayerState.PLAYER_STOPPED){
            mPlayer.prepareAsync();
            mStatus = PlayerState.PLAYER_PREPARING;
            notifyChanged();
        }
    }

    public void stop(){
        if (mPlayer == null) return;

        if (mStatus == PlayerState.PLAYER_PREPARING){
            pendingDestroy();
            return;
        }

        if (mStatus == PlayerState.PLAYER_PREPARED || mStatus == PlayerState.PLAYER_STARTED || mStatus == PlayerState.PLAYER_PAUSED || mStatus == PlayerState.PLAYER_PLAYBACK_COMPLETE){
            mPlayer.stop();
            mStatus = PlayerState.PLAYER_STOPPED;
            notifyChanged();
        }
    }

    public void pause(){
        if (mPlayer == null) return;

        if (mStatus == PlayerState.PLAYER_STARTED) {
            mPlayer.pause();
            mStatus = PlayerState.PLAYER_PAUSED;
            notifyChanged();
        }
    }

    public void start(){
        if (mPlayer == null) return;

        if (mStatus == PlayerState.PLAYER_PREPARED || mStatus == PlayerState.PLAYER_PAUSED || mStatus == PlayerState.PLAYER_PLAYBACK_COMPLETE){
            mPlayer.start();
            mStatus = PlayerState.PLAYER_STARTED;
            notifyChanged();

            progressRunnable.run();
        }

    }

    public void pendingDestroy(){
        mStatus = PlayerState.PLAYER_PENDING_DESTROY;
        notifyChanged();
    }

    public void destroy() {
        pause();
        stop();
        if (mPlayer != null) {
            mPlayer.setSurface(null);
            mPlayer.release();
        }
        mPlayer = null;
        mStatus = PlayerState.PLAYER_INIT;
    }

    private int getCurrentPosition(){
        if (mPlayer == null){
            return 0;
        }

        if (mStatus == PlayerState.PLAYER_STARTED || mStatus == PlayerState.PLAYER_PAUSED || mStatus == PlayerState.PLAYER_STOPPED || mStatus == PlayerState.PLAYER_PREPARED || mStatus == PlayerState.PLAYER_PLAYBACK_COMPLETE){
            return mPlayer.getCurrentPosition();
        }

        return 0;
    }

    private int getDuration(){
        if (mPlayer == null){
            return 0;
        }

        if (mStatus == PlayerState.PLAYER_STARTED || mStatus == PlayerState.PLAYER_PAUSED || mStatus == PlayerState.PLAYER_STOPPED || mStatus == PlayerState.PLAYER_PREPARED || mStatus == PlayerState.PLAYER_PLAYBACK_COMPLETE){
            return mPlayer.getDuration();
        }

        return 0;
    }

    public void notifyChanged(){
        for (PlayerObserver playerObserver : observers){
            playerObserver.onPlayStateChanged(mStatus);
        }
    }

    public void notifyProgress(int current, int duration){
        for (PlayerObserver playerObserver : observers){
            playerObserver.onPlayProgressChanged(current, duration);
        }
    }

    public void notifySizeChanged(int width, int height){
        for (PlayerObserver playerObserver : observers){
            playerObserver.onPlaySizeChanged(width, height);
        }
    }

    public void addObserver(PlayerObserver observer){
        observers.add(observer);
    }

    public void removeObserver(PlayerObserver observer){
        observers.remove(observer);
    }

    public void removeObservers(){
        observers.clear();
    }

    public interface PlayerObserver {
        void onPlaySizeChanged(int width, int height);
        void onPlayStateChanged(int state);
        void onPlayProgressChanged(int current, int duration);
    }
}
