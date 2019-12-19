package cn.xmzt.www.main.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.blankj.utilcode.util.ActivityUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseActivity;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.bean.ToRouteListBus;
import cn.xmzt.www.databinding.ActivityMainBinding;
import cn.xmzt.www.dialog.ConfirmDialog;
import cn.xmzt.www.dialog.GuideHomeDialog;
import cn.xmzt.www.dialog.LoginOutDialig;
import cn.xmzt.www.dialog.NewVersionDialog;
import cn.xmzt.www.helper.UserHelper;
import cn.xmzt.www.home.fragment.HomeFragment;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.intercom.common.ToastHelper;
import cn.xmzt.www.intercom.event.AudioListenStatusEvent;
import cn.xmzt.www.intercom.event.RefreshMyTalkGroupList;
import cn.xmzt.www.intercom.event.RefreshSchedulingListBus;
import cn.xmzt.www.intercom.fragment.SchedulingListFragment;
import cn.xmzt.www.intercom.preference.Preferences;
import cn.xmzt.www.main.globals.TalkManage;
import cn.xmzt.www.main.vm.MainViewModel;
import cn.xmzt.www.mine.activity.LoginActivity;
import cn.xmzt.www.mine.bean.AppVersionBean;
import cn.xmzt.www.mine.event.LogOutEvent;
import cn.xmzt.www.mine.event.LoginEvent;
import cn.xmzt.www.mine.event.PushMessageEvent;
import cn.xmzt.www.mine.fragment.MineFragment;
import cn.xmzt.www.nim.im.login.LogoutHelper;
import cn.xmzt.www.nim.im.main.helper.SystemMessageUnreadManager;
import cn.xmzt.www.nim.im.main.reminder.ReminderManager;
import cn.xmzt.www.nim.uikit.api.model.main.LoginSyncDataStatusObserver;
import cn.xmzt.www.nim.uikit.common.ui.dialog.DialogMaker;
import cn.xmzt.www.nim.uikit.support.permission.MPermission;
import cn.xmzt.www.nim.uikit.support.permission.annotation.OnMPermissionDenied;
import cn.xmzt.www.nim.uikit.support.permission.annotation.OnMPermissionGranted;
import cn.xmzt.www.nim.uikit.support.permission.annotation.OnMPermissionNeverAskAgain;
import cn.xmzt.www.route.fragment.RouteFrament;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.selfdrivingtools.event.NavigationTypeEvent;
import cn.xmzt.www.selfdrivingtools.event.UpdateGroupDefaultEvent;
import cn.xmzt.www.selfdrivingtools.fragment.ElectricGuideFragment;
import cn.xmzt.www.service.LocationService;
import cn.xmzt.www.utils.APKVersionCodeUtils;
import cn.xmzt.www.utils.SPUtils;
import cn.xmzt.www.utils.SchemeActivityUtil;
import cn.xmzt.www.utils.StatusBarUtil;
import cn.xmzt.www.utils.ToastUtils;
import cn.xmzt.www.utils.WriteToSdUtils;

/**
 * @describe 首页
 */
public class MainActivity extends BaseActivity {
    private static final String EXTRA_APP_QUIT = "APP_QUIT";
    private ActivityMainBinding dataBinding;
    private MainViewModel viewModel;
    private FragmentStatePagerAdapter mAdapter;
    /**
     * 四个个Fragment（页面）
     */
    private HomeFragment mHomeFragment;
    private RouteFrament mRouteFrament;
    private ElectricGuideFragment mElectricGuideFragment;
    private SchedulingListFragment mSchedulingListFragment;
    private MineFragment mMineFragment;
    private Fragment currentFragment;

    private ArrayList<Fragment> mTabs = new ArrayList<Fragment>();
    private ArrayList<LottieAnimationView> animTabs = new ArrayList<LottieAnimationView>();
    private ArrayList<TextView> nameTabs = new ArrayList<TextView>();

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

//        StatusBarUtil.setStatusBarLightMode(this,false);
        StatusBarUtil.setFullScreen(this);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new MainViewModel();
        viewModel.versionResult.observe(this, new androidx.lifecycle.Observer<AppVersionBean>() {
            @Override
            public void onChanged(AppVersionBean appVersionBean) {
                String data = appVersionBean.getVersion().substring(1).trim();
                if (data.contains(" ")){
                    data = data.replace(" ","");// 去掉中间的空格
                }
                double datad = Double.parseDouble(data);
                String versionName= APKVersionCodeUtils.getVerName(getApplicationContext());
                double versionCur = Double.parseDouble(versionName);
                if (datad > versionCur) {
                    showNewVersionDialog(appVersionBean);
                }
            }
        });
        initView();
        //不保留后台活动，从厂商推送进聊天页面，会无法退出聊天页面
        if (savedInstanceState == null && parseIntent()) {
            return;
        }
        //parseIntent();
        initReceiver();

        observerSyncDataComplete();
        registerSystemMessageObservers(true);
        requestSystemMessageUnreadCount();
        requestBasicPermission();
        //setPermission();
//        getUserBasicInfo();
        if (!SPUtils.getBoolean("main", false)) {
            GuideHomeDialog dialog = new GuideHomeDialog(this);
            dialog.show();
            SPUtils.putBoolean("main", true);
        }
        viewModel.getUserBasicInfo();
        viewModel.getLatestVersion(getApplicationContext());

        // 监听用户登录情况
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(
                new Observer<StatusCode> () {
                    public void onEvent(StatusCode status) {
                        // 被踢出、账号被禁用、密码错误等情况，自动登录失败，需要返回到登录界面进行重新登录操作
                        /**
                         * INVALID	未定义
                         * UNLOGIN	未登录/登录失败
                         * NET_BROKEN	网络连接已断开
                         * CONNECTING	正在连接服务器
                         * LOGINING	正在登录中
                         * SYNCING	正在同步数据
                         * LOGINED	已成功登录
                         * KICKOUT	被其他端的登录踢掉
                         * KICK_BY_OTHER_CLIENT	被同时在线的其他端主动踢掉
                         * FORBIDDEN	被服务器禁止登录
                         * VER_ERROR	客户端版本错误
                         * PWD_ERROR	用户名或密码错误
                         */
                        switch (status){
                            case INVALID:
//                                ToastUtils.showText(MainActivity.this,"未定义");
                                break;
                            case UNLOGIN:
//                                ToastUtils.showText(MainActivity.this,"未登录/登录失败");
                                break;
                            case NET_BROKEN:
//                                ToastUtils.showText(MainActivity.this,"网络连接已断开");
                                break;
                            case CONNECTING:
//                                ToastUtils.showText(MainActivity.this,"正在连接服务器");
                                break;
                            case LOGINING:
//                                ToastUtils.showText(MainActivity.this,"正在登录中");
                                break;
                            case SYNCING:
//                                ToastUtils.showText(MainActivity.this,"正在同步数据");
                                break;
                            case LOGINED:
//                                ToastUtils.showText(MainActivity.this,"已成功登录");
                                break;
                            case KICKOUT:
//                                ToastUtils.showText(MainActivity.this,"被其他端的登录踢掉");
//                                showConfirmDialog();
                                break;
                            case KICK_BY_OTHER_CLIENT:
//                                ToastUtils.showText(MainActivity.this,"被同时在线的其他端主动踢掉");
//                                showConfirmDialog();
                                break;
                            case FORBIDDEN:
//                                ToastUtils.showText(MainActivity.this,"被服务器禁止登录");
                                break;
                            case VER_ERROR:
//                                ToastUtils.showText(MainActivity.this,"客户端版本错误");
                                break;
                            case PWD_ERROR:
//                                ToastUtils.showText(MainActivity.this,"用户名或密码错误");
                                break;
                            default:
                                break;
                        }
                    }
                }, true);
    }

    private LoginOutDialig confirmDialog;

    private void showConfirmDialog(){
//        View view = View.inflate(StatusBarService.this, R.layout.dialog_confirm, null);
//        AlertDialog.Builder b = new AlertDialog.Builder(StatusBarService.this);
//        b.setView(view);
//        final AlertDialog d = b.create();
//        d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG); //系统中关机对话框就是这个属性
//        //d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);  //窗口可以获得焦点，响应操作
//        //d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);  //窗口不可以获得焦点，点击时响应窗口后面的界面点击事件
//        d.show();
//
//        Button yesButton = (Button) view.findViewById(R.id.yes_button);
//        Button canclButton = (Button) view.findViewById(R.id.cancl_button);
//
//        yesButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_REQUEST_SHUTDOWN);
//                intent.putExtra(Intent.EXTRA_KEY_CONFIRM, false);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            }
//        });
//
//        canclButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                d.dismiss();
//            }
//        });
        // TODO 弹出的时候会报异常 暂时不添加
        if (confirmDialog == null){
            confirmDialog = new LoginOutDialig(MainActivity.this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmDialog.dismiss();
                    kickOut();
                    if (v.getId() == R.id.confirm_tv){
                        //没有登录
                        LoginActivity.start(MainActivity.this);
                    }
                    confirmDialog = null;
                }
            });
        }
        // 增加这句代码 任何界面都可现实
        confirmDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        confirmDialog.show();
    }

    private void kickOut(){
        TourApplication.setToken(null);
        TourApplication.setUser(null);
        UserHelper.logout();
        EventBus.getDefault().post(new LogOutEvent());
        //删除授权
        UMShareAPI.get(MainActivity.this).deleteOauth(MainActivity.this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {

            }
        });
    }

    protected void initView() {
        dataBinding.setActivity(this);
        initTabIndicator();
        initFragments();
//        dataBinding.mViewPager.setCanScroll(false);
//        dataBinding.mViewPager.setAdapter(mAdapter);
//        dataBinding.mViewPager.setOffscreenPageLimit(4);
        // 将Assets里面的自定义地图写入到sd卡中
        WriteToSdUtils.setAssetsDataToSd(getApplicationContext());
    }

    /**
     * 初始化TabBar
     */
    private void initTabIndicator() {
        animTabs.add(dataBinding.ivTabHome);
        animTabs.add(dataBinding.ivTabRoute);
        animTabs.add(dataBinding.ivTabSelfDriving);
        animTabs.add(dataBinding.ivTabXc);
        animTabs.add(dataBinding.ivTabUser);

        nameTabs.add(dataBinding.tvTabHome);
        nameTabs.add(dataBinding.tvTabRoute);
        nameTabs.add(dataBinding.tvTabSelfDriving);
        nameTabs.add(dataBinding.tvTabXc);
        nameTabs.add(dataBinding.tvTabUser);

        dataBinding.ivTabHome.playAnimation();
        dataBinding.tvTabHome.setTextColor(Color.parseColor("#24A4FF"));
    }

    /**
     * 初始化主页面的四个主页面Fragment
     */
    private void initFragments() {
        if (mTabs != null && mTabs.size() == 0) {
            mHomeFragment = new HomeFragment();
            mRouteFrament = new RouteFrament();
            mElectricGuideFragment = new ElectricGuideFragment();
            mSchedulingListFragment = new SchedulingListFragment();
            mMineFragment = new MineFragment();
            mTabs.add(mHomeFragment);
            mTabs.add(mRouteFrament);
            mTabs.add(mElectricGuideFragment);
            mTabs.add(mSchedulingListFragment);
            mTabs.add(mMineFragment);
            setCurrentTab(0);
        }
    }

    private void showFragment(int position) {
        FragmentManager mSupportFragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = mSupportFragmentManager.beginTransaction();
        Fragment showFragment = mTabs.get(position);
        Fragment sfrgment = mSupportFragmentManager.findFragmentByTag("ragment_" + position);
        if (currentFragment != null) {
            ft.hide(currentFragment);
        }
        if (sfrgment == null && !showFragment.isAdded()) {
            ft.add(R.id.fragmentLayout, showFragment, "ragment_" + position);
            Log.e("shiyong", "111111111111111111111111111");
        } else {
            if (sfrgment == showFragment) {
                ft.show(showFragment);
                Log.e("shiyong", "2222222222222222222222222");
            } else {
                ft.add(R.id.fragmentLayout, showFragment, "ragment_" + position);
                Log.e("shiyong", "3333333333333333333333");
            }
        }
        currentFragment = showFragment;
        ft.commitAllowingStateLoss();
    }

    /**
     * 重置TabBar的图标文字颜色
     */
    private void resetTabsIconAlpha() {
        for (int i = 0; i < animTabs.size(); i++) {
            animTabs.get(i).cancelAnimation();
            animTabs.get(i).setFrame(0);
            nameTabs.get(i).setTextColor(Color.parseColor("#666666"));
        }
    }

    private int currentTab=0;
    /**
     * 设置当前的TabBar
     *
     * @param index 表示第几个tabbar
     */
    private void setCurrentTab(int index) {
        if (index == 1 || index == 3) {
            if (index == 3) {
                if (TextUtils.isEmpty(TourApplication.getToken())) {
                    //没有登录
                    LoginActivity.start(MainActivity.this);
                    return;
                }
            }
            StatusBarUtil.setStatusBarLightMode(MainActivity.this, true);
        } else {
            StatusBarUtil.setStatusBarLightMode(MainActivity.this, false);
        }
        resetTabsIconAlpha();
        animTabs.get(index).playAnimation();
        nameTabs.get(index).setTextColor(Color.parseColor("#24A4FF"));
        showFragment(index);
        if (index == 3&&currentTab!=index) {
            if (!TextUtils.isEmpty(TourApplication.getToken())) {
                EventBus.getDefault().post(new RefreshSchedulingListBus(1));
//                EventBus.getDefault().postSticky(new MessageRefreshEvent(1001));
            }
        }
        currentTab=index;
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void loginEvent(LoginEvent event) {
        viewModel.getUserBasicInfo();
    }
    @Subscribe
    public void talkGroupListEvent(RefreshMyTalkGroupList event) {
        viewModel.getTalkGroupList(false);
    }

    @Subscribe
    public void GroupDefaultEvent(UpdateGroupDefaultEvent event) {
        if (!TextUtils.isEmpty(event.getGroupid()) && !TextUtils.isEmpty(TourApplication.getToken())) { // token不为空并且 群编号不为空
            viewModel.setUserGroupOrPosition(TourApplication.getToken(),event.getGroupid(),null);
        }
    }
    @Subscribe
    public void onJGPushMessageEvent(PushMessageEvent mPushMessageEvent) {
        viewModel.getMsgUnreadCount();
    }
    @Subscribe
    public void onAudioListenStatusEvent(AudioListenStatusEvent event) {
        viewModel.getAutoPlayGroups();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tabLayout1: {
                setCurrentTab(0);
                break;
            }
            case R.id.tabLayout2: {
                setCurrentTab(1);
                break;
            }
            case R.id.tabLayout3: {
                setCurrentTab(2);
                break;
            }
            case R.id.tabLayout4: {
                setCurrentTab(3);
                break;
            }
            case R.id.tabLayout5: {
                setCurrentTab(4);
                break;
            }
            default: {
                break;
            }
        }
    }

    @Subscribe
    public void toRouteList(ToRouteListBus event) {
        if (event.getType() == 1) {
            setCurrentTab(1);
            StatusBarUtil.setStatusBarLightMode(MainActivity.this, true);
        }
        if (event.getType() == 2) {
            setCurrentTab(3);
            StatusBarUtil.setStatusBarLightMode(MainActivity.this, true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 只要用户在导航的情况下回到主页，说明离开了当前群，关闭导航事件
        EventBus.getDefault().post(new NavigationTypeEvent(0));
    }

    private void initReceiver() {
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        connectionReceiver = new NetConnectionReceiver();
//        LocalBroadcastManager.getInstance(this).registerReceiver(connectionReceiver, filter);

        // 定位系统服务启动
       startService(new Intent(this, LocationService.class));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == 50) { //RESULT_OK = -1
            if (data != null) {
                Bundle bundle = data.getExtras();
                String scanResult = bundle.getString("qr_scan_result");
                //将扫描出的信息显示出来
//                Toast.makeText(this, "扫描的内容是" + scanResult, Toast.LENGTH_SHORT).show();
                if (scanResult.contains("xmzt.cn") && scanResult.startsWith("http")) {
                    SchemeActivityUtil.startToActivity(this, 4, "", scanResult);
                } else {
                    ToastUtils.showText(this, "二维码格式有误");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        TalkManage.getInstance().logout();
        //关闭悬浮窗
    }

    // 启动
    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    // 注销
    public static void logout(Context context, boolean quit) {
        Intent extra = new Intent();
        extra.putExtra(EXTRA_APP_QUIT, quit);
        start(context, extra);
    }

    private void onLogout() {
        Preferences.saveUserToken("");
        // 清理缓存&注销监听
        LogoutHelper.logout();
        // 启动登录
        LoginActivity.start(this);
        finish();
    }

    /****************************************同步数据*******************************************************/

    private void observerSyncDataComplete() {
        boolean syncCompleted = LoginSyncDataStatusObserver.getInstance().observeSyncDataCompletedEvent(new Observer<Void>() {
            @Override
            public void onEvent(Void v) {
                DialogMaker.dismissProgressDialog();
            }
        });
        //如果数据没有同步完成，弹个进度Dialog
        if (!syncCompleted) {
            DialogMaker.showProgressDialog(this, getString(R.string.prepare_data)).setCanceledOnTouchOutside(false);
        }
    }

    /****************************************消息通知*******************************************************/

    private boolean parseIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_APP_QUIT)) {
            intent.removeExtra(EXTRA_APP_QUIT);
            onLogout();
            return true;
        }
        return false;
    }

    /****************************************注册广播*******************************************************/

    /**
     * 注册/注销系统消息未读数变化
     */
    private void registerSystemMessageObservers(boolean register) {
        NIMClient.getService(SystemMessageObserver.class).observeUnreadCountChange(sysMsgUnreadCountChangedObserver, register);
    }

    /**
     * 查询系统消息未读数
     */
    private void requestSystemMessageUnreadCount() {
        int unread = NIMClient.getService(SystemMessageService.class).querySystemMessageUnreadCountBlock();
        SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unread);
        ReminderManager.getInstance().updateUnreadMessageNumIntercom(unread);
    }

    private Observer<Integer> sysMsgUnreadCountChangedObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer unreadCount) {
            SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unreadCount);
            ReminderManager.getInstance().updateUnreadMessageNumIntercom(unreadCount);
        }
    };

    /****************************************权限相关*******************************************************/

    private static final int BASIC_PERMISSION_REQUEST_CODE = 100;
    private static final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.VIBRATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private void requestBasicPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getInfo();
        }
        // Android 10.0之后存储权限需要adb 命令处理 adb shell sm set-isolated-storage on
        MPermission.printMPermissionResult(true, this, BASIC_PERMISSIONS);
        MPermission.with(MainActivity.this)
                .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(BASIC_PERMISSIONS)
                .request();
    }
    private void getInfo() {
        BufferedReader reader = null;
        String content = "";
        try {
            Process process = Runtime.getRuntime().exec("adb shell sm set-isolated-storage on");
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuffer output = new StringBuffer();
            int read;
            char[] buffer = new char[1024];
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();
            content = output.toString();
            Log.d("manman", "content = " + content);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
        try {
            //ToastHelper.showToast(this, "授权成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        try {
            ToastHelper.showToast(this, "未全部授权，部分功能可能无法正常运行！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
    }

    long temptime;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        /*if (System.currentTimeMillis() - temptime > 2000) // 2s内再次选择back键有效
        {
            ToastUtils.showText(this,"请在按一次退出");
            temptime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0); //凡是非零都表示异常退出!0表示正常退出!
        }*/
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
    private NewVersionDialog newVersionDialog;
    private void showNewVersionDialog(AppVersionBean appVersionBean) {
        if (null == newVersionDialog) {
            newVersionDialog = new NewVersionDialog(this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downLoad(appVersionBean.getUrl());
                }
            });
            if (appVersionBean.isForceUpgrade()) {
                newVersionDialog.setCancelable(false);
                newVersionDialog.setCancelGONE();
            }
        }
        newVersionDialog.setViewData(appVersionBean);
        newVersionDialog.show();
    }
    private void downLoad(String url) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (Exception e) {
            ToastUtils.showText(this,"新版本下载失败");
        }
    }
}
