package cn.xmzt.www.route.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.base.WebActivity;
import cn.xmzt.www.bean.OftenVisitorsBean;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ItemRouteFillinOrder1Binding;
import cn.xmzt.www.databinding.ItemRouteFillinOrder2Binding;
import cn.xmzt.www.databinding.ItemRouteFillinOrder3Binding;
import cn.xmzt.www.databinding.ItemRouteFillinOrder4Binding;
import cn.xmzt.www.databinding.ItemRouteFillinOrder5Binding;
import cn.xmzt.www.databinding.ItemRouteFillinOrder61Binding;
import cn.xmzt.www.databinding.ItemRouteFillinOrder6Binding;
import cn.xmzt.www.databinding.ItemRouteFillinOrder7Binding;
import cn.xmzt.www.databinding.ItemRouteFillinOrder8Binding;
import cn.xmzt.www.databinding.ItemRouteFillinOrder9Binding;
import cn.xmzt.www.intercom.bean.UserBasicInfoBean;
import cn.xmzt.www.manager.IntentManager;
import cn.xmzt.www.mine.bean.MyCouponBean;
import cn.xmzt.www.route.activity.CouponUseActivity;
import cn.xmzt.www.route.activity.RouteFillinInvoiceActivity;
import cn.xmzt.www.route.activity.SelectVisitorsActivity;
import cn.xmzt.www.route.bean.CarForm;
import cn.xmzt.www.route.bean.ContactForm;
import cn.xmzt.www.route.bean.CostForm;
import cn.xmzt.www.route.bean.LineOrderForm;
import cn.xmzt.www.route.bean.OrderInvoiceForm;
import cn.xmzt.www.route.bean.PersonCountBean;
import cn.xmzt.www.route.bean.RouteDetailCategory;
import cn.xmzt.www.route.bean.RoutePage;
import cn.xmzt.www.route.bean.TimePriceDayBean;
import cn.xmzt.www.route.bean.TourInsurance;
import cn.xmzt.www.utils.KeyboardUtil;
import cn.xmzt.www.utils.MatcherUtils;
import cn.xmzt.www.utils.MathUtil;
import cn.xmzt.www.view.listener.TextChangedListener;


public class RouteFillinOrderAdapter extends BaseRecycleViewAdapter<Object, RouteFillinOrderAdapter.ItemHolder> {
    private final int TYPE_ITEM_1 = 1;//线路
    private final int TYPE_ITEM_2 = 2;//分类
    private final int TYPE_ITEM_3 = 3;//出游人
    private final int TYPE_ITEM_4 = 4;//车辆信息
    private final int TYPE_ITEM_5 = 5;//添加更多车辆信息
    private final int TYPE_ITEM_6 = 6;//预定人信息
    private final int TYPE_ITEM_6_1 = 61;//保险信息
    private final int TYPE_ITEM_7 = 7;//优惠券
    private final int TYPE_ITEM_8 = 8;//发票
    private final int TYPE_ITEM_9 = 9;//同意协议

    public PersonCountBean mPersonCountBean;
    public TourInsurance tourInsurance=null;
    private KeyboardUtil keyboardUtil;
    private FrameLayout keyboardLayout;
    public void setKeyboardLayout(FrameLayout keyboardLayout){
        this.keyboardLayout=keyboardLayout;
    }

    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = ActivityUtils.getTopActivity().getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        //DecorView即为activity的顶级view
        ActivityUtils.getTopActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
        //选取screenHeight*2/3进行判断
        return screenHeight*2/3 > rect.bottom;
    }
    public void hideSoftKeyboardUtil() {
        if(keyboardUtil!=null){
            keyboardUtil.hideKeyboard();
        }
    }

    /**
     * 隐藏软键盘(只适用于Activity，不适用于Fragment)
     */
    public static void hideSoftKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * @param lineName         线路名称
     * @param photoUrl         线路封面图片
     * @param mPersonCountBean 线路选择出发人数的数量
     */
    public void setData(String lineName, String photoUrl, PersonCountBean mPersonCountBean) {
        this.datas.clear();
        this.mPersonCountBean = mPersonCountBean;
        RoutePage.ItemsBean routeItem = new RoutePage.ItemsBean();
        routeItem.setLineName(lineName);
        routeItem.setPhotoUrl(photoUrl);
        routeItem.setDepartTime(mPersonCountBean.getTime());
        this.datas.add(routeItem);
        this.datas.add(new RouteDetailCategory("出行人信息", R.drawable.icon_route_info_add_visitor));
        startVisitorIndex = this.datas.size();
        for (int i = 0; i < mPersonCountBean.getCrCount(); i++) {
            int index = i + 1;
            this.datas.add(new ContactForm(0, "成人" + index, false));
        }
        for (int i = 0; i < mPersonCountBean.getXhCount(); i++) {
            int index = i + 1;
            this.datas.add(new ContactForm(1, "儿童" + index, false));
        }
        if (mPersonCountBean.getCrCount() > 0) {
            endVisitorIndex = this.datas.size() - 1;
        }

        this.datas.add(new RouteDetailCategory("车辆信息", 0));
        startCarIndex = this.datas.size();
        /*this.datas.add(new CarForm());
        endCarIndex = startCarIndex;*/
        this.datas.add("添加更多车辆信息");

        startContactIndex = this.datas.size();
        ContactForm contactForm=new ContactForm(0, null, true);
        UserBasicInfoBean user=TourApplication.getUser();
        if(user!=null){
            contactForm.setPhone(user.getTel());
        }
        this.datas.add(contactForm);
        this.datas.add("保险");
        this.datas.add("优惠");
        this.datas.add("发票");
        this.datas.add("我已同意");
        notifyDataSetChanged();
    }

    private List<OftenVisitorsBean> selectOftenVisitorsList = null;

    public void selectOftenVisitorsList(List<OftenVisitorsBean> list) {
        selectOftenVisitorsList = list;
        for (int i = 0; i < list.size(); i++) {
            OftenVisitorsBean mOftenVisitorsBean = list.get(i);
            Object obj = getItem(startVisitorIndex + i);
            if (obj instanceof ContactForm) {
                ContactForm mContactForm = (ContactForm) obj;
                mContactForm.setName(mOftenVisitorsBean.getName());
                mContactForm.setPhone(mOftenVisitorsBean.getTel());
                mContactForm.setIdCard(mOftenVisitorsBean.getIdentityCard());
                mContactForm.setAgeStage(mOftenVisitorsBean.isChildren() ? 1 : 0);
                mContactForm.setIdType(mOftenVisitorsBean.getCertificateType());
            }
        }
        notifyDataSetChanged();
    }

    private OftenVisitorsBean selectOftenVisitors = null;//选中的预定人

    public void selectOftenVisitors(OftenVisitorsBean visitors) {
        selectOftenVisitors = visitors;
        Object obj = getItem(startContactIndex);
        if (obj instanceof ContactForm) {
            ContactForm mContactForm = (ContactForm) obj;
            mContactForm.setName(visitors.getName());
            mContactForm.setPhone(visitors.getTel());
            mContactForm.setIdCard(visitors.getIdentityCard());
            mContactForm.setAgeStage(visitors.isChildren() ? 1 : 0);
            mContactForm.setIdType(visitors.getCertificateType());
            notifyItemChanged(startContactIndex);
        }
    }

    private OrderInvoiceForm mOrderInvoiceForm;

    public void setOrderInvoiceForm(OrderInvoiceForm mOrderInvoiceForm) {
        this.mOrderInvoiceForm = mOrderInvoiceForm;
        notifyDataSetChanged();
    }

    private int usableCouponSize = 0;//可用优惠券数量
    private MyCouponBean myCouponBean;//优惠券

    public void setMyCouponBean(MyCouponBean myCouponBean, int usableCouponSize) {
        this.usableCouponSize = usableCouponSize;
        this.myCouponBean = myCouponBean;
        notifyDataSetChanged();
    }

    public void setMyCarBean(CarForm carForm) {
        endCarIndex = startCarIndex;
        datas.add(startCarIndex, carForm);
        startContactIndex++;
        notifyDataSetChanged();
    }

    public void setMyCarBeanList(List<CarForm> carForms) {
        List<CarForm> list = getCarFormList(false);
        datas.removeAll(list);
        endCarIndex = startCarIndex + carForms.size();
        datas.addAll(startCarIndex, carForms);
        startContactIndex = endCarIndex+1;
        notifyDataSetChanged();
    }


    public int startVisitorIndex = 0;//出游人开始位置
    private int endVisitorIndex = 0;//出游人结束位置
    private int startCarIndex = 0;//车辆信息开始位置
    private int endCarIndex = 0;//车辆信息结束位置
    public int startContactIndex = 0;//预订人信息的位置

    @Override
    public int getItemCount() {
        return this.datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        if (position < 0){ // TODO 因为经常出现崩溃是因为position为-1 所以加了这个判断
            return null;
        }
        if (position < datas.size()) {
            return datas.get(position);
        }
        return null;
    }

    /**
     * 获取车俩表单信息
     *@param isAddCar true表示添加车辆，false表示提交订单
     * @return
     */
    public List<CarForm> getCarFormList(boolean isAddCar) {
        List<CarForm> list = new ArrayList();
        for (int i = startCarIndex; i <= endCarIndex; i++) {
            Object obj = getItem(i);
            if (obj instanceof CarForm) {
                CarForm mCarForm = (CarForm) obj;
                if(isAddCar){
                    list.add((CarForm) obj);
                }else {
                    String carNumber = mCarForm.getCarNumber();
                    if (!TextUtils.isEmpty(carNumber)) {
                        list.add(mCarForm);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 获取车俩表单信息
     * @return
     */
    public ArrayList<String> getCarFormList() {
        ArrayList<String> list = new ArrayList();
        for (int i = startCarIndex; i <= endCarIndex; i++) {
            Object obj = getItem(i);
            if (obj instanceof CarForm) {
                CarForm mCarForm = (CarForm) obj;
                list.add(mCarForm.getCarNumber());
            }
        }
        return list;
    }

    /**
     * 判断车辆信息是否填写
     *
     * @param isAddCar true表示添加车辆判断，false表示提交订单判断
     * @return
     */
    public boolean isCarFormFillin(boolean isAddCar) {
        boolean isCarFormFillin = true;
        List<CarForm> list = getCarFormList(isAddCar);
        for (int i = 0; i < list.size(); i++) {
            CarForm mCarForm = list.get(i);
            String carNumber = mCarForm.getCarNumber();
            if (!MatcherUtils.isCarCard(carNumber)) {
                isCarFormFillin = false;
                ToastUtils.showShort("请填写有效车牌号");
                break;
            }
        }
        return isCarFormFillin;
    }

    /**
     * 获取出有人表单信息
     *
     * @return
     */
    public List<ContactForm> getVisitorList() {
        List<ContactForm> list = new ArrayList();
        for (int i = startVisitorIndex; i <= endVisitorIndex; i++) {
            Object obj = getItem(i);
            if (obj instanceof ContactForm) {
                list.add((ContactForm) obj);
            }
        }
        return list;
    }

    /**
     * 获取预定人表单信息
     *
     * @return
     */
    public ContactForm getContactForm() {
        Object obj = getItem(startContactIndex);
        if (obj instanceof ContactForm) {
            return (ContactForm) obj;
        }
        return null;
    }

    private boolean isAgreeProtocol = true;

    /**
     * 获取订单表单信息
     *
     * @return
     */
    public LineOrderForm getLineOrderForm() {
        if (mPersonCountBean == null) {
            return null;
        }
        List<ContactForm> visitorList = getVisitorList();//获取出游人
        List<CostForm> cost = new ArrayList<>();//费用
        List<ContactForm> xhList = new ArrayList<>();//小孩列表
        List<ContactForm> crList = new ArrayList<>();//成人列表
        boolean isSelectVisitors = true;
        for (int i = 0; i < visitorList.size(); i++) {
            ContactForm mContactForm = visitorList.get(i);
            if (TextUtils.isEmpty(mContactForm.getName())) {
                isSelectVisitors = false;
                break;
            }
            if (i < mPersonCountBean.getCrCount()) {
                crList.add(mContactForm);
            } else {
                xhList.add(mContactForm);
            }
        }
        if (!isSelectVisitors) {
            ToastUtils.showShort("请选择出行人信息");
            return null;
        }
        cost.add(new CostForm(1, mPersonCountBean.getCrCount(), crList));
        cost.add(new CostForm(2, mPersonCountBean.getXhCount(), xhList));
        cost.add(new CostForm(3, mPersonCountBean.getFangCount(), null));
        List<CarForm> carList = getCarFormList(false);//车辆信息
        ContactForm bookPeople = getContactForm();//联系人
        if (TextUtils.isEmpty(bookPeople.getName())){
            ToastUtils.showShort("请填写联系人信息");
            return null;
        }
        if (!MatcherUtils.isMobile(bookPeople.getPhone())){
            ToastUtils.showShort("请填写正确的联系人电话");
            return null;
        }
        LineOrderForm mLineOrderForm = new LineOrderForm();
        mLineOrderForm.setCost(cost);
        mLineOrderForm.setCarList(carList);
        mLineOrderForm.setBookPeople(bookPeople);
        mLineOrderForm.setDepartDate(mPersonCountBean.getTime());
        if (mOrderInvoiceForm != null && mOrderInvoiceForm.getInvoiceTitleId() > 0) {
            mLineOrderForm.setOpenInvoice(1);
            mLineOrderForm.setInvoice(mOrderInvoiceForm);
        } else {
            mLineOrderForm.setOpenInvoice(0);
        }
        if (myCouponBean != null) {
            mLineOrderForm.setCouponId("" + myCouponBean.getCouponId());
        }
        if (!isAgreeProtocol) {
            ToastUtils.showShort("请先勾选同意相关条款协议");
            return null;
        }
        if(tourInsurance!=null&&tourInsurance.isBuy()){
            //是否购买保险 1 是 2 否
            mLineOrderForm.setIsBuyInsurance(1);
        }else {
            mLineOrderForm.setIsBuyInsurance(2);
        }
        return mLineOrderForm;
    }

    public double getTourInsuranceAmount() {
        double tourInsuranceAmount=0;
        if(tourInsurance!=null&&tourInsurance.isBuy()){
            List<ContactForm> visitorList=getVisitorList();
            for (ContactForm visitor:visitorList){
                if(!TextUtils.isEmpty(visitor.getIdCard())){
                    if(visitor.isThanAge(tourInsurance.getAgeLimit())){
                        tourInsuranceAmount=tourInsuranceAmount+tourInsurance.getInsuranceAmount()*tourInsurance.getAmountMultiple();
                        mPersonCountBean.setBigAgeCount(mPersonCountBean.getBigAgeCount()+1);
                    }else {
                        tourInsuranceAmount=tourInsuranceAmount+tourInsurance.getInsuranceAmount();
                    }
                }
            }
        }else {
            mPersonCountBean.setBigAgeCount(0);
        }
        return tourInsuranceAmount;
    }
    public double getAmount() {
        if (mPersonCountBean == null) {
            return 0;
        }
        TimePriceDayBean.Ext ext = mPersonCountBean.getExt();
        if(ext==null){
            return 0;
        }
        double tourInsuranceAmount=getTourInsuranceAmount();
        double amount = mPersonCountBean.getCrCount() * ext.getJcrPrice()
                + mPersonCountBean.getXhCount() * ext.getJxhPrice()
                + mPersonCountBean.getFangCount() * ext.getFangPrice()
                +tourInsuranceAmount;
        return amount;
    }

    @Override
    public int getItemViewType(int position) {
        Object obj = getItem(position);
        if (obj instanceof RoutePage.ItemsBean) {
            return TYPE_ITEM_1;
        } else if (obj instanceof RouteDetailCategory) {
            return TYPE_ITEM_2;
        } else if (obj instanceof ContactForm) {
            ContactForm mContactForm = (ContactForm) obj;
            if (mContactForm.isContact()) {
                return TYPE_ITEM_6;
            } else {
                return TYPE_ITEM_3;
            }
        } else if (obj instanceof CarForm) {
            return TYPE_ITEM_4;
        } else if ("添加更多车辆信息".equals(obj)) {
            return TYPE_ITEM_5;
        } else if ("保险".equals(obj)) {
            return TYPE_ITEM_6_1;
        } else if ("优惠".equals(obj)) {
            return TYPE_ITEM_7;
        } else if ("发票".equals(obj)) {
            return TYPE_ITEM_8;
        } else if ("我已同意".equals(obj)) {
            return TYPE_ITEM_9;
        }
        return 0;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("shyong", "viewType=" + viewType);
        ItemHolder holder = null;
        if (viewType == TYPE_ITEM_1) {
            holder = new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_fillin_order_1, parent, false));
        } else if (viewType == TYPE_ITEM_2) {
            holder = new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_fillin_order_2, parent, false));
        } else if (viewType == TYPE_ITEM_3) {
            holder = new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_fillin_order_3, parent, false));
        } else if (viewType == TYPE_ITEM_4) {
            holder = new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_fillin_order_4, parent, false));
        } else if (viewType == TYPE_ITEM_5) {
            holder = new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_fillin_order_5, parent, false));
        } else if (viewType == TYPE_ITEM_6) {
            holder = new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_fillin_order_6, parent, false));
        } else if (viewType == TYPE_ITEM_6_1) {
            holder = new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_fillin_order_6_1, parent, false));
        } else if (viewType == TYPE_ITEM_7) {
            holder = new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_fillin_order_7, parent, false));
        } else if (viewType == TYPE_ITEM_8) {
            holder = new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_fillin_order_8, parent, false));
        } else if (viewType == TYPE_ITEM_9) {
            holder = new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_route_fillin_order_9, parent, false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.setViewDate(position);
    }

    class ItemHolder<DB extends ViewDataBinding> extends RecyclerView.ViewHolder {
        private DB dbBinding;

        public ItemHolder(DB dbBinding) {
            super(dbBinding.getRoot());
            this.dbBinding = dbBinding;
            if (dbBinding instanceof ItemRouteFillinOrder2Binding) {
                ItemRouteFillinOrder2Binding itemBinding = (ItemRouteFillinOrder2Binding) this.dbBinding;
                itemBinding.ivIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        Object obj = getItem(position);
                        if (obj instanceof RouteDetailCategory) {
                            RouteDetailCategory category = (RouteDetailCategory) obj;
                            if ("出行人信息".equals(category.getName())) {
                                Intent intent = new Intent();
                                intent.setClass(v.getContext(), SelectVisitorsActivity.class);
                                intent.putExtra("A", mPersonCountBean.getCrCount());
                                intent.putExtra("B", mPersonCountBean.getXhCount());
                                intent.putExtra("C", 1);
                                if (selectOftenVisitorsList == null) {
                                    selectOftenVisitorsList = new ArrayList<>();
                                }
                                EventBus.getDefault().postSticky(selectOftenVisitorsList);
                                v.getContext().startActivity(intent);
                            }
                        }
                    }
                });

            } else if (dbBinding instanceof ItemRouteFillinOrder3Binding) {
                ItemRouteFillinOrder3Binding itemBinding = (ItemRouteFillinOrder3Binding) this.dbBinding;
                itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(v.getContext(), SelectVisitorsActivity.class);
                        intent.putExtra("A", mPersonCountBean.getCrCount());
                        intent.putExtra("B", mPersonCountBean.getXhCount());
                        intent.putExtra("C", 1);
                        if (selectOftenVisitorsList == null) {
                            selectOftenVisitorsList = new ArrayList<>();
                        }
                        EventBus.getDefault().postSticky(selectOftenVisitorsList);
                        v.getContext().startActivity(intent);
                    }
                });

            } else if (dbBinding instanceof ItemRouteFillinOrder4Binding) {
                ItemRouteFillinOrder4Binding itemBinding = (ItemRouteFillinOrder4Binding) this.dbBinding;
                itemBinding.ivDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        Object obj = getItem(position);
                        datas.remove(position);
                        endCarIndex--;
                        startContactIndex--;
                        notifyDataSetChanged();
                    }
                });

//                itemBinding.etCarNum.addTextChangedListener(new TextChangedListener() {
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                        int position = getAdapterPosition();
//                        Object obj = getItem(position);
//                        if (obj instanceof CarForm) {
//                            CarForm mCarForm = (CarForm) obj;
//                            mCarForm.setCarNumber(s.toString());
//                        }
//                    }
//                });
//
//                itemBinding.etCarNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                    @Override
//                    public void onFocusChange(View view, boolean b) {
//                        if (b) {
//                            if(isSoftShowing()){
//                                hideSoftKeyboard(ActivityUtils.getTopActivity());
//                            }
//                            if (keyboardUtil == null) {
//                                keyboardUtil = new KeyboardUtil(ActivityUtils.getTopActivity(), itemBinding.etCarNum);
//                                keyboardUtil.hideSoftInputMethod();
//                                keyboardUtil.showKeyboard();
//                            } else {
//                                keyboardUtil.setmEdit(itemBinding.etCarNum);
//                                keyboardUtil.hideSoftInputMethod();
//                                keyboardUtil.showKeyboard();
//                            }
//                            if(keyboardLayout!=null){
//                                keyboardLayout.setVisibility(View.VISIBLE);
//                            }
//                        } else {
//                            keyboardUtil.hideKeyboard();
//                            if(keyboardLayout!=null){
//                                keyboardLayout.setVisibility(View.GONE);
//                            }
//                        }
//                    }
//                });
//                itemBinding.etCarNum.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(keyboardUtil!=null&&!keyboardUtil.isShow() ){
//                            keyboardUtil.setmEdit(itemBinding.etCarNum);
//                            keyboardUtil.hideSoftInputMethod();
//                            keyboardUtil.showKeyboard();
//                        }
//                    }
//                });
                itemBinding.etRemark.addTextChangedListener(new TextChangedListener() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        int position = getAdapterPosition();
                        Object obj = getItem(position);
                        if (obj instanceof CarForm) {
                            CarForm mCarForm = (CarForm) obj;
                            mCarForm.setRemark(s.toString());
                        }
                    }
                });
                itemBinding.carTypeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemListener != null) {
                            itemListener.onItemClick(v, getAdapterPosition());
                        }
                    }
                });
            } else if (dbBinding instanceof ItemRouteFillinOrder5Binding) {
                ItemRouteFillinOrder5Binding itemBinding = (ItemRouteFillinOrder5Binding) this.dbBinding;
                itemBinding.addCarInfoLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IntentManager.getInstance().goCommonVehiclesActivity(ActivityUtils.getTopActivity(),true,true,getCarFormList());
//                        int position = getAdapterPosition();
//                        if(startCarIndex==0){
//                            startCarIndex = position;
//                            endCarIndex = startCarIndex;
//                            datas.add(startCarIndex, new CarForm());
//                            startContactIndex++;
//                            notifyDataSetChanged();
//                        }else {
//                            if (isCarFormFillin(true)) {
//                                datas.add(endCarIndex + 1, new CarForm());
//                                endCarIndex = position;
//                                startContactIndex++;
//                                notifyDataSetChanged();
//                            }
//                        }
                    }
                });
            } else if (dbBinding instanceof ItemRouteFillinOrder6Binding) {
                ItemRouteFillinOrder6Binding itemBinding = (ItemRouteFillinOrder6Binding) this.dbBinding;
                itemBinding.ivIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(v.getContext(), SelectVisitorsActivity.class);
                        intent.putExtra("A", 1);
                        intent.putExtra("B", 0);
                        intent.putExtra("C", 2);
                        if (selectOftenVisitors == null) {
                            EventBus.getDefault().postSticky(new OftenVisitorsBean());
                        } else {
                            EventBus.getDefault().postSticky(selectOftenVisitors);
                        }
                        v.getContext().startActivity(intent);
                    }
                });
                itemBinding.etName.addTextChangedListener(new TextChangedListener() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        int position = getAdapterPosition();
                        Object obj = getItem(position);
                        if (obj instanceof ContactForm) {
                            ContactForm mContactForm = (ContactForm) obj;
                            mContactForm.setName(s.toString());
                        }
                    }
                });
                itemBinding.etPhone.addTextChangedListener(new TextChangedListener() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        int position = getAdapterPosition();
                        Object obj = getItem(position);
                        if (obj instanceof ContactForm) {
                            ContactForm mContactForm = (ContactForm) obj;
                            mContactForm.setPhone(s.toString());
                            if(TextUtils.isEmpty(mContactForm.getPhone())){
                                itemBinding.ivClear.setVisibility(View.GONE);
                            }else {
                                itemBinding.ivClear.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
                itemBinding.ivClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        Object obj = getItem(position);
                        if (obj instanceof ContactForm) {
                            ContactForm mContactForm = (ContactForm) obj;
                            mContactForm.setPhone("");
                            itemBinding.etPhone.setText("");
                        }
                        itemBinding.ivClear.setVisibility(View.GONE);
                    }
                });
            }else if (dbBinding instanceof ItemRouteFillinOrder61Binding) {
                ItemRouteFillinOrder61Binding itemBinding = (ItemRouteFillinOrder61Binding) this.dbBinding;
                itemBinding.ivCheckInsurance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mPersonCountBean!=null&&selectOftenVisitorsList != null && selectOftenVisitorsList.size() == (mPersonCountBean.getCrCount()+mPersonCountBean.getXhCount())) {
                            if (tourInsurance != null) {
                                tourInsurance.setBuy(!tourInsurance.isBuy());
                                if (tourInsurance.isBuy()) {
                                    itemBinding.ivCheckInsurance.setImageResource(R.drawable.icon_check_yx_duigou);
                                } else {
                                    itemBinding.ivCheckInsurance.setImageResource(R.drawable.icon_check_yx_un);
                                }
                                if (itemListener != null) {
                                    itemListener.onItemClick(v, getAdapterPosition());
                                }
                            }
                        } else {
                            ToastUtils.showShort("请完成出行人信息");
                        }
                    }
                });
            } else if (dbBinding instanceof ItemRouteFillinOrder7Binding) {
                ItemRouteFillinOrder7Binding itemBinding = (ItemRouteFillinOrder7Binding) this.dbBinding;
                itemBinding.yhqLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(v.getContext(), CouponUseActivity.class);
                        intent.putExtra("A", 1);//优惠券类型 (0:通用,1:线路,2:酒店,3:门票)
                        intent.putExtra("B", getAmount());//优惠券类型 (0:通用,1:线路,2:酒店,3:门票)
                        if (myCouponBean != null) {
                            intent.putExtra("C", myCouponBean.getCouponId());//优惠券类id
                        }
                        v.getContext().startActivity(intent);
                    }
                });

            } else if (dbBinding instanceof ItemRouteFillinOrder8Binding) {
                ItemRouteFillinOrder8Binding itemBinding = (ItemRouteFillinOrder8Binding) this.dbBinding;
                itemBinding.fpLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(v.getContext(), RouteFillinInvoiceActivity.class);
                        intent.putExtra("A", 1);//线路订单填写发票
                        v.getContext().startActivity(intent);
                    }
                });

            } else if (dbBinding instanceof ItemRouteFillinOrder9Binding) {
                ItemRouteFillinOrder9Binding itemBinding = (ItemRouteFillinOrder9Binding) this.dbBinding;
                itemBinding.ivProtocol.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isAgreeProtocol) {
                            isAgreeProtocol = false;
                        } else {
                            isAgreeProtocol = true;
                        }
                        itemBinding.ivProtocol.setSelected(isAgreeProtocol);
                    }
                });
                itemBinding.tvTgzc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //退改政策
                        Intent intent = new Intent(v.getContext(), WebActivity.class);
                        intent.putExtra("title", "《退改政策》");
                        intent.putExtra("url", Constants.getXzUrl(5));
                        v.getContext().startActivity(intent);
                    }
                });
                itemBinding.tvHtxy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //合同/协议
                        Intent intent = new Intent(v.getContext(), WebActivity.class);
                        intent.putExtra("title", "《合同/协议》");
                        intent.putExtra("url", Constants.getXzUrl(17));
                        v.getContext().startActivity(intent);
                    }
                });
            }
        }

        private void setViewDate(int position) {
            if (dbBinding instanceof ItemRouteFillinOrder1Binding) {
                ItemRouteFillinOrder1Binding itemBinding = (ItemRouteFillinOrder1Binding) this.dbBinding;
                Object obj = getItem(position);
                if (obj instanceof RoutePage.ItemsBean) {
                    itemBinding.setItem((RoutePage.ItemsBean) obj);
                }
            } else if (dbBinding instanceof ItemRouteFillinOrder2Binding) {
                ItemRouteFillinOrder2Binding itemBinding = (ItemRouteFillinOrder2Binding) this.dbBinding;
                Object obj = getItem(position);
                if (obj instanceof RouteDetailCategory) {
                    RouteDetailCategory category = (RouteDetailCategory) obj;
                    itemBinding.setItem(category);
                    itemBinding.ivIcon.setImageResource(category.getIcon());
                }
            } else if (dbBinding instanceof ItemRouteFillinOrder3Binding) {
                ItemRouteFillinOrder3Binding itemBinding = (ItemRouteFillinOrder3Binding) this.dbBinding;
                if (endVisitorIndex > 0 && position == endVisitorIndex) {
                    itemBinding.viewBottomC.setVisibility(View.VISIBLE);
                    itemBinding.viewLine.setVisibility(View.INVISIBLE);
                } else {
                    itemBinding.viewBottomC.setVisibility(View.GONE);
                    itemBinding.viewLine.setVisibility(View.VISIBLE);
                }
                Object obj = getItem(position);
                if (obj instanceof ContactForm) {
                    itemBinding.setItem((ContactForm) obj);
                }
            } else if (dbBinding instanceof ItemRouteFillinOrder4Binding) {
                ItemRouteFillinOrder4Binding itemBinding = (ItemRouteFillinOrder4Binding) this.dbBinding;
                Object obj = getItem(position);
                if (obj instanceof CarForm) {
                    CarForm mCarForm = (CarForm) obj;
                    itemBinding.setItem(mCarForm);
//                    if (position == startCarIndex) {
//                        itemBinding.ivDel.setVisibility(View.GONE);
//                    } else {
                        itemBinding.ivDel.setVisibility(View.VISIBLE);
//                    }
                }
            } else if (dbBinding instanceof ItemRouteFillinOrder5Binding) {
                ItemRouteFillinOrder5Binding itemBinding = (ItemRouteFillinOrder5Binding) this.dbBinding;
                if(startCarIndex==0){
                    itemBinding.tvAddCar.setText("添加车辆信息");
                }else {
                    itemBinding.tvAddCar.setText("添加更多车辆信息");
                }
                Object obj = getItem(position);
                if (obj instanceof String) {
                }
            } else if (dbBinding instanceof ItemRouteFillinOrder6Binding) {
                ItemRouteFillinOrder6Binding itemBinding = (ItemRouteFillinOrder6Binding) this.dbBinding;
                Object obj = getItem(position);
                if (obj instanceof ContactForm) {
                    ContactForm mContactForm=(ContactForm) obj;
                    itemBinding.setItem(mContactForm);
                    if(TextUtils.isEmpty(mContactForm.getPhone())){
                        itemBinding.ivClear.setVisibility(View.GONE);
                    }else {
                        itemBinding.ivClear.setVisibility(View.VISIBLE);
                    }
                }
            } else if (dbBinding instanceof ItemRouteFillinOrder61Binding) {
                ItemRouteFillinOrder61Binding itemBinding = (ItemRouteFillinOrder61Binding) this.dbBinding;
                Object obj = getItem(position);
                if("保险".equals(obj)&&tourInsurance!=null) {
                    itemBinding.tvInsurance.setText(tourInsurance.getInsuranceNameUnder());
//                    itemBinding.tvInsuranceHint.setText(tourInsurance.getInsuranceNameOver());
                    itemBinding.tvInsuranceHint.setText("若出行人中有75周岁以上客人，保险费为以上价格的1.5倍！");
                    itemBinding.tvInsurancePrice.setText(tourInsurance.getInsuranceAmounts());
                    if(tourInsurance.isBuy()){
                        itemBinding.ivCheckInsurance.setImageResource(R.drawable.icon_check_yx_duigou);
                    }else {
                        itemBinding.ivCheckInsurance.setImageResource(R.drawable.icon_check_yx_un);
                    }
                }
            }else if (dbBinding instanceof ItemRouteFillinOrder7Binding) {
                ItemRouteFillinOrder7Binding itemBinding = (ItemRouteFillinOrder7Binding) this.dbBinding;
                Object obj = getItem(position);
                if ("优惠".equals(obj)) {
                    if (usableCouponSize > 0 && myCouponBean != null) {
                        String maxSubtract= MathUtil.formatDouble(myCouponBean.getMaxSubtract(),2);
                        itemBinding.tvCouponPrice.setText("-¥" + maxSubtract);
                        itemBinding.tvTvCouponUsableCount.setText("有" + usableCouponSize + "张优惠券可用");
                    } else {
                        itemBinding.tvCouponPrice.setText("");
                        if (usableCouponSize > 0) {
                            itemBinding.tvTvCouponUsableCount.setText("不使用优惠券");
                        } else {
                            itemBinding.tvTvCouponUsableCount.setText("");
                        }
                    }
                }
            } else if (dbBinding instanceof ItemRouteFillinOrder8Binding) {
                ItemRouteFillinOrder8Binding itemBinding = (ItemRouteFillinOrder8Binding) this.dbBinding;
                Object obj = getItem(position);
                if (mOrderInvoiceForm != null) {
                    itemBinding.setItem(mOrderInvoiceForm);
                }
            } else if (dbBinding instanceof ItemRouteFillinOrder9Binding) {
                ItemRouteFillinOrder9Binding itemBinding = (ItemRouteFillinOrder9Binding) this.dbBinding;
                itemBinding.ivProtocol.setSelected(isAgreeProtocol);
            }
        }
    }
}