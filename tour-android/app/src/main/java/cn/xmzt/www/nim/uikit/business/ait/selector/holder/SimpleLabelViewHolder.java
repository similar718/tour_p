package cn.xmzt.www.nim.uikit.business.ait.selector.holder;

import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.nim.uikit.business.ait.selector.model.AitContactItem;
import cn.xmzt.www.nim.uikit.common.ui.recyclerview.adapter.BaseQuickAdapter;
import cn.xmzt.www.intercom.business.session.viewholder.BaseViewHolder;
import cn.xmzt.www.nim.uikit.common.ui.recyclerview.holder.RecyclerViewHolder;

/**
 * Created by hzchenkang on 2017/6/21.
 */

public class SimpleLabelViewHolder extends RecyclerViewHolder<BaseQuickAdapter, BaseViewHolder, AitContactItem<String>> {

    private TextView textView;

    public SimpleLabelViewHolder(BaseQuickAdapter adapter) {
        super(adapter);
    }

    @Override
    public void convert(BaseViewHolder holder, AitContactItem<String> data, int position, boolean isScrolling) {
        inflate(holder);
        refresh(data.getModel());
    }

    public void inflate(BaseViewHolder holder) {
        textView = holder.getView(R.id.tv_label);
    }

    public void refresh(String label) {
        textView.setText(label);
    }
}
