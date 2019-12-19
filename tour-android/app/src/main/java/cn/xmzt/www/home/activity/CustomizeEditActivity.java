package cn.xmzt.www.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.databinding.ActivityCustomizeEditBinding;
import cn.xmzt.www.dialog.CustomizeLeaderTypeDialog;
import cn.xmzt.www.dialog.CustomizeLiveDialog;
import cn.xmzt.www.dialog.CustomizeLuodiTypeDialog;
import cn.xmzt.www.dialog.CustomizePersonNumDialog;
import cn.xmzt.www.dialog.CustomizeYuSuanDialog;
import cn.xmzt.www.dialog.TextTitleDialog;
import cn.xmzt.www.home.bean.CityBean;
import cn.xmzt.www.home.bean.CustomizeForm;
import cn.xmzt.www.home.bean.CustomizeLiveDialogBean;
import cn.xmzt.www.home.event.SelectCityBus;
import cn.xmzt.www.home.event.SelectDateIntervalBus;
import cn.xmzt.www.home.vm.CustomizeEditViewModel;
import cn.xmzt.www.utils.LocalDataUtils;
import cn.xmzt.www.utils.TimeUtil;
import cn.xmzt.www.view.listener.ItemListener;

/**
 * @describe 私人定制修改编辑
 */
public class CustomizeEditActivity extends TourBaseActivity<CustomizeEditViewModel, ActivityCustomizeEditBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_customize_edit;
    }

    @Override
    protected CustomizeEditViewModel createViewModel() {
        viewModel = new CustomizeEditViewModel(dataBinding);
        viewModel.result.observe(this, new Observer<BaseDataBean<Object>>() {
            @Override
            public void onChanged(@Nullable BaseDataBean<Object> result) {
                onBackPressed();
            }
        });
        return viewModel;
    }

    @Override
    protected void initData() {
        dataBinding.setActivity(this);
        dataBinding.setVm(viewModel);
        EventBus.getDefault().register(this);
        dataBinding.etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获取焦点时
                    if(!TextUtils.isEmpty(dataBinding.etName.getText())){
                        dataBinding.ivDelName.setVisibility(View.VISIBLE);
                    }else {
                        dataBinding.ivDelName.setVisibility(View.GONE);
                    }
                } else {
                    // 失去焦点时
                    dataBinding.ivDelName.setVisibility(View.GONE);
                }
            }
        });
        dataBinding.etPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获取焦点时
                    if(!TextUtils.isEmpty(dataBinding.etPhone.getText())){
                        dataBinding.ivDelPhone.setVisibility(View.VISIBLE);
                    }else {
                        dataBinding.ivDelPhone.setVisibility(View.GONE);
                    }
                } else {
                    // 失去焦点时
                    dataBinding.ivDelPhone.setVisibility(View.GONE);
                }
            }
        });
        dataBinding.etRemark.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获取焦点时
                    if(!TextUtils.isEmpty(dataBinding.etRemark.getText())){
                        dataBinding.ivDelRemark.setVisibility(View.VISIBLE);
                    }else {
                        dataBinding.ivDelRemark.setVisibility(View.GONE);
                    }
                } else {
                    // 失去焦点时
                    dataBinding.ivDelRemark.setVisibility(View.GONE);
                }
            }
        });

    }
    /**
     * 要修改的定制表单
     * @param customizeForm
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void eventCustomizeForm(@NonNull CustomizeForm customizeForm) {
        viewModel.customizeForm=customizeForm;
        CustomizeForm.CityArea departArea=viewModel.customizeForm.getDepartArea();//出发地
        viewModel.customizeForm.setDepartArea(departArea);
        dataBinding.tvStartAddress.setText(departArea.getCityName());
        setTvEndAddressText(viewModel.customizeForm.getArrivalArea());

        String departDate=viewModel.customizeForm.getDepartDate();
        String endDate=viewModel.customizeForm.getEndDate();
        int betweenIntervalDays= TimeUtil.getBetweenIntervalDays(departDate,endDate,"yyyy-MM-dd")+1;
        dataBinding.tvDateContent.setText(TimeUtil.stringDateToString(departDate,"yyyy-MM-dd","MM月dd日")
                +" - "+TimeUtil.stringDateToString(endDate,"yyyy-MM-dd","MM月dd日")
                +"  "+ betweenIntervalDays+"天");

        mPersonNum = viewModel.customizeForm.getCrNumber();
        mChildNum = viewModel.customizeForm.getXhNumber();
        dataBinding.tvPersonContent.setText(viewModel.customizeForm.getCrNumber()+"成人，"+viewModel.customizeForm.getXhNumber()+"儿童");
        dataBinding.tvLeaderTypeContent.setText(viewModel.customizeForm.getTacticsType() == 1 ? "无需领队" : "金牌领队");
        dataBinding.tvLuodiTypeContent.setText(viewModel.customizeForm.getGroundType() == 1 ? "落地自驾" : "自己爱车");
        int accommodationType=viewModel.customizeForm.getAccommodationType();
        mLiveData= LocalDataUtils.getCustomizeTypeLevelName(accommodationType,viewModel.customizeForm.getAccommodationLevel());
        dataBinding.tvLiveContent.setText("");
        if (accommodationType == 1){ // 酒店
            dataBinding.tvLiveContent.setText("酒店   "+mLiveData);
        } else if (accommodationType == 2){ // 民宿
            dataBinding.tvLiveContent.setText("民宿   "+mLiveData);
        }
        mYusuanData= LocalDataUtils.getCustomizeTypeLevelName(3,viewModel.customizeForm.getBudgetNew());
        dataBinding.tvSpendContent.setText(mYusuanData);

        dataBinding.etName.setText(viewModel.customizeForm.getName());
        dataBinding.etPhone.setText(viewModel.customizeForm.getTel());
        dataBinding.etRemark.setText(viewModel.customizeForm.getRemark());
        if(TextUtils.isEmpty(viewModel.customizeForm.getRemark())){
            dataBinding.tvRemarkHint.setVisibility(View.VISIBLE);
        }else {
            dataBinding.tvRemarkHint.setVisibility(View.GONE);
        }

    }
    /**
     * 出发时间结束时间
     * @param selectDateIntervalBus
     */
    @Subscribe
    public void eventStartEndDate(@NonNull SelectDateIntervalBus selectDateIntervalBus) {
        if (selectDateIntervalBus.getType()==1) {
            String departDate=selectDateIntervalBus.getStartDate();
            String endDate=selectDateIntervalBus.getEndDate();
            viewModel.customizeForm.setDepartDate(departDate);
            viewModel.customizeForm.setEndDate(endDate);
            int betweenIntervalDays=TimeUtil.getBetweenIntervalDays(departDate,endDate,"yyyy-MM-dd")+1;
            dataBinding.tvDateContent.setText(TimeUtil.stringDateToString(departDate,"yyyy-MM-dd","MM月dd日")
                    +" - "+TimeUtil.stringDateToString(endDate,"yyyy-MM-dd","MM月dd日")
                    +"  "+ betweenIntervalDays+"天");
        }
    }
    /**
     * 出发城市的选择
     * @param city
     */
    @Subscribe
    public void eventDepartCity(@NonNull CityBean city) {
        //[{’citycode’:’110000’,’cityname’:’北京市’}]
        if (!TextUtils.isEmpty(city.getAreaName())) {
            CustomizeForm.CityArea cityArea=new CustomizeForm.CityArea();
            cityArea.setCityCode(city.getAreaCode());
            cityArea.setCityName(city.getAreaName());
            viewModel.customizeForm.setDepartArea(cityArea);
            dataBinding.tvStartAddress.setText(city.getAreaName());
        }
    }
    private void setTvEndAddressText(List<CustomizeForm.CityArea> arrivalList){
        if (arrivalList.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < arrivalList.size(); i++) {
                CustomizeForm.CityArea city = arrivalList.get(i);
                if (i == 0) {
                    sb.append(city.getCityName());
                } else {
                    sb.append("/");
                    sb.append(city.getCityName());
                }
            }
            dataBinding.tvEndAddress.setText(sb.toString());
        }
    }
    @Subscribe
    public void eventArrivalCityList(@NonNull List<CityBean> cityList) {
        if (cityList.size() > 0) {
            List<CustomizeForm.CityArea> arrivalList = new ArrayList<>();//目的地
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < cityList.size(); i++) {
                CityBean city = cityList.get(i);
                CustomizeForm.CityArea cityArea=new CustomizeForm.CityArea();
                cityArea.setCityCode(city.getAreaCode());
                cityArea.setCityName(city.getAreaName());
                arrivalList.add(cityArea);
                if (i == 0) {
                    sb.append(city.getAreaName());
                } else {
                    sb.append("/");
                    sb.append(city.getAreaName());
                }
            }
            viewModel.customizeForm.setArrivalArea(arrivalList);
            dataBinding.tvEndAddress.setText(sb.toString());
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back: // 返回
                onBackPressed();
                break;
            case R.id.tv_del_customize: //删除定制单
                showDelDialog();
                break;
            case R.id.tv_start_address: // 选择出发地
                startToActivity(SelectCityActivity.class);
                CityBean city = new CityBean();
                CustomizeForm.CityArea departArea=viewModel.customizeForm.getDepartArea();
                if (departArea != null) {
                    city.setSelect(true);
                    city.setAreaCode(departArea.getCityCode());
                    city.setAreaName(departArea.getCityName());
                }
                SelectCityBus mSelectCityBus = new SelectCityBus(0);
                mSelectCityBus.setCity(city);
                EventBus.getDefault().postSticky(mSelectCityBus);
                break;
            case R.id.tv_end_address: {// 选择目的地
                Intent intent = new Intent(this, SelectCityActivity.class);
                intent.putExtra("A", 1);
                intent.putExtra("B", 6);
                startActivity(intent);
                SelectCityBus mSelectCityBus1 = new SelectCityBus(1);
                List<CityBean> list = new ArrayList<>();//目的地
                List<CustomizeForm.CityArea> selectArrivalList = viewModel.customizeForm.getArrivalArea();
                if (selectArrivalList != null) {
                    for (CustomizeForm.CityArea mCityBean : selectArrivalList) {
                        CityBean arrivalCity = new CityBean();
                        arrivalCity.setSelect(true);
                        arrivalCity.setAreaCode(mCityBean.getCityCode());
                        arrivalCity.setAreaName(mCityBean.getCityName());
                        list.add(arrivalCity);
                    }
                }
                mSelectCityBus1.setList(list);
                EventBus.getDefault().postSticky(mSelectCityBus1);
                break;
            }
            case R.id.tv_person_content: // 人员数量设置
                showSettingPersonNumDialog();
                break;
            case R.id.tv_date_content: {// 日期选择
                Intent intent = new Intent(this, SelectTimeIntervalActivity.class);
                intent.putExtra("A", viewModel.customizeForm.getDepartDate());
                intent.putExtra("B", viewModel.customizeForm.getEndDate());
                startActivity(intent);
//                showTimePickerView();
                break;
            }
            case R.id.ll_luodi_type: // 是否落地
                showLuodiTypeDialog();
                break;
            case R.id.ll_leader_type: // 领队类型
                showLeaderTypeDialog();
                break;
            case R.id.ll_live: // 住宿标准
                showLiveDialog();
                break;
            case R.id.ll_spend: // 人均预算
                showYuSuanDialog();
                break;
            case R.id.iv_del_name: // 清除行程备注
                dataBinding.etName.setText("");
                viewModel.customizeForm.setName("");
                break;
            case R.id.iv_del_phone: // 清除行程备注
                dataBinding.etPhone.setText("");
                viewModel.customizeForm.setTel("");
                break;
            case R.id.iv_del_remark: // 清除行程备注
                dataBinding.etRemark.setText("");
                viewModel.customizeForm.setRemark("");
                break;
            case R.id.tv_confirm: // 确定修改定制
                viewModel.modifyCustomize();
                break;
            default:
                break;
        }
    }

    private int mPersonNum = 0; // 默认的成人0人
    private int mChildNum = 0; // 默认的儿童0人
    private CustomizePersonNumDialog customizePersonNumDialog;
    /**
     * 显示设置人数的dialog
     */
    private void showSettingPersonNumDialog(){
        if (customizePersonNumDialog == null){
            customizePersonNumDialog = new CustomizePersonNumDialog(this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.iv_person_add:
                            mPersonNum++;
                            break;
                        case R.id.iv_person_reduce:
                            mPersonNum--;
                            break;
                        case R.id.iv_child_add:
                            mChildNum++;
                            break;
                        case R.id.iv_child_reduce:
                            mChildNum--;
                            break;
                        default:
                            break;
                    }
                    mPersonNum = mPersonNum < 0 ? 0 : mPersonNum;
                    mChildNum = mChildNum < 0 ? 0 : mChildNum;
                    mPersonNum = mPersonNum > 99 ? 99 : mPersonNum;
                    mChildNum = mChildNum > 99 ? 99 : mChildNum;
                    customizePersonNumDialog.setViewData(mPersonNum, mChildNum);
                    viewModel.customizeForm.setCrNumber(mPersonNum);
                    viewModel.customizeForm.setXhNumber(mChildNum);
                    // 界面更改
                    dataBinding.tvPersonContent.setText(mPersonNum + "成人，" + mChildNum + "儿童");
                }
            }, new CustomizePersonNumDialog.CustomizePersonNumClickListener() {
                @Override
                public void requestInputInfo(int personNum, int childNum) { // 监听输入框的更新界面
                    mPersonNum = personNum;
                    mChildNum = childNum;
                    viewModel.customizeForm.setCrNumber(mPersonNum);
                    viewModel.customizeForm.setXhNumber(mChildNum);
                    // 界面更改
                    dataBinding.tvPersonContent.setText(viewModel.customizeForm.getCrNumber() + "成人，" + viewModel.customizeForm.getXhNumber() + "儿童");

                }
            });
        }
        customizePersonNumDialog.setViewData(mPersonNum,mChildNum);
        customizePersonNumDialog.show();
    }

    private CustomizeLeaderTypeDialog mLeaderTypeDialog;
    /**
     * 显示设置领队类型的dialog
     */
    private void showLeaderTypeDialog(){
        if (mLeaderTypeDialog == null){
            mLeaderTypeDialog = new CustomizeLeaderTypeDialog(this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int type = 1;//领队类型：1无需领队 2金牌领队
                    switch (v.getId()) {
                        case R.id.rl_leader: // 金牌领队
                            type = 2;
                            break;
                        case R.id.rl_no_leader: // 无需领队
                            type = 1;
                            break;
                        default:
                            break;
                    }
                    if (type != viewModel.customizeForm.getTacticsType()){
                        mLeaderTypeDialog.setViewData(type);
                        dataBinding.tvLeaderTypeContent.setText(type == 1 ? "无需领队" : "金牌领队");
                        viewModel.customizeForm.setTacticsType(type);
                    }
                    mLeaderTypeDialog.dismiss();
                }
            });
        }
        mLeaderTypeDialog.setViewData(viewModel.customizeForm.getTacticsType());
        mLeaderTypeDialog.show();
    }

    private CustomizeLuodiTypeDialog mLduodiTypeDialog;
    /**
     * 显示设置领队类型的dialog
     */
    private void showLuodiTypeDialog(){
        if (mLduodiTypeDialog == null){
            mLduodiTypeDialog = new CustomizeLuodiTypeDialog(this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int type = 1;//落地类型：1落地自驾 2自己爱车
                    switch (v.getId()) {
                        case R.id.rl_leader: //  自己爱车
                            type = 2;
                            break;
                        case R.id.rl_no_leader: // 落地自驾
                            type = 1;
                            break;
                        default:
                            break;
                    }
                    if (type != viewModel.customizeForm.getGroundType()){
                        mLduodiTypeDialog.setViewData(type);
                        dataBinding.tvLuodiTypeContent.setText(type == 1 ? "落地自驾" : "自己爱车");
                        viewModel.customizeForm.setGroundType(type);
                    }
                    mLduodiTypeDialog.dismiss();
                }
            });
        }
        mLduodiTypeDialog.setViewData(viewModel.customizeForm.getGroundType());
        mLduodiTypeDialog.show();
    }
    private CustomizeLiveDialog mLiveDialog;
    private String mLiveData="";
    /**
     * 显示设置领队类型的dialog
     */
    private void showLiveDialog(){
        if (mLiveDialog == null){
            mLiveDialog = new CustomizeLiveDialog(this, new ItemListener() {
                @Override
                public void onItemClick(View view, int position) {
                    CustomizeLiveDialogBean mCustomizeDialogBean=mLiveDialog.getItem(position);
                    int type=0;
                    if(mCustomizeDialogBean!=null){
                        type=mCustomizeDialogBean.getType();
                        mLiveData=mCustomizeDialogBean.getTitle();
                    }
                    if (type == 1){ // 酒店
                        dataBinding.tvLiveContent.setText("酒店   "+mLiveData);
                        viewModel.customizeForm.setAccommodationType(type);
                        viewModel.customizeForm.setAccommodationLevel(position+2);
                    } else if (type == 2){ // 民宿
                        dataBinding.tvLiveContent.setText("民宿   "+mLiveData);
                        viewModel.customizeForm.setAccommodationType(type);
                        viewModel.customizeForm.setAccommodationLevel(position+1);
                    }

                }
            });
        }
        mLiveDialog.setViewData(viewModel.customizeForm.getAccommodationType(),mLiveData);
        mLiveDialog.show();
    }

    private String mYusuanData = ""; // 默认选中为空数据
    private CustomizeYuSuanDialog mYuSuanDialog;
    /**
     * 显示设置领队类型的dialog
     */
    private void showYuSuanDialog(){
        if (mYuSuanDialog == null){
            mYuSuanDialog = new CustomizeYuSuanDialog(this, new ItemListener() {
                @Override
                public void onItemClick(View view, int position) {
                    CustomizeLiveDialogBean mCustomizeDialogBean=mYuSuanDialog.getItem(position);
                    int type=0;
                    String title="";
                    if(mCustomizeDialogBean!=null){
                        type=mCustomizeDialogBean.getType();
                        title=mCustomizeDialogBean.getTitle();
                    }
                    if (type == 3){ // 人均预算
                        mYusuanData = title;
                        dataBinding.tvSpendContent.setText(mYusuanData);
                        viewModel.customizeForm.setBudgetNew(position+1);
                    }
                }
            });
        }
        mYuSuanDialog.setViewData(mYusuanData);
        mYuSuanDialog.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    private TextTitleDialog delDialog;

    private void showDelDialog() {
        delDialog = new TextTitleDialog(this, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delDialog.dismiss();
                viewModel.deleteCustomize();
            }
        });
        delDialog.setViewData("确定删除定制记录？","我再想想","删除");
        delDialog.setBtnBackground(R.drawable.shape_gray_f4_radius_4,R.drawable.shape_blue_radius_4);
        delDialog.setBtnTextColor(Color.parseColor("#24A4FF"),Color.parseColor("#FFFFFF"));
        delDialog.show();
    }
}
