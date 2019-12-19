package cn.xmzt.www.view;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class ScrollTitleLayout extends LinearLayout implements NestedScrollingParent2 {

	private static final String TAG = ScrollTitleLayout.class.getSimpleName();

	private View topView;
	private View bottomView;

	public ScrollTitleLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		ensureView();
		final int height = MeasureSpec.getSize(heightMeasureSpec);
		measureChild(topView, widthMeasureSpec, heightMeasureSpec);
		measureChild(bottomView, widthMeasureSpec,
					 MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));

		final int desireHeight = topView.getMeasuredHeight() + bottomView.getMeasuredHeight();
		setMeasuredDimension(widthMeasureSpec, Math.min(desireHeight, height));
	}

	/**
	 * 从上往下布局
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int topOffset = 0;
		topView.layout(0, topOffset, topView.getMeasuredWidth(), topOffset + topView.getMeasuredHeight());
		topOffset += topView.getMeasuredHeight();
		bottomView.layout(0, topOffset, bottomView.getMeasuredWidth(), topOffset + bottomView.getMeasuredHeight());

	}

	private void ensureView() {
		if (getChildCount() < 2) {
			throw new IllegalStateException();
		}
		topView = getChildAt(0);
		bottomView = getChildAt(1);
	}

	@Override
	public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
		//如果是竖直方向就返回true,表示接受竖直方向的滚动
		return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
	}

	@Override
	public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
	}

	@Override
	public void onStopNestedScroll(@NonNull View target, int type) {
	}

	@Override
	public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
	}

	@Override
	public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
		//向下滑动，如果符合bottomView不能往下滑并且mScrollY大于0，就要显示顶部
		boolean showTop = dy < 0 && getScrollY() > 0 && !bottomView.canScrollVertically(-1);
		//向上滑动，如果mScrollY小于顶部的高，就要隐藏顶部
		boolean hideTop = dy > 0 && getScrollY() <= hideTopHeight;
		if (showTop || hideTop) {
			scrollBy(0, dy);
			consumed[1] = dy;
			if(onScrollY!=null){
				onScrollY.onScrollY(dy);
			}
		}
	}

	@Override
	public void scrollTo(int x, int y) {
		//限制滚动的范围，不能小于0和大于topView的高度
		if (y <= 0) {
			y = 0;
		}
		if (y >= topView.getMeasuredHeight()) {
			y = topView.getMeasuredHeight();
		}

		super.scrollTo(x, y);

	}
	private OnScrollY onScrollY;
	private int hideTopHeight;//隐藏高度

	public void setHideTopHeight(int hideTopHeight) {
		this.hideTopHeight = hideTopHeight;
	}

	public int getHideTopHeight() {
		return hideTopHeight;
	}

	public void setOnScrollY(OnScrollY onScrollY) {
		this.onScrollY = onScrollY;
	}


	public static interface OnScrollY{
		void onScrollY(int dy);
	}
}