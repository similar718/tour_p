package cn.xmzt.www.smartteam.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import org.greenrobot.eventbus.EventBus;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.databinding.ActivitySmartTeamAddCarBinding;
import cn.xmzt.www.dialog.TextTitleDialog;
import cn.xmzt.www.intercom.event.GroupManageEvent;
import cn.xmzt.www.smartteam.vm.SmartTeamAddCarViewModel;
import cn.xmzt.www.utils.KeyboardUtil;
import cn.xmzt.www.utils.MatcherUtils;

/**
 * @describe 智能组队添加车辆/智能组队修改车辆/删除车辆
 */
public class SmartTeamAddCarActivity extends TourBaseActivity<SmartTeamAddCarViewModel, ActivitySmartTeamAddCarBinding> {
    private KeyboardUtil keyboardUtil;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_smart_team_add_car;
    }

    @Override
    protected SmartTeamAddCarViewModel createViewModel() {
        viewModel = new SmartTeamAddCarViewModel();
        viewModel.result.observe(this, new Observer<BaseDataBean<String>>() {
            @Override
            public void onChanged(@Nullable BaseDataBean<String> result) {
                EventBus.getDefault().post(new GroupManageEvent(1));//提示上一个页面刷新
                onBackPressed();
            }
        });
        return viewModel;
    }
    private String oldLicencePlate="";//旧产车牌
    @Override
    protected void initData() {
        Intent intent = getIntent();
        viewModel.groupId = intent.getStringExtra("A");
        viewModel.licencePlate = intent.getStringExtra("B");
        oldLicencePlate = intent.getStringExtra("B");
        if(TextUtils.isEmpty(oldLicencePlate)){
            dataBinding.tvDel.setVisibility(View.GONE);
            dataBinding.btnConfirm.setBackgroundResource(R.drawable.icon_confirm_add_disable);
            dataBinding.btnConfirm.setEnabled(false);
        }else {
            dataBinding.btnConfirm.setEnabled(false);
            dataBinding.tvDel.setVisibility(View.VISIBLE);
            dataBinding.etCarID.setText(viewModel.licencePlate);
            dataBinding.btnConfirm.setBackgroundResource(R.drawable.icon_confirm_update_disable);
        }
        dataBinding.setActivity(this);
        dataBinding.setVm(viewModel);

        dataBinding.etCarID.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (keyboardUtil == null) {
                    keyboardUtil = new KeyboardUtil(SmartTeamAddCarActivity.this, dataBinding.etCarID);
                    keyboardUtil.hideSoftInputMethod();
                    keyboardUtil.showKeyboard();
                } else {
                    keyboardUtil.showKeyboard();
                }
                return false;
            }
        });
        dataBinding.etCarID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    keyboardUtil.showKeyboard();
                } else {
                    keyboardUtil.hideKeyboard();
                }
            }
        });
    }



    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_confirm:
                //添加车辆
                if(TextUtils.isEmpty(oldLicencePlate)){
                    viewModel.smartTeamAddOrUpdtOrDelDriver(1);
                }else {
                    viewModel.smartTeamAddOrUpdtOrDelDriver(2);
                }
                break;
            case R.id.tv_del:
                showDelDialog();
                break;
            default:
                break;
        }
    }
    public void carIDAfterTextChanged(Editable s) {
        viewModel.licencePlate = s.toString();
        dataBinding.btnConfirm.setEnabled(false);
        if(MatcherUtils.isCarCard(viewModel.licencePlate)){
            if(TextUtils.isEmpty(oldLicencePlate)){
                //新增车牌
                dataBinding.btnConfirm.setEnabled(true);
                dataBinding.btnConfirm.setBackgroundResource(R.drawable.icon_confirm_add);
            }else {
                //修改车牌
                if(oldLicencePlate.equals(viewModel.licencePlate)){
                    //相同的车牌不用修改
                    dataBinding.btnConfirm.setBackgroundResource(R.drawable.icon_confirm_update_disable);
                }else {
                    dataBinding.btnConfirm.setEnabled(true);
                    dataBinding.btnConfirm.setBackgroundResource(R.drawable.icon_confirm_update);
                }
            }
        }else {
            if(TextUtils.isEmpty(oldLicencePlate)){
                //新增车牌
                dataBinding.btnConfirm.setBackgroundResource(R.drawable.icon_confirm_add_disable);
            }else {
                //修改车牌
                dataBinding.btnConfirm.setBackgroundResource(R.drawable.icon_confirm_update_disable);
            }
        }
    }
    private TextTitleDialog delDialog;
    private void showDelDialog(){
        if(delDialog==null){
            delDialog = new TextTitleDialog(this, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delDialog.dismiss();
                    viewModel.smartTeamAddOrUpdtOrDelDriver(3);
                }
            });
        }
        delDialog.setTitle("是否确认删除车辆信息，删除后将无法在地图上展示车辆信息!");
        delDialog.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
