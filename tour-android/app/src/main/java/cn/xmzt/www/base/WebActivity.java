package cn.xmzt.www.base;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import androidx.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ActivityWebBinding;

import java.util.List;

import static android.view.KeyEvent.KEYCODE_BACK;
import static android.view.View.GONE;

public class WebActivity extends BaseActivity {
    private WebView web;
    private String title;
    private String url;
    private String content;
    private ActivityWebBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getIntent().getStringExtra("url");

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_web);
        if (TextUtils.isEmpty(title)) {
            dataBinding.titleRl.setVisibility(GONE);
        }else {
            dataBinding.titleTv.setText(title);
        }
        initView();
        initData();
    }

    protected void initView() {
        dataBinding.setActivity(this);
        web = dataBinding.web.getWebView();
        initWebView();
    }
    ImageView shareImg;

    private void initWebView() {
        web.requestFocus();
        web.requestFocusFromTouch();
        // 设置WebView的客户端
        WebSettings webSettings = web.getSettings();
        // 设置缓存模式,一共有四种模式
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {//作用：打开网页时不调用系统浏览器， 而是在本WebView中显示；在网页上的所有加载都经过这个方法,这个函数我们可以做很多操作。
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {//作用：开始载入页面调用的，我们可以设定一个loading的页面，告诉用户程序在等待网络响应。
                //设定加载开始的操作
                if(url!=null){
                    boolean isValid=Patterns.WEB_URL.matcher(url).matches();
                    if(isValid||!url.contains("://")){
                        super.onPageStarted(view,url,favicon);
                    }else{
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            PackageManager packageManager = getPackageManager();
                            List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
                            boolean schemeIsValid = !activities.isEmpty();
                            if (schemeIsValid) {//Scheme是否有效
                                startActivity(intent);
                                finish();
                            }else{
                                super.onPageStarted(view,url,favicon);
                            }
                        } catch (Exception e) {
                            super.onPageStarted(view,url,favicon);
                        }
                    }
                }else{
                    super.onPageStarted(view,url,favicon);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {//作用：在页面加载结束时调用。我们可以关闭loading 条，切换程序动作。
                super.onPageFinished(view, url);
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
                super.onReceivedError(view,errorCode,description,failingUrl);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {//作用：处理https请求   webView默认是不处理https请求的，页面显示空白，需要进行如下设置
                handler.proceed();    //表示等待证书响应
                // handler.cancel();      //表示挂起连接，为默认方式
                // handler.handleMessage(null);    //可做其他处理
                super.onReceivedSslError(view,handler,error);
            }
        });
    }
    protected void initData() {
        if (!TextUtils.isEmpty(url)) {
            web.loadUrl(url);
        } else {
            if (content != null) {
                web.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && web.canGoBack()) {
            web.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv: {
                onBackPressed();
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //激活WebView为活跃状态，能正常执行网页的响应
        web.onResume();

        //恢复pauseTimers状态
        web.resumeTimers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //当页面被失去焦点被切换到后台不可见状态，需要执行onPause
        //通过onPause动作通知内核暂停所有的动作，比如DOM的解析、plugin的执行、JavaScript执行。
        web.onPause();

        //当应用程序(存在webview)被切换到后台时，这个方法不仅仅针对当前的webview而是全局的全应用程序的webview
        //它会暂停所有webview的layout，parsing，javascripttimer。降低CPU功耗。
        web.pauseTimers();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*if (web.canGoBack()) {
            web.goBack();
        }else{
            super.onBackPressed();
        }*/
    }

    @Override
    protected void onDestroy() {
        if (web != null) {
            web.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            web.clearHistory();

            ((ViewGroup) web.getParent()).removeView(web);
            web.destroy();
            web = null;
        }
        super.onDestroy();
    }
}