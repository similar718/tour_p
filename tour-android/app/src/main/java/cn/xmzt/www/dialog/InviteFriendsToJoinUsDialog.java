package cn.xmzt.www.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.DialogInviteFriendsToJoinUsBinding;
import cn.xmzt.www.utils.DpUtil;

public class InviteFriendsToJoinUsDialog extends Dialog {

    private Context mContext;

    private View.OnClickListener onClickListener;

    DialogInviteFriendsToJoinUsBinding binding;

    public InviteFriendsToJoinUsDialog(Context context) {
        super(context, R.style.dialog_custom);
        this.mContext = context;
    }

    public InviteFriendsToJoinUsDialog(Context context, View.OnClickListener onClickListener) {
        super(context, R.style.dialog_custom);
        this.mContext = context;
        this.onClickListener = onClickListener;
        binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_invite_friends_to_join_us, null, false);
        setContentView(binding.getRoot());
        initView();
    }

    protected void initView() {
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.gravity = Gravity.CENTER; // 设置重力
        //lp.y = -mScreenHeight / 10;// 设置y坐标
        lp.width= WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        // 取消事件
        binding.dialogCancelIv.setOnClickListener(onClickListener);
        // 邀请按钮
        binding.dialogInviteTv.setOnClickListener(onClickListener);
        // 点击之外的地方 dismiss
        setCancelable(true);
    }
    public void setViewData(String content) {
        String contentdata = "";
        for (int i = 0 ; i < content.length();i++){
            contentdata += content.substring(i,i+1);
            if (i != (content.length()-1)) {
                contentdata += "  ";
            }
        }
        binding.dialogInviteCodeStv.setText(contentdata);
    }
}
