package com.bili.app;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bili.base.constant.Path;
import com.bili.base.mvp.BaseMvpActivity;
import com.bili.base.mvp.BasePresenter;
import com.bili.base.widget.BaseViewPageAdapter;
import com.bili.base.widget.TabEntity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: cuihai
 * @description: 类描述
 * @date: 2019/12/14
 * @email: nicech6@163.com
 */
@Route(path = Path.App.MAIN)
public class MainActivity extends BaseMvpActivity {
    private CommonTabLayout mTabLayout;
    private ViewPager mViewPager;
    private String titles[] = new String[]{"首页", "频道", "动态", "我的"};
    private int selectedIds[] = new int[]{R.mipmap.ic_home_selected, R.mipmap.ic_category_selected,
            R.mipmap.ic_dynamic_selected, R.mipmap.ic_communicate_selected};
    private int unSelectedIds[] = new int[]{R.mipmap.ic_home_unselected, R.mipmap.ic_category_unselected,
            R.mipmap.ic_dynamic_unselected, R.mipmap.ic_communicate_unselected};
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
    }

    @Override
    protected void initData() {
        mFragmentList = new ArrayList<>();
        Fragment fragment = (Fragment) ARouter.getInstance().build(Path.Home.MAIN).navigation();
        Fragment fragment1 = (Fragment) ARouter.getInstance().build(Path.Mine.MAIN).navigation();
        Fragment fragment2 = (Fragment) ARouter.getInstance().build(Path.Mine.MAIN).navigation();
        Fragment fragment3 = (Fragment) ARouter.getInstance().build(Path.Mine.MAIN).navigation();
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
