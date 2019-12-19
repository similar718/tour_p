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

public class CouponRangePopupWindow extends BasePopupWindow {
    private LinearLayout ll_all, ll_line, ll_hotel, ll_ticket;
    private TextView tv_all, tv_line, tv_hotel, tv_ticket;
    private ImageView iv_all, iv_line, iv_hotel, iv_ticket;
    private CouponReceivePopupWindow.OnTypeClickListener onTypeClickListener;

    public CouponReceivePopupWindow.OnTypeClickListener getOnTypeClickListener() {
        return onTypeClickListener;
    }

    public void setOnTypeClickListener(CouponReceivePopupWindow.OnTypeClickListener onTypeClickListener) {
        this.onTypeClickListener = onTypeClickListener;
    }

    public CouponRangePopupWindow(Activity context) {
        this(context, null);
    }

    public CouponRangePopupWindow(Activity context, AttributeSet attrs) {
        super(context, attrs);
        setPopupWindowSize(0, 1);
        setFocusable(true);

        ll_all = view.findViewById(R.id.ll_all);
        ll_line = view.findViewById(R.id.ll_line);
        ll_hotel = view.findViewById(R.id.ll_hotel);
        ll_ticket = view.findViewById(R.id.ll_ticket);
        tv_all = view.findViewById(R.id.tv_all);
        tv_line = view.findViewById(R.id.tv_line);
        tv_hotel = view.findViewById(R.id.tv_hotel);
        tv_ticket = view.findViewById(R.id.tv_ticket);
        iv_all = view.findViewById(R.id.iv_all);
        iv_line = view.findViewById(R.id.iv_line);
        iv_hotel = view.findViewById(R.id.iv_hotel);
        iv_ticket = view.findViewById(R.id.iv_ticket);
        ll_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getOnTypeClickListener() != null) {
                    onTypeClickListener.onTypeClick(0);
                    dismiss();
                }
            }
        });
        ll_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTypeClickListener.onTypeClick(1);
                dismiss();
            }
        });
        ll_hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getOnTypeClickListener() != null) {
                    onTypeClickListener.onTypeClick(2);
                    dismiss();
                }
            }
        });
        ll_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTypeClickListener.onTypeClick(3);
                dismiss();
            }
        });
    }

    public void setType(String type) {
        tv_all.setTextColor(Color.parseColor("#666666"));
        iv_all.setVisibility(View.GONE);
        tv_line.setTextColor(Color.parseColor("#666666"));
        iv_line.setVisibility(View.GONE);
        tv_hotel.setTextColor(Color.parseColor("#666666"));
        iv_hotel.setVisibility(View.GONE);
        tv_ticket.setTextColor(Color.parseColor("#666666"));
        iv_ticket.setVisibility(View.GONE);
        switch (type) {
            case "1":
                tv_line.setTextColor(Color.parseColor("#24A4FF"));
                iv_line.setVisibility(View.VISIBLE);
                break;
            case "2":
                tv_hotel.setTextColor(Color.parseColor("#24A4FF"));
                iv_hotel.setVisibility(View.VISIBLE);
                break;
            case "3":
                tv_ticket.setTextColor(Color.parseColor("#24A4FF"));
                iv_ticket.setVisibility(View.VISIBLE);
                break;
            default:
                tv_all.setTextColor(Color.parseColor("#24A4FF"));
                iv_all.setVisibility(View.VISIBLE);
                break;
        }
    }

    public interface OnTypeClickListener {
        void onTypeClick(int position);
    }

    @Override
    protected int setPopupWindowLayoutId() {
        return R.layout.popupwindow_coupon_range;
    }
}
