package cn.xmzt.www.popup;

import android.app.Activity;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BasePopupWindow;
import cn.xmzt.www.intercom.adapter.TalkGroupListAdapter;
import cn.xmzt.www.intercom.bean.MyTalkGroupBean;
import cn.xmzt.www.intercom.cache.TalkGroupListCache;
import cn.xmzt.www.nim.uikit.common.util.sys.ScreenUtil;

import net.jg.framework.view.RecycleViewDivider;

import java.util.List;

/**
 * 对讲按钮上面的群聊列表
 * @author Averysk
 */
public class IntercomGroupListPopupWindow extends BasePopupWindow {

    private Activity activity;
    private RecyclerView rv_intercom_group; // 记录时间

    @Override
    protected int setPopupWindowLayoutId() {
        return R.layout.popupwindow_intercom_group_list;
    }

    public IntercomGroupListPopupWindow(Activity context) {
        this(context, null);
    }

    public IntercomGroupListPopupWindow(Activity context, AttributeSet attrs) {
        super(context, attrs);
        this.activity = context;
        setPopupWindowSize(0, 0);
        initView();
    }

    // 初始化
    private void initView() {
        rv_intercom_group = view.findViewById(R.id.rv_intercom_group);
        initRecyclerViewRoute(rv_intercom_group);
    }

    // 显示窗口
    public void showPop(View anchor){
        this.showAsDropDown(anchor, 0, -ScreenUtil.dip2px(230+100) , Gravity.CENTER_HORIZONTAL);
    }

    // 隐藏窗口
    public void holePop(){
        this.dismiss();
    }

    /**
     * 初始推荐路线RecyclerView
     * @param recyclerView
     */
    public void initRecyclerViewRoute(RecyclerView recyclerView) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 5);
        recyclerView.setLayoutManager(gridLayoutManager);
        //禁止滑动
        recyclerView.setNestedScrollingEnabled(false);
        //设置分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(activity, LinearLayoutManager.HORIZONTAL, 20, activity.getResources().getColor(R.color.white)));
        //设置数据
        List<MyTalkGroupBean> datas = TalkGroupListCache.getInstance().getTalkGroupList();
        //设置适配器
        TalkGroupListAdapter mAdapterBanner = new TalkGroupListAdapter(activity, datas);
        recyclerView.setAdapter(mAdapterBanner);
        mAdapterBanner.setOnItemClickListener(new TalkGroupListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, RecyclerView.ViewHolder vh) {
                if (mClickListener != null){
                    MyTalkGroupBean groupInfoBean = datas.get(position);
                    mClickListener.onItemClick(groupInfoBean);
                }
            }
        });
    }

    private OnItemClickListener mClickListener;

    public interface OnItemClickListener {
        void onItemClick(MyTalkGroupBean groupInfoBean);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

}
