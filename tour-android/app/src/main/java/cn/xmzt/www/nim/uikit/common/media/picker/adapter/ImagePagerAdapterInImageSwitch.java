package cn.xmzt.www.nim.uikit.common.media.picker.adapter;

import android.content.Context;
import android.os.Build;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.nim.uikit.common.activity.UI;
import cn.xmzt.www.nim.uikit.common.media.picker.activity.PreviewImageFromLocalActivity;
import cn.xmzt.www.nim.uikit.common.ui.imageview.BaseZoomableImageView;
import cn.xmzt.www.nim.uikit.common.util.sys.ScreenUtil;

import java.util.List;

public class ImagePagerAdapterInImageSwitch extends PagerAdapter {
    private Context mContext;
    private List<String> mList;
    private LayoutInflater mInflater;
    private int viewPagerWidth, viewPagerHeight;
    private UI mActivity;

    public ImagePagerAdapterInImageSwitch(Context cx, List<String> list, LayoutInflater inflater, int width, int height, UI activity) {
        mContext = cx;
        mList = list;
        mInflater = inflater;
        viewPagerHeight = height;
        viewPagerWidth = width;
        mActivity = activity;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View layout = (View) object;
        BaseZoomableImageView iv = (BaseZoomableImageView) layout.findViewById(R.id.imageView);
        iv.clear();
        container.removeView(layout);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        ImageView iv = new ImageView(mContext);
        View layout = null;

//		if( Build.VERSION.SDK_INT >=  17 )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            layout = mInflater.inflate(R.layout.nim_preview_image_layout_multi_touch, null);
        } else {
            layout = mInflater.inflate(R.layout.nim_preview_image_layout_zoom_control, null);
        }

        container.addView(layout);
        layout.setTag(position);
        viewPagerWidth = ScreenUtil.screenWidth;
        viewPagerHeight = ScreenUtil.screenHeight;

        return layout;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void setPrimaryItem(View container, int position, Object object) {
        ((PreviewImageFromLocalActivity) mActivity).updateCurrentImageView(position);
    }
}
