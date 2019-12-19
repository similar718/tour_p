package cn.xmzt.www.dialog;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import cn.xmzt.www.R;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.view.DWebView;

/**
 * @author tanlei
 * @date 2019/7/31
 * @describe 积分规则对话框
 */

public class ScoreRuleExplainDialog extends Dialog {
    WebView webView;
    public ScoreRuleExplainDialog(@NonNull Context context) {
        this(context, 0);
    }

    public ScoreRuleExplainDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.dialog_custom);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_score_rule_explain, null);
        setContentView(view);
        DWebView web = view.findViewById(R.id.web);
        webView = web.getWebView();
        webView.loadUrl(Constants.getXzUrl(1));
    }
}
