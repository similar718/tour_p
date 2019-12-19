package cn.xmzt.www.popup;

import android.app.Activity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BasePopupWindow;
import cn.xmzt.www.route.adapter.SortPopupAdapter;
import cn.xmzt.www.view.listener.ItemListener;

import java.util.List;

/**
 * @describe 排序的弹出框
 */
public class SortPopupWindow extends BasePopupWindow {
    private SortPopupAdapter adapter;
    private View fillView;
    private ItemListener itemListener;
    private View.OnClickListener onClickListener;

    public SortPopupWindow(Activity context) {
        this(context, null);
    }

    public SortPopupWindow(Activity context, AttributeSet attrs) {
        super(context, attrs);
        setPopupWindowSize(0, 1);
        setMyOutsideTouchable(false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        GridLayoutManager manager= (GridLayoutManager) recyclerView.getLayoutManager();
        manager.setSpanCount(1);
        adapter = new SortPopupAdapter();
        recyclerView.setAdapter(adapter);
        fillView=view.findViewById(R.id.fillView);
        if(this.onClickListener!=null){
            fillView.setOnClickListener(this.onClickListener);
        }

    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        if(fillView!=null){
            fillView.setOnClickListener(this.onClickListener);
        }
    }

    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        super.setOnDismissListener(onDismissListener);
    }

    public void setViewData(List<String> list){
        adapter.setDatas(list);
    }
    /**
     * 设置默认选中那个
     *
     * @param position
     */
    public void setSelectPosition(int position) {
        adapter.setSelectPosition(position);
    }
    public String getItem(int position) {
        return adapter.getItem(position);
    }
    @Override
    protected int setPopupWindowLayoutId() {
        return R.layout.popupwindow_sort;
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
        adapter.setItemListener(itemListener);
    }
}
