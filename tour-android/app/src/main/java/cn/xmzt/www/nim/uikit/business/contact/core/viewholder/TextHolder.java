package cn.xmzt.www.nim.uikit.business.contact.core.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.nim.uikit.business.contact.core.item.TextItem;
import cn.xmzt.www.nim.uikit.business.contact.core.model.ContactDataAdapter;

public class TextHolder extends AbsContactViewHolder<TextItem> {
    private TextView textView;

    public void refresh(ContactDataAdapter contactAdapter, int position, TextItem item) {
        textView.setText(item.getText());
    }

    @Override
    public View inflate(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.nim_contact_text_item, null);
        textView = (TextView) view.findViewById(R.id.text);
        return view;
    }
}
