package net.jg.framework.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.xmzt.www.R;

import net.jg.framework.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Averysk
 * @date 2017/08/09
 */
public class DynamicSoreView<T> extends LinearLayout {
    Context mContext;
    private ViewPager viewPager;
    private LinearLayout llIndicator;

    //选中点
    private int RadioSelect;
    //未选中点
    private int RadioUnselected;
    //圆点间距
    private int distance;
    //每页展示几个
    private int number;
    //展示布局
    private Integer inflater;
    //展示数据的gridView
    private Integer gridView;
    //总页数
    private int page;
    //当前页数
    private int currentItem;
    //数据List
    private List<T> dataList;
    //延迟时间
    private int delayTime = 2000;
    //是否允许自动轮滚
    private boolean isAutoPlay = false;

    List<View> listSoreView = new ArrayList<>();
    View soreView;


    //接口
    private IDynamicSore iDynamicSore;
    //接口定义
    public interface IDynamicSore<T> {
        void setGridView(View view, int type, List<T> data);
    }
    //设置接口
    public IDynamicSore getiDynamicSore() {
        return iDynamicSore;
    }

    public void setiDynamicSore(IDynamicSore iDynamicSore) {
        this.iDynamicSore = iDynamicSore;
    }

    public Integer getLayoutId() {
        return setLayoutId(0);
    }

    public Integer setLayoutId(Integer resId){
        if (resId == 0){
            return R.layout.viewpager_default;
        }
        return resId;
    }

    public DynamicSoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DynamicSoreView);
        if (typedArray != null) {
            inflater = typedArray.getResourceId(R.styleable.DynamicSoreView_SoreViewLayout,R.layout.viewpager_sore);
            LayoutInflater.from(context).inflate(inflater, this, true);
            viewPager = (ViewPager) findViewById(R.id.viewPager);
            llIndicator = (LinearLayout) findViewById(R.id.llIndicator);

            //选中点
            RadioSelect = typedArray.getResourceId(R.styleable.DynamicSoreView_SoreRadioSelect, R.drawable.radio_select);
            //未选中点
            RadioUnselected = typedArray.getResourceId(R.styleable.DynamicSoreView_SoreRadioUnselected, R.drawable.radio_unselected);
            //圆点间距
            distance = typedArray.getInteger(R.styleable.DynamicSoreView_SoreDistance,10);
            //每页显示几个
            number = typedArray.getInteger(R.styleable.DynamicSoreView_SoreNumber,8);
            gridView = typedArray.getResourceId(R.styleable.DynamicSoreView_SoreViewDefault,R.layout.viewpager_default);
            typedArray.recycle();

        }
        //设置空布局
        //gridView = getLayoutId();
    }

    //初始化ViewPager
    private void initViewPager(){
        listSoreView = new ArrayList<>();
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < page; i++) {
            //循环拿到传入的View
            soreView = layoutInflater.inflate(gridView, null);
            //通过接口回掉的形式返回当前的View,实现接口后开源拿到每个View然后进行操作
            if (iDynamicSore!=null){
                List<T> data;
                int total = dataList.size();
                if(i == page-1){
                    //添加按钮
                    data = new ArrayList<>();
                    for(int j = i*number;j<total;j++){
                        data.add(dataList.get(j));
                    }
                }else{
                    data = new ArrayList<>();
                    int size;
                    if(total< number){
                        size = total;
                    }else{
                        size = (i+1)*number;
                    }
                    for(int j = i*number;j<size;j++){
                        data.add(dataList.get(j));
                    }
                }
                iDynamicSore.setGridView(soreView,i,data);
            }
            //将获取到的View添加到List中
            listSoreView.add(soreView);
        }
        //设置viewPager的Adapter
        viewPager.setAdapter(new ViewPagerAdapter(listSoreView));
        //初始化LinearLayout，加入指示器
        initLinearLayout(viewPager, page, llIndicator);
    }

    /**
     * 设置指示器，设置ViewPager滑动事件监听
     * @param viewPager --ViewPager
     * @param pageSize --View的页数
     * @param linearLayout --LinearLayout
     */
    private void initLinearLayout(ViewPager viewPager, int pageSize, LinearLayout linearLayout) {
        linearLayout.removeAllViews();
        //定义数组放置指示器的点，pageSize是View个数
        final ImageView[] imageViews = new ImageView[pageSize];
        for (int i = 0; i < pageSize; i++) {
            //创建ImageView
            ImageView image = new ImageView(mContext);
            //将ImageView放入数组
            imageViews[i] = image;
            //默认选中第一个
            if (i == 0) {
                //选中的点
                image.setImageResource(RadioSelect);
            } else {
                //未选中的点
                image.setImageResource(RadioUnselected);
            }
            //设置宽高
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(distance, 0, distance, 0);
            //将点添加到LinearLayout中
            linearLayout.addView(image, params);
        }

        //ViewPager的滑动事件
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {}
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}
            @Override
            public void onPageSelected(int arg0) {
                currentItem=arg0;
                //arg0当前ViewPager
                for (int i = 0; i < imageViews.length; i++) {
                    //设置为选中的点
                    imageViews[arg0].setImageResource(RadioSelect);
                    //判断当前的点i如果不等于当前页的话就设置为未选中
                    if (arg0 != i) {
                        imageViews[i].setImageResource(RadioUnselected);
                    }
                }
            }
        });
    }

    /**
     * 设置view
     * @param gridView
     * @return
     */
    public DynamicSoreView setGridView(Integer gridView){
        this.gridView = gridView;
        return this;
    }
    /**
     * 设置初始化
     */
    public DynamicSoreView init(List<T> t){
        this.dataList = t;
        this.page = (int) Math.ceil((double) t.size()/number);//计算出有几页/这里用了ceil函数凑整，2.1=3
        this.currentItem = 0;
        initViewPager();
        if (isAutoPlay){
            startAutoPlay();
        }
        return this;
    }

    /**
     * 设置是否显示指示图标点
     * @param isShow
     */
    public void setIsShowIndicator(boolean isShow){
        if (isShow){
            llIndicator.setVisibility(View.VISIBLE);
        } else {
            llIndicator.setVisibility(View.GONE);
        }
    }

    public void isAutoPlay(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.i(tag, ev.getAction() + "--" + isAutoPlay);
        if (isAutoPlay) {
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                startAutoPlay();
            } else if (action == MotionEvent.ACTION_DOWN) {
                stopAutoPlay();
            } else {
                startAutoPlay();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void startAutoPlay() {
        handler.removeCallbacks(task);
        handler.postDelayed(task, delayTime);
    }

    public void stopAutoPlay() {
        handler.removeCallbacks(task);
    }

    private Handler handler = new Handler();

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (page > 1 && isAutoPlay) {
                currentItem = currentItem % (page + 1) + 1;
                if (currentItem == page){
                    currentItem = 0;
                }
                viewPager.setCurrentItem(currentItem);
                handler.postDelayed(task, delayTime);
            }
        }
    };

}
