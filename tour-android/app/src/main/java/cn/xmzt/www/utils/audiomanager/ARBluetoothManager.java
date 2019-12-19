package cn.xmzt.www.utils.audiomanager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import androidx.annotation.Nullable;

import org.ar.common.utils.ARUtils;
import org.webrtc.ThreadUtils;

import java.util.List;

public class ARBluetoothManager {
    private static final String TAG = "ARBluetoothManager";
    private final Context apprtcContext;
    private final ARAudioManager apprtcAudioManager;
    private final AudioManager audioManager;
    private final Handler handler;
    int scoConnectionAttempts;
    private ARBluetoothManager.State bluetoothState;
    private final BluetoothProfile.ServiceListener bluetoothServiceListener;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothHeadset bluetoothHeadset;
    private BluetoothDevice bluetoothDevice;
    private final BroadcastReceiver bluetoothHeadsetReceiver;
    private final Runnable bluetoothTimeoutRunnable = new Runnable() {
        public void run() {
            ARBluetoothManager.this.bluetoothTimeout();
        }
    };

    static ARBluetoothManager create(Context context, ARAudioManager audioManager) {
        Log.d(TAG, "create" + ARUtils.getThreadInfo());
        return new ARBluetoothManager(context, audioManager);
    }

    protected ARBluetoothManager(Context context, ARAudioManager audioManager) {
        Log.d(TAG, "ctor");
        ThreadUtils.checkIsOnMainThread();
        this.apprtcContext = context;
        this.apprtcAudioManager = audioManager;
        this.audioManager = this.getAudioManager(context);
        this.bluetoothState = ARBluetoothManager.State.UNINITIALIZED;
        this.bluetoothServiceListener = new ARBluetoothManager.BluetoothServiceListener();
        this.bluetoothHeadsetReceiver = new ARBluetoothManager.BluetoothHeadsetBroadcastReceiver();
        this.handler = new Handler(Looper.getMainLooper());
    }

    public ARBluetoothManager.State getState() {
        ThreadUtils.checkIsOnMainThread();
        return this.bluetoothState;
    }

    public void start() {
        bluetoothHeadset = null;
        bluetoothDevice = null;
        scoConnectionAttempts = 0;
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
        ThreadUtils.checkIsOnMainThread();
        Log.d(TAG, "stop: BT state=" + this.bluetoothState);
        if (this.bluetoothAdapter != null) {
            this.stopScoAudio();
            if (this.bluetoothState != ARBluetoothManager.State.UNINITIALIZED) {
                this.unregisterReceiver(this.bluetoothHeadsetReceiver);
                this.cancelTimer();
                if (this.bluetoothHeadset != null) {
                    this.bluetoothAdapter.closeProfileProxy(1, this.bluetoothHeadset);
                    this.bluetoothHeadset = null;
                }

                this.bluetoothAdapter = null;
                this.bluetoothDevice = null;
                this.bluetoothState = ARBluetoothManager.State.UNINITIALIZED;
                Log.d(TAG, "stop done: BT state=" + this.bluetoothState);
            }
        }
    }

    public boolean startScoAudio() {
        ThreadUtils.checkIsOnMainThread();
        Log.d(TAG, "startSco: BT state=" + this.bluetoothState + ", attempts: " + this.scoConnectionAttempts + ", SCO is on: " + this.isScoOn());
        if (this.scoConnectionAttempts >= 2) {
            Log.e(TAG, "BT SCO connection fails - no more attempts");
            return false;
        } else if (this.bluetoothState != ARBluetoothManager.State.HEADSET_AVAILABLE) {
            Log.e(TAG, "BT SCO connection fails - no headset available");
            return false;
        } else {
            Log.d(TAG, "Starting Bluetooth SCO and waits for ACTION_AUDIO_STATE_CHANGED...");
            this.bluetoothState = ARBluetoothManager.State.SCO_CONNECTING;
            this.audioManager.startBluetoothSco();
            this.audioManager.setBluetoothScoOn(true);
            ++this.scoConnectionAttempts;
            this.startTimer();
            Log.d(TAG, "startScoAudio done: BT state=" + this.bluetoothState + ", SCO is on: " + this.isScoOn());
            return true;
        }
    }

    public void stopScoAudio() {
        ThreadUtils.checkIsOnMainThread();
        Log.d(TAG, "stopScoAudio: BT state=" + this.bluetoothState + ", SCO is on: " + this.isScoOn());
        if (this.bluetoothState == ARBluetoothManager.State.SCO_CONNECTING || this.bluetoothState == ARBluetoothManager.State.SCO_CONNECTED) {
            this.cancelTimer();
            this.audioManager.stopBluetoothSco();
            this.audioManager.setBluetoothScoOn(false);
            this.bluetoothState = ARBluetoothManager.State.SCO_DISCONNECTING;
            Log.d(TAG, "stopScoAudio done: BT state=" + this.bluetoothState + ", SCO is on: " + this.isScoOn());
        }
    }

    public void updateDevice() {
        if (this.bluetoothState != ARBluetoothManager.State.UNINITIALIZED && this.bluetoothHeadset != null) {
            Log.d(TAG, "updateDevice");
            List<BluetoothDevice> devices = this.bluetoothHeadset.getConnectedDevices();
            if (devices.isEmpty()) {
                this.bluetoothDevice = null;
                this.bluetoothState = ARBluetoothManager.State.HEADSET_UNAVAILABLE;
                Log.d(TAG, "No connected bluetooth headset");
            } else {
                this.bluetoothDevice = (BluetoothDevice)devices.get(0);
                this.bluetoothState = ARBluetoothManager.State.HEADSET_AVAILABLE;
//                Log.d(TAG, "Connected bluetooth headset: name=" + this.bluetoothDevice.getName() + ", state=" + this.stateToString(this.bluetoothHeadset.getConnectionState(this.bluetoothDevice)) + ", SCO audio=" + this.bluetoothHeadset.isAudioConnected(this.bluetoothDevice));
            }

            Log.d(TAG, "updateDevice done: BT state=" + this.bluetoothState);
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

    protected boolean hasPermission(Context context, String permission) {
        return this.apprtcContext.checkPermission(permission, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED;
    }

    private void updateAudioDeviceState() {
        ThreadUtils.checkIsOnMainThread();
        Log.d(TAG, "updateAudioDeviceState");
        this.apprtcAudioManager.updateAudioDeviceState();
    }

    private void startTimer() {
        ThreadUtils.checkIsOnMainThread();
        Log.d(TAG, "startTimer");
        this.handler.postDelayed(this.bluetoothTimeoutRunnable, 4000L);
    }

    private void cancelTimer() {
        ThreadUtils.checkIsOnMainThread();
        Log.d(TAG, "cancelTimer");
        this.handler.removeCallbacks(this.bluetoothTimeoutRunnable);
    }

    private void bluetoothTimeout() {
        ThreadUtils.checkIsOnMainThread();
        if (this.bluetoothState != ARBluetoothManager.State.UNINITIALIZED && this.bluetoothHeadset != null) {
            Log.d(TAG, "bluetoothTimeout: BT state=" + this.bluetoothState + ", attempts: " + this.scoConnectionAttempts + ", SCO is on: " + this.isScoOn());
            if (this.bluetoothState == ARBluetoothManager.State.SCO_CONNECTING) {
                boolean scoConnected = false;
                List<BluetoothDevice> devices = this.bluetoothHeadset.getConnectedDevices();
                if (devices.size() > 0) {
                    this.bluetoothDevice = (BluetoothDevice)devices.get(0);
                    if (this.bluetoothHeadset.isAudioConnected(this.bluetoothDevice)) {
                        Log.d(TAG, "SCO connected with " + this.bluetoothDevice.getName());
                        scoConnected = true;
                    } else {
                        Log.d(TAG, "SCO is not connected with " + this.bluetoothDevice.getName());
                    }
                }

                if (scoConnected) {
                    this.bluetoothState = ARBluetoothManager.State.SCO_CONNECTED;
                    this.scoConnectionAttempts = 0;
                } else {
                    Log.w(TAG, "BT failed to connect after timeout");
                    this.stopScoAudio();
                }

                this.updateAudioDeviceState();
                Log.d(TAG, "bluetoothTimeout done: BT state=" + this.bluetoothState);
            }
        }
    }

    private boolean isScoOn() {
        return this.audioManager.isBluetoothScoOn();
    }

    private class BluetoothHeadsetBroadcastReceiver extends BroadcastReceiver {
        private BluetoothHeadsetBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (ARBluetoothManager.this.bluetoothState != ARBluetoothManager.State.UNINITIALIZED) {
                String action = intent.getAction();
                int state;
                if (action.equals(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED)) {
                    state = intent.getIntExtra(BluetoothHeadset.EXTRA_STATE, BluetoothHeadset.STATE_DISCONNECTED);
                    if (state == BluetoothHeadset.STATE_CONNECTED) {
                        ARBluetoothManager.this.scoConnectionAttempts = 0;
                        ARBluetoothManager.this.updateAudioDeviceState();
                    } else if (state == BluetoothHeadset.STATE_DISCONNECTED) {
                        ARBluetoothManager.this.stopScoAudio();
                        ARBluetoothManager.this.updateAudioDeviceState();
                    }
                } else if (action.equals(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED)) {
                    state = intent.getIntExtra(BluetoothHeadset.EXTRA_STATE, BluetoothHeadset.STATE_AUDIO_DISCONNECTED);
                    if (state == BluetoothHeadset.STATE_AUDIO_CONNECTED) {
                        ARBluetoothManager.this.cancelTimer();
                        if (ARBluetoothManager.this.bluetoothState == ARBluetoothManager.State.SCO_CONNECTING) {
                            Log.d(TAG, "+++ Bluetooth audio SCO is now connected");
                            ARBluetoothManager.this.bluetoothState = ARBluetoothManager.State.SCO_CONNECTED;
                            ARBluetoothManager.this.scoConnectionAttempts = 0;
                            ARBluetoothManager.this.updateAudioDeviceState();
                        } else {
                            Log.w(TAG, "Unexpected state BluetoothHeadset.STATE_AUDIO_CONNECTED");
                        }
                    } else if (state == 11) {
                        Log.d(TAG, "+++ Bluetooth audio SCO is now connecting...");
                    } else if (state == 10) {
                        Log.d(TAG, "+++ Bluetooth audio SCO is now disconnected");
                        if (this.isInitialStickyBroadcast()) {
                            Log.d(TAG, "Ignore STATE_AUDIO_DISCONNECTED initial sticky broadcast.");
                            return;
                        }

//                        ARBluetoothManager.this.updateAudioDeviceState();
                    }
                }

                Log.d(TAG, "onReceive done: BT state=" + ARBluetoothManager.this.bluetoothState);
            }
        }
    }

    private class BluetoothServiceListener implements BluetoothProfile.ServiceListener {
        private BluetoothServiceListener() {
        }

        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            if (profile == BluetoothProfile.HEADSET ) {
                Log.d(TAG, "BluetoothServiceListener.onServiceConnected: BT state=" + ARBluetoothManager.this.bluetoothState);
                ARBluetoothManager.this.bluetoothHeadset = (BluetoothHeadset)proxy;
                ARBluetoothManager.this.updateAudioDeviceState();
                List<BluetoothDevice> bluetoothDevices=bluetoothHeadset.getConnectedDevices();
                if(bluetoothDevices!=null&&bluetoothDevices.size()>0){
                    ARBluetoothManager.this.bluetoothState = ARBluetoothManager.State.HEADSET_AVAILABLE;
                    bluetoothDevice=bluetoothDevices.get(0);
                    bluetoothHeadset.startVoiceRecognition(bluetoothDevice);
                    Log.e(TAG,"bluetooth headset onServiceConnected mBluetoothDevice="+bluetoothDevice);
                }else {
                    ARBluetoothManager.this.bluetoothState = ARBluetoothManager.State.HEADSET_UNAVAILABLE;
                }
                Log.d(TAG, "onServiceConnected done: BT state=" + ARBluetoothManager.this.bluetoothState);
            }
        }

        public void onServiceDisconnected(int profile) {
            if (profile == BluetoothProfile.HEADSET) {
                if(bluetoothHeadset!=null&&bluetoothDevice!=null){
                    bluetoothHeadset.stopVoiceRecognition(bluetoothDevice);
                }
                Log.d(TAG, "BluetoothServiceListener.onServiceDisconnected: BT state=" + ARBluetoothManager.this.bluetoothState);
                stopScoAudio();
                bluetoothHeadset = null;
                bluetoothDevice = null;
                bluetoothState = ARBluetoothManager.State.HEADSET_UNAVAILABLE;
                updateAudioDeviceState();
                Log.d(TAG, "onServiceDisconnected done: BT state=" + ARBluetoothManager.this.bluetoothState);
            }
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