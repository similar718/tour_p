package cn.xmzt.www.mine.activity;

import android.content.Context;
import android.content.Intent;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityLoginBinding;
import cn.xmzt.www.helper.UserHelper;
import cn.xmzt.www.mine.model.LoginViewModel;
import com.umeng.socialize.UMShareAPI;

/**
 * @author tanlei
 * @date 2019/8/5
 * @describe 新的登录界面
 */

public class LoginActivity extends TourBaseActivity<LoginViewModel, ActivityLoginBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginViewModel createViewModel() {
        return new LoginViewModel();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean isFromWx = intent.getBooleanExtra("B", false);
        if (isFromWx) {
            String wx_code = intent.getStringExtra("A");
            viewModel.wxLogin(wx_code);
        }
    }
    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);

        // 相除对讲相关业务
        UserHelper.clearTalkBusiness(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    public void startToRegisterActivity() {
        startToActivity(RegisterActivity.class);
    }

    public void startToFindPassWordActivity() {
        startToActivity(FindPasswordActivity.class);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
