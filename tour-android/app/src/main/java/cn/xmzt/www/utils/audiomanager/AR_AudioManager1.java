package cn.xmzt.www.utils.audiomanager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;

import java.util.List;

public class AR_AudioManager1 implements AudioManager.OnAudioFocusChangeListener{
    private static final String TAG = "AnyRTCAudioManager";
    private final Context apprtcContext;
    private boolean initialized = false;
    private AudioManager audioManager;

    private int savedAudioMode = -2;
    private boolean savedIsSpeakerPhoneOn = false;
    private boolean savedIsMicrophoneMute = false;
    private BroadcastReceiver wiredHeadsetReceiver;
    private AR_AudioManager1.BluetoothConnectionReceiver audioNoisyReceiver;
    private BluetoothAdapter mBluetoothAdapter;
    public static AR_AudioManager1 create(Context context) {
        return new AR_AudioManager1(context);
    }

    private AR_AudioManager1(Context context) {
        this.apprtcContext = context;
        this.audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

        /*BluetoothManager bm=(BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter=bm.getAdapter();*/

        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if (audioManager.isBluetoothScoAvailableOffCall()) {
            mBluetoothAdapter.getProfileProxy(context,mBluetoothProfileListener,BluetoothProfile.HEADSET);
        }
    }
    public void init() {
        Log.d("AnyRTCAudioManager", "init");
        if (!this.initialized) {
            this.savedAudioMode = this.audioManager.getMode();
            this.savedIsSpeakerPhoneOn = this.audioManager.isSpeakerphoneOn();
            this.savedIsMicrophoneMute = this.audioManager.isMicrophoneMute();
            if(hasWiredHeadset()){
                changeToHeadset();
            }else if(getConnectedBluetoothProfile()!=-1) {
                changeToBluetoothHeadset();
            }else {
                changeToSpeaker();
            }
            this.registerForWiredHeadsetIntentBroadcast();
            this.registerBluetooth();
            this.initialized = true;
        }
    }
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
        if(audioManager.isBluetoothScoOn()){
            stopBluetoothSco();//停止蓝牙SCO音频通信
        }
        this.audioManager.setMode(AudioManager.MODE_NORMAL);
        return 0;
    }
    @Override
    public void onAudioFocusChange(int focusChange) {
        //焦点变化监听
        switch(focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
                //失去焦点
                Log.d("AnyRTCAudioManager", "AudioManager.AUDIOFOCUS_LOSS");
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                //暂时丢失。
                Log.d("AnyRTCAudioManager", "AudioManager.AUDIOFOCUS_LOSS_TRANSIENT");
                return;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                //焦点的暂态损耗，而音频焦点的损耗者可以
                Log.d("AnyRTCAudioManager", "AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                return;
            default:
                break;
        }
    }
    public void close() {
        Log.d("AnyRTCAudioManager", "close");
        if (this.initialized) {
            this.unregisterForWiredHeadsetIntentBroadcast();
            this.unRegisterBluetooch();
            this.setSpeakerphoneOn(this.savedIsSpeakerPhoneOn);
            this.setMicrophoneMute(this.savedIsMicrophoneMute);
            this.audioManager.setMode(this.savedAudioMode);
            this.audioManager.abandonAudioFocus(this);
            this.initialized = false;
        }
    }

    private void registerForWiredHeadsetIntentBroadcast() {
        IntentFilter filter = new IntentFilter("android.intent.action.HEADSET_PLUG");
        this.wiredHeadsetReceiver = new BroadcastReceiver() {
            private static final int STATE_UNPLUGGED = 0;
            private static final int STATE_PLUGGED = 1;
            private static final int HAS_NO_MIC = 0;
            private static final int HAS_MIC = 1;

            public void onReceive(Context context, Intent intent) {
                int state = intent.getIntExtra("state", 0);
                int microphone = intent.getIntExtra("microphone", 0);
                String name = intent.getStringExtra("name");
                Log.d("AnyRTCAudioManager", "BroadcastReceiver.onReceive" + ": a=" + intent.getAction() + ", s=" + (state == 0 ? "unplugged" : "plugged") + ", m=" + (microphone == 1 ? "mic" : "no mic") + ", n=" + name + ", sb=" + this.isInitialStickyBroadcast());
                if(state == 1){
                    //有有线耳机连接
                    AR_AudioManager1.this.changeToHeadset();
                }else {
                    if(audioManager.isBluetoothA2dpOn()){
                        AR_AudioManager1.this.changeToBluetoothHeadset();
                    }else {
                        AR_AudioManager1.this.changeToSpeaker();
                    }
                }
            }
        };
        this.apprtcContext.registerReceiver(this.wiredHeadsetReceiver, filter);
    }

    private void unregisterForWiredHeadsetIntentBroadcast() {
        this.apprtcContext.unregisterReceiver(this.wiredHeadsetReceiver);
        this.wiredHeadsetReceiver = null;
    }

    private void setSpeakerphoneOn(boolean on) {
        boolean wasOn = this.audioManager.isSpeakerphoneOn();
        if (wasOn != on) {
            this.audioManager.setSpeakerphoneOn(on);
        }
    }

    /**
     * 设置是否让麦克风静音
     * @param on
     */
    private void setMicrophoneMute(boolean on) {
        boolean wasMuted = this.audioManager.isMicrophoneMute();
        if (wasMuted != on) {
            this.audioManager.setMicrophoneMute(on);
        }
    }

    /** @deprecated */
    @Deprecated
    private boolean hasWiredHeadset() {
        return this.audioManager.isWiredHeadsetOn();
    }

    private void registerBluetooth() {
        this.audioNoisyReceiver = new AR_AudioManager1.BluetoothConnectionReceiver();
        IntentFilter audioFilter = new IntentFilter();
//        audioFilter.addAction("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED");
        audioFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        audioFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        audioFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        audioFilter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);//用于广播耳机配置文件连接状态的更改。

        audioFilter.addAction(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED);
        this.apprtcContext.registerReceiver(this.audioNoisyReceiver, audioFilter);
    }

    private void unRegisterBluetooch() {
        this.apprtcContext.unregisterReceiver(this.audioNoisyReceiver);
        this.audioNoisyReceiver = null;
    }

    private int getConnectedBluetoothProfile(){
        int flag = -1;
        if(mBluetoothAdapter==null){
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        if (mBluetoothAdapter.isEnabled()) {
            int headset = mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET); //蓝牙头戴式耳机，支持语音输入输出
            int a2dp = mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP); //可操控蓝牙设备，如带播放暂停功能的蓝牙耳机
            int health = mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEALTH); //蓝牙穿戴式设备
            //查看是否蓝牙是否连接到三种设备的一种，以此来判断是否处于连接状态还是打开并没有连接的状态
            if(headset == BluetoothProfile.STATE_CONNECTED) {
                flag = headset;
                isBluetoothSco=true;
            }else if (a2dp == BluetoothProfile.STATE_CONNECTED) {
                flag = a2dp;
            }else if (health == BluetoothProfile.STATE_CONNECTED) {
                flag = health;
            }
        }
        return flag;
    }
    /**
     * 切换到听筒  手机自带喇叭
     */
    public void changeToReceiver() {
        audioManager.setSpeakerphoneOn(false);
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
    }
    /**
     * 切换到外放
     */
    public void changeToSpeaker(){
        //注意此处，蓝牙未断开时使用MODE_IN_COMMUNICATION而不是MODE_NORMAL
        isHeadset=false;
        if (getConnectedBluetoothProfile()!=-1) {
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        }else {
            audioManager.setMode(AudioManager.MODE_NORMAL);
        }
        if(audioManager.isBluetoothScoOn()){
            stopBluetoothSco();//停止蓝牙SCO音频通信
        }
        audioManager.setSpeakerphoneOn(true);

    }
    private boolean isHeadset=false;//耳机模式
    /**
     * 切换到耳机模式
     */
    public void changeToHeadset(){
        Log.d("AnyRTCAudioManager", "Bluetooth changeToHeadset isBluetoothScoOn: " + audioManager.isBluetoothScoOn());
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        isHeadset=true;
        if(audioManager.isBluetoothScoOn()){
            stopBluetoothSco();//停止蓝牙SCO音频通信
        }
        if(audioManager.isSpeakerphoneOn()){
            this.audioManager.setSpeakerphoneOn(false);
        }
    }
    /**
     * 切换到蓝牙模式
     */
    public void changeToBluetoothHeadset() {
        Log.d("AnyRTCAudioManager", "Bluetooth changeToBluetoothHeadset isBluetoothScoOn: " + audioManager.isBluetoothScoOn());
        isHeadset=false;
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        if(audioManager.isSpeakerphoneOn()){
            this.audioManager.setSpeakerphoneOn(false);
        }
    }

    public class BluetoothConnectionReceiver extends BroadcastReceiver {
        public BluetoothConnectionReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            int state;
            String action=intent.getAction();
            /*if (BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED.equals(intent.getAction())) {
                state = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, -1);
                if (state == 2) {
                    Log.d("AnyRTCAudioManager", "Bluetooth STATE_CONNECTED");
                    AR_AudioManager.this.changeToBluetoothHeadset();
                } else if (state == 0) {
                    Log.d("AnyRTCAudioManager", "Bluetooth STATE_DISCONNECTED");
                    if(hasWiredHeadset()){
                        AR_AudioManager.this.updateAudioDeviceState(true);
                    }else {
                        AR_AudioManager.this.changeToSpeaker();
                    }

                } else if (state == 1) {
                    Log.d("AnyRTCAudioManager", "Bluetooth STATE_CONNECTING");
                } else if (state == 3) {
                    Log.d("AnyRTCAudioManager", "Bluetooth STATE_DISCONNECTING");
                }
            }*/
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                //蓝牙状态
                state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                if (state == BluetoothAdapter.STATE_OFF) {
                    Log.d("AnyRTCAudioManager", "Bluetooth STATE_OFF or STATE_TURNING_OFF");
                    //是否有有线耳机连接
                    if(hasWiredHeadset()){
                        AR_AudioManager1.this.changeToHeadset();
                    }else {
                        AR_AudioManager1.this.changeToSpeaker();
                    }
                }
            }else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                mBluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d("AnyRTCAudioManager", "Bluetooth 蓝牙已连接到："+mBluetoothDevice.getName());
                AR_AudioManager1.this.changeToBluetoothHeadset();
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                mBluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.e("AnyRTCAudioManager",  mBluetoothDevice.getName()+" 蓝牙已断开");//蓝牙的名字
                if(hasWiredHeadset()){
                    AR_AudioManager1.this.changeToHeadset();
                }else {
                    AR_AudioManager1.this.changeToSpeaker();
                }
            }else if (BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
                if (intent.getExtras().getInt(BluetoothHeadset.EXTRA_STATE) == BluetoothHeadset.STATE_DISCONNECTED) {
                    Log.e(TAG, "BluetoothHeadset 蓝牙未连接");
                    isBluetoothSco=false;
                    if(mBluetoothHeadset!=null&&mBluetoothDevice!=null){
                        mBluetoothHeadset.stopVoiceRecognition(mBluetoothDevice);
                    }
                } else if (intent.getExtras().getInt(BluetoothHeadset.EXTRA_STATE) == BluetoothHeadset.STATE_CONNECTED) {
                    isBluetoothSco=true;
                    mBluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.e(TAG, "BluetoothHeadset 蓝牙已连接"+mBluetoothDevice);
                    if(mBluetoothHeadset!=null&&mBluetoothDevice!=null){
                        mBluetoothHeadset.startVoiceRecognition(mBluetoothDevice);
                    }
                }
            }else if (action.equals(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED)){
                state = intent.getIntExtra(BluetoothProfile.EXTRA_STATE,-1);
                if (state==BluetoothHeadset.STATE_AUDIO_CONNECTED){
                    mBluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.e(TAG, "BluetoothHeadset 开始蓝牙语音识别链接"+mBluetoothDevice);
                }else if (state==BluetoothHeadset.STATE_AUDIO_DISCONNECTED){
                    mBluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.e(TAG, "BluetoothHeadset 开始蓝牙语音识别断开"+mBluetoothDevice);
                }
            }
        }
    }
    private boolean isBluetoothSco=false;//是否是蓝牙SCO音频通信
    private void stopBluetoothSco() {
        audioManager.stopBluetoothSco();//停止蓝牙SCO音频连接。
        audioManager.setBluetoothScoOn(false);//不使用蓝牙SCO耳机进行通讯
    }
    public void startBluetoothSco() {
        if(isBluetoothSco&&!isHeadset){
            if(!audioManager.isBluetoothScoOn()){
                audioManager.startBluetoothSco();//开启蓝牙SCO音频连接。
                audioManager.setBluetoothScoOn(true);//使用蓝牙SCO耳机进行通讯。
            }
        }
    }
    private BluetoothHeadset mBluetoothHeadset;
    private BluetoothDevice mBluetoothDevice;
    private BluetoothProfile.ServiceListener mBluetoothProfileListener = new BluetoothProfile.ServiceListener(){
        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            if (profile == BluetoothProfile.HEADSET){
                mBluetoothHeadset = (BluetoothHeadset) proxy;
                List<BluetoothDevice> bluetoothDevices=mBluetoothHeadset.getConnectedDevices();
                if(bluetoothDevices!=null&&bluetoothDevices.size()>0){
                    mBluetoothDevice=bluetoothDevices.get(0);
                    mBluetoothHeadset.startVoiceRecognition(mBluetoothDevice);
                    Log.e(TAG,"bluetooth headset onServiceConnected mBluetoothDevice="+mBluetoothDevice);
                }
                Log.e(TAG,"bluetooth headset onServiceConnected mBluetoothHeadset="+mBluetoothHeadset);
            }
        }
        @Override
        public void onServiceDisconnected(int profile) {
            if (profile == BluetoothProfile.HEADSET){
                if(mBluetoothHeadset!=null&&mBluetoothDevice!=null){
                    mBluetoothHeadset.stopVoiceRecognition(mBluetoothDevice);
                }
                Log.e(TAG,"bluetooth headset onServiceDisconnected mBluetoothHeadset="+mBluetoothHeadset);
                mBluetoothHeadset = null;
                mBluetoothDevice = null;
            }
        }
    };
}
