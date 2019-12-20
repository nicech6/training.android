package com.bili.home.live;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bili.base.constant.Path;
import com.bili.base.entity.home.HomeLiveEntity;
import com.bili.base.entity.home.LiveMultiItemEntity;
import com.bili.base.mvvm.BaseMVVMFragment;
import com.bili.home.R;
import com.bili.home.databinding.FragmentLiveBinding;
import com.bili.http.CommonBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: cuihai
 * @description: 类描述
 * @date: 2019/12/19
 * @email: nicech6@163.com
 */
@Route(path = Path.HOME_LIVE)
public class LiveFragment extends BaseMVVMFragment<LiveViewModel, FragmentLiveBinding> {
    private LiveAdapter mLiveAdapter;
    private List<LiveMultiItemEntity> mEntities = new ArrayList<>();

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_live;
    }

    @Override
    protected void initData() {
        mLiveAdapter = new LiveAdapter(mEntities);
        mBindingView.rv.setAdapter(mLiveAdapter);
        mBindingView.rv.setLayoutManager(new LinearLayoutManager(mContext));
        mViewModel.getData().observe(this, new Observer<CommonBean<HomeLiveEntity>>() {
            @Override
            public void onChanged(CommonBean<HomeLiveEntity> entityCommonBean) {
                LiveMultiItemEntity entityBannner = new LiveMultiItemEntity(LiveMultiItemEntity.BANNER);
                entityBannner.mBannerBeans = entityCommonBean.data.getBanner();
                mEntities.add(entityBannner);

                for (int i = 0; i < entityCommonBean.data.getPartitions().size(); i++) {
                    LiveMultiItemEntity entityList = new LiveMultiItemEntity(LiveMultiItemEntity.LIST);
                    entityList.mPartitionsBean = entityCommonBean.data.getPartitions().get(i);
                    mEntities.add(entityList);
                }
                mLiveAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void bindEvent() {

    }
}
