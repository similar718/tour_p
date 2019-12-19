package cn.xmzt.www.nim.uikit.impl.customization;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import cn.xmzt.www.intercom.common.ToastHelper;

import cn.xmzt.www.R;
import cn.xmzt.www.intercom.api.NimUIKit;
import cn.xmzt.www.nim.uikit.api.model.session.SessionCustomization;
import cn.xmzt.www.nim.uikit.business.team.model.TeamExtras;
import cn.xmzt.www.nim.uikit.business.team.model.TeamRequestCode;
import com.netease.nimlib.sdk.team.model.Team;

import java.util.ArrayList;

/**
 * SessionCustomization 可以实现聊天界面定制项：
 * 1. 聊天背景 <br>
 * 2. 加号展开后的按钮和动作，如自定义消息 <br>
 * 3. ActionBar右侧按钮。
 * <p>
 * DefaultTeamSessionCustomization 提供默认的群聊界面定制，添加了ActionBar右侧按钮，用于跳转群信息界面
 * <p>
 * Created by Averysk
 */

public class DefaultTeamSessionCustomization extends SessionCustomization {

    public DefaultTeamSessionCustomization() {

        // ActionBar右侧按钮，跳转至群信息界面
        OptionsButton infoButton = new OptionsButton() {
            @Override
            public void onClick(Context context, View view, String sessionId, String lineId, int tripId) {
                Team team = NimUIKit.getTeamProvider().getTeamById(sessionId);
                if (team != null && team.isMyTeam()) {
                    NimUIKit.startTeamInfo(context, sessionId, lineId,  tripId);
                } else {
                    ToastHelper.showToast(context, R.string.team_invalid_tip);
                }
            }
        };
        infoButton.iconId = R.drawable.action_bar_black_more_icon;
        buttons = new ArrayList<>();
        buttons.add(infoButton);
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == TeamRequestCode.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String reason = data.getStringExtra(TeamExtras.RESULT_EXTRA_REASON);
                boolean finish = reason != null && (reason.equals(TeamExtras
                        .RESULT_EXTRA_REASON_DISMISS) || reason.equals(TeamExtras.RESULT_EXTRA_REASON_QUIT));
                if (finish) {
                    activity.finish(); // 退出or解散群直接退出多人会话
                }
            }
        }
    }

}
