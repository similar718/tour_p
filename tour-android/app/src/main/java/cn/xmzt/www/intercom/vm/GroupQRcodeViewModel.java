package cn.xmzt.www.intercom.vm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.intercom.activity.GroupQRcodeActivity;
import cn.xmzt.www.share.ShareFunction;
import cn.xmzt.www.utils.BitmapUtils;
import cn.xmzt.www.utils.DpUtil;
import cn.xmzt.www.zxing.encoding.EncodingHandler;

import java.io.IOException;

/**
 * @author tanlei
 * @date 2019/9/6
 * @describe
 */

public class GroupQRcodeViewModel extends BaseViewModel {
    private GroupQRcodeActivity activity;
    private Bitmap qrCode;

    public void setTeamId(String teamId) {
        if (!TextUtils.isEmpty(TourApplication.getToken())) {
            createCode("https://g.xmzt.cn/g/g?t=6&i=" + teamId + "&p=" + TourApplication.getUser().getRefCode());
        }
    }

    public void setActivity(GroupQRcodeActivity activity) {
        this.activity = activity;
    }

    private void createCode(String str) {
        qrCode = EncodingHandler.createQRCode(str, DpUtil.dp2px(activity, 247), DpUtil.dp2px(activity, 247),
                BitmapFactory.decodeResource(activity.getResources(), R.drawable.icon_logo));
        activity.dataBinding.ivCode.setImageBitmap(qrCode);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_iv:
                activity.finish();
                break;
            case R.id.tv_save_qr_code://保存二维码
                try {
                    BitmapUtils.saveBitmap(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/", qrCode,
                            System.currentTimeMillis() + ".png", activity);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_share_qr_code://分享二维码
                ShareFunction.getInstance().showShareAction(activity, qrCode);
                break;
            default:
                break;
        }
    }
}
