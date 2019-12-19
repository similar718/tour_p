package cn.xmzt.www.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemGroupInfoMemberAddBinding;
import cn.xmzt.www.databinding.ItemGroupInfoMemberMinusBinding;
import cn.xmzt.www.databinding.ItemHome4Binding;
import cn.xmzt.www.databinding.ItemHome4PopularityPlayBinding;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.home.bean.PopularityBean;
import cn.xmzt.www.utils.DensityUtil;
import cn.xmzt.www.view.listener.ItemListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页人气必玩列表
 */
public class HomePopularityList extends RecyclerView {
  private DataAdapter itemAdapter=null;
  private GridLayoutManager manager;

  public HomePopularityList(Context context) {
    super(context);
    init(context, null);
  }

  public HomePopularityList(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public HomePopularityList(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    if (attrs != null) {
      int[] attrsArray = {
              android.R.attr.columnWidth
      };
      TypedArray array = context.obtainStyledAttributes(attrs, attrsArray);
      columnWidth = array.getDimensionPixelSize(0, -1);
      array.recycle();
    }
    manager=new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL,false);
    setLayoutManager(manager);
    itemAdapter=new DataAdapter();
    //下面是真正创建recyclerView
    setRecycledViewPool(pool);
    setAdapter(itemAdapter);
  }
  private int columnWidth = -1;
  @Override
  protected void onMeasure(int widthSpec, int heightSpec) {
    super.onMeasure(widthSpec, heightSpec);
    if (columnWidth > 0) {
      int spanCount = Math.max(1, getMeasuredWidth() / columnWidth);
      manager.setSpanCount(spanCount);
    }
  }
  @Override
  public boolean onInterceptTouchEvent(MotionEvent e) {
    return false;
  }
  private List<PopularityBean> dataList=new ArrayList<>();//首页人气必玩
  /**
   * 设置列表数据
   * @param datas
   */
  public void setDatas(List datas) {
    if(datas!=null){
      dataList=datas;
    }else {
      dataList.clear();
    }
    itemAdapter.notifyDataSetChanged();
  }
  public PopularityBean getItem(int position) {
    if (dataList!=null&&position < dataList.size()) {
      return dataList.get(position);
    } else {
      return null;
    }
  }
  private ItemListener itemListener;
  public void setItemListener(ItemListener itemListener) {
    this.itemListener = itemListener;
  }
  private RecycledViewPool pool = new RecycledViewPool();
  class DataAdapter extends Adapter<DataAdapter.ItemHolder> {
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_home_4_popularity_play, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
      holder.setViewDate(position);
    }

    @Override
    public int getItemCount() {
      int size=dataList.size();
      return size;
    }

    public PopularityBean getItem(int position) {
      if(position>=0&&position<dataList.size()){
        return dataList.get(position);
      }
      return null;
    }

    class ItemHolder<DB extends ViewDataBinding> extends ViewHolder{
      private DB dbBinding;
      private int dp_10=0;
      private int widthPixels=0;
      public ItemHolder(DB dbBinding) {
        super(dbBinding.getRoot());
        this.dbBinding = dbBinding;
        widthPixels=dbBinding.getRoot().getResources().getDisplayMetrics().widthPixels;
        dp_10= DensityUtil.dip2px(dbBinding.getRoot().getContext(),10);
        if(dbBinding instanceof ItemHome4PopularityPlayBinding){
          ItemHome4PopularityPlayBinding itemBinding= (ItemHome4PopularityPlayBinding) dbBinding;
          itemBinding.ivHomeMustPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              if(itemListener!=null){
                itemListener.onItemClick(v,getAdapterPosition());
              }
            }
          });
        }
      }
      protected void setViewDate(int position) {
        PopularityBean item = getItem(position);
        if(dbBinding instanceof ItemHome4PopularityPlayBinding){
          ItemHome4PopularityPlayBinding itemBinding= (ItemHome4PopularityPlayBinding) this.dbBinding;
          LinearLayout.LayoutParams mLayoutParams= (LinearLayout.LayoutParams) itemBinding.ivHomeMustPlay.getLayoutParams();
          mLayoutParams.width=108*widthPixels/375;
          mLayoutParams.height=95*widthPixels/375;
          GlideUtil.loadImgRadius(itemBinding.ivHomeMustPlay,4,item.getPictures());
        }
      }
    }
  }
}