package cn.xmzt.www.intercom.actions;

import android.content.Intent;

import cn.xmzt.www.R;
import cn.xmzt.www.intercom.business.session.constant.RequestCode;
import cn.xmzt.www.intercom.business.session.helper.SendImageHelper;
import cn.xmzt.www.intercom.common.ToastHelper;
import cn.xmzt.www.intercom.common.media.imagepicker.ImagePickerLauncher;
import cn.xmzt.www.intercom.event.AnyRtcStatusEvent;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * Created by Averysk
 */
public class CameraAction extends BaseAction {


    public CameraAction(){
        this(R.drawable.ic_chat_actions_camera, R.string.intercom_input_panel_camera);
    }

    public CameraAction(int iconResId, int titleId) {
        super(iconResId, titleId);
    }

    @Override
    public void onClick() {
        EventBus.getDefault().postSticky(new AnyRtcStatusEvent(1013));
        int requestCode = makeRequestCode(RequestCode.PICK_CAMERA);
        showSelector(requestCode);
    }

    /**
     * 打开相机拍照
     */
    private void showSelector(final int requestCode) {
        ImagePickerLauncher.takePhoto(getActivity(), requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCode.PICK_CAMERA:
                onPickImageActivityResult(requestCode, data);
                break;
        }
    }

    /**
     * 图片选取回调
     */
    private void onPickImageActivityResult(int requestCode, Intent data) {
        if (data == null) {
            ToastHelper.showToastLong(getActivity(), R.string.picker_image_error);
            return;
        }
        sendImageAfterSelfImagePicker(data);
    }


    /**
     * 发送图片
     */
    private void sendImageAfterSelfImagePicker(final Intent data) {
        SendImageHelper.sendImageAfterSelfImagePicker(getActivity(), data, new SendImageHelper.Callback() {

            @Override
            public void sendImage(File file, boolean isOrig) {
                onPicked(file);
            }
        });
    }

    private void onPicked(File file){
        IMMessage message = MessageBuilder.createImageMessage(getAccount(), getSessionType(), file, file.getName());
        sendMessage(message);
    }

}
