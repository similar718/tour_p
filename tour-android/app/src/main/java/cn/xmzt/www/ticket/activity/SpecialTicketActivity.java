package cn.xmzt.www.ticket.activity;

import androidx.lifecycle.Observer;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivitySpecialTicketBinding;
import cn.xmzt.www.hotel.GlideImageLoader;
import cn.xmzt.www.ticket.adapter.SpecialTicketResultAdapter;
import cn.xmzt.www.ticket.bean.SpecialTicketBean;
import cn.xmzt.www.ticket.bean.SpecialTicketMustPlayBean;
import cn.xmzt.www.ticket.bean.SpecialTicketResultBean;
import cn.xmzt.www.ticket.model.SpecialTicketViewModel;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 *  特价门票的主页
 */
public class SpecialTicketActivity extends TourBaseActivity<SpecialTicketViewModel, ActivitySpecialTicketBinding> {

    private static final String mLocation = "113.87370575878904,22.595243590396187";
    private static final String mCity = "深圳";

    @Override
    protected int setLayoutId() {
        return R.layout.activity_special_ticket;
    }

    @Override
    protected SpecialTicketViewModel createViewModel() {
        viewModel = new SpecialTicketViewModel();
        viewModel.setIView(this);
        return viewModel;
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        dataBinding.setActivity(this);
        // 内容板块
        dataBinding.rvTicketList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        viewModel.mSpecialTicketList.observe(this, new Observer<SpecialTicketBean>() {
            @Override
            public void onChanged(@Nullable SpecialTicketBean specialTicketBean) {
                mSpecialTicketBean = specialTicketBean;
                setData();
            }
        });
        viewModel.mSpecialTicketMustPlayList.observe(this, new Observer<SpecialTicketMustPlayBean>() {
            @Override
            public void onChanged(@Nullable SpecialTicketMustPlayBean specialTicketMustPlayBean) {
                mSpecialTicketMustPlayBean = specialTicketMustPlayBean;
                setData();
            }
        });
        viewModel.getSpecialTicket(mLocation); // 获取首页数据数据
        viewModel.getSpecialTicketMustPlay(mCity); //获取首页必玩数据
    }

    private  SpecialTicketBean mSpecialTicketBean;
    private  SpecialTicketMustPlayBean mSpecialTicketMustPlayBean;

    private void setData(){
        if (mSpecialTicketMustPlayBean != null && mSpecialTicketBean != null) {
            // 对banner的赋值操作
            dataBinding.banner.setImageLoader(new GlideImageLoader());
            List<String> images = new ArrayList<>();
            if (mSpecialTicketBean.getAdvertise() != null) {
                for (int i = 0; i < mSpecialTicketBean.getAdvertise().size(); i++) {
                    images.add(mSpecialTicketBean.getAdvertise().get(i).getAdvPic());
                }
                dataBinding.banner.setImages(images);
                dataBinding.banner.start();
            }

            // 初始化数据
            List<SpecialTicketResultBean> list = new ArrayList<>();
            for (int i = 1; i < 4; i++) {
                SpecialTicketResultBean bean = new SpecialTicketResultBean();
                bean.setType(i);
                if (i == 1) {
                    bean.setTitle("深圳");
                    if (mSpecialTicketMustPlayBean.getItems() != null) {
                        bean.setListmust(mSpecialTicketMustPlayBean.getItems());
                    }
                } else if (i == 2) {
                    if (mSpecialTicketBean.getSubjectList() != null) {
                        bean.setListtitle(mSpecialTicketBean.getSubjectList());
                    }
                } else {
                    if (mSpecialTicketBean.getHot() != null) {
                        bean.setListhot(mSpecialTicketBean.getHot());
                    }
                }
                list.add(bean);
            }
            SpecialTicketResultAdapter adapter = new SpecialTicketResultAdapter(list, this);
            dataBinding.rvTicketList.setAdapter(adapter);

//            adapter.notifyItemChanged(0); // 更新人气必玩的列表信息
        }

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
    public void OnClickListener(Object data,int tag){ // 根据不同的item点击进行不一样的跳转操作
        if (tag == 3){ // 热门景点
            SpecialTicketBean.HotBean da = (SpecialTicketBean.HotBean) data;
            // 景点详请
            Intent detail = new Intent(SpecialTicketActivity.this,TicketDetailActivity.class);
            detail.putExtra("id",da.getId());
            startActivity(detail);
        }
        if (tag == 2){ // 精选主题
          SpecialTicketBean.SubjectListBean da = (SpecialTicketBean.SubjectListBean) data;
        }
        if (tag == 1){ // 人气必玩
            SpecialTicketMustPlayBean.ItemsBean da = (SpecialTicketMustPlayBean.ItemsBean) data;
            // 景点详请
            Intent detail = new Intent(SpecialTicketActivity.this,TicketDetailActivity.class);
            detail.putExtra("id",da.getId());
            startActivity(detail);
        }
    }
}
