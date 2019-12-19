package cn.xmzt.www.dialog;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.xmzt.www.R;

/**
 * @author tanlei
 * @date 2019/7/30
 * @describe 注销账号确认弹出框
 */

public class LogoffConfirmDialog extends Dialog {
    private TextView tvConfirm, tvCancel, tvTitle;

    public LogoffConfirmDialog(@NonNull Context context, View.OnClickListener onClickListener) {
        this(context, 0, onClickListener);
    }

    public LogoffConfirmDialog(@NonNull Context context, int themeResId, View.OnClickListener onClickListener) {
        super(context, themeResId);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_logoff_confirm, null);
        tvConfirm = view.findViewById(R.id.tv_confirm);
        tvCancel = view.findViewById(R.id.tv_cancel);
        tvTitle = view.findViewById(R.id.tv_title);
        setContentView(view);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        tvConfirm.setOnClickListener(onClickListener);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }
}
