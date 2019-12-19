package cn.xmzt.www.main.activity;

import android.content.Intent;
import android.os.Bundle;

import cn.xmzt.www.config.Constants;
import cn.xmzt.www.utils.SPUtils;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

/**
 * 欢迎/导航页（app启动Activity）
 * <p/>
 * Created by Averysk
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SPUtils.getBoolean("isPermission", false)) {
            startActivity(new Intent(WelcomeActivity.this, PermissionHintActivity.class));
            finish();
        } else {
            if (!SPUtils.getBoolean("isFirst", false)) {
                // 删除景区地图绘制
                deleteDir(new File(Constants.ASSETS_IMG_PATH));
                // 删除景区音频文件
                deleteDir(new File(Constants.SCENIC_VOICE_FILE_PATH));
                startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
                finish();
            } else {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        }
    }

    /**
     * 删除整个文件夹 或者文件
     *
     * @param file
     */
    public void deleteDir(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
            }

            for (int index = 0; index < childFiles.length; index++) {
                deleteDir(childFiles[index]);
            }
        }
        file.delete();
    }

}
