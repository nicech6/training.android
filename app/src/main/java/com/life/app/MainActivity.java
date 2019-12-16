package com.life.app;

import com.life.base.mvp.BaseMvpActivity;
import com.life.base.mvp.BasePresenter;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;

import java.util.ArrayList;

/**
 * @author: cuihai
 * @description: 类描述
 * @date: 2019/12/14
 * @email: nicech6@163.com
 */
public class MainActivity extends BaseMvpActivity {
    private CommonTabLayout mTabLayout;
    private String titles[] = new String[]{"首页", "收藏", "我的"};
    private int unSelectedIds[] = new int[]{R.mipmap.icon_home_unselected, R.mipmap.icon_collect_unselected, R.mipmap.icon_mine_unselected};
    private int selectedIds[] = new int[]{R.mipmap.icon_home_selected, R.mipmap.icon_collect_selected, R.mipmap.icon_mine_selected};
    private ArrayList<CustomTabEntity> mEntityList = new ArrayList<>();

    @Override
    protected void initView() {
        mTabLayout = findViewById(R.id.tab);
    }

    @Override
    protected void bindEvent() {

    }

    @Override
    protected void initData() {
        for (int i = 0; i < titles.length; i++) {
            mEntityList.add(new TabEntity(titles[i], selectedIds[i], unSelectedIds[i]));
        }
        mTabLayout.setTabData(mEntityList);
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
        return false;
    }
}
