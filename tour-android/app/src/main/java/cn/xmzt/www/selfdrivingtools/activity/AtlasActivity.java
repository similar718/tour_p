package cn.xmzt.www.selfdrivingtools.activity;

import androidx.recyclerview.widget.GridLayoutManager;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityAtlasBinding;
import cn.xmzt.www.selfdrivingtools.adapter.AtlasAdapter;
import cn.xmzt.www.manager.IntentManager;
import cn.xmzt.www.selfdrivingtools.viewmodel.AtlasViewModel;
import cn.xmzt.www.utils.StatusBarUtil;

import java.util.ArrayList;

/**
 * 图集界面
 */
public class AtlasActivity extends TourBaseActivity<AtlasViewModel, ActivityAtlasBinding> {

    ArrayList<String> list = new ArrayList<>();
    @Override
    protected int setLayoutId() {
        return R.layout.activity_atlas;
    }

    @Override
    protected AtlasViewModel createViewModel() {
        viewModel = new AtlasViewModel();
        viewModel.setIView(this);
        return viewModel;
    }

    @Override
    protected void initData() {
        // 设置顶部状态栏
        StatusBarUtil.setStatusBarLightMode(this,true);
        dataBinding.setModel(viewModel);
        dataBinding.setActivity(this);

        dataBinding.rvList.setLayoutManager(new GridLayoutManager(this,2));
        AtlasAdapter atlasAdapter = new AtlasAdapter(this);
        dataBinding.rvList.setAdapter(atlasAdapter);
        // 获取传递过来的数据集合
         list = getIntent().getStringArrayListExtra("list");
        atlasAdapter.setDatas(list);
        dataBinding.topRightTv.setText(list.size()+"张");
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case cn.xmzt.www.R.id.back_iv:// 返回
                finish();
                break;
            default:
                break;
        }
    }

    public void OnBottomClickListener(String item,int position){
        if (item.contains(".jpg") || item.contains(".png")){
            IntentManager.getInstance().goPhotoViewActivity(this,"",list,position);
        } else {
            IntentManager.getInstance().goVideoPlayActivity(this,item,item);
        }
    }
}
