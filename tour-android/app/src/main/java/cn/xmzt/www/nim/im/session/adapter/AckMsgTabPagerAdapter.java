package cn.xmzt.www.nim.im.session.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import cn.xmzt.www.nim.im.common.ui.viewpager.SlidingTabPagerAdapter;
import cn.xmzt.www.nim.im.session.fragment.tab.AckMsgTabFragment;
import cn.xmzt.www.nim.im.session.model.AckMsgTab;

import java.util.List;

public class AckMsgTabPagerAdapter extends SlidingTabPagerAdapter {

    @Override
    public int getCacheCount() {
        return AckMsgTab.values().length;
    }

    public AckMsgTabPagerAdapter(FragmentManager fm, Context context, ViewPager pager) {
        super(fm, AckMsgTab.values().length, context.getApplicationContext(), pager);

        for (AckMsgTab tab : AckMsgTab.values()) {
            try {
                AckMsgTabFragment fragment = null;

                List<Fragment> fs = fm.getFragments();
                if (fs != null) {
                    for (Fragment f : fs) {
                        if (f.getClass() == tab.clazz) {
                            fragment = (AckMsgTabFragment) f;
                            break;
                        }
                    }
                }

                if (fragment == null) {
                    fragment = tab.clazz.newInstance();
                }

                fragment.setState(this);
                fragment.attachTabData(tab);

                fragments[tab.tabIndex] = fragment;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getCount() {
        return AckMsgTab.values().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        AckMsgTab tab = AckMsgTab.fromTabIndex(position);

        int resId = tab != null ? tab.resId : 0;

        return resId != 0 ? context.getText(resId) : "";
    }

}