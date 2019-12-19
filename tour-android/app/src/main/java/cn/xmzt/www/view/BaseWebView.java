package cn.xmzt.www.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
public class BaseWebView extends WebView {
    public BaseWebView(Context context) {
        super(context);
        init(context);
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //声明WebSettings子类
        WebSettings webSettings = getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        //支持插件
//        webSettings.setPluginsEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(false); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        //1，LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //2，LOAD_DEFAULT: 根据cache-control决定是否从网络上取数据。
       // 3，LOAD_CACHE_NORMAL: API level 17中已经废弃, 从API level 11开始作用同LOAD_DEFAULT模式
       // 4，LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
       // 5，LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        webSettings.setDomStorageEnabled(true);//主要是这句
        webSettings.setBlockNetworkImage(false);//解决图片不显示

        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {//作用：打开网页时不调用系统浏览器， 而是在本WebView中显示；在网页上的所有加载都经过这个方法,这个函数我们可以做很多操作。
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {//作用：开始载入页面调用的，我们可以设定一个loading的页面，告诉用户程序在等待网络响应。
                //设定加载开始的操作
            }

            @Override
            public void onPageFinished(WebView view, String url) {//作用：在页面加载结束时调用。我们可以关闭loading 条，切换程序动作。
                //设定加载结束的操作
            }

            @Override
            public void onLoadResource(WebView view, String url) {//作用：在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次。
                super.onLoadResource(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {//作用：加载页面的服务器出现错误时（如404）调用。
                switch (errorCode) {
//                    case HttpStatus.SC_NOT_FOUND:
//                        view.loadUrl("file:///android_assets/error_handle.html");
//                        break;
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {//作用：处理https请求   webView默认是不处理https请求的，页面显示空白，需要进行如下设置
                handler.proceed();    //表示等待证书响应
                // handler.cancel();      //表示挂起连接，为默认方式
                // handler.handleMessage(null);    //可做其他处理
            }
        });

        //WebChromeClient类 作用：辅助 WebView 处理 Javascript 的对话框,网站图标,网站标题等等。
        setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {//作用：获得网页的加载进度并显示
                super.onProgressChanged(view, newProgress);
                if (null != onProgressUpdate) {
                    onProgressUpdate.onProgressUpdate(newProgress);
                }
//                if (newProgress < 100) {
//                    String progress = newProgress + "%";
//                    progress.setText(progress);
//                } else {
//                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {// 作用：获取Web页中的标题    每个网页的页面都有一个标题，比如www.baidu.com这个页面的标题即“百度一下，你就知道”，那么如何知道当前webview正在加载的页面的title并进行设置呢？
                super.onReceivedTitle(view, title);
                //                titleview.setText(title)；
            }
        });

    }

    private boolean enabledSlide=true;
    public  void setEnabledSlide(boolean enabledSlide){
        this.enabledSlide=enabledSlide;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(enabledSlide){
            return super.onTouchEvent(event);
        }else{
            return false;
        }
    }
    private IOnProgressUpdate onProgressUpdate;

    public void setOnProgressUpdate(IOnProgressUpdate onProgressUpdate) {
        this.onProgressUpdate = onProgressUpdate;
    }

    public interface IOnProgressUpdate {
        void onProgressUpdate(int progress);
    }
}
