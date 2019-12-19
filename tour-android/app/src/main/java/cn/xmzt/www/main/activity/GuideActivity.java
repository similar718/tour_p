package cn.xmzt.www.main.activity;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.nim.uikit.common.media.imagepicker.view.SystemBarTintManager;
import cn.xmzt.www.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * @author tanlei
 * @date 2019/9/24
 * @describe 引导页
 */
public class GuideActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private List<ImageView> imageViewList = new ArrayList<>();
    private ImageView iv,iv_jump;
    private ImageView tv1,tv2,tv3,tv4;
    private Drawable mSelectedDrawable;
    private Drawable mUnselectedDrawable;
    private LinearLayout ll_bottom_spot;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //使用SystemBarTint库使4.4版本状态栏变色，需要先将状态栏设置为透明
//            transparencyBar(activity);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.white);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);

        viewPager = findViewById(R.id.vp);
        iv = findViewById(R.id.iv);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        iv_jump = findViewById(R.id.iv_jump);
        ll_bottom_spot = findViewById(R.id.ll_bottom_spot);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.start(GuideActivity.this);
                SPUtils.putBoolean("isFirst",true);
                finish();
            }
        });
        iv_jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.start(GuideActivity.this);
                SPUtils.putBoolean("isFirst",true);
                finish();
            }
        });
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ImageView imageView1 = new ImageView(this);
        imageView1.setLayoutParams(layoutParams);
        imageView1.setImageResource(R.drawable.guide_bg1);
        imageView1.setBackgroundColor(getResources().getColor(R.color.color_FF_FF_FF));
        imageViewList.add(imageView1);

        ImageView imageView2 = new ImageView(this);
        imageView2.setLayoutParams(layoutParams);
        imageView2.setImageResource(R.drawable.guide_bg2);
        imageView1.setBackgroundColor(getResources().getColor(R.color.color_FF_FF_FF));
        imageViewList.add(imageView2);

        ImageView imageView3 = new ImageView(this);
        imageView3.setLayoutParams(layoutParams);
        imageView3.setImageResource(R.drawable.guide_bg3);
        imageView1.setBackgroundColor(getResources().getColor(R.color.color_FF_FF_FF));
        imageViewList.add(imageView3);

        ImageView imageView4 = new ImageView(this);
        imageView4.setLayoutParams(layoutParams);
        imageView4.setImageResource(R.drawable.guide_bg4);
        imageView1.setBackgroundColor(getResources().getColor(R.color.color_FF_FF_FF));
        imageViewList.add(imageView4);

        viewPager.setAdapter(new GuidePageAdapter());
        viewPager.setOnPageChangeListener(new GuidePageChangeListener());

        if (mSelectedDrawable == null) {
            //绘制默认选中状态图形
            GradientDrawable selectedGradientDrawable = new GradientDrawable();
            selectedGradientDrawable.setShape(GradientDrawable.RECTANGLE);
            selectedGradientDrawable.setColor(getResources().getColor(R.color.color_24A4FF));
            selectedGradientDrawable.setSize(dp2px(17), dp2px(6));
            selectedGradientDrawable.setCornerRadius(dp2px(6) / 2);
            mSelectedDrawable = new LayerDrawable(new Drawable[]{selectedGradientDrawable});
        }
        if (mUnselectedDrawable == null) {
            //绘制默认未选中状态图形
            GradientDrawable unSelectedGradientDrawable = new GradientDrawable();
            unSelectedGradientDrawable.setShape(GradientDrawable.OVAL);
            unSelectedGradientDrawable.setColor(getResources().getColor(R.color.color_24A4FF_30));
            unSelectedGradientDrawable.setSize(dp2px(6), dp2px(6));
            unSelectedGradientDrawable.setCornerRadius(dp2px(6) / 2);
            mUnselectedDrawable = new LayerDrawable(new Drawable[]{unSelectedGradientDrawable});
        }

        tv1.setImageDrawable(mSelectedDrawable);
        tv2.setImageDrawable(mUnselectedDrawable);
        tv3.setImageDrawable(mUnselectedDrawable);
        tv4.setImageDrawable(mUnselectedDrawable);
        iv.setVisibility(View.GONE);
        ll_bottom_spot.setVisibility(View.VISIBLE);
    }

    protected int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    class GuidePageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViewList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = imageViewList.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    class GuidePageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            tv1.setImageDrawable(mUnselectedDrawable);
            tv2.setImageDrawable(mUnselectedDrawable);
            tv3.setImageDrawable(mUnselectedDrawable);
            tv4.setImageDrawable(mUnselectedDrawable);
            switch (position){
                case 0:
                    tv1.setImageDrawable(mSelectedDrawable);
                    iv.setVisibility(View.GONE);
                    ll_bottom_spot.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    tv2.setImageDrawable(mSelectedDrawable);
                    iv.setVisibility(View.GONE);
                    ll_bottom_spot.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    tv3.setImageDrawable(mSelectedDrawable);
                    iv.setVisibility(View.GONE);
                    ll_bottom_spot.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    tv4.setImageDrawable(mSelectedDrawable);
                    iv.setVisibility(View.VISIBLE);
                    ll_bottom_spot.setVisibility(View.GONE);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
