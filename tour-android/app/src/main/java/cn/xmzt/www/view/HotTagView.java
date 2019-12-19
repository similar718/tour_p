package cn.xmzt.www.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.xmzt.www.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xujin on 16/08/24.
 */
public class HotTagView extends ViewGroup {
    private Paint mBackgroudPaint;//背景
    private int mBackgroudPaintColor = Color.LTGRAY;
    private Paint mLinePaint;//外围线条
    private int mLinePaintColor = Color.BLUE;

    private float mTextSize;
    private int mTextColor = Color.WHITE;

    private float mTextViewPaddingLeft;
    private float mTextViewPaddingRight;
    private float mTextViewPaddingTop;
    private float mTextViewPaddingBottom;

    private float mTextViewMarginLeft;
    private float mTextViewMarginRight;
    private float mTextViewMarginTop;
    private float mTextViewMarginBottom;

    private List<String> tags = new ArrayList<String>();
    private List<TagView> tagViews = new ArrayList<TagView>();//返回所有的TagView

    //小TagView
    private float rx = 10, ry = 10;
    private float mStrokeWidth = 5;


    public int getmBackgroudPaintColor() {
        return mBackgroudPaintColor;
    }

    public void setmBackgroudPaintColor(int mBackgroudPaintColor) {
        this.mBackgroudPaintColor = mBackgroudPaintColor;
    }

    public int getmLinePaintColor() {
        return mLinePaintColor;
    }

    public void setmLinePaintColor(int mLinePaintColor) {
        this.mLinePaintColor = mLinePaintColor;
    }

    public float getmStrokeWidth() {
        return mStrokeWidth;
    }

    public void setmStrokeWidth(float mStrokeWidth) {
        this.mStrokeWidth = mStrokeWidth;
    }

    public float getmTextViewMarginBottom() {
        return mTextViewMarginBottom;
    }

    public void setmTextViewMarginBottom(float mTextViewMarginBottom) {
        this.mTextViewMarginBottom = mTextViewMarginBottom;
    }

    public float getmTextViewMarginLeft() {
        return mTextViewMarginLeft;
    }

    public void setmTextViewMarginLeft(float mTextViewMarginLeft) {
        this.mTextViewMarginLeft = mTextViewMarginLeft;
    }

    public float getmTextViewMarginRight() {
        return mTextViewMarginRight;
    }

    public void setmTextViewMarginRight(float mTextViewMarginRight) {
        this.mTextViewMarginRight = mTextViewMarginRight;
    }

    public float getmTextViewMarginTop() {
        return mTextViewMarginTop;
    }

    public void setmTextViewMarginTop(float mTextViewMarginTop) {
        this.mTextViewMarginTop = mTextViewMarginTop;
    }

    public float getmTextViewPaddingBottom() {
        return mTextViewPaddingBottom;
    }

    public void setmTextViewPaddingBottom(float mTextViewPaddingBottom) {
        this.mTextViewPaddingBottom = mTextViewPaddingBottom;
    }

    public float getmTextViewPaddingLeft() {
        return mTextViewPaddingLeft;
    }

    public void setmTextViewPaddingLeft(float mTextViewPaddingLeft) {
        this.mTextViewPaddingLeft = mTextViewPaddingLeft;
    }

    public float getmTextViewPaddingRight() {
        return mTextViewPaddingRight;
    }

    public void setmTextViewPaddingRight(float mTextViewPaddingRight) {
        this.mTextViewPaddingRight = mTextViewPaddingRight;
    }

    public float getmTextViewPaddingTop() {
        return mTextViewPaddingTop;
    }

    public void setmTextViewPaddingTop(float mTextViewPaddingTop) {
        this.mTextViewPaddingTop = mTextViewPaddingTop;
    }

    public float getRx() {
        return rx;
    }

    public void setRx(float rx) {
        this.rx = rx;
    }

    public float getRy() {
        return ry;
    }

    public void setRy(float ry) {
        this.ry = ry;
    }

    public List<String> getTags() {
        return tags;
    }

    /**
     * 获取已经选择position的tag
     * @param position
     * @return
     */
    public String getSelectedTag(int position){
        String selectTag=null;
        if(tagViews.size()>position){
            TagView tagView = tagViews.get(position);
            if(tagView.isSelected()){
                selectTag=tagView.getText().toString();
            }
        }
        return selectTag;
    }

    /**
     * 设置选择position的 tag
     * @param selected
     * @param position
     */
    public void setSelectTag(boolean selected,int position){
        String selectTag=null;
        if(tagViews.size()>position){
            TagView tagView = tagViews.get(position);
            tagView.setSelected(selected);
        }
    }

    /**
     * 设置所有的tag的选择
     * @param selected
     */
    public void setSelectTagAll(boolean selected){
        for( int i=0;i<tagViews.size();i++){
            TagView tagView=tagViews.get(i);
            tagView.setSelected(selected);
        }
    }

    /**
     * 获取已经选择tags
     * @return
     */
    public List<String> getSelectTagAll() {
        List<String> selectTags = new ArrayList<String>();
        for( int i=0;i<tagViews.size();i++){
            TagView tagView=tagViews.get(i);
            if(tagView.isSelected()){
                selectTags.add(tagView.getText().toString());
            }
        }
        return selectTags;
    }

    public HotTagView(Context context) {
        super(context);
    }

    public HotTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HotTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HotTagView);
        mBackgroudPaintColor = ta.getColor(R.styleable.HotTagView_background_color, Color.LTGRAY);
        mLinePaintColor = ta.getColor(R.styleable.HotTagView_line_color, Color.LTGRAY);
        mStrokeWidth = ta.getDimension(R.styleable.HotTagView_stroke_width, 3);

        mTextViewMarginLeft = ta.getDimension(R.styleable.HotTagView_textview_margin_left, 2);
        mTextViewMarginRight = ta.getDimension(R.styleable.HotTagView_textview_margin_right, 2);
        mTextViewMarginTop = ta.getDimension(R.styleable.HotTagView_textview_margin_top, 2);
        mTextViewMarginBottom = ta.getDimension(R.styleable.HotTagView_textview_margin_bottom, 2);

        mTextViewPaddingLeft = ta.getDimension(R.styleable.HotTagView_textview_padding_left, 2);
        mTextViewPaddingRight = ta.getDimension(R.styleable.HotTagView_textview_padding_right, 2);
        mTextViewPaddingTop = ta.getDimension(R.styleable.HotTagView_textview_padding_top, 2);
        mTextViewPaddingBottom = ta.getDimension(R.styleable.HotTagView_textview_padding_bottom, 2);

        mTextColor = ta.getColor(R.styleable.HotTagView_text_color, Color.WHITE);
        mTextSize = ta.getDimension(R.styleable.HotTagView_text_size, 6);

        float mTextViewPadding = ta.getDimension(R.styleable.HotTagView_textview_padding, 0);
        if (mTextViewPadding != 0) {
            mTextViewPaddingLeft = mTextViewPadding;
            mTextViewPaddingRight = mTextViewPadding;
            mTextViewPaddingBottom = mTextViewPadding;
            mTextViewPaddingTop = mTextViewPadding;
        }

        float mTextViewMargin = ta.getDimension(R.styleable.HotTagView_textview_margin, 0);
        if (mTextViewMargin != 0) {
            mTextViewMarginLeft = mTextViewMargin;
            mTextViewMarginRight = mTextViewMargin;
            mTextViewMarginTop = mTextViewMargin;
            mTextViewMarginBottom = mTextViewMargin;
        }
        ta.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        //wrap_content
        int width = 0;//整个大view的宽度，就是所有lineWidth的最大值
        int height = 0;

        //每一行的宽度和高度
        int lineWidth = 0;
        int lineHeight = 0;

        //内部元素个数
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            //测量子view的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            //得到LayoutParams
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();//这个是Linearlayout的

            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            //如果这一行不够了，换行
            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {
                //得到最大宽度
                width = Math.max(width, lineWidth);
                //lineWidth
                lineWidth = childWidth;
                //记录行高
                height += lineHeight;
                lineHeight = childHeight;
            } else {//未换行
                //叠加行宽
                lineWidth += childWidth;
                //得到当前行最大高度
                lineHeight = Math.max(lineHeight, childHeight);
            }


            //到达最后一个控件
            if (i == cCount - 1) {
                width = Math.max(lineWidth, width);
                height += lineHeight;
            }
        }

        setMeasuredDimension(
                modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width + getPaddingLeft() + getPaddingRight(),
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingTop() + getPaddingBottom()
        );
    }

    /**
     * 存储所有的View,一行一行的存
     */
    private List<List<View>> mAllViews = new ArrayList<List<View>>();
    /**
     * 存储每一行的高度
     */
    private List<Integer> mLineHeight = new ArrayList<>();


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();

        //当前ViewGroup的宽度
        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;

        List<View> lineViews = new ArrayList<>();

        int cCount = getChildCount();
        Log.v("MYTAG:", cCount + "");
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            //重新开了一行
            if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width - getPaddingLeft() - getPaddingRight()) {
                //记录lineheight
                mLineHeight.add(lineHeight);

                //当前行的Views
                mAllViews.add(lineViews);

                //重置行宽，高
                lineWidth = 0;
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;

                lineViews = new ArrayList<View>();
            }


            lineWidth += childWidth + lp.rightMargin + lp.leftMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin
                    + lp.bottomMargin);
            lineViews.add(child);

        }


        //处理最后一行
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);


        //设置子View的位置
        int left = getPaddingLeft();
        int top = getPaddingTop();

        //行数
        int lineNum = mAllViews.size();

        for (int i = 0; i < lineNum; i++) {
            lineViews = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);

            //遍历行
            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }

                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();
                child.layout(lc, tc, rc, bc);

                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }

            //重置left
            left = getPaddingLeft();
            top += lineHeight;
        }


    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    public void setTags(List<String> tags) {
        removeAllViews();
        tagViews.clear();
        for (String tag : tags) {
            TagView tv = new TagView(getContext(), tag);
            tv.setTextColor(mTextColor);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            MarginLayoutParams lp = new MarginLayoutParams(
                    MarginLayoutParams.WRAP_CONTENT,
                    MarginLayoutParams.WRAP_CONTENT
            );
            lp.setMargins((int) mTextViewMarginLeft,
                    (int) mTextViewMarginTop,
                    (int) mTextViewMarginRight,
                    (int) mTextViewMarginBottom);
            this.addView(tv, lp);
            tagViews.add(tv);

            final CharSequence cs = tag;
            if (null != onItemClickListener) {
                tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(v, cs.toString(), indexOfChild(v));
                    }
                });
            }
        }
        invalidate();
    }


    public List<TagView> getTagViews() {
        return tagViews;
    }
    public TagView getTagView(int position) {
        if(tagViews.size()>position){
            TagView tagView = tagViews.get(position);
            return tagView;
        }else{
            return null;
        }
    }

    public void clearItemBackground() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            TextView tv = (TextView) getChildAt(i);
            tv.setTextColor(mTextColor);
            tv.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    /**
     * 替换所有item颜色和背景
     * @param textColor
     * @param backgroundColor
     */
    public void replaceItemBackgroundAllColor(int textColor,int backgroundColor) {
        int count = tagViews.size();
        for (int i = 0; i < count; i++) {
            TagView tv = tagViews.get(i);
            tv.setTextColor(textColor);
            tv.setmBackgroudPaintColor(backgroundColor);
        }
    }

    /**
     * 替换item颜色和背景
     * @param position
     * @param textColor
     * @param backgroundColor
     */
    public void replaceItemBackgroundAllColor(int position,int textColor,int backgroundColor) {
        int count = tagViews.size();
        if(count>position){
            TagView tv = tagViews.get(position);
            tv.setTextColor(textColor);
            tv.setmBackgroudPaintColor(backgroundColor);
        }
    }

    /**
     * 设置选中tag的样式
     * @param tv
     * @param isSelected
     */
    public void setTagViewSelected(TagView tv, boolean isSelected) {
        if (tv != null) {
            tv.setSelected(isSelected);
            if (tv.isSelected()) {
                tv.setTextColor(Color.WHITE);
                tv.setmBackgroudPaintColor(Color.parseColor("#666666"));
            } else {
                tv.setTextColor(Color.parseColor("#333333"));
                tv.setmBackgroudPaintColor(Color.WHITE);
            }
        }
    }
    public class TagView extends androidx.appcompat.widget.AppCompatTextView {

        float top;
        float left;
        float right;
        float bottom;

        int mBackgroudPaintColor;
        Paint mBackgroudPaint;

        int mLinePaintColor;
        Paint mLinePaint;

        float mTextSize;
        int mTextColor;

        float mStrokeWidth;

        public TagView(Context context) {
            super(context);
        }

        public TagView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public TagView(Context context, CharSequence tag) {
            super(context);

            //初始化
            mBackgroudPaintColor = HotTagView.this.mBackgroudPaintColor;
            mBackgroudPaint = HotTagView.this.mBackgroudPaint;
            mLinePaint = HotTagView.this.mLinePaint;
            mLinePaintColor = HotTagView.this.mLinePaintColor;
            mStrokeWidth = HotTagView.this.mStrokeWidth;
            mTextSize = HotTagView.this.mTextSize;
            mTextColor = HotTagView.this.mTextColor;

            TagView.this.setPadding((int) mTextViewPaddingLeft, (int) mTextViewPaddingTop,
                    (int) mTextViewPaddingRight, (int) mTextViewPaddingBottom);
            setGravity(TEXT_ALIGNMENT_CENTER);
            setText(tag);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            top = 0;
            left = 0;
            right = getWidth();
            bottom = getHeight();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            RectF rectF = new RectF(left + mStrokeWidth / 2,
                    top + mStrokeWidth / 2,
                    right - mStrokeWidth / 2,
                    bottom - mStrokeWidth / 2);

            mBackgroudPaint = new Paint();
            mBackgroudPaint.setColor(mBackgroudPaintColor);
            canvas.drawRoundRect(rectF, rx, ry, mBackgroudPaint);

            mLinePaint = new Paint();
            mLinePaint.setColor(mLinePaintColor);
            mLinePaint.setStyle(Paint.Style.STROKE);
            mLinePaint.setStrokeWidth(mStrokeWidth);
            mLinePaint.setAntiAlias(true);
            canvas.drawRoundRect(rectF, rx, ry, mLinePaint);
            super.onDraw(canvas);
        }

        public void setmBackgroudPaintColor(int mBackgroudPaintColor) {
            this.mBackgroudPaintColor = mBackgroudPaintColor;
            invalidate();
        }

        public void setmLinePaintColor(int mLinePaintColor) {
            this.mLinePaintColor = mLinePaintColor;
            invalidate();

        }

        public void setmStrokeWidth(float mStrokeWidth) {
            this.mStrokeWidth = mStrokeWidth;
            invalidate();
        }

        public void setmTextColor(int mTextColor) {
            this.mTextColor = mTextColor;
            invalidate();
        }
    }

    private IOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(IOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface IOnItemClickListener {
        void onItemClick(View tv, String itemText, int position);
    }
}
