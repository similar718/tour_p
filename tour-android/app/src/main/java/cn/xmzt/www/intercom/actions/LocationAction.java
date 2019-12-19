package cn.xmzt.www.intercom.actions;

import cn.xmzt.www.R;
import cn.xmzt.www.intercom.api.model.location.LocationProvider;
import cn.xmzt.www.intercom.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * Created by Averysk
 */
public class LocationAction extends BaseAction {

    public LocationAction() {
        super(R.drawable.ic_chat_bottom_position, R.string.intercom_input_panel_position);
    }

    @Override
    public void onClick() {
        if (NimUIKitImpl.getLocationProvider() != null) {
            NimUIKitImpl.getLocationProvider().requestLocation(getActivity(), new LocationProvider.Callback() {
                @Override
                public void onSuccess(double longitude, double latitude, String address) {
                    IMMessage message = MessageBuilder.createLocationMessage(getAccount(), getSessionType(), latitude, longitude, address);
                    sendMessage(message);
                }
            });
        }
    }
}
