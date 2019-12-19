package cn.xmzt.www.home.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivitySelectCityBinding;
import cn.xmzt.www.home.adapter.SelectCityAdapter;
import cn.xmzt.www.home.adapter.SelectedCityAdapter;
import cn.xmzt.www.home.bean.CityBean;
import cn.xmzt.www.home.event.SelectCityBus;
import cn.xmzt.www.home.vm.SelectCityViewModel;
import cn.xmzt.www.rxjava2.AsyncUtil;
import cn.xmzt.www.view.ScrollGridLayoutManager;
import cn.xmzt.www.view.listener.ItemListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @describe 选择城市
 */
public class SelectCityActivity extends TourBaseActivity<SelectCityViewModel, ActivitySelectCityBinding> implements ItemListener {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_select_city;
    }

    @Override
    protected SelectCityViewModel createViewModel() {
        viewModel = new SelectCityViewModel();
        viewModel.cityList.observe(this, new Observer<List<Object>>() {
            @Override
            public void onChanged(@Nullable List<Object> result) {
                if (result != null && result.size() > 0) {
                    adapter.setDatas(result);
                }
            }
        });
        return viewModel;
    }

    SelectCityAdapter adapter = null;
    ScrollGridLayoutManager manager = null;

    private int selectMaxCount = 1;//最多选择数

    @Override
    protected void initData() {
        Intent intent = getIntent();
        viewModel.selectType = intent.getIntExtra("A", 0);
        selectMaxCount = intent.getIntExtra("B", 1);
        if (viewModel.selectType == 1) {
            dataBinding.tvTitle.setText("选择目的地");
        } else {
            dataBinding.tvTitle.setText("选择出发地");
        }
        dataBinding.setActivity(this);
        dataBinding.setVm(viewModel);
        EventBus.getDefault().register(this);
        dataBinding.letterSideBar.setItemListener(this);
        viewModel.selectedAdapter = new SelectedCityAdapter();
        if (viewModel.selectType == 1) {
            selectMaxCount = 6;
            dataBinding.selectLayout.setVisibility(View.VISIBLE);
            GridLayoutManager selectManager = (GridLayoutManager) dataBinding.selectList.getLayoutManager();
            selectManager.setSpanCount(1);
            selectManager.setOrientation(GridLayoutManager.HORIZONTAL);
            viewModel.selectedAdapter.setItemListener(this);
            dataBinding.selectList.setAdapter(viewModel.selectedAdapter);
        } else {
            selectMaxCount = 1;
            dataBinding.selectLayout.setVisibility(View.GONE);
        }
        manager = new ScrollGridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
//        manager.setSpeed(0.001f);
        dataBinding.recyclerView.setLayoutManager(manager);
        adapter = new SelectCityAdapter();
        adapter.setItemListener(this);
        adapter.setSelectType(viewModel.selectType);
        dataBinding.recyclerView.setAdapter(adapter);
        viewModel.getCityList(getApplicationContext());
    }

    @Subscribe(sticky = true)
    public void onSelectCityBus(@NonNull SelectCityBus selectCityBus) {
        //[{’citycode’:’110000’,’cityname’:’北京市’}]
        viewModel.selectType = selectCityBus.getType();
        if (viewModel.selectType == 1) {
            dataBinding.tvTitle.setText("选择目的地");
        } else {
            dataBinding.tvTitle.setText("选择出发地");
        }
        CityBean city = selectCityBus.getCity();
        List<CityBean> cityList = selectCityBus.getList();//目的地
        if (viewModel.selectType == 0 && !TextUtils.isEmpty(city.getAreaName())) {
            viewModel.selectList.clear();
            viewModel.selectList.add(city);
        } else if (viewModel.selectType == 1) {
            if (cityList.size() > 0) {
                viewModel.selectList = cityList;
            } else {
                viewModel.selectList.clear();
            }
        }
        if (viewModel.selectList != null) {
            dataBinding.tvSelectCount.setText(viewModel.selectList.size() + "/" + selectMaxCount);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_select_count: {
                int selectCount = viewModel.selectedAdapter.getItemCount();
                if (selectCount > 0) {
                    if (viewModel.showSelect.get() == 0) {
                        viewModel.showSelect.set(1);
                    } else {
                        viewModel.showSelect.set(0);
                    }
                } else {
                    viewModel.showSelect.set(0);
                }
                break;
            }
            case R.id.tv_confirm: {
                //选择确认
                List<CityBean> selectCity = viewModel.selectedAdapter.getDatas();
                if (selectCity.size() > 0) {
                    if (viewModel.selectType == 0) {
                        //选择出发地为单选
                        EventBus.getDefault().post(selectCity.get(0));
                    } else {
                        EventBus.getDefault().post(selectCity);
                    }
                    finish();
                } else {
                    ToastUtils.showShort("请先选择城市");
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (view.getId() == R.id.iv_del) {
            CityBean cityBean = viewModel.selectedAdapter.getItem(position);
            viewModel.selectedAdapter.removeData(cityBean);
            cityBean.setSelect(false);
            adapter.notifyDataSetChanged();
            dataBinding.tvSelectCount.setText(viewModel.selectedAdapter.getItemCount() + "/" + selectMaxCount);
            if (viewModel.selectedAdapter.getItemCount() == 0) {
                viewModel.showSelect.set(0);
            }
        } else if (view.getId() == R.id.itemLayout) {
            Object obj = adapter.getItem(position);
            if (obj instanceof CityBean) {
                CityBean cityBean = (CityBean) obj;
                if ("暂无定位信息".equals(cityBean.getAreaName())) {
                    cityBean.setSelect(false);
                    return;
                }
                if (!cityBean.isSelect()) {
                    if (viewModel.selectType == 0) {
                        //出发地只选择一项
                        cityBean.setSelect(true);
                        viewModel.selectedAdapter.clearData();
                        viewModel.selectedAdapter.addData(cityBean);
                        int temp = viewModel.radioPosition;
                        viewModel.radioPosition = position;
                        Object tempObj = adapter.getItem(temp);
                        if (tempObj instanceof CityBean) {
                            CityBean tempCity = (CityBean) tempObj;
                            tempCity.setSelect(false);
                        }
                        adapter.notifyItemChanged(0);
                        adapter.notifyItemChanged(temp);
                    } else {
                        if (viewModel.selectedAdapter.getItemCount() < selectMaxCount) {
                            cityBean.setSelect(true);
                            viewModel.selectedAdapter.addData(cityBean);
                        }
                    }
                } else {
                    viewModel.radioPosition = -1;
                    viewModel.selectedAdapter.removeData(cityBean);
                    cityBean.setSelect(false);
                }
                viewModel.selectedAdapter.notifyDataSetChanged();
                adapter.notifyItemChanged(position);
            }
            dataBinding.tvSelectCount.setText(viewModel.selectedAdapter.getItemCount() + "/" + selectMaxCount);
        } else if (view.getId() == R.id.tv_letter_name) {
            int leterPosition = adapter.getItemPosition(dataBinding.letterSideBar.getItem(position));
            dataBinding.recyclerView.scrollToPosition(leterPosition);
            //异步线程控制smoothScrollToPosition与scrollToPosition滑动冲突
            AsyncUtil.async(new Function<String, String>() {
                @Override
                public String apply(String o) throws Exception {
                    Thread.sleep(10);
                    return "";
                }
            }, new Consumer<String>() {
                @Override
                public void accept(String String) throws Exception {
                    dataBinding.recyclerView.smoothScrollToPosition(leterPosition);
                }
            });

        }
    }
}
