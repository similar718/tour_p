package cn.xmzt.www.route.adapter;


import androidx.recyclerview.widget.RecyclerView;

import cn.xmzt.www.view.listener.ItemListener;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecycleViewAdapter<T,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected ItemListener itemListener;
    protected List<T> datas = new ArrayList<T>();

    public List<T> getDatas() {
        if (datas==null)
            datas = new ArrayList<T>();
        return datas;
    }

    public void setDatas(List<T> datas) {
        if(this.datas==null){
            this.datas = new ArrayList<T>();
        }
        this.datas.clear();
        if(datas!=null){
            this.datas.addAll(datas) ;
        }
    }
    public abstract T getItem(int position);
    public void setItemListener(ItemListener listener){
        this.itemListener = listener;
    }

}