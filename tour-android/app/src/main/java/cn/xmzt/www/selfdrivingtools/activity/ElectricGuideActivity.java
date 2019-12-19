package cn.xmzt.www.selfdrivingtools.activity;

import android.view.View;

import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityElectricGuideBinding;
import cn.xmzt.www.selfdrivingtools.viewmodel.ElectricGuideViewModel;

/**
 * 自驾工具-3 将Activity转为了Fragment  这个类没有使用
 */
public class ElectricGuideActivity extends TourBaseActivity<ElectricGuideViewModel, ActivityElectricGuideBinding> {

    @Override
    protected int setLayoutId() {
        return cn.xmzt.www.R.layout.activity_electric_guide;
    }

    @Override
    protected ElectricGuideViewModel createViewModel() {
        viewModel = new ElectricGuideViewModel();
        viewModel.setIView(this);
        return viewModel;
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
//        dataBinding.setActivity(this);
        // 顶部图片的显示
//        com.bumptech.glide.Glide.with(this).load(com.jm.selfdriving.R.drawable.electric_guide_top_img).into(dataBinding.electricGuideTopImgIv);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case cn.xmzt.www.R.id.rl_wisdom_work:// 智能出行
                break;
            case cn.xmzt.www.R.id.rl_wisdom: // 智慧景区导览
//                Intent intent = new Intent(this,WisdomGuideActivity.class);
//                startActivity(intent);
                startToActivity(WisdomGuideActivity.class);
                break;
            case cn.xmzt.www.R.id.rl_sos: // 一键救援
                break;
            default:
                break;
        }
    }
}
