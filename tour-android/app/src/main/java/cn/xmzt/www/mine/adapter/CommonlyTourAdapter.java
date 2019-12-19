package cn.xmzt.www.mine.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.mine.bean.TourBean;

import java.util.List;

/**
 * @author tanlei
 * @date 2019/8/15
 * @describe
 */

public class CommonlyTourAdapter extends RecyclerView.Adapter<CommonlyTourAdapter.TourViewHolder> {
    private Context context;
    private List<TourBean> list;
    private OnEditOrDeleteClickListener onEditOrDeleteClickListener;

    public OnEditOrDeleteClickListener getOnEditOrDeleteClickListener() {
        return onEditOrDeleteClickListener;
    }

    public void setOnEditOrDeleteClickListener(OnEditOrDeleteClickListener onEditOrDeleteClickListener) {
        this.onEditOrDeleteClickListener = onEditOrDeleteClickListener;
    }

    public CommonlyTourAdapter(Context context, List<TourBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TourViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TourViewHolder(LayoutInflater.from(context).inflate(R.layout.item_commonly_tourer, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TourViewHolder tourViewHolder, int i) {
        tourViewHolder.name_tv.setText(list.get(i).getName());
        tourViewHolder.phone_tv.setText(list.get(i).getTel());
        tourViewHolder.id_tv.setText(list.get(i).getIdentityCard());
        tourViewHolder.edit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getOnEditOrDeleteClickListener() != null) {
                    onEditOrDeleteClickListener.onEditOrDeleteClick(i, 0);
                }
            }
        });
        tourViewHolder.delete_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getOnEditOrDeleteClickListener() != null) {
                    onEditOrDeleteClickListener.onEditOrDeleteClick(i, 1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class TourViewHolder extends RecyclerView.ViewHolder {
        private TextView name_tv, phone_tv, id_tv, edit_tv, delete_tv;

        public TourViewHolder(@NonNull View itemView) {
            super(itemView);
            name_tv = itemView.findViewById(R.id.name_tv);
            phone_tv = itemView.findViewById(R.id.phone_tv);
            id_tv = itemView.findViewById(R.id.id_tv);
            edit_tv = itemView.findViewById(R.id.edit_tv);
            delete_tv = itemView.findViewById(R.id.delete_tv);
        }
    }

    public interface OnEditOrDeleteClickListener {
        void onEditOrDeleteClick(int position, int type);
    }
}
