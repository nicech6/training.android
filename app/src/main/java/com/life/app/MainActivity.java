package com.life.app;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.life.base.constant.Path;
import com.life.base.mvp.BaseMvpActivity;
import com.life.base.mvp.BasePresenter;
import com.life.base.widget.BaseViewPageAdapter;
import com.life.base.widget.TabEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: cuihai
 * @description: 类描述
 * @date: 2019/12/14
 * @email: nicech6@163.com
 */
@Route(path = Path.APP_MAIN)
public class MainActivity extends BaseMvpActivity {
    private CommonTabLayout mTabLayout;
    private ViewPager mViewPager;
    private String titles[] = new String[]{"首页", "频道","动态","我的"};
    private int unSelectedIds[] = new int[]{R.mipmap.gift_un, R.mipmap.gloves_un, R.mipmap.tree_un, R.mipmap.santa_un};
    private int selectedIds[] = new int[]{R.mipmap.gift, R.mipmap.gloves, R.mipmap.tree, R.mipmap.santa};
    private ArrayList<CustomTabEntity> mEntityList = new ArrayList<>();
    private List<Fragment> mFragmentList;

    @Override
    protected void initView() {
        mTabLayout = findViewById(R.id.tab);
        mViewPager = findViewById(R.id.viewpager);
    }

    @Override
    protected void bindEvent() {
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void initData() {
        mFragmentList = new ArrayList<>();
        Fragment fragment = (Fragment) ARouter.getInstance().build(Path.HOME).navigation();
        Fragment fragment1 = (Fragment) ARouter.getInstance().build(Path.MINE).navigation();
        Fragment fragment2 = (Fragment) ARouter.getInstance().build(Path.MINE).navigation();
        Fragment fragment3 = (Fragment) ARouter.getInstance().build(Path.MINE).navigation();
        mFragmentList.add(fragment);
        mFragmentList.add(fragment1);
        mFragmentList.add(fragment2);
        mFragmentList.add(fragment3);
        for (int i = 0; i < titles.length; i++) {
            mEntityList.add(new TabEntity(titles[i], selectedIds[i], unSelectedIds[i]));
        }
        mTabLayout.setTabData(mEntityList);
        mViewPager.setAdapter(new BaseViewPageAdapter(getSupportFragmentManager(), mFragmentList));
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected boolean immersion() {
        return true;
    }
}
