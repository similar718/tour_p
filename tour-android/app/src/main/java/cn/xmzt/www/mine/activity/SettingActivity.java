package cn.xmzt.www.mine.activity;

import android.content.pm.PackageManager;
import android.view.View;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ActivitySettingBinding;
import cn.xmzt.www.mine.model.SettingViewModel;
import cn.xmzt.www.roomdb.TourDatabase;
import cn.xmzt.www.roomdb.beans.ScenicContents;

/**
 * 设置界面
 */
public class SettingActivity extends TourBaseActivity<SettingViewModel, ActivitySettingBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected SettingViewModel createViewModel() {
        return new SettingViewModel();
    }

    @Override
    protected void initData() {
        dataBinding.setModel(viewModel);
        viewModel.setActivity(this);
        try {
            dataBinding.versionTv.setText("V" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 有缓存的情况显示离线包
        List<ScenicContents> list = TourDatabase.getDefault(this).getScenicContentDao().getScenicContents(Constants.KeywordsType.FUNTYPES_SCENIC_VOICE);
        if (list != null && list.size() > 0) {
            dataBinding.offlinePackageLayout.setVisibility(View.VISIBLE);
        } else {
            dataBinding.offlinePackageLayout.setVisibility(View.GONE);
        }
    }

    //    @BindView(R.id.title_name_tv)
//    TextView nameTv;
//    @BindView(R.id.version_tv)
//    TextView versionTv;//缓存
//    @BindView(R.id.cache_tv)
//    TextView cacheTv;//版本号
//
//    private Context mContext;
//    private CustomDialog dialog;
//    private int dialogWidth;//dialog宽度
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_setting);
//        ButterKnife.bind(this);
//        init();
//    }
//
//
//    @OnClick({R.id.title_back_iv, R.id.account_number_rl, R.id.clear_ll, R.id.feedback_rl})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.title_back_iv://返回
//                finish();
//                break;
//
//            case R.id.account_number_rl://账号与安全
//                startActivity(new Intent(SettingActivity.this,AccountSecurityActivity.class));
//                break;
//
//            case R.id.clear_ll://清理缓存
//                clearCache();
//                break;
//
//            case R.id.feedback_rl://意见反馈
//                startActivity(new Intent(mContext, FeedbackActivity.class));
//                break;
//
//        }
//    }
//
//
//
//    private void init(){
//        mContext = this;
//        nameTv.setText("设置");
//        WindowManager wm = (WindowManager) this
//                .getSystemService(Context.WINDOW_SERVICE);
//        dialogWidth = (int)(wm.getDefaultDisplay().getWidth() * 0.8);
//        try {
//            cacheTv.setText(CleanDataUtils.getTotalCacheSize(mContext));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            PackageManager manager = getPackageManager();
//            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
//            versionTv.setText("V" + info.versionName);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 清理缓存
//     */
//    private void clearCache(){
//        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_default, null);
//        TextView tipsTv = view.findViewById(R.id.tips_tv);
//        TextView confirmTv = view.findViewById(R.id.confirm_tv);
//        TextView cancelTv = view.findViewById(R.id.cancel_tv);
//        tipsTv.setText("确定要清除缓存吗？");
//        confirmTv.setOnClickListener(this);
//        cancelTv.setOnClickListener(this);
//        dialog = new CustomDialog(mContext, dialogWidth, 0, view, R.style.CustomDialog);
//        dialog.show();
//    }
//
//    @Override
//    public void onClick(View view) {
//        int id = view.getId();
//        switch (id){
//            case R.id.confirm_tv://确定
//                dialog.dismiss();
//                CleanDataUtils.clearAllCache(mContext);
//                cacheTv.setText(0 + ".00K");
//                break;
//
//            case R.id.cancel_tv://取消
//                dialog.dismiss();
//                break;
//        }
//    }
}
