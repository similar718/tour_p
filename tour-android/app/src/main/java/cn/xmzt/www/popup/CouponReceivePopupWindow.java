package cn.xmzt.www.popup;

import android.app.Activity;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BasePopupWindow;


public class CouponReceivePopupWindow extends BasePopupWindow {
    private LinearLayout llReceive, llExpire;
    private TextView tvReceive, tvExpire;
    private ImageView ivReceive, ivExpire;
    private OnTypeClickListener onTypeClickListener;

    public OnTypeClickListener getOnTypeClickListener() {
        return onTypeClickListener;
    }

    public void setOnTypeClickListener(OnTypeClickListener onTypeClickListener) {
        this.onTypeClickListener = onTypeClickListener;
    }

    public CouponReceivePopupWindow(Activity context) {
        this(context, null);
    }

    public CouponReceivePopupWindow(Activity context, AttributeSet attrs) {
        super(context, attrs);
        setPopupWindowSize(0, 1);
        setFocusable(true);
        llReceive = view.findViewById(R.id.ll_receive);
        llExpire = view.findViewById(R.id.ll_expire);
        tvReceive = view.findViewById(R.id.tv_receive);
        tvExpire = view.findViewById(R.id.tv_expire);
        ivReceive = view.findViewById(R.id.iv_receive);
        ivExpire = view.findViewById(R.id.iv_expire);
        llReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getOnTypeClickListener() != null) {
                    onTypeClickListener.onTypeClick(0);
                    dismiss();
                }
            }
        });
        llExpire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTypeClickListener.onTypeClick(1);
                dismiss();
            }
        });
    }

    public void setType(int type) {
        if (type == 0) {
            tvReceive.setTextColor(Color.parseColor("#24A4FF"));
            tvExpire.setTextColor(Color.parseColor("#666666"));
            ivExpire.setVisibility(View.GONE);
            ivReceive.setVisibility(View.VISIBLE);
        } else {
            tvReceive.setTextColor(Color.parseColor("#666666"));
            tvExpire.setTextColor(Color.parseColor("#24A4FF"));
            ivExpire.setVisibility(View.VISIBLE);
            ivReceive.setVisibility(View.GONE);
        }
    }

    public interface OnTypeClickListener {
        void onTypeClick(int position);
    }

    @Override
    protected int setPopupWindowLayoutId() {
        return R.layout.popupwindow_coupon_receive;
    }
}
