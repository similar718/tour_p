package cn.xmzt.www.nim.im.session.action;


import cn.xmzt.www.R;
import cn.xmzt.www.nim.rtskit.RTSKit;
import cn.xmzt.www.intercom.actions.BaseAction;
import cn.xmzt.www.intercom.common.ToastHelper;
import cn.xmzt.www.nim.uikit.common.util.sys.NetworkUtil;

/**
 * Created by huangjun on 2015/7/7.
 */
public class RTSAction extends BaseAction {

    public RTSAction() {
        super(R.drawable.message_plus_rts_selector, R.string.input_panel_RTS);
    }

    @Override
    public void onClick() {
        if (NetworkUtil.isNetAvailable(getActivity())) {
            RTSKit.startRTSSession(getActivity(), getAccount());
        } else {
            ToastHelper.showToast(getActivity(), R.string.network_is_not_available);
        }

    }
}
