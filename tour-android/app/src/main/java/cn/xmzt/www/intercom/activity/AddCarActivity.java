package cn.xmzt.www.intercom.activity;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.databinding.ActivityAddCarBinding;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.intercom.event.GroupManageEvent;
import cn.xmzt.www.intercom.vm.AddCarViewModel;
import cn.xmzt.www.utils.KeyboardUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

/**
 * @describe 添加车辆
 */
public class AddCarActivity extends TourBaseActivity<AddCarViewModel, ActivityAddCarBinding> {
    private KeyboardUtil keyboardUtil;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_add_car;
    }

    @Override
    protected AddCarViewModel createViewModel() {
        viewModel = new AddCarViewModel();
        viewModel.result.observe(this, new Observer<BaseDataBean<String>>() {
            @Override
            public void onChanged(@Nullable BaseDataBean<String> result) {
                if (driverBean != null) {
                    driverBean.setLicencePlate(viewModel.licencePlate);
                    driverBean.setDriver(true);
                    driverBean.setLicencePlate(viewModel.licencePlate);
                    EventBus.getDefault().post(new GroupManageEvent(1, driverBean));//提示上一个页面刷新
                    onBackPressed();
                }
            }
        });
        return viewModel;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        viewModel.groupId = intent.getStringExtra("A");
        EventBus.getDefault().register(this);
        dataBinding.setActivity(this);
        dataBinding.setVm(viewModel);

        dataBinding.etCarID.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (keyboardUtil == null) {
                    keyboardUtil = new KeyboardUtil(AddCarActivity.this, dataBinding.etCarID);
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

    private GroupMemberBean driverBean;

    @Subscribe
    public void onSelectDriver(GroupMemberBean driverBean) {
        this.driverBean = driverBean;
        viewModel.userId = driverBean.getUserId();
        dataBinding.tvDriver.setText(driverBean.getNickname());
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_save:
                //添加车辆
                viewModel.addDriver();
                break;
            case R.id.layout1:
                //选择司机
                Intent intent = new Intent(this, SelectDriverActivity.class);
                intent.putExtra("A", viewModel.groupId);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
