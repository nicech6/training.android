package com.bili.base.entity.home;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * @author: cuihai
 * @description: 类描述
 * @date: 2019/12/20
 * @email: nicech6@163.com
 */
public class LiveMultiItemEntity implements MultiItemEntity {
    private int itemType;
    public static final int BANNER = 1;
    public static final int LIST = 2;
    public List<HomeLiveEntity.BannerBean> mBannerBeans;
    public HomeLiveEntity.PartitionsBean mPartitionsBean;

    public LiveMultiItemEntity(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
