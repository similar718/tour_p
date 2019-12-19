package cn.xmzt.www.dialog;

import android.app.Dialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.DialogRoutePickDetailBinding;
import cn.xmzt.www.route.bean.PersonCountBean;
import cn.xmzt.www.route.bean.TimePriceDayBean;
import cn.xmzt.www.route.bean.TourInsurance;
import cn.xmzt.www.utils.MathUtil;


/**
 * 线路选择明细Dialog
 */
public class RoutePickDetailDialog extends Dialog {
	private Context context;
	private View.OnClickListener onClickListener;
	public RoutePickDetailDialog(Context context) {
		super(context, R.style.dialog_custom);
		this.context = context;
	}
	DialogRoutePickDetailBinding binding;
	public RoutePickDetailDialog(Context context, View.OnClickListener onClickListener) {
		super(context, R.style.dialog_custom);
		this.context = context;
		this.onClickListener = onClickListener;
		binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_route_pick_detail, null, false);
		setContentView(binding.getRoot());
		initView();
	}

	protected void initView() {
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.gravity = Gravity.BOTTOM; // 设置重力
		//lp.y = -mScreenHeight / 10;// 设置y坐标
		lp.width= WindowManager.LayoutParams.MATCH_PARENT;
		dialogWindow.setAttributes(lp);
		binding.ivCancel.setOnClickListener(onClickListener);
		binding.tvDetail.setOnClickListener(onClickListener);
		binding.tvReserve.setOnClickListener(onClickListener);
		setCancelable(false);
	}
    public void setViewData(TimePriceDayBean.Ext ext, PersonCountBean mPersonCountBean, double yhqPrice, TourInsurance tourInsurance) {
        double jbfyPrice=mPersonCountBean.getCrCount()*ext.getJcrPrice()
                +mPersonCountBean.getXhCount()*ext.getJxhPrice();
        double kxcpPrice=mPersonCountBean.getFangCount()*ext.getFangPrice();
		double tourInsuranceAmount=0;

        binding.tvJbfy.setText(MathUtil.formatDouble(jbfyPrice,2));
        binding.tvCrPrice.setText(MathUtil.formatDouble(ext.getJcrPrice(),2)+"/人  x"+mPersonCountBean.getCrCount());
        binding.tvXhPrice.setText(MathUtil.formatDouble(ext.getJxhPrice(),2)+"/人  x"+mPersonCountBean.getXhCount());
        binding.tvFangPrice.setText(MathUtil.formatDouble(ext.getFangPrice(),2)+"/人  x"+mPersonCountBean.getFangCount());
        int unBigAgeCount = 0;
		double CrPrice = 0.00;
		double LrPrice = 0.00;
		if(tourInsurance!=null&&tourInsurance.isBuy()){
			binding.bxLayout.setVisibility(View.VISIBLE);
			binding.bigAgeBXLayout.setVisibility(View.VISIBLE);
			int bigAgeCount=mPersonCountBean.getBigAgeCount();
			unBigAgeCount=mPersonCountBean.getCrCount()+mPersonCountBean.getXhCount()-bigAgeCount;
			binding.tvBxPrice.setText(MathUtil.formatDouble(tourInsurance.getInsuranceAmount(),2)+"/人  x"+unBigAgeCount);
			binding.tvBigAgeBXPrice.setText(MathUtil.formatDouble(tourInsurance.getInsuranceAmount()*tourInsurance.getAmountMultiple(),2)+"/人  x"+bigAgeCount);
			if(unBigAgeCount<=0){
				binding.bxLayout.setVisibility(View.GONE);
			}
			if(bigAgeCount<=0){
				binding.bigAgeBXLayout.setVisibility(View.GONE);
			}
			CrPrice = tourInsurance.getInsuranceAmount()*unBigAgeCount;
			LrPrice = tourInsurance.getInsuranceAmount()*tourInsurance.getAmountMultiple() * bigAgeCount;
			tourInsuranceAmount=tourInsurance.getInsuranceAmount()*unBigAgeCount+tourInsurance.getInsuranceAmount()*tourInsurance.getAmountMultiple()*bigAgeCount;
			String amount= MathUtil.formatDouble(tourInsuranceAmount+jbfyPrice+kxcpPrice-yhqPrice,2);
			binding.tvAmount.setText(amount);
		}else {
			String amount= MathUtil.formatDouble(jbfyPrice+kxcpPrice-yhqPrice,2);
			binding.tvAmount.setText(amount);
			binding.bxLayout.setVisibility(View.GONE);
			binding.bigAgeBXLayout.setVisibility(View.GONE);
		}
		binding.tvKxcpPrice.setText(MathUtil.formatDouble(kxcpPrice+tourInsuranceAmount,2));
        if(mPersonCountBean.getXhCount()>0){
			binding.xhLayout.setVisibility(View.VISIBLE);
		}else {
			binding.xhLayout.setVisibility(View.GONE);
		}

		binding.fangLayout.setVisibility(View.GONE);
		if(mPersonCountBean.getFangCount()>0){
			binding.fangLayout.setVisibility(View.VISIBLE);
		}
        if(mPersonCountBean.getFangCount()>0||tourInsuranceAmount>0){
			binding.cpLayout.setVisibility(View.VISIBLE);
			binding.cpLineView.setVisibility(View.VISIBLE);
		}else {
			binding.cpLayout.setVisibility(View.GONE);
			binding.cpLineView.setVisibility(View.GONE);
		}
        if (binding.cpLayout.getVisibility() != View.VISIBLE){
			if (binding.bxLayout.getVisibility() == View.VISIBLE){
				binding.cpLayout.setVisibility(View.VISIBLE);
				String price = MathUtil.formatDouble(LrPrice+CrPrice,2);
				binding.tvKxcpPrice.setText(price);
			} else {
				binding.cpLayout.setVisibility(View.GONE);
			}
			if (binding.bigAgeBXLayout.getVisibility() == View.VISIBLE){
				binding.cpLayout.setVisibility(View.VISIBLE);
				String price = MathUtil.formatDouble(LrPrice+CrPrice,2);
				binding.tvKxcpPrice.setText(price);
			} else {
				binding.cpLayout.setVisibility(View.GONE);
			}
		} else {
			String totalprice = MathUtil.formatDouble(kxcpPrice + CrPrice + LrPrice,2);
			binding.tvKxcpPrice.setText(totalprice);
		}
		if(yhqPrice>0){
			binding.vYhqLine.setVisibility(View.VISIBLE);
			binding.tvYhqLayout.setVisibility(View.VISIBLE);
			binding.tvYhqPrice.setText("-¥"+MathUtil.formatDouble(yhqPrice,2));
		}else {
			binding.vYhqLine.setVisibility(View.GONE);
			binding.tvYhqLayout.setVisibility(View.GONE);
		}
    }
}
