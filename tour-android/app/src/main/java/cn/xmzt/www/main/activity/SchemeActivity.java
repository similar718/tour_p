package cn.xmzt.www.main.activity;

import androidx.lifecycle.Observer;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.databinding.ActivitySchemeBinding;
import cn.xmzt.www.home.activity.SelectedLineActivity;
import cn.xmzt.www.home.bean.ArticleBean;
import cn.xmzt.www.intercom.activity.TeamRoomActivity;
import cn.xmzt.www.intercom.bean.GroupRoomBean;
import cn.xmzt.www.intercom.event.RefreshMyTalkGroupList;
import cn.xmzt.www.main.vm.SchemeViewModel;
import cn.xmzt.www.manager.IntentManager;
import cn.xmzt.www.route.activity.RouteDetailActivity1;
import cn.xmzt.www.selfdrivingtools.activity.ScenicSpotMapActivity;
import cn.xmzt.www.utils.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * @describe Scheme  协议的Activity
 */
public class SchemeActivity extends TourBaseActivity<SchemeViewModel, ActivitySchemeBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_scheme;
    }
    @Override
    protected SchemeViewModel createViewModel() {
        viewModel = new SchemeViewModel();
        viewModel.result.observe(this, new Observer<GroupRoomBean>() {
            @Override
            public void onChanged(@Nullable GroupRoomBean result) {
                EventBus.getDefault().post(new RefreshMyTalkGroupList()); // 更新对讲群列表信息
                //1智能组队群，2行程群
                if(result.getGroupType()==1){
                    IntentManager.getInstance().goSmartTeamMapActivity(SchemeActivity.this,result.getGroupId(),false);
                }else {
                    IntentManager.getInstance().goSharedNavigationMapActivity(SchemeActivity.this,result.getGroupId());
                }
                finish();
            }
        });
        return viewModel;
    }
    @Override
    protected void initData() {
        StatusBarUtil.transparencyBar(this);
        String url=getIntent().getStringExtra("url");
        String groupId=getIntent().getStringExtra("groupId");
        EventBus.getDefault().register(this);

        if (!TextUtils.isEmpty(url)) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent();
            // Query部分
            String query = uri.getEncodedQuery();
            //获取指定参数值
            String actiontype = uri.getQueryParameter("t");//类型
            String id = uri.getQueryParameter("i");//类型对于的产品id
            String ref_code = uri.getQueryParameter("p");//ref_code	#用户推荐码
            switch (actiontype) {
                case "1":{//1、精品线路
                    //id 精品线路id
                    ArticleBean articleBean=new ArticleBean();
                    articleBean.setId(Integer.parseInt(id));
                    intent.setClass(this, SelectedLineActivity.class);
                    EventBus.getDefault().postSticky(articleBean);
                    break;
                }
                case "2": {//2、线路
                    //id 为线路id
                    intent.setClass(this, RouteDetailActivity1.class);
                    intent.putExtra("A",Integer.parseInt(id));
                    break;
                }
                case "3": {//3、邀请好友
                    //ref_code 用户推荐码
//                        intent.setClass(mContext, FriendInviteActivity.class);
                    break;
                }
                case "4": {//4、电子导览
                    //id 为电子导览id
                    intent.setClass(this, ScenicSpotMapActivity.class);
                    intent.putExtra("id",Integer.parseInt(id));
                    break;
                }
                case "5": {//5、邀请加入行程分享
                    //id 为行程id

                    break;
                }
                case "6": {//6、群主邀请入群二维码
                    //id 为群id
                    viewModel.scanQRInvited(ref_code,id);
                    break;
                }
                default:{

                }
            }
            if(!"6".equals(actiontype)){
                finish();
            }
        }else if(!TextUtils.isEmpty(groupId)){
            viewModel.getGroupRoomInfo(groupId,true);
        }else {
            finish();
        }
    }

    @Override
    public void showLoadFail(String msg) {
        super.showLoadFail(msg);
        finish();
    }

    @Subscribe
    public void talkGroupListEvent(RefreshMyTalkGroupList event) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}