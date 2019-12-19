package cn.xmzt.www.intercom.cache;

import android.content.Context;

import cn.xmzt.www.R;
import cn.xmzt.www.intercom.api.NimUIKit;
import cn.xmzt.www.nim.uikit.business.session.audio.AudioMessagePlayable;
import cn.xmzt.www.intercom.common.ToastHelper;
import cn.xmzt.www.nim.uikit.common.media.audioplayer.BaseAudioControl;
import cn.xmzt.www.nim.uikit.common.media.audioplayer.Playable;
import cn.xmzt.www.nim.uikit.common.util.storage.StorageUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;
import java.util.List;

/**
 * Created by Averysk
 * <p>
 * 群聊相关业语音播放控件中心务
 */
public class TeamMessageAudioControl extends BaseAudioControl<IMMessage> {
    private static TeamMessageAudioControl mMessageAudioControl = null;

    private boolean mIsNeedPlayNext = false;
    private List<IMMessage> mIMMessageList = null;
    private IMMessage mItem = null;

    private TeamMessageAudioControl(Context context) {
        super(context, true);
    }

    public static TeamMessageAudioControl getInstance() {
        if (mMessageAudioControl == null) {
            synchronized (TeamMessageAudioControl.class) {
                if (mMessageAudioControl == null) {
                    mMessageAudioControl = new TeamMessageAudioControl(NimUIKit.getContext());
                }
            }
        }

        return mMessageAudioControl;
    }

    @Override
    protected void setOnPlayListener(Playable playingPlayable, AudioControlListener audioControlListener) {
        this.audioControlListener = audioControlListener;

        BasePlayerListener basePlayerListener = new BasePlayerListener(currentAudioPlayer, playingPlayable) {

            @Override
            public void onInterrupt() {
                if (!checkAudioPlayerValid()) {
                    return;
                }

                super.onInterrupt();
                cancelPlayNext();
            }

            @Override
            public void onError(String error) {
                if (!checkAudioPlayerValid()) {
                    return;
                }

                super.onError(error);
                cancelPlayNext();
            }

            @Override
            public void onCompletion() {
                if (!checkAudioPlayerValid()) {
                    return;
                }

                resetAudioController(listenerPlayingPlayable);

                boolean isLoop = false;
                if (mIsNeedPlayNext) {
                    if (mItem != null) {
                        isLoop = playNextAudio(mItem);
                    }
                }

                if (!isLoop) {
                    if (audioControlListener != null) {
                        audioControlListener.onEndPlay(currentPlayable);
                    }

                    playSuffix();
                }
            }
        };

        basePlayerListener.setAudioControlListener(audioControlListener);
        currentAudioPlayer.setOnPlayListener(basePlayerListener);
    }

    @Override
    public IMMessage getPlayingAudio() {
        if (isPlayingAudio() && AudioMessagePlayable.class.isInstance(currentPlayable)) {
            return ((AudioMessagePlayable) currentPlayable).getMessage();
        } else {
            return null;
        }
    }

    @Override
    public void startPlayAudioDelay(
            final long delayMillis,
            final IMMessage message,
            final AudioControlListener audioControlListener, final int audioStreamType) {
        // 如果不存在则下载
        AudioAttachment audioAttachment = (AudioAttachment) message.getAttachment();
        File file = new File(audioAttachment.getPathForSave());
        if (!file.exists()) {
            NIMClient.getService(MsgService.class).downloadAttachment(message, false).setCallback(new RequestCallbackWrapper() {
                @Override
                public void onResult(int code, Object result, Throwable exception) {
                    startPlayAudio(message, audioControlListener, audioStreamType, true, delayMillis);
                }
            });
            return;
        }
        startPlayAudio(message, audioControlListener, audioStreamType, true, delayMillis);
    }

    //连续播放时不需要resetOrigAudioStreamType
    private void startPlayAudio(
            IMMessage message,
            AudioControlListener audioControlListener,
            int audioStreamType,
            boolean resetOrigAudioStreamType,
            long delayMillis) {
        if (StorageUtil.isExternalStorageExist()) {
            if (startAudio(new AudioMessagePlayable(message), audioControlListener, audioStreamType, resetOrigAudioStreamType, delayMillis)) {
                // 将未读标识去掉,更新数据库
                if (isUnreadAudioMessage(message)) {
                    message.setStatus(MsgStatusEnum.read);
                    NIMClient.getService(MsgService.class).updateIMMessageStatus(message);
                }
            }
        } else {
            ToastHelper.showToast(mContext, R.string.sdcard_not_exist_error);
        }
    }

    private boolean playNextAudio(IMMessage messageItem) {
        if (mIMMessageList == null && mIMMessageList.size() == 0){
            return false;
        }
        final List<?> list = mIMMessageList;
        int index = 0;
        int nextIndex = -1;
        //找到当前已经播放的
        for (int i = 0; i < list.size(); ++i) {
            IMMessage item = (IMMessage) list.get(i);
            if (item.equals(messageItem)) {
                index = i;
                break;
            }
        }
        //找到下一个将要播放的
        for (int i = index; i < list.size(); ++i) {
            IMMessage item = (IMMessage) list.get(i);
            IMMessage message = item;
            if (isUnreadAudioMessage(message)) {
                nextIndex = i;
                break;
            }
        }

        if (nextIndex == -1) {
            cancelPlayNext();
            return false;
        }
        IMMessage message = (IMMessage) list.get(nextIndex);
        AudioAttachment attach = (AudioAttachment) message.getAttachment();
        if (mMessageAudioControl != null && attach != null) {
            if (message.getAttachStatus() != AttachStatusEnum.transferred) {
                cancelPlayNext();
                return false;
            }
            if (message.getStatus() != MsgStatusEnum.read) {
                message.setStatus(MsgStatusEnum.read);
                NIMClient.getService(MsgService.class).updateIMMessageStatus(message);
            }
            //不是直接通过点击ViewHolder开始的播放，不设置AudioControlListener
            //连续播放 1.继续使用playingAudioStreamType 2.不需要resetOrigAudioStreamType
            mMessageAudioControl.startPlayAudio(message, null, getCurrentAudioStreamType(), false, 0);
            mItem = (IMMessage) list.get(nextIndex);
            return true;
        }
        return false;
    }

    private void cancelPlayNext() {
        setPlayNext(false, null, null);
    }

    public void setPlayNext(boolean isPlayNext, IMMessage item, List<IMMessage> imMessageList) {
        mIsNeedPlayNext = isPlayNext;
        mItem = item;
        mIMMessageList = imMessageList;
    }

    public void stopAudio() {
        super.stopAudio();
    }

    public boolean isUnreadAudioMessage(IMMessage message) {
        if ((message.getMsgType() == MsgTypeEnum.audio)
                && message.getDirect() == MsgDirectionEnum.In
                && message.getAttachStatus() == AttachStatusEnum.transferred
                && message.getStatus() != MsgStatusEnum.read) {
            return true;
        } else {
            return false;
        }
    }
}
