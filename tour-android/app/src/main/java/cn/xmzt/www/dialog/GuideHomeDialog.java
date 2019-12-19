package cn.xmzt.www.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import cn.xmzt.www.R;

import androidx.annotation.NonNull;

/**
 * @author tanlei
 * @date 2019/9/28
 * @describe
 */
public class GuideHomeDialog extends Dialog {
    private ImageView iv1, iv2, iv3, iv4, iv5, iv6;

    public GuideHomeDialog(@NonNull Activity context) {
        this(context, 0);
    }

    public GuideHomeDialog(@NonNull Activity context, int themeResId) {
        super(context, R.style.dialog_custom1);

        Window window = this.getWindow();
        // 设置dialog 显示的位置 默认center 显示再屏幕中间  现设置显示再顶部
        window.setGravity(Gravity.TOP);
        // 设置dialog 尺寸 和偏移
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        // 前2 个flag设置dialog 显示到状态栏    第三个设置点击dialog以外的蒙层 不抢夺焦点  响应点击事件
        lp.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
                WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lp.dimAmount = 0.5f;
        window.setAttributes(lp);
        // 设置点击蒙层不消失
        setCanceledOnTouchOutside(false);


        View view = LayoutInflater.from(context).inflate(R.layout.dialog_guide_home1, null);
        iv1 = view.findViewById(R.id.iv_1);
        iv2 = view.findViewById(R.id.iv_2);
        iv3 = view.findViewById(R.id.iv_3);
        iv4 = view.findViewById(R.id.iv_4);
        iv5 = view.findViewById(R.id.iv_5);
        iv6 = view.findViewById(R.id.iv_6);
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv1.setVisibility(View.GONE);
                iv2.setVisibility(View.GONE);
                iv3.setVisibility(View.VISIBLE);
                iv4.setVisibility(View.VISIBLE);
                iv5.setVisibility(View.GONE);
                iv6.setVisibility(View.GONE);
            }
        });

        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv1.setVisibility(View.GONE);
                iv2.setVisibility(View.GONE);
                iv3.setVisibility(View.GONE);
                iv4.setVisibility(View.GONE);
                iv5.setVisibility(View.VISIBLE);
                iv6.setVisibility(View.VISIBLE);
            }
        });
        iv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        setContentView(view);
    }
}
