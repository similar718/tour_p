package cn.xmzt.www.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.DialogCustomizePersonNumBinding;
import cn.xmzt.www.utils.ToastUtils;


/**
 * 定制界面的设置人数dialog
 */
public class CustomizePersonNumDialog extends Dialog {
	private Context mContext;

	private View.OnClickListener onClickListener;

	public CustomizePersonNumDialog(Context context) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
	}

	DialogCustomizePersonNumBinding binding;
	private int personNum = 0;
	private int childNum = 0;

	public CustomizePersonNumDialog(Context context, View.OnClickListener onClickListener,CustomizePersonNumClickListener listeners) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
		this.listener = listeners;
		this.onClickListener = onClickListener;
		binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_customize_person_num, null, false);
		setContentView(binding.getRoot());
		initView();
	}

	protected void initView() {
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.gravity = Gravity.BOTTOM; // 设置重力
		//lp.y = -mScreenHeight / 10;// 设置y坐标
		lp.width= WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		dialogWindow.setAttributes(lp);

		// 取消
		binding.ivCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});
		binding.ivChildAdd.setOnClickListener(onClickListener);
		binding.ivChildReduce.setOnClickListener(onClickListener);
		binding.ivPersonAdd.setOnClickListener(onClickListener);
		binding.ivPersonReduce.setOnClickListener(onClickListener);

		binding.etChildNum.addTextChangedListener(textWatcherChild);
		binding.etPersonNum.addTextChangedListener(textWatcherPerson);
		setCancelable(true);
	}

    public void setViewData(int personNum,int childNum) {
		mIsSettingC = true;
		mIsSettingP = true;
		this.personNum = personNum;
		this.childNum = childNum;
		if (personNum > 0){
			binding.ivPersonReduce.setImageResource(R.drawable.dialog_customize_person_num_reduce);
		} else {
			binding.ivPersonReduce.setImageResource(R.drawable.dialog_customize_person_num_reduced);
		}
		if (childNum > 0){
			binding.ivChildReduce.setImageResource(R.drawable.dialog_customize_person_num_reduce);
		} else {
			binding.ivChildReduce.setImageResource(R.drawable.dialog_customize_person_num_reduced);
		}
		binding.etPersonNum.setText(personNum+"");
		binding.etChildNum.setText(childNum+"");
    }

    private boolean mIsSettingP = true;
    private boolean mIsSettingC = true;

    private TextWatcher textWatcherPerson = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			if (mIsSettingP){
				mIsSettingP = false;
				return;
			}
			String data = binding.etPersonNum.getText().toString().trim();
			int num = Integer.parseInt(TextUtils.isEmpty(data) ? "0" : data);
			if (num > 99){
				num = 99;
				mIsSettingP = true;
				ToastUtils.showText(mContext,"最多设置99人");
				binding.etPersonNum.setText(num+"");
				binding.ivPersonReduce.setImageResource(R.drawable.dialog_customize_person_num_reduce);
			} else if (num == 0){
				num = 0;
				mIsSettingP = true;
				binding.etPersonNum.setText(num+"");
				binding.ivPersonReduce.setImageResource(R.drawable.dialog_customize_person_num_reduced);
			} else {
				mIsSettingP = true;
				binding.etPersonNum.setText(num+"");
				binding.ivPersonReduce.setImageResource(R.drawable.dialog_customize_person_num_reduce);
			}
			personNum = num;
			listener.requestInputInfo(personNum,childNum);
		}
	};

    private TextWatcher textWatcherChild = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			if (mIsSettingC){
				mIsSettingC = false;
				return;
			}
			String data = binding.etChildNum.getText().toString().trim();
			int num = Integer.parseInt(TextUtils.isEmpty(data) ? "0" : data);
			if (num > 99){
				num = 99;
				mIsSettingC = true;
				ToastUtils.showText(mContext,"最多设置99人");
				binding.etChildNum.setText(num+"");
				binding.ivChildReduce.setImageResource(R.drawable.dialog_customize_person_num_reduce);
			} else if (num == 0){
				num = 0;
				mIsSettingC = true;
				binding.etChildNum.setText(num+"");
				binding.ivChildReduce.setImageResource(R.drawable.dialog_customize_person_num_reduced);
			} else {
				mIsSettingC = true;
				binding.etChildNum.setText(num+"");
				binding.ivChildReduce.setImageResource(R.drawable.dialog_customize_person_num_reduce);
			}
			childNum = num;
			listener.requestInputInfo(personNum,childNum);
		}
	};

    private CustomizePersonNumClickListener listener;

    public interface CustomizePersonNumClickListener{
    	// 监听输入框的变化
		void requestInputInfo(int personNum,int childNum);
	}
}
