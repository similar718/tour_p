package cn.xmzt.www.dialog;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;

import cn.xmzt.www.R;

/**
 * @author tanlei
 * @date 2019/7/31
 * @describe 入住人填写姓名说明弹出框
 */

public class NameTextExplainDialog extends Dialog{
    public NameTextExplainDialog(@NonNull Context context) {
        this(context,0);
    }

    public NameTextExplainDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_name_text_explain);
    }
}
