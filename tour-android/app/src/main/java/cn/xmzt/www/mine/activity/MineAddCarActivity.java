package cn.xmzt.www.mine.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import androidx.lifecycle.Observer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityMineAddCarBinding;
import cn.xmzt.www.dialog.TextTitleDialog;
import cn.xmzt.www.mine.event.UpdateVehicleEvent;
import cn.xmzt.www.mine.model.MineAddCarViewModel;
import cn.xmzt.www.utils.KeyboardUtil;
import cn.xmzt.www.utils.MatcherUtils;
import cn.xmzt.www.utils.ToastUtils;

public class MineAddCarActivity extends TourBaseActivity<MineAddCarViewModel, ActivityMineAddCarBinding> {
    private KeyboardUtil keyboardUtil;
    public String licencePlate;
    private boolean mIsDefault = false;
    private boolean mIsDefaultOld = false;

    private String oldLicencePlate = "";//旧产车牌
    private String mToken;
    private int mId = -1 ;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_mine_add_car;
    }

    @Override
    protected MineAddCarViewModel createViewModel() {
        viewModel = new MineAddCarViewModel();
        viewModel.setIView(this);

        viewModel.mVehicleInfo.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                // 添加成功
                String title = "";
                if (mId == -1){
                    title = "添加车辆成功";
                    // 刷新上一个界面
                    EventBus.getDefault().post(new UpdateVehicleEvent(1));
                } else {
                    title = "修改车辆成功";
                    // 刷新上一个界面
                    EventBus.getDefault().post(new UpdateVehicleEvent(5,oldLicencePlate,licencePlate));
                }
                ToastUtils.showText(MineAddCarActivity.this,title);
                onBackPressed();
            }
        });
        viewModel.mVehicleDelInfo.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ToastUtils.showText(MineAddCarActivity.this,"删除车辆信息成功");
                // 刷新上一个界面
                EventBus.getDefault().post(new UpdateVehicleEvent(4,oldLicencePlate,null));
                onBackPressed();
            }
        });
        return viewModel;
    }
    @Override
    protected void initData() {
        dataBinding.setActivity(this);
        dataBinding.setVm(viewModel);
        EventBus.getDefault().register(this);

        mToken = TourApplication.getToken();

        dataBinding.etCarID.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (keyboardUtil == null) {
                    keyboardUtil = new KeyboardUtil(MineAddCarActivity.this, dataBinding.etCarID);
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

        oldLicencePlate = getIntent().getStringExtra("car");
        mId = getIntent().getIntExtra("id",-1);
        mIsDefault = getIntent().getBooleanExtra("default",false);
        mIsDefaultOld = mIsDefault;

        if (TextUtils.isEmpty(oldLicencePlate)){
            // 添加车辆的布局显示
            dataBinding.ivSetDefault.setImageResource(R.drawable.icon_group_off);
            dataBinding.tvDel.setVisibility(View.GONE);
            dataBinding.btnConfirm.setBackgroundResource(R.drawable.icon_confirm_add_disable);
            dataBinding.btnConfirm.setEnabled(false);
            dataBinding.tvTitle.setText("添加车辆");
        } else {
            // 修改车辆的布局显示
            if (mIsDefault){
                dataBinding.ivSetDefault.setImageResource(R.drawable.icon_group_on);
            } else {
                dataBinding.ivSetDefault.setImageResource(R.drawable.icon_group_off);
            }
            dataBinding.btnConfirm.setEnabled(false);
            dataBinding.tvDel.setVisibility(View.VISIBLE);
            dataBinding.etCarID.setText(oldLicencePlate);
            dataBinding.btnConfirm.setBackgroundResource(R.drawable.icon_confirm_update_disable);
            dataBinding.tvTitle.setText("编辑车辆");
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_confirm:
                //添加车辆
                if (TextUtils.isEmpty(oldLicencePlate)) {
                    // 添加
                    viewModel.getsysUserPlateNumberSave(mToken,licencePlate,null,mIsDefault ? 1 : 0);
                } else {
                    // 修改
                    viewModel.getsysUserPlateNumberSave(mToken,TextUtils.isEmpty(licencePlate) ? oldLicencePlate : licencePlate,mId,mIsDefault ? 1 : 0);
                }
                break;
            case R.id.tv_del:
                showDelDialog();
                break;
            case R.id.iv_set_default:
                if (mIsDefault){
                    mIsDefault = false;
                    dataBinding.ivSetDefault.setImageResource(R.drawable.icon_group_off);
                } else {
                    mIsDefault = true;
                    dataBinding.ivSetDefault.setImageResource(R.drawable.icon_group_on);
                }
                if (licencePlate == null && oldLicencePlate != null){
                    if (mIsDefault != mIsDefaultOld){
                        dataBinding.btnConfirm.setEnabled(true);
                        dataBinding.btnConfirm.setBackgroundResource(R.drawable.icon_confirm_update);
                    }
                }
                break;
            default:
                break;
        }
    }

    public void carIDAfterTextChanged(Editable s) {
        licencePlate = s.toString();
        dataBinding.btnConfirm.setEnabled(false);
        if (MatcherUtils.isCarCard(licencePlate)) {
            if (TextUtils.isEmpty(oldLicencePlate)) {
                //新增车牌
                dataBinding.btnConfirm.setEnabled(true);
                dataBinding.btnConfirm.setBackgroundResource(R.drawable.icon_confirm_add);
            } else {
                //修改车牌
                if (oldLicencePlate.equals(licencePlate)) {
                    //相同的车牌不用修改
                    dataBinding.btnConfirm.setBackgroundResource(R.drawable.icon_confirm_update_disable);
                } else {
                    dataBinding.btnConfirm.setEnabled(true);
                    dataBinding.btnConfirm.setBackgroundResource(R.drawable.icon_confirm_update);
                }
            }
        } else {
            if (TextUtils.isEmpty(oldLicencePlate)) {
                //新增车牌
                dataBinding.btnConfirm.setBackgroundResource(R.drawable.icon_confirm_add_disable);
            } else {
                //修改车牌
                dataBinding.btnConfirm.setBackgroundResource(R.drawable.icon_confirm_update_disable);
            }
        }
    }

    private TextTitleDialog delDialog;

    private void showDelDialog() {
        if (delDialog == null) {
            delDialog = new TextTitleDialog(this, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delDialog.dismiss();
                    viewModel.getsysUserPlateNumberDelete(mToken,mId);
                }
            });
        }
        delDialog.setTitle("是否确认删除此车辆，删除后相关行程的车辆信息将会被清空!");
        delDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getOrderType(UpdateVehicleEvent event) {

    }
}
