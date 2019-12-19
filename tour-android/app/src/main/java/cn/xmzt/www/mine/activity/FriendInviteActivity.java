package cn.xmzt.www.mine.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityFriendInviteBinding;
import cn.xmzt.www.mine.bean.ShapeBitmapBean;
import cn.xmzt.www.mine.model.FriendInviteViewModel;
import cn.xmzt.www.share.ShareFunction;
import cn.xmzt.www.utils.BitmapUtils;
import cn.xmzt.www.utils.DpUtil;
import cn.xmzt.www.zxing.encoding.EncodingHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author tanlei
 * @date 2019/8/16
 * @describe 邀请好友
 */

public class FriendInviteActivity extends TourBaseActivity<FriendInviteViewModel, ActivityFriendInviteBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_friend_invite;
    }

    @Override
    protected FriendInviteViewModel createViewModel() {
        return new FriendInviteViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setBitmap(ShapeBitmapBean shapeBitmapBean) {
        Bitmap mBitmap = EncodingHandler.createQRCode(shapeBitmapBean.getUrl(), DpUtil.dp2px(TourApplication.context, 32),
                DpUtil.dp2px(TourApplication.context, 32), BitmapFactory.decodeResource(getResources(), R.drawable.xmzt_log));
        ShareFunction.getInstance().shareImage(this, BitmapUtils.mergeBitmap(shapeBitmapBean.getBitmap(), mBitmap), shapeBitmapBean.getMedia());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
