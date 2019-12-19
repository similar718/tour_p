package cn.xmzt.www.route.activity;

import androidx.lifecycle.Observer;
import android.content.Intent;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ActivityKeywordSearchBinding;
import cn.xmzt.www.roomdb.TourDatabase;
import cn.xmzt.www.roomdb.beans.KeyWords;
import cn.xmzt.www.route.vm.KeywordSearchViewModel;
import cn.xmzt.www.view.HotTagView;

import java.util.List;

/**
 * @describe 关键字搜索
 */
public class KeywordSearchActivity extends TourBaseActivity<KeywordSearchViewModel, ActivityKeywordSearchBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_keyword_search;
    }

    @Override
    protected KeywordSearchViewModel createViewModel() {
        viewModel = new KeywordSearchViewModel();
        viewModel.recommendSearchList.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> result) {
                if(result!=null&&result.size()>0){
                    dataBinding.sstjTags.setTags(result);
                }
            }
        });
        viewModel.hotSearchList.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> result) {
                if(result!=null&&result.size()>0){
                    dataBinding.rmssTags.setTags(result);
                }
            }
        });
        return viewModel;
    }
    private int type=0;//2表示线路
    private String cityName="";//城市名称
    protected void initData() {
        Intent intent=getIntent();
        type=intent.getIntExtra("A",0);
        cityName=intent.getStringExtra("B");
        dataBinding.setActivity(this);
        //点击搜索历史
        dataBinding.sslsTags.setOnItemClickListener(new HotTagView.IOnItemClickListener() {
            @Override
            public void onItemClick(View v, String itemText, int position) {
                //搜索历史
                toRouteSearchResultListActivity(itemText);
            }
        });
        //点击搜索推荐
        dataBinding.sstjTags.setOnItemClickListener(new HotTagView.IOnItemClickListener() {
            @Override
            public void onItemClick(View v, String itemText, int position) {
                toRouteSearchResultListActivity(itemText);
            }
        });
        //点击热门搜索
        dataBinding.rmssTags.setOnItemClickListener(new HotTagView.IOnItemClickListener() {
            @Override
            public void onItemClick(View v, String itemText, int position) {
                toRouteSearchResultListActivity(itemText);
            }
        });
        dataBinding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId==EditorInfo.IME_ACTION_SEARCH){
                    onClick(dataBinding.ivSearch);
                }
                return true;
            }
        });
        List<String> historyTagList=viewModel.historyTagList(getApplicationContext());
        if(historyTagList.size()>0){
            dataBinding.sslsLayout.setVisibility(View.VISIBLE);
            dataBinding.sslsTags.setTags(historyTagList);
        }else {
            dataBinding.sslsLayout.setVisibility(View.GONE);
        }
        viewModel.searchKeywordList(""+type);
        /*HotTagView.TagView tagView= dataBinding.sstjTags.getTagView(0);
        if(tagView!=null){
            tagView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.icon_order_line,0,0,0);
            TextPaint paint = tagView.getPaint();
            paint.setFakeBoldText(true);
            tagView.setmLinePaintColor(Color.parseColor("#ffffff"));
            tagView.setmBackgroudPaintColor(Color.parseColor("#ffffff"));
        }*/
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_search:
                String keyword=dataBinding.etSearch.getText().toString();
                toRouteSearchResultListActivity(keyword);
                break;
            case R.id.iv_del:
                int count= TourDatabase.getDefault(getApplicationContext()).getKeyWordsDao().deleteFunTypes(Constants.KeywordsType.TYPE_ROUTE_SEARCH);
                if(count>0){
                    dataBinding.sslsLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.iv_search_delete:
                viewModel.keyword.set("");
                dataBinding.etSearch.setText("");
                break;
            default:
                break;
        }
    }

    private void toRouteSearchResultListActivity(String keyword){
        // TODO 需求更改
//        if(!TextUtils.isEmpty(keyword)){
            if (!TextUtils.isEmpty(keyword)) {
                KeyWords keyWords=new KeyWords();
                keyWords.name=keyword;
                keyWords.funTypes=Constants.KeywordsType.TYPE_ROUTE_SEARCH;
                keyWords.time=System.currentTimeMillis();
                TourDatabase.getDefault(getApplicationContext()).getKeyWordsDao().insert(keyWords);
            }
            Intent mIntent = new Intent(this, RouteSearchResultListActivity.class);
            mIntent.putExtra("A",keyword);
            startActivity(mIntent);
            finish();
//        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
