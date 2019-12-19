package cn.xmzt.www.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.xmzt.www.R;

import androidx.annotation.Nullable;

/**
 * @author tanlei
 * @date 2019/10/9
 * @describe
 */
public class KeyNumberLinearLayout extends LinearLayout implements View.OnClickListener {
    private TextView tv_1_1, tv_1_2, tv_1_3, tv_1_4, tv_1_5, tv_1_6, tv_1_7, tv_1_8, tv_1_9, tv_1_10;
    private TextView tv_2_1, tv_2_2, tv_2_3, tv_2_4, tv_2_5, tv_2_6, tv_2_7, tv_2_8, tv_2_9;
    private TextView tv_3_1, tv_3_2, tv_3_3, tv_3_4, tv_3_5, tv_3_6, tv_3_7, tv_3_8;
    private TextView tv_4_1, tv_4_2, tv_4_3, tv_4_4, tv_4_5;
    private RelativeLayout rl_delete;
    private OnKeyNumberListeners onKeyListeners;

    public OnKeyNumberListeners getOnKeyListeners() {
        return onKeyListeners;
    }

    public void setOnKeyListeners(OnKeyNumberListeners onKeyListeners) {
        this.onKeyListeners = onKeyListeners;
    }

    public KeyNumberLinearLayout(Context context) {
        this(context, null);
    }

    public KeyNumberLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.key_number_linearlayout, this, true);
        view.findViewById(R.id.tv_1_1).setOnClickListener(this);
        view.findViewById(R.id.tv_1_2).setOnClickListener(this);
        view.findViewById(R.id.tv_1_3).setOnClickListener(this);
        view.findViewById(R.id.tv_1_4).setOnClickListener(this);
        view.findViewById(R.id.tv_1_5).setOnClickListener(this);
        view.findViewById(R.id.tv_1_6).setOnClickListener(this);
        view.findViewById(R.id.tv_1_7).setOnClickListener(this);
        view.findViewById(R.id.tv_1_8).setOnClickListener(this);
        view.findViewById(R.id.tv_1_9).setOnClickListener(this);
        view.findViewById(R.id.tv_1_10).setOnClickListener(this);
        view.findViewById(R.id.tv_2_1).setOnClickListener(this);
        view.findViewById(R.id.tv_2_2).setOnClickListener(this);
        view.findViewById(R.id.tv_2_3).setOnClickListener(this);
        view.findViewById(R.id.tv_2_4).setOnClickListener(this);
        view.findViewById(R.id.tv_2_5).setOnClickListener(this);
        view.findViewById(R.id.tv_2_6).setOnClickListener(this);
        view.findViewById(R.id.tv_2_7).setOnClickListener(this);
        view.findViewById(R.id.tv_2_8).setOnClickListener(this);
        view.findViewById(R.id.tv_2_9).setOnClickListener(this);
        view.findViewById(R.id.tv_2_10).setOnClickListener(this);
        view.findViewById(R.id.tv_3_1).setOnClickListener(this);
        view.findViewById(R.id.tv_3_2).setOnClickListener(this);
        view.findViewById(R.id.tv_3_3).setOnClickListener(this);
        view.findViewById(R.id.tv_3_4).setOnClickListener(this);
        view.findViewById(R.id.tv_3_5).setOnClickListener(this);
        view.findViewById(R.id.tv_3_6).setOnClickListener(this);
        view.findViewById(R.id.tv_3_7).setOnClickListener(this);
        view.findViewById(R.id.tv_3_8).setOnClickListener(this);
        view.findViewById(R.id.tv_3_9).setOnClickListener(this);
        view.findViewById(R.id.tv_3_10).setOnClickListener(this);
        view.findViewById(R.id.tv_4_1).setOnClickListener(this);
        view.findViewById(R.id.tv_4_2).setOnClickListener(this);
        view.findViewById(R.id.tv_4_3).setOnClickListener(this);
        view.findViewById(R.id.tv_4_4).setOnClickListener(this);
        view.findViewById(R.id.tv_4_5).setOnClickListener(this);
        view.findViewById(R.id.tv_4_6).setOnClickListener(this);
        view.findViewById(R.id.tv_4_7).setOnClickListener(this);
        view.findViewById(R.id.tv_4_8).setOnClickListener(this);
        view.findViewById(R.id.rl_delete).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_delete) {
            onKeyListeners.onKeyNumberListener("");
        } else {
            if (getOnKeyListeners() != null) {
                onKeyListeners.onKeyNumberListener(((TextView) v).getText().toString());
            }
        }
    }

    public interface OnKeyNumberListeners {
        void onKeyNumberListener(String str);
    }
}
