package cn.xmzt.www.nim.uikit.common.media.imagepicker.ui;

import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.view.Window;

import cn.xmzt.www.nim.uikit.common.activity.UI;
import cn.xmzt.www.utils.ToastUtils;

public abstract class ImageBaseActivity extends UI {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public boolean checkPermission(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void showToast(String toastText) {
        ToastUtils.showText(getApplicationContext(), toastText);
    }

    @Override
    protected void onPause() {
        super.onPause();
        clearRequest();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearMemoryCache();
    }

    public abstract void clearRequest();

    public abstract void clearMemoryCache();

    protected void asFullscreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}
