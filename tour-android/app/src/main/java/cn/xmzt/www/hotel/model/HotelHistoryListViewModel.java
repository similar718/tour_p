package cn.xmzt.www.hotel.model;

import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.dialog.CleanHistoryHotelListDialog;

/**
 * @author tanlei
 * @date 2019/7/27
 * @describe
 */

public class HotelHistoryListViewModel extends BaseViewModel {
    private CleanHistoryHotelListDialog dialog;

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_clean://删除浏览历史
                dialog = new CleanHistoryHotelListDialog(view.getContext(), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(null != dialog){
                            //做对应的删除操作
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
                break;
            default:
                break;
        }
    }
}
