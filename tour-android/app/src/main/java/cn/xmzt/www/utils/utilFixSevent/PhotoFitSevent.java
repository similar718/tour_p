package cn.xmzt.www.utils.utilFixSevent;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** 
 * description: 适配 android 7.0 拍照
 * version: 
*/
public class PhotoFitSevent {
    public static final int REQUEST_CODE_TAKE_PHOTO = 0;
    public final static int SELECT_PIC_BY_PICK_PHOTO = 1;
    public final static int SELECT_CUT_PICK_PHOTO = 2;
    private static String mCurrentPhotoPath;
    /**
     * 启动拍照
     */
    public static String takePhotoNoCompress(Context context) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//拍照到指定目录
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            String filename = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA)
                    .format(new Date()) + ".png";
            File file = new File(Environment.getExternalStorageDirectory(), filename);//创建文件
            mCurrentPhotoPath = file.getAbsolutePath();
            Uri fileUri = FileProvider7.getUriForFile(context, file);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            ((Activity) context).startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO);
        }
        return mCurrentPhotoPath;
    }


    /**
     * h从相册把图片拿出来
     */
    public static void takePhoto(Context context) {
        try {
            Intent intent = new Intent();

            intent.setAction("android.intent.action.PICK");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setType("image/*");
            ((Activity) context).startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 从相册获取图片
     * @param data
     * @param context
     * @return
     */
    public static String handleImageBeforeKitKat(Intent data, Context context) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null,context);
        return imagePath;
    }

    public static String handleImageOnKitKat(Intent data, Context context){
        String imagePath = null;

        if (data == null) {
            return null;
        }
        Uri uri = data.getData();
        if (uri == null) {
            return null ;
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&& DocumentsContract.isDocumentUri(context,uri)){
            //如果是document类型的Uri,则通过document id 处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" +id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection,context);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                //这个方法负责把id和contentUri连接成一个新的Uri
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null,context);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的Uri,则使用普通方式处理
            imagePath =getImagePath(uri,null,context);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri,直接获取图片路径即可
            imagePath = uri.getPath();
        }
        return imagePath;
    }
    public static String getImagePath(Uri uri, String selection, Context context){
        String path = null;
        Cursor cursor =  context.getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToNext()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return  path;
    }
}
