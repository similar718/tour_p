package cn.xmzt.www.popup;

import android.app.Activity;
import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BasePopupWindow;
import cn.xmzt.www.mine.adapter.MessageFilterAdapter;
import cn.xmzt.www.mine.bean.HorseMiMessageFilterBean;

/**
 * @describe 日期天数、出发地/目的地、线路主题 筛选 PopupWindow
 */
public class MessageRemindFilterPopupWindow extends BasePopupWindow {
    private MessageFilterAdapter adapter;
    private Activity activity;
    private View.OnClickListener listener;

    public MessageRemindFilterPopupWindow(Activity context) {
        this(context, null);
    }

    public MessageRemindFilterPopupWindow(Activity activity, AttributeSet attrs) {
        super(activity, attrs);
        this.activity=activity;
        setPopupWindowSize(0, 1);
        setMyOutsideTouchable(false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        GridLayoutManager manager= (GridLayoutManager) recyclerView.getLayoutManager();
        manager.setSpanCount(4);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                boolean isSingleShow = adapter.isSingleShow(position);
                return isSingleShow ? manager.getSpanCount() : 1;
            }
        });
        adapter = new MessageFilterAdapter();
        recyclerView.setAdapter(adapter);
    }
    /**
     * 设置弹出框的偏移量
     * @param y y偏移量
     */
    public void setYLocation(int y) {
        int h=getHeight();
        this.setHeight(h-y);
    }
    public void setViewData(List<HorseMiMessageFilterBean> list){
        adapter.setDatas(list);
    }

    /**
     * 获取选中的日期
     * @return
     */
    public HorseMiMessageFilterBean getSelectPositionDate() {
        return adapter.getItem(adapter.selectPositionDate);
    }

    /**
     * 获取选中的类型
     * @return
     */
    public HorseMiMessageFilterBean getSelectPositionType() {
        return adapter.getItem(adapter.selectPositionType);
    }
    /**
     * 重置
     */
    public void retry(){
        adapter.selectPositionType=0;
        adapter.selectPositionDate=0;
        adapter.notifyDataSetChanged();
    }

    @Override
    protected int setPopupWindowLayoutId() {
        return R.layout.popupwindow_filter_message_remind;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
        view.findViewById(R.id.tv_retry).setOnClickListener(listener);
        view.findViewById(R.id.tv_confirm).setOnClickListener(listener);
        view.findViewById(R.id.fillView).setOnClickListener(listener);
    }

}
