package cn.xmzt.www.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.xmzt.www.R;


public class RoundRelativeLayout extends RelativeLayout {
    private Path path;
    private float radius = 20f;

    public RoundRelativeLayout(@NonNull Context context) {
        this(context, null);
    }

    public RoundRelativeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundRelativeLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundRelativeLayout);
            radius = ta.getDimension(R.styleable.RoundRelativeLayout_r_radius, 20);
            ta.recycle();
        }
        path = new Path();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        path.reset();
        path.addRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), radius, radius, Path.Direction.CW);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
        canvas.clipPath(path, Region.Op.REPLACE);
        super.dispatchDraw(canvas);
    }
}
