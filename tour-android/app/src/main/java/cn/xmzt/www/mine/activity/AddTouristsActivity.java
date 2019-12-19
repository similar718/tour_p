package cn.xmzt.www.mine.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityAddTouristsBinding;
import cn.xmzt.www.mine.bean.TourBean;
import cn.xmzt.www.mine.model.AddTouristsViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author tanlei
 * @date 2019/8/1
 * @describe 新增常用出游人界面(新增出游人和编辑出游人共用此界面)
 */

public class AddTouristsActivity extends TourBaseActivity<AddTouristsViewModel, ActivityAddTouristsBinding> {

    @Override
    protected int setLayoutId() {
        return R.layout.activity_add_tourists;
    }

    @Override
    protected AddTouristsViewModel createViewModel() {
        return new AddTouristsViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void setTourBeanData(TourBean bean) {
        viewModel.setBean(bean);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                String phoneNum = null;
                // 创建内容解析者
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = null;
                if (uri != null) {
                    cursor = contentResolver.query(uri, new String[]{"display_name","data1"}, null, null, null);
                }
                while (cursor.moveToNext()) {
                    String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    phoneNum = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                cursor.close();
                //  把电话号码中的  -  符号 替换成空格
                if (phoneNum != null) {
                    phoneNum = phoneNum.replaceAll("-", " ");
                    // 空格去掉  为什么不直接-替换成"" 因为测试的时候发现还是会有空格 只能这么处理
                    phoneNum= phoneNum.replaceAll(" ", "");
                }
                if(!TextUtils.isEmpty(phoneNum)){
                    dataBinding.etPhone.setText(phoneNum);
                }
            }
        }
    }
}
