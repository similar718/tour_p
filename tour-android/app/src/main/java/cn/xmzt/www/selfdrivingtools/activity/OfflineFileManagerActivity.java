package cn.xmzt.www.selfdrivingtools.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ActivityOfflineFileManagerBinding;
import cn.xmzt.www.dialog.TextTitleDialog;
import cn.xmzt.www.manager.IntentManager;
import cn.xmzt.www.roomdb.TourDatabase;
import cn.xmzt.www.roomdb.beans.ScenicContents;
import cn.xmzt.www.selfdrivingtools.adapter.OfflineFileManagerAdapter;
import cn.xmzt.www.selfdrivingtools.bean.OfflineFileManagerBean;
import cn.xmzt.www.selfdrivingtools.viewmodel.OfflineFileManagerViewModel;
import cn.xmzt.www.utils.DataCleanManager;
import cn.xmzt.www.utils.MD5Utils;
import cn.xmzt.www.utils.StatusBarUtil;

public class OfflineFileManagerActivity extends TourBaseActivity<OfflineFileManagerViewModel, ActivityOfflineFileManagerBinding> {

    private Context mContext;
    private OfflineFileManagerAdapter mAdapter;
    List<OfflineFileManagerBean> mListData = null;
    private boolean mIsAllSelect = false;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_offline_file_manager;
    }

    @Override
    protected OfflineFileManagerViewModel createViewModel() {
        viewModel = new OfflineFileManagerViewModel();
        viewModel.setIView(this);

        viewModel.mPackageDowns.observe(this, new Observer<List<OfflineFileManagerBean>>() {
            @Override
            public void onChanged(List<OfflineFileManagerBean> data) {
                if (data != null){
                    mListData = data;
                    mAdapter.setDatas(mListData);
                }
            }
        });

        return viewModel;
    }

    @Override
    protected void initData() {
        mContext = this;
        // 设置顶部状态栏
        StatusBarUtil.setStatusBarLightMode(this,true);
        dataBinding.setModel(viewModel);
        dataBinding.setActivity(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        dataBinding.rvList.setLayoutManager(layoutManager);
        mAdapter = new OfflineFileManagerAdapter(mContext);
        dataBinding.rvList.setAdapter(mAdapter);
        // 底部隐藏
        dataBinding.llBottom.setVisibility(View.GONE);

        getDataBaseData();
    }

    private void getDataBaseData(){
        List<ScenicContents> list = TourDatabase.getDefault(mContext).getScenicContentDao().getScenicContents(Constants.KeywordsType.FUNTYPES_SCENIC_VOICE);
        if (list != null && list.size() > 0) {
            String ids = "";
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).id > 0) {
                    ids += list.get(i).id;
                    if (i != (list.size() - 1)) {
                        ids += ",";
                    }
                }
            }
            viewModel.getOfficeLinePackageDowns(ids);
        } else {
            // 空数据情况
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case cn.xmzt.www.R.id.back_iv:// 返回
                finish();
                break;
            case cn.xmzt.www.R.id.top_right_tv:// 编辑
                if (dataBinding.topRightTv.getText().toString().trim().equals("编辑")){
                    dataBinding.llBottom.setVisibility(View.VISIBLE);
                    dataBinding.topRightTv.setText("完成");
                    mAdapter.setTypes(1);
                } else if (dataBinding.topRightTv.getText().toString().trim().equals("完成")){
                    for (int i = 0 ; i < mListData.size(); i++){
                        mListData.get(i).setSelect(false);
                    }
                    dataBinding.llBottom.setVisibility(View.GONE);
                    dataBinding.topRightTv.setText("编辑");
                    mAdapter.setTypes(0);
                }
                break;
            case cn.xmzt.www.R.id.tv_all_select:// 全选
                if (!mIsAllSelect) {
                    for (int i = 0; i < mListData.size(); i++) {
                        mListData.get(i).setSelect(true);
                    }
                    mAdapter.notifyDataSetChanged();
                    mIsAllSelect = true;
                    Drawable drawable = mContext.getResources().getDrawable(R.drawable.icon_check_yx_duigou);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    dataBinding.tvAllSelect.setCompoundDrawables(drawable, null, null, null);
                } else {
                    for (int i = 0; i < mListData.size(); i++) {
                        mListData.get(i).setSelect(false);
                    }
                    mAdapter.notifyDataSetChanged();
                    mIsAllSelect = false;
                    Drawable drawable = mContext.getResources().getDrawable(R.drawable.icon_check_yx_un);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    dataBinding.tvAllSelect.setCompoundDrawables(drawable, null, null, null);
                }
                break;
            case cn.xmzt.www.R.id.tv_delete:// 删除
                showDelDialog();
                break;
            default:
                break;
        }
    }

    private TextTitleDialog delDialog;

    private void showDelDialog() {
        delDialog = new TextTitleDialog(this, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delDialog.dismiss();
                deleteFile();
            }
        });
        delDialog.setViewData("确定要删除选中的离线包？","我再想想","删除");
        delDialog.setBtnBackground(R.drawable.shape_gray_f4_radius_4,R.drawable.shape_blue_radius_4);
        delDialog.setBtnTextColor(Color.parseColor("#24A4FF"),Color.parseColor("#FFFFFF"));
        delDialog.show();
    }

    public void OnListClickListener(int position,int types){
        if (types == 0){
            IntentManager.getInstance().goScenicSpotMapActivity(mContext,mListData.get(position).getScenicId(),null);
        } else {
            // 选中操作
            mAdapter.getItem(position).setSelect(!mAdapter.getItem(position).isSelect());
            mAdapter.notifyItemChanged(position);

            int size = 0;
            for (int i = 0 ;i < mListData.size(); i++) { // 遍历所有的数据
                if (mListData.get(i).isSelect()) { // 将选中的进行删除操作
                    size ++;
                }
            }
            if (size == mListData.size()){
                mIsAllSelect = true;
                Drawable drawable = mContext.getResources().getDrawable(R.drawable.icon_check_yx_duigou);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                dataBinding.tvAllSelect.setCompoundDrawables(drawable, null, null, null);
            } else {
                mIsAllSelect = false;
                Drawable drawable = mContext.getResources().getDrawable(R.drawable.icon_check_yx_un);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                dataBinding.tvAllSelect.setCompoundDrawables(drawable, null, null, null);
            }
        }
    }

    private void deleteFile(){
        List<OfflineFileManagerBean> listData = new ArrayList<>();
        for (int i = 0 ;i < mListData.size(); i++) { // 遍历所有的数据
            if (mListData.get(i).isSelect()) { // 将选中的进行删除操作
                String filename = MD5Utils.getMd5Value(mListData.get(i).getResUrl());
                String mVoiceFileName = Constants.SCENIC_VOICE_FILE_PATH + filename;
                DataCleanManager.cleanCustomCache(mVoiceFileName);
                // 数据库删除
                TourDatabase.getDefault(mContext).getScenicContentDao().delete(TourDatabase.getDefault(mContext).getScenicContentDao().getScenicContents(mListData.get(i).getScenicId(),Constants.KeywordsType.FUNTYPES_SCENIC_VOICE));
            } else {
                listData.add(mListData.get(i));
            }
        }
        mListData.clear();
        mListData = listData;
        mAdapter.setDatas(mListData);

        mIsAllSelect = false;
        Drawable drawable = mContext.getResources().getDrawable(R.drawable.icon_check_yx_un);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        dataBinding.tvAllSelect.setCompoundDrawables(drawable, null, null, null);
    }
}
