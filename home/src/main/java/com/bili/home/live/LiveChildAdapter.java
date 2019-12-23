package com.bili.home.live;

import androidx.databinding.DataBindingUtil;

import com.bili.base.entity.home.HomeLiveEntity;
import com.bili.base.widget.GlideUtil;
import com.bili.home.BR;
import com.bili.home.R;
import com.bili.home.databinding.ItemLiveBinding;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LiveChildAdapter extends BaseQuickAdapter<HomeLiveEntity.PartitionsBean.LivesBeanXX, BaseViewHolder> {
    public LiveChildAdapter(@Nullable List<HomeLiveEntity.PartitionsBean.LivesBeanXX> data) {
        super(R.layout.item_live, data);
    }

    @Override
    protected void onItemViewHolderCreated(@NotNull BaseViewHolder viewHolder, int viewType) {
        DataBindingUtil.bind(viewHolder.itemView);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, @Nullable HomeLiveEntity.PartitionsBean.LivesBeanXX beanXX) {
        ItemLiveBinding binding = baseViewHolder.getBinding();
        binding.setVariable(com.bili.home.BR.live,beanXX);
        binding.executePendingBindings();
    }
}
