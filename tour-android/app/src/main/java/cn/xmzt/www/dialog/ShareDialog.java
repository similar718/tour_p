package cn.xmzt.www.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import cn.xmzt.www.R;

import androidx.annotation.NonNull;

/**
 * @author tanlei
 * @date 2019/9/24
 * @describe
 */
public class ShareDialog extends Dialog {
    private ImageView iv1,iv2,iv3,iv4;
    private TextView tv;
    private OnShareClickListener listener;

    public OnShareClickListener getListener() {
        return listener;
    }

    public void setListener(OnShareClickListener listener) {
        this.listener = listener;
    }

    public ShareDialog(@NonNull Context context) {
        this(context,0);
    }

    public ShareDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.dialog_custom);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_share_platform,null);
        tv = view.findViewById(R.id.tv);
        iv1 = view.findViewById(R.id.iv1);
        iv2 = view.findViewById(R.id.iv2);
        iv3 = view.findViewById(R.id.iv3);
        iv4 = view.findViewById(R.id.iv4);
        setContentView(view);
        this.getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        this.getWindow().setAttributes(params);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getListener()!=null){
                    listener.onShareClick(0);
                }
            }
        });
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getListener()!=null){
                    listener.onShareClick(1);
                }
            }
        });
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getListener()!=null){
                    listener.onShareClick(2);
                }
            }
        });
        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getListener()!=null){
                    listener.onShareClick(3);
                }
            }
        });
    }

    public interface OnShareClickListener{
        void onShareClick(int position);
    }
}
