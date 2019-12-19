package cn.xmzt.www.popup;

import android.app.Activity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BasePopupWindow;
import cn.xmzt.www.route.adapter.FilterAdapter;
import cn.xmzt.www.route.bean.FilterBean;
import cn.xmzt.www.route.bean.FilterThemeBean;

import java.util.List;

/**
 * @describe 日期天数、出发地/目的地、线路主题 筛选 PopupWindow
 */
public class FilterPopupWindow extends BasePopupWindow {
    private FilterAdapter adapter;
    private Activity activity;
    private View.OnClickListener listener;

    public FilterPopupWindow(Activity context) {
        this(context, null);
    }

    public FilterPopupWindow(Activity activity, AttributeSet attrs) {
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
        adapter = new FilterAdapter();
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
    public void setViewData(List<FilterBean> list){
        adapter.setDatas(list);
    }

    /**
     * 获取选中的日期
     * @return
     */
    public FilterBean getSelectPositionDate() {
        return adapter.getItem(adapter.selectPositionDate);
    }

    /**
     * 获取选中的日期
     * @return
     */
    public FilterBean getSelectPositionDays() {
        return adapter.getItem(adapter.selectPositionDays);
    }
    /**
     * 获取选中的出发地
     * @return
     */
    public FilterBean getSelectPositionDepart() {
        return adapter.getItem(adapter.selectPositionDepart);
    }
    /**
     * 获取选中的目的地
     * @return
     */
    public FilterBean getSelectPositionArrival() {
        return adapter.getItem(adapter.selectPositionArrival);
    }
    /**
     * 获取选中的主题
     * @return
     */
    public String getSelectPositionTheme() {
        if(adapter.selectThemeList.size()>0){
            StringBuilder stringBuilder=new StringBuilder();
            for (int i=0;i<adapter.selectThemeList.size();i++){
                FilterThemeBean mFilterThemeBean=adapter.selectThemeList.get(i);
                if(stringBuilder.length()>0){
                    stringBuilder.append(",");
                    stringBuilder.append(mFilterThemeBean.getCode());
                }else {
                    stringBuilder.append(mFilterThemeBean.getCode());
                }
            }
            return stringBuilder.toString();
        }
        return null;
    }


    @Override
    protected int setPopupWindowLayoutId() {
        return R.layout.popupwindow_filter;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
        view.findViewById(R.id.tv_retry).setOnClickListener(listener);
        view.findViewById(R.id.tv_confirm).setOnClickListener(listener);
        view.findViewById(R.id.fillView).setOnClickListener(listener);
    }
    /**
     * 重置日期天数
     */
    public void retryDateDays(){
        adapter.selectPositionDate=0;
        adapter.selectPositionDays=0;
        adapter.notifyDataSetChanged();
    }

    /**
     * 重置出发地目的地
     */
    public void retryDepartArrival(){
        adapter.selectPositionDepart=0;
        adapter.selectPositionArrival=0;
        adapter.notifyDataSetChanged();
    }
    /**
     * 重置主题
     */
    public void retryTheme(){
        adapter.selectThemeList.clear();
        adapter.notifyDataSetChanged();
    }
}
