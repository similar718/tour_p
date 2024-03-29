package cn.xmzt.www.intercom.adapter;

import android.content.Context;
import android.os.Handler;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.intercom.business.session.emoji.MoonUtil;
import cn.xmzt.www.nim.uikit.business.session.helper.TeamNotificationHelper;
import cn.xmzt.www.nim.uikit.common.ui.imageview.HeadImageView;
import cn.xmzt.www.nim.uikit.common.ui.recyclerview.adapter.BaseFetchLoadAdapter;
import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * 新消息提醒模块
 * Created by Averysk
 */
public class ChatMsgPromptPanel {
    // 底部新消息提示条
    private View newMessageTipLayout;
    private TextView newMessageTipTextView;
    private HeadImageView newMessageTipHeadImageView;

    private Context context;
    private FrameLayout view;
    private RecyclerView messageListView;
    private BaseFetchLoadAdapter adapter;
    private Handler uiHandler;

    public ChatMsgPromptPanel(Context context, FrameLayout view, RecyclerView messageListView, BaseFetchLoadAdapter adapter,
                              Handler uiHandler) {
        this.context = context;
        this.view = view;
        this.messageListView = messageListView;
        this.adapter = adapter;
        this.uiHandler = uiHandler;
    }

    // 显示底部新信息提示条
    public void show(IMMessage newMessage) {
        if (newMessageTipLayout == null) {
            init();
        }

        if (!TextUtils.isEmpty(newMessage.getFromAccount())) {
            newMessageTipHeadImageView.loadBuddyAvatar(newMessage.getFromAccount());
        } else {
            newMessageTipHeadImageView.resetImageView();
        }

        MoonUtil.identifyFaceExpression(context, newMessageTipTextView, TeamNotificationHelper.getMsgShowText(newMessage),
                ImageSpan.ALIGN_BOTTOM);
        newMessageTipLayout.setVisibility(View.VISIBLE);
        uiHandler.removeCallbacks(showNewMessageTipLayoutRunnable);
        uiHandler.postDelayed(showNewMessageTipLayoutRunnable, 5 * 1000);
    }

    public void onBackPressed() {
        removeHandlerCallback();
    }

    // 初始化底部新信息提示条
    private void init() {
        //ViewGroup containerView = (ViewGroup) view.findViewById(R.id.message_activity_list_view_container);
        View.inflate(context, R.layout.nim_new_message_tip_layout, view);
        newMessageTipLayout = view.findViewById(R.id.new_message_tip_layout);
        newMessageTipLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                messageListView.scrollToPosition(adapter.getBottomDataPosition());
                newMessageTipLayout.setVisibility(View.GONE);
            }
        });
        newMessageTipTextView = (TextView) newMessageTipLayout.findViewById(R.id.new_message_tip_text_view);
        newMessageTipHeadImageView = (HeadImageView) newMessageTipLayout.findViewById(R.id.new_message_tip_head_image_view);
    }

    private Runnable showNewMessageTipLayoutRunnable = new Runnable() {

        @Override
        public void run() {
            newMessageTipLayout.setVisibility(View.GONE);
        }
    };

    private void removeHandlerCallback() {
        if (showNewMessageTipLayoutRunnable != null) {
            uiHandler.removeCallbacks(showNewMessageTipLayoutRunnable);
        }
    }
}
