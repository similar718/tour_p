package cn.xmzt.www.selfdrivingtools.activity;

import android.Manifest;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ActivityScenicSpotSearchBinding;
import cn.xmzt.www.dialog.ConfirmDialog;
import cn.xmzt.www.selfdrivingtools.adapter.ScenicSpotSearchResultLineListAdapter;
import cn.xmzt.www.selfdrivingtools.bean.ScenicHotSearchBean;
import cn.xmzt.www.selfdrivingtools.bean.WisdomGuideInfo;
import cn.xmzt.www.manager.IntentManager;
import cn.xmzt.www.selfdrivingtools.viewmodel.ScenicSpotSearchViewModel;
import cn.xmzt.www.service.LocationService;
import cn.xmzt.www.utils.StatusBarUtil;
import cn.xmzt.www.utils.ToastUtils;
import cn.xmzt.www.view.HotTagView;
import cn.xmzt.www.view.listener.TextChangedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 景区搜索界面
 *  1.点击热门和历史逻辑处理
 *  2.点击搜索逻辑处理
 */
public class ScenicSpotSearchActivity extends TourBaseActivity<ScenicSpotSearchViewModel, ActivityScenicSpotSearchBinding> {

    private  String mKeywords = ""; // 搜索关键字
    private  int mPageNum = 1;
    private  int mPageSize = 20;

    private  int mLimit = 12; // 限制条数 最多12个 本地最多10个
    private  final int mType = 11;// 1 城市 11 景区

    private  List<WisdomGuideInfo.ItemsBean> mSearchlist = new ArrayList<>();
    ScenicSpotSearchResultLineListAdapter mSearchAdapter = null;
    // 确认删除的dialog
    private ConfirmDialog dialog;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_scenic_spot_search;
    }

    @Override
    protected ScenicSpotSearchViewModel createViewModel() {
        viewModel = new ScenicSpotSearchViewModel();
        viewModel.setIView(this);
        // 搜索的时候回传的数据显示
        viewModel.mKeyWordsSearchList.observe(this, new Observer<WisdomGuideInfo>() {
            @Override
            public void onChanged(@Nullable WisdomGuideInfo wisdomGuideInfo) {
                setData(wisdomGuideInfo);
            }
        });
        // 热门搜索的接口
        viewModel.mScenicHotSearchList.observe(this, new Observer<List<ScenicHotSearchBean>>() {
            @Override
            public void onChanged(@androidx.annotation.Nullable List<ScenicHotSearchBean> scenicHotSearchBean) {
                // 获取得到数据
                setHotData(scenicHotSearchBean);
            }
        });
        return viewModel;
    }

    @Override
    protected void initData() {
        // 设置顶部状态栏
        StatusBarUtil.setStatusBarLightMode(this,true);
        dataBinding.setModel(viewModel);
        dataBinding.setActivity(this);

        // 历史搜索 需要在onclick不为空的情况下设置tag  才会有点击事件
        dataBinding.zjTags.setOnItemClickListener(new HotTagView.IOnItemClickListener() {
            @Override
            public void onItemClick(View tv, String itemText, int position) {
                // 历史搜索
                dataBinding.etSearch.setText(itemText);
            }
        });

        // 热门搜索
        dataBinding.zjHotTags.setOnItemClickListener(new HotTagView.IOnItemClickListener() {
            @Override
            public void onItemClick(View v, String itemText, int position) {
                dataBinding.etSearch.setText(itemText);
            }
        });

        viewModel.getHotSearchList(this,mLimit,mType);

        getLocalHistoryData();
        // 点击搜索事件
        dataBinding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {//按下搜索
                    startSearch();
                }
                return false;//返回true，保留软键盘。false，隐藏软键盘
            }
        });
        dataBinding.etSearch.addTextChangedListener(mTextWatcher);

        // 搜索界面界面list
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        dataBinding.rvSearchResult.setLayoutManager(layoutManager);
        DividerItemDecoration decor = new DividerItemDecoration(this, layoutManager.getOrientation());
        decor.setDrawable(ContextCompat.getDrawable(this,R.drawable.item_ticket_detail_recyleview_line));
        dataBinding.rvSearchResult.addItemDecoration(decor);
        mSearchAdapter = new ScenicSpotSearchResultLineListAdapter(this);
        mSearchAdapter.setDatas(mSearchlist);
        dataBinding.rvSearchResult.setAdapter(mSearchAdapter);
    }
    private void startSearch(){
        String data = dataBinding.etSearch.getText().toString();
        if (!"".equals(data) && data != null){
            mKeywords = data;
            viewModel.getKeyWordsSearchList(Constants.mCity,mKeywords,Constants.mLocation,mPageNum,mPageSize);
        } else {
            ToastUtils.showText(this,"请输入搜索景区……");
        }
    }

    private void setData(WisdomGuideInfo data){
        if (data != null) {
            dataBinding.rlResultSearch.setVisibility(View.VISIBLE);
            dataBinding.rlHotSearch.setVisibility(View.GONE);

            mSearchlist.clear();
            mSearchlist = new ArrayList<>();

            // 需要添加第一条数据
            WisdomGuideInfo.ItemsBean bean = new WisdomGuideInfo.ItemsBean();
            bean.setScenicName(dataBinding.etSearch.getText().toString().trim()+"全部景区");
            bean.setId(dataBinding.etSearch.getText().toString().trim().length()); // 获取搜索文字长度
            bean.setCity(dataBinding.etSearch.getText().toString().trim()); // 获取搜索文字
            bean.setArea(data.getTotal()+"个景区");// 右边数据
            mSearchlist.add(bean);
            mSearchAdapter.setDatas(mSearchlist);
            mSearchAdapter.notifyDataSetChanged();

            mSearchlist = data.getItems();
            // 搜索之后的显示
            mSearchAdapter.addDatas(mSearchlist);
            mSearchAdapter.notifyDataSetChanged();
        } else {
            dataBinding.rlResultSearch.setVisibility(View.GONE);
            dataBinding.rlHotSearch.setVisibility(View.VISIBLE);
        }
    }

    private void setHotData(List<ScenicHotSearchBean> data){
        if (data != null) { // 热门数据
            List<String> hotSearch = new ArrayList<>();
            for (int i = 0 ; i< data.size(); i++){
                hotSearch.add(data.get(i).getKeyword());
            }
            dataBinding.zjHotTags.setTags(hotSearch);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case cn.xmzt.www.R.id.title_back_iv:
                finish();
                break;
            case cn.xmzt.www.R.id.iv_search: //搜索点击事件
                startSearch();
                break;
            case cn.xmzt.www.R.id.delete_iv: // 清除搜索数据
                dataBinding.etSearch.setText("");
                break;
            case cn.xmzt.www.R.id.iv_delete_history: // 删除本地搜索历史信息 点击没有反应
                if (dialog == null){
                    dialog = new ConfirmDialog(this, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            if (view.getId() == R.id.confirm_tv){
                                // 确定
                                deleteDataBaseInfo();
                            }
                        }
                    });
                }
                dialog.setViewData("确定要删除你所有的搜索景区历史记录吗？");
                dialog.show();
                break;
            default:
                break;
        }
    }

    /**
     * 进入到景区导览地图界面的地址
     * @param data
     * @param position
     */
    public void OnClickListener(WisdomGuideInfo.ItemsBean data, int position){
        if (position == 0){ // 到搜索结果界面
            String dataSearch = dataBinding.etSearch.getText().toString().trim();
            if (!TextUtils.isEmpty(dataSearch)){
                dataBinding.etSearch.setText(""); // 界面跳转的时候将搜索框进行清空处理
            }
            IntentManager.getInstance().goScenicSpotSearchResultActivity(this,dataSearch);
        } else { // 景区导览界面
            if ( ContextCompat.checkSelfPermission(ScenicSpotSearchActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                this.getApplicationContext().startService(new Intent(this, LocationService.class)); // 开始定位
                IntentManager.getInstance().goScenicSpotMapActivity(this,data.getId(),null);
            } else {
                ToastUtils.showText(ScenicSpotSearchActivity.this,"需要读取数据权限才能进入");
            }
        }
    }

    /**
     * 输入文字的监听事件
     */
    TextChangedListener mTextWatcher = new TextChangedListener() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String data = dataBinding.etSearch.getText().toString().trim();
            // 进行过滤
            if (!"".equals(data) && data != null)
                startSearch();
            else {
                dataBinding.rlResultSearch.setVisibility(View.GONE);
                dataBinding.rlHotSearch.setVisibility(View.VISIBLE);
            }
        }
    };

    /**
     * 获取本地数据库里面的搜索景区历史信息
     */
    private void getLocalHistoryData(){
        List<String> historySearch = viewModel.getSearchHistoryInfo(Constants.KeywordsType.FUNTYPES_SCENIC,10,getApplicationContext());
        // 本地没有历史数据
        if (historySearch == null){
            // 需要不显示历史搜索控件
            dataBinding.rlHistorySearch.setVisibility(View.GONE);
            dataBinding.zjTags.setVisibility(View.GONE);
        } else { // 有历史数据
            dataBinding.rlHistorySearch.setVisibility(View.VISIBLE);
            dataBinding.zjTags.setVisibility(View.VISIBLE);

            dataBinding.zjTags.setTags(historySearch);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // 重新到这个界面的时候需要去请求一下本地 可能已经有保存的数据
        getLocalHistoryData();
    }

    /**
     *  清除本地当前类型的数据信息
     */
    private void deleteDataBaseInfo(){
        int is_Success = viewModel.setDeleteSearchHistoryInfo(Constants.KeywordsType.FUNTYPES_SCENIC,getApplicationContext());
        if (is_Success >0){ // 删除成功
            getLocalHistoryData();
        } else { // 操作失败
            ToastUtils.showText(this,"操作失败，请稍后再试！");
        }
    }
}
