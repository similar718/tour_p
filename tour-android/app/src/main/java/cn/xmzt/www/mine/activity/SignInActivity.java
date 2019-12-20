package cn.xmzt.www.mine.activity;

import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.GridLayoutManager;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivitySigninBinding;
import cn.xmzt.www.mine.model.SignInViewModel;
import cn.xmzt.www.route.adapter.RouteTimePersonsAdapter;
import cn.xmzt.www.utils.StatusBarUtil;

/**
 * @author tanlei
 * @date 2019/8/23
 * @describe 签到
 */

public class SignInActivity extends TourBaseActivity<SignInViewModel,ActivitySigninBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_signin;
    }

    @Override
    protected SignInViewModel createViewModel() {
        return new SignInViewModel();
    }
    RouteTimePersonsAdapter adapter =null;
    GridLayoutManager manager=null;
    @Override
    protected void initData() {
//        StatusBarUtil.setStatusBarLightMode(this,false);
        StatusBarUtil.setStatusBarLightMode(this,false);

        int statusBarHeight = StatusBarUtil.getStatusBarHeight(this);
        FrameLayout.LayoutParams mLayoutParams= (FrameLayout.LayoutParams) dataBinding.titleLayout.getLayoutParams();
        mLayoutParams.topMargin=statusBarHeight;
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
        adapter = new RouteTimePersonsAdapter();
        dataBinding.recyclerView.setAdapter(adapter);
        manager= (GridLayoutManager) dataBinding.recyclerView.getLayoutManager();
        manager.setSpanCount(7);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                boolean isSingleShow=adapter.isSingleShow(position);
                return isSingleShow ? manager.getSpanCount() : 1;
            }
        });
    }
}
