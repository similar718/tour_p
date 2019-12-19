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
 * @describe 展示文本的dialog（左边确定 右边取消）
 */

public class TextTitleDialog extends Dialog {
    private TextView tvConfirm, tvCancel, tvTitle;

    public TextTitleDialog(@NonNull Context context, View.OnClickListener onClickListener) {
        this(context, R.style.dialog_custom, onClickListener);
    }

    public TextTitleDialog(@NonNull Context context, int themeResId, View.OnClickListener onClickListener) {
        super(context,themeResId == 0 ? R.style.dialog_custom : themeResId);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_text_title, null);
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

    public TextTitleDialog(@NonNull Context context, View.OnClickListener onClickCancelListener, View.OnClickListener onClickConfirmListener) {
        super(context, 0);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_text_title, null);
        tvConfirm = view.findViewById(R.id.tv_confirm);
        tvCancel = view.findViewById(R.id.tv_cancel);
        tvTitle = view.findViewById(R.id.tv_title);
        setContentView(view);

        tvCancel.setOnClickListener(onClickCancelListener);
        tvConfirm.setOnClickListener(onClickConfirmListener);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }
    public void setBtnBackground(int leftResId,int rightResId) {
        tvCancel.setBackgroundResource(leftResId);
        tvConfirm.setBackgroundResource(rightResId);
    }
    public void setBtnTextColor(int leftColor,int rightColor) {
        tvCancel.setTextColor(leftColor);
        tvConfirm.setTextColor(rightColor);
    }
    public void setViewData(String title,String leftBtn,String rightBtn) {
        tvTitle.setText(title);
        tvCancel.setText(leftBtn);
        tvConfirm.setText(rightBtn);
    }
}
