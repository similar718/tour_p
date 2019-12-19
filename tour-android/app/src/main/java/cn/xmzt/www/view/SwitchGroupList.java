package cn.xmzt.www.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemGroupBinding;
import cn.xmzt.www.intercom.bean.MyTalkGroupBean;
import cn.xmzt.www.view.listener.ItemListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 切换群聊得群列表
 */
public class SwitchGroupList extends RecyclerView {
  private GroupListAdapter itemAdapter=null;
  private GridLayoutManager manager;
  private String groupId;

  public SwitchGroupList(Context context) {
    super(context);
    init(context, null);
  }

  public SwitchGroupList(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public SwitchGroupList(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    manager=new GridLayoutManager(getContext(), 4, RecyclerView.VERTICAL,false);
    setLayoutManager(manager);
    itemAdapter=new GroupListAdapter();
    //下面是真正创建recyclerView
    setAdapter(itemAdapter);
  }

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  @Override
  protected void onMeasure(int widthSpec, int heightSpec) {
    super.onMeasure(widthSpec, heightSpec);
  }

  private List<MyTalkGroupBean> dataList=new ArrayList<>();//行程列表
  private String currentGroupId="";//当前群id
  /**
   * 设置列表数据
   * @param datas
   * @param currentGroupId 当前群id 做列表选中用
   */
  public void setDatas(List<MyTalkGroupBean> datas, String currentGroupId) {
    this.currentGroupId=currentGroupId;
    if(datas!=null){
      for(int i=0;i<datas.size();i++){
        MyTalkGroupBean mTalkGroupInfoBean=datas.get(i);
        if(mTalkGroupInfoBean.getGroupId().equals(currentGroupId)){
          if(i>0){
            MyTalkGroupBean temp = datas.get(0);
            datas.set(0,mTalkGroupInfoBean);
            datas.set(i,temp);
          }
          break;
        }
      }

      if(datas.size()>3){
        manager.setSpanCount(4);
      }else if(datas.size()>0){
        manager.setSpanCount(datas.size());
      }else{
        manager.setSpanCount(1);
      }
      dataList=datas;
    }else {
      manager.setSpanCount(4);
    }
    itemAdapter.notifyDataSetChanged();
  }
  public MyTalkGroupBean getItem(int position) {
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
  class GroupListAdapter extends Adapter<GroupListAdapter.ItemHolder> {
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_group, parent, false));
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

    public MyTalkGroupBean getItem(int position) {
      if(position>=0&&position<dataList.size()){
        return dataList.get(position);
      }
      return null;
    }

    class ItemHolder<DB extends ViewDataBinding> extends ViewHolder{
      private DB dbBinding;
      public ItemHolder(DB dbBinding) {
        super(dbBinding.getRoot());
        this.dbBinding = dbBinding;
        if(dbBinding instanceof ItemGroupBinding){
          ItemGroupBinding itemBinding= (ItemGroupBinding) dbBinding;
          itemBinding.itemLayout.setOnClickListener(new OnClickListener() {
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
        MyTalkGroupBean item = getItem(position);
        if(dbBinding instanceof ItemGroupBinding){
          ItemGroupBinding itemBinding= (ItemGroupBinding) dbBinding;
          itemBinding.setItem(item);
          RecyclerView.LayoutParams mLayoutParams= (LayoutParams) itemBinding.itemLayout.getLayoutParams();
          if(position<4){
            mLayoutParams.topMargin=getResources().getDimensionPixelOffset(R.dimen.dp_10);
          }else {
            mLayoutParams.topMargin=getResources().getDimensionPixelOffset(R.dimen.dp_16);
          }
          if(item.getGroupId().equals(currentGroupId)){
            itemBinding.ivSelect.setVisibility(VISIBLE);
          }else {
            itemBinding.ivSelect.setVisibility(GONE);
          }
        }
      }
    }
  }
}