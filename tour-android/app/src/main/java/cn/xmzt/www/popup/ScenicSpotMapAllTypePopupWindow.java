package cn.xmzt.www.popup;

import android.app.Activity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BasePopupWindow;
import cn.xmzt.www.selfdrivingtools.adapter.ScenicSpotMapTypePopListAdapter;
import cn.xmzt.www.selfdrivingtools.bean.ScenicSpotMapTypeListBean;

import java.util.List;

/**
 * @author
 * @date
 * @describe 景区导览——带有地图所有类型的popupwindow
 */
public class ScenicSpotMapAllTypePopupWindow extends BasePopupWindow {
    public ScenicSpotMapAllTypePopupWindow(Activity context,List<ScenicSpotMapTypeListBean> data) {
        this(context, null,data);
    }
    public ScenicSpotMapAllTypePopupWindow(Activity context, AttributeSet attrs,List<ScenicSpotMapTypeListBean> data) {
        super(context, attrs);
        setPopupWindowSize(0, 1);
        RecyclerView rv = view.findViewById(R.id.rv);
        ImageView cancel_iv = view.findViewById(R.id.cancel_iv);
        ScenicSpotMapTypePopListAdapter adapter = new ScenicSpotMapTypePopListAdapter(data,context);
        rv.setLayoutManager(new GridLayoutManager(context, 5));
        rv.setAdapter(adapter);

        cancel_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 如果当前显示就取消显示
                if (isShowing())
                    dismiss();
            }
        });
    }
    @Override
    protected int setPopupWindowLayoutId() {
        return R.layout.popupwindow_scenic_spot_map_all_type;
    }
}
