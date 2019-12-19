package cn.xmzt.www.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import cn.xmzt.www.base.BaseActivity;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.pay.eventbus.PayResultBus;


public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            //setContentView(R.layout.pay_result);
            api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
            api.handleIntent(getIntent(), this);
        }catch (Exception e){
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        Log.e("shiyong","req.openId="+req.openId);
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if(resp.errCode==BaseResp.ErrCode.ERR_OK){
                EventBus.getDefault().post(new PayResultBus(true,resp.errCode));
            }else {
                EventBus.getDefault().post(new PayResultBus(false,resp.errCode));
            }
        }
        /*switch (payResultBus.getResultCode()) {
                case BaseResp.ErrCode.ERR_OK:
                    break;
                case BaseResp.ErrCode.ERR_COMM:
                    //签名不对
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    //用户取消
                case BaseResp.ErrCode.ERR_SENT_FAILED:
                    //发送失败
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    //鉴权错误
                case BaseResp.ErrCode.ERR_UNSUPPORT:
                    //不支持
                case BaseResp.ErrCode.ERR_BAN:
                    //禁止
                default:
                    break;
            }*/
        finish();
    }
}