package com.bili.home;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bili.base.constant.Path;
import com.bili.base.mvvm.BaseMVVMFragment;
import com.bili.base.widget.BaseViewPageAdapter;
import com.bili.base.widget.SizePageTitleView;
import com.bili.home.databinding.FragmentHomeBinding;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: cuihai
 * @description: 类描述
 * @date: 2019/12/16
 * @email: nicech6@163.com
 */
@Route(path = Path.APP_HOME)
public class HomeFragment extends BaseMVVMFragment<HomeViewModel, FragmentHomeBinding> {
    private List<Fragment> mFragments = new ArrayList<>();
    private String[] titles = new String[]{"推荐", "热门", "追番", "影视","直播"};

    @Override
    protected void bindEvent() {

    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData() {
        Fragment fragment = (Fragment) ARouter.getInstance().build(Path.HOME_LIVE).navigation();
        Fragment fragment1 = (Fragment) ARouter.getInstance().build(Path.HOME_TEMP).navigation();
        Fragment fragment2 = (Fragment) ARouter.getInstance().build(Path.HOME_TEMP).navigation();
        Fragment fragment3 = (Fragment) ARouter.getInstance().build(Path.HOME_TEMP).navigation();
        Fragment fragment4 = (Fragment) ARouter.getInstance().build(Path.HOME_TEMP).navigation();
        mFragments.add(fragment);
        mFragments.add(fragment1);
        mFragments.add(fragment2);
        mFragments.add(fragment3);
        mFragments.add(fragment4);

        final CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return titles == null ? 0 : titles.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);
                SizePageTitleView sizePageTitleView = new SizePageTitleView(context);
                sizePageTitleView.setNormalColor(getResources().getColor(R.color.config_color_text_66));
                sizePageTitleView.setSelectedColor(getResources().getColor(R.color.config_color_white));
                sizePageTitleView.setText(titles[index]);
                sizePageTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mBindingView.viewpager.setCurrentItem(index);
                    }
                });
                badgePagerTitleView.setInnerPagerTitleView(sizePageTitleView);
                badgePagerTitleView.setAutoCancelBadge(false);
                return badgePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                return null;
            }
        });
        mBindingView.tab.setNavigator(commonNavigator);
        BaseViewPageAdapter adapter = new BaseViewPageAdapter(getChildFragmentManager(), mFragments, titles);
        mBindingView.viewpager.setAdapter(adapter);
        mBindingView.viewpager.setOffscreenPageLimit(mFragments.size());
        mBindingView.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mBindingView.tab.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                mBindingView.tab.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mBindingView.tab.onPageScrollStateChanged(state);
            }
        });
    }
}
