package cn.xmzt.www.nim.uikit.common.adapter;

import android.view.View;

/**
 */
public interface OnItemClickListener<T> {

    void onClick(View v, int pos, T data);

    boolean onLongClick(View v, int pos, T data);
}
