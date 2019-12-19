package cn.xmzt.www.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.greenrobot.eventbus.EventBus;

import cn.xmzt.www.base.WebActivity;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.home.activity.SelectedLineActivity;
import cn.xmzt.www.home.bean.ArticleBean;
import cn.xmzt.www.intercom.activity.SchedulingDetailActivity;
import cn.xmzt.www.main.activity.MainActivity;
import cn.xmzt.www.main.activity.SchemeActivity;
import cn.xmzt.www.route.activity.RouteDetailActivity1;
import cn.xmzt.www.route.activity.RouteOrderDetailActivity;
import cn.xmzt.www.route.activity.RoutePhotoViewActivity;
import cn.xmzt.www.route.activity.VideoPlayActivity;
import cn.xmzt.www.selfdrivingtools.activity.CarCheckInfoActivity;
import cn.xmzt.www.selfdrivingtools.activity.ScenicSpotMapActivity;

public class SchemeActivityUtil {
    /**
     *
     * @param mContext
     * @param jumpType //1图片 2视频 3webview 4内链 5极光推送
     * @param title 表示webview的标题
     * @param url 图片的url 视频的url webview的url 和内链的url
     */
    public static void startToActivity(Context mContext,int jumpType,String title,String url) {
        Intent intent = new Intent();
        //1图片 2视频 3webview 4内链
        if(jumpType==1){
            intent.setClass(mContext, RoutePhotoViewActivity.class);
            intent.putExtra("A",0);
            intent.putExtra("C",url);
        }else if(jumpType==2){
            intent.setClass(mContext, VideoPlayActivity.class);
            intent.putExtra("A",0);
            intent.putExtra("url",url);
        }else if(jumpType==3){
            intent.setClass(mContext, WebActivity.class);
            intent.putExtra("title",title);
            intent.putExtra("url",url);
        }else if(jumpType==4){
            Uri uri = Uri.parse(url);
            if (uri != null) {
                // Query部分
                String query = uri.getEncodedQuery();
                //获取指定参数值
                String actiontype = uri.getQueryParameter("t");//类型
                String id = uri.getQueryParameter("i");//类型对于的产品id
                String ref_code = uri.getQueryParameter("p");//ref_code	#用户推荐码
                switch (actiontype) {
                    case "1":{//1、精品线路
                        //id 精品线路id
                        ArticleBean articleBean=new ArticleBean();
                        articleBean.setId(Integer.parseInt(id));
                        intent.setClass(mContext, SelectedLineActivity.class);
                        EventBus.getDefault().postSticky(articleBean);
                        break;
                    }
                    case "2": {//2、线路
                        //id 为线路id
                        intent.setClass(mContext, RouteDetailActivity1.class);
                        intent.putExtra("A",Integer.parseInt(id));
                        break;
                    }
                    case "3": {//3、邀请好友
                        //ref_code 用户推荐码
//                        intent.setClass(mContext, FriendInviteActivity.class);
                        break;
                    }
                    case "4": {//4、电子导览
                        //id 为电子导览id
                        intent.setClass(mContext, ScenicSpotMapActivity.class);
                        intent.putExtra("id",Long.parseLong(id));
                        break;
                    }
                    case "5": //5、邀请加入行程分享
                        //id 为行程id

                        break;

                    case "6": {//6、群主邀请入群二维码
                        //id 为行程id
                        intent.setClass(mContext, SchemeActivity.class);
                        intent.putExtra("url",url);
                        break;
                    }
                    case "7":
                        intent.setClass(mContext, RouteOrderDetailActivity.class);
                        intent.putExtra("A",id);
                        break;

                    case "8":
                        intent.setClass(mContext, SchedulingDetailActivity.class);
                        intent.putExtra("A",Integer.valueOf(id));
                        break;
                    default:{

                    }
                }
            }
        } else if (jumpType == 5){
            intent.setClass(mContext, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("type",1);
        }
        mContext.startActivity(intent);
    }
    /**
     *推送通知跳转
     * @param mContext
     * @param title 标题
     * @param objectId 关联id 如行程动态为 行程id 订单消息 为 订单id 消息类型为7 则为xx市美食指南id 为8 则为xx酒店介绍id
     * @param type 1 行程创建成功 2 明日出行提醒 3 早睡提醒 4 集结地更新提醒 5 车辆检查提醒
     *             6 出行清单提醒 7 xx市美食指南 8 xxx酒店介绍提醒 9 订单提醒 10 群即将解散提醒
     */
    public static void pushToActivity(Context mContext,String title,String objectId,String type) {
        Intent intent = new Intent();
        if("1".equals(type)||"4".equals(type)||"10".equals(type)){
            //跳行程地图页面
            intent.setClass(mContext, SchemeActivity.class);
            intent.putExtra("groupId",objectId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intent);
        }else if("5".equals(type)){
            //跳车辆检测
            intent.setClass(mContext, CarCheckInfoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intent);
        }else if("6".equals(type)||"7".equals(type) ||"8".equals(type)){
            //跳WebActivity
            intent.setClass(mContext, WebActivity.class);
            intent.putExtra("title",title);
            intent.putExtra("url", Constants.getTipsUrl(type,objectId));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intent);
        }else if("9".equals(type)){
            //跳订单详情
            intent.setClass(mContext, RouteOrderDetailActivity.class);
            intent.putExtra("A",objectId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intent);
        }
    }
}
