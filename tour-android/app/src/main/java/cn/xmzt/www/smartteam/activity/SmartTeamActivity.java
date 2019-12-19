package cn.xmzt.www.smartteam.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.Observer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivitySmartTeamBinding;
import cn.xmzt.www.dialog.ConfirmDialog;
import cn.xmzt.www.intercom.event.RefreshMyTalkGroupList;
import cn.xmzt.www.manager.IntentManager;
import cn.xmzt.www.mine.activity.LoginActivity;
import cn.xmzt.www.mine.event.LoginEvent;
import cn.xmzt.www.smartteam.vm.SmartTeamViewModel;
import cn.xmzt.www.utils.GPSUtils;
import cn.xmzt.www.utils.KeyBoardUtils;
import cn.xmzt.www.utils.StatusBarUtil;
import cn.xmzt.www.view.TextEditTextView;
import cn.xmzt.www.view.listener.SoftKeyboardStateHelper;
import cn.xmzt.www.view.listener.TextChangedListener;

/**
 * 智能组队
 */
public class SmartTeamActivity extends TourBaseActivity<SmartTeamViewModel, ActivitySmartTeamBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_smart_team;
    }
    @Override
    protected SmartTeamViewModel createViewModel() {
        viewModel = new SmartTeamViewModel();
        viewModel.liveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String groupId) {
                    EventBus.getDefault().post(new RefreshMyTalkGroupList()); // 更新对讲群列表信息
                    IntentManager.getInstance().goSmartTeamMapActivity(SmartTeamActivity.this,groupId,mIsCreate);
                    mIsCreate = false;
                    finish();
            }
        });

        return viewModel ;
    }
    private int statusBarHeight;
    @Override
    protected void initData() {
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarLightMode(this,true);
        statusBarHeight= StatusBarUtil.getStatusBarHeight(getApplicationContext());
        RelativeLayout.LayoutParams listParams = (RelativeLayout.LayoutParams) dataBinding.titleLayout.getLayoutParams();
        listParams.topMargin=statusBarHeight;
        dataBinding.setActivity(this);
        EventBus.getDefault().register(this);
        dataBinding.etTeam.addTextChangedListener(mTextWatcher);
        setListenerFotEditText(dataBinding.etTeam);
        ((AppCompatEditText)dataBinding.etTeam).addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.e("shiyong","oldTop="+oldTop+ " top="+top+" bottom="+bottom);
                if(oldTop>0&&oldTop!=top){
                    if(oldTop<top){
                        dataBinding.animView.setVisibility(View.VISIBLE);
                        dataBinding.noAnimLayout.setVisibility(View.GONE);
                        LinearLayout.LayoutParams mLayoutParams= (LinearLayout.LayoutParams) dataBinding.tvTeamBuilder.getLayoutParams();
                        mLayoutParams.topMargin=0;
                    }
                }
            }
        });
    }
    private void setListenerFotEditText(View view){
        SoftKeyboardStateHelper softKeyboardStateHelper = new SoftKeyboardStateHelper(view);
        softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
                //键盘打开
            }

            @Override
            public void onSoftKeyboardClosed() {
                //键盘关闭
                dataBinding.tvTitle.setVisibility(View.VISIBLE);
                dataBinding.animView.setVisibility(View.VISIBLE);
                dataBinding.noAnimLayout.setVisibility(View.GONE);
                LinearLayout.LayoutParams mLayoutParams= (LinearLayout.LayoutParams) dataBinding.tvTeamBuilder.getLayoutParams();
                mLayoutParams.topMargin=0;
            }
        });
    }
    /**
     * input text listener
     */
    TextChangedListener mTextWatcher = new TextChangedListener() {
        @Override
        public void afterTextChanged(Editable editable) {
            String groupPwdcard=dataBinding.etTeam.getText().toString().trim();
            if(groupPwdcard.length()==6){
                mIsCreate = false;
                viewModel.joinTeam(groupPwdcard);
            }
        }
    };

    private boolean mIsCreate = false;
    private boolean isKeyBoardShowed=false;//这个界面键盘是否有显示过
    public void showSoftInputFromWindow(){
        if(!isKeyBoardShowed){
            if(!KeyBoardUtils.isSoftShowing(this)){
                isKeyBoardShowed=true;
                KeyBoardUtils.showKeyboard(dataBinding.etTeam);
            }
        }

    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.et_team:
                if(!TextUtils.isEmpty(TourApplication.getToken())){
                    dataBinding.tvTitle.setVisibility(View.INVISIBLE);
                    dataBinding.animView.setVisibility(View.GONE);
                    dataBinding.noAnimLayout.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams mLayoutParams= (LinearLayout.LayoutParams) dataBinding.tvTeamBuilder.getLayoutParams();
                    mLayoutParams.topMargin=getResources().getDimensionPixelOffset(R.dimen.dp_172);
                    showSoftInputFromWindow();
                }else {
                    showLoginDialog();
                }
                break;
            case R.id.tv_team_builder:
                if(!TextUtils.isEmpty(TourApplication.getToken())){
                    mIsCreate = true;
                    viewModel.createGroup();
                }else {
                    showLoginDialog();
                }
                break;
            default:
                break;
        }
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void loginEvent(LoginEvent event) {
        if(TourApplication.getToken()!=null){
            mIsCreate = false;
            viewModel.getSmartTeamGroup();
        }
    }
    private ConfirmDialog loginDialog=null;
    private void showLoginDialog(){
        if(loginDialog==null){
            loginDialog=new ConfirmDialog(this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginDialog.cancel();
                    if(v.getId()==R.id.confirm_tv){
                        //到登录页.
                        LoginActivity.start(SmartTeamActivity.this);
                    }
                }
            });
            loginDialog.setViewData("登录后，才可与队友实时对讲哦！","登录");
        }
        loginDialog.show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(KeyBoardUtils.isSoftShowing(this)){
            KeyBoardUtils.hideKeyBoard(this,dataBinding.etTeam);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}