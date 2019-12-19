package cn.xmzt.www.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class ViewPagerIndicator extends LinearLayout {


	public ViewPagerIndicator(Context context) {
		this(context, null);
	}

	public ViewPagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}


	public void scroll(int position, float offset) {
		invalidate();
	}

}