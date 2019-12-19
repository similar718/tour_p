package cn.xmzt.www.selfdrivingtools.map;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import cn.xmzt.www.utils.ToastUtils;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * 第三方地图跳转
 */
public class ThirdPartyMapsGuide {

    public static final String PN_GAODE_MAP = "com.autonavi.minimap";// 高德地图包名
    public static final String PN_BAIDU_MAP = "com.baidu.BaiduMap"; // 百度地图包名
    public static final String PN_TENCENT_MAP = "com.tencent.map"; // 腾讯地图包名

    /**
     * 启动百度App进行导航
     *
     * @param address 目的地
     * @param lat     必填 纬度
     * @param lon     必填 经度
     */
    public static void goToBaiduActivity(Context context, String address, double lon, double lat) {
        double[] doubles = gcj02_To_Bd09(lon, lat);
        //启动路径规划页面
        baiduMap(context,doubles[0],doubles[1]);
    }

    /**
     * 百度地图
     */
    public static void baiduMap(Context context,double lng,double lat) {
        if (isAvilible(context, PN_BAIDU_MAP)) {//传入指定应用包名
            Intent il = new Intent();
            il.setData(Uri.parse("baidumap://map/direction?destination=" + lat + "," + lng + "&mode=driving"));
            startActivity(il);
        } else {//未安装
            //market为路径，id为包名
            //显示手机上所有的market商店
            ToastUtils.showText(context, "您尚未安装百度地图");
            //显示手机上所有的market商店
            Uri uri = Uri.parse("market://details?id="+PN_BAIDU_MAP);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    /**
     * 腾讯地图
     */
    public static void goToTencentMap(Context context,String address,double lng,double lat) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        //将功能Scheme以URI的方式传入data
        Uri uri = Uri.parse("qqmap://map/routeplan?type=drive&to="+address+"&tocoord=" + lat + "," + lng);
        intent.setData(uri);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            //启动该页面即可
            startActivity(intent);
        } else {
            ToastUtils.showText(context, "您尚未安装腾讯地图");
            //显示手机上所有的market商店
            Uri uri1 = Uri.parse("market://details?id="+PN_TENCENT_MAP);
            Intent intent1 = new Intent(Intent.ACTION_VIEW, uri1);
            startActivity(intent1);
        }
    }

    /**
     * 高德地图
     */
    public static void goToGaoDeMap(Context context,double lng,double lat) {
        if (isAvilible(context, PN_GAODE_MAP)) {//传入指定应用包名
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setPackage(PN_GAODE_MAP);
            try {
                intent = Intent.getIntent("amapuri://route/plan/?dlat=" + lat + "&dlon=" + lng + "&d&dev=0&t=0");
                startActivity(intent);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {//未安装
            //market为路径，id为包名
            //显示手机上所有的market商店
            ToastUtils.showText(context, "您尚未安装高德地图");
            Uri uri = Uri.parse("market://details?id="+PN_GAODE_MAP);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    /**检测应用是否安装*/
    private static boolean isAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();//获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);//获取所有已安装程序的包信息
        List<String> pName = new ArrayList<String>();//用于存储所有已安装程序的包名
        //从pinfo中将包名字逐一取出，压入pName list中
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);//判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
     *
     * @param lat
     * @param lon
     */
    public static double[] gcj02_To_Bd09(double lat, double lon) {
        double x = lon, y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002;
        double theta = Math.atan2(y, x) + 0.000003;
        double tempLat = z * Math.sin(theta) + 0.006;
        double tempLon = z * Math.cos(theta) + 0.0065;
        double[] gps = {tempLat, tempLon};
        return gps;
    }
}
