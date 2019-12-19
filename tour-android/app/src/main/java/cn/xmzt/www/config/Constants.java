package cn.xmzt.www.config;

import android.os.Environment;

import cn.xmzt.www.base.TourApplication;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class Constants {

    final public static String OK = "X0000";//网络请求成功状态码

    final public static String baseUrl = "http://gateway.xmzt.cn/";//域名更换

    final public static long UPDATE_TIME_MAP_INFO = 2 * 1000; // 界面用户同步时间 5秒刷新一次
    final public static long SEND_TIME_MAP_INFO = 2 * 1000; // 界面用户发送时间 5秒发送一次
    final public static int TIMER_UPDATE_PROGRESS = 20; // 共享导航界面数据刷新进度条每20毫秒刷新一次
    final public static long TIME_OUT = 60 * 1000 * 5L; // 5分钟之内 在线 5分钟之外离线
    /**
     * 类型：1=积分规则
     * 2=签到奖励规则
     * 3=关于我们
     * 4=线路-自驾须知
     * 5=线路-退改规则
     * 6=线路-安全须知
     * 7=线路-服务保障
     * 8=酒店-安全须知
     * 9=酒店-扣款说明
     * 10=酒店-特别提醒
     * 11:门票-预订条款
     * 12服务协议
     * 13免责申明
     * 14隐私政策
     * 15用户注销协议
     * 16预定条款
     * 17委托服务协议
     */
    private static String xzUrl = "https://weixin.xmzt.cn/#/pages/textview/index";//须知url
    private static String tipsUrl = " https://weixin.xmzt.cn/#/pages/tips/guideDetails/index";//行程相关的url
    private static String travelListingUrl = " https://weixin.xmzt.cn/#/pages/tips/TravelListing/index";//出行清单页面


    public static String token = "a19d5ab4918a4b48ac8bb54dff270a3a";//token

//    public static UserInfoBean userInfoBean;//用户信息

    public static String phone = "15595625091";//用户手机号码

    public static double mLatitude = 22.583757866753473; // 维度
    public static double mLongitude = 113.8640673828125; // 经度
    public static String mCity = "深圳"; // 拿到定位的城市
    public static String mAddress = "深圳"; // 拿到定位的城市
    public static String mCityCode = "440300" ;//默认深圳code
    public static String mLocation = "113.87370575878904,22.595243590396187"; // 默认的一个经纬度
    public static boolean mIsOtherLocation = false; // 判断当前其他界面是否正在定位

    // 音频下载位置
    public static final String SCENIC_VOICE_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/cn.xmzt.www/scenic_voice_file/";
    // 自定义地图存放位置
    public static final String ASSETS_MAP_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/cn.xmzt.www/map/";
    // 地图背景存放位置
    public static final String ASSETS_IMG_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/cn.xmzt.www/img/";
    // 音频录制存放位置
    public static final String TALK_AUDIO_RECORD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/cn.xmzt.www/audio_record_file/";
    // crash 文件
    public static final String CRASH_FILE = Environment.getExternalStorageDirectory().getPath() + "/android/cn.xmzt.www/crash_log/";

    // 地图的自定义样式id  定义在线地图样式使用
    public static final String MAP_STYLE_ID = "9109753e81ab5ce86f986947aeb26edb";
    /**
     * 类型：1=积分规则
     * 2=签到奖励规则
     * 3=关于我们
     * 4=线路-自驾须知
     * 5=线路-退改规则
     * 6=线路-安全须知
     * 7=线路-服务保障
     * 8=酒店-安全须知
     * 9=酒店-扣款说明
     * 10=酒店-特别提醒
     * 11:门票-预订条款
     * 12服务协议
     * 13免责申明
     * 14隐私政策
     * 15用户注销协议
     * 16预定条款
     * 17委托服务协议
     *获取须知url
     * @param type  4自驾注意事项说明 5线路退改规则说明 6安全须知 17委托服务协议
     * @return
     */
    public static String getXzUrl(int type) {
        String param="?type="+type;
        return xzUrl+param;
    }

    /**
     * 行程相关的url
     * @param types 6 是行程清单 7 是美食指南 8 是酒店介绍
     * @param relationId 相关的id
     * @return
     */
    public static String getTipsUrl(String types,String relationId) {
        String param="?types="+types+"&routeid="+relationId;
        if("6".equals(types)){
            return travelListingUrl;
        }
        return tipsUrl+param;
    }

    /**
     * 播放服务
     */
    public class PlayerMag{
        public static final int PLAY_MAG = 1;//开始播放
        public static final int PAUSE = 2;//暂停播放
        public static final int STOP = 3;//停止播放
    }

    /**
     * 广播banner的类型
     */
    public class BannerType{
        // 0:首页,1:景区,2:酒店, 3:线路, 4:门票 banner
        public static final int ADVERTISE_BANNER_HOME = 0;
        public static final int ADVERTISE_BANNER_SCENIC = 1;
        public static final int ADVERTISE_BANNER_HOTEL = 2;
        public static final int ADVERTISE_BANNER_ROUTE = 3;
        public static final int ADVERTISE_BANNER_TICKET = 4;
    }

    //下面是新的微信appid和appsecret
    public static final String WX_APP_ID = "wx3b08b3acfc7dbcb9";
    public static final String WX_SECRET = "5a213cbbe951b9cd6a3beba237aac1be";
    private static IWXAPI wx_api; //全局的微信api对象

    public static IWXAPI getWx_api() {
        if (null == wx_api) {
            wx_api = WXAPIFactory.createWXAPI(TourApplication.context, Constants.WX_APP_ID, true);
            wx_api.registerApp(Constants.WX_APP_ID);
        }
        return wx_api;
    }

    public static boolean mServiceIsStart = false;
    public static String mServiceIsStartConteng = "";
    public static long mScenicId = 0; // 景区编号
    public static int mScenicSpotId = 0; // 景点编号
    public static double mVoiceLatitude = 0.0; // 当前播放音频文件的经纬度
    public static double mVoiceLongitude = 0.0; // 当前播放音频文件的经纬度

    public static class KeywordsType{
        // 景区需要的编号
        public static final int FUNTYPES_SCENIC = 1001;
        // 景区需要的音频编号
        public static final int FUNTYPES_SCENIC_VOICE = 1002;
        //线路搜索 类型
        public static int TYPE_ROUTE_SEARCH=1003;
        //景区导览首页Banner 类型
        public static int TYPE_SCENIC_BANNER = 1004;
        // 景区导览界面自动播放是否打开保存信息
        public static int TYPE_SCENIC_AUTO_PLAYING = 1010;
    }
    // 智能组队中我的车辆变化之后的保存，可以改变之后下一次发送出去就是改变之后的数据
    public static String mMyCarInfo = ""; // 车辆信息
    public static String mMyNickName = ""; // 昵称信息
    public static String mGroupName = ""; // 群昵称信息

    public static final int DISTANCE = 150; // 集结点坐标距离

    public static final String CLIENTTELNUM = "400-666-1235"; // 晓风提供的后台客服号码
}
