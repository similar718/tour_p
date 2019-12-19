package cn.xmzt.www.route.activity;

import androidx.lifecycle.Observer;
import android.content.Intent;
import androidx.annotation.Nullable;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.bean.InvoiceTitleBean;
import cn.xmzt.www.bean.TypeEventBus;
import cn.xmzt.www.databinding.ActivityAddInvoiceTitleBinding;
import cn.xmzt.www.dialog.InvoiceTypeDialog;
import cn.xmzt.www.route.vm.AddInvoiceTitleViewModel;

import org.greenrobot.eventbus.EventBus;

/**
 * @describe 选择发票抬头
 */
public class AddInvoiceTitleActivity extends TourBaseActivity<AddInvoiceTitleViewModel, ActivityAddInvoiceTitleBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_add_invoice_title;
    }
    @Override
    protected AddInvoiceTitleViewModel createViewModel() {
        viewModel = new AddInvoiceTitleViewModel();
        viewModel.result.observe(this, new Observer<BaseDataBean<String>>() {
            @Override
            public void onChanged(@Nullable BaseDataBean<String> result) {
                EventBus.getDefault().post(new TypeEventBus(TypeEventBus.TYPE_REFRESH));//提示上一个页面刷新
                onBackPressed();
            }
        });
        return viewModel;
    }
    private InvoiceTitleBean mInvoiceTitleBean=null;
    @Override
    protected void initData() {
        Intent intent=getIntent();
        mInvoiceTitleBean= (InvoiceTitleBean) intent.getSerializableExtra("A");
        dataBinding.setActivity(this);
        dataBinding.setVm(viewModel);
        if(mInvoiceTitleBean!=null){
            dataBinding.tvDel.setVisibility(View.VISIBLE);
            viewModel.selected.set(mInvoiceTitleBean.getTitleType());

            dataBinding.etCompanyName.setText(mInvoiceTitleBean.getTitleName());
            viewModel.companyName=mInvoiceTitleBean.getTitleName();
            dataBinding.etCompanyName.setSelection(mInvoiceTitleBean.getTitleName().length());

            dataBinding.etDutyNum.setText(mInvoiceTitleBean.getDutyParagraph());
            viewModel.dutyId=mInvoiceTitleBean.getDutyParagraph();

            dataBinding.etAddress.setText(mInvoiceTitleBean.getRegistrationAddress());
            viewModel.address=mInvoiceTitleBean.getRegistrationAddress();

            dataBinding.etPhone.setText(mInvoiceTitleBean.getRegistrationPhone());
            viewModel.phone=mInvoiceTitleBean.getRegistrationPhone();

            dataBinding.etDepositBank.setText(mInvoiceTitleBean.getDepositBank());
            viewModel.depositBank=mInvoiceTitleBean.getDepositBank();

            dataBinding.etBankAccount.setText(mInvoiceTitleBean.getBankCount());
            viewModel.bankAccount=mInvoiceTitleBean.getBankCount();
        }else {
            dataBinding.tvDel.setVisibility(View.INVISIBLE);
        }
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.layout1:
                showPickServiceDialog();
                break;
            case R.id.layout3:
                showPickServiceDialog();
                break;
            case R.id.tv_submit:
                if(mInvoiceTitleBean!=null){
                    viewModel.updateInvoiceTitle(mInvoiceTitleBean.getId());
                }else {
                    viewModel.addInvoiceTitle();
                }
                break;
            case R.id.tv_del:
                //删除（编辑才有删除）
                if(mInvoiceTitleBean!=null){
                    viewModel.delInvoiceTitle(mInvoiceTitleBean.getId());
                }
                break;
            default:
                break;
        }
    }
    private InvoiceTypeDialog mInvoiceTypeDialog;
    private void showPickServiceDialog(){
        if(mInvoiceTypeDialog==null){
            mInvoiceTypeDialog=new InvoiceTypeDialog(AddInvoiceTitleActivity.this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mInvoiceTypeDialog.cancel();
                    if(v.getId()==R.id.tv_item1){
                        viewModel.selected.set(1);
                        dataBinding.etCompanyName.setHint("请输入公司名称，必填");
                    }else {
                        viewModel.selected.set(2);
                        dataBinding.etCompanyName.setHint("请输入个人姓名");
                    }
                }
            });
        }
        mInvoiceTypeDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
