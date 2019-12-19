package cn.xmzt.www.view;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.view.listener.ItemListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 26个字母排序滑块
 */
public class LetterSideBar extends RecyclerView {
  private LetterAdapter letterAdapter=null;
  private LinearLayoutManager manager;
  private ItemListener itemListener;

  public void setItemListener(ItemListener itemListener) {
    this.itemListener = itemListener;
  }

  public LetterSideBar(Context context) {
    super(context);
    init(context, null);
  }

  public LetterSideBar(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public LetterSideBar(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
    setLayoutManager(manager);
    letterAdapter=new LetterAdapter();
    Collections.addAll(letterList,letters);
    setAdapter(letterAdapter);
  }

  @Override
  protected void onMeasure(int widthSpec, int heightSpec) {
    super.onMeasure(widthSpec, heightSpec);
  }
  String[] letters = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
          "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
          "W", "X", "Y", "Z"};
  private List<String> letterList=new ArrayList();
  public List<String> getLetterList() {
    return letterList;
  }

  public void setLetterList(List<String> letterList) {
    this.letterList = letterList;
    letterAdapter.notifyDataSetChanged();
  }
  public String getItem(int position) {
    if (letterList!=null&&position < letterList.size()) {
      return letterList.get(position);
    } else {
      return null;
    }
  }
  class LetterAdapter extends Adapter<LetterAdapter.ItemHolder> {
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_letter, parent, false);
      ItemHolder viewHolder = new ItemHolder(view);
      return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemHolder viewHolder,int position) {
      viewHolder.setViewDate(position);
    }

    public String getItem(int position) {
      if (letterList!=null&&position < letterList.size()) {
        return letterList.get(position);
      } else {
        return null;
      }
    }

    @Override
    public int getItemCount() {
      if(letterList!=null){
        return letterList.size();
      }else{
        return 0;
      }
    }
    public class ItemHolder extends ViewHolder {
      TextView tv_letter_name;
      public ItemHolder(View itemView) {
        super(itemView);
        tv_letter_name = itemView.findViewById(R.id.tv_letter_name);
        tv_letter_name.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            if(itemListener!=null){
              itemListener.onItemClick(v,getAdapterPosition());
            }
          }
        });
      }
      protected void setViewDate(int position) {
        tv_letter_name.setText(getItem(position));
      }
    }
  }
}
