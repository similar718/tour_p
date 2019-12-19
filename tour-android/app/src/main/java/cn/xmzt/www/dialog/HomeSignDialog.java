package cn.xmzt.www.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.bean.HomeSignBean;
import cn.xmzt.www.home.bean.HomeIndexBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tanlei
 * @date 2019/9/18
 * @describe 首页签到弹出框
 */

public class HomeSignDialog extends Dialog {
    private ImageView iv_dismiss;
    private TextView tv_sign, tv_days;
    private TextView tv_day1, tv_day2, tv_day3, tv_day4, tv_day5, tv_day6, tv_day7;
    private TextView tv_value1, tv_value2, tv_value3, tv_value4, tv_value5, tv_value6, tv_value7;
    private RelativeLayout ll1, ll2, ll3, ll4, ll5, ll6, ll7;
    private List<HomeSignBean> list = new ArrayList<>();

    public HomeSignDialog(@NonNull Context context, View.OnClickListener listener) {
        this(context, 0, listener);
    }

    public HomeSignDialog(@NonNull Context context, int themeResId, View.OnClickListener listener) {
        super(context, R.style.dialog_custom);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_home_sign, null);
        setContentView(view);
        iv_dismiss = view.findViewById(R.id.iv_dismiss);
        tv_day1 = view.findViewById(R.id.tv_day1);
        tv_day2 = view.findViewById(R.id.tv_day2);
        tv_day3 = view.findViewById(R.id.tv_day3);
        tv_day4 = view.findViewById(R.id.tv_day4);
        tv_day5 = view.findViewById(R.id.tv_day5);
        tv_day6 = view.findViewById(R.id.tv_day6);
        tv_day7 = view.findViewById(R.id.tv_day7);
        tv_value1 = view.findViewById(R.id.tv_value1);
        tv_value2 = view.findViewById(R.id.tv_value2);
        tv_value3 = view.findViewById(R.id.tv_value3);
        tv_value4 = view.findViewById(R.id.tv_value4);
        tv_value5 = view.findViewById(R.id.tv_value5);
        tv_value6 = view.findViewById(R.id.tv_value6);
        tv_value7 = view.findViewById(R.id.tv_value7);
        ll1 = view.findViewById(R.id.ll1);
        ll2 = view.findViewById(R.id.ll2);
        ll3 = view.findViewById(R.id.ll3);
        ll4 = view.findViewById(R.id.ll4);
        ll5 = view.findViewById(R.id.ll5);
        ll6 = view.findViewById(R.id.ll6);
        ll7 = view.findViewById(R.id.ll7);
        tv_days = view.findViewById(R.id.tv_days);
        iv_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        tv_sign = view.findViewById(R.id.tv_sign);
        tv_sign.setOnClickListener(listener);
    }

    public void setData(HomeIndexBean bean) {
        if (!bean.isSign()) {
            tv_sign.setBackgroundResource(R.drawable.shape_no_sign_home);
            tv_sign.setEnabled(false);
        } else {
            tv_sign.setEnabled(true);
            tv_sign.setBackgroundResource(R.drawable.shape_yes_sign_home);
        }
        tv_days.setText("你已连续签到" + bean.getContinuouNum() + "天");
        if (0 <= bean.getContinuouNum() && bean.getContinuouNum() < 7) {
            for (int i = 0; i < 7; i++) {
                String a = bean.getSignIntegralReward().get(i + 1 + "") + "";
                HomeSignBean homeSignBean = new HomeSignBean(false, i + 1 + "", a);
                if (bean.getContinuouNum() > i) {
                    homeSignBean.setSign(true);
                }
                list.add(homeSignBean);
            }
        } else if (7 <= bean.getContinuouNum() && bean.getContinuouNum() < 14) {
            for (int i = 7; i < 14; i++) {
                HomeSignBean homeSignBean = new HomeSignBean(false, i + 1 + "", bean.getSignIntegralReward().get(i + 1 + "") + "");
                if (bean.getContinuouNum() > i) {
                    homeSignBean.setSign(true);
                }
                list.add(homeSignBean);
            }
        } else if (14 <= bean.getContinuouNum() && bean.getContinuouNum() < 21) {
            for (int i = 14; i < 21; i++) {
                HomeSignBean homeSignBean = new HomeSignBean(false, i + 1 + "", bean.getSignIntegralReward().get(i + 1 + "") + "");
                if (bean.getContinuouNum() > i) {
                    homeSignBean.setSign(true);
                }
                list.add(homeSignBean);
            }
        } else if (21 <= bean.getContinuouNum() && bean.getContinuouNum() < 28) {
            for (int i = 21; i < 28; i++) {
                HomeSignBean homeSignBean = new HomeSignBean(false, i + 1 + "", bean.getSignIntegralReward().get(i + 1 + "") + "");
                if (bean.getContinuouNum() > i) {
                    homeSignBean.setSign(true);
                }
                list.add(homeSignBean);
            }
        } else {
            for (int i = 23; i < 30; i++) {
                HomeSignBean homeSignBean = new HomeSignBean(false, i + 1 + "", bean.getSignIntegralReward().get(i + 1 + "") + "");
                if (bean.getContinuouNum() > i) {
                    homeSignBean.setSign(true);
                }
                list.add(homeSignBean);
            }
        }
        Log.e("lee", list.toString());
        for (int i = 0; i < list.size(); i++) {
            setValue(i);
        }
    }

    private void setValue(int i) {
        switch (i) {
            case 0:
                tv_day1.setText("第" + list.get(i).getName() + "天");
                tv_value1.setText("+" + list.get(i).getValue());
                if (list.get(i).isSign()) {
                    ll1.setBackgroundResource(R.drawable.yes_sign_bg);
                    tv_value1.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    ll1.setBackgroundResource(R.drawable.no_sign_bg);
                    tv_value1.setTextColor(Color.parseColor("#24AD77"));
                }
                break;
            case 1:
                tv_day2.setText("第" + list.get(i).getName() + "天");
                tv_value2.setText("+" + list.get(i).getValue());
                if (list.get(i).isSign()) {
                    ll2.setBackgroundResource(R.drawable.yes_sign_bg);
                    tv_value2.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    ll2.setBackgroundResource(R.drawable.no_sign_bg);
                    tv_value2.setTextColor(Color.parseColor("#24AD77"));
                }
                break;
            case 2:
                tv_day3.setText("第" + list.get(i).getName() + "天");
                tv_value3.setText("+" + list.get(i).getValue());
                if (list.get(i).isSign()) {
                    ll3.setBackgroundResource(R.drawable.yes_sign_bg);
                    tv_value3.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    ll3.setBackgroundResource(R.drawable.no_sign_bg);
                    tv_value3.setTextColor(Color.parseColor("#24AD77"));
                }
                break;
            case 3:
                tv_day4.setText("第" + list.get(i).getName() + "天");
                tv_value4.setText("+" + list.get(i).getValue());
                if (list.get(i).isSign()) {
                    ll4.setBackgroundResource(R.drawable.yes_sign_bg);
                    tv_value4.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    ll4.setBackgroundResource(R.drawable.no_sign_bg);
                    tv_value4.setTextColor(Color.parseColor("#24AD77"));
                }
                break;
            case 4:
                tv_day5.setText("第" + list.get(i).getName() + "天");
                tv_value5.setText("+" + list.get(i).getValue());
                if (list.get(i).isSign()) {
                    ll5.setBackgroundResource(R.drawable.yes_sign_bg);
                    tv_value5.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    ll5.setBackgroundResource(R.drawable.no_sign_bg);
                    tv_value5.setTextColor(Color.parseColor("#24AD77"));
                }
                break;
            case 5:
                tv_day6.setText("第" + list.get(i).getName() + "天");
                tv_value6.setText("+" + list.get(i).getValue());
                if (list.get(i).isSign()) {
                    ll6.setBackgroundResource(R.drawable.yes_sign_bg);
                    tv_value6.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    ll6.setBackgroundResource(R.drawable.no_sign_bg);
                    tv_value6.setTextColor(Color.parseColor("#24AD77"));
                }
                break;
            case 6:
                tv_day7.setText("第" + list.get(i).getName() + "天");
                tv_value7.setText("+" + list.get(i).getValue());
                if (list.get(i).isSign()) {
                    ll7.setBackgroundResource(R.drawable.yes_sign_bg);
                    tv_value7.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    ll7.setBackgroundResource(R.drawable.no_sign_bg);
                    tv_value7.setTextColor(Color.parseColor("#24AD77"));
                }
                break;
        }
    }
}
