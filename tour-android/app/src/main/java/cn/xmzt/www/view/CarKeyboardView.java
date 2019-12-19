package cn.xmzt.www.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.utils.DpUtil;

/**
 * 车牌号专用键盘
 */
public class CarKeyboardView extends KeyboardView {

    public CarKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public CarKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Keyboard keyboard = getKeyboard();
        if (keyboard == null) return;
        List<Keyboard.Key> keys = keyboard.getKeys();
        if (keys != null && keys.size() > 0) {
            for (Keyboard.Key key : keys) {
                if (key.codes[0] == -3) {
                    drawKeyBackground(key,canvas,R.drawable.keyboard_del);//按键背景
                    // 绘制按键图标
                    drawKeyIcon(key, canvas, getResources().getDrawable(R.drawable.icon_delete));
                }else if(key.codes[0] == -1){//省和数字切换
                    drawKeyBackground(key,canvas,R.drawable.keyboard_switch);//按键背景
                    drawTextColor(key,canvas,R.color.white);//按键字体颜色
                }
                /* if (key.codes[0] == 73||key.codes[0] == 79) {  // notice 字母 I 和 O 设置特殊背景
                    Drawable dr = getContext().getResources().getDrawable(R.drawable.shape_keyboard_gray_color_r5);
                    dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                    dr.draw(canvas);
                    if (key.label != null) {
                        paint.setTextSize(ScreenUtils.sp2px(getContext(),18));
                        paint.setColor(getContext().getResources().getColor(R.color.color_white_999999));
                        // 注意： 这个方法不支持硬件加速，所以我们要测试时必须先关闭硬件加速。加上这一句
//                        setLayerType(LAYER_TYPE_SOFTWARE, null);
//                        paint.setShadowLayer(0.1f, 0, 0, getContext().getResources().getColor(R.color.color_white_999999));
                        @SuppressLint("drawallocation")
                        Rect rect = new Rect(key.x, key.y, key.x + key.width, key.y + key.height);
                        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
                        int baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;
                        paint.setTextAlign(Paint.Align.CENTER);
                        canvas.drawText(key.label.toString(), rect.centerX(), baseline, paint);
//                        paint.setShadowLayer(0, 0, 0, 0);
                    }
                }*/
            }
        }
    }
    /**
     * 绘制按键的 背景
     */
    private void drawKeyBackground(Keyboard.Key key, Canvas canvas, int resourcesId) {
        Drawable dr = getContext().getResources().getDrawable(resourcesId);
        dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
        dr.draw(canvas);//按键背景
    }
    /**
     * 绘制按键的 背景
     */
    private void drawTextColor(Keyboard.Key key, Canvas canvas, int resourcesId) {
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        paint.setTypeface(font);
        paint.setAntiAlias(true);

        if (key.label != null) {
            paint.setTextSize(DpUtil.sp2px(getContext(),17));
            //设置字体颜色
            paint.setColor(getContext().getResources().getColor(resourcesId));
            // 注意： 这个方法不支持硬件加速，所以我们要测试时必须先关闭硬件加速。加上这一句
//                        setLayerType(LAYER_TYPE_SOFTWARE, null);
//                        paint.setShadowLayer(0.1f, 0, 0, getContext().getResources().getColor(R.color.color_white));
            @SuppressLint("DrawAllocation")
            Rect rect = new Rect(key.x, key.y, key.x + key.width, key.y + key.height);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            int baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;
            // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(key.label.toString(), rect.centerX(), baseline, paint);
        }
    }
    /**
     * 绘制按键的 icon
     */
    private void drawKeyIcon(Keyboard.Key key, Canvas canvas, Drawable iconDrawable) {
        // 得到 keyicon 的显示大小，因为图片放在不同的drawable-dpi目录下，显示大小也不一样
        int intrinsicWidth = iconDrawable.getIntrinsicWidth();
        int intrinsicHeight = iconDrawable.getIntrinsicHeight();
        int drawWidth = intrinsicWidth;
        int drawHeight = intrinsicHeight;
        // 限制图片的大小，防止图片按键范围
        if (drawWidth > key.width) {
            drawWidth = key.width;
            // 此时高就按照比例缩放
            drawHeight = (int) (drawWidth * 1.0f / intrinsicWidth * intrinsicHeight);
        } else if (drawHeight > key.height) {
            drawHeight = key.height;
            drawWidth = (int) (drawHeight * 1.0f / intrinsicHeight * intrinsicWidth);
        }
        // 获取图片的 x,y 坐标,图片在按键的正中间
        int left = key.x + key.width / 2 - drawWidth / 2;
        int top = key.y + key.height / 2 - drawHeight / 2;
        Rect keyIconRect = new Rect(left, top, left + drawWidth, top + drawHeight);
        iconDrawable.setBounds(keyIconRect);
        iconDrawable.draw(canvas);
    }
}