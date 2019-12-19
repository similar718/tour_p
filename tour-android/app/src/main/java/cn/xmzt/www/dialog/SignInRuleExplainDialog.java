package cn.xmzt.www.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import cn.xmzt.www.R;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.view.DWebView;

import androidx.annotation.NonNull;

/**
 * @author tanlei
 * @date 2019/7/31
 * @describe 奖励规则对话框
 */

public class SignInRuleExplainDialog extends Dialog {
    WebView webView;

    public SignInRuleExplainDialog(@NonNull Context context) {
        this(context, 0);
    }

    public SignInRuleExplainDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.dialog_custom);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_sign_in_rule_explain, null);
        setContentView(view);
        DWebView web = view.findViewById(R.id.web);
        webView = web.getWebView();
        webView.loadUrl(Constants.getXzUrl(2));
        view.findViewById(R.id.iv_dismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInRuleExplainDialog.this.dismiss();
            }
        });
    }
}
