package cn.xmzt.www.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author tanlei
 * @date 2019/7/30
 * @describe 嵌套在scrollview中时所有的item全部显示
 */

public class MaxListView extends ListView {
    public MaxListView(Context context) {
        super(context);
    }

    public MaxListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
