//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.xmzt.www.utils.audiomanager;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build.VERSION;
import android.util.Log;

import org.ar.common.utils.ARUtils;
import org.ar.common.utils.ARUtils.NonThreadSafe;

public class ARProximitySensor implements SensorEventListener {
    private static final String TAG = "RTCProximitySensor";
    private final NonThreadSafe nonThreadSafe = new NonThreadSafe();
    private final Runnable onSensorStateListener;
    private final SensorManager sensorManager;
    private Sensor proximitySensor = null;
    private boolean lastStateReportIsNear = false;

    static ARProximitySensor create(Context context, Runnable sensorStateListener) {
        return new ARProximitySensor(context, sensorStateListener);
    }

    private ARProximitySensor(Context context, Runnable sensorStateListener) {
        Log.d("RTCProximitySensor", "RTKProximitySensor" + ARUtils.getThreadInfo());
        this.onSensorStateListener = sensorStateListener;
        this.sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
    }

    public boolean start() {
        this.checkIfCalledOnValidThread();
        Log.d("RTCProximitySensor", "start" + ARUtils.getThreadInfo());
        if (!this.initDefaultSensor()) {
            return false;
        } else {
            this.sensorManager.registerListener(this, this.proximitySensor, 3);
            return true;
        }
    }

    public void stop() {
        this.checkIfCalledOnValidThread();
        Log.d("RTCProximitySensor", "stop" + ARUtils.getThreadInfo());
        if (this.proximitySensor != null) {
            this.sensorManager.unregisterListener(this, this.proximitySensor);
        }
    }

    public boolean sensorReportsNearState() {
        this.checkIfCalledOnValidThread();
        return this.lastStateReportIsNear;
    }

    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        this.checkIfCalledOnValidThread();
        ARUtils.assertIsTrue(sensor.getType() == 8);
        if (accuracy == 0) {
            Log.e("RTCProximitySensor", "The values returned by this sensor cannot be trusted");
        }

    }

    public final void onSensorChanged(SensorEvent event) {
        this.checkIfCalledOnValidThread();
        ARUtils.assertIsTrue(event.sensor.getType() == 8);
        float distanceInCentimeters = event.values[0];
        if (distanceInCentimeters < this.proximitySensor.getMaximumRange()) {
            Log.d("RTCProximitySensor", "Proximity sensor => NEAR state");
            this.lastStateReportIsNear = true;
        } else {
            Log.d("RTCProximitySensor", "Proximity sensor => FAR state");
            this.lastStateReportIsNear = false;
        }

        if (this.onSensorStateListener != null) {
            this.onSensorStateListener.run();
        }

        Log.d("RTCProximitySensor", "onSensorChanged" + ARUtils.getThreadInfo() + ": accuracy=" + event.accuracy + ", timestamp=" + event.timestamp + ", distance=" + event.values[0]);
    }

    private boolean initDefaultSensor() {
        if (this.proximitySensor != null) {
            return true;
        } else {
            this.proximitySensor = this.sensorManager.getDefaultSensor(8);
            if (this.proximitySensor == null) {
                return false;
            } else {
                this.logProximitySensorInfo();
                return true;
            }
        }
    }

    private void logProximitySensorInfo() {
        if (this.proximitySensor != null) {
            StringBuilder info = new StringBuilder("Proximity sensor: ");
            info.append("name=").append(this.proximitySensor.getName());
            info.append(", vendor: ").append(this.proximitySensor.getVendor());
            info.append(", power: ").append(this.proximitySensor.getPower());
            info.append(", resolution: ").append(this.proximitySensor.getResolution());
            info.append(", max range: ").append(this.proximitySensor.getMaximumRange());
            if (VERSION.SDK_INT >= 9) {
                info.append(", min delay: ").append(this.proximitySensor.getMinDelay());
            }

            if (VERSION.SDK_INT >= 20) {
                info.append(", type: ").append(this.proximitySensor.getStringType());
            }

            if (VERSION.SDK_INT >= 21) {
                info.append(", max delay: ").append(this.proximitySensor.getMaxDelay());
                info.append(", reporting mode: ").append(this.proximitySensor.getReportingMode());
                info.append(", isWakeUpSensor: ").append(this.proximitySensor.isWakeUpSensor());
            }

            Log.d("RTCProximitySensor", info.toString());
        }
    }

    private void checkIfCalledOnValidThread() {
        if (!this.nonThreadSafe.calledOnValidThread()) {
            throw new IllegalStateException("Method is not called on valid thread");
        }
    }
}
