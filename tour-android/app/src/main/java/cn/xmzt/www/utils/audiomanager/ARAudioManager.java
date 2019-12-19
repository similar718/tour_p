//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.xmzt.www.utils.audiomanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioDeviceInfo;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Build;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;
import android.util.Log;

import org.ar.common.utils.ARUtils;
import org.webrtc.ThreadUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ARAudioManager implements OnAudioFocusChangeListener{
    private static final String TAG = "ARAudioManager";
    private static final String SPEAKERPHONE_AUTO = "auto";
    private static final String SPEAKERPHONE_TRUE = "true";
    private static final String SPEAKERPHONE_FALSE = "false";
    private final Context apprtcContext;
    private final String useSpeakerphone;
    private final BluetoothManager bluetoothManager;
    private AudioManager audioManager;
    private ARAudioManager.AudioManagerEvents audioManagerEvents;
    private ARAudioManager.AudioManagerState amState;
    private int savedAudioMode = -2;
    private boolean savedIsSpeakerPhoneOn = false;
    private boolean savedIsMicrophoneMute = false;
    private boolean hasWiredHeadset = false;//是否切换到有线耳机
    private ARAudioManager.AudioDevice defaultAudioDevice;
    private ARAudioManager.AudioDevice selectedAudioDevice;
    private ARAudioManager.AudioDevice userSelectedAudioDevice;
    private ARProximitySensor proximitySensor = null;
    private Set<ARAudioManager.AudioDevice> audioDevices = new HashSet();
    private BroadcastReceiver wiredHeadsetReceiver;
    private AudioFocusRequest mFocusRequest;
    private AudioAttributes mAudioAttributes;
    private boolean isFocusRequest = false;
    /*
     *  durationHint  AUDIOFOCUS_GAIN_TRANSIENT 暂时的焦点，很快就会放弃专注
     *                     AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK 暂时的焦点，以前的焦点所有者继续播放，声音变小
     *                     AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE  这得益于系统不播放干扰性的声音
     *                     AUDIOFOCUS_GAIN  来处理一个持续时间未知的焦点请求 播放歌曲或视频。
     *                     AUDIOFOCUS_REQUEST_FAILED
     */

    /**
     *注册音频焦点.
     * @param isTalk 是否是对讲
     * @return AUDIOFOCUS_REQUEST_GRANTED：申请成功；AUDIOFOCUS_REQUEST_FAILED：申请失败；
     */
    public int requestAudioFocus(boolean isTalk) {
        if(isTalk){
            this.audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        }
        int result = 0;
        if(!isFocusRequest){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if(mFocusRequest==null){
                    mAudioAttributes = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .build();
                    mFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
                            .setAudioAttributes(mAudioAttributes)
                            .setWillPauseWhenDucked(true)
                            .setOnAudioFocusChangeListener(this)
                            .build();
                }
                result = this.audioManager.requestAudioFocus(mFocusRequest);
            } else {
                result = this.audioManager.requestAudioFocus(this, AudioManager.STREAM_VOICE_CALL, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
            }
            if(result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                isFocusRequest=true;
            }else {
                isFocusRequest=false;
            }
        }
        this.bluetoothManager.startScoAudio();
        return result;
    }

    /**
     * 注销音频焦点.
     * @return AudioManager.AUDIOFOCUS_REQUEST_GRANTED注销成功 AudioManager.AUDIOFOCUS_REQUEST_FAILED 注销失败
     */
    public int unRequestAudioFocus(){
        int result = 0;
        if(isFocusRequest){
            result =this.audioManager.abandonAudioFocus(this);
            if(result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                isFocusRequest=false;
            }
        }
        this.audioManager.setMode(AudioManager.MODE_NORMAL);
        this.bluetoothManager.stopScoAudio();
        return result;
    }
    private ARAudioManager(Context context) {
        Log.d(TAG, "ctor");
        ThreadUtils.checkIsOnMainThread();
        this.apprtcContext = context;
        this.audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        this.bluetoothManager = BluetoothManager.create(context, this);
        this.wiredHeadsetReceiver = new ARAudioManager.WiredHeadsetReceiver();
        this.amState = ARAudioManager.AudioManagerState.UNINITIALIZED;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.useSpeakerphone = sharedPreferences.getString("speakerphone_preference", "auto");
        Log.d(TAG, "useSpeakerphone: " + this.useSpeakerphone);
        if (this.useSpeakerphone.equals("false")) {
            this.defaultAudioDevice = ARAudioManager.AudioDevice.EARPIECE;
        } else {
            this.defaultAudioDevice = ARAudioManager.AudioDevice.SPEAKER_PHONE;
        }

        this.proximitySensor = ARProximitySensor.create(context, new Runnable() {
            public void run() {
            }
        });
        Log.d(TAG, "defaultAudioDevice: " + this.defaultAudioDevice);
        ARUtils.logDeviceInfo(TAG);
    }

    public static ARAudioManager create(Context context) {
        return new ARAudioManager(context);
    }

    private void onProximitySensorChangedState() {
        if (this.useSpeakerphone.equals("auto")) {
            if (this.audioDevices.size() == 2 && this.audioDevices.contains(ARAudioManager.AudioDevice.EARPIECE) && this.audioDevices.contains(ARAudioManager.AudioDevice.SPEAKER_PHONE)) {
                if (this.proximitySensor.sensorReportsNearState()) {
                    Log.d(TAG, "onProximitySensorChangedState: ARAudioManager.AudioDevice.EARPIECE");
                    this.setAudioDeviceInternal(ARAudioManager.AudioDevice.EARPIECE);
                } else {
                    Log.d(TAG, "onProximitySensorChangedState: ARAudioManager.AudioDevice.SPEAKER_PHONE");
                    this.setAudioDeviceInternal(ARAudioManager.AudioDevice.SPEAKER_PHONE);
                }
            }

        }
    }

    public void start(ARAudioManager.AudioManagerEvents audioManagerEvents) {
        Log.d(TAG, "start");
        ThreadUtils.checkIsOnMainThread();
        if (this.amState == ARAudioManager.AudioManagerState.RUNNING) {
            Log.e(TAG, "AudioManager is already active");
        } else {
            Log.d(TAG, "AudioManager starts...");
            this.audioManagerEvents = audioManagerEvents;
            this.amState = ARAudioManager.AudioManagerState.RUNNING;
            this.savedAudioMode = this.audioManager.getMode();
            this.savedIsSpeakerPhoneOn = this.audioManager.isSpeakerphoneOn();
            this.savedIsMicrophoneMute = this.audioManager.isMicrophoneMute();
            hasWiredHeadset = this.hasWiredHeadset();
            /*int result = this.audioManager.requestAudioFocus(this, 0, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
            if (result == 1) {
                Log.d(TAG, "Audio focus request granted for VOICE_CALL streams");
            } else {
                Log.e(TAG, "Audio focus request failed");
            }

            this.audioManager.setMode(3);
            this.setMicrophoneMute(false);*/
            this.userSelectedAudioDevice = ARAudioManager.AudioDevice.NONE;
            this.selectedAudioDevice = ARAudioManager.AudioDevice.NONE;
            this.audioDevices.clear();
            this.updateAudioDeviceState();
            this.bluetoothManager.start();
            this.registerReceiver(this.wiredHeadsetReceiver, new IntentFilter("android.intent.action.HEADSET_PLUG"));
            Log.d(TAG, "AudioManager started");
        }
    }

    public void stop() {
        Log.d(TAG, "stop");
        ThreadUtils.checkIsOnMainThread();
        if (this.amState != ARAudioManager.AudioManagerState.RUNNING) {
            Log.e(TAG, "Trying to stop AudioManager in incorrect state: " + this.amState);
        } else {
            this.amState = ARAudioManager.AudioManagerState.UNINITIALIZED;
            this.unregisterReceiver(this.wiredHeadsetReceiver);
            this.bluetoothManager.stop();
            this.setSpeakerphoneOn(this.savedIsSpeakerPhoneOn);
            this.setMicrophoneMute(this.savedIsMicrophoneMute);
            this.audioManager.setMode(this.savedAudioMode);
            this.audioManager.abandonAudioFocus(this);
            Log.d(TAG, "Abandoned audio focus for VOICE_CALL streams");
            if (this.proximitySensor != null) {
                this.proximitySensor.stop();
                this.proximitySensor = null;
            }

            this.audioManagerEvents = null;
            Log.d(TAG, "AudioManager stopped");
        }
    }

    private void setAudioDeviceInternal(ARAudioManager.AudioDevice device) {
        Log.d(TAG, "setAudioDeviceInternal(device=" + device + ")");
        ARUtils.assertIsTrue(this.audioDevices.contains(device));
        this.bluetoothManager.stopScoAudio();
        switch(device) {
            case SPEAKER_PHONE:
                this.setSpeakerphoneOn(true);
                break;
            case EARPIECE:
                this.setSpeakerphoneOn(false);
                break;
            case WIRED_HEADSET:
                this.setSpeakerphoneOn(false);
                break;
            case BLUETOOTH:
                this.setSpeakerphoneOn(false);
                break;
            default:
                Log.e(TAG, "Invalid audio device selection");
        }

        this.selectedAudioDevice = device;
    }

    public Set<ARAudioManager.AudioDevice> getAudioDevices() {
        ThreadUtils.checkIsOnMainThread();
        return Collections.unmodifiableSet(new HashSet(this.audioDevices));
    }

    public ARAudioManager.AudioDevice getSelectedAudioDevice() {
        ThreadUtils.checkIsOnMainThread();
        return this.selectedAudioDevice;
    }

    private void registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        this.apprtcContext.registerReceiver(receiver, filter);
    }

    private void unregisterReceiver(BroadcastReceiver receiver) {
        this.apprtcContext.unregisterReceiver(receiver);
    }
    private void setSpeakerphoneOn(boolean on) {
        boolean wasOn = this.audioManager.isSpeakerphoneOn();
        Log.d(TAG, "setSpeakerphoneOn wasOn=" + wasOn );
        if (wasOn != on) {
            this.audioManager.setSpeakerphoneOn(on);
            Log.d(TAG, "setSpeakerphoneOn on=" + on );
        }
    }
    private void setMicrophoneMute(boolean on) {
        boolean wasMuted = this.audioManager.isMicrophoneMute();
        if (wasMuted != on) {
            this.audioManager.setMicrophoneMute(on);
        }
    }

    private boolean hasEarpiece() {
        return this.apprtcContext.getPackageManager().hasSystemFeature( PackageManager.FEATURE_TELEPHONY);
    }

    /** @deprecated */
    @Deprecated
    private boolean hasWiredHeadset() {
        if (VERSION.SDK_INT < 23) {
            return this.audioManager.isWiredHeadsetOn();
        } else {
            AudioDeviceInfo[] devices = this.audioManager.getDevices(3);
            AudioDeviceInfo[] var2 = devices;
            int var3 = devices.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                AudioDeviceInfo device = var2[var4];
                int type = device.getType();
                if (type == 3) {
                    Log.d(TAG, "hasWiredHeadset: found wired headset");
                    return true;
                }

                if (type == 11) {
                    Log.d(TAG, "hasWiredHeadset: found USB audio device");
                    return true;
                }
            }

            return false;
        }
    }

    public void updateAudioDeviceState() {
        ThreadUtils.checkIsOnMainThread();
        Set<ARAudioManager.AudioDevice> newAudioDevices = new HashSet();
        if (bluetoothManager.getState() != BluetoothManager.State.UNINITIALIZED
                &&bluetoothManager.getState() != BluetoothManager.State.HEADSET_UNAVAILABLE) {
            newAudioDevices.add(ARAudioManager.AudioDevice.BLUETOOTH);
        }
        if (hasWiredHeadset) {
            newAudioDevices.add(ARAudioManager.AudioDevice.WIRED_HEADSET);
        } else {
            newAudioDevices.add(ARAudioManager.AudioDevice.SPEAKER_PHONE);
            /*if (this.hasEarpiece()) {
                newAudioDevices.add(ARAudioManager.AudioDevice.EARPIECE);
            }*/
        }
        boolean audioDeviceSetUpdated = !this.audioDevices.equals(newAudioDevices);//是否更新了设备
        Set<ARAudioManager.AudioDevice> tempAudioDevices = this.audioDevices;
        this.audioDevices = newAudioDevices;
        ARAudioManager.AudioDevice newAudioDevice=ARAudioManager.AudioDevice.NONE;
        if(audioDeviceSetUpdated){
            newAudioDevices.removeAll(tempAudioDevices);
            if(newAudioDevices.contains(ARAudioManager.AudioDevice.WIRED_HEADSET)){
                newAudioDevice=ARAudioManager.AudioDevice.WIRED_HEADSET;
            }else if(newAudioDevices.contains(ARAudioManager.AudioDevice.BLUETOOTH)){
                newAudioDevice=ARAudioManager.AudioDevice.BLUETOOTH;
            }else {
                newAudioDevice=ARAudioManager.AudioDevice.SPEAKER_PHONE;
            }
        }
        if (newAudioDevice!=ARAudioManager.AudioDevice.NONE&&newAudioDevice != this.selectedAudioDevice) {
            setAudioDeviceInternal(newAudioDevice);
            Log.d(TAG, "New device status: available=" + this.audioDevices + ", selected=" + newAudioDevice);
            if (this.audioManagerEvents != null) {
                this.audioManagerEvents.onAudioDeviceChanged(this.selectedAudioDevice, this.audioDevices);
            }
        }
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        String typeOfChange;
        switch(focusChange) {
            case -3:
                typeOfChange = "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK";
                break;
            case -2:
                typeOfChange = "AUDIOFOCUS_LOSS_TRANSIENT";
                break;
            case -1:
                typeOfChange = "AUDIOFOCUS_LOSS";
                break;
            case 0:
            default:
                typeOfChange = "AUDIOFOCUS_INVALID";
                break;
            case 1:
                typeOfChange = "AUDIOFOCUS_GAIN";
                break;
            case 2:
                typeOfChange = "AUDIOFOCUS_GAIN_TRANSIENT";
                break;
            case 3:
                typeOfChange = "AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK";
                break;
            case 4:
                typeOfChange = "AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE";
        }

        Log.d(TAG, "onAudioFocusChange: " + typeOfChange);
    }

    private class WiredHeadsetReceiver extends BroadcastReceiver {
        private static final int STATE_UNPLUGGED = 0;
        private static final int STATE_PLUGGED = 1;
        private static final int HAS_NO_MIC = 0;
        private static final int HAS_MIC = 1;

        private WiredHeadsetReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra("state", 0);
            int microphone = intent.getIntExtra("microphone", 0);
            String name = intent.getStringExtra("name");
            Log.d(TAG, "WiredHeadsetReceiver.onReceive" + ARUtils.getThreadInfo() + ": a=" + intent.getAction() + ", s=" + (state == 0 ? "unplugged" : "plugged") + ", m=" + (microphone == 1 ? "mic" : "no mic") + ", n=" + name + ", sb=" + this.isInitialStickyBroadcast());
            hasWiredHeadset = state == 1;
            ARAudioManager.this.updateAudioDeviceState();
        }
    }

    public interface AudioManagerEvents {
        void onAudioDeviceChanged(ARAudioManager.AudioDevice var1, Set<ARAudioManager.AudioDevice> var2);
    }

    public static enum AudioManagerState {
        UNINITIALIZED,
        PREINITIALIZED,
        RUNNING;

        private AudioManagerState() {
        }
    }

    public static enum AudioDevice {
        SPEAKER_PHONE,
        WIRED_HEADSET,
        EARPIECE,
        BLUETOOTH,
        NONE;

        private AudioDevice() {
        }
    }
}
