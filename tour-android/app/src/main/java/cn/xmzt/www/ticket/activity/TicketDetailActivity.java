package cn.xmzt.www.ticket.activity;

import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityTicketDetailBinding;
import cn.xmzt.www.hotel.GlideImageLoader;
import cn.xmzt.www.ticket.adapter.TicketDetailTAdapter;
import cn.xmzt.www.ticket.bean.TicketDetailBean;
import cn.xmzt.www.ticket.bean.TicketDetailInfo;
import cn.xmzt.www.ticket.model.TicketDetailViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 *  门票详情界面
 */
public class TicketDetailActivity extends TourBaseActivity<TicketDetailViewModel, ActivityTicketDetailBinding> {

    @Override
    protected int setLayoutId() {
        return R.layout.activity_ticket_detail;
    }

    @Override
    protected TicketDetailViewModel createViewModel() {
        viewModel = new TicketDetailViewModel();
        viewModel.setIView(this);
        return viewModel;
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        dataBinding.setActivity(this);

        // 对banner的赋值操作
        dataBinding.banner.setImageLoader(new GlideImageLoader());
        List<String> images = new ArrayList<>();
        images.add("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_" +
                "4000&sec=1564216863&di=2e009b334c4135bdf023104d6deba987&src=http://dingyue.nosdn.127.net/8jZqtz1mW8Cp" +
                "GNDQthJNta6IK3RL90zRKygBREoV8NSEO1541474222791compressflag.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1564226996937&di=c36dc03e31ba0f7697b04c6e6f0c2881&" +
                "imgtype=jpg&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D2278011335%2C2910610009%26fm%3D214%26gp%3D0.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1564226956669&di=abcbf25e311be30315f674d" +
                "b6a1567a3&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20180125%2Fc35e9967ad5c4375893db0abf683e4e3.jpeg");
        dataBinding.banner.setImages(images);
        dataBinding.banner.start();

        // 内容板块
        dataBinding.rvTicketDetailList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        int tour_id = getIntent().getIntExtra("id",0);

        // 请求接口
        viewModel.mTicketDetailList.observe(this, new Observer<TicketDetailInfo>() {
            @Override
            public void onChanged(@Nullable TicketDetailInfo ticketDetailInfo) {
                setData(ticketDetailInfo); // 获取得到的数据进行界面的显示
            }
        });
        viewModel.getSpecialTicket(tour_id); // 获取数据

        setData(null);
    }

    private void setData(TicketDetailInfo data){
        TicketDetailInfo info = data; // 对数据进行分解
        // 初始化数据
        List<TicketDetailBean> list = new ArrayList<>();
        for (int i = 1; i < 5; i++){
            TicketDetailBean bean = new TicketDetailBean();
//            beanUtils.setType(i);
//            if (i == 1){
//                beanUtils.setListmust(data.getPopularity());
//            } else if(i == 2){
//                beanUtils.setListtitle(data.getAdvertise());
//            } else {
//                beanUtils.setListhot(data.getHot());
//            }
            list.add(bean);
        }
        TicketDetailTAdapter adapter = new TicketDetailTAdapter(list, this); // 对数据进行赋值
        dataBinding.rvTicketDetailList.setAdapter(adapter);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }
    public void OnClickListener(Object data,int tag){
        // 1 预定 2 订票须知
    }
}
