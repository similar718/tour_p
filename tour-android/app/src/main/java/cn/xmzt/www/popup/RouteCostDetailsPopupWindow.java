package cn.xmzt.www.popup;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import androidx.databinding.DataBindingUtil;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.PopupwindowRouteCostDetailsBinding;
import cn.xmzt.www.route.bean.CostBean;
import cn.xmzt.www.route.bean.RouteOrderDetailBean;
import cn.xmzt.www.utils.MathUtil;
import cn.xmzt.www.utils.StatusBarUtil;

/**
 * @author shiyong
 * @describe 线路费用明细弹出框
 */

public class RouteCostDetailsPopupWindow extends PopupWindow {

    public RouteCostDetailsPopupWindow(Activity context) {
        this(context, null);
    }
    private int topMargin=0;
    PopupwindowRouteCostDetailsBinding binding;
    public RouteCostDetailsPopupWindow(Activity context, AttributeSet attrs) {
        super(context, attrs);
        topMargin=context.getResources().getDimensionPixelOffset(R.dimen.dp_136)- StatusBarUtil.getStatusBarHeight(context);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.popupwindow_route_cost_details, null, false);
        View view=binding.getRoot();

        setContentView(view);
        initView();
    }

    protected void initView() {
        //所有的弹出框的弹出动画
        setAnimationStyle(R.style.popup_window_anim_style);
        //设置点击空白处消失
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        FrameLayout.LayoutParams rootLayoutParams= (FrameLayout.LayoutParams) binding.rootLayout.getLayoutParams();
        rootLayoutParams.topMargin=topMargin;
        setFocusable(true);
        binding.cancelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    public void setViewData(RouteOrderDetailBean.Product product, RouteOrderDetailBean.OrderBean orderBean) {
        if(product==null||orderBean==null){
            return;
        }
        double yhqPrice=orderBean.getDiscountAmount();

        CostBean crCost=product.getAdult();
        CostBean xhCost=product.getChildren();
        CostBean fangCost=product.getSingleRoom();
        double crPrice=0;
        double xhPrice=0;
        double kxcpPrice=0;
        if(crCost!=null&&crCost.getQuantity()>0){
            binding.crLayout.setVisibility(View.VISIBLE);
            crPrice=crCost.getPrice()*crCost.getQuantity();

            binding.tvCrPrice.setText("¥"+MathUtil.formatDouble(crCost.getPrice(),2)+"/人 x"+crCost.getQuantity());
        }else {
            binding.crLayout.setVisibility(View.GONE);
        }
        if(xhCost!=null&&xhCost.getQuantity()>0){
            binding.xhLayout.setVisibility(View.VISIBLE);
            xhPrice=xhCost.getPrice()*xhCost.getQuantity();
            binding.tvXhPrice.setText("¥"+MathUtil.formatDouble(xhCost.getPrice(),2)+"/人 x"+xhCost.getQuantity());
        }else {
            binding.xhLayout.setVisibility(View.GONE);
        }

        binding.tvJbfy.setText(MathUtil.formatDouble(crPrice+xhPrice,2));
        if(fangCost!=null&&fangCost.getQuantity()>0){
            binding.dfcLayout.setVisibility(View.VISIBLE);
            binding.kxcpLayout.setVisibility(View.VISIBLE);
            kxcpPrice=fangCost.getPrice()*fangCost.getQuantity();
            binding.tvFangPrice.setText("¥"+MathUtil.formatDouble(fangCost.getPrice(),2)+"/人 x"+fangCost.getQuantity());
        }else {
            binding.dfcLayout.setVisibility(View.GONE);
            binding.kxcpLayout.setVisibility(View.GONE);
        }

        binding.tvYhqPrice.setText(MathUtil.formatDouble(yhqPrice,2));
        if(yhqPrice>0){
            binding.yhqLayout.setVisibility(View.VISIBLE);
        }else {
            binding.yhqLayout.setVisibility(View.GONE);
        }
        List<RouteOrderDetailBean.InsuranceCost> orderGuestInsurances= orderBean.getOrderGuestInsurances();//保险费用列表
        binding.bxLayout.setVisibility(View.GONE);
        binding.bigAgeBXLayout.setVisibility(View.GONE);

        double insurancePrice=0;
        for(int i=0;i<orderGuestInsurances.size();i++){
            RouteOrderDetailBean.InsuranceCost insurancecost=orderGuestInsurances.get(i);
            insurancePrice=insurancePrice+insurancecost.getInsurancePrice()*insurancecost.getInsuranceNum();
            if(insurancecost.getInsuranceType()==1){//1、表示75以上、2、表示75以下
                binding.bigAgeBXLayout.setVisibility(View.VISIBLE);
                binding.tvBigAgeBXPrice.setText("¥"+MathUtil.formatDouble(insurancecost.getInsurancePrice(),2)+"/人  x"+insurancecost.getInsuranceNum());
            }else if(insurancecost.getInsuranceType()==2){
                binding.bxLayout.setVisibility(View.VISIBLE);
                binding.tvBxPrice.setText("¥"+MathUtil.formatDouble(insurancecost.getInsurancePrice(),2)+"/人  x"+insurancecost.getInsuranceNum());
            }
        }
        kxcpPrice=kxcpPrice+insurancePrice;
        binding.tvKxcpPrice.setText(MathUtil.formatDouble(kxcpPrice,2));
        if(kxcpPrice>0){
            binding.kxcpLayout.setVisibility(View.VISIBLE);
        }
        String amount=MathUtil.formatDouble(crPrice+xhPrice+kxcpPrice-yhqPrice,2);
        binding.tvAmount.setText(amount);
    }
}
