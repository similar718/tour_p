package cn.xmzt.www.utils;

import android.content.Context;

import cn.xmzt.www.rxjava2.AsyncUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.xmzt.www.config.Constants;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class WriteToSdUtils {
    public static void setAssetsDataToSd(Context context) {
        AsyncUtil.async(new Function<String, String>() {
            @Override
            public String apply(String o) throws Exception {
                try {
                    // 先获取系统默认的文档存放目录
                    File dir = new File(Constants.ASSETS_MAP_PATH);
                    if (!dir.exists()) {
                        dir.mkdirs(); // mkdirs 与 mkdir 区别 mkdirs()可以建立多级文件夹， mkdir()只会建立一级的文件夹 如果找不到上面级的目录 是不会自动创建目录的 会返回false
                    }
                    File file = new File(dir.getAbsoluteFile(), "style.data");
                    if (file.exists()) {
                        return "";
                    }
                    //读取数据文件
                    InputStream open = context.getResources().getAssets().open("map/style.data");
                    file.createNewFile();
                    FileOutputStream fos = new FileOutputStream(file);
                    int len;
                    byte[] buf = new byte[1024];
                    while ((len = open.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    fos.close();

                    File file1 = new File(dir.getAbsoluteFile(), "style_extra.data");
                    if (file1.exists()) {
                        return "";
                    }
                    //读取数据文件
                    InputStream open1 = context.getResources().getAssets().open("map/style_extra.data");
                    file1.createNewFile();
                    FileOutputStream fos1 = new FileOutputStream(file1);
                    int len1;
                    byte[] buf1 = new byte[1024];
                    while ((len1 = open1.read(buf1)) != -1) {
                        fos1.write(buf1, 0, len1);
                    }
                    fos1.flush();
                    fos1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return "";
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String bitmap) throws Exception {
            }
        });
    }

}
