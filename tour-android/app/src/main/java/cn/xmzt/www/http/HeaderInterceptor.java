package cn.xmzt.www.http;

import android.content.Context;

import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.utils.NetWorkUtils;
import cn.xmzt.www.utils.SPUtils;
import cn.xmzt.www.utils.VersionUtils;

import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Header拦截器
 * 主要是设置一些公用的header的数据
 */
public class HeaderInterceptor implements Interceptor {

    private Map<String, String> headers;

    public HeaderInterceptor() {
        headers=new HashMap<>();
        headers.put("Accept", "*/*");
        headers.put("token", SPUtils.getToken());
        headers.put("os","android");
        headers.put("client","android");
        Context context= TourApplication.context;
        headers.put("version", VersionUtils.getVersionCode(context)+"");
    }
    public HeaderInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        if (NetWorkUtils.isNetConnected(TourApplication.context)){
            Request.Builder builder = chain.request()
                    .newBuilder();
            headers.put("token",SPUtils.getToken());
            int networkType=NetWorkUtils.getNetworkType(TourApplication.context);
            if(networkType==1){
                headers.put("NetType","WIFI");
            }else {
                headers.put("NetType","4G");
            }
            if (headers != null && headers.size() > 0) {
                Set<String> keys = headers.keySet();
                for (String headerKey : keys) {
                    builder.addHeader(headerKey, headers.get(headerKey));
                }
            }
            Response mResponse=chain.proceed(builder.build());
            return mResponse;
        } else {
            throw new ConnectException();
        }
    }
}
