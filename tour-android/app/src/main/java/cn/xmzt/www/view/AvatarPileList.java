package cn.xmzt.www.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import cn.xmzt.www.R;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.intercom.activity.GroupPersonalInfoActivity;
import cn.xmzt.www.intercom.bean.GroupMemberBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 头像堆叠效果
 */
public class AvatarPileList<T> extends RecyclerView {
  private ItemAdapter itemAdapter=null;
  private GridLayoutManager manager;
  private String groupId;

  public AvatarPileList(Context context) {
    super(context);
    init(context, null);
  }

  public AvatarPileList(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public AvatarPileList(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    manager=new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL,false);
    setLayoutManager(manager);
    itemAdapter=new ItemAdapter();
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

  private List<T> dataList=new ArrayList<>();
  public void setDataList(List<T> dataList) {
    this.dataList.clear();
    if(dataList.size()<=9){
      this.dataList.addAll(dataList);
    }else {
      this.dataList.addAll(dataList.subList(0,9));
    }
    itemAdapter.notifyDataSetChanged();
  }
  public T getItem(int position) {
    if (dataList!=null&&position < dataList.size()) {
      return dataList.get(position);
    } else {
      return null;
    }
  }
  private RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();
  class ItemAdapter extends Adapter<ItemAdapter.ItemHolder> {

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_avatar_pile, parent, false);
      ItemHolder viewHolder = new ItemHolder(view);
      pool.putRecycledView(viewHolder);
      return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemHolder viewHolder,int position) {
      viewHolder.setViewDate(position);
    }

    public T getItem(int position) {
      if (dataList!=null&&position < dataList.size()) {
        return dataList.get(position);
      } else {
        return null;
      }
    }

    @Override
    public int getItemCount() {
      if(dataList!=null){
        return dataList.size();
      }else{
        return 0;
      }
    }
    public class ItemHolder extends ViewHolder {
      FrameLayout avatarPileLayout;
      ImageView iv_avatar;
      int dp_15=0;
      int dp_16=0;
      int itemW=0;
      public ItemHolder(View itemView) {
        super(itemView);
        avatarPileLayout = itemView.findViewById(R.id.avatarPileLayout);
        iv_avatar = itemView.findViewById(R.id.iv_avatar);
        Resources mResources=avatarPileLayout.getResources();
        dp_15=avatarPileLayout.getResources().getDimensionPixelOffset(R.dimen.dp_15);
        dp_16=avatarPileLayout.getResources().getDimensionPixelOffset(R.dimen.dp_16);
        itemW=(mResources.getDisplayMetrics().widthPixels-2*dp_16)/10;
        itemW=48*mResources.getDisplayMetrics().widthPixels/375;
        iv_avatar.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            Object obj=getItem(getAdapterPosition());
            if(obj instanceof GroupMemberBean){
              //进入成员信息
              GroupMemberBean mGroupMemberBean= (GroupMemberBean) obj;
              Intent intent=new Intent(v.getContext(), GroupPersonalInfoActivity.class);
              intent.putExtra("userId",""+mGroupMemberBean.getUserId());
              intent.putExtra("teamId",groupId);
              v.getContext().startActivity(intent);
            }
          }
        });
      }
      protected void setViewDate(int position) {
        Object obj=getItem(position);
        RecyclerView.LayoutParams mLayoutParams= (RecyclerView.LayoutParams) avatarPileLayout.getLayoutParams();
        ViewGroup.LayoutParams imgParams= iv_avatar.getLayoutParams();
        imgParams.width=itemW;
        imgParams.height=itemW;
        if(position==0){
          mLayoutParams.leftMargin=0;
        }else {
          mLayoutParams.leftMargin=-dp_15;
        }
        if(obj instanceof GroupMemberBean){
          GroupMemberBean memberBean= (GroupMemberBean) obj;
          GlideUtil.setImageCircle(iv_avatar.getContext(),memberBean.getImage(),iv_avatar,R.drawable.icon_head_default);
        }else if(obj instanceof String){
          GlideUtil.setImageCircle(iv_avatar.getContext(), (String) obj,iv_avatar,R.drawable.icon_head_default);
        }
      }
    }
  }
}
