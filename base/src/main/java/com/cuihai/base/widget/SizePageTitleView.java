package com.cuihai.base.widget;

import android.content.Context;

import net.lucode.hackware.magicindicator.buildins.ArgbEvaluatorHolder;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

/**
 * @author: cuihai
 * @description: 类描述
 * @version: V_1.0.0
 * @date: 2019/7/8 13:57
 * @company:
 * @email: nicech6@163.com
 */
public class SizePageTitleView extends SimplePagerTitleView {

    public SizePageTitleView(Context context) {
        super(context);
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        float size = 2 * leavePercent;
        setTextSize(16 - size);
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        float size = 2 * enterPercent;
        setTextSize(14 + size);
    }
}
