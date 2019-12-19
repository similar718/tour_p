package cn.xmzt.www.nim.uikit.business.session.viewholder;

import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.intercom.business.session.viewholder.MsgViewHolderBase;
import cn.xmzt.www.nim.uikit.common.ui.imageview.MsgThumbImageView;
import cn.xmzt.www.intercom.adapter.list.BaseMultiItemFetchLoadAdapter;
import cn.xmzt.www.nim.uikit.common.util.media.ImageUtil;
import cn.xmzt.www.nim.uikit.common.util.sys.ScreenUtil;
import cn.xmzt.www.intercom.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.msg.attachment.LocationAttachment;

/**
 * Created by zhoujianghua on 2015/8/7.
 */
public class MsgViewHolderLocation extends MsgViewHolderBase {

    public MsgViewHolderLocation(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    public MsgThumbImageView mapView;

    public TextView addressText;

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_location;
    }

    @Override
    protected void inflateContentView() {
        mapView = (MsgThumbImageView) view.findViewById(R.id.message_item_location_image);
        addressText = (TextView) view.findViewById(R.id.message_item_location_address);
    }

    @Override
    protected void bindContentView() {
        final LocationAttachment location = (LocationAttachment) message.getAttachment();
        addressText.setText(location.getAddress());

        int[] bound = ImageUtil.getBoundWithLength(getLocationDefEdge(), R.drawable.nim_location_bk, true);
        int width = bound[0];
        int height = bound[1];

        setLayoutParams(width, height, mapView);
        setLayoutParams(width, (int) (0.38 * height), addressText);

        mapView.loadAsResource(R.drawable.nim_location_bk, R.drawable.nim_message_item_round_bg);
    }

    @Override
    protected int leftBackground() {
        return 0;
    }

    @Override
    protected int rightBackground() {
        return 0;
    }

    @Override
    protected void onItemClick() {
        if (NimUIKitImpl.getLocationProvider() != null) {
            LocationAttachment location = (LocationAttachment) message.getAttachment();
            NimUIKitImpl.getLocationProvider().openMap(context, location.getLongitude(), location.getLatitude(), location.getAddress());
        }
    }

    public static int getLocationDefEdge() {
        return (int) (0.5 * ScreenUtil.screenWidth);
    }
}
