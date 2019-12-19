package cn.xmzt.www.intercom.business.session.helper;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;

import cn.xmzt.www.R;
import cn.xmzt.www.intercom.common.ToastHelper;
import cn.xmzt.www.intercom.common.util.string.StringUtil;
import cn.xmzt.www.nim.uikit.business.session.activity.CaptureVideoActivity;
import cn.xmzt.www.nim.uikit.common.ui.dialog.CustomAlertDialog;
import cn.xmzt.www.nim.uikit.common.util.C;
import cn.xmzt.www.nim.uikit.common.util.file.AttachmentStore;
import cn.xmzt.www.nim.uikit.common.util.file.FileUtil;
import cn.xmzt.www.nim.uikit.common.util.storage.StorageType;
import cn.xmzt.www.nim.uikit.common.util.storage.StorageUtil;
import cn.xmzt.www.nim.uikit.common.util.string.MD5;

/**
 * Created by hzxuwen on 2015/4/10.
 */
public class VideoMessageHelper {
    private File videoFile;
    private String videoFilePath;

    private Activity activity;
    private VideoMessageHelperListener listener;

    private int localRequestCode;
    private int captureRequestCode;

    public VideoMessageHelper(Activity activity, VideoMessageHelperListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    public interface VideoMessageHelperListener {
        void onVideoPicked(File file, String md5);
    }

    /**
     * 显示视频拍摄或从本地相册中选取
     */
    public void showVideoSource(int local, int capture) {
        this.localRequestCode = local;
        this.captureRequestCode = capture;
        CustomAlertDialog dialog = new CustomAlertDialog(activity);
        dialog.setTitle(activity.getString(R.string.input_panel_video));
        dialog.addItem("拍摄视频", new CustomAlertDialog.onSeparateItemClickListener() {
            @Override
            public void onClick() {
                chooseVideoFromCamera();
            }
        });
        dialog.addItem("从相册中选择视频", new CustomAlertDialog.onSeparateItemClickListener() {
            @Override
            public void onClick() {
                chooseVideoFromLocal();
            }
        });
        dialog.show();
    }
    /**
     * 显示视频拍摄或从本地相册中选取
     */
    public void showRequestCode(int local, int capture) {
        this.localRequestCode = local;
        this.captureRequestCode = capture;
    }

    /************************************************* 视频操作S *******************************************/

    /**
     * 拍摄视频
     */
    protected void chooseVideoFromCamera() {
        if (!StorageUtil.hasEnoughSpaceForWrite(activity, StorageType.TYPE_VIDEO, true)) {
            return;
        }
        videoFilePath = StorageUtil.getWritePath(activity, StringUtil.get36UUID() + C.FileSuffix.MP4,
                                                 StorageType.TYPE_TEMP);
        videoFile = new File(videoFilePath);
        // 启动视频录制
        CaptureVideoActivity.start(activity, videoFilePath, captureRequestCode);
    }

    /**
     * 从本地相册中选择视频
     */
    public void chooseVideoFromLocal() {
        if (Build.VERSION.SDK_INT >= 19) {
            chooseVideoFromLocalKitKat();
        } else {
            chooseVideoFromLocalBeforeKitKat();
        }
    }

    /**
     * API19 之后选择视频
     */
    protected void chooseVideoFromLocalKitKat() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*"); //选择视频 （mp4 3gp 是android支持的视频格式）
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        try {
            activity.startActivityForResult(intent, localRequestCode);
        } catch (ActivityNotFoundException e) {
            ToastHelper.showToast(activity, R.string.gallery_invalid);
        } catch (SecurityException e) {

        }
    }

    /**
     * API19 之前选择视频
     */
    protected void chooseVideoFromLocalBeforeKitKat() {
        Intent mIntent = new Intent(Intent.ACTION_GET_CONTENT);
        mIntent.setType(C.MimeType.MIME_VIDEO_ALL);
        mIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        try {
            activity.startActivityForResult(mIntent, localRequestCode);
        } catch (ActivityNotFoundException e) {
            ToastHelper.showToast(activity, R.string.gallery_invalid);
        }
    }

    /****************************视频选中后回调操作********************************************/

    /**
     * 获取本地相册视频回调操作
     */
    public void onGetLocalVideoResult(final Intent data,String filePathSource ) {
        if (data == null) {
            return;
        }
        String filePath = filePathFromIntent(data);
        if(filePath==null){
            filePath=filePathSource;
        }

        if (StringUtil.isEmpty(filePath) || !checkVideoFile(filePath)) {
            return;
        }

        String md5 = MD5.getStreamMD5(filePath);
        String filename = md5 + "." + FileUtil.getExtensionName(filePath);
        String md5Path = StorageUtil.getWritePath(filename, StorageType.TYPE_VIDEO);

        if (AttachmentStore.copy(filePath, md5Path) != -1) {
            if (listener != null) {
                listener.onVideoPicked(new File(md5Path), md5);
            }
        } else {
            ToastHelper.showToast(activity, R.string.video_exception);
        }
    }

    /**
     * 拍摄视频后回调操作
     */
    public void onCaptureVideoResult(Intent data) {

        if (videoFile == null || !videoFile.exists()) {
            //activity 可能会销毁重建，所以从这取一下
            String dataFilePath = data.getStringExtra(CaptureVideoActivity.EXTRA_DATA_FILE_NAME);
            if (!TextUtils.isEmpty(dataFilePath)) {
                videoFile = new File(dataFilePath);
            }
        }

        if (videoFile == null || !videoFile.exists()) {
            return;
        }

        //N930拍照取消也产生字节为0的文件
        if (videoFile.length() <= 0) {
            videoFile.delete();
            return;
        }

        String videoPath = videoFile.getPath();
        String md5 = MD5.getStreamMD5(videoPath);
        String md5Path = StorageUtil.getWritePath(md5 + ".mp4", StorageType.TYPE_VIDEO);

        if (AttachmentStore.move(videoPath, md5Path)) {
            if (listener != null) {
                listener.onVideoPicked(new File(md5Path), md5);
            }
        }
    }

    /**
     * 获取文件路径
     *
     * @param data intent数据
     * @return
     */
    private String filePathFromIntent(Intent data) {
        Uri uri = data.getData();
        if(uri!=null){
            String path=getMediaPath(uri,activity);
            return path; // 文件路径
        }else {
            return null; // 文件路径
        }
    }
    public static String getMediaPath(Uri uri, Context context){
        String path = null;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        try {
            if (cursor == null) {
                //miui 2.3 有可能为null
                return uri.getPath();
            } else {
                cursor.moveToFirst();
                int columnIndex=cursor.getColumnIndex(MediaStore.Video.Media.DATA);
                path = cursor.getString(columnIndex);// 文件路径
            }
        } catch (Exception e){
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return  path;
    }
    /**
     * 检查文件
     *
     * @param file 视频文件
     * @return boolean
     */
    private boolean checkVideoFile(String file) {
        if (!AttachmentStore.isFileExist(file)) {
            return false;
        }

        if (new File(file).length() > C.MAX_LOCAL_VIDEO_FILE_SIZE) {
            ToastHelper.showToast(activity, R.string.im_choose_video_file_size_too_large);
            return false;
        }

        if (!StorageUtil.isInvalidVideoFile(file)) {
            ToastHelper.showToast(activity, R.string.im_choose_video);
            return false;
        }
        return true;
    }

}
