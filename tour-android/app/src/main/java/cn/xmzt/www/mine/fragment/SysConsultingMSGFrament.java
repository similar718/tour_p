package cn.xmzt.www.mine.fragment;


import androidx.fragment.app.Fragment;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseFragment;
import cn.xmzt.www.databinding.FragmentConsultingMsgBinding;
import cn.xmzt.www.mine.model.SysMsgViewModel;
import cn.xmzt.www.view.listener.ItemListener;

/**
 * 系统咨询服务消息
 */
public class SysConsultingMSGFrament extends BaseFragment<SysMsgViewModel, FragmentConsultingMsgBinding> implements ItemListener {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_consulting_msg;
    }

    @Override
    protected SysMsgViewModel createViewModel() {
        viewModel = new SysMsgViewModel();
        return viewModel;
    }
    @Override
    protected void initData() {
    }
    @Override
    public void onItemClick(View view, int position) {


    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }
    @Override
    public void showLoadFail(String msg) {
        super.showLoadFail(msg);
    }
}
