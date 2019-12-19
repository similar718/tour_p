package cn.xmzt.www.home.activity;

import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.databinding.ActivityCustomizeInfoBinding;
import cn.xmzt.www.dialog.TextTitleDialog;
import cn.xmzt.www.home.event.CustomizeRefreshBus;
import cn.xmzt.www.home.vm.CustomizeInfoViewModel;
import cn.xmzt.www.utils.DensityUtil;
import cn.xmzt.www.utils.StatusBarUtil;

/**
 * @describe 私人定制详情
 */
public class CustomizeInfoActivity extends TourBaseActivity<CustomizeInfoViewModel, ActivityCustomizeInfoBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_customize_info;
    }

    @Override
    protected CustomizeInfoViewModel createViewModel() {
        viewModel = new CustomizeInfoViewModel(dataBinding);
        viewModel.result.observe(this, new Observer<BaseDataBean<Object>>() {
            @Override
            public void onChanged(@Nullable BaseDataBean<Object> result) {
                onBackPressed();
            }
        });
        return viewModel;
    }

    @Override
    protected void initData() {
        viewModel.customizeId=getIntent().getIntExtra("A",0);
        EventBus.getDefault().register(this);
        dataBinding.setActivity(this);
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarLightMode(this,false);
        FrameLayout.LayoutParams listParams = (FrameLayout.LayoutParams) dataBinding.titleLayout.getLayoutParams();
        listParams.topMargin = StatusBarUtil.getStatusBarHeight(getApplicationContext());
        // 滑动事件监听
        dataBinding.scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            int lastScrollY = 0;
            int h = DensityUtil.dip2px(CustomizeInfoActivity.this,156);
            int color = ContextCompat.getColor(CustomizeInfoActivity.this, R.color.color_24A4FF) & 0x00ffffff;
            int mScrollY = 0;
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (lastScrollY < h) {
                    scrollY = Math.min(h, scrollY);
                    mScrollY = scrollY > h ? h : scrollY;
                    dataBinding.titleBarLayout.setBackgroundColor(((255 * mScrollY / h) << 24) | color);
                }
                float percent = mScrollY / h;
                if (percent >= 1) {
                    percent = 1;
                }
                if (percent <= 0.1) {
                    percent = 0;
                }
                if (percent > 0.5) {

                } else {
                }
                lastScrollY = scrollY;
            }
        });
        viewModel.getCustomizeInfo();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back: // 返回
                onBackPressed();
                break;
            case R.id.tv_del: // 删除定制
                showDelDialog();
                break;
            case R.id.tv_modify:
                // 修改定制
                if(viewModel.customizeForm!=null){
                    startToActivity(CustomizeEditActivity.class);
                    EventBus.getDefault().postSticky(viewModel.customizeForm);
                }

                break;
            default:
                break;
        }
    }
    @Subscribe
    public void refreshBus(@NonNull CustomizeRefreshBus refresh) {
        if(refresh.getType()==CustomizeRefreshBus.TYPE_DEL){
            finish();
        }else {
            if (viewModel != null) {
                viewModel.getCustomizeInfo();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    private TextTitleDialog delDialog;

    private void showDelDialog() {
        delDialog = new TextTitleDialog(this, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delDialog.dismiss();
                viewModel.deleteCustomize();
            }
        });
        delDialog.setViewData("确定删除定制记录？","我再想想","删除");
        delDialog.setBtnBackground(R.drawable.shape_gray_f4_radius_4,R.drawable.shape_blue_radius_4);
        delDialog.setBtnTextColor(Color.parseColor("#24A4FF"),Color.parseColor("#FFFFFF"));
        delDialog.show();
    }
}
