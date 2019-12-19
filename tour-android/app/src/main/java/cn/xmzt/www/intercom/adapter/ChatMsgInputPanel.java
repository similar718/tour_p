package cn.xmzt.www.intercom.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ActivityTeamRoomBinding;
import cn.xmzt.www.intercom.actions.BaseAction;
import cn.xmzt.www.intercom.actions.BroadcastAction;
import cn.xmzt.www.intercom.actions.CameraAction;
import cn.xmzt.www.intercom.actions.LocationAction;
import cn.xmzt.www.intercom.actions.PhototAction;
import cn.xmzt.www.intercom.actions.VideoAction;
import cn.xmzt.www.intercom.activity.TeamRoomActivity;
import cn.xmzt.www.intercom.session.module.Container;
import cn.xmzt.www.intercom.vm.TeamRoomViewModel;
import cn.xmzt.www.intercom.api.NimUIKit;
import cn.xmzt.www.intercom.business.ait.AitTextChangeListener;
import cn.xmzt.www.intercom.business.session.constant.RequestCode;
import cn.xmzt.www.intercom.business.session.emoji.IEmoticonSelectedListener;
import cn.xmzt.www.intercom.business.session.emoji.MoonUtil;
import cn.xmzt.www.intercom.business.session.module.input.ActionsPanel;
import cn.xmzt.www.intercom.common.util.log.LogUtil;
import cn.xmzt.www.intercom.common.util.string.StringUtil;
import cn.xmzt.www.intercom.impl.NimUIKitImpl;
import cn.xmzt.www.main.globals.FloatIntercomManage;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.CustomNotificationConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 底部文本编辑，语音等模块
 * Created by Averysk
 */
public class ChatMsgInputPanel implements IEmoticonSelectedListener, AitTextChangeListener {

    private static final String TAG = "MsgSendLayout";

    private static final int SHOW_LAYOUT_DELAY = 200;

    private TeamRoomActivity activity;
    private TeamRoomViewModel viewModel;
    private ActivityTeamRoomBinding dataBinding;
    private Container container; // 发送消息容器(包含 activity,account(群id), sessionType(群类型), proxy(发送代理), proxySend(是否发送))
    private View viewAction; // 底部更多操作模块
    private View llActionPanel; // 底部更多操作模块
    protected Handler uiHandler; // ui变化
    private boolean isKeyboardShowed = true; // 是否显示键盘
    private boolean actionPanelBottomLayoutHasSetup = false; // 是否初始化过更多模块布局
    private List<BaseAction> actions;  // 更新多模块数据集合, adapter
    private long typingTime = 0; // 打字时间
    private TextWatcher aitTextWatcher; // 文本@成员模块
    private PhototAction phototAction; // 图片选择
    private LocationAction locationAction; // 定位发送

    public ChatMsgInputPanel(TeamRoomActivity activity, TeamRoomViewModel viewModel, ActivityTeamRoomBinding dataBinding, String groupId, Container container) {
        this.activity = activity;
        this.viewModel = viewModel;
        this.dataBinding = dataBinding;
        this.container = container;
        this.actions = getActionList();
        this.uiHandler = new Handler();
        init();
    }

    // 页面暂停
    public void onPause() {
    }

    // 页面销毁
    public void onDestroy() {
    }

    public boolean collapse(boolean immediately) {
        boolean respond = (dataBinding.emoticonPickerView != null && dataBinding.emoticonPickerView.getVisibility() == View.VISIBLE
                || llActionPanel != null && llActionPanel.getVisibility() == View.VISIBLE);

        hideAllInputLayout(immediately);

        return respond;
    }

    public void addAitTextWatcher(TextWatcher watcher) {
        aitTextWatcher = watcher;
    }

    private void init() {
        initTextEdit();
        restoreText(false);
        initActionList();
        initActionView();
    }

    public void reload(Container container) {
        this.container = container;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initTextEdit() {
        dataBinding.etBottomInputMessage.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        dataBinding.etBottomInputMessage.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    switchToTextLayout(true);
                }
                return false;
            }
        });

        dataBinding.etBottomInputMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                dataBinding.etBottomInputMessage.setHint("");
                checkSendButtonEnable(dataBinding.etBottomInputMessage);
                if (!hasFocus){
                    hideAudioRecord(false);
                }
            }
        });

        dataBinding.etBottomInputMessage.addTextChangedListener(new TextWatcher() {
            private int start;
            private int count;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                this.start = start;
                this.count = count;
                if (aitTextWatcher != null) {
                    aitTextWatcher.onTextChanged(s, start, before, count);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (aitTextWatcher != null) {
                    aitTextWatcher.beforeTextChanged(s, start, count, after);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkSendButtonEnable(dataBinding.etBottomInputMessage);
                MoonUtil.replaceEmoticons(container.activity, s, start, count);

                int editEnd = dataBinding.etBottomInputMessage.getSelectionEnd();
                dataBinding.etBottomInputMessage.removeTextChangedListener(this);
                while (StringUtil.counterChars(s.toString()) > NimUIKitImpl.getOptions().maxInputTextLength && editEnd > 0) {
                    s.delete(editEnd - 1, editEnd);
                    editEnd--;
                }
                dataBinding.etBottomInputMessage.setSelection(editEnd);
                dataBinding.etBottomInputMessage.addTextChangedListener(this);

                if (aitTextWatcher != null) {
                    aitTextWatcher.afterTextChanged(s);
                }

                sendTypingCommand();
            }
        });
    }


    /**
     * 发送“正在输入”通知
     */
    private void sendTypingCommand() {
        if (container.account.equals(NimUIKit.getAccount())) {
            return;
        }

        if (container.sessionType == SessionTypeEnum.Team || container.sessionType == SessionTypeEnum.ChatRoom) {
            return;
        }

        if (System.currentTimeMillis() - typingTime > 5000L) {
            typingTime = System.currentTimeMillis();
            CustomNotification command = new CustomNotification();
            command.setSessionId(container.account);
            command.setSessionType(container.sessionType);
            CustomNotificationConfig config = new CustomNotificationConfig();
            config.enablePush = false;
            config.enableUnreadCount = false;
            command.setConfig(config);

            JSONObject json = new JSONObject();
            json.put("id", "1");
            command.setContent(json.toString());

            NIMClient.getService(MsgService.class).sendCustomNotification(command);
        }
    }

    /*************************** 键盘布局切换 ********************************/
    // 点击底部位置按钮，选择发送位置
    public void openLocationAction() {
        if (locationAction == null){
            locationAction = new LocationAction();
            locationAction.setContainer(container);
        }
        locationAction.onClick();
        // 隐藏其它相关布局
        hideInputMethod();
        hideEmojiLayout();
        hideActionPanelLayout();
        hideAudioRecord(false);
    }

    // 点击底部图片按钮，选择发送图片
    public void openPhotoAction() {
        if (phototAction == null){
            phototAction = new PhototAction();
            phototAction.setContainer(container);
        }
        phototAction.onClick();
        // 隐藏其它相关布局
        hideInputMethod();
        hideEmojiLayout();
        hideActionPanelLayout();
        hideAudioRecord(false);
    }

    // 点击edittext，切换键盘和更多布局
    private void switchToTextLayout(boolean needShowInput) {
        hideEmojiLayout();
        hideActionPanelLayout();
        if (needShowInput) {
            uiHandler.postDelayed(showTextRunnable, SHOW_LAYOUT_DELAY);
            hideAudioRecord(true);
        } else {
            hideInputMethod();
            hideAudioRecord(false);
        }
    }

    // 发送文本消息
    public void onTextMessageSendButtonPressed() {
        String text = dataBinding.etBottomInputMessage.getText().toString();
        IMMessage textMessage = createTextMessage(text);
        if (container.proxy.sendMessage(textMessage)) {
            restoreText(true);
        }
        // 隐藏其它相关布局
        hideInputMethod();
        hideEmojiLayout();
        hideActionPanelLayout();
        hideAudioRecord(false);
    }

    // 创建文本消息
    private IMMessage createTextMessage(String text) {
        return MessageBuilder.createTextMessage(container.account, container.sessionType, text);
    }

    // 点击“+”号按钮，切换更多布局和键盘
    public void toggleActionPanelLayout(boolean isShow) {
        if (llActionPanel == null || llActionPanel.getVisibility() == View.GONE || isShow) {
            // 显示更多布局
            showActionPanelLayout();
            // 隐藏其它相关布局
            hideInputMethod();
            hideEmojiLayout();
            hideAudioRecord(true);
        } else {
            // 隐藏更多布局
            hideActionPanelLayout();
            // 隐藏其它相关布局
            hideInputMethod();
            hideEmojiLayout();
            hideAudioRecord(false);
        }
    }

    // 显示更多布局
    private void showActionPanelLayout() {
        addActionPanelLayout();
        uiHandler.postDelayed(showMoreFuncRunnable, SHOW_LAYOUT_DELAY);
        container.proxy.onInputPanelExpand();
        hideActionPlusOrCross(true);
    }

    // 隐藏更多布局
    private void hideActionPanelLayout() {
        uiHandler.removeCallbacks(showMoreFuncRunnable);
        if (llActionPanel != null) {
            llActionPanel.setVisibility(View.GONE);
        }
        hideActionPlusOrCross(false);
    }

    // 点击表情，切换到表情布局
    public void toggleEmojiLayout() {
        if (dataBinding.emoticonPickerView == null || dataBinding.emoticonPickerView.getVisibility() == View.GONE) {
            // 显示表情布局
            showEmojiLayout();
            // 隐藏其它相关布局
            hideInputMethod();
            hideActionPanelLayout();
            hideAudioRecord(true);
        } else {
            // 隐藏表情布局
            hideEmojiLayout();
            // 隐藏其它相关布局
            hideInputMethod();
            hideActionPanelLayout();
            hideAudioRecord(false);
        }
    }

    // 显示表情布局
    private void showEmojiLayout() {
        dataBinding.etBottomInputMessage.requestFocus();
        uiHandler.postDelayed(showEmojiRunnable, 200);
        dataBinding.emoticonPickerView.setVisibility(View.VISIBLE);
        dataBinding.emoticonPickerView.show(this);
        container.proxy.onInputPanelExpand();
    }

    // 隐藏表情布局
    private void hideEmojiLayout() {
        uiHandler.removeCallbacks(showEmojiRunnable);
        if (dataBinding.emoticonPickerView != null) {
            dataBinding.emoticonPickerView.setVisibility(View.GONE);
        }
    }

    // 隐藏键盘布局
    private void hideInputMethod() {
        isKeyboardShowed = false;
        uiHandler.removeCallbacks(showTextRunnable);
        InputMethodManager imm = (InputMethodManager) container.activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(dataBinding.etBottomInputMessage.getWindowToken(), 0);
        dataBinding.etBottomInputMessage.clearFocus();
    }

    // 是否隐藏对讲录制按钮
    public void hideAudioRecord(boolean isShow){
        if (isShow){
            // 隐藏大语音录制按钮
            //ivAudioRecord.setVisibility(View.INVISIBLE);
            FloatIntercomManage.getInstance().hideFloatView(activity);
            // 显示小语音录制按钮
            dataBinding.ivBottomVoice.setVisibility(View.VISIBLE);
        } else {
            if (dataBinding.ivBottomVoice.getVisibility() == View.VISIBLE) {
                // 显示大语音录制按钮
                //ivAudioRecord.setVisibility(View.VISIBLE);
                FloatIntercomManage.getInstance().showFloatView(activity);
                // 隐藏小语音录制按钮
                dataBinding.ivBottomVoice.setVisibility(View.INVISIBLE);
            }
        }
    }

    // 是否隐藏"+"号 或 "x"号
    private void hideActionPlusOrCross(boolean isShow){
        if (isShow){
            // 隐藏"+"号
            dataBinding.ivBottomPlus.setVisibility(View.INVISIBLE);
            // 显示"x"号
            dataBinding.ivBottomCross.setVisibility(View.VISIBLE);
        } else {
            // 显示"+"号
            dataBinding.ivBottomPlus.setVisibility(View.VISIBLE);
            // 隐藏"x"号
            dataBinding.ivBottomCross.setVisibility(View.INVISIBLE);
        }
    }

    // 初始化更多布局
    private void addActionPanelLayout() {
        if (llActionPanel == null) {
            llActionPanel = viewAction.findViewById(R.id.ll_actions);
            actionPanelBottomLayoutHasSetup = false;
        }
        initActionPanelLayout();
    }

    // 显示键盘布局
    private void showInputMethod(EditText editTextMessage) {
        editTextMessage.requestFocus();
        //如果已经显示,则继续操作时不需要把光标定位到最后
        if (!isKeyboardShowed) {
            editTextMessage.setSelection(editTextMessage.getText().length());
            isKeyboardShowed = true;
        }

        InputMethodManager imm = (InputMethodManager) container.activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editTextMessage, 0);

        container.proxy.onInputPanelExpand();
    }

    // 点击更多布局中的 "小视频" 或 "相机" 或 "广播"
    public void toggleActionsLayout(){
        // 隐藏其它相关布局
        // 隐藏输入布局
        hideInputMethod();
        // 隐藏表情布局
        hideEmojiLayout();
        // 隐藏更多布局
        hideActionPanelLayout();
        // 隐藏小语音录制按钮,显示大语音录制按钮
        hideAudioRecord(false);
    }

    // 初始化具体more layout中的项目
    private void initActionPanelLayout() {
        if (actionPanelBottomLayoutHasSetup) {
            return;
        }
        ActionsPanel.init(viewAction, actions);
        actionPanelBottomLayoutHasSetup = true;
    }

    private void initActionList() {
        for (int i = 0; i < actions.size(); ++i) {
            actions.get(i).setIndex(i);
            actions.get(i).setContainer(container);
        }
        if (phototAction == null){
            phototAction = new PhototAction();
            phototAction.setContainer(container);
        }
        if (locationAction == null){
            locationAction = new LocationAction();
            locationAction.setContainer(container);
        }
    }

    private void initActionView() {
        if (viewAction == null){
            viewAction = View.inflate(container.activity, R.layout.layout_intercom_team_message_actions, dataBinding.llBottomMessage);
        }
        if (llActionPanel == null) {
            llActionPanel = viewAction.findViewById(R.id.ll_actions);
            actionPanelBottomLayoutHasSetup = false;
        }
    }

    // 操作面板集合(默认排版)
    private List<BaseAction> getActionList() {
        List<BaseAction> actions = new ArrayList<>();
        actions.add(new VideoAction());
        actions.add(new CameraAction());
        actions.add(new BroadcastAction());
        setActionsPanel(actions);
        return actions;
    }

    public void setActionsPanel(List<BaseAction> actions){
        this.actions = actions;
        for (int i = 0; i < actions.size(); ++i) {
            actions.get(i).setIndex(i);
            actions.get(i).setContainer(container);
        }
        if (viewAction == null){
            viewAction = View.inflate(container.activity, R.layout.layout_intercom_team_message_actions, dataBinding.llBottomMessage);
        }
        if (llActionPanel == null) {
            llActionPanel = viewAction.findViewById(R.id.ll_actions);
            actionPanelBottomLayoutHasSetup = false;
        }
        ActionsPanel.init(viewAction, actions);
        actionPanelBottomLayoutHasSetup = true;
    }

    private Runnable showEmojiRunnable = new Runnable() {
        @Override
        public void run() {
            dataBinding.emoticonPickerView.setVisibility(View.VISIBLE);
        }
    };

    private Runnable showMoreFuncRunnable = new Runnable() {
        @Override
        public void run() {
            llActionPanel.setVisibility(View.VISIBLE);
        }
    };

    private Runnable showTextRunnable = new Runnable() {
        @Override
        public void run() {
            showInputMethod(dataBinding.etBottomInputMessage);
        }
    };

    // 恢复文本输入
    private void restoreText(boolean clearText) {
        if (clearText) {
            dataBinding.etBottomInputMessage.setText("");
        }
        checkSendButtonEnable(dataBinding.etBottomInputMessage);
    }

    // 检查是否 显示发送按钮
    private void checkSendButtonEnable(EditText editText) {
        String textMessage = editText.getText().toString();
        if (!TextUtils.isEmpty(StringUtil.removeBlanks(textMessage))) {
        //if (!TextUtils.isEmpty(StringUtil.removeBlanks(textMessage)) && editText.hasFocus()) {
            dataBinding.tvBottomSend.setVisibility(View.VISIBLE);
        } else {
            dataBinding.tvBottomSend.setVisibility(View.GONE);
        }
    }

    /**
     * *************** IEmojiSelectedListener ***************
     */
    @Override
    public void onEmojiSelected(String key) {
        Editable mEditable = dataBinding.etBottomInputMessage.getText();
        if (key.equals("/DEL")) {
            dataBinding.etBottomInputMessage.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
        } else {
            int start = dataBinding.etBottomInputMessage.getSelectionStart();
            int end = dataBinding.etBottomInputMessage.getSelectionEnd();
            start = (start < 0 ? 0 : start);
            end = (start < 0 ? 0 : end);
            mEditable.replace(start, end, key);
        }
    }

    private Runnable hideAllInputLayoutRunnable;

    @Override
    public void onStickerSelected(String category, String item) {
        Log.i("InputPanel", "onStickerSelected, category =" + category + ", sticker =" + item);
        MsgAttachment attachment = null;
        IMMessage stickerMessage = MessageBuilder.createCustomMessage(container.account, container.sessionType, "贴图消息", attachment);
        container.proxy.sendMessage(stickerMessage);
    }

    @Override
    public void onTextAdd(String content, int start, int length) {
        if (dataBinding.etBottomInputMessage.getVisibility() != View.VISIBLE ||
                (dataBinding.emoticonPickerView != null && dataBinding.emoticonPickerView.getVisibility() == View.VISIBLE)) {
            switchToTextLayout(true);
        } else {
            uiHandler.postDelayed(showTextRunnable, SHOW_LAYOUT_DELAY);
        }
        dataBinding.etBottomInputMessage.getEditableText().insert(start, content);
    }

    @Override
    public void onTextDelete(int start, int length) {
        if (dataBinding.etBottomInputMessage.getVisibility() != View.VISIBLE) {
            switchToTextLayout(true);
        } else {
            uiHandler.postDelayed(showTextRunnable, SHOW_LAYOUT_DELAY);
        }
        int end = start + length - 1;
        dataBinding.etBottomInputMessage.getEditableText().replace(start, end, "");
    }

    public int getEditSelectionStart() {
        return dataBinding.etBottomInputMessage.getSelectionStart();
    }


    // 隐藏所有输入布局
    private void hideAllInputLayout(boolean immediately) {
        if (hideAllInputLayoutRunnable == null) {
            hideAllInputLayoutRunnable = new Runnable() {
                @Override
                public void run() {
                    hideInputMethod();
                    hideActionPanelLayout();
                    hideEmojiLayout();
                    if (!immediately){
                        hideAudioRecord(false);
                    }
                }
            };
        }
        long delay = immediately ? 0 : ViewConfiguration.getDoubleTapTimeout();
        uiHandler.postDelayed(hideAllInputLayoutRunnable, delay);
    }

    // 界面回调信息
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        int index = (requestCode << 16) >> 24;
        if (index != 0) {
            index--;
            if (index < 0 | index >= actions.size()) {
                LogUtil.d(TAG, "request code out of actions' range");
                return;
            }
            BaseAction action = actions.get(index);
            if (action != null) {
                action.onActivityResult(requestCode & 0xff, resultCode, data);
            }
        }
        switch (requestCode & 0xff) {
            case RequestCode.PICK_PHOTO:
                if (phototAction != null){
                    phototAction.onActivityResult(requestCode & 0xff, resultCode, data);
                }
                break;
        }
    }

}
