package cn.xmzt.www.mine.model;

import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.mine.activity.BindContactActivity;

/**
 * @author tanlei
 * @date 2019/8/10
 * @describe
 */

public class BindContactViewModel extends BaseViewModel {
    private BindContactActivity activity;

    public void setActivity(BindContactActivity activity) {
        this.activity = activity;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_iv:
                activity.finish();
                break;
            default:
                break;
        }
    }
}
