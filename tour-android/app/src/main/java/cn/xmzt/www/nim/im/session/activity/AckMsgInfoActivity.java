package cn.xmzt.www.nim.im.session.activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import cn.xmzt.www.R;
import cn.xmzt.www.nim.im.common.ui.viewpager.FadeInOutPageTransformer;
import cn.xmzt.www.nim.im.common.ui.viewpager.PagerSlidingTabStrip;
import cn.xmzt.www.nim.im.main.reminder.ReminderItem;
import cn.xmzt.www.nim.im.session.adapter.AckMsgTabPagerAdapter;
import cn.xmzt.www.nim.im.session.model.AckMsgTab;
import cn.xmzt.www.nim.im.session.model.AckMsgViewModel;
import cn.xmzt.www.nim.uikit.api.wrapper.NimToolBarOptions;
import cn.xmzt.www.nim.uikit.common.activity.ToolBarOptions;
import cn.xmzt.www.nim.uikit.common.activity.UI;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.TeamMsgAckInfo;

/**
 * 消息已读详情界面
 * Created by winnie on 2018/3/14.
 */

public class AckMsgInfoActivity extends UI implements ViewPager.OnPageChangeListener {
    public final static String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    private PagerSlidingTabStrip tabs;

    private ViewPager pager;

    private int scrollState;

    private AckMsgTabPagerAdapter adapter;

    private AckMsgViewModel viewModel;

    private ReminderItem unreadItem;

    private ReminderItem readItem;

    public static void start(Context context, IMMessage message) {
        Intent intent = new Intent();
        intent.setClass(context, AckMsgInfoActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ack_msg_info_layout);


        ToolBarOptions options = new NimToolBarOptions();
        options.titleId = R.string.ack_msg_info;
        options.navigateId = R.drawable.actionbar_dark_back_icon;
        setToolBar(R.id.toolbar, options);

        findViews();
        setupPager();
        setupTabs();

        IMMessage message = (IMMessage) getIntent().getSerializableExtra(AckMsgInfoActivity.EXTRA_MESSAGE);
        viewModel = ViewModelProviders.of(this).get(AckMsgViewModel.class);
        viewModel.init(message);
        unreadItem = new ReminderItem(AckMsgTab.UNREAD.reminderId);
        readItem = new ReminderItem(AckMsgTab.READ.reminderId);
        viewModel.getTeamMsgAckInfo().observe(this, new Observer<TeamMsgAckInfo>() {
            @Override
            public void onChanged(@Nullable TeamMsgAckInfo teamMsgAckInfo) {
                unreadItem.setUnread(teamMsgAckInfo.getUnAckCount());
                updateReminder(unreadItem, AckMsgTab.UNREAD.reminderId);

                readItem.setUnread(teamMsgAckInfo.getAckCount());
                updateReminder(readItem, AckMsgTab.READ.reminderId);
            }
        });
    }

    /**
     * 查找页面控件
     */
    private void findViews() {
        tabs = findView(R.id.tabs);
        tabs.setFakeDropOpen(false);
        pager = findView(R.id.main_tab_pager);
    }

    /**
     * 设置viewPager
     */
    private void setupPager() {
        // CACHE COUNT
        adapter = new AckMsgTabPagerAdapter(getSupportFragmentManager(), this, pager);
        pager.setOffscreenPageLimit(adapter.getCacheCount());
        // page swtich animation
        pager.setPageTransformer(true, new FadeInOutPageTransformer());
        // ADAPTER
        pager.setAdapter(adapter);
        // TAKE OVER CHANGE
        pager.setOnPageChangeListener(this);
    }

    /**
     * 设置tab条目
     */
    private void setupTabs() {
        tabs.setOnCustomTabListener(new PagerSlidingTabStrip.OnCustomTabListener() {
            @Override
            public int getTabLayoutResId(int position) {
                return R.layout.tab_layout_main;
            }

            @Override
            public boolean screenAdaptation() {
                return true;
            }
        });
        tabs.setViewPager(pager);
        tabs.setOnTabClickListener(adapter);
        tabs.setOnTabDoubleTapListener(adapter);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // TO TABS
        tabs.onPageScrolled(position, positionOffset, positionOffsetPixels);
        // TO ADAPTER
        adapter.onPageScrolled(position);
    }

    @Override
    public void onPageSelected(int position) {
        // TO TABS
        tabs.onPageSelected(position);

        selectPage(position);

//        enableMsgNotification(false);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // TO TABS
        tabs.onPageScrollStateChanged(state);

        scrollState = state;

        selectPage(pager.getCurrentItem());
    }

    private void selectPage(int page) {
        // TO PAGE
        if (scrollState == ViewPager.SCROLL_STATE_IDLE) {
            adapter.onPageSelected(pager.getCurrentItem());
        }
    }

    private void updateReminder(ReminderItem item, int id) {

    }
}
