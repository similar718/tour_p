package cn.xmzt.www.utils.audiomanager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.util.Log;

import androidx.annotation.Nullable;

import org.ar.common.utils.ARUtils;
import org.webrtc.ThreadUtils;

import java.util.List;

public class BluetoothManager {
    private static final String TAG = "BluetoothManager";
    private final Context apprtcContext;
    private final ARAudioManager apprtcAudioManager;
    private final AudioManager audioManager;
    private BluetoothManager.State bluetoothState;
    private final BluetoothProfile.ServiceListener bluetoothServiceListener;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothHeadset bluetoothHeadset;
    private BluetoothDevice bluetoothDevice;
    private final BroadcastReceiver bluetoothHeadsetReceiver;

    static BluetoothManager create(Context context, ARAudioManager audioManager) {
        Log.d(TAG, "create" + ARUtils.getThreadInfo());
        return new BluetoothManager(context, audioManager);
    }

    protected BluetoothManager(Context context, ARAudioManager audioManager) {
        Log.d(TAG, "ctor");
        ThreadUtils.checkIsOnMainThread();
        this.apprtcContext = context;
        this.apprtcAudioManager = audioManager;
        this.audioManager = this.getAudioManager(context);
        this.bluetoothState = BluetoothManager.State.UNINITIALIZED;
        this.bluetoothServiceListener = new BluetoothManager.BluetoothServiceListener();
        this.bluetoothHeadsetReceiver = new BluetoothManager.BluetoothHeadsetBroadcastReceiver();
    }

    public BluetoothManager.State getState() {
        ThreadUtils.checkIsOnMainThread();
        return this.bluetoothState;
    }

    public void start() {
        bluetoothHeadset = null;
        bluetoothDevice = null;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            getBluetoothProfileProxy(this.apprtcContext, this.bluetoothServiceListener, BluetoothProfile.HEADSET);
            IntentFilter bluetoothHeadsetFilter = new IntentFilter();
            bluetoothHeadsetFilter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);
            bluetoothHeadsetFilter.addAction(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED);
            registerReceiver(this.bluetoothHeadsetReceiver, bluetoothHeadsetFilter);
        }
    }

    public void stop() {
        if (this.bluetoothAdapter != null) {
            this.stopScoAudio();
            if (this.bluetoothState != BluetoothManager.State.UNINITIALIZED) {
                this.unregisterReceiver(this.bluetoothHeadsetReceiver);
                if (this.bluetoothHeadset != null) {
                    this.bluetoothAdapter.closeProfileProxy(BluetoothProfile.HEADSET, this.bluetoothHeadset);
                    this.bluetoothHeadset = null;
                }

                this.bluetoothAdapter = null;
                this.bluetoothDevice = null;
                this.bluetoothState = BluetoothManager.State.UNINITIALIZED;
                Log.d(TAG, "stop done: BT state=" + this.bluetoothState);
            }
        }
    }
    private boolean isCanBluetoothSco=false;//是否可以蓝牙SCO音频通信

    /**
     * 设置是否可以蓝牙SCO音频通信
     * @param canBluetoothSco
     */
    private void setCanBluetoothSco(boolean canBluetoothSco) {
        isCanBluetoothSco = canBluetoothSco;
    }

    /**
     * 是否蓝牙音频
     * @return 是否处于蓝牙音频模式
     */
    public boolean isBluetoothAudio() {
        if(apprtcAudioManager.getSelectedAudioDevice()==ARAudioManager.AudioDevice.BLUETOOTH){
            return true;
        }else {
            return false;
        }
    }
    public void startScoAudio() {
        if(isBluetoothAudio()&&isCanBluetoothSco&&!audioManager.isBluetoothScoOn()){
            this.bluetoothState = BluetoothManager.State.SCO_CONNECTING;
            this.audioManager.startBluetoothSco();
            this.audioManager.setBluetoothScoOn(true);
        }
    }

    public void stopScoAudio() {
        if(audioManager.isBluetoothScoOn()){
            this.bluetoothState = BluetoothManager.State.SCO_DISCONNECTING;
            this.audioManager.stopBluetoothSco();
            this.audioManager.setBluetoothScoOn(false);
        }

    }

    @Nullable
    protected AudioManager getAudioManager(Context context) {
        return (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
    }

    protected void registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        this.apprtcContext.registerReceiver(receiver, filter);
    }

    protected void unregisterReceiver(BroadcastReceiver receiver) {
        this.apprtcContext.unregisterReceiver(receiver);
    }

    protected boolean getBluetoothProfileProxy(Context context, BluetoothProfile.ServiceListener listener, int profile) {
        return this.bluetoothAdapter.getProfileProxy(context, listener, profile);
    }



    private class BluetoothHeadsetBroadcastReceiver extends BroadcastReceiver {
        private BluetoothHeadsetBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (BluetoothManager.this.bluetoothState != BluetoothManager.State.UNINITIALIZED) {
                String action = intent.getAction();
                int state;
                if (action.equals(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED)) {
                    state = intent.getIntExtra(BluetoothHeadset.EXTRA_STATE, BluetoothHeadset.STATE_DISCONNECTED);
                    if (state == BluetoothHeadset.STATE_CONNECTED) {
                        bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        if(bluetoothHeadset!=null&&bluetoothDevice!=null){
                            BluetoothManager.this.bluetoothState = State.HEADSET_AVAILABLE;
                            setCanBluetoothSco(true);
                            bluetoothHeadset.startVoiceRecognition(bluetoothDevice);
                        }
                    } else if (state == BluetoothHeadset.STATE_DISCONNECTED) {
                        bluetoothState = State.HEADSET_UNAVAILABLE;
                        setCanBluetoothSco(false);
                        stopScoAudio();
                        if(bluetoothHeadset!=null&&bluetoothDevice!=null){
                            bluetoothHeadset.stopVoiceRecognition(bluetoothDevice);
                        }
                    }
                    apprtcAudioManager.updateAudioDeviceState();
                } else if (action.equals(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED)) {
                    state = intent.getIntExtra(BluetoothHeadset.EXTRA_STATE, BluetoothHeadset.STATE_AUDIO_DISCONNECTED);
                    if (state==BluetoothHeadset.STATE_AUDIO_CONNECTED){
                        bluetoothState = BluetoothManager.State.SCO_CONNECTED;
                    }else if (state==BluetoothHeadset.STATE_AUDIO_DISCONNECTED){
                        bluetoothState = BluetoothManager.State.SCO_DISCONNECTING;
                    }
                }
                Log.d(TAG, "onReceive done: BT state=" + BluetoothManager.this.bluetoothState);
            }
        }
    }

    private class BluetoothServiceListener implements BluetoothProfile.ServiceListener {
        private BluetoothServiceListener() {
        }

        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            if (profile == BluetoothProfile.HEADSET ) {
                Log.d(TAG, "BluetoothServiceListener.onServiceConnected: BT state=" + BluetoothManager.this.bluetoothState);
                bluetoothHeadset = (BluetoothHeadset)proxy;
                List<BluetoothDevice> bluetoothDevices=bluetoothHeadset.getConnectedDevices();
                if(bluetoothDevices!=null&&bluetoothDevices.size()>0){
                    BluetoothManager.this.bluetoothState = BluetoothManager.State.HEADSET_AVAILABLE;
                    setCanBluetoothSco(true);
                    bluetoothDevice=bluetoothDevices.get(0);
                    startVoiceRecognition();
                    Log.e(TAG,"bluetooth headset onServiceConnected mBluetoothDevice="+bluetoothDevice);
                }else {
                    BluetoothManager.this.bluetoothState = BluetoothManager.State.HEADSET_UNAVAILABLE;
                    setCanBluetoothSco(false);
                }
                apprtcAudioManager.updateAudioDeviceState();
                Log.d(TAG, "onServiceConnected done: BT state=" + BluetoothManager.this.bluetoothState);
            }
        }

        public void onServiceDisconnected(int profile) {
            if (profile == BluetoothProfile.HEADSET) {
                stopScoAudio();
                stopVoiceRecognition();
                bluetoothHeadset = null;
                bluetoothDevice = null;
                bluetoothState = BluetoothManager.State.HEADSET_UNAVAILABLE;
                setCanBluetoothSco(false);
                apprtcAudioManager.updateAudioDeviceState();
                Log.d(TAG, "BluetoothServiceListener.onServiceDisconnected: BT state=" + BluetoothManager.this.bluetoothState);
            }
        }
    }
    public synchronized void startVoiceRecognition(){
        //当前音乐会暂停一下，没有其他方案了
        if(bluetoothHeadset!=null&&bluetoothDevice!=null){
            if(bluetoothHeadset.startVoiceRecognition(bluetoothDevice)){
                if(bluetoothHeadset.stopVoiceRecognition(bluetoothDevice)){
                    bluetoothHeadset.startVoiceRecognition(bluetoothDevice);
                }
            }
        }
    }
    public void stopVoiceRecognition(){
        if(bluetoothHeadset!=null&&bluetoothDevice!=null){
            bluetoothHeadset.stopVoiceRecognition(bluetoothDevice);
        }
    }
    public static enum State {
        UNINITIALIZED,
        ERROR,
        HEADSET_UNAVAILABLE,
        HEADSET_AVAILABLE,
        SCO_DISCONNECTING,
        SCO_CONNECTING,
        SCO_CONNECTED;

        private State() {
        }
    }
}