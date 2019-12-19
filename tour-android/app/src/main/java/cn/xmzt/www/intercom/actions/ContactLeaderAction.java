package cn.xmzt.www.intercom.actions;

import com.blankj.utilcode.util.ActivityUtils;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.dialog.ContactLeaderDialog;
import cn.xmzt.www.intercom.bean.GroupLeaderBean;

/**
 * 联系领队按钮
 */
public class ContactLeaderAction extends BaseAction {
    private List<GroupLeaderBean> leaderList;//领队列表
    public ContactLeaderAction() {
        super(R.drawable.icon_contact_leader, R.string.intercom_input_panel_contact_leader);
    }
    public ContactLeaderAction(List<GroupLeaderBean> leaderList) {
        super(R.drawable.icon_contact_leader, R.string.intercom_input_panel_contact_leader);
        this.leaderList=leaderList;
    }

    public void setLeaderList(List<GroupLeaderBean> leaderList) {
        this.leaderList = leaderList;
    }

    @Override
    public void onClick() {
        if(leaderList!=null&&leaderList.size()>0){
            showContactLeaderDialog();
//            callPhone(phone);
        }
    }
    private ContactLeaderDialog contactLeaderDialog;
    private void showContactLeaderDialog(){
        if(contactLeaderDialog==null){
            contactLeaderDialog=new ContactLeaderDialog(ActivityUtils.getTopActivity());
        }
        contactLeaderDialog.setViewData(leaderList);
        contactLeaderDialog.show();
    }

}

