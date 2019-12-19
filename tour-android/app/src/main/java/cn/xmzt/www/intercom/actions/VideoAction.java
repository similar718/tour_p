package cn.xmzt.www.intercom.actions;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;

import com.blankj.utilcode.util.ActivityUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;
import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.intercom.business.session.constant.RequestCode;
import cn.xmzt.www.intercom.business.session.helper.VideoMessageHelper;
import cn.xmzt.www.utils.utilFixSevent.FileProvider7;

/**
 * 小视频
 * Created by Averysk
 */
public class VideoAction extends BaseAction {

    // 视频
    protected transient VideoMessageHelper videoMessageHelper;

    public VideoAction() {
        super(R.drawable.ic_chat_actions_small_video, R.string.intercom_input_panel_video);

    }

    @Override
    public void onClick() {
        //videoHelper().showVideoSource(makeRequestCode(RequestCode.GET_LOCAL_VIDEO), makeRequestCode(RequestCode.CAPTURE_VIDEO));
        /*videoHelper().showRequestCode(makeRequestCode(RequestCode.GET_LOCAL_VIDEO), makeRequestCode(RequestCode.CAPTURE_VIDEO));
        videoHelper().chooseVideoFromLocal();*/
        PictureSelector.create(ActivityUtils.getTopActivity())
                .openGallery(PictureMimeType.ofVideo())
                .isChangeStatusBarFontColor(true)// 是否关闭白色状态栏字体颜色
                .setStatusBarColorPrimaryDark(Color.parseColor("#ffffff"))// 状态栏背景色
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                .forResult(makeRequestCode(RequestCode.GET_LOCAL_VIDEO));//结果回调onActivityResult code
    }

    /**
     * ********************** 视频 *******************************
     */
    private void initVideoMessageHelper() {
        videoMessageHelper = new VideoMessageHelper(getActivity(), new VideoMessageHelper.VideoMessageHelperListener() {

            @Override
            public void onVideoPicked(File file, String md5) {
                MediaPlayer mediaPlayer = getVideoMediaPlayer(file);
                long duration = mediaPlayer == null ? 0 : mediaPlayer.getDuration();
                int height = mediaPlayer == null ? 0 : mediaPlayer.getVideoHeight();
                int width = mediaPlayer == null ? 0 : mediaPlayer.getVideoWidth();
                IMMessage message = MessageBuilder.createVideoMessage(getAccount(), getSessionType(), file, duration, width, height, md5);
                sendMessage(message);
            }
        });
    }

    /**
     * 获取视频mediaPlayer
     *
     * @param file 视频文件
     * @return mediaPlayer
     */
    private MediaPlayer getVideoMediaPlayer(File file) {
        try {
            return MediaPlayer.create(getActivity(), Uri.fromFile(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCode.GET_LOCAL_VIDEO:
                // 图片、视频、音频选择结果回调
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                String videoPath=null;
                if(selectList!=null&&selectList.size()>0){
                    LocalMedia media=selectList.get(0);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        videoPath=media.getAndroidQToPath();
                    }else {
                        videoPath=media.getPath();
                    }
                }
                videoHelper().onGetLocalVideoResult(data,videoPath);
                break;
            case RequestCode.CAPTURE_VIDEO:
                videoHelper().onCaptureVideoResult(data);
                break;
        }
    }

    private VideoMessageHelper videoHelper() {
        if (videoMessageHelper == null) {
            initVideoMessageHelper();
        }
        return videoMessageHelper;
    }
}
