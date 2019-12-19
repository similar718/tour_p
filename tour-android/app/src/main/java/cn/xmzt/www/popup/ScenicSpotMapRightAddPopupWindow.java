package cn.xmzt.www.popup;

import android.app.Activity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BasePopupWindow;

/**
 * @describe  景区导览——带有地图的顶部右边popupwindow
 */

public class ScenicSpotMapRightAddPopupWindow extends BasePopupWindow {
    public ScenicSpotMapRightAddPopupWindow(Activity context) {
        this(context, null);
    }

    public ScenicSpotMapRightAddPopupWindow(Activity context, AttributeSet attrs) {
        super(context, attrs);
        setPopupWindowSize(0, 0);
        TextView mImagesTv = view.findViewById(R.id.pop_scenic_spot_map_right_add_images_tv); // 图集
        TextView mShareTv = view.findViewById(R.id.pop_scenic_spot_map_right_add_share_tv); // 分享
        TextView mFeedBackTv = view.findViewById(R.id.pop_scenic_spot_map_right_add_feedback_tv); // 反馈

        mImagesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapRightAddPopupWindowOnClick.imagesClick();
            }
        });
        mShareTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapRightAddPopupWindowOnClick.shareClick();
            }
        });
        mFeedBackTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapRightAddPopupWindowOnClick.feedBackClick();
            }
        });
    }

    private ScenicSpotMapRightAddPopupWindowOnClick mapRightAddPopupWindowOnClick = null;

    public void setMapRightAddPopupWindowOnClick(ScenicSpotMapRightAddPopupWindowOnClick itemOnClick){
        mapRightAddPopupWindowOnClick = itemOnClick;
    }

    public interface ScenicSpotMapRightAddPopupWindowOnClick{
        void imagesClick(); // 图集
        void shareClick(); // 分享
        void feedBackClick(); // 反馈
    }

    @Override
    protected int setPopupWindowLayoutId() {
        return R.layout.popupwindow_scenic_spot_map_right_add;
    }
}
