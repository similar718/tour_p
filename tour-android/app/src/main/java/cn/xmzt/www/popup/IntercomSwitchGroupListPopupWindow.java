package cn.xmzt.www.popup;

import android.app.Activity;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BasePopupWindow;
import cn.xmzt.www.intercom.bean.MyTalkGroupBean;
import cn.xmzt.www.intercom.cache.TalkGroupListCache;
import cn.xmzt.www.main.globals.AnyRtcMaxManage;
import cn.xmzt.www.main.globals.FloatIntercomManage;
import cn.xmzt.www.main.globals.MsgExtensionType;
import cn.xmzt.www.selfdrivingtools.event.UpdateGroupDefaultEvent;
import cn.xmzt.www.utils.StatusBarUtil;
import cn.xmzt.www.view.SwitchGroupList;
import cn.xmzt.www.view.listener.ItemListener;

import org.greenrobot.eventbus.EventBus;
import org.webrtc.Logging;

import java.util.List;

/**
 * 对讲按钮上面的切换群聊列表
 */
public class IntercomSwitchGroupListPopupWindow extends BasePopupWindow implements ItemListener {

    private static final String TAG = "IntercomSwitchGroupListPopupWindow";
    private Activity activity;
    private SwitchGroupList switchGroupList; //切换的群列表view
    private ImageView iv_bubble; //显示气泡位置的气泡

    @Override
    protected int setPopupWindowLayoutId() {
        return R.layout.popupwindow_intercom_switch_group_list;
    }

    public IntercomSwitchGroupListPopupWindow(Activity context) {
        this(context, null);
    }

    public IntercomSwitchGroupListPopupWindow(Activity context, AttributeSet attrs) {
        super(context, attrs);
        this.activity = context;
        setPopupWindowSize(0, 0);
        initView();
    }

    // 初始化
    private void initView() {
        switchGroupList = view.findViewById(R.id.switchGroupList);
        iv_bubble = view.findViewById(R.id.iv_bubble);
        switchGroupList.setItemListener(this);
    }
    private int widthPixels=0;
    private int statusBarHeight;

    /**
     * 显示窗口
     * @param parent
     * @param currentGroupId 当前群id 做列表选中用
     */
    public void showPop(View parent,String currentGroupId){
        List<MyTalkGroupBean>  talkGroupList=TalkGroupListCache.getInstance().getTalkGroupList();
        if(talkGroupList!=null&&talkGroupList.size()==0){
            return;
        }
        if(switchGroupList!=null){
            if(talkGroupList.size()>8){
                ViewGroup.LayoutParams listLayoutParams =switchGroupList.getLayoutParams();
                listLayoutParams.height=parent.getResources().getDimensionPixelOffset(R.dimen.dp_200);
            }else if(talkGroupList.size()>4){
                ViewGroup.LayoutParams listLayoutParams =switchGroupList.getLayoutParams();
                listLayoutParams.height=parent.getResources().getDimensionPixelOffset(R.dimen.dp_180);
            }
            switchGroupList.setDatas(talkGroupList,currentGroupId);
        }
        statusBarHeight= StatusBarUtil.getStatusBarHeight(parent.getContext());
//        this.showAsDropDown(parent, 0, -ScreenUtil.dip2px(230+100) , Gravity.CENTER_HORIZONTAL);
        int parentWidth=parent.getMeasuredWidth();
        if(widthPixels==0){
            widthPixels= parent.getResources().getDisplayMetrics().widthPixels;
        }
        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        int dp16=parent.getResources().getDimensionPixelOffset(R.dimen.dp_16);
        if(location[0]< dp16){
            parent.setX(dp16);
        }else if(location[0]+parentWidth> widthPixels-dp16){
            parent.setX(widthPixels-dp16-parentWidth);
        }
        if(location[1]<getMeasureHeight()+statusBarHeight){
            parent.setY(getMeasureHeight()+statusBarHeight);
        }
        parent.getLocationOnScreen(location);
        FrameLayout.LayoutParams mLayoutParams= (FrameLayout.LayoutParams) iv_bubble.getLayoutParams();
        int centerX=location[0]+parentWidth/2;
        int offset=0;
        if(talkGroupList.size()<2){
            if(centerX>widthPixels/2){
                offset=widthPixels-location[0]-parentWidth;
                mLayoutParams.gravity= Gravity.RIGHT|Gravity.BOTTOM;
                mLayoutParams.leftMargin=0;
                if(offset>0){
                    mLayoutParams.rightMargin=widthPixels-centerX+mLayoutParams.width/2-dp16-offset;
                }else {
                    mLayoutParams.rightMargin=widthPixels-centerX+mLayoutParams.width/2-dp16;
                }
                showAtLocation(parent, Gravity.TOP|Gravity.RIGHT, offset, location[1] - getMeasureHeight());
            }else {
                offset=location[0];
                mLayoutParams.gravity= Gravity.LEFT|Gravity.BOTTOM;
                if(offset>0){
                    mLayoutParams.leftMargin=centerX-mLayoutParams.width/2-dp16-offset;
                }else {
                    mLayoutParams.leftMargin=centerX-mLayoutParams.width/2-dp16;
                }
                mLayoutParams.rightMargin=0;
                showAtLocation(parent, Gravity.TOP|Gravity.LEFT, offset, location[1] - getMeasureHeight());
            }
        }else {
            int dp80=parent.getResources().getDimensionPixelOffset(R.dimen.dp_80);//群列表的item的宽
            int dp20=parent.getResources().getDimensionPixelOffset(R.dimen.dp_20);//群列表的左右间距的总大小
            int popupW=dp80*talkGroupList.size()+dp20;//当前PopupWindow的宽
            int popupMarginRL=widthPixels-popupW;//当前PopupWindow的左右间距的总大小
            if(centerX>widthPixels/2){//右边显示
                int parentMarginRight=widthPixels-location[0]-parentWidth;//parent的右边距
                if(parentMarginRight>popupMarginRL/2){
                    offset=widthPixels-popupW-parentWidth;
                }else {
                    offset=parentMarginRight-parentWidth/2;
                }
                if(offset<dp16){
                    offset=dp16;
                }
                mLayoutParams.gravity= Gravity.RIGHT|Gravity.BOTTOM;
                mLayoutParams.leftMargin=0;
                mLayoutParams.rightMargin=widthPixels-centerX+mLayoutParams.width/2-dp16-offset;
                showAtLocation(parent, Gravity.TOP|Gravity.RIGHT, offset, location[1] - getMeasureHeight());
            }else if(centerX<widthPixels/2){//左边显示
                int parentMarginLeft=location[0];//parent的左边距
                if(parentMarginLeft>popupMarginRL/2){
                    offset=widthPixels-popupW-parentWidth;
                }else {
                    offset=parentMarginLeft-parentWidth/2;
                }
                if(offset<dp16){
                    offset=dp16;
                }
                mLayoutParams.gravity= Gravity.LEFT|Gravity.BOTTOM;
                mLayoutParams.leftMargin=centerX-mLayoutParams.width/2-dp16-offset;
                mLayoutParams.rightMargin=0;
                showAtLocation(parent, Gravity.TOP|Gravity.LEFT, offset, location[1] - getMeasureHeight());
            }else {//中间显示
                mLayoutParams.gravity= Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM;
                mLayoutParams.leftMargin=0;
                mLayoutParams.rightMargin=0;
                showAtLocation(parent, Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, location[1] - getMeasureHeight());
            }
        }

    }
    // 隐藏窗口
    public void holePop(){
        this.dismiss();
    }
    /**
     * 测量高度
     * @return
     */
    public int getMeasureHeight() {
        getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popHeight = getContentView().getMeasuredHeight();
        return popHeight;
    }

    /**
     * 测量宽度
     * @return
     */
    public int getMeasuredWidth() {
        getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popWidth = getContentView().getMeasuredWidth();
        return popWidth;
    }
    @Override
    public void onItemClick(View view, int position) {
        if(switchGroupList!=null){
            MyTalkGroupBean talkGroupInfoBean=switchGroupList.getItem(position);
            if(!talkGroupInfoBean.getGroupId().equals(MsgExtensionType.groupId)){
                ToastUtils.showShort("切换群组成功");
            }
            MsgExtensionType.groupId = talkGroupInfoBean.getGroupId();
            Logging.d(TAG, "对讲事件: 已加入对讲组,则切换对讲组");
            EventBus.getDefault().post(new UpdateGroupDefaultEvent(MsgExtensionType.groupId)); // 设置默认开启或者不开启位置信息
            // 切换对讲组
            AnyRtcMaxManage.getInstance().switchIntercomGroup();
            holePop();
            FloatIntercomManage.getInstance().setTalkStatus(6);
        }
    }
}
