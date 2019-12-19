package cn.xmzt.www.selfdrivingtools.activity;

import androidx.lifecycle.Observer;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ActivityScenicFeedBackBinding;
import cn.xmzt.www.dialog.ScenicSpotListDialog;
import cn.xmzt.www.selfdrivingtools.adapter.SuggestAndFeedBackContentAdapter;
import cn.xmzt.www.selfdrivingtools.adapter.SuggestAndFeedBackNaviAdapter;
import cn.xmzt.www.selfdrivingtools.bean.ScenicFeedBackTypeBean;
import cn.xmzt.www.selfdrivingtools.bean.SuggestAndFeedBackNaviBean;
import cn.xmzt.www.selfdrivingtools.viewmodel.ScenicFeedBackViewModel;
import cn.xmzt.www.utils.LocalDataUtils;
import cn.xmzt.www.utils.MatcherUtils;
import cn.xmzt.www.utils.ToastUtils;
import cn.xmzt.www.view.listener.TextChangedListener;

import java.util.ArrayList;
import java.util.List;

public class ScenicFeedBackActivity extends TourBaseActivity<ScenicFeedBackViewModel, ActivityScenicFeedBackBinding> {

    SuggestAndFeedBackNaviAdapter mNaviAdapter = null;
    SuggestAndFeedBackContentAdapter mContentAdapter = null;
    // 获取得到的数据
    List<ScenicFeedBackTypeBean> mTypeData = null;
    // 当前类型
    List<SuggestAndFeedBackNaviBean> mNaviData = null;
    // 当前底部内容
    List<ScenicFeedBackTypeBean.ChildrenBean> mContentData = null;

    List<ScenicFeedBackTypeBean> mTypeDatas = null;
    long mScenicId = 0;
    String scenicStr = "";
    int mQuestionType = 0; // 0 地图问题 1 语音问题 2 功能问题 3 其他问题

    private ArrayList<String> list = new ArrayList<>();

    private ScenicSpotListDialog mDialog;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_scenic_feed_back;
    }

    @Override
    protected ScenicFeedBackViewModel createViewModel() {
        viewModel = new ScenicFeedBackViewModel();
        viewModel.setIView(this);
        viewModel.mScenicFeedBack.observe(this, new Observer<List<ScenicFeedBackTypeBean>>() {
            @Override
            public void onChanged(@Nullable List<ScenicFeedBackTypeBean> scenicFeedBackTypeBeans) {
                mTypeData = scenicFeedBackTypeBeans;
                setData();
            }
        });
        viewModel.mSave.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String scenicFeedBackTypeBeans) {
                // 表示提交成功  更新界面
                dataBinding.llContent.setVisibility(View.GONE);
                dataBinding.ivCustomServer.setVisibility(View.GONE);
                dataBinding.rlSubmit.setVisibility(View.VISIBLE);
                // 3秒钟后跳转
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 3000);//3秒后执行Runnable中的run方法
            }
        });
        return viewModel;
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        dataBinding.setActivity(this);
        // 获取传递过来的景区id 或者不用传  直接选择
        mScenicId = getIntent().getLongExtra("scenicid",0);
        scenicStr = getIntent().getStringExtra("scenicStr");
        list = getIntent().getStringArrayListExtra("list");

        mTypeDatas = LocalDataUtils.getQuestDetail();

        if ("".equals(TourApplication.getToken()) || null == TourApplication.getToken()){
            ToastUtils.showText(this,"使用当前功能需要登陆账号");
        }

        viewModel.getScenicFeedBack();

        dataBinding.feekBackTypeRv.setLayoutManager(new GridLayoutManager(this,4));
        mNaviAdapter = new SuggestAndFeedBackNaviAdapter(this);
        mNaviData = viewModel.getNaviTypeData();
        mNaviAdapter.setDatas(mNaviData);
        dataBinding.feekBackTypeRv.setAdapter(mNaviAdapter);

        dataBinding.rvContent.setLayoutManager(new GridLayoutManager(this,2));
        mContentAdapter = new SuggestAndFeedBackContentAdapter(this);
        dataBinding.rvContent.setAdapter(mContentAdapter);

        dataBinding.tvScenicSpotName.setText(scenicStr);

        dataBinding.etTel.addTextChangedListener(mTextWatcher);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case cn.xmzt.www.R.id.feed_back_iv:// 返回
                finish();
                break;
            case cn.xmzt.www.R.id.iv_submit:// 提交
                onSubmit();
                break;
            case cn.xmzt.www.R.id.tv_to_main:// 立即跳转
                finish();
                break;
            case cn.xmzt.www.R.id.iv_custom_server:// 拨打客服
                callPhone(Constants.CLIENTTELNUM);
                break;
            case cn.xmzt.www.R.id.iv_tel_delete:// 清除电话号码
                dataBinding.etTel.setText("");
                break;
            case cn.xmzt.www.R.id.rl_question_scenic_spot:// 景点选择
                setScenicSpotDialog();
                break;
            default:
                break;
        }
    }

    private void setScenicSpotDialog(){
        if (mDialog == null){
            mDialog = new ScenicSpotListDialog(ScenicFeedBackActivity.this, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }
        mDialog.setViewData(list);
        mDialog.show();
    }

    public void OnTypeClickListener(int position){
        String data = list.get(position);
        dataBinding.tvScenicSpotName.setText(scenicStr+"-"+data);
        if (mDialog.isShowing()){
            mDialog.dismiss();
        }
    }

    /**
     * 拨打电话（直接拨打电话）
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum){
        Intent intent = new Intent(Intent.ACTION_DIAL);//ACTION_DIAL ACTION_CALL TODO
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    private void setData(){
        if (mTypeData == null) {
            mTypeData = mTypeDatas;
        }
        if (mTypeData.size()==0) {
            mTypeData = mTypeDatas;
        }
        mQuestionType = 0;
        // 默认的是第一个地图问题
        mContentData = mTypeData.get(mQuestionType).getChildren();
        mContentAdapter.setDatas(mContentData);
    }

    /**
     * 类型的点击事件 0 地图 1 语音 2 功能 3 其他
     * @param position
     * @param tag 1 类型点击 2 内容点击
     */
    public void OnNaviClickListener(int position,int tag){
        if (mTypeData == null) {
            mTypeData = mTypeDatas;
        }
        if (mTypeData.size() == 0){
            mTypeData = mTypeDatas;
        }
        if (tag == 1) {
            mQuestionType = position;
            // 更新内容
            mContentData = mTypeData.get(mQuestionType).getChildren();
            mContentAdapter.setDatas(mContentData);
            // 更新类型
            for (int i = 0 ; i<mNaviData.size();i++){
                if (i == position){
                    mNaviData.get(i).setIs_Checked(true);
                } else {
                    mNaviData.get(i).setIs_Checked(false);
                }
            }
            mNaviAdapter.setDatas(mNaviData);
            if(position == 0){
                dataBinding.rlQuestionScenicSpot.setVisibility(View.VISIBLE);
                dataBinding.tvQues.setVisibility(View.VISIBLE);
            } else {
                dataBinding.rlQuestionScenicSpot.setVisibility(View.GONE);
                dataBinding.tvQues.setVisibility(View.GONE);
            }

        } else {
            // 底部内容区的更新
            mContentData.get(position).setIs_Checked(!mContentData.get(position).isIs_Checked());
            mContentAdapter.setDatas(mContentData);
        }
    }

    /**
     * 除了手机号其他均为必填
     */
    private void onSubmit(){
        String token = TourApplication.getToken();
        if ("".equals(token) || null == token){
            ToastUtils.showText(this,"使用当前功能需要登陆账号");
            return;
        }
        String bigClass = mQuestionType == 0 ? "地图问题":mQuestionType == 1 ? "语音问题" : mQuestionType == 2 ? "功能问题" : "其他问题";
        String description = dataBinding.etDesc.getText().toString().trim();
        if ("".equals(description) || null == description){
            ToastUtils.showText(ScenicFeedBackActivity.this,"请补充描述您的问题");
            return;
        }
        String data = dataBinding.tvScenicSpotName.getText().toString().trim();
        if (!data.contains("-") && mQuestionType == 0){
            ToastUtils.showText(ScenicFeedBackActivity.this,"请选择景点");
            return;
        }
        String phone = dataBinding.etTel.getText().toString().trim();
        StringBuilder smallClass = new StringBuilder();
        for (int i = 0;i < mContentData.size(); i++){
            if (mContentData.get(i).isIs_Checked())
                smallClass.append(mContentData.get(i).getDes() + ",");
        }
        if (smallClass.toString().length()<1) {
            ToastUtils.showText(ScenicFeedBackActivity.this,"请选择当前现状");
            return;
        }
        if (!TextUtils.isEmpty(phone)){ // 如果填写的手机号不为空就需要判断格式
            if (!MatcherUtils.isMobile(phone)){
                ToastUtils.showText(ScenicFeedBackActivity.this,"请输入正确的手机号码");
                return;
            }
        }
        viewModel.getScenicFeedBackSave(bigClass,description,phone,mScenicId,smallClass.toString(),token);
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
            String data = dataBinding.etTel.getText().toString().trim();
            // 进行过滤
            if (!"".equals(data) && data != null)
                dataBinding.ivTelDelete.setVisibility(View.VISIBLE);
            else {
                dataBinding.ivTelDelete.setVisibility(View.INVISIBLE);
            }
        }
    };

}