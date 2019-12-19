package cn.xmzt.www.base;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.anyrtc.DeveloperInfo;
import cn.xmzt.www.anyrtc.NameUtils;
import cn.xmzt.www.intercom.bean.UserBasicInfoBean;
import cn.xmzt.www.main.globals.FloatAudioPlayManage;
import cn.xmzt.www.main.globals.FloatIntercomManage;
import cn.xmzt.www.main.globals.TalkManage;
import cn.xmzt.www.nim.im.NimApplication;
import cn.xmzt.www.service.LocationService;
import cn.xmzt.www.utils.CrashHandlerUtils;
import cn.xmzt.www.utils.FastJsonUtil;
import cn.xmzt.www.utils.PermissionUtil;
import cn.xmzt.www.utils.SPUtils;
import cn.xmzt.www.view.floatview.intercom.FloatViewMagnet;
import cn.xmzt.www.view.floatview.intercom.IFloatingViewMagnet;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import org.ar.common.enums.ARLogLevel;
import org.ar.rtmax_kit.ARMaxEngine;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author tanlei
 * @date 2019/8/7
 * @describe Application
 */

public class TourApplication extends NimApplication {

    public static TourApplication INSTANCE;

    public static TourApplication getInstance() {
        return INSTANCE;
    }

    /**
     * 极光推送的rid
     */
    public static String rid;
    public static Context context;
    private static String token;
    private static UserBasicInfoBean userBasicInfoBean;

    public static String tempUserid = "";
    public static String tempNickName = "";

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.color_F4_F4_F4, R.color.color_99_99_99);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    public static void setToken(String token) {
        if(TextUtils.isEmpty(token)){
            SPUtils.setToken("");
        }else {
            SPUtils.setToken(token);
        }
        TourApplication.token = token;
    }
    public static void setUser(UserBasicInfoBean user) {
        if(user==null){
            SPUtils.putUser("");
        }else {
            String userJson= FastJsonUtil.objToJson(user);
            SPUtils.putUser(userJson);
        }
        userBasicInfoBean=user;
    }
    public static UserBasicInfoBean getUser() {
        if(userBasicInfoBean==null){
            userBasicInfoBean = SPUtils.getUser();
        }
        return userBasicInfoBean;
    }
    public static int getUserId() {
        if(userBasicInfoBean==null){
            userBasicInfoBean = SPUtils.getUser();
        }
        if(userBasicInfoBean!=null){
            return userBasicInfoBean.getUserId();
        }
        return 0;
    }
    public static String getRefCode() {
        UserBasicInfoBean userBasicInfoBean=getUser();
        if (userBasicInfoBean!= null) {
            return userBasicInfoBean.getRefCode();
        }
        return "";
    }

    public static String getToken() {
        if (TextUtils.isEmpty(token)) {
            token = SPUtils.getToken();
        }
        return token;
    }

    /**
     * 全局标记是否登录
     */
    public static boolean isLogin;

    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,getResources().getDisplayMetrics().heightPixels*2/5);
        INSTANCE = this;
        context = getApplicationContext();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        rid = JPushInterface.getRegistrationID(this);
        CrashReport.initCrashReport(getApplicationContext(), "14bc50e8f4", false);//第三个参数为SDK调试模式开关，建议在测试阶段建议设置成true，发布时设置为false。
        Log.e("lee", rid);

        UMConfigure.setLogEnabled(true);
        UMConfigure.init(this, "5d5a51793fc1951313000177", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "45e8e640e909905cfd7aeaa75dde784c");
        PlatformConfig.setWeixin("wx3b08b3acfc7dbcb9", "816a72d5b8a28655768c4da0cc76780b");
        PlatformConfig.setQQZone("101766637", "394233500e8ad1d98eb3c455a2b00200");
        startAlarm();

        CrashHandlerUtils crashHandler = CrashHandlerUtils.getInstance();
        crashHandler.init(this);
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                //异常处理
            }
        });
        initAnyRtc();
        addFloatView();
    }

    public void startAlarm() {
        /**
         首先获得系统服务
         */
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //设置闹钟的意图，我这里是去调用一个服务，该服务功能就是获取位置并且上传
        Intent intent = new Intent(this, LocationService.class);
        PendingIntent pendSender = PendingIntent.getService(this, 0, intent, 0);
        am.cancel(pendSender);
        //AlarmManager.RTC_WAKEUP 这个参数表示系统会唤醒进程；我设置的间隔时间是10分钟
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10 * 60 * 1000, pendSender);
    }


    private void initAnyRtc() {
        tempNickName = NameUtils.getNickName();
        tempUserid = randomNum(6);
        //WebRtcAudioUtils.setDefaultSampleRateHz(32000);
        ARMaxEngine.Inst().initEngine(getApplicationContext(), false, DeveloperInfo.APPID, DeveloperInfo.APPTOKEN);
//        ARMaxEngine.Inst().configServerForPriCloud("pro.anyrtc.io", 9080);//pro.anyrtc.io由于测试需要
        ARMaxEngine.Inst().setLogLevel(ARLogLevel.All);
    }

    //测试
    public static String randomNum(int num) {
        StringBuilder str = new StringBuilder();//定义变长字符串
        Random random = new Random();
        for (int i = 0; i < num; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

    public static MediaPlayer mp;  //音频播放

    public static void playClickVoiceMAX() {
        try {
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            AssetFileDescriptor afd = context.getResources().openRawResourceFd(R.raw.map_max);
            if (afd == null) return;
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();

            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();
    }

    public static void playClickVoiceMIN() {
        try {
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            AssetFileDescriptor afd = context.getResources().openRawResourceFd(R.raw.map_min);
            if (afd == null) return;
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();

            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();
    }

    // 添加悬浮窗
    private void addFloatView() {
        FloatIntercomManage.getInstance().initFloatView();
        FloatAudioPlayManage.getInstance().initFloatView();
        // 注册悬浮窗事件回调
        registerFloatIntercomListener();
    }

    // 注册监听对讲悬浮窗口事件
    private void registerFloatIntercomListener() {
        FloatIntercomManage.getInstance().listenerFloatView(new IFloatingViewMagnet() {
            @Override
            public void onRemove(FloatViewMagnet magnetView) {
                //ToastUtils.showText(getApplicationContext(), "按钮消了");
                TalkManage.getInstance().intercomButtomRemove();
            }

            @Override
            public void onClick(FloatViewMagnet magnetView) {
                //ToastUtils.showText(getApplicationContext(), "单击我了");
                TalkManage.getInstance().intercomButtomClick();
            }

            @Override
            public void onDoubleClick(FloatViewMagnet magnetView) {
                //ToastUtils.showText(getApplicationContext(), "双击我了");
                TalkManage.getInstance().intercomButtomDoubleClick();
            }

            @Override
            public void onUpClick(FloatViewMagnet magnetView) {
                TalkManage.getInstance().intercomButtomUpClick();
            }

            @Override
            public void onTalkClick(FloatViewMagnet magnetView) {
                TalkManage.getInstance().intercomButtomTalkClick();
            }
        });

        FloatAudioPlayManage.getInstance().listenerFloatView(new cn.xmzt.www.view.floatview.audioplay.IFloatingViewMagnet() {
            @Override
            public void onRemove(cn.xmzt.www.view.floatview.audioplay.FloatViewMagnet magnetView) {
                //ToastUtils.showText(getApplicationContext(), "按钮消了");
                TalkManage.getInstance().audioPlayButtomNextRemove();
            }

            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.iv_hide){
                    FloatAudioPlayManage.getInstance().hideFloatView(ActivityUtils.getTopActivity());
                }else {
                    //ToastUtils.showText(getApplicationContext(), "点击下一条了");
                    TalkManage.getInstance().audioPlayButtomNextClick();
                }
            }
        });
    }


}
