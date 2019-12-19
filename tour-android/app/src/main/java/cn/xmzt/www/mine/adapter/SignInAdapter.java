package cn.xmzt.www.mine.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.mine.bean.SignDayBean;

import java.util.List;

/**
 * @author tanlei
 * @date 2019/8/23
 * @describe
 */

public class SignInAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SignDayBean> list;
    private Context context;
    private static final int TYPE_TEXT = 0;
    private static final int TYPE_IMAGE = 1;

    public SignInAdapter(List<SignDayBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == TYPE_TEXT) {
            return new SignTextViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_sign_text, null));
        } else {
            return new SignImageViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_sign_image, null));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof SignTextViewHolder) {
            ((SignTextViewHolder) viewHolder).tv.setText(list.get(i).getDay() + "");
            if (list.get(i).isSign()) {
                ((SignTextViewHolder) viewHolder).tv.setTextColor(Color.parseColor("#333333"));
            } else {
                ((SignTextViewHolder) viewHolder).tv.setTextColor(Color.parseColor("#DADADA"));
            }
        } else {
            if (list.get(i).isSign()) {
                ((SignImageViewHolder) viewHolder).iv.setImageResource(R.drawable.icon_sign_reward);
            } else {
                ((SignImageViewHolder) viewHolder).iv.setImageResource(R.drawable.icon_sign_reward_un);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 29) {
            return TYPE_TEXT;
        } else {
            return TYPE_IMAGE;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class SignTextViewHolder extends RecyclerView.ViewHolder {
        public TextView tv;

        public SignTextViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
        }
    }

    static class SignImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv;

        public SignImageViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
        }
    }
}
