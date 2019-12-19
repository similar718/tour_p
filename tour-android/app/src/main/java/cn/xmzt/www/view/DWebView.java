package cn.xmzt.www.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import cn.xmzt.www.R;

public class DWebView extends RelativeLayout {
    private ProgressBar progressBar;
    private BaseWebView webView;

    public DWebView(Context context) {
        super(context);
        init(context);
    }

    public DWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_webview, this);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(GONE);
        webView = findViewById(R.id.webView);
        webView.setOnProgressUpdate(new BaseWebView.IOnProgressUpdate() {
            @Override
            public void onProgressUpdate(int progress) {
                if (progress < 100) {
                    progressBar.setVisibility(VISIBLE);
                    progressBar.setProgress(progress);
                } else {
                    progressBar.setVisibility(GONE);
                }
            }
        });
    }

    public BaseWebView getWebView() {
        return webView;
    }
    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
