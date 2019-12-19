package cn.xmzt.www.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cn.xmzt.www.service.LocationService;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("LOCATION_CLOCK")) {
            Log.e("ggb", "--->>>   onReceive  LOCATION_CLOCK");
//            Intent locationIntent = new Intent(context, LocationService.class);
//            context.startService(locationIntent);
        }
    }
}
