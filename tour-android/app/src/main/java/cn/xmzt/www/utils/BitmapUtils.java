package cn.xmzt.www.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.facebook.common.internal.ByteStreams;
import com.umeng.commonsdk.debug.E;

import cn.xmzt.www.base.TourApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.LogManager;

public class BitmapUtils {
    public static int IO_BUFFER_SIZE = 2 * 1024;

    /**
     * 把网络图片设置成毛玻璃的效果
     *
     * @param url
     * @param scaleRatio
     * @return
     */
    public static Bitmap GetUrlBitmap(String url, int scaleRatio) {
        int blurRadius = 8;//通常设置为8就行。
        if (scaleRatio <= 0) {
            scaleRatio = 10;
        }
        Bitmap originBitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
            ByteStreams.copy(in, out);
            out.flush();
            byte[] data = dataStream.toByteArray();
            originBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(originBitmap,
                    originBitmap.getWidth() / scaleRatio,
                    originBitmap.getHeight() / scaleRatio,
                    false);
            Bitmap blurBitmap = doBlur(scaledBitmap, blurRadius, true);
            return blurBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap doBlur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {
        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }
        if (radius < 1) {
            return (null);
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;
        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];
        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }
        yw = yi = 0;
        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;
        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;


            for (x = 0; x < w; x++) {
                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);


                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];


                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;


                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];


                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];


                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];


                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;


                sir = stack[i + radius];


                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];


                rbs = r1 - Math.abs(i);


                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;


                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }


                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];
                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
                yi += w;
            }
        }
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }


    /**
     * 将自定义的view 转bitmap
     */
    public static Bitmap convertViewToBitmap(View view) {
        try {
            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            view.buildDrawingCache();
            Bitmap bitmap = view.getDrawingCache();
            return bitmap;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 把两个位图覆盖合成为一个位图，以底层位图的长宽为基准
     *
     * @param backBitmap  在底部的位图
     * @param frontBitmap 盖在上面的位图
     * @return
     */
    public static Bitmap mergeBitmap(Bitmap backBitmap, Bitmap frontBitmap) {
        if (backBitmap == null || backBitmap.isRecycled()
                || frontBitmap == null || frontBitmap.isRecycled()) {
            return null;
        }
        Bitmap newBitmap = Bitmap.createBitmap(backBitmap.getWidth(), backBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(backBitmap, 0, 0, null);
        canvas.drawBitmap(frontBitmap, backBitmap.getWidth() - frontBitmap.getWidth() - DpUtil.dp2px(TourApplication.context, 2),
                backBitmap.getHeight() - DpUtil.dp2px(TourApplication.context, 16) - frontBitmap.getHeight(), null);
        Paint paint = new Paint();
        paint.setTextSize(DpUtil.sp2px(TourApplication.context, 3));
        paint.setColor(Color.parseColor("#24A4FF"));
        Rect bounds = new Rect();
        String text = "长按识别二维码";
        paint.getTextBounds(text, 0, text.length(), bounds);
        canvas.drawText(text, backBitmap.getWidth() - (frontBitmap.getWidth() + bounds.width()) / 2 - DpUtil.dp2px(TourApplication.context, 2),
                backBitmap.getHeight() - DpUtil.dp2px(TourApplication.context, 12), paint);
        canvas.save();
        canvas.restore();
        return newBitmap;
    }

    /**
     * 保存本地图片至文件中
     *
     * @param path
     * @param bm
     * @param picName
     * @param context
     * @throws IOException
     */
    public static void saveBitmap(String path, Bitmap bm, String picName, Context context) throws IOException {
        File f = new File(path, picName);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            //把图片保存后声明这个广播事件通知系统相册有新图片到来
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(f);
            intent.setData(uri);
            context.sendBroadcast(intent);
            ToastUtils.showText(context, "图片保存成功!!!");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 得到图片长宽均不超过最大值需要缩放的倍数
     *
     * @param imagePath
     * @param screen_width
     * @param screen_height
     * @return 图片缩放倍数
     */
    public static int getImageScale(String imagePath, int screen_width,
                                    int screen_height) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;// 表示仅读取图片文件信息而不为图片分配内存。
        BitmapFactory.decodeFile(imagePath, option);
        int outWidth = option.outWidth;
        int outHeight = option.outHeight;
        int scale = 1;
        float wScale = outWidth / screen_width;
        float hScale = outHeight / screen_height;
        if (wScale > 1 && hScale > 1) {
            scale = Math.round((wScale > hScale) ? wScale : hScale);
        }

        Log.e("tag", "wScale= " + wScale + "  hScale= " + hScale + "  scale= "
                + scale + "  outWidth= " + outWidth + "  outHeight= "
                + outHeight);
        return scale;
    }
    /**
     * 将图片截取为圆角图片
     *
     * @param bitmap
     *            原图片
     * @param roundPx
     *            截取比例，如果是8，则圆角半径是宽高的1/8，如果是2，则是圆形图片
     * @return 圆角矩形图片
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        Bitmap output = null;
        if (bitmap != null) {
            try {
                output = Bitmap.createBitmap(bitmap.getWidth(),
                        bitmap.getHeight(), Bitmap.Config.ARGB_4444);
            } catch (OutOfMemoryError e) {
                // TODO: handle exception
                Log.e("tag", "ReadBitMap: OutOfMemoryError异常", e);
                if (output != null) {
                    output.recycle();
                    output = null;
                }
                if (bitmap != null) {
                    bitmap.recycle();
                    bitmap = null;
                }
            }
            if (output == null) {
                return null;
            }
            Canvas canvas = new Canvas(output);
            // canvas.drawColor(Color.RED);
            final int color = 0xff424242;
            final Paint paint = new Paint();
            int size = (bitmap.getWidth() > bitmap.getHeight()) ? bitmap
                    .getHeight() : bitmap.getWidth();
            final Rect rect = new Rect(0, 0, size, size);
            final RectF rectF = new RectF(rect);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, size / roundPx, size / roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
        }
        return output;
    }

    /**
     * 获取圆形Drawable
     * @param context
     * @param path
     * @return
     */
    public static RoundedBitmapDrawable getRoundedBitmapDrawable(Context context, String path) {
        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), path);
        circularBitmapDrawable.setCircular(true);
        return circularBitmapDrawable;
    }
    /**
     * 获取圆形Drawable
     * @param context
     * @param bitmap
     * @return
     */
    public static RoundedBitmapDrawable getRoundedBitmapDrawable(Context context,Bitmap bitmap) {
        RoundedBitmapDrawable circularBitmapDrawable =RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        circularBitmapDrawable.setCircular(true);
        return circularBitmapDrawable;
    }
    /**
     * 获取圆形Drawable
     * @param context
     * @param resId
     * @return
     */
    public static RoundedBitmapDrawable getRoundedBitmapDrawable(Context context,int resId) {
        Bitmap bitmap=ReadBitMap(context, resId);
        if(bitmap!=null){
            RoundedBitmapDrawable circularBitmapDrawable =RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
            circularBitmapDrawable.setCircular(true);
            return circularBitmapDrawable;
        }else{
            return null;
        }

    }
    public static ByteArrayOutputStream compressBmpToByteArrayOutputStream(Bitmap bmp,long fileCeiling) throws Exception {
        if (null == bmp) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length > fileCeiling) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        return baos;
    }

    /**
     * Bitmap转byte
     * @param bmp
     * @param fileCeiling 大小上限
     * @return
     */
    public static byte[] bmpToByteArray(Bitmap bmp,long fileCeiling) {
        ByteArrayOutputStream output = null;
        byte[] result =null;
        try {
            output = compressBmpToByteArrayOutputStream(bmp,fileCeiling);
            result = output.toByteArray();
            output.close();
        } catch (Exception e) {
            Log.e("","",e);
        }
        return result;
    }
    /**
     * 旋转图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {

        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        if (degrees == 0) {
            return bitmap;
        } else {
            Bitmap output = null;
            try {
                output = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), matrix, true);
                bitmap.recycle();
            } catch (OutOfMemoryError e) {
                // TODO: handle exception
                Log.e("tag", "ReadBitMap: OutOfMemoryError异常", e);
                if (output != null) {
                    output.recycle();
                    output = null;
                }
                if (bitmap != null) {
                    bitmap.recycle();
                    bitmap = null;
                }
            }
            return output;

        }
    }

    public static Bitmap canvasRotateBitmap(Bitmap bitmap, final int degrees) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix m = new Matrix();
        m.setRotate(degrees, (float) width / 2, (float) height / 2);
        float targetX, targetY;
        if (degrees == 90) {
            targetX = height;
            targetY = 0;
        } else {
            targetX = height;
            targetY = width;
        }

        final float[] values = new float[9];
        m.getValues(values);

        float x1 = values[Matrix.MTRANS_X];
        float y1 = values[Matrix.MTRANS_Y];

        m.postTranslate(targetX - x1, targetY - y1);

        Bitmap output = Bitmap.createBitmap(height, width,
                Bitmap.Config.ARGB_8888);

        Paint paint = new Paint();
        Canvas canvas = new Canvas(output);
        canvas.drawBitmap(bitmap, m, paint);

        return output;
    }

    /**
     * 放大缩小图片
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidht = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidht, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return newbmp;
    }

    /**
     * Drawable转化为Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
                .getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

    public static Bitmap setAlpha(Bitmap sourceImg, int number) {
        int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];
        sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0,
                sourceImg.getWidth(), sourceImg.getHeight());
        number = number * 255 / 100;

        for (int i = 0; i < argb.length; i++) {
            argb[i] = (number << 24) | argb[i] & 0x00FFFFFF;
        }
        sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(),
                sourceImg.getHeight(), Bitmap.Config.ARGB_8888);
        return sourceImg;
    }

    public static Intent getPDF(String path) {
        Intent i = new Intent(Intent.ACTION_VIEW);

        i.addCategory(Intent.CATEGORY_DEFAULT);

        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uri = Uri.fromFile(new File(path));

        i.setDataAndType(uri, "pdf");

        return i;

    }

    public static Bitmap ReadBitMap(String imgPath, int multiple) {
        Bitmap bitmap = null;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inSampleSize = multiple;// 表示图片长宽同时缩放的倍数。
        opt.inJustDecodeBounds = false;
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = null;
        try {
            File mfile = new File(imgPath);
            if (mfile.exists()) {// 若该文件存在
                is = new FileInputStream(mfile);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            Log.e("tag", "ReadBitMap: 没有发现文件异常", e);
            if (is != null) {
                try {
                    is.close();
                    is = null;
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    Log.e("tag", "ReadBitMap: io关闭异常", e1);
                }
            }
        }
        if (is != null) {
            // 获取资源图片
            try {
                bitmap = BitmapFactory.decodeStream(is, null, opt);
            } catch (OutOfMemoryError e) {
                // TODO: handle exception
                Log.e("tag", "ReadBitMap: OutOfMemoryError异常", e);
                if (bitmap != null) {
                    bitmap.recycle();
                    bitmap = null;
                }
            }
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                Log.e("tag", "ReadBitMap: io关闭异常", e1);
            }
        }

        return bitmap;

    }
    public static Bitmap ReadBitMap(Context context, int resId) {
        Bitmap bitmap = null;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = false;
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        try {
            bitmap = BitmapFactory.decodeStream(is, null, opt);
        } catch (OutOfMemoryError e) {
            Log.e("shiyong", "ReadBitMap 通过资源图片id获取图片", e);
        }
        return bitmap;

    }
    public static Bitmap ReadBitMap(Context context, int resId, int width) {
        Bitmap bitmap = null;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        // opt.inSampleSize = 4;
        opt.inJustDecodeBounds = false;
        opt.inPreferredConfig = Bitmap.Config.RGB_565;

        opt.inPurgeable = true;

        opt.inInputShareable = true;
        // 获取资源图片

        InputStream is = context.getResources().openRawResource(resId);
        try {
            if (width > 0) {
                Bitmap mbitmap = BitmapFactory.decodeStream(is, null, opt);
                int height = mbitmap.getHeight() * width / mbitmap.getWidth();
                bitmap = Bitmap
                        .createScaledBitmap(mbitmap, width, height, true);
                // mbitmap.recycle();

            } else {
                bitmap = BitmapFactory.decodeStream(is, null, opt);
            }

        } catch (OutOfMemoryError e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return bitmap;

    }
    /**
     * @param imgPath
     * @return
     */
    public static Bitmap ReadBitMap(String imgPath) {
        if (imgPath == null) {
            return null;
        }
        File mfile = new File(imgPath);
        if (!mfile.exists()) {// 若该文件不存在
            return null;
        }
        Bitmap bitmap = null;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgPath, opt);
        opt.inSampleSize = getImageScale(imgPath, 480, 800);
        opt.inJustDecodeBounds = false;
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opt.inPurgeable = true;
        opt.inInputShareable = true;

        InputStream is = null;
        try {
            is = new FileInputStream(mfile);
        } catch (FileNotFoundException e) {
            Log.e("tag", "ReadBitMap: 没有发现文件异常", e);
            if (is != null) {
                try {
                    is.close();
                    is = null;
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    Log.e("tag", "ReadBitMap: io关闭异常", e1);
                }
            }
        }
        if (is != null) {
            // 获取资源图片
            Bitmap mbitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(is, null, opt);
            } catch (OutOfMemoryError e) {
                // TODO: handle exception
                Log.e("tag", "ReadBitMap: OutOfMemoryError异常", e);
                if (mbitmap != null) {
                    mbitmap.recycle();
                    mbitmap = null;
                }
                if (bitmap != null) {
                    bitmap.recycle();
                    bitmap = null;
                }
            }
            try {
                is.close();
                is = null;
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                Log.e("tag", "ReadBitMap: io关闭异常", e1);
            }
        }

        return bitmap;

    }
    /**
     * 得到图片长宽均不超过480*800
     *
     * @param imgPath
     * @param width
     * @param height
     * @return
     */
    @SuppressWarnings("unused")
    public static Bitmap ReadBitMap(String imgPath, int width, int height) {
        if (imgPath == null) {
            return null;
        }
        File mfile = new File(imgPath);
        if (!mfile.exists()) {// 若该文件不存在
            return null;
        }
        Bitmap bitmap = null;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgPath, opt);
        opt.inSampleSize = getImageScale(imgPath, 480, 800);
        // opt.inSampleSize = 4;

        opt.inJustDecodeBounds = false;
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opt.inPurgeable = true;
        opt.inInputShareable = true;

        InputStream is = null;
        try {
            is = new FileInputStream(mfile);
        } catch (FileNotFoundException e) {
            Log.e("tag", "ReadBitMap: 没有发现文件异常", e);
            if (is != null) {
                try {
                    is.close();
                    is = null;
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    Log.e("tag", "ReadBitMap: io关闭异常", e1);
                }
            }
        }
        if (is != null) {
            // 获取资源图片
            Bitmap mbitmap = null;
            try {
                if (width > 0 || height > 0) {
                    if (width < 480 && height < 800) {
                        mbitmap = BitmapFactory.decodeStream(is, null, opt);
                        bitmap = Bitmap.createScaledBitmap(mbitmap, width,
                                height, true);
                        mbitmap.recycle();
                        mbitmap = null;
                    }
                } else {
                    bitmap = BitmapFactory.decodeStream(is, null, opt);
                }
            } catch (OutOfMemoryError e) {
                // TODO: handle exception
                Log.e("tag", "ReadBitMap: OutOfMemoryError异常", e);
                if (mbitmap != null) {
                    mbitmap.recycle();
                    mbitmap = null;
                }
                if (bitmap != null) {
                    bitmap.recycle();
                    bitmap = null;
                }
            }
            try {
                is.close();
                is = null;
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                Log.e("tag", "ReadBitMap: io关闭异常", e1);
            }
        }

        return bitmap;

    }

    /**
     * 将字节数组转换为ImageView可调用的Bitmap对象
     * @param bytes
     * @param width
     * @param height
     * @return Bitmap
     */
    public static Bitmap getPicFromBytes(byte[] bytes, int width, int height) {
        Bitmap bitmap = null;
        if (bytes != null) {
            Bitmap mbitmap = null;
            try {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                // 这3句是处理图片溢出的begin( 如果不需要处理溢出直接 opts.inSampleSize=1;)
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
                opts.inSampleSize = computeSampleSize(opts, -1, 480 * 800);
                opts.inJustDecodeBounds = false;
                // 下面三行代码可以不要
                opts.inPreferredConfig = Bitmap.Config.RGB_565;
                opts.inPurgeable = true;
                opts.inInputShareable = true;
                if (width > 0 || height > 0) {
                    mbitmap = BitmapFactory.decodeByteArray(bytes, 0,
                            bytes.length, opts);
                    bitmap = Bitmap.createScaledBitmap(mbitmap, width, height,
                            true);
                    mbitmap.recycle();
                    mbitmap = null;
                } else {
                    bitmap = BitmapFactory.decodeByteArray(bytes, 0,
                            bytes.length, opts);
                }

                return bitmap;
            } catch (OutOfMemoryError e) {
                // TODO: handle exception
                Log.e("tag", "ReadBitMap: OutOfMemoryError异常", e);
                if (mbitmap != null) {
                    mbitmap.recycle();
                    mbitmap = null;
                }
                if (bitmap != null) {
                    bitmap.recycle();
                    bitmap = null;
                }
            }
        }
        return null;
    }

    /****
     * 处理图片bitmap size exceeds VM budget （Out Of Memory 内存溢出）
     */
    private static int computeSampleSize(BitmapFactory.Options options,
                                         int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static byte[] bitmap2Bytes(Bitmap bm) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);

        return baos.toByteArray();

    }

    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }
    /**
     * 图片过滤
     * @param sat sat==0是黑白图片
     * @return
     */
    public static ColorMatrixColorFilter getColorMatrixColorFilter(float sat) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(sat);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        return filter;
    }

    /**
     * 把view截图
     * @param view
     * @param path
     * @return
     */
    public static boolean viewScreenShot(View view,String path){
        boolean isSave=false;
        try {
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();  //启用DrawingCache并创建位图
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache()); //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
            isSave=saveBitmapToSDCard(bitmap,path);
            view.setDrawingCacheEnabled(false);  //禁用DrawingCahce否则会影响性能
        }catch (Exception e){
            Log.e("shiyong", "View 截屏失败",e);
            view.setDrawingCacheEnabled(false);  //禁用DrawingCahce否则会影响性能
        }
        return isSave;
    }
    /**将bitmap对象保存成图片到sd卡中*/
    public static boolean saveBitmapToSDCard(Bitmap bitmap, String path) {
        File file = new File(path);
        if(file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch(Exception e) {
            Log.e("shiyong","saveBitmapToSDCard:失败 ",e );
            return false;
        }
    }
}
