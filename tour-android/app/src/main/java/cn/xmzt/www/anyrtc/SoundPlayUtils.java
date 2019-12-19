package cn.xmzt.www.anyrtc;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourApplication;


/**
 * Created by Averysk
 */

public class SoundPlayUtils {

    private SoundPlayUtils() {
        if (mSoundPlayer == null) {
            if (Build.VERSION.SDK_INT >= 21) {
                SoundPool.Builder builder = new SoundPool.Builder();
                //传入最多播放音频数量,
                builder.setMaxStreams(1);
                //AudioAttributes是一个封装音频各种属性的方法
                AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
                //设置音频流的合适的属性
                attrBuilder.setLegacyStreamType(AudioManager.STREAM_VOICE_CALL);
                //加载一个AudioAttributes
                builder.setAudioAttributes(attrBuilder.build());
                mSoundPlayer = builder.build();
            } else {
                /**
                 * 第一个参数：int maxStreams：SoundPool对象的最大并发流数
                 * 第二个参数：int streamType：AudioManager中描述的音频流类型
                 *第三个参数：int srcQuality：采样率转换器的质量。 目前没有效果。 使用0作为默认值。
                 */
                mSoundPlayer = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
            }
        }
    }
    public void init(){

        if(burst_release_voiceId==0){
            burst_release_voiceId = mSoundPlayer.load(TourApplication.context, R.raw.burst_release, 1);//对讲录音结束的提示声音
        }
        if(burst_alarm_voiceId==0){
            burst_alarm_voiceId = mSoundPlayer.load(TourApplication.context, R.raw.burst_alarm, 1);//录音开始的提示声音
        }
        if(burst_talk_voiceId==0){
            burst_talk_voiceId = mSoundPlayer.load(TourApplication.context, R.raw.burst_talk, 1);//对讲的提示声音
        }
    }
    public static SoundPlayUtils getInstance() {
        return SoundPlayUtils.InstanceHolder.instance;
    }

    private static class InstanceHolder {
        private final static SoundPlayUtils instance = new SoundPlayUtils();
    }
    // SoundPool对象
    private SoundPool mSoundPlayer = null;

    private int streamID;
    public int burst_talk_voiceId;//对讲开始的提示声音
    public int burst_release_voiceId;//对讲录音结束的提示声音
    public int burst_alarm_voiceId;//录音开始的提示声音
    /**
     * 播放声音
     *
     * @param voiceId
     */
    public void play(int voiceId) {
        streamID=mSoundPlayer.play(voiceId, 1, 1, 1, 0, 1);
    }

    public void stop() {
        mSoundPlayer.stop(streamID);
    }
}
