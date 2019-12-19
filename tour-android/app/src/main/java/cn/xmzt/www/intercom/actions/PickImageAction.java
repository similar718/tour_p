package cn.xmzt.www.intercom.actions;

import android.content.Intent;

import cn.xmzt.www.R;
import cn.xmzt.www.intercom.business.session.constant.RequestCode;
import cn.xmzt.www.intercom.business.session.helper.SendImageHelper;
import cn.xmzt.www.intercom.common.ToastHelper;
import cn.xmzt.www.intercom.common.media.imagepicker.ImagePickerLauncher;
import cn.xmzt.www.intercom.common.media.imagepicker.option.DefaultImagePickerOption;
import cn.xmzt.www.intercom.common.media.imagepicker.option.ImagePickerOption;

import java.io.File;

/**
 * Created by Averysk
 */
public abstract class PickImageAction extends BaseAction {

    private static final int PICK_IMAGE_COUNT = 9;

    public static final String MIME_JPEG = "image/jpeg";

    private boolean multiSelect;

    protected abstract void onPicked(File file);

    protected PickImageAction(int iconResId, int titleId, boolean multiSelect) {
        super(iconResId, titleId);
        this.multiSelect = multiSelect;
    }

    @Override
    public void onClick() {
        int requestCode = makeRequestCode(RequestCode.PICK_IMAGE);
        showSelector(getTitleId(), requestCode, multiSelect);
    }

    /**
     * 打开图片选择器
     */
    private void showSelector(int titleId, final int requestCode, final boolean multiSelect) {
        ImagePickerOption option = DefaultImagePickerOption.getInstance().setShowCamera(true).setPickType(
                ImagePickerOption.PickType.All).setMultiMode(multiSelect).setSelectMax(PICK_IMAGE_COUNT);
        ImagePickerLauncher.selectImage(getActivity(), requestCode, option, titleId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCode.PICK_IMAGE:
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

}
