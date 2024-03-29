package cn.xmzt.www.nim.uikit.business.contact.core.model;

import cn.xmzt.www.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nimlib.sdk.team.model.TeamMember;

/**
 * Created by huangjun on 2015/5/5.
 */
public class TeamMemberContact extends AbsContact {

    private TeamMember teamMember;

    public TeamMemberContact(TeamMember teamMember) {
        this.teamMember = teamMember;
    }

    @Override
    public String getContactId() {
        return teamMember.getAccount();
    }

    @Override
    public int getContactType() {
        return Type.TeamMember;
    }

    @Override
    public String getDisplayName() {
        return TeamHelper.getTeamMemberDisplayName(teamMember.getTid(), teamMember.getAccount());
    }
}
