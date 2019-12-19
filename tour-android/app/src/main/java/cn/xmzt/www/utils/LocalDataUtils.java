package cn.xmzt.www.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.home.bean.CustomizeLiveDialogBean;
import cn.xmzt.www.selfdrivingtools.bean.ScenicFeedBackTypeBean;
import cn.xmzt.www.selfdrivingtools.bean.ScenicSpotMapTypeListBean;
import cn.xmzt.www.selfdrivingtools.bean.SettingDestinationTypeInfo;
import cn.xmzt.www.selfdrivingtools.bean.SuggestAndFeedBackNaviBean;

public class LocalDataUtils {

    private static int[] mTypeAllIcon = {
            R.drawable.scenic_spot_map_scenic_spot_icon,
            R.drawable.scenic_spot_map_route_icon,
            R.drawable.scenic_spot_map_work_icon,
            R.drawable.scenic_spot_map_food_icon,
            R.drawable.scenic_spot_map_type_doctor_icon,
            R.drawable.scenic_spot_map_type_car_icon,
            R.drawable.scenic_spot_map_type_into_icon,
            R.drawable.scenic_spot_map_type_toilet_icon,
            R.drawable.scenic_spot_map_type_bus_icon,
            R.drawable.scenic_spot_map_type_service_icon,
            R.drawable.scenic_spot_map_type_buy_icon,
            R.drawable.scenic_spot_map_type_live_icon,
            R.drawable.scenic_spot_map_type_tour_center_icon,
    };
    private static String[] mTypeAllTitle = {"景点","路线","出行","美食","医务室","停车场","出入口","厕所","公交站","服务店","购物点","住宿","游客中心"};

    private static String[] mDateDay = {"全程","第1天","第2天","第3天","第4天","第5天","第6天","第7天","第8天","第9天","第10天","第11天","第12天","第13天","第14天","第15天","第16天","第17天","第18天","第19天","第20天"};
//    private static String[] mDateDay = {"全程","第一天","第二天","第三天","第四天","第五天","第六天","第七天","第八天","第九天","第十天","第十一天","第十二天"};

    private static int[] mSuggestAndFeedbackTypeAllIcon = {
            R.drawable.scenic_feed_back_map_icon,
            R.drawable.scenic_feed_back_voice_icon,
            R.drawable.scenic_feed_back_option_icon,
            R.drawable.scenic_feed_back_other_icon,
    };

    private static String[] mSettingDestinationTypeInfo = {"地图选点","今日行程"};

    private static String[] mSmartTeamType = {"人员","车辆"};

    /**
     * 获取景区全部类型的数据
     * @return
     */
    public static List<ScenicSpotMapTypeListBean> getTypeAllData(){
        List<ScenicSpotMapTypeListBean> list  = new ArrayList<>();
        for (int i = 0 ; i < mTypeAllTitle.length ; i++){
            ScenicSpotMapTypeListBean bean = new ScenicSpotMapTypeListBean();
            bean.setImg_icon(mTypeAllIcon[i]);
            bean.setTitle(mTypeAllTitle[i]);
            bean.setIs_selected( i == 0 ? true : false);// 默认景点选中
            bean.setId(i);
            list.add(bean);
        }
        return list;
    }
    /**
     * 获取不同类型的marker图标
     * @param type
     * @return
     */
    public static int getMarkerIcon(int type){
        int mTypeIcon = R.drawable.map_marker_scenic_spot_icon;
        switch (type){
            case 0 :
                mTypeIcon = R.drawable.map_marker_scenic_spot_icon;
                break;
            case 1:
                mTypeIcon = R.drawable.map_marker_scenic_spot_icon;
                break;
            case 2 :
                mTypeIcon = R.drawable.map_marker_work_icon;
                break;
            case 3 :
                mTypeIcon = R.drawable.map_marker_food_icon;
                break;
            case 4:
                mTypeIcon = R.drawable.map_marker_doctor_icon;
                break;
            case 5 :
                mTypeIcon = R.drawable.map_marker_park_icon;
                break;
            case 6 :
                mTypeIcon = R.drawable.map_marker_into_icon;
                break;
            case 7:
                mTypeIcon = R.drawable.map_marker_toilet_icon;
                break;
            case 8 :
                mTypeIcon = R.drawable.map_marker_bus_icon;
                break;
            case 9 :
                mTypeIcon = R.drawable.map_marker_service_icon;
                break;
            case 10:
                mTypeIcon = R.drawable.map_marker_buy_icon;
                break;
            case 11 :
                mTypeIcon = R.drawable.map_marker_live_icon;
                break;
            case 12 :
                mTypeIcon = R.drawable.map_marker_center_icon;
                break;
            default:
                mTypeIcon = R.drawable.map_marker_scenic_spot_icon;
                break;
        }
        return mTypeIcon;
    }

    /**
     * 获取搜索目的地界面类型
     * @return
     */
    public static List<SettingDestinationTypeInfo> getSettingDestinationTypeInfo(){
        List<SettingDestinationTypeInfo> infos = new ArrayList<>();
        for (int i = 0 ; i < mSettingDestinationTypeInfo.length ; i++){
            SettingDestinationTypeInfo bean = new SettingDestinationTypeInfo();
            bean.setContent(mSettingDestinationTypeInfo[i]);
            bean.setIs_Checked( i == 0 ? true : false);// 默认景点选中
            bean.setId(i);
            infos.add(bean);
        }
        return infos;
    }

    /**
     * 获取智能组队类型
     * @return
     */
    public static List<SettingDestinationTypeInfo> getSmartTeamTypeInfo(){
        List<SettingDestinationTypeInfo> infos = new ArrayList<>();
        for (int i = 0 ; i < mSmartTeamType.length ; i++){
            SettingDestinationTypeInfo bean = new SettingDestinationTypeInfo();
            bean.setContent(mSmartTeamType[i]);
            bean.setIs_Checked( i == 0 ? true : false);// 默认选中人员
            bean.setId(i);
            infos.add(bean);
        }
        return infos;
    }

    /**
     * 获取行程天数
     * @return
     */
    public static List<SettingDestinationTypeInfo> getTripDateDaysInfo(int day){
        List<SettingDestinationTypeInfo> infos = new ArrayList<>();
        for (int i = 0 ; i < (day+1) ; i++){
            SettingDestinationTypeInfo bean = new SettingDestinationTypeInfo();
            bean.setContent(mDateDay[i]);
            bean.setIs_Checked( i == 0 ? true : false);// 默认全程选中
            bean.setId(i);
            infos.add(bean);
        }
        return infos;
    }



    private static String[] mSuggestAndFeedbackTypeAllTitle = {"地图问题","语音问题","功能问题","其他问题"};

    private static String[] mSuggestAndFeedbackTypeAllTitle1 = {"景点信息有误","定位不准","线路错误","缺少景点"};
    private static String[] mSuggestAndFeedbackTypeAllTitle2 = {"没有声音","景点语音缺失","语音不清晰","讲解内容有误"};
    private static String[] mSuggestAndFeedbackTypeAllTitle3 = {"无法下载离线包","无法加载地图","APP闪退"};
    private static String[] mSuggestAndFeedbackTypeAllTitle4 = {"没有找到需要的景区","介绍不够丰富","我有其他建议"};
    /**
     * 获取建议与反馈的全部类型
     * @return
     */
    public static List<SuggestAndFeedBackNaviBean> getSuggestAndFeedBackNaviTypeAllData(){
        List<SuggestAndFeedBackNaviBean> list  = new ArrayList<>();
        for (int i = 0 ; i < mSuggestAndFeedbackTypeAllTitle.length ; i++){
            SuggestAndFeedBackNaviBean bean = new SuggestAndFeedBackNaviBean();
            bean.setIcon(mSuggestAndFeedbackTypeAllIcon[i]);
            bean.setTitel(mSuggestAndFeedbackTypeAllTitle[i]);
            bean.setIs_Checked( i == 0 ? true : false);// 默认景点选中
            bean.setId(i);
            list.add(bean);
        }
        return list;
    }

    public static  List<ScenicFeedBackTypeBean> getQuestDetail(){
        List<ScenicFeedBackTypeBean> minfo = new ArrayList<>();
        for (int i = 0;i < mSuggestAndFeedbackTypeAllTitle.length ; i++){
            ScenicFeedBackTypeBean bean = new ScenicFeedBackTypeBean();
            List<ScenicFeedBackTypeBean.ChildrenBean> list = new ArrayList<>();
            if (i == 0) {
                for (int j = 0; j < mSuggestAndFeedbackTypeAllTitle1.length; j++){
                    ScenicFeedBackTypeBean.ChildrenBean info = new ScenicFeedBackTypeBean.ChildrenBean();
                    info.setDes(mSuggestAndFeedbackTypeAllTitle1[j]);
                    list.add(info);
                }
            } else if (i == 1) {
                for (int j = 0; j < mSuggestAndFeedbackTypeAllTitle2.length; j++){
                    ScenicFeedBackTypeBean.ChildrenBean info = new ScenicFeedBackTypeBean.ChildrenBean();
                    info.setDes(mSuggestAndFeedbackTypeAllTitle2[j]);
                    list.add(info);
                }
            } else if (i == 2) {
                for (int j = 0; j < mSuggestAndFeedbackTypeAllTitle3.length; j++){
                    ScenicFeedBackTypeBean.ChildrenBean info = new ScenicFeedBackTypeBean.ChildrenBean();
                    info.setDes(mSuggestAndFeedbackTypeAllTitle3[j]);
                    list.add(info);
                }
            } else if (i == 3) {
                for (int j = 0; j < mSuggestAndFeedbackTypeAllTitle4.length; j++){
                    ScenicFeedBackTypeBean.ChildrenBean info = new ScenicFeedBackTypeBean.ChildrenBean();
                    info.setDes(mSuggestAndFeedbackTypeAllTitle4[j]);
                    list.add(info);
                }
            }
            bean.setChildren(list);
            minfo.add(bean);
        }
        return minfo;
    }

    /**
     * 根据类型获取类型名称  // 2景点 4住宿 8早餐 9午餐 10晚餐
     * @param type
     * @return
     */
    public static String getTypeStr(int type){
        String content = "";
        switch (type){
            case 2:
                content = "景点";
                break;
            case 4:
                content = "住宿";
                break;
            case 5:
                content = "集结地";
                break;
            case 6:
                content = "出发地";
                break;
            case 7:
                content = "目的地";
                break;
            case 8:
                content = "早餐";
                break;
            case 9:
                content = "午餐";
                break;
            case 10:
                content = "晚餐";
                break;
            default:
                content = "景点";
                break;
        }
        return content;
    }

    private static String[] mCustomizeJiuDian = {"二星/简约型","三星/舒适型","四星/品质型","五星/豪华型"};
    /**
     * 定制酒店的选择类型
     * @return
     */
    public static List<CustomizeLiveDialogBean> getCustomizeJiuDianAllData(String data){
        List<CustomizeLiveDialogBean> list  = new ArrayList<>();
        if (TextUtils.isEmpty(data)){
            for (int i = 0 ; i < mCustomizeJiuDian.length ; i++){
                CustomizeLiveDialogBean bean = new CustomizeLiveDialogBean();
                bean.setTitle(mCustomizeJiuDian[i]);
                bean.setType(1);
                bean.setSelect(false);
                bean.setPosition(i);
                list.add(bean);
            }
        } else {
            for (int i = 0 ; i < mCustomizeJiuDian.length ; i++){
                CustomizeLiveDialogBean bean = new CustomizeLiveDialogBean();
                bean.setTitle(mCustomizeJiuDian[i]);
                if (data.equals(mCustomizeJiuDian[i])){
                    bean.setSelect(true);
                } else {
                    bean.setSelect(false);
                }
                bean.setPosition(i);
                bean.setType(1);
                list.add(bean);
            }
        }
        return list;
    }

    private static String[] mCustomizeMinSu = {"100 - 300元","300 - 500元","500 - 800元","800 - 1000元","1000元以上"};
    /**
     * 定制民宿的选择类型
     * @return
     */
    public static List<CustomizeLiveDialogBean> getCustomizeMinSuAllData(String data){
        List<CustomizeLiveDialogBean> list  = new ArrayList<>();
        if (TextUtils.isEmpty(data)){
            for (int i = 0 ; i < mCustomizeMinSu.length ; i++){
                CustomizeLiveDialogBean bean = new CustomizeLiveDialogBean();
                bean.setTitle(mCustomizeMinSu[i]);
                bean.setSelect(false);// 默认景点选中
                bean.setPosition(i);
                bean.setType(2);
                list.add(bean);
            }
        } else {
            for (int i = 0 ; i < mCustomizeMinSu.length ; i++){
                CustomizeLiveDialogBean bean = new CustomizeLiveDialogBean();
                bean.setTitle(mCustomizeMinSu[i]);
                if (data.equals(mCustomizeMinSu[i])){
                    bean.setSelect(true);
                } else {
                    bean.setSelect(false);
                }
                bean.setPosition(i);
                bean.setType(2);
                list.add(bean);
            }
        }
        return list;
    }

    private static String[] mCustomizeYuSuan = {"无明确预算","1000元以下","1000 - 3000元","5000 - 8000元","8000 - 10000元","10000 - 15000元","15000 - 20000元","20000元以上"};
    /**
     * 定制预算的选择类型
     * @return
     */
    public static List<CustomizeLiveDialogBean> getCustomizeYuSuanAllData(String data){
        List<CustomizeLiveDialogBean> list  = new ArrayList<>();
        if (TextUtils.isEmpty(data)){
            for (int i = 0 ; i < mCustomizeYuSuan.length ; i++){
                CustomizeLiveDialogBean bean = new CustomizeLiveDialogBean();
                bean.setTitle(mCustomizeYuSuan[i]);
                bean.setSelect(false);// 默认景点选中
                bean.setPosition(i);
                bean.setType(3);
                list.add(bean);
            }
        } else {
            for (int i = 0 ; i < mCustomizeYuSuan.length ; i++){
                CustomizeLiveDialogBean bean = new CustomizeLiveDialogBean();
                bean.setTitle(mCustomizeYuSuan[i]);
                if (data.equals(mCustomizeYuSuan[i])){
                    bean.setSelect(true);
                } else {
                    bean.setSelect(false);
                }
                bean.setPosition(i);
                bean.setType(3);
                list.add(bean);
            }
        }
        return list;
    }

    /**
     * 获取定制类型的级别名称
     * @param type 酒店类型 1酒店 2名宿 3预算
     * @param level
     * @return
     */
    public static String getCustomizeTypeLevelName(int type,int level){
        String levelName="";
        if(type==1){
            int index=level-2;
            if(index>=0&&index<mCustomizeJiuDian.length){
                levelName=mCustomizeJiuDian[index];
            }
        }else if(type==2){
            int index=level-1;
            if(index>=0&&index<mCustomizeMinSu.length){
                levelName=mCustomizeMinSu[index];
            }
        }else if(type==3){
            int index=level-1;
            if(index>=0&&index<mCustomizeYuSuan.length){
                levelName=mCustomizeYuSuan[index];
            }
        }
        return levelName;
    }
 }
