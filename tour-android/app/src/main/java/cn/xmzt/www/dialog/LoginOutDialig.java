package cn.xmzt.www.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import cn.xmzt.www.R;

/**
 * 加载dialog
 */
public class LoginOutDialig extends AlertDialog{
    private Context mContext;

    private View.OnClickListener onClickListener;

    public LoginOutDialig(Context context) {
        super(context);
        mContext = context;
    }

    public LoginOutDialig(Context context,View.OnClickListener onClickListener) {
        super(context);
        mContext = context;
        this.onClickListener = onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT ;
        params.gravity = Gravity.CENTER; // 设置重力
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
        setCanceledOnTouchOutside(false);
        setCancelable(true);

        TextView confirm_tv = findViewById(R.id.confirm_tv);
        TextView tips_tv = findViewById(R.id.tips_tv);
        TextView cancel_tv = findViewById(R.id.cancel_tv);

        confirm_tv.setText("去登录");
        tips_tv.setText("您的账号在其他设备登录");

        confirm_tv.setOnClickListener(onClickListener);
        cancel_tv.setOnClickListener(onClickListener);
    }
}
