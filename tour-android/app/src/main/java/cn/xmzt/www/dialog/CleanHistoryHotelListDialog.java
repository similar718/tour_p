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
 * @describe 清空酒店浏览历史列表的dialog
 */

public class CleanHistoryHotelListDialog extends Dialog {
    private TextView tvConfirm, tvCancel;

    public CleanHistoryHotelListDialog(@NonNull Context context, View.OnClickListener onClickListener) {
        this(context, 0, onClickListener);
    }

    public CleanHistoryHotelListDialog(@NonNull Context context, int themeResId, View.OnClickListener onClickListener) {
        super(context, themeResId);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_clean_history_hotel_list, null);
        tvConfirm = view.findViewById(R.id.tv_confirm);
        tvCancel = view.findViewById(R.id.tv_cancel);
        setContentView(view);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        tvConfirm.setOnClickListener(onClickListener);
    }
}
