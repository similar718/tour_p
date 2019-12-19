package cn.xmzt.www.nim.uikit.common.media.imagepicker.data;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import android.text.TextUtils;

import cn.xmzt.www.nim.uikit.common.media.model.GLImage;

import java.io.File;
import java.util.ArrayList;


/**

 */

public class ImageDataSource extends CursorDataSource {

    private final String[] IMAGE_PROJECTION = {     //查询图片需要的数据列
                                                    MediaStore.MediaColumns.DISPLAY_NAME,
                                                    //图片的显示名称  aaa.jpg
                                                    MediaStore.MediaColumns.DATA,
                                                    //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
                                                    MediaStore.MediaColumns.SIZE,
                                                    //图片的大小，long型  132492
                                                    MediaStore.MediaColumns.WIDTH,
                                                    //图片的宽度，int型  1920
                                                    MediaStore.MediaColumns.HEIGHT,
                                                    //图片的高度，int型  1080
                                                    MediaStore.MediaColumns.MIME_TYPE,
                                                    //图片的类型     image/jpeg
                                                    MediaStore.MediaColumns.DATE_ADDED,
                                                    //图片被添加的时间，long型  1450518608
                                                    MediaStore.Images.ImageColumns.ORIENTATION,
                                                    };

    private final static String IMAGE_SELECTION =
            MediaStore.Images.Media.MIME_TYPE + "!=? and " + MediaStore.Images.Media.MIME_TYPE + "!=?";

    private final static String[] IMAGE_SELECTION_ARGS = {"image/gif", "image/webp"};

    //    private final static String IMAGE_SELECTION = MediaStore.Images.Media.MIME_TYPE + "!=?";
    //    private final static String[] IMAGE_SELECTION_ARGS = {"image/webp"};

    /**
     * @param activity 用于初始化LoaderManager，需要兼容到2.3
     * @param path     指定扫描的文件夹目录，可以为 null，表示扫描所有图片
     */
    ImageDataSource(FragmentActivity activity, String path) {
        super(activity, path);
    }

    @Override
    protected int getBaseId() {
        return 10;
    }

    @Override
    protected Uri getMediaStoreUri() {
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    protected String[] getProjection() {
        return IMAGE_PROJECTION;
    }

    @Override
    protected String[] getSelectionArgs() {
        return IMAGE_SELECTION_ARGS;
    }

    @NonNull
    @Override
    protected String getSelection() {
        return IMAGE_SELECTION;
    }

    protected void parserRealData(Cursor data, ArrayList<GLImage> allImages, ArrayList<ImageFolder> imageFolders) {
        if (data.moveToFirst()) {
            int keyName = data.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
            int keyData = data.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            int keySize = data.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE);
            int keyWidth = data.getColumnIndexOrThrow(MediaStore.MediaColumns.WIDTH);
            int keyHeight = data.getColumnIndexOrThrow(MediaStore.MediaColumns.HEIGHT);
            int keyDate = data.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED);
            int keyMime = data.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE);
            do {
                String imagePath = data.getString(keyData);
                if (TextUtils.isEmpty(imagePath)) {
                    continue;
                }
                String imageMimeType = data.getString(keyMime);
                //String imageMimeType = MediaUtil.getMimeType(imagePath);
                if (TextUtils.isEmpty(imageMimeType) || imageMimeType.contains("tiff")) {
                    continue;
                }

                String imageName = data.getString(keyName);
                long imageSize = data.getLong(keySize);
                File imageFile = new File(imagePath);
                if (imageSize == 0) {
                    imageSize = imageFile!=null? imageFile.length(): 0;
                }

                int imageWidth = data.getInt(keyWidth);
                int imageHeight = data.getInt(keyHeight);
                long imageAddTime = data.getLong(keyDate);

                GLImage glImage = GLImage.Builder.newBuilder().setName(imageName).setPath(imagePath).setSize(
                        imageSize).setWidth(imageWidth).setHeight(imageHeight).setMimeType(imageMimeType).setAddTime(
                        imageAddTime * 1000).build();

                allImages.add(glImage);
                //根据父路径分类存放图片
                File imageParentFile = imageFile != null ? imageFile.getParentFile() : null;
                if (imageParentFile == null) {
                    continue;
                }
                ImageFolder imageFolder = new ImageFolder();
                imageFolder.name = imageParentFile.getName();
                imageFolder.path = imageParentFile.getAbsolutePath();

                if (!imageFolders.contains(imageFolder)) {
                    ArrayList<GLImage> images = new ArrayList<>();
                    images.add(glImage);
                    imageFolder.cover = glImage;
                    imageFolder.images = images;
                    imageFolders.add(imageFolder);
                } else {
                    imageFolders.get(imageFolders.indexOf(imageFolder)).images.add(glImage);
                }
            } while (data.moveToNext());
        }
    }
}
