package cn.xmzt.www.intercom.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import com.blankj.utilcode.util.ToastUtils;
import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.databinding.ActivityGroupMemberDelBinding;
import cn.xmzt.www.dialog.TextTitleDialog;
import cn.xmzt.www.intercom.adapter.GroupMemberDelAdapter;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.intercom.event.GroupManageEvent;
import cn.xmzt.www.intercom.vm.GroupMemberDelViewModel;
import cn.xmzt.www.view.listener.ItemListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * @describe 群成员管理移除成员
 */
public class GroupMemberDelActivity extends TourBaseActivity<GroupMemberDelViewModel, ActivityGroupMemberDelBinding> implements ItemListener {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_group_member_del;
    }

    @Override
    protected GroupMemberDelViewModel createViewModel() {
        viewModel = new GroupMemberDelViewModel();
        viewModel.result.observe(this, new Observer<BaseDataBean<Object>>() {
            @Override
            public void onChanged(@Nullable BaseDataBean<Object> result) {
                viewModel.getMemberList();
                EventBus.getDefault().post(new GroupManageEvent(2));//提示上一个页面刷新
            }
        });
        viewModel.memberList.observe(this, new Observer<List<GroupMemberBean>>() {
            @Override
            public void onChanged(@Nullable List<GroupMemberBean> result) {
                adapter.setDatas(result);
                if (adapter.getItemCount()>0) {
                    dataBinding.tvNoData.setVisibility(View.GONE);
                    dataBinding.bottomLayout.setVisibility(View.VISIBLE);
                    dataBinding.recyclerView.setVisibility(View.VISIBLE);
                } else {
                    dataBinding.tvNoData.setVisibility(View.VISIBLE);
                    dataBinding.bottomLayout.setVisibility(View.GONE);
                    dataBinding.recyclerView.setVisibility(View.GONE);
                }
            }
        });
        return viewModel;
    }
    GroupMemberDelAdapter adapter =null;
    GridLayoutManager manager=null;
    @Override
    protected void initData() {
        Intent intent=getIntent();
        viewModel.groupId=intent.getStringExtra("A");
        dataBinding.setActivity(this);
        manager= (GridLayoutManager) dataBinding.recyclerView.getLayoutManager();
        manager.setSpanCount(1);
        adapter = new GroupMemberDelAdapter();
        adapter.setItemListener(this);
        dataBinding.recyclerView.setAdapter(adapter);
        viewModel.getMemberList();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.all_CheckBox:
                //全选成员
                dataBinding.ivCheckBox.setSelected(!dataBinding.ivCheckBox.isSelected());
                adapter.selectAll(dataBinding.ivCheckBox.isSelected());
                break;
            case R.id.tv_remove:
                //移除选中成员
                String driverIds=adapter.getSelectIds();
                if(!TextUtils.isEmpty(driverIds)){
                    showHintDialog();
                }else {
                    ToastUtils.showShort("请先选择您要移除的成员");
                }
                break;
            default:
                break;
        }
    }
    private TextTitleDialog hintDialog;
    private void showHintDialog(){
        if(hintDialog==null){
            hintDialog = new TextTitleDialog(this, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hintDialog.dismiss();
                    //多个移除成员
                    String driverIds=adapter.getSelectIds();
                    viewModel.removeMembers(viewModel.groupId,driverIds);
                }
            });
        }
        String driverIds=adapter.getSelectIds();
        int size=driverIds.split(",").length;
        hintDialog.setTitle(" 确定移除这"+size+"位群成员？");
        hintDialog.show();
    }
    @Override
    public void onItemClick(View view, int position) {
        GroupMemberBean memberBean=adapter.getItem(position);
        switch (view.getId()) {
            case R.id.iv_edit:
                //编辑成员
                memberBean.setSelect(!memberBean.isSelect());
                adapter.notifyItemChanged(position);
                if(adapter.isSelectAll()){
                    dataBinding.ivCheckBox.setSelected(true);
                }else {
                    dataBinding.ivCheckBox.setSelected(false);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
