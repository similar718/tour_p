package cn.xmzt.www.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import cn.xmzt.www.mine.event.PushMessageEvent;
import cn.xmzt.www.utils.FastJsonUtil;
import cn.xmzt.www.utils.SchemeActivityUtil;

import static org.webrtc.ContextUtils.getApplicationContext;

/**
 * @author tanlei
 * @date 2019/9/12
 * @describe 极光推送的消息
 */

public class JGPushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

            //官网提供根据Registration ID 进行推送 此方法用于处理该类推送消息

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {

            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
            // 处理自定义消息

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            //接收到通知会走的方法
            String extra_extra=bundle.getString(JPushInterface.EXTRA_EXTRA);
            Map<String,Object> extraMap=FastJsonUtil.jsonToMap(extra_extra);
            EventBus.getDefault().post(new PushMessageEvent(false,extraMap));
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {

            //用户点击通知会走的方法
            //获取推送消息的方法
            //String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            //{"appKey":"bc7dd2ea0788e55e6046d41a","ext":"","id":"18071adc0372e2241d2","jumpPages":"1","msg":"订单1128529248242634752，（公测）西湾红树林公园一日游已完成支付，等待客服进行确认，将在1小时内处理回复您，确认结果将以短信方式通知您，谢谢！如需帮助请致电4001234123","objectId":"1128529248242634752","secret":"ca1ec90b35f3e9169e3d1341","title":"支付成功","type":"2"}

            String extra_extra=bundle.getString(JPushInterface.EXTRA_EXTRA);
            Map<String,Object> extraMap=FastJsonUtil.jsonToMap(extra_extra);
            if(extraMap!=null){
                String title= (String) extraMap.get("title");
                String objectId= (String) extraMap.get("objectId");
                String type= (String) extraMap.get("type");
                String jumpPages= (String) extraMap.get("jumpPages");
                if("1".equals(jumpPages)){
                    SchemeActivityUtil.pushToActivity(getApplicationContext(),title,objectId,type);
                }
            }
        }
    }
    /**
     * 判断应用是否已经启动
     *
     * @param context     一个context
     * @param packageName 要判断应用的包名
     * @return boolean
     */
    public static boolean isAppAlive(Context context, String packageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (processInfos.get(i).processName.equals(packageName)) {
                Log.i("NotificationLaunch", String.format("the %s is running, isAppAlive return true", packageName));
                return true;
            }
        }
        Log.i("NotificationLaunch", String.format("the %s is not running, isAppAlive return false", packageName));
        return false;
    }
}
