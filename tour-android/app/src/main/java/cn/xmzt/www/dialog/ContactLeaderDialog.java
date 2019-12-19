package cn.xmzt.www.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.ActivityUtils;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.DialogContactLeaderListBinding;
import cn.xmzt.www.intercom.adapter.DialogContactLeaderAdapter;
import cn.xmzt.www.intercom.bean.GroupLeaderBean;
import cn.xmzt.www.view.listener.ItemListener;


/**
 * 联系领队的dialog
 */
public class ContactLeaderDialog extends Dialog implements ItemListener {
	private Context mContext;
	private DialogContactLeaderAdapter mAdaptr;
	private DialogContactLeaderListBinding binding;

	public ContactLeaderDialog(Context context) {
		super(context, R.style.dialog_custom);
		this.mContext = context;
		binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_contact_leader_list, null, false);
		setContentView(binding.getRoot());
		initView();
	}
	private List<GroupLeaderBean> leaderList;
	WindowManager.LayoutParams windowLayoutParams;
	protected void initView() {
		Window dialogWindow = getWindow();
		windowLayoutParams = dialogWindow.getAttributes();
		windowLayoutParams.gravity = Gravity.CENTER; // 设置重力
		//lp.y = -mScreenHeight / 10;// 设置y坐标
//		lp.width= WindowManager.LayoutParams.MATCH_PARENT;

		mAdaptr = new DialogContactLeaderAdapter(mContext);
		mAdaptr.setDatas(leaderList);
		mAdaptr.setItemListener(this);
		binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
		binding.recyclerView.setAdapter(mAdaptr);
		setCancelable(true);
		setCanceledOnTouchOutside(true);
	}
    public void setViewData(List<GroupLeaderBean> list) {
		this.leaderList = list;
		if(leaderList!=null&&leaderList.size()>4){
			windowLayoutParams.height = getContext().getResources().getDimensionPixelOffset(R.dimen.dp_438);
		}else {
			windowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		}
		mAdaptr.setDatas(list);
    }

	@Override
	public void onItemClick(View view, int position) {
		GroupLeaderBean item = mAdaptr.getItem(position);
		callPhone(item.getCompletePhone());
		cancel();
	}
	/**
	 * 拨打电话（直接拨打电话）
	 * @param phoneNum 电话号码
	 */
	public void callPhone(String phoneNum){
		Intent intent = new Intent(Intent.ACTION_DIAL);//ACTION_DIAL ACTION_CALL
		Uri data = Uri.parse("tel:" + phoneNum);
		intent.setData(data);
		ActivityUtils.getTopActivity().startActivity(intent);
	}
}
