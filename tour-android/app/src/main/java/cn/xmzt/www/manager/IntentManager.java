package cn.xmzt.www.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import cn.xmzt.www.intercom.activity.GroupEditorActivity;
import cn.xmzt.www.intercom.activity.GroupPersonalInfoActivity;
import cn.xmzt.www.main.activity.WelcomeActivity;
import cn.xmzt.www.mine.activity.CommonVehiclesActivity;
import cn.xmzt.www.mine.activity.MineAddCarActivity;
import cn.xmzt.www.route.activity.RoutePhotoViewActivity;
import cn.xmzt.www.route.activity.VideoPlayActivity;
import cn.xmzt.www.selfdrivingtools.activity.AtlasActivity;
import cn.xmzt.www.selfdrivingtools.activity.CarCheckInfoActivity;
import cn.xmzt.www.selfdrivingtools.activity.GuideInfoActivity;
import cn.xmzt.www.selfdrivingtools.activity.OfflineFileManagerActivity;
import cn.xmzt.www.selfdrivingtools.activity.ScenicFeedBackActivity;
import cn.xmzt.www.selfdrivingtools.activity.ScenicSpotDetailActivity;
import cn.xmzt.www.selfdrivingtools.activity.ScenicSpotMapActivity;
import cn.xmzt.www.selfdrivingtools.activity.ScenicSpotSearchActivity;
import cn.xmzt.www.selfdrivingtools.activity.ScenicSpotSearchResultActivity;
import cn.xmzt.www.selfdrivingtools.activity.SharedNavigationGoHereActivityBackUp;
import cn.xmzt.www.selfdrivingtools.activity.SharedNavigationGuideActivity;
import cn.xmzt.www.selfdrivingtools.activity.SharedNavigationGuideActivityBackUp;
import cn.xmzt.www.selfdrivingtools.activity.WisdomGuideActivity;
import cn.xmzt.www.selfdrivingtools.activity.SharedNavigationGoHereActivity;
import cn.xmzt.www.selfdrivingtools.activity.SharedNavigationMapActivity;
import cn.xmzt.www.selfdrivingtools.activity.SharedNavigationSettingDestinationActivity;
import cn.xmzt.www.smartteam.activity.SmartTeamActivity;
import cn.xmzt.www.smartteam.activity.SmartTeamAddCarActivity;
import cn.xmzt.www.smartteam.activity.SmartTeamGroupInfoActivity;
import cn.xmzt.www.smartteam.activity.SmartTeamMapActivity;
import cn.xmzt.www.smartteam.activity.SmartTeamSettingDestinationAndTimeActivity;
import cn.xmzt.www.smartteam.activity.TripSignInActivity;
import cn.xmzt.www.smartteam.activity.TripSignInListActivity;

import java.util.ArrayList;
import java.util.List;

public class IntentManager {

    private final String TAG = "IntentManager";

    public static final int EDIT_ACTIVITY = 888;

    private IntentManager() {
    }

    public static final IntentManager getInstance() {
        return IntentManagerHolder.instance;
    }

    private void startActivity(Context context, Intent intent) {
        if (context == null) {
            return;
        }
        context.startActivity(intent);
        // page jump anim
//        if (context instanceof Activity) {
//            ((Activity) context).overridePendingTransition(R.anim.anim_push_left_in,
//                    R.anim.anim_push_left_out);
//        }
    }

    public void startActivity(Context context, Class clz) {
        startActivity(context, new Intent(context, clz));
    }

    private void startAcitivityForResult(Activity context, Intent intent, int requestCode) {
        if (context == null) {
            return;
        }
        context.startActivityForResult(intent, requestCode);
    }

    public void goActivity(Context context, Intent intent) {
        startActivity(context, intent);
    }


    private static class IntentManagerHolder {
        private static final IntentManager instance = new IntentManager();
    }
    /**
     * jump to scenic guide main activity
     * @param context
     */
    public void goWisdomGuideActivity(Context context){
        startActivity(context, WisdomGuideActivity.class);
    }
    /**
     * jump to scenic spot map activity
     * @param context
     * @param id scenicId
     */
    public void goScenicSpotMapActivity(Context context,long id,String groupId){
        Intent intent = new Intent(context, ScenicSpotMapActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("groupid",groupId);
        startActivity(context,intent);
    }
    /**
     * jump to images activity
     * @param context
     * @param url
     */
    public void goPhotoViewActivity(Context context, String url, ArrayList<String> list, int position){
        Intent intent = new Intent(context, RoutePhotoViewActivity.class);
        intent.putExtra("A",position); // 当前点击了第几个
        intent.putStringArrayListExtra("B",list); // 传递图片集合 只传递两个中的一个
        intent.putExtra("C",url); // 传递图片的地址
        intent.putExtra("D",true); // 传递图片的地址
        startActivity(context,intent);
    }
    /**
     * jump to scenic search
     * @param context
     */
    public void goScenicSpotSearchActivity(Context context){
        startActivity(context, ScenicSpotSearchActivity.class);
    }
    /**
     * jump to scenic search result
     * @param context
     * @param keyword search result keywords
     */
    public void goScenicSpotSearchResultActivity(Context context,String keyword){
        Intent intent = new Intent(context, ScenicSpotSearchResultActivity.class);
        intent.putExtra("keyword",keyword);
        startActivity(context,intent);
    }

    /**
     * jump to scenic suggestion and feedback
     * @param context
     * @param scenicid scenicId
     * @param scenicStr scenicName
     */
    public void goScenicFeedBackActivity(Context context,long scenicid,String scenicStr,ArrayList<String> list){
        Intent intent = new Intent(context, ScenicFeedBackActivity.class);
        intent.putExtra("scenicid", scenicid);
        intent.putExtra("scenicStr", scenicStr);
        intent.putStringArrayListExtra("list",list);
        startActivity(context,intent);
    }

    /**
     *  jump scenic spot detail
     * @param context
     * @param scenicid scenicId
     * @param scenicSpotId scenicSpotId
     * @param name scenicSpotName
     * @param bannerurl bannerurl
     * @param intro intro
     * @param play_path play_path
     * @param lat 经纬度
     * @param lng 经纬度
     */
    public void goScenicSpotDetailActivity(Context context,long scenicid,int scenicSpotId,String name,String bannerurl,String intro,String play_path,double lat,double lng,String filepath){
        Intent intent = new Intent(context, ScenicSpotDetailActivity.class);
        intent.putExtra("scenicid", scenicid); // 景区编号
        intent.putExtra("scenicSpotId", scenicSpotId); // 景点id 点击景点的编号
        intent.putExtra("name", name); // 景点名称 点击景点的名称
        intent.putExtra("bannerurl", bannerurl); // 景点banner 点击景点的简介
        intent.putExtra("intro", intro); // 景点简介 点击景点的简介
        intent.putExtra("play_path", play_path);// 景点音频地址 点击音频文件地址
        intent.putExtra("lat", lat);// 经纬度 方便回来之后点击操作
        intent.putExtra("lng", lng);// 经纬度 方便回来之后点击操作
        intent.putExtra("filepath",filepath);// 当前景区音频文件夹地址名称
        startActivity(context,intent);
    }
    /**
     * jump to shared navigation map
     * @param context
     * @param groupId groupId
     */
    public void goSharedNavigationMapActivity(Context context,String groupId){
        Intent intent = new Intent(context, SharedNavigationMapActivity.class);
        intent.putExtra("groupid",groupId);
        startActivity(context,intent);
    }
    /**
     * jump to shared navigation map
     * @param context
     * @param groupId groupId
     */
    public void goSharedNavigationMapActivitys(Context context,String groupId,boolean isfinish,boolean istz){
        Intent intent = new Intent(context, SharedNavigationMapActivity.class);
        intent.putExtra("groupid",groupId);
        intent.putExtra("isFinish",isfinish);
        intent.putExtra("istz",istz);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(context,intent);
    }
    /**
     * jump to shared navigation setting destination
     * @param context
     * @param lineId lineId
     */
    public void goSharedNavigationSettingDestinationActivity(Activity context,int lineId,int daynum,int requestCode,String stagingTime){
        Intent intent = new Intent(context, SharedNavigationSettingDestinationActivity.class);
        intent.putExtra("lineId",lineId);
        intent.putExtra("daynum",daynum);
        intent.putExtra("stagingTime",stagingTime);
        startAcitivityForResult(context,intent,requestCode);
    }
    /**
     *  jump to shared navigation go here
     * @param context
     * @param lat destination Lat
     * @param lng destination Lng
     * @param locStr destination Name
     */
    public void goSharedNavigationGoHereActivity(Context context,double lat,double lng,String locStr,String groupId){
        Intent intent = new Intent(context, SharedNavigationGoHereActivity.class);
        intent.putExtra("lat", lat);
        intent.putExtra("lng",lng);
        intent.putExtra("locStr",locStr);
        intent.putExtra("groupId", groupId);
        startActivity(context,intent);
    }
    /**
     *  jump to shared navigation go here
     * @param context
     * @param lat destination Lat
     * @param lng destination Lng
     * @param locStr destination Name
     */
    public void goSharedNavigationGoHereActivityBackUp(Context context,double lat,double lng,String locStr,String groupId){
        Intent intent = new Intent(context, SharedNavigationGoHereActivityBackUp.class);
        intent.putExtra("lat", lat);
        intent.putExtra("lng",lng);
        intent.putExtra("locStr",locStr);
        intent.putExtra("groupId", groupId);
        startActivity(context,intent);
    }
    /**
     *  jump shared navigation guide
     * @param context
     * @param isDriving isDriving
     */
    public void goSharedNavigationGuideActivity(Context context,boolean isDriving,double slat,double slng ,double elat,double elng,String groupId,int routeId){
        Intent intent = new Intent(context, SharedNavigationGuideActivity.class);
        intent.putExtra("slat", slat);
        intent.putExtra("slng",slng);
        intent.putExtra("elat", elat);
        intent.putExtra("elng",elng);
        intent.putExtra("isDriving", isDriving); // 是否是驾车导航模式
        intent.putExtra("groupId", groupId); // 是否是驾车导航模式
        intent.putExtra("routeId", routeId); // 是否是驾车导航模式
        startActivity(context,intent);
    }
    /**
     *  jump shared navigation guide
     * @param context
     * @param isDriving isDriving
     */
    public void goSharedNavigationGuideActivityBackUp(Context context,boolean isDriving,double slat,double slng ,double elat,double elng,String groupId,int routeId){
        Intent intent = new Intent(context, SharedNavigationGuideActivityBackUp.class);
        intent.putExtra("slat", slat);
        intent.putExtra("slng",slng);
        intent.putExtra("elat", elat);
        intent.putExtra("elng",elng);
        intent.putExtra("isDriving", isDriving); // 是否是驾车导航模式
        intent.putExtra("groupId", groupId); // 是否是驾车导航模式
        intent.putExtra("routeId", routeId); // 是否是驾车导航模式
        startActivity(context,intent);
    }

    /**
     *  jump group personal info
     * @param context
     * @param teamId groupId
     * @param userId userId
     */
    public void goGroupPersonalInfoActivity(Context context,String teamId,String userId){
        Intent intent = new Intent(context, GroupPersonalInfoActivity.class);
        intent.putExtra("teamId", teamId);
        intent.putExtra("userId",userId);
        startActivity(context,intent);
    }

    /**
     *  跳转到群编辑界面
     * @param context
     * @param menderName 修改人名称
     * @param topType 传递编辑顶部的名称  或者类型  1 群介绍 2 我在群的昵称 3 群聊名称
     * @param isEdit 传递过来是否是编辑状态 true 编辑 false 查看
     * @param content 传递过来的数据内容
     * @param groupId 传递过来的数据内容
     */
    public void goGroupEditorActivity(Context context,String menderName,int topType,boolean isEdit,String content,String groupId){
        Intent intent = new Intent(context, GroupEditorActivity.class);
        intent.putExtra("topType", topType);
        intent.putExtra("menderName", menderName);
        intent.putExtra("isEdit",isEdit);
        intent.putExtra("content",content);
        intent.putExtra("groupId",groupId);
        startActivity(context,intent);
    }

    /**
     * 跳转到景区图集界面
     * @param context
     * @param list 图片集合
     */
    public void goAtlasActivity(Context context,ArrayList<String> list){
        Intent intent = new Intent(context, AtlasActivity.class);
        intent.putStringArrayListExtra("list",list);
        startActivity(context,intent);
    }

    /**
     * 跳转到视频播放界面
     * @param context
     * @param videoUrl 视频文件url
     * @param thumb 视频文件的缩略图
     */
    public void goVideoPlayActivity(Context context, String videoUrl, String thumb){
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putExtra("url",videoUrl);
        intent.putExtra("thumb",thumb);
        startActivity(context,intent);
    }

    /**
     * 智能组队设置目的地和时间
     * @param context
     * @param stagingTime 如果有时间就传递一个时间 没有就传空
     * @param requestCode 请求码
     */
    public void goSmartTeamSettingDestinationAndTimeActivity(Activity context,String stagingTime,int requestCode){
        Intent intent = new Intent(context, SmartTeamSettingDestinationAndTimeActivity.class);
        intent.putExtra("stagingTime",stagingTime);
        startAcitivityForResult(context,intent,requestCode);
    }

    /**
     * 智能组队中地图界面
     * @param context 上下文
     * @param groupId 群id
     * @param isCreate 是否是创建队伍进入
     */
    public void goSmartTeamMapActivity(Context context,String groupId,boolean isCreate){
        Intent intent = new Intent(context, SmartTeamMapActivity.class);
        intent.putExtra("groupid",groupId);
        intent.putExtra("isCreate",isCreate);
        startActivity(context,intent);
    }
    /**
     * 智能组队中地图界面
     * @param context 上下文
     * @param groupId 群id
     * @param isCreate 是否是创建队伍进入
     */
    public void goSmartTeamMapActivitys(Context context,String groupId,boolean isCreate,boolean isfinish,boolean istz){
        Intent intent = new Intent(context, SmartTeamMapActivity.class);
        intent.putExtra("groupid",groupId);
        intent.putExtra("isCreate",isCreate);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("isFinish",isfinish);
        intent.putExtra("istz",istz);
        startActivity(context,intent);
    }
    /**
     * 到智能组队进入界面
     */
    public void goSmartTeamActivity(Context context){
        Intent mIntent = new Intent(context, SmartTeamActivity.class);
        startActivity(context,mIntent);
    }
    /**
     * 到智能组队的群详情
     */
    public void goSmartTeamGroupInfoActivity(Context context,String groupId){
        Intent intent = new Intent(context, SmartTeamGroupInfoActivity.class);
        intent.putExtra("groupId",groupId);
        startActivity(context,intent);
    }

    /**
     * 智能组队 添加车辆界面
     * @param context
     * @param groupId
     * @param licencePlate
     */
    public void goSmartTeamAddCarActivity(Context context,String groupId,String licencePlate){
        Intent intent = new Intent(context, SmartTeamAddCarActivity.class);
        intent.putExtra("A",groupId);
        intent.putExtra("B",licencePlate);
        startActivity(context,intent);
    }

    /**
     * 共享导航 指南界面
     */
    public void goGuideInfoActivity(Context context,int lineId,int tripId){
        Intent intent = new Intent(context, GuideInfoActivity.class);
        intent.putExtra("lineId",lineId);
        intent.putExtra("tripId",tripId);
        startActivity(context,intent);
    }

    /**
     * 共享导航 车辆检查界面
     */
    public void goCarCheckInfoActivity(Context context){
        Intent intent = new Intent(context, CarCheckInfoActivity.class);
        startActivity(context,intent);
    }

    /**
     * 个人中心——常用车辆
     */
    public void goCommonVehiclesActivity(Context context, boolean isEdit, boolean isMore, ArrayList<String> data){
        Intent intent = new Intent(context, CommonVehiclesActivity.class);
        intent.putExtra("edit",isEdit);
        intent.putExtra("more",isMore);
        intent.putStringArrayListExtra("list",data);
        startActivity(context,intent);
    }

    /**
     * 个人中心——添加和修改车辆
     * @param context
     * @param carinfo 车辆信息
     * @param isDefault 是否是默认
     */
    public void goMineAddCarActivity(Context context,String carinfo,boolean isDefault,int id){
        Intent intent = new Intent(context, MineAddCarActivity.class);
        intent.putExtra("car",carinfo);
        intent.putExtra("id",id);
        intent.putExtra("default",isDefault);
        startActivity(context,intent);
    }

    /**
     * 地图界面的行程非领队和队长签到界面
     * @param context
     * @param desid 查询目的地-集合时间接口返回的id
     */
    public void goTripSignInActivity(Context context,String desid){
        Intent intent = new Intent(context, TripSignInActivity.class);
        intent.putExtra("desid",desid);
        startActivity(context,intent);
    }

    /**
     * 地图界面的行程领队和队长签到列表界面
     *
     * @param context
     * @param desid 查询目的地-集合时间接口返回的id
     */
    public void goTripSignInListActivity(Context context,String desid){
        Intent intent = new Intent(context, TripSignInListActivity.class);
        intent.putExtra("desid",desid);
        startActivity(context,intent);
    }

    /**
     * 离线包管理
     *
     * @param context
     */
    public void goOfflineFileManagerActivity(Context context){
        Intent intent = new Intent(context, OfflineFileManagerActivity.class);
        startActivity(context,intent);
    }
}
