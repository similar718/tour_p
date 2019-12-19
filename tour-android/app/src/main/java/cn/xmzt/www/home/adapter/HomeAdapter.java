package cn.xmzt.www.home.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.databinding.ItemHome1Binding;
import cn.xmzt.www.databinding.ItemHome2Binding;
import cn.xmzt.www.databinding.ItemHome3Binding;
import cn.xmzt.www.databinding.ItemHome4Binding;
import cn.xmzt.www.databinding.ItemHome5Binding;
import cn.xmzt.www.databinding.ItemHome6Binding;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.home.activity.CustomizeAddActivity;
import cn.xmzt.www.home.activity.PopularityMustPlayActivity;
import cn.xmzt.www.home.activity.SelectedLineActivity;
import cn.xmzt.www.home.bean.AdvertiseBean;
import cn.xmzt.www.home.bean.ArticleBean;
import cn.xmzt.www.home.bean.HomeCategory;
import cn.xmzt.www.home.bean.HomeIndexBean;
import cn.xmzt.www.home.bean.PopularityBean;
import cn.xmzt.www.mine.activity.LoginActivity;
import cn.xmzt.www.route.adapter.BaseRecycleViewAdapter;
import cn.xmzt.www.utils.DensityUtil;
import cn.xmzt.www.utils.SchemeActivityUtil;
import cn.xmzt.www.view.banner.BannerLayout;
import cn.xmzt.www.view.listener.ItemListener;


public class HomeAdapter extends BaseRecycleViewAdapter<Object, HomeAdapter.ItemHolder> {
    private final int TYPE_ITEM_1 = 1;//广告栏和智能出行已及智慧导览
    private final int TYPE_ITEM_2 = 2;//分类head
    private final int TYPE_ITEM_3 = 3;//分类floor
    private final int TYPE_ITEM_4 = 4;//人气必玩
    private final int TYPE_ITEM_5 = 5;//第一个精品线路文章
    private final int TYPE_ITEM_6 = 6;//精品线路文章


    public int startJXXLIndex=0;//精选线路开始位置
    private List<AdvertiseBean> advertiseVOs = null;
    private List<PopularityBean> popularityVOs;//首页人气必玩
    /**
     * @param mHomeIndexBean
     */
    public void setData(HomeIndexBean mHomeIndexBean) {
        this.datas.clear();
        advertiseVOs = mHomeIndexBean.getAdvertiseVOs();
        popularityVOs=mHomeIndexBean.getPopularityVOs();
        this.datas.add(mHomeIndexBean);
        this.datas.add(new HomeCategory("人气必玩","来一段说走就走的旅行"));
        this.datas.add("人气必玩");
        this.datas.add("分类底部");
        this.datas.add(new HomeCategory("精选路线","精挑细选热门推荐线路"));
        startJXXLIndex=this.datas.size();
        this.datas.addAll(mHomeIndexBean.getArticleList());
        this.datas.add("分类底部");
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        Object obj=getItem(position);
       if(position == 0){
            return TYPE_ITEM_1;
        }else if(obj instanceof HomeCategory){
            return TYPE_ITEM_2;
        }else if(obj instanceof String){
           if("人气必玩".equals(obj)){
               return TYPE_ITEM_4;
           }
           return TYPE_ITEM_3;
       }else if(obj instanceof ArticleBean){
           if(startJXXLIndex==position){
               return TYPE_ITEM_5;
           }else {
               return TYPE_ITEM_6;
           }
       }
       return 0;
    }
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("shyong","viewType="+viewType);
        ItemHolder holder=null;
        if(viewType==TYPE_ITEM_1){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_home_1, parent, false));
        }else if(viewType==TYPE_ITEM_2){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_home_2, parent, false));
        }else if(viewType==TYPE_ITEM_3){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_home_3, parent, false));
        }else if(viewType==TYPE_ITEM_4){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_home_4, parent, false));
        }else if(viewType==TYPE_ITEM_5){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_home_5, parent, false));
        }else if(viewType==TYPE_ITEM_6){
            holder=new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_home_6, parent, false));
        }
        return holder;
    }
    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.setViewDate(position);
    }
    @Override
    public int getItemCount() {
        return this.datas == null ? 0: datas.size();
    }
    public int getSpanCount(int position){
        Object obj=getItem(position);
        if(obj instanceof ArticleBean){
            if(position>startJXXLIndex){
                return 1;
            }
        }
        return 2;
    }

    @Override
    public Object getItem(int position) {
        if(position<datas.size()){
            return datas.get(position);
        }
        return null;
    }
    class ItemHolder<DB extends ViewDataBinding> extends RecyclerView.ViewHolder{
        private DB dbBinding;
        private HomeBannerAdapter homeBannerAdapter = null;
        private int dp_10=0;
        private int widthPixels=0;

        public ItemHolder(DB dbBinding) {
            super(dbBinding.getRoot());
            this.dbBinding = dbBinding;
            widthPixels=dbBinding.getRoot().getResources().getDisplayMetrics().widthPixels;
            dp_10= DensityUtil.dip2px(dbBinding.getRoot().getContext(),10);
            if(dbBinding instanceof ItemHome1Binding){
                ItemHome1Binding itemBinding= (ItemHome1Binding) this.dbBinding;
                homeBannerAdapter = new HomeBannerAdapter(dbBinding.getRoot().getContext(),advertiseVOs);
                itemBinding.bannerAdvertise.setAdapter(homeBannerAdapter);
                homeBannerAdapter.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        // 顶部banner的点击事件
                        AdvertiseBean mAdvertiseBean = homeBannerAdapter.getItem(position);
                        if(mAdvertiseBean!=null){
                            SchemeActivityUtil.startToActivity(itemBinding.bannerAdvertise.getContext(), mAdvertiseBean.getLinkType(), mAdvertiseBean.getHomeName(), mAdvertiseBean.getAdvLink());
                        }
                    }
                });
                // 显示指示器
                itemBinding.bannerAdvertise.setShowIndicator(true);
                itemBinding.bannerAdvertise.setAutoPlaying(true);
                itemBinding.zncxLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(itemListener!=null){
                            itemListener.onItemClick(view,getAdapterPosition());
                        }
                    }
                });
                itemBinding.zhdlLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(itemListener!=null){
                            itemListener.onItemClick(view,getAdapterPosition());
                        }
                    }
                });
                itemBinding.mxmLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(itemListener!=null){
                            itemListener.onItemClick(view,getAdapterPosition());
                        }
                    }
                });

            }else if(dbBinding instanceof ItemHome4Binding){
                ItemHome4Binding itemBinding= (ItemHome4Binding) this.dbBinding;
                itemBinding.popularityRecyclerView.setItemListener(new ItemListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        PopularityBean popularityBean= itemBinding.popularityRecyclerView.getItem(position);
                        if(popularityBean.getType()==3){
                            //类型(1：出发，2：落地，3：私人订制)
                            if(!TextUtils.isEmpty(TourApplication.getToken())){
                                Intent mIntent = new Intent(view.getContext(), CustomizeAddActivity.class);
                                view.getContext().startActivity(mIntent);
                            }else {
                                //没有登录
                                LoginActivity.start(view.getContext());
                            }
                        } else {
                            Intent intent = new Intent(view.getContext(), PopularityMustPlayActivity.class);
                            EventBus.getDefault().postSticky(popularityBean);
                            view.getContext().startActivity(intent);
                        }
                    }
                });
            }else if(dbBinding instanceof ItemHome5Binding){
                ItemHome5Binding itemBinding= (ItemHome5Binding) this.dbBinding;
                itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Object obj=getItem(getAdapterPosition());
                        if(obj instanceof ArticleBean){
                            Intent intent = new Intent(v.getContext(), SelectedLineActivity.class);
                            EventBus.getDefault().postSticky(obj);
                            v.getContext().startActivity(intent);
                        }
                    }
                });
            }else if(dbBinding instanceof ItemHome6Binding){
                ItemHome6Binding itemBinding= (ItemHome6Binding) this.dbBinding;
                itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Object obj=getItem(getAdapterPosition());
                        if(obj instanceof ArticleBean){
                            Intent intent = new Intent(v.getContext(), SelectedLineActivity.class);
                            EventBus.getDefault().postSticky(obj);
                            v.getContext().startActivity(intent);
                        }
                    }
                });
            }
        }
        private void setViewDate(int position){
            if(dbBinding instanceof ItemHome1Binding){
                ItemHome1Binding itemBinding= (ItemHome1Binding) this.dbBinding;
                Object obj=getItem(position);
                if(obj instanceof HomeIndexBean){
                    HomeIndexBean mHomeIndexBean= (HomeIndexBean) obj;
                    homeBannerAdapter.setDatas(mHomeIndexBean.getAdvertiseVOs());
                }
            }else if(dbBinding instanceof ItemHome2Binding){
                ItemHome2Binding itemBinding= (ItemHome2Binding) this.dbBinding;
                Object obj=getItem(position);
                if(obj instanceof HomeCategory){
                    HomeCategory homeCategory= (HomeCategory) obj;
                    itemBinding.tvTitle.setText(homeCategory.getCategoryName());
                    itemBinding.tvSubTitle.setText(homeCategory.getSubCategoryName());
                }
            }else if(dbBinding instanceof ItemHome3Binding){
                ItemHome3Binding itemBinding= (ItemHome3Binding) this.dbBinding;
                if(position==getItemCount()-1){
                    itemBinding.floorView.setVisibility(View.VISIBLE);
                }else {
                    itemBinding.floorView.setVisibility(View.GONE);
                }
            }else if(dbBinding instanceof ItemHome4Binding){
                ItemHome4Binding itemBinding= (ItemHome4Binding) this.dbBinding;
                Object obj=getItem(position);
                itemBinding.popularityRecyclerView.setDatas(popularityVOs);
            }else if(dbBinding instanceof ItemHome5Binding){
                ItemHome5Binding itemBinding= (ItemHome5Binding) this.dbBinding;
                Object obj=getItem(position);
                if(obj instanceof ArticleBean){
                    ArticleBean articleBean= (ArticleBean) obj;
                    GlideUtil.loadImgTopC(itemBinding.ivPhoto,articleBean.getImage());
                    itemBinding.tvArea.setText(articleBean.getArea());
                    itemBinding.tvGive.setText(articleBean.getGiveCountStr());
                    itemBinding.tvTitle.setText(articleBean.getTitle());
                    itemBinding.tvDes.setText(articleBean.getDes());
                    itemBinding.tvLineType.setText(articleBean.getLineType() == 1 ? "自由出行" : "跟团自驾");
                }
            }else if(dbBinding instanceof ItemHome6Binding){
                ItemHome6Binding itemBinding= (ItemHome6Binding) this.dbBinding;
                Object obj=getItem(position);
                if(obj instanceof ArticleBean){
                    ArticleBean articleBean= (ArticleBean) obj;
                    GlideUtil.loadImgTopC(itemBinding.ivPhoto,articleBean.getImage());
                    itemBinding.tvArea.setText(articleBean.getArea());
                    itemBinding.tvGive.setText(articleBean.getGiveCountStr());
                    itemBinding.tvTitle.setText(articleBean.getTitle());
                    itemBinding.tvDes.setText(articleBean.getDes());
                    itemBinding.tvLineType.setText(articleBean.getLineType() == 1 ? "自由出行" : "跟团自驾");
                }
            }
        }
    }
}