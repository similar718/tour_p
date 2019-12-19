package cn.xmzt.www.dialog;

import android.app.Dialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.DialogSelectCarTypeBinding;
import cn.xmzt.www.route.adapter.SelectCarTypeAdapter;
import cn.xmzt.www.view.listener.ItemListener;


/**
 * 填写订单选择车辆类型Dialog
 */
public class SelectCarTypeDialog extends Dialog {
	private Context context;
	private SelectCarTypeAdapter adapter;
	private ItemListener itemListener;

	DialogSelectCarTypeBinding binding;
	public SelectCarTypeDialog(Context context) {
		super(context, R.style.dialog_custom);
		this.context = context;
		binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_select_car_type, null, false);
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
		binding.ivCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancel();
			}
		});

		GridLayoutManager manager= (GridLayoutManager) binding.recyclerView.getLayoutManager();
		manager.setSpanCount(1);
		adapter = new SelectCarTypeAdapter(getContext());
		binding.recyclerView.setAdapter(adapter);
		setCancelable(true);
	}
	/**
	 * 设置默认选中那个
	 *
	 * @param position
	 */
	public void setSelectPosition(int position) {
		adapter.setSelectPosition(position);
	}
	public String getItem(int position) {
		return adapter.getItem(position);
	}

	public void setItemListener(ItemListener itemListener) {
		this.itemListener = itemListener;
		adapter.setItemListener(itemListener);
	}
}
