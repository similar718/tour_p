package cn.xmzt.www.intercom.activity;

import androidx.lifecycle.Observer;
import android.content.Context;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ActivityGroupEditorBinding;
import cn.xmzt.www.intercom.event.GroupManageEvent;
import cn.xmzt.www.intercom.vm.GroupEditorViewModel;
import cn.xmzt.www.utils.StatusBarUtil;
import cn.xmzt.www.utils.ToastUtils;
import cn.xmzt.www.view.listener.TextChangedListener;

import org.greenrobot.eventbus.EventBus;

public class GroupEditorActivity extends TourBaseActivity<GroupEditorViewModel, ActivityGroupEditorBinding> {

    private int topType = 0; // 传递编辑顶部的名称  或者类型  1 群介绍 2 我在群的昵称 3 群聊名称
    private boolean isEdit = true; //传递过来是否是编辑状态 true 编辑 false 查看
    private String content = ""; //传递过来的数据内容
    private String mGroupId = "";// 群id

    private Context mContext;

    private int mTextMaxNum = 30;

    private int mIntroTextMaxNum = 100;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_group_editor;
    }

    @Override
    protected GroupEditorViewModel createViewModel() {
        viewModel = new GroupEditorViewModel();
        viewModel.setIView(this);
        viewModel.mSetGroupInfo.observe(this, new Observer<Object>() {
            @Override
            public void onChanged(@Nullable Object info) {
                if (Constants.OK.equals(info)){
                    // setting destination setting time interface return info
                    ToastUtils.showText(mContext,"设置成功");
                    EventBus.getDefault().post(new GroupManageEvent(22));//提示上一个页面刷新
                    finish();
                }
            }
        });
        viewModel.mSetNickname.observe(this, new Observer<Object>() {
            @Override
            public void onChanged(@Nullable Object info) {
                if (Constants.OK.equals(info)){
                    // setting destination setting time interface return info
                    ToastUtils.showText(mContext,"设置成功");
                    EventBus.getDefault().post(new GroupManageEvent(23));//提示上一个页面刷新
                    finish();
                }
            }
        });
        return viewModel;
    }

    @Override
    protected void initData() {
        mContext = this;
        StatusBarUtil.setStatusBarLightMode(this,true);
        dataBinding.setModel(viewModel);
        dataBinding.setActivity(this);
        topType = getIntent().getIntExtra("topType",0);
        viewModel.menderName = getIntent().getStringExtra("menderName");
        isEdit = getIntent().getBooleanExtra("isEdit",true);
        content = getIntent().getStringExtra("content");
        mGroupId = getIntent().getStringExtra("groupId");
        if (topType == 0 ){
            ToastUtils.showText(mContext,"传递信息错误");
            return;
        }
        // 初始化顶部信息
        initTopData();
        // 初始化内容数据底部显示问题
        initContentData();
    }



    public void onClick(View view) {
        switch (view.getId()) {
            case cn.xmzt.www.R.id.iv_back:
                finish();
                break;
            case cn.xmzt.www.R.id.tv_top_save: //保存
                if (topType == 1){ // 100 群介绍
                    String data = dataBinding.etGroupIntroE.getText().toString().trim();
                    if (!"".equals(data) && data != null) {
                        viewModel.getUpdateGroupTitleAdIntro(TourApplication.getToken(),mGroupId,data,"");
                    }
                } else if (topType == 2){ // 群昵称
                    String data = dataBinding.etGroupName.getText().toString().trim();
                    if (!"".equals(data) && data != null) {
                        viewModel.getUpdateGroupNickname(TourApplication.getToken(),mGroupId,data);
                    }
                } else if (topType == 3){ // 群名称
                    String data = dataBinding.etGroupName.getText().toString().trim();
                    if (!TextUtils.isEmpty(data)) {
                        viewModel.getUpdateGroupTitleAdIntro(TourApplication.getToken(),mGroupId,"",data);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void initTopData(){
        switch (topType){
            case 1 :
                dataBinding.tvTopContent.setText("群介绍");
                if (isEdit){
                    dataBinding.tvTopSave.setVisibility(View.VISIBLE);
                } else {
                    dataBinding.tvTopSave.setVisibility(View.GONE);
                }
                break;
            case 2 :
                dataBinding.tvTopContent.setText("群昵称");
                dataBinding.tvTopSave.setVisibility(View.VISIBLE);
                break;
            case 3 :
                dataBinding.tvTopContent.setText("群名称");
                dataBinding.tvTopSave.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void initContentData(){
        switch (topType){
            case 1 :
                if (isEdit) {
                    dataBinding.llGroupIntroE.setVisibility(View.VISIBLE);
                    dataBinding.llGroupName.setVisibility(View.GONE);
                    dataBinding.rlGroupIntroS.setVisibility(View.GONE);
                    if (!"".equals(content) && null != content){
                        dataBinding.etGroupIntroE.setText(content);
                    }
                    dataBinding.etGroupIntroE.addTextChangedListener(mTextWatcher);
                } else {
                    dataBinding.llGroupIntroE.setVisibility(View.GONE);
                    dataBinding.llGroupName.setVisibility(View.GONE);
                    dataBinding.rlGroupIntroS.setVisibility(View.VISIBLE);
                    if (!"".equals(content) && null != content){
                        dataBinding.tvGroupIntroS.setText(content);
                    }
                }
                break;
            case 2 :
                dataBinding.llGroupIntroE.setVisibility(View.GONE);
                dataBinding.llGroupName.setVisibility(View.VISIBLE);
                dataBinding.rlGroupIntroS.setVisibility(View.GONE);
                dataBinding.tvShow.setText("群内昵称");
                dataBinding.etGroupName.setHint("请输入30字以内群内昵称");
                if (!"".equals(content) && null != content){
                    dataBinding.etGroupName.setText(content);
                }
                dataBinding.etGroupName.addTextChangedListener(mTextWatcher);
                break;
            case 3 :
                dataBinding.llGroupIntroE.setVisibility(View.GONE);
                dataBinding.llGroupName.setVisibility(View.VISIBLE);
                dataBinding.rlGroupIntroS.setVisibility(View.GONE);
                dataBinding.tvShow.setText("群聊名称");
                dataBinding.etGroupName.setHint("请输入30字以内群名称");
                if (!"".equals(content) && null != content){
                    dataBinding.etGroupName.setText(content);
                }
                dataBinding.etGroupName.addTextChangedListener(mTextWatcher);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setEmptyData();
    }

    private void setEmptyData(){
        content = "";
        isEdit = true;
        topType = 0;
        dataBinding.etGroupName.setText("");
        dataBinding.etGroupIntroE.setText("");
    }

    /**
     * 输入文字的监听事件
     */
    TextChangedListener mTextWatcher = new TextChangedListener() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (topType == 1){ // 100 群介绍
                String data = dataBinding.etGroupIntroE.getText().toString().trim();
                // 进行过滤
                if (!"".equals(data) && data != null) {
                    dataBinding.tvTopSave.setBackground(getResources().getDrawable(R.drawable.group_editor_save_blue_bg));
                    if (data.length() > mIntroTextMaxNum){
                        dataBinding.etGroupIntroE.setText(data.substring(0,mIntroTextMaxNum));
                        ToastUtils.showText(mContext,"您输入已经超过限制字数");
                    }
                } else {
                    dataBinding.tvTopSave.setBackground(getResources().getDrawable(R.drawable.group_editor_save_gray_bg));
                }
            } else { // 群昵称 群名称
                String data = dataBinding.etGroupName.getText().toString().trim();
                // 进行过滤
                if (!"".equals(data) && data != null) {
                    dataBinding.tvTopSave.setBackground(getResources().getDrawable(R.drawable.group_editor_save_blue_bg));
                    if (data.length() > mTextMaxNum){
                        dataBinding.etGroupName.setText(data.substring(0,mTextMaxNum));
                        ToastUtils.showText(mContext,"您输入已经超过限制字数");
                    }
                } else {
                    dataBinding.tvTopSave.setBackground(getResources().getDrawable(R.drawable.group_editor_save_gray_bg));
                }
            }
        }
    };
}
