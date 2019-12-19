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

import java.util.ArrayList;
import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemGroupInfoMemberAddBinding;
import cn.xmzt.www.databinding.ItemGroupInfoMemberMinusBinding;
import cn.xmzt.www.databinding.ItemSmartTeamGroupInfoCarBinding;
import cn.xmzt.www.databinding.ItemSmartTeamGroupInfoMemberBinding;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.view.listener.ItemListener;

/**
 * 智能组队群成员列表
 */
public class SmartTeamGroupMemberList extends RecyclerView {
  private GroupMemberListAdapter itemAdapter=null;
  private GridLayoutManager manager;
  private String groupId;

  public SmartTeamGroupMemberList(Context context) {
    super(context);
    init(context, null);
  }

  public SmartTeamGroupMemberList(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public SmartTeamGroupMemberList(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    manager=new GridLayoutManager(getContext(), 5, RecyclerView.VERTICAL,false);
    setLayoutManager(manager);
    itemAdapter=new GroupMemberListAdapter();
    //下面是真正创建recyclerView
    setRecycledViewPool(pool);
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

  private List<GroupMemberBean> dataList=new ArrayList<>();
  /**
   * 设置列表数据
   * @param datas
   */
  public void setDatas(List datas) {
    dataList.clear();
    dataList.addAll(datas);
    itemAdapter.notifyDataSetChanged();
  }
  public GroupMemberBean getItem(int position) {
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
  public boolean isLD=false;//是否是领队
  public boolean isDriver=false;//是否是车辆
  private RecycledViewPool pool = new RecycledViewPool();
  class GroupMemberListAdapter extends Adapter<GroupMemberListAdapter.ItemHolder> {
    private final int TYPE_ITEM_CAR = 0;
    private final int TYPE_ITEM = 1;
    private final int TYPE_ITEM_ADD = 2;
    private final int TYPE_ITEM_MINUS = 3;
    @Override
    public int getItemViewType(int position) {
      if(dataList.size()>0){
        if(isDriver){
          return TYPE_ITEM_CAR;
        }else {
          if(isLD){
            if(position==getItemCount()-1){
              return TYPE_ITEM_MINUS;
            }else if(position==getItemCount()-2){
              return TYPE_ITEM_ADD;
            }else {
              return TYPE_ITEM;
            }
          }else {
            if(position==getItemCount()-1){
              return TYPE_ITEM_ADD;
            }else {
              return TYPE_ITEM;
            }
          }
        }
      }else {
        return TYPE_ITEM_ADD;
      }
    }
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      if(viewType==TYPE_ITEM_ADD){
        return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_group_info_member_add, parent, false));
      }else if(viewType==TYPE_ITEM_MINUS){
        return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_group_info_member_minus, parent, false));
      }else if(viewType==TYPE_ITEM_CAR){
        return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_smart_team_group_info_car, parent, false));
      }else {
        return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_smart_team_group_info_member, parent, false));
      }
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
      holder.setViewDate(position);
    }

    @Override
    public int getItemCount() {
      int size=dataList.size();
      if(isDriver){
        return size;
      }else {
        if(isLD){
          return size >0 ? size+2 : 1;
        }else {
          return size + 1;
        }
      }
    }

    public GroupMemberBean getItem(int position) {
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
        if(dbBinding instanceof ItemGroupInfoMemberAddBinding){
          ItemGroupInfoMemberAddBinding itemBinding= (ItemGroupInfoMemberAddBinding) dbBinding;
          itemBinding.ivAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              if(itemListener!=null){
                itemListener.onItemClick(v,getAdapterPosition());
              }
            }
          });
        }else if(dbBinding instanceof ItemGroupInfoMemberMinusBinding){
          ItemGroupInfoMemberMinusBinding itemBinding= (ItemGroupInfoMemberMinusBinding) dbBinding;
          itemBinding.ivMinus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              if(itemListener!=null){
                itemListener.onItemClick(v,getAdapterPosition());
              }
            }
          });
        }else if(dbBinding instanceof ItemSmartTeamGroupInfoMemberBinding){
          ItemSmartTeamGroupInfoMemberBinding itemBinding= (ItemSmartTeamGroupInfoMemberBinding) dbBinding;
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
        GroupMemberBean item = getItem(position);
        if(dbBinding instanceof ItemSmartTeamGroupInfoMemberBinding){
          ItemSmartTeamGroupInfoMemberBinding itemBinding= (ItemSmartTeamGroupInfoMemberBinding) dbBinding;
          itemBinding.setItem(item);

          if(item.isLeader()) {
            itemBinding.tvLabel.setVisibility(VISIBLE);
          }else {
            itemBinding.tvLabel.setVisibility(GONE);
          }
        }else if(dbBinding instanceof ItemSmartTeamGroupInfoCarBinding){
          ItemSmartTeamGroupInfoCarBinding itemBinding= (ItemSmartTeamGroupInfoCarBinding) dbBinding;
          itemBinding.setItem(item);

          if(item.isLeader()) {
            itemBinding.tvLabel.setVisibility(VISIBLE);
          }else {
            itemBinding.tvLabel.setVisibility(GONE);
          }
        }
      }
    }
  }
}