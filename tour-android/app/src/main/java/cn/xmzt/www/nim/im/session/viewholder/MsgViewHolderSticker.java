package cn.xmzt.www.nim.im.session.viewholder;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import cn.xmzt.www.R;
import cn.xmzt.www.nim.im.session.extension.StickerAttachment;
import cn.xmzt.www.nim.uikit.business.session.emoji.StickerManager;
import cn.xmzt.www.intercom.business.session.viewholder.MsgViewHolderBase;
import cn.xmzt.www.nim.uikit.business.session.viewholder.MsgViewHolderThumbBase;
import cn.xmzt.www.intercom.adapter.list.BaseMultiItemFetchLoadAdapter;

/**
 * Created by zhoujianghua on 2015/8/7.
 */
public class MsgViewHolderSticker extends MsgViewHolderBase {

    private ImageView baseView;

    public MsgViewHolderSticker(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_sticker;
    }

    @Override
    protected void inflateContentView() {
        baseView = findViewById(R.id.message_item_sticker_image);
        baseView.setMaxWidth(MsgViewHolderThumbBase.getImageMaxEdge());
    }

    @Override
    protected void bindContentView() {
        StickerAttachment attachment = (StickerAttachment) message.getAttachment();
        if (attachment == null) {
            return;
        }

        Glide.with(context)
                .load(StickerManager.getInstance().getStickerUri(attachment.getCatalog(), attachment.getChartlet()))
                .apply(new RequestOptions()
                        .error(R.drawable.nim_default_img_failed)
                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(baseView);
    }

    @Override
    protected int leftBackground() {
        return 0;
    }

    @Override
    protected int rightBackground() {
        return 0;
    }
}
