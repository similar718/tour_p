package cn.xmzt.www.mine.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityInformationBinding;
import cn.xmzt.www.mine.event.NicknameEvent;
import cn.xmzt.www.mine.model.InformationViewModel;
import cn.xmzt.www.utils.PhotoUtils;
import cn.xmzt.www.utils.ToastUtils;
import cn.xmzt.www.utils.utilFixSevent.FileProvider7;
import cn.xmzt.www.utils.utilFixSevent.PhotoFitSevent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tanlei
 * @date 2019/8/5
 * @describe 修改个人信息界面
 */
public class InformationActivity extends TourBaseActivity<InformationViewModel, ActivityInformationBinding> {

    @Override
    protected int setLayoutId() {
        return R.layout.activity_information;
    }

    @Override
    protected InformationViewModel createViewModel() {
        return new InformationViewModel();
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        dataBinding.setModel(viewModel);
        viewModel.getUserBasicInfo();
        viewModel.setActivity(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void changeNickName(NicknameEvent nicknameEvent) {
        dataBinding.nicknameTv.setText(nicknameEvent.getNickName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 检查拍照权限并启动相机
     */
    public void startCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 进入这儿表示没有权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                // 提示已经禁止
                ToastUtils.showText(this, "请开启相机权限");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            }
        } else {
            takePhotoByCamera();
        }
    }

    /**
     * 启动相机
     */
    private void takePhotoByCamera() {
//        imageUri=FileProvider7.getUriForFile(getApplicationContext(),fileUri);
//        PhotoUtils.takePicture(this, imageUri, 200);
        PictureSelector.create(InformationActivity.this)
                .openCamera(PictureMimeType.ofImage())// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                .isChangeStatusBarFontColor(true)// 是否关闭白色状态栏字体颜色
                .setStatusBarColorPrimaryDark(Color.parseColor("#ffffff"))// 状态栏背景色
                .enableCrop(true)// 是否裁剪
                .compress(true)// 是否压缩
                .glideOverride(480, 480)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                .isGif(true)// 是否显示gif图片
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .circleDimmedLayer(true)// 是否圆形裁剪
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .previewEggs(false)//预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                .cutOutQuality(90)// 裁剪输出质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .cropWH(480,480)// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    /**
     * 检查读取sd卡权限并启动相册
     */
    public void startAlbum() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 进入这儿表示没有权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // 提示已经禁止
                ToastUtils.showText(this, "请开启读取sd卡权限");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 进入这儿表示没有权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // 提示已经禁止
                ToastUtils.showText(this, "请开启写入sd卡权限");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        } else {
//            takePhotoByAlbum();
            PictureSelector.create(InformationActivity.this)
                    .openGallery(PictureMimeType.ofImage())
                    .maxSelectNum(1)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                    .enableCrop(true)
                    .compress(true)// 是否压缩
                    .glideOverride(480, 480)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .cutOutQuality(90)// 裁剪输出质量 默认100
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    .cropWH(480,480)// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                    .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                    .isGif(true)// 是否显示gif图片
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                    .circleDimmedLayer(true)// 是否圆形裁剪
                    .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .forResult(PictureConfig.CHOOSE_REQUEST);// 裁剪输出质量 默认100;
        }
    }

    /**
     * 启动相册
     */
    private void takePhotoByAlbum() {
        PhotoUtils.openPic(this, 300);
    }

    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 200: {//拍照完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, 480, 480, 400);
                    break;
                }
                case 300: {//访问相册完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
                    String imgPath = PhotoFitSevent.handleImageOnKitKat(data, this);
                    Uri newUri = FileProvider7.getUriForFile(getApplicationContext(), new File(imgPath));
                    PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, 480, 480, 400);
                    break;
                }
                case 400: {
                    String realFilePath = PhotoUtils.getRealFilePath(this, cropImageUri);
                    File file = new File(realFilePath);
                    List<File> list = new ArrayList<>();
                    list.add(file);
                    viewModel.uploadImage(list);
                    break;
                }
                case PictureConfig.CHOOSE_REQUEST:{
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if(selectList!=null&&selectList.size()>0){
//                        Log.i(TAG, "压缩---->" + media.getCompressPath());
//                        Log.i(TAG, "原图---->" + media.getPath());
//                        Log.i(TAG, "裁剪---->" + media.getCutPath());
//                        Log.i(TAG, "Android Q 特有Path---->" + media.getAndroidQToPath());
                        LocalMedia media=selectList.get(0);
                        File fileCompr = new File(media.getCompressPath());
                        List<File> list = new ArrayList<>();
                        list.add(fileCompr);
                        viewModel.uploadImage(list);
                    }

                    break;
                }
            }
        }
    }
}
