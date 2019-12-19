package cn.xmzt.www.view;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.databinding.ItemGroupInfoCarBinding;
import cn.xmzt.www.databinding.ItemGroupInfoMemberAddBinding;
import cn.xmzt.www.databinding.ItemGroupInfoMemberBinding;
import cn.xmzt.www.databinding.ItemGroupInfoMemberMinusBinding;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.view.listener.ItemListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 成员列表
 */
public class GroupMemberList extends RecyclerView {
  private GroupMemberListAdapter itemAdapter=null;
  private GridLayoutManager manager;
  private String groupId;

  public GroupMemberList(Context context) {
    super(context);
    init(context, null);
  }

  public GroupMemberList(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public GroupMemberList(Context context, AttributeSet attrs, int defStyle) {
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
  public boolean isQZLD=false;//是群主或者领队
  public boolean isDriver=false;//车辆
  private RecycledViewPool pool = new RecycledViewPool();
  class GroupMemberListAdapter extends RecyclerView.Adapter<GroupMemberListAdapter.ItemHolder> {
    private final int TYPE_ITEM_CAR = 0;
    private final int TYPE_ITEM = 1;
    private final int TYPE_ITEM_ADD = 2;
    private final int TYPE_ITEM_MINUS = 3;
    @Override
    public int getItemViewType(int position) {
      if(isDriver){
        if(isQZLD){
          if(position==getItemCount()-1){
            return TYPE_ITEM_ADD;
          }else {
            return TYPE_ITEM_CAR;
          }
        }else {
          return TYPE_ITEM_CAR;
        }
      }else {
        if(isQZLD){
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
    }
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      if(viewType==TYPE_ITEM_ADD){
        return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_group_info_member_add, parent, false));
      }else if(viewType==TYPE_ITEM_MINUS){
        return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_group_info_member_minus, parent, false));
      }else if(viewType==TYPE_ITEM_CAR){
        return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_group_info_car, parent, false));
      }else {
        return new ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_group_info_member, parent, false));
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
        if(isQZLD){
          return size+1;
        }else {
          return size;
        }
      }else {
        if(isQZLD){
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
    public int selectPosition=0;

    class ItemHolder<DB extends ViewDataBinding> extends RecyclerView.ViewHolder{
      private DB dbBinding;
      public ItemHolder(DB dbBinding) {
        super(dbBinding.getRoot());
        this.dbBinding = dbBinding;
        if(dbBinding instanceof ItemGroupInfoMemberAddBinding){
          ItemGroupInfoMemberAddBinding itemBinding= (ItemGroupInfoMemberAddBinding) dbBinding;
          itemBinding.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(itemListener!=null){
                itemListener.onItemClick(v,getAdapterPosition());
              }
            }
          });
        }else if(dbBinding instanceof ItemGroupInfoMemberMinusBinding){
          ItemGroupInfoMemberMinusBinding itemBinding= (ItemGroupInfoMemberMinusBinding) dbBinding;
          itemBinding.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(itemListener!=null){
                itemListener.onItemClick(v,getAdapterPosition());
              }
            }
          });
        }else if(dbBinding instanceof ItemGroupInfoMemberBinding){
          ItemGroupInfoMemberBinding itemBinding= (ItemGroupInfoMemberBinding) dbBinding;
          itemBinding.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(itemListener!=null){
                itemListener.onItemClick(v,getAdapterPosition());
              }
            }
          });
        } else if (dbBinding instanceof ItemGroupInfoCarBinding){
          ItemGroupInfoCarBinding itemGroupInfoCarBinding = (ItemGroupInfoCarBinding) dbBinding;
          itemGroupInfoCarBinding.itemLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              if (itemListener != null){
                itemListener.onItemClick(v,getAdapterPosition());
              }
            }
          });
        }
      }
      protected void setViewDate(int position) {
        GroupMemberBean item = getItem(position);
        if(dbBinding instanceof ItemGroupInfoMemberBinding){
          ItemGroupInfoMemberBinding itemBinding= (ItemGroupInfoMemberBinding) dbBinding;
          itemBinding.setItem(item);
          itemBinding.ivLabel.setVisibility(GONE);
          itemBinding.ivLabelYct.setVisibility(GONE);
          //角色(0：普通成员, 1：群主)
          if(item.getRole()==1){
            itemBinding.ivLabel.setVisibility(VISIBLE);
            itemBinding.ivLabel.setImageResource(R.drawable.bg_group_menber_label_qz);
          }else if(item.isLeader()){
            itemBinding.ivLabel.setVisibility(VISIBLE);
            itemBinding.ivLabel.setImageResource(R.drawable.bg_group_menber_label_ld);
          }else if(item.getPayingUser()){
            itemBinding.ivLabelYct.setVisibility(VISIBLE);
            itemBinding.ivLabelYct.setImageResource(R.drawable.bg_group_menber_label_yct);
          }
        }else if(dbBinding instanceof ItemGroupInfoCarBinding){
          ItemGroupInfoCarBinding itemBinding= (ItemGroupInfoCarBinding) dbBinding;
          itemBinding.setItem(item);
          itemBinding.tvLabel.setVisibility(GONE);
          if(item.isLeader()){
            itemBinding.tvLabel.setVisibility(VISIBLE);
          }
        }
      }
    }
  }
}