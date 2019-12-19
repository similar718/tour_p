package cn.xmzt.www.popup;

import android.app.Activity;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BasePopupWindow;
import cn.xmzt.www.hotel.GlideImageLoader;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tanlei
 * @date 2019/7/30
 * @describe 房型详情的弹出框
 */

public class HotelRoomDetailsPopupWindow extends BasePopupWindow {
    private TextView tvCoupon;
    private ImageView ivCoupon;
    //优惠券可用标志
    public static final int HOTEL_ROOM_DETAILS_SURE_COUPON_TAG = 1;
    //优惠券不可用标志
    public static final int HOTEL_ROOM_DETAILS_NOT_COUPON_TAG = 0;

    //房间可预定
    public static final int HOTEL_ROOM_DETAILS_RESERVE_TYPE = 1;
    //房间不可预定
    public static final int HOTEL_ROOM_DETAILS_FIXED_TYPE = 0;

    public HotelRoomDetailsPopupWindow(Activity context, int type) {
        this(context, null, type);
    }

    public HotelRoomDetailsPopupWindow(Activity context, AttributeSet attrs, int type) {
        super(context, attrs);
        setPopupWindowSize(0.83, 1);
        Banner banner = view.findViewById(R.id.banner);
        banner.setImageLoader(new GlideImageLoader());
        List<String> images = new ArrayList<>();
        images.add("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_" +
                "4000&sec=1564216863&di=2e009b334c4135bdf023104d6deba987&src=http://dingyue.nosdn.127.net/8jZqtz1mW8Cp" +
                "GNDQthJNta6IK3RL90zRKygBREoV8NSEO1541474222791compressflag.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1564226996937&di=c36dc03e31ba0f7697b04c6e6f0c2881&" +
                "imgtype=jpg&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D2278011335%2C2910610009%26fm%3D214%26gp%3D0.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1564226956669&di=abcbf25e311be30315f674d" +
                "b6a1567a3&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20180125%2Fc35e9967ad5c4375893db0abf683e4e3.jpeg");
        banner.setImages(images);
        banner.start();
        ImageView ivDetermine = view.findViewById(R.id.iv_determine);
        tvCoupon = view.findViewById(R.id.tv_coupon);
        ivCoupon = view.findViewById(R.id.iv_coupon);

        if (type == 0) {//代表为不可预订的状态下的弹出框
            ivDetermine.setImageResource(R.drawable.hotel_room_details_fixed);
        } else {//代表为可预定的状态下
            ivDetermine.setImageResource(R.drawable.hotel_room_details_reserve);
        }
    }

    public void setPopupWindow(int tag) {
        if (tag == 1) {//优惠券可用
            ivCoupon.setImageResource(R.drawable.hotel_room_details_sure_coupon);
            tvCoupon.setText("此商品可使用优惠券");
        } else {//优惠券不可用
            ivCoupon.setImageResource(R.drawable.hotel_room_details_not_coupon);
            tvCoupon.setText("此商品不支持使用优惠券");
        }
    }

    @Override
    protected int setPopupWindowLayoutId() {
        return R.layout.popupwindow_hotel_room_details;
    }
}
