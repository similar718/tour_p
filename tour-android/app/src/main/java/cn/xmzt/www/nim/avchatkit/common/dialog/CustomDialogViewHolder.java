package cn.xmzt.www.nim.avchatkit.common.dialog;

import android.util.Pair;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.nim.avchatkit.common.adapter.TViewHolder;


public class CustomDialogViewHolder extends TViewHolder {

    private TextView itemView;

    @Override
    protected int getResId() {
        return R.layout.nim_custom_dialog_list_item;
    }

    @Override
    protected void inflate() {
        itemView = (TextView) view.findViewById(R.id.custom_dialog_text_view);
    }

    @Override
    protected void refresh(Object item) {
        if (item instanceof Pair<?, ?>) {
            Pair<String, Integer> pair = (Pair<String, Integer>) item;
            itemView.setText(pair.first);
            itemView.setTextColor(context.getResources().getColor(pair.second));
        }
    }

}
