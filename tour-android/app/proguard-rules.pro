# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# 指定代码的压缩级别
-optimizationpasses 5
# 忽略警告
-ignorewarnings
# 不忽略库中的非public的类成员
-dontskipnonpubliclibraryclassmembers

# google推荐算法
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*

# 避免混淆Annotation、内部类、泛型、匿名类
-keepattributes *Annotation*,InnerClasses,Signature,EnclosingMethod

# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable
# 保持哪些类不被混淆
# 保持四大组件
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
#如果引用了v4或者v7包
-dontwarn android.support.**
-keep interface android.support.** { *; }
-keep class android.support.** { *; }
-keep public class * extends android.support.**
-keep public class * extends android.support.v4.widget
-keep public class * extends android.app.Fragment
-keep public class * extends android.support.v4.app.Fragment

#如果引用了androidx包
-printconfiguration
-keep,allowobfuscation @interface androidx.annotation.Keep
-keep @androidx.annotation.Keep class *
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}
-dontwarn androidx.**
-keep class androidx.** {*;}
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-dontwarn com.google.**
-keep class com.google.** {*;}
-dontnote com.google.**

#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
#自定义组件不被混淆
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#保持枚举 enum 类不被混淆
-keepclassmembers enum * {
  public static **[] values();
  public static ** valueOf(java.lang.String);
}

-keepclassmembers class * {
    public void *ButtonClicked(android.view.View);
}

#不混淆资源类
-keep class **.R$* {*;}
-keep class **.R{*;}
-dontwarn **.R$*
-keepclassmembers class **.R$* {
    public static <fields>;
}

#避免混淆泛型 如果混淆报错建议关掉
-keepattributes Signature
-keepattributes Exceptions,InnerClasses,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
-repackageclasses ''
#-----------处理js交互---------------
#webview cn.xx.xx.Activity$AppAndroid表示与js交互的内部类
#-keepclassmembers class cn.xx.xx.Activity$AppAndroid {
#  public *;
#}
#-----------处理webview---------------
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}
#-----------处理实体类---------------
-dontwarn cn.xmzt.www.mine.bean.**
-keep class cn.xmzt.www.mine.bean.** { *; }

-dontwarn cn.xmzt.www.home.bean.**
-keep class cn.xmzt.www.home.bean.** { *; }

-dontwarn cn.xmzt.www.route.bean.**
-keep class cn.xmzt.www.route.bean.** { *; }

-dontwarn cn.xmzt.www.hotel.bean.**
-keep class cn.xmzt.www.hotel.bean.** { *; }

-dontwarn cn.xmzt.www.intercom.bean.**
-keep class cn.xmzt.www.intercom.bean.** { *; }

-dontwarn cn.xmzt.www.main.globals.**
-keep class cn.xmzt.www.main.globals.** { *; }

-dontwarn cn.xmzt.www.utils.audiomanager.**
-keep class cn.xmzt.www.utils.audiomanager.** { *; }

-dontwarn cn.xmzt.www.nim.**
-keep class cn.xmzt.www.nim.** { *; }

-dontwarn cn.xmzt.www.pay.bean.**
-keep class cn.xmzt.www.pay.bean.** { *; }

-dontwarn cn.xmzt.www.roomdb.**
-keep class cn.xmzt.www.roomdb.** { *; }

-dontwarn cn.xmzt.www.selfdrivingtools.bean.**
-keep class cn.xmzt.www.selfdrivingtools.bean.** { *; }

-dontwarn cn.xmzt.www.smartteam.bean.**
-keep class cn.xmzt.www.smartteam.bean.** { *; }

-dontwarn cn.xmzt.www.ticket.bean.**
-keep class cn.xmzt.www.ticket.bean.** { *; }

-dontwarn cn.xmzt.www.rxjava2.**
-keep class cn.xmzt.www.rxjava2.** { *; }

-dontwarn net.jg.framework.**
-keep class net.jg.framework.** { *; }

-dontwarn cn.xmzt.www.wxapi.**
-keep class cn.xmzt.www.wxapi.** { *; }

-dontwarn cn.xmzt.www.zxing.**
-keep class cn.xmzt.www.zxing.** { *; }

-dontwarn cn.xmzt.www.config.**
-keep class cn.xmzt.www.config.** { *; }

-dontwarn cn.xmzt.www.view.**
-keep class cn.xmzt.www.view.** { *; }

-dontwarn cn.xmzt.www.popup.**
-keep class cn.xmzt.www.popup.** { *; }

-dontwarn cn.xmzt.www.dialog.**
-keep class cn.xmzt.www.dialog.** { *; }

-dontwarn cn.xmzt.www.suspension.**
-keep class cn.xmzt.www.suspension.** { *; }

-dontwarn cn.xmzt.www.share.**
-keep class cn.xmzt.www.share.** { *; }

-dontwarn cn.xmzt.www.receiver.**
-keep class cn.xmzt.www.receiver.** { *; }

-dontwarn cn.xmzt.www.service.**
-keep class cn.xmzt.www.service.** { *; }

#3D 地图
-keep class com.amap.api.mapcore.**{*;}
-keep class com.amap.api.maps.**{*;}
-keep class com.autonavi.amap.mapcore.*{*;}

#定位
-keep class com.amap.api.location.**{*;}
-keep class com.loc.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

# 搜索
-keep class com.amap.api.services.**{*;}

# 2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}

# 导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}
#友盟推送
-dontwarn com.umeng.**
-keep class com.umeng.** {*;}

-keep public class **.R$*{
   public static final int *;
}
#语音
-keep class com.iflytek.cloud.**{*;}
-keep class com.iflytek.msc.**{*;}
-keep interface com.iflytek.**{*;}

#对讲
-dontwarn org.anyrtc.**
-keep class org.anyrtc.**{*;}
-dontwarn org.ar.**
-keep class org.ar.**{*;}
-dontwarn org.webrtc.**
-keep class org.webrtc.**{*;}
# OkHttp3
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
# javawriter
-dontwarn com.squareup.**
-keep class com.squareup.**{*;}

# Okio
-dontwarn okio.**
-keep class okio.**{*;}
# retrofit2
-dontwarn retrofit2.**
-keep class retrofit2.**{*;}
# rxjava2
-dontwarn io.reactivex.**
-keep class io.reactivex.**{*;}
# youth.banner
-dontwarn com.youth.banner.**
-keep class com.youth.banner.**{*;}
# 沉浸式crossoverone:StatusBarUtil
-dontwarn crossoverone.statuslib.**
-keep class crossoverone.statuslib.**{*;}
# 时间选择Android-PickerView
-dontwarn com.bigkoo.pickerview.**
-keep class com.bigkoo.pickerview.**{*;}
# facebook.fresco
-dontwarn com.facebook.**
-keep class com.facebook.**{*;}
# Glide
-dontwarn com.bumptech.glide.**
-keep class com.bumptech.glide.**{*;}
-dontwarn jp.wasabeef.glide.transformations.**
-keep class jp.wasabeef.glide.transformations.**{*;}

# 工具库utilcode
-dontwarn com.blankj.utilcode.**
-keep class com.blankj.utilcode.**{*;}
# 下拉刷新、上拉加载更多scwang.smartrefresh
-dontwarn com.scwang.smartrefresh.**
-keep class com.scwang.smartrefresh.**{*;}
# eventbus
-dontwarn org.greenrobot.eventbus.**
-keep class org.greenrobot.eventbus.**{*;}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keepclassmembers class * {
      @org.greenrobot.eventbus.Subscribe <methods>;
}
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#双向拖动的seekBar
-dontwarn com.jaygoo.widget.**
-keep class com.jaygoo.widget.**{*;}
#Fragmentation框架: 集成一个Activity对应多个Fragment模式
-dontwarn me.yokeyword.fragmentation.**
-keep class me.yokeyword.fragmentation.**{*;}
-dontwarn com.daimajia.**
-keep class com.daimajia.**{*;}
# wechat
-dontwarn com.tencent.mm.**
-keep class com.tencent.mm.**{*;}
-dontwarn com.tencent.**
-keep class com.tencent.**{*;}
#支付宝
-dontwarn com.alipay.**
-keep class com.alipay.**{*;}
-dontwarn com.ta.utdid2.**
-keep class com.ta.utdid2.**{*;}
-dontwarn com.ut.device.**
-keep class com.ut.device.**{*;}
-dontwarn org.json.alipay.**
-keep class org.json.alipay.**{*;}
#视频播放器gsyvideoplayer
-dontwarn com.shuyu.gsyvideoplayer.**
-keep class com.shuyu.gsyvideoplayer.**{*;}
-dontwarn com.shuyu.gsy.**
-keep class com.shuyu.gsy.**{*;}
-dontwarn com.danikula.videocache.**
-keep class com.danikula.videocache.**{*;}
-dontwarn shuyu.com.androidvideocache.**
-keep class shuyu.com.androidvideocache.**{*;}
-dontwarn tv.danmaku.ijk.media.**
-keep class tv.danmaku.ijk.media.**{*;}
#云信
-dontwarn com.netease.**
-keep class com.netease.**{*;}
-dontwarn com.coloros.mcssdk.**
-keep class com.coloros.mcssdk.**{*;}
-dontwarn com.faceunity.wrapper.**
-keep class com.faceunity.wrapper.**{*;}
-dontwarn com.spap.**
-keep class com.spap.**{*;}
-dontwarn com.google.protobuf.micro.**
-keep class com.google.protobuf.micro.**{*;}
-dontwarn org.apache.lucene.**
-keep class org.apache.lucene.**{*;}
#云信小米推送
-dontwarn com.xiaomi.**
-keep class com.xiaomi.**{*;}
-dontwarn org.apache.thrift.**
-keep class org.apache.thrift.**{*;}
#云信小米推送
-dontwarn com.vivo.**
-keep class com.vivo.**{*;}
#云信java-json
-dontwarn org.json.**
-keep class org.json.**{*;}
# 魅族推送
-dontwarn com.meizu.cloud.**
-keep class com.meizu.cloud.**{*;}
# me.everything
-dontwarn me.everything.**
-keep class me.everything.**{*;}
# me.leolin
-dontwarn me.leolin.shortcutbadger.**
-keep class me.leolin.shortcutbadger.**{*;}

# rotatephotoview 或者photoview
-dontwarn uk.co.senab.photoview.**
-keep class uk.co.senab.photoview.**{*;}
# org.adw.library:discrete-seekbar
-dontwarn org.adw.library.widgets.discreteseekbar.**
-keep class org.adw.library.widgets.discreteseekbar.**{*;}
# com.zhy:base-adapter
-dontwarn com.zhy.base.adapter.**
-keep class com.zhy.base.adapter.**{*;}
# com.yanzhenjie.permission
-dontwarn com.yanzhenjie.permission.**
-keep class com.yanzhenjie.permission.**{*;}
# com.yinglan.scrolllayout:scrolllayout
-dontwarn com.yinglan.scrolllayout.**
-keep class com.yinglan.scrolllayout.**{*;}
#fastjson
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.**{*;}
#极光推送
-dontwarn cn.jcore.client.android.**
-keep class cn.jcore.client.android.** { *; }
-dontwarn cn.jpush.client.android.**
-keep class cn.jpush.client.android.** { *; }
-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep public class com.sina.** {
    *;
}
#lottie动画
-dontwarn com.airbnb.lottie.**
-keep class com.airbnb.lottie.**{*;}
#com.zyp.cardview:cardview
-dontwarn com.zyp.cardview.**
-keep class com.zyp.cardview.**{*;}
#LuckSiege.PictureSelector
-dontwarn com.luck.picture.**
-keep class com.luck.picture.**{*;}
#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
