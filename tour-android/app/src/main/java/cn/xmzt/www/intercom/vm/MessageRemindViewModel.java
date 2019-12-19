package cn.xmzt.www.intercom.vm;

import androidx.databinding.ObservableInt;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.intercom.activity.MessageRemindActivity;
import cn.xmzt.www.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamMessageNotifyTypeEnum;

import static com.blankj.utilcode.util.StringUtils.getString;

/**
 * @author tanlei
 * @date 2019/9/5
 * @describe
 */

public class MessageRemindViewModel extends BaseViewModel {
    private MessageRemindActivity activity;
    public ObservableInt checkPosition = new ObservableInt();
    private String teamId;

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public void setActivity(MessageRemindActivity activity) {
        this.activity = activity;
    }

    public void onClick(View view) {
        TeamMessageNotifyTypeEnum type;
        switch (view.getId()) {
            case R.id.title_back_iv:
                activity.finish();
                break;
            case R.id.ll_open://开启
                type = TeamMessageNotifyTypeEnum.All;
                setMessageRemind(type,0);
                break;
            case R.id.ll_accept_message://接收但不提醒
                type = TeamMessageNotifyTypeEnum.All;
                setMessageRemind(type,1);
                break;
            case R.id.ll_close_message://屏蔽消息
                type = TeamMessageNotifyTypeEnum.Mute;
                setMessageRemind(type,2);
                break;
            default:
                break;
        }
    }

    private void setMessageRemind(TeamMessageNotifyTypeEnum type,int position) {
        DialogMaker.showProgressDialog(activity, getString(R.string.empty), true);
        NIMClient.getService(TeamService.class).muteTeam(teamId, type)
                .setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        DialogMaker.dismissProgressDialog();
                        checkPosition.set(position);
                    }

                    @Override
                    public void onFailed(int code) {
                        DialogMaker.dismissProgressDialog();
                    }

                    @Override
                    public void onException(Throwable exception) {
                        DialogMaker.dismissProgressDialog();
                    }
                });
    }
}
